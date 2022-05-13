package com.apextalos.cvitfusion.client.diagram;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.client.controllers.BaseController;
import com.apextalos.cvitfusion.client.controllers.BaseController.EventType;
import com.apextalos.cvitfusion.client.controls.DiagramNodeControl;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;

public class DiagramBuilder {

	private static final Logger logger = LogManager.getLogger(DiagramBuilder.class.getSimpleName());

	private static final int NODE_WIDTH = 160;
	private static final int NODE_MARGIN = 40;
	private static final int NODE_HEIGHT = 100;
	
	private List<javafx.scene.Node> dncs = new ArrayList<>();
	private BaseController listener;
	
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
	
	public List<javafx.scene.Node> layout(OperationalFlow of, BaseController listener) {
		if(of == null)
			return sample(listener);
		
		this.listener = listener;
		PanelPosition pos = new PanelPosition(NODE_MARGIN, NODE_MARGIN);
		
		// go through the top-level nodes
		for(com.apextalos.cvitfusion.common.opflow.Node node : of.getNodes()) {
			
			// restart at the top
			pos.setY(NODE_MARGIN);
			pos = layoutNode(node, pos);
		}
		
		return dncs;
	}
		
	private PanelPosition layoutNode(com.apextalos.cvitfusion.common.opflow.Node node, PanelPosition pos) {	
		// calculate the total width of this branch
		int width = calculateWidth(node.getChildren());
		logger.info(String.format("width [%s]", width));
			
		// x of the node
		int x = pos.getX() + (width / 2) - (NODE_WIDTH / 2);
		
		// draw the node
		DiagramNodeControl r = new DiagramNodeControl();
	    r.setLayoutX(x);
	    r.setLayoutY(pos.getY());
	    r.getController().getModel().setName(String.valueOf(node.getTypeID()));
	    r.getController().getModel().setID(String.valueOf(node.getNodeID()));
	    r.addActionListener(listener);
	    dncs.add(r);
				
	    // move down to the children
	    pos.addY(NODE_MARGIN);
	    
		return pos;
	}
	
	private int calculateWidth(List<com.apextalos.cvitfusion.common.opflow.Node> nodes) {
		// width of children
		int width = 0;
		for(com.apextalos.cvitfusion.common.opflow.Node node : nodes) {
			width += calculateWidth(node);
		}
		
		// plus margin
		if(nodes.size() > 1)
			width += (nodes.size() - 1) * NODE_MARGIN;
		
		return width;
	}
	
	private int calculateWidth(com.apextalos.cvitfusion.common.opflow.Node node) {
		List<com.apextalos.cvitfusion.common.opflow.Node> children = node.getChildren();
		
		// if i don't have children, just return my width
		if(children == null || children.size() == 0)
			return NODE_WIDTH;
		
		// return the width of my children
		return calculateWidth(children);
	}
}
