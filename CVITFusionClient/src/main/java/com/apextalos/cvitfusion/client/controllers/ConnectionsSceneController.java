package com.apextalos.cvitfusion.client.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.client.app.ConfigItems;
import com.apextalos.cvitfusion.client.models.Connection;
import com.apextalos.cvitfusion.client.models.ConnectionsSceneModel;
import com.apextalos.cvitfusion.client.scene.SceneManager;
import com.apextalos.cvitfusion.common.settings.ConfigFile;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
	@FXML private TextField clientIdTextField;
	@FXML private CheckBox tlsEnabledCheckBox;
	@FXML private TextField caCertTextField;
	@FXML private TextField clientCertTextField;
	@FXML private TextField clientKeyTextField;
	@FXML private CheckBox pwdEnabledCheckBox;
	@FXML private TextField usernameTextField;
	@FXML private TextField passwordTextField;
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		model = new ConnectionsSceneModel();
		
		sessionList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// get the session from the map
				Connection session = model.getSessionsMap().get(newValue);
				
				// fill the fields
				nameTextField.setText(newValue);
				urlTextField.setText(session.getUrl());
				clientIdTextField.setText(session.getClientId());
				tlsEnabledCheckBox.setSelected(session.isUseTls());
				caCertTextField.setText(session.getCaCertFile());
				clientCertTextField.setText(session.getClientCertFile());
				clientKeyTextField.setText(session.getClientKeyFile());
				pwdEnabledCheckBox.setSelected(session.isUsePassword());
				usernameTextField.setText(session.getUsername());
				passwordTextField.setText(session.getPassword());
			}
		});
		
		fillSessionsList();
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
		
		// stage other
		stage.setMaximized(false);
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
	}

	@Override
	public void onActionPerformed(Object o, EventType et) {
		// N/A
	}

	@FXML
	private void OnActionDeleteButton(ActionEvent action) {
		// update the list
		model.getSessionsMap().remove(nameTextField.getText());
		
		// update settings
		cf.setString(ConfigItems.CONNECTIONS_SESSIONLIST, model.sessionsToJSON(), false);
				
		// re-render the list
		fillSessionsList();
	}
	
	@FXML
	private void OnActionSaveButton(ActionEvent action) {
		// load from the fields
		Connection current = new Connection();
		current.setUrl(urlTextField.getText());
		current.setClientId(clientIdTextField.getText());
		current.setUseTls(tlsEnabledCheckBox.isSelected());
		current.setCaCertFile(caCertTextField.getText());
		current.setClientCertFile(clientCertTextField.getText());
		current.setClientKeyFile(clientKeyTextField.getText());
		current.setUsePassword(pwdEnabledCheckBox.isSelected());
		current.setUsername(usernameTextField.getText());
		current.setPassword(passwordTextField.getText());
		
		// update the list
		model.getSessionsMap().put(nameTextField.getText(), current);
		
		// update settings
		cf.setString(ConfigItems.CONNECTIONS_SESSIONLIST, model.sessionsToJSON(), false);
		
		// re-render the list
		fillSessionsList();
	}
	
	
	private void fillSessionsList() {
		sessionList.getItems().clear();
		
		ArrayList<String> names = new ArrayList<>(model.getSessionsMap().keySet());	
		Collections.sort(names, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}
		});
		
		for(String name : names) {
			sessionList.getItems().add(name);
		}

	}
	
	
	@FXML
	private void OnActionConnectButton(ActionEvent action) {
		// unsaved changes?
		
		// update the config with this current connection
		
		// change to the main scene
		try {
			Stage stage = (Stage)topVbox.getScene().getWindow();
			SceneManager.getInstance(cf).showMain(stage);
		} catch (IOException e) {
			logger.error("Unable to change to the main scene: " + e.getMessage());
		}
	}
	
	
	@FXML
	private void OnActionCancelButton(ActionEvent action) {
		// close
		Stage stage = (Stage)topVbox.getScene().getWindow();
		SceneManager.getInstance(cf).close(stage);
	}
	
	
	@FXML
	private void OnActionCaCertButton(ActionEvent action) {
		Stage stage = (Stage)topVbox.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open CA Cert File");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Certificate", "*.ca")
            );
		File f = fileChooser.showOpenDialog(stage);
		if(f != null)
			caCertTextField.setText(f.getAbsolutePath());
	}
	
	@FXML
	private void OnActionClientCertButton(ActionEvent action) {
		Stage stage = (Stage)topVbox.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open CA Cert File");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Certificate", "*.ca")
            );
		File f = fileChooser.showOpenDialog(stage);
		if(f != null)
			clientCertTextField.setText(f.getAbsolutePath());
	}
	
	@FXML
	private void OnActionClientKeyButton(ActionEvent action) {
		Stage stage = (Stage)topVbox.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Client Key File");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Key", "*.key")
            );
		File f = fileChooser.showOpenDialog(stage);
		if(f != null)
			clientKeyTextField.setText(f.getAbsolutePath());
	}
}
