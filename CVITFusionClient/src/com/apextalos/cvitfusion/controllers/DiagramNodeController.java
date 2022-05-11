package com.apextalos.cvitfusion.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.apextalos.cvitfusion.models.DiagramNodeModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DiagramNodeController extends BaseController {

    // Model
    private DiagramNodeModel model;
    public DiagramNodeModel getModel() {
        return model;
    }

    // View
    @FXML private Label nodeName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // create model
        model = new DiagramNodeModel("TBD");

        // create bindings
        nodeName.textProperty().bind(model.getNodeNameProperty());
    }

    @FXML
    protected void onMenuAction(ActionEvent actionEvent) {
        System.out.println("onMenuAction " + actionEvent.toString());
    }
}
