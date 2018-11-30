package com.schedule.wezen.demo.http;

public class CreateMeetingRequest {
	public final String requestName;
	public final String requestDate;
	public final String requestTime;
	
	public CreateMeetingRequest(String n, String d, String t) {
		this.requestName = n;
		this.requestDate = d;
		this.requestTime = t;
	}
	
	public String toString() {
		return "CreateMeeting(" + requestName + " on " + requestDate + " at " + requestTime +")";
	}
}