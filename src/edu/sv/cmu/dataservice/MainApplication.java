package edu.sv.cmu.dataservice;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class MainApplication extends Application {

    private static final String TAG = "MainApplication";

   // ...

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the singletons so their instances
        // are bound to the application process.
        // ...
        Log.e(TAG, "onCreate");
         Intent startServiceIntent = new Intent(getApplicationContext(), AlarmService.class);
         startService(startServiceIntent);
    }

    //...

}