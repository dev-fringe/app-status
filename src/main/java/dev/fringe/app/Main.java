package dev.fringe.app;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import dev.fringe.app.config.HibernateConfig;
import dev.fringe.app.config.SchedulerConfig;
import dev.fringe.app.config.WebClientConfig;
import dev.fringe.app.mapper.DatabaseMapper;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Import({HibernateConfig.class,WebClientConfig.class})
@ComponentScan("dev.fringe.app.service")
public class Main implements InitializingBean {
	@Autowired DatabaseMapper mapper;
	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(Main.class);
	}
	public void afterPropertiesSet() throws Exception {
		mapper.select2().forEach(e -> System.out.println(e));
//		log.info("done");
	}
}
