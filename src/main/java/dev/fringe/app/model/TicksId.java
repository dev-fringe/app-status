package dev.fringe.app.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Embeddable
public class TicksId implements Serializable{

	private static final long serialVersionUID = 492630530287774503L;
	
	@JsonProperty("sequential_id")
	@Column(name="sequential_id")
	private String sequentialId;
	private String market;
	
	
	
}
