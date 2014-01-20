package com.xaviar.ui;

import java.util.Properties;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.xaviar.collect.CollectData;

public class ExportDataDBEventPhone extends Activity {

	private static final String TAG = "ExportDataDBEventPhone";
	public boolean displayLayout = false;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Enter ExportDataDBEventPhone onCreate()");
		CollectData coolData = new CollectData(this);
		coolData.registerPhoneEvent();
		coolData.collect();

		Properties prop = coolData.getResult();
		String msg = prop.toString();
		Log.d(TAG, msg);
		if(displayLayout) {
		 setContentView(R.layout.exportdatadbeventphone);
		}
	}

}
