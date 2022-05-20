package com.apextalos.cvitfusion.common.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.thread.SimpleThread;
import com.apextalos.cvitfusion.common.utils.SleepUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ConfigMqttTransceiver extends MqttTransceiver {

	private static Logger logger = LogManager.getLogger(ConfigMqttTransceiver.class.getSimpleName());

	// topics
	public static final String TOPIC_VERSION_1 = "v1";
	public static final String TOPIC_CONFIG = "CONFIG";
	public static final String TOPIC_STATUS = "STATUS";
	public static final String TOPIC_GPIO = "GPIO";
	public static final String TOPIC_SUMMARY = "SUMMARY";
	public static final String TOPIC_BSM = "BSM";
	public static final String TOPIC_WRONGWAY = "WRONGWAY";
	public static final String TOPIC_PED = "PED";
	public static final String TOPIC_COLLISION = "COLLISION";
	public static final String TOPIC_LOWVIS = "LOWVIS";
	public static final String TOPIC_HIGHWIND = "HIGHWIND";
	public static final String TOPIC_QUEUE = "QUEUE";
	public static final String TOPIC_AVGSPEED = "AVGSPEED";
	public static final String TOPIC_TRAIN = "TRAIN";
	public static final String TOPIC_WORKZONE = "WORKZONE";
	public static final String TOPIC_SECURITY = "SECURITY";
	public static final String TOPIC_SURFACE = "SURFACE";
	public static final String TOPIC_OCCUPANCY = "OCCUPANCY";
	public static final String TOPIC_DEACTIVATE = "DEACTIVATE";
	public static final String TOPIC_TEST = "TEST";
	
	// broker settings
	public static final String CONFIG_MQTT_CACERT_DESC = "";
	public static final String CONFIG_MQTT_CACERT = "MQTT_CACERT";
	public static final String CONFIG_MQTT_CACERT_DEFAULT = "ca-cert.pem";
	public static final String CONFIG_MQTT_CLIENTCERT_DESC = "";
	public static final String CONFIG_MQTT_CLIENTCERT = "MQTT_CLIENTCERT";
	public static final String CONFIG_MQTT_CLIENTCERT_DEFAULT = "server-cert.pem";
	public static final String CONFIG_MQTT_CLIENTKEY_DESC = "";
	public static final String CONFIG_MQTT_CLIENTKEY = "MQTT_CLIENTKEY";
	public static final String CONFIG_MQTT_CLIENTKEY_DEFAULT = "server-key.pem";
	public static final String CONFIG_MQTT_CLIENTID_DESC = "Connection client ID";
	public static final String CONFIG_MQTT_CLIENTID = "MQTT_CLIENTID";
	public static final String CONFIG_MQTT_CLIENTID_DEFAULT = "";
	public static final String CONFIG_MQTT_BROKER_DESC = "Broker url";
	public static final String CONFIG_MQTT_BROKER = "MQTT_BROKER";
	public static final String CONFIG_MQTT_BROKER_DEFAULT = "";
	public static final String CONFIG_MQTT_USERNAME_DESC = "Broker Username";
	public static final String CONFIG_MQTT_USERNAME = "MQTT_USERNAME";
	public static final String CONFIG_MQTT_USERNAME_DEFAULT = "";
	public static final String CONFIG_MQTT_PASSWORD_DESC = "Broker Password";
	public static final String CONFIG_MQTT_PASSWORD = "MQTT_PASSWORD";
	public static final String CONFIG_MQTT_PASSWORD_DEFAULT = "";
	public static final String CONFIG_MQTT_CUSTOMER_DESC = "Topic customer";
	public static final String CONFIG_MQTT_CUSTOMER = "MQTT_CUSTOMER";
	public static final String CONFIG_MQTT_CUSTOMER_DEFAULT = "";
	public static final String CONFIG_MQTT_REGION_DESC = "Topic region";
	public static final String CONFIG_MQTT_REGION = "MQTT_REGION";
	public static final String CONFIG_MQTT_REGION_DEFAULT = "";
	
	// BSM forward
	public static final String CONFIG_MQTT_ENABLEBSMFORWARD = "MQTT_ENABLEBSMFORWARD";
	public static final boolean CONFIG_MQTT_ENABLEBSMFORWARD_DEFAULT = true;
	public static final String CONFIG_MQTT_ENABLEBSMFORWARD_DESC = "Enable BSM forwarding";
	
	// periodic messages
	public static final String CONFIG_MQTT_PERIODIC_FREQ_SEC_DESC = "Summary report interval (sec)";
	public static final String CONFIG_MQTT_PERIODIC_FREQ_SEC = "MQTT_PERIODIC_FREQ_SEC";
	public static final int CONFIG_MQTT_PERIODIC_FREQ_SEC_DEFAULT = 60;
	public static final String CONFIG_MQTT_ZONES_DESC = "Status report zones (JSON)";
	public static final String CONFIG_MQTT_ZONES = "MQTT_ZONES";
	public static final String CONFIG_MQTT_ZONES_DEFAULT = "";
	
	// SFTP settings
	public static final String CONFIG_MQTT_SFTPHOST = "mqtt_sftphost";
	public static final String CONFIG_MQTT_SFTPHOST_DEFAULT = "sftphost";
	public static final String CONFIG_MQTT_SFTPHOST_DESC = "SFTP Host";
	public static final String CONFIG_MQTT_SFTPUSER = "mqtt_sftpuser";
	public static final String CONFIG_MQTT_SFTPUSER_DEFAULT = "sftpuser";
	public static final String CONFIG_MQTT_SFTPUSER_DESC = "SFTP Username";
	public static final String CONFIG_MQTT_SFTPPASS = "mqtt_sftppass";
	public static final String CONFIG_MQTT_SFTPPASS_DEFAULT = "";
	public static final String CONFIG_MQTT_SFTPPASS_DESC = "SFTP Password";
	public static final String CONFIG_MQTT_SFTPFOLDER = "mqtt_sftpfolder";
	public static final String CONFIG_MQTT_SFTPFOLDER_DEFAULT = "";
	public static final String CONFIG_MQTT_SFTPFOLDER_DESC = "SFTP destination folder";

	/*
	private static final String PARAMETER_SCRIPT = "script";
	private static final String PARAMETER_EVENT = "event";
	private static final String PARAMETER_EVENT_START = "start";
	private static final String PARAMETER_EVENT_STOP = "stop";
	private static final String PARAMETER_EVENT_ENABLE = "enable";
	private static final String PARAMETER_EVENT_DISABLE = "disable";
	private static final String PARAMETER_EVENT_STARTALL = "start-all";
	private static final String PARAMETER_EVENT_STOPALL = "stop-all";
	private static final String PARAMETER_EVENT_ENABLESTART = "enable-start";
	private static final String PARAMETER_EVENT_STOPDISABLE = "stop-disable";
	*/
	
	protected ConfigFile cf;
	protected ObjectMapper mapper = new ObjectMapper();
	private SimpleThread statusTask;

	public ConfigMqttTransceiver(ConfigFile cf) {
		super(cf.getString(CONFIG_MQTT_BROKER, CONFIG_MQTT_BROKER_DEFAULT),
				cf.getString(CONFIG_MQTT_CLIENTID, CONFIG_MQTT_CLIENTID_DEFAULT));
		this.cf = cf;
	}
	
	public abstract String[] subscriptionTopics();
	public abstract String statusTopic();
	public abstract String buildStatusPayload();

	public void start() {
		// check for TLS certs
		setCerts(
				cf.getString(CONFIG_MQTT_CACERT, CONFIG_MQTT_CACERT_DEFAULT),
				cf.getString(CONFIG_MQTT_CLIENTCERT, CONFIG_MQTT_CLIENTCERT_DEFAULT),
				cf.getString(CONFIG_MQTT_CLIENTKEY, CONFIG_MQTT_USERNAME_DEFAULT)
				);
		
		// set user auth
		setUserAuth(
				cf.getString(CONFIG_MQTT_USERNAME, CONFIG_MQTT_USERNAME_DEFAULT),
				cf.getString(CONFIG_MQTT_PASSWORD, CONFIG_MQTT_PASSWORD_DEFAULT).toCharArray()
				);
		
		// connect
		if (!connect()) {
			logger.error("ConfigMqttTransceiver connection failed");
			return;
		}

		// subscribe to the config topic
		//String configTopic = topicBuilder(TOPIC_VERSION_1, TOPIC_CONFIG);
		//String[] topics = new String[] { configTopic };
		subscribe(subscriptionTopics());
		
		// create the thread for periodic summary reports
		int freqsec = cf.getInt(CONFIG_MQTT_PERIODIC_FREQ_SEC, CONFIG_MQTT_PERIODIC_FREQ_SEC_DEFAULT);
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
					
					// update status
					//summaryConfig.updateTimestamp();
					//summaryConfig.setSubtype(cf.getString(ConfigFile.SUBTYPE, ConfigFile.SUBTYPE_DEFAULT));
					//summaryConfig.setLocation(cf.getString(ConfigFile.LOCATIONNAME, ConfigFile.LOCATIONNAME_DEFAULT));
					//summaryConfig.setLatitude(cf.getString(ConfigFile.LATITUDE, ConfigFile.LATITUDE_DEFAULT));
					//summaryConfig.setLongitude(cf.getString(ConfigFile.LONGITUDE, ConfigFile.LONGITUDE_DEFAULT));
					//summaryConfig.setAltitude(cf.getString(ConfigFile.ALTITUDE, ConfigFile.ALTITUDE_DEFAULT));
					// system status
					// sensor status
					// script status
					// -num scripts
					// -running scripts
					// GPIOs
					// versions
					// -java
					// -mainboard
					// logging level
					// cpu temp
					// cpu load
					// num procs
					// used disk
					// used ram
					// uptime

					// publish periodic status message (with aggregated BSM data)
					publish(statusTopic(), buildStatusPayload(), true);
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
