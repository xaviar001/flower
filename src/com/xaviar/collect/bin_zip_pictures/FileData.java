package com.xaviar.collect.bin_zip_pictures;

public class FileData {
	String data;
	String type;
	String name;
	
	public FileData(String name, String type, String data) {
		this.name = name;
		this.type = type;
		this.data = data;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
