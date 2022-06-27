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
	private final StringProperty modeProperty;
	private final StringProperty locationNameProperty;
	private final StringProperty versionProperty;
	private final StringProperty logLevelProperty;
	private final StringProperty sinceLastUpdateProperty;
	private final ObjectProperty<Image> imageProperty;
	private final IntegerProperty spinProperty;
	private final StringProperty latitudeProperty;
	private final StringProperty longitudeProperty;
	private final StringProperty elevationProperty;
	
	// saved only for intermediate time updates
	private DateTime lastUpdate;
	private boolean busy;

	public EngineStatusModel(EngineStatus es) {
		this.idProperty = new SimpleStringProperty(es.getId());
		this.modeProperty = new SimpleStringProperty(es.getMode().toString());
		this.locationNameProperty = new SimpleStringProperty(es.getLocationName());
		this.versionProperty = new SimpleStringProperty(es.getVersion());
		this.logLevelProperty = new SimpleStringProperty(es.getLogLevel().toString());
		this.sinceLastUpdateProperty = new SimpleStringProperty(DateTimeUtils.timeSinceLastUpdate(es.getLastUpdate(), DateTime.now()));
		this.imageProperty = new SimpleObjectProperty<>();
		this.spinProperty = new SimpleIntegerProperty(0);
		
		this.latitudeProperty = new SimpleStringProperty(String.format("%.6f", es.getCoord().getLatitude()));
		this.longitudeProperty = new SimpleStringProperty(String.format("%.6f", es.getCoord().getLongitude()));
		this.elevationProperty = new SimpleStringProperty(String.format("%.2f", es.getCoord().getElevation()));
		
		this.lastUpdate = es.getLastUpdate();
		this.busy = false;
	}

	public void update(EngineStatus es) {
		this.idProperty.set(es.getId());
		this.modeProperty.set(es.getMode().toString());
		this.locationNameProperty.set(es.getLocationName());
		this.versionProperty.set(es.getVersion());
		this.logLevelProperty.set(es.getLogLevel().toString());
		this.sinceLastUpdateProperty.set(DateTimeUtils.timeSinceLastUpdate(es.getLastUpdate(), DateTime.now()));
		//this.imageProperty.set(image); // the image resource must be set by the caller
		// this.spinProiperty NO CHANGE
		this.latitudeProperty.set(String.format("%.6f", es.getCoord().getLatitude()));
		this.longitudeProperty.set(String.format("%.6f", es.getCoord().getLongitude()));
		this.elevationProperty.set(String.format("%.2f", es.getCoord().getElevation()));
		
		this.lastUpdate = es.getLastUpdate();
		//this.busy NO CHANGE
	}

	public StringProperty getIdProperty() {
		return idProperty;
	}
	
	public StringProperty getModeProperty() {
		return modeProperty;
	}

	public StringProperty getLocationNameProperty() {
		return locationNameProperty;
	}

	public StringProperty getVersionProperty() {
		return versionProperty;
	}
	
	public StringProperty getLogLevelProperty() {
		return logLevelProperty;
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

	public StringProperty getLongitudeProperty() {
		return longitudeProperty;
	}
	
	public StringProperty getLatitudeProperty() {
		return latitudeProperty;
	}
	
	public StringProperty getElevationProperty() {
		return elevationProperty;
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
