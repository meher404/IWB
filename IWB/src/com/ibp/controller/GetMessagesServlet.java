package com.ibp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neo4j.graphdb.GraphDatabaseService;

import com.ibp.model.DBUtilities;
import com.ibp.model.UserDetailsSql;

/**
 * Servlet implementation class getUpdates
 */
@WebServlet("/GetMessagesServlet")
public class GetMessagesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GraphDatabaseService graph;
	private Connection sqlConnection;
	private ConcurrentHashMap<String, ArrayList<String>> userInterests;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetMessagesServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() throws ServletException {
		System.out.println("init called from GetMessage");	
		graph= (GraphDatabaseService) this.getServletContext().getAttribute("db");
		sqlConnection=(Connection) this.getServletContext().getAttribute("sqlDBConnection");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBUtilities dbUtilities= new DBUtilities(graph,sqlConnection);
		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw=null;
		System.out.println("-> In do get of getUpdates function");
		String url=request.getParameter("url");
		String uId=request.getParameter("uid");
		String uname=request.getParameter("uname");
		String msg=request.getParameter("mes");
		String chatType=request.getParameter("chatType");
		System.out.println("url:"+url+";uid:"+uId+"uname:"+uname+";msg:"+msg+";chatyoe:"+chatType);
		/*String url="http://mechatronics.poly.edu/smart/";
		String uId="3453";
		String uname="prathyusha";
		String msg="";
		String chatType="similar";*/
		
	//	 userInterests =(ConcurrentHashMap<String, ArrayList<String>>) this.getServletContext().getAttribute("db");
	//	 System.out.println(userInterests);
/*		String category=dbUtilities.getrelatedCoreNodeOfUrl(url);		
		ArrayList<String> categories=new ArrayList<String>();
		if(userInterests.containsKey(uId)){
			categories=userInterests.get(uId);
			if(categories.contains(category)){
				
			}else{
				categories.add(category);
			}
		}else{
			
		}
*/		
		
		UserDetailsSql userdetails=new UserDetailsSql(sqlConnection);
		if(chatType.equalsIgnoreCase("similar"))
		{
			try
			{
				
			while(true)
			{
				System.out.println("REQUEST FROM:"+uname+"url:"+url);
				System.out.println("CHAT TYPE&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+chatType);
				
					double randomNumber=Math.random()*3000;
					pw=response.getWriter();
					String messages=dbUtilities.getMessageFromCoreNode(uId, url);	//need to be implemented with parameters

					System.out.println("messges=>"+messages);
		
					if(messages.compareTo("")==0)
						messages="empty";
					else
						messages=url+"[(:-,;,-:)]"+chatType+"[(:-,;,-:)]"+messages;
					System.out.println("messages:"+messages);
					pw.print("data: "+messages+"\n\n");
					response.flushBuffer();
					Thread.sleep((long)randomNumber);
			}
			}
			catch(Exception e)
			{
				System.out.println("in getUpdates Servlet of similar url catch");
			    dbUtilities.deActivateUser(uId, url,1);
				String category=dbUtilities.getrelatedCoreNodeOfUrl(url);
			    //userdetails.updateCategoryCount(uId, category);
			    userdetails.updateURLCount(uId, url, category);
				e.printStackTrace();
				pw.close();
			}
		}
		else
		{
			try
			{
			while(true)
			{
				System.out.println("REQUEST FROM:"+uname);
				System.out.println("CHAT TYPE##################################################"+chatType);
				
					double randomNumber=Math.random()*3000;
					pw=response.getWriter();
					String messages=dbUtilities.getMessageFromURLNode( uId, url);	//need to be implemented with parameters
					System.out.println("-------------------------------------------------------");
					System.out.println("Message in get message servlet is "+messages+",len:"+messages.length());
					System.out.println("-------------------------------------------------------");
					//String jsonMessage="data: ";
					/*if(messages.compareToIgnoreCase("")!=0)
					{
						String msgString[]=messages.split(",");

						for (int i = 0; i < msgString.length-1; i++) {
							String elements[]=msgString[i].split("::");
							if(elements.length>1)
								jsonMessage+="{\\\"message\\\":\\\""+elements[0]+"\\\",\\\"time\\\":\\\""+elements[1]+"\\\",\\\"userId\\\":\\\""+elements[2]+"\\\",\\\"userName\\\": \\\""+elements[3]+"\\\",\\\"url\\\": \\\""+url+"\\\" }";    
							if(i<msgString.length-2){
								jsonMessage+=",";

							}
							pw.print(jsonMessage+"\n\n");
						}

					}*/
					/*else
					{
						jsonMessage+="{ \"message\" : "+"empty"+", \"time\" : "+"empty"+", \"userId\" : "+"empty"+", \"userName\" : "+"empty"+", \"url\" : "+url+" } "; 
					}*/
					if(messages.compareTo("")==0)
						messages="empty";
					else
						messages=url+"[(:-,;,-:)]"+chatType+"[(:-,;,-:)]"+messages;

					System.out.println("messages:"+messages);



					pw.print("data: "+messages+"\n\n");
					response.flushBuffer();
					Thread.sleep((long)randomNumber);
				
			}
			}
			catch(Exception e)
			{
				
				System.out.println("in getUpdates servlet of exact url catch");
				 dbUtilities.deActivateUser(uId, url,2);
				 
				 String category=dbUtilities.getrelatedCoreNodeOfUrl(url);
				 //userdetails.updateCategoryCount(uId, category);
				 userdetails.updateURLCount(uId, url, category);
				 e.printStackTrace();
				pw.close();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
