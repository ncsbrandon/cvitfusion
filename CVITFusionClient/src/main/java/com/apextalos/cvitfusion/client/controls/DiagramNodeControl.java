package com.apextalos.cvitfusion.client.controls;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

	public DiagramNodeControl() throws IOException {
		super();
		
		controller = new DiagramNodeController();
		controller.addActionListener(this);
		
		FXMLLoader fxmlLoader = null;
		Node node = null;
		
		// try loading as the jar
		InputStream in = getClass().getResourceAsStream("/diagramNode.fxml");
		logger.info(String.format("getResourceAsStream is null: %b", in==null));
		if(in != null) {
			fxmlLoader = new FXMLLoader();
			fxmlLoader.setController(controller);
			node = fxmlLoader.load(in);
		} else {	
			// try loading as the debugger
			URL url = getClass().getResource("../../../../../diagramNode.fxml");
			logger.info(String.format("getResource is null: %b", url==null));
			if(url != null) {
				fxmlLoader = new FXMLLoader(url);
				fxmlLoader.setController(controller);
				node = fxmlLoader.load();
			} else {
				logger.error("unable to load the diagramNode fxml");
				System.exit(0);
			}
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
