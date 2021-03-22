package dev.fringe.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TestService2 {

	@Autowired WebClient webclient;
	
	public void flux() {
//		var expression = CronExpression.parse("10 * * * * *");
//		var result = expression.next(LocalDateTime.now());
//		System.out.println(result);
//		webclient.get().uri("/posts").retrieve().bodyToFlux(TestModel.class).collectList().block();
	}
}
