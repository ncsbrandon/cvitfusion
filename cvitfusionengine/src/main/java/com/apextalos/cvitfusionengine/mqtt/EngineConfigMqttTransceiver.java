package com.apextalos.cvitfusionengine.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.apextalos.cvitfusion.common.mqtt.ConfigMqttTransceiver;
import com.apextalos.cvitfusion.common.mqtt.message.Coordinate;
import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus;
import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus.LogLevel;
import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus.Mode;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicBuilder;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.settings.ConfigItems;
import com.apextalos.cvitfusionengine.app.Version;
import com.apextalos.cvitfusionengine.mqtt.subscription.EngineConfigRequestSubscriptionExListener;
import com.apextalos.cvitfusionengine.mqtt.subscription.EngineConfigSaveSubscriptionExListener;
import com.fasterxml.jackson.core.JsonProcessingException;

public class EngineConfigMqttTransceiver extends ConfigMqttTransceiver {

	private static final Logger logger = LogManager.getLogger(EngineConfigMqttTransceiver.class.getSimpleName());

	public EngineConfigMqttTransceiver(ConfigFile cf) {
		super(cf);
	}

	@Override
	public void start() {
		super.start();
		
		// listen for config requests
		logger.info("creating config request listener");
		EngineConfigRequestSubscriptionExListener request = new EngineConfigRequestSubscriptionExListener(cf, this);
		subscribe(request.topic());
		addSubscriptionListener(request);
		
		// listen for config saves
		logger.info("creating config save listener");
		EngineConfigSaveSubscriptionExListener save = new EngineConfigSaveSubscriptionExListener(cf, this);
		subscribe(save.topic());
		addSubscriptionListener(save);
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
