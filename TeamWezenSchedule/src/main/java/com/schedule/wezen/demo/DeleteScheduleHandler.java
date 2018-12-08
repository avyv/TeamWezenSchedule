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

import com.schedule.wezen.db.SchedulesDAO;
import com.schedule.wezen.demo.http.DeleteScheduleRequest;
import com.schedule.wezen.demo.http.DeleteScheduleResponse;
import com.schedule.wezen.model.Model;
import com.schedule.wezen.model.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;


public class DeleteScheduleHandler implements RequestStreamHandler {
	
	public LambdaLogger logger = null;
	
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

		DeleteScheduleResponse response = null;
		
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
			response = new DeleteScheduleResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
	        responseJson.put("body", new Gson().toJson(response));
	        processed = true;
	        body = null;
		}

		if (!processed) {
			DeleteScheduleRequest req = new Gson().fromJson(body, DeleteScheduleRequest.class);
			logger.log(req.toString());
			
			logger.log("Before SchedulesDAO");

			SchedulesDAO dao = new SchedulesDAO();
			
			logger.log("After SchedulesDAO before model");
			
			Model m = new Model();
			
			logger.log("After model, before Strings");
			
			String startd = "2018-03-11";
			String endd = "2018-03-12";
			String startt = "11:00:00";
			String endt = "12:00:00";
			
			LocalDate sd = m.stringToDate(startd);
			LocalDate ed = m.stringToDate(endd);
			LocalTime st = m.stringToTime(startt);
			LocalTime et = m.stringToTime(endt);
			int dur = 0;
			int secretCode = 0;
			
			logger.log("After initializations");
			
			
			// See how awkward it is to call delete with an object, when you only
			// have one part of its information?
			Schedule schedule = new Schedule(sd, ed, st, et, dur, req.requestSchedID, secretCode);
			
			logger.log("After creating new dummy schedule with real secret code");
			
			DeleteScheduleResponse resp;
			try {
				
				logger.log("In try");
				
				if (dao.deleteSchedule(schedule)) {
					
					logger.log("After DAO delete schedule");
					
					resp = new DeleteScheduleResponse("Successfully deleted schedule: " + req.requestSchedID);
				} else {
					
					logger.log("In else");
					
					resp = new DeleteScheduleResponse("Unable to delete schedule: " + req.requestSchedID, 422);
				}
			} catch (Exception e) {
				
				logger.log("Exception caught");
				
				resp = new DeleteScheduleResponse("Unable to delete schedule: " + req.requestSchedID + "(" + e.getMessage() + ")", 403);
			}
			
			// compute proper response
	        responseJson.put("body", new Gson().toJson(resp));  
		}
		
        logger.log("end result:" + responseJson.toJSONString());
        logger.log(responseJson.toJSONString());
        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(responseJson.toJSONString());  
        writer.close();
	}
}
