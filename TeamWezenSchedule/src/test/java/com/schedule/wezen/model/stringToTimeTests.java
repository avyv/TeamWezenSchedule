package com.schedule.wezen.model;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

public class stringToTimeTests {
	public stringToTimeTests() {}
	
	@Test
	public void testStringTime() throws Exception {
		Model model = new Model();
		String stringTime = "09:15";
		LocalTime time = model.stringToTime(stringTime);
		assertEquals(time.getHour(), 9);
	}

	@Test
	public void testStringTime2() throws Exception {
		Model model = new Model();
		String stringTime = "09:15";
		LocalTime time = model.stringToTime(stringTime);
		assertEquals(time.toString(), stringTime);
	}
}
