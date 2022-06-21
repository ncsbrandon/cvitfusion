package com.apextalos.cvitfusion.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.apextalos.cvitfusion.common.opflow.Parameter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class ParameterBooleanController extends ParameterBaseController {

	@FXML
	private CheckBox valueCheckbox;
  
	@Override
	void updateParameter(Parameter parameter) {
		valueCheckbox.setText(parameter.getDescription());
	}
	
	@Override
	void updateValue(String value) {
		valueCheckbox.setSelected(Boolean.parseBoolean(value));
	}
	
	@FXML
    void onValueCheckbox(ActionEvent event) {
    	valueChanged(String.valueOf(valueCheckbox.isSelected()));
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@Override
	public void onActionPerformed(Object o, EventType et) {
	}
}
