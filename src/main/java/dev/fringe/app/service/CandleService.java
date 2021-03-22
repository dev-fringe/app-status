package dev.fringe.app.service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import dev.fringe.app.model.Candles;
import dev.fringe.app.model.Market;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class CandleService {

	@Autowired WebClient webclient;
	@Autowired private SessionFactory sessionFactory;
	
	public static String DAYS = "days";
	public static String MINUTES = "minutes";

	public List<Candles> getWebCandles(Market market) {
		return this.getWebCandles(market, DAYS, "365");
	}
	
	public List<Candles> getWebCandles(Market market, String candleType) {
		if(candleType.equals(DAYS)) {
			return this.getWebCandles(market, candleType, "365");
		}else {
			return this.getWebCandles(market, candleType, "200");
		}
	}
	
	@SneakyThrows
	public List<Candles> getWebCandles(Market market, String candleType, String count) {
		if(candleType.equals(DAYS)) {
			return webclient.get().uri(uriBuilder -> uriBuilder.path("/candles/days").queryParam("count", count).queryParam("market", "{market}").build(market.getMarket())).retrieve().bodyToFlux(Candles.class).collectList().block();
		}else if(candleType.equals(MINUTES)) {
			return webclient.get().uri(uriBuilder -> uriBuilder.path("/candles/minutes/"+"1").queryParam("count", count).queryParam("market", "{market}").build(market.getMarket())).retrieve().bodyToFlux(Candles.class).collectList().block();
		}else {
			return null;
		}
	}
	
	@SneakyThrows
	public List<Candles> getWebCandles(String market, String candleType, String count) {
		if(candleType.equals(DAYS)) {
			return webclient.get().uri(uriBuilder -> uriBuilder.path("/candles/days").queryParam("count", count).queryParam("market", "{market}").build(market)).retrieve().bodyToFlux(Candles.class).collectList().block();
		}else if(candleType.equals(MINUTES)) {
			return webclient.get().uri(uriBuilder -> uriBuilder.path("/candles/minutes/"+"1").queryParam("count", count).queryParam("market", "{market}").build(market)).retrieve().bodyToFlux(Candles.class).collectList().block();
		}else {
			return null;
		}
	}
	
	
	@SneakyThrows
	public Candles getCandles(String market, String candleType) {
		Session session = sessionFactory.openSession();
		Candles c = null;
		try {
			session.getTransaction().begin();
			c = session.createQuery("select u from Candles u where id.market = :market and u.unit = 'days' and u.candleDateTimeUtc = :candleDateTimeUtc AND rownum = 1", Candles.class).setParameter("market",market).setParameter("candleDateTimeUtc", new SimpleDateFormat("yyyy-MM-dd").format(new Date(new Date().getTime() - TimeUnit.DAYS.toMillis(1))) + "T00:00:00").uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			log.error(e.getMessage());
			session.getTransaction().rollback();
		}
		return c;
		
	}
	public List<Candles> lastCandlesByCount(String market, String count) {
		Session session = sessionFactory.openSession();
		List<Candles> candles = null;
		try {
			session.getTransaction().begin();
			candles = session.createQuery("select u from Candles u where id.market = :name AND rownum <= :count  and u.unit = '1' ORDER BY timestamp DESC ", Candles.class).setParameter("name",market).setParameter("count",count).list();
			session.getTransaction().commit();
			Collections.sort(candles, new Comparator<Candles>() {
		        public int compare(Candles o1, final Candles o2) {
		            return o1.getId().getTimestamp().compareTo(o2.getId().getTimestamp());
		        }
		    });
		} catch (Exception e) {
			log.error(e.getMessage());
			session.getTransaction().rollback();
		}
		return candles;
	}
	
	public Candles getDayCandle(String market) {
		Session session = sessionFactory.openSession();
		Candles candles = null;
		try {
			session.getTransaction().begin();
			candles = session.createQuery("select u from Candles u where id.market = :name AND rownum = 1  and u.unit = 'days' ORDER BY timestamp DESC ", Candles.class).setParameter("name",market).uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			log.error(e.getMessage());
			session.getTransaction().rollback();
		}
		return candles;
	}
}
