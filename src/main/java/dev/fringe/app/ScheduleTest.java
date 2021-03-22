package dev.fringe.app;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import dev.fringe.app.config.HibernateConfig;
import dev.fringe.app.config.WebClientConfig;
import dev.fringe.app.service.CandleService;
import dev.fringe.app.service.SchedulerService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Import({ WebClientConfig.class, HibernateConfig.class })
@PropertySource("classpath:app.properties")
@ComponentScan("dev.fringe.app.service")
public class ScheduleTest implements InitializingBean {

	@Autowired SchedulerService schedulerService;
	@Autowired CandleService c;
	@Autowired SessionFactory sessionFactory;
	
	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(ScheduleTest.class);
	}

	@Transactional
	public void afterPropertiesSet() throws Exception {
		schedulerService.day();
		
//		Session session = sessionFactory.openSession();
//		try {
//			session.getTransaction().begin();
//			session.createQuery("delete from Candles u where u.unit = 'days' u.candleDateTimeUtc = :candleDateTimeUtc").setParameter("candleDateTimeUtc", "").executeUpdate();
//			session.getTransaction().commit();
//		} catch (Exception e) {
//			log.error(e.getMessage());
//			session.getTransaction().rollback();
//		}
//		System.out.println(c.getCandles("KRW-BTC", CandleService.DAYS));
	}

}
