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
import com.schedule.wezen.demo.http.CreateScheduleRequest;
import com.schedule.wezen.demo.http.PostResponse;


/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class CreateScheduleHandlerTest {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }

	
    private static final String SAMPLE_INPUT_STRING = "{\"startDate\":\"2018-11-30\",\"endDate\":\"2018-12-01\",\"startTime\":\"08:00\",\"endTime\":\"17:00\",\"slotDuration\":\"10\"}";
    private static final String EXPECTED_OUTPUT_STRING = "{\"FOO\": \"BAR\"}";

    @Test
    public void testLambdaFunctionHandler() throws IOException {
        LambdaFunctionHandler handler = new LambdaFunctionHandler();

        InputStream input = new ByteArrayInputStream(SAMPLE_INPUT_STRING.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("sample")); 

        // TODO: validate output here if needed.
        String sampleOutputString = output.toString();
        JSONParser json = new JSONParser();
        try {
			JSONObject obj = (JSONObject) json.parse(sampleOutputString);
			String body = (String) obj.get("body");
			JSONObject bson = (JSONObject) json.parse(body);
			Assert.assertEquals("Successfully created schedule", bson.get("createScheduleResponse"));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    @Test
    public void testCreateScheduleHandlerFromFile() throws IOException {
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
		Assert.assertEquals("Successfully created schedule", bson.get("createScheduleResponse"));        
    }
}
