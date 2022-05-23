package com.apextalos.cvitfusion.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.client.models.ConnectionsSceneModel;
import com.apextalos.cvitfusion.client.models.MainSceneModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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
		
	}
	
	@FXML
	private void OnActionCancelButton(ActionEvent action) {
		
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
