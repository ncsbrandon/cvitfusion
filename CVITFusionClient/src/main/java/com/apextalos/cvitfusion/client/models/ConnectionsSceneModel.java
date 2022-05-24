package com.apextalos.cvitfusion.client.models;

import java.util.HashMap;
import java.util.Map;

public class ConnectionsSceneModel {

	private Map<String, Connection> connectionMap = new HashMap<>();	
	private String currentName;
	private Connection currentSelection;
	
	public Map<String, Connection> getConnectionMap() {
		return connectionMap;
	}
	public void setConnectionMap(Map<String, Connection> connectionMap) {
		this.connectionMap = connectionMap;
	}
	public String getCurrentName() {
		return currentName;
	}
	public void setCurrentName(String currentName) {
		this.currentName = currentName;
	}
	public Connection getCurrentSelection() {
		return currentSelection;
	}
	public void setCurrentSelection(Connection currentSelection) {
		this.currentSelection = currentSelection;
	}
	
	public ConnectionsSceneModel() {
		
	}
	
	public ConnectionsSceneModel(Map<String, Connection> connectionMap, String currentName,
			Connection currentSelection) {
		super();
		this.connectionMap = connectionMap;
		this.currentName = currentName;
		this.currentSelection = currentSelection;
	}
	
	public void fromJSON() {
		
	}
	
	public void toJSON() {
		
	}
}
