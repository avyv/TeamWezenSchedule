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
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM TimeSlots WHERE startTime=? AND slotDate=?;");
            ps.setTime(1,  Time.valueOf(time));
            ps.setDate(2, Date.valueOf(date));
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
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM TimeSlots WHERE startTime=? AND slotDate=?;");
            ps.setTime(1,  Time.valueOf(timeSlot.getStartTime()));
            ps.setDate(2, Date.valueOf(timeSlot.getDate()));
            ResultSet resultSet = ps.executeQuery();
            
            // already present?
            if (resultSet.next()) {
                TimeSlot ts = generateTimeSlot(resultSet);
                resultSet.close();
                return false;
            }

            ps = conn.prepareStatement("INSERT INTO TimeSlots (sid, startTime, slotDate, id, meetingName, secretCode, isOpen, hasMeeting) values(?,?,?,?,?,?,?,?);");
            ps.setString(1, timeSlot.getSid());
            ps.setTime(2,  Time.valueOf(timeSlot.getStartTime()));
            ps.setDate(3, Date.valueOf(timeSlot.getDate()));
            ps.setString(4, timeSlot.getId());
            ps.setString(5,  timeSlot.getMeeting());
            ps.setInt(6, timeSlot.getSecretCode());
            ps.setBoolean(7, timeSlot.getIsOpen());
            ps.setBoolean(8, timeSlot.getHasMeeting());
            ps.execute();
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to insert time slot: " + e.getMessage());
        }
    }
    
    public boolean deleteTimeSlot(TimeSlot ts) throws Exception {
        try {
        	PreparedStatement ps = conn.prepareStatement("DELETE FROM TimeSlots WHERE id = ?;");
        	ps.setString(1, ts.getId());
            int numAffected = ps.executeUpdate();
            ps.close();
            
            return (numAffected == 1);

        } catch (Exception e) {
            throw new Exception("Failed to delete schedule: " + e.getMessage());
        }
    }

    public List<TimeSlot> getAllTimeSlots() throws Exception {
        
        List<TimeSlot> allTS = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM TimeSlots";
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
    
    public List<TimeSlot> getAllScheduleTimeSlots(String sid) throws Exception {
    	
    	List<TimeSlot> scheduleTS = new ArrayList<>();
        try { 
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM TimeSlots WHERE sid=?;"); // selects all timeslots with the entered sid
            ps.setString(1,  sid);
            
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) { // adds all timeslots from the resultset to the returned list
                TimeSlot ts = generateTimeSlot(resultSet);
                scheduleTS.add(ts);
            }
            resultSet.close();
            ps.close();
            return scheduleTS; // return the list of timeslots in the database with the entered id

        } catch (Exception e) {
            throw new Exception("Failed in getting timeSlots: " + e.getMessage());
        }
    }
    
    public void deleteAllScheduleTimeSlots(String sid) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM TimeSlots WHERE sid=?;");
        	ps.setString(1, sid);
        	
        	ps.execute();
        	
            ps.close();
            
            
        } catch (Exception e) {
            throw new Exception("Failed in deleting timeSlots: " + e.getMessage());
        }
    }
    
    public void deleteAllTimeSlots() {
    	PreparedStatement ps;
		try {
			ps = conn.prepareStatement("DELETE FROM TimeSlots;");
			
			ps.execute();
			
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private TimeSlot generateTimeSlot(ResultSet resultSet) throws Exception {
    	String sid = resultSet.getString("sid");
    	Time startTime = resultSet.getTime("startTime");
    	Date slotDate = resultSet.getDate("slotDate");
    	String id = resultSet.getString("id");
    	String meetingName = resultSet.getString("meetingName");
    	int secretCode = resultSet.getInt("secretCode");
    	boolean isOpen = resultSet.getBoolean("isOpen");
    	boolean hasMeeting = resultSet.getBoolean("hasMeeting");
        return new TimeSlot(LocalTime.parse(startTime.toString()), LocalDate.parse(slotDate.toString()).plusDays(1), id, meetingName, sid, secretCode, isOpen, hasMeeting);
    }
}
