package com.gammon.qs.client.ui.util;

import java.math.BigDecimal;

/**
 * @author peterchan 
 * 
 */
public class RoundingUtil {
	/**
	 * Rounding function for Client using <code>BigDecimal</code> with gwt-Math lib
	 * @author peterchan 
	 */
	public static double round(double num, int dp){
//		return Math.round(num * Math.pow(10, dp))/Math.pow(10, dp);
		return new BigDecimal(Math.round(new BigDecimal(num+"").multiply(new BigDecimal("10").pow(dp)).doubleValue())+"").doubleValue()/new BigDecimal("10").pow(dp).doubleValue();	
	}
	
	/**
	 * Convert the string that with , to Double
	 * eg "10,000" -> 10000.00#
	 * @author peterchan 
	 * Date: Jun 8, 2011
	 * @param stringDouble
	 * @return
	 */
	public static Double stringToDouble(String stringDouble){
		if (stringDouble==null)
			return null;
		return Double.valueOf(stringDouble.replaceAll(",", ""));
	}
}
