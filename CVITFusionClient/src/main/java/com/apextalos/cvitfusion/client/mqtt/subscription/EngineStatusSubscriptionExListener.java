package com.apextalos.cvitfusion.client.mqtt.subscription;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExEvent;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExListener;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EngineStatusSubscriptionExListener implements SubscriptionExListener {

	private static final Logger logger = LogManager.getLogger(EngineStatusSubscriptionExListener.class.getSimpleName());

	private EngineStatusGuiListener eventListener = null;
	protected ObjectMapper mapper = new ObjectMapper();
	
	public EngineStatusSubscriptionExListener(EngineStatusGuiListener eventListener) {
		this.eventListener = eventListener;
	}
	
	@Override
	public String topic() {
		// listen for any engine status
		return TopicBuilder.engineStatusAny();
	}

	@Override
	public void incomingMessage(SubscriptionExEvent se) {
		logger.debug("incoming engine status");
		
		// convert it from json to pojo
		EngineStatus engineStatus = null;
		try {
			engineStatus = mapper.readValue(se.getPayload(), EngineStatus.class);
		} catch (JsonProcessingException e) {
			logger.error("Engine status parsing failure" + e.getMessage());
			return;
		}
		
		if (eventListener != null) {
			eventListener.onEngineStatus(se.getTopic(), se.getPayload(), engineStatus);
		}
	}
}
