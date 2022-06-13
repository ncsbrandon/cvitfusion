package com.apextalos.cvitfusionengine.mqtt.subscription;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.mqtt.MqttTransceiver;
import com.apextalos.cvitfusion.common.mqtt.message.EngineConfigResult;
import com.apextalos.cvitfusion.common.mqtt.message.EngineSaveRequest;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExEvent;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExListener;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicBuilder;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.settings.ConfigItems;
import com.apextalos.cvitfusionengine.app.DesignManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EngineConfigSaveSubscriptionExListener implements SubscriptionExListener {

private static final Logger logger = LogManager.getLogger(EngineConfigSaveSubscriptionExListener.class.getSimpleName());
	
	private ConfigFile cf;
	private MqttTransceiver mt;
	
	public EngineConfigSaveSubscriptionExListener(ConfigFile cf, MqttTransceiver mt) {
		this.cf = cf;
		this.mt = mt;
	}
	
	@Override
	public String topic() {
		return TopicBuilder.saveConfig(cf.getString(ConfigItems.DEVICE_UUID_CONFIG, ConfigItems.DEVICE_UUID_DEFAULT));
	}

	@Override
	public void incomingMessage(SubscriptionExEvent se) {
		logger.debug("incoming engine config save");
		
		ObjectMapper mapper = new ObjectMapper();
		
		// convert it from json to pojo
		EngineSaveRequest request = null;
		try {
			request = mapper.readValue(se.getPayload(), EngineSaveRequest.class);
		} catch (JsonProcessingException e) {
			logger.error("Engine config result parsing failure: " + e.getMessage());
			return;
		}

		DesignManager dm = DesignManager.getInstance();
		
		// now build a response
		EngineConfigResult result = new EngineConfigResult(request.getUuid());
		result.setSuccess(dm.setProcesses(request.getData().getProcesses(), cf));
	
		String resultPayload;
		try {
			resultPayload = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
		} catch (JsonProcessingException e) {
			logger.error("Engine config result write failure: " + e.getMessage());
			return;
		}
		
		// response topic
		String resultTopic = TopicBuilder.resultConfig(cf.getString(ConfigItems.DEVICE_UUID_CONFIG, ConfigItems.DEVICE_UUID_DEFAULT));
		
		// publish it
		mt.publish(resultTopic, resultPayload, false);
	}
}
