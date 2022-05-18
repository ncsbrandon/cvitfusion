package com.apextalos.cvitfusion.client.diagram;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apextalos.cvitfusion.client.controllers.BaseController;
import com.apextalos.cvitfusion.client.controllers.BaseController.EventType;
import com.apextalos.cvitfusion.client.controls.DiagramNodeControl;
import com.apextalos.cvitfusion.client.models.DiagramNodeModel;
import com.apextalos.cvitfusion.common.opflow.OperationalFlow;
import com.apextalos.cvitfusion.common.opflow.Process;
import com.apextalos.cvitfusion.common.opflow.ProcessLink;
import com.apextalos.cvitfusion.common.opflow.Style;
import com.apextalos.cvitfusion.common.opflow.Type;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;

public class DiagramBuilder {

	private static final Logger logger = LogManager.getLogger(DiagramBuilder.class.getSimpleName());

	private static final int NODE_WIDTH = 160;
	private static final int NODE_MARGIN = 40;
	private static final int NODE_HEIGHT = 100;

	private OperationalFlow opflow;
	private BaseController listener;
	private List<javafx.scene.Node> dncs;

	public List<javafx.scene.Node> layout(OperationalFlow opflow, BaseController listener) {
		this.opflow = opflow;
		this.listener = listener;
		dncs = new ArrayList<>();

		// start at the top left
		PanelPosition pos = new PanelPosition(0, 0);

		// go through the top-level nodes
		int nextX = 0;
		for (Process topProcess : opflow.getProcesses()) {

			logger.info(String.format("layout top process ID [] Type []", topProcess.getProcessID(), topProcess.getTypeID()));
			
			// restart each at the top
			pos.setY(0);
			pos.setX(nextX);
			layoutNode(topProcess, pos, null, null);

			nextX += calculateWidth(topProcess);
		}

		return dncs;
	}

	private void layoutNode(Process process, PanelPosition startPos, Process parentProcess, PanelPosition parentOutputPos) {
		// calculate the position of the control
		int x = 0;
		int y = startPos.getY();
		int width = 0;
		if (process.hasChildren()) {
			width = calculateWidth(process.getChildren());
			x = startPos.getX() - (NODE_WIDTH / 2) + (width / 2);
		} else {
			x = startPos.getX();
		}
		PanelPosition thisPos = new PanelPosition(x, y);

		if (parentProcess != null && parentOutputPos != null) {
			PanelPosition inputPos = new PanelPosition(x + (NODE_WIDTH / 2), y + 8);

			Line l = new Line(parentOutputPos.getX(), parentOutputPos.getY(), inputPos.getX(), inputPos.getY());
			l.setStrokeWidth(4);
			l.setUserData(new ProcessLink(parentProcess, process));
			l.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					arg0.consume();
					listener.onActionPerformed(l, EventType.SELECTED);
				}
			});
			dncs.add(l);
		}

		// draw the control
		DiagramNodeControl r = new DiagramNodeControl();
		r.setLayoutX(x);
		r.setLayoutY(thisPos.getY());
		DiagramNodeModel model = r.getController().getModel();

		model.setID(String.valueOf(process.getProcessID()));
		model.setEnabled(process.isEnabled());
		Style s = opflow.lookupStyleForType(process.getTypeID());
		model.setFillColor(s.getFill());
		model.setFontColor(s.getFont());
		Type t = opflow.lookupType(process.getTypeID());
		model.setHasInput(t.hasSupportedInputs());
		model.setHasOutput(t.hasSupportedOutputs());
		model.setName(t.getName());
		r.addActionListener(listener);
		dncs.add(r);

		// calculate the input and output connector coordinate
		PanelPosition outputPos = new PanelPosition(x + (NODE_WIDTH / 2), y + NODE_HEIGHT - 5);

		// go through the children
		if (process.hasChildren()) {
			int childWidth = width / process.getChildren().size();
			int i = 0;
			for (Process child : process.getChildren()) {
				int childX = startPos.getX() + (childWidth * i);
				int childY = startPos.getY() + NODE_HEIGHT + NODE_MARGIN;
				PanelPosition childPos = new PanelPosition(childX, childY);
				layoutNode(child, childPos, process, outputPos);
				i++;
			}
		}
	}

	private int calculateWidth(List<Process> processes) {
		// no process is no width
		if (processes == null || processes.size() == 0)
			return 0;

		// width of each
		int width = 0;
		for (Process process : processes) {
			width += calculateWidth(process);
		}

		// plus margin
		if (processes.size() > 1)
			width += (processes.size() - 1) * NODE_MARGIN;

		return width;
	}

	private int calculateWidth(Process process) {
		// if i don't have children, just return my width
		if (!process.hasChildren())
			return NODE_WIDTH;

		// return the width of my children
		return calculateWidth(process.getChildren());
	}
}
