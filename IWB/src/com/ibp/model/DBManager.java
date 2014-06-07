package com.ibp.model;

import java.sql.Connection;
import java.util.ArrayList;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;

public class DBManager {
	static GraphDatabaseService graph;
	ExecutionEngine engine;
	private Connection sqlConnection;

	public static void main(String[] args) {
		graph=new GraphDatabaseFactory().newEmbeddedDatabase("C:\\Users\\Rahul Konda\\Desktop\\db");
		/*
		DBManager nf= new DBManager(graph);
		
		CoreNode cn=new  CoreNode("I`m corenode14");
		
		MessageNodeUrl mnc= new MessageNodeUrl("hair8");
		Node connectedMessageNodeCore=nf.createMessageNodeUrl(mnc);
		System.out.println("node:"+connectedMessageNodeCore);
				cn.setConnectedMessageNodeCoreId(connectedMessageNodeCore.getId());
		//	System.out.println("coreNode:"+cn.getValue());
		
		
		UrlNode un=new UrlNode();
		un.setValue("i`m URL14");
		un.setConnectedToCoreNodeId(cn.getValue(),graph);
		un.setConnectedToMessageNodeUrlId(connectedMessageNodeCore.getId());
		System.out.println("un is connectedto:"+un.getConnectedToCoreNodeId());
		Node urlNodeInDB=nf.createUrlNode(un);
		nf.insert(cn,un);
		
		System.out.println("CoreNode:"+nf.getCoreNode(cn));
		System.out.println("Url:"+nf.getUrlNode(un));
		System.out.println("Relation hasurl:"+nf.isLinkExists(nf.getCoreNode(cn), nf.getUrlNode(un), RelTypes.hasUrl));
		
		UserNode userNode= new UserNode("112", "abcd", urlNodeInDB.getId());
		userNode.setLastSeenMessageTime((long) 0);
		Node userNodeInDB= nf.createUserNode(userNode);
		System.out.println("userNode:"+userNodeInDB);*/
	}

	public static enum RelTypes implements RelationshipType
	{
		//	toCoreNode,
		toUrlNode,
		hasUrl,
		hasCoreChat,
		hasUrlChat
	}
	
	/*public GraphDatabaseService getgraphObject()
	{
		return graph;
	}*/
	

	public DBManager(GraphDatabaseService graph, Connection sqlConnection ){
		DBManager.graph=graph;
		this.sqlConnection=sqlConnection;
		this.engine=new  ExecutionEngine(graph);
		
		/*try ( Transaction tx = graph.beginTx() )
		{
			IndexDefinition indexDefinition;
		    Schema schema = graph.schema();
		    indexDefinition = schema.indexFor( DynamicLabel.label( "LabelUserNode" ) )
		            .on( "username" )
		            .create();
	//	    schema.awaitIndexOnline( indexDefinition, 10, TimeUnit.SECONDS );
		    tx.success();
		}*/
	}


	public boolean isURLExists(UrlNode url)          //should be verified
	{
		//	System.out.println("inside search url");
		try(Transaction tx=graph.beginTx())
		{
			Label label = DynamicLabel.label("LabelUrlNode");
			ResourceIterable<Node> ncol= graph.findNodesByLabelAndProperty(label,"value",url.getValue());
			ResourceIterator<Node> niterator=ncol.iterator();
			if(niterator.hasNext())
			{
				return true;
			}	
			else{
				return false;
			}
		}
	}

	public boolean isUserNodeExists(String userId)          //should be verified
	{
		
		try(Transaction tx=graph.beginTx())
		{
			Label label = DynamicLabel.label("LabelUserNode");
			ResourceIterable<Node> ncol= graph.findNodesByLabelAndProperty(label,"UserID",userId);
			ResourceIterator<Node> niterator=ncol.iterator();
			if(niterator.hasNext())
			{
				return true;
			}	
			else
			{
				return false;
			}
		}
	}


	public boolean isCoreNodeExists(CoreNode coreNode)          //should be verified
	{

		
		try(Transaction tx=graph.beginTx())
		{
			Label label = DynamicLabel.label("LabelCoreNode");
			ResourceIterable<Node> ncol= graph.findNodesByLabelAndProperty(label,"value",coreNode.getValue());
			ResourceIterator<Node> niterator=ncol.iterator();
			if(niterator.hasNext())
			{
				return true;
			}	
			else
			{
				return false;
			}
		}
	}

	public Node createCoreNode(CoreNode cn)
	{
		Node newNode=null;
		try(Transaction tx=graph.beginTx())
		{
			Label label = DynamicLabel.label( "LabelCoreNode" );
			newNode=graph.createNode(label);
			newNode.setProperty("totalUsers", 0);
			newNode.setProperty("onlineUsers",0);
			newNode.setProperty("value",cn.getValue());
			newNode.setProperty("connectedMessageNodeCore", cn.getConnectedMessageNodeCoreId());
			System.out.println("created core node with id"+newNode);
			tx.success();
		}
		return newNode;
	}

