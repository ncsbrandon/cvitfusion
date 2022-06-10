package com.apextalos.cvitfusion.common.opflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class OperationalFlow {

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

	public OperationalFlow() {
	}
	
	public OperationalFlow(List<Process> processes, List<Type> types, List<Style> styles, Map<Integer, Integer> typeStyle) {
		super();
		this.processes = processes;
		this.types = types;
		this.styles = styles;
		this.typeStyle = typeStyle;
	}

	@JsonIgnore
	public Process lookupProcess(int id) {
		// top level procs
		for (Process p : processes) {
			if (p.getProcessID() == id)
				return p;
			if (p.hasChildren()) {
				Process child = lookupProcessRecur(id, p);
				if(child != null)
					return child;
			}
		}
		return null;
	}
	
	@JsonIgnore
	private Process lookupProcessRecur(int id, Process process) {
		for (Process p : process.getChildren()) {
			if (p.getProcessID() == id)
				return p;
			if (p.hasChildren()) {
				Process child = lookupProcessRecur(id, p);
				if(child != null)
					return child;
			}
		}
		return null;
	}
	
	@JsonIgnore
	public boolean removeProcess(Process process) {
		return removeProcessRecur(processes, process);
	}
	
	@JsonIgnore
	public boolean removeProcessRecur(List<Process> processes, Process process) {
		if(processes.removeIf(x -> x.equals(process)))
			return true;
		
		for (Process procIter : processes) {
			if(removeProcessRecur(procIter.getChildren(), process))
				return true;
		}
		
		return false;
	}
	
	@JsonIgnore
	public Type lookupType(int id) {
		for (Type t : types) {
			if (t.getTypeID() == id)
				return t;
		}
		return null;
	}
	
	@JsonIgnore
	public List<Type> getTopLevelTypes() {
		List<Type> topLevelTypes = new ArrayList<>();
		for (Type t : types) {
			if (t.getIsTopLevel())
				topLevelTypes.add(t);
		}
		return topLevelTypes;
	}

	@JsonIgnore
	public Style lookupStyle(int id) {
		for (Style s : styles) {
			if (s.getStyleID() == id)
				return s;
		}
		return null;
	}

	@JsonIgnore
	public Style lookupStyleForType(int typeID) {
		if (!typeStyle.containsKey(typeID))
			return null;

		return lookupStyle(typeStyle.get(typeID));
	}
}
