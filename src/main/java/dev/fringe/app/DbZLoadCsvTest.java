package dev.fringe.app;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.h2.tools.Csv;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;

import dev.fringe.app.config.HibernateConfig;
import dev.fringe.app.model.Bars;
import dev.fringe.app.model.Trades;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Import(HibernateConfig.class)
public class DbZLoadCsvTest implements InitializingBean {

	@Autowired SessionFactory sessionFactory;

	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(DbZLoadCsvTest.class);
	}

	@Transactional
	public void afterPropertiesSet() throws Exception {
		ResultSet rs = new Csv().read("appleinc_bars_from_20130101_usd.csv", null, null); // rest로 전환
		List<Bars> bars = new ArrayList<Bars>();
		while (rs.next()) {
			bars.add(new Bars("apple", rs));
		}
		rs.close();
		rs = new Csv().read("appleinc_trades.csv", null, null); // rest로 전환
		List<Trades> trades = new ArrayList<Trades>();
		while (rs.next()) {
			trades.add(new Trades("apple",rs));
		}
		Session session = sessionFactory.openSession();
		try {
			session.getTransaction().begin();
			for (Bars bars2 : bars) {
				session.save(bars2);
			}
			for (Trades trade : trades) {
				session.save(trade);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			log.error(e.getMessage());
			session.getTransaction().rollback();
		}
	}
}
