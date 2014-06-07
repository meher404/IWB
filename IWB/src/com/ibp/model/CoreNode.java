package com.ibp.model;

public class CoreNode
{
	//private String category;   //corenode
	private String value;      //string value of core node(food+manchuriay)
	private long connectedMessageNodeCoreId;
	private int onlineUsers;
	private int totalUsers;
	
	public CoreNode(String value, long connectedMessageNodeCoreId) {
		super();
		this.value = value;
		this.connectedMessageNodeCoreId = connectedMessageNodeCoreId;
	}

	public CoreNode() {
		// TODO Auto-generated constructor stub
	}
	
	
	public CoreNode(String value)
	{
    	//setCategory(category);
    	setValue(value);
	}
	

	public int getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(int totalUsers) {
		this.totalUsers = totalUsers;
	}
	public void setOnlineUsers(int onlineUsers){
		this.onlineUsers=onlineUsers;
	}
	
	public int getOnlineUsers(){
		return this.onlineUsers;
	}
	
	/*public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}*/
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public long getConnectedMessageNodeCoreId() {
		return connectedMessageNodeCoreId;
	}

	public void setConnectedMessageNodeCoreId(long connectedMessageNodeCoreId) {
		this.connectedMessageNodeCoreId = connectedMessageNodeCoreId;
	}

	
	

}
