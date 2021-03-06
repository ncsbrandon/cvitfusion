package com.apextalos.cvitfusionengine.app;

import java.io.IOException;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.Map.Entry;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.design.DesignManager;
import com.apextalos.cvitfusion.common.engine.ProcessingEngine;
import com.apextalos.cvitfusion.common.license.Feature;
import com.apextalos.cvitfusion.common.license.FeatureManager;
import com.apextalos.cvitfusion.common.license.License;
import com.apextalos.cvitfusion.common.license.LicenseManager;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.settings.ConfigItems;
import com.apextalos.cvitfusionengine.mqtt.EngineConfigMqttTransceiver;

public class App {
	
	private static final Logger logger = LogManager.getLogger(App.class.getSimpleName());

	private static volatile boolean keepRunning = true;
	public static void main(String[] args) {
		// create the shutdown hook		
		final Thread mainThread = Thread.currentThread();
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    @Override
		    public void run() {
		        keepRunning = false;
		        try {
					mainThread.join();
				} catch (InterruptedException e) {
					logger.error("Shutdown hook interrupted: {}", e.getMessage());
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
				logger.error("App interrupted: {}", e.getMessage());
			}
		}
		
		// stop the app
		app.stop();
	}
	
	
	private ConfigFile cf;
	private EngineConfigMqttTransceiver cmt;
	private ProcessingEngine pe;
	
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
		if(cf.isEmpty(ConfigItems.DEVICE_UUID_CONFIG)) {
			cf.setString(ConfigItems.DEVICE_UUID_CONFIG, UUID.randomUUID().toString(), false);
			logger.info("Created device UUID: {}", cf.getString(ConfigItems.DEVICE_UUID_CONFIG, ConfigItems.DEVICE_UUID_DEFAULT));
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
		if(cf.isEmpty(ConfigItems.DEVICE_LICENSEID_CONFIG)) {	
			String iface;
			try {
				iface = lm.getFirstInterface();
			} catch (SocketException e1) {
				logger.error("Unable to find license interface");
				return false;
			}
			
			try {		
				String licenseID = lm.generateLicenseID(iface, Version.getInstance().getVersion());
				cf.setString(ConfigItems.DEVICE_LICENSEID_CONFIG, licenseID, false);
				logger.error("license id created: {}", licenseID);
				
				// save and exit
				cf.save();
				return false;
			} catch (IllegalBlockSizeException | IOException e) {
				logger.error("Unable to create license id for: {}", iface);
				return false;
			}
		}
		
		// check for a license key
		if(cf.isEmpty(ConfigItems.DEVICE_LICENSEKEY_CONFIG)) {
			logger.error("No license key provided");
			// help the user by creating an empty entry
			cf.setString(ConfigItems.DEVICE_LICENSEKEY_CONFIG, "", true);	

			// save and exit
			cf.save();
			return false;
		}
		
		// load the license key
		License licenseKey = null;
		try {
			licenseKey = lm.loadLicense(cf.getString(ConfigItems.DEVICE_LICENSEKEY_CONFIG, ConfigItems.DEVICE_LICENSEKEY_DEFAULT));
			logger.info("license key property count: {}", licenseKey.getProperties().size());
		} catch (ClassNotFoundException | IllegalBlockSizeException | BadPaddingException | IOException e) {
			logger.error("Unable to load license key");
			return false;
		}
		
		// what interface is being requested
		String iface = licenseKey.getStringFeature(FeatureManager.FEATURE_INTERFACE);
		logger.info("using interface: {}", iface);
		
		try {
			if(!lm.verifyAddress(iface, licenseKey)) {
				logger.error("Invalid license key");
				return false;
			}
		} catch (SocketException e) {
			logger.error("Unable to verify license key");
			return false;
		}
		
		// report the visible features of this key
		for(Entry<Feature, String> featureValue : licenseKey.getVisibleFeatures().entrySet()) {
    		logger.info("{}: {}", featureValue.getKey(), featureValue.getValue());
    	}
		
		// load the design
		DesignManager dm = DesignManager.getInstance();
		OperationalFlow design = new OperationalFlow(
			dm.getProcessesFromConfig(cf),
			dm.getTypes(licenseKey),
			dm.getStyles(),
			dm.getTypeStyleMap()
		);
		
		// validate it before we proceed
		String validationFailure = design.validate();
		if(!validationFailure.isBlank()) {
			logger.error("Validation failure: {}", validationFailure);
			return false;
		}
		logger.info("No design validation issues found");
		
		// the processing engine
		pe = new ProcessingEngine(design, cf);
		// main transceiver
		// in the engine, the handlers are autonomous; they don't need listeners
		cmt = new EngineConfigMqttTransceiver(cf, design, pe);
						
		if(!pe.start(cmt)) {
			logger.error("unable to start PE");
			return false;
		}
		logger.info("PE started");
		
		if(!cmt.start()) {
			logger.error("unable to start MQTT");
			return false;
		}
		logger.info("MQTT started");
		
		return true;
	}
	
	public void stop() {
		logger.info("app stopping");
		
		pe.stop();
		cmt.stop();
		cf.save();
		
		logger.info("app stopped");
	}
}
