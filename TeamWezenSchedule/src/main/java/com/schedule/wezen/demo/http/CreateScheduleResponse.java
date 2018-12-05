// Team Wezen

package com.schedule.wezen.demo.http;
import java.util.ArrayList;

import com.schedule.wezen.model.TimeSlot;

public class CreateScheduleResponse {
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
	
	public CreateScheduleResponse (String resp, String sd, String ed, String st, int duration, int ns, int secretCode, ArrayList<TimeSlot> ts, int code) {
		this.createScheduleResponse = resp;
		this.responseStartDate = sd;
		this.responseEndDate = ed;
		this.responseStartTime = st;
		this.responseSlotDuration = duration;
		this.responseNumSlotsDay = ns;
		this.secretCode = secretCode;
		this.responseTimeSlots = ts;
		this.httpCode = code;
	}
	
	public CreateScheduleResponse (String resp, String sd, String ed, String st, int duration, int ns, int secretCode, ArrayList<TimeSlot> ts) {
		this.createScheduleResponse = resp;
		this.responseStartDate = sd;
		this.responseEndDate = ed;
		this.responseStartTime = st;
		this.responseSlotDuration = duration;
		this.responseNumSlotsDay = ns;
		this.secretCode = secretCode;
		this.responseTimeSlots = ts;
		this.httpCode = 200;
	}
	
	public CreateScheduleResponse (String resp, int code) {
		this.createScheduleResponse = resp;
		this.responseStartDate = null;
		this.responseEndDate = null;
		this.responseStartTime = null;
		this.responseSlotDuration = 0;
		this.responseNumSlotsDay = 0;
		this.httpCode = code;
	}
	
	public String toString() {
		return "CreateScheduleResponse(" + createScheduleResponse + ")";
	}
}