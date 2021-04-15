package dev.fringe.app;

<<<<<<< HEAD
=======
import java.util.Arrays;
>>>>>>> 3a2d28f35c1803f9b96442bd8afe07f829ff2b5b
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import dev.fringe.app.config.HibernateConfig;
import dev.fringe.app.config.WebClientConfig;
import dev.fringe.app.service.MarketService;
import dev.fringe.app.service.TickService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Import({HibernateConfig.class,WebClientConfig.class})
@PropertySource("classpath:app.properties")
@ComponentScan("dev.fringe.app.service")
public class Db6LoadTickByWeb extends Support{

	@Autowired TickService tickService;
	@Autowired MarketService marketService;
	
	public static void main(String[] args) {
		if(args == null) {
		}
		args = new String[]{"KRW-BTC","200"};
		new AnnotationConfigApplicationContext(Db6LoadTickByWeb.class).getBean(Db6LoadTickByWeb.class).run(args);
	}

	@Transactional
	@SneakyThrows
	public void run(String[] args) {
<<<<<<< HEAD
		List<String> markets = marketService.getAllmarket();
//		List<String> markets = Arrays.asList("KRW-BTC");
=======
//		List<String> markets = marketService.getAllmarket();
		List<String> markets = Arrays.asList("KRW-BTC");
>>>>>>> 3a2d28f35c1803f9b96442bd8afe07f829ff2b5b
		for (String market : markets) {
			Thread.sleep(350);
			tickService.saveTickAndGetWebMarket(market, "200");
		}
	}

}
