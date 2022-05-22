package com.apextalos.cvitfusion.client.mqtt;

import com.apextalos.cvitfusion.common.mqtt.ConfigMqttTransceiver;
import com.apextalos.cvitfusion.common.mqtt.ISubscriptionHander;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ClientConfigMqttTransceiver extends ConfigMqttTransceiver {

	public ClientConfigMqttTransceiver(ConfigFile cf) {
		super(cf);
	}

	@Override
	public String[] subscriptionTopics() {
		return null;
	}

	@Override
	public ISubscriptionHander[] subscriptionHandlers() {
		ISubscriptionHander[] subscriptionHandlers = new ISubscriptionHander[1];
		subscriptionHandlers[0] = new EngineStatusSubscriptionHandler();
		return subscriptionHandlers;
	}

	@Override
	public String statusTopic() {
		return null;
	}

	@Override
	public String buildStatusPayload() throws JsonProcessingException {
		return null;
	}
}
