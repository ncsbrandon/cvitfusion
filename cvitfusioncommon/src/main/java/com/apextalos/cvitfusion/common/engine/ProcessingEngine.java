package com.apextalos.cvitfusion.common.engine;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.mqtt.MqttTransceiver;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
import com.apextalos.cvitfusion.common.opflow.Process;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.settings.ConfigItems;

public class ProcessingEngine {

	private static Logger logger = LogManager.getLogger(ProcessingEngine.class.getSimpleName());

	private OperationalFlow design;
	private ConfigFile cf;
	
	private List<Processor> processors = new ArrayList<>();
	
	public ProcessingEngine(OperationalFlow design, ConfigFile cf) {
		this.design = design;
		this.cf = cf;
	}
	
	public boolean start(MqttTransceiver mt) {
		// ensure there's no active processors
		stop();
		
		String engineID = cf.getString(ConfigItems.DEVICE_UUID_CONFIG, ConfigItems.DEVICE_UUID_DEFAULT);
		
		// create them from the top level
		List<Process> processes = design.getProcesses();
		processes.forEach(process -> createProcessRecur(engineID, process, null, mt));
		
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

	private void createProcessRecur(String engineID, Process child, Processor parent, MqttTransceiver mt) {
		if(ProcessorLidar.getType().getTypeID() == child.getTypeID()) 			loadProcessor(new ProcessorLidar(engineID, child, cf, mt), parent, engineID, child, mt);		
		if(ProcessorWWVD.getType().getTypeID() == child.getTypeID()) 			loadProcessor(new ProcessorWWVD(engineID, child, cf, mt), parent, engineID, child, mt);		
		if(ProcessorFB.getType().getTypeID() == child.getTypeID()) 				loadProcessor(new ProcessorFB(engineID, child, cf, mt), parent, engineID, child, mt);
		if(ProcessorDigitalOutput.getType().getTypeID() == child.getTypeID()) 	loadProcessor(new ProcessorDigitalOutput(engineID, child, cf, mt), parent, engineID, child, mt);
	}
	
	private void loadProcessor(Processor processor, Processor parent, String engineID, Process child, MqttTransceiver mt) {
		logger.debug("creating {}", processor.getClass().getSimpleName());
		processors.add(processor);
		if(parent != null)
			parent.addListener(parent);
		if(child.hasChildren())
			child.getChildren().forEach(process -> createProcessRecur(engineID, process, parent, mt));
	}
	
	public Processor getProcessor(int processID) {
		for(Processor processor : processors) {
			if(processID == processor.getProcessID())
				return processor;
		}
		
		return null;
	}
}
