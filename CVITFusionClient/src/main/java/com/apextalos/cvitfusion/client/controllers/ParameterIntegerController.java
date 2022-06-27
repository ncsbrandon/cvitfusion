package com.apextalos.cvitfusion.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.apextalos.cvitfusion.common.opflow.Parameter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;

public class ParameterIntegerController extends ParameterBaseController {

	@FXML private Label descLabel;
	@FXML private TextField valueTextfield;
	
	@Override
	void updateParameter(Parameter parameter) {
		descLabel.setText(parameter.getDescription());
		valueTextfield.setTooltip(new Tooltip(parameter.getTooltip()));
	}
	
	@Override
	void updateValue(String value) {
		valueTextfield.setText(value);
	}
	
	@FXML void onValueTextfield(ActionEvent event) {
		valueChanged(valueTextfield.getText());
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// link Controller to View - ensure only numeric input (integers) in text field
		valueTextfield.setTextFormatter(new TextFormatter<>(change -> {
			if (change.getText().matches("\\d+") || change.getText().equals("")) {
				return change;
			}

			change.setText("");
			change.setRange(change.getRangeStart(), change.getRangeStart());
			return change;
		}));
	}

	@Override
	public void onActionPerformed(Object o, EventType et) {
	}
}
