package com.apextalos.cvitfusion.client.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.client.app.ConfigItems;
import com.apextalos.cvitfusion.client.models.ConnectionsSceneModel;
import com.apextalos.cvitfusion.client.scene.SceneManager;
import com.apextalos.cvitfusion.common.settings.ConfigFile;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ConnectionsSceneController extends BaseController {

	private static final Logger logger = LogManager.getLogger(ConnectionsSceneController.class.getSimpleName());

	// Model
	private ConnectionsSceneModel model;

	public ConnectionsSceneModel getModel() {
		return model;
	}
	
	@FXML private VBox topVbox;
	@FXML private ListView<String> sessionList;
	@FXML private TextField nameTextField;
	@FXML private TextField urlTextField;
	@FXML private TextField caCertTextField;
	@FXML private TextField clientCertTextField;
	@FXML private TextField clientKeyTextField;
	@FXML private TextField usernameTextField;
	@FXML private TextField passwordTextField;
	@FXML private TextField clientIdTextField;
	@FXML private CheckBox tlsEnabledCheckBox;
	@FXML private CheckBox pwdEnabledCheckBox;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		model = new ConnectionsSceneModel();

	}
	
	@Override
	public void begin(ConfigFile cf) {
		super.begin(cf);
		
		Stage stage = (Stage)topVbox.getScene().getWindow();
		
		// stage size
		if(cf.hasKey(ConfigItems.CONNECTIONS_WIDTH_CONFIG))
			stage.setWidth(cf.getDouble(ConfigItems.CONNECTIONS_WIDTH_CONFIG, -1));
		if(cf.hasKey(ConfigItems.CONNECTIONS_HEIGHT_CONFIG))
			stage.setHeight(cf.getDouble(ConfigItems.CONNECTIONS_HEIGHT_CONFIG, -1));		
		
		// stage position
		stage.setX(cf.getDouble(ConfigItems.CONNECTIONS_POSITION_X_CONFIG, ConfigItems.CONNECTIONS_POSITION_X_DEFAULT));
		stage.setY(cf.getDouble(ConfigItems.CONNECTIONS_POSITION_Y_CONFIG, ConfigItems.CONNECTIONS_POSITION_Y_DEFAULT));
		
		// divider positions
	}

	@Override
	public void end() {
		super.end();
		
		Stage stage = (Stage)topVbox.getScene().getWindow();
		
		// stage position
		cf.setDouble(ConfigItems.CONNECTIONS_POSITION_X_CONFIG, stage.getX());
		cf.setDouble(ConfigItems.CONNECTIONS_POSITION_Y_CONFIG, stage.getY());
		
		// stage size
		cf.setDouble(ConfigItems.CONNECTIONS_WIDTH_CONFIG, stage.getWidth());
		cf.setDouble(ConfigItems.CONNECTIONS_HEIGHT_CONFIG, stage.getHeight());
		
		// divider positions
	}

	@Override
	public void onActionPerformed(Object o, EventType et) {
		
	}

	@FXML
	private void OnActionDeleteButton(ActionEvent action) {
		
	}
	
	@FXML
	private void OnActionSaveButton(ActionEvent action) {
		
	}
	
	@FXML
	private void OnActionConnectButton(ActionEvent action) {
		// change to the main scene
		try {
			SceneManager.getInstance(null, null).showMain();
		} catch (IOException e) {
			logger.error("Unable to change to the main scene: " + e.getMessage());
		}
	}
	
	@FXML
	private void OnActionCancelButton(ActionEvent action) {
		System.exit(0);
	}
	
	@FXML
	private void OnActionCaCertButton(ActionEvent action) {
		
	}
	
	@FXML
	private void OnActionClientCertButton(ActionEvent action) {
		
	}
	
	@FXML
	private void OnActionClientKeyButton(ActionEvent action) {
		
	}

	
}
