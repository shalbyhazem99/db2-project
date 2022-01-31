package it.polimi.db2.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: user
 *
 */
@Entity
@Table(name = "user", schema = "gamified_marketing_db")
@NamedQueries({
		@NamedQuery(name = "User.checkCredentials", query = "SELECT u FROM User u  WHERE u.username = ?1 and u.password = ?2"),
		@NamedQuery(name = "User.count", query = "SELECT count(u) FROM User u  WHERE u.username = ?1"),
		@NamedQuery(name = "User.getLeaderBoard", query = "SELECT NEW it.polimi.db2.utils.LeaderBoard(u.username, sum(r.points)) FROM User u, Record r  WHERE u = r.user AND r.questionnaire = (SELECT q FROM Questionnaire q where q.date = current_date ) GROUP BY u.id ORDER BY r.points DESC")})
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String username;
	private String password;
	private String email;
	private boolean active;
	private boolean admin;

	@OneToMany(mappedBy = "user")
	private List<Log> log_list;
	
	@OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE,CascadeType.REFRESH,CascadeType.MERGE}, fetch = FetchType.LAZY)
	private List<Record> records;
	
	@ElementCollection
	@CollectionTable(name = "review", schema = "gamified_marketing_db", joinColumns = @JoinColumn(name = "id_user"))
	@MapKeyJoinColumn(name = "id_product")
	@Column(name = "text")
	private Map<Product,String> reviews;
	
	public User() {
		
	}
		
	public User(String username, String password, String email, boolean active, boolean admin) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.active = active;
		this.admin = admin;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
