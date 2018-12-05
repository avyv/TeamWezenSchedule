package com.schedule.wezen.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

import org.junit.Test;

public class stringToDateTests {
	public stringToDateTests() {}
	
	@Test
	public void testStringDate() throws Exception {
		Model model = new Model();
		String stringDate = "2017-12-12";
		LocalDate date = model.stringToDate(stringDate);
		assertEquals(date.getYear(), 2017);
	}

	@Test
	public void testStringDate2() throws Exception {
		Model model = new Model();
		String stringDate = "2017-12-12";
		LocalDate date = model.stringToDate(stringDate);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		assertEquals(date.format(formatter), stringDate);
	}
}
