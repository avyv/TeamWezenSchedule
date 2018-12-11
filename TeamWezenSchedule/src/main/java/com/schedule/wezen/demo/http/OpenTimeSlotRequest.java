package com.schedule.wezen.demo.http;

public class OpenTimeSlotRequest {
	public final String requestSchedID;
	public final String requestWeekStart;
	public final String requestTSId;
	
	public final String requestWeekday;
	public final String requestMonth;
	public final String requestYear;
	public final String requestDate;
	public final String requestTime;
	
	public OpenTimeSlotRequest(String sid, String ws, String tsid, String wd, String m, String y, String d, String t) {
		this.requestSchedID = sid;
		this.requestWeekStart = ws;
		this.requestTSId = tsid;
		
		this.requestWeekday = wd;
		this.requestMonth = m;
		this.requestYear = y;
		this.requestDate = d;
		this.requestTime = t;
	}
	
	public String toString() {
		return "OpenTimeSlot( schedule with ID: " + requestSchedID + ", in timeslot with ID: " + requestTSId +")";
	}
}