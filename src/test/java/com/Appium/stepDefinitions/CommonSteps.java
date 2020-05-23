package com.Appium.stepDefinitions;

import java.io.File;
import java.lang.reflect.Constructor;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;

import com.Appium.core.BaseFunctions;
import com.Appium.core.helpers.ThreadFactory;
import com.Appium.core.utils.DateUtil;
import com.vimalselvam.cucumber.listener.Reporter;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;
import io.cucumber.java.*;
import io.cucumber.java.en.*;

public class CommonSteps extends BaseFunctions {

	String ExeStartTime;
	String ExeEndTime;
	private String text;

	@Before
	public void setup(Scenario scenario) {
		ExeStartTime = dateUtil.getTimeStamp("");
		threadFactory.setFeatureName(scenario.getId().split(";")[0].split("-")[0]);
		System.out.println("===============================================================");
		System.out.println("Starting scenario : " + scenario.getName());

		setupApp(scenario);

		String capabilities = "";
		capabilities = capabilities + "<p>Device&nbsp;Name&nbsp;:&nbsp;"
				+ threadFactory.getCapability().get("DeviceName") + "<p>";
		capabilities = capabilities + "<p>OS&nbsp;:&nbsp;" + threadFactory.getCapability().get("DeviceOS") + "<p>";
		capabilities = capabilities + "<p>OS&nbsp;version&nbsp;:&nbsp;"
				+ threadFactory.getCapability().get("DeviceOsVersion") + "<p>";
		capabilities = capabilities + "<p>uid&nbsp;:&nbsp;" + threadFactory.getCapability().get("DeviceUdid") + "<p>";
		capabilities = capabilities + "<p>appium&nbsp;server&nbsp;:&nbsp;"
				+ threadFactory.getCapability().get("DeviceGridUrl") + "<p>";
		capabilities = capabilities + "<p>server&nbsp;:&nbsp;" + threadFactory.getCapability().get("DeviceServerId")
				+ "<p>";
		Reporter.assignAuthor("Running&nbsp;&nbsp;with&nbsp;&nbsp;configuration&nbsp;&nbsp;:" + capabilities);
		Reporter.loadXMLConfig(new File(baseDir + "/src/test/resources/configs/extent-config.xml"));
		Reporter.setSystemInfo("user", System.getProperty("user.name"));
		Reporter.setSystemInfo("os", System.getProperty("os.name"));
		Reporter.setSystemInfo("Mobile OS", (String) threadFactory.getCapability().get("DeviceOS"));
		Reporter.setTestRunnerOutput(""); // Logs displayed from here
	}

	@After
	public void cleanup(Scenario scenario) {
		try {
			Reporter.addScreenCaptureFromPath(getScreenshot(scenario.getName()));
		} catch (Exception e) {
			System.err.println("ERROR IN GETTING FAILED SCREENSHOT :" + e.getMessage());
			Reporter.addStepLog("ERROR IN GETTING FAILED SCREENSHOT :" + e.getMessage());
		}

		ExeEndTime = dateUtil.getTimeStamp("");

		System.out.println("App closed!!");
		System.out.println("End of scenario : " + scenario.getName());
		System.out.println("===============================================================");
		switch (System.getProperty("projectName")) {
		case "BVEApp":
			// tearDownBVEapp();
			break;
		default:
			threadFactory.getDriver().closeApp();
		}
	}

	private void setupApp(Scenario scenario) {
		String featureName = "Feature ";
		String rawFeatureName = scenario.getId().split(";")[0].replace("-", " ");
		featureName = featureName + rawFeatureName.substring(0, 1).toUpperCase() + rawFeatureName.substring(1);
		featureName = featureName.toLowerCase();
		threadFactory.setSavedData("featureName", featureName);
		if (System.getProperty("debug") != null && System.getProperty("debug").equals("true")) {
			HashMap<String, Object> args = new HashMap<>();
			System.out.println("Attempting to Switch back to App ");
			args.put("bundleId", "<my.app.id>");
			threadFactory.getDriver().executeScript("mobile: launchApp", args);
		} else {
			System.out.println("Attempting to Switch back to App ");
			threadFactory.getDriver().activateApp("<my.app.id>");
		}
	}

	private void tearDownBVEapp() {

		if (System.getProperty("debugging") != null && System.getProperty("debugging").equals("true")) {
		} else {
			threadFactory.getDriver().resetApp();
		}
	}

