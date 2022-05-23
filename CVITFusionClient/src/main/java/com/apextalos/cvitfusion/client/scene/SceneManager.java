package com.apextalos.cvitfusion.client.scene;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.client.controllers.BaseController;
import com.apextalos.cvitfusion.common.settings.ConfigFile;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SceneManager {

	private static final Logger logger = LogManager.getLogger(SceneManager.class.getSimpleName());
	
	private static SceneManager singleInstance = null;

	public static SceneManager getInstance(ConfigFile cf) {
		if (singleInstance == null)
			singleInstance = new SceneManager(cf);

		return singleInstance;
	}
	
	private ConfigFile cf;
	private FXMLLoader fxmlLoader = null;
	private BaseController activeController = null;			
	
	private SceneManager(ConfigFile cf) {
		this.cf = cf;
	}
	
	public void showConnections(Stage stage) throws IOException {
		if(activeController != null) {
			activeController.end();
			activeController = null;
			stage.close();
		}
		
		stage.setScene(loadScene("connectionsScene.fxml"));
		stage.setResizable(false);
		stage.setMaximized(false);
		activeController = (BaseController) fxmlLoader.getController();
		activeController.begin(cf);
		stage.show();
		
		// save window size and position at app close
		stage.setOnCloseRequest((final WindowEvent event) -> {
			if(activeController != null)
				activeController.end();
		});
	}
	
	public void showMain(Stage stage) throws IOException {
		if(activeController != null) {
			activeController.end();
			activeController = null;
			stage.close();
		}
		
		stage.setScene(loadScene("mainScene.fxml"));
		stage.setResizable(true);		
		activeController = (BaseController) fxmlLoader.getController();
		activeController.begin(cf);
		stage.show();
		
		// save window size and position at app close
		stage.setOnCloseRequest((final WindowEvent event) -> {
			if(activeController != null)
				activeController.end();
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
	
	public void close(Stage stage) {
		if(activeController != null) {
			activeController.end();
			activeController = null;
		}
		
		stage.close();
	}
}
