package com.apextalos.cvitfusion.client.mqtt;

import com.apextalos.cvitfusion.client.mqtt.subscription.EngineStatusGuiListener;
import com.apextalos.cvitfusion.client.mqtt.subscription.EngineStatusSubscriptionExListener;
import com.apextalos.cvitfusion.common.mqtt.ConfigMqttTransceiver;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ClientConfigMqttTransceiver extends ConfigMqttTransceiver {

	public ClientConfigMqttTransceiver(ConfigFile cf) {
		super(cf);
	}
	
	
	public void start(EngineStatusGuiListener subscriptionListener) {
		super.start();
		
		EngineStatusSubscriptionExListener l = new EngineStatusSubscriptionExListener(subscriptionListener);
		subscribe(l.topic());
		addSubscriptionListener(l);
	}
	
	@Override
	public String statusTopic() {
		// i won't have the client generating status - atleast not initially
		return null;
	}

	@Override
	public String buildStatusPayload() throws JsonProcessingException {
		// i won't have the client generating status - atleast not initially
		return null;
	}
}
