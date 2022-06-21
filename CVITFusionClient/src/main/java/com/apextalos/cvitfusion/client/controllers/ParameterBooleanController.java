package com.apextalos.cvitfusion.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.apextalos.cvitfusion.common.opflow.Parameter;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class ParameterBooleanController extends ParameterBaseController {

	@FXML private Label descLabel;
	@FXML private CheckBox valueCheckbox;
	    
	@Override
	void updateParameter(Parameter parameter) {
		descLabel.setText(parameter.getDescription());
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActionPerformed(Object o, EventType et) {
		// TODO Auto-generated method stub
		
	}
}
