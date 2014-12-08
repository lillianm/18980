package edu.sv.cmu.clustering.eu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

/**
 * Segment of a linear shape, such as a road.
 * 
 * @author Christophe Jacquet
 *
 */
public class Segment {
	private final static Stroke BASIC_STROKE = new BasicStroke(3);
	
	private final Point pointA, pointB;
	private final Color color;
	private final Stroke stroke;
	
	/**
	 * Creates a new segment.
	 * 
	 * @param cur the first point
	 * @param cur2 the second point
	 * @param color the color of the segment
	 */
	public Segment(edu.sv.cmu.clustering.eu.Point cur, edu.sv.cmu.clustering.eu.Point cur2, Color color) {
		this(cur, cur2, color, BASIC_STROKE);
	}
	
	public Segment(Point pointA, Point pointB, Color color, Stroke stroke) {
		this.pointA = pointA;
		this.pointB = pointB;
		this.color = color;
		this.stroke = stroke;
	}

	/**
	 * Returns the first point of the segment ("A")
	 * 
	 * @return the first point of the segment
	 */
	public Point getPointA() {
		return pointA;
	}

	/**
	 * Returns the second point of the segment ("B")
	 * 
	 * @return the second point of the segment
	 */
	public Point getPointB() {
		return pointB;
	}

	/**
	 * Returns the color of the segment
	 * 
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}
	
	public Stroke getStroke() {
		return stroke;
	}	
}
