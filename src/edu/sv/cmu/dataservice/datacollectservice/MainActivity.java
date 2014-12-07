package edu.sv.cmu.dataservice.datacollectservice;

import edu.sv.cmu.dataservice.AlarmService;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

				// Log.e("Launch", "onCreate");
         Intent startServiceIntent = new Intent(getApplicationContext(), AlarmService.class);
         startService(startServiceIntent);
         finish();

	}

		
}
