package com.apextalos.cvitfusion.common.mqtt.message;

import org.joda.time.DateTime;

public class Response extends Request{

	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Response() {
		super();
	}
	
	public Response(String uuid) {
		super(uuid);
	}
		
	public Response(String uuid, DateTime at, Object data) {
		super(uuid, at);
		this.data = data;
	}
}
