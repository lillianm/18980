package edu.sv.cmu.clustering;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Center {
	public double latitude;
	public double longitude;
	private ArrayList<double[]> geoDistribution;
	public double[] timeDistribution = new double[24];
	public double radius = 0;
	public Point furthestPoint = null;

	public Center(){}

	public Center(Point point){
		this.latitude = point.latitude;
		this.longitude = point.longitude;
	}

	public Center(double lat,double lon){
		this.latitude = lat;
		this.longitude = lon;
	}
	public double euclideanDistanceTo(Point p){
		// the longitude of -180 to +180
		// latitude is -90 to +90

		double longitudeDiff = Math.abs(this.longitude - p.longitude);
		longitudeDiff = Math.min(longitudeDiff, 360 - longitudeDiff);
		double latitudeDiff  = Math.abs(this.latitude - p.latitude);

		double dist =  Math.sqrt(longitudeDiff * longitudeDiff + latitudeDiff * latitudeDiff);
		return dist;

	}
	@Override
	public String toString(){
		return "Center:" +" latitude: " + this.latitude + " longitude: "+ this.longitude;
	}
	
	public void updateTimeDistribution(Date date){
		if(date == null){
			System.out.println("Time formatting Error ");
		}
		
		int hour = date.getHours();
		timeDistribution[hour]++;
	}
	
	public void printTimeDistribution(){
		for(int i = 0;i<24;i++){
			System.out.println(timeDistribution[i] + " ");
		}
		System.out.println("\n");
	}

}
