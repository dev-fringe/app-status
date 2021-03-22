package dev.fringe.app.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.SneakyThrows;

@Data
@Entity
@Table
public class Trades {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@Column(name = "uuid", unique = true)
	private String uuid;
	
	private String market;
	private String timestamp;
	private String price;
	private String amount;
	
	@Transient
	private String date;
	
	public Trades() {
	}
	
	@SneakyThrows
	public Trades(String name, ResultSet rs) {
		this.setMarket(name);
		this.setTimestamp(rs.getString(0 + 1));
		this.setPrice(rs.getString(0 + 2));
		this.setAmount(rs.getString(0 + 3));
        Timestamp ts=new Timestamp(Long.parseLong(this.getTimestamp()));  
        Date date=new Date(ts.getTime());
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(date);
	}

	
}
