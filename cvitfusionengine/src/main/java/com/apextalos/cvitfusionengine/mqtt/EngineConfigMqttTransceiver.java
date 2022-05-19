package com.apextalos.cvitfusionengine.mqtt;

import com.apextalos.cvitfusion.common.mqtt.ConfigMqttTransceiver;
import com.apextalos.cvitfusion.common.settings.ConfigFile;

public class EngineConfigMqttTransceiver extends ConfigMqttTransceiver {

	public EngineConfigMqttTransceiver(ConfigFile cf) {
		super(cf);
	}

	@Override
	protected void incomingMessage(String topic, String payload) {
		// TODO Auto-generated method stub

	}

}
