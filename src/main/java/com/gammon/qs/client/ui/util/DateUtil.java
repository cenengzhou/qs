package com.gammon.qs.client.ui.util;


import java.util.Date;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.gammon.qs.shared.GlobalParameter;

public class DateUtil 
{ 
    /** 
     * Convert a date string into a Date object
     * according to the date format in GlobalParameter 
     */
    public static Date parseDate(String dateString) {
        return parseDate(dateString, GlobalParameter.DATE_FORMAT);
    }
    
    /** 
     * Convert a date string into a Date object
     * according to the date format input 
     */
    public static Date parseDate(String dateString, String dateFormat) {
        if (dateString == null || dateString.equals(""))
            return null;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
            return sdf.parse(dateString);
		} catch (Exception e) {
			return null;
		}
    }

    public static String formatDate(Date date) {
        return formatDate(date, GlobalParameter.DATE_FORMAT);
    }

    public static String formatDate(Date date, String dateFormat) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date).toUpperCase();
    }
        
    
    
    
    /**
     * date1 > date2  return 1
     * date1 == date 2 return 0
     * date1 < date2 return -1
     */
	public static int compare(Date date1, Date date2)
	{
		if (date1 == null && date2 == null)
			return 0;
		if (date1 == null)
			return 1;
		if (date2 == null)
			return -1;
		if(date1.getTime() > date2.getTime())
			return 1;
		else if (date1.getTime() == date2.getTime())
			return 0;
		else
			return -1;
	}
    
    

} 

