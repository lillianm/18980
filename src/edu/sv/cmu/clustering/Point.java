package edu.sv.cmu.clustering;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.*;
public class Point {
	public String timeStamp;
	public double latitude;
	public double longitude;
	public int belongingId = -1;
	public double minDist = Double.MAX_VALUE;
	HashMap<Point, Double> dist = new HashMap<Point, Double>();
	PriorityQueue<Point> pq = new PriorityQueue<Point>(100, new Comparator<Point>(){
		@Override
		public int compare(Point p1, Point p2){
			return (int) (dist.get(p1) - dist.get(p2));
		}
	});


	// throw exception to check the validation
	public Point(String time, double lat, double lot){
		this.timeStamp = time;
		this.latitude = lat;
		this.longitude = lot;

			}
	@Override
	public String toString(){
		return "Point:" +" latitude: " + this.latitude + " longitude: "+ this.longitude+ " minDist: " + this.minDist;
	}

	

}
