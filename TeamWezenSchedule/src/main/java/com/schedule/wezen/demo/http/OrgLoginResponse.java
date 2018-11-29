package com.schedule.wezen.demo.http;

public class OrgLoginResponse {
	public String message;
	public int httpCode;
	
	public OrgLoginResponse (String s, int code) {
		this.message = s;
		this.httpCode = code;
	}
	
	// 200 means success
	public OrgLoginResponse (String s) {
		this.message = s;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Message(" + message + ")";
	}
}