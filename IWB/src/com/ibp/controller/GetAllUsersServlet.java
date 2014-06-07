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

import com.ibp.model.categoryWiseStatistics;

/**
 * Servlet implementation class GetAllUsersServlet
 */
@WebServlet("/GetAllUsersServlet")
public class GetAllUsersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAllUsersServlet() {
        super();
        // TODO Auto-generated constructor stub
       
    
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//Connection con = (Connection) this.getServletContext().getAttribute("sqlDBConnection");
		PrintWriter pw=response.getWriter();
		GraphDatabaseService db = (GraphDatabaseService) this.getServletContext().getAttribute("db");
		categoryWiseStatistics cws=new categoryWiseStatistics(db);
		String users=cws.getAllUsers();
		System.out.println("all online users in servlet is:"+users);
		pw.write(users);	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
