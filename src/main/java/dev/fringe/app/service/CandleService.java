package dev.fringe.app.service;

import java.util.List;

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
	
	@SneakyThrows
	public List<Candles> getCandles(Market market) {
		Thread.sleep(200);
		return webclient.get().uri(uriBuilder -> uriBuilder.path("/candles/days").queryParam("count", "1").queryParam("market", "{market}").build(market.getMarket())).retrieve().bodyToFlux(Candles.class).collectList().block();
//		var expression = CronExpression.parse("10 * * * * *");
//		var result = expression.next(LocalDateTime.now());
//		System.out.println(result);
//		webclient.get().uri("/posts").retrieve().bodyToFlux(TestModel.class).collectList().block();
	}
}
