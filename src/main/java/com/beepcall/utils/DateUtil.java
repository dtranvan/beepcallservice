/*
 * devjav [http://devjav.com]
 * Copyright (C) 2014-2014 Pham Thai Thinh
 * Contact:phamthaithinh@gmail.com
 * 
 */
package com.beepcall.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {	

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DateUtil.class);

	public static String datetoString(Date date) {
		if (date == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_NORMAL);
		return sdf.format(date);
	}

	public static String datetoString(Date date, String format) {
		if (date == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static Date stringToDate(String str, String format) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(str);
		} catch (ParseException ex) {
			return null;
		}

		return date;
	}

	public static long subDate(Date d1, Date d2) {
		long diff = Math.abs(d1.getTime() - d2.getTime());
		long diffDays = diff / (24 * 60 * 60 * 1000);

		return diffDays;
	}

	public static Date minuteToDate(Date date, int minute) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MINUTE, minute);
			return cal.getTime();
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}
		return null;
	}
	
	public static Date secondToDate(Date date, int second) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.SECOND, second);
			return cal.getTime();
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}
		return null;
	}
	
	public static Date addDayToDate(Date date, int day) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, day);
			return cal.getTime();
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}
		return null;
	}
	
	public static long diffTime(Date d1, Date d2) {
		long diff = Math.abs(d1.getTime() - d2.getTime());
		long diffMinute = diff / (60 * 1000);

		return diffMinute;
	}
	
	public static long getCreationTime(String fileName) throws IOException {
		Path p = Paths.get(fileName);
		BasicFileAttributes view = Files.getFileAttributeView(p,
				BasicFileAttributeView.class).readAttributes();
		
		return view.creationTime().toMillis();
	}
	
	
	public static boolean check(String fileName, Date toDate, int count) {
		
		long creationTime = 0;
		try {
			creationTime = DateUtil.getCreationTime(fileName);
		} catch (IOException e) {
			LOGGER.error("IOException", e);
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(toDate);
		cal.add(Calendar.DATE, count);		
		
		return cal.getTimeInMillis() - creationTime > 0;
	}
	
}
