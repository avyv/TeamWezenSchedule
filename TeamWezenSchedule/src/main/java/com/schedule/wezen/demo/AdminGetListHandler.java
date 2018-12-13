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
import com.schedule.wezen.demo.http.AdminGetListRequest;
import com.schedule.wezen.demo.http.AdminGetListResponse;
import com.schedule.wezen.model.Schedule;

import java.time.LocalDate;

/**
 * Found gson JAR file from
 * https://repo1.maven.org/maven2/com/google/code/gson/gson/2.6.2/gson-2.6.2.jar
 */
public class AdminGetListHandler implements RequestStreamHandler {
	
	public LambdaLogger logger = null;
	
	/** Load from RDS, if it exists
	 * 
	 * @throws Exception
	 */
	ArrayList<String> adminGetListLambda(String range, int daysOrHours) throws Exception {
		if (logger != null) { logger.log("in retrieveScheduleLambda"); }
		
		SchedulesDAO dao = new SchedulesDAO();
		
		logger.log("After SchedulesDAO object");
		
		ArrayList<String> listOfSchedules = null;
		
		if (range.equals("hr"))
		{
			listOfSchedules = dao.getCreatedInLastHours(daysOrHours);
		}
		else if (range.equals("day"))
		{
			listOfSchedules = dao.getCreatedInLastDays(daysOrHours);
		}
		
		logger.log("After creating schedule object and using getSchedule from the DAO");
		
		return listOfSchedules;
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
		
		AdminGetListResponse adminResponse = null;
		
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
			adminResponse = new AdminGetListResponse("Bad Request: " + pe.getMessage(), 422); // unable to process input
			responseJson.put("body", new Gson().toJson(adminResponse));
			processed = true;
			body = null;
		}
		
		if(!processed) {
			AdminGetListRequest adminRequest = new Gson().fromJson(body, AdminGetListRequest.class);
			logger.log(adminRequest.toString());
			
			logger.log("Before retrieveSchedule");
			
			try {
				
				int numDaysOrHours = Integer.parseInt(adminRequest.requestNumDaysOrHours);
				
				ArrayList<String> listOfSchedules = adminGetListLambda(adminRequest.requestRange, numDaysOrHours);
				
				logger.log("Retrieved List of Schedules");
					
				adminResponse = new AdminGetListResponse(listOfSchedules, "Successfully got list", 200);
				
			} catch (Exception e) {
				
				e.printStackTrace();
				
				logger.log(e.getMessage());
				adminResponse = new AdminGetListResponse("Unable to retrieve schedule: " + e.getMessage(), 403);
			}
			
			// compute proper response
			responseJson.put("body", new Gson().toJson(adminResponse));
			
		}
		
		logger.log("end result: " + responseJson.toJSONString());
		logger.log(responseJson.toJSONString());
		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
		writer.write(responseJson.toJSONString());
		writer.close();
		
	}
	
	
}