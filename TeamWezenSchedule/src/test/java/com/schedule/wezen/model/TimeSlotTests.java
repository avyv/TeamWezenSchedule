package com.schedule.wezen.model;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

import com.schedule.wezen.db.TimeSlotsDAO;

public class TimeSlotTests {
	TimeSlotsDAO dao;
	TimeSlot testSlot;
	//DAO
	@Test
	public void testDAO() throws Exception {
		dao = new TimeSlotsDAO();
		testSlot = createTimeSlot();
		dao.addTimeSlot(testSlot);

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
