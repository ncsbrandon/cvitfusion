package com.apextalos.cvitfusion.common.opflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class OperationalFlow {

	private List<Process> processes;
	private List<Type> types;
	private List<Style> styles;
	private Map<Integer, Integer> typeStyleMap;

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

	public Map<Integer, Integer> getTypeStyleMap() {
		return typeStyleMap;
	}

	public void setTypeStyleMap(Map<Integer, Integer> typeStyleMap) {
		this.typeStyleMap = typeStyleMap;
	}

	public OperationalFlow() {
	}
	
	public OperationalFlow(List<Process> processes, List<Type> types, List<Style> styles, Map<Integer, Integer> typeStyleMap) {
		super();
		this.processes = processes;
		this.types = types;
		this.styles = styles;
		this.typeStyleMap = typeStyleMap;
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
		if(process == null)
			return false;
		
		if(processes.removeIf(x -> x.equals(process)))
			return true;
		
		for (Process procIter : processes) {
			if(procIter.getChildren() != null && removeProcessRecur(procIter.getChildren(), process)) {
				return true;
			}
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
		if (!typeStyleMap.containsKey(typeID))
			return null;

		return lookupStyle(typeStyleMap.get(typeID));
	}
	
	@JsonIgnore
	public String validate() {
		// let's validate that type ID is unique
		// not optimized - who cares
		for(Type type1 : getTypes()) {
			for(Type type2 : getTypes()) {
				if(type1 != type2 && type1.getTypeID() == type2.getTypeID()) {
					// duplicate ID found
					return String.format("Duplicate Type ID [%d]", type1.getTypeID());
				}
			}
		}
		
		// name is required
		for(Type type1 : getTypes()) {
			if(type1.getName().isBlank()) {
				return String.format("Type ID [%d] name cannot be blank", type1.getTypeID());
			}
		}
		
		// all supported outputs exist
		/*
		for(Type type1 : getTypes()) {
			if(type1.hasSupportedOutputs()) {
				List<Integer> outputIDs = type1.getSupportedOutputs();
				for(Integer outputID : outputIDs) {
					Type type2 = lookupType(outputID);
					if(type2 == null) {
						return String.format("Unable to locate the supported output Type ID [%d] for Type [%d] ID [%s]", outputID, type1.getTypeID(), type1.getName());
					}
				}				
			}			
		}
		*/
		
		// let's validate that style ID is unique
		// not optimized - who cares
		for(Style style1 : getStyles()) {
			for(Style style2 : getStyles()) {
				if(style1 != style2 && style1.getStyleID() == style2.getStyleID()) {
					// duplicate ID found
					return String.format("Duplicate Style ID [%d]", style1.getStyleID());
				}
			}
		}
		
		// fill and font colors are required
		for(Style style1 : getStyles()) {
			if(style1.getFill() == null)
				return String.format("Style ID [%d] missing fill", style1.getStyleID());
			if(style1.getFont() == null)
				return String.format("Style ID [%d] missing font", style1.getStyleID());
		}
		
		// check that all map entries point
		for(Map.Entry<Integer, Integer> kvp : typeStyleMap.entrySet()) {
			//if(null == lookupType(kvp.getKey()))
			//	return String.format("Unable to locate the mapped Type ID [%d]", kvp.getKey());
			if(null == lookupStyle(kvp.getValue()))
				return String.format("Unable to locate the mapped Style ID [%d]", kvp.getValue());
		}
		
		// check that every type has a style
		for(Type type1 : getTypes()) {
			if(null == lookupStyleForType(type1.getTypeID()))
				return String.format("Unable to locate a Style for Type ID [%d]", type1.getTypeID());
		}
		
		// let's validate that process ID is unique
		// not optimized - who cares
		for(Process process1 : getProcesses()) {
			for(Process process2 : getProcesses()) {
				if(process1 != process2 && process1.getProcessID() == process2.getProcessID()) {
					// duplicate ID found
					return String.format("Duplicate Process ID [%d]", process1.getProcessID());
				}
			}
		}
		
		// check that every process has a type
		for(Process process1 : getProcesses()) {
			if(null == lookupType(process1.getTypeID()))
				return String.format("Unable to locate a Type for Process ID [%d]", process1.getProcessID());
		}
		
		return "";
	}
	
	@JsonIgnore
	public void clearChanges() {
		clearChangesRecur(processes);
	}
	
	@JsonIgnore
	private boolean clearChangesRecur(List<Process> processes) {
				
		for (Process procIter : processes) {
			
			procIter.clearChangedProperties();
			
			if(procIter.getChildren() != null && clearChangesRecur(procIter.getChildren())) {
				return true;
			}
		}
		
		return false;
	}
}
