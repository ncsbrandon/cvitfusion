package com.apextalos.cvitfusion.client.controls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.apextalos.cvitfusion.client.controllers.ActionListener;
import com.apextalos.cvitfusion.client.controllers.BaseController.EventType;
import com.apextalos.cvitfusion.client.controllers.DiagramNodeController;
import com.apextalos.cvitfusion.client.controllers.FXMLResource;
import com.apextalos.cvitfusion.client.controllers.ResourceLoader;

import javafx.scene.layout.AnchorPane;

public class DiagramNodeControl extends AnchorPane implements ActionListener {

	// private static final Logger logger = LogManager.getLogger(DiagramNodeControl.class.getSimpleName());

	private ResourceLoader rl = new ResourceLoader();
	private DiagramNodeController controller;

	public DiagramNodeController getController() {
		return controller;
	}

	public DiagramNodeControl() throws IOException {
		super();

		controller = new DiagramNodeController();
		controller.addActionListener(this);

		FXMLResource res = rl.createLoader("diagramNode.fxml", controller);

		this.getChildren().add(res.getResource());
	}

	@Override
	public void onActionPerformed(Object o, EventType et) {
		actionPerformed(this, et);
	}

	// ------ JUST PUT THIS FOR HERE RIGHT NOW UNTIL I SORT OUT A CUSTOM CONTROL BASE
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
