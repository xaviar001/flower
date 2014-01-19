package com.xaviar.ui;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

public class CommandProcessor implements LocationListener {



	public static final String LOG_TAG = "CommandProcessor";
	public static final int LOCATION_REQUEST_TIMEOUT = 1000 * 60 * 3; // 3 minutes
	private static final long USE_OLD_FIX_THRESHOLD = 1000 * 60 * 3; // 3 minutes
	private static final int GPS_UPDATE_INTERVAL = 1000 * 20; // 20 seconds
	private Context context;
	private boolean inSearch;
	private LocationManager locationManager;
	private String currentFromAddress;
	private String currentProvider;
	


	public CommandProcessor(Context context) {
		this.context = context;	
	}

	private void retreiveBestLocation(boolean networkOk) {
		currentProvider = null;
		if(inSearch) {
			Log.d(LOG_TAG, "retrieveBestLocation begin (inSearch)");
			locationManager.removeUpdates(this);
		} else {
			Log.d(LOG_TAG, "retrieveBestLocation begin (NOT inSearch)");
		}
		// Check if we have an old location that will do
		Log.d(LOG_TAG, "Check for an acceptable last known location");
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		long threshold = Calendar.getInstance().getTimeInMillis() - USE_OLD_FIX_THRESHOLD;
		if(location != null && location.getTime() > threshold ) {
			Log.d(LOG_TAG, "Found an acceptable last known location (GPS)");
			processLocation(location, LocationManager.GPS_PROVIDER); // Found an OK GPS location
		} else if(networkOk) { // Check if we have a current network location
			Log.d(LOG_TAG, "OK to use Network provider");
			if(!providerExists(LocationManager.NETWORK_PROVIDER)) {
				failLocationSearch();
			} else {
				Log.d(LOG_TAG, "Reading location from Network");
				location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if(location != null && location.getTime() > threshold) {
					Log.d(LOG_TAG, "Found an acceptable last known location (Network)");
					processLocation(location, LocationManager.NETWORK_PROVIDER); // Found an OK Network location
				} else {
					Log.d(LOG_TAG, "Request Location Updates (Network)");
					inSearch = true;
					currentProvider = LocationManager.NETWORK_PROVIDER;				
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
				}
			}
		} else { // Try to get GPS fix
			if(providerExists(LocationManager.GPS_PROVIDER)) {
				Log.d(LOG_TAG, "Trying to get GPS Fix");
				inSearch = true;
				currentProvider = LocationManager.GPS_PROVIDER;			
				Log.d(LOG_TAG, "requestLocationUpdates");
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_UPDATE_INTERVAL, 0, this);
				Log.d(LOG_TAG, "LocationUpdates called");

			} else if(!networkOk) { // Try network
				retreiveBestLocation(true);
			}
		}
		Log.d(LOG_TAG, "retreiveBestLocation done");
	}

	private void failLocationSearch() {
		Log.d(LOG_TAG, "Failed to get location. Taking last known location even though it's old. Considering both GPS and network");
		Location gpsLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location netLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if(gpsLoc == null && netLoc == null) {
			Log.d(LOG_TAG, "No last known location at all!");
			processLocation(null, null);
		} else if(netLoc != null && (gpsLoc == null || netLoc.getTime() > gpsLoc.getTime())) {
			Log.d(LOG_TAG, "Failback to last known NETWORK");
			processLocation(netLoc, LocationManager.NETWORK_PROVIDER);
		} else {
			Log.d(LOG_TAG, "Failback to last known GPS");
			processLocation(gpsLoc, LocationManager.GPS_PROVIDER);			
		}
	}

	private boolean providerExists(String checkProvider) {
		List<String> plist = locationManager.getAllProviders();
		Log.d(LOG_TAG, "providerExists. Nr providers " + plist.size());
		boolean result = false;
		for (Iterator<String> iterator = plist.iterator(); iterator.hasNext();) {
			String provider = iterator.next();
			Log.d(LOG_TAG, "Checking providers... " + provider);
			if(checkProvider.equals(provider)) {
				result  = true;
				break;
			}
		}
		return result;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(LOG_TAG, "Location Changed " + currentProvider);
		locationManager.removeUpdates(this);
		processLocation(location, currentProvider);
	}

	private void processLocation(Location location, String provider) {
		inSearch = false;		

		if(location != null) {
			double lat = location.getLatitude();
			double lon = location.getLongitude();
			Log.d(LOG_TAG, "Got fix! lat " + lat + ", long " + lon);
		}
		// send UDP
		report(location);
	}


	public synchronized int report(Location location) {
				
	     Log.d(LOG_TAG, "Going to send msg:" + " ");		
		
		 return 0;		
	}

	
	@Override
	public void onProviderDisabled(String provider) {
		Log.d(LOG_TAG, "Provider " + provider + " disabled!");
		if(LocationManager.GPS_PROVIDER.equals(provider)) {
			locationManager.removeUpdates(this);
		}
	}

	

	@Override
	public void onProviderEnabled(String provider) {
		Log.d(LOG_TAG, "Provider enabled " + provider);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d(LOG_TAG, "Change of status " + status);
		if(inSearch) {
			switch(status) {
			case LocationProvider.OUT_OF_SERVICE:
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				Log.d(LOG_TAG, "Location Not available yet. Trying Network location (Current " + currentProvider + ")");
				inSearch = false;
				locationManager.removeUpdates(this);
				retreiveBestLocation(true);
				break;
			}
		}
	}

	
	public void processCommand(String command) {
	
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);				
		Log.d(LOG_TAG, "processCommand, reply to phonenr: " + currentFromAddress);
		retreiveBestLocation(false);
	}

}
