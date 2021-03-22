package dev.fringe.app;

import javax.transaction.Transactional;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import dev.fringe.app.config.HibernateConfig;
import dev.fringe.app.config.WebClientConfig;
import dev.fringe.app.service.QuickStartService;
import lombok.extern.log4j.Log4j2;

@Import({HibernateConfig.class, WebClientConfig.class})
@PropertySource("classpath:app.properties")
@ComponentScan("dev.fringe.app.service")
@Log4j2
public class QuickstartWithUpBit implements InitializingBean {

	
	@Autowired QuickStartService service;
	
	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(QuickstartWithUpBit.class);
	}

	@Transactional
	public void afterPropertiesSet() throws Exception {
		service.run();
	}

}
