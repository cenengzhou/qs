package com.gammon.qs.client.ui.renderer;

import com.google.gwt.i18n.client.NumberFormat;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.Renderer;

public class PaymentCertAmountRenderer implements Renderer{

	public String render(String strValue)
	{
		
		@SuppressWarnings("unused")
		Long rawLong = new Long(0);

		Double raw = new Double(0.0);
		try {
			String str = strValue;
			str = str.replaceAll(",", "");
			raw = Double.parseDouble(str.trim());
			
			double rawDouble  = raw;
			raw = new Double(rawDouble);
			//rawLong = Math.round(raw);
		}catch(Exception ex) {
			
			
		}
		
		
		NumberFormat format = NumberFormat.getFormat("#,##0.00");
		return format.format(raw.doubleValue());
		
	}
	
	
	
	public String render(Object value, CellMetadata cellMetadata,
			Record record, int rowIndex, int colNum, Store store) {
		
		try{
		
			String strValue = value+"";
			
			if(value ==null || "".equals(strValue.trim()))
				return "";
			
		
			
			@SuppressWarnings("unused")
			Long rawLong = new Long(0);

			Double raw = new Double(0.0);
			try {
				String str = (String) value;
				str = str.replaceAll(",", "");
				raw = Double.parseDouble(str.trim());
				
				double rawDouble  = raw;
				raw = new Double(rawDouble);
				//rawLong = Math.round(raw);
			}catch(Exception ex) {
				
				
			}
			
			
			NumberFormat format = NumberFormat.getFormat("#,##0.00");
			
			if( (store.getCount()-1)== rowIndex)
				return "<b>"+format.format(raw.doubleValue()) +"</b>";
			else		
				return format.format(raw.doubleValue());
		
		}catch(Exception e)
		{
			
		}
		
		return "0";
		
		
	}

}
