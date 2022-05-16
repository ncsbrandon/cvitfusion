package com.apextalos.cvitfusion.common.opflow;

public class Style {

	private int styleID;
	private int shape;
	private Color fill;
	private Color font;

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

	public Color getFont() {
		return font;
	}

	public void setFont(Color font) {
		this.font = font;
	}

	public Style(int styleID, int shape, Color fill, Color font) {
		super();
		this.styleID = styleID;
		this.shape = shape;
		this.fill = fill;
		this.font = font;
	}
}
