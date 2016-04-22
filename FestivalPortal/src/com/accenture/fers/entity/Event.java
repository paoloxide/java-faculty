package com.accenture.fers.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
/**
 * Entity class for Event
 */
@Entity
@Table(name="Event")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="eventid")
	private int eventid;
	@Column(name="name")
	private String name;
	@Column(name="description")
	private String description;
	@Column(name="places")
	private String place;
	@Column(name="duration")
	private String duration;
	@Column(name="eventtype")
	private String eventtype;
	@Column(name="seatsavailable")
	private int seatsavailable;

	@ManyToMany(cascade=CascadeType.ALL,mappedBy="registeredEvents", fetch = FetchType.EAGER)
	private Set<Visitor> visitors;

	public Set<Visitor> getVisitors() {
		return visitors;
	}
	public void setVisitors(Set<Visitor> visitors) {
		this.visitors = visitors;
	}

	public int getEventid() {
		return eventid;
	}
	public void setEventid(int eventid) {
		this.eventid = eventid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getEventtype() {
		return eventtype;
	}
	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}
	public int getSeatsavailable() {
		return seatsavailable;
	}
	public void setSeatsavailable(int seatsavailable) {
		this.seatsavailable = seatsavailable;
	}


}
