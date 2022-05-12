package com.apextalos.cvitfusion.common.opflow;

import java.util.List;
import java.util.Map;

public class OperationalFlow {

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
}
