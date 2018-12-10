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
import com.schedule.wezen.db.TimeSlotsDAO;
import com.schedule.wezen.demo.http.CloseTimeSlotRequest;
import com.schedule.wezen.demo.http.CloseTimeSlotResponse;
import com.schedule.wezen.model.Schedule;

import java.time.LocalDate;

/**
 * Found gson JAR file from
 * https://repo1.maven.org/maven2/com/google/code/gson/gson/2.6.2/gson-2.6.2.jar
 */
public class CloseTimeSlotHandler implements RequestStreamHandler {
	
	public LambdaLogger logger = null;
	
	/** Load from RDS, if it exists
	 * 
	 * @throws Exception
	 */
	boolean closeTimeSlotLambda(String tsid) throws Exception {
		if (logger != null) { logger.log("in closeTimeSlotLambda"); }
		
		TimeSlotsDAO tsdao = new TimeSlotsDAO();
		
		logger.log("After creating TimeSlotsDAO, before closing time slot");
		
		return tsdao.closeTimeSlot(tsid);
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
		
		CloseTimeSlotResponse closeTimeSlotResponse = null;
		
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
			closeTimeSlotResponse = new CloseTimeSlotResponse("Bad Request: " + pe.getMessage(), 422); // unable to process input
			responseJson.put("body", new Gson().toJson(closeTimeSlotResponse));
			processed = true;
			body = null;
		}
		
		if(!processed) {
			CloseTimeSlotRequest closeTimeSlotRequest = new Gson().fromJson(body, CloseTimeSlotRequest.class);
			
			logger.log(closeTimeSlotRequest.toString());
			
			logger.log("Before closing time slot");
			
			String response = "";
			
			boolean closedTimeSlot = false;
			
			try {
				
				logger.log("Trying to close time slot");
				
				closedTimeSlot = closeTimeSlotLambda(closeTimeSlotRequest.requestTSId);
				
			} catch (Exception e) {
				
				logger.log(e.getStackTrace().toString());
				
				logger.log("Caught exception: " + e.getMessage());
				
				response = "Unable to close time slot";
				
				closeTimeSlotResponse = new CloseTimeSlotResponse(response + " (" + e.getMessage(), 403);
				
			}
			
			logger.log("Before retrieving schedule");
			
			if(closedTimeSlot)
			{
				try {
					
					SchedulesDAO dao = new SchedulesDAO();
					
					Schedule retrievedSchedule = dao.getSchedule(closeTimeSlotRequest.requestSchedID); // this is where we call the retrieveSchedule function that utilizes SchedulesDAO
					
					logger.log("Retrieved Schedule: " + retrievedSchedule.getId());
					
					String startDateOfWeek = "";
					String startTime = retrievedSchedule.getStartTime().toString();
					String scheduleID = retrievedSchedule.getId();
					int slotDuration = retrievedSchedule.getSlotDuration();
					int secretCode = retrievedSchedule.getSecretCode();
					int numSlotsDay = retrievedSchedule.getNumSlotsDay();
					String scheduleStartDate = retrievedSchedule.getStartDate().toString();
					String scheduleEndDate = retrievedSchedule.getEndDate().toString();
					
					
					ArrayList<Schedule> scheduleDividedByWeeks = retrievedSchedule.divideByWeeks();
					
					logger.log("Assigned values to variables");
					
					/**
					 * This will return the schedule for the week beginning at requestWeekStart
					 */
						
					Schedule byWeek = null;
					
					int index = 0;
					int week = 0;
					
					for(Schedule schedule : scheduleDividedByWeeks)
					{
						LocalDate listStartDate = schedule.getStartDate();
						String arrayListStartDate = listStartDate.toString();
						String requestStart = closeTimeSlotRequest.requestWeekStart;
						
						if(arrayListStartDate.equals(requestStart)) {
							week = index + 1;
						}
						
						index++;
					}
					
					byWeek = scheduleDividedByWeeks.get(week);
					startDateOfWeek = byWeek.getStartDate().toString();
				
					
					response = "Successfully retrieved schedule";
					
					closeTimeSlotResponse = new CloseTimeSlotResponse(startDateOfWeek, startTime, scheduleID, slotDuration, secretCode, numSlotsDay, scheduleStartDate, scheduleEndDate, byWeek.getTimeSlots(), response, 200);
				} catch (Exception e) {
					
					e.printStackTrace();
					
					logger.log(e.getMessage());
					closeTimeSlotResponse = new CloseTimeSlotResponse("A schedule with that ID does not exist in our database: " + e.getMessage(), 403);
				}
			}
			
			// compute proper response
			responseJson.put("body", new Gson().toJson(closeTimeSlotResponse));
			
		}
		
		logger.log("end result: " + responseJson.toJSONString());
		logger.log(responseJson.toJSONString());
		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
		writer.write(responseJson.toJSONString());
		writer.close();
		
	}
	
	
}