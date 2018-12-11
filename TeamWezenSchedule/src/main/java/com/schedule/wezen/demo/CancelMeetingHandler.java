package com.schedule.wezen.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.google.gson.Gson;
import com.schedule.wezen.db.SchedulesDAO;
import com.schedule.wezen.db.TimeSlotsDAO;
import com.schedule.wezen.demo.http.CancelMeetingRequest;
import com.schedule.wezen.demo.http.CancelMeetingResponse;
import com.schedule.wezen.model.Model;
import com.schedule.wezen.model.Schedule;
import com.schedule.wezen.model.TimeSlot;


public class CancelMeetingHandler implements RequestStreamHandler {
	
	public static LambdaLogger logger = null;
	
	
	boolean cancelMeetingLambda(String tsid) throws Exception {
		if(logger != null) { logger.log("in cancelMeetingLambda"); }
		
		TimeSlotsDAO tsdao = new TimeSlotsDAO();
		
		logger.log("DAO created");
		
		tsdao.deleteMeeting(tsid);
		
		return true;
		
	}
	

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

    	logger = context.getLogger();
		logger.log("Loading Java Lambda handler of RequestStreamHandler");
		
		// This is the header for all the JSON code
		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	    
	    // This is the actual JSON response object
		JSONObject dateResponseJson = new JSONObject();
		dateResponseJson.put("headers", headerJson);

		// This is the object where the 200 or 400 codes and response message will be stored
		CancelMeetingResponse cancelMeetingResponse = null;
		
		// extract body from incoming HTTP POST request. If any error, then return 422 error
		// The InputStream object "input" (parameter) is the request
		String body;
		boolean processed = false;
		try {
			// The input request gets turned into a JSON object
			BufferedReader reader = new BufferedReader(new InputStreamReader(input)); // Reads the input
			JSONParser parser = new JSONParser(); // creates a JSON parser, will convert input into JSON code
			JSONObject event = (JSONObject) parser.parse(reader); // This JSON object will hold the converted value of the input
			logger.log("event:" + event.toJSONString()); // logs the request
			
			// Determines what kind of method the request is (get, body, options)
			String method = (String) event.get("httpMethod"); // turns the method into a string
			logger.log("method:" + method);
			if (method != null && method.equalsIgnoreCase("OPTIONS")) { // if the method is OPTIONS...
				logger.log("Options request"); // log it as an OPTIONS request
				
				
				cancelMeetingResponse = new CancelMeetingResponse("", 200);  // OPTIONS needs a 200 response
				
				// dateResponseJson is the object created earlier to hold the JSON response
		        dateResponseJson.put("body", new Gson().toJson(cancelMeetingResponse)); // puts the 200 response into the JSON body
		        processed = true; // tell the program the response has been processed
		        body = null;
			} else { // If the request is a get or body
				logger.log("in-branch");
				body = (String)event.get("body");
				logger.log("pre-body:" + body);
				if (body == null) {
					body = event.toJSONString();  // this is only here to make testing easier
				}
				logger.log("body:" +body);
			}
			// if the input couldn't be processed
		} catch (ParseException pe) {
			logger.log("exceptionL::" + pe.toString());
		
			cancelMeetingResponse = new CancelMeetingResponse("Unable to process", 422);  // Send a 422 code (unable to process input)
	        dateResponseJson.put("body", new Gson().toJson(cancelMeetingResponse));
	        processed = true;
	        body = null;
		}
		
