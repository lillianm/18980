package edu.sv.cmu.clustering.eu;

import java.awt.Color;


/**
 * Geographic point, defined by its coordinates and by a label.
 * 
 * @author Christophe Jacquet
 * @see POI
 *
 */
public class POI extends Point {
	private final String label;
	public Color color = Color.BLACK;
	public POI(double latitude, double longitude, String label) {
		super(latitude, longitude);
		this.label = label;
	}
	
	public POI(double latitude, double longitude, String label, Color color){
		super(latitude, longitude);
		this.label = label;
		this.color = color;
	}
	
	public POI(Point point, String label) {
		this(point.getLatitude(), point.getLongitude(), label);
	}
	
	public String getLabel() {
		return label;
	}
}
