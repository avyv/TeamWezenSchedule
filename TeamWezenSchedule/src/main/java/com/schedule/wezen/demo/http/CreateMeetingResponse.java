package com.schedule.wezen.demo.http;

import java.util.ArrayList;
import com.schedule.wezen.model.TimeSlot;

public class CreateMeetingResponse {
	public final String responseStartDateOfWeek;
	public final String responseStartTime;
	public final String responseID;
	public final int responseSlotDuration;
	public final String responseSecretCode;
	public final int responseNumSlotsDay;
	public final String responseScheduleStartDate;
	public final String responseScheduleEndDate;
	public final ArrayList<TimeSlot> responseWeeklyTimeSlots;
	public final String response; // necessary
	public final int httpCode;
	
	public CreateMeetingResponse (String sdow, String st, String id, int sd, String sc, int nsd, ArrayList<TimeSlot> ts, String resp, int code) {
		this.responseStartDateOfWeek = sdow;
		this.responseStartTime = st;
		this.responseID = id;
		this.responseSlotDuration = sd;
		this.responseSecretCode = sc;
		this.responseNumSlotsDay = nsd;
		this.responseWeeklyTimeSlots = ts;
		this.response = resp;
		this.httpCode = code;
	}
	
	public CreateMeetingResponse(String sdow, String st, String id, int sd, String sc, int nsd, ArrayList<TimeSlot> ts, String resp) {
		this.responseStartDateOfWeek = sdow;
		this.responseStartTime = st;
		this.responseID = id;
		this.responseSlotDuration = sd;
		this.responseSecretCode = sc;
		this.responseNumSlotsDay = nsd;
		this.responseWeeklyTimeSlots = ts;
		this.response = resp;
		this.httpCode = 200;
	}
	
	public CreateMeetingResponse(int code) {
		this.responseStartDateOfWeek = "";
		this.responseStartTime = "";
		this.responseID = "";
		this.responseSlotDuration = 0;
		this.responseSecretCode = "";
		this.responseNumSlotsDay = 0;
		this.responseWeeklyTimeSlots = new ArrayList<TimeSlot>();
		this.response = "";
		this.httpCode = code;
	}
	
	
	public String toString() {
		return "CreateMeetingResponse(Meeting : " + response + ")";
	}
}