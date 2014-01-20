package com.xaviar.ui;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class UpdaterService extends Service {
	private static final String TAG = "UpdaterService";
	public static final String NEW_STATUS_INTENT = "com.xaviar.ui.NEW_STATUS";
	public static final String NEW_STATUS_EXTRA_COUNT = "com.xaviar.ui.NEW_STATUS_EXTRA_COUNT";
	public static final String RECEIVE_TIMELINE_NOTIFICATIONS = "com.xaviar.ui.RECEIVE_TIMELINE_NOTIFICATIONS";
	static final int DELAY = 1000; 
	private boolean runFlag = false;
	private Updater updater;
	XaviarApplication xaviar;
	Context context;
	private static final int PERIOD=10000; 	// 30 minutes
	//private PendingIntent pi=null;
	//private AlarmManager mgr=null;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG, "onCreated");
		updater = new Updater();
		xaviar = (XaviarApplication) getApplication();
		context = xaviar.getApplicationContext();
		//locationFinder = new LocationFinder(xaviar.getApplicationContext(),xaviar);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		this.runFlag = false;
		this.updater.interrupt();
		updater = null;
		xaviar.setServiceRunning(false);
		
		Log.d(TAG, "onDestroy");
	}

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);
		Log.d(TAG, "onStartCommand");
		
		if (this.runFlag == false) {
			this.runFlag = true;
			//locationFinder.enable();
			this.updater.start();
			xaviar.setServiceRunning(true);
		}
		
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onBind");
		return null;
	}
	
	private class Updater extends Thread {		
		public Updater() {
			super("UpdaterService-Updater");
		}
		
		public void run() {
			UpdaterService updaterService = UpdaterService.this;
			while (updaterService.runFlag) {
				Log.d(TAG, "Updater thread running");
				try {
				
					Log.d(TAG, "Startar callToLocation");
				    //callToLocation();
					//if (newStatusCount > 0) {
					//	Log.d(TAG, "We have new statuses");
					//	intent = new Intent(NEW_STATUS_INTENT);
					//	intent.putExtra(NEW_STATUS_EXTRA_COUNT, newStatusCount);
					//	updaterService.sendBroadcast(intent, RECEIVE_TIMELINE_NOTIFICATIONS);
					//}
					//-------- End work -------------
					Log.d(TAG, "Updater thread finished, sleeping thread...");
					long interval = xaviar.getUpdateInterval();
					if(0 == interval)
					{
					  Thread.sleep(DELAY *100000);	
					}
					else
					{
					 Thread.sleep(interval);	
					}
				
					//handleLocation();
				} catch (InterruptedException e ) {
					updaterService.runFlag = false;
				}
			}
		}

		
	
		
	}

	

}
