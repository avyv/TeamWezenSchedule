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
		String stringDate = "12/12/2017";
		LocalDate date = model.stringToDate(stringDate);
		assertEquals(date.getYear(), 2017);
	}

	@Test
	public void testStringDate2() throws Exception {
		Model model = new Model();
		String stringDate = "12/12/2017";
		LocalDate date = model.stringToDate(stringDate);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		assertEquals(date.format(formatter), stringDate);
	}
}
