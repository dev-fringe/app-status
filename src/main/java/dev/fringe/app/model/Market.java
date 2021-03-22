package dev.fringe.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Market {
	private String market;
	@JsonProperty("korean_name")
	private String koreanName;
	@JsonProperty("english_name")
	private String englishName;
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public String getKoreanName() {
		return koreanName;
	}
	public void setKoreanName(String koreanName) {
		this.koreanName = koreanName;
	}
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	@Override
	public String toString() {
		return "Market [market=" + market + ", koreanName=" + koreanName + ", englishName=" + englishName + "]";
	}
}
