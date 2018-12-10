package com.schedule.wezen.db;

import java.sql.*;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.schedule.wezen.model.*;

public class SchedulesDAO {
	
	java.sql.Connection conn;
	TimeSlotsDAO tsdao = new TimeSlotsDAO();

    public SchedulesDAO() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		e.printStackTrace();
    		conn = null;
    	}
    }

    public Schedule getSchedule(String id) throws Exception {
        
        try {
            Schedule schedule = null;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedules WHERE id=?;");
            ps.setString(1,  id);
            ResultSet resultSet = ps.executeQuery();
            
            while (resultSet.next()) {
                schedule = generateSchedule(resultSet);
            }
            resultSet.close();
            ps.close();
            
            return schedule;

        } catch (Exception e) {
        	e.printStackTrace();
        	
            throw new Exception("Failed in getting schedule: " + e.getMessage());
        }
    }
    
    public boolean deleteSchedule(String id) throws Exception {
        try {
        	tsdao.deleteAllScheduleTimeSlots(id);
        	PreparedStatement psts = conn.prepareStatement("DELETE FROM TimeSlots WHERE sid = ?;");
        	psts.setString(1, id);
        	psts.execute();
        	psts.close();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Schedules WHERE id = ?;");
            ps.setString(1, id);
            ps.execute();
            ps.close();
            
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to delete schedule: " + e.getMessage());
        }
    }

    public boolean updateSchedule(Schedule schedule) throws Exception {
        try {
        	String query = "UPDATE Schedules SET startDate=?, endDate=?, startTime=?, endTime=? WHERE id=?;";
        	PreparedStatement ps = conn.prepareStatement(query);
        	ps.setDate(1, Date.valueOf(schedule.getStartDate().toString()));
        	ps.setDate(2, Date.valueOf(schedule.getEndDate().toString()));
        	ps.setTime(3, Time.valueOf(schedule.getStartTime().toString()));
        	ps.setTime(4, Time.valueOf(schedule.getEndTime().toString()));
        	
            int numAffected = ps.executeUpdate();
            ps.close();
            
            return (numAffected == 1);
        } catch (Exception e) {
            throw new Exception("Failed to update report: " + e.getMessage());
        }
    }
    
    
    public boolean addSchedule(Schedule schedule) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedules WHERE id = ?;");
            ps.setString(1, schedule.getId());
            ResultSet resultSet = ps.executeQuery();
            
            // already present?
            if (resultSet.next()) {
//                Schedule s = generateSchedule(resultSet);
//                resultSet.close();
                return false;
            }

            ps = conn.prepareStatement("INSERT INTO Schedules (startDate,endDate,startTime,endTime,duration,id,secretCode,created) VALUES (?,?,?,?,?,?,?,?);");
            
            ps.setDate(1, Date.valueOf(schedule.getStartDate()));
        	ps.setDate(2, Date.valueOf(schedule.getEndDate()));
        	ps.setTime(3, Time.valueOf(schedule.getStartTime()));
        	ps.setTime(4, Time.valueOf(schedule.getEndTime()));
        	ps.setInt(5, schedule.getSlotDuration());
        	ps.setString(6, schedule.getId());
        	ps.setInt(7, schedule.getSecretCode());
        	ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        	
        	for(TimeSlot ts: schedule.getTimeSlots()) {
        		tsdao.addTimeSlot(ts);
        	}
        	
            
            ps.execute();
            return true;

        } catch (Exception e) {
        	e.printStackTrace();
        	
            throw new Exception("Failed to insert schedule: "/* + str2*/ + ":" + e.getMessage());
        }
    }
    
    //TODO get schedules created in last n hours
    public List<Schedule> getCreatedInLastHours(int num) throws Exception{
    	List<Schedule> inLastNum;
    	LocalDateTime now = LocalDateTime.now();
    	for(Schedule s: getAllSchedules()) {
    		//TODO
    	}
    }
    
    //TODO delete schedules over n days old

    public List<Schedule> getAllSchedules() throws Exception {
        
        List<Schedule> allSchedules = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM Schedules";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Schedule s = generateSchedule(resultSet);
                allSchedules.add(s);
            }
            resultSet.close();
            statement.close();
            return allSchedules;

        } catch (Exception e) {
            throw new Exception("Failed in getting books: " + e.getMessage());
        }
    }
    
    public void deleteAllSchedules() {
    	PreparedStatement ps;
		try {
			ps = conn.prepareStatement("DELETE FROM Schedules;");
			
			ps.execute();
			
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private Schedule generateSchedule(ResultSet resultSet) throws Exception {
        Date startDate = resultSet.getDate("startDate");
        Date endDate = resultSet.getDate("endDate");
        Time startTime = resultSet.getTime("startTime");
        Time endTime = resultSet.getTime("endTime");
        int duration = resultSet.getInt("duration");
        String id = resultSet.getString("id");
        int secretCode = resultSet.getInt("secretCode");
        Timestamp created = resultSet.getTimestamp("created");
        
        
        Schedule toRet = new Schedule (startDate.toLocalDate(), endDate.toLocalDate(), startTime.toLocalTime(), endTime.toLocalTime(), duration, id, secretCode, created.toLocalDateTime());
        toRet.emptyTimeSlots();
        
        TimeSlotsDAO getTimeSlots = new TimeSlotsDAO();
        List<TimeSlot> scheduleTimeSlots = getTimeSlots.getAllScheduleTimeSlots(id); // get a list of all the schedules timeSlots
        for(TimeSlot ts: scheduleTimeSlots) { // Add all of the schedules timeSlots to its timeSlot arraylist
        	toRet.addTimeSlot(ts);
        }
        
        return toRet;
    }

}