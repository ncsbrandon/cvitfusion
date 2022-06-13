package com.apextalos.cvitfusion.client.mqtt.subscription;

public interface EngineConfigResultGuiListener {
	public void onEngineConfigSave(String engineID, String topic, String payload, boolean result);
}
