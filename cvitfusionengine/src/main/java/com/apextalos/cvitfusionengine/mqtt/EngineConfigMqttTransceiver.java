package com.apextalos.cvitfusionengine.mqtt;

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
import com.apextalos.cvitfusionengine.mqtt.subscription.ConfigRequestSubscriptionExListener;
import com.fasterxml.jackson.core.JsonProcessingException;

public class EngineConfigMqttTransceiver extends ConfigMqttTransceiver {

	public EngineConfigMqttTransceiver(ConfigFile cf) {
		super(cf);
	}

	public void start() {
		super.start();
		
		addSubscriptionListener(new ConfigRequestSubscriptionExListener(cf));
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
