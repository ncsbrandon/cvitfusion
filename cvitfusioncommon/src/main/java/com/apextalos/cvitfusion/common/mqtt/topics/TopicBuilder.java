package com.apextalos.cvitfusion.common.mqtt.topics;

public class TopicBuilder {

	public static final String REQUEST_PROCESS_STATUS_TOPIC = "/apextalos/cvitfusion/request_process_status/";
	public static final String RESPOND_PROCESS_STATUS_TOPIC = "/apextalos/cvitfusion/respond_process_status/";
	public static final String STOP_PROCESS_STATUS_TOPIC = "/apextalos/cvitfusion/stop_process_status/";

	public static final String REQUEST_ENGINE_CONFIG_TOPIC = "/apextalos/cvitfusion/request_engine_config/";
	public static final String RESPOND_ENGINE_CONFIG_TOPIC = "/apextalos/cvitfusion/respond_engine_config/";

	public static final String SAVE_ENGINE_CONFIG_TOPIC = "/apextalos/cvitfusion/save_engine_config/";
	public static final String RESULT_ENGINE_CONFIG_TOPIC = "/apextalos/cvitfusion/result_engine_config/";

	public static final String ENGINE_STATUS_TOPIC = "/apextalos/cvitfusion/enginestatus/";

	private TopicBuilder() {
		// prevent instance
	}

	public static String requestProcessStatusAny(String engineID) {
		return String.format("%s%s/#", REQUEST_PROCESS_STATUS_TOPIC, engineID);
	}

	public static String requestProcessStatus(String engineID, int processID) {
		return String.format("%s%s/%d", REQUEST_PROCESS_STATUS_TOPIC, engineID, processID);
	}

	public static String stopProcessStatusAny(String engineID) {
		return String.format("%s%s/#", STOP_PROCESS_STATUS_TOPIC, engineID);
	}

	public static String stopProcessStatus(String engineID, int processID) {
		return String.format("%s%s/%d", STOP_PROCESS_STATUS_TOPIC, engineID, processID);
	}

	public static String respondProcessStatus(String engineID, int processID) {
		return String.format("%s%s/%d", RESPOND_PROCESS_STATUS_TOPIC, engineID, processID);
	}

	public static String requestConfig(String engineID) {
		return String.format("%s%s", REQUEST_ENGINE_CONFIG_TOPIC, engineID);
	}

	public static String respondConfig(String engineID) {
		return String.format("%s%s", RESPOND_ENGINE_CONFIG_TOPIC, engineID);
	}

	public static String saveConfig(String engineID) {
		return String.format("%s%s", SAVE_ENGINE_CONFIG_TOPIC, engineID);
	}

	public static String resultConfig(String engineID) {
		return String.format("%s%s", RESULT_ENGINE_CONFIG_TOPIC, engineID);
	}

	public static String engineStatus(String engineID) {
		return String.format("%s%s", ENGINE_STATUS_TOPIC, engineID);
	}

	public static String engineStatusAny() {
		return String.format("%s#", ENGINE_STATUS_TOPIC);
	}
}