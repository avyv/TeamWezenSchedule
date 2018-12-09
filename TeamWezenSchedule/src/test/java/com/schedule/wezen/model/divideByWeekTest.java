package com.schedule.wezen.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import com.schedule.wezen.model.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class divideByWeekTest {
	public divideByWeekTest() {}
	
	@Test
	public void testPopulate() throws Exception {
		Model model = new Model();
		String stringDate1 = "2018-03-24";//"2018-12-03";
		String stringDate2 = "2018-05-05";//"2018-12-31";
		LocalDate startDate = model.stringToDate(stringDate1);
		LocalDate endDate = model.stringToDate(stringDate2);
		String stringTime1 = "15:00:00";//"09:00:00";
		String stringTime2 = "16:00:00";//"20:00:00";
		LocalTime startTime = model.stringToTime(stringTime1);
		LocalTime endTime = model.stringToTime(stringTime2);
		int duration = 15;//30;
		int secretCode = 1111;
		Schedule schedule = new Schedule(startDate, endDate, startTime, endTime, duration, "id", secretCode);
		ArrayList<Schedule> weeklySchedules = schedule.divideByWeeks(/*startDate, endDate, startTime, endTime, duration, "id", secretCode*/);
		int counter = 0;
		System.out.println(weeklySchedules.size());
		
//		for(Schedule s : weeklySchedules) {
//			System.out.println("New schedule");
//			for(TimeSlot ts: s.getTimeSlots()) {
//				if(counter == 0) {
//				System.out.println(ts.getId());
//				}
//			}
//			counter++;
//
//		}
		ArrayList<Schedule> scheduleDividedByWeeks = schedule.divideByWeeks();
		Schedule firstWeek = scheduleDividedByWeeks.get(0);
		String startDateOfWeek = firstWeek.getStartDate().toString();
		String msgd = "startd: " + startDateOfWeek + " endd: " + firstWeek.getEndDate().toString();
		System.out.println(msgd);
		String msgt = "startt: " + firstWeek.getStartTime() + " endt: " + firstWeek.getEndTime();
		System.out.println(msgt);

		String s = "size: " + firstWeek.getTimeSlots().size();
		System.out.println(s);
		for(TimeSlot ts : firstWeek.getTimeSlots()) {
			System.out.println(ts.getId());
		}
		assertEquals(counter,5);
	}
}
