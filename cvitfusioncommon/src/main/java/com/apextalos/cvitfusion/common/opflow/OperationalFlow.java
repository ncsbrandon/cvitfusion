package com.apextalos.cvitfusion.common.opflow;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OperationalFlow {

	private final ObjectMapper mapper = new ObjectMapper();
	
	private List<Node> nodes;
	private List<Type> types;
	private List<Style> styles;
	private Map<Integer, Integer> typeStyle;
	
	public List<Node> getNodes() {
		return nodes;
	}
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
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
	
	public OperationalFlow(List<Node> nodes, List<Type> types, List<Style> styles, Map<Integer, Integer> typeStyle) {
		super();
		this.nodes = nodes;
		this.types = types;
		this.styles = styles;
		this.typeStyle = typeStyle;
	}
	
	public String toJSON() {
		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}
}
