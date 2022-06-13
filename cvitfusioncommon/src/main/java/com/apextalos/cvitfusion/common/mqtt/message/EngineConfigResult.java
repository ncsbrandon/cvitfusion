package com.apextalos.cvitfusion.common.mqtt.message;

import org.joda.time.DateTime;

public class EngineConfigResult extends EngineRequest {

	private boolean success;
	
	public boolean getSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public EngineConfigResult() {
		super();
	}

	public EngineConfigResult(String uuid) {
		super(uuid);
	}

	public EngineConfigResult(String uuid, DateTime at) {
		super(uuid, at);
	}
}
