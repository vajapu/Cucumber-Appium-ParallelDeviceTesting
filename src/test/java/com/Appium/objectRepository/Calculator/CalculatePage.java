package com.Appium.objectRepository.Calculator;

import java.util.List;

import org.openqa.selenium.support.PageFactory;

import com.Appium.core.GlobalVariables;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class CalculatePage extends GlobalVariables {

	public CalculatePage(AppiumDriver<MobileElement> driver) {
		PageFactory.initElements(driver, this);
	}

	public MobileElement getElement(String elementName) {
		try {
			return (MobileElement) getClass().getDeclaredField(elementName).get(this);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<MobileElement> getElements(String elementName) {
		try {
			return (List<MobileElement>) getClass().getDeclaredField(elementName).get(this);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
