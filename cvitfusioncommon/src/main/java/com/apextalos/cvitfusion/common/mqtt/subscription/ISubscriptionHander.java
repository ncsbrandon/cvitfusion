package com.apextalos.cvitfusion.common.mqtt.subscription;

public interface ISubscriptionHander {

	public String topic();

	public void onMessage(String payload);
}
