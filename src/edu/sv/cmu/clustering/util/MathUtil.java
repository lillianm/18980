package edu.sv.cmu.clustering.util;

import edu.sv.cmu.clustering.Point;

public class MathUtil {
	/* calculate distance */
	public static double euclideanDistance(Point p1, Point p2){
		// the longitude of -180 to +180
		// latitude is -90 to +90

		double longitudeDiff = Math.abs(p1.longitude - p1.longitude);
		longitudeDiff = Math.min(longitudeDiff, 360 - longitudeDiff);
		double latitudeDiff  = Math.abs(p1.latitude - p2.latitude);

		double dist =  Math.sqrt(longitudeDiff * longitudeDiff + latitudeDiff * latitudeDiff);
		return dist;

	}

}
