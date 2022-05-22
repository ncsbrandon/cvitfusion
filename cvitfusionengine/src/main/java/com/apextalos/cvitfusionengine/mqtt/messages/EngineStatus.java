package com.apextalos.cvitfusionengine.mqtt.messages;

public class EngineStatus {

	public enum Mode {
		UNKNOWN,
		RUNNING,
		STANDBY
	}
	
	public enum LogLevel {
		UNKNOWN,
		ERROR,
		INFO,
		DEBUG
	}
	
	private String ts;
	private Mode mode;
	private String locationName;
	private String version;
	private LogLevel logLevel;
	private Coordinate coord;
	
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public Mode getMode() {
		return mode;
	}
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public LogLevel getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
	}
	public Coordinate getCoord() {
		return coord;
	}
	public void setCoord(Coordinate coord) {
		this.coord = coord;
	}
	
	public EngineStatus(String ts, Mode mode, String locationName, String version, LogLevel logLevel,
			Coordinate coord) {
		super();
		this.ts = ts;
		this.mode = mode;
		this.locationName = locationName;
		this.version = version;
		this.logLevel = logLevel;
		this.coord = coord;
	}
}
