package dev.fringe.app.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;

import dev.fringe.app.model.Candles;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class BarService {

	@Autowired WebClient webclient;
	@Autowired CandleService candleService;
	@Autowired SessionFactory sessionFactory;
	
    public BarSeries getBarSeries(String market) {
    	return this.getBarSeries(market, "200", candleService.getDayCandle(market));
    }
    
    public BarSeries getBarSeries(String market, String count, Candles daycandle) {
    	List<Candles> candles = candleService.lastCandlesByCount(market,count);
        BarSeries series = new BaseBarSeriesBuilder().withName(market).build();
        candles.forEach(candle -> {
            series.addBar(
            		Duration.ofMinutes(1),//Duration timePeriod,
            		ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(candle.getId().getTimestamp())), ZoneId.of("+09:00:00")), //endTime
            		new BigDecimal(daycandle.getPrevClosingPrice()),//openPrice
            		new BigDecimal(candle.getHighPrice()),//highPrice
            		new BigDecimal(candle.getLowPrice()),//lowPrice
            		new BigDecimal(daycandle.getPrevClosingPrice()),//closePrice
            		new BigDecimal(candle.getCandleAccTradeVolume()), //volume
            		new BigDecimal(candle.getCandleAccTradePrice()) //amount
            		); //amount
            //    default void addBar(Duration timePeriod, ZonedDateTime endTime, Number openPrice, Number highPrice, Number lowPrice,
            //Number closePrice, Number volume, Number amount) {
        });
        return series;
    }
    
    public List<Candles> getCandles(String maket){
		Transaction transaction = null;
	List<Candles> bars = null;
	try {
		Session session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		bars = session.createQuery("select u from Candles u where id.market = :name ORDER BY id.candleDateTimeUtc ASC", Candles.class).setParameter("name",maket).list();
		transaction.commit();
	} catch (Exception e) {
		if (transaction != null) {
			transaction.rollback();
		}
	}
	 	return bars;
    }
}
