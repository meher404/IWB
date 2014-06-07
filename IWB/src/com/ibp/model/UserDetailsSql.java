package com.ibp.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDetailsSql {

	Connection conn;
	Statement stmt;
	ResultSet rs;

	public UserDetailsSql(Connection conn) {
		System.out.println("conn is "+conn);
		this.conn=conn;
	}

	public UserNode setUserDetails(UserNode userNode, String uid){
		try {
			stmt = conn.createStatement();
			Statement stmt1=conn.createStatement();

			String sql1="select interest from userinterests WHERE uid='"+uid+"' AND FREQUENCY=(SELECT MAX(FREQUENCY) FROM userinterests) ;";
			sql1 = sql1.toLowerCase();
			ResultSet rs1=stmt1.executeQuery(sql1);
			String interest="";
			if(rs1.next()){
				interest=rs1.getString(1);
			}else{
				interest="No Interests available";
			}
			String sql;
			sql = "select * from userdetails where uId='"+uid+"' ";
			////sql = sql.toLowerCase();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				userNode.setDob(rs.getString("dob"));
				userNode.setGender(rs.getString("gender"));
				userNode.setImageUrl(rs.getString("imageurl"));
				userNode.setInterests(interest);
				userNode.setName(rs.getString("uName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userNode;
	}

	public void insertUserSql(String uid,String fullname, String gender,String dob,String imageurl, String placeLived)
	{
		try {
			stmt=conn.createStatement();
			System.out.println("stmt:"+stmt);
			String interests="interests not available";
			String sql="insert into userdetails  values('"+uid+"','"+fullname+"','"+gender+"','"+dob+"','"+imageurl+"','"+placeLived+"','"+interests+"',0)";			
			//sql = sql.toLowerCase();
			System.out.println("====================>");
			System.out.println(sql);
			System.out.println("<====================");
			stmt.executeUpdate(sql);
			System.out.println("user"+fullname+" inserted successfully");

		} catch (SQLException e) {
				e.printStackTrace();
			System.out.println("User already in mysql userdetails Table:");
		}
	}
	
	
	public void addSpamMessages(String reportedBy,String reportedOn, String message, String time){
		try {
			stmt=conn.createStatement();
			String sql="insert into spamusers values('"+reportedBy+"','"+reportedOn+"','"+message+"','"+time+"')";			
			//sql = sql.toLowerCase();
			stmt.executeUpdate(sql);
			
		} catch (SQLException e) {
				e.printStackTrace();
		}
	}

	public void UserSpamMessagesUpdate(String reportedOn){
		try {
			stmt=conn.createStatement();
			String sql;
			int spamCount=0;
			sql = "select spamcount from userdetails where uId='"+reportedOn+"' ";
			//sql = sql.toLowerCase();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				spamCount=rs.getInt("spamcount");
			}
			spamCount = spamCount+1;
			String sql1="UPDATE USERDETAILS SET SPAMCOUNT = "+ spamCount +" where uId ='"+reportedOn+"' ;";			
			sql1 = sql1.toLowerCase();
			stmt.executeUpdate(sql1);
			
		} catch (SQLException e) {
				e.printStackTrace();
		}
		
	}
	
	public boolean isUserSpammed(String uid){
		String sql;
		sql = "select spamCount from userdetails where uId='"+uid+"' ";
		//sql = sql.toLowerCase();
		try {
			rs = stmt.executeQuery(sql);
			rs.next();
			if(rs.getInt("spamCount")>5)	//to say user is spammed if spamcount is >5
				return true;
			else
				return false;
		} catch (SQLException e) {
			System.out.println("exception from spam catch");
			e.printStackTrace();
		}
		return false;

	}

	public boolean isUserExists(String uId) {

		return false;
	}

	public void updateCategoryCount(String uid,String category){
		try {

			stmt=conn.createStatement();
			String sql = "select frequency from userinterests where uid='"+uid+"' and interest='"+category+"';" ;
			//sql = sql.toLowerCase();
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				int freq=rs.getInt(1);
				rs.close();
				freq++;
				String sql1="update  userinterests set frequency="+freq+" where uid='"+uid+"' and interest='"+category+"';" ;
				sql1 = sql1.toLowerCase();
				//	stmt=conn.createStatement();
				stmt.executeUpdate(sql1);
			}else{
				sql="insert into userinterests values('"+new String(uid)+"','"+category+"',"+1+")";//inserting into the database
				//sql = sql.toLowerCase();
				stmt.executeUpdate(sql);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateURLCount(String uid,String url, String category){
		try {

			stmt=conn.createStatement();
			String sql = "select frequency from userinterests where uid='"+uid+"' and url='"+url+"';" ;
			//sql = sql.toLowerCase();
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				int freq=rs.getInt(1);
				rs.close();
				freq++;
				String sql1="update  userinterests set frequency="+freq+" where uid='"+uid+"' and interest='"+url+"';" ;
				sql1 = sql1.toLowerCase();
				//	stmt=conn.createStatement();
				stmt.executeUpdate(sql1);
			}else{
				sql="insert into userinterests values('"+new String(uid)+"','"+category+"',"+1+", '"+url+"')";//inserting into the database
				//sql = sql.toLowerCase();
				stmt.executeUpdate(sql);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
