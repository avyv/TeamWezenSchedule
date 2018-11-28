package com.schedule.wezen.demo;

public class DateRequest {
	String requestName;
	String requestDate;
	
	public DateRequest (String n, String d) {
		this.requestName = n;
		this.requestDate = d;
	}
	
	public String toString() {
		return "Date(" + requestDate + ": " + requestName + ")";
	}
}