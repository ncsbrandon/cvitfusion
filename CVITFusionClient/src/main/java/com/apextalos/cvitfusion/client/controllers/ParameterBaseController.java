package com.apextalos.cvitfusion.client.controllers;

import com.apextalos.cvitfusion.client.models.ParameterModel;
import com.apextalos.cvitfusion.common.opflow.Parameter;
import com.apextalos.cvitfusion.common.opflow.Process;

public abstract class ParameterBaseController extends BaseController {

	ParameterModel parameterModel = null;	
	public ParameterModel getParameterModel() {
		return parameterModel;
	}

	public void setParameterModel(ParameterModel parameterModel) {
		this.parameterModel = parameterModel;
		
		// value
		Process process = parameterModel.getProcess();
		Parameter parameter = parameterModel.getParameter();
		String value = process.getPropertyValue(parameter.getParameterID());
		updateValue(value);
		
		// label, choices, and validations
		updateParameter(parameterModel.getParameter());
	}
	
	abstract void updateParameter(Parameter parameter);
	abstract void updateValue(String value);
	
	protected void valueChanged(String value) {
		Process process = parameterModel.getProcess();
		Parameter parameter = parameterModel.getParameter();
		process.setPropertyValue(parameter.getParameterID(), value);
		parameterModel.setValue(value);
		actionPerformed(parameterModel, EventType.VALUECHANGED);
	}
}
