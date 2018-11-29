package com.schedule.wezen.demo.http;

public class DateRequest {
	public String requestName;
	public String requestDate;
	
	public DateRequest (String n, String d) {
		this.requestName = n;
		this.requestDate = d;
	}
	
	public String toString() {
		return "Date(" + requestDate + ": " + requestName + ")";
	}
}