package edu.sv.cmu.clustering;


import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.sv.cmu.clustering.eu.MapWindow;
import edu.sv.cmu.clustering.eu.POI;
import edu.sv.cmu.clustering.eu.Point;
import edu.sv.cmu.clustering.eu.Segment;


public class MinigeoDemo {
	private static Pattern POINT = Pattern.compile("^.*?(-?\\d+\\.\\d+)\\s+(-?\\d+\\.\\d+)$");
	
	public static void main(String[] args) throws IOException {
		MapWindow window = new MapWindow();
				//File file = new File("/Users/yima/Documents/DeepAndroid/Thumbtack/src/trunk/MiniGeo/src/france.poly");
				File file = new File("/Users/yima/Documents/DeepAndroid/DataCollectService/src/edu/sv/cmu/clustering/test.txt");
				

		BufferedReader r = new BufferedReader(new FileReader(file));
		String line;
		Point cur, prec = null;
		int readCount = 0;
		int errCount = 0;
		while((line = r.readLine()) != null) {
			readCount++;
			Matcher m = POINT.matcher(line);
			System.out.println(line);

			if(m.matches()) {
				double lon = Double.parseDouble(m.group(1));
				double lat = Double.parseDouble(m.group(2));
//				double lat = Double.parseDouble(line.split(" ")[0]);
//				double lon = Double.parseDouble(line.split(" ")[1]);

				cur = new Point(lat, lon);
				//window.addPOI(new POI(new Point(48.8567, 2.3508), "Paris"));
				window.addPOI(new POI(lat,lon,"hehe"));				//if(prec != null) window.addSegment(new Segment(prec, cur, lon<0 ? Color.BLUE : Color.RED));
				prec = cur;
				
							} else errCount++;
		}
		
		window.addPOI(new POI(new Point(48.8567, 2.3508), "Paris"));
		
		System.out.println("Read " + readCount + " lines; ignored " + errCount);
		
		window.setVisible(true);


	}
}
