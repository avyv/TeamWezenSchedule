package com.schedule.wezen.db;

import com.schedule.wezen.model.*;

import java.sql.*;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotsDAO {
	java.sql.Connection conn;
	
    public TimeSlotsDAO() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }

    public TimeSlot getTimeSlot(LocalTime time, LocalDate date) throws Exception {
        
        try {
        	TimeSlot timeSlot = null;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM TimeSlots WHERE (startTime, slotDate) values (?,?);");
            ps.setTime(2,  Time.valueOf(time));
            ps.setDate(4, Date.valueOf(date));
            ResultSet resultSet = ps.executeQuery();
            
            while (resultSet.next()) {
                timeSlot = generateTimeSlot(resultSet);
            }
            resultSet.close();
            ps.close();
            
            return timeSlot;

        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed in getting time slot: " + e.getMessage());
        }
    }
    
    public boolean addTimeSlot(TimeSlot timeSlot) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM TimeSlots WHERE (startTime, slotDate) values (?,?);");
            ps.setTime(2,  Time.valueOf(timeSlot.getStartTime()));
            ps.setDate(4, Date.valueOf(timeSlot.getDate()));
            ResultSet resultSet = ps.executeQuery();
            
            // already present?
            while (resultSet.next()) {
                TimeSlot ts = generateTimeSlot(resultSet);
                resultSet.close();
                return false;
            }

            ps = conn.prepareStatement("INSERT INTO TimeSlot (startTime, slotDate) values(?,?);");
            ps.setTime(2,  Time.valueOf(timeSlot.getStartTime()));
            ps.setDate(4, Date.valueOf(timeSlot.getDate()));
            ps.execute();
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to insert time slot: " + e.getMessage());
        }
    }

    public List<TimeSlot> getAllTimeSlots() throws Exception {
        
        List<TimeSlot> allTS = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM Time Slots";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                TimeSlot ts = generateTimeSlot(resultSet);
                allTS.add(ts);
            }
            resultSet.close();
            statement.close();
            return allTS;

        } catch (Exception e) {
            throw new Exception("Failed in getting books: " + e.getMessage());
        }
    }
    
    private TimeSlot generateTimeSlot(ResultSet resultSet) throws Exception {
    	Time startTime = resultSet.getTime("startTime");
    	Time duration = resultSet.getTime("duration");
    	Date slotDate = resultSet.getDate("slotDate");
        return new TimeSlot (LocalTime.parse(startTime.toString()), LocalTime.parse(duration.toString()), LocalDate.parse(slotDate.toString()));
    }
}
