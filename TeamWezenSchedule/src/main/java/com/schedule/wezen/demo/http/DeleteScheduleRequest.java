package com.schedule.wezen.demo.http;

public class DeleteScheduleRequest {
	public final String scheduleSecretCode;
	
	public DeleteScheduleRequest (String secretCode) {
		this.scheduleSecretCode = secretCode;
	}
	
	public String toString() {
		return "DeleteScheduleRequest(" + scheduleSecretCode + ")";
	}
}

// may need alterations