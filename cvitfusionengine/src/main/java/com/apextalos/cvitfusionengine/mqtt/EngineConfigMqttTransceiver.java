package com.apextalos.cvitfusionengine.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.apextalos.cvitfusion.common.mqtt.ConfigMqttTransceiver;
import com.apextalos.cvitfusion.common.mqtt.message.Coordinate;
import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus;
import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus.LogLevel;
import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus.Mode;
import com.apextalos.cvitfusion.common.mqtt.message.Request;
import com.apextalos.cvitfusion.common.mqtt.message.Response;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExEvent;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExListener;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicBuilder;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.settings.ConfigItems;
import com.apextalos.cvitfusionengine.app.Version;
import com.fasterxml.jackson.core.JsonProcessingException;

public class EngineConfigMqttTransceiver extends ConfigMqttTransceiver {

	private static final Logger logger = LogManager.getLogger(EngineConfigMqttTransceiver.class.getSimpleName());

	public EngineConfigMqttTransceiver(ConfigFile cf) {
		super(cf);
	}

	public void start() {
		super.start();
		
		SubscriptionExListener l = new SubscriptionExListener() {
			
			@Override
			public String topic() {
				return TopicBuilder.requestConfig(cf.getString(ConfigItems.DEVICE_UUID_CONFIG, ConfigItems.DEVICE_UUID_DEFAULT));
			}
			
			@Override
			public void incomingMessage(SubscriptionExEvent se) {
				
				// convert it from json to pojo
				Request request = null;
				try {
					request = mapper.readValue(se.getPayload(), Request.class);
				} catch (JsonProcessingException e) {
					logger.error("Engine config request parsing failure" + e.getMessage());
					return;
				}
				
				// now build a response
				Response response = new Response(request.getUuid());
				response.setData(new OperationalFlow(null, null, null, null));
				
				String reponsePayload;
				try {
					reponsePayload = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
				} catch (JsonProcessingException e) {
					logger.error("Engine config request write failure" + e.getMessage());
					return;
				}
				
				// response topic
				String repsonseTopic = TopicBuilder.respondConfig(cf.getString(ConfigItems.DEVICE_UUID_CONFIG, ConfigItems.DEVICE_UUID_DEFAULT));
				
				// publish it
				publish(repsonseTopic, reponsePayload, false);
			}
		};
		
		subscribe(l.topic());
		addSubscriptionListener(l);
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
