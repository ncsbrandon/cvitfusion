package com.apextalos.cvitfusionclient.controllers;

import com.apextalos.cvitfusionclient.models.DiagramNodeModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DiagramNodeController implements Initializable {

    // Model
    private DiagramNodeModel model;

    // View
    @FXML private Label nodeName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = new DiagramNodeModel("TBD");

        // create bindings
        nodeName.textProperty().bind(model.getNodeNameProperty());
    }

    public void onMenuAction(ActionEvent actionEvent) {
        System.out.println("onMenuAction " + actionEvent.toString());
    }
}
