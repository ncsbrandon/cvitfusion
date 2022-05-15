package com.apextalos.cvitfusion.client.diagram;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.client.controllers.BaseController;
import com.apextalos.cvitfusion.client.controls.DiagramNodeControl;
import com.apextalos.cvitfusion.client.models.DiagramNodeModel;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
import com.apextalos.cvitfusion.common.opflow.Process;
import com.apextalos.cvitfusion.common.opflow.Style;
import com.apextalos.cvitfusion.common.opflow.Type;

public class DiagramBuilder {

	private static final Logger logger = LogManager.getLogger(DiagramBuilder.class.getSimpleName());

	private static final int NODE_WIDTH = 160;
	private static final int NODE_MARGIN = 40;
	private static final int NODE_HEIGHT = 100;
	
	private OperationalFlow of;
	private List<javafx.scene.Node> dncs;
	private BaseController listener;
	
	/*public List<Node> sample(BaseController listener) {
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
	}*/
	
	public List<javafx.scene.Node> layout(OperationalFlow of, BaseController listener) {
		this.listener = listener;
		dncs = new ArrayList<>();
		this.of = of;
		
		// sample data
		//if(of == null)
		//	return sample(listener);
		
		// start at the top left
		PanelPosition pos = new PanelPosition(0, 0);
		
		// go through the top-level nodes
		for(Process node : of.getProcesses()) {
			
			// restart each at the top
			pos.setY(0);
			layoutNode(node, pos);
		}
		
		return dncs;
	}
		
	private void layoutNode(Process process, PanelPosition startPos) {	
		// calculate the position of the control
		int x = 0;
		int y = startPos.getY();
		int width = 0;
		if(process.hasChildren()) {
			width = calculateWidth(process.getChildren());
			x = startPos.getX() - (NODE_WIDTH / 2) + (width / 2);
		} else {
			x = startPos.getX();
		}	
		PanelPosition thisPos = new PanelPosition(x, y);
		
		// draw the control
		DiagramNodeControl r = new DiagramNodeControl();
	    r.setLayoutX(x);
	    r.setLayoutY(thisPos.getY());
	    DiagramNodeModel model = r.getController().getModel();
	    model.setName(String.valueOf(process.getTypeID()));
	    model.setID(String.valueOf(process.getNodeID()));
	    model.setEnabled(process.isEnabled());
	    Style s = of.lookupStyleForType(process.getTypeID());
	    model.setColor(s.getFill());
	    Type t = of.lookupType(process.getTypeID());
	    model.setHasInput(t.hasSupportedInputs());
	    model.setHasOutput(t.hasSupportedOutputs());
	    r.addActionListener(listener);
	    dncs.add(r);
				
	    // go through the children
	    if(process.hasChildren()) {
	    	int childWidth = width / process.getChildren().size();
	    	int i = 0;
		 	for(Process child : process.getChildren()) {
		 		int childX = startPos.getX() + (childWidth * i);
		 		int childY = startPos.getY() + NODE_HEIGHT + NODE_MARGIN;
		 		PanelPosition childPos = new PanelPosition(childX, childY);
		 		layoutNode(child, childPos);
		 		i++;
		 	}
	    }
	}
	
	private int calculateWidth(List<Process> processes) {
		// no process is no width
		if(processes == null || processes.size() == 0)
			return 0;
				
		// width of each
		int width = 0;
		for(Process process : processes) {
			width += calculateWidth(process);
		}
		
		// plus margin
		if(processes.size() > 1)
			width += (processes.size() - 1) * NODE_MARGIN;
		
		return width;
	}
	
	private int calculateWidth(Process process) {
		// if i don't have children, just return my width
		if(!process.hasChildren())
			return NODE_WIDTH;
		
		// return the width of my children
		return calculateWidth(process.getChildren());
	}
}
