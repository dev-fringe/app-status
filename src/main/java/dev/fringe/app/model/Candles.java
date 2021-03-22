package dev.fringe.app.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table
public class Candles implements Serializable{
	
	private static final long serialVersionUID = -8842235631963721948L;
	@Id private CandlesId id;
	
	@Transient
	@JsonProperty("market")
	private String market;
	@Transient
	@JsonProperty("candle_date_time_utc")
	private String candleDateTimeUtc;
	@JsonProperty("candle_date_time_kst")
	private String candleDateTimeKst;
	@JsonProperty("opening_price")
	private String openingPrice;
	@JsonProperty("high_price")
	private String highPrice;
	@JsonProperty("low_price")
	private String lowPrice;
	@JsonProperty("trade_price")
	private String tradePrice;
	@JsonProperty("timestamp")
	private String timestamp;
	@JsonProperty("candle_acc_trade_price")
	private String candleAccTradePrice;
	@JsonProperty("candle_acc_trade_volume")
	private String candleAccTradeVolume;
	@JsonProperty("prev_closing_price")
	private String prevClosingPrice;
	@JsonProperty("change_price")
	private String changePrice;
	@JsonProperty("change_rate")
	private String changeRate;
}