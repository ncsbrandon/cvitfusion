package com.apextalos.cvitfusion.client.mqtt.subscription;

import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus;

public interface EngineStatusGuiListener {
	public void onEngineStatus(String topic, String payload, EngineStatus es);
}
