package com.apextalos.cvitfusionengine.app;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
		app.start();
		
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
	
	public void start() {
		logger.info("app starting");
		
		// load the config
		cf = new ConfigFile("cvitfusion.properties");
		if (!cf.load())
			System.exit(0);
		
		// if we don't have a device UUID at startup, create one
		if(!cf.hasKey(ConfigItems.DEVICE_UUID_CONFIG))
			cf.setString(ConfigItems.DEVICE_UUID_CONFIG, UUID.randomUUID().toString(), false); 
		
		// main transceiver
		cmt = new EngineConfigMqttTransceiver(cf);
		cmt.start();
		
		logger.info("app started");
	}
	
	public void stop() {
		logger.info("app stopping");
		
		cmt.stop();
		cf.save();
		
		logger.info("app stopped");
	}
}
