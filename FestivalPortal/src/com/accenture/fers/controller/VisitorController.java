package com.accenture.fers.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.accenture.fers.entity.Event;
import com.accenture.fers.entity.Visitor;
import com.accenture.fers.exceptions.FERSGenericException;
import com.accenture.fers.service.EventFacade;
import com.accenture.fers.service.VisitorFacade;

/**
 * Visitor controller handles all the visitor related transactions with the data
 * classes and triggered by visitormain.jsp
 */

@Controller
public class VisitorController {

	@Autowired
	VisitorFacade visitorServiceImpl;

	@Autowired
	EventFacade eventServiceImpl;

	private static final Logger LOGGER = Logger.getLogger(VisitorController.class);

	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             method will register new Visitor into FERS system by
	 *             accepting registration details and load into database
	 */
	@RequestMapping("/newVistor.htm")
	public ModelAndView newVisitor(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		LOGGER.info("For Client Demo");
		if (request == null || response == null) {
			LOGGER.info("Request or Response failed for NEWVISITOR METHOD..");
			throw new FERSGenericException(
					"Error in Transaction, Please re-Try. for more information check Logfile in C:\\FERSLOG folder",
					new NullPointerException());
		}
		String username = request.getParameter("USERNAME");
		String password = request.getParameter("PASSWORD");
		String firstname = request.getParameter("FIRSTNAME");
		String lastname = request.getParameter("LASTNAME");
		String email = request.getParameter("EMAIL");
		String phoneno = request.getParameter("PHONENO");
		String address = request.getParameter("ADDRESS");

		LOGGER.info("creating new visitor with UserName :" + username);

		Visitor visitor = new Visitor();
		visitor.setUserName(username);
		visitor.setPassword(password);
		visitor.setFirstName(firstname);
		visitor.setLastName(lastname);
		visitor.setEmail(email);
		visitor.setPhoneNumber(phoneno);
		visitor.setAddress(address);

		boolean insertStatus = visitorServiceImpl.createVisitor(visitor);

		ModelAndView mv = new ModelAndView();
		if (insertStatus == true) {
			mv.addObject("REGISTRATIONSTATUSMESSAGE",
					"User Registered Succesfully !!!");
			LOGGER.info("Succesfully created visitor " + username);
			mv.setViewName("/registration.jsp");
		} else {
			mv.addObject("REGISTRATIONSTATUSMESSAGE",
					"USERNAME already exists.. please register again with different USERNAME..");
			LOGGER.info("Username " + username
					+ " already exists and visitor creation failed..");
			mv.setViewName("/registration.jsp");
		}
		return mv;
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             method is for validating visitor in the Login page and
	 *             redirects to visitor homepage based on credentials
	 *             authentication and authorization. If authorization fails,
	 *             user will be redirected to Login page and error message is
	 *             printed on Login screen
	 */

	@RequestMapping("/searchVisitor.htm")
	public ModelAndView searchVisitor(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (request == null || response == null) {
			LOGGER.info("Request or Response failed for SEARCHVISITOR METHOD..");
			throw new FERSGenericException(
					"Error in Transaction, Please re-Try. for more information check Logfile in C:\\FERSLOG folder",
					new NullPointerException());
		}
		String username = request.getParameter("USERNAME");
		String password = request.getParameter("PASSWORD");
		HttpSession hs = request.getSession();
		if (hs.isNew()) {
			hs.setAttribute("USERNAME", username);
			hs.setAttribute("PASSWORD", password);
		} else {
			username = hs.getAttribute("USERNAME").toString();
			password = hs.getAttribute("PASSWORD").toString();
		}

		LOGGER.info("Logging into FERS using username :" + username
				+ " and password :" + password);

		Visitor visitor = new Visitor();
		ModelAndView mv = new ModelAndView();

		try{
		visitor = visitorServiceImpl.searchVisitor(username, password);
		}catch(NoResultException exception){
			LOGGER.error("Invalid Login",exception);
			mv.addObject("ERROR", "Invalid Username / Password.");
			mv.setViewName("/index.jsp");
			return mv;

		}

		if (visitor.getVisitorId() == 0) {
			mv.addObject("ERROR", "Invalid Username / Password.");
			mv.setViewName("/index.jsp");
			return mv;
		} else {

			LOGGER.info("Visitor details available for the username :" + username);

			List<Event> eventList = new ArrayList<Event>();
			eventList = eventServiceImpl.getAllEvents();

			LOGGER.info("All events listed for th visitor :" + eventList);

			List<Object[]> regList = new ArrayList<Object[]>();
			regList = visitorServiceImpl.showRegisteredEvents(visitor);

			LOGGER.info("All Registered events listed for the visitor :" + regList);

			HttpSession session = request.getSession();
			session.setAttribute("VISITOR", visitor);

			mv.addObject("visitor", visitor);
			mv.addObject("allEvents", eventList);
			mv.addObject("regEvents", regList);
			mv.setViewName("/visitormain.jsp");
			return mv;
		}
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             method is for validating visitor in the Login page and
	 *             redirects to visitor homepage based on credentials
	 *             authentication and authorization. If authorization fails,
	 *             user will be redirected to Login page and error message is
	 *             printed on Login screen
	 */

	@RequestMapping("/searchUsername.htm")
	public void searchUserName(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		boolean value=false;
		if (request == null || response == null) {
			LOGGER.info("Request or Response failed for SEARCHVISITOR METHOD..");
			throw new FERSGenericException(
					"Error in Transaction, Please re-Try. for more information check Logfile in C:\\FERSLOG folder",
					new NullPointerException());
		}
		String username = request.getParameter("userName");
		try{
		 value = visitorServiceImpl.searchVisitor(username);
		}catch(NoResultException exception){
			LOGGER.error("Invalid Login",exception);

		}
   if(value){
			response.getWriter().print("success");
   } else{
		response.getWriter().print("fail");

   }
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             method is used to register specific event by visitor and
	 *             maintains list of all the events visitor registers. if user
	 *             already registered for event, then show error message on page
	 *             about registration failure.
	 */

	@RequestMapping("/eventreg.htm")
	public ModelAndView registerVisitor(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (request == null || response == null) {
			LOGGER.info("Request or Response failed for REGISTERVISITOR METHOD..");
			throw new FERSGenericException(
					"Error in Transaction, Please re-Try. for more information check Logfile in C:\\FERSLOG folder",
					new NullPointerException());
		}

		HttpSession session = request.getSession();
		Visitor visitor = (Visitor) session.getAttribute("VISITOR");
		int eventid = Integer.parseInt(request.getParameter("eventId"));

		LOGGER.info("Visitor registered for the event :" + eventid);

		ModelAndView mv = new ModelAndView();

		boolean regStatus = eventServiceImpl.checkEventsofVisitor(visitor,
				eventid);

		LOGGER.info("Status of the visitor for the event :" + regStatus);

		if (regStatus == false) {
			visitorServiceImpl.registerVisitor(visitor, eventid);
			LOGGER.info("Visitor succesfully registed for event :" + eventid);
		} else {
			mv.addObject("RegError", "User already Registered for the EVENT !!");
		}

		List<Object[]> regList = new ArrayList<Object[]>();
		regList = visitorServiceImpl.showRegisteredEvents(visitor);

		List<Event> eventList = new ArrayList<Event>();

		eventList = eventServiceImpl.getAllEvents();

		mv.addObject("visitor", visitor);
		mv.addObject("allEvents", eventList);
		mv.addObject("regEvents", regList);
		mv.setViewName("/visitormain.jsp");
		return mv;

	}

	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 *             method will update existing details of the visitor and
	 *             requires logout and re-login for getting the details updated
	 *             in database.
	 */

	@RequestMapping("/updatevisitor.htm")
	public ModelAndView updateVisitor(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (request == null || response == null) {
			LOGGER.info("Request or Response failed for UPDATEVISITOR METHOD..");
			throw new FERSGenericException(
					"Error in Transaction, Please re-Try. for more information check Logfile in C:\\FERSLOG folder",
					new NullPointerException());
		}

		HttpSession session = request.getSession();
		Visitor visitor = (Visitor) session.getAttribute("VISITOR");

		LOGGER.info("Updating visitor details with VisitorID :"
				+ visitor.getVisitorId());

		String username = request.getParameter("username");
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String email = request.getParameter("email");
		String phoneno = request.getParameter("phoneno");
		String place = request.getParameter("address");

		visitor.setFirstName(firstname);
		visitor.setLastName(lastname);
		visitor.setUserName(username);
		visitor.setEmail(email);
		visitor.setPhoneNumber(phoneno);
		visitor.setAddress(place);

		int status = visitorServiceImpl.updateVisitorDetails(visitor);

		LOGGER.info("Number of Visitor records updated is :" + status);

		ModelAndView mv = new ModelAndView();

		if (status > 0) {
			mv.addObject("status", "success");
			mv.setViewName("/updatevisitor.jsp");
		} else {
			mv.addObject("updatestatus",
					"Error in updation.. Please Check fields and retry");
			mv.setViewName("/updatevisitor.jsp");
		}
		return mv;
	}

	@RequestMapping("/changePWD.htm")
	public ModelAndView changePassword(HttpServletRequest request,
			HttpServletResponse response) {
		int status = -1;

		HttpSession session = request.getSession();
		Visitor visitor = (Visitor) session.getAttribute("VISITOR");

		if (visitor != null) {
			LOGGER.info("Changing visitor password with VisitorID :"
					+ visitor.getVisitorId());

			String password = request.getParameter("password");

			if (password != null) {
				visitor.setPassword(password);

				try {
					status = visitorServiceImpl.changePassword(visitor);
				} catch (FERSGenericException e) {
					status = -5;
					LOGGER.error(e.getMessage(), e);
				}
			} else {
				LOGGER.error("Password cannot be blank");
			}

			LOGGER.info("Visitor password changed :" + status);
		} else {
			LOGGER.error("Visitor details are invalid");
		}

		ModelAndView mv = new ModelAndView();

		if (status > 0) {
			mv.addObject("status", "success");
			mv.setViewName("/changePWD.jsp");
		} else if (status == -5) {
			mv.addObject("status", "error");
			mv.addObject("errorMsg",
					"System error occurred, Please verify log file for more details");
			mv.setViewName("/changePWD.jsp");
		} else if (status == -10) {
			mv.addObject("status", "error");
			mv.addObject(
					"errorMsg",
					"New password must be different from current password, please choose a different password and retry");
			mv.setViewName("/changePWD.jsp");
		} else {
			mv.addObject("status", "error");
			mv.addObject(
					"errorMsg",
					"Error while changing password.. Please verify visitor and password details and retry again");
			mv.setViewName("/changePWD.jsp");
		}
		return mv;
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             method is to unregister event by the visitor and seats
	 *             allocated for that event will be released and updated into
	 *             database
	 */

	@RequestMapping("/eventunreg.htm")
	public ModelAndView unregisterEvent(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (request == null || response == null) {
			LOGGER.info("Request or Response failed for UNREGISTEREVENT METHOD..");
			throw new FERSGenericException(
					"Error in Transaction, Please re-Try. for more information check Logfile in C:\\FERSLOG folder",
					new NullPointerException());
		}

		HttpSession session = request.getSession();
		Visitor visitor = (Visitor) session.getAttribute("VISITOR");
		int eventid = Integer.parseInt(request.getParameter("eventId"));

		LOGGER.info("Unregistering for the event :" + eventid);

		visitorServiceImpl.unregisterEvent(visitor, eventid);

		List<Object[]> regList = new ArrayList<Object[]>();
		regList = visitorServiceImpl.showRegisteredEvents(visitor);

		List<Event> eventList = new ArrayList<Event>();

		eventServiceImpl.updateEventDeletions(eventid);

		LOGGER.info("Seats allocated for the event are released :" + eventid);

		eventList = eventServiceImpl.getAllEvents();

		ModelAndView mv = new ModelAndView();
		mv.addObject("visitor", visitor);
		mv.addObject("allEvents", eventList);
		mv.addObject("regEvents", regList);
		mv.setViewName("/visitormain.jsp");
		return mv;
	}


}
