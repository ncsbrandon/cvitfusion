package com.apextalos.cvitfusion.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.apextalos.cvitfusion.common.opflow.Parameter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

public class ParameterChoiceController extends ParameterBaseController {

    @FXML private Label descLabel;
    @FXML private ComboBox<String> valueCombobox;

    @Override
	void updateParameter(Parameter parameter) {
		descLabel.setText(parameter.getDescription());
		
		valueCombobox.getItems().addAll(parameter.getChoices());
		valueCombobox.setTooltip(new Tooltip(parameter.getTooltip()));
	}
    
    @Override
	void updateValue(String value) {
    	valueCombobox.getSelectionModel().select(value);
	}
    
    @FXML void onValueCombobox(ActionEvent event) {
    	valueChanged(valueCombobox.getSelectionModel().getSelectedItem());
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@Override
	public void onActionPerformed(Object o, EventType et) {
	}
}
