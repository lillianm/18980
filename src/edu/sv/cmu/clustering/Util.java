package edu.sv.cmu.clustering;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.sv.cmu.clustering.maputil.MapWindow;
import edu.sv.cmu.clustering.maputil.Segment;



public class Util {
	public static ArrayList<Point> readFeaturesFromFile(){
		ArrayList<Point> initFeatures = new ArrayList<Point>();
		Pattern POINT = Pattern.compile("^.*?(-?\\d+\\.\\d+)\\s+(-?\\d+\\.\\d+)$");
		File file = new File("/Users/yima/Documents/DeepAndroid/DataCollectService/src/edu/sv/cmu/clustering/test.txt");

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
	
	public static void plotResult(KmeansModel kmeansModel){
		MapWindow window = new MapWindow();
		for(Point p:kmeansModel.initFeatures){
			edu.sv.cmu.clustering.maputil.Point cp = new edu.sv.cmu.clustering.maputil.Point (p.latitude,p.longitude);
			Color cc = p.belongingId == 0? Color.RED:p.belongingId == 1?Color.BLUE:Color.ORANGE;

			window.addSegment(new Segment(cp, cp, cc));

		}
		window.setVisible(true);


	}

}
