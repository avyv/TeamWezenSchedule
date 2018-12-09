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
		String stringDate1 = "2017-12-05";
		String stringDate2 = "2018-01-03";
		LocalDate startDate = model.stringToDate(stringDate1);
		LocalDate endDate = model.stringToDate(stringDate2);
		String stringTime1 = "09:00:00";
		String stringTime2 = "20:00:00";
		LocalTime startTime = model.stringToTime(stringTime1);
		LocalTime endTime = model.stringToTime(stringTime2);
		int duration = 30;
		int secretCode = 1111;
		Schedule schedule = new Schedule(startDate, endDate, startTime, endTime, duration, "id", secretCode);
		ArrayList<Schedule> weeklySchedules = schedule.divideByWeeks(/*startDate, endDate, startTime, endTime, duration, "id", secretCode*/);
		int counter = 0;
		for(Schedule s : weeklySchedules) {
			counter++;
			System.out.println("New schedule");
			for(TimeSlot ts: s.getTimeSlots()) {
				System.out.println(ts.getId());
			}
		}
		assertEquals(counter,5);
	}
}
