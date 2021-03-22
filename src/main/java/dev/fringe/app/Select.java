package dev.fringe.app;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.ta4j.core.BacktestExecutor;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Order;
import org.ta4j.core.Strategy;
import org.ta4j.core.num.PrecisionNum;
import org.ta4j.core.tradereport.TradingStatement;

import dev.fringe.app.config.HibernateConfig;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Import(HibernateConfig.class)
public class Select extends Support implements InitializingBean {

	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(Select.class);
	}

	@Transactional
	public void afterPropertiesSet() throws Exception {
		String name = "apple";
		BarSeries series  = getBarSeries(name);
        final List<Strategy> strategies = getStrategies(name + "_Sma", series);
        BacktestExecutor backtestExecutor = new BacktestExecutor(series);
        List<TradingStatement> list = backtestExecutor.execute(strategies, PrecisionNum.valueOf(50), Order.OrderType.BUY);
        for (TradingStatement tradingStatement : list) {
			System.out.println(tradingStatement);
		}
	}
}
