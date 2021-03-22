package dev.fringe.app;

import org.h2.tools.Server;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;

import dev.fringe.app.config.H2Config;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Import({H2Config.class})
public class Db implements InitializingBean {
	
	@Autowired Server h2server;
	
	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(Db.class);
	}
	
	public void afterPropertiesSet() throws Exception {
		h2server.start();
//		log.info("done");
	}
}
