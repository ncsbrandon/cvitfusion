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
	
	public static String getEngineID(String topic) {
		if(topic.contains(TopicBuilder.RESPONDCONFIGTOPIC))
			return topic.replace(TopicBuilder.RESPONDCONFIGTOPIC, "");
		
		if(topic.contains(TopicBuilder.REQUESTCONFIGTOPIC))
			return topic.replace(TopicBuilder.REQUESTCONFIGTOPIC, "");
		
		if(topic.contains(TopicBuilder.ENGINESTATUSTOPIC))
			return topic.replace(TopicBuilder.ENGINESTATUSTOPIC, "");
		
		return "";		
	}
}
