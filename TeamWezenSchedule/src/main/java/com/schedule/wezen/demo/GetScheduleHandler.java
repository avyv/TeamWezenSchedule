package com.schedule.wezen.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import com.schedule.wezen.db.SchedulesDAO;
import com.schedule.wezen.demo.http.GetScheduleRequest;
import com.schedule.wezen.demo.http.GetScheduleResponse;
import com.schedule.wezen.model.Schedule;

import java.time.LocalDate;

/**
 * Found gson JAR file from
 * https://repo1.maven.org/maven2/com/google/code/gson/gson/2.6.2/gson-2.6.2.jar
 */
public class GetScheduleHandler implements RequestStreamHandler {
	
	public LambdaLogger logger = null;
	
	/** Load from RDS, if it exists
	 * 
	 * @throws Exception
	 */
	Schedule retrieveSchedule(String scheduleID) throws Exception {
		if (logger != null) { logger.log("in retrieveSchedule"); }
		
		SchedulesDAO dao = new SchedulesDAO();
		
		return dao.getSchedule(scheduleID);
	}
	
	
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to retrieve schedule");
		
		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type", "application/json");
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin", "*");
		
		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);
		
		GetScheduleResponse getScheduleResponse = null;
		
		// extract body from incoming HTTP POST request. If any error, then return 422 error
		String body;
		boolean processed = false;
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			JSONParser parser = new JSONParser();
			JSONObject event = (JSONObject) parser.parse(reader);
			logger.log("event: " + event.toJSONString());
			
			body = (String) event.get("body");
			
			if(body == null) {
				body = event.toJSONString(); // this is only here to make testing easier
			}
		} catch (ParseException pe) {
			logger.log(pe.toString());
			getScheduleResponse = new GetScheduleResponse("Bad Request: " + pe.getMessage(), 422); // unable to process input
			responseJson.put("body", new Gson().toJson(getScheduleResponse));
			processed = true;
			body = null;
		}
		
		if(!processed) {
			GetScheduleRequest getScheduleRequest = new Gson().fromJson(body, GetScheduleRequest.class);
			logger.log(getScheduleRequest.toString());
			
			try {
				Schedule retrievedSchedule = retrieveSchedule(getScheduleRequest.requestSchedID); // this is where we call the retrieveSchedule function that utilizes SchedulesDAO
				
				String startDateOfWeek = "";
				String startTime = retrievedSchedule.getStartTime().toString();
				String scheduleID = retrievedSchedule.getId();
				int slotDuration = retrievedSchedule.getSlotDuration();
				int secretCode = retrievedSchedule.getSecretCode();
				int numSlotsDay = retrievedSchedule.getNumSlotsDay();
				String scheduleStartDate = retrievedSchedule.getStartDate().toString();
				String scheduleEndDate = retrievedSchedule.getEndDate().toString();
				
				
				ArrayList<Schedule> scheduleDividedByWeeks = retrievedSchedule.divideByWeeks(retrievedSchedule.getStartDate(), retrievedSchedule.getEndDate(), retrievedSchedule.getStartTime(), retrievedSchedule.getEndTime(), retrievedSchedule.getSlotDuration(), retrievedSchedule.getId(), retrievedSchedule.getNumSlotsDay());
				Schedule byWeek = null;
				
				/**
				 * This will return the schedule for the first week
				 */
				if(getScheduleRequest.requestWeekStart.equals("")) {
					byWeek = scheduleDividedByWeeks.get(0);
					startDateOfWeek = byWeek.getStartDate().toString();
				}
				
				/**
				 * This will return the schedule for the week beginning at requestWeekStart
				 */
				else if(!(getScheduleRequest.requestWeekStart.equals(""))) {
					
					int index = 0;
					int week = 0;
					
					for(Schedule schedule : scheduleDividedByWeeks)
					{
						index++;
						
						LocalDate listStartDate = schedule.getStartDate();
						String arrayListStartDate = listStartDate.toString();
						String requestStart = getScheduleRequest.requestWeekStart;
						
						if(arrayListStartDate.equals(requestStart)) {
							week = index - 1;
						}
					}
					
					byWeek = scheduleDividedByWeeks.get(week);
					startDateOfWeek = byWeek.getStartDate().toString();
				}
				
				String response = "Successfully retrieved schedule";
				
				getScheduleResponse = new GetScheduleResponse(startDateOfWeek, startTime, scheduleID, slotDuration, secretCode, numSlotsDay, scheduleStartDate, scheduleEndDate, byWeek.getTimeSlots(), response, 200);
			} catch (Exception e) {
				getScheduleResponse = new GetScheduleResponse("Unable to retrieve schedule, incorrect ID: " + e.getMessage(), 403);
			}
			
			// compute proper response
			responseJson.put("body", new Gson().toJson(getScheduleResponse));
			
		}
		
		logger.log("end result: " + responseJson.toJSONString());
		logger.log(responseJson.toJSONString());
		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
		writer.write(responseJson.toJSONString());
		writer.close();
		
	}
	
	
}