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
import com.schedule.wezen.demo.http.BulkEditRequest;
import com.schedule.wezen.demo.http.BulkEditResponse;
import com.schedule.wezen.model.Model;
import com.schedule.wezen.model.Schedule;
import com.schedule.wezen.model.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Found gson JAR file from
 * https://repo1.maven.org/maven2/com/google/code/gson/gson/2.6.2/gson-2.6.2.jar
 */
public class BulkEditTimeSlotsHandler implements RequestStreamHandler {
	
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
		
		BulkEditResponse bulkResponse = null;
		 
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
			bulkResponse = new BulkEditResponse("Bad Request: " + pe.getMessage(), 422); // unable to process input
			responseJson.put("body", new Gson().toJson(bulkResponse));
			processed = true;
			body = null;
		}
		
		if(!processed) {
			BulkEditRequest bulkRequest = new Gson().fromJson(body, BulkEditRequest.class);
			logger.log(bulkRequest.toString());
			
			logger.log("Before retrieveSchedule");
			
			try {
				Schedule retrievedSchedule = retrieveScheduleLambda(bulkRequest.requestSchedID); // this is where we call the retrieveSchedule function that utilizes SchedulesDAO
				
				logger.log("Retrieved Schedule: " + retrievedSchedule.getId());
				
				logger.log("Before TS DAO");
				
				TimeSlotsDAO tsdao = new TimeSlotsDAO();
				
				logger.log("Before For Loop");
				
				for(TimeSlot ts: retrievedSchedule.getTimeSlots()) { 
					
					logger.log("In for loop");
					
					if(!(bulkRequest.requestMonth.equals("0"))) { 
						if(ts.getDate().getMonthValue() == Integer.parseInt(bulkRequest.requestMonth))
						{
							if(bulkRequest.requestToggle.equals("open"))
							{
								tsdao.openTimeSlot(ts.getId());
							}
							else if(bulkRequest.requestToggle.equals("close"))
							{
								tsdao.closeTimeSlot(ts.getId());
							}
						}
					}
					if(!(bulkRequest.requestYear.equals("0"))) { 
						if(ts.getDate().getYear() == Integer.parseInt(bulkRequest.requestYear))
						{
							if(bulkRequest.requestToggle.equals("open"))
							{
								tsdao.openTimeSlot(ts.getId());
							}
							else if(bulkRequest.requestToggle.equals("close"))
							{
								tsdao.closeTimeSlot(ts.getId());
							}
						}
					}
					if(!(bulkRequest.requestWeekday.equals("0"))) { 
						if(ts.getDate().getDayOfWeek().getValue() == Integer.parseInt(bulkRequest.requestWeekday))
						{
							if(bulkRequest.requestToggle.equals("open"))
							{
								tsdao.openTimeSlot(ts.getId());
							}
							else if(bulkRequest.requestToggle.equals("close"))
							{
								tsdao.closeTimeSlot(ts.getId());
							}
						}
					}
					if(!(bulkRequest.requestDate.equals(""))) { 
						if((ts.getDate().toString().equals(bulkRequest.requestDate)))
						{
							if(bulkRequest.requestToggle.equals("open"))
							{
								tsdao.openTimeSlot(ts.getId());
							}
							else if(bulkRequest.requestToggle.equals("close"))
							{
								tsdao.closeTimeSlot(ts.getId());
							}
						}
					}
					if(!(bulkRequest.requestStartTime.equals("0"))) { 
						if((ts.getStartTime().toString().equals(bulkRequest.requestStartTime)))
						{
							if(bulkRequest.requestToggle.equals("open"))
							{
								tsdao.openTimeSlot(ts.getId());
							}
							else if(bulkRequest.requestToggle.equals("close"))
							{
								tsdao.closeTimeSlot(ts.getId());
							}
						}
					}
				}
				
				Schedule retrieved2 = retrieveScheduleLambda(bulkRequest.requestSchedID); // this is where we call the retrieveSchedule function that utilizes SchedulesDAO
				
				logger.log("Retrieved Schedule 2nd time: " + retrieved2.getId());
				
				String startDateOfWeek = "";
				String startTime = retrieved2.getStartTime().toString();
				String scheduleID = retrieved2.getId();
				int slotDuration = retrieved2.getSlotDuration();
				int secretCode = retrieved2.getSecretCode();
				int numSlotsDay = retrieved2.getNumSlotsDay();
				String scheduleStartDate = retrieved2.getStartDate().toString();
				String scheduleEndDate = retrieved2.getEndDate().toString();
				
				
				ArrayList<Schedule> scheduleDividedByWeeks = retrieved2.divideByWeeks();
				
				
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
					String requestStart = bulkRequest.requestWeekStart;
					
					if(arrayListStartDate.equals(requestStart)) {
						week = index;
					}
					
					index++;
				}
				
				byWeek = scheduleDividedByWeeks.get(week);
				startDateOfWeek = byWeek.getStartDate().toString();
			
				
				String response = "Successfully retrieved schedule";
				
				bulkResponse = new BulkEditResponse(startDateOfWeek, startTime, scheduleID, slotDuration, secretCode, numSlotsDay, scheduleStartDate, scheduleEndDate, byWeek.getTimeSlots(), response, 200);
				
				
			} catch (Exception e) {
				
				e.printStackTrace();
				
				logger.log(e.getMessage());
				bulkResponse = new BulkEditResponse("Unable to extend start and end dates: " + e.getMessage(), 403);
			}
			
			// compute proper response
			responseJson.put("body", new Gson().toJson(bulkResponse));
			
		}
		
		logger.log("end result: " + responseJson.toJSONString());
		logger.log(responseJson.toJSONString());
		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
		writer.write(responseJson.toJSONString());
		writer.close();
		
	}
	
	
}