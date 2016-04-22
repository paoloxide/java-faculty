package com.accenture.fers.service;

import java.util.List;

import com.accenture.fers.entity.Event;
import com.accenture.fers.entity.Visitor;

public interface EventFacade {

	public List<Event> getAllEvents();

	public boolean checkEventsofVisitor(Visitor visitor, int eventid);

	public void updateEventDeletions(int eventid);


}
