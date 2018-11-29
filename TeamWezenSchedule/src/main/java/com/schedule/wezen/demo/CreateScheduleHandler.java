package com.schedule.wezen.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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

import com.schedule.wezen.demo.http.CreateScheduleRequest;
import com.schedule.wezen.demo.http.CreateScheduleResponse;


public class CreateScheduleHandler implements RequestStreamHandler {
	
	public LambdaLogger logger = null;
	
	/**
	 * Load from RDS, if it exists (WILL DO THIS EVENTUALLY)
	 * 
	 * @throws Exception
	 */
	boolean createSchedule() throws Exception {
		return true;
	}

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

    	logger = context.getLogger();
		logger.log("Loading Java Lambda handler to create constant");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	        
		JSONObject createScheduleResponseJson = new JSONObject();
		createScheduleResponseJson.put("headers", headerJson);
		
		// This is where the response will be stored
		CreateScheduleResponse cSchedResponse = null;
		
		// extract body from incoming HTTP POST request. If any error, then return 422 error
		String body;
		boolean processed = false;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			JSONParser parser = new JSONParser();
			JSONObject event = (JSONObject) parser.parse(reader);
			logger.log("event:" + event.toJSONString());
			
			String method = (String) event.get("httpMethod");
			if (method != null && method.equalsIgnoreCase("OPTIONS")) {
				logger.log("Options request");
				cSchedResponse = new CreateScheduleResponse("Success: Schedule created!!!", 200);  // OPTIONS needs a 200 response
		        createScheduleResponseJson.put("body", new Gson().toJson(cSchedResponse));
		        processed = true;
		        body = null;
			} else {
				body = (String)event.get("body");
				if (body == null) {
					body = event.toJSONString();  // this is only here to make testing easier
				}
			}
		} catch (ParseException pe) {
			logger.log(pe.toString());
			cSchedResponse = new CreateScheduleResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
	        createScheduleResponseJson.put("body", new Gson().toJson(cSchedResponse));
	        processed = true;
	        body = null;
		}

		if (!processed) {
			CreateScheduleRequest req = new Gson().fromJson(body, CreateScheduleRequest.class);
			logger.log(req.toString());

			CreateScheduleResponse resp;
			try {
				/* NOTE: When the database is created, we will use these lines to create Schedules */
				
				// DATABASE STUFF!!!
				
				if (createSchedule()) {
					resp = new CreateScheduleResponse("Successfully created schedule");
				} else {
					resp = new CreateScheduleResponse("Unable to create schedule: ", 422);
				}
			} catch (Exception e) {
				resp = new CreateScheduleResponse("Unable to create schedule: (" + e.getMessage() + ")", 403);
			}

			// compute proper response
	        createScheduleResponseJson.put("body", new Gson().toJson(resp));  
		}
		
        logger.log("end result:" + createScheduleResponseJson.toJSONString());
        logger.log(createScheduleResponseJson.toJSONString());
        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(createScheduleResponseJson.toJSONString());  
        writer.close();
    }

}
