package edu.sv.cmu.clustering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.sv.cmu.clustering.util.MathUtil;
import edu.sv.cmu.clustering.util.Util;

//import edu.sv.cmu.clustering.exception.ClusterException;

public class KmeansModel {

	public static boolean IS_DEBUG = false;

	public List<Long> counts = null;
	public List<Long> newcounts = null;
	public List<double[]> newCentroid = null;
	public Center[] centroids;
	public List<Point> initFeatures = new ArrayList<Point>();
	public Integer nbCluster;

	public KmeansModel(Integer nbCluster) {
		this.nbCluster = nbCluster;

	}

	protected int getNearestCentroidWithoutUpdate(Point p) {
		// Find nearest centroid
		Integer nearestCentroidIndex = p.belongingId;

		Double minDistance = Double.MAX_VALUE;
		Center currentCentroid;
		Double currentDistance;
		for (int i = 0; i < this.centroids.length; i++) {
			currentCentroid = this.centroids[i];
			if (currentCentroid != null) {
				currentDistance = currentCentroid.euclideanDistanceTo(p);
				if (currentDistance < minDistance) {
					minDistance = currentDistance;
					nearestCentroidIndex = i;
				}
			}
		}

		return nearestCentroidIndex;
	}

	protected int getNearestCentroid(Point point) {

		int nearestCentroidIndex = point.belongingId;

		Center currentCentroid;
		Double currentDistance;

		// looping through all centroids 
		for (int i = 0; i < this.centroids.length; i++) {
			currentCentroid = this.centroids[i];
			if (currentCentroid != null) {
				currentDistance = currentCentroid.euclideanDistanceTo(point);
				if (currentDistance < point.minDist) {
					point.minDist = currentDistance;
					nearestCentroidIndex = i;
				}
			}
		}

		return nearestCentroidIndex;
	}



	public void calculateNewCentroids(){

	}

	public Integer updateCenter(Point point) {
		if (!this.isReady()) {
			//this.initIfPossible(features);
			return null;
		} else {
			//Integer centroidIndex = this.classify(point);
			int centroidIndex = point.belongingId;
			// Increment count


			//center value is updated within this function
			updateCentroidValue(centroidIndex, point);
			this.newcounts.set(centroidIndex, this.newcounts.get(centroidIndex) + 1);
			return centroidIndex;
		}


	}

	/* return the updatedCenter*/
	public Center updateCentroidValue(int centroidIndex, Point point){
		Center center = centroids[centroidIndex];
		long count = this.counts.get(centroidIndex);
		center.latitude += ( point.latitude - center.latitude)/count;
		center.longitude += ( point.longitude - point.longitude)/count;
		return center;
	}

	public double[] distToAllCenters(Point point) {
		if (!this.isReady()) {
			throw new IllegalStateException("KMeans is not ready yet");
		}

		double[] dist = new double[this.nbCluster];
		Center currentCentroid;
		for (int i = 0; i < this.nbCluster; i++) {
			currentCentroid = this.centroids[i];
			dist[i] = currentCentroid.euclideanDistanceTo(point);
		}

		return dist;
	}

	public Center[] getCentroids() {
		return this.centroids;
	}

	protected boolean isReady() {
		boolean countsReady = this.counts != null;
		boolean centroidsReady = this.centroids != null;
		return countsReady && centroidsReady;
	}

	protected void initIfPossible(Point point) {
		this.initFeatures.add(point);

		// magic number : 10 ??!
		if (this.initFeatures.size() >= 10 * this.nbCluster) {
			this.initCentroids();
		}
	}
	protected double[] computeDxs() {
		double[] dxs = new double[this.initFeatures.size()];

		int sum = 0;
		Point features;
		int nearestCentroidIndex;
		Center nearestCentroid;
		for (int i = 0; i < this.initFeatures.size(); i++) {
			features = this.initFeatures.get(i);
			nearestCentroidIndex = this.getNearestCentroidWithoutUpdate(features);
			nearestCentroid = this.centroids[nearestCentroidIndex];
			sum += Math.pow(nearestCentroid.euclideanDistanceTo(features), 2);
			dxs[i] = sum;
		}

		return dxs;
	}

