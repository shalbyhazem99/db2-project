package it.polimi.db2.services;

import java.util.Arrays;
import java.util.Date;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.Question;
import it.polimi.db2.entities.Questionnaire;
import it.polimi.db2.exceptions.DeletionAfterCurrentDateException;

@Stateless
public class QuestionnaireService {
	@PersistenceContext(unitName = "DemoEJB")
	private EntityManager em;

	public QuestionnaireService() {
	}

	public Questionnaire getQuestionnaireOfTheDay() {
		return this.getQuestionnaireByDate(new Date());
	}

	public Questionnaire getQuestionnaireByDate(Date date) {
		try {
			return em.createNamedQuery("Questionnaire.getQuestionnaireByDate", Questionnaire.class)
					.setParameter(1, date).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public void addQuestionnaire(Date date, Product product, String[] questions) {
		Questionnaire questionnaire = new Questionnaire(date, product);
		Arrays.stream(questions).forEach(elem -> questionnaire.addQuestion(new Question(elem, questionnaire)));
		em.persist(questionnaire);
	}

	public void eraseQuestionnaire(Date date) throws DeletionAfterCurrentDateException {
		Date currentDate = new Date();
		if (date.before(currentDate)) {
			em.createNamedQuery("Questionnaire.eraseQuestionnaireByDate").setParameter(1, date).executeUpdate();
		} else {
			throw new DeletionAfterCurrentDateException("Deletion after current date not permitted");
		}
	}

}
