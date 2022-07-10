package com.apextalos.cvitfusion.common.mqtt.message;

public class ProcessStatusResponse extends EngineRequest {

	private int processID;
	private String contents;
	
	public int getProcessID() {
		return processID;
	}
	public void setProcessID(int processID) {
		this.processID = processID;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	public ProcessStatusResponse() {
		// needed for mapper
	}
	
	public ProcessStatusResponse(String engineID, int processID, String contents) {
		super(engineID);
		this.processID = processID;
		this.contents = contents;
	}
}
