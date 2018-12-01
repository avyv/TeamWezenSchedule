package com.schedule.wezen.demo.http;

public class CancelMeetingResponse {
	public final String meetingNameResponse;
	public final String cancelResponse;
	public final int httpCode;
	
	public CancelMeetingResponse(String n, String resp, int code) {
		this.meetingNameResponse = n;
		this.cancelResponse = resp;
		this.httpCode = code;
	}
	
	public CancelMeetingResponse(String n, String resp) {
		this.meetingNameResponse = n;
		this.cancelResponse = resp;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "CancelMeetingResponse(" + meetingNameResponse + " " + cancelResponse + ")";
	}
}

// may need alterations