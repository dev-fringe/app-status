package dev.fringe.app.model;

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
	private String marketCode;

	@JsonProperty("korean_name")
	private String koreanName;
	@JsonProperty("english_name")
	private String englishName;
	
}
