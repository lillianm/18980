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

public class KmeansStragedy{

	public static KmeansModel geoDesicKmeans(KmeansModel kmeansModel){
		int k = kmeansModel.nbCluster;
		int neighbors = 100;
		kmeansModel.initFeatures = Util.readFeaturesFromFile();
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

	public static KmeansModel onlineKmeans(KmeansModel kmeansModel){
		int k = kmeansModel.nbCluster;

		kmeansModel.initFeatures = Util.readFeaturesFromFile();
		//System.out.println(kmeansModel.initFeatures.size());
		kmeansModel.initCentroids();



		kmeansModel.newcounts = new ArrayList<Long>();
		for(int i = 0;i<k;i++){
			kmeansModel.newcounts.add(0L);
		}

		for(Point p: kmeansModel.initFeatures){
			int nearestCentroidIndex = kmeansModel.getNearestCentroid(p);
			p.belongingId = nearestCentroidIndex;
			kmeansModel.counts.set(nearestCentroidIndex, kmeansModel.counts.get(nearestCentroidIndex) +1);

		}

		for(Point p: kmeansModel.initFeatures){
			kmeansModel.updateCenter(p);				
		}


		int changeCount = 1;
		while(changeCount >=1){
			changeCount = 0;
			for(int i = 0;i<k;i++){
				kmeansModel.counts.add(0L);
			}
			for(Point p: kmeansModel.initFeatures){
				int nearestCentroidIndex = kmeansModel.getNearestCentroid(p);
				if(p.belongingId!=nearestCentroidIndex){

					kmeansModel.counts.set(p.belongingId,kmeansModel.counts.get(p.belongingId)-1);
					kmeansModel.counts.set(nearestCentroidIndex,kmeansModel.counts.get(nearestCentroidIndex)-1);
					p.belongingId = nearestCentroidIndex;
					changeCount++;
				}


			}
			//System.out.println(kmeansModel.getCentroids()[0]+";"+kmeansModel.getCentroids()[1]+kmeansModel.getCentroids()[2]);
			System.out.println(changeCount);
			kmeansModel.counts = kmeansModel.newcounts;


			System.out.println(kmeansModel.counts);
		}
		return kmeansModel;

	}

	public static KmeansModel classicKmeans(KmeansModel kmeansModel){
		int k = 3;

		kmeansModel.initFeatures = Util.readFeaturesFromFile();
		//System.out.println(kmeansModel.initFeatures.size());
		kmeansModel.initCentroids();



		kmeansModel.newcounts = new ArrayList<Long>();
		for(int i = 0;i<k;i++){
			kmeansModel.newcounts.add(0L);
		}

		for(Point p: kmeansModel.initFeatures){
			int nearestCentroidIndex = kmeansModel.getNearestCentroid(p);
			p.belongingId = nearestCentroidIndex;
			kmeansModel.counts.set(nearestCentroidIndex, kmeansModel.counts.get(nearestCentroidIndex) +1);

		}

		for(Point p: kmeansModel.initFeatures){
			kmeansModel.updateCenter(p);				
		}


		int changeCount = 1;
		while(changeCount >=1){
			changeCount = 0;
			kmeansModel.newcounts = new ArrayList<Long>();
			for(int i = 0;i<k;i++){
				kmeansModel.newcounts.add(0L);
			}

			for(Point p: kmeansModel.initFeatures){
				int nearestCentroidIndex = kmeansModel.getNearestCentroidWithoutUpdate(p);
				if(p.belongingId!=nearestCentroidIndex){
					p.belongingId = nearestCentroidIndex;
					changeCount++;
				}

			}
			for(Point p:kmeansModel.initFeatures){
				kmeansModel.newcounts.set(p.belongingId,kmeansModel.counts.get(p.belongingId)+1);

			}
			kmeansModel.counts = kmeansModel.newcounts;
		}
		//System.out.println(kmeansModel.getCentroids()[0]+";"+kmeansModel.getCentroids()[1]+kmeansModel.getCentroids()[2]);
		System.out.println(changeCount);
		kmeansModel.counts = kmeansModel.newcounts;


		System.out.println(kmeansModel.counts);

		return kmeansModel;

	}



	public static double[] elbow(int minK, int maxK, KmeansModel kmeansModel){
		double[] dxs = new double[maxK-minK+1];
		for(int k = minK; k <= maxK; k++){
			kmeansModel = onlineKmeans(kmeansModel);
			dxs[k-minK] = kmeansModel.computeDxsSum();
		}
		return dxs;

	}
	public static void main(String[] args){

		int k = 3;
		KmeansModel kmeansModel = new KmeansModel(k);
		kmeansModel = geoDesicKmeans(kmeansModel);
//		kmeansModel = onlineKmeans(kmeansModel);
//		System.out.println(kmeansModel.computeDxsSum());
		Util.plotResult(kmeansModel);
		double[] dxs = elbow(1,10,kmeansModel);
		for(int i = 0;i<dxs.length;i++){
			System.out.println(dxs[i]);
		}

	}
}
