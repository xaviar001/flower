package com.xaviar.ui;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.xaviar.collect.CollectData;

public class ExportDataAll extends Activity {

	private static final String TAG = "ExportDataAll";
	public boolean displayLayout = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"Enter ExportDataAll onCreate()");		
		CollectData coolData = new CollectData(this);
	    coolData.registerAll();
		// coolData.registerFiles();
		coolData.collect();
		
		//collectFromDB();

		//Properties prop = coolData.getResult();
		//String msg = prop.toString();
		//Log.d(TAG,msg);
		 if(displayLayout) {
		   setContentView(R.layout.exportdataall);
		 }
	}

	private void collectFromDB() {
		// TODO Auto-generated method stub
		
	}

	

}
