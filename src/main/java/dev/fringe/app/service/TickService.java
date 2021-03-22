package dev.fringe.app.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import dev.fringe.app.mapper.DatabaseMapper;
import dev.fringe.app.model.Ticks;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TickService {

	@Autowired WebClient webclient;
	@Autowired private SessionFactory sessionFactory;
	
	@SneakyThrows
	public List<Ticks> getCountByMarket(String market, String count) {
		return webclient.get().uri(uriBuilder -> uriBuilder.path("/trades/ticks").queryParam("count", count).queryParam("market", "{market}").build(market)).retrieve().bodyToFlux(Ticks.class).collectList().block();
	}
	
	public void saveTickAndGetWebMarket(String market, String count) {
		List<Ticks> ticks = this.getCountByMarket(market,count);
		Session session = sessionFactory.openSession();
		try {
			session.getTransaction().begin();
			for (Ticks tick : ticks) {
				tick.setId(tick);
				tick.setTradeDateTimeGmt9();
				Ticks t = (Ticks) session.get(Ticks.class, tick.getId());
    			if(t != null) {
//    				session.update(tick);
    			}else {
    			    if ( ticks.size() % 50 == 0 ) { //20, same as the JDBC batch size
    			        //flush a batch of inserts and release memory:
    			        session.flush();
    			        session.clear();
    			    }
    				session.save(tick);
    			}
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			log.error(e.getMessage());
			session.getTransaction().rollback();
		}
	}

	public void saveTickAndGetWebMarket(String market) {
		this.saveTickAndGetWebMarket(market, "1");
	}
	@Autowired DatabaseMapper mapper;
	public List<Ticks> getTicksByMarketAndCount(String market, String count) {
		List<Ticks> ticks = mapper.selectTick(market, count);
//		Session session = sessionFactory.openSession();
//		List<Ticks> ticks = null;
//		try {
//			session.getTransaction().begin();
//			ticks = session.createQuery("select u from Ticks u where id.market = :name AND rownum <= :count ORDER BY u.timestamp ASC ", Ticks.class).setParameter("name",market).setParameter("count",count).list();
//			session.getTransaction().commit();
//		} catch (Exception e) {
//			log.error(e.getMessage());
//			session.getTransaction().rollback();
//		}
		return ticks;
	}
	
}
