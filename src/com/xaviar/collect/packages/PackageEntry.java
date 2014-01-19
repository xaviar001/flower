package com.xaviar.collect.packages;

public class PackageEntry {
	private String appname = "";
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	private String pname = "";
	private String versionName = "";
	private String versionCode ="";
	String icon;
	// private void prettyPrint() {
	// Log.v(appname + "\t" + pname + "\t" + versionName + "\t" + versionCode);
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
}