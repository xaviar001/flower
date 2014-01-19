package com.xaviar.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.xaviar.collect.bin_zip_pictures.FileData;
import com.xaviar.utils.BinUtil;
import com.xaviar.utils.NetworkUtil;

public class RequestServerState extends Activity {

	private static final String TAG = "RequestServerState";
	public boolean displayLayout = false;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Enter RequestServerState onCreate()");
		//DataHolder dh = ServerFsm.getServerWish(this);
		//ServerFsm.handleServerWish(dh,this);				
		//if(displayLayout) {
		FileData fileData = null;
		
		// boolean isNotworkAvailable = NetworkUtil.isNetworkAvailable(this);
		// boolean isServerAvailable = NetworkUtil.isURLReachable(this);
		 
		// if(isNotworkAvailable && isServerAvailable)
		 {
			startService(new Intent(this, UpdaterIntentService.class));	
			fileData = BinUtil.getFileDataOK(this);
		 }
		// else
		 {
			// fileData = BinUtil.getFileDataError(this);	 
		 }
		 
		
		 setContentView(new BitmapView(this,fileData));
		//}
	}

}
