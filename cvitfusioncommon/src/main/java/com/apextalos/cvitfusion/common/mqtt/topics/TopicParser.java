package com.apextalos.cvitfusion.common.mqtt.topics;

public class TopicParser {

	private TopicParser() {
		// prevent instances
	}
	
	public static boolean match(String topic1, String topic2) {
		if(topic1.endsWith("#")) {
			return topic2.replace("#", "").startsWith(topic1.replace("#", ""));
		}
		if(topic2.endsWith("#")) {
			return topic1.replace("#", "").startsWith(topic2.replace("#", ""));
		}
		
		return 0 == topic1.compareToIgnoreCase(topic2);
	}
}
