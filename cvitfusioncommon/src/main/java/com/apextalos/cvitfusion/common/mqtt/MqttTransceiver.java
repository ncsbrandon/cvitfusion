package com.apextalos.cvitfusion.common.mqtt;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.apextalos.cvitfusion.common.mqtt.connection.ConnectionEvent;
import com.apextalos.cvitfusion.common.mqtt.connection.ConnectionEvent.Change;
import com.apextalos.cvitfusion.common.mqtt.connection.ConnectionListener;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExEvent;
import com.apextalos.cvitfusion.common.mqtt.subscription.SubscriptionExListener;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicParser;

public abstract class MqttTransceiver implements MqttCallback {

	private static Logger logger = LogManager.getLogger(MqttTransceiver.class.getSimpleName());

	private static final int QOS = 2;

	// config
	private String broker = "";
	private String clientId = "";
	private boolean useAuth = false;
	private String username = "";
	private char[] password;
	private boolean useCerts = false;
	private String caCert;
	private String clientCrt;
	private String clientKey;
	
	// status
	private MemoryPersistence persistence = new MemoryPersistence();
	private MqttClient client = null;
	private List<String> activeSubscriptions = new ArrayList<>();
	public List<String> getActiveSubscriptions() {	return activeSubscriptions;	}
	private List<String> activePublications = new ArrayList<>();
	public List<String> getActivePublications() {	return activePublications;	}
	private int sentCount = 0;
	public int getSentCount() { return sentCount; }
	private int receivedCount = 0;
	public int getReceivedCount() { return receivedCount; }
	
	// connection event
	private final List<ConnectionListener> connectionlisteners = new ArrayList<>();
	public void addConnectionListener(ConnectionListener l) {
		connectionlisteners.add(l);
	}
	public void connectionChanged(ConnectionEvent ce) {
		for (ConnectionListener l : connectionlisteners) {
			l.connectionChange(ce);
		}
	}
	
	// subscription events
	private final List<SubscriptionExListener> subsriptionExlisteners = new ArrayList<>();
	public void addSubscriptionListener(SubscriptionExListener l) {
		subsriptionExlisteners.add(l);
	}
	public void removeSubscriptionListener(SubscriptionExListener s) {
		subsriptionExlisteners.removeIf(x -> 0 == x.topic().compareToIgnoreCase(s.topic()));
	}
	public void incomingMessage(SubscriptionExEvent se) {
		for (SubscriptionExListener l : subsriptionExlisteners) {
			if (TopicParser.match(l.topic(), se.getTopic())) {
				l.incomingMessage(se);
			}
		}
	}
	
	protected MqttTransceiver(String broker, String clientId) {	
		this.broker = broker;
		this.clientId = clientId;
	}

	public void setUserAuth(String username, char[] password) {
		// no auth
		if(username.isEmpty() && password.length == 0) {
			logger.info("MQTT auth disabled");
			this.useAuth = false;
			this.username = "";
			this.password = new char[0];
			return;
		}
		
		// auth
		logger.info("MQTT auth enabled");
		this.useAuth = true;
		this.username = username;
		this.password = password;
	}

	public void clearUserAuth() {
		// no auth
		logger.info("MQTT auth cleared");
		this.useAuth = false;
		this.username = "";
		this.password = new char[0];
	}
	
	public boolean certsExist() {
		//String folder = BackupRestore.APP_FOLDER
		//if OSDetect.isWindows(
		//	folder = "C:\\temp\\"
		
		File caCertFile = new File(caCert);
		File clientCrtFile = new File(clientCrt);
		File clientKeyFile = new File(clientKey);
		
		return caCertFile.exists() && clientCrtFile.exists() && clientKeyFile.exists();
	}
	
	public boolean setCerts(String caCert, String clientCrt, String clientKey) {
		// certs
		if(!caCert.isEmpty() && !clientCrt.isEmpty() && !clientKey.isEmpty()) {
			this.caCert = caCert;
			this.clientCrt = clientCrt;
			this.clientKey = clientKey;
			useCerts = certsExist();
			logger.info("MQTT certificates {}", useCerts);
			return useCerts;
		} 
		
		// no certs
		useCerts = false;
		logger.info("MQTT certificates {}", useCerts);
		return useCerts;
	}
	
	public void clearCerts() {
		// no certs
		useCerts = false;
		logger.info("MQTT certificates {}", useCerts);
	}

	public boolean isConnected() {
		return (client != null && client.isConnected());
	}

