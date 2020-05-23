package com.Appium.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private static Date date = new Date();

	public static String getTimeStamp(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
}
