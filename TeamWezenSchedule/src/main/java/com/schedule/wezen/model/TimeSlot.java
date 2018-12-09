package com.schedule.wezen.model;

import java.time.LocalTime;
import java.time.LocalDate;

public class TimeSlot {
	
	LocalTime startTime/*, endTime*/;
	LocalDate slotDate;
	//Meeting slotMeeting;
	String id, sid, meetingName = " ";
	int secretCode, index;
	boolean isOpen, hasMeeting;
	
	public TimeSlot(LocalTime startTime, LocalDate slotDate, String id, String meetingName, String sid, int secretCode, boolean isOpen, boolean hasMeeting,int index) {
		this.startTime = startTime;
		this.slotDate = slotDate;
		this.id = id;
		this.meetingName = meetingName;
		this.sid = sid;
		this.secretCode = secretCode;
		this.isOpen = isOpen;
		this.hasMeeting = hasMeeting;
		this.index = index;
		//this.endTime = calculateEndTime(startTime, duration); (Unnecessary)
	}
	
	public TimeSlot(LocalTime startTime, LocalDate slotDate, String id, String sid, int secretCode, boolean isOpen, boolean hasMeeting) {
		this.startTime = startTime;
		this.slotDate = slotDate;
		this.id = id;
		this.sid = sid;
		this.secretCode = secretCode;
		this.isOpen = isOpen;
		this.hasMeeting = hasMeeting;
		//this.endTime = calculateEndTime(startTime, duration); (Unnecessary)
	}
	
	public TimeSlot(LocalTime startTime, LocalDate slotDate, String id, String sid, int secretCode) {
		this.startTime = startTime;
		this.slotDate = slotDate;
		this.id = id;
		this.sid = sid;
		this.secretCode = secretCode;
		this.isOpen = true;
		this.hasMeeting = false;
		//this.endTime = calculateEndTime(startTime, duration); (Unnecessary)
	}
	
	public LocalTime getStartTime() {return startTime;}
	//public LocalTime getEndTime() {return endTime;}
	public LocalDate getDate() {return slotDate;}
	public String getMeeting() {return meetingName;}
	public String getId() {return id;}
	public String getSid() {return sid;}
	public boolean getIsOpen() {return isOpen;}
	public int getSecretCode() {return secretCode;}
	public boolean getHasMeeting() {return hasMeeting;}
	public int getIndex() {return index;}
	
	public void setIsOpen(boolean isOpen) {this.isOpen = isOpen;}
	public void setMeeting(String m) {
		this.meetingName = m;
		this.hasMeeting = true;
		this.isOpen = false;
	}
	
	public boolean isCorrectCode(int sc) {
		return (sc == secretCode);
	}
	
	// Unnecessary?
	/*public LocalTime calculateEndTime(LocalTime startTime, LocalTime duration) {
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
	}*/
	
	public boolean deleteMeeting() {
		if(isOpen) {
			return false;
		} else {
			meetingName = " ";
			isOpen = true;
			hasMeeting = false;
			return true;
		}
	}
	
	public boolean createMeeting(String name) {
		if(!isOpen) {
			return false;
		} else {
			meetingName = name;
			isOpen = false;
			hasMeeting = true;
			return true;
		}
	}
}
