package dev.fringe.app.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import dev.fringe.app.http.RestTemplateHeaderJwtInterceptor;
import dev.fringe.app.http.RestTemplateQueryHashAndHeaderJwtInterceptor;
import lombok.extern.log4j.Log4j2;


/**
 * RestTemplate As of 5.0 this class is in maintenance mode, with only minor requests for changes and bugs to be accepted going forward
 * spring 5.0 release date - September 28, 2017
 */
@Configuration
@Log4j2
public class RestTemplateConfig {
	
	@Value("${app.access.key:oBpIMdHiTCITDOL9iDlpiMMx7ypppi6Lcqglh6wo}")
	private String accessKey;
	@Value("${app.secret.key:I62MxLxFar2OR8aP4HlOtVqUxd59YLAfsEq1Xewm}")
	private String secretKey;
	

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new RestTemplateHeaderJwtInterceptor(accessKey, secretKey)));
		return restTemplate;
	}
	
	@Bean
	public RestTemplate restTemplateQueryHash() {
		RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new RestTemplateQueryHashAndHeaderJwtInterceptor(accessKey, secretKey)));
		return restTemplate;
	}

}
