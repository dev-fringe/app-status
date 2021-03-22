package dev.fringe.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import dev.fringe.app.model.Market;
import dev.fringe.app.model.Ticks;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TestService {

	@Autowired WebClient webclient;
	
	public void test() {
		List<Market> m = webclient.get().uri("/market/all?isDetails=false").retrieve().bodyToFlux(Market.class).collectList().block();
		for (Market market : m) {
			List<Ticks> t = webclient.get().uri(uriBuilder -> uriBuilder
					    .path("/trades/ticks")
					    .queryParam("count", "1")
					    .queryParam("market", "{market}")
					    .build(market.getMarket()))
					    .retrieve().bodyToFlux(Ticks.class).collectList().block();
			System.out.println(t);
//			System.out.println(market);
//			https://api.upbit.com/v1/trades/ticks?count=1&market=KRW-BTC
		}
	}
}
