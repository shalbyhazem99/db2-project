package it.polimi.db2.entities;

import java.io.Serializable;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the Product database table.
 * 
 */
@Entity
@Table(name = "product", schema = "gamified_marketing_db")
@NamedQueries({
		@NamedQuery(name = "Product.getProductByDate", query = "SELECT p FROM Product p, Questionnaire q where q.date = ?1 and p= q.product"),
		@NamedQuery(name = "Product.getAllProducts", query = "SELECT p FROM Product p") })
public class Product implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	@Basic(fetch = FetchType.EAGER)
	@Lob
	private byte[] image;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "review", schema = "gamified_marketing_db", joinColumns = @JoinColumn(name = "id_product"))
	@MapKeyJoinColumn(name = "id_user")
	@Column(name = "text")
	private Map<User, String> reviews;

	@OneToMany(mappedBy = "product")
	private List<Questionnaire> questionnaires;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Map<User, String> getReviews() {
		return reviews;
	}

	public void setReviews(Map<User, String> reviews) {
		this.reviews = reviews;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getImageData() {
		return Base64.getMimeEncoder().encodeToString(image);
	}
}
