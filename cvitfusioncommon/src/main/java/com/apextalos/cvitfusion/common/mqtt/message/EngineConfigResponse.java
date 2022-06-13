package com.apextalos.cvitfusion.common.mqtt.message;

import org.joda.time.DateTime;

import com.apextalos.cvitfusion.common.opflow.OperationalFlow;

public class EngineConfigResponse extends Request {

	private OperationalFlow data;

	public OperationalFlow getData() {
		return data;
	}

	public void setData(OperationalFlow data) {
		this.data = data;
	}

	public EngineConfigResponse() {
		super();
	}

	public EngineConfigResponse(String uuid) {
		super(uuid);
	}

	public EngineConfigResponse(String uuid, DateTime at, OperationalFlow data) {
		super(uuid, at);
		this.data = data;
	}
}
