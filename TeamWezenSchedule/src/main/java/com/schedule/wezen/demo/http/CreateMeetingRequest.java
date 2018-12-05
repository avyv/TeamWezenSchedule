package com.schedule.wezen.demo.http;

public class CreateMeetingRequest {
	public final String requestSchedID;
	public final String requestWeekStart;
	public final String requestMtngName;
	public final String requestTSId;
	
	public CreateMeetingRequest(String sid, String ws, String mn, String tsid) {
		this.requestSchedID = sid;
		this.requestWeekStart = ws;
		this.requestMtngName = mn;
		this.requestTSId = tsid;
	}
	
	public String toString() {
		return "CreateMeeting(" + requestMtngName + " in schedule with ID: " + requestSchedID + " in timeslot with ID: " + requestTSId +")";
	}
}