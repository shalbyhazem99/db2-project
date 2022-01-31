package it.polimi.db2.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Log
 *
 */
@Entity
@Table(name = "log", schema = "gamified_marketing_db")
public class Log implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	@ManyToOne
	@JoinColumn(name = "id_user")
	private User user;
	
	
	public Log() {
		super();
	}


	public Log(Date date, User user) {
		super();
		this.date = date;
		this.user = user;
	}
	
	
	
}