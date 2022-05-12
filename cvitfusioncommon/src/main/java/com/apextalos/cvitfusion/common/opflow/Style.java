package com.apextalos.cvitfusion.common.opflow;

import java.awt.Color;

public class Style {

	private int nodeStyleID;
	private int shape;
	private Color fill;
	private Color outline;
	
	public int getNodeStyleID() {
		return nodeStyleID;
	}
	public void setNodeStyleID(int nodeStyleID) {
		this.nodeStyleID = nodeStyleID;
	}
	public int getShape() {
		return shape;
	}
	public void setShape(int shape) {
		this.shape = shape;
	}
	public Color getFill() {
		return fill;
	}
	public void setFill(Color fill) {
		this.fill = fill;
	}
	public Color getOutline() {
		return outline;
	}
	public void setOutline(Color outline) {
		this.outline = outline;
	}
}
