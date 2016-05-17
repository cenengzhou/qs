package com.gammon.qs.shared.util;

import java.math.BigDecimal;

/**
 * koeyyeung
 * Jan 10, 2014 12:03:41 PM
 */
public class CalculationUtil {
	
	public static double round(Double doubleValue, int scale){
		if(doubleValue!=null){
			//modified by Tiky Wong on 2016-02-05
			//To resolve double looses precision issue
			return new BigDecimal(doubleValue.toString()).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		else
			return 0.0;
	}
	
	public static Double stringToDouble(String string){
		if (string==null)
			return null;
		return Double.valueOf(string.replaceAll(",", ""));
	}
	
}