package com.ibp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neo4j.graphdb.GraphDatabaseService;

import com.ibp.model.DBUtilities;

/**
 * Servlet implementation class AddMessageServlet
 */
@WebServlet("/AddMessageServlet")
public class AddMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	GraphDatabaseService graph;
	Connection sqlConnection;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddMessageServlet() {
		super();
	}

	public void init() throws ServletException {
		System.out.println("init called from AddMessage");	
		graph= (GraphDatabaseService) this.getServletContext().getAttribute("db");
		sqlConnection=(Connection) this.getServletContext().getAttribute("sqlDBConnection");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBUtilities dbUtilities= new DBUtilities(graph,sqlConnection);
		PrintWriter pw=response.getWriter();
		System.out.println("-> In do get of Addmessage function");
		String url=request.getParameter("url");
		String uId=request.getParameter("uid");
	//	String uname=request.getParameter("uname");
		String message=request.getParameter("mes");
		String chatType=request.getParameter("chatType");
		
		System.out.println("url->"+url);
		System.out.println("uid->"+uId);
	//	System.out.println("uname->"+uname);
		System.out.println("message->"+message);
		System.out.println("Chattype->"+chatType);
		
		System.out.println("addmessage called^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		
		
		if(chatType.equalsIgnoreCase("similar"))
		{
			dbUtilities.addMessagesToCoreNode( uId, url, message);;	//need to be implemented with parameters
		}
		else
		{
			dbUtilities.addMessageToURLNode( uId, url, message);;	//need to be implemented with parameters
		}
		pw.write("rcvd");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
