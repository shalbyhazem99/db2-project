package it.polimi.db2.entities;

import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "record", schema = "gamified_marketing_db")
@NamedQuery(name = "Record.getRecordPerUserAndQuestionnaire", query = "SELECT r FROM Record r  WHERE r.user = ?1 and r.questionnaire = ?2")
public class Record {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int points;
	private int age;
	private boolean submit;
	private UserSex sex;
	private UserExpertiseLevel expertise_level;

	@ManyToOne
	@JoinColumn(name = "id_user")
	private User user;

	@ManyToOne
	@JoinColumn(name = "id_questionnaire")
	private Questionnaire questionnaire;

	// operation are always cascaded
	@ElementCollection
	@CollectionTable(name = "answer", schema = "gamified_marketing_db", joinColumns = @JoinColumn(name = "id_record"))
	@MapKeyJoinColumn(name = "id_question")
	@Column(name = "text")
	private Map<Question, String> answers;

	public Record() {
		super();
	}

	public Record(int age, UserSex sex, UserExpertiseLevel expertise_level, User user, Questionnaire questionnaire,
			Map<Question, String> answers, boolean submit) {
		super();
		this.age = age;
		this.sex = sex;
		this.expertise_level = expertise_level;
		this.user = user;
		this.questionnaire = questionnaire;
		this.answers = answers;
		this.submit = submit;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Map<Question, String> getAnswers() {
		return answers;
	}

	public void setAnswers(Map<Question, String> answers) {
		this.answers = answers;
	}

	public boolean isSubmit() {
		return submit;
	}

	public void setSubmit(boolean submit) {
		this.submit = submit;
	}
	
}