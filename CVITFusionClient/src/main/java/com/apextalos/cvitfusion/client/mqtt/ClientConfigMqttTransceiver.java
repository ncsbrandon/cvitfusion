package com.apextalos.cvitfusion.client.mqtt;

import com.apextalos.cvitfusion.client.mqtt.subscription.EngineStatusSubscriptionHandler;
import com.apextalos.cvitfusion.common.mqtt.ConfigMqttTransceiver;
import com.apextalos.cvitfusion.common.mqtt.subscription.ISubscriptionHander;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionListener;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ClientConfigMqttTransceiver extends ConfigMqttTransceiver {

	public ClientConfigMqttTransceiver(ConfigFile cf) {
		super(cf);
	}

	@Override
	public String[] subscriptionTopics() {
		// no loose subscription topics.  these should be in proper handlers
		return new String[0];
	}

	@Override
	public ISubscriptionHander[] subscriptionHandlers(SubscriptionListener subscriptionListener) {
		ISubscriptionHander[] subscriptionHandlers = new ISubscriptionHander[1];
		subscriptionHandlers[0] = new EngineStatusSubscriptionHandler(subscriptionListener);
		return subscriptionHandlers;
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
