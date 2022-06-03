package com.apextalos.cvitfusion.common.opflow;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Color {

	private int r;
	private int g;
	private int b;
	private double a;

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	@JsonIgnore
	public String asColorString() {
		return String.format("rgba(%d, %d, %d, %.02f)", r, g, b, a);
	}

	public Color() {
	}
	
	public Color(int r, int g, int b, double a) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
}
