package dev.fringe.app;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import dev.fringe.app.config.HibernateConfig;
import dev.fringe.app.config.WebClientConfig;
import dev.fringe.app.model.Candles;
import dev.fringe.app.model.CandlesId;
import dev.fringe.app.model.Market;
import dev.fringe.app.service.CandleService;
import dev.fringe.app.service.MarketService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Import({ HibernateConfig.class, WebClientConfig.class })
@ComponentScan("dev.fringe.app.service")
public class Db5LoadCandleMinutesByWeb implements InitializingBean {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired MarketService marketService;
	@Autowired CandleService candleService;

	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(Db5LoadCandleMinutesByWeb.class);
	}

	@Transactional
	public void afterPropertiesSet() throws Exception {
		Session session = sessionFactory.openSession();
		Market market = marketService.getmarket("KRW-BTC");
		try {
			session.getTransaction().begin();
			if (market.getMarket().contains("KRW")) {
//				if (market.getMarket().contains("BTC")) {
					List<Candles> list2 = candleService.getWebCandles(market, CandleService.MINUTES);
					for (Candles candles : list2) {
						candles.setId(new CandlesId(candles));
						session.save(candles);
					}
//				}
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			log.error(e.getMessage());
			session.getTransaction().rollback();
		}
	}
}
