package com.ibp.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlListener {

	private  String db_driver;
	private   String db_url;
	private   String db_username;
	private   String db_password;

	public Connection getMysqlConnection() {
		try {
			
			String dbName = "plugin db name";

			db_username = "db username";
			db_password = "db password"; 

			db_driver = "com.mysql.jdbc.Driver";
			db_url = "jdbc:mysql://localhost:3306/" + dbName;

			System.out.println("Database Driver :"+db_driver+ " Database URL : "+db_url+" uname: "+db_username);
		
			Class.forName(db_driver);

			
			System.out.println("Connecting to database...");
			Connection conn = DriverManager.getConnection(db_url,db_username,db_password);
			System.out.println("con in listener msql:"+conn);
			return conn;
		} catch (SQLException |ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
