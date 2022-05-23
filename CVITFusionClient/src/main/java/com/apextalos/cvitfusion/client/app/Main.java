package com.apextalos.cvitfusion.client.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.client.controllers.HelloController;
import com.apextalos.cvitfusion.common.settings.ConfigFile;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

	private static final Logger logger = LogManager.getLogger(Main.class.getSimpleName());

	FXMLLoader fxmlLoader = null;

	@Override
	public void start(Stage stage) throws IOException {
		logger.info("app starting");

		// load the config
		ConfigFile cf = new ConfigFile("cvitfusion.properties");
		if (!cf.load())
			System.exit(0);

		//Scene connections = loadScene("connections.fxml");
		
		Scene scene = loadScene("hello-view.fxml");
		Image icon = loadIcon("missile.png");		
		stage.setTitle("Apex Talos CVITFusion Client");
		stage.getIcons().add(icon);
		stage.setScene(scene);
		stage.setResizable(true);
		((HelloController) fxmlLoader.getController()).begin(cf);
		stage.setX(cf.getDouble(ConfigItems.WINDOW_POSITION_X_CONFIG, ConfigItems.WINDOW_POSITION_X_DEFAULT));
		stage.setY(cf.getDouble(ConfigItems.WINDOW_POSITION_Y_CONFIG, ConfigItems.WINDOW_POSITION_Y_DEFAULT));
		stage.setWidth(cf.getDouble(ConfigItems.WINDOW_WIDTH_CONFIG, ConfigItems.WINDOW_WIDTH_DEFAULT));
		stage.setHeight(cf.getDouble(ConfigItems.WINDOW_HEIGHT_CONFIG, ConfigItems.WINDOW_HEIGHT_DEFAULT));
		stage.show();

		// save window size and position at close
		stage.setOnCloseRequest((final WindowEvent event) -> {
			((HelloController) fxmlLoader.getController()).end();
			cf.setDouble(ConfigItems.WINDOW_POSITION_X_CONFIG, stage.getX());
			cf.setDouble(ConfigItems.WINDOW_POSITION_Y_CONFIG, stage.getY());
			cf.setDouble(ConfigItems.WINDOW_WIDTH_CONFIG, stage.getWidth());
			cf.setDouble(ConfigItems.WINDOW_HEIGHT_CONFIG, stage.getHeight());
			cf.save();
		});
	}
	
	private Scene loadScene(String name) throws IOException {
		Scene scene = null;
		
		InputStream in = getClass().getResourceAsStream("/" + name);
		logger.info(String.format("getResourceAsStream is null: %b", in==null));
		if(in != null) {
			fxmlLoader = new FXMLLoader();
			scene = new Scene(fxmlLoader.load(in));
		} else {	
			// try loading as the debugger
			URL url = getClass().getResource("../../../../../" + name);
			logger.info(String.format("getResource is null: %b", url==null));
			if(url != null) {
				fxmlLoader = new FXMLLoader(url);
				scene = new Scene(fxmlLoader.load());
			} else {
				logger.error("unable to load the scene fxml " + name);
			}
		}
		
		return scene;
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
