package com.apextalos.cvitfusion.client.controls;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.client.controllers.DiagramNodeController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;


public class DiagramNodeControl extends AnchorPane {

	private static final Logger logger = LogManager.getLogger(DiagramNodeControl.class.getSimpleName());
	
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
            logger.error(e);
            return;
        }
        this.getChildren().add(node);
    }
}
