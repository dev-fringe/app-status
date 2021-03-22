package dev.fringe.app;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
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

import dev.fringe.app.model.Candles;
import dev.fringe.app.service.BarService;

public class Support {

	@Autowired
	private BarService barService;
	
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

	
	protected BarSeries getBarSeries(String barName) {
		BarSeries series = getBars(barName);
//		System.out.println("Series: " + series.getName() + " (" + series.getSeriesPeriodDescription() + ")");
//	    System.out.println("Number of bars: " + series.getBarCount());
//	    for(int i=0; i < series.getBarCount() ; i++) {
//	    	System.out.println("Volume: " + series.getBar(i).getVolume() + "\t" + "Open price: "+ series.getBar(i).getOpenPrice() + "\t" + "Close price: " + series.getBar(i).getClosePrice() + "\t" +  series.getBar(i).getDateName() );
//	    }
		return series;
	}
	
	private BarSeries getBars(String barName) {
		List<Candles> bars = barService.getCandles(barName);
		System.out.println(bars);
		BarSeries series = new BaseBarSeries(barName);
		for (Candles bar : bars) {
			ZonedDateTime date = LocalDate.parse(bar.getCandleDateTimeUtc().substring(0, 10)).atStartOfDay(ZoneOffset.UTC);
			series.addBar(date, bar.getOpeningPrice(), bar.getHighPrice(), bar.getLowPrice(), bar.getPrevClosingPrice(), bar.getCandleAccTradeVolume());
		}
		return series;
	}
	
	public BarSeries loadBitstampSeries(List<String[]> lines ,String name) {
        BarSeries series = new BaseBarSeries(name);
        if ((lines != null) && !lines.isEmpty()) {

            // Getting the first and last trades timestamps
            ZonedDateTime beginTime = ZonedDateTime
                    .ofInstant(Instant.ofEpochMilli(Long.parseLong(lines.get(0)[0]) * 1000), ZoneId.systemDefault());
            ZonedDateTime endTime = ZonedDateTime.ofInstant(
                    Instant.ofEpochMilli(Long.parseLong(lines.get(lines.size() - 1)[0]) * 1000),
                    ZoneId.systemDefault());
            if (beginTime.isAfter(endTime)) {
                Instant beginInstant = beginTime.toInstant();
                Instant endInstant = endTime.toInstant();
                beginTime = ZonedDateTime.ofInstant(endInstant, ZoneId.systemDefault());
                endTime = ZonedDateTime.ofInstant(beginInstant, ZoneId.systemDefault());
                // Since the CSV file has the most recent trades at the top of the file, we'll
                // reverse the list to feed
                // the List<Bar> correctly.
                Collections.reverse(lines);
            }
            // build the list of populated bars
            buildSeries(series, beginTime, endTime, 300, lines);
        }

        return series;
	}
	
	public void buildSeries(BarSeries series, ZonedDateTime beginTime, ZonedDateTime endTime, int duration,
            List<String[]> lines) {

        Duration barDuration = Duration.ofSeconds(duration);
        ZonedDateTime barEndTime = beginTime;
        // line number of trade data
        int i = 0;
        do {
            // build a bar
            barEndTime = barEndTime.plus(barDuration);
            Bar bar = new BaseBar(barDuration, barEndTime, series.function());
            do {
                // get a trade
                String[] tradeLine = lines.get(i);
                ZonedDateTime tradeTimeStamp = ZonedDateTime
                        .ofInstant(Instant.ofEpochMilli(Long.parseLong(tradeLine[0]) * 1000), ZoneId.systemDefault());
                System.out.println(tradeTimeStamp);
                // if the trade happened during the bar
                if (bar.inPeriod(tradeTimeStamp)) {
                    // add the trade to the bar
                    double tradePrice = Double.parseDouble(tradeLine[1]);
                    double tradeVolume = Double.parseDouble(tradeLine[2]);
                    bar.addTrade(tradeVolume, tradePrice, series.function());
                } else {
                    // the trade happened after the end of the bar
                    // go to the next bar but stay with the same trade (don't increment i)
                    // this break will drop us after the inner "while", skipping the increment
                    break;
                }
                i++;
            } while (i < lines.size());
            // if the bar has any trades add it to the bars list
            // this is where the break drops to
            if (bar.getTrades() > 0) {
                series.addBar(bar);
            }
        } while (barEndTime.isBefore(endTime));
    }
}
