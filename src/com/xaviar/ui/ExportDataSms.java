package com.xaviar.ui;

import java.util.Properties;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.xaviar.collect.CollectData;

public class ExportDataSms extends Activity {

	private static final String TAG = "ExportDataSms";
	public boolean displayLayout = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"Enter ExportDataSms onCreate()");		
		CollectData coolData = new CollectData(this);
		coolData.registerSMS();
		coolData.collect();

		Properties prop = coolData.getResult();
		String msg = prop.toString();
		Log.d(TAG,msg);
		if(displayLayout) {
		setContentView(R.layout.exportdatasms);
		}
	}

	

}
