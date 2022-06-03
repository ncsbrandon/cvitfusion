package com.apextalos.cvitfusionengine.mqtt;

import com.apextalos.cvitfusion.common.mqtt.subscription.ISubscriptionHander;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.settings.ConfigItems;

public class ConfigRequestSubscriptionHandler implements ISubscriptionHander {

	private ConfigFile cf;

	public ConfigRequestSubscriptionHandler(ConfigFile cf) {
		this.cf = cf;
	}

	@Override
	public String topic() {
		return String.format("/apextalos/cvitfusion/requestconfig/%s",
				cf.getString(ConfigItems.DEVICE_UUID_CONFIG, ConfigItems.DEVICE_UUID_DEFAULT));
	}

	@Override
	public void onMessage(String payload) {
		// TBD
	}

}
