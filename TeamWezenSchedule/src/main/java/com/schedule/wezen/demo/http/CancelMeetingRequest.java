package com.schedule.wezen.demo.http;

public class CancelMeetingRequest {
	public final String requestSchedID;
	public final String requestWeekStart;
	public final String requestMtngName;
	public final String requestTSId;
	
	public final String requestWeekDay;
	public final String requestMonth;
	public final String requestYear;
	public final String requestDate;
	public final String requestTime;
	
	
	public CancelMeetingRequest(String id, String ws, String mn, String tsid, String wd, String m, String y, String d, String t) {
		this.requestSchedID = id;
		this.requestWeekStart = ws;
		this.requestMtngName = mn;
		this.requestTSId = tsid;
		
		this.requestWeekDay = wd;
		this.requestMonth = m;
		this.requestYear = y;
		this.requestDate = d;
		this.requestTime = t;
	}
	
	public String toString() {
		return "CancelMeeting( schedule: " + requestSchedID + ", start of week: " + requestWeekStart + ", meeting name: " + requestMtngName + ", timeslot id: " + requestTSId + " )";
	}
}


