package dev.fringe.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Ticks {

	private String market;
	@JsonProperty("trade_date_utc")
	private String tradeDateUtc;
	@JsonProperty("trade_time_utc")
	private String tradeTimeUtc;
	private String timestamp;
	@JsonProperty("trade_price")
	private String tradeprice;
	@JsonProperty("trade_volume")
	private String tradevolume;
	@JsonProperty("prev_closing_price")
	private String prevClosingPrice;
	@JsonProperty("chane_price")
	private String chanePrice;
	@JsonProperty("ask_bid")
	private String askBid;
}
