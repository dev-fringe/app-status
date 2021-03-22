package dev.fringe.app.http;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.SneakyThrows;

/**
 * @author 
 * http 영역은 autowired가 안된다. 생성자..
 */
public class RestTemplateQueryHashAndHeaderJwtInterceptor implements ClientHttpRequestInterceptor {

	private String accessKey;
	private String secretKey;
	
	public RestTemplateQueryHashAndHeaderJwtInterceptor() {
		super();
	}

	public RestTemplateQueryHashAndHeaderJwtInterceptor(String accessKey, String secretKey) {
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}

	@SneakyThrows
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution){
		HttpHeaders headers = request.getHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Authorization", createJwt(request.getURI().getQuery()));
		ClientHttpResponse response = execution.execute(request, body);
		return response;
	}

	@SneakyThrows
	public String createJwt(String query) {// 거래 관련된 서비스는 추가적으로 queryhash를 해서 보내줘야 한다.
        ArrayList<String> queryElements = new ArrayList<>();
        queryElements.add(query);
        String queryString = String.join("&", queryElements.toArray(new String[0]));
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes("UTF-8"));
        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));
		return "Bearer " + JWT.create().withClaim("access_key", accessKey).withClaim("nonce", UUID.randomUUID().toString()).withClaim("query_hash", queryHash).withClaim("query_hash_alg", "SHA512").sign(Algorithm.HMAC256(secretKey));
	}
}