/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Marc de Verdelhan, 2017-2019 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


import java.awt.Color;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.Indicator;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.CashFlow;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;

import dev.fringe.app.config.HibernateConfig;
import dev.fringe.app.config.WebClientConfig;
import dev.fringe.app.mapper.DatabaseMapper;
import dev.fringe.app.model.Ticks;
import dev.fringe.app.service.MarketService;
import dev.fringe.app.service.QuickStartService;
import dev.fringe.app.service.TickService;
import lombok.extern.log4j.Log4j2;
import ta4jexamples.strategies.MovingMomentumStrategy;

@Import({HibernateConfig.class, WebClientConfig.class})
@PropertySource("classpath:app.properties")
@ComponentScan("dev.fringe.app.service")
@Log4j2
public class CashFlowToChart  implements InitializingBean {

    /**
     * Builds a JFreeChart time series from a Ta4j bar series and an indicator.
     *
     * @param barSeries the ta4j bar series
     * @param indicator the indicator
     * @param name      the name of the chart time series
     * @return the JFreeChart time series
     */
    private static org.jfree.data.time.TimeSeries buildChartBarSeries(BarSeries barSeries, Indicator<Num> indicator,
            String name) {
        org.jfree.data.time.TimeSeries chartBarSeries = new org.jfree.data.time.TimeSeries(name);
        for (int i = 0; i < barSeries.getBarCount(); i++) {
            Bar bar = barSeries.getBar(i);
            chartBarSeries.add(new Minute(new Date(bar.getEndTime().toEpochSecond() * 1000)),
                    indicator.getValue(i).doubleValue());
        }
        return chartBarSeries;
    }

    /**
     * Adds the cash flow axis to the plot.
     *
     * @param plot    the plot
     * @param dataset the cash flow dataset
     */
    private static void addCashFlowAxis(XYPlot plot, TimeSeriesCollection dataset) {
        final NumberAxis cashAxis = new NumberAxis("Cash Flow Ratio");
        cashAxis.setAutoRangeIncludesZero(false);
        plot.setRangeAxis(1, cashAxis);
        plot.setDataset(1, dataset);
        plot.mapDatasetToRangeAxis(1, 1);
        final StandardXYItemRenderer cashFlowRenderer = new StandardXYItemRenderer();
        cashFlowRenderer.setSeriesPaint(0, Color.blue);
        plot.setRenderer(1, cashFlowRenderer);
    }

    /**
     * Displays a chart in a frame.
     *
     * @param chart the chart to be displayed
     */
    private static void displayChart(JFreeChart chart) {
        // Chart panel
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new Dimension(1024, 400));
        // Application frame
        ApplicationFrame frame = new ApplicationFrame("Ta4j example - Cash flow to chart");
        frame.setContentPane(panel);
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
    }

    public static void main(String[] args) {

		new AnnotationConfigApplicationContext(CashFlowToChart.class);
    }

	@Autowired TickService service;
	@Autowired DatabaseMapper mapper;
	@Autowired SessionFactory sessionFactory;
	@Autowired MarketService marketService;
	@Autowired QuickStartService qservice;
	
	public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

	@Override
	public void afterPropertiesSet() throws Exception {
//		List<String> markets = marketService.getAllmarket();
//		for (String market : markets) {
			String market = "KRW-BTC";
			System.out.println(market);
			List<Ticks> l = service.getTicksByMarketAndCount(market, "100000");
			List<String[]> a = new ArrayList<>();
			Map<String, Ticks> m = new HashMap<>();
			for (Ticks o : l) {
				m.put(String.valueOf(Long.parseLong(o.getTimestamp())/1000), o);
			}
		    for (Map.Entry<String, Ticks> entry : m.entrySet()) {
		    	Ticks tt = (Ticks) entry.getValue();
                ZonedDateTime tradeTimeStamp = ZonedDateTime
                        .ofInstant(Instant.ofEpochMilli(Long.parseLong((String) entry.getKey()) * 1000), ZoneId.systemDefault());
                System.out.println(tradeTimeStamp);
		    	System.out.println("" + entry.getKey() + "," + tt.getTradePrice()+ "," + tt.getTradeVolume());
		    	a.add(new String[] {(String) entry.getKey() , tt.getTradePrice(), tt.getTradeVolume()});
		    }
	        BarSeries series = qservice.loadBitstampSeries(a, "KRW-BTC");
	        System.out.println("Series: " + series.getName() + " (" + series.getSeriesPeriodDescription() + ")");
	        System.out.println("Number of bars: " + series.getBarCount());
	        System.out.println("First bar: \n" + "\tVolume: " + series.getBar(0).getVolume() + "\n" + "\tNumber of trades: "
	                + series.getBar(0).getTrades() + "\n" + "\tClose price: " + series.getBar(0).getClosePrice());
        Strategy strategy = MovingMomentumStrategy.buildStrategy(series);
        BarSeriesManager seriesManager = new BarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(strategy);
        CashFlow cashFlow = new CashFlow(series, tradingRecord);

        TimeSeriesCollection datasetAxis1 = new TimeSeriesCollection();
        datasetAxis1.addSeries(buildChartBarSeries(series, new ClosePriceIndicator(series), "Bitstamp Bitcoin (BTC)"));
        TimeSeriesCollection datasetAxis2 = new TimeSeriesCollection();
        datasetAxis2.addSeries(buildChartBarSeries(series, cashFlow, "Cash Flow"));

        /*
         * Creating the chart
         */
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Bitstamp BTC", // title
                "Date", // x-axis label
                "Price", // y-axis label
                datasetAxis1, // data
                true, // create legend?
                true, // generate tooltips?
                false // generate URLs?
        );
        XYPlot plot = (XYPlot) chart.getPlot();
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("MM-dd HH:mm:ssZ"));

        /*
         * Adding the cash flow axis (on the right)
         */
        addCashFlowAxis(plot, datasetAxis2);

        /*
         * Displaying the chart
         */
        displayChart(chart);
//		}
	}
}
