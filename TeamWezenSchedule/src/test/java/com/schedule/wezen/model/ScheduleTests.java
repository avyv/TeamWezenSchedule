package com.schedule.wezen.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

import org.junit.Test;

import com.schedule.wezen.db.SchedulesDAO;
import com.schedule.wezen.db.TimeSlotsDAO;

import junit.framework.TestCase;

public class ScheduleTests extends TestCase{
	SchedulesDAO dao;
	Schedule sched, big;
	
	
	@Override
	protected void setUp() {
		dao = new SchedulesDAO();
		sched = new Schedule(LocalDate.parse("2018-12-15"), LocalDate.parse("2018-12-25"), LocalTime.parse("08:00:00"), LocalTime.parse("10:00:00"), 20, "thisSchedule", 123456);
		big = new Schedule(LocalDate.parse("2018-12-15"), LocalDate.parse("2019-12-25"), LocalTime.parse("08:00:00"), LocalTime.parse("10:00:00"), 15, "bigSchedule", 10101);
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
		assertEquals(newSched.startDate.plusDays(1), sched.startDate);
		assertEquals(newSched.endTime, sched.endTime);
		assertEquals(newSched.endDate.plusDays(1), sched.endDate);
		
		dao.deleteSchedule(sched.id);
	}
	
	//Schedules
	
	@Test
	public void testCalculateNumTimeSlots() {
		
		Schedule s = new Schedule(LocalDate.parse("2018-12-17"), LocalDate.parse("2018-12-21"), LocalTime.parse("08:00:00"), LocalTime.parse("13:00:00"), 15, "aSchedule", 0123);
		
		assertTrue(s.calculateNumTimeSlots(s.startTime, s.endTime, s.slotDuration) == 20);
	}
	
	
	@Test
	public void testChangeDuration() {
		
		Schedule s = new Schedule(LocalDate.parse("2018-12-17"), LocalDate.parse("2018-12-21"), LocalTime.parse("08:00:00"), LocalTime.parse("13:00:00"), 15, "aSchedule", 0123);
		
		assertTrue(s.getTimeSlots().size() == 140);
		
		s.changeDuration(LocalDate.parse("2018-12-11"), LocalDate.parse("2018-12-25"));
		
		assertEquals(s.getTimeSlots().size(), 420);
		assertEquals(s.getStartDate(), LocalDate.parse("2018-12-11"));
		assertEquals(s.getEndDate(), LocalDate.parse("2018-12-25"));
		
		Schedule s1 = new Schedule(LocalDate.parse("2018-12-17"), LocalDate.parse("2018-12-21"), LocalTime.parse("08:00:00"), LocalTime.parse("13:00:00"), 15, "aSchedule", 0123);
		s1.changeDuration(LocalDate.parse("2018-12-17"), LocalDate.parse("2018-12-21"));
		
		assertEquals(s1.getTimeSlots().size(), 140);
		assertEquals(s1.getStartDate(), LocalDate.parse("2018-12-17"));
		assertEquals(s1.getEndDate(), LocalDate.parse("2018-12-21"));
		
		Schedule s2 = new Schedule(LocalDate.parse("2018-12-17"), LocalDate.parse("2018-12-21"), LocalTime.parse("08:00:00"), LocalTime.parse("13:00:00"), 15, "aSchedule", 0123);
		s2.changeDuration(LocalDate.parse("2018-12-17"), LocalDate.parse("2018-12-29"));
		
		assertEquals(s2.getTimeSlots().size(), 280);
		assertEquals(s2.getStartDate(), LocalDate.parse("2018-12-17"));
		assertEquals(s2.getEndDate(), LocalDate.parse("2018-12-29"));
		
		Schedule s3 = new Schedule(LocalDate.parse("2018-12-19"), LocalDate.parse("2018-12-21"), LocalTime.parse("08:00:00"), LocalTime.parse("13:00:00"), 15, "aSchedule", 0123);
		s3.changeDuration(LocalDate.parse("2018-12-11"), LocalDate.parse("2018-12-21"));
		
		assertEquals(s3.getTimeSlots().size(), 280);
		assertEquals(s3.getStartDate(), LocalDate.parse("2018-12-11"));
		assertEquals(s3.getEndDate(), LocalDate.parse("2018-12-21"));
		
		Schedule s4 = new Schedule(LocalDate.parse("2018-12-19"), LocalDate.parse("2018-12-21"), LocalTime.parse("08:00:00"), LocalTime.parse("13:00:00"), 15, "aSchedule", 0123);
		s4.changeDuration(LocalDate.parse("2018-12-07"), LocalDate.parse("2018-12-31"));
		
		assertEquals(s4.getTimeSlots().size(), 700);
		assertEquals(s4.getStartDate(), LocalDate.parse("2018-12-07"));
		assertEquals(s4.getEndDate(), LocalDate.parse("2018-12-31"));
	}
	
	@Test
	public void testSearchForTime() {
		
		ArrayList<TimeSlot> sort1 = big.searchForTime(0, 0, 0, 0, LocalTime.parse("00:00:01"));
		
		assertEquals(sort1.size(), big.timeSlots.size());
		
		ArrayList<TimeSlot> sort2 = big.searchForTime(1, 0, 0, 0, LocalTime.parse("00:00:01"));
		
		assertEquals(sort2.size(), 2*(60/15)*31);
		for(TimeSlot ts: sort2) {
			assertEquals(ts.slotDate.getMonthValue(), 1);
		}
		
		ArrayList<TimeSlot> sort3 = big.searchForTime(0, 2018, 0, 0, LocalTime.parse("00:00:01"));
		
		assertEquals(sort3.size(), 2*(60/15)*(16+6));
		for(TimeSlot ts: sort3) {
			assertEquals(ts.slotDate.getYear(), 2018);
		}
		
		ArrayList<TimeSlot> sort4 = big.searchForTime(0, 0, 2, 0, LocalTime.parse("00:00:01"));
		
		assertEquals(sort4.size(), 2*(60/15)*55);
		for(TimeSlot ts: sort4) {
			assertEquals(ts.slotDate.getDayOfWeek().getValue(), 2);
		}
		
		ArrayList<TimeSlot> sort5 = big.searchForTime(0, 0, 0, 15, LocalTime.parse("00:00:01"));
		
		assertEquals(sort5.size(), 2*(60/15)*13);
		for(TimeSlot ts: sort5) {
			assertEquals(ts.slotDate.getDayOfMonth(), 15);
		}
		
		ArrayList<TimeSlot> sort6 = big.searchForTime(0, 0, 0, 0, LocalTime.parse("09:15:00"));
		
		assertEquals(sort6.size(), 385);
		for(TimeSlot ts: sort6) {
			assertTrue(ts.startTime.equals(LocalTime.parse("09:15:00")));
		}
		
		ArrayList<TimeSlot> sort7 = big.searchForTime(2, 2019, 3, 6, LocalTime.parse("08:30:00"));
		
		assertEquals(sort7.size(), 1);
		for(TimeSlot ts: sort7) {
			assertEquals(ts.slotDate.getMonthValue(), 2);
			assertEquals(ts.slotDate.getYear(), 2019);
			assertEquals(ts.slotDate.getDayOfWeek().getValue(), 3);
			assertEquals(ts.slotDate.getDayOfMonth(), 6);
			assertTrue(ts.startTime.equals(LocalTime.parse("08:30:00")));
		}
		
		ArrayList<TimeSlot> sort8 = big.searchForTime(2, 2019, 3, 7, LocalTime.parse("08:30:00"));
		
		assertEquals(sort8.size(), 0);
	}

}
