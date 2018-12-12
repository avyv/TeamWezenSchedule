package com.schedule.wezen.demo.http;

public class AdminGetListRequest {
	
	public final String requestRange;
	public final String requestNumDaysOrHours;
	
	public AdminGetListRequest (String range, String num) {
		this.requestRange = range;
		this.requestNumDaysOrHours = num;
	}
	
	public String toString() 
	{
		return "AdminGetListRequest( " + requestRange + ", " + requestNumDaysOrHours + " )";
	}
}