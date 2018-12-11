package com.schedule.wezen.demo.http;

public class CancelMeetingRequest {
	public final String requestSchedID;
	public final String requestWeekStart;
	public final String requestMtngName;
	public final String requestTSId;
	
	
	public CancelMeetingRequest(String id, String ws, String mn, String tsid) {
		this.requestSchedID = id;
		this.requestWeekStart = ws;
		this.requestMtngName = mn;
		this.requestTSId = tsid;
	}
	
	public String toString() {
		return "CancelMeeting( schedule: " + requestSchedID + ", start of week: " + requestWeekStart + ", meeting name: " + requestMtngName + ", timeslot id: " + requestTSId + " )";
	}
}


