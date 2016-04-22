package com.accenture.fers.service;


import java.util.List;
import com.accenture.fers.entity.Visitor;
import com.accenture.fers.exceptions.FERSGenericException;

public interface VisitorFacade {

	public boolean createVisitor(Visitor visitor);

	public Visitor searchVisitor(String username, String password);

	public void registerVisitor(Visitor visitor, int eventid);

	public List<Object[]> showRegisteredEvents(Visitor visitor);

	public int updateVisitorDetails(Visitor visitor);

	public int changePassword(Visitor visitor) throws FERSGenericException;

	public void unregisterEvent(Visitor visitor, int eventid);

	public boolean searchVisitor(String username);
}
