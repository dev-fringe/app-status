package dev.fringe.app.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class BarsId implements Serializable{
	private static final long serialVersionUID = 6364949544116724531L;
	private String date;
	private String name;
	
	
}
