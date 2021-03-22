package dev.fringe.app;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.ta4j.core.BacktestExecutor;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Indicator;
import org.ta4j.core.Order;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.AverageProfitCriterion;
import org.ta4j.core.analysis.criteria.AverageProfitableTradesCriterion;
import org.ta4j.core.analysis.criteria.BuyAndHoldCriterion;
import org.ta4j.core.analysis.criteria.LinearTransactionCostCriterion;
import org.ta4j.core.analysis.criteria.MaximumDrawdownCriterion;
import org.ta4j.core.analysis.criteria.NumberOfBarsCriterion;
import org.ta4j.core.analysis.criteria.NumberOfTradesCriterion;
import org.ta4j.core.analysis.criteria.RewardRiskRatioCriterion;
import org.ta4j.core.analysis.criteria.TotalProfitCriterion;
import org.ta4j.core.analysis.criteria.VersusBuyAndHoldCriterion;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.PrecisionNum;
import org.ta4j.core.tradereport.TradingStatement;
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import org.ta4j.core.trading.rules.StopGainRule;
import org.ta4j.core.trading.rules.StopLossRule;
import org.ta4j.core.trading.rules.UnderIndicatorRule;

import dev.fringe.app.config.HibernateConfig;
import dev.fringe.app.model.Bars;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Import(HibernateConfig.class)
public class Select2 implements InitializingBean {

	@Autowired
	private SessionFactory sessionFactory;

	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(Select2.class);
	}

	@Transactional
	public void afterPropertiesSet() throws Exception {
		BarSeries series  = getBarSeries("apple_bars");
        final List<Strategy> strategies = new ArrayList<>();
        int start = 0;
        int stop = series.getBarCount();
        int step = 1;
        for (int i = start; i <= stop; i += step) {
            Strategy strategy = new BaseStrategy("Sma(" + i + ")", createEntryRule(series, i),createExitRule(series, i));
            strategies.add(strategy);
//            BarSeriesManager seriesManager = new BarSeriesManager(series);
//            TradingRecord tradingRecord = seriesManager.run(strategy);
        }
        BacktestExecutor backtestExecutor = new BacktestExecutor(series);
        List<TradingStatement> list = backtestExecutor.execute(strategies, PrecisionNum.valueOf(50), Order.OrderType.BUY);
        for (TradingStatement tradingStatement : list) {
			System.out.println(tradingStatement);
		}
        System.out.println(backtestExecutor);
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        SMAIndicator shortSma = new SMAIndicator(closePrice, 1);
        SMAIndicator longSma = new SMAIndicator(closePrice, 30);
        Rule entryRule = new CrossedUpIndicatorRule(shortSma, longSma).or(new CrossedDownIndicatorRule(closePrice, BigDecimal.valueOf(10000)));
        Rule exitRule = new CrossedDownIndicatorRule(shortSma, longSma).or(new StopLossRule(closePrice, BigDecimal.valueOf(3))).or(new StopGainRule(closePrice, BigDecimal.valueOf(2)));
        Strategy myStrategy = new BaseStrategy(entryRule, exitRule);
        BarSeriesManager seriesManager = new BarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(myStrategy);
        
        // Total profit
        TotalProfitCriterion totalProfit = new TotalProfitCriterion();
        System.out.println("Total profit: " + totalProfit.calculate(series, tradingRecord));
        // Number of bars
        System.out.println("Number of bars: " + new NumberOfBarsCriterion().calculate(series, tradingRecord));
        // Average profit (per bar)
        System.out
                .println("Average profit (per bar): " + new AverageProfitCriterion().calculate(series, tradingRecord));
        // Number of trades
        System.out.println("Number of trades: " + new NumberOfTradesCriterion().calculate(series, tradingRecord));
        // Profitable trades ratio
        System.out.println(
                "Profitable trades ratio: " + new AverageProfitableTradesCriterion().calculate(series, tradingRecord));
        // Maximum drawdown
        System.out.println("Maximum drawdown: " + new MaximumDrawdownCriterion().calculate(series, tradingRecord));
        // Reward-risk ratio
        System.out.println("Reward-risk ratio: " + new RewardRiskRatioCriterion().calculate(series, tradingRecord));
        // Total transaction cost
        System.out.println("Total transaction cost (from $1000): "
                + new LinearTransactionCostCriterion(1000, 0.005).calculate(series, tradingRecord));
        // Buy-and-hold
        System.out.println("Buy-and-hold: " + new BuyAndHoldCriterion().calculate(series, tradingRecord));
        // Total profit vs buy-and-hold
        System.out.println("Custom strategy profit vs buy-and-hold strategy profit: "
                + new VersusBuyAndHoldCriterion(totalProfit).calculate(series, tradingRecord));
	}

	private BarSeries getBars(String barName) {
		Transaction transaction = null;
		List<Bars> bars = null;
		try {
			Session session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			bars = session.createQuery("select u from Bars u", Bars.class).list();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		BarSeries series = new BaseBarSeries(barName);
		for (Bars bar : bars) {
			ZonedDateTime date = LocalDate.parse(bar.getId().getDate()).atStartOfDay(ZoneId.systemDefault());
			series.addBar(date, bar.getOpen(), bar.getHigh(), bar.getLow(), bar.getClose(), bar.getVolume());
		}
		return series;
	}

	
    private Rule createEntryRule(BarSeries series, int barCount) {
        Indicator<Num> closePrice = new ClosePriceIndicator(series);
        SMAIndicator sma = new SMAIndicator(closePrice, barCount);
        return new UnderIndicatorRule(sma, closePrice);
    }

    private Rule createExitRule(BarSeries series, int barCount) {
        Indicator<Num> closePrice = new ClosePriceIndicator(series);
        SMAIndicator sma = new SMAIndicator(closePrice, barCount);
        return new OverIndicatorRule(sma, closePrice);
    }
    
	private BarSeries getBarSeries(String barName) {
		BarSeries series = getBars(barName);
		System.out.println("Series: " + series.getName() + " (" + series.getSeriesPeriodDescription() + ")");
	    System.out.println("Number of bars: " + series.getBarCount());
	    for(int i=0; i < series.getBarCount() ; i++) {
	    	System.out.println("Volume: " + series.getBar(i).getVolume() + "\t" + "Open price: "+ series.getBar(i).getOpenPrice() + "\t" + "Close price: " + series.getBar(i).getClosePrice() + "\t" +  series.getBar(i).getDateName() );
	    }
		return series;
	}

}
