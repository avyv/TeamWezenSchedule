package com.schedule.wezen.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseUtil {
	
	// These are to be configured and NEVER stored in the code.
	// once you retrieve this code, you can update
	/*public final static String rdsMySqlDatabaseUrl;
	public final static String dbUsername;
	public final static String dbPassword;
	
	public final static String jdbcTag;
	public final static String rdsMySqlDatabasePort;
	public final static String multiQueries;
	
	public final static String dbName;*/
	
	// pooled across all usages.
	static Connection conn;
	
	/**
	 * Singleton access to DB connection to share resources effectively across multiple accesses.
	 */
	protected static Connection connect() throws Exception {
		if (conn != null) { return conn;}
		
		try {
			//System.out.println("start connecting......");
			Class.forName("com.mysql.jdbc.Driver");
			//conn = DriverManager.getConnection();
			//System.out.println("Database has been connected successfully.");
			return conn;
		} catch (Exception ex) {
			throw new Exception("Failed in database connection");
		}
	}
	
}