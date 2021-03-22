package dev.fringe.app.service;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import dev.fringe.app.model.Candles;
import dev.fringe.app.model.CandlesId;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import net.bytebuddy.asm.Advice.OffsetMapping.Target.AbstractReadOnlyAdapter;

@Service
@Log4j2
public class SchedulerService {

	@Autowired WebClient webclient;
	@Autowired MarketService marketService;
	@Autowired SessionFactory sessionFactory;
	@Autowired TickService tickService;
	@Autowired CandleService candleService;
	@Autowired QuickStartService service;
	// SOME RULE From DB
	//그냥 그날짜 지우고 insert
	@Transactional
	public void day() {
		List<String> markets = marketService.getAllmarket();
		Session session = sessionFactory.openSession();
		try {
			session.getTransaction().begin();
			for (String market : markets) {
				Candles cc =  candleService.getCandles(market, CandleService.DAYS);
				if(cc == null) {
					Thread.sleep(350);
					List<Candles> list = candleService.getWebCandles(market, CandleService.DAYS, "1");
					for (Candles candles : list) {
						candles.setId(new CandlesId(candles));
						Candles t = (Candles) session.get(Candles.class, candles.getId());
		    			if(t == null) {
		    				session.save(candles);
		    			}
					}
				}
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			log.error(e.getMessage());
			session.getTransaction().rollback();
		}
		
	}
	
	@SneakyThrows
	public void tick() {
		List<String> markets = Arrays.asList("KRW-BTC");
		for (String market : markets) {
			Thread.sleep(350);
			tickService.saveTickAndGetWebMarket(market, "200");
		}
	}

	public void quickstart() {
	}
	
	public void order() {
	}
	
	public void orderDelete() {
	}
	
}
