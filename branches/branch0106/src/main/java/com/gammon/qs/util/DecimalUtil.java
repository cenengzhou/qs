package com.gammon.qs.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public  class DecimalUtil{
	private DecimalFormat decimalFormat = new DecimalFormat("###,###,##0.00");
	
	public static DecimalUtil getInstance(){
		return new DecimalUtil();
	}
	
	public void setDecimalFormat(String format){
		decimalFormat = new DecimalFormat(format);
	}

	public void reSetDecimalFormat(){
		decimalFormat = new DecimalFormat("###,###,##0.00");
	}
	
	public String formatDecimal(Double number, int decimalPlaces){
		if(number !=null){
			return decimalFormat.format(number);	
		}
		String output = "0";
		for (int i=0; i<decimalPlaces; i++){
			if(i==0)
				output = output.concat(".0");
			else
				output = output.concat("0");
		}
		return output;
	}

	public static double parseDouble(String number){
		Double defaultResult = new Double(0);;
		try{
			if(number == null)
				return defaultResult;
			NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
			Number newNumber = format.parse(number.replace(",", ""));
			double d = newNumber.doubleValue();
			return d;
		}
		catch (NumberFormatException e){
			return defaultResult;
		} catch (ParseException e) {
			return defaultResult;
		}
	}

	
	
}
