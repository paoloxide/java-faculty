package com.accenture.fers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.accenture.fers.dao.VisitorDAO;
import com.accenture.fers.entity.Event;
import com.accenture.fers.entity.Visitor;

/**
 * JUNIT test class for VisitorDAO class
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
public class TestVisitorDAO {

	/** Adding @Autowired to inject VisitorDAO instance */
	@Autowired
	private VisitorDAO visitorDAO;

	@PersistenceContext
	EntityManager entityManager;

	private Visitor visitor;
	private ArrayList<Event> registeredEvents;

	/**
	 * Setting up initial objects
	 *
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		visitor = new Visitor();
		registeredEvents = new ArrayList<Event>();
	}

	/**
	 * Deallocating objects after execution of every method
	 *
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		visitor = null;
		registeredEvents = null;
	}

	public void insertData() {

		visitor.setUserName("TestVisitor");
		visitor.setFirstName("TestVFname");
		visitor.setLastName("TestVLname");
		visitor.setPassword("ttt");
		visitor.setPhoneNumber("2344");
		visitor.setAddress("TestPlace");
		assertTrue(visitorDAO.insertData(visitor));

	}

	public void registerVisitorToEvent() {
		try {
			insertData();
			visitor = visitorDAO.searchUser("TestVisitor", "ttt");
			visitorDAO.registerVisitorToEvent(visitor, 1002);

		} catch (Exception exception) {
			fail("Exception !!!");
		}
	}

	/**
	 * Positive Test case for method insertData
	 */
	@Test
	public void testInsertData_Positive() {

		visitor.setUserName("TestVisitor");
		visitor.setFirstName("TestVFname");
		visitor.setLastName("TestVLname");
		visitor.setPassword("ttt");
		visitor.setPhoneNumber("2344");
		visitor.setAddress("TestPlace");
		assertTrue(visitorDAO.insertData(visitor));

	}

	/**
	 * Negative Test case for method insertData
	 */
	@Test
	public void testInsertData_Negative() {

		insertData();
		visitor.setUserName("TestVisitor");
		visitor.setFirstName("TestVFname");
		visitor.setLastName("TestVLname");
		visitor.setPassword("ttt");
		visitor.setPhoneNumber("2344");
		visitor.setAddress("TestPlace");
		assertFalse(visitorDAO.insertData(visitor));

	}

	/**
	 * Positive Test case for method searchUser
	 */
	@Test
	public void testSearchUser_Positive() {
		insertData();
		visitor = visitorDAO.searchUser("TestVisitor", "ttt");
		assertEquals(true, visitor.getUserName().equals("TestVisitor"));

	}

	/**
	 * Negative Test case for method searchUser
	 */
	@Test
	public void testSearchUser_Negative() {
		insertData();

		try {

			visitor = visitorDAO.searchUser("TestVisitor1", "ttt");
		} catch (NoResultException exception) {
			assertEquals("No rows found", exception.getMessage());
		}

	}

	/**
	 * Positive Test case for method registerVisitorToEvent
	 */
	//@Test
	public void testRegisterVisitorToEvent_Positive() {
		try {
			insertData();
			visitor = visitorDAO.searchUser("TestVisitor", "ttt");
			visitorDAO.registerVisitorToEvent(visitor, 1002);

		} catch (Exception exception) {
			exception.printStackTrace();
			fail("Exception !!!");
		}
	}

	/**
	 * Negative Test case for method registerVisitorToEvent
	 */
	@Test
	public void testRegisterVisitorToEvent_Negative() {
		try {
			insertData();
			visitor = visitorDAO.searchUser("bsmith", "password");
			visitorDAO.registerVisitorToEvent(visitor, 1001);

		} catch (Exception exception) {
			// YET TO BE COMPLETED
			assertTrue(true);
		}
	}

	/**
	 * Positive Test case for method registeredEvents
	 */

	//@Test
	public void testRegisteredEvents_Positive() {
		registerVisitorToEvent();
		List<Object[]> registeredEvents = new ArrayList<Object[]>();

		visitor = visitorDAO.searchUser("TestVisitor", "ttt");
		registeredEvents = visitorDAO.registeredEvents(visitor);

		assertTrue(registeredEvents.size() >= 1);
	}

	/**
	 * Negative Test case for method registeredEvents
	 */

	@Test
	public void testRegisteredEvents_Negative() {

		Visitor visitor = new Visitor();
		List<Object[]> registeredEvents = visitorDAO.registeredEvents(visitor);

		assertTrue(registeredEvents.size() == 0);
	}
	/**
	 * Positive Test case for method updateVisitor
	 */
	@Test
	public void testUpdateVisitor_Positive() {
		int updateStatus = 0;

		insertData();
		visitor = visitorDAO.searchUser("TestVisitor", "ttt");
		visitor.setFirstName("NewTestName");
		updateStatus = visitorDAO.updateVisitor(visitor);
		assertEquals(1, updateStatus);
	}

	/**
	 * Negative Test case for method updateVisitor
	 */
	@Test
	public void testUpdateVisitor_Negative() {
		int updateStatus = 0;

		Visitor v = new Visitor();
		try{
			updateStatus = visitorDAO.updateVisitor(visitor);
		}
		catch(PersistenceException e)
		{
			assertEquals("Update visitor failed", e.getMessage());
		}
		assertEquals(0, updateStatus);
	}

	/**
	 * Positive Test case for method UnregisterEvents
	 */

	//@Test
	public void testUnregisterEvent_Positive() {

		/*
		 * Can be tested in one of the two ways explained below. Uncomment Line1
		 * and Line2 and comment Line3 and Line 4 to test it in other way
		 */

		/*
		 * Visitor v = visitorDAO.searchUser("bsmith", "password"); //Line 1
		 * visitorDAO.registerVisitorToEvent(v, 1001); //Line 2
		 */

		registerVisitorToEvent(); // Line 3
		Visitor v = visitorDAO.searchUser("TestVisitor", "ttt"); // Line 4

		try {

			visitorDAO.unregisterEvent(v, 1002);
		} catch (Exception exception) {
			fail("Exception");
		}
	}

	/**
	 * Negative Test case for method UnregisterEvents
	 */

	//@Test
	public void testUnregisterEvent_Negative() {

		/*
		 * Can be tested in one of the two ways explained below. Uncomment Line1
		 * and Line2 and comment Line3 and Line 4 to test it in other way
		 */

		/*
		 * Visitor v = visitorDAO.searchUser("bsmith", "password"); //Line 1
		 * visitorDAO.registerVisitorToEvent(v, 1001); //Line 2
		 */

		registerVisitorToEvent(); // Line 3
		Visitor v = visitorDAO.searchUser("TestVisitor", "ttt"); // Line 4

		try {

			visitorDAO.unregisterEvent(v, 0000);
		} catch (NoResultException exception) {
			assertTrue(true);
		}

	}

	/**
	 * Positive Test case for method changePassword
	 */

	@Test
	public void testChangePassword_Positive() {
		Visitor v = new Visitor();
		v.setVisitorId(1001);
		v.setUserName("bsmith");
		v.setPassword("NewPassword");

		int flag = visitorDAO.changePassword(v);
		assertTrue(flag == 1);

	}

	/**
	 * Negative Test case for method changePassword
	 */

	@Test
	public void testChangePassword_Negative() {
		Visitor v = new Visitor();
		v.setVisitorId(1001);
		v.setUserName("bsmith");
		v.setPassword("Password");

		int flag = visitorDAO.changePassword(v);
		assertTrue(flag == 0);

	}

	/**
	 * Positive Test case for method searchVisitor
	 */

	@Test
	public void testSearchVisitor_Positive() {
		boolean flag = visitorDAO.searchVisitor("bsmith");
		assertTrue(flag);

	}

	/**
	 * Positive Test case for method searchVisitor
	 */

	@Test
	public void testSearchVisitor_Negative() {
		boolean flag = visitorDAO.searchVisitor("NonExistingUser");
		assertFalse(flag);

	}

}
