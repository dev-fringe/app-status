package dev.fringe.app.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class RemainReqId implements Serializable{
	
	private static final long serialVersionUID = 3407375558098280715L;
	private String limitdate;
	private String limitgroup;
	
	public RemainReqId() {
		super();
	}
	public RemainReqId(String value) {
		this.limitdate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
		this.limitgroup = value;
	}
}