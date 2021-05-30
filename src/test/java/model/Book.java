package model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement(name = "book")
@Data
public class Book {
    private Long id;
    private String name;
    private String author;
    private Date date;
}