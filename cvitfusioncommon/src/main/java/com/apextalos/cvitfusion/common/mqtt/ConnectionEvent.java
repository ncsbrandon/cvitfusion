package com.apextalos.cvitfusion.common.mqtt;

public class ConnectionEvent {

	public enum Change {
		CONNECTSUCCESS,
		CONNECTFAILURE,
		DISCONNECT,
		INCOMINGMESSAGE
	}
	
	private Change change;
	private String message;

	public Change getChange() {
		return change;
	}

	public void setChange(Change change) {
		this.change = change;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public ConnectionEvent(Change change, String message) {
		super();
		this.change = change;
		this.message = message;
	}


}
