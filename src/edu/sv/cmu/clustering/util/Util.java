package edu.sv.cmu.clustering.util;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.sv.cmu.clustering.Center;
import edu.sv.cmu.clustering.KmeansModel;
import edu.sv.cmu.clustering.Point;
import edu.sv.cmu.clustering.eu.MapWindow;
import edu.sv.cmu.clustering.eu.POI;
import edu.sv.cmu.clustering.eu.Segment;

public class Util {
	public static Color[] colorList = {Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.PINK, Color.ORANGE};

	public static ArrayList<Point> readFeaturesFromFile(String filename){
		ArrayList<Point> initFeatures = new ArrayList<Point>();
		//Pattern POINT = Pattern.compile("^.*?(-?\\d+\\.\\d+)\\s+(-?\\d+\\.\\d+)$");
		Pattern POINT = Pattern.compile(".+?(-?\\d+\\.\\d+).*(-+?\\d+\\.\\d+)$");
				File file = new File(filename);//"/Users/yima/Documents/DeepAndroid/DataCollectService/src/edu/sv/cmu/clustering/test.txt");

		BufferedReader r;
		try {
			r = new BufferedReader(new FileReader(file));
			String line;
			Point cur, prec = null;
			int readCount = 0;
			int errCount = 0;
			while((line = r.readLine()) != null) {
				
				readCount++;
				Matcher m = POINT.matcher(line);
				if(m.matches()) {
					double lon = Double.parseDouble(m.group(1));
					double lat = Double.parseDouble(m.group(2));

					cur = new Point("hehe",lat, lon);
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


	public static ArrayList<Point> readNoGeoFeaturesFromFile(){
		ArrayList<Point> initFeatures = new ArrayList<Point>();
		Pattern POINT = Pattern.compile("^.*?(-?\\d+)\\s+(-?\\d+)$");
		File file = new File("/Users/yima/Documents/DeepAndroid/DataCollectService/src/edu/sv/cmu/clustering/test2.txt");

		BufferedReader r;
		try {
			r = new BufferedReader(new FileReader(file));
			String line;
			Point cur, prec = null;
			int readCount = 0;
			int errCount = 0;
			while((line = r.readLine()) != null) {

				readCount++;
				Matcher m = POINT.matcher(line);
				if(m.matches()) {
					double lat = Double.parseDouble(m.group(1));
					double lon = Double.parseDouble(m.group(2));
					System.out.println(lon + ":"+lat);
					cur = new Point("hehe",lat, lon);
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
		for(Point p:kmeansModel.initFeatures){
			edu.sv.cmu.clustering.eu.Point cp = new edu.sv.cmu.clustering.eu.Point(p.longitude,p.latitude);
			Color cc = colorList[p.belongingId];

			window.addSegment(new Segment(cp, cp, cc));

		}
		for(int i = 0; i<kmeansModel.nbCluster; i++){
			Center c = kmeansModel.centroids[i];
			
			window.addPOI(new POI(c.longitude, c.latitude, i+"", colorList[i]) );
		}
		
		window.setVisible(true);


	}

}