	public boolean connect() {
		if (isConnected())
			disconnect(true);

		if (broker == null || broker.isEmpty() || clientId == null || clientId.isEmpty()) {
			logger.error("broker and client id are required");
			return false;
		}

		try {
			client = new MqttClient(broker, clientId, persistence);
			
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			//connOpts setConnectionTimeout 60)
			//connOpts setKeepAliveInterval 60)
			//connOpts setMqttVersion MqttConnectOptions MQTT_VERSION_3_1)
			connOpts.setHttpsHostnameVerificationEnabled(false);
			
			if (useAuth) {
				if(!username.isEmpty())
					connOpts.setUserName(username);
				if(password.length > 0)
					connOpts.setPassword(password);
			}
			
			if (useCerts) {
				SSLSocketFactory socketFactory = SSLUtils.getSocketFactory(caCert, clientCrt, clientKey, "");
				connOpts.setSocketFactory(socketFactory);				
			}

			logger.debug("Connecting to broker: {}", broker);
			connectionChanged(new ConnectionEvent(Change.CONNECTING, "Connecting to " + broker + "..."));
			client.connect(connOpts);
			logger.debug("Connected");
			connectionChanged(new ConnectionEvent(Change.CONNECTSUCCESS, "Connected to " + broker));
			client.setCallback(this);
			return true;
		} catch (MqttException e) {
			client = null;
			logger.error("connection failure [{}]: {}", e.getReasonCode(), e.getMessage());
			connectionChanged(new ConnectionEvent(Change.CONNECTFAILURE, e.getMessage()));
		} catch (UnrecoverableKeyException | KeyManagementException | CertificateException | KeyStoreException | NoSuchAlgorithmException | IOException e) {
			client = null;
			logger.error("TLS socket creation failure: {}", e.getMessage());
			connectionChanged(new ConnectionEvent(Change.CONNECTFAILURE, String.format("TLS failure %s", e.getMessage())));
		}
		return false;
	}

	public void disconnect(boolean clearPubSubs) {
		try {
			if(client != null) {
				logger.debug("Disconnecting from broker: {}", broker);
				client.setCallback(null);
				if(client.isConnected())
					client.disconnect();
				client.close(true);
				client = null;
			}
			if(clearPubSubs) {
				logger.debug("Clearing subscriptions");
				activeSubscriptions.clear();
				activePublications.clear();
			}
			connectionChanged(new ConnectionEvent(Change.DISCONNECT, "Connection lost"));
		} catch (MqttException e) {
			logger.error("disonnection failure reason: {}", e.getReasonCode());
			logger.error("msg: {}", e.getMessage());
			
			try {
				client.disconnectForcibly();
				client.close(true);
				client = null;
			} catch (MqttException e1) {
				logger.error("disonnection FORCED failure reason: {}", e.getReasonCode());
			}
		}
	}

	public boolean publish(String topic, String content, boolean retained) {
		if (!isConnected()) {
			logger.debug("Not connected, attempting reconnection");
			if(!connect() || !resubscribe())
				return false; // failure to reconnect or resubscribe
		}
		
		if(topic == null || topic.isBlank() ||
		   content == null || content.isBlank()) {
			logger.debug("nothing to publish");
			return false;
		}

		logger.debug("Publishing message [{}]: {}", content.length(), topic);
		MqttMessage message = new MqttMessage(content.getBytes());
		message.setQos(QOS);
		message.setRetained(retained);

		if(!activePublications.contains(topic))
			activePublications.add(topic);
		
		try {
			client.publish(topic, message);
			logger.debug("Message published");
			sentCount++;
		} catch (MqttException e) {
			logger.error("publish failure reason: {}", e.getReasonCode());
			logger.error("msg: {}", e.getMessage());
			return false; // failure to publish
		}
		
		return true; // success
	}
	
	public void subscribe(String topicFilter) {
		if(topicFilter == null || topicFilter.isBlank())
			return;
		
		try {
			logger.debug("subscribing: {}", topicFilter);
			activeSubscriptions.add(topicFilter);
			client.subscribe(topicFilter);
		} catch (MqttException e) {
			logger.error("subscription failure reason: {}", e.getReasonCode());
			logger.error("msg: {}", e.getMessage());
		}
	}
	
	public void subscribe(String[] topicFilters) {
		if(topicFilters == null || topicFilters.length == 0)
			return;
		
		try {
			logger.debug("subscribing: {}", Arrays.toString(topicFilters));
			activeSubscriptions.addAll(Arrays.asList(topicFilters));
			client.subscribe(topicFilters);
		} catch (MqttException e) {
			logger.error("subscription failure reason: {}", e.getReasonCode());
			logger.error("msg: {}", e.getMessage());
		}
	}
	
	public boolean resubscribe() {
		for(String topic : activeSubscriptions) {
			logger.debug("resubscribing: {}", topic);
			try {
				client.subscribe(topic);
			} catch (MqttException e) {
				logger.error("reubscription failure reason: {}", e.getReasonCode());
				logger.error("msg: {}", e.getMessage());
				return false;
			}
		}
		
		return true;
	}
	
	public void unsubscribe(String topicFilter) {
		if(topicFilter == null || topicFilter.isBlank())
			return;
		
		try {
			logger.debug("subscribing: {}", topicFilter);
			activeSubscriptions.remove(topicFilter);
			client.unsubscribe(topicFilter);
		} catch (MqttException e) {
			logger.error("unsubscription failure reason: {}", e.getReasonCode());
			logger.error("msg: {}", e.getMessage());
		}
	}

	@Override
	public void connectionLost(Throwable arg0) {
		logger.debug("Connection lost: {}", arg0.getMessage());
		
		// cleanup this connection
		disconnect(false);
		
		// make a new one
		if(connect())
			resubscribe();
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		logger.debug("Delivery complete");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) {
        // convert to a string
		String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
		
		// URL decode (puts JSON back together)
		String decoded = "";
		try {
			decoded = URLDecoder.decode(payload, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("Failure decoding [{}]: {}", topic, e.getMessage());
			return;
		}
		
		// let all listeners handle it
		incomingMessage(new SubscriptionExEvent(topic, decoded));
		
		// increment count
		receivedCount++;
	}
}