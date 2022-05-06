package com.apextalos.cvitfusionclient.controls;

import com.apextalos.cvitfusionclient.HelloApplication;
import com.apextalos.cvitfusionclient.controllers.DiagramNodeController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


public class DiagramNodeControl extends AnchorPane {

    private DiagramNodeController controller;

    public DiagramNodeController getController() {
        return controller;
    }

    public DiagramNodeControl() {
        super();

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("diagramNode.fxml"));
        controller = new DiagramNodeController();
        loader.setController(controller);
        Node node;
        try {
            node = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.getChildren().add(node);

        //controller.getNodeName().textProperty().bind(nodeName);
    }
}
