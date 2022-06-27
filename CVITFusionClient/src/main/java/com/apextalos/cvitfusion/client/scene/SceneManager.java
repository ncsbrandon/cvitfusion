package com.apextalos.cvitfusion.client.scene;

import com.apextalos.cvitfusion.client.controllers.BaseController;
import com.apextalos.cvitfusion.client.controllers.FXMLResource;
import com.apextalos.cvitfusion.client.controllers.ResourceLoader;
import com.apextalos.cvitfusion.common.settings.ConfigFile;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SceneManager {

	//private static final Logger logger = LogManager.getLogger(SceneManager.class.getSimpleName());
	
	private static SceneManager singleInstance = null;

	public static SceneManager getInstance(ConfigFile cf) {
		if (singleInstance == null)
			singleInstance = new SceneManager(cf);

		return singleInstance;
	}
	
	private ConfigFile cf;
	private ResourceLoader rl = new ResourceLoader();
	private Scene sceneMain = null;
	private BaseController controllerMain = null;
	private Scene sceneConnections = null;
	private BaseController controllerConnections = null;
	
	private SceneManager(ConfigFile cf) {
		this.cf = cf;
	}
	
	public void showConnections(Stage stage) {
		close(stage);
		
		if(sceneConnections == null) {
			FXMLResource fr = rl.createLoader("connectionsScene.fxml", null);
			controllerConnections = fr.getLoader().getController();
			sceneConnections = new Scene((Parent) fr.getResource());
		}
		
		stage.setScene(sceneConnections);
		stage.setResizable(false);
		stage.setMaximized(false);		
		controllerConnections.begin(cf, stage);
		stage.show();
		
		// save window size and position at app close
		stage.setOnCloseRequest((final WindowEvent event) -> {
			if(controllerConnections != null) {
				controllerConnections.end(stage);
				controllerConnections = null;
			}
		});
	}
	
	public void showMain(Stage stage) {
		close(stage);
		
		if(sceneMain == null) {
			FXMLResource fr = rl.createLoader("mainScene.fxml", null);
			controllerMain = fr.getLoader().getController();
			sceneMain = new Scene((Parent) fr.getResource());
		}
		
		stage.setScene(sceneMain);
		stage.setResizable(true);		
		controllerMain.begin(cf, stage);
		stage.show();
		
		// save window size and position at app close
		stage.setOnCloseRequest((final WindowEvent event) -> {
			if(controllerMain != null) {
				controllerMain.end(stage);
				controllerMain = null;
			}
		});
	}
	
	public void close(Stage stage) {
		if(controllerMain != null) {
			controllerMain.end(stage);
			//controllerMain = null;
		}
		
		if(controllerConnections != null) {
			controllerConnections.end(stage);
			//controllerConnections = null;
		}
		
		stage.close();
	}
}
