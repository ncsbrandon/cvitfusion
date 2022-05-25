package com.apextalos.cvitfusion.common.mqtt.subscription;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class SubscriptionHandler implements ISubscriptionHander {

	protected ObjectMapper mapper = new ObjectMapper();
	
	// the handler listener.
	// this passes an event in the backend to the frontend
	private SubscriptionListener subscriptionListener = null;

	public void setConnectionListener(SubscriptionListener subscriptionListener) {
		this.subscriptionListener = subscriptionListener;
	}

	public void subscriptionArrived(SubscriptionEvent subscriptionEvent) {
		if (subscriptionListener != null) {
			subscriptionListener.onSubscriptionArrived(subscriptionEvent);
		}
	}

	public abstract String topic();

	public abstract void onMessage(String payload);
}
