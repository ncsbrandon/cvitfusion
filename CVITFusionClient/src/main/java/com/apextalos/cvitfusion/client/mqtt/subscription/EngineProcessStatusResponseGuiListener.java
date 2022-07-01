package com.apextalos.cvitfusion.client.mqtt.subscription;

public interface EngineProcessStatusResponseGuiListener {
	public void onProcessStatusResponse(String engineID, String topic, String payload, String status);
}
