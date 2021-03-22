package dev.fringe.app.model;

public class Ticks {

	private String market;
	private String trade_date_utc;
	private String trade_time_utc;
	private String timestamp;
	private String trade_price;
	private String trade_volume;
	private String prev_closing_price;
	private String chane_price;
	private String ask_bid;
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public String getTrade_date_utc() {
		return trade_date_utc;
	}
	public void setTrade_date_utc(String trade_date_utc) {
		this.trade_date_utc = trade_date_utc;
	}
	public String getTrade_time_utc() {
		return trade_time_utc;
	}
	public void setTrade_time_utc(String trade_time_utc) {
		this.trade_time_utc = trade_time_utc;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getTrade_price() {
		return trade_price;
	}
	public void setTrade_price(String trade_price) {
		this.trade_price = trade_price;
	}
	public String getTrade_volume() {
		return trade_volume;
	}
	public void setTrade_volume(String trade_volume) {
		this.trade_volume = trade_volume;
	}
	public String getPrev_closing_price() {
		return prev_closing_price;
	}
	public void setPrev_closing_price(String prev_closing_price) {
		this.prev_closing_price = prev_closing_price;
	}
	public String getChane_price() {
		return chane_price;
	}
	public void setChane_price(String chane_price) {
		this.chane_price = chane_price;
	}
	public String getAsk_bid() {
		return ask_bid;
	}
	public void setAsk_bid(String ask_bid) {
		this.ask_bid = ask_bid;
	}
	@Override
	public String toString() {
		return "Ticks [market=" + market + ", trade_date_utc=" + trade_date_utc + ", trade_time_utc=" + trade_time_utc
				+ ", timestamp=" + timestamp + ", trade_price=" + trade_price + ", trade_volume=" + trade_volume
				+ ", prev_closing_price=" + prev_closing_price + ", chane_price=" + chane_price + ", ask_bid=" + ask_bid
				+ "]";
	}
	
	
//    "market": "KRW-BTC",
//    "trade_date_utc": "2018-04-18",
//    "trade_time_utc": "10:19:58",
//    "timestamp": 1524046798000,
//    "trade_price": 8616000,
//    "trade_volume": 0.03060688,
//    "prev_closing_price": 8450000,
//    "chane_price": 166000,
//    "ask_bid": "ASK"
    	
}
