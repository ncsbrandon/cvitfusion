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
import com.apextalos.cvitfusion.common.mqtt.message.ResponseEngineConfig;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExEvent;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExListener;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicBuilder;
import com.apextalos.cvitfusion.common.opflow.Color;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
import com.apextalos.cvitfusion.common.opflow.Process;
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
				ResponseEngineConfig response = new ResponseEngineConfig(request.getUuid());
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

		Process n111 = new Process(111, true, 3, null, "", 4317, new Properties());
		Process n112 = new Process(112, true, 6, null, "", 4317, new Properties());
		Process n121 = new Process(121, true, 8, null, "", 4317, new Properties());
		Process n122 = new Process(122, true, 3, null, "", 4317, new Properties());
		Process n12 = new Process(12, true, 2, new ArrayList<>() {
			{
				add(n121);
				add(n122);
			}
		}, "", 420, new Properties());
		Process n11 = new Process(11, true, 5, new ArrayList<>() {
			{
				add(n111);
				add(n112);
			}
		}, "", 420, new Properties());
		Process n1 = new Process(1, true, 1, new ArrayList<>() {
			{
				add(n11);
				add(n12);
			}
		}, "", 69, new Properties());
		activeDesign.getProcesses().add(n1);

		Process n211 = new Process(211, true, 6, null, "", 4317, new Properties());
		Process n21 = new Process(21, true, 7, new ArrayList<>() {
			{
				add(n211);
			}
		}, "", 420, new Properties());
		Process n2 = new Process(2, true, 4, new ArrayList<>() {
			{
				add(n21);
			}
		}, "", 69, new Properties());
		activeDesign.getProcesses().add(n2);

		// INPUTS----------------------
		activeDesign.getTypes().add(new Type(1, 1, "Lidar", new Properties(), null, new ArrayList<>() {
			{
				add(Integer.valueOf(2));
				add(Integer.valueOf(5));
				add(Integer.valueOf(7));
			}
		}, true));
		activeDesign.getTypes().add(new Type(4, 1, "Camera", new Properties(), null, new ArrayList<>() {
			{
				add(Integer.valueOf(2));
				add(Integer.valueOf(5));
				add(Integer.valueOf(7));
			}
		}, true));

		// LOGICS-------------------------
		activeDesign.getTypes().add(new Type(2, 1, "WWVD", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(1));
				add(Integer.valueOf(4));
			}
		}, new ArrayList<>() {
			{
				add(Integer.valueOf(3));
				add(Integer.valueOf(6));
				add(Integer.valueOf(8));
			}
		}, false));
		activeDesign.getTypes().add(new Type(5, 1, "Curve Speed", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(1));
				add(Integer.valueOf(4));
			}
		}, new ArrayList<>() {
			{
				add(Integer.valueOf(3));
				add(Integer.valueOf(6));
				add(Integer.valueOf(8));
			}
		}, false));
		activeDesign.getTypes().add(new Type(7, 1, "Queue Detection", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(1));
				add(Integer.valueOf(4));
			}
		}, new ArrayList<>() {
			{
				add(Integer.valueOf(3));
				add(Integer.valueOf(6));
				add(Integer.valueOf(8));
			}
		}, false));

		// OUTPUTS -------------
		activeDesign.getTypes().add(new Type(3, 1, "Email", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(2));
			}
		}, null, false));
		activeDesign.getTypes().add(new Type(6, 1, "Flashing Beacon", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(2));
			}
		}, null, false));
		activeDesign.getTypes().add(new Type(8, 1, "Digital Output", new Properties(), new ArrayList<>() {
			{
				add(Integer.valueOf(2));
			}
		}, null, false));

		// https://coolors.co/233d4d-915e3d-fe7f2d-fda53a-fcca46-cfc664-a1c181-619b8a
		activeDesign.getStyles().add(new Style(1, 1, new Color(0x23, 0x3D, 0x4D, 1), new Color(255, 255, 255, 1))); // Charcoal
		activeDesign.getStyles().add(new Style(2, 1, new Color(0x91, 0x5E, 0x3D, 1), new Color(255, 255, 255, 1))); // Coyote Brown
		activeDesign.getStyles().add(new Style(3, 1, new Color(0xFE, 0x7F, 0x2D, 1), new Color(255, 255, 255, 1))); // Pumpkin
		activeDesign.getStyles().add(new Style(4, 1, new Color(0xFD, 0xA5, 0x3A, 1), new Color(0, 0, 0, 1))); // Yellow Orange
		activeDesign.getStyles().add(new Style(5, 1, new Color(0xFC, 0xCA, 0x46, 1), new Color(0, 0, 0, 1))); // Sunglow
		activeDesign.getStyles().add(new Style(6, 1, new Color(0xCF, 0xC6, 0x64, 1), new Color(0, 0, 0, 1))); // Straw
		activeDesign.getStyles().add(new Style(7, 1, new Color(0xA1, 0xC1, 0x81, 1), new Color(0, 0, 0, 1))); // Olivine
		activeDesign.getStyles().add(new Style(8, 1, new Color(0x61, 0x9B, 0x8A, 1), new Color(0, 0, 0, 1))); // Polished Pine

		activeDesign.getTypeStyle().put(1, 1);
		activeDesign.getTypeStyle().put(2, 2);
		activeDesign.getTypeStyle().put(3, 3);
		activeDesign.getTypeStyle().put(4, 4);
		activeDesign.getTypeStyle().put(5, 5);
		activeDesign.getTypeStyle().put(6, 6);
		activeDesign.getTypeStyle().put(7, 7);
		activeDesign.getTypeStyle().put(8, 8);
		
		return activeDesign;
	}
}
