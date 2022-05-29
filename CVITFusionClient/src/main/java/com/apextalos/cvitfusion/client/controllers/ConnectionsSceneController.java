package com.apextalos.cvitfusion.client.controllers;

import java.io.File;
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
import javafx.scene.control.Label;
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
	@FXML private Label errorMessageLabel;
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		model = new ConnectionsSceneModel();
				
		sessionList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// get the session from the map
				Connection session = model.getSessionsMap().get(newValue);
				if(session == null) {
					logger.debug("null session selected");
					return;
				}
				
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
		
		// clear the error message
		showError("");
		
		// load the json
		String json = cf.getString(ConfigItems.CONNECTIONS_SESSIONLIST_CONFIG, ConfigItems.CONNECTIONS_SESSIONLIST_DEFAULT);
		if(!json.isBlank())
			model.sessionsFromJSON(json);
				
		// fill the list
		fillSessionsList();
		
		// if we have items, select the first one
		if(!sessionList.getItems().isEmpty())
			sessionList.getSelectionModel().select(sessionList.getItems().get(0));
			
	}

	@Override
	public void end() {
		super.end();
		
		Stage stage = (Stage)topVbox.getScene().getWindow();
		if(stage == null)
			return;
		
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
	private void onActionDeleteButton(ActionEvent action) {
		// update the list
		model.getSessionsMap().remove(nameTextField.getText());
		
		// update settings
		cf.setString(ConfigItems.CONNECTIONS_SESSIONLIST_CONFIG, model.sessionsToJSON(), false);
				
		// re-render the list
		fillSessionsList();
		
		showError("");
	}
	
	@FXML
	private void onActionSaveButton(ActionEvent action) {
		// cannot save blank name
		if(nameTextField.getText().isBlank())
			return;
		
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
		cf.setString(ConfigItems.CONNECTIONS_SESSIONLIST_CONFIG, model.sessionsToJSON(), false);
		
		// re-render the list
		fillSessionsList();
		
		showError("");
	}
	
	private void fillSessionsList() {
		// is there a current selection?
		String currentSelection = sessionList.getSelectionModel().getSelectedItem();
		
		// clear the list
		sessionList.getItems().clear();
		
		// sort the session list from the model
		ArrayList<String> names = new ArrayList<>(model.getSessionsMap().keySet());	
		Collections.sort(names, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}
		});
		
		// fill the list
		sessionList.getItems().addAll(names);
		
		// return the selection
		if(currentSelection != null && !currentSelection.isBlank() && names.contains(currentSelection))
			sessionList.getSelectionModel().select(currentSelection);
	}
	
	@FXML
	private void onActionConnectButton(ActionEvent action) {
		// validate settings
		if(nameTextField.getText().isBlank()) {
			showError("Name cannot be blank");
			return;
		} else if(urlTextField.getText().isBlank()) {
			showError("URL cannot be blank");
			return;
		} else if(clientIdTextField.getText().isBlank()) {
			showError("Client ID cannot be blank");
			return;
		}
		
		// save any changes
		onActionSaveButton(action);
		
		// update the config to say this is the connection we are using
		cf.setString(ConfigItems.CONNECTIONS_ACTIVESESSION_CONFIG, nameTextField.getText(), false);
				
		// once the main scene loads, it will create a MQTT client with these:
		cf.setString(com.apextalos.cvitfusion.common.settings.ConfigItems.CONFIG_MQTT_BROKER, urlTextField.getText(), false);
		cf.setString(com.apextalos.cvitfusion.common.settings.ConfigItems.CONFIG_MQTT_CLIENTID, clientIdTextField.getText(), false);
		if(tlsEnabledCheckBox.isSelected()) {
			cf.setString(com.apextalos.cvitfusion.common.settings.ConfigItems.CONFIG_MQTT_CACERT, caCertTextField.getText(), false);
			cf.setString(com.apextalos.cvitfusion.common.settings.ConfigItems.CONFIG_MQTT_CLIENTCERT, clientCertTextField.getText(), false);
			cf.setString(com.apextalos.cvitfusion.common.settings.ConfigItems.CONFIG_MQTT_CLIENTKEY, clientKeyTextField.getText(), false);
		} else {
			cf.setString(com.apextalos.cvitfusion.common.settings.ConfigItems.CONFIG_MQTT_CACERT, "", true);
			cf.setString(com.apextalos.cvitfusion.common.settings.ConfigItems.CONFIG_MQTT_CLIENTCERT, "", true);
			cf.setString(com.apextalos.cvitfusion.common.settings.ConfigItems.CONFIG_MQTT_CLIENTKEY, "", true);
		}
		if(pwdEnabledCheckBox.isSelected()) {
			cf.setString(com.apextalos.cvitfusion.common.settings.ConfigItems.CONFIG_MQTT_USERNAME, usernameTextField.getText(), false);
			cf.setString(com.apextalos.cvitfusion.common.settings.ConfigItems.CONFIG_MQTT_PASSWORD, passwordTextField.getText(), false);
		} else {
			cf.setString(com.apextalos.cvitfusion.common.settings.ConfigItems.CONFIG_MQTT_USERNAME, "", true);
			cf.setString(com.apextalos.cvitfusion.common.settings.ConfigItems.CONFIG_MQTT_PASSWORD, "", true);
		}
		
		// change to the main scene
		Stage stage = (Stage)topVbox.getScene().getWindow();
		SceneManager.getInstance(cf).showMain(stage);
	}
	
	private void showError(String message) {
		errorMessageLabel.setText(message);
	}
	
	@FXML
	private void onActionCancelButton(ActionEvent action) {
		// close
		Stage stage = (Stage)topVbox.getScene().getWindow();
		SceneManager.getInstance(cf).close(stage);
	}
	
	@FXML
	private void onActionCaCertButton(ActionEvent action) {
		Stage stage = (Stage)topVbox.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open CA Cert File");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Certificates and Keys", "*.ca", "*.pem", "*.key", "*.cer")
            );
		File f = fileChooser.showOpenDialog(stage);
		if(f != null)
			caCertTextField.setText(f.getAbsolutePath());
	}
	
	@FXML
	private void onActionClientCertButton(ActionEvent action) {
		Stage stage = (Stage)topVbox.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open CA Cert File");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Certificates and Keys", "*.ca", "*.pem", "*.key", "*.cer")
            );
		File f = fileChooser.showOpenDialog(stage);
		if(f != null)
			clientCertTextField.setText(f.getAbsolutePath());
	}
	
	@FXML
	private void onActionClientKeyButton(ActionEvent action) {
		Stage stage = (Stage)topVbox.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Client Key File");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Certificates and Keys", "*.ca", "*.pem", "*.key", "*.cer")
            );
		File f = fileChooser.showOpenDialog(stage);
		if(f != null)
			clientKeyTextField.setText(f.getAbsolutePath());
	}
	
	@FXML
	private void onMenuWindowReset(ActionEvent action) {
		cf.removeKey(ConfigItems.CONNECTIONS_POSITION_X_CONFIG);
		cf.removeKey(ConfigItems.CONNECTIONS_POSITION_Y_CONFIG);
		cf.removeKey(ConfigItems.CONNECTIONS_WIDTH_CONFIG);
		cf.removeKey(ConfigItems.CONNECTIONS_HEIGHT_CONFIG);
		
		cf.removeKey(ConfigItems.MAIN_POSITION_X_CONFIG);
		cf.removeKey(ConfigItems.MAIN_POSITION_Y_CONFIG);
		cf.removeKey(ConfigItems.MAIN_WIDTH_CONFIG);
		cf.removeKey(ConfigItems.MAIN_HEIGHT_CONFIG);			
		cf.removeKey(ConfigItems.MAIN_MAXIMIZED_CONFIG);		
		cf.removeKey(ConfigItems.MAIN_SP1_DIV_POS_CONFIG);
		cf.removeKey(ConfigItems.MAIN_SP11_DIV_POS_CONFIG);
		cf.removeKey(ConfigItems.MAIN_SP112_DIV_POS_CONFIG);
		
		cf.save();
		
		System.exit(0);
	}
}
