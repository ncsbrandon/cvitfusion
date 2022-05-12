package com.apextalos.cvitfusion.common.opflow;

import java.util.List;
import java.util.Properties;

public class Node {

	private int nodeID;
	private String name;
	private boolean enabled;
	private int typeID;
	private List<Node> children;
	private String notes;
	private int status;
	private Properties properties;
	
	public int getNodeID() {
		return nodeID;
	}
	public void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public List<Node> getChildren() {
		return children;
	}
	public void setChildren(List<Node> children) {
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
	
	public Node(int nodeID, String name, boolean enabled, int typeID, List<Node> children, String notes, int status,
			Properties properties) {
		super();
		this.nodeID = nodeID;
		this.name = name;
		this.enabled = enabled;
		this.typeID = typeID;
		this.children = children;
		this.notes = notes;
		this.status = status;
		this.properties = properties;
	}
}
