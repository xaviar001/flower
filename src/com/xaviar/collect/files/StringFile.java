package com.xaviar.collect.files;

public class StringFile {

	public String getName() {
		return name;
	}


	public String getValue() {
		return value;
	}


	public String getPath() {
		return path;
	}


	private String name;
	private String value;
	private String path;
	
	
	public void setName(String name) {
		this.name = name;
		
	}


	public void setValue(String value) {
		this.value = value;
		
	}


	public void setPath(String path) {
		this.path = path;
		
	}

}
