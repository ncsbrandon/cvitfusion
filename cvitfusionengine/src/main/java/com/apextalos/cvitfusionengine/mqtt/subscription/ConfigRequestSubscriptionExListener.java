package com.apextalos.cvitfusionengine.mqtt.subscription;

import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExEvent;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExListener;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicBuilder;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.settings.ConfigItems;

public class ConfigRequestSubscriptionExListener implements SubscriptionExListener {

	private ConfigFile cf;

	public ConfigRequestSubscriptionExListener(ConfigFile cf) {
		this.cf = cf;
	}
	
	@Override
	public String topic() {
		return TopicBuilder.requestConfig(cf.getString(ConfigItems.DEVICE_UUID_CONFIG, ConfigItems.DEVICE_UUID_DEFAULT));
	}

	@Override
	public void incomingMessage(SubscriptionExEvent se) {
		// TODO Auto-generated method stub
	}
}
