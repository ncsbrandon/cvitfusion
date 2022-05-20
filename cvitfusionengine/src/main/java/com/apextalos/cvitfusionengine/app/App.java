package com.apextalos.cvitfusionengine.app;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.Map.Entry;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.license.Feature;
import com.apextalos.cvitfusion.common.license.License;
import com.apextalos.cvitfusion.common.license.LicenseManager;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.settings.ConfigItems;
import com.apextalos.cvitfusionengine.mqtt.EngineConfigMqttTransceiver;

public class App {
	
	private static final Logger logger = LogManager.getLogger(App.class.getSimpleName());

	static volatile boolean keepRunning = true;
	public static void main(String[] args) {
		// create the shutdown hook		
		final Thread mainThread = Thread.currentThread();
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		        keepRunning = false;
		        try {
					mainThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }
		});
		
		// start the app
		App app = new App();
		if(!app.start())
			return;
		
		// main loop
		while(keepRunning) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// stop the app
		app.stop();
	}
	
	
	private ConfigFile cf;
	private EngineConfigMqttTransceiver cmt;
	
	public boolean start() {
		logger.info("----------------------------------");
		logger.info("app starting");
		logger.info("----------------------------------");
		
		// load the config
		cf = new ConfigFile("cvitfusion.properties");
		if (!cf.load()) {
			logger.error("Failure while loading configuration");
			return false;
		}
		logger.info("configuration loaded");
		
		// if we don't have a device UUID at startup, create one
		if(!cf.hasKey(ConfigItems.DEVICE_UUID_CONFIG)) {
			cf.setString(ConfigItems.DEVICE_UUID_CONFIG, UUID.randomUUID().toString(), false);
			logger.info("Created device UUID: " + cf.getString(ConfigItems.DEVICE_UUID_CONFIG, ConfigItems.DEVICE_UUID_DEFAULT));
		}
		
		// get the license manager instance
		LicenseManager lm = null;
		try {
			lm = LicenseManager.getInstance();
			logger.info("license manager loaded");
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			logger.error("Unable to instantiate the license manager");
			return false;
		}
		
		// if we don't have a license ID, create one
		if(!cf.hasKey(ConfigItems.DEVICE_LICENSEID_CONFIG)) {
			String iface = "";
			try {
				iface = lm.getFirstInterface();
				String licenseID = lm.generateLicenseID(iface, Version.getInstance().getVersion());
				cf.setString(ConfigItems.DEVICE_LICENSEID_CONFIG, licenseID, false);
				cf.save();
				logger.error("license id created: " + licenseID);
				return false;
			} catch (IllegalBlockSizeException | IOException e) {
				logger.error("Unable to create license id for: " + iface);
				return false;
			}
		}
		
		// check for a license key
		if(!cf.hasKey(ConfigItems.DEVICE_LICENSEKEY_CONFIG)) {
			logger.error("No license key provided");
			// help the user by creating an empty entry
			cf.setString(ConfigItems.DEVICE_LICENSEKEY_CONFIG, "", true);
			cf.save();
			return false;
		}
		
		// if it's blank
		if(cf.getString(ConfigItems.DEVICE_LICENSEKEY_CONFIG, "").isBlank()) {
			logger.error("license key is blank");
			// help the user by creating an empty entry
			return false;
		}
		
		// load the license key
		License licenseKey = null;
		try {
			licenseKey = lm.loadLicense(cf.getString(ConfigItems.DEVICE_LICENSEKEY_CONFIG, ConfigItems.DEVICE_LICENSEKEY_DEFAULT));
			logger.error("license key loaded property count: " + licenseKey.getProperties().size());
		} catch (ClassNotFoundException | IllegalBlockSizeException | BadPaddingException | IOException e) {
			logger.error("Unable to load license key");
			return false;
		}
		
		// report the visible features of this key
		for(Entry<Feature, String> featureValue : licenseKey.getVisibleFeatures().entrySet()) {
    		logger.info(featureValue.getKey().toString() + "= " + featureValue.getKey());
    	}
			
		// main transceiver
		cmt = new EngineConfigMqttTransceiver(cf);
		cmt.start();
		
		logger.info("app started");
		return true;
	}
	
	public void stop() {
		logger.info("app stopping");
		
		cmt.stop();
		cf.save();
		
		logger.info("app stopped");
	}
}
