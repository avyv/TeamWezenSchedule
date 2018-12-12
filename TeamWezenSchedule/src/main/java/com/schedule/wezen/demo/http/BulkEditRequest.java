package com.schedule.wezen.demo.http;

public class BulkEditRequest {
	public final String requestSchedID;
	public final String requestWeekStart;
	public final String requestToggle;
	public final String requestWeekday;
	public final String requestMonth;
	public final String requestYear;
	public final String requestDate;
	public final String requestStartTime;
	
	public BulkEditRequest (String id, String ws, String t, String wd, String m, String y, String d, String st) {
		this.requestSchedID = id;
		this.requestWeekStart = ws;
		this.requestToggle = t;
		this.requestWeekday = wd;
		this.requestMonth = m;
		this.requestYear = y;
		this.requestDate = d;
		this.requestStartTime = st;
	}
	
	public String toString () {
		return "BulkEditRequest( schedule: " + requestSchedID + ", toggle: " + requestToggle + ")"; 
	}
}