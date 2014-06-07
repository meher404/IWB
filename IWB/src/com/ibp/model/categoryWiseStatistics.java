
package com.ibp.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.tooling.GlobalGraphOperations;


class coreNodeFreq implements Comparable<coreNodeFreq>
{
	String name;
	int value;
	@Override
	public int compareTo(coreNodeFreq o) {
		if(this.value>o.value)
		{
			return 1;
		}
		else if(this.value<o.value)
		{
			return -1;
		}
		return 0;
	}
}


class Categories{
	 static ArrayList<String> ALL_CAT = new ArrayList<String>();
		static{
			ALL_CAT.add(0,"Arts");
			ALL_CAT.add(1,"Business");
			ALL_CAT.add(2,"Computers");
			ALL_CAT.add(3,"Games");
			ALL_CAT.add(4,"Health");
			ALL_CAT.add(5,"Home");
			ALL_CAT.add(6,"News");
			ALL_CAT.add(7,"Recreation");
			ALL_CAT.add(8,"Reference");
			ALL_CAT.add(9,"Regional");
			ALL_CAT.add(10,"Science");
			ALL_CAT.add(11,"Shopping");
			ALL_CAT.add(12,"Society");
			ALL_CAT.add(13,"Sports");
		}
}














public class categoryWiseStatistics
{
	Connection conn;
	Statement stmt;
	ResultSet rs;
	GraphDatabaseService graph;
	
	
	public String getAllUsers()
	{
		
		String query="";
		//DBUtilities dbu=new DBUtilities();
		
		int count=0, total = 0;
		try(Transaction tx=graph.beginTx())
    	{
			Label label = DynamicLabel.label("LabelUserNode");
			ResourceIterable<Node> iter = GlobalGraphOperations.at(graph).getAllNodesWithLabel(label);
			
			for(Node node : iter)
			{
				total++;
				String presentNode=(String) node.getProperty("status");
				if(presentNode.equalsIgnoreCase("true"))
				{
					count++;
				}
			}
			
    	}
		return total + "::" + count;
	}
	
	
	public categoryWiseStatistics(GraphDatabaseService graph)
	{
		this.graph = graph;
	}
	
	
	
	public categoryWiseStatistics(Connection conn)
	{
		System.out.println("connectin in analayitcs "+conn);
		this.conn=conn;
	}
	
	public String topFiveURLsInCategory(String category)
	{
		try
		{
			String top5results="";
			Categories c=new Categories();
			HashMap<String, Integer> url_count = new HashMap<String, Integer>(); 
			PriorityQueue<coreNodeFreq> pq=new PriorityQueue<>(14, Collections.reverseOrder());
			
			stmt = conn.createStatement();
			String sql="select * from userinterests where interest like'"+category+"%';";
			ResultSet rs=stmt.executeQuery(sql);
			int count=0;
			while(rs.next())
			{
				if(url_count.containsKey(rs.getString(4)))
				{
					count = url_count.get(rs.getString(4));
					url_count.put(rs.getString(4), rs.getInt(3)+count);
				}
				else
				{
					url_count.put(rs.getString(4), rs.getInt(3));
				}
			}	
			ArrayList<String> urls = new ArrayList<String>(url_count.keySet());
			for(String url: urls)
			{
				coreNodeFreq corenode=new coreNodeFreq();
				corenode.name=url;
				corenode.value=url_count.get(url);
				pq.add(corenode);
			}
			
			
			for(int i=0;i<5;i++)
			{
				coreNodeFreq cn=pq.poll();
				if(cn == null)
					break;
				top5results+=cn.name+"::"+cn.value+"::::";
			}
			System.out.println(">>>>>>>>>>Returning top 5 urls in " + category + ": " + top5results);
			return top5results;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String topFiveCategories()
	{
		System.out.println("!!!!!!!!!!!!@@@@@@@@@@@@@@@@@@@@In top categories!!!!!!!!!!!!!!!!");
		try
		{
			String top5results="";
			Categories c=new Categories();
			PriorityQueue<coreNodeFreq> pq=new PriorityQueue<>(14, Collections.reverseOrder());
			for(int i=0;i<c.ALL_CAT.size();i++)
			{
				stmt = conn.createStatement();
				String sql="select * from userinterests where interest like'"+c.ALL_CAT.get(i)+"%';";
				ResultSet rs1=stmt.executeQuery(sql);
				int count=0;
				while(rs1.next())
				{
					count=count+Integer.parseInt(rs1.getString(3));
				}	
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>" + c.ALL_CAT.get(i) + " : " + count);
				//hm.put(c.ALL_CAT.get(i), count);
				coreNodeFreq corenode=new coreNodeFreq();
				corenode.name=c.ALL_CAT.get(i);
				corenode.value=count;
				pq.add(corenode);
			}
			
			for(int i=0;i<5;i++)
			{
				coreNodeFreq cn=pq.poll();
				top5results+=cn.name+"::"+cn.value+"::::";
			}
			System.out.println(">>>>>>>>>>Returning top 5 categories: " + top5results);
			return top5results;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return null;
		
	}
}