package com.schedule.wezen.demo.http;

public class DeleteScheduleRequest {
	public final int scheduleSecretCode;
	
	public DeleteScheduleRequest (int secretCode) {
		this.scheduleSecretCode = secretCode;
	}
	
	public String toString() {
		return "DeleteScheduleRequest(" + scheduleSecretCode + ")";
	}
}

// may need alterations