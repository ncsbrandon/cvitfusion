package com.apextalos.cvitfusion.common.mqtt.topics;

public class TopicBuilder {
	
	private TopicBuilder() {
		// prevent instance
	}

	public static String requestConfig(String engineID) {
		return String.format("/apextalos/cvitfusion/requestconfig/%s", engineID);
	}

	public static String respondConfig(String engineID) {
		return String.format("/apextalos/cvitfusion/respondconfig/%s", engineID);
	}

	public static String engineStatus(String engineID) {
		return String.format("/apextalos/cvitfusion/engineStatus/%s", engineID);
	}

	public static String engineStatusAny() {
		return "/apextalos/cvitfusion/engineStatus/#";
	}
}