package com.apextalos.cvitfusion.client.diagram;

public class PanelPosition {

	private int x;
	private int y;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void addX(int dx) {
		this.x += dx;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void addY(int dy) {
		this.y += dy;
	}

	public PanelPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public PanelPosition(PanelPosition other) {
		this.x = other.getX();
		this.y = other.getY();
	}
}
