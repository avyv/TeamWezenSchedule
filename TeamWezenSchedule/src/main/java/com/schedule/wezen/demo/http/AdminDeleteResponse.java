package com.schedule.wezen.demo.http;

import java.util.ArrayList;

public class AdminDeleteResponse {
	
	public final ArrayList<String> responseList;
	public final String response;
	
	public AdminDeleteResponse (ArrayList<String> list, String resp) {
		this.responseList = list;
		this.response = resp;
	}
}