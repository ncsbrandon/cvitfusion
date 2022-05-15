package com.apextalos.cvitfusion.common.opflow;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OperationalFlow {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	private List<Process> processes;
	private List<Type> types;
	private List<Style> styles;
	private Map<Integer, Integer> typeStyle;
	
	public List<Process> getProcesses() {
		return processes;
	}
	public void setProcesses(List<Process> processes) {
		this.processes = processes;
	}
	public List<Type> getTypes() {
		return types;
	}
	public void setTypes(List<Type> types) {
		this.types = types;
	}
	public List<Style> getStyles() {
		return styles;
	}
	public void setStyles(List<Style> styles) {
		this.styles = styles;
	}
	public Map<Integer, Integer> getTypeStyle() {
		return typeStyle;
	}
	public void setTypeStyle(Map<Integer, Integer> typeStyle) {
		this.typeStyle = typeStyle;
	}
	
	public OperationalFlow(List<Process> processes, List<Type> types, List<Style> styles, Map<Integer, Integer> typeStyle) {
		super();
		this.processes = processes;
		this.types = types;
		this.styles = styles;
		this.typeStyle = typeStyle;
	}
	
	@JsonIgnore
	public static String toJSON(OperationalFlow instance) {
		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(instance);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	@JsonIgnore
	public static OperationalFlow fromJSON(String json) {
		try {
			return mapper.readValue(json, OperationalFlow.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
