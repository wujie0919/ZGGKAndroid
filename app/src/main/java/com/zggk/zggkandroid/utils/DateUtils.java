package com.zggk.zggkandroid.utils;

import java.text.SimpleDateFormat;

public class DateUtils {

	public static String getDate(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(time);
		return dateString;
	}
}
