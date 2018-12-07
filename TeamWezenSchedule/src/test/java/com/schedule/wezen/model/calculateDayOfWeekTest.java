package com.schedule.wezen.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import com.schedule.wezen.model.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class calculateDayOfWeekTest {
	public calculateDayOfWeekTest() {}
	
	@Test
	public void testOfWeek1() throws Exception {
		Model model = new Model();
		String stringDate1 = "2017-12-04";
		String stringDate2 = "2018-01-03";
		LocalDate startDate = model.stringToDate(stringDate1);
		LocalDate endDate = model.stringToDate(stringDate2);
		String stringTime1 = "09:00:00";
		String stringTime2 = "14:00:00";
		LocalTime startTime = model.stringToTime(stringTime1);
		LocalTime endTime = model.stringToTime(stringTime2);
		int duration = 30;
		int secretCode = 1111;
		Schedule schedule = new Schedule(startDate, endDate, startTime, endTime, duration, "id", secretCode);
		assertEquals(schedule.calculateDayOfWeek(startDate),1);
	}
	
	@Test
	public void testOfWeek2() throws Exception {
		Model model = new Model();
		String stringDate1 = "2017-12-05";
		String stringDate2 = "2018-01-03";
		LocalDate startDate = model.stringToDate(stringDate1);
		LocalDate endDate = model.stringToDate(stringDate2);
		String stringTime1 = "09:00:00";
		String stringTime2 = "14:00:00";
		LocalTime startTime = model.stringToTime(stringTime1);
		LocalTime endTime = model.stringToTime(stringTime2);
		int duration = 30;
		int secretCode = 1111;
		Schedule schedule = new Schedule(startDate, endDate, startTime, endTime, duration, "id", secretCode);
		assertEquals(schedule.calculateDayOfWeek(startDate),2);
	}
	
	@Test
	public void testOfWeek3() throws Exception {
		Model model = new Model();
		String stringDate1 = "2017-12-06";
		String stringDate2 = "2018-12-02";
		LocalDate startDate = model.stringToDate(stringDate1);
		LocalDate endDate = model.stringToDate(stringDate2);
		String stringTime1 = "09:00:00";
		String stringTime2 = "14:00:00";
		LocalTime startTime = model.stringToTime(stringTime1);
		LocalTime endTime = model.stringToTime(stringTime2);
		int duration = 30;
		int secretCode = 1111;
		Schedule schedule = new Schedule(startDate, endDate, startTime, endTime, duration, "id", secretCode);
		assertEquals(schedule.calculateDayOfWeek(startDate),3);
	}
	
	@Test
	public void testOfWeek4() throws Exception {
		Model model = new Model();
		String stringDate1 = "2017-12-07";
		String stringDate2 = "2018-01-03";
		LocalDate startDate = model.stringToDate(stringDate1);
		LocalDate endDate = model.stringToDate(stringDate2);
		String stringTime1 = "09:00:00";
		String stringTime2 = "14:00:00";
		LocalTime startTime = model.stringToTime(stringTime1);
		LocalTime endTime = model.stringToTime(stringTime2);
		int duration = 30;
		int secretCode = 1111;
		Schedule schedule = new Schedule(startDate, endDate, startTime, endTime, duration, "id", secretCode);
		assertEquals(schedule.calculateDayOfWeek(startDate),4);
	}
	
	@Test
	public void testOfWeek5() throws Exception {
		Model model = new Model();
		String stringDate1 = "2017-12-08";
		String stringDate2 = "2018-01-03";
		LocalDate startDate = model.stringToDate(stringDate1);
		LocalDate endDate = model.stringToDate(stringDate2);
		String stringTime1 = "09:00:00";
		String stringTime2 = "14:00:00";
		LocalTime startTime = model.stringToTime(stringTime1);
		LocalTime endTime = model.stringToTime(stringTime2);
		int duration = 30;
		int secretCode = 1111;
		Schedule schedule = new Schedule(startDate, endDate, startTime, endTime, duration, "id", secretCode);
		assertEquals(schedule.calculateDayOfWeek(startDate),5);
	}
	
	@Test
	public void testOfWeek6() throws Exception {
		Model model = new Model();
		String stringDate1 = "2017-12-09";
		String stringDate2 = "2018-01-03";
		LocalDate startDate = model.stringToDate(stringDate1);
		LocalDate endDate = model.stringToDate(stringDate2);
		String stringTime1 = "09:00:00";
		String stringTime2 = "14:00:00";
		LocalTime startTime = model.stringToTime(stringTime1);
		LocalTime endTime = model.stringToTime(stringTime2);
		int duration = 30;
		int secretCode = 1111;
		Schedule schedule = new Schedule(startDate, endDate, startTime, endTime, duration, "id", secretCode);
		assertEquals(schedule.calculateDayOfWeek(startDate),6);
	}
	
	@Test
	public void testOfWeek7() throws Exception {
		Model model = new Model();
		String stringDate1 = "2017-12-10";
		String stringDate2 = "2018-01-03";
		LocalDate startDate = model.stringToDate(stringDate1);
		LocalDate endDate = model.stringToDate(stringDate2);
		String stringTime1 = "09:00:00";
		String stringTime2 = "14:00:00";
		LocalTime startTime = model.stringToTime(stringTime1);
		LocalTime endTime = model.stringToTime(stringTime2);
		int duration = 30;
		int secretCode = 1111;
		Schedule schedule = new Schedule(startDate, endDate, startTime, endTime, duration, "id", secretCode);
		assertEquals(schedule.calculateDayOfWeek(startDate),7);
	}
}

