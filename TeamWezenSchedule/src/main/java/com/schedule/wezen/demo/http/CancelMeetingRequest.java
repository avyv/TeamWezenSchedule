package com.schedule.wezen.demo.http;

public class CancelMeetingRequest {
	public final String cancelMeetingName;
	public final String cancelMeetingID;
	
	
	public CancelMeetingRequest(String n, String id) {
		this.cancelMeetingName = n;
		this.cancelMeetingID = id;
	}
	
	public String toString() {
		return "DeleteMeeting(" + cancelMeetingName + " : " + cancelMeetingID + ")";
	}
}

