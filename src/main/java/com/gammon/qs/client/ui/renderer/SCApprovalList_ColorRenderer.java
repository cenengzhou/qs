package com.gammon.qs.client.ui.renderer;

import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.Renderer;

public class SCApprovalList_ColorRenderer implements Renderer{
	boolean isChangedColor = false;

	public String render(Object value, CellMetadata cellMetadata,
			Record record, int rowIndex, int colNum, Store store) {

		
		if(value ==null || value.toString().trim().length() == 0) {
			if (rowIndex>0) {
				if (!store.getAt(rowIndex-1).getAsString("approvalRequestID").equals(store.getAt(rowIndex).getAsString("approvalRequestID"))){
					isChangedColor = !isChangedColor;
				}
			}
			return "";
		}
		
		String strValue = value.toString();
		if (rowIndex>0) {
			//if (!store.getAt(rowIndex-1).getAsString("approvalRequestID").equals(((String)value))){
			if (!store.getAt(rowIndex-1).getAsString("approvalRequestID").equals(store.getAt(rowIndex).getAsString("approvalRequestID"))){
				isChangedColor = !isChangedColor;
			}
			
			if(isChangedColor)
				return "<font color=#0000FF>" + strValue + "</font>";
				
		} else if (rowIndex==0){
				if(isChangedColor)
					return "<font color=#0000FF>" + strValue + "</font>";
		}
		return strValue;
	}
}
