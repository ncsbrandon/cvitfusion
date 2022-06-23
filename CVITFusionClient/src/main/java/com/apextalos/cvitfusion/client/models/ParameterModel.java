package com.apextalos.cvitfusion.client.models;

import com.apextalos.cvitfusion.common.opflow.Parameter;
import com.apextalos.cvitfusion.common.opflow.Type;
import com.apextalos.cvitfusion.common.opflow.Process;

import javafx.beans.property.SimpleStringProperty;

public class ParameterModel {
	private final SimpleStringProperty key;
	private final SimpleStringProperty value;
	
	private final Parameter parameter;
	private final Process process;
	private final Type type;

	public ParameterModel(Parameter parameter, Type type, Process process, String value) {
		this.key = new SimpleStringProperty(parameter.getDescription());
		this.value = new SimpleStringProperty(value);
		this.parameter = parameter;
		this.type = type;
		this.process = process;
	}
	
	public ParameterModel(String key, String value) {
		this.key = new SimpleStringProperty(key);
		this.value = new SimpleStringProperty(value);
		this.parameter = null;
		this.type = null;
		this.process = null;
	}

	public String getKey() {
		return key.get();
	}

	public String getValue() {
		return value.get();
	}

	public void setValue(String value) {
		this.value.set(value);
		
		if(process == null || parameter == null)
			return;
		
		process.putChangedProperty(parameter.getParameterID());
	}
	
	public Parameter getParameter() {
		return parameter;
	}
	
	public Process getProcess() {
		return process;
	}
	
	public Type getType() {
		return type;
	}
	
	public boolean isChanged() {
		if(process == null || parameter == null)
			return false;
		
		return process.isChangedProperty(parameter.getParameterID());
	}
}
