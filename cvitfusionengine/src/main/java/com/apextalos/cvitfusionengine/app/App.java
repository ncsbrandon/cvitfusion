package com.apextalos.cvitfusionengine.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.settings.ConfigFile;

public class App {
	private static final Logger logger = LogManager.getLogger(App.class.getSimpleName());

	public static void main(String[] args) {
		logger.info("app starting");

		// load the config
		ConfigFile cf = new ConfigFile("cvitfusion.properties");
		if (!cf.load())
			System.exit(0);
		
		//EngineConfigMqttTransceiver cmt = new EngineConfigMqttTransceiver(cf);
		//cmt.start();
		
		
		//cmt.stop();
		logger.info("app stopping");
	}
}
