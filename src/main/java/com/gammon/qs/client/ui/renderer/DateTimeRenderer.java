package com.gammon.qs.client.ui.renderer;

import java.util.Date;

import com.gammon.qs.client.ui.util.DateUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.Renderer;

public class DateTimeRenderer implements Renderer{
	public static final String DEFAULT_DATE_TIME_FORMATE ="yyyy-MM-dd HH:mm:ss";
	public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store){
		
		try{
		
		if(value == null || "".equals(value))
			return "";
		
		Date dateObj = null;
		if (value instanceof Date) {
			dateObj=(Date) value;
			if (dateObj.getTime()==0)
				return "";
		}
		else {
			if (value!=null && value.toString().trim().length()>0 && Long.parseLong(value.toString())>0 )
				dateObj = new Date(Long.parseLong(value.toString()));
			else return "";
		}
		
		
		return DateUtil.formatDate(dateObj,DEFAULT_DATE_TIME_FORMATE);
		
		}catch(Exception e)
		{
			UIUtil.alert("date render catch");
			UIUtil.alert(e);
			return null;
		}
		
		
		
	}

}
