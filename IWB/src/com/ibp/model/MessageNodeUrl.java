package com.ibp.model;

public class MessageNodeUrl
{
//	private Node connectedUrlNodeValue;  //url string value
//	private String category;               //MessageNodeurl
	private String value;                 //messages and time stamp including user separated with a delimiter
	
	public MessageNodeUrl() {
		// TODO Auto-generated constructor stub
	}
	
	public MessageNodeUrl(String value) {
		super();
//		setConnectedUrlNodeValue(connectedUrlNodeValue);
		setValue(value);
	}
	/*public Node getConnectedUrlNodeValue() {
		return connectedUrlNodeValue;
	}
	public void setConnectedUrlNodeValue(Node connectedUrlNodeValue) {
		this.connectedUrlNodeValue = connectedUrlNodeValue;
	}*/
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
