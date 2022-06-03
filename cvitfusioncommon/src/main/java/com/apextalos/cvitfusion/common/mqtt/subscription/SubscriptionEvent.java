package com.apextalos.cvitfusion.common.mqtt.subscription;

public class SubscriptionEvent {

	private String objType;
	private Object obj;

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getObjType() {
		return objType;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

	public SubscriptionEvent(Object obj, String objType) {
		super();
		this.obj = obj;
		this.objType = objType;
	}
}
