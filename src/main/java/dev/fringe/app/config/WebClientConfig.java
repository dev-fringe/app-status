package dev.fringe.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.fringe.app.mapper.DatabaseMapper;
import dev.fringe.app.model.RemainReq;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Configuration
@Import(HibernateConfig.class)
@Log4j2
public class WebClientConfig {
	@Value("${app.baseurl:https://api.upbit.com/v1}")
	String baseUrl;
	@Autowired DatabaseMapper remainReqMapper;
	
	@Bean
	public WebClient webclient() {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
		final ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
				.codecs(configurer -> configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(mapper)))
				.build();
		return WebClient.builder().filters(exchangeFilterFunctions -> {
//			exchangeFilterFunctions.add(logRequest());
			exchangeFilterFunctions.add(logResponse());
		}).baseUrl(baseUrl).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).defaultHeader("Accept-Encoding", "gzip")
				.exchangeStrategies(exchangeStrategies).build();
	}

	@Deprecated
	private ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
			StringBuilder sb = new StringBuilder("Request headers: \t").append(clientRequest.method()).append(" ")
					.append(clientRequest.url());
			clientRequest.headers().forEach(
					(name, values) -> values.forEach(value -> sb.append(",").append(name).append(":").append(value)));
			log.debug(sb.toString());
			return Mono.just(clientRequest);
		});
	}

	private ExchangeFilterFunction logResponse() {
		return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
			StringBuilder sb = new StringBuilder("Response headers: \t").append("Status: ").append(clientResponse.rawStatusCode());
			clientResponse.headers().asHttpHeaders().forEach(
					(key, value1) -> value1.forEach(value -> {	
						if(key.equals("Remaining-Req")){ 
							sb.append(",").append(key).append(":").append(value);
							RemainReq req = new RemainReq(value);
							if(remainReqMapper.select(req)) {
								remainReqMapper.update(req);
							}else {
								remainReqMapper.save(req);
							}
//							Session session = sessionFactory.openSession();
//				    		try {
//				    			session.getTransaction().begin();
//				    			RemainReq remain = (RemainReq) session.get(RemainReq.class, new RemainReq(value).getId());
//				    			if(remain != null) {
//				    				session.update(new RemainReq(value));
//				    			}else {
//				    				session.save(new RemainReq(value));
//				    			}
//					            session.getTransaction().commit();
//					        } catch (Exception e) {
//								log.error(e.getMessage());
//								session.getTransaction().rollback();
//					        }							
						}
					}));
			log.debug(sb.toString());
			return Mono.just(clientResponse);
		});
	}
}
