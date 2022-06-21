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
		updateParameter(parameterModel.getParameter());
	}
	
	abstract void updateParameter(Parameter parameter);
	
	protected void valueChanged(String value) {
		Process process = parameterModel.getProcess();
		Parameter parameter = parameterModel.getParameter();
		process.setPropertyValue(parameter.getParameterID(), value);
	}
}
