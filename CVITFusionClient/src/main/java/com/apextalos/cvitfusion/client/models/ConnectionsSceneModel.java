package com.apextalos.cvitfusion.client.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConnectionsSceneModel {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	private Map<String, Connection> sessionsMap = new HashMap<>();	
	
	public Map<String, Connection> getSessionsMap() {
		return sessionsMap;
	}
	public void setSessionsMap(Map<String, Connection> connectionMap) {
		this.sessionsMap = connectionMap;
	}
	
	public boolean sessionsFromJSON(String json) {
		try {
			sessionsMap = mapper.readValue(json, new TypeReference<Map<String, Connection>>(){});
			return true;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String sessionsToJSON() {
		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(sessionsMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}
}
