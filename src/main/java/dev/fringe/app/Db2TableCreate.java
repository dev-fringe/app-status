package dev.fringe.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;

import dev.fringe.app.config.HibernateTableCreationConfig;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Import(HibernateTableCreationConfig.class)
public class Db2TableCreate {

	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(Db2TableCreate.class);
	}
}
