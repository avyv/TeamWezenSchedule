package com.schedule.wezen.demo.http;

import java.util.ArrayList;

import com.schedule.wezen.model.TimeSlot;

public class OpenTimeSlotResponse {
	public final String responseStartDateOfWeek;
	public final String responseStartTime;
	public final String responseID;
	public final int responseSlotDuration;
	public final int responseSecretCode;
	public final int responseNumSlotsDay;
	public final String responseScheduleStartDate;
	public final String responseScheduleEndDate;
	public final ArrayList<TimeSlot> responseWeeklyTimeSlots;
	public final String response;
	public final int httpCode;
	
	public OpenTimeSlotResponse (String sdow, String st, String id, int sd, int sc, int nsd, String ssd, String sed, ArrayList<TimeSlot> wts, String response, int code) {
		this.responseStartDateOfWeek = sdow;
		this.responseStartTime = st;
		this.responseID = id;
		this.responseSlotDuration = sd;
		this.responseSecretCode = sc;
		this.responseNumSlotsDay = nsd;
		this.responseScheduleStartDate = ssd;
		this.responseScheduleEndDate = sed;
		this.responseWeeklyTimeSlots = wts;
		this.response = response;
		this.httpCode = code;
	}
	
	public OpenTimeSlotResponse (String sdow, String st, String id, int sd, int sc, int nsd, String ssd, String sed, ArrayList<TimeSlot> wts, String response) {
		this.responseStartDateOfWeek = sdow;
		this.responseStartTime = st;
		this.responseID = id;
		this.responseSlotDuration = sd;
		this.responseSecretCode = sc;
		this.responseNumSlotsDay = nsd;
		this.responseScheduleStartDate = ssd;
		this.responseScheduleEndDate = sed;
		this.responseWeeklyTimeSlots = wts;
		this.response = response;
		this.httpCode = 200;
	}
	
	public OpenTimeSlotResponse (String response, int code) {
		this.responseStartDateOfWeek = "";
		this.responseStartTime = "";
		this.responseID = "";
		this.responseSlotDuration = 0;
		this.responseSecretCode = 0;
		this.responseNumSlotsDay = 0;
		this.responseScheduleStartDate = "";
		this.responseScheduleEndDate = "";
		this.responseWeeklyTimeSlots = null;
		this.response = response;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "OpenTimeSlotResponse(" + response + ")";
	}
	
}