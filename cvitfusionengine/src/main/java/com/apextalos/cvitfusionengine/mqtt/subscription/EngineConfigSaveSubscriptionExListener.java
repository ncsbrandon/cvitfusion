package com.apextalos.cvitfusionengine.mqtt.subscription;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.design.DesignManager;
import com.apextalos.cvitfusion.common.engine.ProcessingEngine;
import com.apextalos.cvitfusion.common.mqtt.MqttTransceiver;
import com.apextalos.cvitfusion.common.mqtt.message.EngineConfigResult;
import com.apextalos.cvitfusion.common.mqtt.message.EngineSaveRequest;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExEvent;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExListener;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicBuilder;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.settings.ConfigItems;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EngineConfigSaveSubscriptionExListener implements SubscriptionExListener {

	private static final Logger logger = LogManager.getLogger(EngineConfigSaveSubscriptionExListener.class.getSimpleName());
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private ConfigFile cf;
	private MqttTransceiver mt;
	private OperationalFlow design;
	private ProcessingEngine pe;
	
	public EngineConfigSaveSubscriptionExListener(ConfigFile cf, MqttTransceiver mt, OperationalFlow design, ProcessingEngine pe) {
		this.cf = cf;
		this.mt = mt;
		this.design = design;
		this.pe = pe;
	}
	
	@Override
	public String topic() {
		return TopicBuilder.saveConfig(cf.getString(ConfigItems.DEVICE_UUID_CONFIG, ConfigItems.DEVICE_UUID_DEFAULT));
	}

	@Override
	public void incomingMessage(SubscriptionExEvent se) {
		logger.debug("incoming engine config save");
		
		// convert it from json to pojo
		EngineSaveRequest request = null;
		try {
			request = mapper.readValue(se.getPayload(), EngineSaveRequest.class);
		} catch (JsonProcessingException e) {
			logger.error("Engine config result parsing failure: {}", e.getMessage());
			return;
		}

		design.setProcesses(request.getData().getProcesses());
		design.setStyles(request.getData().getStyles());
		design.setTypes(request.getData().getTypes());
		design.setTypeStyleMap(request.getData().getTypeStyleMap());
		
		// now build a response
		EngineConfigResult configResult = new EngineConfigResult(request.getUuid());
				
		// decode and set in the config
		DesignManager dm = DesignManager.getInstance();
		configResult.setSuccess(dm.setProcessesInConfig(design.getProcesses(), cf));
		
		// write to disk
		cf.save();
		
		// restart the engine
		pe.start(mt);
	
		// payload
		String resultPayload;
		try {
			resultPayload = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(configResult);
		} catch (JsonProcessingException e) {
			logger.error("Engine config result write failure: {}", e.getMessage());
			return;
		}
		
		// response topic
		String resultTopic = TopicBuilder.resultConfig(cf.getString(ConfigItems.DEVICE_UUID_CONFIG, ConfigItems.DEVICE_UUID_DEFAULT));
		
		// publish it
		mt.publish(resultTopic, resultPayload, false);
	}
}
