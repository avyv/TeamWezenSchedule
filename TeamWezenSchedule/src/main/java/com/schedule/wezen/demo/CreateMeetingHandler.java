package com.schedule.wezen.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
import com.schedule.wezen.demo.http.CreateMeetingRequest;
import com.schedule.wezen.demo.http.CreateMeetingResponse;
import com.schedule.wezen.demo.http.CreateScheduleResponse;
import com.schedule.wezen.model.Model;
import com.schedule.wezen.model.Schedule;
import com.schedule.wezen.model.TimeSlot;


public class CreateMeetingHandler implements RequestStreamHandler {

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

        /*// TODO: Implement your stream handler. See https://docs.aws.amazon.com/lambda/latest/dg/java-handler-io-type-stream.html for more information.
        // This demo implementation capitalizes the characters from the input stream.
        int letter = 0;
        while((letter = input.read()) >= 0) {
            output.write(Character.toUpperCase(letter));
        }*/
    	
    	LambdaLogger logger = context.getLogger();
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
		CreateMeetingResponse createMeetingResponse = null;
		
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
				
				
				createMeetingResponse = new CreateMeetingResponse("", 200);  // OPTIONS needs a 200 response
				
				// dateResponseJson is the object created earlier to hold the JSON response
		        dateResponseJson.put("body", new Gson().toJson(createMeetingResponse)); // puts the 200 response into the JSON body
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
		
			createMeetingResponse = new CreateMeetingResponse("Unable to process", 422);  // Send a 422 code (unable to process input)
	        dateResponseJson.put("body", new Gson().toJson(createMeetingResponse));
	        processed = true;
	        body = null;
		}
		
		// If the request is a body or a get request
		logger.log("processedL" + processed);
		if (!processed) {
			
			logger.log("in not processed");
			
			CreateMeetingRequest createMeetingRequest = new Gson().fromJson(body, CreateMeetingRequest.class); // create a new DateRequest
			logger.log(createMeetingRequest.toString()); // log request

			// This is where stuff starts happening
			
			
			logger.log("Before creating SchedulesDAO object");
			
			
			SchedulesDAO dao = new SchedulesDAO();
			
			Schedule retrievedSchedule = null;
			
			boolean didRetrieveSchedule = false; 
			
			
			try { 
				
				logger.log("Before attempt at retrieving schedule");
				
				retrievedSchedule = dao.getSchedule(createMeetingRequest.requestSchedID);
				
				logger.log("After retrieving schedule: " + retrievedSchedule.getId());
				
				didRetrieveSchedule = true;
				
			} catch (Exception e) {
				
				logger.log("Caught exception: " + e.getMessage());
				
				createMeetingResponse = new CreateMeetingResponse("Unable to retrieve schedule: (" + e.getMessage() + ")", 403);
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
				
				
				ArrayList<Schedule> scheduleDividedByWeeks = retrievedSchedule.divideByWeeks(retrievedSchedule.getStartDate(), retrievedSchedule.getEndDate(), retrievedSchedule.getStartTime(), retrievedSchedule.getEndTime(), retrievedSchedule.getSlotDuration(), retrievedSchedule.getId(), retrievedSchedule.getNumSlotsDay());
				
				logger.log("After initializing variables, before retrieving the correct weekly schedule");
				
				Schedule byWeek = null;
				int week = -1;
				
				for(Schedule schedule: scheduleDividedByWeeks) {
					
					week++;
					
					if((schedule.getStartDate().toString()).equals(createMeetingRequest.requestWeekStart)) {
						
						logger.log("Found the correct week");
						
						byWeek = scheduleDividedByWeeks.get(week);
					}
				}
				
				logger.log("After 1st for loop, before 2nd");
				
				
				for(TimeSlot timeslot : byWeek.getTimeSlots()) {
					
					if(timeslot.getId().equals(createMeetingRequest.requestTSId)) {
						
						logger.log("Found the correct timeslot to insert meeting");
						
						timeslot.createMeeting(createMeetingRequest.requestMtngName);
					}
				}
				
				logger.log("After 2nd for loop");
				
				startDateOfWeek = byWeek.getStartDate().toString();
				
				String response = "Successfully created meeting";
				
				createMeetingResponse = new CreateMeetingResponse(startDateOfWeek, startTime, scheduleID, slotDuration, secretCode, numSlotsDay, scheduleStartDate, scheduleEndDate, byWeek.getTimeSlots(), response, 200);
				
			}
			
			// compute proper response
			
	        dateResponseJson.put("body", new Gson().toJson(createMeetingResponse)); // put it in responseJson 
		}
		
        logger.log("end result:" + dateResponseJson.toJSONString()); // log that the process is finished
        logger.log(dateResponseJson.toJSONString()); // log responseJson
        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8"); // create an OutputStreamWriter object
        writer.write(dateResponseJson.toJSONString()); // write the response and send it out
        writer.close(); // close
    	
    }

}
