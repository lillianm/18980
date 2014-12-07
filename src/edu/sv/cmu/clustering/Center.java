package edu.sv.cmu.clustering;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Center {
	public double latitude;
	public double longitude;
	private ArrayList<double[]> geoDistribution;
	private ArrayList<double[]> timeDistribution;

	public Center(){}

	public Center(Point point){
		this.latitude = point.latitude;
		this.longitude = point.longitude;
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
	
	public void updateTimeDistribution(Point point){
		SimpleDateFormat dateFormatParser = new SimpleDateFormat(Protocol.dateFormat);
		try {
			Date time = dateFormatParser.parse(point.timeStamp);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
