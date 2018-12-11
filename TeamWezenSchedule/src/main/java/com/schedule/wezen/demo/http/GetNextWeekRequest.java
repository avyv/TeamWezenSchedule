package com.schedule.wezen.demo.http;

public class GetNextWeekRequest {
	public final String requestSchedID;
	public final String requestWeekStart;
	
	public final String requestWeekday;
	public final String requestMonth;
	public final String requestYear;
	public final String requestDate;
	public final String requestTime;
	
	public GetNextWeekRequest(String sid, String ws, String wd, String m, String y, String d, String t) {
		this.requestSchedID = sid;
		this.requestWeekStart = ws;
		
		this.requestWeekday = wd;
		this.requestMonth = m;
		this.requestYear = y;
		this.requestDate = d;
		this.requestTime = t;
	}
	
	public String toString() {
		return "GetNextWeekRequest( of" + requestSchedID + " with a start date of " + requestWeekStart + " )";
	}
	
}