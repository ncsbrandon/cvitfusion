package com.apextalos.cvitfusion.client.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainSceneModel {

	private final ObservableList<String> statusItems;
	private final ObservableList<ParameterModel> parameterItems;

	public MainSceneModel() {
		this.statusItems = FXCollections.observableArrayList();
		this.parameterItems = FXCollections.observableArrayList();
	}

	public ObservableList<String> getStatusListItems() {
		return statusItems;
	}

	public ObservableList<ParameterModel> getParameterListItems() {
		return parameterItems;
	}
}
