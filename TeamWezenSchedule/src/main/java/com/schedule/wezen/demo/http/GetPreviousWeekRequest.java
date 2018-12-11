package com.schedule.wezen.demo.http;

public class GetPreviousWeekRequest {
	public final String requestSchedID;
	public final String requestWeekStart;
	
	public GetPreviousWeekRequest (String id, String ws) {
		this.requestSchedID = id;
		this.requestWeekStart = ws;
	}
	
	public String toString () {
		return "GetPreviousWeekRequest( schedule: " + requestSchedID + ", start of week: " + requestWeekStart + " )";
	}
}