package com.ibp.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.tartarus.snowball.ext.PorterStemmer;
import com.ibp.model.DBManager.RelTypes;


public class DBUtilities {
	static GraphDatabaseService graph;
	Connection sqlConnection;
	DBManager db;

	public static void main(String[] args) {
		//		graph=new GraphDatabaseFactory().newEmbeddedDatabase("C:\\Users\\Nikhitha\\Desktop\\GraphDB\\DB-graph\\neo4j-community-2.1.0-M01");
		DBUtilities util=new DBUtilities();
		//		util.deleteNode("1");
		//System.out.println("From Core\n"+util.getMessageFromCoreNode("Nikhitha", "258", "www.rahul.com"));
		//util.addMessagesToCoreNode("Rahul", "25", "www.rahul.com", "message2");
		//util.addMessagesToCoreNode("Rahul", "25", "www.rahul.com", "message1");
		//util.addMessagesToCoreNode("Rahul", "25", "www.rahul.com", "message3");
		//System.out.println("From Core\n"+util.getMessageFromCoreNode("Rahul", "25", "www.rahul.com"));
		//util.addMessageToURLNode("nikhitha", "245", "www.nikhithagoodgirl24.com", "REDDYNEW");
		//System.out.println("--"+util.getMessageFromURLNode("nikhitha", "245", "www.nikhithagoodgirl24.com"));
		/*System.out.print("Messages:");
		System.out.println("--"+util.getMessageFromURLNode("Rahul", "1", "www.test19.com"));
		System.out.println("msg2=======================================");
		util.addMessageToURLNode("Rahul", "1", "www.test19.com", "msitmsg2");
		System.out.print("Messages:");
		System.out.println("--"+util.getMessageFromURLNode("Rahul", "1", "www.test11.com"));*/
		//	util.deleteUser("245");
		System.out.println("--"+util.getMessageFromURLNode( "111", "www.abc.com"));
	}

	public DBUtilities(GraphDatabaseService graph,Connection con){
		DBUtilities.graph=graph;
		sqlConnection=con;
		db= new DBManager(graph,sqlConnection);
	}

	public DBUtilities() {
		graph=new GraphDatabaseFactory().newEmbeddedDatabase("C:\\Users\\shalini\\Contacts\\Desktop\\testingSimilar\\Neo4j_db");
		db= new DBManager(graph,sqlConnection);
	}
	
	public GraphDatabaseService getgraphObject()
	{
		return graph;
	}

	public int getOnlineUsersForCoreNode(Node coreNode)
	{
		int users;
		try ( Transaction tx = graph.beginTx() ){
			users=(int) coreNode.getProperty("onlineUsers");
			tx.success();
		}




		//	System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
		System.out.println("online users for core node: "+users);
		return users;
	}

	public int getTotalUsersForCoreNode(Node coreNode){
		int users;
		try ( Transaction tx = graph.beginTx() ){
			users=(int) coreNode.getProperty("totalUsers");
			tx.success();
		}
		//	System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
		System.out.println("total users for core node: "+users);
		return users;
	}

