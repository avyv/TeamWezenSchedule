package com.schedule.wezen.demo;

public class DateResponse {
	public String responseName;
	public String responseDate;
	int httpCode;
	
	public DateResponse (String n, String d, int code) {
		this.responseName = n;
		this.responseDate = d;
		this.httpCode = code;
	}
	
	// 200 means success
	public DateResponse (String n, String d) {
		this.responseName = n;
		this.responseDate = d;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Name(" + responseDate + ": " + responseName + ")";
	}
}