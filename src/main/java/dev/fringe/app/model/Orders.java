package dev.fringe.app.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "uuid", "side", "ord_type", "price", "state", "market", "created_at", "volume", "remaining_volume",
		"reserved_fee", "remaining_fee", "paid_fee", "locked", "executed_volume", "trades_count" })
@Data
public class Orders {

	@JsonProperty("uuid")
	private String uuid;
	@JsonProperty("side")
	private String side;
	@JsonProperty("ord_type")
	private String ordType;
	@JsonProperty("price")
	private BigDecimal price;
	@JsonProperty("state")
	private String state;
	@JsonProperty("market")
	private String market;
	@JsonProperty("created_at")
	private String createdAt;
	@JsonProperty("volume")
	private BigDecimal volume;
	@JsonProperty("remaining_volume")
	private String remainingVolume;
	@JsonProperty("reserved_fee")
	private String reservedFee;
	@JsonProperty("remaining_fee")
	private String remainingFee;
	@JsonProperty("paid_fee")
	private String paidFee;
	@JsonProperty("locked")
	private String locked;
	@JsonProperty("executed_volume")
	private String executedVolume;
	@JsonProperty("trades_count")
	private Integer tradesCount;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}