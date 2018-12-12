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
	
	public ArrayList<TimeSlot> displayed(Schedule s) {
		ArrayList<TimeSlot> disp = new ArrayList<TimeSlot>();
		for(TimeSlot ts: s.timeSlots) {
			if(ts.isDisplayed) {
				disp.add(ts);
			}
		}
		return disp;
	}
	
//	@Test
//	public void testSearchForTime() {
//		
//		big.searchForTime("0", "0", "0", "", "0");
//		ArrayList<TimeSlot> sort1 = displayed(big);
//		
//		assertEquals(sort1.size(), big.timeSlots.size());
//		
//		big.searchForTime("1", "0", "0", "", "0");
//		ArrayList<TimeSlot> sort2 = displayed(big);
//		
//		assertEquals(sort2.size(), 2*(60/15)*31);
//		for(TimeSlot ts: sort2) {
//			assertEquals(ts.slotDate.getMonthValue(), 1);
//		}
//		
//		big.searchForTime("0", "2018", "0", "", "0");
//		ArrayList<TimeSlot> sort3 = displayed(big);
//		
//		assertEquals(sort3.size(), 2*(60/15)*(16+6));
//		for(TimeSlot ts: sort3) {
//			assertEquals(ts.slotDate.getYear(), 2018);
//		}
//		
//		big.searchForTime("0", "0", "2", "", "0");
//		ArrayList<TimeSlot> sort4 = displayed(big);
//		
//		assertEquals(sort4.size(), 2*(60/15)*55);
//		for(TimeSlot ts: sort4) {
//			assertEquals(ts.slotDate.getDayOfWeek().getValue(), 2);
//		}
//		
//		big.searchForTime("0", "0", "0", "15", "0");
//		ArrayList<TimeSlot> sort5 = displayed(big);
//		
//		//searches for date not day of month
//		//assertEquals(sort5.size(), 2*(60/15)*13);
//		for(TimeSlot ts: sort5) {
//			//assertEquals(ts.slotDate.getDayOfMonth(), 15);
//		}
//		
//		big.searchForTime("0", "0", "0", "", "09:15");
//		ArrayList<TimeSlot> sort6 = displayed(big);
//		
//		assertEquals(sort6.size(), 385);
//		for(TimeSlot ts: sort6) {
//			assertTrue(ts.startTime.equals(LocalTime.parse("09:15")));
//		}
//		
//		big.searchForTime("2", "2019", "3", "6/2/2019", "08:30");
//		ArrayList<TimeSlot> sort7 = displayed(big);
//		
//		assertEquals(sort7.size(), 1);
//		for(TimeSlot ts: sort7) {
//			assertEquals(ts.slotDate.getMonthValue(), 2);
//			assertEquals(ts.slotDate.getYear(), 2019);
//			assertEquals(ts.slotDate.getDayOfWeek().getValue(), 3);
//			assertEquals(ts.slotDate.getDayOfMonth(), 6);
//			assertTrue(ts.startTime.equals(LocalTime.parse("08:30")));
//		}
//		
//		big.searchForTime("2", "2019", "3", "", "08:30");
//		ArrayList<TimeSlot> sort8 = displayed(big);
//		
//		assertEquals(sort8.size(), 0);
//	}
	
	@Test
	public void testSearchForTime() {
		
		Schedule s1 = new Schedule(LocalDate.parse("2018-12-17"), LocalDate.parse("2018-12-21"), LocalTime.parse("08:00:00"), LocalTime.parse("13:00:00"), 15, "aSchedule", 0123);
		s1.searchForTime("0", "0", "0", "0", "0");
		ArrayList<TimeSlot> sort1 = displayed(s1);
		
		assertEquals(sort1.size(), s1.timeSlots.size());
		assertEquals(sort1.size(), 140);
		
		Schedule s2 = new Schedule(LocalDate.parse("2017-12-17"), LocalDate.parse("2018-12-21"), LocalTime.parse("08:00:00"), LocalTime.parse("13:00:00"), 15, "aSchedule", 0123);
		s2.searchForTime("1", "0", "0", "0", "0");
		ArrayList<TimeSlot> sort2 = displayed(s2);
		
		assertEquals(sort2.size(), 31*20);
		for(TimeSlot ts: sort2) {
			assertEquals(ts.slotDate.getMonthValue(), 1);
		}
		
		Schedule s3 = new Schedule(LocalDate.parse("2017-12-17"), LocalDate.parse("2018-12-21"), LocalTime.parse("08:00:00"), LocalTime.parse("13:00:00"), 15, "aSchedule", 0123);
		s3.searchForTime("0", "2018", "0", "0", "0");
		ArrayList<TimeSlot> sort3 = displayed(s3);
		
		assertEquals(sort3.size(), 357*20);
		for(TimeSlot ts: sort3) {
			assertEquals(ts.slotDate.getYear(), 2018);
		}
		
		Schedule s4 = new Schedule(LocalDate.parse("2017-12-17"), LocalDate.parse("2018-12-21"), LocalTime.parse("08:00:00"), LocalTime.parse("13:00:00"), 15, "aSchedule", 0123);
		s4.searchForTime("0", "0", "2", "0", "0");
		ArrayList<TimeSlot> sort4 = displayed(s4);
		
		assertEquals(sort4.size(), 54*20);
		for(TimeSlot ts: sort4) {
			assertEquals(ts.slotDate.getDayOfWeek().getValue(), 2);
		}
		
//		Schedule s5 = new Schedule(LocalDate.parse("2017-12-17"), LocalDate.parse("2018-12-21"), LocalTime.parse("08:00:00"), LocalTime.parse("13:00:00"), 15, "aSchedule", 0123);
//		s5.searchForTime("0", "0", "0", "15", "0");
//		ArrayList<TimeSlot> sort5 = displayed(s5);
//		
//		assertEquals(sort5.size(), 13*20);
//		for(TimeSlot ts: sort5) {
//			assertEquals(ts.slotDate.getDayOfMonth(), 15);
//		}
		
		Schedule s6 = new Schedule(LocalDate.parse("2017-12-17"), LocalDate.parse("2018-12-21"), LocalTime.parse("08:00:00"), LocalTime.parse("13:00:00"), 15, "aSchedule", 0123);
		s6.searchForTime("0", "0", "0", "0", "09:15");
		ArrayList<TimeSlot> sort6 = displayed(s6);
		
		assertEquals(sort6.size(), 378);
		for(TimeSlot ts: sort6) {
			assertEquals(ts.startTime, LocalTime.parse("09:15:00"));
		}
		
//		Schedule s7 = new Schedule(LocalDate.parse("2017-12-17"), LocalDate.parse("2018-12-21"), LocalTime.parse("08:00:00"), LocalTime.parse("13:00:00"), 15, "aSchedule", 0123);
//		s7.searchForTime("2", "2018", "0", "5", "08:30:00");
//		ArrayList<TimeSlot> sort7 = displayed(s7);
//		
//		assertEquals(sort7.size(), 1);
//		for(TimeSlot ts: sort7) {
//			assertEquals(ts.slotDate.getMonthValue(), 2);
//			assertEquals(ts.slotDate.getYear(), 2018);
//			assertEquals(ts.slotDate.getDayOfMonth(), 5);
//			assertEquals(ts.startTime, LocalTime.parse("08:30:00"));
//		}
		
		Schedule s8 = new Schedule(LocalDate.parse("2017-12-17"), LocalDate.parse("2018-12-21"), LocalTime.parse("08:00:00"), LocalTime.parse("13:00:00"), 15, "aSchedule", 0123);
		s8.searchForTime("11", "2018", "2", "0", "12:45");
		ArrayList<TimeSlot> sort8 = displayed(s8);
		
		assertEquals(sort8.size(), 4);
		for(TimeSlot ts: sort8) {
			assertEquals(ts.slotDate.getMonthValue(), 11);
			assertEquals(ts.slotDate.getYear(), 2018);
			assertEquals(ts.slotDate.getDayOfWeek().getValue(), 2);
			assertEquals(ts.startTime, LocalTime.parse("12:45"));
		}
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
	public void testEditTimeSlots() {
		
		Schedule s = new Schedule(LocalDate.parse("2018-12-17"), LocalDate.parse("2018-12-21"), LocalTime.parse("08:00:00"), LocalTime.parse("13:00:00"), 15, "aSchedule", 0123);
		
		s.editTimeSlots("close", LocalDate.parse("2018-12-20"), null);
		for(TimeSlot ts:s.getTimeSlots()) {
			if(ts.getDate().equals(LocalDate.parse("2018-12-20"))) {
				assertEquals(ts.isOpen, false);
			}
		}
		
		s.editTimeSlots("open", LocalDate.parse("2018-12-20"), null);
		for(TimeSlot ts:s.getTimeSlots()) {
			if(ts.getDate().equals(LocalDate.parse("2018-12-20"))) {
				assertEquals(ts.isOpen, true);
			}
		}
		
		s.editTimeSlots("close", null, LocalTime.parse("10:15:00"));
		for(TimeSlot ts:s.getTimeSlots()) {
			if(ts.getStartTime().equals(LocalTime.parse("10:15:00"))) {
				assertEquals(ts.isOpen, false);
			}
		}
		
		s.editTimeSlots("open", null, LocalTime.parse("10:15:00"));
		for(TimeSlot ts:s.getTimeSlots()) {
			if(ts.getStartTime().equals(LocalTime.parse("10:15:00"))) {
				assertEquals(ts.isOpen, true);
			}
		}
		
		s.editTimeSlots("close", LocalDate.parse("2018-12-20"), LocalTime.parse("10:15:00"));
		for(TimeSlot ts:s.getTimeSlots()) {
			if(ts.getDate().equals(LocalDate.parse("2018-12-20")) && ts.getStartTime().equals(LocalTime.parse("10:15:00"))) {
				assertEquals(ts.isOpen, false);
			}
		}
		
		s.editTimeSlots("open", LocalDate.parse("2018-12-20"), LocalTime.parse("12:15:00"));
		for(TimeSlot ts:s.getTimeSlots()) {
			if(ts.getDate().equals(LocalDate.parse("2018-12-20")) && ts.getStartTime().equals(LocalTime.parse("12:15:00"))) {
				assertEquals(ts.isOpen, true);
			}
		}
	}

}
