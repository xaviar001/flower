package com.xaviar.collect.sms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import flexjson.JSONSerializer;

public class SmsUtil {
	public static List<Sms> getAllSms(Context ctx) {
	    List<Sms> lstSms = new ArrayList<Sms>();
	    Sms objSms = new Sms();
	    Uri message = Uri.parse("content://sms/");
	    ContentResolver cr = ctx.getContentResolver();

	    Cursor c = cr.query(message, null, null, null, null);
	    int totalSMS = c.getCount();

	    if (c.moveToFirst()) {
	        for (int i = 0; i < totalSMS; i++) {

	            objSms = new Sms();
	            String smsID = c.getString(c.getColumnIndexOrThrow("_id"));
	            objSms.setId(smsID);
	            objSms.setAddress(c.getString(c
	                    .getColumnIndexOrThrow("address")));
	            objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
	            objSms.setReadState(c.getString(c.getColumnIndex("read")));
	            objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
	            if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
	                objSms.setFolderName("inbox");
	            } else {
	                objSms.setFolderName("sent");
	            }

	            lstSms.add(objSms);
	            c.moveToNext();
	        }
	    }
	    // else {
	    // throw new RuntimeException("You have no SMS");
	    // }
	    c.close();

	    return lstSms;
	}

	public static String toJsonArray(Collection<Sms> collection) {
		   return new JSONSerializer().exclude("*.class").serialize(collection);
		}


}
