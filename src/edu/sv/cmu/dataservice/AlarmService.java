package edu.sv.cmu.dataservice;

import java.util.ArrayList;
import java.util.Calendar;

import edu.sv.cmu.dataservice.GPSCollector.GPSBinder;
import edu.sv.cmu.dataservice.datacollectservice.MainActivity;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;


public class AlarmService extends Service {
	public static final String TAG = "AlarmService";
	final Messenger alarmMessager = new Messenger(new IncomingHandler());
	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {

			switch(msg.what){
//			case Protocol.REGISTER_SENSOR:
//					sensorMessengers.add(msg.replyTo);
//					break;
//			case Protocol.UNREGISTER_SENSOR:
//				sensorMessengers.remove(msg.replyTo);
//				break;
//			case Protocol.REQUEST_REGISTERED_SENSOR_DATA:
//				for(Messenger messenger:sensorMessengers){
//					messenger.send(Message.obtain(null, Protocol.REQUEST_REGISTERED_SENSOR_DATA, );
//				}
			}
		}
	}
	public class AlarmBinder extends Binder{
		Messenger getAlarmMessenger(){
			return alarmMessager;
		} 
	}

	public static Messenger gpsMessenger = null;
	public ArrayList<Messenger> sensorMessengers = null;
	public MetadataLogger logger = new MetadataLogger();
	public static final String PREFS_NAME = "shared_preferrence";
	PeriodicTaskReceiver mPeriodicTaskReceiver = new PeriodicTaskReceiver();

	@Override
	public void onCreate(){
		super.onCreate();


	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "onstartCommand");
		Context myApplication =  getApplicationContext();

		Intent gpsIntent  = new Intent(this, GPSCollector.class);
		if(!isGPSServiceRunning()){
			Log.d(TAG,"GPS service is not running");
			startService(gpsIntent);

		}
		bindService(gpsIntent, alarmServiceConnection, Context.BIND_AUTO_CREATE);

		//mPeriodicTaskReceiver.holdService = this;
		SharedPreferences sharedPreferences = myApplication.getSharedPreferences(PREFS_NAME, 0);
		IntentFilter batteryStatusIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatusIntent = registerReceiver(null, batteryStatusIntentFilter);

		if (batteryStatusIntent != null) {
			int level = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			int scale = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			float batteryPercentage = level / (float) scale;
			float lowBatteryPercentageLevel = 0.14f;

			try {
				int lowBatteryLevel = Resources.getSystem().getInteger(Resources.getSystem().getIdentifier("config_lowBatteryWarningLevel", "integer", "android"));
				lowBatteryPercentageLevel = lowBatteryLevel / (float) scale;
			} catch (Resources.NotFoundException e) {
				Log.e(TAG, "Missing low battery threshold resource");
			}

			// sharedPreferences.edit().putBoolean(Constants.BACKGROUND_SERVICE_BATTERY_CONTROL, batteryPercentage >= lowBatteryPercentageLevel).apply();
		} else {
			// sharedPreferences.edit().putBoolean(Constants.BACKGROUND_SERVICE_BATTERY_CONTROL, true).apply();
		}


		mPeriodicTaskReceiver.restartPeriodicTaskHeartBeat(this, myApplication);
		return START_STICKY;
	}



	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private ServiceConnection alarmServiceConnection = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			gpsMessenger = ((GPSCollector.GPSBinder) binder).getGPSMessenger();

			Log.d(TAG,"Connected");
			if(gpsMessenger == null){
				Log.e(TAG,"Messenger is null");
			}
			//mPeriodicTaskReceiver.gpsMessenger = gpsMessenger;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d(TAG,"disconnected");			
		}

	};


	private boolean isGPSServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (service.service.getShortClassName().equals(GPSCollector.class.getSimpleName())) {
				return true;
			}
		}
		return false;
	}

	public static class PeriodicTaskReceiver extends BroadcastReceiver {

		private static final String TAG = "PeriodicTaskReceiver";
		private static final String INTENT_ACTION = "com.example.app.PERIODIC_TASK_HEART_BEAT";

		public PeriodicTaskReceiver(){}
		@Override
		public void onReceive(Context context, Intent intent) {
			// if (!Strings.isNullOrEmpty(intent.getAction())) {
			MainApplication myApplication = (MainApplication) context.getApplicationContext();
			SharedPreferences sharedPreferences = myApplication.getSharedPreferences(AlarmService.PREFS_NAME,0);

			if (intent.getAction().equals("android.intent.action.BATTERY_LOW")) {
				//sharedPreferences.edit().putBoolean(Constants.BACKGROUND_SERVICE_BATTERY_CONTROL, false).apply();
				stopPeriodicTaskHeartBeat(context);
			} else if (intent.getAction().equals("android.intent.action.BATTERY_OKAY")) {
				//sharedPreferences.edit().putBoolean(Constants.BACKGROUND_SERVICE_BATTERY_CONTROL, true).apply();
				//restartPeriodicTaskHeartBeat(context, myApplication);
			} else if (intent.getAction().equals(INTENT_ACTION)) {
				doPeriodicTask(context, myApplication);
			}
			// }
		}


		private void doPeriodicTask(Context context, Context myApplication) {
			// Periodic task(s) go here ...
			Log.d(Thread.currentThread().getName(),"breath");
			//broadcast
			Message msg = Message.obtain();
			msg.what = Protocol.REQUEST_GPS;
			try {

				if(gpsMessenger!=null){
					gpsMessenger.send(msg);
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//
		}	



		@SuppressLint("NewApi")
		private void setAlarm(Context context, Calendar calendar, PendingIntent pIntent) {
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
				alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
			} else {
				alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
			}
		}


		public void restartPeriodicTaskHeartBeat(AlarmService context, Context  myApplication) {

			Log.e(TAG,"restartPeriodicTaskHeartBeat");
			// SharedPreferences sharedPreferences = myApplication.getSharedPreferences();
			boolean isBatteryOk = true;//sharedPreferences.getBoolean(Constants.BACKGROUND_SERVICE_BATTERY_CONTROL, true);
			//this.holdService = context;
			boolean isAlarmUp = PendingIntent.getBroadcast(context, 0, new Intent(INTENT_ACTION), PendingIntent.FLAG_NO_CREATE) != null;
			if (isBatteryOk && !isAlarmUp) {
				AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				Intent alarmIntent = new Intent(context, PeriodicTaskReceiver.class);
				alarmIntent.setAction(INTENT_ACTION);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
				alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, 10000, pendingIntent);
			}
		}

		public void stopPeriodicTaskHeartBeat(Context context) {
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			Intent alarmIntent = new Intent(context, PeriodicTaskReceiver.class);
			alarmIntent.setAction(INTENT_ACTION);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
			alarmManager.cancel(pendingIntent);
		}
	}




}

