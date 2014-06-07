package com.ibp.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.util.ArrayList;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class CoreNodeData {
	String coreValue;
	CoreNode coreNode;
	ArrayList<UrlNode> urls;
	DBManager db;
	public CoreNodeData(GraphDatabaseService graph,Connection sqlConnection) {
		db=new DBManager(graph,sqlConnection);
		urls=new ArrayList<>();
		coreValue="";
	}
	public static void main(String[] args) {
		GraphDatabaseService graph=null;
		graph=new GraphDatabaseFactory().newEmbeddedDatabase("C:\\Users\\Rahul Konda\\Desktop\\db");
		System.out.println("db connected/n Data loading....");
		CoreNodeData c=new CoreNodeData(graph, null);
		
		System.out.println("after");
		c.readFile();
	}
	public void readFile(){

		try {
	//		Scanner in=new Scanner(new FileReader("data.txt"));
			BufferedReader in = new BufferedReader(new FileReader("data.txt"));
			System.out.println("confi:"+in);
			String line="";
			boolean newCore=true;

			while((line=in.readLine())!=null){
				if(line.length()<1){
					newCore=true;
					
					System.out.println("line read:->"+line);
					
					
					if(coreValue.length()>0 && urls.size()>0){
						db.insert(coreNode, urls);
						System.out.println("core:"+coreValue);
						System.out.println("urls:"+urls.toString());
					}
					
					coreValue="";
					urls.clear();
				}else{
					
					System.out.println("line read:->"+line);
					if(newCore){
						newCore=false;
						coreValue=line;
				//		System.out.println(line);
						MessageNodeCore mnc= new MessageNodeCore("");
						long connectedToMessageNodeUrlId=db.createMessageNodeCore(mnc).getId();
						coreNode=new CoreNode(coreValue, connectedToMessageNodeUrlId);
					}
					else
					{
						//System.out.println("\n line: \n"+line);
						if(line.split(":::").length>1){
							String url=line.split(":::")[0];
							UrlNode urlNode=new UrlNode(url);
							urls.add(urlNode);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
