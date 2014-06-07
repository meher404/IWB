package com.ibp.model;

public class MessageNodeCore
{
//	private Node connectedCoreNodeValue; //value of the core node it is connected to
	private String value;                  //messages string seperted with a delimiter
	//	private String category;               //messageNodecore

	public MessageNodeCore() {
		// TODO Auto-generated constructor stub
	}
	
	public MessageNodeCore( String value) {
		super();
		System.out.println(value);

//		setConnectedCoreNodeValue(connectedCoreNodeValue);
		setValue(value);
	}
	/*public Node getConnectedCoreNodeValue() {
		return connectedCoreNodeValue;
	}
	public void setConnectedCoreNodeValue(Node connectedCoreNodeValue) {
		this.connectedCoreNodeValue = connectedCoreNodeValue;
	}*/
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	/*	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	 */
}