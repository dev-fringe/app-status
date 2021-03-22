package dev.fringe.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table
public class Wallet {
		@Id
		@JsonProperty("block_updated_at")
		private String blockUpdatedAt;
		@JsonProperty("currency")
		private String currency;
		@JsonProperty("wallet_state")
		private String walletState;
		@JsonProperty("block_state")
		private String blockState;
		@JsonProperty("block_height")
		private Integer blockHeight;
		@JsonProperty("block_elapsed_minutes")
		private String blockElapsedMinutes;
}