	public void deActivateUser(String uId,String url,int flag)// should be verified by konda rahul
	{
		
		System.out.println("<<<<<<<<<<<<<<<<<<inside deactivate :: flag :::::" + flag + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		UserNode userNode=new UserNode();
		userNode.setUserID(uId);
		userNode.setHasUrl(url);
		Node userNodeInDB=db.getUserNode(userNode); 
		System.out.println();
		try(Transaction tx1=graph.beginTx()){
			//System.out.println("in  deactivate:"+userNodeInDB.getProperty("value"));
			if(userNodeInDB != null){

				userNodeInDB.setProperty("status","false");
				System.out.println("status in deactivate:"+userNodeInDB.getProperty("status"));
			}
			System.out.println("user is:"+userNode.getName());
			System.out.println("connectedToUrl usernodindb:"+userNodeInDB.getProperty("connectedToUrl"));
			System.out.println("usr id:"+userNodeInDB.getProperty("userID"));
			//			Node urlNode=graph.getNodeById(userNode.getConnectedToUrlId());
			Node urlNode=graph.getNodeById((long) userNodeInDB.getProperty("connectedToUrl"));

			long coreNodeId=(long)urlNode.getProperty("connectedToCoreNode");
			Node coreNodeInDB=graph.getNodeById(coreNodeId);
			int users=(int) coreNodeInDB.getProperty("onlineUsers");
			if(flag==1)
			{
				System.out.println("pre>"+coreNodeInDB.getProperty("onlineUsers"));
				//if(users>=1)
					coreNodeInDB.setProperty("onlineUsers",users-1);
				System.out.println("corenode id"+coreNodeInDB.getId()+":corenode value:"+coreNodeInDB.getProperty("value"));
				System.out.println("pre>"+coreNodeInDB.getProperty("onlineUsers"));
			}
			//onlineUsers
			tx1.success();
		}
	}

	public int getOnlineUsersURL(Node urlNodeInDB){

		int n=0;
		try(Transaction tx=graph.beginTx()){
			Label label = DynamicLabel.label("LabelUserNode");
			//			System.out.println("url Node :"+urlNodeInDB.getProperty("value"));
			//			System.out.println("url Node :"+urlNodeInDB.getProperty("connectedToMessageNodeUrl"));
			//			System.out.println("url Node :"+urlNodeInDB.getProperty("connectedToCoreNode"));
			//			System.out.println("url node id : "+urlNodeInDB.getId());
			ResourceIterable<Node> ncol= graph.findNodesByLabelAndProperty(label,"connectedToUrl",urlNodeInDB.getId());
			ResourceIterator<Node> nodeIterator=ncol.iterator();
			while(nodeIterator.hasNext()){
				Node userNodeInDB;
				userNodeInDB=nodeIterator.next();
				//				System.out.println("status : "+userNodeInDB.getProperty("status"));
				//		System.out.println("connectedToUrlId in count : "+userNodeInDB.getProperty("connectedToUrlId"));
				if(userNodeInDB.getProperty("status").equals("true")){
					//System.out.println("true");
					n++;
				}
			}
			tx.success();
		}
		System.out.println("number of online users : "+n);
		return n;
	}

	public String getMessageFromCoreNode(String uId,String url)
	{


		Node msgNodeInDB=null;
		UserNode userNode=new UserNode();
		//userNode.setName(uname);
		userNode.setUserID(uId);
		userNode.setHasUrl(url);

		UserDetailsSql sql= new UserDetailsSql(sqlConnection);
		sql.setUserDetails(userNode, uId);


		Node userNodeInDB=db.getUserNode(userNode);//get usernode returns the node from the db or null
		String messagesToSend="";
		UrlNode urlNode=new UrlNode(url);

		Node urlNodeInDB=db.getUrlNode(urlNode);
		Node coreNodeInDB;
		if(urlNodeInDB==null){

			System.out.println("----------------------------new url node");

			String msgs="";
			urlNode.setConnectedToCoreNodeId(url,graph, sqlConnection);      //+++++++++++++++calls meher method internally
			MessageNodeCore mnc= new MessageNodeCore("");

			long connectedToMessageNodeUrlId=db.createMessageNodeCore(mnc).getId();

			urlNode.setConnectedToMessageNodeUrlId(connectedToMessageNodeUrlId);

			urlNodeInDB=db.createUrlNode(urlNode);

			userNode.setConnectedToUrlId(urlNodeInDB.getId());
			userNode.setLastSeenMessageTime(System.currentTimeMillis());
			userNode.setStatus("true");

			updateUserInterests(userNode,urlNode);

			userNodeInDB=db.createUserNode(userNode);

			db.createLink(userNodeInDB, urlNodeInDB, RelTypes.toUrlNode);


			try(Transaction trans1=graph.beginTx()){

				long coreNodeId=(long)urlNodeInDB.getProperty("connectedToCoreNode");

				coreNodeInDB=graph.getNodeById(coreNodeId);

				System.out.println("connected to core node------------------>"+coreNodeInDB.getProperty("value"));

				long msgNodeId=(long)coreNodeInDB.getProperty("connectedMessageNodeCore");
				msgNodeInDB=graph.getNodeById(msgNodeId);
				msgs=(String) msgNodeInDB.getProperty("value");





				int onlineUsers=(int) coreNodeInDB.getProperty("onlineUsers");
				coreNodeInDB.setProperty("onlineUsers",onlineUsers+1);
				System.out.println("incremented when url not exit");
				int totalUsers=(int) coreNodeInDB.getProperty("totalUsers");				
				coreNodeInDB.setProperty("totalUsers", totalUsers+1);

				trans1.success();
			}
			//here
			String[] msgWithTime=msgs.split(",");
			String[] msgTime;
			for (int i = 0; i < msgWithTime.length && i<15; i++) {
				//begin
				System.out.println("mesg with time-->"+msgWithTime[i]+"---");
				msgTime=msgWithTime[i].split("::");

				if(msgTime.length<2)
					continue;
				//	System.out.println("length="+msgTime.length+",mesgtime="+msgTime[0]);
				/*Long messageTime = new Long(msgTime[1]);
				Long lastSeenTime=(Long) userNodeInDB.getProperty("lastSeenMessageTime");
				if(messageTime<lastSeenTime)
					break;*/
				for (int j = 0; j < msgTime.length; j++) {
					messagesToSend+=msgTime[j]+"[(:-,-:)]";
				}
				messagesToSend=messagesToSend+"[(:-;-:)]";

				//end
				//	messagesToSend+=msgWithTime[i]+"[(:-,-:)]";
			}
			try(Transaction trans3=graph.beginTx()){
				//userNodeInDB.setProperty("lastSeenMessageTime", System.currentTimeMillis());
				System.out.println("cf="+coreNodeInDB.getProperty("value"));
				trans3.success();

			}

			int n=getOnlineUsersForCoreNode(coreNodeInDB);
			messagesToSend=messagesToSend+"[(:-,;,-:)]"+n;
			System.out.println("message to send:"+messagesToSend);
			return messagesToSend;
		}

		if(userNodeInDB == null)
		{

			System.out.println("new user");

			userNode.setConnectedToUrlId(urlNodeInDB.getId());
			userNode.setLastSeenMessageTime(System.currentTimeMillis());
			userNode.setStatus("true");

			userNodeInDB=db.createUserNode(userNode);

			db.createLink(userNodeInDB, urlNodeInDB, RelTypes.toUrlNode);

			String msgs;

			try(Transaction trans1=graph.beginTx()){

				long coreNodeId=(long)urlNodeInDB.getProperty("connectedToCoreNode");

				coreNodeInDB=graph.getNodeById(coreNodeId);

				System.out.println("core node value for new user:-------------->"+coreNodeInDB.getProperty("value"));

				long msgNodeId=(long)coreNodeInDB.getProperty("connectedMessageNodeCore");

				msgNodeInDB=graph.getNodeById(msgNodeId);

				msgs=(String) msgNodeInDB.getProperty("value");

				System.out.println("msgs://///"+msgs);

				int onlineUsers=(int) coreNodeInDB.getProperty("onlineUsers");
				coreNodeInDB.setProperty("onlineUsers",onlineUsers+1);
				System.out.println("incremented when user not exit");
				int totalUsers=(int) coreNodeInDB.getProperty("totalUsers");				
				coreNodeInDB.setProperty("totalUsers", totalUsers+1);
				trans1.success();
			}
			String[] msgWithTime=msgs.split(",");
			String[] msgTime;
			////////////////////////////////////////////////////handle below
			for (int i = 0; i < msgWithTime.length && i<15; i++) {
				//begin
				System.out.println("mesg with time-->"+msgWithTime[i]+"---");
				msgTime = msgWithTime[i].split("::");

				if(msgTime.length<2)
					continue;
				//		System.out.println("length="+msgTime.length+",mesgtime="+msgTime[0]);
				/*Long messageTime = new Long(msgTime[1]);
				Long lastSeenTime=(Long) userNodeInDB.getProperty("lastSeenMessageTime");
				if(messageTime<lastSeenTime)
					break;*/
				for (int j = 0; j < msgTime.length; j++) {
					messagesToSend+=msgTime[j]+"[(:-,-:)]";
				}
				messagesToSend=messagesToSend+"[(:-;-:)]";



				//end
				//messagesToSend+=msgWithTime[i]+"[(:-,-:)]";

			}
			try(Transaction trans3=graph.beginTx()){
				userNodeInDB.setProperty("lastSeenMessageTime", System.currentTimeMillis());
				trans3.success();
			}
			System.out.println("new user corenode:"+coreNodeInDB.getProperty("value"));
			int n=getOnlineUsersForCoreNode(coreNodeInDB);
			messagesToSend=messagesToSend+"[(:-,;,-:)]"+n;
			System.out.println("message to send:"+messagesToSend);
			return messagesToSend;
		}

		System.out.println("IN getmsgs() ---user and url exists---");
		String msgs="";
		try(Transaction trans2=graph.beginTx())
		{
			//System.out.println("user and url exists");

			long urlNodeId=(long)userNodeInDB.getProperty("connectedToUrl");
			urlNodeInDB=graph.getNodeById(urlNodeId);

			long coreNodeId=(long)urlNodeInDB.getProperty("connectedToCoreNode");
			coreNodeInDB=graph.getNodeById(coreNodeId);


			//		System.out.println("user details`````````:"+userNodeInDB.getProperty("value")+"status:"+status);
			trans2.success();

		}
		try(Transaction trans22=graph.beginTx())
		{
			String status=(String) userNodeInDB.getProperty("status");

			if(status.equalsIgnoreCase("false")){

				System.out.println("false is made true!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				userNodeInDB.setProperty("status", "true");

				int onlineUsers=(int) coreNodeInDB.getProperty("onlineUsers");

				System.out.println("retirved"+onlineUsers);

				coreNodeInDB.setProperty("onlineUsers",onlineUsers+1);
				System.out.println("incremented when bothe exist");

				System.out.println("updated value:"+coreNodeInDB.getProperty("onlineUsers"));


				int totalUsers=(int) coreNodeInDB.getProperty("totalUsers");	

				coreNodeInDB.setProperty("totalUsers", totalUsers+1);
			}else
			{

				System.out.println("elseeeeeeeeeeeeeeeeeeeeeeee");
			}
			trans22.success();
		}

		try(Transaction trans23=graph.beginTx()){
			System.out.println("refering to code node------------------------------>>>>"+coreNodeInDB.getProperty("value"));

			long msgNodeId=(long)coreNodeInDB.getProperty("connectedMessageNodeCore");
			msgNodeInDB=graph.getNodeById(msgNodeId);

			//System.out.println("get messages \t"+msgNodeInDB.getId());
			//	System.out.println("msgs:"+ msgNodeInDB.getProperty("value"));
			msgs=(String) msgNodeInDB.getProperty("value");
			//	trans.success();
			//System.out.println("Msgsss:"+msgs);

			if(!msgs.equals("")){
				String[] msgWithTime=msgs.split(",");
				String[] msgTime;

				for (int i = 0;i<msgWithTime.length ; i++) {
					System.out.println("-->>>>>> MsgWithTime[i]: "+msgWithTime[i]);
					msgTime=msgWithTime[i].split("::");
					if(msgTime.length==0)
						continue;
					Long messageTime = new Long(msgTime[1]);
					Long lastSeenTime=(Long) userNodeInDB.getProperty("lastSeenMessageTime");
					if(messageTime<lastSeenTime){
						//System.out.println("last seen");
						break;
					}
					messagesToSend+=msgTime[0]+"[(:-,-:)]"+msgTime[1]+"[(:-,-:)]"+msgTime[2]+"[(:-,-:)]"+msgTime[3]+"[(:-,-:)]"+msgTime[4]+"[(:-,-:)]"+msgTime[5]+"[(:-,-:)]"+msgTime[6]+"[(:-;-:)]";

				}
			}
			String status1=(String) userNodeInDB.getProperty("status");
			System.out.println("status1"+status1);
			trans23.success();

		}

		try(Transaction trans3=graph.beginTx()){

			String status2=(String) userNodeInDB.getProperty("status");
			System.out.println("status2"+status2);


			userNodeInDB.setProperty("lastSeenMessageTime", System.currentTimeMillis());

			System.out.println("both exist:cccccccccccccccccccccccccccccccc"+coreNodeInDB.toString());
			int n=(int) coreNodeInDB.getProperty("onlineUsers");              //getOnlineUsersForCoreNode(coreNodeInDB);
			messagesToSend=messagesToSend+"[(:-,;,-:)]"+n;
			System.out.println("message to send1:----->"+messagesToSend);
			trans3.success();
		}
		return messagesToSend;

	}
	private void updateUserInterests(UserNode userNode, UrlNode urlNode) {


	}

	public String getMessageFromURLNode(String uId,String url){

		System.out.println("inside dbutilites ,uid:"+uId+",url:"+url);

		Node msgNodeInDB=null;
		UserNode userNode=new UserNode();
		userNode.setUserID(uId);
		userNode.setHasUrl(url);

		UserDetailsSql sql= new UserDetailsSql(sqlConnection);
		sql.setUserDetails(userNode, uId);

		Node userNodeInDB=db.getUserNode(userNode);
		String messagesToSend="";
		UrlNode urlNode=new UrlNode(url);

		Node urlNodeInDB=db.getUrlNode(urlNode);
		if(urlNodeInDB==null){
			System.out.println("new url");
			urlNode.setConnectedToCoreNodeId(url,graph, sqlConnection);//calls meher method internally
			MessageNodeCore mnc= new MessageNodeCore("");
			long connectedToMessageNodeUrlId=db.createMessageNodeCore(mnc).getId();
			urlNode.setConnectedToMessageNodeUrlId(connectedToMessageNodeUrlId);
			urlNodeInDB=db.createUrlNode(urlNode);

			userNode.setConnectedToUrlId(urlNodeInDB.getId());
			userNode.setLastSeenMessageTime(System.currentTimeMillis());
			userNode.setStatus("true");

			userNodeInDB=db.createUserNode(userNode);

			db.createLink(userNodeInDB, urlNodeInDB, RelTypes.toUrlNode);

			return "empty";
		}
		if(userNodeInDB == null){
			userNode.setConnectedToUrlId(urlNodeInDB.getId());
			userNode.setLastSeenMessageTime(System.currentTimeMillis());
			userNode.setStatus("true");
			userNodeInDB=db.createUserNode(userNode);


			db.createLink(userNodeInDB, urlNodeInDB, RelTypes.toUrlNode);
			String msgs;

			try(Transaction trans1=graph.beginTx())
			{
				long msgNodeId=(long)urlNodeInDB.getProperty("connectedToMessageNodeUrl");
				msgNodeInDB=graph.getNodeById(msgNodeId);
				msgs=(String) msgNodeInDB.getProperty("value");
				trans1.success();
			}
			String[] msgWithTime=msgs.split(",");//to seperate msg strings //to do:change delimiter here and while adding msg

			for (int i = 0; i < msgWithTime.length && i<15; i++) {
				messagesToSend+=msgWithTime[i]+"[(:-,-:)]";
			}

			try(Transaction trans3=graph.beginTx()){
				userNodeInDB.setProperty("lastSeenMessageTime", System.currentTimeMillis());
				trans3.success();
			}
			int n=getOnlineUsersURL(urlNodeInDB);
			messagesToSend=messagesToSend+"[(:-,;,-:)]"+n;
			System.out.println("message to send:"+messagesToSend);
			return messagesToSend;
		}
		try(Transaction trans2=graph.beginTx())
		{
			System.out.println("user and url exists");

			long urlNodeId=(long)userNodeInDB.getProperty("connectedToUrl");
			urlNodeInDB=graph.getNodeById(urlNodeId);

			long msgNodeId=(long)urlNodeInDB.getProperty("connectedToMessageNodeUrl");

			String status=(String) userNodeInDB.getProperty("status");
			if(status.equalsIgnoreCase("false")){
				userNodeInDB.setProperty("status", "true");
			}

			msgNodeInDB=graph.getNodeById(msgNodeId);
			String msgs=(String)msgNodeInDB.getProperty("value");
			//	trans.success();
			System.out.println("Msgsss:"+msgs);
			String[] msgWithTime=msgs.split(",");

			String[] msgTime;
			System.out.println("message with time "+msgWithTime.length);
			for (int i = 0;i<msgWithTime.length; i++) {
				System.out.println("mesg with time-->"+msgWithTime[i]+"---");
				msgTime=msgWithTime[i].split("::");

				if(msgTime.length<2)
					continue;
				System.out.println("length="+msgTime.length+",mesgtime="+msgTime[0]);
				Long messageTime = new Long(msgTime[1]);
				Long lastSeenTime=(Long) userNodeInDB.getProperty("lastSeenMessageTime");
				if(messageTime<lastSeenTime)
					break;
				//here
				for (int j = 0; j < msgTime.length; j++) {
					messagesToSend+=msgTime[j]+"[(:-,-:)]";
				}
				messagesToSend=messagesToSend+"[(:-;-:)]";

				System.out.println("999999"+messagesToSend);

				//		messagesToSend+=msgTime[0]+"[(:-,-:)]"+msgTime[1]+"[(:-,-:)]"+msgTime[2]+"[(:-,-:)]"+msgTime[3]+"[(:-,-:)]"+msgTime[4]+"[(:-;-:)]";


			}

			System.out.println("message to send in dbutil==>"+messagesToSend);
			trans2.success();
		}

		try(Transaction trans3=graph.beginTx()){
			userNodeInDB.setProperty("lastSeenMessageTime", System.currentTimeMillis());
			trans3.success();
		}
		int n=getOnlineUsersURL(urlNodeInDB);
		messagesToSend=messagesToSend+"[(:-,;,-:)]"+n;
		System.out.println("message to send:"+messagesToSend);
		return messagesToSend;

	}

	public void addMessageToURLNode(String uId,String url,String message){

		UserNode userNode=new UserNode();
		userNode.setUserID(uId);
		userNode.setHasUrl(url);

		UserDetailsSql sql= new UserDetailsSql(sqlConnection);
		sql.setUserDetails(userNode, uId);

		Node userNodeInDB=db.getUserNode(userNode);  //checks for user existence

		UrlNode urlNode=new UrlNode(url);          //check for url existence returns node object if exist or else returns null
		Node urlNodeInDB=db.getUrlNode(urlNode);

		if(urlNodeInDB==null){                                 
			System.out.println("In addMsg  url not exists");

			urlNode.setConnectedToCoreNodeId(url,graph, sqlConnection);   //calls meher method internally   and links to core node

			MessageNodeUrl mnu= new MessageNodeUrl(message+"::"+System.currentTimeMillis()+"::"+uId+"::"+userNode.getName()+"::"+url+"::"+userNode.getImageUrl()+"::"+userNode.getInterests());  //attaching a message node to an url and appending the user message

			Node msgNode=db.createMessageNodeUrl(mnu);

			System.out.println("message=:"+msgNode);

			long connectedToMessageNodeUrlId=msgNode.getId();

			//System.out.println("con:"+connectedToMessageNodeUrlId);

			urlNode.setConnectedToMessageNodeUrlId(connectedToMessageNodeUrlId);

			urlNodeInDB=db.createUrlNode(urlNode);//creating neu url node as it not exists previously

			userNode.setConnectedToUrlId(urlNodeInDB.getId());
			userNode.setLastSeenMessageTime((long) 0);
			userNode.setStatus("true");

			userNodeInDB=db.createUserNode(userNode);

			db.createLink(userNodeInDB, urlNodeInDB, RelTypes.toUrlNode);
			return ;
		}
		if(userNodeInDB == null){
			System.out.println("In addMsg user not exists");

			userNode.setConnectedToUrlId(urlNodeInDB.getId());
			userNode.setLastSeenMessageTime((long) 0);
			userNodeInDB=db.createUserNode(userNode);
			db.createLink(userNodeInDB, urlNodeInDB, RelTypes.toUrlNode);

			Transaction trans=graph.beginTx();
			Node msgNodeInDB=null;

			try(Transaction tx=graph.beginTx()){
				long msgNodeId=(long)urlNodeInDB.getProperty("connectedToMessageNodeUrl");
				msgNodeInDB=graph.getNodeById(msgNodeId);
				String msgs=(String) msgNodeInDB.getProperty("value");
				//System.out.println("prev msgs:"+msgs);
				msgNodeInDB.setProperty("value", message+"::"+System.currentTimeMillis()+"::"+uId+"::"+userNode.getName()+"::"+url+"::"+userNode.getImageUrl()+"::"+userNode.getInterests()+","+msgs);
				trans.success();
				msgs=(String) msgNodeInDB.getProperty("value");
				tx.success();
			}
			return ;

		}

		Node msgNodeInDB=null;
		String msgs = "";
		try(Transaction tx=graph.beginTx())
		{
			System.out.println("In addMsg user and url exists");
			long urlNodeId=(long)userNodeInDB.getProperty("connectedToUrl");
			urlNodeInDB=graph.getNodeById(urlNodeId);

			long msgNodeId=(long)urlNodeInDB.getProperty("connectedToMessageNodeUrl");
			msgNodeInDB=graph.getNodeById(msgNodeId);

			msgs=(String)msgNodeInDB.getProperty("value");

			//		System.out.println("add msg url \n"+msgs);
			msgNodeInDB.setProperty("value", message+"::"+System.currentTimeMillis()+"::"+uId+"::"+userNode.getName()+"::"+url+"::"+userNode.getImageUrl()+"::"+userNode.getInterests()+","+msgs);
			System.out.println("properties got set");
			tx.success();
			String msgs1=(String)msgNodeInDB.getProperty("value");
			//System.out.println("inserted as:"+msgs1);
		}
		System.out.println("message added");
	}

	public void addMessagesToCoreNode(String uId,String url,String message){
		UserNode userNode=new UserNode();
		userNode.setUserID(uId);
		userNode.setHasUrl(url);

		UserDetailsSql sql= new UserDetailsSql(sqlConnection);
		sql.setUserDetails(userNode, uId);

		Node userNodeInDB=db.getUserNode(userNode);
		UrlNode urlNode=new UrlNode(url);
		Node urlNodeInDB=db.getUrlNode(urlNode);
		if(urlNodeInDB==null){
			System.out.println("In addMsg  url not exists");
			urlNode.setConnectedToCoreNodeId(url,graph, sqlConnection);//calls meher method internally
			urlNodeInDB=db.getUrlNode(urlNode);
			urlNodeInDB=db.createUrlNode(urlNode);
			userNode.setConnectedToUrlId(urlNodeInDB.getId());
			userNode.setLastSeenMessageTime((long) 0);
			userNode.setStatus("true");
			userNodeInDB=db.createUserNode(userNode);
			db.createLink(userNodeInDB, urlNodeInDB, RelTypes.toUrlNode);
			try(Transaction trans2=graph.beginTx())
			{
				System.out.println(urlNodeInDB);
				long coreNodeId=(long)urlNodeInDB.getProperty("connectedToCoreNode");
				Node coreNodeInDB=graph.getNodeById(coreNodeId);

				long msgNodeId=(long)coreNodeInDB.getProperty("connectedMessageNodeCore");
				Node msgNodeInDB=graph.getNodeById(msgNodeId);
				String msgs=(String) msgNodeInDB.getProperty("value");
				msgs=message+"::"+System.currentTimeMillis()+"::"+uId+"::"+userNode.getName()+"::"+url+"::"+userNode.getImageUrl()+"::"+userNode.getInterests()+','+msgs;
				msgNodeInDB.setProperty("value",msgs);
				trans2.success();
			}
			return ;
		}
		if(userNodeInDB == null){
			System.out.println("In addMsg user not exists");
			userNode.setConnectedToUrlId(urlNodeInDB.getId());
			userNode.setLastSeenMessageTime((long) 0);
			userNode.setStatus("true");
			userNodeInDB=db.createUserNode(userNode);
			db.createLink(userNodeInDB, urlNodeInDB, RelTypes.toUrlNode);

			Node msgNodeInDB=null;
			try(Transaction trans=graph.beginTx())
			{
				long coreNodeId=(long)urlNodeInDB.getProperty("connectedToCoreNode");
				Node coreNodeInDB=graph.getNodeById(coreNodeId);

				long msgNodeId=(long)coreNodeInDB.getProperty("connectedMessageNodeCore");
				msgNodeInDB=graph.getNodeById(msgNodeId);

				String msgs=(String) msgNodeInDB.getProperty("value");
				msgs=message+"::"+System.currentTimeMillis()+"::"+uId+"::"+userNode.getName()+"::"+url+"::"+userNode.getImageUrl()+"::"+userNode.getInterests()+','+msgs;
				msgNodeInDB.setProperty("value",msgs);
				trans.success();

			}
			return ;
		}

		System.out.println("In AddMSg---user and url exists:");
		Node msgNodeInDB=null;
		String msgs = "";

		try(Transaction trans1=graph.beginTx()){

			long urlNodeId=(long)userNodeInDB.getProperty("connectedToUrl");
			urlNodeInDB=graph.getNodeById(urlNodeId);
			System.out.println("----->");
			long coreNodeId=(long)urlNodeInDB.getProperty("connectedToCoreNode");
			/////////////////////////////////////////////////see if getstatus of a user is needed
			System.out.println("corenode:"+coreNodeId);


			Node coreNodeInDB=graph.getNodeById(coreNodeId);

			long msgNodeId=(long)coreNodeInDB.getProperty("connectedMessageNodeCore");
			msgNodeInDB=graph.getNodeById(msgNodeId);

			msgs=(String) msgNodeInDB.getProperty("value");

			System.out.println("add: "+msgs);

			msgs=message+"::"+System.currentTimeMillis()+"::"+uId+"::"+userNode.getName()+"::"+url+"::"+userNode.getImageUrl()+"::"+userNode.getInterests()+','+msgs;

			msgNodeInDB.setProperty("value",msgs);
			//System.out.println("In add message function :  to msg node "+msgNodeInDB.getId()+"\n"+msgNodeInDB.getProperty("value"));
			trans1.success();
		}
	}
	public String getrelatedCoreNodeOfUrl(String url)
	{
		try(Transaction trans1=graph.beginTx()){
			UrlNode un=new UrlNode(url);
			Node urlNodeInDB=db.getUrlNode(un);
			long coreNodeId=(long)urlNodeInDB.getProperty("connectedToCoreNode");
			Node coreNodeInDB=graph.getNodeById(coreNodeId);
			String coreValue=(String) coreNodeInDB.getProperty("value");
			System.out.println("coreValue in getrelatedCoreNodeOfUrl:"+coreValue);
			//		trans1.success();
			return coreValue;
		}
	}
	//code from meher
	//
	//
	//
	/**
	 * 1. Gets the 10 important words
	 * 2. Checks the TF-IDF(core node table) and matches the most appropriate core node(by its ID)
	 * 3. IF the threshold word math is not met, creates a new core node
	 * 4. Insets these 10 words into TF-IDF(core node table) with newly generated core node id
	 * 5. Ultimately, returns an existing node id or creates and returns the new  core node id.
	 * @param url that is to be parsed
	 * @return core node id
	 */
	public String getCoreNodeID(String url){
		try{

			String top10=getTop10Words(url);
			if(top10 == null || top10.equals("null") || !top10.contains("-")){
				System.out.println("Top10 words are NULL");
				throw new Exception();
			}
			String[] checkWords=top10.split("-");

			Connection conn = sqlConnection;
			ArrayList<String> existingwords = new ArrayList<String>();

			String corenode_status = matchWordsFetchNode(checkWords,conn,existingwords);
			System.out.println("========================Find match core node : "+corenode_status);

			if(corenode_status.equals("Exception")) throw new Exception();
			else if(corenode_status.equals("Create")){
				//create a new Core node and insert into core node table
				System.out.println("========================Creating core node...");

				MessageNodeCore mnc = new MessageNodeCore();
				mnc.setValue("");
				Node msgnodegraph = db.createMessageNodeCore(mnc);

				CoreNode cn = new CoreNode();
				cn.setConnectedMessageNodeCoreId(msgnodegraph.getId());
				cn.setValue(top10);
				Node corenodegraph = db.createCoreNode(cn);
				insertNewCoreNodeIntoTable(conn,checkWords,corenodegraph.getId(),existingwords);
				System.out.println("==============Sql data created ");
				return corenodegraph.getId()+"";
			}
			else{
				//returns the existing core node id.
				System.out.println("==============Existing node: "+corenode_status);
				return corenode_status;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the 10 most relevant words from a URL.
	 * @param url
	 * @return
	 */
	private String getTop10Words(String url){
		try{
			String words = "";
			//make a GET request to jelastix server and get the 10 words.
			String jelastic_url = "http://harsha.jelastic.elastx.net/predict/CoreNodeServlet?url="+url;
			System.out.println("Making a cal to jelastix: "+jelastic_url);
			URL u = new URL(jelastic_url);
			BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));

			String line = "";
			while((line=in.readLine())!=null){
				words+=line;
			}
			in.close();

			System.out.println("Response from Jelastix: "+words);

			return words;
		}
		catch(Exception e){
			System.out.println("Error from getTop10Words...");
			return null;
		}
	}

	private String matchWordsFetchNode(String[] words, Connection con, ArrayList<String> existingwords){
		//remove connection object at last.
		try{
			HashMap<String,Integer> wordhits = new HashMap<String,Integer>();
			String max_corenode_id = "";
			int max_count = 0;
			int threshold = 3;

			Statement st = con.createStatement();
			String query = "select corenodeid from corenodetable where word=";

			for(String s : words){
				s = s.replaceAll("[^A-Za-z0-9]", "").trim();
				s = stem(s);
				s = "'"+s+"'";
				System.out.println("Qurying: "+query+s);
				ResultSet rs = st.executeQuery(query+s);
				if(!rs.next()) { //result set is empty
					continue;
				}
				else{ //there is cid with this word 

					String cid = rs.getString("corenodeid");
					existingwords.add(s);
					Integer val = wordhits.get(cid);
					if(val==null){
						wordhits.put(cid, 1);
					}else{
						wordhits.put(cid, val+1);
						if(val+1 > max_count){
							max_corenode_id = cid;
							max_count = val+1;
						}
					}
				}
			}

			if(max_count>=threshold){
				return max_corenode_id;
			}
			else{
				return "Create";
			}


		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Exception in matchWordsFetchNode");
			return "Exception";
		}

	}


	private void insertNewCoreNodeIntoTable(Connection conn, String[] checkWords, long id, ArrayList<String> existingwords) {
		try{
			PreparedStatement ps_insert = conn.prepareStatement("insert into corenodetable values (?, '"+id+""+"' );");
			String get_query = "select corenodeid from corenodetable where word=";
			PreparedStatement ps_update = conn.prepareStatement("update corenodetable set corenodeid=? where word=? ;");
			for(String s : checkWords){
				s = s.replaceAll("[^A-Za-z0-9]", "").trim();
				s = stem(s);
				s = "'"+s+"'";

				if(!existingwords.contains(s)){
					s = s.substring(1, s.lastIndexOf('\''));
					ps_insert.setString(1, s);
					System.out.println(ps_insert.toString());
					ps_insert.execute();
				}
				else{
					ResultSet rs = conn.createStatement().executeQuery(get_query+s);
					rs.next();
					String nodes = rs.getString("corenodeid");

					nodes = nodes+","+id;
					//nodes = "'"+nodes+"'";
					s = s.substring(1, s.lastIndexOf('\''));

					ps_update.setString(2, s);
					ps_update.setString(1, nodes.toString());
					System.out.println(ps_update.toString());
					ps_update.executeUpdate();	
				}
			}
			System.out.println("MySql lo added Strings...");

		}
		catch(Exception e){
			System.out.println("Error from insertNewCoreNodeIntoTable");
			e.printStackTrace();
		}

	}


	private String stem(String s) {
		PorterStemmer stemmer = new PorterStemmer();
		stemmer.setCurrent(s);
		stemmer.stem();
		return stemmer.getCurrent();
	}

}
