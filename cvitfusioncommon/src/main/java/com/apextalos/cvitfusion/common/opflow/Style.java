package com.apextalos.cvitfusion.common.opflow;

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
	
	public Style(int nodeStyleID, int shape, Color color, Color color2) {
		super();
		this.nodeStyleID = nodeStyleID;
		this.shape = shape;
		this.fill = color;
		this.outline = color2;
	}
}
