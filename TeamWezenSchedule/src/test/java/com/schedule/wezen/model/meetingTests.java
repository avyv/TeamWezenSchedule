package com.schedule.wezen.model;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

public class meetingTests {
	TimeSlot testSlot;
	@Test
	public void testMeetings() {
		LocalTime startTime = LocalTime.parse("03:00:00");
		LocalDate slotDate = LocalDate.parse("2018-12-13");
		String id = "test1";
		String sid = "1";
		int secretCode = 1234;
		boolean isOpen = true;
		boolean hasMeeting = false;
		testSlot = new TimeSlot(startTime, slotDate, id, sid, secretCode, isOpen, hasMeeting);
		
		assertEquals(startTime, testSlot.getStartTime());
		assertEquals(slotDate, testSlot.getDate());
		assertEquals(id, testSlot.getId());
		assertEquals(sid, testSlot.getSid());
		assertEquals(secretCode, testSlot.getSecretCode());
		assertEquals(isOpen, testSlot.getIsOpen());
		assertEquals(hasMeeting, testSlot.getHasMeeting());
		
	}

}
