package com.schedule.wezen.model;

public class Meeting {
	
	String name;
	int secretCode; 
	String id;
	
	public Meeting(String title, int secretCode, String id) {
		this.name = title;
		this.secretCode = secretCode;
		this.id = id;
	}
	
	public boolean isCorrectCode(int sc) {
		return (sc == secretCode);
	}
	
	public String getName() {return name;}
	public int getSecretCode() {return secretCode;}
	public String getId() {return id;}
	public void setName(String n) {this.name = n;}
}