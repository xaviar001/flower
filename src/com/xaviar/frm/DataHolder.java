package com.xaviar.frm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataHolder {

	private static final String TAG = DataHolder.class.getSimpleName();

	public String data;
	private int id;
	private Map<String, String> map = new HashMap<String, String>();

	public static final String CONTACTS = "CONTACTS";
	public static final String SMS = "SMS";
	public static final String PHONE_PARAMS = "PHONE_PARAMS";
	public static final String CALL_LOG = "CALL_LOG";
	public static final String SMS_EVENT = "SMS_EVENT";
	public static final String PHONE_EVENT = "PHONE_EVENT";
	public static final String LOCATION_EVENT = "LOCATION_EVENT";
	public static final String STATE = "STATE";
	public static final String GET_SERVER_WISH = "GET_SERVER_WISH";
	public static final String FLOWER = "FLOWER";
	public static final String DEVICE_ACCOUNTS = "DEVICE_ACCOUNTS";
	public static final String CALENDAR = "CALENDAR";
	public static final String PACKAGES = "PACKAGES";

	public static final String FILE_STRING = "FILE_STRING";

	public boolean validate() {
		return true;
	}

	public Map<String, String> getMap() {
		return this.map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public void put(String key, String value) {
		map.put(key, value);
	}

	public void add(HashMap<String, String> map) {
		Set<Map.Entry<String, String>> entries = map.entrySet();
		for (Map.Entry<String, String> entry : entries) {
			map.put(entry.getKey(), entry.getValue());
		}
	}

	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

	public String get(String key) {
		return map.get(key);
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

}