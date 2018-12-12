package com.schedule.wezen.demo.http;

import java.util.ArrayList;

public class AdminGetListResponse {
	
	public final ArrayList<String> responseList;
	public final String response;
	
	public AdminGetListResponse (ArrayList<String> list, String resp) {
		this.responseList = list;
		this.response = resp;
	}
	
	public String toString()
	{
		return "AdminGetListResponse( " + response + " )";
	}
}