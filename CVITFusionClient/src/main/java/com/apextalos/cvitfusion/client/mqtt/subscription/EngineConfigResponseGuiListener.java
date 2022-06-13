package com.apextalos.cvitfusion.client.mqtt.subscription;

import com.apextalos.cvitfusion.common.opflow.OperationalFlow;

public interface EngineConfigResponseGuiListener {
	public void onEngineConfigRequest(String engineID, String topic, String payload, OperationalFlow engineConfig);
}
