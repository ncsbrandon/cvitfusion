package com.apextalos.cvitfusionengine.mqtt.subscription;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.engine.ProcessingEngine;
import com.apextalos.cvitfusion.common.engine.Processor;
import com.apextalos.cvitfusion.common.mqtt.MqttTransceiver;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExEvent;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExListener;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicBuilder;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicParser;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.settings.ConfigItems;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProcessStatusStopSubscriptionExListener implements SubscriptionExListener{

	private static final Logger logger = LogManager.getLogger(ProcessStatusRequestSubscriptionExListener.class.getSimpleName());

	private static final ObjectMapper mapper = new ObjectMapper();
	
	private ConfigFile cf;
	private MqttTransceiver mt;
	private OperationalFlow design;
	private ProcessingEngine pe;
	
	public ProcessStatusStopSubscriptionExListener(ConfigFile cf, MqttTransceiver mt, OperationalFlow design, ProcessingEngine pe) {
		this.cf = cf;
		this.mt = mt;
		this.design = design;
		this.pe = pe;
	}

	@Override
	public String topic() {
		return TopicBuilder.stopProcessStatusAny(cf.getString(ConfigItems.DEVICE_UUID_CONFIG, ConfigItems.DEVICE_UUID_DEFAULT));
	}

	@Override
	public void incomingMessage(SubscriptionExEvent se) {
		int processID = TopicParser.getProcessID(se.getTopic());
		logger.debug("incoming process [{}] status stop", processID);
		
		Processor processor = pe.getProcessor(processID);
		if(processor == null) {
			logger.error("unable to find process {}", processID);
			return;
		}
		
		processor.setPublishingStatus(false);
	}
}
