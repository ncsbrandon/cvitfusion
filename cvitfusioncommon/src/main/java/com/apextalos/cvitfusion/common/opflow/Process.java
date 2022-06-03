package com.apextalos.cvitfusion.common.opflow;

import java.util.List;
import java.util.Properties;

public class Process {

	private int processID;
	private boolean enabled;
	private int typeID;
	private List<Process> children;
	private String notes;
	private int status;
	private Properties properties;

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Process() {
	}
	
	public Process(int processID, boolean enabled, int typeID, List<Process> children, String notes, int status, Properties properties) {
		super();
		this.processID = processID;
		this.enabled = enabled;
		this.typeID = typeID;
		this.children = children;
		this.notes = notes;
		this.status = status;
		this.properties = properties;
	}
}
