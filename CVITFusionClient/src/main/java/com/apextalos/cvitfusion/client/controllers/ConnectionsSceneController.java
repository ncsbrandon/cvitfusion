package com.apextalos.cvitfusion.client.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.client.models.ConnectionsSceneModel;
import com.apextalos.cvitfusion.client.scene.SceneManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConnectionsSceneController extends BaseController {

	private static final Logger logger = LogManager.getLogger(ConnectionsSceneController.class.getSimpleName());

	// Model
	private ConnectionsSceneModel model;

	public ConnectionsSceneModel getModel() {
		return model;
	}
	
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
		//savePosition(null);
		end();
		
		
		
		
		try {
			SceneManager.getInstance(null, null).showMain();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void OnActionCancelButton(ActionEvent action) {
		action.consume();
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

	@Override
	public void loadPosition(Stage stage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void savePosition(Stage stage) {
		// TODO Auto-generated method stub
		
	}
}
