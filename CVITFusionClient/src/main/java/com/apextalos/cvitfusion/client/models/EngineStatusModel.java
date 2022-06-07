package com.apextalos.cvitfusion.client.models;

import org.joda.time.DateTime;

import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus;
import com.apextalos.cvitfusion.common.utils.DateTimeUtils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class EngineStatusModel {

	private final StringProperty idProperty;
	private final StringProperty locationNameProperty;
	private final StringProperty sinceLastUpdateProperty;
	private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();

	// saved only for intermediate time updates
	private DateTime lastUpdate;

	public EngineStatusModel(EngineStatus es) {
		this.idProperty = new SimpleStringProperty(es.getId());
		this.locationNameProperty = new SimpleStringProperty(es.getLocationName());
		this.sinceLastUpdateProperty = new SimpleStringProperty(DateTimeUtils.timeSinceLastUpdate(es.getLastUpdate(), DateTime.now()));
		this.imageProperty = new SimpleObjectProperty<>();
		this.lastUpdate = es.getLastUpdate();
	}

	public void update(EngineStatus es) {
		this.idProperty.set(es.getId());
		this.locationNameProperty.set(es.getLocationName());
		this.sinceLastUpdateProperty.set(DateTimeUtils.timeSinceLastUpdate(es.getLastUpdate(), DateTime.now()));
		//this.imageProperty.set(image); // the image resource must be set by the caller
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

	public ObjectProperty<Image> getImageProperty() {
		return imageProperty;
	}

	public DateTime getLastUpdate() {
		return lastUpdate;
	}
}
