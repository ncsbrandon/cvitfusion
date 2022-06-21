package com.apextalos.cvitfusion.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.apextalos.cvitfusion.common.opflow.Parameter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ParameterEmailController extends ParameterBaseController {

	@FXML private Label descLabel;
	@FXML private TextField valueTextfield;
	
	@Override
	void updateParameter(Parameter parameter) {
		descLabel.setText(parameter.getDescription());
	}
	
	@FXML
    void onValueTextfield(ActionEvent event) {
		valueChanged(valueTextfield.getText());
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	@Override
	public void onActionPerformed(Object o, EventType et) {
		

	}
}
