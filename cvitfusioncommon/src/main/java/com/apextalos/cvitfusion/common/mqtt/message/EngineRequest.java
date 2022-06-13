package com.apextalos.cvitfusion.common.mqtt.message;

import java.util.UUID;

import org.joda.time.DateTime;

public class EngineRequest {
	
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

	public EngineRequest() {
		this.uuid = UUID.randomUUID().toString();
		this.at = DateTime.now().toString();
	}
	
	public EngineRequest(String uuid) {
		this.uuid = uuid;
		this.at = DateTime.now().toString();
	}
	
	public EngineRequest(String uuid, DateTime at) {
		this.uuid = uuid;
		this.at = at.toString();
	}
}
