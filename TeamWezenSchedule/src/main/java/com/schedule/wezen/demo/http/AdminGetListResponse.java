package com.schedule.wezen.demo.http;

import java.util.ArrayList;

public class AdminGetListResponse {
	
	public final ArrayList<String> responseList;
	public final String response;
	public final int httpCode;
	
	public AdminGetListResponse (ArrayList<String> list, String resp, int code) {
		this.responseList = list;
		this.response = resp;
		this.httpCode = code;
	}
	
	public AdminGetListResponse (String resp, int code) {
		this.responseList = new ArrayList<String>();
		this.response = resp;
		this.httpCode = code;
	}
	
	public String toString()
	{
		return "AdminGetListResponse( " + response + " )";
	}
}