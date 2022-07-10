package com.apextalos.cvitfusion.common.engine;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.common.mqtt.MqttTransceiver;
import com.apextalos.cvitfusion.common.mqtt.message.ProcessStatusResponse;
import com.apextalos.cvitfusion.common.mqtt.topics.TopicBuilder;
import com.apextalos.cvitfusion.common.opflow.Process;
import com.apextalos.cvitfusion.common.settings.ConfigFile;
import com.apextalos.cvitfusion.common.thread.SimpleThread;
import com.apextalos.cvitfusion.common.utils.SleepUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Processor {

	private static Logger logger = LogManager.getLogger(Processor.class.getSimpleName());
	
	protected String engineID;
	protected Process process;
	protected ConfigFile cf;
	protected MqttTransceiver mt;
	
	private SimpleThread t;
	private boolean publishingStatus;
	private List<Processor> listeners = new ArrayList<>();
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	protected Processor(String engineID, Process process, ConfigFile cf, MqttTransceiver mt) {
		this.engineID = engineID;
		this.process = process;
		this.cf = cf;
		this.publishingStatus = false;
		this.mt = mt;
	}
	
	public void addListener(Processor listener) {
		listeners.add(listener);
	}
	
	public void clearListeners() {
		listeners.clear();
	}

	public int getProcessID() {
		return process.getProcessID();
	}
	
	public void setPublishingStatus(boolean publishingStatus) {
		this.publishingStatus = publishingStatus;
	}
	
	public boolean getPublishingStatus() {
		return publishingStatus;
	}
	
	public void publishStatus(String status) {
		if(!publishingStatus)
			return;
		
		ProcessStatusResponse psr = new ProcessStatusResponse(engineID, getProcessID(), status);
		
		String json = null;
		try {
			json = mapper.writeValueAsString(psr);
		} catch (JsonProcessingException e) {
			logger.error("process status encoding failure: {}", e.getMessage());
			return;
		}
		
		mt.publish(statusTopic(), json, false);
	}
	
	public void start() {
		if(!process.isEnabled())
			return;
		
		logger.info("starting processor");
		
		t = new SimpleThread() {
			@Override
			protected void running() {
				while(!getStop()) {
					loop();
					SleepUtils.safeSleep(100);
				}
			}	
		};
		t.start();
	}

	public void stop() {
		logger.info("stopping processor");
		
		if(t != null)
			t.setStopAndJoin(0);
	}
	
	public abstract void loop();
	
	public String statusTopic() {
		return TopicBuilder.respondProcessStatus(engineID, getProcessID());
	}
}
