package com.apextalos.cvitfusion.common.opflow;

public class Style {

	private int styleID;
	private int shape;
	private Color fill;
	private Color outline;
	
	public int getStyleID() {
		return styleID;
	}
	public void setStyleID(int styleID) {
		this.styleID = styleID;
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
	
	public Style(int styleID, int shape, Color color, Color color2) {
		super();
		this.styleID = styleID;
		this.shape = shape;
		this.fill = color;
		this.outline = color2;
	}
}
