package com.apextalos.cvitfusion.common.settings;

public class ConfigItems {

	// THESE ARE APPLICATION COMMON
	public static final String PARSE_FAILED = "Failure while parsing the value [%s] for key [%s]";

	public static final String DEVICE_UUID_CONFIG = "DEVICE_UUID";
	public static final String DEVICE_UUID_DEFAULT = "UNKNOWN";
	public static final String DEVICE_LICENSEID_CONFIG = "DEVICE_LICENSEID";
	public static final String DEVICE_LICENSEID_DEFAULT = "UNKNOWN";
	public static final String DEVICE_LICENSEKEY_CONFIG = "DEVICE_LICENSEKEY";
	public static final String DEVICE_LICENSEKEY_DEFAULT = "UNKNOWN";
	public static final String DEVICE_LOCATION_CONFIG = "DEVICE_LOCATION";
	public static final String DEVICE_LOCATION_DEFAULT = "UNKNOWN";

	public static final String DEVICE_LATITUDE_CONFIG = "DEVICE_LATITUDE";
	public static final double DEVICE_LATITUDE_DEFAULT = -9999;
	public static final String DEVICE_LONGITUDE_CONFIG = "DEVICE_LONGITUDE";
	public static final double DEVICE_LONGITUDE_DEFAULT = -9999;
	public static final String DEVICE_ELEVATION_CONFIG = "DEVICE_ELEVATION";
	public static final double DEVICE_ELEVATION_DEFAULT = -9999;

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

	public static final String CONFIG_MQTT_PERIODIC_FREQ_SEC = "MQTT_PERIODIC_FREQ_SEC";
	public static final int CONFIG_MQTT_PERIODIC_FREQ_SEC_DEFAULT = 60;
}
