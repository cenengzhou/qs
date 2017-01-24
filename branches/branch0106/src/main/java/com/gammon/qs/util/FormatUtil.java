package com.gammon.qs.util;

import java.text.DecimalFormat;

import com.gammon.qs.shared.util.CalculationUtil;

public class FormatUtil {
	
	public static String formatString(String str){
		return formatString(str,"");
	}

	public static String formatString(String str, String defaultValue){
		if(str == null)
			return defaultValue;
		else
			return str;
	}

	
	public static String formatString(Integer number, String defaultValue){
		if(number == null)
			return defaultValue;
		if(defaultValue == null)
			return "0";
		else if(number instanceof Integer){
			return String.valueOf(number);
		}
		else
			return defaultValue;
	}
	
	public static String formatString(Integer number){
		return formatString(number, "0");
	}
	
	public static String doubleToString(Double doubleValue, int scale, String format){
		DecimalFormat decimalFormat = new DecimalFormat(format);
		return decimalFormat.format(CalculationUtil.round(doubleValue, scale));
	}

}
