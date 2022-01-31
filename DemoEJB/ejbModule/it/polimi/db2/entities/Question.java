package it.polimi.db2.entities;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Table;

/**
 * The persistent class for the Question database table.
 * 
 */
@Entity
@Table(name = "question", schema = "gamified_marketing_db")
public class Question implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String text;

	@ManyToOne
	@JoinColumn(name = "id_questionnaire")
	private Questionnaire questionnaire;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "answer", schema = "gamified_marketing_db", joinColumns = @JoinColumn(name = "id_question"))
	@MapKeyJoinColumn(name = "id_record")
	@Column(name = "text")
	private Map<Record, String> answers;

	public Question() {

	}

	public Question(String text, Questionnaire questionnaire) {
		super();
		this.text = text;
		this.questionnaire = questionnaire;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}
}