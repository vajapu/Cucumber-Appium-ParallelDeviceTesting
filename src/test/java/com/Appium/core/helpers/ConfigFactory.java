package com.Appium.core.helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.Appium.core.GlobalVariables;

public class ConfigFactory extends GlobalVariables {
	public static void loadConfig(String path) {
		properties = new Properties();
		try (FileInputStream file = new FileInputStream(path)) {
			properties.load(file);
			for (String key : properties.stringPropertyNames()) {
				String[] appProp = key.split(".");
				if(appProp.length > 1) {
					if (!appProp[0].toLowerCase().contains(System.getProperty("projectName").toLowerCase())) {
						properties.setProperty(appProp[1], properties.getProperty(key));
					}
					properties.remove(key);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
