package com.schedule.wezen.model;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

import static org.junit.Assert.*;

public class ScheduleTests {
	
	@Test
	public void testCalculateNumTimeSlots() {
		
		Schedule s = new Schedule(LocalDate.parse("2018-12-13"), LocalDate.parse("2018-12-21"), LocalTime.parse("08:00:00"), LocalTime.parse("13:00:00"), 15, "aSchedule", 0123);
		
		assertTrue(s.calculateNumTimeSlots(s.startTime, s.endTime, s.slotDuration) == 20);
	}
	
}
