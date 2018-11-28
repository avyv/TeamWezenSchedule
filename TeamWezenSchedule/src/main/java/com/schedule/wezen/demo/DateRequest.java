package com.schedule.wezen.demo;

public class DateRequest {
	String name;
	int date;
	
	public DateRequest (String n, int d) {
		this.name = n;
		this.date = d;
	}
	
	public String toString() {
		return "Date(" + date + ": " + name + ")";
	}
}