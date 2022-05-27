package com.apextalos.cvitfusion.common.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.mqtt.subscription.ISubscriptionHander;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionListener;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicParser;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.settings.ConfigItems;
import com.apextalos.cvitfusion.common.thread.SimpleThread;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ConfigMqttTransceiver extends MqttTransceiver {

	private static Logger logger = LogManager.getLogger(ConfigMqttTransceiver.class.getSimpleName());

	protected ConfigFile cf;
	protected ObjectMapper mapper = new ObjectMapper();
	private SimpleThread statusTask;
	private ISubscriptionHander[] handlers;

	public ConfigMqttTransceiver(ConfigFile cf) {
		super(cf.getString(ConfigItems.CONFIG_MQTT_BROKER, ConfigItems.CONFIG_MQTT_BROKER_DEFAULT),
				cf.getString(ConfigItems.CONFIG_MQTT_CLIENTID, ConfigItems.CONFIG_MQTT_CLIENTID_DEFAULT));
		this.cf = cf;
	}

	public abstract String[] subscriptionTopics();

	public abstract ISubscriptionHander[] subscriptionHandlers(SubscriptionListener subscriptionListener);

	public abstract String statusTopic();

	public abstract String buildStatusPayload() throws JsonProcessingException;

	public void start(SubscriptionListener subscriptionListener) {
		// check for TLS certs
		setCerts(
				cf.getString(ConfigItems.CONFIG_MQTT_CACERT, ConfigItems.CONFIG_MQTT_CACERT_DEFAULT),
				cf.getString(ConfigItems.CONFIG_MQTT_CLIENTCERT, ConfigItems.CONFIG_MQTT_CLIENTCERT_DEFAULT),
				cf.getString(ConfigItems.CONFIG_MQTT_CLIENTKEY, ConfigItems.CONFIG_MQTT_USERNAME_DEFAULT)
				);

		// set user auth
		setUserAuth(cf.getString(ConfigItems.CONFIG_MQTT_USERNAME, ConfigItems.CONFIG_MQTT_USERNAME_DEFAULT),
				cf.getString(ConfigItems.CONFIG_MQTT_PASSWORD, ConfigItems.CONFIG_MQTT_PASSWORD_DEFAULT).toCharArray()
				);

		// connect
		if (!connect()) {
			logger.error("ConfigMqttTransceiver connection failed");
			return;
		}

		// subscriptions
		subscribe(subscriptionTopics());
		handlers = subscriptionHandlers(subscriptionListener);
		for (ISubscriptionHander handler : handlers) {
			subscribe(handler.topic());
		}

		// create the thread for periodic summary reports
		int freqsec = cf.getInt(ConfigItems.CONFIG_MQTT_PERIODIC_FREQ_SEC, ConfigItems.CONFIG_MQTT_PERIODIC_FREQ_SEC_DEFAULT);
		if (freqsec == 0) {
			logger.info("MQTT status reporting disabled");
			return;
		}

		// status thread
		statusTask = new SimpleThread() {
			@Override
			protected void running() {
				setName("Status publisher");
				while (!getStop()) {
					// publish periodic status message (with aggregated BSM data)
					try {
						publish(statusTopic(), buildStatusPayload(), true);
					} catch (JsonProcessingException e) {
						logger.error("Unable to build status payload: " + e.getMessage());
					}
					
					// wait until the next run, or interrupted
					if (stopDelay((long) 1000 * freqsec))
						break;
				}
			}
		};

		statusTask.start();
	}

	public void stop() {
		if (statusTask != null) {
			statusTask.setStopAndJoin(1000);
			statusTask = null;
		}

		disconnect(true);
	}

	@Override
	protected void incomingMessage(String topic, String payload) {
		for (ISubscriptionHander handler : handlers) {
			if (TopicParser.match(handler.topic(), topic)) {
				handler.onMessage(payload);
				return;
			}
		}
	}
}
