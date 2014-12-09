package edu.sv.cmu.clustering.eu;

import java.awt.Color;

public class Circle {
	public double center_x;
	public double center_y;
	public double radius;
	public Point center;
	public Color color;
	public Point furthestPoint;
	public Circle(Point center, POI fPOI, double radius, Color color){
		this.center = center;
		this.center_x = center.getLatitude();
		this.center_y = center.getLongitude();
		this.radius = radius;
		this.color = color;
		this.furthestPoint = fPOI;
	}
}
