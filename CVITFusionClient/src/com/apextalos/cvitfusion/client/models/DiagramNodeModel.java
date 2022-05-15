package com.apextalos.cvitfusion.client.models;

import com.apextalos.cvitfusion.common.opflow.Color;

import javafx.beans.property.*;
import javafx.scene.paint.Paint;

public class DiagramNodeModel {

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty id = new SimpleStringProperty();
    private final BooleanProperty enabled = new SimpleBooleanProperty();
    private final ObjectProperty<Paint> fillPaint = new SimpleObjectProperty<Paint>();

    public StringProperty getNameProperty() {
        return name;
    }
    public void setName(String name) {
        this.name.set(name);
    } 
    public StringProperty getIDProperty() {
        return id;
    }
    public void setID(String id) {
        this.id.set(id);
    }
    public BooleanProperty getEnabledProperty() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
    }
    public ObjectProperty<Paint> getFillPaintProperty() {
    	return fillPaint;
    }
    public void setColor(Color color) {
    	String s = color.asColorString();
    	Paint p = Paint.valueOf(s);
    	this.fillPaint.set(p);
    }
    
    public DiagramNodeModel() {
    }
}
