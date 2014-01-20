package com.xaviar.frm;

import java.util.Properties;

import android.content.Context;

import com.xaviar.utils.NetworkUtil;

public abstract class ThreadCollector implements ICollector {
	protected String name = "ThreadCollect";
	
	protected String genericData = null;
	protected Context ctx;
	protected String time;

	Runnable thread = new Runnable() {
		public void run() {
			System.out.println(name + ": Get all data.");
			genericData = getAllData();
			String holderKey = getHolderKey();
			processData(genericData,holderKey);
		}
	};

	public String getGenericType() {
		return this.genericData;
	}

	public void setContext(Context ctx) {
		this.ctx = ctx;
	}

	public void collect() {
		System.out.println(name + ":  start collect()");
		Thread th = new Thread(thread);
		th.setName(getHolderKey());
		th.start();
		try {
			th.join(TIMEOUT_JOIN);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(name + ": (4) end collect() ************");
	}

	
	
	public  DataHolder processData(String genericData, String holderKey) {

		DataHolder dhReturn = NetworkUtil.processData(genericData, holderKey,ctx);
		return dhReturn;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public Properties getResult() {
		if ((null == genericData) || (genericData.length() == 0)) {
			return null;
		}

		Properties prop = new Properties();
		prop.put(name, genericData);
		return prop;
	}

	protected abstract String getAllData();
	
	
	
	@Override
	public String  getTime() {
		return time;
	}
	
	@Override
	public void setTime(String time)
	{
	  this.time = time;	
	}
		
	
}
