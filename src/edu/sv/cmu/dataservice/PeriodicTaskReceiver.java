package edu.sv.cmu.dataservice;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.SyncStateContract.Constants;
import android.util.Log;

//public class PeriodicTaskReceiver extends BroadcastReceiver {
//
//	private static final String TAG = "PeriodicTaskReceiver";
//	private static final String INTENT_ACTION = "com.example.app.PERIODIC_TASK_HEART_BEAT";
//	
//	public AlarmService holdService = null;
//	public Messenger gpsMessenger = null;
//	public PeriodicTaskReceiver(){
//	}
//	@Override
//	public void onReceive(Context context, Intent intent) {
//		// if (!Strings.isNullOrEmpty(intent.getAction())) {
//		MainApplication myApplication = (MainApplication) context.getApplicationContext();
//		SharedPreferences sharedPreferences = myApplication.getSharedPreferences(AlarmService.PREFS_NAME,0);
//
//		if (intent.getAction().equals("android.intent.action.BATTERY_LOW")) {
//			//sharedPreferences.edit().putBoolean(Constants.BACKGROUND_SERVICE_BATTERY_CONTROL, false).apply();
//			stopPeriodicTaskHeartBeat(context);
//		} else if (intent.getAction().equals("android.intent.action.BATTERY_OKAY")) {
//			//sharedPreferences.edit().putBoolean(Constants.BACKGROUND_SERVICE_BATTERY_CONTROL, true).apply();
//			//restartPeriodicTaskHeartBeat(context, myApplication);
//		} else if (intent.getAction().equals(INTENT_ACTION)) {
//			doPeriodicTask(context, myApplication);
//		}
//		// }
//	}
//	@SuppressLint("NewApi")
//	private static void setAlarm(Context context, Calendar calendar, PendingIntent pIntent) {
//		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//			alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
//		} else {
//			alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
//		}
//	}
//
//
//	private void doPeriodicTask(Context context, Context myApplication) {
//		// Periodic task(s) go here ...
//		Log.d(Thread.currentThread().getName(),"breath");
//		broadcast
////		Message msg = Message.obtain();
////		msg.what = 0;
////		try {
////
////			if(holdService!=null){
////				Log.e(TAG, "hold service is not null");
////			}
////			if(gpsMessenger!=null){
////				gpsMessenger.send(msg);
////			}
////
////
////		} catch (RemoteException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////
//	}	
//
//
//
//
//
//	public void restartPeriodicTaskHeartBeat(AlarmService context, Context  myApplication) {
//
//		Log.e(TAG,"restartPeriodicTaskHeartBeat");
//		// SharedPreferences sharedPreferences = myApplication.getSharedPreferences();
//		boolean isBatteryOk = true;//sharedPreferences.getBoolean(Constants.BACKGROUND_SERVICE_BATTERY_CONTROL, true);
//		this.holdService = context;
//		boolean isAlarmUp = PendingIntent.getBroadcast(context, 0, new Intent(INTENT_ACTION), PendingIntent.FLAG_NO_CREATE) != null;
//		if (isBatteryOk && !isAlarmUp) {
//			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//			Intent alarmIntent = new Intent(context, PeriodicTaskReceiver.class);
//			alarmIntent.setAction(INTENT_ACTION);
//			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
//			alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, 10000, pendingIntent);
//		}
//	}
//
//	public void stopPeriodicTaskHeartBeat(Context context) {
//		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//		Intent alarmIntent = new Intent(context, PeriodicTaskReceiver.class);
//		alarmIntent.setAction(INTENT_ACTION);
//		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
//		alarmManager.cancel(pendingIntent);
//	}
//}
