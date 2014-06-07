package com.ibp.model;

public class UserNode
{
	//  private String category;  //user category
	private String userID;     //user ID 
	private String name;     //
	private Long lastSeenMessageTime;//last seen message time
	private long connectedToUrlId;
	private String hasUrl;
	private String status;   //added to know whether the user is active or not
	private String dob;
	private String imageUrl;
	private String gender;
	private String interests;
	
	
	public String getInterests() {
		return interests;
	}

	public void setInterests(String interests) {
		this.interests = interests;
	}

	public UserNode(String userID, String name, long connectedToUrlId) {
		super();
		setUserID(userID);
		setName(name);
		setConnectedToUrlId(connectedToUrlId);
	}

	public UserNode() {

	}
	
	
	
	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getLastSeenMessageTime() {
		return lastSeenMessageTime;
	}

	public void setLastSeenMessageTime(Long lastSeenMessageTime) {
		this.lastSeenMessageTime = lastSeenMessageTime;
	}

	public long getConnectedToUrlId() {
		return connectedToUrlId;
	}

	public void setConnectedToUrlId(long connectedToUrlId) {
		this.connectedToUrlId = connectedToUrlId;
	}

	public String getHasUrl() {
		return hasUrl;
	}

	public void setHasUrl(String hasUrl) {
		this.hasUrl = hasUrl;
	}
	
	
}