package com.apextalos.cvitfusion.common.mqtt.message;

import org.joda.time.DateTime;

import com.apextalos.cvitfusion.common.opflow.OperationalFlow;

public class ResponseEngineConfig extends Request {

	private OperationalFlow data;

	public OperationalFlow getData() {
		return data;
	}

	public void setData(OperationalFlow data) {
		this.data = data;
	}

	public ResponseEngineConfig() {
		super();
	}

	public ResponseEngineConfig(String uuid) {
		super(uuid);
	}

	public ResponseEngineConfig(String uuid, DateTime at, OperationalFlow data) {
		super(uuid, at);
		this.data = data;
	}
}
