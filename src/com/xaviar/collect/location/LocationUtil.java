package com.xaviar.collect.location;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.xaviar.frm.DataHolder;
import com.xaviar.ui.UserData;
import com.xaviar.utils.DbUtil;

import flexjson.JSONSerializer;

public class LocationUtil {

	private static final String TAG = LocationUtil.class.getSimpleName();

	public static String getAllLocationGeneric(Context ctx) {
		 String  genericData = DbUtil.getAllGeneric(ctx,DataHolder.LOCATION_EVENT);
		 if(genericData.length() < 6)
		 {
			  return generateEmptyLocationEvent();	  	 
		 }
		return genericData;
	}

	private static String generateEmptyLocationEvent() {

		long seconds = System.currentTimeMillis();// / 1000l;
		Location location = new Location();
		String secStr1 = String.valueOf(seconds - 50000);
		location.setTime(secStr1);
		location.setLatitude("32.050145");
		location.setLongitude("34.769552");
		List<Location> allLocation = new LinkedList<Location>();
		allLocation.add(location);
		String ret = LocationUtil.toJsonArray(allLocation);
		Log.d(TAG, "toJsonArray:" + ret);
		return ret;
	}

	public static List<Location> getAllLocationStub(Context ctx) {
		// 32.050145,34.769552 // herzal 160
		// 32.056039,34.770814 // herzal 100
		// 32.082591,34.771275 // ben-yehuda 100
		// 32.066158,34.777819 // alenbi-100
		long seconds = System.currentTimeMillis();// / 1000l;

		Location loc1 = new Location();
		String secStr1 = String.valueOf(seconds - 50000);
		loc1.setTime(secStr1);
		loc1.setLatitude("32.050145");
		loc1.setLongitude("34.769552");

		Location loc2 = new Location();
		String secStr2 = String.valueOf(seconds - 40000);
		loc2.setTime(secStr2);
		loc2.setLatitude("32.056039");
		loc2.setLongitude("34.770814");

		Location loc3 = new Location();
		String secStr3 = String.valueOf(seconds - 30000);
		loc3.setTime(secStr3);
		loc3.setLatitude("32.082591");
		loc3.setLongitude("34.771275");

		Location loc4 = new Location();
		String secStr4 = String.valueOf(seconds);
		loc4.setTime(secStr4);
		loc4.setLatitude("32.066158");
		loc4.setLongitude("34.777819");

		List<Location> locations = new LinkedList<Location>();
		locations.add(loc1);
		locations.add(loc2);
		locations.add(loc3);
		locations.add(loc4);

		return locations;
	}

	public static void saveLocation(Context ctx, LocationInfo locationInfo) {

		Location location = createLocation(locationInfo);
		saveJsonToDB(ctx, location);
	}

	private static Location createLocation(LocationInfo locationInfo) {
		long lastTimeUpdate = locationInfo.lastLocationUpdateTimestamp;
		String latitude = Float.toString(locationInfo.lastLat);
		String longtitude = Float.toString(locationInfo.lastLong);
		String accuracyMeters = Integer.toString(locationInfo.lastAccuracy);
		String provider = locationInfo.lastProvider;
		Location location = new Location();
		String timeStr = String.valueOf(lastTimeUpdate);
		location.setTime(timeStr);
		location.setId(timeStr);
		location.setLatitude(latitude);
		location.setLongitude(longtitude);
		location.setProvider(provider);
		location.setAccuracyMeters(accuracyMeters);
		return location;
	}

	private static void saveJsonToDB(Context ctx, Location locationMsg) {
		List<Location> listLocation = new LinkedList<Location>();
		listLocation.add(locationMsg);
		String jsonTextWithBrackrt = toJsonArray(listLocation);
		Log.w(TAG, "save:jsonText:" + jsonTextWithBrackrt);
		String jsonText = DbUtil.removeBracket(jsonTextWithBrackrt,'[',']');
		Log.w(TAG, "location save:jsonText:" + jsonText);
		UserData userData = new UserData();
		userData.setText(jsonText);
		userData.setSource(DataHolder.LOCATION_EVENT);
		List<UserData> userDataList = new LinkedList<UserData>();
		userDataList.add(userData);
		DbUtil.update(ctx, userDataList);
	}

	public static String toJsonArray(Collection<Location> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

}
