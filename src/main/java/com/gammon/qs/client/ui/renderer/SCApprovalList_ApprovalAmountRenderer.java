package com.gammon.qs.client.ui.renderer;

import com.google.gwt.i18n.client.NumberFormat;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.Renderer;

public class SCApprovalList_ApprovalAmountRenderer implements Renderer{
	
	boolean isChangedColor = false;
	
	public String render(String strValue)
	{
		Double raw = new Double(0.0);
		try {
			String str = strValue;
			str = str.replaceAll(",", "");
			raw = Double.parseDouble(str.trim());
			
			double rawDouble  = raw;
			raw = new Double(rawDouble);
		}catch(Exception ex) {

		}

		NumberFormat format = NumberFormat.getFormat("#,##0.00");
		return format.format(raw.doubleValue());
		
	}

	
	public String render(Object value, CellMetadata cellMetadata,
			Record record, int rowIndex, int colNum, Store store) {
		String strValue = (String)value;
		
		if(value ==null || "".equals(strValue.trim())) {
			if (rowIndex>0) {
				if (!store.getAt(rowIndex-1).getAsString("approvalRequestID").equals(store.getAt(rowIndex).getAsString("approvalRequestID"))){
					isChangedColor = !isChangedColor;
				}
			}
			return "";
		}

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

		//return format.format(raw.doubleValue());
		
		if (rowIndex>0) {
			if (!store.getAt(rowIndex-1).getAsString("approvalRequestID").equals(store.getAt(rowIndex).getAsString("approvalRequestID"))){
				isChangedColor = !isChangedColor;
			}
			
			if(isChangedColor)
				return "<font color=#0000FF>"+ (value!=null? format.format(raw.doubleValue()):"")+"</font>";

		} else if (rowIndex==0){
				if(isChangedColor)
					return "<font color=#0000FF>"+ (value!=null? format.format(raw.doubleValue()):"")+"</font>";
		}
			return format.format(raw.doubleValue());

	}
}
