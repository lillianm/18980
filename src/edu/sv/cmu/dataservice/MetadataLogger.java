package edu.sv.cmu.dataservice;

import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;





public class MetadataLogger {

	public static File outputFolder = null;
	public String timeStamp;

	/* File extensions */

	public void setOutputDirectory(File out) {
		outputFolder = out;
	}

	public void setOutputDirectory(String out) {
		outputFolder = new File(out);
	}

	public void setTime(String timestamp) {
		timeStamp = timestamp;
	}
	
	public File getOutputFolder(){
		// To be safe, you should check that the SDCard is mounted
				// using Environment.getExternalStorageState() before doing this.
				File storageDir;
				String dirName = "AlarmService/GPSData/";
				storageDir = new File(Environment.getExternalStoragePublicDirectory(
							Environment.DIRECTORY_PICTURES), dirName);

				// This location works best if you want the created images to be shared
				// between applications and persist after your app has been uninstalled.

				// Create the storage directory if it does not exist
				if (! storageDir.exists()){
					if (! storageDir.mkdirs()){
						Log.e("MainActivity", "failed to create VehicleStateEstimation directory");
						return null;
					}
				}

				
				
				// Create a media file name
				timeStamp = new SimpleDateFormat(Protocol.dateFormat).format(new Date());
				File outputFolder;

				outputFolder = new File(storageDir.getPath() );
				if (! outputFolder.exists()) {
					if (! outputFolder.mkdirs()) {
						Log.e("MainActivity", "failed to create local directory");
						return null;
					}
				}
				return outputFolder;


	}
	public void setTimestampHeader() {
		try {

			String filename = Protocol.STARTSTOP_FILENAME;
			String starttime = new SimpleDateFormat(Protocol.dateFormat).format(new Date());


			Log.e("LOGGER",filename);
			String fullPath = new File(outputFolder.getPath() + File.separator + timeStamp + "-" + filename).getPath();
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fullPath, true)));

			out.println(starttime);

			out.close();

		} catch (IOException e) {
			// Uh oh!
			Log.e("MetadataLogger", e.getMessage());
		} 	
	}

	public void setTimestampFooter() {
		try {

			String endtime = new SimpleDateFormat(Protocol.dateFormat).format(new Date());

			String filename = Protocol.STARTSTOP_FILENAME;
			Log.e("LOGGER",filename);
			String fullPath = new File(outputFolder.getPath() + File.separator + timeStamp + "-" + filename).getPath();
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fullPath, true)));

			out.println(endtime);

			out.close();


		} catch (IOException e) {
			// Uh oh!
			Log.e("MetadataLogger", e.getMessage());
		} 	}


//	public void appendToFileSingleSensor(SensorNames sensorName, float[] value) {
//		try {
//			String time = new SimpleDateFormat(Protocol.dateFormat).format(new Date());
//
//
//			String filename = Protocol.fileExtension.get(sensorName);
//			//Log.e("LOGGER",filename);
//			String fullPath = new File(outputFolder.getPath() + File.separator + timeStamp + "-" + filename).getPath();
//			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fullPath, true)));
//			/* current time*/
//			out.println(time +": "+value[0] + ", " + value[1] + ", "+value[2]);
//
//			out.close();
//
//
//		} catch (IOException e) {
//			// Uh oh!
//			Log.e("MetadataLogger", e.getMessage());
//		} 	}

	public void appendToFileGPS(double d, double f) {
		try {
			String time = new SimpleDateFormat(Protocol.dateFormat).format(new Date());


			String filename = Protocol.fileExtension.get(Protocol.SensorNames.GPS);
			Log.e("LOGGER",filename);
			outputFolder = getOutputFolder();
			String fullPath = new File(outputFolder.getPath() + File.separator + filename).getPath();
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fullPath, true)));
			/* current time*/
			out.println(time +": "+d+", "+f);

			out.close();


		} catch (IOException e) {
			// Uh oh!
			Log.e("MetadataLogger", e.getMessage());
		} 	}


//	public void appendToFile(JSONObject obj) {
//		try {
//			String time = new SimpleDateFormat(Protocol.dateFormat).format(new Date());
//
//			for(SensorNames name:Protocol.SensorNames.values()){
//
//				String filename = Protocol.fileExtension.get(name);
//				Log.e("LOGGER",filename);
//				String fullPath = new File(outputFolder.getPath() + File.separator + timeStamp + "-" + filename).getPath();
//				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fullPath, true)));
//				/* current time*/
//				out.println(time +": "+obj.getString(name.name()));
//
//				out.close();
//			}
//
//		} catch (IOException e) {
//			// Uh oh!
//			Log.e("MetadataLogger", e.getMessage());
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//
//		}
//	}
}
