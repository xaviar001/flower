package com.xaviar.frm;

import java.util.Properties;

import android.content.Context;

public interface ICollector {
	
	public static final int SLEEP_TIME = 1000;
		
	
	public static final int TIMEOUT_JOIN = 30000;
	
	public void collect();
	
	public void setContext(Context ctx);
	
	public Properties getResult();
	
	public void setTime(String time);
	
	public String getTime();
	
	public String getHolderKey();
	
		
	

}
