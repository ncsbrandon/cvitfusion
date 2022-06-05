package com.apextalos.cvitfusion.client.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.client.mqtt.subscription.EngineConfigGuiListener;
import com.apextalos.cvitfusion.client.mqtt.subscription.EngineConfigSubscriptionExListener;
import com.apextalos.cvitfusion.client.mqtt.subscription.EngineStatusGuiListener;
import com.apextalos.cvitfusion.client.mqtt.subscription.EngineStatusSubscriptionExListener;
import com.apextalos.cvitfusion.common.mqtt.ConfigMqttTransceiver;
import com.apextalos.cvitfusion.common.mqtt.message.Request;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicBuilder;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ClientConfigMqttTransceiver extends ConfigMqttTransceiver {

	private static final Logger logger = LogManager.getLogger(ClientConfigMqttTransceiver.class.getSimpleName());

	public ClientConfigMqttTransceiver(ConfigFile cf) {
		super(cf);
	}
	
	public void start(EngineStatusGuiListener guiListener) {
		super.start();
		
		// this is a persistent generic subscription for engine status
		EngineStatusSubscriptionExListener l = new EngineStatusSubscriptionExListener(guiListener);
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
	
	public void requestConfig(String engineID, EngineConfigGuiListener guiListener) {
		Request request = new Request();
		
		// create a subscription for the response
		EngineConfigSubscriptionExListener l = new EngineConfigSubscriptionExListener(guiListener, engineID, request);
		subscribe(l.topic());
		addSubscriptionListener(l);
		
		// build the payload
		String requestPayload;
		try {
			logger.debug("Requesting id: " + request.getUuid());
			requestPayload = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
		} catch (JsonProcessingException e) {
			logger.error("Engine config request write failure" + e.getMessage());
			return;
		}
		
		// make the request
		String requestTopic = TopicBuilder.requestConfig(engineID);
		publish(requestTopic, requestPayload, false);
	}
}
