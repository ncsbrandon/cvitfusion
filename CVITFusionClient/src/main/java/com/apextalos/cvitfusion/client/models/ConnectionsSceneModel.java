package com.apextalos.cvitfusion.client.models;

import java.util.HashMap;
import java.util.Map;

public class ConnectionsSceneModel {

	private Map<String, Connection> sessionsMap = new HashMap<>();	
	//private String currentName = "";
	//private Connection currentSelection = new Connection();
	
	public Map<String, Connection> getSessionsMap() {
		return sessionsMap;
	}
	public void setSessionsMap(Map<String, Connection> connectionMap) {
		this.sessionsMap = connectionMap;
	}
	/*
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
	*/
	
	public ConnectionsSceneModel() {
		
	}
	
	public ConnectionsSceneModel(Map<String, Connection> sessionsMap, String currentName,
			Connection currentSelection) {
		super();
		this.sessionsMap = sessionsMap;
		//this.currentName = currentName;
		//this.currentSelection = currentSelection;
	}
	
	public void sessionsFromJSON(String json) {
		
	}
	
	public String sessionsToJSON() {
		return "";
	}
}
