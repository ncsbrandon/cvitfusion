package com.apextalos.cvitfusion.common.opflow;

import java.util.List;
import java.util.Properties;

public class Type {

	private int typeID;
	private int version;
	private String name;
	private Properties properties;
	private List<Integer> supportedInputs;
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

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public boolean hasSupportedInputs() {
		return supportedInputs != null && !supportedInputs.isEmpty();
	}

	public List<Integer> getSupportedInputs() {
		return supportedInputs;
	}

	public void setSupportedInputs(List<Integer> supportedInputs) {
		this.supportedInputs = supportedInputs;
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
	
	public Type(int typeID, int version, String name, Properties properties, 
			List<Integer> supportedInputs, List<Integer> supportedOutputs, boolean isTopLevel) {
		super();
		this.typeID = typeID;
		this.version = version;
		this.name = name;
		this.properties = properties;
		this.supportedInputs = supportedInputs;
		this.supportedOutputs = supportedOutputs;
		this.isTopLevel = isTopLevel;
	}
}
