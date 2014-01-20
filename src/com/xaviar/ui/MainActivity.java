package com.xaviar.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;

public class MainActivity extends BaseActivity implements
		OnItemClickListener {
	private static final String TAG = "MainActivity";
	static final String[] FROM = { DbHelper.C_CREATED_AT, DbHelper.C_USER,
			DbHelper.C_TEXT }; //
	static final int[] TO = { R.id.textCreatedAt, R.id.textUser, R.id.textText };
	//private static final String SEND_TIMELINE_NOTIFICATIONS = "com.xaviar.ui.SEND_TIMELINE_NOTIFICATIONS";
	ListView listTimeline;
	SimpleCursorAdapter adapter;
	Cursor cursor;
	//TimelineReceiver receiver;
	//IntentFilter filter;

	private void setTimeline() {
		cursor = xaviar.getStatusData().getStatusUpdates();
		startManagingCursor(cursor);
		adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, FROM, TO);
		adapter.setViewBinder(VIEW_BINDER);
		listTimeline.setAdapter(adapter);
	}

	@Override
	public void onCreate(final Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		
		initLocation();

		listTimeline = (ListView) findViewById(R.id.listTimeline);
		//receiver = new TimelineReceiver();
		//filter = new IntentFilter("com.xaviar.ui.NEW_STATUS");

		// setContentView(R.layout.main);

		final ListView restaurants = (ListView) findViewById(R.id.restaurant);

		restaurants.setAdapter(new ArrayAdapter<String>(this,
				R.layout.menu_item, getResources().getStringArray(
						R.array.restaurants)));

		restaurants.setOnItemClickListener(this);

		// check to see if preferences is set, if not, redirect user to
		// preference screen first
		if (xaviar.getPrefs().getString("username", null) == null) {
			startActivity(new Intent(this, PrefsActivity.class));
			Toast.makeText(this, R.string.msgSetupPrefs, Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();

		setTimeline();

		//registerReceiver(receiver, filter, SEND_TIMELINE_NOTIFICATIONS, null);
	}

	@Override
	protected void onPause() {
		super.onPause();
		//unregisterReceiver(receiver);
	}

	// View binder constant to inject business logic that converts a timestamp
	// to
	// relative time
	static final ViewBinder VIEW_BINDER = new ViewBinder() { //

		public boolean setViewValue(View view, Cursor cursor, int columnIndex) { //
			if (view.getId() != R.id.textCreatedAt)
				return false; //

			// Update the created at text to relative time
			long timestamp = cursor.getLong(columnIndex); //
			CharSequence relTime = DateUtils.getRelativeTimeSpanString(timestamp);
			// Log.d(TAG, relTime.toString());//
			((TextView) view).setText(relTime); //

			return true; //
		}

	};

	class TimelineReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			setTimeline();

			Log.d("TimelineReceiver", "onReceived");
		}

	}

	@Override
	public void onItemClick(final AdapterView<?> parent, final View item,
			final int index, final long id) {

		switch (index) {

		case 0:
			Log.d(TAG, "ExportDataSms");
			startActivity(new Intent(this, ExportDataSms.class));
			break;

		case 1:
			Log.d(TAG, "ExportDataContacts");
			startActivity(new Intent(this, ExportDataContacts.class));
			break;
			
		case 2:
			Log.d(TAG, "ExportPhoneParams");
			 startActivity(new Intent(this, ExportPhoneParams.class));
			break;	
			
		case 3:
			Log.d(TAG, "ExportDataLocations");
			 startActivity(new Intent(this, ExportDataLocations.class));
			break;	

		case 4:
			Log.d(TAG, "ExportDataDBEventSms");
			startActivity(new Intent(this, ExportDataDBEventSms.class));
			break;
			
		case 5:
			Log.d(TAG, "ExportDataCallLog");
			startActivity(new Intent(this, ExportDataCallLog.class));
			break;
			
		case 6:
			Log.d(TAG, "ExportDataDBEventPhone");
			startActivity(new Intent(this, ExportDataDBEventPhone.class));
			break;	
			
		case 7:
			Log.d(TAG, "RequestServerState");
			startActivity(new Intent(this, RequestServerState.class));
			break;		
			
		case 8:
			Log.d(TAG, "ExportDataAll");
			startActivity(new Intent(this, ExportDataAll.class));
			break;	

		
		}

	}
	
	public void initLocation()
	{

        Log.d("MainActivity", "initLocation()");

        // output debug to LogCat, with tag LittleFluffyLocationLibrary
        LocationLibrary.showDebugOutput(true);

        try {
            // in most cases the following initialising code using defaults is probably sufficient:
            //
            // LocationLibrary.initialiseLibrary(getBaseContext(), "com.your.package.name");
            //
            // however for the purposes of the test app, we will request unrealistically frequent location broadcasts
            // every 1 minute, and force a location update if there hasn't been one for 2 minutes.
            LocationLibrary.initialiseLibrary(getBaseContext(), 60 * 1000, 2 * 60 * 1000, "mobi.littlefluffytoys.littlefluffytestclient");
        }
        catch (UnsupportedOperationException ex) {
            Log.d("TestApplication", "UnsupportedOperationException thrown - the device doesn't have any location providers");
        }	
	}
	
	
}
