package dev.fringe.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table
public class Account {
		@Id 
		private String balance;
		private String currency,locked;
		@Column(name = "avg_buy_price")
		@JsonProperty("avg_buy_price")
		private String avgBuyPrice;
		@Column(name = "avg_buy_price_modified")
		@JsonProperty("avg_buy_price_modified")
		private Boolean avgBuyPriceModified;
		@Column(name = "unit_currency")
		@JsonProperty("unit_currency")
		private String unitCurrency;
}
