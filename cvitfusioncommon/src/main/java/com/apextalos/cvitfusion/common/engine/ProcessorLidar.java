package com.apextalos.cvitfusion.common.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.opflow.Parameter;
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

	public ProcessorLidar(Properties properties, ConfigFile cf) {
		super(properties, cf);
	}

	@Override
	public void start() {
		logger.info("starting ProcessorLidar");
	}

	@Override
	public void stop() {
		logger.info("stopping ProcessorLidar");
	}
}
