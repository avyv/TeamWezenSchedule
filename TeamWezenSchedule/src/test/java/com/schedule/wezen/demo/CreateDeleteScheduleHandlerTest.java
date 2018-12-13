package com.schedule.wezen.demo;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.schedule.wezen.db.DatabaseUtil;
import com.schedule.wezen.db.TestContext;
import com.schedule.wezen.demo.http.PostResponse;


/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class CreateDeleteScheduleHandlerTest {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }

	
    private static final String SAMPLE_INPUT_STRING_CREATE = "{\"requestStartDate\":\"2018-12-10\",\"requestEndDate\":\"2018-12-12\",\"requestStartTime\":\"01:00:00\",\"requestEndTime\":\"02:00:00\",\"requestSlotDuration\":\"10\",\"requestID\":\"mysched\"}";
    private static final String SAMPLE_INPUT_STRING_DELETE = "{\"requestSchedID\":\"mysched\"}";
    private static final String EXPECTED_OUTPUT_STRING = "{\"FOO\": \"BAR\"}";

    
    @Test
    public void testDeleteScheduleHandler() throws IOException {
        DeleteScheduleHandler handler = new DeleteScheduleHandler();

        InputStream input = new ByteArrayInputStream(SAMPLE_INPUT_STRING_DELETE.getBytes());;
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("sample"));

        // TODO: validate output here if needed.
        String sampleOutputString = output.toString();
        JSONParser json = new JSONParser();
        try {
			JSONObject obj = (JSONObject) json.parse(sampleOutputString);
			String body = (String) obj.get("body");
			JSONObject bson = (JSONObject) json.parse(body);
			Assert.assertEquals("Successfully deleted schedule", bson.get("deleteScheduleResponse"));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    @Test
    public void testCreateScheduleHandler() throws IOException {
    	
        CreateScheduleHandler handler = new CreateScheduleHandler();

        InputStream input = new ByteArrayInputStream(SAMPLE_INPUT_STRING_CREATE.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("sample")); 

        // TODO: validate output here if needed.
        String sampleOutputString = output.toString();
        JSONParser json = new JSONParser();
        try {
			JSONObject obj = (JSONObject) json.parse(sampleOutputString);
			String body = (String) obj.get("body");
			JSONObject bson = (JSONObject) json.parse(body);
			Assert.assertEquals("Successfully created schedule", bson.get("response"));
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    @Test
    public void testDeleteScheduleHandlerFromFile() throws IOException {
        DeleteScheduleHandler handler = new DeleteScheduleHandler();

        FileInputStream input = new FileInputStream( new File("src/test/resources/sampleDeleteSchedule.in"));
        
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("sample"));

        // TODO: validate output here if needed.
        String sampleOutputString = output.toString();
        JSONParser json = new JSONParser();
        try {
			JSONObject obj = (JSONObject) json.parse(sampleOutputString);
			String body = (String) obj.get("body");
			JSONObject bson = (JSONObject) json.parse(body);
			Assert.assertEquals("Successfully deleted schedule", bson.get("deleteScheduleResponse"));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    @Test
    public void testCreateScheduleHandlerFromFile() throws IOException {
    	
    	Assert.assertTrue (DatabaseUtil.conn != null);
        CreateScheduleHandler  handler = new CreateScheduleHandler ();

        FileInputStream input = new FileInputStream( new File("src/test/resources/sampleCreateSchedule.in"));
        
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("sample"));

        // TODO: validate output here if needed.
        PostResponse pr = new Gson().fromJson(output.toString(), PostResponse.class);
        JSONParser json = new JSONParser();
		JSONObject bson = null;
		try {
			bson = (JSONObject) json.parse(pr.body);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals("Successfully created schedule", bson.get("response"));
    }
    
    
}
