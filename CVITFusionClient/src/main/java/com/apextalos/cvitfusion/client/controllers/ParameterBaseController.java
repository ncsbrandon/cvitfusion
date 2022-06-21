package com.apextalos.cvitfusion.client.controllers;

import com.apextalos.cvitfusion.common.opflow.Parameter;

public abstract class ParameterBaseController extends BaseController {

	Parameter parameter = null;	
	public Parameter getParameter() {
		return parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
		updateParameter(parameter);
	}
	
	abstract void updateParameter(Parameter parameter);
}
