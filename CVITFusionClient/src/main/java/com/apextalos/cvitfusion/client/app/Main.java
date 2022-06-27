package com.apextalos.cvitfusion.client.app;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.client.controllers.ResourceLoader;
import com.apextalos.cvitfusion.client.scene.SceneManager;
import com.apextalos.cvitfusion.common.settings.ConfigFile;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	private static final Logger logger = LogManager.getLogger(Main.class.getSimpleName());

	private ConfigFile cf;
	private ResourceLoader rl = new ResourceLoader();
	
	@Override
	public void start(Stage stage) throws IOException {
		logger.info("app starting");

		// load the config
		cf = new ConfigFile("cvitfusion.properties");
		if (!cf.load())
			System.exit(0);

		// setup the stage
		stage.setTitle("Apex Talos CVITFusion Client");
		stage.getIcons().add(rl.loadImageByFilename(ResourceLoader.IMAGE_MISSLE));
				
		// first show the connections dialog
		SceneManager.getInstance(cf).showConnections(stage);	
	}

	@Override
	public void stop() throws Exception {
		logger.info("app stopping");
		
		cf.save();
		
		super.stop();
	}

	public static void main(String[] args) {
		logger.info("app main");
		launch(args);
	}
}
