package it.polimi.db2.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.exception.ExceptionUtils;

import it.polimi.db2.entities.Log;
import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.Question;
import it.polimi.db2.entities.Questionnaire;
import it.polimi.db2.entities.Record;
import it.polimi.db2.entities.User;
import it.polimi.db2.entities.UserExpertiseLevel;
import it.polimi.db2.entities.UserSex;
import it.polimi.db2.exceptions.NonActiveUserException;
import it.polimi.db2.exceptions.NotValidEntryException;
import it.polimi.db2.exceptions.OffensiveLanguageException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class RecordService {
	@PersistenceContext(unitName = "DemoEJB")
	private EntityManager em;
	
	private static final String ERROR_OFFENSIVE = "45001";

	public RecordService() {
	}

	public boolean isThereAnyRecord(User user, Questionnaire questionnaire) {
		return em.createNamedQuery("Record.getRecordPerUserAndQuestionnaire", Questionnaire.class).setParameter(1, user)
				.setParameter(2, questionnaire).getResultList().size() != 0;
	}

	// age is -1 if not presence
	public void addNewRecord(String[] answers, int age, UserSex sex, UserExpertiseLevel user_expertise_level, User user, Questionnaire questionnaire, List<Question> questions) throws NotValidEntryException, NonActiveUserException, OffensiveLanguageException {
		if (user.isActive()) {
			if (questions.size() == answers.length) {
				Map<Question, String> temp = new HashMap();
				for (int i = 0; i < answers.length; i++) {
					temp.put(questions.get(i), answers[i]);
				}
				Record record = new Record(age, sex, user_expertise_level, user, questionnaire, temp, true);
				//todo add the record to the corresponding part
				em.persist(record);
				//to catch exeption in case of bad words
				 try {
				      em.flush();
				 }catch(Exception e) {
					 if(ExceptionUtils.getRootCause(e) instanceof SQLException) {
					                if (((SQLException) (ExceptionUtils.getRootCause(e))).getSQLState().equals(ERROR_OFFENSIVE)) {
			                    throw new OffensiveLanguageException("No way ! You cannot use this language");
			                }
					 }
				 }				
			} else {
				throw new NotValidEntryException("Answers inserted aren't valid");
			}
		} else {
			throw new NonActiveUserException("User not active");
		}
	}

	public void cancelQuestionnaire(User user, Questionnaire questionnaire) {
		Record record = new Record(-1, null, null, user, questionnaire, null, false);
		Log log = new Log(new Date(), user);
		em.persist(record);
		em.persist(log);
	}
}
