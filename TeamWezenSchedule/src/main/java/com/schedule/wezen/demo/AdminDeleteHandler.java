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
import com.schedule.wezen.demo.http.AdminDeleteRequest;
import com.schedule.wezen.demo.http.AdminDeleteResponse;


public class AdminDeleteHandler implements RequestStreamHandler {
	
	public LambdaLogger logger = null;
	
	boolean adminDeleteLambda(int numDays) throws Exception {
		
		SchedulesDAO dao = new SchedulesDAO();
		
		logger.log("DAO created");
		
		return dao.deleteOverDays(numDays);
	}
	
	@Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to create constant");
		
		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "DELETE,GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	        
		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		AdminDeleteResponse response = null;
		
		// extract body from incoming HTTP DELETE request. If any error, then return 422 error
		String body;
		boolean processed = false;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			JSONParser parser = new JSONParser();
			JSONObject event = (JSONObject) parser.parse(reader);
			logger.log("event:" + event.toJSONString());

			body = (String)event.get("body");
			if (body == null) {
				body = event.toJSONString();  // this is only here to make testing easier
			}
		} catch (ParseException pe) {
			logger.log(pe.toString());
			response = new AdminDeleteResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
	        responseJson.put("body", new Gson().toJson(response));
	        processed = true;
	        body = null;
		}

		if (!processed) {
			AdminDeleteRequest req = new Gson().fromJson(body, AdminDeleteRequest.class);
			logger.log(req.toString());
			
			try {
				
				int numDays = Integer.parseInt(req.requestNumDays);
				
				logger.log("Trying to delete list of schedules N days old");
				
				boolean deleted = adminDeleteLambda(numDays);
				
				logger.log("After delete schedule");
				
				logger.log("" + deleted);
				
				if(deleted)
				{
					response = new AdminDeleteResponse(new ArrayList<String>(), "Sucessfully deleted schedules", 200);
				}
				else
				{
					response = new AdminDeleteResponse("Unable to delete schedules", 403);
				}
				
			} catch (Exception e) {
				
				e.printStackTrace();
				
				logger.log("Caught Exception: " + e.getMessage());
				
				response = new AdminDeleteResponse("Unable to delete schedules: " + e.getMessage(), 403);
				
			}
			
			// compute proper response
	        responseJson.put("body", new Gson().toJson(response));  
		}
		
        logger.log("end result:" + responseJson.toJSONString());
        logger.log(responseJson.toJSONString());
        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(responseJson.toJSONString());  
        writer.close();
	}
}
