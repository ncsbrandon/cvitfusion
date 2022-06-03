package com.apextalos.cvitfusion.client.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.client.mqtt.subscription.EngineStatusSubscriptionHandler;
import com.apextalos.cvitfusion.common.mqtt.ConfigMqttTransceiver;
import com.apextalos.cvitfusion.common.mqtt.message.Request;
import com.apextalos.cvitfusion.common.mqtt.subscription.ISubscriptionHander;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionEvent;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionListener;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicBuilder;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ClientConfigMqttTransceiver extends ConfigMqttTransceiver {

	private static Logger logger = LogManager.getLogger(ClientConfigMqttTransceiver.class.getSimpleName());

	public ClientConfigMqttTransceiver(ConfigFile cf) {
		super(cf);
	}

	@Override
	public void start() {
		super.start();
		
		addSubscriptionListener(null);
		new EngineStatusSubscriptionHandler(subscriptionListener);
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
	
	public void requestConfig(String engineID, SubscriptionListener listener) {
		// topic
		String topic = TopicBuilder.requestConfig(engineID);
		
		// content
		Request r = new Request(null);

		// convert to json
		String payload;
		try {
			payload = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(r);
		} catch (JsonProcessingException e) {
			logger.error("Failure building request config: " + e.getMessage());
			return;
		}
		
		// send to all connected engines
		publish(topic, payload, false);
		
		// wait for a response
		addSubscription(TopicBuilder.respondConfig(engineID), r.getUUID(), listener);
	}
}
