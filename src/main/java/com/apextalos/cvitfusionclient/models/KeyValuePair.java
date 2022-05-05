package com.apextalos.cvitfusionclient.models;

import javafx.beans.property.SimpleStringProperty;

public class KeyValuePair {
    private final SimpleStringProperty key;
    private final SimpleStringProperty value;

    public KeyValuePair(String key, String value) {
        this.key = new SimpleStringProperty(key);
        this.value = new SimpleStringProperty(value);
    }

    public String getKey() {
        return key.get();
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public String getValue() {
        return value.get();
    }

    public void setLastName(String value) {
        this.value.set(value);
    }
}
