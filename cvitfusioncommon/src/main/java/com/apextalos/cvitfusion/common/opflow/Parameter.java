package com.apextalos.cvitfusion.common.opflow;

import java.util.List;

public class Parameter {

	public enum Form {
		INTEGER,
		DECIMAL,
		STRING,
		BOOLEAN,
		STRINGLIST,
		EMAIL,
		PASSWORD
	}
	
	private String parameterID;
	private String description;
	private String tooltip;
	private Form form;
	private String defaultValue;
	private List<String> choices;
	
	public String getParameterID() {
		return parameterID;
	}
	public void setParameterID(String parameterID) {
		this.parameterID = parameterID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTooltip() {
		return tooltip;
	}
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
	public Form getForm() {
		return form;
	}
	public void setForm(Form form) {
		this.form = form;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public List<String> getChoices() {
		return choices;
	}
	public void setChoices(List<String> choices) {
		this.choices = choices;
	}
	
	public Parameter() {
	}
	
	public Parameter(String parameterID, String description, Form form, String defaultValue, String tooltip, List<String> choices) {
		super();
		this.parameterID = parameterID;
		this.description = description;
		this.tooltip = tooltip;
		this.form = form;
		this.defaultValue = defaultValue;
		this.choices = choices;
	}
}
