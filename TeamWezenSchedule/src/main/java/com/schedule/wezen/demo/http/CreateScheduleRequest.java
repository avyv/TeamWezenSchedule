// Team Wezen

package com.schedule.wezen.demo.http;

public class CreateScheduleRequest {
	public final String requestStartDate;
	public final String requestEndDate;
	public final String requestStartTime;
	public final String requestEndTime;
	public final String requestSlotDuration;
	public final String requestID;
	
	public CreateScheduleRequest(String startDate, String endDate, String startTime, String endTime, String slotDuration, String id) {
		this.requestStartDate = startDate;
		this.requestEndDate = endDate;
		this.requestStartTime = startTime;
		this.requestEndTime = endTime;
		this.requestSlotDuration = slotDuration;
		this.requestID = id;
	}
	
	public String toString() {
		return "CreateSchedule( Start Date: " + requestStartDate + ", End Date : " + requestEndDate + ", Start Time : " + requestStartTime + ", End Time : " + requestEndTime + ", Slot Duration : " + requestSlotDuration + ", ID : " + requestID + " )";
	}
}