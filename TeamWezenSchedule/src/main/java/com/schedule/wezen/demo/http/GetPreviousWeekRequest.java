package com.schedule.wezen.demo.http;

public class GetPreviousWeekRequest {
	public final String requestSchedID;
	public final String requestWeekStart;
	
	public final String requestWeekDay;
	public final String requestMonth;
	public final String requestYear;
	public final String requestDate;
	public final String requestTime;
	
	public GetPreviousWeekRequest (String id, String ws, String wd, String m, String y, String d, String t) {
		this.requestSchedID = id;
		this.requestWeekStart = ws;
		
		this.requestWeekDay = wd;
		this.requestMonth = m;
		this.requestYear = y;
		this.requestDate = d;
		this.requestTime = t;
	}
	
	public String toString () {
		return "GetPreviousWeekRequest( schedule: " + requestSchedID + ", start of week: " + requestWeekStart + " )";
	}
}