	public Node createUrlNode(UrlNode urlNode){
		Node newNode=null;
		try(Transaction tx=graph.beginTx()){
			Label label = DynamicLabel.label( "LabelUrlNode" );
			newNode=graph.createNode(label);
			newNode.setProperty("value",urlNode.getValue());
			newNode.setProperty("connectedToMessageNodeUrl",urlNode.getConnectedToMessageNodeUrlId());
			newNode.setProperty("connectedToCoreNode", urlNode.getConnectedToCoreNodeId());
			System.out.println("created url node with id"+newNode);
			tx.success();
		}
		return newNode;
	}

	public Node createMessageNodeCore(MessageNodeCore mnc){
		Node newNode=null;
		try(Transaction tx=graph.beginTx()){
			Label label = DynamicLabel.label( "LabelMessageCoreNode" );
			newNode=graph.createNode(label);
			newNode.setProperty("value",mnc.getValue());
			//		newNode.setProperty("connectedCoreNodeValue",mnc.getConnectedCoreNodeValue());

			System.out.println("created messagenode core with id"+newNode);
			tx.success();
		}
		return newNode;
	}

	public Node createMessageNodeUrl(MessageNodeUrl mnu){
		Node newNode=null;
		try(Transaction tx=graph.beginTx()){
			Label label = DynamicLabel.label( "LabelMessageNodeUrl" );
			newNode=graph.createNode(label);
			newNode.setProperty("value",mnu.getValue());
			//			newNode.setProperty("connectedUrlNodeValue",mnu.getConnectedUrlNodeValue());

			System.out.println("created message node url with id"+newNode);
			tx.success();
		}
		return newNode;
	}

	public Node createUserNode(UserNode un){
		Node newNode=null;
		try(Transaction tx=graph.beginTx()){
			Label label = DynamicLabel.label( "LabelUserNode" );
			newNode=graph.createNode(label);
			//			newNode.setProperty("label",category);
		//	newNode.setProperty("name",un.getName());
			newNode.setProperty("userID",un.getUserID());
			newNode.setProperty("lastSeenMessageTime",un.getLastSeenMessageTime());
			newNode.setProperty("hasUrl", un.getHasUrl());
			newNode.setProperty("connectedToUrl", un.getConnectedToUrlId());
			newNode.setProperty("status",un.getStatus());
		/*	newNode.setProperty("dob", un.getDob());	
			newNode.setProperty("imageUrl", un.getImageUrl());
			newNode.setProperty("gender", un.getGender());*/
			
			System.out.println("created user node with id"+newNode);
			tx.success();
		}
		/*try(Transaction tx1=graph.beginTx()){
			Node urlNode=graph.getNodeById(un.getConnectedToUrlId());
			long coreNodeId=(long)urlNode.getProperty("connectedToCoreNode");
			Node coreNode=graph.getNodeById(coreNodeId);
			int users=(int) coreNode.getProperty("onlineUsers");
			coreNode.setProperty("onlineUsers",users+1);
			//onlineUsers
		}*/
		return newNode;
	}

	//to bulid mehers database
	public void insert(CoreNode coreNode, UrlNode url){
		Node coreNodeInDB=getCoreNode(coreNode);
		Node urlNodeInDB=getUrlNode(url);
		if(coreNodeInDB==null){
			MessageNodeCore coreChat=new MessageNodeCore("");
			Node messageNodeCore=createMessageNodeCore(coreChat);
			System.out.println("mnode:"+messageNodeCore);
			coreNode.setConnectedMessageNodeCoreId(messageNodeCore.getId());
			coreNodeInDB=createCoreNode(coreNode);
			createLink(coreNodeInDB, messageNodeCore, RelTypes.hasCoreChat);
		}
		if(urlNodeInDB==null){
			MessageNodeUrl urlChat=new MessageNodeUrl("");
			Node messageNodeUrl=createMessageNodeUrl(urlChat);
			url.setConnectedToMessageNodeUrlId(messageNodeUrl.getId());
			url.setConnectedToCoreNodeIdValue(coreNodeInDB.getId());
			urlNodeInDB=createUrlNode(url);
			createLink(urlNodeInDB, messageNodeUrl, RelTypes.hasUrlChat);
		}
		if(!isLinkExists(coreNodeInDB,urlNodeInDB,RelTypes.hasUrl)){
			createLink(coreNodeInDB,urlNodeInDB,RelTypes.hasUrl);
		}
	}

