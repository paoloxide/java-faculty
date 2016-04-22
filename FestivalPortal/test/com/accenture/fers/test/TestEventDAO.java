package com.accenture.fers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.accenture.fers.dao.EventDAO;
import com.accenture.fers.dao.VisitorDAO;
import com.accenture.fers.entity.Event;
import com.accenture.fers.entity.Visitor;

/**
 * JUNIT test class for EventDAO class
 *
 */

/** Adding @ContextConfiguration for it to pick up context mappings */
@ContextConfiguration(locations = "classpath:/applicationContext.xml")
/** Adding @RunWith to indicate that the class should use Spring's JUnit facilities */
@RunWith(SpringJUnit4ClassRunner.class)
/** Adding @Transactional to use transaction capabilities without having to use begin, commit etc. */
@Transactional
/** Adding @Component to make TestEventDAO as a component and to initiate Spring Dependency Injection */
@Component
public class TestEventDAO {

	/** Adding @Autowired to inject EventDAO instance */
	@Autowired
	private EventDAO eventDao;

	@Autowired
	private VisitorDAO visitorDAO;

	/** Adding @PersistenceContext to inject EntityManager instance */
	@PersistenceContext
	private EntityManager entityManager;

	/** An ArrayList to hold returned events */
	ArrayList<Event> showAllEvents;

	/**
	 * Sets up the object(s) required in other methods
	 *
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		showAllEvents = new ArrayList<Event>();

	}

	/**
	 * Deallocate the resources after execution of each method
	 *
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		showAllEvents = null;

	}

	/**
	 * Positive test case to test the method showAllEvents
	 */
	@Test
	public void testShowAllEvents_Positive() {

		showAllEvents = (ArrayList<Event>) eventDao.showAllEvents();

		assertTrue(showAllEvents.size() > 0);
	}

	/**
	 * Test case to test the exception scenario in database
	 */
	@Test
	public void testShowAllEvents_Exception() {
		try {
			showAllEvents = (ArrayList<Event>) eventDao.showAllEvents();

		} catch (NoResultException exception) {
			assertEquals("No Results", exception.getMessage());
		}

	}

	/**
	 * JUNIT test case to test positive case for updateEventDeletions
	 */
	@Test
	public void testUpdateEventDeletions_Positive() {
		int eventid = 1001;
		int testSeatsAvailableBefore = 0;
		int testSeatsAvailableAfter = 0;

		Query q = entityManager
				.createQuery("SELECT e.seatsavailable FROM Event e WHERE e.eventid = ?1");
		q.setParameter(1, eventid);
		testSeatsAvailableBefore = (Integer) q.getSingleResult();

		eventDao.updateEventDeletions(eventid);

		Query q1 = entityManager
				.createQuery("SELECT e.seatsavailable FROM Event e WHERE e.eventid = ?1");
		q1.setParameter(1, eventid);
		testSeatsAvailableAfter = (Integer) q.getSingleResult();

		assertEquals(testSeatsAvailableBefore, testSeatsAvailableAfter - 1);

	}

	/**
	 * Negative test case for method updateEventDeletions
	 */
	@Test
	public void testUpdateEventDeletions_Negative() {

		int eventid = 2001;
		try {

			eventDao.updateEventDeletions(eventid);

		} catch (PersistenceException exception) {
			assertEquals("No updates for Event Deletions",
					exception.getMessage());
		}
	}

	/**
	 * Positive test case for method updateEventNominations
	 */
	@Test
	public void testUpdateEventNominations_Positive() {

		int eventid = 1001;
		int testSeatsAvailableBefore = 0;
		int testSeatsAvailableAfter = 0;

		Query q = entityManager
				.createQuery("SELECT e.seatsavailable FROM Event e WHERE e.eventid = ?1");
		q.setParameter(1, eventid);

		testSeatsAvailableBefore = (Integer) q.getSingleResult();

		eventDao.updateEventNominations(eventid);

		Query q1 = entityManager
				.createQuery("SELECT e.seatsavailable FROM Event e WHERE e.eventid = ?1");
		q1.setParameter(1, eventid);
		testSeatsAvailableAfter = (Integer) q.getSingleResult();

		assertEquals(testSeatsAvailableBefore, testSeatsAvailableAfter + 1);

	}

	/**
	 * Negative test case for method updateEventNominations
	 */
	@Test
	public void testUpdateEventNominations_Negative() {

		int eventid = 2001;
		try {
			eventDao.updateEventDeletions(eventid);

		}

		catch (PersistenceException exception) {
			assertEquals("No updates for Event Deletions",
					exception.getMessage());
		}

	}

	/**
	 * Positive test case for method checkEventsofVisitor. It means that user is
	 * not already registered for this event. So, checkEventsOfVisitor should
	 * return false.
	 */
	@Test
	public void testCheckEventsOfVisitor_Positive() {
		int eventid = 1001;

		Visitor visitor = new Visitor();
		visitor.setUserName("TestVisitor");
		visitor.setFirstName("TestVFname1");
		visitor.setLastName("TestVLname1");
		visitor.setPassword("ttt");
		visitor.setPhoneNumber("2344");
		visitor.setAddress("TestPlace");
		visitor.setVisitorId(17);
		boolean status = eventDao.checkEventsofVisitor(visitor, eventid);
		assertFalse(status);

	}

	/**
	 * Negative test case for method checkEventsofVisitor. It means user is
	 * already registered for this event. So, checkEventsOfVisitor should return
	 * true.
	 */
	@Test
	public void testCheckEventsOfVisitor_Negative() {
		int eventid = 1001;
		Visitor visitor = entityManager.find(Visitor.class, 1001);
		visitorDAO.registerVisitorToEvent(visitor, eventid);

		boolean status = eventDao.checkEventsofVisitor(visitor, eventid);
		assertTrue(status);

	}
}
