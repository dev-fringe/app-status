package dev.fringe.app.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class CandlesId implements Serializable{
	
	private static final long serialVersionUID = 3407375558098280715L;
	private String id,market,timestamp;
	
	public CandlesId() {
		super();
	}
	public CandlesId(Candles candles) {
		this.market = candles.getMarket();
		this.timestamp = candles.getTimestamp();
		this.id = UUID.randomUUID().toString();
	}
}