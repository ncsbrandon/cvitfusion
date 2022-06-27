package com.apextalos.cvitfusion.client.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class FXMLResource {

	private Node resource = null;
	
	public Node getResource() {
		return resource;
	}
	
	private FXMLLoader loader = null;
	
	public FXMLLoader getLoader() {
		return loader;
	}
	
	public FXMLResource(FXMLLoader loader, Node resource) {
		this.loader = loader;
		this.resource = resource;
	}
}
