package com.apextalos.cvitfusion.common.mqtt.message;

import com.apextalos.cvitfusion.common.opflow.OperationalFlow;

public class EngineSaveRequest extends EngineRequest {

	private OperationalFlow data;

	public OperationalFlow getData() {
		return data;
	}

	public void setData(OperationalFlow data) {
		this.data = data;
	}
	
	public EngineSaveRequest() {
		super();
	}
	
	public EngineSaveRequest(String uuid) {
		super(uuid);
	}
	
	public EngineSaveRequest(OperationalFlow data) {
		super();
		this.data = data;
	}
}
