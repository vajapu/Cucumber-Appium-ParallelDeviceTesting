package com.Appium.runner;

import org.json.simple.JSONObject;

import com.Appium.core.GlobalVariables;
import com.Appium.core.helpers.CapabilityFactory;
import com.Appium.core.helpers.ConfigFactory;
import com.Appium.core.helpers.DriverFactory;
import com.Appium.core.utils.DateUtil;

import io.cucumber.core.cli.Main;

public class ParallelRunner extends GlobalVariables implements Runnable {
	JSONObject deviceCapability;
	static String timeStamp;
	
	public ParallelRunner() {
	}

	public ParallelRunner(JSONObject device) {
		this.deviceCapability = device;
	}

	public static void main(String args[]) {
		ConfigFactory.loadConfig(baseDir + "/src/test/resources/configs/config.properties");
		if (System.getProperty("buildNumber") == null) {
			timeStamp = DateUtil.getTimeStamp("MM-dd-yy HH.mm.ss");
		} else {
			timeStamp = System.getProperty("buildNumber");
		}
		devices = CapabilityFactory.getDeviceCapabilities(baseDir + "/src/test/resources/configs/devices.json");
		if (devices.size() == 0)
			throw new RuntimeException("Device capabilities not defined in configS");
		for (int i = 0; i < devices.size(); i++) {
			Thread thread = new Thread(new ParallelRunner((JSONObject) devices.get(i)),String.valueOf(i));
			thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
				@Override
				public void uncaughtException(Thread arg0, Throwable arg1) {
					System.out.println(String.format("Thread %s threw error %s", arg0,arg1));
					threadFactory.getDriver().quit();
				}
			});
			thread.start();
		}
	}

	public void run() {
		threadFactory.setCapability(deviceCapability);
		threadFactory.setTimeStamp(timeStamp);
		new DriverFactory().setDriver(deviceCapability);
		String[] argv = new String[] {
			"--plugin","pretty","-m",
			"--plugin","html:target/cucumber-reports/cucumber-pretty",
			"--plugin","json:target/cucumber-reports/cucumberTestReport.json",
			"--plugin","com.vimalselvam.cucumber.listener.ExtentCucumberFormatter:test-output/"+threadFactory.getTimeStamp()+"report.html",
			"-g","stepDefinitions",properties.getProperty("featuresPath","/src/test/resources/features"),
			"--tags",System.getProperty("tags")
		};
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		byte exitStatus = Main.run(argv, contextClassLoader);
		threadFactory.getDriver().quit();
	}
}
