package com.xaviar.utils;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.util.Log;

import com.xaviar.frm.DataHolder;
import com.xaviar.ui.DbHelper;
import com.xaviar.ui.StatusData;
import com.xaviar.ui.StatusProvider;
import com.xaviar.ui.UserData;

public class DbUtil {

	private static final String TAG = DbUtil.class.getSimpleName();

	public static List<UserData> getAll(Context context) {
		List<UserData> listUserData = new LinkedList<UserData>();

		Cursor c = context.getContentResolver().query(
				StatusProvider.CONTENT_URI, null, null, null,
				StatusData.GET_ALL_ORDER_BY);
		Log.d(TAG, "UserData:getAll elements size:" + c.getCount());
		try {
			while (c.moveToNext()) {
				UserData userData = new UserData();
				CharSequence user = c.getString(c
						.getColumnIndex(DbHelper.C_USER)); 
				userData.setUser((String) user);
				CharSequence createdAt = DateUtils.getRelativeTimeSpanString(
						context,
						c.getLong(c.getColumnIndex(DbHelper.C_CREATED_AT)));
				userData.setCreatedAt((String) createdAt);
				CharSequence text = c.getString(c
						.getColumnIndex(DbHelper.C_TEXT));
				userData.setText((String) text);
				CharSequence source = c.getString(c
						.getColumnIndex(DbHelper.C_SOURCE));
				userData.setSource((String) source);
				listUserData.add(userData);
			}
			return listUserData;

		} finally {
			c.close();
		}
	}

	public static int update(Context ctx, List<UserData> userDataList) {
		try {
			int num = userDataList.size();
			StatusData statusData = new StatusData(ctx);
			ContentValues values = new ContentValues();
			for (UserData userData : userDataList) {				
				values.clear();
				values.put(DbHelper.C_ID, userData.id);
				long statusCreatedAt = Long.valueOf(userData.createdAt);
				values.put(DbHelper.C_CREATED_AT, statusCreatedAt);
				values.put(DbHelper.C_SOURCE, userData.source);
				values.put(DbHelper.C_TEXT, userData.text);
				values.put(DbHelper.C_USER, userData.userName);
				statusData.insertOrIgnore(values);
			}
			Log.d(TAG, "Got " + num + " userData updates");
			return num;
		} catch (RuntimeException e) {			
			Log.e(TAG, "Failed to fetch status updates", e);
			return 0;
		}
	}

	public static int updateState(Context ctx, List<UserData> userDataList) {
		try {
			int num = userDataList.size();
			StatusData statusData = new StatusData(ctx);
			ContentValues values = new ContentValues();
			for (UserData userData : userDataList) {				
				values.clear();
				values.put(DbHelper.C_ID, userData.id);
				long statusCreatedAt = Long.valueOf(userData.createdAt);
				values.put(DbHelper.C_CREATED_AT, statusCreatedAt);
				values.put(DbHelper.C_SOURCE, userData.source);
				values.put(DbHelper.C_TEXT, userData.text);
				values.put(DbHelper.C_USER, userData.userName);
				//statusData.insertOrIgnore(values);
				//statusData.updateStatuses(values, selection, selectionArgs)
				int numAffectedRows = statusData.updateStatuses(values, DbHelper.C_SOURCE + " like " +"'" +DataHolder.STATE+"'", null);
				return numAffectedRows;
			}
			Log.d(TAG, "Got " + num + " userData updates");
			return num;
		} catch (RuntimeException e) {			
			Log.e(TAG, "Failed to fetch status updates", e);
			return 0;
		}	
	}
	
	public static long updateStateFirstTime(Context ctx, List<UserData> userDataList) {
		try {
			int num = userDataList.size();
			StatusData statusData = new StatusData(ctx);
			ContentValues values = new ContentValues();
			for (UserData userData : userDataList) {				
				values.clear();
				values.put(DbHelper.C_ID, userData.id);
				long statusCreatedAt = Long.valueOf(userData.createdAt);
				values.put(DbHelper.C_CREATED_AT, statusCreatedAt);
				values.put(DbHelper.C_SOURCE, userData.source);
				values.put(DbHelper.C_TEXT, userData.text);
				values.put(DbHelper.C_USER, userData.userName);
				long numAffectedRows = statusData.insertUpdate(values);				
				return numAffectedRows;
			}
			Log.d(TAG, "Got " + num + " userData updates");
			return num;
		} catch (RuntimeException e) {			
			Log.e(TAG, "Failed to fetch status updates", e);
			return 0;
		}	
	}
	
	
	public static String getAllGeneric(Context context, String requestSource) {
		Log.d(TAG, "Enter getAllGeneric(..)");
		StringBuilder sb = new StringBuilder();
		Cursor c = context.getContentResolver().query(
				StatusProvider.CONTENT_URI, null, DbHelper.C_SOURCE + " LIKE '" + requestSource + "'", null,
				//StatusProvider.CONTENT_URI, null,null, null,
				StatusData.GET_ALL_ORDER_BY);
		int cursorSize =  c.getCount();		
		Log.d(TAG, "UserData:getAll elements size:" + cursorSize);
		if(0 == cursorSize)
		{
		 return sb.toString();	
		}
		
		try {
			sb.append("[");
			boolean firstItem = true;
			while (c.moveToNext()) {								
					appendIfFirst(firstItem, sb);
					firstItem = false;
					CharSequence text = c.getString(c.getColumnIndex(DbHelper.C_TEXT));
					String textStr = (String) text;
					sb.append(textStr);								
			}
			sb.append("]");
			Log.d(TAG, "getAllGeneric elements json:" + sb.toString());
			return sb.toString();

		} finally {
			c.close();
		}
	}

	// remove "[" and "]" that create to the json string
	public static String removeBracket(String inStr,char whatToRemoveAtStart,char whatToRemoveAtEnd)
	{
		int index1 = inStr.indexOf(whatToRemoveAtStart);
		inStr = inStr.substring(index1+1);		
		String outStr = inStr.substring(0, inStr.lastIndexOf(whatToRemoveAtEnd));
		return outStr;
	}
	
	public static String getStateRow(Context context, String requestSource) {
		Log.d(TAG, "Enter getAllGeneric(..)");
		StringBuilder sb = new StringBuilder();
		Cursor c = context.getContentResolver().query(
				StatusProvider.CONTENT_URI, null, DbHelper.C_SOURCE + " LIKE '" + requestSource + "'", null,
				//StatusProvider.CONTENT_URI, null,null, null,
				StatusData.GET_ALL_ORDER_BY);
		int cursorSize =  c.getCount();		
		Log.d(TAG, "UserData:getAll elements size:" + cursorSize);
		if(0 == cursorSize)
		{
		 return sb.toString();	
		}		
		try {						
			while (c.moveToNext()) {																		
					CharSequence text = c.getString(c.getColumnIndex(DbHelper.C_TEXT));
					String textStr = (String) text;
					sb.append(textStr);								
			}			
			Log.d(TAG, "getAllGeneric elements json:" + sb.toString());
			return sb.toString();

		} finally {
			c.close();
		}
	}

	
	
	private static void appendIfFirst(boolean firstItem, StringBuilder sb) {
		if (firstItem) {
			// [ {first},{second},{more..} ]
		} else {
			sb.append(",");
		}
	}

		public static int delete(Context ctx,String selection, String[] selectionArgs) {
			StatusData statusData = new StatusData(ctx);
		return statusData.deleteStatuses(selection, selectionArgs); 		
	}

	

}
