package com.apextalos.cvitfusion.common.mqtt.subscription;

public class SubscriptionEvent {

	private Object obj;

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public SubscriptionEvent(Object obj) {
		super();
		this.obj = obj;
	}
}
