package com.ibp.controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neo4j.graphdb.GraphDatabaseService;

import com.ibp.model.UserDetailsSql;

/**
 * Servlet implementation class SpamMessagesServlet
 */
@WebServlet("/SpamMessagesServlet")
public class SpamMessagesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	GraphDatabaseService graph;
	Connection sqlConnection;
   
    public SpamMessagesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init() throws ServletException {
		System.out.println("init called from SpamMessages");	
		graph= (GraphDatabaseService) this.getServletContext().getAttribute("db");

		sqlConnection=(Connection) this.getServletContext().getAttribute("sqlDBConnection");
		System.out.println("sql connection is "+sqlConnection);
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		UserDetailsSql uds = new UserDetailsSql(sqlConnection);
		
		String reportedOn = request.getParameter("reportedId");
		String reportedBy = request.getParameter("reporterId");
		String message = request.getParameter("message");
		String time = request.getParameter("time");
		
		uds.addSpamMessages(reportedBy, reportedOn, message, time);
		uds.UserSpamMessagesUpdate(reportedOn);
	
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
