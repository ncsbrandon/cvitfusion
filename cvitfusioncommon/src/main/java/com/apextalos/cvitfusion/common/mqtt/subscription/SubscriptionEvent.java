package com.apextalos.cvitfusion.common.mqtt.subscription;

import com.apextalos.cvitfusion.common.mqtt.topics.TopicDef;

public class SubscriptionEvent {

	private TopicDef topic;
	private Object obj;

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public TopicDef getTopic() {
		return topic;
	}

	public void setTopic(TopicDef topic) {
		this.topic = topic;
	}

	public SubscriptionEvent(Object obj, TopicDef topic) {
		super();
		this.obj = obj;
		this.topic = topic;
	}
}
