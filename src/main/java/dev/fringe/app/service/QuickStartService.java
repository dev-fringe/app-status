package dev.fringe.app.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.AverageProfitableTradesCriterion;
import org.ta4j.core.analysis.criteria.RewardRiskRatioCriterion;
import org.ta4j.core.analysis.criteria.TotalProfitCriterion;
import org.ta4j.core.analysis.criteria.VersusBuyAndHoldCriterion;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;
import org.ta4j.core.trading.rules.StopGainRule;
import org.ta4j.core.trading.rules.StopLossRule;

import dev.fringe.app.Support;
import dev.fringe.app.mapper.DatabaseMapper;
import dev.fringe.app.model.Quickstart;
import dev.fringe.app.model.Ticks;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class QuickStartService extends Support {

	@Autowired TickService service;
	@Autowired DatabaseMapper mapper;
	@Autowired SessionFactory sessionFactory;
	@Autowired MarketService marketService;
	
	public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }
	
	public void run() {
		mapper.delete();
		List<String> markets = marketService.getAllmarket();
//		List<String> markets = Arrays.asList("KRW-BTC");
		for (String market : markets) {
			List<Ticks> l = service.getTicksByMarketAndCount(market, "100000");
			List<String[]> a = new ArrayList<>();
			for (Ticks o : l) {
				a.add(new String[] {replaceLast((String) o.getTimestamp(),"000",""), o.getTradePrice(), o.getTradeVolume()});
			}
	        BarSeries series = this.loadBitstampSeries(a, market);
	        Num firstClosePrice = series.getBar(0).getClosePrice();
	        System.out.println("First close price: " + new BigDecimal(firstClosePrice.doubleValue()));
	        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
	        SMAIndicator shortSma = new SMAIndicator(closePrice, 5);
//	        System.out.println("5-bars-SMA value at the 42nd index: " + new BigDecimal(shortSma.getValue(5).doubleValue()));
	        SMAIndicator longSma = new SMAIndicator(closePrice, 30);
	        Rule buyingRule = new CrossedUpIndicatorRule(shortSma, longSma).or(new CrossedDownIndicatorRule(closePrice, 50000));
	        Rule sellingRule = new CrossedDownIndicatorRule(shortSma, longSma).or(new StopLossRule(closePrice, series.numOf(3))).or(new StopGainRule(closePrice, series.numOf(2)));
	        BarSeriesManager seriesManager = new BarSeriesManager(series);
	        TradingRecord tradingRecord = seriesManager.run(new BaseStrategy(buyingRule, sellingRule));
	        System.out.println("Number of trades for our strategy: " + tradingRecord.getTradeCount());//우리의 전략에 대한 거래 수 
	        AnalysisCriterion profitTradesRatio = new AverageProfitableTradesCriterion();//평균 수익성 거래 기준 수익성있는 거래 수 
	        System.out.println("Profitable trades ratio: " + profitTradesRatio.calculate(series, tradingRecord));
	        AnalysisCriterion rewardRiskRatio = new RewardRiskRatioCriterion();//보상 위험 비율 기준 (즉, 최대 감소에 대한 총 이익. 
	        System.out.println("Reward-risk ratio: " + rewardRiskRatio.calculate(series, tradingRecord));
	        AnalysisCriterion vsBuyAndHold = new VersusBuyAndHoldCriterion(new TotalProfitCriterion());
	        System.out.println("Our profit vs buy-and-hold profit: " + vsBuyAndHold.calculate(series, tradingRecord));//우리의 이익 대 매수 및 보유 이익
	        System.out.println(new BigDecimal(firstClosePrice.doubleValue()).toString());
	        // Your turn!
			Session session = sessionFactory.openSession();
			try {
				session.getTransaction().begin();
				session.save(new Quickstart(market,
						"5-bars-SMA value at the 42nd index: " +new BigDecimal(firstClosePrice.doubleValue()).toString(),
								 String.valueOf(tradingRecord.getTradeCount()),
								profitTradesRatio.calculate(series, tradingRecord).toString(),
								rewardRiskRatio.calculate(series, tradingRecord).toString(),
								vsBuyAndHold.calculate(series, tradingRecord).toString(),
								String.valueOf(series.getBarCount())
								));
				session.getTransaction().commit();
			} catch (Exception e) {
				log.error(e.getMessage());
				session.getTransaction().rollback();
			}        
		}
	}
	
}
