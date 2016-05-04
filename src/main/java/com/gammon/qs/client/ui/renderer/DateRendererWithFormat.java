package com.gammon.qs.client.ui.renderer;

import java.util.Date;

import com.gammon.qs.client.ui.util.DateUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.Renderer;

public class DateRendererWithFormat implements Renderer{
	
	private String dateFormat;
	public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store){
		
		try{
		
		if(value == null || "".equals(value))
			return "";
		
		Date dateObj = null;
		if (value instanceof Date)
			dateObj=(Date) value;
		else
			if (value!=null && value.toString().trim().length()>0 && Long.parseLong(value.toString())>0 )
				dateObj = new Date(Long.parseLong(value.toString()));
			else return "";

		
		
		return DateUtil.formatDate(dateObj,dateFormat);
		
		}catch(Exception e)
		{
			UIUtil.alert("date render catch Test");
			UIUtil.alert(e);
			return null;
		}
		
		
		
	}
	public DateRendererWithFormat(String dateFormat) {
		super();
		this.dateFormat = dateFormat;
	}

}
