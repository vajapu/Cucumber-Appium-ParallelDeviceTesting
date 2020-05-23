package com.Appium.core;

import java.util.Properties;
import org.json.simple.JSONArray;

import com.Appium.core.helpers.Report;
import com.Appium.core.helpers.ThreadFactory;
import com.Appium.core.utils.DateUtil;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public abstract class GlobalVariables {
	public static String baseDir = System.getProperty("user.dir").replaceAll("\\\\", "/");
	public static Properties properties;
	public static JSONArray devices;
	public static DateUtil dateUtil = new DateUtil();
	public static ThreadFactory threadFactory = new ThreadFactory();
	public static Report report = new Report();
}
