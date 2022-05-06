package com.apextalos.cvitfusionclient.models;

import javafx.beans.property.*;

public class DiagramNodeModel {

    private final StringProperty nodeName;

    public DiagramNodeModel(String nodeName) {
        this.nodeName = new SimpleStringProperty(nodeName);
    }

    public StringProperty getNodeNameProperty() {
        return nodeName;
    }
}
