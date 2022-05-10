package com.apextalos.cvitfusion.client.app;
	
import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusioncommon.settings.ConfigFile;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static final Logger logger = LogManager.getLogger(Main.class.getSimpleName());
	
	@Override
	public void start(Stage stage) throws IOException {
		logger.info("app starting");
		
		URL url = getClass().getResource("hello-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Apex Talos CVITFusion Client");
        //stage.getIcons().add(new Image(getClass().getResource("/img/missile.png").toExternalForm()));
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
		
	}
	
	@Override
    public void stop() throws Exception {
        logger.info("app stopping");

        super.stop();
    }

    public static void main(String[] args) {
        logger.info("app main");

        ConfigFile cf = new ConfigFile("cvitfusion.properties");
        if(!cf.load())
          System.exit(0);

        launch(args);
    }
}
