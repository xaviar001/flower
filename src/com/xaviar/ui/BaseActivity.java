package com.xaviar.ui;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseActivity extends Activity {
	protected XaviarApplication xaviar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		xaviar = (XaviarApplication) getApplication();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.layout.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.itemPurge:
			((XaviarApplication) getApplication()).getStatusData().delete();
			Toast.makeText(this, R.string.msgAllDataPurged, Toast.LENGTH_LONG).show();
			break;
		case R.id.menuCollectAll:
			startActivity(new Intent(this, ExportDataAll.class));
			break;	
			
		case R.id.itemTimeline:
			startActivity(new Intent(this, MainActivity.class).addFlags(
			          Intent.FLAG_ACTIVITY_SINGLE_TOP).addFlags(
			                  Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;			
		case R.id.itemPrefs:
			startActivity(new Intent(this, PrefsActivity.class));
			break;
		case R.id.itemToggleService:
			if (xaviar.isServiceRunning()) {
				stopService(new Intent(this, UpdaterService.class));
			} else {
				startService(new Intent(this, UpdaterService.class));
			}
			break;
		case R.id.itemRefresh:
			startService(new Intent(this, UpdaterIntentService.class));
			break;
		}
		return true;

	}
	
	 // Called every time menu is opened
	  @Override
	  public boolean onMenuOpened(int featureId, Menu menu) { // 
	    MenuItem toggleItem = menu.findItem(R.id.itemToggleService); // 
	    if (xaviar.isServiceRunning()) { // 
	      toggleItem.setTitle(R.string.menuStopService);
	      toggleItem.setIcon(android.R.drawable.ic_media_pause);
	    } else { // 
	      toggleItem.setTitle(R.string.menuStartService);
	      toggleItem.setIcon(android.R.drawable.ic_media_play);
	    }
	    return true;
	  }
}
