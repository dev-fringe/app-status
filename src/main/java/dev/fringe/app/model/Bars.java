package dev.fringe.app.model;

import java.io.Serializable;
import java.sql.ResultSet;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
@Table
public class Bars implements Serializable{
	private static final long serialVersionUID = 1746036454594292856L;
	private String open,high,low,close,volume;
	@Id private BarsId id;

	@SneakyThrows
	public Bars(String name, ResultSet rs) {
		if(id == null) {
			id = new BarsId();
		}
		id.setName(name);
		id.setDate(rs.getString(0 + 1));
		open = rs.getString(0 + 2);
		high = rs.getString(0 + 3);
		low = rs.getString(0 + 4);
		close = rs.getString(0 + 5);
		volume = rs.getString(0 + 6);
	}
	
}
