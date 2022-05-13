package com.apextalos.cvitfusion.client.diagram;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.client.controllers.BaseController;
import com.apextalos.cvitfusion.client.controllers.DiagramNodeController;
import com.apextalos.cvitfusion.client.controllers.BaseController.EventType;
import com.apextalos.cvitfusion.client.controls.DiagramNodeControl;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;

public class DiagramBuilder {

	private static final Logger logger = LogManager.getLogger(DiagramBuilder.class.getSimpleName());

	public List<javafx.scene.Node> sample(BaseController listener) {
		List<javafx.scene.Node> dncs = new ArrayList<>();
		
		DiagramNodeControl r = new DiagramNodeControl();
        r.setLayoutX(20);
        r.setLayoutY(20);
        r.getController().getModel().setName("Node 1");
        r.addActionListener(listener);
        dncs.add(r);
        
        Line l = new Line(200, 200, 300, 300);
        l.setStrokeWidth(4);
        l.setUserData("Some line object");
        l.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				arg0.consume();
				listener.onActionPerformed(l, EventType.SELECTED);
			}
		});      
        dncs.add(l);
        
        return dncs;
	}
	
	public List<javafx.scene.Node> generateNodes(OperationalFlow of, BaseController listener) {
		if(of == null)
			return sample(listener);
		
		// determine our width
		int width = calculateWidth(of.getNodes());
		logger.info(String.format("width [%s]", width));
		
		List<javafx.scene.Node> dncs = new ArrayList<>();
		//for(Node node : of.getNodes())
		return dncs;
	}
	
	private int calculateWidth(List<com.apextalos.cvitfusion.common.opflow.Node> nodes) {
		int width = 0;
		for(com.apextalos.cvitfusion.common.opflow.Node node : nodes) {
			if(node.getChildren().size() == 0)
				return 100;
			
			width += calculateWidth(node.getChildren());
		}
		
		return width;
	}
}
