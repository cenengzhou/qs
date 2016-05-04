package com.gammon.qs.util;

import java.math.BigDecimal;
/**
 * RoundingUtil 
 * This Class handle all double customize calculation, e.g. Rounding, Double Multiple 
 * @author peterchan
 *
 */
public class RoundingUtil {
	/**
	 * Round the double amount to specific dp
	 * @param num a <code>double</code> value to be rounded to a <code>double</code> with specific decimal point <code>dp</code>
	 * @param dp specific decimal point to <code>num</code> 
	 * @return round(a,dp)
	 */
	public static double round(double num, int dp){

		//return Math.round(num * Math.pow(10, dp))/Math.pow(10, dp);
		return BigDecimal.valueOf(Math.round(BigDecimal.valueOf(num).multiply(BigDecimal.valueOf(10).pow(dp)).doubleValue())).divide(BigDecimal.valueOf(10).pow(dp)).doubleValue();
	}
	
	/**
	 * Handle the rounding problem of double * double
	 * Example:
	 *   3193.45*10.9=34808.605 however system give out 34808.604999999996 
	 *   
	 * @author peterchan 
	 * Date: May 19, 2011
	 * @param a
	 * @param b
	 * @return a*b
	 */
	public static double multiple(double a, double b){
		return BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b)).doubleValue();
	}
}
