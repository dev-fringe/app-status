package dev.fringe.app;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.fringe.app.config.HibernateConfig;
import dev.fringe.app.config.WebClientConfig;
import dev.fringe.app.model.Account;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Import({ WebClientConfig.class, HibernateConfig.class })
@PropertySource("classpath:app.properties")
@ComponentScan({ "dev.fringe.app.service" })
public class OAuthTest extends Support implements InitializingBean {

	@Value("${app.baseurl:https://api.upbit.com/v1}") String baseUrl;
	@Autowired private SessionFactory sessionFactory;
	@Autowired RestTemplate restTemplate;
	@Autowired RestTemplate restTemplateQueryHash;

	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(OAuthTest.class);
	}

	@Transactional
	public void afterPropertiesSet() throws Exception {
		List accounts = restTemplate.getForObject(baseUrl + "/accounts", List.class);//jwt는 webclient에선 안된다. 
		Session session = sessionFactory.openSession();
		try {
			session.getTransaction().begin();
			session.createQuery("delete from Account").executeUpdate();
			for (Object account : accounts) {
				Account ac = new ObjectMapper().convertValue(account, Account.class);
				session.save(ac);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			log.error(e.getMessage());
			session.getTransaction().rollback();
		}
		//필요 없을 듯 아 괜히 만들었다.
//		List wallet = restTemplate.getForObject(baseUrl + "/status/wallet", List.class);//jwt는 webclient에선 안된다. 
//		for (Object wal : wallet) {
//			Wallet w = new ObjectMapper().convertValue(wal, Wallet.class);
//			System.out.println(w);
////			session.save(w);
//		}
		
		String order = restTemplateQueryHash.getForObject(baseUrl + "/orders/chance?market="+ "KRW-BTC", String.class);//jwt는 webclient에선 안된다.
		System.out.println(order);
		
	}
}
