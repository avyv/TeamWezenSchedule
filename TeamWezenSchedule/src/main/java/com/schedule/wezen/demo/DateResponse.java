package com.schedule.wezen.demo;

public class DateResponse {
	public String name;
	public int date;
	int httpCode;
	
	public DateResponse (String n, int d, int code) {
		this.name = n;
		this.date = d;
		this.httpCode = code;
	}
	
	// 200 means success
	public DateResponse (String n, int d) {
		this.name = n;
		this.date = d;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Name(" + date + ": " + name + ")";
	}
}