	//to bulid mehers database
	public void insert(CoreNode coreNode, ArrayList< UrlNode> urls){
		
		
		for(int i=0;i<urls.size();i++)
		{
			UrlNode nourl=urls.get(i);
			System.out.println("url in arrraylist->"+nourl.getValue()+"*");
		}
		
		Node coreNodeInDB=getCoreNode(coreNode);
		if(coreNodeInDB==null){
			MessageNodeCore coreChat=new MessageNodeCore("");
			Node messageNodeCore=createMessageNodeCore(coreChat);
			coreNode.setConnectedMessageNodeCoreId(messageNodeCore.getId());
			coreNodeInDB=createCoreNode(coreNode);
			createLink(coreNodeInDB, messageNodeCore, RelTypes.hasCoreChat);
		}
		for (UrlNode url : urls) {
			Node urlNodeInDB=getUrlNode(url);
			if(urlNodeInDB==null){
				MessageNodeUrl urlChat=new MessageNodeUrl("");
				Node messageNodeUrl=createMessageNodeUrl(urlChat);
				url.setConnectedToMessageNodeUrlId(messageNodeUrl.getId());
				url.setConnectedToCoreNodeIdValue(coreNodeInDB.getId());
				urlNodeInDB=createUrlNode(url);
				createLink(urlNodeInDB, messageNodeUrl, RelTypes.hasUrlChat);
			}
			if(!isLinkExists(coreNodeInDB,urlNodeInDB,RelTypes.hasUrl)){
				
				createLink(coreNodeInDB,urlNodeInDB,RelTypes.hasUrl);
			}
		}
	}

	public void insertUser(UserNode userNode, String url){
		UrlNode un=new UrlNode();
		un.setValue(url);
		Node urlNodeInDB=getUrlNode(un);
		if(urlNodeInDB==null){
			MessageNodeUrl urlChat=new MessageNodeUrl("");
			Node messageNodeUrl=createMessageNodeUrl(urlChat);
			un.setConnectedToCoreNodeId(url,graph,sqlConnection);//internally calls mehar method
			un.setConnectedToMessageNodeUrlId(messageNodeUrl.getId());
			urlNodeInDB=createUrlNode(un);
			createLink(urlNodeInDB, messageNodeUrl, RelTypes.hasUrlChat);
			Node coreNodeInDB=graph.getNodeById(un.getConnectedToCoreNodeId());
			createLink(coreNodeInDB, urlNodeInDB, RelTypes.hasUrl);
		}
		Node userNodeInDB=getUserNode(userNode);
		if(userNodeInDB==null){
			userNode.setConnectedToUrlId(urlNodeInDB.getId());
			userNode.setHasUrl(url);
			userNodeInDB=createUserNode(userNode);
		}
		createLink(urlNodeInDB, userNodeInDB, RelTypes.toUrlNode);
	}

	public void createLink(Node node1, Node node2, RelationshipType type) {
		try(Transaction tx=graph.beginTx()){
			System.out.println("node1:"+node1);
			System.out.println("node2:"+node2);

			//		RelationshipType type=RelTypes.hasUrl;
			Relationship r = node1.createRelationshipTo(node2, type);
			tx.success();
			System.out.println("link created");
		}
	}

	public Node getUrlNode(UrlNode url) {
			System.out.println("inside geturlNode");
		try(Transaction tx=graph.beginTx()){
			Label label = DynamicLabel.label("LabelUrlNode");
			ResourceIterable<Node> ncol= graph.findNodesByLabelAndProperty(label,"value",url.getValue());
			ResourceIterator<Node> nodeIterator=ncol.iterator();
			if(nodeIterator.hasNext())
				return nodeIterator.next();
			tx.success();
		}
		return null;
	}

	public Node getUserNode(UserNode userNode) {
		//		System.out.println("inside geturlNode");
		try(Transaction tx=graph.beginTx()){
			Label label = DynamicLabel.label("LabelUserNode");
			ResourceIterable<Node> ncol= graph.findNodesByLabelAndProperty(label,"userID",userNode.getUserID());
			ResourceIterator<Node> nodeIterator=ncol.iterator();
			while(nodeIterator.hasNext()){
				Node userNodeInDB;
				if((userNodeInDB=nodeIterator.next()).getProperty("hasUrl").equals(userNode.getHasUrl())){
					return userNodeInDB;
				}
			}
			tx.success();
		}
		return null;
	}


	public Node getCoreNode(CoreNode coreNode) {
		//	System.out.println("inside getcoreNode");
		try(Transaction tx=graph.beginTx()){
			Label label = DynamicLabel.label("LabelCoreNode");
			ResourceIterable<Node> ncol= graph.findNodesByLabelAndProperty(label,"value",coreNode.getValue());
			ResourceIterator<Node> nodeIterator=ncol.iterator();
			if(nodeIterator.hasNext())
				return nodeIterator.next();
			tx.success();
		}
		return null;
	}


	public boolean isLinkExists(Node node1, Node node2,RelationshipType type) {
		try(Transaction tx=graph.beginTx()){

			//		RelationshipType type=RelTypes.hasUrl;
			for(Relationship r :node1.getRelationships(type))
			{
				if (r.getOtherNode(node1).equals(node2))
				{
					tx.success();
					System.out.println("relation exists");
					return true;
				}
			}
			tx.success();
		}
		
		return false;
	}

}
