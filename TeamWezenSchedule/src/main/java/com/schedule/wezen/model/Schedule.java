package com.schedule.wezen.model;

import java.util.ArrayList;
import java.util.Random;
import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
	
	LocalDate startDate, endDate;
	LocalTime startTime, endTime, slotDuration;
	String id; 
	int secretCode;
	ArrayList<TimeSlot> timeSlots;
	ArrayList<Meeting> meetings;
	
	public Schedule(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, LocalTime slotDuration, String id, int secretCode) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.slotDuration = slotDuration;
		this.id = id;
		this.secretCode = secretCode;
		
		populateTimeSlots(startDate, endDate, startTime, endTime, slotDuration);
	}
	
	boolean populateTimeSlots(LocalDate sd, LocalDate ed, LocalTime st, LocalTime et, LocalTime dur) {
		if(ed.isBefore(sd) || (ed.isEqual(sd) && et.isBefore(st))) {
			return false;
		}
		
		for(int year = sd.getYear(); year <= ed.getYear(); year++) {
			for(int mon = sd.getMonthValue(); mon <= ed.getMonthValue(); mon++) {
				for(int day = sd.getDayOfMonth(); day <= ed.getDayOfMonth(); day++) {
					
					for(int hour = 0; hour < 24; hour++) {
						for(int min = 0; min < 60; min += dur.getMinute()) {
							if(dur.getMinute() == 0) {
								hour++;
								
								LocalTime start = LocalTime.parse(hour + ":00:00");
								LocalDate date = LocalDate.parse(year + "-" + mon + "-" + day);
								
								timeSlots.add(new TimeSlot(start, dur, date));
								
								break;
							}
							
							LocalTime start = LocalTime.parse(hour + ":" + min +":00");
							LocalDate date = LocalDate.parse(year + "-" + mon + "-" + day);
							
							timeSlots.add(new TimeSlot(start, dur, date));
						}
					}
				}
			}
		}
		return true;
	}
	
	public int createSecretCode() {
		Random r = new Random();
		int code = r.nextInt();
		for(Meeting m: meetings) {
			if(m.secretCode == code) {
				return createSecretCode();
			}
		}
		return code;
	}
	
	public boolean createMeeting(TimeSlot t, String title, String id) {
		if(t.isOccupied()) {
			return false;
		}
		else {
			t.slotMeeting = new Meeting(title, createSecretCode(), id);
			return true;
		}
	}
	
	public boolean changeDuration(LocalDate sd, LocalDate ed) {
		if(this.startDate.isBefore(sd) || this.endDate.isAfter(ed)) {
			return false;
		}
		else {
			this.startDate = sd;
			this.endDate = ed;
			return true;
		}
	}
	
	public ArrayList<TimeSlot> searchForTime(int month, int year, int dayWeek, int dayMonth, LocalTime time) {
		ArrayList<TimeSlot> available = timeSlots;
		for(TimeSlot ts: available) {
			if(month != 0 && ts.getDate().getMonthValue() != month) {
				available.remove(ts);
			}
			else if(year != 0 && ts.getDate().getYear() != year) {
				available.remove(ts);
			}
			else if(dayWeek != 0 && ts.getDate().getDayOfWeek().getValue() != dayWeek) {
				available.remove(ts);
			}
			else if(dayMonth != 0 && ts.getDate().getDayOfMonth() != dayMonth) {
				available.remove(ts);
			}
			else if(time.getSecond() != 1 && (ts.getStartTime().getHour() != time.getHour() || ts.getStartTime().getMinute() != time.getMinute())) {
				available.remove(ts);
			}
		}
		return available;
	}
	
	public boolean isCorrectCode(int sc) {
		return (sc == secretCode);
	}
	
	public LocalDate getStartDate() {return startDate;}
	public LocalDate getEndDate() {return endDate;}
	public LocalTime getStartTime() {return startTime;}
	public LocalTime getEndTime() {return endTime;}
	public LocalTime getSlotDuration() {return slotDuration;}
	public String getId() {return id;}
	public int getSecretCode() {return secretCode;}
	public ArrayList<TimeSlot> getTimeSlots() {return timeSlots;}
	public ArrayList<Meeting> meetings() {return meetings;}
	
	public void setStartDate(LocalDate sd) {this.startDate = sd;}
	public void setEndDate(LocalDate ed) {this.endDate = ed;}
	public void setStartTime(LocalTime st) {this.startTime = st;}
	public void setEndTime(LocalTime et) {this.endTime = et;}
}
