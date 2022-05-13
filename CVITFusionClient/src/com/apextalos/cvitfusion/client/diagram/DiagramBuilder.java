package com.apextalos.cvitfusion.client.diagram;

import java.util.ArrayList;
import java.util.List;

import com.apextalos.cvitfusion.client.controllers.BaseController;
import com.apextalos.cvitfusion.client.controls.DiagramNodeControl;

public class DiagramBuilder {

	public List<DiagramNodeControl> fromJSON(String json, BaseController listener) {
		List<DiagramNodeControl> dncs = new ArrayList<>();
		
		DiagramNodeControl r = new DiagramNodeControl();
        r.setLayoutX(20);
        r.setLayoutY(20);
        r.getController().getModel().setName("Node 1");
        r.getController().addActionListener(listener);
        dncs.add(r);
        
        return dncs;
	}
}
