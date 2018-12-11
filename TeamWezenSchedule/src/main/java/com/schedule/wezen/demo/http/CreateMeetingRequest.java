package com.schedule.wezen.demo.http;

public class CreateMeetingRequest {
	public final String requestSchedID;
	public final String requestWeekStart;
	public final String requestMtngName;
	public final String requestTSId;
	
	public final String requestWeekDay;
	public final String requestMonth;
	public final String requestYear;
	public final String requestDate;
	public final String requestTime;
	
	public CreateMeetingRequest(String sid, String ws, String mn, String tsid, String wd, String m, String y, String d, String t) {
		this.requestSchedID = sid;
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
		return "CreateMeeting(" + requestMtngName + ", in schedule with ID: " + requestSchedID + ", in timeslot with ID: " + requestTSId +")";
	}
}