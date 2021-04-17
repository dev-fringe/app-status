package dev.fringe.app.model;

import java.io.Serializable;

import javax.persistence.Column;
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
	@Column(name = "first_Close_Price")	
	private String firstClosePrice;
	@Column(name = "trade_Count")
	private String tradeCount; 
	@Column(name = "profit_Trades_Ratio")
	private String profitTradesRatio;
	@Column(name = "reward_Risk_Ratio")
	private String rewardRiskRatio;
	@Column(name = "vs_Buy_And_Hold")
	private String vsBuyAndHold;
	@Column(name= "bar_Count")
	private String barCount;
	
	public Quickstart(String market, 
			String firstClosePrice, 
			String tradeCount, 
			String profitTradesRatio, 
			String rewardRiskRatio, 
			String vsBuyAndHold, 
			String barCount) {
		if(id == null) {
			id = new QuickstartId(market);
		}
		this.firstClosePrice = firstClosePrice;
		this.tradeCount = tradeCount;
		this.profitTradesRatio = profitTradesRatio;
		this.rewardRiskRatio = rewardRiskRatio;
		this.vsBuyAndHold = vsBuyAndHold;
		this.barCount = barCount;
	}

}
