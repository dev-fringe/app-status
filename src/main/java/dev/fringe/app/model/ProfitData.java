package dev.fringe.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.ta4j.core.Bar;
import org.ta4j.core.Order.OrderType;
import org.ta4j.core.num.PrecisionNum;
import org.ta4j.core.tradereport.TradingStatement;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
public class ProfitData {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@Column(name = "uuid", unique = true)
	private String uuid;
	private String volumn;
	private String openPrice;
	private String closePrice;
	private String date;
	private String totalloss;
	private String totalprofitloss;
	private String totalprofitpercentage;
	private String breakeventradecount;
	private String losstradecount;
	private String profittradecount;
	private String totalprofit;
	private String precisionNum;
	private String orderType;
	
	public ProfitData(Bar b, TradingStatement t) {
		super();
    	this.volumn= b.getVolume().toString(); 
    	this.openPrice = b.getOpenPrice().toString(); 
    	this.closePrice = b.getClosePrice().toString(); 
    	this.date = b.getDateName().toString();
    	this.totalloss = t.getPerformanceReport().getTotalLoss().toString();
    	this.totalprofitloss = t.getPerformanceReport().getTotalProfitLoss().toString();
    	this.totalprofitpercentage = t.getPerformanceReport().getTotalProfitLossPercentage().toString();
    	this.breakeventradecount = t.getTradeStatsReport().getBreakEvenTradeCount().toString();
    	this.losstradecount = t.getTradeStatsReport().getLossTradeCount().toString();
    	this.profittradecount = t.getTradeStatsReport().getProfitTradeCount().toString();
    	this.totalprofit = t.getPerformanceReport().getTotalProfit().toString();
	}
	
	public ProfitData(Bar bar, TradingStatement tradingStatement, PrecisionNum precisionNum, OrderType orderType) {
		this(bar, tradingStatement);
		this.precisionNum = precisionNum.toString();
		this.orderType = orderType.toString();
	}
	
}
