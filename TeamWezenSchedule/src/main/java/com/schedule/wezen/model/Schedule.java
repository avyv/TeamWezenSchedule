package com.schedule.wezen.model;

import java.math.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Random;

import org.joda.time.Days;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Schedule {
	
	LocalDate startDate, endDate;
	LocalTime startTime, endTime;
	String id; 
	int slotDuration, secretCode, numSlotsDay;
	ArrayList<TimeSlot> timeSlots;
	
	public Schedule(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, int slotDuration, String id, int secretCode) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.slotDuration = slotDuration;
		this.id = id;
		this.secretCode = secretCode;
		this.numSlotsDay = calculateNumTimeSlots(startTime, endTime, slotDuration);
		this.endTime = endTime; /*calculateEndTime(startTime, slotDuration, numSlotsDay);*/
		this.timeSlots = new ArrayList<TimeSlot>();
		populateTimeSlots();
	}
	
	
	//TODO Don't use this? the arraylist doesn't populate
	public Schedule(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, int slotDuration, String id, int secretCode, ArrayList<TimeSlot> timeSlots) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.slotDuration = slotDuration;
		this.id = id;
		this.secretCode = secretCode;
		this.numSlotsDay = calculateNumTimeSlots(startTime, endTime, slotDuration);
		this.endTime = endTime; /*calculateEndTime(startTime, slotDuration, numSlotsDay);*/
		//TODO this doesn't work
		this.timeSlots = timeSlots;
//		for(TimeSlot ts: timeSlots) {
//			this.addTimeSlot(ts);
//		}
	}
	
	
	public boolean populateTimeSlots()
	{
		// test to make sure start date isn't after end date or start time isn't after end time
		if(this.endDate.isBefore(this.startDate) || this.endTime.isBefore(this.startTime))
		{
			return false;
		}
		
		LocalDate scheduleStartDate = this.startDate;
		
		LocalDate scheduleEndDate = this.endDate;
		
		
		int dayOfWeekStart = scheduleStartDate.getDayOfWeek().getValue();
		
		int dayOfWeekEnd = scheduleEndDate.getDayOfWeek().getValue();
		
		if(dayOfWeekStart > 1)
		{
			scheduleStartDate = scheduleStartDate.minusDays(dayOfWeekStart - 1); // start on Monday
		}
		if(dayOfWeekEnd < 7)
		{
			scheduleEndDate = scheduleEndDate.plusDays(7 - dayOfWeekEnd); // end on Sunday
		}
		
		
		LocalDate timeSlotDate = scheduleStartDate;
		
		LocalTime timeSlotStartTime = this.startTime;
		
		int numDaysInSchedule = (int) (ChronoUnit.DAYS.between(scheduleStartDate, scheduleEndDate) + 1);
		
		int numWeeksInSchedule = numDaysInSchedule / 7;
		
		//int numOfTimeSlotsPerDay = calculateNumTimeSlots(this.startTime, this.endTime, this.slotDuration);
		
		LocalDate startOfWeekDate = scheduleStartDate;
		
		
		for(int week = 0; week < numWeeksInSchedule; week++)
		{
			for(int time = 0; time < this.numSlotsDay; time++)
			{
				for(int day = 0; day < 7; day++)
				{
					String tsID = this.id + " " + timeSlotStartTime.toString() + " " + timeSlotDate.toString();
					
					// populate with closed TimeSlots if the schedule does not start on Monday
					if(timeSlotDate.isBefore(this.startDate))
					{	
						this.timeSlots.add(new TimeSlot(timeSlotStartTime, timeSlotDate, tsID, " ", this.id, createSecretCode(), false, false));
					}
					// populate with closed TimeSlots if the schedule does not end on Sunday
					else if(timeSlotDate.isAfter(this.endDate))
					{
						this.timeSlots.add(new TimeSlot(timeSlotStartTime, timeSlotDate, tsID, " ", this.id, createSecretCode(), false, false));
					}
					// if the date is within the range of the schedule start and end dates, populate array with open time slots
					else
					{
						this.timeSlots.add(new TimeSlot(timeSlotStartTime, timeSlotDate, tsID, " ", this.id, createSecretCode(), true, false));
					}
					
					timeSlotDate = timeSlotDate.plusDays(1);
					
				}
				timeSlotDate = startOfWeekDate;
				timeSlotStartTime = timeSlotStartTime.plusMinutes(this.slotDuration);
			}
			timeSlotStartTime = this.startTime;
			startOfWeekDate = startOfWeekDate.plusDays(7);
		}
		
		return true;
	}
	
	public ArrayList<Schedule> divideByWeeks() {
		ArrayList<Schedule> weeklySchedules = new ArrayList<Schedule>();
		
		int numSlotsPerWeek = 7 * this.numSlotsDay;
		
		int numWeeks = (this.timeSlots.size()) / numSlotsPerWeek;
		
		int weekCounter = 0;
		
		LocalDate weekStartDate = this.timeSlots.get(0).slotDate;
		
		LocalDate weekEndDate = weekStartDate.plusDays(6);
		
		for(int i = 0; i < numWeeks; i++)
		{
			ArrayList<TimeSlot> weeklyTimeSlots = new ArrayList<TimeSlot>();
			
			for(int j = 0; j < numSlotsPerWeek; j++)
			{
				weeklyTimeSlots.add(this.timeSlots.get(weekCounter + j));
			}
			
			weeklySchedules.add(new Schedule(weekStartDate, weekEndDate, this.startTime, this.endTime, this.slotDuration, this.id, this.secretCode, weeklyTimeSlots));
			
			weekStartDate = weekStartDate.plusDays(7);
			weekEndDate = weekEndDate.plusDays(7);
			
			weekCounter += numSlotsPerWeek;
		}
		
		return weeklySchedules;
	}
	
	
