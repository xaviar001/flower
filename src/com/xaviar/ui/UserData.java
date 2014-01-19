package com.xaviar.ui;

import java.util.UUID;

import android.util.Log;

public class UserData {
	
	private static final String TAG = UserData.class.getSimpleName();
	
	public String createdAt;
	public String text;
	private String user;
	public String id;
	public String source;
	
	public String userName;
	
	public UserData()
	{
	 id =  UUID.randomUUID().toString();
	 Log.d(TAG, "UUID:id" + id);
	 long time = System.currentTimeMillis();
	 this.setCreatedAt(String.valueOf(time));
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	

}
