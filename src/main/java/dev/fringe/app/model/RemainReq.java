package dev.fringe.app.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "remain")
public class RemainReq implements Serializable{

	private static final long serialVersionUID = 7424395225185590017L;
	@Id
	private RemainReqId id;
	private String minute;
	private String sequence;
	
	public RemainReq(String value) {
		if(id == null) {
			this.id = new RemainReqId(value.split(";")[0].split("=")[1]);
		}
		this.minute = value.split(";")[1].split("=")[1];
		this.sequence = value.split(";")[2].split("=")[1];

	}
}
