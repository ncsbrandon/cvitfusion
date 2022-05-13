package com.apextalos.cvitfusion.client.diagram;

import java.util.ArrayList;
import java.util.List;

import com.apextalos.cvitfusion.client.controllers.BaseController;
import com.apextalos.cvitfusion.client.controls.DiagramNodeControl;

import javafx.scene.Node;
import javafx.scene.shape.Line;

public class DiagramBuilder {

	public List<Node> fromJSON(String json, BaseController listener) {
		List<Node> dncs = new ArrayList<>();
		
		DiagramNodeControl r = new DiagramNodeControl();
        r.setLayoutX(20);
        r.setLayoutY(20);
        r.getController().getModel().setName("Node 1");
        r.getController().addActionListener(listener);
        dncs.add(r);
        
        Line l = new Line(200, 200, 300, 300);
        l.setStrokeWidth(5);
        dncs.add(l);
        
        return dncs;
	}
}
