package com.apextalos.cvitfusionengine.mqtt.subscription;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.mqtt.MqttTransceiver;
import com.apextalos.cvitfusion.common.mqtt.message.EngineConfigResponse;
import com.apextalos.cvitfusion.common.mqtt.message.EngineRequest;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExEvent;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExListener;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicBuilder;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.settings.ConfigItems;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EngineConfigRequestSubscriptionExListener implements SubscriptionExListener {

	private static final Logger logger = LogManager.getLogger(EngineConfigRequestSubscriptionExListener.class.getSimpleName());

	private static final ObjectMapper mapper = new ObjectMapper();
	
	private ConfigFile cf;
	private MqttTransceiver mt;
	private OperationalFlow design;
	
	public EngineConfigRequestSubscriptionExListener(ConfigFile cf, MqttTransceiver mt, OperationalFlow design) {
		this.cf = cf;
		this.mt = mt;
		this.design = design;
	}
	
	@Override
	public String topic() {
		return TopicBuilder.requestConfig(cf.getString(ConfigItems.DEVICE_UUID_CONFIG, ConfigItems.DEVICE_UUID_DEFAULT));
	}

	@Override
	public void incomingMessage(SubscriptionExEvent se) {
		logger.debug("incoming engine config request");
		
		// convert it from json to pojo
		EngineRequest request = null;
		try {
			request = mapper.readValue(se.getPayload(), EngineRequest.class);
		} catch (JsonProcessingException e) {
			logger.error("Engine config request parsing failure: " + e.getMessage());
			return;
		}
		
		// now build a response
		EngineConfigResponse response = new EngineConfigResponse(request.getUuid());
		response.setData(design);
		
		String responsePayload;
		try {
			responsePayload = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
		} catch (JsonProcessingException e) {
			logger.error("Engine config response write failure: " + e.getMessage());
			return;
		}
		
		// response topic
		String repsonseTopic = TopicBuilder.respondConfig(cf.getString(ConfigItems.DEVICE_UUID_CONFIG, ConfigItems.DEVICE_UUID_DEFAULT));
		
		// publish it
		mt.publish(repsonseTopic, responsePayload, false);
	}
}
