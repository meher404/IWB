package com.ibp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibp.model.categoryWiseStatistics;

/**
 * Servlet implementation class GetTopUrlsServlet
 */
@WebServlet("/GetTopUrlsServlet")
public class GetTopUrlsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetTopUrlsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String category = request.getParameter("category");
		Connection con = (Connection) this.getServletContext().getAttribute("sqlDBConnection");
		System.out.println("con in gettopurlservlet"+con);
		categoryWiseStatistics cws = new categoryWiseStatistics(con);
		PrintWriter out = response.getWriter();
		out.write(cws.topFiveURLsInCategory(category));
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
