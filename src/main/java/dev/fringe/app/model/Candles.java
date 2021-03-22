package dev.fringe.app.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table
public class Candles implements Serializable{
	
	private static final long serialVersionUID = -8842235631963721948L;
	@Id private CandlesId id;
	
	@Transient
	private String market,timestamp;
	private String unit;
	@JsonProperty("candle_date_time_utc")
	@Column(name ="candle_date_time_utc")
	private String candleDateTimeUtc;
	@JsonProperty("candle_date_time_kst")
	@Column(name ="candle_date_time_kst")
	private String candleDateTimeKst;
	@JsonProperty("opening_price")
	@Column(name ="opening_price")
	private String openingPrice;
	@JsonProperty("high_price")
	@Column(name ="high_price")
	private String highPrice;
	@JsonProperty("low_price")
	@Column(name ="low_price")
	private String lowPrice;
	@JsonProperty("trade_price")
	@Column(name ="trade_price")
	private String tradePrice;
	@JsonProperty("candle_acc_trade_price")
	@Column(name ="candle_acc_trade_price")
	private String candleAccTradePrice;
	@JsonProperty("candle_acc_trade_volume")
	@Column(name ="candle_acc_trade_volume")
	private String candleAccTradeVolume;
	@JsonProperty("prev_closing_price")
	@Column(name ="prev_closing_price")
	private String prevClosingPrice;
	@JsonProperty("change_price")
	@Column(name ="change_price")
	private String changePrice;
	@JsonProperty("change_rate")
	@Column(name ="change_rate")
	private String changeRate;
	
	public void setId(CandlesId id) {
		this.id = id;
		if(StringUtils.hasText(unit) == false) {
			this.unit = "days";
		}
	}
	
}