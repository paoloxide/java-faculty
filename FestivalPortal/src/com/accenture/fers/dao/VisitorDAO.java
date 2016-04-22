package com.accenture.fers.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.accenture.fers.entity.Event;
import com.accenture.fers.entity.Visitor;

@Component
public class VisitorDAO {

	private static final Logger LOGGER = Logger.getLogger(VisitorDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public VisitorDAO() {

	}

	@Transactional
	public boolean insertData(Visitor visitor) throws NoResultException,
			PersistenceException {
		LOGGER.info("Insert Data");
		boolean flag = false;
		boolean userFound = false;
		Query userNameQuery = entityManager
				.createQuery("select v.userName from Visitor v");
		List<String> usernameLst = (ArrayList<String>) userNameQuery
				.getResultList();
		for (String name : usernameLst) {
			if (name.equals(visitor.getUserName())) {
				userFound = true;
				break;
			}
		}
		if (userFound == false) {
			entityManager.merge(visitor);
			flag = true;
		}

		return flag;
	}

	public Visitor searchUser(String username, String password)
			throws NonUniqueResultException, PersistenceException,NoResultException {

		Visitor visitor = null;
		Query query = entityManager
				.createQuery("SELECT v from Visitor v where v.userName=:username and v.password=:password");
		query.setParameter("username", username);
		query.setParameter("password", password);

		try {
			visitor = (Visitor) query.getSingleResult();
		}catch (NoResultException exception) {
			LOGGER.info("No rows found",exception);
			throw new NoResultException("No rows found");
		}

		return visitor;
	}

	@Transactional
	public void registerVisitorToEvent(Visitor visitor, int eventid)
			throws NoResultException, NonUniqueResultException,
			PersistenceException {
		Query query = entityManager
				.createQuery("SELECT e from Event e where e.eventid=:eventid");
		query.setParameter("eventid", eventid);
		Event event = (Event) query.getSingleResult();

		/** Fetch existing registered events of this visitor */

		Visitor v = entityManager.find(Visitor.class, visitor.getVisitorId());

		Set<Event> resgisteredEvents = v.getRegisteredEvents();
		resgisteredEvents.add(event);

		v.setRegisteredEvents(resgisteredEvents);
		entityManager.merge(v);
	}

	public List<Object[]> registeredEvents(Visitor visitor)
			throws NoResultException, PersistenceException {
		Query query = entityManager
				.createNativeQuery(
						"SELECT E1.eventid, E1.name, E1.description, E1.places, E1.duration, E1.eventtype, E2.signupid "
								+ "   FROM Event E1, eventsignup E2 "
								+ "   WHERE E1.eventid = E2.eventid AND E2.visitorid = ? ORDER BY E2.signupid DESC");
		query.setParameter(1, visitor.getVisitorId());
		return (ArrayList<Object[]>) query.getResultList();
	}

	@Transactional
	public int updateVisitor(Visitor visitor) throws PersistenceException {
		int rows = 0;

		Query query = entityManager
				.createQuery("Update Visitor v set v.firstName=:firstname, v.lastName=:lastname, "
						+ " v.userName=:username, v.email=:email,"
						+ " v.phoneNumber=:phonenumber, v.address=:address "
						+ " where v.visitorId=:visitorid  ");
		query.setParameter("firstname", visitor.getFirstName());
		query.setParameter("lastname", visitor.getLastName());
		query.setParameter("username", visitor.getUserName());
		query.setParameter("email", visitor.getEmail());
		query.setParameter("phonenumber", visitor.getPhoneNumber());
		query.setParameter("address", visitor.getAddress());
		query.setParameter("visitorid", visitor.getVisitorId());
		rows = query.executeUpdate();

		if (rows == 0) {
			throw new PersistenceException("Update visitor failed");
		}
		return rows;

	}

	@Transactional
	public int changePassword(Visitor visitor) throws PersistenceException {
		int flag = 0;

		if (matchWithOldPwd(visitor)) {
			flag = 0;

		} else {
			if (visitor.getPassword() != null && visitor.getVisitorId() != 0) {
				Query query = entityManager
						.createQuery("UPDATE Visitor v SET v.password = :password where v.visitorId = :visitorid ");
				query.setParameter("password", visitor.getPassword());
				query.setParameter("visitorid", visitor.getVisitorId());
				flag = query.executeUpdate();
			}

		}

		return flag;
	}

	private boolean matchWithOldPwd(Visitor visitor)
			throws NonUniqueResultException, PersistenceException {
		boolean passwordMatch = false;

		Query query = entityManager
				.createQuery("SELECT v.password FROM Visitor v where v.visitorId = :visitorid");
		query.setParameter("visitorid", visitor.getVisitorId());
		String password = (String) query.getSingleResult();

		if (password.equalsIgnoreCase(visitor.getPassword())) {
			passwordMatch = true;
		}

		return passwordMatch;

	}

	@Transactional
	public void unregisterEvent(Visitor visitor, int eventid)
			throws NonUniqueResultException, PersistenceException {

		Query query = entityManager
				.createQuery("SELECT e from Event e where e.eventid=:eventid");
		query.setParameter("eventid", eventid);
		Event event = (Event) query.getSingleResult();

		/** Fetch existing registered events of this visitor */

		Visitor v = entityManager.find(Visitor.class, visitor.getVisitorId());

		Set<Event> resgisteredEvents = v.getRegisteredEvents();
		resgisteredEvents.remove(event);

		v.setRegisteredEvents(resgisteredEvents);
		entityManager.merge(v);



	}

	public boolean searchVisitor(String username) {
		boolean flag = false;
		Query query = entityManager
				.createQuery("SELECT count(*) from Visitor v where v.userName = :username");
		query.setParameter("username", username);

		Long count = (Long) query.getSingleResult();

		if (count == 1) {
			flag = true;
		}

		return flag;

	}
}
