package com.Appium.core.helpers;

import java.io.IOException;

import org.junit.Assert;

import com.Appium.core.BaseFunctions;
import com.vimalselvam.cucumber.listener.Reporter;

public class Report extends BaseFunctions {

	public synchronized void reportPass(String message) {
		takeScreenshotAndAddToReport(message);
		addStepLogToReport(message);
		Assert.assertTrue(message, true);

	}

	public synchronized void reportFail(String message) {
		takeScreenshotAndAddToReport(message);
		addStepLogToReport(message);
		Assert.assertFalse(message, true);
	}

	public synchronized void addStepLogToReport(String message) {
		Reporter.addStepLog(message);
		System.out.println(message);

	}

	public synchronized void takeScreenshotAndAddToReport(String screenShotName) {
		try {

			if (screenShotName.length() > 30)
				screenShotName = screenShotName.substring(0, 30);

			String imgPath = getScreenshot(screenShotName);
			if (imgPath != null)
				Reporter.addScreenCaptureFromPath(imgPath);
		} catch (IOException e) {
			addStepLogToReport("ERROR GETTING SCREENSHOT :" + e.getMessage());
		}
	}

	public synchronized void attachImageToReport(String screenShotName, String actionMessage) {
		String imageElementName = null;
		try {

			if (screenShotName.length() > 30)
				screenShotName = screenShotName.substring(0, 30);
			imageElementName = screenShotName + "_" + threadFactory.getOS() + ".png";
			String imgPath = System.getProperty("user.dir") + "/src/test/resources/ElementScreenShot/"
					+ imageElementName;

			if (imgPath != null)
				Reporter.addScreenCaptureFromPath(imgPath);
			addStepLogToReport(actionMessage + " imageElement -->" + imageElementName);
		} catch (IOException e) {
			addStepLogToReport("ERROR GETTING imageElement -->" + imageElementName + e.getMessage());
		}
	}
}