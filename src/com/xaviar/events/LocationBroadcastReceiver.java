package com.xaviar.events;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;
import com.xaviar.collect.location.LocationUtil;
import com.xaviar.ui.LocationActivity;
import com.xaviar.ui.R;

public class LocationBroadcastReceiver extends BroadcastReceiver {
	
	public static int ID = 113556;
	
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LocationBroadcastReceiver", "onReceive: received location update");
        
        final LocationInfo locationInfo = (LocationInfo) intent.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);
        
        // The broadcast has woken up your app, and so you could do anything now - 
        // perhaps send the location to a server, or refresh an on-screen widget.
        // We're gonna create a notification.
        
        // Construct the notification.
       
        LocationUtil.saveLocation(context, locationInfo);	
        
        Notification notification = new Notification(R.drawable.gyro, "Locaton updated " + locationInfo.getTimestampAgeInSeconds() + " seconds ago", System.currentTimeMillis());

        Intent contentIntent = new Intent(context, LocationActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        notification.setLatestEventInfo(context, "notify id:" + ID + " Location update received", "Timestamped " + LocationInfo.formatTimeAndDay(locationInfo.lastLocationUpdateTimestamp, true), contentPendingIntent);
        
        // Trigger the notification.
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(ID, notification);       
    }

	
}