package com.schedule.wezen.model;

public class Meeting {
	
	String name;

	public Meeting() {this.name = " ";}
	public Meeting(String title) {this.name = title;}
	
	public String getName() {return name;}
	public void setName(String n) {this.name = n;}
}