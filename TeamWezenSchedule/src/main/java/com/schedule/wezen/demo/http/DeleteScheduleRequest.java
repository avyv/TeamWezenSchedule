package com.schedule.wezen.demo.http;

public class DeleteScheduleRequest {
	public final String requestSchedID;
	
	public DeleteScheduleRequest (String id) {
		this.requestSchedID = id;
	}
	
	public String toString() {
		return "DeleteScheduleRequest(" + requestSchedID + ")";
	}
}

// may need alterations