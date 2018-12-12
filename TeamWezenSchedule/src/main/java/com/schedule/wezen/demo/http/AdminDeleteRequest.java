package com.schedule.wezen.demo.http;

public class AdminDeleteRequest {
	
	public final String numDays;
	
	public AdminDeleteRequest (String num) {
		this.numDays = num;
	}
	
	public String toString() 
	{
		return "AdminDeleteRequest( " + numDays + " )";
	}
}