package com.schedule.wezen.model;

import java.time.LocalTime;
import java.time.LocalDate;

public class TimeSlot {
	
	LocalTime startTime, endTime;
	LocalDate slotDate;
	Meeting slotMeeting;
	String id, sid;
	int secretCode;
	boolean isOpen;
	
	public TimeSlot(LocalTime startTime, LocalTime duration, LocalDate slotDate, String id, String sid, int secretCode) {
		this.startTime = startTime;
		this.slotDate = slotDate;
		this.id = id;
		this.sid = sid;
		this.secretCode = secretCode;
		this.isOpen = true;
		this.endTime = calculateEndTime(startTime, duration);
	}
	
	public TimeSlot(LocalTime startTime, LocalTime duration, LocalDate slotDate, String id, String sid, int secretCode, boolean isOpen) {
		this.startTime = startTime;
		this.slotDate = slotDate;
		this.id = id;
		this.sid = sid;
		this.secretCode = secretCode;
		this.isOpen = isOpen;
		this.endTime = calculateEndTime(startTime, duration);
	}
	
	public LocalTime getStartTime() {return startTime;}
	public LocalTime getEndTime() {return endTime;}
	public LocalDate getDate() {return slotDate;}
	public Meeting getMeeting() {return slotMeeting;}
	public String getId() {return id;}
	public String getSid() {return sid;}
	public boolean getIsOpen() {return isOpen;}
	public int getSecretCode() {return secretCode;}
	
	public void setMeeting(Meeting m) {this.slotMeeting = m;}
	
	public boolean isCorrectCode(int sc) {
		return (sc == secretCode);
	}
	
	public LocalTime calculateEndTime(LocalTime startTime, LocalTime duration) {
		int min = startTime.getMinute();
		int hour = startTime.getHour();
		int d = duration.getMinute();
		if((min+d) > 59) {
			min = (min+d) - 60;
			hour++;
		} 
		min+=min;
		String endMin = String.valueOf(min);
		String endHour = String.valueOf(hour);
		String endTimeString = endHour + ":" + endMin;
		Model model = new Model();
		LocalTime endTime = model.stringToTime(endTimeString);
		return endTime;
	}
	
	public boolean deleteMeeting() {
		if(isOpen) {
			return false;
		} else {
			slotMeeting = null;
			isOpen = true;
			return true;
		}
	}
	
	public boolean createMeeting(String name) {
		if(!isOpen) {
			return false;
		} else {
			slotMeeting = new Meeting(name, id);
			isOpen = false;
			return true;
		}
	}
}
