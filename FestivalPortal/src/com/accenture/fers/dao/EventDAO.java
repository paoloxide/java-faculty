package com.accenture.fers.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
public class EventDAO {

	private static final Logger LOGGER = Logger.getLogger(EventDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public EventDAO() {

	}

	public List<Event> showAllEvents() throws NoResultException,
			PersistenceException {
		Query query = entityManager.createQuery("SELECT e from Event e");
		List eventLst = (ArrayList<Event>) query.getResultList();
		if(eventLst.isEmpty()){
			LOGGER.info("Test: No events");

			throw new NoResultException("No events");
		}

		return eventLst;
	}

	@Transactional
	public void updateEventNominations(int eventid) throws PersistenceException {
		Query query = entityManager
				.createQuery("UPDATE Event e SET e.seatsavailable = e.seatsavailable - 1 WHERE e.eventid=:eventid");
		query.setParameter("eventid", eventid);
		int rows = query.executeUpdate();
		if(rows == 0){
			throw new PersistenceException("No updates for Event Nominations");
		}
	}

	public boolean checkEventsofVisitor(Visitor visitor, int eventid)
			throws NonUniqueResultException, PersistenceException {
		boolean flag = false;
		Query q = entityManager
				.createNativeQuery("SELECT COUNT(eventsignup.signupid) AS EVENTCOUNT FROM eventsignup WHERE eventid=:eventid AND visitorid=:visitorid ");
		q.setParameter("eventid", eventid);
		q.setParameter("visitorid", visitor.getVisitorId());
		BigInteger eventCount = (BigInteger) q.getSingleResult();
		if (eventCount.intValue() >= 1) {
			flag = true;
		}

		if(eventCount.intValue() == 0){
			flag = false;
		}

		return flag;
	}

	@Transactional
	public void updateEventDeletions(int eventid) throws PersistenceException {
		Query query = entityManager
				.createQuery("UPDATE Event e SET e.seatsavailable = e.seatsavailable + 1 WHERE e.eventid=:eventid");
		query.setParameter("eventid", eventid);
	int rows = 	query.executeUpdate();

	if(rows == 0){
		throw new PersistenceException("No updates for Event Deletions");
	}

	}

}
