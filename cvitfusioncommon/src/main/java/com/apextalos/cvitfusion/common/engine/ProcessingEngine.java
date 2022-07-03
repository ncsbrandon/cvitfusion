package com.apextalos.cvitfusion.common.engine;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
import com.apextalos.cvitfusion.common.opflow.Process;
import com.apextalos.cvitfusion.common.settings.ConfigFile;

public class ProcessingEngine {

	private static Logger logger = LogManager.getLogger(ProcessingEngine.class.getSimpleName());

	private OperationalFlow design;
	private ConfigFile cf;
	
	private List<Processor> processors = new ArrayList<>();
	
	public ProcessingEngine(OperationalFlow design, ConfigFile cf) {
		this.design = design;
		this.cf = cf;
	}
	
	public boolean start() {
		// ensure there's no active processors
		stop();
		
		// create them from the top level
		List<Process> processes = design.getProcesses();
		processes.forEach(process -> createProcessRecur(process, null));
		
		// start everyone
		processors.forEach(Processor::start);
		
		// tbd
		return true;
	}
	
	public void stop() {
		// stop everyone
		processors.forEach(Processor::stop);
		
		// clear the list
		processors.clear();
	}

	private void createProcessRecur(Process child, Processor parent) {
		if(ProcessorLidar.getType().getTypeID() == child.getTypeID()) 			loadProcessor(new ProcessorLidar(child, cf), parent, child);		
		if(ProcessorWWVD.getType().getTypeID() == child.getTypeID()) 			loadProcessor(new ProcessorWWVD(child, cf), parent, child);		
		if(ProcessorFB.getType().getTypeID() == child.getTypeID()) 				loadProcessor(new ProcessorFB(child, cf), parent, child);
		if(ProcessorDigitalOutput.getType().getTypeID() == child.getTypeID()) 	loadProcessor(new ProcessorDigitalOutput(child, cf), parent, child);
	}
	
	private void loadProcessor(Processor processor, Processor parent, Process child) {
		logger.debug("creating {}", processor.getClass().getSimpleName());
		processors.add(processor);
		if(parent != null)
			parent.addListener(parent);
		if(child.hasChildren())
			child.getChildren().forEach(process -> createProcessRecur(process, parent));
	}
	
	public Processor getProcessor(int processID) {
		for(Processor processor : processors) {
			if(processID == processor.getProcess().getProcessID())
				return processor;
		}
		
		return null;
	}
}
