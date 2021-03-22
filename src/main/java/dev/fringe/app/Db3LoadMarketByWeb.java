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
import dev.fringe.app.model.Market;
import dev.fringe.app.service.MarketService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Import({HibernateConfig.class, WebClientConfig.class})
@ComponentScan("dev.fringe.app.service")
public class Db3LoadMarketByWeb implements InitializingBean {

	 @Autowired private SessionFactory sessionFactory;
	 @Autowired MarketService marketService;
	 
	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(Db3LoadMarketByWeb.class);
	}

	@Transactional
	public void afterPropertiesSet() throws Exception {
        List<Market> markets = marketService.getAllmarketByWeb();
		Session session = sessionFactory.openSession();
    		try {
    			session.getTransaction().begin();
	            for (Market market : markets) {
	            	if(market.getMarket().contains("KRW")) {
	            		Market t = (Market) session.get(Market.class, market.getMarket());
	            		if(t == null) {
			            	market.setMoney(market.getMarket().split("-")[0]);
			            	market.setMarketCode(market.getMarket().split("-")[1]);
			            	session.save(market);
	            		}
	            	}
	            }
			session.getTransaction().commit();
			session.flush();
			session.clear();
        } catch (Exception e) {
			log.error(e.getMessage());
			session.getTransaction().rollback();
        }
	}
}