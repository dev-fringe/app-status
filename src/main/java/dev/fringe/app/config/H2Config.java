package dev.fringe.app.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.SneakyThrows;

@Configuration
public class H2Config {

	/**
	 * 
	 * > java -cp h2-*.jar org.h2.tools.Shell
		Welcome to H2 Shell
		Exit with Ctrl+C
		[Enter]   jdbc:h2:mem:2
		URL       jdbc:h2:./test
		[Enter]   org.h2.Driver
		Driver
		[Enter]   sa
		User      your_username
		Password  (hidden)
		Type the same password again to confirm database creation.
		Password  (hidden)
		Connected
		sql> quit
		Connection closed
	 */
	@Bean
	@SneakyThrows
	public Server h2server() {
		return Server.createTcpServer();
	}
}
