package com.schedule.wezen.demo.http;

import java.util.ArrayList;

public class AdminListHoursResponse {
	
	public final ArrayList<String> responseListOfSched;
	public final String response;
	
	public AdminListHoursResponse (ArrayList<String> ls, String resp) {
		this.responseListOfSched = ls;
		this.response = resp;
	}
	
	public String toString()
	{
		return "AdminListHoursResponse( " + response + " )";
	}
}