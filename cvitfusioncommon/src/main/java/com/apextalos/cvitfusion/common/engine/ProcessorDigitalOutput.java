package com.apextalos.cvitfusion.common.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.opflow.Parameter;
import com.apextalos.cvitfusion.common.opflow.Type;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.thread.SimpleThread;
import com.apextalos.cvitfusion.common.utils.SleepUtils;

public class ProcessorDigitalOutput extends Processor {
	
	private static Logger logger = LogManager.getLogger(ProcessorDigitalOutput.class.getSimpleName());
	
	private SimpleThread t;
	private int status = 0;
	
	public static Type getType() {
		List<Parameter> parameters = new ArrayList<>();
		
		return new Type(102, 1, "Digital Output", parameters, null, false);
	}
	
	protected ProcessorDigitalOutput(Properties properties, ConfigFile cf) {
		super(properties, cf);
	}

	@Override
	public void start() {
		logger.info("starting ProcessorDigitalOutput");
		
		t = new SimpleThread() {
			@Override
			protected void running() {
				while(!getStop()) {
					logger.info("Status {}", status);
					status++;
					SleepUtils.safeSleep(100);
				}
			}	
		};
		t.start();
	}

	@Override
	public void stop() {
		logger.info("stopping ProcessorDigitalOutput");
		
		t.setStopAndJoin(0);
	}

}
