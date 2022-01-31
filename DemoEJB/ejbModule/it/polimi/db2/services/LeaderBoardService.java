package it.polimi.db2.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.Questionnaire;
import it.polimi.db2.utils.LeaderBoard;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Stateless
public class LeaderBoardService {
	@PersistenceContext(unitName = "DemoEJB")
	private EntityManager em;

	public LeaderBoardService() {
	}

	public List<LeaderBoard> getLeaderBoardOfTheDay() {
		return em.createNamedQuery("User.getLeaderBoard", LeaderBoard.class).getResultList();
	}
}
