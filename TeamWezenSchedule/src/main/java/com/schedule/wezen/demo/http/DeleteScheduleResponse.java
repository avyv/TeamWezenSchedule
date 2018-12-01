package com.schedule.wezen.demo.http;

public class DeleteScheduleResponse {
	public final String deleteScheduleResponse;
	public final int httpCode;
	
	public DeleteScheduleResponse (String s, int code) {
		this.deleteScheduleResponse = s;
		this.httpCode = code;
	}
	
	public DeleteScheduleResponse (String s) {
		this.deleteScheduleResponse = s;
		this.httpCode = 200;
	}
}

// may need alterations