package com.schedule.wezen.demo.http;

import java.util.ArrayList;

import com.schedule.wezen.model.TimeSlot;

public class CancelMeetingResponse {
	public final String responseStartDateOfWeek;
	public final String responseStartTime;
	public final String responseID;
	public final int responseSlotDuration;
	public final int responseSecretCode;
	public final int responseNumSlotsDay;
	public final String responseScheduleStartDate;
	public final String responseScheduleEndDate;
	public final ArrayList<TimeSlot> responseWeeklyTimeSlots;
	public final String response; // necessary
	public final int httpCode;
	
	public CancelMeetingResponse(String startDateOfWeek, String startTime, String id, int slotDuration, int secretCode, int numSlotsDay, String scheduleStartDate, String scheduleEndDate, ArrayList<TimeSlot> weeklyTimeSlots, String response, int code) {
		this.responseStartDateOfWeek = startDateOfWeek;
		this.responseStartTime = startTime;
		this.responseID = id;
		this.responseSlotDuration = slotDuration;
		this.responseSecretCode = secretCode;
		this.responseNumSlotsDay = numSlotsDay;
		this.responseScheduleStartDate = scheduleStartDate;
		this.responseScheduleEndDate = scheduleEndDate;
		this.responseWeeklyTimeSlots = weeklyTimeSlots;
		this.response = response;
		this.httpCode = code;
	}
	
	public CancelMeetingResponse(String response, int code) {
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
		this.httpCode = code;
	}
	
	public String toString() {
		return "CancelMeetingResponse(" + this.response + ")";
	}
}

// may need alterations