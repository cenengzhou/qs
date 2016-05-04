package com.gammon.qs.shared.util;

/**
 * created by Matthew, 2015-01-21
 */
public class DateUtil {

	/**
	 * validate integer input by regex,
	 * the input must range from 1 to 12.
	 * @param input the input string
	 * @param isOptional true for optional input, false for mandatory input
	 * @return true if the input is in valid format as an integer of month, false otherwise
	 */
	public static boolean validateMonthInput(String input, boolean isOptional) {
		//optional shoule be placed at last to perform format checking first for non-empty optional input
		return (input == null || input.isEmpty() || input.matches("^(0?[1-9]|1[012])$")) && isOptional;
	}

	/**
	 * validate integer input by regex,
	 * the input must be a 4-digit number.
	 * @param input the input number as a string
	 * @param isOptional true for optional input, false for mandatory input
	 * @return true if the input is in valid format as an integer of month, false otherwise
	 */
	public static boolean validateYearInput(String input, boolean isOptional) {
		//optional shoule be placed at last to perform format checking first for non-empty optional input
		return (input == null || input.isEmpty() || input.matches("^\\d{4}$")) && isOptional;
	}
}
