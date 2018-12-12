package com.schedule.wezen.demo.http;

public class AdminDeleteRequest {
	
	public final String requestNumDays;
	
	public AdminDeleteRequest (String num) {
		this.requestNumDays = num;
	}
	
	public String toString() 
	{
		return "AdminDeleteRequest( " + requestNumDays + " )";
	}
}