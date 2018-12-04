package com.schedule.wezen.model;

import java.util.*;
import java.util.ArrayList;
import java.util.Random;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Schedule {
	
	LocalDate startDate, endDate;
	LocalTime startTime, endTime, slotDuration;
	String id; 
	int secretCode, numSlotsDay;
	ArrayList<TimeSlot> timeSlots;
	
	public Schedule(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, LocalTime slotDuration, String id, int secretCode) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.slotDuration = slotDuration;
		this.id = id;
		this.secretCode = secretCode;
		this.numSlotsDay = calculateNumTimeSlots(startTime, endTime, slotDuration);
		this.endTime = endTime; /*calculateEndTime(startTime, slotDuration, numSlotsDay);*/
		populateTimeSlots(startDate, endDate, startTime, endTime, slotDuration, numSlotsDay);
	}
	
	public ArrayList<TimeSlot> divideWeeks(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, LocalTime duration, String id, int secretCode, int numSlotsDay){
		LocalDate copySD = startDate;
		LocalDate copyED = endDate;
		int startDayOfWeek = calculateDayOfWeek(startDate);
		int endDayOfWeek = calculateDayOfWeek(endDate);
		if(startDayOfWeek > 1) {
			startDate = startDate.minusDays(1);
			startDate = startDate.minusDays(startDayOfWeek - 1);
		}
		if(endDayOfWeek < 7) {
			startDate = endDate.plusDays(1);
			endDate = endDate.plusDays(7 - endDayOfWeek);
		}
		return timeSlots;
	}
	
	public boolean sortTimeSlots(ArrayList<TimeSlot> ts, int numSlotsDay) {
		ArrayList<TimeSlot> sortedTs = new ArrayList<TimeSlot>();
		for (int i = 0; i < numSlotsDay; i++) {
			int k = i;
			for (int j = 0; j < 7; j++) {
				sortedTs.add(ts.get(k));
				k += numSlotsDay;
				if(k>=ts.size()) {return false;}
			}
		}
		return true;
	}
	
	public int calculateNumTimeSlots(LocalTime st, LocalTime et, LocalTime dur) {
		int numSlots = 0;
	
		int overflowMinutes = 0;
		for(int hour = st.getHour(); hour <= et.getHour(); hour++) {
			int endMinutes = et.getMinute();
			if(et.getHour() > hour) {
				endMinutes = 59;
			}
			
			int min = overflowMinutes;
			if(hour == st.getHour()) {
				min = st.getMinute();
			}
			
			while(min < endMinutes) {
				min += dur.getMinute();
				if(min >= 59) {
					overflowMinutes = min-59;
				}
				numSlots++;
			}
		}
		return numSlots;
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
	
	/*public LocalTime calculateEndTime(LocalTime startTime, LocalTime duration, int numSlotsDay) {
		int min = startTime.getMinute();
		int hour = startTime.getHour();
		int d = duration.getMinute();
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
	}*/
	
	boolean populateTimeSlots(LocalDate sd, LocalDate ed, LocalTime st, LocalTime et, LocalTime dur, int numSlotsDay) {
		if(ed.isBefore(sd) || (ed.isEqual(sd) && et.isBefore(st))) {
			return false;
		}
		for(int year = sd.getYear(); year <= ed.getYear(); year++) {
			int endMonth = ed.getMonthValue();
			if(ed.getYear() > year){
				endMonth = 12;
			}
			for(int mon = sd.getMonthValue(); mon <= endMonth; mon++) {
				int endDay = ed.getDayOfMonth();
				if(ed.getMonthValue() > mon || ed.getYear() > year){
					endDay = sd.lengthOfMonth();
				} 
				for(int day = sd.getDayOfMonth(); day <= endDay; day++) {
//					for(int hour = 0; hour < 24; hour++) {
//						for(int min = 0; min < 60; min += dur.getMinute()) {
//							if(dur.getMinute() == 0) {
//								hour++;
//								dur.get
//								
//								if(hour >= st.getHour() && hour < et.getHour()) {
//									LocalTime start = LocalTime.parse(hour + ":00:00");
//									LocalDate date = LocalDate.parse(year + "-" + mon + "-" + day);
//									
//									timeSlots.add(new TimeSlot(start, dur, date));
//								}
//								
//								break;
//							}
//
//							if(hour >= st.getHour() && hour <= et.getHour() && min >= st.getMinute() && min <= et.getMinute()) {
//								LocalTime start = LocalTime.parse(hour + ":" + min +":00");
//								LocalDate date = LocalDate.parse(year + "-" + mon + "-" + day);
//								
//								timeSlots.add(new TimeSlot(start, dur, date));
//							}
//						}
//					}
					int overflowMinutes = 0;
					for(int hour = st.getHour(); hour <= et.getHour(); hour++) {
						int endMinutes = et.getMinute();
						if(et.getHour() > hour) {
							endMinutes = 59;
						} 
						int min = overflowMinutes;
						if(hour == st.getHour()) {
							min = st.getMinute();
						} 
						while(min < endMinutes) {
							min += dur.getMinute();
							if(min >= 59) {
								overflowMinutes = min-59;
								LocalTime startTime = LocalTime.parse(hour+":"+overflowMinutes, DateTimeFormatter.ofPattern("HH:mm"));
							} else {
								LocalTime startTime = LocalTime.parse(hour+":"+min, DateTimeFormatter.ofPattern("HH:mm"));
							}
							LocalDate slotDate = LocalDate.parse(year+"-"+mon+"-"+day, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
							timeSlots.add(new TimeSlot(startTime, slotDate, startTime.toString()+slotDate.toString(), id, createSecretCode()));
						}
					}
				}
			}
		}
		int startDayOfWeek = calculateDayOfWeek(sd);
		int endDayOfWeek = calculateDayOfWeek(ed);
		if(startDayOfWeek > 1) {
			ed = sd.minusDays(1);
			sd = sd.minusDays(startDayOfWeek - 1);
			populateTimeSlots(sd, ed, st, et, dur, numSlotsDay);
		}
		if(endDayOfWeek < 7) {
			sd = ed.plusDays(1);
			ed = ed.plusDays(7 - endDayOfWeek);
			populateTimeSlots(sd, ed, st, et, dur, numSlotsDay);
		}
		return true;
	}
	
	public int createSecretCode() {
		Random r = new Random();
		int code = r.nextInt();
		for(TimeSlot ts: timeSlots) {
			if(ts.secretCode == code) {
				return createSecretCode();
			}
		}
		return code;
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
	
	public void addTimeSlot(TimeSlot ts) {
		this.timeSlots.add(ts);
	}
	
	public LocalDate getStartDate() {return startDate;}
	public LocalDate getEndDate() {return endDate;}
	public LocalTime getStartTime() {return startTime;}
	public LocalTime getEndTime() {return endTime;}
	public LocalTime getSlotDuration() {return slotDuration;}
	public String getId() {return id;}
	public int getSecretCode() {return secretCode;}
	public int getNumSlotsDay() {return numSlotsDay;}
	public ArrayList<TimeSlot> getTimeSlots() {return timeSlots;}
	
	public void setStartDate(LocalDate sd) {this.startDate = sd;}
	public void setEndDate(LocalDate ed) {this.endDate = ed;}
	public void setStartTime(LocalTime st) {this.startTime = st;}
	public void setEndTime(LocalTime et) {this.endTime = et;}
	public void setNumSlotsDay(int num) {this.numSlotsDay = num;}
}
