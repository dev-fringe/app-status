package dev.fringe.app.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table
@Entity
public class Quickstart implements Serializable{

	private static final long serialVersionUID = 5863716675045480772L;
	
	@Id
	private QuickstartId id;
	private String firstClosePrice;
	private String valueAtIndex; //5-bars-SMA value at the 42nd index: 78331200
	private String numberOfTradesForOurStrategy;
	private String profitableTradesRatio;
	private String OurProfitVsBuyAndHoldProfit;
	private String barCount;
	public Quickstart(String market, String firstClosePrice, String valueAtIndex, String numberOfTradesForOurStrategy, String profitableTradesRatio, String OurProfitVsBuyAndHoldProfit, String barCount) {
		if(id == null) {
			id = new QuickstartId(market);
		}
		this.firstClosePrice = firstClosePrice;
		this.valueAtIndex = valueAtIndex;
		this.numberOfTradesForOurStrategy = numberOfTradesForOurStrategy;
		this.profitableTradesRatio = profitableTradesRatio;
		this.OurProfitVsBuyAndHoldProfit = OurProfitVsBuyAndHoldProfit;
		this.barCount = barCount;
	}

}
