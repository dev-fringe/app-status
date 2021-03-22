package dev.fringe.app;

import javax.transaction.Transactional;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;
import org.ta4j.core.trading.rules.StopGainRule;
import org.ta4j.core.trading.rules.StopLossRule;

import dev.fringe.app.config.HibernateConfig;
import dev.fringe.app.config.WebClientConfig;
import dev.fringe.app.service.BarService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Import({HibernateConfig.class, WebClientConfig.class})
@PropertySource("classpath:app.properties")
@ComponentScan("dev.fringe.app.service")
public class SelectBarSeries extends Support implements InitializingBean {

	@Autowired private BarService barService;
	
	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(SelectBarSeries.class);
	}

	@Transactional
	public void afterPropertiesSet() throws Exception {
		BarSeries series = barService.getBarSeries("KRW-BTC");
		Num firstClosePrice = series.getBar(0).getClosePrice();
		System.out.println("First close price: " + firstClosePrice.toString());
		// Or within an indicator:
		ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
		// Here is the same close price:
		System.out.println(firstClosePrice.isEqual(closePrice.getValue(0))); // equal to firstClosePrice

		// Getting the simple moving average (SMA) of the close price over the last 5 ticks
		SMAIndicator shortSma = new SMAIndicator(closePrice, 5);
		// Here is the 5-ticks-SMA value at the 42nd index
		System.out.println("5-ticks-SMA value at the 44nd index: " + shortSma.getValue(99).toString());

		// Getting a longer SMA (e.g. over the 30 last ticks)
		SMAIndicator longSma = new SMAIndicator(closePrice, 100);
		// Buying rules
		// We want to buy:
		//  - if the 5-ticks SMA crosses over 30-ticks SMA
		//  - or if the price goes below a defined price (e.g $800.00)
		Rule buyingRule = new CrossedUpIndicatorRule(shortSma, longSma)
		        .or(new CrossedDownIndicatorRule(closePrice, 1800));

		// Selling rules
		// We want to sell:
		//  - if the 5-ticks SMA crosses under 30-ticks SMA
		//  - or if the price loses more than 3%
		//  - or if the price earns more than 2%
		Rule sellingRule = new CrossedDownIndicatorRule(shortSma, longSma)
		        .or(new StopLossRule(closePrice, 3.0))
		        .or(new StopGainRule(closePrice, 2));

		Strategy strategy = new BaseStrategy(buyingRule, sellingRule);
		BarSeriesManager manager = new BarSeriesManager(series);
		TradingRecord tradingRecord = manager.run(strategy);
		System.out.println("Number of trades for our strategy: " + tradingRecord.getTradeCount());
	}
	
}
