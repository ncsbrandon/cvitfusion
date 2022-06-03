package com.apextalos.cvitfusion.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.client.models.DiagramNodeModel;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class DiagramNodeController extends BaseController {

	private static final Logger logger = LogManager.getLogger(DiagramNodeController.class.getSimpleName());

	// Model
	private DiagramNodeModel model;

	public DiagramNodeModel getModel() {
		return model;
	}

	// View
	@FXML private Rectangle connectOut;
	@FXML private Rectangle connectIn;
	@FXML private Rectangle body;
	@FXML private Rectangle header;
	@FXML private CheckBox enabled;
	@FXML private Label name;
	@FXML private Label id;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// create model
		model = new DiagramNodeModel();

		// create bindings
		name.textProperty().bind(model.getNameProperty());
		name.textFillProperty().bind(model.getFontPaintProperty());
		id.textProperty().bind(model.getIDProperty());
		enabled.setSelected(model.getEnabledProperty().get());
		body.fillProperty().bind(model.getFillPaintProperty());
		connectOut.fillProperty().bind(model.getFillPaintProperty());
		connectIn.fillProperty().bind(model.getFillPaintProperty());
		connectOut.visibleProperty().bind(model.getHasOutputProperty());
		connectIn.visibleProperty().bind(model.getHasInputProperty());
	}

	public void select(boolean on) {
		connectOut.setEffect(on ? new DropShadow() : null);
		connectIn.setEffect(on ? new DropShadow() : null);
		body.setEffect(on ? new DropShadow() : null);
		header.setEffect(on ? new DropShadow() : null);
	}

	@FXML
	protected void onMouseClicked(MouseEvent mouseEvent) {
		logger.debug("onMouseClicked " + mouseEvent.toString());
		mouseEvent.consume();
		actionPerformed(this, EventType.SELECTED);
	}

	@Override
	public void onActionPerformed(Object o, EventType et) {
		// N/A
	}
}
