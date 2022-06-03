package com.apextalos.cvitfusion.common.mqtt.message;

import java.util.UUID;

import org.joda.time.DateTime;

public class Request {
	
	private String uuid;
	private String at;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getAt() {
		return at;
	}

	public void setAt(String at) {
		this.at = at;
	}

	public Request() {
		super();
		this.uuid = UUID.randomUUID().toString();
		this.at = DateTime.now().toString();
	}
	
	public Request(String uuid) {
		super();
		this.uuid = uuid;
		this.at = DateTime.now().toString();
	}
	
	public Request(String uuid, DateTime at) {
		super();
		this.uuid = uuid;
		this.at = at.toString();
	}
}
