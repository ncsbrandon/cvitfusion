package com.apextalos.cvitfusion.common.opflow;

public class Color {

	private int R;
	private int G;
	private int B;
	private int A;
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
	public int getA() {
		return A;
	}
	public void setA(int a) {
		A = a;
	}
	public Color(int r, int g, int b, int a) {
		super();
		R = r;
		G = g;
		B = b;
		A = a;
	}
}
