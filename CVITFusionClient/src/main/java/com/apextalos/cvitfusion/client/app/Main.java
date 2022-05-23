package com.apextalos.cvitfusion.client.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.client.scene.SceneManager;
import com.apextalos.cvitfusion.common.settings.ConfigFile;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

	private static final Logger logger = LogManager.getLogger(Main.class.getSimpleName());

	

	@Override
	public void start(Stage stage) throws IOException {
		logger.info("app starting");

		// load the config
		ConfigFile cf = new ConfigFile("cvitfusion.properties");
		if (!cf.load())
			System.exit(0);

		// setup the stage
		stage.setTitle("Apex Talos CVITFusion Client");
		stage.getIcons().add(loadIcon("missile.png"));
				
		// first show the connections dialog
		SceneManager.getInstance(stage, cf).showConnections();		
	}
	
	private Image loadIcon(String name) throws IOException {
		Image icon = null;
		
		// try loading as the jar
		InputStream in = getClass().getResourceAsStream("/" + name);
		logger.info(String.format("getResourceAsStream is null: %b", in==null));
		if(in != null) {
			icon = new Image(in);
		} else {	
			// try loading as the debugger
			URL url = getClass().getResource("../../../../../" + name);
			logger.info(String.format("getResource is null: %b", url==null));
			if(url != null) {
				icon = new Image(url.toExternalForm());
			} else {
				logger.error("unable to load the icon fxml " + name);
			}
		}
		
		return icon;
	}

	@Override
	public void stop() throws Exception {
		logger.info("app stopping");
		super.stop();
	}

	public static void main(String[] args) {
		logger.info("app main");
		launch(args);
	}
}
