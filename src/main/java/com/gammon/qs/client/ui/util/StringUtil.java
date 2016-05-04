package com.gammon.qs.client.ui.util;

public class StringUtil {

	public static int compare(String str1, String str2)
	{
		if(str1 ==null && str2==null)
			return 0;
		
		boolean isFloat = true;
		float float1=0;
		float float2=0;

		try{
			float1 = Float.parseFloat(str1);		
			float2 = Float.parseFloat(str2);
			
		}catch(NumberFormatException e)
		{
			isFloat = false;
		}
		
		
		if(isFloat)
		{
			if(float1 > float2)
				return 1;
			else if(float1 == float2)
				return 0;
			else
				return -1;
		}
		else
		{
			if(str1==null && str2!=null)
				return 1;
			else if (str1 !=null && str2 ==null)
				return -1;
			else 
				return str1.compareTo(str2);
			
			
		}
		
		
		
	}
}
