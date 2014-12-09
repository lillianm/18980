package edu.sv.cmu.clustering;

import java.util.*;
import java.util.logging.SimpleFormatter;
public class GeoPoint {
	public String timeStamp;
	public Date time;
	public double latitude;
	public double longitude;
	public int belongingId = -1;
	public double minDist = Double.MAX_VALUE;
	HashMap<GeoPoint, Double> dist = new HashMap<GeoPoint, Double>();
	PriorityQueue<GeoPoint> pq = new PriorityQueue<GeoPoint>(100, new Comparator<GeoPoint>(){
		@Override
		public int compare(GeoPoint p1, GeoPoint p2){
			return (int) (dist.get(p1) - dist.get(p2));
		}
	});


	// throw exception to check the validation
	public GeoPoint(String time, double lat, double lot){
		this.timeStamp = time;
		this.latitude = lat;
		this.longitude = lot;

	}
	@Override
	public String toString(){
		return "Point:" +" latitude: " + this.latitude + " longitude: "+ this.longitude+ " minDist: " + this.minDist;
	}





}
