package com.schedule.wezen.model;

import java.time.LocalTime;
import java.time.LocalDate;

public class TimeSlot {
	
	LocalTime startTime, endTime;
	LocalDate slotDate;
	Meeting slotMeeting;
	String id;
	int secretCode;
	boolean isOpen;
	
	public TimeSlot(LocalTime startTime, LocalTime duration, LocalDate date) {
		this.startTime = startTime;
		this.duration = duration;
		this.slotDate = date;
	}
	
	public boolean isOccupied() {
		return (slotMeeting != null);
	}
	
	public boolean deleteMeeting() {
		if(slotMeeting == null) {
			return false;
		}
		else {
			slotMeeting = null;
			return true;
		}
	}
	
	public LocalTime getStartTime() {return startTime;}
	public LocalTime getDuration() {return duration;}
	public Meeting getMeeting() {return slotMeeting;}
	public LocalDate getDate() {return slotDate;}
	
	public void setMeeting(Meeting m) {this.slotMeeting = m;}
}
