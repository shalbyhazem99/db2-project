package it.polimi.db2.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the Questionnaire database table.
 * 
 */
@Entity
@Table(name = "questionnaire", schema = "gamified_marketing_db")
@NamedQueries({
		@NamedQuery(name = "Questionnaire.getQuestionnaireOfToday", query = "SELECT q FROM Questionnaire q where q.date = current_date"),
		@NamedQuery(name = "Questionnaire.getQuestionnaireByDate", query = "SELECT q FROM Questionnaire q where q.date = ?1"),
		@NamedQuery(name = "Questionnaire.eraseQuestionnaireByDate", query = "Delete FROM Questionnaire q where q.date = ?1") })
public class Questionnaire {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Temporal(TemporalType.DATE)
	private Date date;

	@ManyToOne
	@JoinColumn(name = "id_product")
	private Product product;

	@OneToMany(mappedBy = "questionnaire", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private List<Question> questions;

	@OneToMany(mappedBy = "questionnaire", cascade = { CascadeType.REMOVE, CascadeType.REFRESH })
	private List<Record> records;

	public Questionnaire() {

	}

	public Questionnaire(Date date, Product product) {
		super();
		this.date = date;
		this.product = product;
		questions = new ArrayList<Question>();
	}

	public void addQuestion(Question question) {
		this.questions.add(question);
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public List<Record> getRecords() {
		return records;
	}

	public void setRecords(List<Record> records) {
		this.records = records;
	}

}