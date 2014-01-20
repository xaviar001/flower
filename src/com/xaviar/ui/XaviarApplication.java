package com.xaviar.ui;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;


public class XaviarApplication extends Application implements OnSharedPreferenceChangeListener{
	private static final String TAG = XaviarApplication.class.getSimpleName();
	
	private SharedPreferences prefs;
	private boolean serviceRunning;
	private StatusData statusData;
	public static final String LOCATION_PROVIDER_NONE = "NONE";
	private AlarmManager alarmManager;
	PendingIntent pendingIntent;	
	
	public void pauseAlarm() {
		if (pendingIntent != null) {
			alarmManager.cancel(pendingIntent);
		}
		Log.d(TAG, "pauseAlarm");
	}

	public void setAlarm() {
		long interval = getUpdateInterval();
		Intent intent = new Intent(this, UpdaterIntentService.class);
		pendingIntent = PendingIntent.getService(this, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		if (interval != 0) {
			Log.d(TAG, "setAlarm: Setting Alarm to interval " + interval);
			alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), interval, pendingIntent);
		} else {
			Log.d(TAG, "setAlarm: Stopping Alarm");
			alarmManager.cancel(pendingIntent);
		}
	}
	
	

	public SharedPreferences getPrefs() {
		return prefs;
	}
	
	public String getProvider() {
	    return prefs.getString("locationProvider", LOCATION_PROVIDER_NONE);
	  }

	public long getUpdateInterval() {
		return Long.parseLong(prefs.getString("interval", "0"));
	}
	
	public StatusData getStatusData() {
		return statusData;
	}
	


	public boolean isServiceRunning () {
		return serviceRunning;
	}

	@Override
	public void onCreate() {		
		super.onCreate();
		Log.i(TAG, "onCreated");
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.prefs.registerOnSharedPreferenceChangeListener(this);
		//this.fsm = new Fsm(this.getApplicationContext());
		statusData = new StatusData(this);
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	}

	@Override
	public synchronized void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
		this.setAlarm();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		Log.d(TAG, "onTerminate()");
	}

	public void setServiceRunning (boolean serviceRunning){
		this.serviceRunning = serviceRunning;
	}

	
	
}
