package com.apextalos.cvitfusionengine.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.apextalos.cvitfusion.common.engine.ProcessingEngine;
import com.apextalos.cvitfusion.common.mqtt.ConfigMqttTransceiver;
import com.apextalos.cvitfusion.common.mqtt.message.Coordinate;
import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus;
import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus.LogLevel;
import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus.Mode;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicBuilder;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.settings.ConfigItems;
import com.apextalos.cvitfusionengine.app.Version;
import com.apextalos.cvitfusionengine.mqtt.subscription.EngineConfigRequestSubscriptionExListener;
import com.apextalos.cvitfusionengine.mqtt.subscription.EngineConfigSaveSubscriptionExListener;
import com.apextalos.cvitfusionengine.mqtt.subscription.ProcessStatusRequestSubscriptionExListener;
import com.apextalos.cvitfusionengine.mqtt.subscription.ProcessStatusStopSubscriptionExListener;
import com.fasterxml.jackson.core.JsonProcessingException;

public class EngineConfigMqttTransceiver extends ConfigMqttTransceiver {

	private static final Logger logger = LogManager.getLogger(EngineConfigMqttTransceiver.class.getSimpleName());

	private OperationalFlow design;
	private ProcessingEngine pe;
	
	public EngineConfigMqttTransceiver(ConfigFile cf, OperationalFlow design, ProcessingEngine pe) {
		super(cf);
		this.design = design;
		this.pe = pe;
	}

	@Override
	public boolean start() {
		if(!super.start())
			return false;
		
		// listen for config requests
		logger.info("creating config request listener");
		EngineConfigRequestSubscriptionExListener engineConfigRequest = new EngineConfigRequestSubscriptionExListener(cf, this, design);
		subscribe(engineConfigRequest.topic());
		addSubscriptionListener(engineConfigRequest);
		
		// listen for config saves
		logger.info("creating config save listener");
		EngineConfigSaveSubscriptionExListener engineConfigSave = new EngineConfigSaveSubscriptionExListener(cf, this, design, pe);
		subscribe(engineConfigSave.topic());
		addSubscriptionListener(engineConfigSave);
		
		// listen for process status requests
		logger.info("creating process status request listener");
		ProcessStatusRequestSubscriptionExListener processStatusRequest = new ProcessStatusRequestSubscriptionExListener(cf, this, design, pe);
		subscribe(processStatusRequest.topic());
		addSubscriptionListener(processStatusRequest);
		
		// listen for process status requests
		logger.info("creating process status stop listener");
		ProcessStatusStopSubscriptionExListener processStatusStop = new ProcessStatusStopSubscriptionExListener(cf, this, design, pe);
		subscribe(processStatusStop.topic());
		addSubscriptionListener(processStatusStop);
		
		return true;
	}

	@Override
	public String statusTopic() {
		return TopicBuilder.engineStatus(cf.getString(ConfigItems.DEVICE_UUID_CONFIG, ConfigItems.DEVICE_UUID_DEFAULT));
	}

	@Override
	public String buildStatusPayload() throws JsonProcessingException {
		EngineStatus es = new EngineStatus(
			cf.getString(ConfigItems.DEVICE_UUID_CONFIG, ConfigItems.DEVICE_UUID_DEFAULT),
			DateTime.now().toString(),
			Mode.RUNNING,
			cf.getString(ConfigItems.DEVICE_LOCATION_CONFIG, ConfigItems.DEVICE_LOCATION_DEFAULT),
			Version.getInstance().getVersion(),
			LogLevel.UNKNOWN,
			new Coordinate(
				cf.getDouble(ConfigItems.DEVICE_LATITUDE_CONFIG, ConfigItems.DEVICE_LATITUDE_DEFAULT),
				cf.getDouble(ConfigItems.DEVICE_LONGITUDE_CONFIG, ConfigItems.DEVICE_LONGITUDE_DEFAULT),
				cf.getDouble(ConfigItems.DEVICE_ELEVATION_CONFIG, ConfigItems.DEVICE_ELEVATION_DEFAULT)
			)
		);

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(es);
	}
}
