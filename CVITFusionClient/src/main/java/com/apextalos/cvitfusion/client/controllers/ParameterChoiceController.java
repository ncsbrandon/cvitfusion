package com.apextalos.cvitfusion.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.apextalos.cvitfusion.common.opflow.Parameter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class ParameterChoiceController extends ParameterBaseController {

    @FXML private Label descLabel;
    @FXML private ComboBox<?> valueCombobox;

    @Override
	void updateParameter(Parameter parameter) {
		descLabel.setText(parameter.getDescription());
	}
    
    @FXML void onValuesCombo(ActionEvent event) {
    	
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@Override
	public void onActionPerformed(Object o, EventType et) {

	}
}
