package com.Appium.core.helpers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import org.json.simple.JSONObject;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.Appium.core.GlobalVariables;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

public class DriverFactory extends GlobalVariables {
	DesiredCapabilities capabilities = new DesiredCapabilities();

	public void setDriver(JSONObject deviceCapability) {
		switch ((String) deviceCapability.get("deviceOS")) {
		case "ios":
			setIosDriver(deviceCapability);
			break;
		case "android":
			setAndroidDriver(deviceCapability);
			break;
		default:
			break;
		}
	}

	private void setIosDriver(JSONObject deviceCapability) {
		AppiumDriver<MobileElement> iosDriver = null;
		String appPath = properties.getProperty("iosAppPath");
		int wdaLocalPort = new Random().nextInt((8200 - 8100) + 1) + 8100;
		threadFactory.setOS("ios");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceCapability.get("deviceName"));
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, deviceCapability.get("deviceOSVersion"));
		capabilities.setCapability(MobileCapabilityType.UDID, deviceCapability.get("deviceUDID"));
		if (System.getProperty("debug") != null && System.getProperty("debug").equalsIgnoreCase("true")) {
			capabilities.setCapability(MobileCapabilityType.APP, appPath);
			capabilities.setCapability(MobileCapabilityType.NO_RESET, false);
			capabilities.setCapability(MobileCapabilityType.FULL_RESET, true);
		} else {
			capabilities.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.apple.mobilecal");
			capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
			capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
		}
		// ADDITIONAL CAPABILITY
		capabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, wdaLocalPort);
		capabilities.setCapability(MobileCapabilityType.CLEAR_SYSTEM_FILES, true);
		capabilities.setCapability(IOSMobileCapabilityType.USE_NEW_WDA, true);
		capabilities.setCapability(IOSMobileCapabilityType.USE_PREBUILT_WDA, false);
		capabilities.setCapability(IOSMobileCapabilityType.SHOULD_USE_SINGLETON_TESTMANAGER, false);
		capabilities.setCapability(IOSMobileCapabilityType.RESET_ON_SESSION_START_ONLY, true);
		capabilities.setCapability(IOSMobileCapabilityType.WAIT_FOR_APP_SCRIPT, false);
		capabilities.setCapability(IOSMobileCapabilityType.SHOW_XCODE_LOG, true);
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 300);
		capabilities.setCapability(MobileCapabilityType.ORIENTATION, ScreenOrientation.PORTRAIT);
		try {
			iosDriver = new IOSDriver<>(new URL((String) deviceCapability.get("serverURL")), capabilities);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error initiating ios driver " + e);
		}
		threadFactory.setDriver(iosDriver);
	}

	private void setAndroidDriver(JSONObject deviceCapability) {
		AppiumDriver<MobileElement> androidDriver = null;
		String appPath = properties.getProperty("androidAppPath");
		int systemPort = new Random().nextInt((8200 - 8100) + 1) + 8100;
		threadFactory.setOS("android");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
		if (Integer.valueOf(((String) deviceCapability.get("deviceOSVersion")).substring(0)) < 6)
			capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium");
		else
			capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceCapability.get("deviceName"));
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, deviceCapability.get("deviceOSVersion"));
		capabilities.setCapability(MobileCapabilityType.UDID, deviceCapability.get("deviceUDID"));
		if (System.getProperty("debug") != null && System.getProperty("debug").equalsIgnoreCase("true")) {
			capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
					"com.android.contacts.activities.PeopleActivity");
			capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "com.android.contacts");
			capabilities.setCapability(MobileCapabilityType.NO_RESET, false);
			capabilities.setCapability(MobileCapabilityType.FULL_RESET, true);
		} else {
			capabilities.setCapability(MobileCapabilityType.APP, appPath);
			capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
			capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
		}
		// ADDITIONAL CAPABILITY
		capabilities.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, systemPort);
		capabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS,
				properties.getProperty("autoGrantPermission", "true"));
		capabilities.setCapability(AndroidMobileCapabilityType.UNICODE_KEYBOARD, true);
		capabilities.setCapability(AndroidMobileCapabilityType.RESET_KEYBOARD, true);
		capabilities.setCapability(MobileCapabilityType.CLEAR_SYSTEM_FILES, true);
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 300);
		capabilities.setCapability(MobileCapabilityType.ORIENTATION, ScreenOrientation.PORTRAIT);
		try {
			androidDriver = new AndroidDriver<>(new URL((String) deviceCapability.get("serverURL")), capabilities);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error initiating ios driver " + e);
		}
		threadFactory.setDriver(androidDriver);
	}
}
