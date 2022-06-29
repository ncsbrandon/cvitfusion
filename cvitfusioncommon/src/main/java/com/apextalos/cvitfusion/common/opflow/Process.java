package com.apextalos.cvitfusion.common.opflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Process {

	private int processID;
	private boolean enabled;
	private int typeID;
	private List<Process> children;
	private String notes;
	private Properties properties;
	private List<String> changedProperties = new ArrayList<>();

	public int getProcessID() {
		return processID;
	}

	public void setProcessID(int processID) {
		this.processID = processID;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getTypeID() {
		return typeID;
	}

	public void setTypeID(int typeID) {
		this.typeID = typeID;
	}

	public boolean hasChildren() {
		return children != null && !children.isEmpty();
	}

	public List<Process> getChildren() {
		return children;
	}

	public void setChildren(List<Process> children) {
		this.children = children;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Process() {
	}
	
	public Process(int processID, boolean enabled, int typeID, List<Process> children, String notes, Properties properties) {
		super();
		this.processID = processID;
		this.enabled = enabled;
		this.typeID = typeID;
		this.children = children;
		this.notes = notes;
		this.properties = properties;
	}
	
	public int nextChildID() {
		int childCount = 0;
		if (children != null)
			childCount = children.size();

		return processID * 10 + childCount + 1;
	}
	
	@JsonIgnore
	public String getPropertyValue(String parameterID) {
		return properties.getProperty(parameterID);
	}
	
	@JsonIgnore
	public void setPropertyValue(String parameterID, String value) {
		properties.setProperty(parameterID, value);
	}
	
	@JsonIgnore
	public boolean isChangedProperty(String parameterID) {
		return changedProperties.contains(parameterID);
	}
	
	@JsonIgnore
	public void putChangedProperty(String parameterID) {
		if(!changedProperties.contains(parameterID))
			changedProperties.add(parameterID);
	}
	
	@JsonIgnore
	public boolean hasChangedProperties() {
		return !changedProperties.isEmpty();
	}
	
	@JsonIgnore
	public void clearChangedProperties() {
		changedProperties.clear();
	}
}
