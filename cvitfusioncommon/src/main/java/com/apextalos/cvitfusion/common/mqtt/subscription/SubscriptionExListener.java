package com.apextalos.cvitfusion.common.mqtt.subscription;

public interface SubscriptionExListener {
	public String topic();
	public void incomingMessage(SubscriptionExEvent se);
}
