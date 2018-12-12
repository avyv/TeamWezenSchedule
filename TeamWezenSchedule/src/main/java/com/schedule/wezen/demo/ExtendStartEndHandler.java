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
import com.schedule.wezen.demo.http.ExtendStartEndRequest;
import com.schedule.wezen.demo.http.ExtendStartEndResponse;
import com.schedule.wezen.model.Model;
import com.schedule.wezen.model.Schedule;
import com.schedule.wezen.model.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Found gson JAR file from
 * https://repo1.maven.org/maven2/com/google/code/gson/gson/2.6.2/gson-2.6.2.jar
 */
public class ExtendStartEndHandler implements RequestStreamHandler {
	
	public LambdaLogger logger = null;
	
	
	boolean deleteScheduleLambda(String id) throws Exception {
		
		SchedulesDAO dao = new SchedulesDAO();
		
		logger.log("DAO created");
		
		return dao.deleteSchedule(id);
	}
	
	boolean addScheduleLambda(Schedule schedule) throws Exception {
		if(logger != null) { logger.log("in createScheduleLambda");}

		SchedulesDAO dao = new SchedulesDAO();
		
		logger.log("DAO Created");
			
		return dao.addSchedule(schedule);
		
	}
	
	/** Load from RDS, if it exists
	 * 
	 * @throws Exception
	 */
	Schedule retrieveScheduleLambda(String scheduleID) throws Exception {
		if (logger != null) { logger.log("in retrieveScheduleLambda"); }
		
		SchedulesDAO dao = new SchedulesDAO();
		
		logger.log("After SchedulesDAO object");
		
		Schedule schedule = dao.getSchedule(scheduleID);
		
		logger.log("After creating schedule object and using getSchedule from the DAO");
		
		return schedule;
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
		
		ExtendStartEndResponse extendResponse = null;
		 
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
			extendResponse = new ExtendStartEndResponse("Bad Request: " + pe.getMessage(), 422); // unable to process input
			responseJson.put("body", new Gson().toJson(extendResponse));
			processed = true;
			body = null;
		}
		
		if(!processed) {
			ExtendStartEndRequest extendRequest = new Gson().fromJson(body, ExtendStartEndRequest.class);
			logger.log(extendRequest.toString());
			
			logger.log("Before retrieveSchedule");
			
			try {
				Schedule retrievedSchedule = retrieveScheduleLambda(extendRequest.requestSchedID); // this is where we call the retrieveSchedule function that utilizes SchedulesDAO
				
				logger.log("Retrieved Schedule: " + retrievedSchedule.getId());
				
				Model m = new Model();
				
				ArrayList<TimeSlot> nSE = retrievedSchedule.populateTimeSlots2(retrievedSchedule.getTimeSlots(), m.stringToDate(extendRequest.requestNewStart), m.stringToDate(extendRequest.requestNewEnd));
				
				logger.log("After new population");
				
				Schedule updatedSchedule = new Schedule(m.stringToDate(extendRequest.requestNewStart), m.stringToDate(extendRequest.requestNewEnd), retrievedSchedule.getStartTime(), retrievedSchedule.getEndTime(), retrievedSchedule.getSlotDuration(), retrievedSchedule.getId(), retrievedSchedule.getSecretCode(), nSE);
				
				logger.log("After creating new schedule");
				
				
				boolean deleted = deleteScheduleLambda(retrievedSchedule.getId());
				
				logger.log("Schedule deleted? : " + deleted);
				
				boolean added = false;
				
				if(deleted)
				{
					added = addScheduleLambda(updatedSchedule);
				}
				
				logger.log("Successfully added updated schedule: " + added);
				
				
				Schedule retrieveUpdate = retrieveScheduleLambda(extendRequest.requestSchedID);
				
				if(retrieveUpdate.getStartDate().toString().equals(extendRequest.requestNewStart) && retrieveUpdate.getEndDate().toString().equals(extendRequest.requestNewEnd))
				{
					String startDateOfWeek = "";
					String startTime = retrieveUpdate.getStartTime().toString();
					String scheduleID = retrieveUpdate.getId();
					int slotDuration = retrieveUpdate.getSlotDuration();
					int secretCode = retrieveUpdate.getSecretCode();
					int numSlotsDay = retrieveUpdate.getNumSlotsDay();
					String scheduleStartDate = retrieveUpdate.getStartDate().toString();
					String scheduleEndDate = retrieveUpdate.getEndDate().toString();
					
					
					ArrayList<Schedule> scheduleDividedByWeeks = retrieveUpdate.divideByWeeks();
					
					
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
						String requestStart = extendRequest.requestWeekStart;
						
						if(arrayListStartDate.equals(requestStart)) {
							week = index;
						}
						
						index++;
					}
					
					byWeek = scheduleDividedByWeeks.get(week);
					startDateOfWeek = byWeek.getStartDate().toString();
				
					
					String response = "Successfully retrieved schedule";
					
					extendResponse = new ExtendStartEndResponse(startDateOfWeek, startTime, scheduleID, slotDuration, secretCode, numSlotsDay, scheduleStartDate, scheduleEndDate, byWeek.getTimeSlots(), response, 200);

				}
				else
				{
					String response = "Unable to extend start and end dates: ";
					extendResponse = new ExtendStartEndResponse(response, 403);
				}
				
			} catch (Exception e) {
				
				e.printStackTrace();
				
				logger.log(e.getMessage());
				extendResponse = new ExtendStartEndResponse("Unable to extend start and end dates: " + e.getMessage(), 403);
			}
			
			// compute proper response
			responseJson.put("body", new Gson().toJson(extendResponse));
			
		}
		
		logger.log("end result: " + responseJson.toJSONString());
		logger.log(responseJson.toJSONString());
		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
		writer.write(responseJson.toJSONString());
		writer.close();
		
	}
	
	
}