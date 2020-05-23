package com.Appium.core.helpers;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.json.simple.JSONObject;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class ThreadFactory {
	private static ThreadLocal<AppiumDriver<MobileElement>> DRIVER = new ThreadLocal<>();
	private static ThreadLocal<JSONObject> CAPABILITY = new ThreadLocal<>();
	private static ThreadLocal<String> TIMESTAMP = new ThreadLocal<>();
	private static ThreadLocal<String> OS = new ThreadLocal<>();
	private static ThreadLocal<String> FEATURE_NAME = new ThreadLocal<>();
	private static ThreadLocal<Map<String,String>> SAVED_DATA = new ThreadLocal<>();
	private static Map<String,String> data=new HashedMap();
	private static ThreadLocal<List<String>> LIST_DATA = new ThreadLocal<>();

	public void setDriver(AppiumDriver<MobileElement> driver) {
		DRIVER.set(driver);
	}

	public AppiumDriver<MobileElement> getDriver() {
		return DRIVER.get();
	}

	public void setCapability(JSONObject deviceCapability) {
		CAPABILITY.set(deviceCapability);
	}

	public JSONObject getCapability() {
		return CAPABILITY.get();
	}
	
	public void setTimeStamp(String timeStamp) {
		TIMESTAMP.set(timeStamp);
	}

	public String getTimeStamp() {
		return TIMESTAMP.get();
	}
	
	public void setOS(String os) {
		OS.set(os);
	}

	public String getOS() {
		return OS.get();
	}
	
	public void setFeatureName(String featureName) {
		FEATURE_NAME.set(featureName);
	}

	public String getFeatureName() {
		return FEATURE_NAME.get();
	}
	
	public String getSavedData(String key) {
		return SAVED_DATA.get().get(key);
	}

	public void setSavedData(String key,String value) {
		data.put(key, value);
		SAVED_DATA.set(data);
	}
	
	public List<String> getListData() {
		return LIST_DATA.get();
	}

	public void setListData(List<String> data) {
		LIST_DATA.set(data);
	}
}
