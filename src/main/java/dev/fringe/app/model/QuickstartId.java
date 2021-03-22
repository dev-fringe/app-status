package dev.fringe.app.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class QuickstartId implements Serializable{

	private static final long serialVersionUID = -3431992536138151600L;
	private String market;
	private String datetime;
	
	public QuickstartId(String market) {
		this.market = market;
		this.datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ").format(new Date());
	}
}
