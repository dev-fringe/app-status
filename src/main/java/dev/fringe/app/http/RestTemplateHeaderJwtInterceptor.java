package dev.fringe.app.http;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

/**
 * @author 
 * http 영역은 autowired가 안된다. 생성자..
 */
public class RestTemplateHeaderJwtInterceptor implements ClientHttpRequestInterceptor {

	private String accessKey;
	private String secretKey;
	
	public RestTemplateHeaderJwtInterceptor() {
		super();
	}

	public RestTemplateHeaderJwtInterceptor(String accessKey, String secretKey) {
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}

	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		HttpHeaders headers = request.getHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Authorization", createJwt());
		ClientHttpResponse response = execution.execute(request, body);
		return response;
	}
	
	public String createJwt() {
		return "Bearer " + JWT.create().withClaim("access_key", accessKey).withClaim("nonce", UUID.randomUUID().toString()).sign(Algorithm.HMAC256(secretKey));
	}
}