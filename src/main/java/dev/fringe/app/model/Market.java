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
public class Market {
	@Id
	private String market;
	private String money;
	@Column(name = "market_code")
	private String marketCode;
	@JsonProperty("korean_name")
	@Column(name = "korean_name")
	private String koreanName;
	@JsonProperty("english_name")
	@Column(name = "english_name")
	private String englishName;
	
}
