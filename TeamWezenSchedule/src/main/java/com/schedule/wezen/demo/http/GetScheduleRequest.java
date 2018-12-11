package com.schedule.wezen.demo.http;

public class GetScheduleRequest {
	public final String requestSchedID;
	public final String requestWeekStart;
	
	public final String requestWeekday;
	public final String requestMonth;
	public final String requestYear;
	public final String requestDate;
	public final String requestTime;
	
	public GetScheduleRequest (String requestSchedID, String requestStartDateOfWeek, String wd, String m, String y, String d, String t) {
		this.requestSchedID = requestSchedID;
		this.requestWeekStart = requestStartDateOfWeek;
		
		this.requestWeekday = wd;
		this.requestMonth = m;
		this.requestYear = y;
		this.requestDate = d;
		this.requestTime = t;
	}
	
	public String toString() {
		return "GetScheduleRequest( schedule: " + requestSchedID + " start of week: " + requestWeekStart + " )";
	}
}