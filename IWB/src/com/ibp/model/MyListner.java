package com.ibp.model;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;
import org.neo4j.graphdb.Node;

/**
 * Application Lifecycle Listener implementation class MyListner
 *
 */
@WebListener
public class MyListner implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public MyListner() {
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    //DBConnection db = new DBConnection();
    GraphDatabaseService graphUpdateMessage;
    public void contextInitialized(ServletContextEvent arg0) {
        //db.getConnection();
    	try
       	{
    		System.out.println("in side my listerner try");
    		final String DB_PATH = "Path to neo4j";
    		
            System.out.println("dp:"+DB_PATH);
    		graphUpdateMessage = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
    		System.out.println("Created Database Connection from MyListener..<pluginDB>");
    		
    		resetOnlineUsersCount();
    		
    		
       	}
       	catch(Exception e) {
       		e.printStackTrace();
       	}
        ServletContext context = arg0.getServletContext();
        context.setAttribute("db", graphUpdateMessage);//setting graph db objet as contex variable
        
        MysqlListener sqlListener=new MysqlListener();
        Connection sqlConnection=sqlListener.getMysqlConnection();
        System.out.println("Mysql con obj:"+sqlConnection);  
        context.setAttribute("sqlDBConnection", sqlConnection); //setting Mysql object as contex variable
        
   /*     ConcurrentHashMap<String, ArrayList<String>> userInterests = new ConcurrentHashMap<>();
        context.setAttribute("userInterests", userInterests);//setting user interest as context variable
*/        
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    	System.out.println("Database shutdown from mylistener...<Mylistener>");
    	graphUpdateMessage.shutdown();
    }
	
    public void resetOnlineUsersCount()
    {
    	try(Transaction tx=graphUpdateMessage.beginTx())
    	{
	    	Label label = DynamicLabel.label("LabelCoreNode");
			ResourceIterable<Node> iter = GlobalGraphOperations.at(graphUpdateMessage).getAllNodesWithLabel(label);
			for(Node node : iter)
			{
				Iterable<Label> i = node.getLabels();
				for(Label l: i)
					System.out.println(l.name());
				if(node.hasProperty("onlineUsers"))
				{
					System.out.println("::::::::::::::::::::Rewriting node: " + node.getProperty("value") + ": " + node.getProperty("onlineUsers"));
					node.setProperty("onlineUsers",0);
					//System.out.println("     Done rewriting: " + node.getProperty("onlineUsers"));
				}
			}
			System.out.println("..................Done writing all nodes!!!....................");
    	}
    }
}
