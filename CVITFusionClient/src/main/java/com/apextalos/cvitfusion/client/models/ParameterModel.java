package com.apextalos.cvitfusion.client.models;

import com.apextalos.cvitfusion.common.opflow.Parameter;

import javafx.beans.property.SimpleStringProperty;

public class ParameterModel {
	private final SimpleStringProperty key;
	private final SimpleStringProperty value;
	private final Parameter parameter;

	public ParameterModel(Parameter parameter, String value) {
		this.key = new SimpleStringProperty(parameter.getDescription());
		this.value = new SimpleStringProperty(value);
		this.parameter = parameter;
	}
	
	public ParameterModel(String key, String value) {
		this.key = new SimpleStringProperty(key);
		this.value = new SimpleStringProperty(value);
		this.parameter = null;
	}

	public String getKey() {
		return key.get();
	}

	public String getValue() {
		return value.get();
	}

	public void setValue(String value) {
		this.value.set(value);
	}
	
	public Parameter getParameter() {
		return parameter;
	}
}
