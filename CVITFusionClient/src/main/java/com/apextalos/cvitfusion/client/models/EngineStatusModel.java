package com.apextalos.cvitfusion.client.models;

import org.joda.time.DateTime;

import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EngineStatusModel {

	private final StringProperty idProperty;
	private final StringProperty locationNameProperty;
	private final StringProperty sinceLastUpdateProperty;
	private DateTime lastUpdate;

	public EngineStatusModel(EngineStatus es) {
		this.idProperty = new SimpleStringProperty(es.getId());
		this.locationNameProperty = new SimpleStringProperty(es.getLocationName());
		this.sinceLastUpdateProperty = new SimpleStringProperty("const");
		this.lastUpdate = es.getLastUpdate();
	}
		
	public void update(EngineStatus es) {
		this.idProperty.set(es.getId());
		this.locationNameProperty.set(es.getLocationName());
		this.sinceLastUpdateProperty.set("update");
		this.lastUpdate = es.getLastUpdate();
	}

	public StringProperty getIdProperty() {
		return idProperty;
	}

	public StringProperty getLocationNameProperty() {
		return locationNameProperty;
	}

	public StringProperty getSinceLastUpdateProperty() {
		return sinceLastUpdateProperty;
	}
	
	public DateTime getLastUpdate() {
		return lastUpdate;
	}
}
