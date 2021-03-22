package dev.fringe.app;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Indicator;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import org.ta4j.core.trading.rules.UnderIndicatorRule;

import dev.fringe.app.model.Bars;

public class Support {

	@Autowired
	private SessionFactory sessionFactory;
	
	
    public Rule createEntryRule(BarSeries series, int barCount) {
        Indicator<Num> closePrice = new ClosePriceIndicator(series);
        SMAIndicator sma = new SMAIndicator(closePrice, barCount);
        return new UnderIndicatorRule(sma, closePrice);
    }

    public Rule createExitRule(BarSeries series, int barCount) {
        Indicator<Num> closePrice = new ClosePriceIndicator(series);
        SMAIndicator sma = new SMAIndicator(closePrice, barCount);
        return new OverIndicatorRule(sma, closePrice);
    }
    
    public List<Strategy> getStrategies(String name, BarSeries series, Integer start, Integer barCount, Integer step) {
		if(start == null) {
			start = 0;
		}
		if(barCount == null) {
			 barCount = series.getBarCount();
		}
		if(step == null) {
			step = 1;
		}		
		final List<Strategy> strategies = new ArrayList<>();
		for (int i = start; i <= barCount; i += step) {
			Strategy strategy = new BaseStrategy(name + "(" + i + ")", createEntryRule(series, i),createExitRule(series, i));
			strategies.add(strategy);
		}
		return strategies;
	}

	public List<Strategy> getStrategies(String name, BarSeries series) {
		return this.getStrategies(name, series, null, null, null);
	}

	
	public BarSeries getBarSeries(String barName) {
		BarSeries series = getBars(barName);
		System.out.println("Series: " + series.getName() + " (" + series.getSeriesPeriodDescription() + ")");
	    System.out.println("Number of bars: " + series.getBarCount());
	    for(int i=0; i < series.getBarCount() ; i++) {
	    	System.out.println("Volume: " + series.getBar(i).getVolume() + "\t" + "Open price: "+ series.getBar(i).getOpenPrice() + "\t" + "Close price: " + series.getBar(i).getClosePrice() + "\t" +  series.getBar(i).getDateName() );
	    }
		return series;
	}
	
	private BarSeries getBars(String barName) {
		Transaction transaction = null;
		List<Bars> bars = null;
		try {
			Session session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			bars = session.createQuery("select u from Bars u where id.name = :name", Bars.class).setParameter("name",barName).list();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		BarSeries series = new BaseBarSeries(barName);
		for (Bars bar : bars) {
			ZonedDateTime date = LocalDate.parse(bar.getId().getDate()).atStartOfDay(ZoneId.systemDefault());
			series.addBar(date, bar.getOpen(), bar.getHigh(), bar.getLow(), bar.getClose(), bar.getVolume());
		}
		return series;
	}
}
