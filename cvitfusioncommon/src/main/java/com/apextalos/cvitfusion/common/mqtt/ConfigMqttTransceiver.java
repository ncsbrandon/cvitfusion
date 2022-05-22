package com.apextalos.cvitfusion.common.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.settings.ConfigItems;
import com.apextalos.cvitfusion.common.thread.SimpleThread;
import com.apextalos.cvitfusion.common.utils.SleepUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ConfigMqttTransceiver extends MqttTransceiver {

	private static Logger logger = LogManager.getLogger(ConfigMqttTransceiver.class.getSimpleName());

	protected ConfigFile cf;
	protected ObjectMapper mapper = new ObjectMapper();
	private SimpleThread statusTask;

	public ConfigMqttTransceiver(ConfigFile cf) {
		super(cf.getString(ConfigItems.CONFIG_MQTT_BROKER, ConfigItems.CONFIG_MQTT_BROKER_DEFAULT),
				cf.getString(ConfigItems.CONFIG_MQTT_CLIENTID, ConfigItems.CONFIG_MQTT_CLIENTID_DEFAULT));
		this.cf = cf;
	}
	
	public abstract String[] subscriptionTopics();
	public abstract String statusTopic();
	public abstract String buildStatusPayload() throws JsonProcessingException;

	public void start() {
		// check for TLS certs
		setCerts(
				cf.getString(ConfigItems.CONFIG_MQTT_CACERT, ConfigItems.CONFIG_MQTT_CACERT_DEFAULT),
				cf.getString(ConfigItems.CONFIG_MQTT_CLIENTCERT, ConfigItems.CONFIG_MQTT_CLIENTCERT_DEFAULT),
				cf.getString(ConfigItems.CONFIG_MQTT_CLIENTKEY, ConfigItems.CONFIG_MQTT_USERNAME_DEFAULT)
				);
		
		// set user auth
		setUserAuth(
				cf.getString(ConfigItems.CONFIG_MQTT_USERNAME, ConfigItems.CONFIG_MQTT_USERNAME_DEFAULT),
				cf.getString(ConfigItems.CONFIG_MQTT_PASSWORD, ConfigItems.CONFIG_MQTT_PASSWORD_DEFAULT).toCharArray()
				);
		
		// connect
		if (!connect()) {
			logger.error("ConfigMqttTransceiver connection failed");
			return;
		}

		// subscriptions
		subscribe(subscriptionTopics());
		
		// create the thread for periodic summary reports
		int freqsec = cf.getInt(ConfigItems.CONFIG_MQTT_PERIODIC_FREQ_SEC, ConfigItems.CONFIG_MQTT_PERIODIC_FREQ_SEC_DEFAULT);
		if(freqsec == 0) {
			logger.info("MQTT status reporting disabled");
			return;
		}
		
		// status thread
		statusTask = new SimpleThread() {
			@Override
			protected void running() {
				while (!getStop()) {
					// wait until the next run
					SleepUtils.safeSleep((long) 1000 * freqsec);
					
					// publish periodic status message (with aggregated BSM data)
					try {
						publish(statusTopic(), buildStatusPayload(), true);
					} catch (JsonProcessingException e) {
						logger.error("Unable to build status payload: " + e.getMessage());
					}
				}
			}
		};

		statusTask.start();
	}

	public void stop() {
		if(statusTask != null) {
			statusTask.setStop();
			statusTask = null;
		}
		
		disconnect();
	}
}
