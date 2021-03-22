package dev.fringe.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import dev.fringe.app.model.Market;
import dev.fringe.app.model.Ticks;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class MarketService {

	@Autowired WebClient webclient;
	public static List<Market> markets;
	
	@SneakyThrows
	public void test() {
		if(markets == null) {
			markets = webclient.get().uri("/market/all?isDetails=false").retrieve().bodyToFlux(Market.class).collectList().block();
		}
		for (Market market : markets) {
			Thread.sleep(200);
			List<Ticks> t = webclient.get().uri(uriBuilder -> uriBuilder.path("/trades/ticks").queryParam("count", "1").queryParam("market", "{market}").build(market.getMarket())).retrieve().bodyToFlux(Ticks.class).collectList().block();
		}
	}
	public List<Market> getmarket(){
		return webclient.get().uri("/market/all?isDetails=false").retrieve().bodyToFlux(Market.class).collectList().block();
	}
}
