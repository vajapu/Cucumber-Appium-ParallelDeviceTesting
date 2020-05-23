package com.Appium.core;

import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.commons.exec.util.StringUtils;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.vimalselvam.cucumber.listener.Reporter;

import io.appium.java_client.HasOnScreenKeyboard;
import io.appium.java_client.HasSettings;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.Setting;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import net.coobird.thumbnailator.makers.FixedSizeThumbnailMaker;
import net.coobird.thumbnailator.resizers.DefaultResizerFactory;
import net.coobird.thumbnailator.resizers.Resizer;

public class BaseFunctions extends GlobalVariables {
	static int waitTime = 8;

	public void waitForElementDisplayed(MobileElement element) {
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
		try {
			wait.until(ExpectedConditions.visibilityOf(element));

		} catch (Exception e) {

			throw e;
		}
	}

	public void waitForElementDisplayed(MobileElement element, int waitTime) {
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
		try {
			wait.until(ExpectedConditions.visibilityOf(element));

		} catch (Exception e) {

			throw e;
		}
	}

	public void iPauseExecution(int parseInt) {
		try {
			Thread.sleep(TimeUnit.SECONDS.toMillis(parseInt));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void waitForElementNotDisplayed(MobileElement element) {
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
		try {
			wait.until(ExpectedConditions.invisibilityOf(element));

		} catch (Exception e) {

			throw e;
		}
	}

	public void waitForElementNotDisplayed(MobileElement element, int waitTime) {
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
		try {
			wait.until(ExpectedConditions.visibilityOf(element));

		} catch (Exception e) {

			throw e;
		}
	}

	public void waitForElementToBeClickable(MobileElement element) {
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element));

		} catch (Exception e) {

			throw e;
		}
	}

	public MobileElement findMobileElement(MobileBy locator) {
		return ((MobileElement) threadFactory.getDriver().findElement(locator));
	}

	public void clickMobileElement(MobileElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);

			wait.until(ExpectedConditions.visibilityOf(element)).click();

			report.addStepLogToReport("Click action performed on " + element.toString());
		} catch (Exception e) {
			throw e;
		}
	}

	public void sendkeys(MobileElement element, String sendKeysText) {
		try {
			WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
			wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(sendKeysText);
// report.addStepLogToReport("Click action performed on " +
// getElementName());
			report.addStepLogToReport("SendKeys action performed on " + element.toString());
		} catch (Exception e) {
			throw e;
		}
	}

	public void clickMobileElementByIndex(List<MobileElement> elements, int index) {
		try {
			WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
			wait.until(ExpectedConditions.visibilityOf(elements.get(index)));
			elements.get(index).click();
// report.reportPass("Click on " + getElementName() + ".");
			report.addStepLogToReport("Click on " + elements.toString() + ".");
		} catch (Exception e) {
// report.reportFail("Click on " + getElementName() + ".");
			report.reportFail("Click on " + elements.toString() + ".FAILED!!!");
			throw e;
		}
	}

	public void tapOnScreen(int x, int y) {
		new TouchAction(threadFactory.getDriver()).tap(PointOption.point(x, y)).perform();
	}

	public void tapOnElement(MobileElement element) {
		new TouchAction(threadFactory.getDriver())
				.tap(TapOptions.tapOptions().withElement(ElementOption.element(element)))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(250))).perform();
		System.out.println("tapped on element" + element);
	}

	public void tapOnElementWithWait(MobileElement element, int ms) {
		new TouchAction(threadFactory.getDriver())
				.tap(TapOptions.tapOptions().withElement(ElementOption.element(element)))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(ms))).release().perform();
		System.out.println("tapped on element" + element);
	}

	public void typeIntoMobileElement(MobileElement element, String typeValue) {
		try {
			element.sendKeys(typeValue);
			report.addStepLogToReport("Entered '" + typeValue + "' into " + element.toString());
		} catch (Exception e) {
			report.addStepLogToReport("Unable to enter '" + typeValue + "' into " + element.toString());
			throw e;
		}
	}

	public String getElementName() {
		String line = "";
		try {
			line = Files
					.readAllLines(Paths.get(((System.getProperty("user.dir")).replace("\\", "/")) + "/src/test/java/"
							+ ((Thread.currentThread().getStackTrace()[3].getClassName()).replace(".", "/")) + ".java"))
					.get(Thread.currentThread().getStackTrace()[3].getLineNumber() - 1);
			line = line.substring(line.indexOf('(') + 1, line.indexOf(')'));
			if (line.contains(",")) {
				line = line.substring(line.indexOf('.') + 1, line.indexOf(','));
			} else {
				line = line.substring(line.indexOf('.') + 1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}

	public void closeTheApp() {
		threadFactory.getDriver().closeApp();
	}

	public void launchTheApp() {
		threadFactory.getDriver().launchApp();
	}

	// take screenshot
	public synchronized String getScreenshot(String screenshotName) {
		String destination = null;
		String dateName = null;
		try {
			dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
			File source = ((TakesScreenshot) threadFactory.getDriver()).getScreenshotAs(OutputType.FILE);
			destination = System.getProperty("user.dir") + "/test-output/" + threadFactory.getTimeStamp()
					+ "/screenshots/" + screenshotName + dateName + ".png";
			File finalDestination = new File(destination);
			FileUtils.copyFile(source, finalDestination);
			// Returns the captured file path
			return "screenshots/" + screenshotName + dateName + ".png";
		} catch (IOException e) {
			System.out.println("ERROR IN TAKING SCREENSHOT: " + e.getMessage());
			return null;
		}
	}

	// Element present
	public boolean isElementVisible(MobileElement element) {
		int count = 0;
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
		count = wait.until(ExpectedConditions.visibilityOfAllElements(element)).size();
		if (count > 0) {

			return true;
		} else {

			return false;
		}
	}

// Element displayed
	public boolean isDisplayed(MobileElement element) {
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
		try {
			return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
		} catch (Exception e) {

			return false;
		}
	}

// Element displayed
	public boolean isDisplayed(MobileElement element, int waitTime) {
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
		try {
			return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public int isDisplayed(MobileElement... element) {
		System.err.println();
		System.err.println();
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), 2);
		int j = 0, i = 0;
		while (i < 3) {
			for (MobileElement ele : element) {
				try {
					j = j + 1;
					if (wait.until(ExpectedConditions.visibilityOf(ele)).isDisplayed())
						return j;
				} catch (Exception e) {
				}
				i++;
			}
		}
		return 0;
	}

	public boolean isDissapeared(MobileElement element) {
		try {
			Thread.sleep(1000);

			if (element.isDisplayed())
				return false;
			else
				return true;
		} catch (Exception e) {
			return true;
		}
	}

	public boolean isDissapeared(String myXpathString) {

		try {
			MobileElement element = threadFactory.getDriver().findElementByXPath(myXpathString); // try to find this
			// xpath .If not
			// found will

			// throw exception as element not found
			if (element.isDisplayed())
				return false;
			else
				return true;

		} catch (Exception e) {
			return true;
		}

	}

	// only for ios element
	public boolean isDissapearedIosElement(String element) {
		try {

			MobileElement ele = toIosClassChainElement(element);

			Thread.sleep(1000);

			if (ele.isDisplayed())
				return false;
			else
				return true;
		} catch (Exception e) {
			return true;
		}
	}

	public void verifyTextOfMobileElement(String text) {
		try {
			List<MobileElement> ele = threadFactory.getDriver().findElementsByXPath(
					"//android.widget.TextView[@resource-id='com.comcast.business.sdwan:id/alert_title']");
			System.out.println(ele.size());
		} catch (Exception e) {
			throw e;
		}
	}

	// hide keyboard
	public void hideKeyboard() {
		try {
			threadFactory.getDriver().hideKeyboard();
		} catch (WebDriverException wde) {
			System.out.println("no keyboard shown to hide!!");
		}
	}

	// isShowkeyboardShow
	public boolean isKeyboardShown() {
		try {
			boolean shoekeyboard = ((HasOnScreenKeyboard) threadFactory.getDriver()).isKeyboardShown();
			return shoekeyboard;
		} catch (Exception e) {
			return false;
		}
	}

	public void dimissAlert() {
		threadFactory.getDriver().switchTo().alert().dismiss();
	}

	public void acceptAlert() {
		threadFactory.getDriver().switchTo().alert().accept();
	}

	// ACCEPT OR DISMISS POPUP WITHOUT ANY EXCEPTIONS
	public void dimissPopUpAlert() {
		try {
			WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), 5);
			wait.until(ExpectedConditions.alertIsPresent());
			threadFactory.getDriver().switchTo().alert().dismiss();
		} catch (Exception e) {
			System.out.println("Pop up is not displayed To DISMISS!!");
		}
	}

	public void acceptPopUpAlert() {
		try {
			WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), 5);
			wait.until(ExpectedConditions.alertIsPresent());
			threadFactory.getDriver().switchTo().alert().accept();
		} catch (Exception e) {
			System.out.println("Pop up is not displayed To DISMISS!!");
		}
	}

	// ACCEPT OR DISMISS POPUP WITHOUT ANY EXCEPTIONS
	public void dimissPopUpAlert(int waitTime) {
		try {
			WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
			wait.until(ExpectedConditions.alertIsPresent());
			threadFactory.getDriver().switchTo().alert().dismiss();
		} catch (Exception e) {
			System.out.println("Pop up is not displayed To DISMISS!!");
		}
	}

	public void acceptPopUpAlert(int waitTime) {
		try {
			WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
			wait.until(ExpectedConditions.alertIsPresent());
			threadFactory.getDriver().switchTo().alert().accept();
		} catch (Exception e) {
			System.out.println("Pop up is not displayed To DISMISS!!");
		}
	}

	// clear text box
	public boolean clearTextBox(MobileElement element) {
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
		wait.until(ExpectedConditions.visibilityOf(element)).clear();
		return true;

	}

	// Swipe Left for an element passed
	public void iShouldSwipeLeft(int parseInt, List<MobileElement> listParam) {

		MobileElement toSwipeELement = listParam.get(parseInt - 1);
		Point toSwipeElementPoint = toSwipeELement.getLocation();

		System.out.println("Swiping for element -> " + getTextOfElement2(toSwipeELement));
		Dimension screenSize = threadFactory.getDriver().manage().window().getSize();

		int startX = (int) (screenSize.getWidth() * 0.8);
		int endX = 0;

		int startY = toSwipeElementPoint.getY();
		int endY = toSwipeElementPoint.getY();

		new TouchAction(threadFactory.getDriver()).press(PointOption.point(startX, startY))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500))).moveTo(PointOption.point(endX, endY))
				.release().perform();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void iShouldSwipeLeftWithPercentage(int parseInt, List<MobileElement> listParam, MobileElement element,
			int percentage) {
		// iShouldSwipeLeft(parseInt, listParam);

		MobileElement toSwipeThisElement = listParam.get(parseInt - 1);

		int midx = toSwipeThisElement.getRect().getPoint().getX() + toSwipeThisElement.getRect().getWidth();
		int midy = toSwipeThisElement.getRect().getPoint().getY() + toSwipeThisElement.getRect().getHeight();

		int startx = midx;
		int starty = toSwipeThisElement.getRect().getPoint().getY();

		int endx = (int) (startx - (toSwipeThisElement.getRect().getWidth() * 0.4));
		int endy = toSwipeThisElement.getRect().getPoint().getY();

		new TouchAction(threadFactory.getDriver()).press(PointOption.point(startx, starty))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500))).moveTo(PointOption.point(endx, endy))
				.release().perform();

		report.addStepLogToReport("Swiping an element with " + percentage + "% from mid point of element completed.");

	}

	public void swipeHorizontally(double startPercentage, double endPercentage, double anchorPercenteage) {
		Dimension size = threadFactory.getDriver().manage().window().getSize();

		int anchorY = (int) (size.getHeight() * anchorPercenteage);
		int startPointX = (int) (size.getWidth() * startPercentage);
		int endPointX = (int) (size.getWidth() * endPercentage);

		new TouchAction(threadFactory.getDriver()).press(PointOption.point(startPointX, anchorY))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
				.moveTo(PointOption.point(endPointX, anchorY)).release().perform();
		iPauseExecution(2);

		System.out.println();
	}

	public void swipeHorizontally(double startPercentage, double endPercentage, int yCoOrdinate) {
		Dimension size = threadFactory.getDriver().manage().window().getSize();

		int anchorY = yCoOrdinate;
		int startPointX = (int) (size.getWidth() * startPercentage);
		int endPointX = (int) (size.getWidth() * endPercentage);

		new TouchAction(threadFactory.getDriver()).press(PointOption.point(startPointX, anchorY))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(2000)))
				.moveTo(PointOption.point(endPointX, anchorY))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(300))).release().perform();
		iPauseExecution(2);

		System.out.println();
	}

	public void swipeHorizontally(double startPercentage, double endPercentage, MobileElement element) {
		Dimension size = threadFactory.getDriver().manage().window().getSize();

		int anchorY = element.getLocation().getY() + 10;
		int startPointX = (int) (size.getWidth() * startPercentage);
		int endPointX = (int) (size.getWidth() * endPercentage);

		// new
		// Toucon(WaitOptions.waitOptions(Duration.ofMillis(2000))).release().perform();

		new TouchAction(threadFactory.getDriver()).press(PointOption.point(startPointX, anchorY))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(2000)))
				.moveTo(PointOption.point(endPointX, anchorY)).release().perform();
		iPauseExecution(2);

		System.out.println();
	}

	// Swipe Up to a passed co-ordinates
	public void iShouldSwipeUp(int startX, int startY, int endX, int endY) {
		new TouchAction(threadFactory.getDriver()).press(PointOption.point(startX, startY))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500))).moveTo(PointOption.point(endX, endY))
				.release().perform();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Swipe Left for all element passed
	public void iShouldSwipeLeftForAll(List<MobileElement> listParam) {
		int scrollCount = 0;
		while (listParam.size() == 6) {

			MobileElement toSwipeELement = listParam.get(1);
			Point toSwipeElementPoint = toSwipeELement.getLocation();

			System.out.println("Swiping for element -> " + getTextOfElement2(toSwipeELement));
			Dimension screenSize = threadFactory.getDriver().manage().window().getSize();

			int startX = (int) (screenSize.getWidth() * 0.8);
			int endX = 0;

			int startY = toSwipeElementPoint.getY();
			int endY = toSwipeElementPoint.getY();

			int startYcopy = toSwipeElementPoint.getY();
			int endYcopy = toSwipeElementPoint.getY();

			// int numberOfPhonesToDelete =
			// toIosClassChainElements("**/XCUIElementTypeCell[$name='Edit'$]").size();
			// loop for 50 phones to delete
			for (int j = 0; listParam.size() == 6; j++) {
				report.addStepLogToReport("Deleting " + j + " BEANYWHERE NUMBER..");
				startY = startYcopy;
				endY = endYcopy;
				for (int i = 0; i < 2; i++) {
					new TouchAction(threadFactory.getDriver()).press(PointOption.point(startX, startY))
							.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
							.moveTo(PointOption.point(endX, endY)).release().perform();
					startY = startY + 62;
					endY = endY + 62;
				}

			}

			//
			// if (size == i && scrollCount<20)
			// {
			// i=0;
			// scrollPage(0.9f, 0.4f, "down");
			// scrollCount++;
			// }

		}

	}

	// Scroll page till element is displayed
	public void scrollUpTillElementVisible(MobileElement element) {
		System.out.println("scrolling page up");
		Dimension size = threadFactory.getDriver().manage().window().getSize();
		int starty = (int) (size.height * 0.4);
		int startx = size.width / 5;
		int endy = (int) (size.height * 0.6);
		int endx = size.width / 5;
		boolean found = false;
		long startTime = System.currentTimeMillis(), endTime = 0;
		while (!found && (endTime - startTime < 60000)) {
			try {
				found = element.isDisplayed();
			} catch (Exception e) {
			}
			if (found)
				break;
			new TouchAction(threadFactory.getDriver()).press(PointOption.point(startx, starty))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500))).moveTo(PointOption.point(endx, endy))
					.release().perform();
			iPauseExecution(1);
			endTime = System.currentTimeMillis();
		}

		iPauseExecution(2);

	}

	public void scrollDownTillElementVisible(MobileElement element) {
		System.out.println("scrolling page down");
		Dimension size = threadFactory.getDriver().manage().window().getSize();
		int starty = (int) (size.height * 0.5);
		int startx = size.width / 5;
		int endy = (int) (size.height * 0.4);
		int endx = size.width / 5;
		boolean found = false;
		long startTime = System.currentTimeMillis(), endTime = 0;
		while (!found && (endTime - startTime < 60000)) {
			try {
				found = element.isDisplayed();
			} catch (Exception e) {

			}
			if (found)
				break;
			new TouchAction(threadFactory.getDriver()).press(PointOption.point(startx, starty))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500))).moveTo(PointOption.point(endx, endy))
					.release().perform();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			endTime = System.currentTimeMillis();
		}
	}

	// scroll down till single element is presence
	public void scrollDownTillElementVisible2(MobileElement element) {
		System.out.println("scrolling page down");
		Dimension size = threadFactory.getDriver().manage().window().getSize();
		int starty = (int) (size.height * 0.9);
		int startx = size.width / 5;
		int endy = (int) (size.height * 0.4);
		int endx = size.width / 5;
		boolean found = false;
		long startTime = System.currentTimeMillis(), endTime = 0;
		while (!found && (endTime - startTime < 60000)) {
			try {
				found = isElementPresent(element, 2);
			} catch (Exception e) {

			}
			if (found)
				break;
			new TouchAction(threadFactory.getDriver()).press(PointOption.point(startx, starty))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500))).moveTo(PointOption.point(endx, endy))
					.release().perform();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			endTime = System.currentTimeMillis();
		}
	}

	// scroll down till element Text is presence
	public boolean scrollDownTillElemenTextVisible(String textToFind) {
		System.out.println("scrolling page down");
		Dimension size = threadFactory.getDriver().manage().window().getSize();
		int starty = (int) (size.height * 0.9);
		int startx = size.width / 5;
		int endy = (int) (size.height * 0.6);
		int endx = size.width / 5;
		boolean found = false;
		long startTime = System.currentTimeMillis(), endTime = 0;

		while (!found && (endTime - startTime < 60000)) {
			try {
				if (threadFactory.getOS().equalsIgnoreCase("ios")) {
					found = threadFactory.getDriver().findElements(MobileBy.iOSClassChain(
							"**/XCUIElementTypeStaticText[`value MATCHES '.*" + textToFind + ".*'`][`visible=true`]"))
							.size() >= 1;

				} else {

					if (textToFind.length() > 15) {

						textToFind = textToFind.substring(0, 15);
						found = (threadFactory.getDriver()
								.findElementsByXPath("//android.widget.TextView[contains(@text,'" + textToFind + "')]"))
										.size() >= 1;

					} else {
						found = (threadFactory.getDriver()
								.findElementsByXPath("//android.widget.TextView[contains(@text,'" + textToFind + "')]"))
										.size() >= 1;
					}

				}
			} catch (Exception e) {

			}
			if (found)
				return true;

			new TouchAction(threadFactory.getDriver()).press(PointOption.point(startx, starty))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500))).moveTo(PointOption.point(endx, endy))
					.release().perform();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			endTime = System.currentTimeMillis();
		}
		return found;
	}

	public void goToTopOfList() {

		if (threadFactory.getOS().equalsIgnoreCase("android")) {
			// ANDROID
			// GO TO THE TOP FOR COUNTING
			List<MobileElement> goToTopTempPre = null;
			List<MobileElement> goToTopTempPost = null;

			List<String> pre = null, post = null;
			do {
				// pre.clear();

				goToTopTempPre = threadFactory.getDriver().findElementsByXPath(
						"//android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout//..//android.widget.TextView[1]");
				pre = goToTopTempPre.stream().map(name -> name.getText()).collect(Collectors.toList());
				scrollPage(0.4f, 0.9f, "up");

				goToTopTempPost = threadFactory.getDriver().findElementsByXPath(
						"//android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout//..//android.widget.TextView[1]");
				post = goToTopTempPost.stream().map(name -> name.getText()).collect(Collectors.toList());

			} while (!pre.equals(post));
		}

		else {// IOS

			// GO TO THE TOP FOR COUNTING
			List<String> goToTopTempPre = null;
			List<String> goToTopTempPost = null;

			List<String> pre, post;
			do {
				goToTopTempPre = threadFactory.getDriver()
						.findElements(MobileBy
								.iOSClassChain("**/XCUIElementTypeCell[`visible==true`]/XCUIElementTypeStaticText"))
						.stream().map(x -> x.getText()).collect(Collectors.toList());
				// pre = goToTopTempPre.stream().map(name ->
				// name.getText()).collect(Collectors.toList());
				scrollPage(0.4f, 0.9f, "up");
				goToTopTempPost = threadFactory.getDriver()
						.findElements(MobileBy
								.iOSClassChain("**/XCUIElementTypeCell[`visible==true`]/XCUIElementTypeStaticText"))
						.stream().map(x -> x.getText()).collect(Collectors.toList());
				// post = goToTopTempPost.stream().map(name ->
				// name.getText()).collect(Collectors.toList());
			} while (!goToTopTempPre.equals(goToTopTempPost));
			System.out.println("List equal now breaking scroll..");
		}
	}

	public boolean scrollDownTillElementEnabled(MobileElement element) {
		System.out.println("scrolling page down");
		Dimension size = threadFactory.getDriver().manage().window().getSize();
		int starty = (int) (size.height * 0.9);
		int startx = size.width / 5;
		int endy = (int) (size.height * 0.4);
		int endx = size.width / 5;
		boolean found = false;
		long startTime = System.currentTimeMillis(), endTime = 0;
		while (!found && (endTime - startTime < 60000)) {
			try {
				found = element.isEnabled();
			} catch (Exception e) {

			}
			if (found)
				break;
			new TouchAction(threadFactory.getDriver()).press(PointOption.point(startx, starty))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500))).moveTo(PointOption.point(endx, endy))
					.release().perform();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			endTime = System.currentTimeMillis();
		}
		return found;
	}

	public void scrollUpTillElementVisible(List<MobileElement> element) {
		System.out.println("scrolling page up");
		Dimension size = threadFactory.getDriver().manage().window().getSize();
		int starty = (int) (size.height * 0.4);
		int startx = size.width / 5;
		int endy = (int) (size.height * 0.9);
		int endx = size.width / 5;
		boolean found = false;
		long startTime = System.currentTimeMillis(), endTime = 0;
		while (!found && (endTime - startTime < 60000)) {
			try {
				found = element.get(0).isDisplayed();
			} catch (Exception e) {
				if (element.contains(null))
					System.out.println("element to find is null!!");
			}
			if (found)
				break;
			new TouchAction(threadFactory.getDriver()).press(PointOption.point(startx, starty))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500))).moveTo(PointOption.point(endx, endy))
					.release().perform();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			endTime = System.currentTimeMillis();
		}
	}

	public void scrollDownTillElementVisible(List<MobileElement> element) {
		System.out.println("scrolling page up");
		Dimension size = threadFactory.getDriver().manage().window().getSize();
		int starty = (int) (size.height * 0.9);
		int startx = size.width / 5;
		int endy = (int) (size.height * 0.4);
		int endx = size.width / 5;
		boolean found = false;
		long startTime = System.currentTimeMillis(), endTime = 0;
		while (!found && (endTime - startTime < 60000)) {
			try {
				found = element.get(0).isDisplayed();
			} catch (Exception e) {
			}
			if (found)
				break;
			new TouchAction(threadFactory.getDriver()).press(PointOption.point(startx, starty))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500))).moveTo(PointOption.point(endx, endy))
					.release().perform();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			endTime = System.currentTimeMillis();
		}
	}

	public void scrollPage(float startY, float endY, String direction) {
		System.out.println("scrolling page" + direction);
		Dimension size = threadFactory.getDriver().manage().window().getSize();
		int starty = (int) (size.height * startY);
		int startx = size.width / 2;
		int endy = (int) (size.height * endY);
		int endx = size.width / 2;

		new TouchAction(threadFactory.getDriver()).press(PointOption.point(startx, starty))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500))).moveTo(PointOption.point(endx, endy))
				.release().perform();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void verifyPhoneNumberFormat(String formatCheck) {
		if (checkPattern("(\\([0-9]{3}\\) [0-9]{3}-[0-9]{4})|(1 \\([0-9]{3}\\) [0-9]{3}-[0-9]{4})", formatCheck))
			report.reportPass(" number Number Format matching");
		else
			report.reportFail(" number Number Format NOT! matching");
	}

	// get exact mid loaction of the element
	private int[] getMidCoOrdinateOfElement(MobileElement element) {
		int x = element.getRect().getX() + (element.getRect().getWidth() / 2);
		int y = element.getRect().getY() + (element.getRect().getHeight() / 2);
		return new int[] { x, y };
	}

	// tap for 250ms on a cordinate
	public void tapForSometime(MobileElement element, String elementName) {

		int x = getMidCoOrdinateOfElement(element)[0];
		int y = getMidCoOrdinateOfElement(element)[1];
		new TouchAction(threadFactory.getDriver()).press(PointOption.point(x, y))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(250))).perform();
		report.addStepLogToReport("Tapped on element -> " + elementName + " for 250ms ");
	}

	public void clickbyCordinate(float x, float y) {

		Dimension size = threadFactory.getDriver().manage().window().getSize();

		int y1 = (int) (size.height * y);
		int x1 = (int) (size.width * x);
		System.out.println("clicked on " + x1 + " " + y1);
		new TouchAction(threadFactory.getDriver()).tap(PointOption.point(x1, y1)).release().perform();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Get Attribute of element
	public String getAttribute(MobileElement element, String attribute) {
		String text;
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
		try {
			text = wait.until(ExpectedConditions.visibilityOf(element)).getAttribute(attribute);
		} catch (Exception e) {
			By by = getBy(element);
			System.err.println("Visibility Failing so Trying with PresenceOfElementLocated!!!");
			text = wait.until(ExpectedConditions.presenceOfElementLocated(by)).getAttribute(attribute);
		}
		return text;
	}

	// Long press element
	public void longPress(MobileElement element, int seconds) {
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
		wait.until(ExpectedConditions.visibilityOf(element));
		new TouchAction(threadFactory.getDriver()).longPress(LongPressOptions.longPressOptions()
				.withElement(ElementOption.element(element)).withDuration(Duration.ofSeconds(seconds))).release()
				.perform();

	}

	public String getTextOfElementByIndexWithoutWaiting(List<MobileElement> elements, int index) {
		try {
			return elements.get(index).getText();
		} catch (Exception e) {
			report.reportFail("Error getting text of element " + e.getMessage());
			throw e;
		}
	}

	public String getTextOfElementByIndex(List<MobileElement> elements, int index) {
		try {
			WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
			return wait.until(ExpectedConditions.visibilityOf(elements.get(index))).getText();
		} catch (Exception e) {
			report.reportFail("Error getting text of element " + e.getMessage());
			throw e;
		}
	}

	public String getTextOfElement(MobileElement element) {

		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);

		return wait.until(ExpectedConditions.visibilityOf(element)).getText();

	}

	// Get text of element by BY......
	public String getTextOfElement2(MobileElement element) {

		String text = null;
		WebDriverWait wait;
		try {
			wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
			text = wait.until(ExpectedConditions.visibilityOf(element)).getText();
			// if (text.equals(""))
			// report.addStepLogToReport("text is empty ");
		} catch (Exception e) {

			wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
			By by = getBy(element);
			System.err.println("Visibility Failing so Trying with PresenceOfElementLocated!!!");
			text = wait.until(ExpectedConditions.presenceOfElementLocated(by)).getText();
		}

		return text;
	}

	// Get text of element2 with wait parameter by BY......
	public String getTextOfElement2(MobileElement element, int waitTime) {

		String text = null;
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
		try {
			text = wait.until(ExpectedConditions.visibilityOf(element)).getText();
		} catch (Exception e) {
			By by = getBy(element);
			System.err.println("Visibility Failing so Trying with PresenceOfElementLocated!!!");
			text = wait.until(ExpectedConditions.presenceOfElementLocated(by)).getText();
		}

		return text;
	}

	// clik on mobile element by try catch with image match
	public void clickMobileElement2(MobileElement element, String elementName) {

		try {
			WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
			try {
				wait.until(ExpectedConditions.visibilityOf(element));
				////
				getElementScreenShot(element, elementName);
				////
				wait.until(ExpectedConditions.visibilityOf(element)).click();
				report.addStepLogToReport("Click on element ->" + elementName);
			} catch (Exception e) {
				try {

					By by = getBy(element);
					System.err.println("Visibility Failing so Trying with PresenceOfElementLocated!!!");
					wait.until(ExpectedConditions.presenceOfElementLocated(by)).click();
					report.addStepLogToReport("Click on element using presenceOfElementLocated ->" + elementName);

					;
				} catch (Exception e1) {
					///
					clickByImage(elementName);
					///
					report.addStepLogToReport(
							"Something went wrong while CLICKING with presenceOfElementLocated!! for-> "
									+ element.toString());
					throw e;
				}

			}
			// report.addStepLogToReport("Click action performed on " + element.toString());
		} catch (Exception e) {
			throw e;
		}
	}

	// clik on mobile element by try catch with image match
	public void clickMobileElement2(String elementName) {
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);

		try {
			if (threadFactory.getOS().equalsIgnoreCase("ios")) {
				threadFactory.getDriver().findElement(MobileBy.iOSClassChain(
						"**/XCUIElementTypeStaticText[`value MATCHES '.*" + elementName + ".*'`][`visible=true`]"))
						.click();
				;
			} else {
				(threadFactory.getDriver()
						.findElementByXPath("//android.widget.TextView[contains(@text,'" + elementName + "')]"))
								.click();
			}
			report.reportPass("Click on element using strting text " + elementName);
		} catch (Exception e) {
			report.addStepLogToReport("Something went wrong while CLICKING with presenceOfElementLocated!! for-> "
					+ elementName.toString());
			throw e;
		}

	}

	// clik on mobile element by try catch with image match
	public void doubleClickMobileElement2(MobileElement element, String elementName) {

		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
		try {
			wait.until(ExpectedConditions.visibilityOf(element));
			////
			getElementScreenShot(element, elementName);
			////
			MobileElement ele = (MobileElement) wait.until(ExpectedConditions.visibilityOf(element));

			ele.click();
			ele.click();

			new TouchAction(threadFactory.getDriver())
					.tap(TapOptions.tapOptions().withElement(ElementOption.element(ele)))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(55)))
					.tap(TapOptions.tapOptions().withElement(ElementOption.element(ele))).perform();
			report.addStepLogToReport("DOUBLE TAPPED on element ->" + elementName);
		} catch (Exception e) {
			System.out.println();
		}

	}

	public void clickMobileElement2(MobileElement element) {

		try {
			WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
			try {
				wait.until(ExpectedConditions.visibilityOf(element)).click();
				report.addStepLogToReport("Click on element ->" + element.toString());
				////
				////
			} catch (Exception e) {
				try {

					By by = getBy(element);

					System.err.println("Visibility Failing so Trying with PresenceOfElementLocated!!!");
					wait.until(ExpectedConditions.presenceOfElementLocated(by)).click();
					report.addStepLogToReport("Clicked using presenceOfElementLocated..");
					;
				} catch (Exception e1) {
					///
					///
					report.addStepLogToReport("Something wrong while CLICKING with presenceOfElementLocated!! for-> "
							+ element.toString());
					throw e;
					// report.reportFail("Something wrong while CLICKING with
					// presenceOfElementLocated!! for -> "
					// + element.toString());
				}

			}
			report.addStepLogToReport("Click action performed on " + element.toString());
		} catch (Exception e) {
			throw e;
		}
	}

	// clik on mobile element by try catch
	public void clickMobileElement2WithoutWaiting(MobileElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
			try {
				// wait.until(ExpectedConditions.visibilityOf(element)).click();
				element.click();
			} catch (Exception e) {
				try {

					By by = getBy(element);

					System.err.println("Visiblility Failing so trying alternative Presence of element..");

					System.err.println("Visibility Failing so Trying with PresenceOfElementLocated!!!");
					// wait.until(ExpectedConditions.presenceOfElementLocated(by)).click();

					element.click();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			report.addStepLogToReport("Click action performed on " + element.toString());
		} catch (Exception e) {
			throw e;
		}
	}

	// Element present or not by getting count
	public boolean isElementCount1(MobileElement element) {
		By by = null;
		WebDriverWait wait;

		wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
		try {
			by = getBy(element);
			if (by.equals(null))
				by = (MobileBy) getBy(element);
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
		} catch (Exception e1) {
			return false;

		}

		return true;

	}

	// Element present without timeout return bool
	public boolean isElementPresent(MobileElement element) {
		By by = null;
		WebDriverWait wait;
		try {
			wait = new WebDriverWait(threadFactory.getDriver(), waitTime);

			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (Exception e) {
			wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
			try {
				by = (MobileBy) getBy(element);
				wait.until(ExpectedConditions.presenceOfElementLocated(by));
			} catch (Exception e1) {
				return false;

			}
		}

		return true;

	}

	// return on bool value
	public int returnNum(boolean check) {
		if (check)
			return 1;
		else
			return 0;

	}

	// Element present with timeout return bool
	public boolean isElementPresent(MobileElement element, int waitTime) {
		By by = null;
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
		try {
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (Exception e) {
			try {
				by = getBy(element);
				wait.until(ExpectedConditions.presenceOfElementLocated(by));
			} catch (Exception e1) {
				return false;

			}
		}

		return true;

	}

	// Element present with timeout return bool
	public boolean invisibilityOfElement(MobileElement element, int waitTime) {

		By by = getBy(element);
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
		try {

			return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
		} catch (Exception e) {
			return false;
		}

	}

	// send the mobile element and get extracted type and locator
	public String getLocatorTypeAndLocator(MobileElement elem) {
		String elementDescription = elem.toString();
		// System.out.println("elementDescription -> " + elementDescription);
		String extractedLocator = null;
		String extractedLocatorType = null;

		try {
			extractedLocator = elementDescription
					.substring(elementDescription.indexOf(":") + 1, elementDescription.lastIndexOf("}")).trim();

			extractedLocatorType = elementDescription.substring(elementDescription.indexOf("{") + 1,
					elementDescription.indexOf(":"));
		} catch (Exception e) {

			System.err.println("Trying to Find Locator in catch for list..");
			extractedLocator = elementDescription
					.substring(elementDescription.lastIndexOf(":") + 1, elementDescription.lastIndexOf("]")).trim();

			extractedLocatorType = elementDescription
					.substring(elementDescription.lastIndexOf("-") + 1, elementDescription.lastIndexOf(":")).trim();
		}

		// System.out.println("Extracted Locator Type ->" + extractedLocatorType);
		// System.out.println("Extracted Locator ->" + extractedLocator);

		return extractedLocatorType + ";" + extractedLocator;
	}

	public String getLocatorTypeAndLocator(List<MobileElement> elem) {
		String elementDescription = elem.toString();
		// System.out.println("elementDescription -> " + elementDescription);
		String extractedLocator = null;
		String extractedLocatorType = null;

		try {
			extractedLocator = elementDescription
					.substring(elementDescription.indexOf(":") + 1, elementDescription.lastIndexOf("}")).trim();

			extractedLocatorType = elementDescription.substring(elementDescription.indexOf("{") + 1,
					elementDescription.indexOf(":"));
		} catch (Exception e) {

			System.err.println("Trying to Find Locator in catch for list..");
			extractedLocator = elementDescription
					.substring(elementDescription.lastIndexOf(":") + 1, elementDescription.lastIndexOf("]") - 1).trim();

			extractedLocatorType = elementDescription
					.substring(elementDescription.lastIndexOf("-> -") + 4, elementDescription.lastIndexOf(":")).trim();
		}

		System.out.println("Extracted Locator Type ->" + extractedLocatorType);

		System.out.println("Extracted Locator ->" + extractedLocator);

		return extractedLocatorType + ";" + extractedLocator;
	}

	// send the mobile element and get "By"
	public By getBy(MobileElement elem) {
		String locatorTypeAndLocator = getLocatorTypeAndLocator(elem);
		String locatorType = locatorTypeAndLocator.split(";")[0];
		String locator = locatorTypeAndLocator.split(";")[1];

		By by = null;
		switch (locatorType) {
		case "By.IosClassChain":
			by = MobileBy.iOSClassChain(locator);
			break;

		case "ios class chain":
			by = MobileBy.iOSClassChain(locator);
			break;

		case "By.id":
			by = By.id(locator);
			break;

		case "By.xpath":
			by = By.xpath(locator);
			break;

		case "By.AccessibilityId":
			by = MobileBy.AccessibilityId(locator);
			break;

		default:
			System.err.println("Not matching any cases so exitting !!!");
			break;
		}

		return by;

	}

	// returns iosclass chain
	public MobileElement toIosClassChainElement(String locator) {
		try {
			return threadFactory.getDriver().findElement(MobileBy.iOSClassChain(locator));
		} catch (Exception e) {
			report.addStepLogToReport("Unable to find the element from the formed iosclasschain locator\n");
			report.reportFail(
					"Unable to find the element from the formed iosclasschain locator\nException ->" + e.toString());

			e.printStackTrace();
			return null;
		}

	}

	// returns iosclass chain
	public List<MobileElement> toIosClassChainElements(String locator) {
		try {
			return threadFactory.getDriver().findElements(MobileBy.iOSClassChain(locator));
		} catch (Exception e) {
			report.addStepLogToReport("Unable to find the elements from the formed iosclasschain locator\n");
			report.reportFail(
					"Unable to find the elements from the formed iosclasschain locator\nException ->" + e.toString());
			;
			e.printStackTrace();
			return null;
		}
	}

	// returns iosclass chain
	public MobileElement toIosClassChainEle(String locator) {

		return threadFactory.getDriver().findElement(MobileBy.iOSClassChain(locator));

	}

	// returns iosclass chain
	public List<MobileElement> toIosClassChainEleList(String locator) {

		return threadFactory.getDriver().findElements(MobileBy.iOSClassChain(locator));

	}

	// return mobile element by using xpath as input
	public MobileElement findElementUsingXpath(String param) {
		return threadFactory.getDriver().findElement(MobileBy.xpath(param));
	}

	// return mobile element by using Id as input
	public MobileElement findElementUsingId(String param) {
		return threadFactory.getDriver().findElement(MobileBy.id(param));
	}

	// return LIST mobile element by using xpath as input
	public List<MobileElement> findElementsUsingXpath(String locator) {

		return threadFactory.getDriver().findElements(MobileBy.xpath(locator));
	}

	public void verifyPattern(String regexinput, String type) {

		if (Pattern.matches(regexinput, type)) {
			report.addStepLogToReport(type + " is correctly matching the regex pattern -> " + regexinput);
		} else {
			report.addStepLogToReport(" " + type + " is NOT matching the regex pattern -> " + regexinput);
			report.reportFail("" + type + " is NOT matching the regex pattern -> " + regexinput);
		}
	}

	public boolean isElementPresentUsingXpath(String param) {

		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), waitTime);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(param)));
			threadFactory.getDriver().findElement(MobileBy.xpath(param));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List listtoText(List<MobileElement> list) {
		List list1 = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			list1.add(list.get(i).getText());

			System.out.println(list1);
		}
		return list1;
	}

	public boolean checkWhetherAppiumSeverIsRunning(String url) {

		URL appiumURL = null;
		HttpURLConnection connection = null;
		int appiumURLStatusCode = 0;

		if (url.contains("127.0.0.1:")) {
			return true;
		}
		try {
			// System.out.println("Into URL creation for " + url);
			appiumURL = new URL(url);
			// System.out.println("URL created");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			// System.out.println("About to open connection");
			connection = (HttpURLConnection) appiumURL.openConnection();
			// System.out.println("Connection established");
			appiumURLStatusCode = connection.getResponseCode();
			// System.out.println("Status code -> " + appiumURLStatusCode);
			connection.disconnect();
		} catch (IOException e) {
			// e.printStackTrace();
		}
		System.out.println("Status code -> " + appiumURLStatusCode);
		if (appiumURLStatusCode != 200)
			System.out.println("retrying connection to appium server...");
		return (appiumURLStatusCode == 200);
	}

	public void waitUntilAppiumServerIsReadyForExecution(String url) {
		long endTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(45);
		boolean statusVerificationFlag = false;
		System.out.println("connecting to appium server...");
		while (System.currentTimeMillis() < endTime) {
			if (checkWhetherAppiumSeverIsRunning(url)) {
				statusVerificationFlag = true;
				break;
			} else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		if (!statusVerificationFlag) {
			System.err.println("Appium server not running in " + url);
			Reporter.setTestRunnerOutput("Appium server not running in " + url);
		} else
			System.out.println("Appium server running at " + url);
	}

	public void clickonAllowInPopup() {
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), 5);
		try {
			wait.until(ExpectedConditions.visibilityOf(
					threadFactory.getDriver().findElementById("com.android.packageinstaller:id/permission_allow_button")))
					.click();
		} catch (Exception e) {
			System.out.println("permission popup is not displayed");
		}
	}

	public void clickonAllowInIosPopup() {
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), 5);
		try {
			wait.until(ExpectedConditions.visibilityOf(threadFactory.getDriver().findElementByAccessibilityId("OK")))
					.click();
		} catch (Exception e) {
			System.out.println("permission popup is not displayed");
		}
	}

	public void clickAndroidBackbutton() {
		((AndroidDriver<MobileElement>) threadFactory.getDriver()).pressKey(new KeyEvent(AndroidKey.BACK));
	}

	public void androidEndCall() {
		((AndroidDriver<MobileElement>) threadFactory.getDriver()).pressKey(new KeyEvent(AndroidKey.ENDCALL));
	}

	public void clickonDenyInPopup() {
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), 5);
		try {
			wait.until(ExpectedConditions.visibilityOf(
					threadFactory.getDriver().findElementById("com.android.packageinstaller:id/permission_deny_button")))
					.click();
		} catch (Exception e) {
			System.out.println("permission popup is not displayed");
		}
	}

	public void clickonDenyInIosPopup() {
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), 5);
		try {
			wait.until(ExpectedConditions.visibilityOf(threadFactory.getDriver().findElementById("Don’t Allow"))).click();
		} catch (Exception e) {
			System.out.println("permission popup is not displayed");
		}
	}

	public void clickonDontAllowInIosPopup() {
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), 5);
		try {
			wait.until(ExpectedConditions.visibilityOf(threadFactory.getDriver().findElementById("Don’t Allow"))).click();
		} catch (Exception e) {
			System.out.println("Alert popup is not displayed");
		}

	}

	public void clickonOKInIosPopup() {
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), 5);
		try {
			wait.until(ExpectedConditions.visibilityOf(threadFactory.getDriver().findElementById("OK"))).click();
		} catch (Exception e) {
			System.out.println("VoiceEdge would you like to access your contacts popup is not displayed");
		}

	}

	public void waitForThisElementToBeDisappear(MobileElement ele, int timeInSec) {
		WebDriverWait wait = new WebDriverWait(threadFactory.getDriver(), timeInSec);
		wait.until(ExpectedConditions.invisibilityOf(ele));

	}

	public boolean checkPattern(String regexString, MobileElement element) {

		String elementText = getTextOfElement2(element);
		System.out.println("Verifying textPattern for ->" + elementText);

		if (Pattern.compile(regexString).matcher(elementText).matches()) {
			// report.reportPass("patter is matching ->"+elementText);
			return true;
		} else {
			// report.reportFail("patter NOT matching-->"+elementText);
			return false;
		}

	}

	public void checkPatternAndReport(String regexString, MobileElement element) {

		String elementText = getTextOfElement2(element);
		System.out.println("Verifying textPattern for ->" + elementText);

		if (Pattern.compile(regexString).matcher(elementText).matches()) {
			report.reportPass("patter is matching ->" + elementText);
		} else {
			report.reportFail("patter NOT matching-->" + elementText);
		}

	}

	public boolean checkPattern(String regexString, String stringToCheck) {
		System.out.println("Verifying textPattern for String ->" + stringToCheck);
		if (Pattern.compile(regexString).matcher(stringToCheck).matches()) {
			return true;
		} else {
			return false;
		}

	}

	public void checkPatternAndReport(String regexString, String stringToCheck) {
		System.out.println("Verifying textPattern for String ->" + stringToCheck);
		if (Pattern.compile(regexString).matcher(stringToCheck).matches()) {
			report.reportPass("patter is matching ->" + stringToCheck);
		} else {
			report.reportFail("patter NOT matching-->" + stringToCheck);
		}

	}

	public boolean validateText(String text, MobileElement element) {
		String context = getTextOfElement2(element);
		System.out.println("Verifying textEqual for ->" + context);

		if (text.equals(context)) {
			return true;
			// report.reportPass("text is verified");
		} else {
			return false;
			// report.reportFail("text is NOT verified");
		}

	}

	public String[] splitElementToLabelAndPhoneNumberForText(String text) {
		String[] arr = new String[2];
		String phoneNumberOnly = text.split("Home |Mobile |Office ")[1].trim();
		String labelOnly = text.split("[0-9 \\(\\)-]{14}")[0];

		arr[0] = phoneNumberOnly;
		arr[1] = labelOnly;
		return arr;

	}

	public String getByte64EncodedString(String filePath) throws Exception {
		String encodedToString = null;
		byte[] fileContent = null;
		try {
			fileContent = FileUtils.readFileToByteArray(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (fileContent.equals(null))
			System.out.println("file path is NULL");
		else {
			encodedToString = new String(Base64.getEncoder().encodeToString(fileContent));

		}
		return encodedToString;
	}

	public void clickByImage(String imageElementName) {
		String osName = threadFactory.getOS();

		String imageElement = null;
		String dirPath = System.getProperty("user.dir") + "/src/test/resources/ElementScreenShot/";
		try {

			((HasSettings) threadFactory.getDriver()).setSetting(Setting.FIX_IMAGE_TEMPLATE_SIZE, true);
			((HasSettings) threadFactory.getDriver()).setSetting(Setting.IMAGE_MATCH_THRESHOLD, 0.2);
			String filePath = dirPath + imageElementName + "_" + osName + ".png";
			String b64Template = getByte64EncodedString(filePath);
			threadFactory.getDriver().findElementByImage(b64Template).click();
			report.addStepLogToReport("Click Succesfull for imageElement->" + imageElementName);
			report.attachImageToReport(imageElementName, "Clicked on ->");

		} catch (Exception e) {
			System.out.println("imageElement->" + imageElementName + "<-NOT FOUND!!!");

			report.addStepLogToReport("imageElement->" + imageElementName + " NOT present in the Screen");
			report.addStepLogToReport("Detailed Exception message->" + e.toString());
			report.attachImageToReport(imageElementName, "Unable to click for ->");
			report.reportFail("imageElement->" + imageElementName + "<-NOT FOUND!!! ");
			e.printStackTrace();

		}
	}

	public boolean isDisplayedImage(String imageElementName) {
		String imageElement = null;
		String osName = threadFactory.getOS();
		String dirPath = System.getProperty("user.dir") + "/src/test/resources/ElementScreenShot/";
		try {
			// ((HasSettings)
			// threadFactory.getDriver()).setSetting(Setting.FIX_IMAGE_TEMPLATE_SIZE, true);

			((HasSettings) threadFactory.getDriver()).setSetting(Setting.FIX_IMAGE_TEMPLATE_SIZE, true);
			((HasSettings) threadFactory.getDriver()).setSetting(Setting.IMAGE_MATCH_THRESHOLD, 0.9);

			String filePath = dirPath + imageElementName + "_" + osName + ".png";
			String b64Template = getByte64EncodedString(filePath);
			Boolean bool = threadFactory.getDriver().findElementByImage(b64Template).isDisplayed();
			System.out.println(" imageElement->" + imageElementName + " present in the Screen:->" + bool);
			if (bool == false) {
				report.addStepLogToReport("imageElement->" + imageElementName + " NOT present in the Screen");
				report.attachImageToReport(imageElementName, "Not Displayed for ");
				return false;
			} else {
				report.addStepLogToReport("imageElement->" + imageElementName + " present in the Screen and Displayed");
				return true;
			}
		} catch (FileNotFoundException e) {
			report.addStepLogToReport(
					"Element Not Found Modify the locators as a Result File Not found. Exception is -> "
							+ e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			report.reportFail("Element Not Displayed, Raise a Defect/Please Modify the locators-> " + imageElementName
					+ "\n As a Result Exception is for imageElement-> " + imageElementName
					+ " in method -> isDisplayedImage\nException is ->" + e.toString());

		} finally {
			report.attachImageToReport(imageElementName, "Image Visible for ");
		}
		return true;
	}

	public void getElementScreenShot(MobileElement element, String elementName) {

		isElementPresent(element, 5);

		String osName = threadFactory.getOS();

		TakesScreenshot scrShot = ((TakesScreenshot) threadFactory.getDriver());

		String dirPath = System.getProperty("user.dir") + "/src/test/resources/ElementScreenShot/";
		File fileObjectDirPath = new File(dirPath);
		String pathWithElementName = dirPath + elementName;
		String pathName = dirPath + elementName + "_" + osName + ".png";

		File destFile = new File(pathName);

		/*
		 * Check if screenshot file exists if exists then skip taking element screenshot
		 */
		if (!destFile.exists() && !destFile.isDirectory())

		{
			/*
			 * Take element screenshot
			 */

			File tempFile = null;

			File srcFile = scrShot.getScreenshotAs(OutputType.FILE);

			boolean isDirPresent = fileObjectDirPath.mkdirs();
			if (isDirPresent)
				System.out.println("Directory Created:" + fileObjectDirPath);

			try {
				tempFile = new File(dirPath + "tempImageForProcessing" + ".png");
				FileUtils.copyFile(srcFile, tempFile);

			} catch (IOException e) {
				e.printStackTrace();
			}

			BufferedImage fullimg = null;
			try {
				fullimg = ImageIO.read(tempFile);
			} catch (IOException e) {
				e.printStackTrace();
			}

			Point point = element.getLocation();
			int eleWidth = element.getSize().getWidth();
			int eleHeight = element.getSize().getHeight();

			int dWidth = 0;
			int dHeight = 0;
			if (osName.equalsIgnoreCase("android")) {
				try {
					Map<String, Object> args = new HashMap<>();
					args.put("command", "wm");
					args.put("args", "size");

					String output = (String) threadFactory.getDriver().executeScript("mobile: shell", args);
					String resolution = output.substring(output.indexOf(": ") + 2, output.indexOf("\n"));
					// String resolution = output.substring(output.indexOf(":") + 1, output.length()
					// - 1);
					dWidth = Integer.parseInt(resolution.split("x")[0].trim());
					dHeight = Integer.parseInt(resolution.split("x")[1].trim());

					System.out.println(output.substring(output.indexOf(":") + 1));
				} catch (Exception e) {
					e.printStackTrace();
					report.addStepLogToReport(
							"Unable to get the screen shot for android as the --relaxed-security flag is not set "
									+ e.toString());
				}
			} else {
				dWidth = threadFactory.getDriver().manage().window().getSize().getWidth();
				dHeight = threadFactory.getDriver().manage().window().getSize().getHeight();
			}

			Resizer resizer = DefaultResizerFactory.getInstance().getResizer(
					new java.awt.Dimension(fullimg.getWidth(), fullimg.getHeight()),
					new java.awt.Dimension(dWidth, dHeight));
			BufferedImage scaledImage = new FixedSizeThumbnailMaker(dWidth, dHeight, false, true).resizer(resizer)
					.make(fullimg);

			try {
				BufferedImage elementScreenShot = scaledImage.getSubimage(point.getX(), point.getY(), eleWidth,
						eleHeight);
				ImageIO.write(elementScreenShot, "png", destFile);

				System.out.println("Element Screenshot taken to -> " + pathName);

			} catch (RasterFormatException ignoRasterFormatException) {
				System.out.println("Ignore Exception");
			} catch (IOException e) {
				System.out.println(" Exception in getElementScreenShot method!");
				e.printStackTrace();
			}
		} else {
			// System.out.println("Element Screenshot ->" + elementName + ".png Exists in
			// ->" + pathName
			// + " so skipping taking element screenshot");
		}

	}

	public void terminateAndLaunchTheApp() {
		report.addStepLogToReport("Terminating the app and Activating the app..");
		threadFactory.getDriver().terminateApp("com.comcast.business.uc");
		threadFactory.getDriver().activateApp("com.comcast.business.uc");
		threadFactory.setSavedData("loggedInState", "loggedIn");
		report.addStepLogToReport("Terminating the app and Activating the app DONE..");
	}

	public String getRandomCharacter(int randomWordLength) {
		// GENERATE RANDOM WORDS
		StringBuilder strBuilder = new StringBuilder();
		for (int i = 1; i < randomWordLength; i++) {
			int gen = new Random().nextInt((126 - 32) + 1) + 32;
			strBuilder.append((char) gen);

		}
		return String.valueOf(strBuilder);

	}

	public void getTheListAndSaveIt(List<MobileElement> listt) {
		List<String> toPush = listt.stream().map(x -> x.getText()).collect(Collectors.toList());
		report.addStepLogToReport("Saving list -> " + toPush);
		threadFactory.setListData(toPush);
	}

	public List<String> getTheSavedList() {

		report.addStepLogToReport("Getting saved list ");
		List<String> pulledList = threadFactory.getListData();
		report.addStepLogToReport("Got saved list as -> " + pulledList);
		return pulledList;

	}

	// if keyboard is shown then only hide it
	public void hideKeyBoardIfOpen() {

		if (isKeyboardShown()) {
			hideKeyboard();
			report.addStepLogToReport("Keyboard is displayed ");
		} else {
		}

	}

}
