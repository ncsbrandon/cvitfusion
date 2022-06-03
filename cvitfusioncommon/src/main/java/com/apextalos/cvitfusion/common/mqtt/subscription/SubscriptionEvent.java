package com.apextalos.cvitfusion.common.mqtt.subscription;

public class SubscriptionEvent {

	private String objectType;
	private Object object;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getObectType() {
		return objectType;
	}

	public void setObectType(String objectType) {
		this.objectType = objectType;
	}

	public SubscriptionEvent(Object object, String objectType) {
		super();
		this.object = object;
		this.objectType = objectType;
	}
}
