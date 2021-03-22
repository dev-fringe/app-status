package dev.fringe.app;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.h2.tools.Csv;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import dev.fringe.app.config.HibernateConfig;
import dev.fringe.app.config.WebClientConfig;
import dev.fringe.app.model.Bars;
import dev.fringe.app.model.Candles;
import dev.fringe.app.model.Market;
import dev.fringe.app.service.CandleService;
import dev.fringe.app.service.MarketService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Import({HibernateConfig.class, WebClientConfig.class})
@ComponentScan("dev.fringe.app.service")
public class Load implements InitializingBean {

	 @Autowired private SessionFactory sessionFactory;
	 @Autowired MarketService marketService;
	 @Autowired CandleService candleService;
	 
	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(Load.class);
	}

	@Transactional
	public void afterPropertiesSet() throws Exception {
		
		ResultSet rs = new Csv().read("appleinc_bars_from_20130101_usd.csv", null, null); // rest·Î ÀüÈ¯
		List<Bars> bars = new ArrayList<Bars>();
		while (rs.next()) {
			bars.add(new Bars("apple",rs));
		}
		rs.close();
        Transaction transaction = null;
        try {
        	  Session session = sessionFactory.openSession();
        	  transaction = session.beginTransaction();
            for (Bars bars2 : bars) {
            	System.out.println(bars2);
            	session.save(bars2);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } 
        List<Market> markets = marketService.getmarket();
        try {
        	  Session session = sessionFactory.openSession();
        	  transaction = session.beginTransaction();
            for (Market market : markets) {
            	if(market.getMarket().contains("KRW")) {
	            	market.setMoney(market.getMarket().split("-")[0]);
	            	market.setMarketCode(market.getMarket().split("-")[1]);
	            	System.out.println(market);
	            	session.save(market);
            	}
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        try {
      	  Session session = sessionFactory.openSession();
      	  transaction = session.beginTransaction();
	        for (Market market : markets) {
	        	if(market.getMarket().contains("KRW")) {
					List<Candles> list = candleService.getCandles(market);
					for (Candles candles : list) {
//						market.setMoney(market.getMarket().split("-")[0]);
//						market.setMarketCode(market.getMarket().split("-")[1]);
//						System.out.println(market);
//						session.save(market);
					}
	        	}
			}
        transaction.commit();
        } catch (Exception e) {
        	if (transaction != null) {
        		transaction.rollback();
        	}
        }
	}
	
}