	public MobileElement getElement(String elementName) {
		String featureName = threadFactory.getFeatureName().substring(0, 1).toUpperCase()
				+ threadFactory.getFeatureName().substring(1);
		Class<?> clazz;
		try {
			clazz = Class.forName("objectRepository.BVEApp.BVEApp_" + featureName + "PageObjects");
			@SuppressWarnings("unchecked")
			Constructor<AppiumDriver<MobileElement>> constructor = (Constructor<AppiumDriver<MobileElement>>) clazz
					.getConstructor(AppiumDriver.class);
			Object instance = constructor.newInstance(threadFactory.getDriver());
			return (MobileElement) instance.getClass().getMethod("getElement", String.class).invoke(instance,
					elementName);
		} catch (Exception e) {
			// Reporter.addStepLog("UNABLE TO GET ELEMENT :" + e.getMessage());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<MobileElement> getElements(String elementName) {
		String featureName = threadFactory.getFeatureName().substring(0, 1).toUpperCase()
				+ threadFactory.getFeatureName().substring(1);
		Class<?> clazz;
		try {
			clazz = Class.forName("objectRepository.BVEApp.BVEApp_" + featureName + "PageObjects");
			Constructor<AppiumDriver<MobileElement>> constructor = (Constructor<AppiumDriver<MobileElement>>) clazz
					.getConstructor(AppiumDriver.class);
			Object instance = constructor.newInstance(threadFactory.getDriver());
			return (List<MobileElement>) instance.getClass().getMethod("getElements", String.class).invoke(instance,
					elementName);
		} catch (Exception e) {
			Reporter.addStepLog("UNABLE TO GET ELEMENT :" + e.getMessage());
		}
		return null;
	}

	@And("^I should click on device back key$")
	public void click_back_key() {
		if (threadFactory.getOS().equals("android")) {
			clickAndroidBackbutton();
		}
	}

	@And("^\"([^\"]*)\" should be enabled$")
	public void isEnabled(String elementName) {
		boolean enabled = false;
		if (threadFactory.getOS().equals("android")) {
			enabled = getAttribute(getElement(elementName), "enabled").equals("true");
		} else {
			enabled = getAttribute(getElement(elementName), "enabled").equals("true");
		}
		if (enabled)
			report.reportPass(elementName + " is enabled as expected");
		else
			report.reportFail(elementName + " is NOT enabled");
	}

	@And("^\"([^\"]*)\" should be disabled$")
	public void isDisabled(String elementName) {
		boolean disabled = false;
		if (threadFactory.getOS().equals("android")) {
			disabled = getAttribute(getElement(elementName), "enabled").equals("true");
		} else {
			disabled = getAttribute(getElement(elementName), "enabled").equals("true");
		}
		if (disabled)
			report.reportPass(elementName + " is disabled as expected");
		else
			report.reportFail(elementName + " is NOT disabled");
	}

	@And("^I clear \"([^\"]*)\" field$")
	public void clear_text_field(String elementName) {
		String[] eles = elementName.split(",");
		boolean cleared = false;
		for (String ele : eles) {
			if (clearTextBox(getElement(ele))) {
				cleared = true;
			}
			if (cleared)
				report.reportPass(elementName + " is cleared Correctly");
			else
				report.reportFail(elementName + "is not cleared");
		}
	}

	@Given("^I scroll up till \"([^\"]*)\" is visible$")
	public void i_scroll_up_till_element_visible(String elementName) {
		scrollUpTillElementVisible(getElement(elementName));
	}

	@Then("^\"([^\"]*)\" should not be visible$")
	public void element_not_visible(String elementName) {
		if (isDissapeared(getElement(elementName)))
			report.reportPass(elementName + " is NOT displayed");
		else
			report.reportFail(elementName + " should NOT be displayed");
	}

	@When("^If I click elsewhere on screen$")
	public void click_on_screen() {
		report.addStepLogToReport("tap on screen");
		Dimension size = threadFactory.getDriver().manage().window().getSize();
		int y = size.height - 50;
		int x = size.width - 10;
		TouchAction action = new TouchAction(threadFactory.getDriver());
		action.tap(PointOption.point(x, y)).perform();
	}

	@Given("^I scroll down till \"([^\"]*)\" is visible$")
	public void i_scroll_down_till_element_visible(String elementName) throws InterruptedException {
		if (getElement(elementName) != null) {
			scrollDownTillElementVisible(getElement(elementName));
			System.out.println();
		} else {
			boolean found = false;
			long startTime = System.currentTimeMillis(), endTime = 0;
			while (!found && (endTime - startTime < 30000)) {
				try {
					found = isDisplayed(getElements(elementName).get(0));
					if (found)
						break;
					else {
						scrollPage(0.5f, 0.4f, "down");
						endTime = System.currentTimeMillis();
					}
				} catch (Exception e1) {
					scrollPage(0.5f, 0.4f, "down");
					Thread.sleep(2000);
					endTime = System.currentTimeMillis();
				}
			}
		}
	}

	@Given("^I scroll down till \"([^\"]*)\" is present$")
	public void i_scroll_down_till_element_present(String elementName) throws InterruptedException {
		if (getElement(elementName) != null) {
			scrollDownTillElementVisible2(getElement(elementName));
		}
	}

	@Given("^I scroll down till \"([^\"]*)\" is enabled$")
	public void i_scroll_down_till_element_enable(String elementName) throws InterruptedException {
		if (getElement(elementName) != null) {
			scrollDownTillElementEnabled(getElement(elementName));
		} else {
			boolean found = false;
			long startTime = System.currentTimeMillis(), endTime = 0;
			while (!found && (endTime - startTime < 180000)) {
				try {
					found = scrollDownTillElementEnabled(getElements(elementName).get(0));
					if (found)
						break;
					else {
						scrollPage(0.9f, 0.2f, "down");
						endTime = System.currentTimeMillis();
					}
				} catch (Exception e1) {
					scrollPage(0.9f, 0.2f, "down");
					Thread.sleep(2000);
					endTime = System.currentTimeMillis();
				}
			}
		}
	}

	@And("^I enter \"([^\"]*)\" in \"([^\"]*)\"$")
	public void i_enter_text_in_element(String text, String elementName) {
		clearTextBox(getElement(elementName));
		typeIntoMobileElement(getElement(elementName), text);
	}

	@Then("^I should see \"([^\"]*)\"(?: button| text| dropDown| checkBox| textArea| radioButton|)$")
	public void i_should_see(String elementName) {
		String[] eles = elementName.split(",");
		threadFactory.getDriver().getSessionId();
		boolean found = false;
		for (String ele : eles) {
			if (isDisplayed(getElement(ele))) {
				found = true;
				/////// getElementScreenShot(getElement(ele), ele);
			} else {
				try {
					report.addStepLogToReport("\nRegular isdisplayed method failing so Trying with imageElement now..");
					isDisplayedImage(ele);
				} catch (Exception e) {
					report.addStepLogToReport("Try failed with imageElement ..");
					report.reportFail("\n-->" + ele + "<-- is NOT displayed ");
				}
			}
		}
		if (found)
			report.reportPass(elementName + " is displayed Correctly");
	}

	@Then("^I should see \"([^\"]*)\"(?:th|st|nd|rd) \"([^\"]*)\"(?: button| text| dropDown| checkBox| textArea| radioButton|)$")
	public void i_should_see(int index, String elementName) {
		index = index - 1;
		String[] eles = elementName.split(",");
		boolean found = false;
		for (String ele : eles) {
			if (isElementPresent((getElements(ele).get(index))))
				found = true;
			else {
				report.reportFail(ele + " is NOT displayed");
				report.addStepLogToReport(ele + " is NOT displayed");
			}
		}
		if (found)
			report.reportPass(elementName + " should be displayed");
	}

	@When("^I click on \"([^\"]*)\"$")
	public void i_click_on_element(String elementName) {
	}

	@When("^I click on \"([^\"]*)\" for ios$")
	public void i_click_on_element_only_for_ios(String elementName) {
		if (threadFactory.getOS().equalsIgnoreCase("ios")) {
			i_click_on_element(elementName);
		} else {
		}
	}

	@Then("^I click on Android back button$")
	public void i_click_on_Android_back_button() {
		if (threadFactory.getOS().equalsIgnoreCase("android")) {
			threadFactory.getDriver().navigate().back();
		} else {
		}
	}

	@And("^I wait for \"([^\"]*)\" seconds$")
	public void i_pause_for(String sec) {
		iPauseExecution(Integer.parseInt(sec));
	}

	@When("^I click on \"([^\"]*)\"(?:th|st|nd|rd) \"([^\"]*)\"$")
	public void i_click_on_element(int index, String elementName) {
		clickMobileElementByIndex(getElements(elementName), index - 1);
		report.reportPass("Clicked on " + index + "positon");
	}

	@When("^I click on \"([^\"]*)\"(?:th|st|nd|rd) \"([^\"]*)\" with explicit wait of \"([^\"]*)\"$")
	public void i_click_on_element_with_wait(int index, String elementName, int customWaitTime) {
		clickMobileElementByIndex(getElements(elementName), index - 1);
		report.reportPass("Clicked on " + index + "positon");
	}

	@When("^I should save the text of \"([^\"]*)\"(?:th|st|nd|rd) from the list \"([^\"]*)\" with the key \"([^\"]*)\"$")
	public void i_save_text_from_list(int index, String elementName, String key) {
		String text = getTextOfElementByIndex(getElements(elementName), --index);
		threadFactory.setSavedData(key, text);
		report.reportPass("Got the text -> " + text + " from " + index + "positon and saved in key->" + key);
		System.out.println();
	}

	@Then("^I should see text present in key \"([^\"]*)\" contains in element \"([^\"]*)\"$")
	public void i_should_see_text_present_in_key_in_element_contains_in(String key, String elementName) {
		String text = threadFactory.getSavedData(key);
		MobileElement ele = getElement(elementName);
		boolean isContaing = ele.getText().contains(text);
		if (isContaing)
			report.reportPass("Text ->" + text + " present in key ->" + key + " is Containg in the current screen");
		else
			report.reportFail("Text ->" + text + " present in key ->" + key + " is NOT Containg in the current screen");
	}

	@Then("^I should see text present in key \"([^\"]*)\" in element \"([^\"]*)\"$")
	public void i_should_see_text_present_in_key_in_element(String key, String elementName) {
		String text = threadFactory.getSavedData(key);
		MobileElement ele = getElement(elementName);
		boolean isTextMatching = ele.getText().trim().equals(text);
		if (isTextMatching)
			report.reportPass("Text ->" + text + " present in key ->" + key + " is matching in the current screen");
		else
			report.reportFail("Text ->" + text + " present in key ->" + key + " is NOT matching in the current screen");
	}

	@When("^I click on \"([^\"]*)\"(?:th|st|nd|rd) \"([^\"]*)\" for \"([^\"]*)\"$")
	public void i_click_on_element(String index, String elementName, String screen) {
		int index2 = Integer.parseInt(index);
		switch (screen) {
		case "aditionalPhones":
			index2 = (index2 * 2) - 2;
			clickMobileElementByIndex(getElements(elementName), index2 - 1);
			break;
		default:
			System.err.println("Case not matched!!");
		}
		clickMobileElementByIndex(getElements(elementName), index2 - 1);
	} //

	@When("^I click on \"([^\"]*)\"(?:th|st|nd|rd) \"([^\"]*)\" present$")
	public void i_click_on_element2(int index, String elementName) {
		clickMobileElementByIndex(getElements(elementName), index - 1);
	}

	@When("^I get text of \"([^\"]*)\"(?:th|st|nd|rd) \"([^\"]*)\"$")
	public void i_get_text_element(int index, String elementName) {
		text = getTextOfElementByIndex(getElements(elementName), index - 1);
	}

	@Then("^I should be able to scroll down the page$")
	public void i_should_scroll_down_page() {
		scrollPage(0.9f, 0.1f, "down");
	}

	@Then("^I should be able to scroll up the page$")
	public void i_should_scroll_up_page() {
		scrollPage(0.4f, 0.9f, "up");
	}

	@Then("^I should be able to scroll up the page \"([^\"]*)\" times$")
	public void i_should_scroll_up_page(int n) {
		for (int i = 0; i < n; i++) {
			scrollPage(0.4f, 0.9f, "up");
		}
	}

	@Then("^Verify \"([^\"]*)\" is selected$")
	public void Verify_is_selected(String elementName) {
		if (getAttribute(getElement(elementName), "selected").equals("true"))
			report.reportPass(elementName + " should be selected");
		else
			report.reportFail(elementName + " is NOT selected");
	}

	@When("^I long press \"([^\"]*)\" for \"([^\"]*)\" seconds$")
	public void long_press_element(String elementName, int seconds) {
		longPress(getElement(elementName), seconds);
	}

	@When("^I hide keyboard$")
	public void hide_keyboard() {
		hideKeyboard();
		hideKeyboard();
		hideKeyboard();
	}

	@When("^I hide keyboard if displayed$")
	public void hide_keyboard_if_displayed() {
		hideKeyboard();
	}

	@And("^I should see saved text present in \"([^\"]*)\" key in \"([^\"]*)\"$")
	public void i_verify_saved_text_present_in_this_element(String key, String elementName) {
		String toFind = threadFactory.getSavedData(key);
		report.addStepLogToReport(
				"Trying to verify the saved key data which is -> " + toFind + " in the current screen");
		if (getTextOfElement2(getElement(elementName)).equals(toFind)
				|| getTextOfElement2(getElement(elementName)).matches(toFind)) {
			report.addStepLogToReport("Text is displayed as expected ->" + getTextOfElement2(getElement(elementName)));
		} else {
			report.reportFail(text + "text is NOT displayed as expected");
		}
	}

	@And("^I should see value \"([^\"]*)\" in \"([^\"]*)\"$")
	public void i_verify_value_in_element(String text, String elementName) {
		if (getAttribute(getElement(elementName), "value").matches(text))
			report.reportPass(text + " text is displayed as expected");
		else
			report.reportFail(text + " text is NOT displayed as expected");
	}

	@When("^I close the app$")
	public void close_app() {
		closeTheApp();
	}

	@When("^I launch the app$")
	public void launch_app() {
		launchTheApp();
	}

	@When("^I re-launch app from background$")
	public void relaunch_app_background() {
		threadFactory.getDriver().runAppInBackground(Duration.ofSeconds(4));
	}

	@When("^I re-launch app$")
	public void relaunch_app() {
		threadFactory.getDriver().closeApp();
		threadFactory.getDriver().launchApp();
	}

	@And("^I go to top of the list$")
	public void i_go_to_top_of_voicemailList() {
		goToTopOfList();
	}

	@Then("^I should \"([^\"]*)\" manually$")
	public void verify_manually(String message) {
		report.addStepLogToReport(message);
	}

	@Then("^I will close the app and Launch the app$")
	public void closeAndOpen() {
		report.addStepLogToReport("Starting to close the app and launch the app..");
		terminateAndLaunchTheApp();
		report.addStepLogToReport("Closing the app and launching the app DONE..");
	}

	@Then("^I should get the info of \"([^\"]*)\"(?:st|nd|rd|th) \"([^\"]*)\" and save it as \"([^\"]*)\" key$")
	public void get_info_and_set(int i, String elementList, String nameForKey) {
		try {

			isElementPresent(getElements(elementList).get(0), 5);
			String elemText = getElements(elementList).get(0).getText();
			threadFactory.setSavedData(nameForKey, elemText);
			report.addStepLogToReport("Saved value is ->" + elemText + " with key -> " + nameForKey);
		} catch (Exception e) {
			System.err.println("Unable to get the data to set it to key: " + e.getMessage());
			report.reportFail("Unable to get the data to set it to key!!" + e.getMessage());
		}

	}

	@Then("^I should not see the \"([^\"]*)\" in the list$")
	public void i_should_not_see_the_in_the_list(String notSeeThis) {
		goToTopOfList();
		int secondsToRun = 40;
		long t = System.currentTimeMillis();
		long end = t + TimeUnit.SECONDS.toMillis(secondsToRun);
		while (System.currentTimeMillis() < end) {
			if (threadFactory.getOS().equalsIgnoreCase("ios")) {
				if (!isDissapearedIosElement(
						"**/XCUIElementTypeStaticText[`name='" + threadFactory.getSavedData(notSeeThis) + "'`]")) {
					report.addStepLogToReport(
							"This ->" + threadFactory.getSavedData(notSeeThis) + " is still visible in the list.");
					report.reportFail(
							"This ->" + threadFactory.getSavedData(notSeeThis) + " is still visible in the list.");

				}
			} else {
				List list = listtoText(getElements("FavoriteCellsNamesList"));
				if (list.contains(threadFactory.getSavedData(notSeeThis))) {
					System.out.println();
					report.addStepLogToReport(
							"This ->" + threadFactory.getSavedData(notSeeThis) + " is still visible in the list.");
					report.reportFail(
							"This ->" + threadFactory.getSavedData(notSeeThis) + " is still visible in the list.");

				}

			}
			scrollPage(0.8f, 0.6f, "up");
			report.addStepLogToReport("Verified the list for invisibility for -> " + secondsToRun);
		}
	}

	@Then("^I should drag \"([^\"]*)\"st element to \"([^\"]*)\"nd element in \"([^\"]*)\" list$")
	public void i_should_drag_st_element_to_nd_element_in_list(int arg1, int arg2, String eles) {
		if (threadFactory.getOS().equalsIgnoreCase("ios")) {
			String locator = getLocatorTypeAndLocator(getElements(eles)).split(";")[1];
			String elem1Loc = locator + "[" + arg1 + "]";
			String elem2Loc = locator + "[" + arg2 + "]";
			MobileElement startElement = toIosClassChainElement(elem1Loc);
			MobileElement endElement = toIosClassChainElement(elem2Loc);
			dragElement(startElement, endElement);
			System.out.println();
		}

		else {
			arg2++;
			List<MobileElement> listOfElements = getElements(eles);
			MobileElement startElement = listOfElements.get(arg1);
			MobileElement endElement = listOfElements.get(arg2);
			dragElement(startElement, endElement);
		}

	}

	public void dragElement(MobileElement startELement, MobileElement endElement) {

		int startX = startELement.getLocation().getX() + (startELement.getSize().getWidth() / 2);
		int startY = startELement.getLocation().getY() + (startELement.getSize().getHeight() / 2);

		int endX = endElement.getLocation().getX() + (endElement.getSize().getWidth() / 2);
		int endY = endElement.getLocation().getY() + (endElement.getSize().getHeight() / 2);
	}

	@Then("^I should save the text present in element \"([^\"]*)\" with key \"([^\"]*)\"$")
	public void i_should_save_the_text_present_in_element_with_key(String ele, String key) {

		threadFactory.setSavedData(key, getElement(ele).getText().split("\n")[0]);
		report.addStepLogToReport("Data ->" + getElement(ele).getText() + " saved in key->" + key);
	}

	@Then("^I should see text present in key \"([^\"]*)\" in \"([^\"]*)\"$")
	public void i_should_see_text_present_in_key(String key, String ele) {
		String textToFind = threadFactory.getSavedData(key).trim();
		textToFind = textToFind.replaceAll("\n", " ");
		if (threadFactory.getOS().equalsIgnoreCase("ios")) {
			iPauseExecution(3);
			String actualTest = getTextOfElement2(getElement(ele)).replaceAll("\n|,", " ");
			actualTest = actualTest.replaceAll(" ", " ");

			if (!actualTest.toLowerCase().trim().contains(textToFind.toLowerCase().trim()))
				report.reportFail("textToFind -> " + textToFind + " is NOT found in the screen");
		}

		else {
			if (!isDisplayed(findElementUsingXpath("//*[@text='" + textToFind + "' or @value='" + textToFind + "']")))
				report.reportFail("textToFind -> " + textToFind + " is NOT found in the screen");

		}

	}

	@Then("^I switch to \"([^\"]*)\" app$")
	public void i_switch_to_app(String bundleIdArg) {
		if (threadFactory.getOS().equalsIgnoreCase("ios")) {
			try {
				HashMap<String, Object> args = new HashMap<String, Object>();
				args.put("bundleId", bundleIdArg);
				threadFactory.getDriver().executeScript("mobile: launchApp", args);
			} catch (Exception e) {
				e.printStackTrace();
				report.addStepLogToReport("Switching to app ->" + bundleIdArg + " Failed");
				report.reportFail("Switching to app ->" + bundleIdArg + " Failed");
			}
		} else {
			try {
				threadFactory.getDriver().activateApp(bundleIdArg);
			} catch (Exception e) {
				e.printStackTrace();
				report.addStepLogToReport("Switching to app ->" + bundleIdArg + " Failed");
				report.reportFail("Switching to app ->" + bundleIdArg + " Failed");
			} // properties.getProperty("androidAppPackageName"); //
				// properties.getProperty("androidAppActivityName");

		}

	}

	@Then("^I should not see text present in key \"([^\"]*)\"$")
	public void i_should_not_see_text_present_in_key(String key) {
		String text = threadFactory.getSavedData(key);

		if (threadFactory.getOS().equalsIgnoreCase("ios")) {
			if (isDissapeared(toIosClassChainEle("**/*[`name='" + text + "'`]"))) {
				report.reportPass("text ->" + text + " is not visible in the screen");
			} else {
				report.reportFail("text ->" + text + " is still visible in the screen");
			}
		} else {
			if (isDissapeared("//*[@text ='" + text + "']")) {
				report.reportPass("text ->" + text + " is not visible in the screen");
			} else {
				report.reportFail("text ->" + text + " is still visible in the screen");
			}
		}

	}

}
