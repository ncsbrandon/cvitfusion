package com.apextalos.cvitfusion.client.mqtt.subscription;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionEvent;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionHandler;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionListener;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicDef;
import com.fasterxml.jackson.core.JsonProcessingException;

import javafx.application.Platform;

public class EngineStatusSubscriptionHandler extends SubscriptionHandler {

	private static final Logger logger = LogManager.getLogger(EngineStatusSubscriptionHandler.class.getSimpleName());

	public EngineStatusSubscriptionHandler(SubscriptionListener subscriptionListener) {
		setConnectionListener(subscriptionListener);
	}

	@Override
	public String topic() {
		// listen for any engine status
		return "/apextalos/cvitfusion/" + TopicDef.ENGINE_STATUS + "/#";
	}

	@Override
	public void onMessage(String payload) {
		logger.debug("incoming engine status");
		
		// convert it from json to pojo
		EngineStatus engineStatus = null;
		try {
			engineStatus = mapper.readValue(payload, EngineStatus.class);
		} catch (JsonProcessingException e) {
			logger.error("Engine status parsing failure" + e.getMessage());
			return;
		}
		
		// switch threads
		postEngineStatusToFXThread(engineStatus);
	}
	
	private void postEngineStatusToFXThread(final EngineStatus engineStatus) {
		if(!Platform.isFxApplicationThread()) {
			Platform.runLater(new Runnable() {
                @Override public void run() {
                	// publish to the GUI
            		subscriptionArrived(new SubscriptionEvent(engineStatus, TopicDef.ENGINE_STATUS));
                }
			});
		} else {
			subscriptionArrived(new SubscriptionEvent(engineStatus, TopicDef.ENGINE_STATUS));
		}
	}
}
