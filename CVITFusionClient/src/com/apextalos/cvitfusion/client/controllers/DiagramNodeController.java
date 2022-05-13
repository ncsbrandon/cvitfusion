package com.apextalos.cvitfusion.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.client.models.DiagramNodeModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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
        model = new DiagramNodeModel("TBD", "1", true);

        // create bindings
        name.textProperty().bind(model.getNameProperty());
        id.textProperty().bind(model.getIDProperty());
        enabled.selectedProperty().setValue(model.getEnabledProperty().getValue());
        connectIn.visibleProperty().set(false);
    }

    @FXML
    protected void onEnabledCheckboxAction(ActionEvent actionEvent) {
        logger.debug("onEnabledCheckboxAction " + actionEvent.toString());
    }
    
    @FXML
    protected void onMouseClicked(MouseEvent mouseEvent) {
    	logger.debug("onMouseClicked " + mouseEvent.toString());
    	mouseEvent.consume();
    	actionPerformed(model, EventType.SELECTED);
    }

	@Override
	public void onActionPerformed(Object o, EventType et) {
		
	}
}