		// If the request is a body or a get request
		logger.log("processedL" + processed);
		if (!processed) {
			
			logger.log("in not processed");
			
			CancelMeetingRequest cancelMeetingRequest = new Gson().fromJson(body, CancelMeetingRequest.class); // create a new DateRequest
			logger.log(cancelMeetingRequest.toString()); // log request

			// This is where stuff starts happening
			
			String response = "";
			
			logger.log("Before try statement to cancel meeting");
			
			try {
				
				logger.log("In try statement, before cancelMeetingLambda");
				
				cancelMeetingLambda(cancelMeetingRequest.requestTSId);
				
				response = "Successfully canceled meeting";
				
			} catch (Exception e) {
				
				logger.log(e.getStackTrace().toString());
				
				logger.log(e.getMessage());
				
				response = "Unable to cancel meeting";
				
				cancelMeetingResponse = new CancelMeetingResponse(response + " (" + e.getMessage(), 403);
			
			}
			
			
			logger.log("Before creating SchedulesDAO object");
			
			SchedulesDAO dao = new SchedulesDAO();
			
			Schedule retrievedSchedule = null;
			
			boolean didRetrieveSchedule = false;
			
			try { 
				
				logger.log("Before attempt at retrieving schedule");
				
				retrievedSchedule = dao.getSchedule(cancelMeetingRequest.requestSchedID);
				
				logger.log("After retrieving schedule: " + retrievedSchedule.getId());
				
				didRetrieveSchedule = true;
				
				
			} catch (Exception e) {
				
				e.printStackTrace();
				
				logger.log("Caught exception: " + e.getMessage());
				
				cancelMeetingResponse = new CancelMeetingResponse("Unable to retrieve schedule: (" + e.getMessage() + ")", 403);
			} 
			
			
			if(didRetrieveSchedule) {
				
				logger.log("After schedule was successfully retrieved");
				
				
				String startDateOfWeek = "";
				String startTime = retrievedSchedule.getStartTime().toString();
				String scheduleID = retrievedSchedule.getId();
				int slotDuration = retrievedSchedule.getSlotDuration();
				int secretCode = retrievedSchedule.getSecretCode();
				int numSlotsDay = retrievedSchedule.getNumSlotsDay();
				String scheduleStartDate = retrievedSchedule.getStartDate().toString();
				String scheduleEndDate = retrievedSchedule.getEndDate().toString();
				
				logger.log("Filtering Schedule");
				
				Model m = new Model();
				
				int month = Integer.parseInt(cancelMeetingRequest.requestMonth);
				int year = Integer.parseInt(cancelMeetingRequest.requestYear);
				int dayWeek = Integer.parseInt(cancelMeetingRequest.requestWeekDay);
				int dayMonth = Integer.parseInt(cancelMeetingRequest.requestDate);
				LocalTime time = m.stringToTime(cancelMeetingRequest.requestTime);
				
				retrievedSchedule.searchForTime(month, year, dayWeek, dayMonth, time);
				
				logger.log("Finished Filtering");
				
				
				ArrayList<Schedule> scheduleDividedByWeeks = retrievedSchedule.divideByWeeks(/*retrievedSchedule.getStartDate(), retrievedSchedule.getEndDate(), retrievedSchedule.getStartTime(), retrievedSchedule.getEndTime(), retrievedSchedule.getSlotDuration(), retrievedSchedule.getId(), retrievedSchedule.getNumSlotsDay()*/);
				
				logger.log("After initializing variables, before retrieving the correct weekly schedule");
				
				Schedule byWeek = null;
				
				int week = 0;
				
				for(Schedule schedule: scheduleDividedByWeeks) {
					
					if((schedule.getStartDate().toString()).equals(cancelMeetingRequest.requestWeekStart)) {
						
						logger.log("Found the correct week");
						
						byWeek = scheduleDividedByWeeks.get(week);
					}
					
					week++;
				}
				
				logger.log("After for loop");
				
				startDateOfWeek = byWeek.getStartDate().toString();
				
				response = "Successfully retrieved schedule";
				
				cancelMeetingResponse = new CancelMeetingResponse(startDateOfWeek, startTime, scheduleID, slotDuration, secretCode, numSlotsDay, scheduleStartDate, scheduleEndDate, byWeek.getTimeSlots(), response, 200);
				
			}
			
			// compute proper response
			
	        dateResponseJson.put("body", new Gson().toJson(cancelMeetingResponse)); // put it in responseJson 
		}
		
        logger.log("end result:" + dateResponseJson.toJSONString()); // log that the process is finished
        logger.log(dateResponseJson.toJSONString()); // log responseJson
        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8"); // create an OutputStreamWriter object
        writer.write(dateResponseJson.toJSONString()); // write the response and send it out
        writer.close(); // close
    	
    }

}
