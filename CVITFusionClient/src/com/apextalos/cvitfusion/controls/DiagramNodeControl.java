package com.apextalos.cvitfusion.controls;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import com.apextalos.cvitfusion.controllers.DiagramNodeController;


public class DiagramNodeControl extends AnchorPane {

    private DiagramNodeController controller;
    public DiagramNodeController getController() {
        return controller;
    }

    public DiagramNodeControl() {
        super();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("diagramNode.fxml"));
        controller = new DiagramNodeController();
        loader.setController(controller);
        Node node;
        try {
            node = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.getChildren().add(node);
    }
}
