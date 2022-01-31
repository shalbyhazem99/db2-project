package it.polimi.db2.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.entities.User;
import it.polimi.db2.exceptions.CredentialsException;
import it.polimi.db2.exceptions.UpdateProfileException;

import java.util.List;

@Stateless
public class UserService {
	@PersistenceContext(unitName = "DemoEJB")
	private EntityManager em;

	public UserService() {
	}

	public User checkCredentials(String usrn, String pwd) throws CredentialsException, NonUniqueResultException {
		List<User> uList = null;
		try {
			uList = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, usrn).setParameter(2, pwd)
					.getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentals");
		}
		if (uList.isEmpty())
			return null;
		else if (uList.size() == 1)
			return uList.get(0);
		throw new NonUniqueResultException("More than one user registered with same credentials");

	}

	public User registerUser(String username, String password, String email, boolean active, boolean admin) throws CredentialsException {
		Long uCount;
		try {
			uCount = em.createNamedQuery("User.count", Long.class).setParameter(1, username)
					.getSingleResult();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not conclude registration");
		}
		if (uCount == 0) {
			User user = new User(username, password, email, active, admin);
			em.persist(user);
			return user;
		}
		else
			throw new CredentialsException("User with the same credential already registred");
	}
	
	public void banUser(User user) {
        user.setActive(false);
        em.merge(user);
	}
}
