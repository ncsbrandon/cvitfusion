package com.apextalos.cvitfusion.client.scene;

import com.apextalos.cvitfusion.client.controllers.BaseController;
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
	private ResourceLoader<Parent> rl = new ResourceLoader<>();
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
			controllerConnections = rl.createLoader("connectionsScene.fxml", null).getController();
			sceneConnections = new Scene(rl.getResource());
		}
		
		stage.setScene(sceneConnections);
		stage.setResizable(false);
		stage.setMaximized(false);		
		controllerConnections.begin(cf, stage);
		stage.show();
		
		// save window size and position at app close
		stage.setOnCloseRequest((final WindowEvent event) -> {
			if(controllerConnections != null)
				controllerConnections.end(stage);
		});
	}
	
	public void showMain(Stage stage) {
		close(stage);
		
		if(sceneMain == null) {
			controllerMain = rl.createLoader("mainScene.fxml", null).getController();
			sceneMain = new Scene(rl.getResource());
		}
		
		stage.setScene(sceneMain);
		stage.setResizable(true);		
		controllerMain.begin(cf, stage);
		stage.show();
		
		// save window size and position at app close
		stage.setOnCloseRequest((final WindowEvent event) -> {
			if(controllerMain != null)
				controllerMain.end(stage);
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
