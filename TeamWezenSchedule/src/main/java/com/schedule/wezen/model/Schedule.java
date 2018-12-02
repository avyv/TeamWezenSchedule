package com.schedule.wezen.model;

import java.util.*;
import java.util.ArrayList;
import java.util.Random;
import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
	
	LocalDate startDate, endDate;
	LocalTime startTime, endTime, slotDuration;
	String id; 
	int secretCode, numSlotsDay;
	ArrayList<TimeSlot> timeSlots;
	
	public Schedule(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime slotDuration, String id, int secretCode, int numSlotsDay) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.slotDuration = slotDuration;
		this.id = id;
		this.secretCode = secretCode;
		this.numSlotsDay = numSlotsDay;
		this.endTime = calculateEndTime(startTime, slotDuration, numSlotsDay);
		//populateTimeSlots(startDate, endDate, startTime, slotDuration);
	}
	
	public int calculateDayOfWeek(LocalDate startDate) {
		String date = startDate.toString();
		int k = Integer.parseInt(date.substring(8, 10));
		int m = Integer.parseInt(date.substring(5, 7));
		if(m > 2) {m -= 2;}
		else {m += 10;}
		int d = Integer.parseInt(date.substring(2, 4));
		int c = Integer.parseInt(date.substring(0, 2));
		int dayOfWeek = (k + doubleToInt((13*m-1)/5) + d + doubleToInt(d/4) + doubleToInt(c/4) + (c*2));
		if (dayOfWeek > 7) {
			dayOfWeek = dayOfWeek % 7;
		}
		return dayOfWeek; //1=Monday, 2=Tuesday,...
	}
	
	public int doubleToInt(double value) {
		int intValue = (int) value;
		return intValue;
	}
	
	public LocalTime calculateEndTime(LocalTime startTime, LocalTime duration, int numSlotsDay) {
		int min = Integer.parseInt(startTime.toString().substring(3, 5));
		int hour = Integer.parseInt(startTime.toString().substring(0, 2));
		int d = Integer.parseInt(duration.toString().substring(0, 2));
		for (int i = 0; i<numSlotsDay; i++) {
			if((min+d) > 59) {
				min = (min+d) - 60;
				hour++;
			} 
			min+=min;
		}
		String endMin = String.valueOf(min);
		String endHour = String.valueOf(hour);
		String endTimeString = endHour + ":" + endMin;
		Model model = new Model();
		LocalTime endTime = model.stringToTime(endTimeString);
		return endTime;
	}
	
	boolean populateTimeSlots(LocalDate sd, LocalDate ed, LocalTime st, LocalTime dur) {
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
								
								if(hour >= st.getHour() && hour < et.getHour()) {
									LocalTime start = LocalTime.parse(hour + ":00:00");
									LocalDate date = LocalDate.parse(year + "-" + mon + "-" + day);
									
									timeSlots.add(new TimeSlot(start, dur, date));
								}
								
								break;
							}
							
							if(hour >= st.getHour() && hour <= et.getHour() && min >= st.getMinute() && min <= et.getMinute()) {
								LocalTime start = LocalTime.parse(hour + ":" + min +":00");
								LocalDate date = LocalDate.parse(year + "-" + mon + "-" + day);
								
								timeSlots.add(new TimeSlot(start, dur, date));
							}
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
	
	public void setStartDate(LocalDate sd) {this.startDate = sd;}
	public void setEndDate(LocalDate ed) {this.endDate = ed;}
	public void setStartTime(LocalTime st) {this.startTime = st;}
	public void setNumSlotsDay(int num) {this.numSlotsDay = num;}
	public void setEndTime(LocalTime et) {this.endTime = et;}
}
