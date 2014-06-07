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
 * Servlet implementation class deleteUserServlet
 */
@WebServlet("/deleteUserServlet")
public class deleteUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	GraphDatabaseService graph;
	private Connection sqlConnection;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public deleteUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
		System.out.println("init called from GetMessage");	
		graph= (GraphDatabaseService) this.getServletContext().getAttribute("db");
		sqlConnection=(Connection) this.getServletContext().getAttribute("sqlConnection");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBUtilities dbUtilities=new DBUtilities(graph,sqlConnection);
		System.out.println("in do get method of deleteUserServlet");
		PrintWriter pw=null;
		String url=request.getParameter("url");
		String uId=request.getParameter("gid");
		String name=request.getParameter("name");
		String messgae=request.getParameter("message");
		String chatType=request.getParameter("chatType");
//		dbUtilities.deleteUser(uId);	//need to be implemented with parameters
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
