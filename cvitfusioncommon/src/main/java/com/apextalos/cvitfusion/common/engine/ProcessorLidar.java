package com.apextalos.cvitfusion.common.engine;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.mqtt.MqttTransceiver;
import com.apextalos.cvitfusion.common.opflow.Parameter;
import com.apextalos.cvitfusion.common.opflow.Process;
import com.apextalos.cvitfusion.common.opflow.Type;
import com.apextalos.cvitfusion.common.settings.ConfigFile;

public class ProcessorLidar extends Processor {

	private static Logger logger = LogManager.getLogger(ProcessorLidar.class.getSimpleName());
	
	public static Type getType() {
		List<Parameter> parameters = new ArrayList<>();
		
		return new Type(1, 1, "Lidar", parameters, new ArrayList<>() {{
			add(Integer.valueOf(ProcessorWWVD.getType().getTypeID()));
		}}, true);
	}

	public ProcessorLidar(String engineID, Process process, ConfigFile cf, MqttTransceiver mt) {
		super(engineID, process, cf, mt);
	}

	@Override
	public void loop() {
		//logger.info("loop");
	}
}
