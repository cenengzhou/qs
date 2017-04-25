package com.gammon.qs.shared.util;

import java.math.BigDecimal;

/**
 * koeyyeung
 * Jan 10, 2014 12:03:41 PM
 */
public class CalculationUtil {
	
	public static double round(Double doubleValue, int scale){
		if(doubleValue!=null && !Double.isNaN(doubleValue)){
			//modified by Tiky Wong on 2016-02-05
			//To resolve double looses precision issue
			return new BigDecimal(doubleValue.toString()).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		else
			return 0.0;
	}
	
	public static BigDecimal roundToBigDecimal(BigDecimal bigDecimalValue, int scale){
		if(bigDecimalValue!=null){
			return new BigDecimal(bigDecimalValue.toString()).setScale(scale, BigDecimal.ROUND_HALF_UP);
		}
		else
			return new BigDecimal(0.0);
	}

	public static BigDecimal roundToBigDecimal(Double doubleValue, int scale){
		if(doubleValue!=null && !Double.isNaN(doubleValue)){
			return new BigDecimal(doubleValue).setScale(scale, BigDecimal.ROUND_HALF_UP);
		}
		else
			return new BigDecimal(0.0);
	}

}
