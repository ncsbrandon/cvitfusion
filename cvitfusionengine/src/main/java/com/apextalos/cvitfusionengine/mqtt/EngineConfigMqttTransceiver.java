package com.apextalos.cvitfusionengine.mqtt;

import org.joda.time.DateTime;

import com.apextalos.cvitfusion.common.mqtt.ConfigMqttTransceiver;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.settings.ConfigItems;
import com.apextalos.cvitfusionengine.app.Version;
import com.apextalos.cvitfusionengine.mqtt.messages.Coordinate;
import com.apextalos.cvitfusionengine.mqtt.messages.EngineStatus;
import com.apextalos.cvitfusionengine.mqtt.messages.EngineStatus.LogLevel;
import com.apextalos.cvitfusionengine.mqtt.messages.EngineStatus.Mode;
import com.fasterxml.jackson.core.JsonProcessingException;

public class EngineConfigMqttTransceiver extends ConfigMqttTransceiver {

	public EngineConfigMqttTransceiver(ConfigFile cf) {
		super(cf);
	}

	@Override
	public String[] subscriptionTopics() {
		String[] subscriptions = new String[1];
		subscriptions[0] = String.format("/apextalos/cvitfusion/requestconfig/%s",
				cf.getString(ConfigItems.DEVICE_UUID_CONFIG, ConfigItems.DEVICE_UUID_DEFAULT));
		return subscriptions;
	}

	@Override
	protected void incomingMessage(String topic, String payload) {
		// TODO Auto-generated method stub

	}

	@Override
	public String statusTopic() {
		return String.format("/apextalos/cvitfusion/status/%s",
				cf.getString(ConfigItems.DEVICE_UUID_CONFIG, ConfigItems.DEVICE_UUID_DEFAULT));
	}

	@Override
	public String buildStatusPayload() throws JsonProcessingException {
		EngineStatus es = new EngineStatus(
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
