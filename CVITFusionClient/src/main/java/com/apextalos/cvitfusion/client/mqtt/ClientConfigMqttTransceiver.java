package com.apextalos.cvitfusion.client.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.client.mqtt.subscription.EngineConfigRequestGuiListener;
import com.apextalos.cvitfusion.client.mqtt.subscription.EngineConfigRequestSubscriptionExListener;
import com.apextalos.cvitfusion.client.mqtt.subscription.EngineStatusGuiListener;
import com.apextalos.cvitfusion.client.mqtt.subscription.EngineStatusSubscriptionExListener;
import com.apextalos.cvitfusion.common.mqtt.ConfigMqttTransceiver;
import com.apextalos.cvitfusion.common.mqtt.message.Request;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicBuilder;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicParser;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
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
	
	public void requestConfig(String engineID, EngineConfigRequestGuiListener guiListener) {
		// create a new request with a new UUID
		Request request = new Request();
		
		// create a subscription for the response
		EngineConfigRequestSubscriptionExListener l = new EngineConfigRequestSubscriptionExListener(guiListener, engineID, request);
		subscribe(l.topic());
		addSubscriptionListener(l);
		
		// build the payload
		logger.debug("Requesting uuid: " + request.getUuid());
		String requestPayload;
		try {
			requestPayload = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
		} catch (JsonProcessingException e) {
			logger.error("Engine config request write failure" + e.getMessage());
			return;
		}
		
		// make the request
		String requestTopic = TopicBuilder.requestConfig(engineID);
		publish(requestTopic, requestPayload, false);
	}
	
	public void requestConfigComplete(String responseTopic) {
		// find the engine id from the request
		String engineID = TopicParser.getEngineID(responseTopic);
		if(engineID.isBlank()) {
			logger.error("Unable to complete the request because the engine ID could not be found");
			return;
		}
		
		// remove the subscription by topic
		EngineConfigRequestSubscriptionExListener l = new EngineConfigRequestSubscriptionExListener(null, engineID, null);
		unsubscribe(l.topic());
		removeSubscriptionListener(l);
	}
	
	public void saveConfig(String engineID, OperationalFlow of, EngineConfigRequestGuiListener guiListener) {
		// create a new request with a new UUID
		Request request = new Request();
		
		// create a subscription for the response
		EngineConfigRequestSubscriptionExListener l = new EngineConfigRequestSubscriptionExListener(guiListener, engineID, request);
		subscribe(l.topic());
		addSubscriptionListener(l);
		
		// build the payload
		logger.debug("Requesting uuid: " + request.getUuid());
		String requestPayload;
		try {
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
