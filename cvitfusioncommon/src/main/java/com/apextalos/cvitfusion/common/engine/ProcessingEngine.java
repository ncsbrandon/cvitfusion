package com.apextalos.cvitfusion.common.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.license.License;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
import com.apextalos.cvitfusion.common.settings.ConfigFile;

public class ProcessingEngine {

	private static Logger logger = LogManager.getLogger(ProcessingEngine.class.getSimpleName());

	private OperationalFlow design;
	private ConfigFile cf;
	private License lic;
	
	public ProcessingEngine(OperationalFlow design, ConfigFile cf, License lic) {
		this.design = design;
		this.cf = cf;
		this.lic = lic;
	}
	
	public boolean start() {
		createTopLevels();
		
		return true;
	}
	
	public void stop() {
		
	}

	private void createTopLevels() {
		
	}

	
}
