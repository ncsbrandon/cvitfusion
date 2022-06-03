package com.apextalos.cvitfusion.common.mqtt.subscription;

import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus;

public class EngineStatusSubscriptionExEvent extends SubscriptionExEvent {

	private EngineStatus es;

	public EngineStatus getEngineStatus() {
		return es;
	}

	public void setEngineStatus(EngineStatus es) {
		this.es = es;
	}

	public EngineStatusSubscriptionExEvent(String topic, String payload, EngineStatus es) {
		super(topic, payload);
		this.es = es;
	}
}
