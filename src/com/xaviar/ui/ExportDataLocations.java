package com.xaviar.ui;

import java.util.List;
import java.util.Properties;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.xaviar.collect.CollectData;
import com.xaviar.collect.device_accounts.AccountEntry;
import com.xaviar.collect.device_accounts.CollectDeviceAccount;


public class ExportDataLocations extends Activity {

	private static final String TAG = "ExportDataLocations";
	public boolean displayLayout = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"Enter ExportDataLocations onCreate()");	
		
		CollectData collData = new CollectData(this);
		//collData.registerLocations();
		//collData.registerDeviceAccount();
		//collData.registerCalendar();
		collData.registerPacakges();
		collData.collect();

		Properties prop = collData.getResult();
		String msg = prop.toString();
		Log.d(TAG,msg);
		if(displayLayout) {	
		setContentView(R.layout.exportdatalocations);
		}
		// setContentView(new BitmapView(this));
	}
	
	
	

	

}
