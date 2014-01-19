package com.xaviar.collect.location;

public class Location {
	
	private String  longitude; 
	private String latitude;
	private String accuracyMeters;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private String provider;
	private String id;
	
	private String  time;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getAccuracyMeters() {
		return accuracyMeters;
	}
	public void setAccuracyMeters(String accuracyMeters) {
		this.accuracyMeters = accuracyMeters;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	

}
