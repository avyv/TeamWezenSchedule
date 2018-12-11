package com.schedule.wezen.model;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

public class meetingTests {
	TimeSlot testSlot;
	@Test
	public void testMeetingsInit1() {
		LocalTime startTime = LocalTime.parse("03:00:00");
		LocalDate slotDate = LocalDate.parse("2018-12-13");
		String id = "test1";
		String sid = "1";
		int secretCode = 1234;
		boolean isOpen = true;
		boolean hasMeeting = false;
		int index = 0;
		testSlot = new TimeSlot(startTime, slotDate, id, sid, secretCode, isOpen, hasMeeting, index);
		
		assertEquals(startTime, testSlot.getStartTime());
		assertEquals(slotDate, testSlot.getDate());
		assertEquals(id, testSlot.getId());
		assertEquals(sid, testSlot.getSid());
		assertEquals(secretCode, testSlot.getSecretCode());
		assertEquals(isOpen, testSlot.getIsOpen());
		assertEquals(hasMeeting, testSlot.getHasMeeting());
		assertEquals(" ", testSlot.getMeeting());
	}
	
	@Test
	public void testMeetingsInit2() {
		LocalTime startTime = LocalTime.parse("03:00:00");
		LocalDate slotDate = LocalDate.parse("2018-12-13");
		String id = "test1";
		String sid = "1";
		int secretCode = 1234;
		boolean isOpen = true;
		boolean hasMeeting = false;
		int index = 0;
		testSlot = new TimeSlot(startTime, slotDate, id, sid, secretCode, index);
		
		assertEquals(startTime, testSlot.getStartTime());
		assertEquals(slotDate, testSlot.getDate());
		assertEquals(id, testSlot.getId());
		assertEquals(sid, testSlot.getSid());
		assertEquals(secretCode, testSlot.getSecretCode());
		assertEquals(isOpen, testSlot.getIsOpen());
		assertEquals(hasMeeting, testSlot.getHasMeeting());
		assertEquals(" ", testSlot.getMeeting());
	}
	
	@Test
	public void testMeetingsInit3() {
		LocalTime startTime = LocalTime.parse("03:00:00");
		LocalDate slotDate = LocalDate.parse("2018-12-13");
		String id = "test1";
		String sid = "1";
		int secretCode = 1234;
		boolean isOpen = true;
		boolean hasMeeting = false;
		String meetingName = "Meeting";
		int index = 0;
		testSlot = new TimeSlot(startTime, slotDate, id, meetingName, sid, secretCode, isOpen, hasMeeting, index);

		
		assertEquals(startTime, testSlot.getStartTime());
		assertEquals(slotDate, testSlot.getDate());
		assertEquals(id, testSlot.getId());
		assertEquals(sid, testSlot.getSid());
		assertEquals(secretCode, testSlot.getSecretCode());
		assertEquals(isOpen, testSlot.getIsOpen());
		assertEquals(hasMeeting, testSlot.getHasMeeting());
		assertEquals("Meeting", testSlot.getMeeting());
	}

	@Test
	public void testMeetingsFunctionality() {
		LocalTime startTime = LocalTime.parse("03:00:00");
		LocalDate slotDate = LocalDate.parse("2018-12-13");
		String id = "test1";
		String sid = "1";
		int secretCode = 1234;
		boolean isOpen = true;
		boolean hasMeeting = false;
		int index = 0;
		testSlot = new TimeSlot(startTime, slotDate, id, sid, secretCode, isOpen, hasMeeting, index);
		//test setting open or closed
		testSlot.setIsOpen(false);
		assertFalse(testSlot.getIsOpen());
		testSlot.setIsOpen(true);
		assertTrue(testSlot.getIsOpen());
		
		//test create and delete a meeting
		testSlot.setIsOpen(false);
		assertFalse(testSlot.createMeeting("ImAMeeting"));
		testSlot.setIsOpen(true);
		assertTrue(testSlot.createMeeting("ImAMeeting"));
		assertEquals("ImAMeeting", testSlot.getMeeting());
		assertTrue(testSlot.getHasMeeting());
		

		testSlot.setIsOpen(true);
		assertFalse(testSlot.deleteMeeting());
		testSlot.setIsOpen(false);
		assertTrue(testSlot.deleteMeeting());
		assertEquals(" ",testSlot.getMeeting());
		assertFalse(testSlot.getHasMeeting());
		assertTrue(testSlot.getIsOpen());

	}

	
}
