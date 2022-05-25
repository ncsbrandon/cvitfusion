package com.apextalos.cvitfusion.common.mqtt.subscription;

import java.util.EventListener;

public interface SubscriptionListener extends EventListener {
	
	public void onSubscriptionArrived(SubscriptionEvent subscriptionEvent);
}
