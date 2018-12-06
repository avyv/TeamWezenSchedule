// Team Wezen

package com.schedule.wezen.demo.http;
import java.util.ArrayList;

import com.schedule.wezen.model.TimeSlot;

public class CreateScheduleResponse {
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
	
	public CreateScheduleResponse (String sdow, String st, String id, int sd, int sc, int nsd, String ssd, String sed, ArrayList<TimeSlot> wts, String resp, int code) {
		this.responseStartDateOfWeek = sdow;
		this.responseStartTime = st;
		this.responseID = id;
		this.responseSlotDuration = sd;
		this.responseSecretCode = sc;
		this.responseNumSlotsDay = nsd;
		this.responseScheduleStartDate = ssd;
		this.responseScheduleEndDate = sed;
		this.responseWeeklyTimeSlots = wts;
		this.response = resp;
		this.httpCode = code;
	}
	
	public CreateScheduleResponse (String sdow, String st, String id, int sd, int sc, int nsd, String ssd, String sed, ArrayList<TimeSlot> wts, String resp) {
		this.responseStartDateOfWeek = sdow;
		this.responseStartTime = st;
		this.responseID = id;
		this.responseSlotDuration = sd;
		this.responseSecretCode = sc;
		this.responseNumSlotsDay = nsd;
		this.responseScheduleStartDate = ssd;
		this.responseScheduleEndDate = sed;
		this.responseWeeklyTimeSlots = wts;
		this.response = resp;
		this.httpCode = 200;
	}
	
	public CreateScheduleResponse (String resp, int code) {
		this.responseStartDateOfWeek = "";
		this.responseStartTime = "";
		this.responseID = "";
		this.responseSlotDuration = 0;
		this.responseSecretCode = 0;
		this.responseNumSlotsDay = 0;
		this.responseScheduleStartDate = "";
		this.responseScheduleEndDate = "";
		this.responseWeeklyTimeSlots = null;
		this.response = resp;
		this.httpCode = code;
	}
	
	public String toString() {
		return "CreateScheduleResponse(" + this.response + ")";
	}
}