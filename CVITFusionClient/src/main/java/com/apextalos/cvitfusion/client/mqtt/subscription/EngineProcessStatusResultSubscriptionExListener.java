package com.apextalos.cvitfusion.client.mqtt.subscription;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.mqtt.message.EngineRequest;
import com.apextalos.cvitfusion.common.mqtt.message.ProcessStatusResponse;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExEvent;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExListener;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EngineProcessStatusResultSubscriptionExListener implements SubscriptionExListener {

	private static final Logger logger = LogManager.getLogger(EngineProcessStatusResultSubscriptionExListener.class.getSimpleName());

	private static final ObjectMapper mapper = new ObjectMapper();
	
	private EngineProcessStatusResponseGuiListener guiListener;
	private String engineID;
	private int processID;
	private EngineRequest request;
	
	public EngineProcessStatusResultSubscriptionExListener(EngineProcessStatusResponseGuiListener guiListener, int processID, String engineID, EngineRequest request) {
		this.guiListener = guiListener;
		this.processID = processID;
		this.engineID = engineID;
		this.request = request;
	}
	
	@Override
	public String topic() {
		return TopicBuilder.respondProcessStatus(engineID, processID);
	}

	@Override
	public void incomingMessage(SubscriptionExEvent se) {
		logger.debug("incoming process status response");
		
		// convert it from json to pojo
		ProcessStatusResponse response = null;
		try {
			response = mapper.readValue(se.getPayload(), ProcessStatusResponse.class);
		} catch (JsonProcessingException e) {
			logger.error("Process status response parsing failure: {}", e.getMessage());
			return;
		}

		/*
		// validate that it matches our request id and came in a timely manner
		if(0 != request.getUuid().compareToIgnoreCase(response.getUuid())) {
			logger.debug("Process status response does not match request {} {}", request.getUuid(), response.getUuid());
			return;
		}
		*/
		
		// pass to the GUI
		if (guiListener != null) {
			guiListener.onProcessStatusResponse(engineID, se.getTopic(), se.getPayload(), response.getContents());
		}
	}
	
}
