package com.apextalos.cvitfusion.models;

import javafx.beans.property.*;

public class DiagramNodeModel {

    private final StringProperty nodeName;

    public DiagramNodeModel(String name) {
        this.nodeName = new SimpleStringProperty(name);
    }

    public StringProperty getNodeNameProperty() {
        return nodeName;
    }

    public void setNodeName(String name) {
        nodeName.set(name);
    }
}
