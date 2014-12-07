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

import edu.sv.cmu.clustering.maputil.*;

public class Kmeans implements Serializable {

	public List<Long> counts = null;
	public List<Long> newcounts = null;
	private Center[] centroids;
	public List<Point> initFeatures = new ArrayList<Point>();
	private HashMap<Point, Integer> belongingId = new HashMap<Point, Integer>();
	private Integer nbCluster;

	public Kmeans(Integer nbCluster) {
		this.nbCluster = nbCluster;

	}

	public Integer classify(Point point) {
		if (!this.isReady()) {
			throw new IllegalStateException("KMeans is not ready yet");
		}

		// Find nearest centroid
		Integer nearestCentroidIndex = this.getNearestCentroid(point);
		return nearestCentroidIndex;
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
		
		//p.belongingId = nearestCentroidIndex;
		if(nearestCentroidIndex == -1){
			System.out.println(p);
		}
		return nearestCentroidIndex;
	}

	protected int getNearestCentroid(Point p) {
		// Find nearest centroid
		Integer nearestCentroidIndex = p.belongingId;

		//Double minDistance = p.minDist;
		Center currentCentroid;
		Double currentDistance;
		for (int i = 0; i < this.centroids.length; i++) {
			currentCentroid = this.centroids[i];
			if(currentCentroid == null) System.exit(0);
			//System.out.println(currentCentroid);
			if (currentCentroid != null) {
				currentDistance = currentCentroid.euclideanDistanceTo(p);
				if (currentDistance < p.minDist) {
					p.minDist = currentDistance;
					nearestCentroidIndex = i;
				}
			}
		}
		
		//p.belongingId = nearestCentroidIndex;
		if(nearestCentroidIndex == -1){
			System.out.println(p);
		}
		return nearestCentroidIndex;
	}

	/* calculate distance */
	public double euclideanDistance(Point p1, Point p2){
		// the longitude of -180 to +180
		// latitude is -90 to +90

		double longitudeDiff = Math.abs(p1.longitude - p1.longitude);
		longitudeDiff = Math.min(longitudeDiff, 360 - longitudeDiff);
		double latitudeDiff  = Math.abs(p1.latitude - p2.latitude);

		double dist =  Math.sqrt(longitudeDiff * longitudeDiff + latitudeDiff * latitudeDiff);
		return dist;

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

	/**
	 * Init clusters using the k-means++ algorithm. (Arthur, D. and
	 * Vassilvitskii, S. (2007). "k-means++: the advantages of careful seeding".
	 * 
	 */
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
		int randIndex = random.nextInt(this.initFeatures.size());
		Point tmp = this.initFeatures.get(0);
		this.initFeatures.set(0, this.initFeatures.get(randIndex));
		this.initFeatures.set(randIndex, tmp);
		
		final Point randPoint = this.initFeatures.remove(random.nextInt(this.initFeatures.size()));

		this.centroids[0] = new Center(randPoint);

		double[] dxs;

		for (int j = 1; j < this.nbCluster; j++) {
			// For each data point x, compute D(x)
			dxs = this.computeDxs(j);

			// Add one new data point as a center.
			Point nextRandPoint;
			double r = random.nextDouble() * dxs[dxs.length - 1];
			for (int i = 0; i < dxs.length; i++) {
				if (dxs[i] >= r) {
					Point p = this.initFeatures.get(j);
					this.initFeatures.set(j, this.initFeatures.get(i));
					this.initFeatures.set(i,p );

					nextRandPoint = this.initFeatures.get(j);
					this.centroids[j] = new Center(nextRandPoint);
					break;
				}
			}
			System.out.println(centroids);
		}

		/* why ???? */
		//this.initFeatures.clear();
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


	public void reset() {
		this.counts = null;
		this.centroids = null;
		this.initFeatures = new ArrayList<Point>();
	}


			

	
	
}
