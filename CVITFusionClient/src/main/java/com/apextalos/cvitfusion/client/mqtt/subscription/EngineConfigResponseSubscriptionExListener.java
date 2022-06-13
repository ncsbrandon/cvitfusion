package com.apextalos.cvitfusion.client.mqtt.subscription;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.mqtt.message.Request;
import com.apextalos.cvitfusion.common.mqtt.message.EngineConfigResponse;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExEvent;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExListener;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EngineConfigResponseSubscriptionExListener implements SubscriptionExListener {

	private static final Logger logger = LogManager.getLogger(EngineConfigResponseSubscriptionExListener.class.getSimpleName());
	
	private EngineConfigResponseGuiListener guiListener;
	private String engineID;
	private Request request;
	
	public EngineConfigResponseSubscriptionExListener(EngineConfigResponseGuiListener guiListener, String engineID, Request request) {
		this.guiListener = guiListener;
		this.engineID = engineID;
		this.request = request;
	}
	
	@Override
	public String topic() {
		return TopicBuilder.respondConfig(engineID);
	}

	@Override
	public void incomingMessage(SubscriptionExEvent se) {
		logger.debug("incoming engine config response");
		
		ObjectMapper mapper = new ObjectMapper();
		
		// convert it from json to pojo
		EngineConfigResponse response = null;
		try {
			response = mapper.readValue(se.getPayload(), EngineConfigResponse.class);
		} catch (JsonProcessingException e) {
			logger.error("Engine config response parsing failure: " + e.getMessage());
			return;
		}

		// validate that it matches our request id and came in a timely manner
		if(0 != request.getUuid().compareToIgnoreCase(response.getUuid())) {
			logger.debug("Engine config response does not match request");
			return;
		}
		
		// pass to the GUI
		if (guiListener != null) {
			guiListener.onEngineConfigRequest(engineID, se.getTopic(), se.getPayload(), response.getData());
		}
	}
}
