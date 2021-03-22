package dev.fringe.app;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.ta4j.core.BacktestExecutor;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Order;
import org.ta4j.core.Order.OrderType;
import org.ta4j.core.Strategy;
import org.ta4j.core.num.PrecisionNum;
import org.ta4j.core.tradereport.TradingStatement;

import dev.fringe.app.config.HibernateConfig;
import dev.fringe.app.model.ProfitData;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Import(HibernateConfig.class)
public class SelectZBarSeriesTest extends ZSupport implements InitializingBean {

	private BarSeries series;
	private List<TradingStatement> list;
	@Autowired private SessionFactory sessionFactory;

	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(SelectZBarSeriesTest.class);
	}

	@Transactional
	public void afterPropertiesSet() throws Exception {
		OrderType orderType = Order.OrderType.SELL;
		Session session = sessionFactory.openSession();
		try {
			for (int j = 100; j <= 1000; j += 300) {
				session.getTransaction().begin();
				PrecisionNum precisionNum = PrecisionNum.valueOf(j);
				this.get("apple", PrecisionNum.valueOf(j), orderType);
				for (TradingStatement tradingStatement : list) {
					if(list.size() -1  <= list.indexOf(tradingStatement)){
						break;
					}
					session.save(new ProfitData(series.getBar(list.indexOf(tradingStatement)), tradingStatement, precisionNum, orderType));
				}
				session.getTransaction().commit();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			session.getTransaction().rollback();
		}
	}

	public void get(String name, PrecisionNum precisionNum, OrderType orderType) {
		BarSeries series = this.getTestBarSeries(name);
		this.series = series;
		final List<Strategy> strategies = this.getTestStrategies(name + "_Sma", series);
		BacktestExecutor backtestExecutor = new BacktestExecutor(series);
		List<TradingStatement> list = backtestExecutor.execute(strategies, precisionNum, orderType);
		this.list = list;
	}
}
