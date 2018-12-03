package com.schedule.wezen.model;

public class Meeting {
	
	String name;
	String tid;

	public Meeting(String title, String t) {
		this.name = title;
		this.tid = t;
	}
	
	public String getTid() {return tid;}
	public String getName() {return name;}
	public void setName(String n) {this.name = n;}
	public void setTid(String inputTid) {this.tid = inputTid;}
}