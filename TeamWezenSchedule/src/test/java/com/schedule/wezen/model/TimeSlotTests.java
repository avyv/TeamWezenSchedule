package com.schedule.wezen.model;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

import com.schedule.wezen.db.TimeSlotsDAO;

import junit.framework.TestCase;

public class TimeSlotTests extends TestCase {
	TimeSlotsDAO dao;
	TimeSlot testSlot;
	
	@Override
	protected void setUp() throws Exception {
		dao = new TimeSlotsDAO();
		testSlot = createTimeSlot();
		dao.deleteAllTimeSlots();
	}
	
	//DAO
	@Test
	public void testAddGetDeleteTimeSlot() throws Exception{
		dao.addTimeSlot(testSlot);
		
		dao.addTimeSlot(testSlot);
		
		TimeSlot returnedSlot = dao.getTimeSlot(testSlot);
		assertTrue(testSlot.id.equals(returnedSlot.id));
		assertTrue(testSlot.secretCode == returnedSlot.secretCode);
		assertTrue(testSlot.sid.equals(returnedSlot.sid));
		assertTrue(testSlot.hasMeeting == returnedSlot.hasMeeting);
		assertTrue(testSlot.isOpen == returnedSlot.isOpen);
		assertTrue(testSlot.meetingName.equals(returnedSlot.meetingName));
		assertTrue(testSlot.slotDate.isEqual(returnedSlot.slotDate));
		assertTrue(testSlot.startTime == returnedSlot.startTime);
		
		dao.deleteTimeSlot(testSlot);
	}
	
	@Test
	public void testAddGetAll() throws Exception{
		dao.addTimeSlot(testSlot);
		TimeSlot alsoTestSlot = new TimeSlot(LocalTime.parse("11:00:00"), LocalDate.parse("2018-12-17"), "1 11:00:00 2018-12-17", "1", 2345);
		dao.addTimeSlot(alsoTestSlot);
		
		TimeSlot[] toTest = new TimeSlot[2];
		toTest[0] = dao.getAllTimeSlots().get(0);
		toTest[1] = dao.getAllTimeSlots().get(1);
		
		assertTrue(testSlot.id.equals(toTest[0].id));
		assertTrue(testSlot.secretCode == toTest[0].secretCode);
		assertTrue(testSlot.sid.equals(toTest[0].sid));
		assertTrue(testSlot.hasMeeting == toTest[0].hasMeeting);
		assertTrue(testSlot.isOpen == toTest[0].isOpen);
		assertTrue(testSlot.meetingName.equals(toTest[0].meetingName));
		assertTrue(testSlot.slotDate.isEqual(toTest[0].slotDate));
		assertTrue(testSlot.startTime == toTest[0].startTime);
		
		assertTrue(alsoTestSlot.id.equals(toTest[1].id));
		assertTrue(alsoTestSlot.secretCode == toTest[1].secretCode);
		assertTrue(alsoTestSlot.sid.equals(toTest[1].sid));
		assertTrue(alsoTestSlot.hasMeeting == toTest[1].hasMeeting);
		assertTrue(alsoTestSlot.isOpen == toTest[1].isOpen);
		assertTrue(alsoTestSlot.meetingName.equals(toTest[1].meetingName));
		assertTrue(alsoTestSlot.slotDate.isEqual(toTest[1].slotDate));
		assertTrue(alsoTestSlot.startTime == toTest[1].startTime);
		
		dao.getAllScheduleTimeSlots("1");
		
		dao.deleteAllScheduleTimeSlots("1");
	}

	//TimeSlot
	@Test
	public void testSetMeeting() {
		testSlot.setMeeting("howdy");
		
		assertEquals(testSlot.meetingName, "howdy");
		assertEquals(testSlot.hasMeeting, true);
		assertEquals(testSlot.isOpen, false);
	}
	
	@Test
	public void testIsCorrectCode() {
		assertTrue(testSlot.isCorrectCode(1234));
		
		assertFalse(testSlot.isCorrectCode(0123));
	}
	
	public void testDeleteMeeting() {
		testSlot.setMeeting("howdy");
		assertTrue(testSlot.deleteMeeting());
		
		assertEquals(testSlot.meetingName, " ");
		assertEquals(testSlot.isOpen, true);
		assertEquals(testSlot.hasMeeting, false);
		
		assertFalse(testSlot.deleteMeeting());
	}
	
	public void testCreateMeeting() {
		assertTrue(testSlot.createMeeting("help"));
		
		assertEquals(testSlot.meetingName, "help");
		assertEquals(testSlot.isOpen, false);
		assertEquals(testSlot.hasMeeting, true);
		
		assertFalse(testSlot.createMeeting("me"));
	}
	
	
	TimeSlot createTimeSlot() {
		TimeSlot mySlot;
		LocalTime startTime = LocalTime.parse("03:00:00");
		LocalDate slotDate = LocalDate.parse("2018-12-17");
		String id = "1 03:00:00 2018-12-14";
		String sid = "1";
		int secretCode = 1234;
		boolean isOpen = true;
		boolean hasMeeting = false;
		mySlot = new TimeSlot(startTime, slotDate, id, sid, secretCode, isOpen, hasMeeting);
		return mySlot;
	}
}
