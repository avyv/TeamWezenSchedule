package com.schedule.wezen.demo.http;

public class CloseTimeSlotRequest {
	public final String requestSchedID;
	public final String requestWeekStart;
	public final String requestTSId;
	
	public CloseTimeSlotRequest(String sid, String ws, String tsid) {
		this.requestSchedID = sid;
		this.requestWeekStart = ws;
		this.requestTSId = tsid;
	}
	
	public String toString() {
		return "CloseTimeSlot( schedule with ID: " + requestSchedID + ", in timeslot with ID: " + requestTSId +")";
	}
}