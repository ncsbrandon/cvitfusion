package com.apextalos.cvitfusionengine.mqtt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.apextalos.cvitfusion.common.mqtt.ConfigMqttTransceiver;
import com.apextalos.cvitfusion.common.mqtt.message.Coordinate;
import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus;
import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus.LogLevel;
import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus.Mode;
import com.apextalos.cvitfusion.common.mqtt.message.Request;
import com.apextalos.cvitfusion.common.mqtt.message.EngineConfigResponse;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExEvent;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExListener;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicBuilder;
import com.apextalos.cvitfusion.common.opflow.Color;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
import com.apextalos.cvitfusion.common.opflow.Style;
import com.apextalos.cvitfusion.common.opflow.Type;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.settings.ConfigItems;
import com.apextalos.cvitfusionengine.app.Version;
import com.fasterxml.jackson.core.JsonProcessingException;

public class EngineConfigMqttTransceiver extends ConfigMqttTransceiver {

	private static final Logger logger = LogManager.getLogger(EngineConfigMqttTransceiver.class.getSimpleName());

	public EngineConfigMqttTransceiver(ConfigFile cf) {
		super(cf);
	}

	@Override
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
					logger.error("Engine config request parsing failure: " + e.getMessage());
					return;
				}
				
				// now build a response
				EngineConfigResponse response = new EngineConfigResponse(request.getUuid());
				response.setData(sample1());
				
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
				publish(repsonseTopic, responsePayload, false);
			}
		};
		
		// listen for config requests
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
	
	public OperationalFlow sample1() {
		OperationalFlow activeDesign = new OperationalFlow(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashMap<>());

		/*
		Process n11100 = new Process(11100, true, 3, null, "", 4317, new Properties());
		Process n11200 = new Process(11200, true, 6, null, "", 4317, new Properties());
		Process n12100 = new Process(12100, true, 8, null, "", 4317, new Properties());
		Process n12200 = new Process(12200, true, 3, null, "", 4317, new Properties());
		Process n1200 = new Process(1200, true, 2, new ArrayList<>() {
			{
				add(n12100);
				add(n12200);
			}
		}, "", 420, new Properties());
		Process n1100 = new Process(1100, true, 5, new ArrayList<>() {
			{
				add(n11100);
				add(n11200);
			}
		}, "", 420, new Properties());
		Process n100 = new Process(100, true, 1, new ArrayList<>() {
			{
				add(n1100);
				add(n1200);
			}
		}, "", 69, new Properties());
		activeDesign.getProcesses().add(n100);

		Process n21100 = new Process(21100, true, 6, null, "", 4317, new Properties());
		Process n2100 = new Process(2100, true, 7, new ArrayList<>() {
			{
				add(n21100);
			}
		}, "", 420, new Properties());
		Process n200 = new Process(200, true, 4, new ArrayList<>() {
			{
				add(n2100);
			}
		}, "", 69, new Properties());
		activeDesign.getProcesses().add(n200);
		*/
		
		// INPUTS----------------------
		activeDesign.getTypes().add(new Type(1, 1, "Lidar", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(11));
				add(Integer.valueOf(12));
				add(Integer.valueOf(13));
				add(Integer.valueOf(14));
				add(Integer.valueOf(15));
			}
		}, true));
		activeDesign.getTypes().add(new Type(2, 1, "Camera", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(11));
				add(Integer.valueOf(12));
				add(Integer.valueOf(13));
			}
		}, true));
		activeDesign.getTypes().add(new Type(3, 1, "Radar", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(14));
				add(Integer.valueOf(15));
			}
		}, true));
		activeDesign.getTypes().add(new Type(4, 1, "BSM", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(11));
				add(Integer.valueOf(12));
				add(Integer.valueOf(13));
				add(Integer.valueOf(14));
				add(Integer.valueOf(15));
			}
		}, true));
		
		// LOGICS-------------------------
		activeDesign.getTypes().add(new Type(11, 1, "WWVD", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(100));
				add(Integer.valueOf(101));
				add(Integer.valueOf(102));
			}
		}, false));
		activeDesign.getTypes().add(new Type(12, 1, "Curve Speed", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(101));
				add(Integer.valueOf(102));
				add(Integer.valueOf(103));
			}
		}, false));
		activeDesign.getTypes().add(new Type(13, 1, "Queue Detection", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(102));
				add(Integer.valueOf(103));
				add(Integer.valueOf(104));
			}
		}, false));
		activeDesign.getTypes().add(new Type(14, 1, "VRU", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(103));
				add(Integer.valueOf(104));
				add(Integer.valueOf(105));
			}
		}, false));
		activeDesign.getTypes().add(new Type(15, 1, "Stopbar", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(105));
				add(Integer.valueOf(106));
				add(Integer.valueOf(107));
			}
		}, false));

		// OUTPUTS -------------
		activeDesign.getTypes().add(new Type(100, 1, "Email", new Properties(), null, false));
		activeDesign.getTypes().add(new Type(101, 1, "Flashing Beacon", new Properties(), null, false));
		activeDesign.getTypes().add(new Type(102, 1, "Digital Output", new Properties(), null, false));
		activeDesign.getTypes().add(new Type(103, 1, "GPIO", new Properties(), null, false));
		activeDesign.getTypes().add(new Type(104, 1, "RSU", new Properties(), null, false));
		activeDesign.getTypes().add(new Type(105, 1, "VMS", new Properties(), null, false));
		activeDesign.getTypes().add(new Type(106, 1, "SDLC", new Properties(), null, false));
		activeDesign.getTypes().add(new Type(107, 1, "MQTT", new Properties(), null, false));

		// https://coolors.co/233d4d-915e3d-fe7f2d-fda53a-fcca46-cfc664-a1c181-619b8a
		activeDesign.getStyles().add(new Style(1, 1, new Color(0x23, 0x3D, 0x4D, 1), new Color(255, 255, 255, 1))); // Charcoal w/ White
		activeDesign.getStyles().add(new Style(2, 1, new Color(0x91, 0x5E, 0x3D, 1), new Color(255, 255, 255, 1))); // Coyote Brown w/ White
		activeDesign.getStyles().add(new Style(3, 1, new Color(0xFE, 0x7F, 0x2D, 1), new Color(255, 255, 255, 1))); // Pumpkin w/ White
		activeDesign.getStyles().add(new Style(4, 1, new Color(0xFD, 0xA5, 0x3A, 1), new Color(0, 0, 0, 1))); // Yellow Orange w/ Black
		activeDesign.getStyles().add(new Style(5, 1, new Color(0xFC, 0xCA, 0x46, 1), new Color(0, 0, 0, 1))); // Sunglow w/ Black
		activeDesign.getStyles().add(new Style(6, 1, new Color(0xCF, 0xC6, 0x64, 1), new Color(0, 0, 0, 1))); // Straw w/ Black
		activeDesign.getStyles().add(new Style(7, 1, new Color(0xA1, 0xC1, 0x81, 1), new Color(0, 0, 0, 1))); // Olivine w/ Black
		activeDesign.getStyles().add(new Style(8, 1, new Color(0x61, 0x9B, 0x8A, 1), new Color(0, 0, 0, 1))); // Polished Pine w/ Black

		activeDesign.getTypeStyleMap().put(1, 1);
		activeDesign.getTypeStyleMap().put(2, 2);
		activeDesign.getTypeStyleMap().put(3, 1);
		activeDesign.getTypeStyleMap().put(4, 2);
		
		activeDesign.getTypeStyleMap().put(11, 3);
		activeDesign.getTypeStyleMap().put(12, 4);
		activeDesign.getTypeStyleMap().put(13, 5);
		activeDesign.getTypeStyleMap().put(14, 3);
		activeDesign.getTypeStyleMap().put(15, 4);

		activeDesign.getTypeStyleMap().put(100, 6);
		activeDesign.getTypeStyleMap().put(101, 7);
		activeDesign.getTypeStyleMap().put(102, 8);
		activeDesign.getTypeStyleMap().put(103, 6);
		activeDesign.getTypeStyleMap().put(104, 7);
		activeDesign.getTypeStyleMap().put(105, 8);
		activeDesign.getTypeStyleMap().put(106, 6);
		activeDesign.getTypeStyleMap().put(107, 7);
		
		return activeDesign;
	}
}
