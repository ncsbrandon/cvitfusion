package com.apextalos.cvitfusion.client.controls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.client.controllers.ActionListener;
import com.apextalos.cvitfusion.client.controllers.BaseController.EventType;
import com.apextalos.cvitfusion.client.controllers.DiagramNodeController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class DiagramNodeControl extends AnchorPane implements ActionListener {

	private static final Logger logger = LogManager.getLogger(DiagramNodeControl.class.getSimpleName());

	private DiagramNodeController controller;

	public DiagramNodeController getController() {
		return controller;
	}

	public DiagramNodeControl() {
		super();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("diagramNode.fxml"));
		controller = new DiagramNodeController();
		controller.addActionListener(this);
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

	@Override
	public void onActionPerformed(Object o, EventType et) {
		actionPerformed(this, et);
	}

	// ------ JUST PUT THIS FOR HERE RIGHT NOW UNTIL I SORT OUT A CUSTOM CONTROL
	// BASE
	private final List<ActionListener> listeners = new ArrayList<>();

	public void addActionListener(ActionListener l) {
		listeners.add(l);
	}

	public void actionPerformed(Object o, EventType et) {
		for (ActionListener l : listeners) {
			l.onActionPerformed(o, et);
		}
	}
	// --------------
}
