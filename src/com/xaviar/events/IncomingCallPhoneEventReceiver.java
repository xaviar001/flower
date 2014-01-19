package com.xaviar.events;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.xaviar.collect.calllog.CallLogEntry;
import com.xaviar.collect.phone_event.PhoneEventUtil;

public class IncomingCallPhoneEventReceiver extends BroadcastReceiver {

	private static final String TAG = IncomingCallPhoneEventReceiver.class.getSimpleName();
	
	private static String TYPE = "INCOMMING";
	
	private static CallLogEntry callLogEntry = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		handleRecive(context, intent);
	}

	private void handleRecive(Context context, Intent intent) {
		
		Bundle extras = intent.getExtras();
		if (null == extras)	return;
		String incomminghPhoneNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
		if( (null == incomminghPhoneNumber ) ||  (incomminghPhoneNumber.length() == 0) )
		{
		 // not an incomming call;	
		 return;	
		}
		
		String state = extras.getString(TelephonyManager.EXTRA_STATE);	
        if(null == state)
        {
         Log.w(TAG, "state is null");
         return;
        }
        Log.d(TAG, "In BroadcastReciever in call handleRecive()");
		if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {			
			callLogEntry = PhoneEventUtil.createStartCallData(incomminghPhoneNumber,TYPE);	
			return;
		} else if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)) {			
			return;
		} else if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)) {
			if(null == callLogEntry)
			{
				return;
			}
			PhoneEventUtil.createEndCallData(callLogEntry);			
			PhoneEventUtil.save(context, callLogEntry);						
		}
		return;
	}



	


}