package com.schedule.wezen.model;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.time.format.DateTimeFormatter;

public class Model {
	
	ArrayList<Schedule> schedules;
	ArrayList<TimeSlot> allTimeSlots;
	
	public Model() {
		
	}
	
	public void sortSlots() {
		for(Schedule s: schedules) {
			for(TimeSlot ts: allTimeSlots) {
				if(s.getId() == ts.getSid()) {
					s.addTimeSlot(ts);
				}
			}
		}
	}
	
	public LocalDate stringToDate(String stringDate) throws Exception {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(stringDate, formatter);
		return date;
	}
	
	//To change LocalDate to String use the .toString() method which turns it into a string with format "yyyy-MM-dd" 
	
	public LocalTime stringToTime(String stringTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm:ss");
		LocalTime time = LocalTime.parse(stringTime,formatter);
		return time;
	}
	
	//To change LocalTime to String use the .toString() method 
	
	public LocalTime stringToDuration(String stringTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm"); // changed
		LocalTime time = LocalTime.parse(stringTime,formatter);
		return time;
	}
	
	public boolean deleteSchedule(String id) {
		for(Schedule s: schedules) {
			if(s.getId().equals(id)) {
				schedules.remove(s);
				return true;
			}
		}
		return false;
	}
	
	public int createSecretCode() {
		Random r = new Random();
		int code = r.nextInt();
		if(schedules.size() == 0) 
			return code;
		for(Schedule s: schedules) {
			if(s.secretCode == code) {
				return createSecretCode();
			}
		}
		return code;
	}
	
	public boolean createSchedule(LocalDate startDate, LocalDate endDate,  LocalTime startTime, LocalTime endTime, int slotDuration, String id) {
		for(Schedule s: schedules) {
			if(s.id.equals(id)) {
				return false;
			}
		}
		schedules.add(new Schedule(startDate, endDate, startTime, endTime, slotDuration, id, createSecretCode()));
		return true;
	}
	
	public ArrayList<Schedule> getSchedules() {return schedules;}
	
	public void setSchedules(ArrayList<Schedule> schedules) {this.schedules = schedules;}
}
