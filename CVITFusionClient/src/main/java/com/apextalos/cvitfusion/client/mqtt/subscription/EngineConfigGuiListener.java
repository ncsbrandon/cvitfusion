package com.apextalos.cvitfusion.client.mqtt.subscription;

import com.apextalos.cvitfusion.common.opflow.OperationalFlow;

public interface EngineConfigGuiListener {
	public void onEngineConfig(String topic, String payload, OperationalFlow engineConfig);
}
