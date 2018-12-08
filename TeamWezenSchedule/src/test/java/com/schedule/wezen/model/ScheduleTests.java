package com.schedule.wezen.model;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.*;

import org.junit.Test;

import com.schedule.wezen.db.SchedulesDAO;
import com.schedule.wezen.db.TimeSlotsDAO;

import junit.framework.TestCase;

public class ScheduleTests extends TestCase{
	SchedulesDAO dao;
	Schedule sched;
	
	
	@Override
	protected void setUp() {
		dao = new SchedulesDAO();
		sched = new Schedule(LocalDate.parse("2018-12-15"), LocalDate.parse("2018-12-25"), LocalTime.parse("08:00:00"), LocalTime.parse("10:00:00"), 20, "thisSchedule", 123456);
		dao.deleteAllSchedules();
	}
	
	//DAO
	
	@Test
	public void testAddGetDeleteSchedule() throws Exception {
		dao.addSchedule(sched);
		
		Schedule newSched = dao.getSchedule("thisSchedule");
		
		int numCont = 0;
		for(TimeSlot schedTS: sched.timeSlots) {
			for(TimeSlot newSchedTS: newSched.timeSlots) {
				if(schedTS.id.equals(newSchedTS.id))
					numCont++;
			}
		}
		
		assertEquals(sched.timeSlots.size(), numCont);
		
		assertEquals(newSched.id, sched.id);
		assertEquals(newSched.numSlotsDay, sched.numSlotsDay);
		assertEquals(newSched.secretCode, sched.secretCode);
		assertEquals(newSched.slotDuration, sched.slotDuration);
		assertEquals(newSched.startTime, sched.startTime);
		assertEquals(newSched.startDate, sched.startDate);
		assertEquals(newSched.endTime, sched.endTime);
		assertEquals(newSched.endDate, sched.endDate);
		
		dao.deleteSchedule(sched);
	}
	
	//Schedules
	
	@Test
	public void testCalculateNumTimeSlots() {
		
		Schedule s = new Schedule(LocalDate.parse("2018-12-17"), LocalDate.parse("2018-12-21"), LocalTime.parse("08:00:00"), LocalTime.parse("13:00:00"), 15, "aSchedule", 0123);
		
		assertTrue(s.calculateNumTimeSlots(s.startTime, s.endTime, s.slotDuration) == 20);
	}
	
}
