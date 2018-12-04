//IRRELEVANT CLASS
/*package com.schedule.wezen.db;

import com.schedule.wezen.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeetingsDAO {
	java.sql.Connection conn;
	
    public MeetingsDAO() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }

    public Meeting getMeeting(String tid) throws Exception {
        
        try {
            Meeting meeting = null;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Meetings WHERE tid=?;");
            ps.setString(1,  tid);
            ResultSet resultSet = ps.executeQuery();
            
            while (resultSet.next()) {
                meeting = generateMeeting(resultSet);
            }
            resultSet.close();
            ps.close();
            
            return meeting;

        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed in getting meeting: " + e.getMessage());
        }
    }
    
    public boolean deleteMeeting(Meeting meeting) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Meetings WHERE tid = ?;");
            ps.setString(1, meeting.getTid());
            int numAffected = ps.executeUpdate();
            ps.close();
            
            return (numAffected == 1);

        } catch (Exception e) {
            throw new Exception("Failed to insert meeting: " + e.getMessage());
        }
    }
    
    public boolean addMeeting(Meeting meeting) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Meetings WHERE tid = ?;");
            ps.setString(1, meeting.getTid());
            ResultSet resultSet = ps.executeQuery();
            
            // already present?
            while (resultSet.next()) {
                Meeting m = generateMeeting(resultSet);
                resultSet.close();
                return false;
            }

            ps = conn.prepareStatement("INSERT INTO Meetings (tid, name) values(?,?);");
            ps.setString(1, meeting.getTid());
            ps.setString(2, meeting.getName());
            ps.execute();
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to insert meeting: " + e.getMessage());
        }
    }

    public List<Meeting> getAllMeetings() throws Exception {
        
        List<Meeting> allMeetings = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM Meetings";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Meeting m = generateMeeting(resultSet);
                allMeetings.add(m);
            }
            resultSet.close();
            statement.close();
            return allMeetings;

        } catch (Exception e) {
            throw new Exception("Failed in getting books: " + e.getMessage());
        }
    }
    
    private Meeting generateMeeting(ResultSet resultSet) throws Exception {
    	String tid = resultSet.getString("tid");
    	String name  = resultSet.getString("name");
        return new Meeting (tid, name);
    }
}*/
