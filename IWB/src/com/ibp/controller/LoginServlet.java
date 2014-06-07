package com.ibp.controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neo4j.graphdb.GraphDatabaseService;

import com.ibp.model.UserDetailsSql;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	GraphDatabaseService graph;
	Connection sqlConnection;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	public void init() throws ServletException {
		System.out.println("init called from AddMessage");	
		graph= (GraphDatabaseService) this.getServletContext().getAttribute("db");

		sqlConnection=(Connection) this.getServletContext().getAttribute("sqlDBConnection");
		System.out.println("sql connection is "+sqlConnection);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String uId=request.getParameter("uId");
		String emailID=request.getParameter("email");
		String uname=request.getParameter("uname");
		String ImageId=request.getParameter("img");
		String dob=request.getParameter("dob");
		String gender=request.getParameter("gender");
		String placeLived=request.getParameter("placeLived");

		UserDetailsSql userDetails=new UserDetailsSql(sqlConnection);
		//	boolean userExists=userDetails.isUserExists(uId);
		try{
			
			userDetails.insertUserSql(uId, uname, gender, dob, ImageId,placeLived);
		}catch(Exception e){
			System.out.println("user already exists");
		}
		if(userDetails.isUserSpammed(uId)){
			response.sendRedirect("loginDenied.html?uId="+uId+"&uname="+uname);
		}else{
			response.sendRedirect("loginSucessfulPage.html?uId="+uId+"&uname="+uname);
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
