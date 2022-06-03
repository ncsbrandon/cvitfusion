package com.apextalos.cvitfusion.common.mqtt.message;

import java.util.UUID;

public class Request {

	private String uuid;
	private Object object;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getUUID() {
		return uuid;
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

	public Request(Object object, String uuid) {
		super();
		this.object = object;
		this.uuid = uuid;
	}
	
	public Request(Object object) {
		super();
		this.object = object;
		this.uuid = UUID.randomUUID().toString();
	}
}
