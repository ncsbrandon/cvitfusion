package com.apextalos.cvitfusion.common.mqtt.topics;

public class TopicBuilder {
	
	public static final String REQUESTCONFIGTOPIC = "/apextalos/cvitfusion/requestconfig/";
	public static final String RESPONDCONFIGTOPIC = "/apextalos/cvitfusion/respondconfig/";
	public static final String ENGINESTATUSTOPIC = "/apextalos/cvitfusion/enginestatus/";
	
	private TopicBuilder() {
		// prevent instance
	}

	public static String requestConfig(String engineID) {
		return String.format("%s%s", REQUESTCONFIGTOPIC, engineID);
	}

	public static String respondConfig(String engineID) {
		return String.format("%s%s", RESPONDCONFIGTOPIC, engineID);
	}

	public static String engineStatus(String engineID) {
		return String.format("%s%s", ENGINESTATUSTOPIC, engineID);
	}

	public static String engineStatusAny() {
		return String.format("%s#", ENGINESTATUSTOPIC);
	}
}