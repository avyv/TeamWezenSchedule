package com.schedule.wezen.model;

import java.math.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Random;

import org.joda.time.Days;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Schedule {

	LocalDate startDate, endDate;
	LocalTime startTime, endTime;
	LocalDateTime created;
	String id;
	int slotDuration, secretCode, numSlotsDay;
	ArrayList<TimeSlot> timeSlots;

	public Schedule(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, int slotDuration, String id, int secretCode) {//, LocalDateTime created) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.slotDuration = slotDuration;
		this.id = id;
		this.secretCode = secretCode;
		this.numSlotsDay = calculateNumTimeSlots(startTime, endTime, slotDuration);
		this.created = LocalDateTime.now();//created;
		this.endTime = endTime; /*calculateEndTime(startTime, slotDuration, numSlotsDay);*/
		this.timeSlots = new ArrayList<TimeSlot>();
		populateTimeSlots();
	}


	public Schedule(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, int slotDuration, String id, int secretCode, ArrayList<TimeSlot> timeSlots, LocalDateTime created) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.slotDuration = slotDuration;
		this.id = id;
		this.secretCode = secretCode;
		this.created = created;
		this.numSlotsDay = calculateNumTimeSlots(startTime, endTime, slotDuration);
		this.endTime = endTime; /*calculateEndTime(startTime, slotDuration, numSlotsDay);*/
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

		int cntval = 0;
		for(int week = 0; week < numWeeksInSchedule; week++)
		{
			for(int time = 0; time < this.numSlotsDay; time++)
			{
				timeSlotDate = startOfWeekDate;

				for(int day = 0; day < 7; day++)
				{
					String tsID = this.id + " " + cntval;//Integer.toBinaryString(cntval);// + timeSlotStartTime.toString() + " " + timeSlotDate.toString();

					// populate with closed TimeSlots if the schedule does not start on Monday
					if(timeSlotDate.isBefore(this.startDate) && (!(timeSlotDate.equals(this.startDate))))
					{
						this.timeSlots.add(new TimeSlot(timeSlotStartTime, timeSlotDate, tsID, " ", this.id, createSecretCode(), false, false,cntval));
					}
					// populate with closed TimeSlots if the schedule does not end on Sunday
					else if(timeSlotDate.isAfter(this.endDate) && (!(timeSlotDate.equals(this.endDate))))
					{
						this.timeSlots.add(new TimeSlot(timeSlotStartTime, timeSlotDate, tsID, " ", this.id, createSecretCode(), false, false,cntval));
					}
					// if the date is within the range of the schedule start and end dates, populate array with open time slots
					else if (timeSlotDate.equals(this.startDate) || timeSlotDate.equals(this.endDate) || (timeSlotDate.isAfter(this.startDate) && timeSlotDate.isBefore(this.endDate)))
					{
						this.timeSlots.add(new TimeSlot(timeSlotStartTime, timeSlotDate, tsID, " ", this.id, createSecretCode(), true, false,cntval));
					}

					timeSlotDate = timeSlotDate.plusDays(1);
					cntval++;
				}
				timeSlotStartTime = timeSlotStartTime.plusMinutes(this.slotDuration);
			}
			timeSlotStartTime = this.startTime;
			startOfWeekDate = startOfWeekDate.plusDays(7);
		}

		// for(TimeSlot ts: this.timeSlots)
		// {
		// 	System.out.println(ts.id);
		// }

		return true;
	}
	
	public ArrayList<TimeSlot> populateTimeSlots2(ArrayList<TimeSlot> ts, LocalDate startDate, LocalDate endDate)
	{
		// test to make sure start date isn't after end date or start time isn't after end time
		if(endDate.isBefore(startDate) || this.endTime.isBefore(this.startTime))
		{
			return null;
		}

		LocalDate scheduleStartDate = startDate;

		LocalDate scheduleEndDate = endDate;

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

		int cntval = 0;
		for(int week = 0; week < numWeeksInSchedule; week++)
		{
			for(int time = 0; time < this.numSlotsDay; time++)
			{
				timeSlotDate = startOfWeekDate;

				for(int day = 0; day < 7; day++)
				{
					String tsID = this.id + " " + cntval;//Integer.toBinaryString(cntval);// + timeSlotStartTime.toString() + " " + timeSlotDate.toString();

					// populate with closed TimeSlots if the schedule does not start on Monday
					if(timeSlotDate.isBefore(startDate) && (!(timeSlotDate.equals(startDate))))
					{
						ts.add(new TimeSlot(timeSlotStartTime, timeSlotDate, tsID, " ", this.id, createSecretCode(), false, false,cntval));
					}
					// populate with closed TimeSlots if the schedule does not end on Sunday
					else if(timeSlotDate.isAfter(endDate) && (!(timeSlotDate.equals(endDate))))
					{
						ts.add(new TimeSlot(timeSlotStartTime, timeSlotDate, tsID, " ", this.id, createSecretCode(), false, false,cntval));
					}
					// if the date is within the range of the schedule start and end dates, populate array with open time slots
					else if (timeSlotDate.equals(startDate) || timeSlotDate.equals(endDate) || (timeSlotDate.isAfter(startDate) && timeSlotDate.isBefore(endDate)))
					{
						ts.add(new TimeSlot(timeSlotStartTime, timeSlotDate, tsID, " ", this.id, createSecretCode(), true, false,cntval));
					}

					timeSlotDate = timeSlotDate.plusDays(1);
					cntval++;
				}
				timeSlotStartTime = timeSlotStartTime.plusMinutes(this.slotDuration);
			}
			timeSlotStartTime = this.startTime;
			startOfWeekDate = startOfWeekDate.plusDays(7);
		}

		// for(TimeSlot ts: this.timeSlots)
		// {
		// 	System.out.println(ts.id);
		// }

		return ts;
	}

	public ArrayList<Schedule> divideByWeeks() {
//		ArrayList<TimeSlot> rdsisdumb = fuckYouRDS();
		int numSlotsPerWeek = 7 * this.numSlotsDay;

		int numWeeks = (this.timeSlots.size()) / numSlotsPerWeek;

		int weekCounter = 0;

		LocalDate weekStartDate = this.timeSlots.get(0).slotDate;

		LocalDate weekEndDate = weekStartDate.plusDays(6);

		ArrayList<Schedule> weeklySchedules = new ArrayList<Schedule>(numWeeks);


		for(int i = 0; i < numWeeks; i++)
		{
			ArrayList<TimeSlot> weeklyTimeSlots = new ArrayList<TimeSlot>(numSlotsPerWeek);

			for(int j = 0; j < numSlotsPerWeek; j++)
			{
				weeklyTimeSlots.add(this.getTimeSlots().get(weekCounter));
				weekCounter ++;
			}

			weeklySchedules.add(i,new Schedule(weekStartDate, weekEndDate, this.startTime, this.endTime, this.slotDuration, this.id, this.secretCode, weeklyTimeSlots, LocalDateTime.now()));

			weekStartDate = weekStartDate.plusDays(7);
			weekEndDate = weekStartDate.plusDays(6);

		}

		return weeklySchedules;
	}

	public ArrayList<TimeSlot> fuckYouRDS(){
		ArrayList<TimeSlot> tscopy = this.timeSlots;
		ArrayList<TimeSlot> fuckRDS = new ArrayList<TimeSlot>();
		int index = 0;
		int startsize = tscopy.size();
		while(fuckRDS.size()<startsize){
			for(TimeSlot tsbitch : tscopy){
				String gohomeRDS = this.id + index;
				if(tsbitch.id == gohomeRDS){
					fuckRDS.add(tsbitch);
					tscopy.remove(tsbitch);
					break;
				}
			}
			index++;
		}
		return fuckRDS;
	}


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
			
			if(!(sd.equals(this.startDate))){
				if((ChronoUnit.DAYS.between(sd, this.startDate)) >= 7 || calculateDayOfWeek(this.startDate) < calculateDayOfWeek(sd)) {
					ArrayList<TimeSlot> ts1 = new ArrayList<TimeSlot>();
					int dayOfWeekStart = this.startDate.getDayOfWeek().getValue();
					LocalDate dummyEd = this.startDate.minusDays(dayOfWeekStart);
					ts1 = populateTimeSlots2(ts1, sd, dummyEd);
					for(TimeSlot ts : ts1) {this.timeSlots.add(0, ts);}
				}
			}
			
			if(!(ed.equals(this.endDate))){
				if((ChronoUnit.DAYS.between(this.endDate, ed)) >= 7 || calculateDayOfWeek(this.endDate) > calculateDayOfWeek(ed)) {
					ArrayList<TimeSlot> ts2 = new ArrayList<TimeSlot>();
					int dayOfWeekEnd = this.endDate.getDayOfWeek().getValue();
					LocalDate dummySd = this.endDate.plusDays(8 - dayOfWeekEnd);
					ts2 = populateTimeSlots2(ts2, dummySd, ed);
					for(TimeSlot ts : ts2) {this.timeSlots.add(ts);}
				}
			}
			
			this.startDate = sd;
			this.endDate = ed;
			
			return true;
		}
	}

	public /*ArrayList<TimeSlot>*/ void searchForTime(String month, String year, String dayWeek, String dayMonth, String time) {
//		ArrayList<TimeSlot> available = new ArrayList<TimeSlot>();
		
//		for(TimeSlot ts: timeSlots) {
//			available.add(ts);
//		}
		
//		ArrayList<TimeSlot> toRemove = new ArrayList<TimeSlot>();
		for(TimeSlot ts: timeSlots) { //available) {
			if(!(month.equals("0"))) { //month != 0 && ts.getDate().getMonthValue() != month) {
//				toRemove.add(ts);
				if(ts.getDate().getMonthValue() != Integer.parseInt(month))
				{
					ts.setIsDisplayed(false);
				}
			}
			else if(!(year.equals("0"))) { //year != 0 && ts.getDate().getYear() != year) {
//				toRemove.add(ts);
				if(ts.getDate().getYear() != Integer.parseInt(year))
				{
					ts.setIsDisplayed(false);
				}
			}
			else if(!(dayWeek.equals("0"))) { //dayWeek != 0 && ts.getDate().getDayOfWeek().getValue() != dayWeek) {
//				toRemove.add(ts);
				if(ts.getDate().getDayOfWeek().getValue() != Integer.parseInt(dayWeek))
				{
					ts.setIsDisplayed(false);
				}
			}
			else if(!(dayMonth.equals(""))) { //dayMonth != 0 && ts.getDate().getDayOfMonth() != dayMonth) {
//				toRemove.add(ts);
				System.out.println(ts.slotDate.getMonthValue());
				if(ts.getDate().toString().equals(dayMonth))
				{
					ts.setIsDisplayed(false);
				}
			}
			else if(!(time.equals("0"))) { //time.getSecond() != 1 && (ts.getStartTime().getHour() != time.getHour() || ts.getStartTime().getMinute() != time.getMinute())) {
//				toRemove.add(ts);
				if(ts.getStartTime().toString().equals(time))
				{
					ts.setIsDisplayed(false);
				}
			}else {
				ts.setIsDisplayed(true);
			}
		}
//		available.removeAll(toRemove);
//		return available;
	}
	
	public boolean editTimeSlots(String action, LocalDate date, LocalTime time) {
		boolean toggle;
		if(action.equals("close")) {toggle = false;}		//Close time slots
		else if(action.equals("open")){toggle = true;}		//Open time slots
		else {return false;} 								//Undefined action
		if(date == null && time == null) {return false;}
		//Date given not in the schedule
		if(date != null) {
			if(date.isBefore(this.startDate) || date.isAfter(this.endDate)) {return false;}
		}
		//Time given not in the schedule
		if(time != null) {
			if(time.isBefore(this.startTime) || time.isAfter(this.endTime)) {return false;}
		}
		for(TimeSlot ts: timeSlots) {
			if(date != null && time != null) {
				if(ts.getDate().equals(date) && ts.getStartTime().equals(time)) {
					ts.isOpen = toggle;
					return true;
				}
			}
			else if(date != null) {
				if(ts.getDate().equals(date)) {ts.isOpen = toggle;}
			} 
			else if(time != null) {
				if(ts.getStartTime().equals(time)) {ts.isOpen = toggle;}
			}
		}
		return true;
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
	public LocalDateTime getCreated() {return created;}

	public void setStartDate(LocalDate sd) {this.startDate = sd;}
	public void setEndDate(LocalDate ed) {this.endDate = ed;}
	public void setStartTime(LocalTime st) {this.startTime = st;}
	public void setEndTime(LocalTime et) {this.endTime = et;}
	public void setNumSlotsDay(int num) {this.numSlotsDay = num;}
	public void setCreated(LocalDateTime created) {this.created = created;}
}
