package edu.sv.cmu.dataservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootAndUpdateReceiver extends BroadcastReceiver {

    private static final String TAG = "BootAndUpdateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") ||
                intent.getAction().equals("android.intent.action.MY_PACKAGE_REPLACED")) {
            //Intent startServiceIntent = new Intent(context, AlarmService.class);
            //context.startService(startServiceIntent);
        }
    }
}
