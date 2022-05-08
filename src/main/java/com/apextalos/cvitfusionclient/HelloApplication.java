package com.apextalos.cvitfusionclient;

import com.apextalos.cvitfusionclient.settings.ConfigFile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class HelloApplication extends Application {

    private static final Logger logger = LogManager.getLogger(HelloApplication.class.getSimpleName());

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("app starting");

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Apex Talos CVITFusion Client");
        stage.getIcons().add(new Image(getClass().getResource("/img/missile.png").toExternalForm()));
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        logger.error("app stopping");

        super.stop();
    }

    public static void main(String[] args) {
        logger.info("app main");

        ConfigFile cf = new ConfigFile("cvitfusion.properties");
        if(!cf.load())
            System.exit(0);

        launch();
    }
}