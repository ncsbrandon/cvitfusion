package com.apextalos.cvitfusion.client.models;

import javafx.beans.property.*;

public class DiagramNodeModel {

    private final StringProperty name;
    private final StringProperty id;
    private final BooleanProperty enabled;
    

    public StringProperty getNameProperty() {
        return name;
    }
    public void setNodeName(String name) {
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
    
    public DiagramNodeModel(String name, String id, boolean enabled) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
        this.enabled = new SimpleBooleanProperty(enabled);
    }
}
