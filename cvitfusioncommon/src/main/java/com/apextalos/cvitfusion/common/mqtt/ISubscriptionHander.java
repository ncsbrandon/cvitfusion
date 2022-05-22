package com.apextalos.cvitfusion.common.mqtt;

public interface ISubscriptionHander {

	public String topic();

	public void onMessage(String payload);
}
