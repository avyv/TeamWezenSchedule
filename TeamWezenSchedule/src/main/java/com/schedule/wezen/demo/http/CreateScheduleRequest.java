// Team Wezen

package com.schedule.wezen.demo.http;

public class CreateScheduleRequest {
	public final String startDate;
	public final String endDate;
	public final String startTime;
	public final String endTime;
	public final int slotDuration;
	public final String id;
	
	public CreateScheduleRequest(String startDate, String endDate, String startTime, String endTime, int slotDuration, String id) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.slotDuration = slotDuration;
		this.id = id;
	}
	
	public String toString() {
		return "CreateSchedule( Start Date: " + startDate + ", End Date : " + endDate + ", Start Time : " + startTime + ", End Time : " + endTime + ", Slot Duration : " + slotDuration + ", ID : " + id + " )";
	}
}