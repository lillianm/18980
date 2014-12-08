package edu.sv.cmu.clustering;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.sv.cmu.clustering.eu.MapWindow;
import edu.sv.cmu.clustering.eu.POI;
import edu.sv.cmu.clustering.eu.Segment;
import edu.sv.cmu.clustering.util.Util;


public class KmeansStragedy{

	public static KmeansModel geoDesicKmeans(KmeansModel kmeansModel, String filename){
		int k = kmeansModel.nbCluster;
		int neighbors = 100;
		kmeansModel.initFeatures = Util.readFeaturesFromFile(filename);
		//System.out.println(kmeansModel.initFeatures.size());
		kmeansModel.initCentroids();
		kmeansModel.precomputeWeight();



		kmeansModel.newcounts = new ArrayList<Long>();
		for(int i = 0;i<k;i++){
			kmeansModel.newcounts.add(1L);
		}

		for(int id = 0;id <k;id++){
			Point cur = kmeansModel.initFeatures.get(id);
			for(int i = 0;i<neighbors;i++){
				Point n = cur.pq.poll();
				n.belongingId = kmeansModel.getNearestCentroid(n);
				kmeansModel.counts.set(n.belongingId, kmeansModel.counts.get(n.belongingId) +1);
				cur.pq.add(n);
				System.out.println("ehe");
			}
		}


		int changeCount = 1;
		int cnt = 100;
		while(changeCount >=1 && cnt >0){
			changeCount = 0;cnt--;
			for(int i = 0;i<k;i++){
				kmeansModel.counts.add(0L);
			}

			for(int id = 0;id <k;id++){
				Point cur = kmeansModel.initFeatures.get(id);

				for(int i = 0;i<neighbors;i++){
					Point n = cur.pq.poll();
					if(n !=null){int nearestCentroidIndex = kmeansModel.getNearestCentroid(n);
					if(n.belongingId!=nearestCentroidIndex){
						n.belongingId = nearestCentroidIndex;
						changeCount++;
						cur.pq.add(n);
					}
					}

				}

			}
			//System.out.println(kmeansModel.getCentroids()[0]+";"+kmeansModel.getCentroids()[1]+kmeansModel.getCentroids()[2]);
			System.out.println(changeCount);
			kmeansModel.counts = kmeansModel.newcounts;


			System.out.println(kmeansModel.counts);
		}
		return kmeansModel;

	}

	public static KmeansModel onlineKmeans(KmeansModel kmeansModel, String filename){
		int k = kmeansModel.nbCluster;

		kmeansModel.initFeatures = Util.readFeaturesFromFile(filename);
		kmeansModel.initCentroids();
		System.out.println(kmeansModel.centroids[2]);
		for(Point p: kmeansModel.initFeatures){
			int nearestCentroidIndex = kmeansModel.getNearestCentroid(p);
			if(nearestCentroidIndex == -1) System.out.println(p);
			p.belongingId = nearestCentroidIndex;
			kmeansModel.updateCenter(p);
			kmeansModel.counts.set(nearestCentroidIndex, kmeansModel.counts.get(nearestCentroidIndex) +1);
		}

			System.out.println(kmeansModel.counts);
//		for(Point p: kmeansModel.initFeatures){
//			kmeansModel.updateCenter(p);				
//		}
//

//		int changeCount = 1;
//		while(changeCount >=1){
//			changeCount = 0;
//			for(Point p: kmeansModel.initFeatures){
//				
//				int nearestCentroidIndex = kmeansModel.getNearestCentroid(p);
//				if(p.belongingId!=nearestCentroidIndex){
//					updateCenter(p);
//					updateCenter()
//					kmeansModel.counts.set(p.belongingId,kmeansModel.counts.get(p.belongingId)-1);
//					kmeansModel.counts.set(nearestCentroidIndex,kmeansModel.counts.get(nearestCentroidIndex)+1);
//					p.belongingId = nearestCentroidIndex;
//					changeCount++;
//				}
//
//
//			}
//			//System.out.println(kmeansModel.getCentroids()[0]+";"+kmeansModel.getCentroids()[1]+kmeansModel.getCentroids()[2]);
//			System.out.println(changeCount);
//			kmeansModel.counts = kmeansModel.newcounts;
//
//
//			
//		}
		return kmeansModel;

	}

	public static KmeansModel classicKmeans(KmeansModel kmeansModel, String filename){
		
		int k = kmeansModel.nbCluster;

		ArrayList<Point> features = Util.readFeaturesFromFile(filename);
		kmeansModel.initFeatures = features;
		kmeansModel.initCentroids();
		kmeansModel.printAllCentroids();
		kmeansModel.initFeatures = Util.readFeaturesFromFile(filename);		
		int changeCount = Integer.MAX_VALUE;
		while(changeCount >=1){
			changeCount = 0;
			kmeansModel.initBuffer();
			for(Point p: kmeansModel.initFeatures){
				int nearestCentroidIndex = kmeansModel.getNearestCentroid(p);
				//System.out.println(nearestCentroidIndex);
				kmeansModel.newcounts.set(nearestCentroidIndex, kmeansModel.newcounts.get(nearestCentroidIndex) +1);
				double[] c = kmeansModel.newCentroid.get(nearestCentroidIndex);
				c[0] += p.latitude;
				c[1] += p.longitude;

				if(p.belongingId!=nearestCentroidIndex){
					p.belongingId = nearestCentroidIndex;
					changeCount++;
				}

			}
			//System.out.println(kmeansModel.counts);
			//System.out.println(kmeansModel.newcounts);
			for(int index = 0;index<k;index++){
				double[] d = kmeansModel.newCentroid.get(index);
				long cnt = kmeansModel.newcounts.get(index);
				kmeansModel.centroids[index]= new Center(d[0]/cnt, d[1]/cnt);
			}
			kmeansModel.counts = kmeansModel.newcounts;
			System.out.println(kmeansModel.counts);
			//System.out.println(changeCount);
		}
		

		return kmeansModel;

	}



	public static double[] elbow(int minK, int maxK, KmeansModel kmeansModel){
		double[] dxs = new double[maxK-minK+1];
		for(int k = minK; k <= maxK; k++){
		//	kmeansModel = onlineKmeans(kmeansModel);
			dxs[k-minK] = kmeansModel.computeDxsSum();
		}
		return dxs;

	}
	public static void main(String[] args){

		int k =4;
		KmeansModel kmeansModel = new KmeansModel(k);
		kmeansModel = classicKmeans(kmeansModel, "/Users/yima/Desktop/gps.txt");
		//		kmeansModel = onlineKmeans(kmeansModel);
		//		System.out.println(kmeansModel.computeDxsSum());
		Util.plotResult(kmeansModel);
//		double[] dxs = elbow(1,10,kmeansModel);
//		for(int i = 0;i<dxs.length;i++){
//			System.out.println(dxs[i]);
//		}

	}
}