//	public ArrayList<Schedule> divideByWeeks(/*LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, int duration, String id, int secretCode*/){
//		ArrayList<TimeSlot> sortedTs = sortTimeSlots(this.timeSlots, this.numSlotsDay);
//		ArrayList<Schedule> weeklySchedules = new ArrayList<Schedule>(); //
//		LocalDate copySD = startDate;
//		LocalDate copyED;
//		int startDayOfWeek = calculateDayOfWeek(startDate);
//		if(startDayOfWeek > 1) {
//			copySD = startDate.minusDays(startDayOfWeek - 1);
//		}
//		copyED = copySD.plusDays(6);
//		int counter = 0;
//		int numSlotsWeek = (7*this.numSlotsDay);
//		while(!(copySD.isAfter(endDate))) {
//			ArrayList<TimeSlot> ts = new ArrayList<TimeSlot>();
//			for(int i = 0; i<numSlotsWeek; i++) {
//				ts.add(sortedTs.get(counter+i)); //
//			}
//			counter+=numSlotsWeek;
//			weeklySchedules.add(new Schedule(copySD, copyED, startTime, endTime, this.slotDuration, id, secretCode, ts));
//			copySD = copySD.plusDays(7);
//			copyED = copyED.plusDays(7);
//		}
//		return weeklySchedules;
//	}
//	
//	public ArrayList<TimeSlot> sortTimeSlots(ArrayList<TimeSlot> ts, int numSlotsDay) {
//		ArrayList<TimeSlot> sortedTs = new ArrayList<TimeSlot>();
//		int numSlotsWeek = numSlotsDay * 7;
//		for (int h = 0; h < ts.size()-1; h+=numSlotsWeek) {
//			for (int i = 0; i < numSlotsDay; i++) {
//				int k = i + h;
//				for (int j = 0; j < 7; j++) {
//					//System.out.println("k: "+k);
//					//if(k>=ts.size()) {return false;}
//					sortedTs.add(ts.get(k));
//					k += numSlotsDay;
//				}
//				//System.out.println(k);
//				//System.out.println(ts.size());
//			}
//		}
//		return sortedTs;
//		//ts = sortedTs;
//		//return true;
//	}
	
	
	
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
	
