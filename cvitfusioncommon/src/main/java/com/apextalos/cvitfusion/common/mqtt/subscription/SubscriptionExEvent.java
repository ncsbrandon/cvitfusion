package com.apextalos.cvitfusion.common.mqtt.subscription;

public class SubscriptionExEvent {

	private String topic;
	private String payload;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public SubscriptionExEvent(String topic, String payload) {
		super();
		this.topic = topic;
		this.payload = payload;
	}
}
