package com.apextalos.cvitfusion.client.app;

import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.controllers.HelloController;
import com.apextalos.cvitfusioncommon.settings.ConfigFile;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

	private static final Logger logger = LogManager.getLogger(Main.class.getSimpleName());

	@Override
	public void start(Stage stage) throws IOException {
		logger.info("app starting");

		ConfigFile cf = new ConfigFile("cvitfusion.properties");
		if (!cf.load())
			System.exit(0);
		
		URL url = getClass().getResource("hello-view.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);
		Scene scene = new Scene(fxmlLoader.load());
		stage.setTitle("Apex Talos CVITFusion Client");
		stage.getIcons().add(new Image(getClass().getResource("missile.png").toExternalForm()));
		stage.setScene(scene);
		stage.setResizable(true);
		
		
		((HelloController)fxmlLoader.getController()).begin(cf);
		
		stage.setX(cf.getDouble(Strings.WINDOW_POSITION_X_CONFIG, Strings.WINDOW_POSITION_X_DEFAULT));
        stage.setY(cf.getDouble(Strings.WINDOW_POSITION_Y_CONFIG, Strings.WINDOW_POSITION_Y_DEFAULT));
        stage.setWidth(cf.getDouble(Strings.WINDOW_WIDTH_CONFIG, Strings.WINDOW_WIDTH_DEFAULT));
        stage.setHeight(cf.getDouble(Strings.WINDOW_HEIGHT_CONFIG, Strings.WINDOW_HEIGHT_DEFAULT));
        stage.show();
        
        stage.setOnCloseRequest((final WindowEvent event) -> {
            cf.setDouble(Strings.WINDOW_POSITION_X_CONFIG, stage.getX());
            cf.setDouble(Strings.WINDOW_POSITION_Y_CONFIG, stage.getY());
            cf.setDouble(Strings.WINDOW_WIDTH_CONFIG, stage.getWidth());
            cf.setDouble(Strings.WINDOW_HEIGHT_CONFIG, stage.getHeight());
            cf.save();
        });
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
