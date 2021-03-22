package dev.fringe.app.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table
public class Ticks implements Serializable {

	private static final long serialVersionUID = 7424871185790325023L;
	
	@Id
	private TicksId id;
	
	@JsonProperty("sequential_id")
	@Column(name="sequential_id")
	@Transient
	private String sequentialId;
	@Transient
	private String market;
	@JsonProperty("trade_date_utc")
	@Column(name="trade_date_utc")
	private String tradeDateUtc;
	@JsonProperty("trade_time_utc")
	@Column(name="trade_time_utc")
	private String tradeTimeUtc;
	private String timestamp;
	@JsonProperty("trade_price")
	@Column(name="trade_price")
	private String tradePrice;
	@JsonProperty("trade_volume")
	@Column(name="trade_volume")
	private String tradeVolume;
	@JsonProperty("prev_closing_price")
	@Column(name="prev_closing_price")
	private String prevClosingPrice;
	@JsonProperty("change_price")
	@Column(name="change_price")
	private String changePrice;
	@JsonProperty("ask_bid")
	@Column(name="ask_bid")
	private String askBid;
	@JsonIgnore
	@Column(name="trade_date_time_gmt9")
	private String tradeDateTimeGmt9;
	
	public void setTradeDateTimeGmt9() {
		Instant.ofEpochMilli(Long.parseLong(this.timestamp)).atZone(ZoneId.systemDefault());
		ZonedDateTime date = Instant.ofEpochMilli(Long.parseLong(this.timestamp)).atZone(ZoneId.systemDefault());
		this.tradeDateTimeGmt9 = date.toString();
	}

	public void setId(Ticks tick) {
		if(id == null) {
			this.id = new TicksId();
		}
		id.setMarket(tick.getMarket());
		id.setSequentialId(tick.getSequentialId());
	}
	
}
