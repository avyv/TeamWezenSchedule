package com.schedule.wezen.demo.http;

import java.util.ArrayList;

import com.schedule.wezen.model.TimeSlot;

public class CancelMeetingResponse {
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
	
	public CancelMeetingResponse(String n, String resp, int code) {
		this.meetingNameResponse = n;
		this.cancelResponse = resp;
		this.httpCode = code;
	}
	
	public CancelMeetingResponse(String n, String resp) {
		this.meetingNameResponse = n;
		this.cancelResponse = resp;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "CancelMeetingResponse(" + meetingNameResponse + " " + cancelResponse + ")";
	}
}

// may need alterations