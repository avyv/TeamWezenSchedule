package com.schedule.wezen.demo;

public class CreateScheduleResponse {
	String createScheduleResponse;
	int httpCode;
	
	public CreateScheduleResponse (String s, int code) {
		this.createScheduleResponse = s;
		this.httpCode = code;
	}
	
	public CreateScheduleResponse (String s) {
		this.createScheduleResponse = s;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "CreateScheduleResponse(" + createScheduleResponse + ")";
	}
}