//	boolean populateTimeSlots(LocalDate sd, LocalDate ed, LocalTime st, LocalTime et, int dur, int numSlotsDay, String id, ArrayList<TimeSlot> timeSlots) {
//		if(ed.isBefore(sd) || (ed.isEqual(sd) && et.isBefore(st))) {
//			return false;
//		}
//		int startDayOfWeek = calculateDayOfWeek(sd);
//		if(startDayOfWeek > 1) {
//			LocalDate dummyEd = sd.minusDays(1);
//			LocalDate dummySd = sd.minusDays(startDayOfWeek - 1);
//			populateEmptyTS(dummySd, dummyEd, st, et, dur, numSlotsDay, id, timeSlots);
//		}
//		//System.out.println(timeSlots.size());
//		for(int year = sd.getYear(); year <= ed.getYear(); year++) {
//			int endMonth = ed.getMonthValue();
//			if(ed.getYear() > year){
//				endMonth = 12;
//			}
//			int mon = 1;
//			if(year == sd.getYear()) {mon = sd.getMonthValue();}
//			int counter = 0;
//			for( ; mon <= endMonth; mon++) {
//				int endDay = ed.getDayOfMonth();
//				if(ed.getMonthValue() != mon || ed.getYear() > year){
//					String monString = Integer.toString(mon);
//					if(mon<10) {
//						monString = "0"+monString;
//					}
//					LocalDate tempDate = LocalDate.parse(year+"-"+monString+"-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//					endDay = tempDate.lengthOfMonth();
//				} 
//				counter++;
//				int day = 1;
//				if(mon == sd.getMonthValue() && year == sd.getYear()) {day = sd.getDayOfMonth();}
//				for( ; day <= endDay; day++) {
//					//System.out.println(timeSlots.size()+" month/day:"+mon+"-"+day);
//					int overflowMinutes = 0;
//					for(int hour = st.getHour(); hour <= et.getHour(); hour++) {
//						int endMinutes = 60;
//						if(et.getHour() == hour) {
//							endMinutes = et.getMinute();
//						}
//						int meetingMin = 0 + overflowMinutes;
//						for(int min = 0; min < endMinutes; min++) {
//							if(min==endMinutes && et.getHour()==hour) {break;}
//							if(min==meetingMin) {
//								String hourString = Integer.toString(hour);
//								String minString = Integer.toString(min);
//								if(hour<10) {
//									hourString = "0"+hourString;
//								}
//								if(min<10) {
//									minString = "0"+minString;
//								}
//								LocalTime startTime = LocalTime.parse(hourString+":"+minString, DateTimeFormatter.ofPattern("HH:mm"));
//								String dayString = Integer.toString(day);
//								String monString = Integer.toString(mon);
//								if(day<10) {
//									dayString = "0"+dayString;
//								}
//								if(mon<10) {
//									monString = "0"+monString;
//								}
//								LocalDate slotDate = LocalDate.parse(year+"-"+monString+"-"+dayString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//								timeSlots.add(new TimeSlot(startTime, slotDate, this.id + " " + startTime.toString()+" "+slotDate.toString(), id, createSecretCode()));
//								//System.out.println((timeSlots.get(timeSlots.size()-1)).getId());
//								meetingMin += dur;
//								if(meetingMin>=endMinutes) {
//									overflowMinutes=meetingMin-60;
//									meetingMin=0;
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		//System.out.println(timeSlots.size());
//		int endDayOfWeek = calculateDayOfWeek(ed);
//		if(endDayOfWeek < 7) {
//			LocalDate dummySd = ed.plusDays(1);
//			LocalDate dummyEd = ed.plusDays(7 - endDayOfWeek);
//			populateEmptyTS(dummySd, dummyEd, st, et, dur, numSlotsDay, id, timeSlots);
//		}
//		//System.out.println(timeSlots.size());
//		return true;
//	}
//	
//	boolean populateEmptyTS(LocalDate sd, LocalDate ed, LocalTime st, LocalTime et, int dur, int numSlotsDay, String id, ArrayList<TimeSlot> timeSlots) {
//		int sDayOfWeek = calculateDayOfWeek(sd);
//		int eDayOfWeek = calculateDayOfWeek(ed);
//		while(sDayOfWeek<=eDayOfWeek) {
//			//System.out.println(timeSlots.size()+" dayOfWeek:"+sDayOfWeek);
//			sDayOfWeek++;
//			int overflowMinutes = 0;
//			for(int hour = st.getHour(); hour <= et.getHour(); hour++) {
//				int endMinutes = 60;
//				if(et.getHour() == hour) {
//					endMinutes = et.getMinute();
//				}
//				int meetingMin = 0 + overflowMinutes;
//				for(int min = 0; min < endMinutes; min++) {
//					if(min==endMinutes && et.getHour()==hour) {break;}
//					if(min==meetingMin) {
//						String hourString = Integer.toString(hour);
//						String minString = Integer.toString(min);
//						if(hour<10) {
//							hourString = "0"+hourString;
//						}
//						if(min<10) {
//							minString = "0"+minString;
//						}
//						LocalTime startTime = LocalTime.parse(hourString+":"+minString, DateTimeFormatter.ofPattern("HH:mm"));
//						String dayString = Integer.toString(sd.getDayOfMonth());
//						String monString = Integer.toString(sd.getMonthValue());
//						if(sd.getDayOfMonth()<10) {
//							dayString = "0"+dayString;
//						}
//						if(sd.getMonthValue()<10) {
//							monString = "0"+monString;
//						}
//						LocalDate slotDate = LocalDate.parse(sd.getYear()+"-"+monString+"-"+dayString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//						timeSlots.add(new TimeSlot(startTime, slotDate, startTime.toString()+" "+slotDate.toString(), id, createSecretCode(), false, false));
//						//System.out.println((timeSlots.get(timeSlots.size()-1)).getId());
//						meetingMin += dur;
//						if(meetingMin>=endMinutes) {
//							overflowMinutes=meetingMin-60;
//							meetingMin=0;
//						}
//					}
//				}
//			}
//			sd = sd.plusDays(1);
//		}
//		return true;
//	}
	
	
	public int calculateNumTimeSlots(LocalTime st, LocalTime et, int dur) {
		double numSlots = 0;
		double shr = st.getHour();
		double ehr = et.getHour();
		
		numSlots = ((ehr - shr)*60)/dur;
		return (int) numSlots;
	}
	
	public int calculateDayOfWeek(LocalDate startDate) {
		LocalDate copyDate = startDate;
		if(startDate.getMonthValue() == 1 || startDate.getMonthValue() == 2) {
			copyDate = startDate.minusYears(1);
		}
		String date = copyDate.toString();
		int k = Integer.parseInt(date.substring(8, 10));
		int m = Integer.parseInt(date.substring(5, 7));
		if(m > 2) {m -= 2;}
		else {m += 10;}
		int d = Integer.parseInt(date.substring(2, 4));
		int c = Integer.parseInt(date.substring(0, 2));
		int dayOfWeek = (k + doubleToInt((13*m-1)/5) + d + doubleToInt(d/4) + doubleToInt(c/4) - (c*2));
		//if(dayOfWeek < 0) {dayOfWeek++;}
		while(dayOfWeek < 0) {dayOfWeek+=7;}
		if (dayOfWeek > 7) {
			dayOfWeek = dayOfWeek % 7;
		}
		if (dayOfWeek == 0) {dayOfWeek = 7;}
		return dayOfWeek; //1=Monday, 2=Tuesday,...
	}
	
	public int doubleToInt(double value) {
		int intValue = (int) value;
		return intValue;
	}
	
	public int createSecretCode() {
		Random r = new Random();
		int code = r.nextInt();
		return code;
		/*if(timeSlots.size() == 0)
			return code;
		for(TimeSlot ts: timeSlots) {
			if(ts.secretCode == code) {
				return createSecretCode();
			}
		}
		return code;*/
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
	
	public void emptyTimeSlots() {
		this.timeSlots = new ArrayList<TimeSlot>();
	}
	
	public void addTimeSlot(TimeSlot ts) {
		this.timeSlots.add(ts);
	}
	
	public LocalDate getStartDate() {return startDate;}
	public LocalDate getEndDate() {return endDate;}
	public LocalTime getStartTime() {return startTime;}
	public LocalTime getEndTime() {return endTime;}
	public int getSlotDuration() {return slotDuration;}
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
