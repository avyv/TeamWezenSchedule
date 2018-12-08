package com.schedule.wezen.model;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

import com.schedule.wezen.db.TimeSlotsDAO;

import junit.framework.TestCase;

public class TimeSlotTests extends TestCase{
	TimeSlotsDAO dao;
	TimeSlot testSlot;
	
	@Override
	protected void setUp() throws Exception {
		dao = new TimeSlotsDAO();
		testSlot = createTimeSlot();
	}

	
	//DAO
	@Test
	public void testDAO() throws Exception {
		dao.addTimeSlot(testSlot);
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
		LocalDate slotDate = LocalDate.parse("2018-12-13");
		String id = "test1";
		String sid = "1";
		int secretCode = 1234;
		boolean isOpen = true;
		boolean hasMeeting = false;
		mySlot = new TimeSlot(startTime, slotDate, id, sid, secretCode, isOpen, hasMeeting);
		return mySlot;
	}
}
