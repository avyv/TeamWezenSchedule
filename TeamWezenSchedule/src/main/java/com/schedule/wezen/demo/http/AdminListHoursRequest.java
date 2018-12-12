package com.schedule.wezen.demo.http;

public class AdminListHoursRequest {
	
	public final String requestRange;
	public final String requestUserInput;
	public final boolean requestToDelete;
	
	public AdminListHoursRequest (String range, String input, boolean del) {
		this.requestRange = range;
		this.requestUserInput = input;
		this.requestToDelete = del;
	}
	
	public String toString()
	{
		return "AdminListHoursRequest( " + requestRange + ", " + requestUserInput + ", " + requestToDelete + " )";
	}
}