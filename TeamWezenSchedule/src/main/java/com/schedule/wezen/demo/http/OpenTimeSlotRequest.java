package com.schedule.wezen.demo.http;

public class OpenTimeSlotRequest {
	public final String requestSchedID;
	public final String requestWeekStart;
	public final String requestTSId;
	
	public OpenTimeSlotRequest(String sid, String ws, String tsid) {
		this.requestSchedID = sid;
		this.requestWeekStart = ws;
		this.requestTSId = tsid;
	}
	
	public String toString() {
		return "OpenTimeSlot( schedule with ID: " + requestSchedID + ", in timeslot with ID: " + requestTSId +")";
	}
}