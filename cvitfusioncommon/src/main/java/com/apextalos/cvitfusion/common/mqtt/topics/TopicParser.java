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
		if(topic.contains(TopicBuilder.REQUEST_PROCESS_STATUS_TOPIC)) {
			topic = topic.replace(TopicBuilder.REQUEST_PROCESS_STATUS_TOPIC, "");
			int index = topic.indexOf('/');
			topic = topic.substring(0, index);
			return topic;
		}
		
		if(topic.contains(TopicBuilder.RESPOND_PROCESS_STATUS_TOPIC)) {
			topic = topic.replace(TopicBuilder.RESPOND_PROCESS_STATUS_TOPIC, "");
			int index = topic.indexOf('/');
			topic = topic.substring(0, index);
			return topic;
		}
		
		if(topic.contains(TopicBuilder.STOP_PROCESS_STATUS_TOPIC)) {
			topic = topic.replace(TopicBuilder.STOP_PROCESS_STATUS_TOPIC, "");
			int index = topic.indexOf('/');
			topic = topic.substring(0, index);
			return topic;
		}
		
		if(topic.contains(TopicBuilder.REQUEST_ENGINE_CONFIG_TOPIC))
			return topic.replace(TopicBuilder.REQUEST_ENGINE_CONFIG_TOPIC, "");
		
		if(topic.contains(TopicBuilder.RESPOND_ENGINE_CONFIG_TOPIC))
			return topic.replace(TopicBuilder.RESPOND_ENGINE_CONFIG_TOPIC, "");
		
		if(topic.contains(TopicBuilder.SAVE_ENGINE_CONFIG_TOPIC))
			return topic.replace(TopicBuilder.SAVE_ENGINE_CONFIG_TOPIC, "");
		
		if(topic.contains(TopicBuilder.RESULT_ENGINE_CONFIG_TOPIC))
			return topic.replace(TopicBuilder.RESULT_ENGINE_CONFIG_TOPIC, "");
		
		if(topic.contains(TopicBuilder.ENGINE_STATUS_TOPIC))
			return topic.replace(TopicBuilder.ENGINE_STATUS_TOPIC, "");
		
		return "";		
	}
	
	public static int getProcessID(String topic) {
		if(topic.contains(TopicBuilder.REQUEST_PROCESS_STATUS_TOPIC)) {
			int index = topic.lastIndexOf('/');
			topic = topic.substring(index+1);
			return Integer.valueOf(topic);
		}
		
		if(topic.contains(TopicBuilder.RESPOND_PROCESS_STATUS_TOPIC)) {
			int index = topic.lastIndexOf('/');
			topic = topic.substring(index+1);
			return Integer.valueOf(topic);
		}
		
		if(topic.contains(TopicBuilder.STOP_PROCESS_STATUS_TOPIC)) {
			int index = topic.lastIndexOf('/');
			topic = topic.substring(index+1);
			return Integer.valueOf(topic);
		}
		
		return -1;
	}
}
