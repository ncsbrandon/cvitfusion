package com.apextalos.cvitfusion.client.mqtt.subscription;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.mqtt.message.EngineConfigResult;
import com.apextalos.cvitfusion.common.mqtt.message.Request;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExEvent;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExListener;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EngineConfigResultSubscriptionExListener implements SubscriptionExListener {

	private static final Logger logger = LogManager.getLogger(EngineConfigResultSubscriptionExListener.class.getSimpleName());
	
	private EngineConfigResultGuiListener guiListener;
	private String engineID;
	private Request request;
	
	public EngineConfigResultSubscriptionExListener(EngineConfigResultGuiListener guiListener, String engineID, Request request) {
		this.guiListener = guiListener;
		this.engineID = engineID;
		this.request = request;
	}
	
	@Override
	public String topic() {
		return TopicBuilder.resultConfig(engineID);
	}

	@Override
	public void incomingMessage(SubscriptionExEvent se) {
		logger.debug("incoming engine config result");
		
		ObjectMapper mapper = new ObjectMapper();
		
		// convert it from json to pojo
		EngineConfigResult result = null;
		try {
			result = mapper.readValue(se.getPayload(), EngineConfigResult.class);
		} catch (JsonProcessingException e) {
			logger.error("Engine config result parsing failure: " + e.getMessage());
			return;
		}

		// validate that it matches our request id and came in a timely manner
		if(0 != request.getUuid().compareToIgnoreCase(result.getUuid())) {
			logger.debug("Engine config result does not match request");
			return;
		}
				
		// pass to the GUI
		if (guiListener != null) {
			guiListener.onEngineConfigSave(engineID, se.getTopic(), se.getPayload(), result.getSuccess());
		}
	}
}
