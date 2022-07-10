package com.apextalos.cvitfusion.common.engine;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.mqtt.MqttTransceiver;
import com.apextalos.cvitfusion.common.mqtt.message.ProcessStatusResponse;
import com.apextalos.cvitfusion.common.opflow.Parameter;
import com.apextalos.cvitfusion.common.opflow.Process;
import com.apextalos.cvitfusion.common.opflow.Type;
import com.apextalos.cvitfusion.common.settings.ConfigFile;

public class ProcessorDigitalOutput extends Processor {
	
	private static Logger logger = LogManager.getLogger(ProcessorDigitalOutput.class.getSimpleName());

	private int counter = 0;
	
	public static Type getType() {
		List<Parameter> parameters = new ArrayList<>();
		
		return new Type(102, 1, "Digital Output", parameters, null, false);
	}
	
	protected ProcessorDigitalOutput(String engineID, Process process, ConfigFile cf, MqttTransceiver mt) {
		super(engineID, process, cf, mt);
	}

	@Override
	public void loop() {
		logger.info("Status {}", counter);
		counter++;
		
		
		publishStatus(String.format("Counter: %s", counter));
	}
}
