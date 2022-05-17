package com.apextalos.cvitfusion.common.license;

public class Feature {

	public static final int MODE_HIDDEN = 0; // these should not be shown to users and are only for internal use
	public static final int MODE_FEATURE = 1; // these are optional features which should be presented to users

	private String id;
	private String description;
	private Class<?> type;
	private String defaultValue;
	private int mode;

	public Feature(String id, String description, Class<?> type, String defaultValue, int mode) {
		this.id = id;
		this.description = description;
		this.type = type;
		this.defaultValue = defaultValue;
		this.mode = mode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
}
