package com.schedule.wezen.demo.http;

public class OrgLoginRequest {
	public String user;
	public String pass;
	
	public OrgLoginRequest (String a1, String a2) {
		this.user = a1;
		this.pass = a2;
	}
	
	public String toString() {
		return "Org(" + user + "," + pass + ")";
	}
}
