package com.ibp.model;

import java.sql.Connection;

import org.neo4j.graphdb.GraphDatabaseService;
//import org.neo4j.graphdb.Node;

@SuppressWarnings("unused")
class UrlNode
{
	private long connectedToCoreNodeId; //name of the core node to which it is connected
	private  String value;     //url string and connected to
	private long connectedToMessageNodeUrlId;
	private int onlineUsers;
	
	public UrlNode() {
	}

	public UrlNode(String value)
	{
		setValue(value);
		//		setConnectedToCoreNodeId(url);
	}   
	
	public int getonlineUsers(){
		return this.getonlineUsers();
	}
	
	public void setonlineUsers(int onlineUsers){
		this.onlineUsers=onlineUsers;
	}

	public long getConnectedToCoreNodeId() {
		return connectedToCoreNodeId;
	}
	public void setConnectedToCoreNodeId(String url,GraphDatabaseService graph,Connection sqlConnection) {
		//to be written by meher or call mehar method
		System.out.println("************************************************");
		DBManager db=new DBManager(graph,sqlConnection);
		CoreNode coreNode=new CoreNode();
		//here call meher method to get value for core node
		DBUtilities dbUtilities=new DBUtilities(graph, sqlConnection);
		String coreId=dbUtilities.getCoreNodeID(url);
		System.out.println("core from meher:"+coreId);
		if(coreId==null || coreId.isEmpty()){
			//handle it later..
			System.out.println("Just for safe side..... null core node value");
		}
		//coreNode.setValue("three");
		//coreNode.setValue(coreId);

		/*Node coreNodeInDB=db.getCoreNode(coreNode);
		if(coreNodeInDB==null){
			System.out.println("new core node");
			MessageNodeCore msg=new MessageNodeCore();
			msg.setValue("");
			Node msgNodeInDB=db.createMessageNodeCore(msg);
			long msgNodeId=msgNodeInDB.getId();
			coreNode.setConnectedMessageNodeCoreId(msgNodeId);
			Node coreNodeinDB=db.createCoreNode(coreNode);
			System.out.println("core node created: "+coreNodeinDB);
			this.connectedToCoreNodeId=coreNodeinDB.getId(); //for testing
			System.out.println("core node id :"+coreNodeinDB.getId());
			
		}else{ */
			//this.connectedToCoreNodeId=coreNodeInDB.getId();
		//}

		this.connectedToCoreNodeId=Long.parseLong(coreId);
		//			this.connectedTo="";
	}
	public void setConnectedToCoreNodeIdValue(long connectedToCoreNodeId) {
		this.connectedToCoreNodeId=connectedToCoreNodeId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public long getConnectedToMessageNodeUrlId() {
		return connectedToMessageNodeUrlId;
	}

	public void setConnectedToMessageNodeUrlId(long connectedToMessageNodeUrlId) {
		this.connectedToMessageNodeUrlId = connectedToMessageNodeUrlId;
	}
}
