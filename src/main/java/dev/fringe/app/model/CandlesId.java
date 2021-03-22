package dev.fringe.app.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class CandlesId implements Serializable{
	private static final long serialVersionUID = 3407375558098280715L;
	private String market;
	private String candleDateTimeUtc;
}