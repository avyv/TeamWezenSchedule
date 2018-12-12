package com.schedule.wezen.demo.http;

public class AdminListDaysRequest {
	
	public final String requestRange;
	public final String requestUserInput;
	public final boolean requestToDelete;
	
	public AdminListDaysRequest (String range, String input, boolean del) {
		this.requestRange = range;
		this.requestUserInput = input;
		this.requestToDelete = del;
	}
	
	public String toString() {
		return "AdminListDaysRequest( " + requestRange + ", " + requestUserInput + ", " + requestToDelete + " )";
	}
}