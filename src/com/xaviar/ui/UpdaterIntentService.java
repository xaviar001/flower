package com.xaviar.ui;


import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.xaviar.frm.ServerFsm;

public class UpdaterIntentService extends IntentService {
	
	private static final String TAG = UpdaterIntentService.class.getSimpleName();
	private static final long DELATA_NOTIFY_MILLIS = 5*60000;
	private static int NOTIFY_ID = 23883;
	private ServerFsm serverFsm = new ServerFsm();
	private static long nextNotifyTime = 0;
	private long currentTimeMillis = 0;
	

	public UpdaterIntentService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent inIntent) {			
		Log.d(TAG,"Enter onHandleIntent(Intent)");
		XaviarApplication xaviar = (XaviarApplication) getApplication();
		notifyUser(xaviar.getApplicationContext());		
		serverFsm.execute(xaviar.getApplicationContext());		
	}
	
	private void notifyUser(Context context)
	{
	 if(!needToNotify())
	 {
	   return;	 
	 }
			
	 nextNotifyTime = currentTimeMillis + DELATA_NOTIFY_MILLIS;	
     Notification notification = new Notification(R.drawable.circles, "updateIntentid:"+NOTIFY_ID, System.currentTimeMillis());
	 Intent contentIntent = new Intent(context, LocationActivity.class);
	 PendingIntent contentPendingIntent = PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);	        
	 notification.setLatestEventInfo(context,  "notify id:" +NOTIFY_ID + " Timestamped " +" Location received", "Timestamped " + LocationInfo.formatTimeAndDay(System.currentTimeMillis(), true), contentPendingIntent);	        	
	 ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFY_ID, notification);	        	
	}

	private boolean needToNotify() {
		currentTimeMillis = System.currentTimeMillis();
		long delta = nextNotifyTime - currentTimeMillis;
		if (delta > 0) {
			return false;
		}
		return true;
	}
	
}
