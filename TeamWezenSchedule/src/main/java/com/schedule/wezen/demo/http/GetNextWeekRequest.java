package com.schedule.wezen.demo.http;

public class GetNextWeekRequest {
	public final String requestSchedID;
	public final String requestWeekStart;
	
	public GetNextWeekRequest(String sid, String ws) {
		this.requestSchedID = sid;
		this.requestWeekStart = ws;
	}
	
	public String toString() {
		return "GetNextWeekRequest( of" + requestSchedID + " with a start date of " + requestWeekStart + " )";
	}
	
}