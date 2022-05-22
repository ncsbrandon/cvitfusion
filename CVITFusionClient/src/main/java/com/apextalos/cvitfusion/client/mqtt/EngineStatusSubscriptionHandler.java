package com.apextalos.cvitfusion.client.mqtt;

import com.apextalos.cvitfusion.common.mqtt.ISubscriptionHander;

public class EngineStatusSubscriptionHandler implements ISubscriptionHander {

	public EngineStatusSubscriptionHandler() {
	}

	@Override
	public String topic() {
		// listen for any engine status
		return "/apextalos/cvitfusion/status/#";
	}

	@Override
	public void onMessage(String payload) {
	}

}
