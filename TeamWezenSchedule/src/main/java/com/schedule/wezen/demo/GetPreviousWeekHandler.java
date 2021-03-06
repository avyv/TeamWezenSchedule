
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
import com.schedule.wezen.demo.http.GetPreviousWeekRequest;
import com.schedule.wezen.demo.http.GetPreviousWeekResponse;
import com.schedule.wezen.model.Model;
import com.schedule.wezen.model.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Found gson JAR file from
 * https://repo1.maven.org/maven2/com/google/code/gson/gson/2.6.2/gson-2.6.2.jar
 */
public class GetPreviousWeekHandler implements RequestStreamHandler {
	
	public LambdaLogger logger = null;
	
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
		
		GetPreviousWeekResponse getPreviousResponse = null;
		
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
			getPreviousResponse = new GetPreviousWeekResponse("Bad Request: " + pe.getMessage(), 422); // unable to process input
			responseJson.put("body", new Gson().toJson(getPreviousResponse));
			processed = true;
			body = null;
		}
		
		if(!processed) {
			GetPreviousWeekRequest getPreviousRequest = new Gson().fromJson(body, GetPreviousWeekRequest.class);
			
			logger.log(getPreviousRequest.toString());
			
			logger.log("Before retrieveSchedule");
			
			try {
				Schedule retrievedSchedule = retrieveScheduleLambda(getPreviousRequest.requestSchedID); // this is where we call the retrieveSchedule function that utilizes SchedulesDAO
				
				logger.log("Retrieved Schedule: " + retrievedSchedule.getId());
				
				String startDateOfWeek = "";
				String startTime = retrievedSchedule.getStartTime().toString();
				String scheduleID = retrievedSchedule.getId();
				int slotDuration = retrievedSchedule.getSlotDuration();
				int secretCode = retrievedSchedule.getSecretCode();
				int numSlotsDay = retrievedSchedule.getNumSlotsDay();
				String scheduleStartDate = retrievedSchedule.getStartDate().toString();
				String scheduleEndDate = retrievedSchedule.getEndDate().toString();
				
				
				logger.log("Filtering Schedule");
				
				retrievedSchedule.searchForTime(getPreviousRequest.requestMonth, getPreviousRequest.requestYear, getPreviousRequest.requestWeekday, getPreviousRequest.requestDate, getPreviousRequest.requestTime);
				
				logger.log("Finished Filtering");
				
				
				ArrayList<Schedule> scheduleDividedByWeeks = retrievedSchedule.divideByWeeks(/*retrievedSchedule.getStartDate(), retrievedSchedule.getEndDate(), retrievedSchedule.getStartTime(), retrievedSchedule.getEndTime(), retrievedSchedule.getSlotDuration(), retrievedSchedule.getId(), retrievedSchedule.getNumSlotsDay()*/);
				//Schedule byWeek = null;
//				Schedule byWeek = scheduleDividedByWeeks.get(0);
//				startDateOfWeek = byWeek.getStartDate().toString();
				
				
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
					String requestStart = getPreviousRequest.requestWeekStart;
					
					if(arrayListStartDate.equals(requestStart)) {
						week = index - 1;
					}
					
					index++;
				}
				
				String response = "";
				
				/*if(week == -1)
				{
					response = "You are currently viewing the first week of the schedule";
				}
				else
				{
					byWeek = scheduleDividedByWeeks.get(week);
					startDateOfWeek = byWeek.getStartDate().toString();
					response = "Successfully retrieved schedule";
				}*/
				
				byWeek = scheduleDividedByWeeks.get(week);
				startDateOfWeek = byWeek.getStartDate().toString();
			
				
				response = "Successfully retrieved schedule";
				
				getPreviousResponse = new GetPreviousWeekResponse(startDateOfWeek, startTime, scheduleID, slotDuration, secretCode, numSlotsDay, scheduleStartDate, scheduleEndDate, byWeek.getTimeSlots(), response, 200);
			} catch (Exception e) {
				
				e.printStackTrace();
				
				logger.log(e.getMessage());
				getPreviousResponse = new GetPreviousWeekResponse("A schedule with that ID does not exist in our database: " + e.getMessage(), 403);
			}
			
			// compute proper response
			responseJson.put("body", new Gson().toJson(getPreviousResponse));
			
		}
		
		logger.log("end result: " + responseJson.toJSONString());
		logger.log(responseJson.toJSONString());
		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
		writer.write(responseJson.toJSONString());
		writer.close();
		
	}
	
	
}