package com.gammon.pcms.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.gammon.qs.shared.GlobalParameter;

public class DateHelper {
	/**
	 * Convert a date string into a Date object according to the date format in GlobalParameter
	 */
	public static Date parseDate(String dateString) {
		return parseDate(dateString, GlobalParameter.DATE_FORMAT);
	}

	/**
	 * Convert a date string into a Date object according to the date format input
	 */
	public static Date parseDate(String dateString, String dateFormat) {
		if (dateString == null || dateString.equals(""))
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
		try {
			return sdf.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date parseGMTDate(String dateString, String dateFormat) {
		if (dateString == null || dateString.equals(""))
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

		try {
			return sdf.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date formatAndParseGMTDate(Date date) {
		return parseGMTDate(formatDate(date), GlobalParameter.DATE_FORMAT);
	}

	public static String formatDate(Date date) {
		return formatDate(date, GlobalParameter.DATE_FORMAT);
	}

	public static String formatDate(Date date, String dateFormat) {
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
		return sdf.format(date).toUpperCase();
	}

	/**
	 * For JDE Infoamtion Default value for Updated Time
	 */
	public static final Integer getTimeLastUpdated() {
		Integer timeLastUpdated = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 10000
				+ Calendar.getInstance().get(Calendar.MINUTE) * 100 + Calendar.getInstance().get(Calendar.SECOND);
		return timeLastUpdated;
	}

	public static Calendar getPreviousMonthCalendar(int year, int zeroBasedMonth) {
		Calendar previousMonthCalendar = Calendar.getInstance();
			previousMonthCalendar.set(year, zeroBasedMonth, 1);
			previousMonthCalendar.add(Calendar.MONTH, -1);
			return previousMonthCalendar;
	}
}
