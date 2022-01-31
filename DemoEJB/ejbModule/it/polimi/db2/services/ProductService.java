package it.polimi.db2.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import it.polimi.db2.entities.Product;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Stateless
public class ProductService {
	@PersistenceContext(unitName = "DemoEJB")
	private EntityManager em;

	public ProductService() {
	}

	public Product getProductbyDate(Date date) {
		try {
			return em.createNamedQuery("Product.getProductByDate", Product.class).setParameter(1, date)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Product getProductOfTheDay() {
		return this.getProductbyDate(new Date());
	}

	public List<Product> getTheProdcutList() {
		return em.createNamedQuery("Product.getAllProducts", Product.class).getResultList();
	}

	public Product getProductById(int id) {
		return em.find(Product.class, id);
	}
}