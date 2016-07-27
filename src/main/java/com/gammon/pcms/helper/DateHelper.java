package com.gammon.pcms.helper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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

	public static String formatDate(Date date) {
		return formatDate(date, GlobalParameter.DATE_FORMAT);
	}

	public static String formatDate(Date date, String dateFormat) {
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
		return sdf.format(date).toUpperCase();
	}

	public static Calendar timestampToCalendar(Timestamp timestamp) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(timestamp.getTime());
		return calendar;
	}

	public static boolean isStartDateAfterEndDate(String startDate, String endDate) {
		Date sDate = parseDate(startDate);
		Date eDate = parseDate(endDate);
		Calendar cStartDate = Calendar.getInstance();
		Calendar cEndDate = Calendar.getInstance();
		cStartDate.setTime(sDate);
		cEndDate.setTime(eDate);

		if (cStartDate.after(cEndDate)) {
			return true;
		}

		return false;
	}

	public static boolean isStartDateAfterOrEqualEndDate(String startDate, String endDate) {
		Date sDate = parseDate(startDate);
		Date eDate = parseDate(endDate);
		Calendar cStartDate = Calendar.getInstance();
		Calendar cEndDate = Calendar.getInstance();
		cStartDate.setTime(sDate);
		cEndDate.setTime(eDate);

		if (cStartDate.after(cEndDate) || !cStartDate.after(cEndDate) && !cStartDate.before(cEndDate)) {
			return true;
		}

		return false;
	}

	public static boolean isStartDateAfterOrEqualEndDate_TimeLevel(String startDate, String endDate) {
		Date sDate = parseDate(startDate, GlobalParameter.DATETIME_FORMAT);
		Date eDate = parseDate(endDate, GlobalParameter.DATETIME_FORMAT);
		Calendar cStartDate = Calendar.getInstance();
		Calendar cEndDate = Calendar.getInstance();
		cStartDate.setTime(sDate);
		cEndDate.setTime(eDate);

		if (cStartDate.after(cEndDate) || !cStartDate.after(cEndDate) && !cStartDate.before(cEndDate)) {
			return true;
		}

		return false;
	}

	public static Date firstDateOfQuarter(Date date) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);

		Calendar result = Calendar.getInstance();
		if (calDate.get(Calendar.MONTH) < 3) {
			result.set(Calendar.MONTH, 0);
		} else if (calDate.get(Calendar.MONTH) >= 3 && calDate.get(Calendar.MONTH) <= 5) {
			result.set(Calendar.MONTH, 3);
		} else if (calDate.get(Calendar.MONTH) >= 6 && calDate.get(Calendar.MONTH) <= 8) {
			result.set(Calendar.MONTH, 6);
		} else if (calDate.get(Calendar.MONTH) >= 9 && calDate.get(Calendar.MONTH) <= 11) {
			result.set(Calendar.MONTH, 9);
		}
		result.set(Calendar.DAY_OF_MONTH, result.getActualMinimum(Calendar.DAY_OF_MONTH));
		return result.getTime();
	}

	public static Date firstDateOfHalfYear(Date date) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);

		Calendar result = Calendar.getInstance();
		if (calDate.get(Calendar.MONTH) < 6) {
			result.set(Calendar.MONTH, 0);
		} else {
			result.set(Calendar.MONTH, 6);
		}
		result.set(Calendar.DAY_OF_MONTH, result.getActualMinimum(Calendar.DAY_OF_MONTH));
		return result.getTime();
	}

	public static Date firstDateOfWeek(Date date) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);

		while (calDate.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			calDate.add(Calendar.DAY_OF_MONTH, -1);
		}

		return calDate.getTime();
	}

	public static Date firstDateOfMonth(Date date) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);

		String year = "" + calDate.get(Calendar.YEAR);
		String month = "" + (calDate.get(Calendar.MONTH) + 1);

		return parseDate("01/" + month + "/" + year, GlobalParameter.DATE_FORMAT);

	}

	public static Date firstDateOfTheYear(Date date) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);

		String year = "" + calDate.get(Calendar.YEAR);

		return parseDate("01/01/" + year, GlobalParameter.DATE_FORMAT);

	}

	public static Date lastDateOfQuarter(Date date) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);

		Calendar result = Calendar.getInstance();
		if (calDate.get(Calendar.MONTH) < 3) {
			result.set(Calendar.MONTH, 2);
		} else if (calDate.get(Calendar.MONTH) >= 3 && calDate.get(Calendar.MONTH) <= 5) {
			result.set(Calendar.MONTH, 5);
		} else if (calDate.get(Calendar.MONTH) >= 6 && calDate.get(Calendar.MONTH) <= 8) {
			result.set(Calendar.MONTH, 8);
		} else if (calDate.get(Calendar.MONTH) >= 9 && calDate.get(Calendar.MONTH) <= 11) {
			result.set(Calendar.MONTH, 11);
		}
		result.set(Calendar.DAY_OF_MONTH, result.getActualMaximum(Calendar.DAY_OF_MONTH));
		return result.getTime();
	}

	public static Date lastDateOfHalfYear(Date date) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);

		Calendar result = Calendar.getInstance();
		if (calDate.get(Calendar.MONTH) < 6) {
			result.set(Calendar.MONTH, 5);
		} else {
			result.set(Calendar.MONTH, 11);
		}
		result.set(Calendar.DAY_OF_MONTH, result.getActualMaximum(Calendar.DAY_OF_MONTH));
		return result.getTime();
	}

	public static Date lastDateOfWeek(Date date) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);

		while (calDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
			calDate.add(Calendar.DAY_OF_MONTH, 1);
		}

		return calDate.getTime();
	}

	public static Date lastDateOfTheYear(Date date) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);

		String year = "" + calDate.get(Calendar.YEAR);

		return parseDate("31/12/" + year, GlobalParameter.DATE_FORMAT);

	}

	public static Date lastDateOfMonth(Date date) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);

		calDate.add(Calendar.MONTH, 1);
		calDate.setTime(firstDateOfMonth(calDate.getTime()));
		calDate.add(Calendar.DATE, -1);

		return calDate.getTime();
	}

	public static Date getEndOfDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));

		return cal.getTime();
	}

	/**
	 * date1 > date2 return 1 date1 == date 2 return 0 date1 < date2 return -1
	 */
	public static int compare(Date date1, Date date2) {
		if (date1 == null && date2 == null)
			return 0;
		if (date1 == null)
			return 1;
		if (date2 == null)
			return -1;
		if (date1.getTime() > date2.getTime())
			return 1;
		else if (date1.getTime() == date2.getTime())
			return 0;
		else
			return -1;
	}

	public static Date truncate(Date date) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);

		calDate.set(Calendar.HOUR_OF_DAY, 0);
		calDate.set(Calendar.MINUTE, 0);
		calDate.set(Calendar.SECOND, 0);
		calDate.set(Calendar.MILLISECOND, 0);

		return calDate.getTime();

	}

	public static Date firstDateInNMonthBefore(Date date, int noOfMonthBefore) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);

		calDate.set(Calendar.DATE, 10);
		calDate.add(Calendar.MONTH, 1 - noOfMonthBefore);

		@SuppressWarnings("unused")
		String day = "" + calDate.get(Calendar.DATE);
		String month = "" + calDate.get(Calendar.MONTH);
		String year = "" + calDate.get(Calendar.YEAR);

		return parseDate("01/" + month + "/" + year, GlobalParameter.DATE_FORMAT);

	}

	public static Date lastDateInNMonthBefore(Date date, int noOfMonthBefore) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);

		calDate.set(Calendar.DATE, 10);
		calDate.add(Calendar.MONTH, 1 - noOfMonthBefore);

		String day = "" + calDate.get(Calendar.DATE);
		String month = "" + calDate.get(Calendar.MONTH);
		String year = "" + calDate.get(Calendar.YEAR);

		return lastDateOfMonth(parseDate(day + "/" + month + "/" + year, "dd/MM/yyyy"));

	}

	public static String parseDateInTwoDigit(int value) {
		String newValue = String.valueOf(value);
		if (newValue.length() == 1)
			return "0" + newValue;

		return newValue;
	}

	public static Calendar dateToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * For JDE Infoamtion Default value for Updated Time
	 */
	public static final Integer getTimeLastUpdated() {
		Integer timeLastUpdated = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 10000 + Calendar.getInstance().get(Calendar.MINUTE) * 100 + Calendar.getInstance().get(Calendar.SECOND);
		return timeLastUpdated;
	}

}
