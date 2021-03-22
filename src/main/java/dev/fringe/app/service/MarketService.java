package dev.fringe.app.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import dev.fringe.app.model.Market;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class MarketService {

	@Autowired WebClient webclient;
	@Autowired private SessionFactory sessionFactory;
	
	public List<Market> getAllmarketByWeb(){
		return webclient.get().uri("/market/all?isDetails=false").retrieve().bodyToFlux(Market.class).collectList().block();
	}
	
	
	public Market getmarket(String marketCode) {
		Session session = sessionFactory.openSession();
		Market market = null;
        try {
			session.getTransaction().begin();
			market = session.createQuery("select u from Market u where id.market = :name ", Market.class).setParameter("name" , marketCode).getSingleResult();
			session.getTransaction().commit();
        } catch (Exception e) {
			log.error(e.getMessage());
			session.getTransaction().rollback();
        }
		return market;
	}
	
	public List<String> getAllmarket() {
		Session session = sessionFactory.openSession();
		List<String> markets = null;
        try {
			session.getTransaction().begin();
			markets = session.createQuery("select u.market from Market u", String.class).list();
			session.getTransaction().commit();
        } catch (Exception e) {
			log.error(e.getMessage());
			session.getTransaction().rollback();
        }
		return markets;
	}
		
}
