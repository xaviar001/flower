package com.xaviar.collect.contacts;

import android.provider.ContactsContract.CommonDataKinds.Phone;

public class ContactEntry {
	//private String id;
	private String name;
	private String number;
	private String type;
	private String image;
	private String id;

	public ContactEntry(String name,String id, String number, int typeEnum, String imageStr) {
		this.name = name;
		this.number = number;		
		//this.id = number + name+ number + type;
		this.setImage(imageStr);
		String numType = "";
		switch (typeEnum) {
		case Phone.TYPE_HOME:
			numType = "HOME";
			break;
		case Phone.TYPE_MOBILE:
			numType = "MOBILE";
			break;
		case Phone.TYPE_WORK:
			numType = "WORK";
			break;
		default:
			numType = "MOBILE";
			break;
		}
		this.type = numType;
	}

	public String getId() {
	return id;
	}

	public void setId(String id) {
	this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getNumber() {
		return number;
	}

	public String getType() {
		return type;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
