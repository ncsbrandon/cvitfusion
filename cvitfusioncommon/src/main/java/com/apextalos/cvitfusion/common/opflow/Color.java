package com.apextalos.cvitfusion.common.opflow;

public class Color {

	private int R;
	private int G;
	private int B;
	private double A;

	public int getR() {
		return R;
	}

	public void setR(int r) {
		R = r;
	}

	public int getG() {
		return G;
	}

	public void setG(int g) {
		G = g;
	}

	public int getB() {
		return B;
	}

	public void setB(int b) {
		B = b;
	}

	public double getA() {
		return A;
	}

	public void setA(double a) {
		A = a;
	}

	public String asColorString() {
		return String.format("rgba(%d, %d, %d, %.02f)", R, G, B, A);
	}

	public Color(int r, int g, int b, double a) {
		super();
		R = r;
		G = g;
		B = b;
		A = a;
	}
}
