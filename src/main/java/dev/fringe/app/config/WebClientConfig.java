package dev.fringe.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Configuration
@Log4j2
public class WebClientConfig {

	//'https://api.upbit.com/v1/market/all?isDetails=false
	@Value("${app.baseurl:https://api.upbit.com/v1}") String baseUrl;

	@Bean
	public WebClient webclient() {
		return WebClient.builder().filters(exchangeFilterFunctions -> {
//			exchangeFilterFunctions.add(logRequest());
//			exchangeFilterFunctions.add(logResponse());
		}).baseUrl(baseUrl).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}
	
	@Deprecated
	private ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
			StringBuilder sb = new StringBuilder("Request headers: \t").append(clientRequest.method()).append(" ").append(clientRequest.url());
			clientRequest.headers().forEach((name, values) -> values.forEach(value -> sb.append(",").append(name).append(":").append(value)));
//			log.debug(sb.toString());
			return Mono.just(clientRequest);
		});
	}

	@Deprecated
	private ExchangeFilterFunction logResponse() {
		return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
			StringBuilder sb = new StringBuilder("Response headers: \t").append("Status: ").append(clientResponse.rawStatusCode());
			clientResponse.headers().asHttpHeaders().forEach((key, value1) -> value1.forEach(value -> sb.append(",").append(key).append(":").append(value)));
//			log.debug(sb.toString());
			return Mono.just(clientResponse);
		});
	}

}
