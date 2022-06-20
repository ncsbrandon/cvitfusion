package com.apextalos.cvitfusion.common.opflow;

import java.util.List;

public class Type {

	private int typeID;
	private int version;
	private String name;
	private List<Parameter> parameters;
	private List<Integer> supportedOutputs;
	private boolean isTopLevel;

	public int getTypeID() {
		return typeID;
	}

	public void setTypeID(int typeID) {
		this.typeID = typeID;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public boolean hasSupportedOutputs() {
		return supportedOutputs != null && !supportedOutputs.isEmpty();
	}

	public List<Integer> getSupportedOutputs() {
		return supportedOutputs;
	}

	public void setSupportedOutputs(List<Integer> supportedOutputs) {
		this.supportedOutputs = supportedOutputs;
	}
	
	public boolean getIsTopLevel() {
		return isTopLevel;
	}
	
	public void setIsTopLevel(boolean isTopLevel) {
		this.isTopLevel = isTopLevel;
	}

	public Type() {
	}
	
	public Type(int typeID, int version, String name, List<Parameter> parameters, 
			List<Integer> supportedOutputs, boolean isTopLevel) {
		super();
		this.typeID = typeID;
		this.version = version;
		this.name = name;
		this.parameters = parameters;
		this.supportedOutputs = supportedOutputs;
		this.isTopLevel = isTopLevel;
	}
}