	protected void initCentroids() {
		// Init counts
		this.counts = new ArrayList<Long>(this.nbCluster);
		for (int i = 0; i < this.nbCluster; i++) {
			this.counts.add(0L);
		}

		this.centroids = new Center[this.nbCluster];

		Random random = new Random();

		// Choose one centroid uniformly at random from among the data points.
		//generate centroid
		Center firstCentroid = new Center(this.initFeatures.remove(random.nextInt(this.initFeatures.size())));
		this.centroids[0] = firstCentroid;

		double[] dxs;

		for (int j = 1; j < this.nbCluster; j++) {
			// For each data point x, compute D(x)
			dxs = this.computeDxs();

			// Add one new data point as a center.
			double r = random.nextDouble() * dxs[dxs.length - 1];

			for (int i = 0; i < dxs.length; i++) {

				if (dxs[i] >= r) {
					boolean duplicated  = false;
					Point candidate = initFeatures.get(i);
					for(int ii = 0;ii<j;ii++){
						if(candidate.latitude == centroids[ii].latitude && candidate.longitude == centroids[ii].longitude){
							duplicated = true;
							break;
						}
					}
					if(duplicated){continue;}
					Center newCenter = new Center(this.initFeatures.remove(i));
					this.centroids[j] = newCenter;
					break;
				}
			}
		}

		this.initFeatures.clear();
	}



	/**
	 * For each features in {@link KMeans#initFeatures}, compute D(x), the
	 * distance between x and the nearest center that has already been chosen.
	 * 
	 * @return
	 */
	protected double[] computeDxs(int j) {
		double[] dxs = new double[this.initFeatures.size()];

		int sum = 0;
		Point samplePoint;
		int nearestCentroidIndex;
		Center nearestCentroid;
		for (int i = 0; i < this.initFeatures.size(); i++) {
			samplePoint = this.initFeatures.get(i);
			nearestCentroidIndex = this.getNearestCentroidWithoutUpdate(samplePoint);
			nearestCentroid = this.centroids[nearestCentroidIndex];
			sum += Math.pow(nearestCentroid.euclideanDistanceTo(samplePoint), 2);
			dxs[i] = sum;
		}
		for(int i =0;i<=j;i++){
			dxs[i] = Double.MAX_VALUE;
		}

		return dxs;
	}

	protected double computeDxsSum() {
		double[] dxs = new double[this.initFeatures.size()];

		//int totalSum = 0;
		int sum = 0;
		Point samplePoint;
		int nearestCentroidIndex;
		Center nearestCentroid;
		for (int i = 0; i < this.initFeatures.size(); i++) {
			samplePoint = this.initFeatures.get(i);
			nearestCentroidIndex = this.getNearestCentroidWithoutUpdate(samplePoint);
			nearestCentroid = this.centroids[nearestCentroidIndex];
			sum += Math.pow(nearestCentroid.euclideanDistanceTo(samplePoint), 2);
			//dxs[i] = sum;
		}

		return sum;
	}

	public void reset() {
		this.counts = null;
		this.centroids = null;
		this.initFeatures = new ArrayList<Point>();
	}

	public void initBuffer(){
		newcounts = new ArrayList<Long>();
		newCentroid = new ArrayList<double[]>();

		for(int i = 0;i<nbCluster;i++){
			newcounts.add(0L);
			newCentroid.add(new double[2]);
		}

	}

	public void getDistribution(ArrayList<Point> points, Center[] centroids){

	}

	public void precomputeWeight() {
		for(Point p1:initFeatures){
			for(Point p2:initFeatures){
				if(p1!=p2){
					p1.dist.put(p2, MathUtil.euclideanDistance(p1,p2));
					p1.pq.add(p2);
				}
			}
			System.out.println("ehe");
		}
		// TODO Auto-generated method stub

	}

	public void printAllCentroids(){
		for(int i = 0;i<nbCluster;i++){
			System.out.println(centroids[i]);
		}
	}

	public void calculateRadius(){
		for(Point point: initFeatures){
			if(centroids[point.belongingId].radius<= point.minDist){
				centroids[point.belongingId].furthestPoint = point;
				centroids[point.belongingId].radius = point.minDist;
			}
			
		}
	}
	public void calculateTimeDistribution(){
		for(Point point: initFeatures){
			Center centroid = centroids[point.belongingId];
			centroid.updateTimeDistribution(Util.getDateFromString(point.timeStamp));
		}
		for(int i = 0;i<nbCluster;i++){
			centroids[i].printTimeDistribution();
		}
	}

	public void calculateDailyDistribution(){
		int prevLabel = -1;
		int prevTime = 0;
		LinkedHashMap<String, Integer> shiftMap = new LinkedHashMap<String, Integer>();
		LinkedHashMap<Integer, Integer> timeMap = new LinkedHashMap<Integer, Integer>();
		for(int i = 0;i<initFeatures.size(); i++){
			Point point = initFeatures.get(i);
			if(point.belongingId!=prevLabel){
				Date date = Util.getDateFromString(point.timeStamp);
				System.out.println(prevLabel+","+ point.belongingId);

				Calendar c = Calendar.getInstance();
				c.setTime(date);
				shiftMap.put(c.get(Calendar.DAY_OF_WEEK)+" "+ c.get(Calendar.HOUR_OF_DAY) +":"+c.get(Calendar.MINUTE)+"", point.belongingId);
				//timeMap.put()
				prevLabel = point.belongingId;
			}
		}
		System.out.println(shiftMap);
	}

}
