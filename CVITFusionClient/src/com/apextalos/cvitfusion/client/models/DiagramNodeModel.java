package com.apextalos.cvitfusion.client.models;

import com.apextalos.cvitfusion.common.opflow.Color;

import javafx.beans.property.*;
import javafx.scene.paint.Paint;

public class DiagramNodeModel {

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty id = new SimpleStringProperty();
    private final BooleanProperty enabled = new SimpleBooleanProperty();
    private final ObjectProperty<Paint> fillPaint = new SimpleObjectProperty<Paint>();
    private final ObjectProperty<Paint> fontPaint = new SimpleObjectProperty<Paint>();
    private final BooleanProperty hasInput = new SimpleBooleanProperty();
    private final BooleanProperty hasOutput = new SimpleBooleanProperty();
    
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
    public void setFillColor(Color fillColor) {
    	String s = fillColor.asColorString();
    	Paint p = Paint.valueOf(s);
    	this.fillPaint.set(p);
    }
    public ObjectProperty<Paint> getFontPaintProperty() {
    	return fontPaint;
    }
    public void setFontColor(Color fontColor) {
    	String s = fontColor.asColorString();
    	Paint p = Paint.valueOf(s);
    	this.fontPaint.set(p);
    }
    public BooleanProperty getHasInputProperty() {
        return hasInput;
    }
    public void setHasInput(boolean hasInput) {
        this.hasInput.set(hasInput);
    }
    public BooleanProperty getHasOutputProperty() {
        return hasOutput;
    }
    public void setHasOutput(boolean hasOutput) {
        this.hasOutput.set(hasOutput);
    }
    
    public DiagramNodeModel() {
    }
}
