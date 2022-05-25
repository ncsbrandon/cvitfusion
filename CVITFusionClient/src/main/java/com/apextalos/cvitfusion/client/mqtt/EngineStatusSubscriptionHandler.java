package com.apextalos.cvitfusion.client.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.mqtt.ISubscriptionHander;

public class EngineStatusSubscriptionHandler implements ISubscriptionHander {

	private static final Logger logger = LogManager.getLogger(EngineStatusSubscriptionHandler.class.getSimpleName());

	public EngineStatusSubscriptionHandler() {
	}

	@Override
	public String topic() {
		// listen for any engine status
		return "/apextalos/cvitfusion/status/#";
	}

	@Override
	public void onMessage(String payload) {
		logger.debug("incoming engine status");
	}

}
