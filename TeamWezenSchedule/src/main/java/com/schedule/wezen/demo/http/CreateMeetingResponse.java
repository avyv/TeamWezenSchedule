package com.schedule.wezen.demo.http;

public class CreateMeetingResponse {
	public final String responseName; // Don't know if we will need these yet
	public final String responseDate;
	public final String responseTime;
	public final String response; // necessary
	public final int httpCode;
	
	public CreateMeetingResponse (String n, String d, String t, String resp, int code) {
		this.responseName = n;
		this.responseDate = d;
		this.responseTime = t;
		this.response = resp;
		this.httpCode = code;
	}
	
	public CreateMeetingResponse(String n, String d, String t, String resp) {
		this.responseName = n;
		this.responseDate = d;
		this.responseTime = t;
		this.response = resp;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "CreateMeetingResponse(Meeting : " + responseName + " on " + responseDate + " at " + responseTime + " " + response + ")";
	}
}