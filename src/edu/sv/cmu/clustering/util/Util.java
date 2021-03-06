package edu.sv.cmu.clustering.util;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.sv.cmu.clustering.Centroid;
import edu.sv.cmu.clustering.KmeansModel;
import edu.sv.cmu.clustering.GeoPoint;
import edu.sv.cmu.clustering.Protocol;
import edu.sv.cmu.clustering.eu.Circle;
import edu.sv.cmu.clustering.eu.MapWindow;
import edu.sv.cmu.clustering.eu.POI;
import edu.sv.cmu.clustering.eu.Segment;

public class Util {
	public static Color[] colorList = {Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.PINK, Color.ORANGE};
	static SimpleDateFormat formater = new SimpleDateFormat(Protocol.dateFormat);
	public static ArrayList<GeoPoint> readFeaturesFromFile(String filename){
		ArrayList<GeoPoint> initFeatures = new ArrayList<GeoPoint>();
		
		Pattern POINT = Pattern.compile("([\\d+|_]+).+?(-?\\d+\\.\\d+).*(-+?\\d+\\.\\d+)$");
		File file = new File(filename);//"/Users/yima/Documents/DeepAndroid/DataCollectService/src/edu/sv/cmu/clustering/test.txt");

		BufferedReader r;
		try {
			r = new BufferedReader(new FileReader(file));
			String line;
			GeoPoint cur, prec = null;
			int readCount = 0;
			int errCount = 0;
			while((line = r.readLine()) != null) {
				
				readCount++;
				Matcher m = POINT.matcher(line);
				if(m.matches()) {
					String timeStamp = m.group(1);
					double lat = Double.parseDouble(m.group(3));
					double lon = Double.parseDouble(m.group(2));

					cur = new GeoPoint(timeStamp,lat, lon);
					initFeatures.add(cur);
				} 
			}


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return initFeatures;

	}


	public static ArrayList<GeoPoint> readNoGeoFeaturesFromFile(){
		ArrayList<GeoPoint> initFeatures = new ArrayList<GeoPoint>();
		Pattern POINT = Pattern.compile("^.*?(-?\\d+)\\s+(-?\\d+)$");
		File file = new File("/Users/yima/Documents/DeepAndroid/DataCollectService/src/edu/sv/cmu/clustering/test2.txt");

		BufferedReader r;
		try {
			r = new BufferedReader(new FileReader(file));
			String line;
			GeoPoint cur, prec = null;
			int readCount = 0;
			int errCount = 0;
			while((line = r.readLine()) != null) {

				readCount++;
				Matcher m = POINT.matcher(line);
				if(m.matches()) {
					double lat = Double.parseDouble(m.group(1));
					double lon = Double.parseDouble(m.group(2));
					System.out.println(lon + ":"+lat);
					cur = new GeoPoint("hehe",lat, lon);
					initFeatures.add(cur);
				} 
			}


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return initFeatures;

	}
	public static void plotResult(KmeansModel kmeansModel){
		MapWindow window = new MapWindow();
		for(GeoPoint p:kmeansModel.initFeatures){
			edu.sv.cmu.clustering.eu.Point cp = new edu.sv.cmu.clustering.eu.Point(p.longitude,p.latitude);
			Color cc = colorList[p.belongingId];

			window.addSegment(new Segment(cp, cp, cc));

		}
		
		for(int i = 0; i<kmeansModel.nbCluster; i++){
			Centroid c = kmeansModel.centroids[i];
			POI newPOI = new POI(c.longitude, c.latitude, i+"", colorList[i]) ;
			POI fPOI = new POI(c.furthestPoint.longitude, c.furthestPoint.latitude, i+"", colorList[i]) ;
			window.addPOI(newPOI);
			window.addCircle(new Circle(newPOI, fPOI, c.radius,colorList[i]));
		}
		
		window.setVisible(true);


	}
	
	public static Date getDateFromString(String timeStamp){
		
		try {
			return formater.parse(timeStamp);
		} catch (ParseException e) {
			System.out.println(timeStamp);
			e.printStackTrace();
		}
		return null;
	}
	


}
