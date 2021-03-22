package dev.fringe.app.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.SneakyThrows;

@Configuration
public class H2Config {

	@Bean
	@SneakyThrows
	public Server h2server() {
		return Server.createTcpServer();
	}
}
