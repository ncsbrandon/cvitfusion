package com.apextalos.cvitfusion.client.models;

import org.joda.time.DateTime;

import com.apextalos.cvitfusion.common.mqtt.message.EngineStatus;
import com.apextalos.cvitfusion.common.utils.DateTimeUtils;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class EngineStatusModel {

	private final StringProperty idProperty;
	private final StringProperty locationNameProperty;
	private final StringProperty sinceLastUpdateProperty;
	private final ObjectProperty<Image> imageProperty;
	private final IntegerProperty spinProperty;
	
	// saved only for intermediate time updates
	private DateTime lastUpdate;
	private boolean busy;

	public EngineStatusModel(EngineStatus es) {
		this.idProperty = new SimpleStringProperty(es.getId());
		this.locationNameProperty = new SimpleStringProperty(es.getLocationName());
		this.sinceLastUpdateProperty = new SimpleStringProperty(DateTimeUtils.timeSinceLastUpdate(es.getLastUpdate(), DateTime.now()));
		this.imageProperty = new SimpleObjectProperty<>();
		this.spinProperty = new SimpleIntegerProperty(0);
		
		this.lastUpdate = es.getLastUpdate();
		this.busy = false;
	}

	public void update(EngineStatus es) {
		this.idProperty.set(es.getId());
		this.locationNameProperty.set(es.getLocationName());
		this.sinceLastUpdateProperty.set(DateTimeUtils.timeSinceLastUpdate(es.getLastUpdate(), DateTime.now()));
		//this.imageProperty.set(image); // the image resource must be set by the caller
		// this.spinProiperty NO CHANGE
		
		this.lastUpdate = es.getLastUpdate();
		//this.busy NO CHANGE
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
	
	public IntegerProperty getSpinProperty() {
		return spinProperty;
	}

	public DateTime getLastUpdate() {
		return lastUpdate;
	}
	
	public boolean getBusy() {
		return busy;
	}
	
	public void setBusy(boolean busy) {
		if(this.busy == true && busy == false) {
			spinProperty.set(0);
		}
		
		this.busy = busy;
	}
}
