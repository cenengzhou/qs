package com.gammon.qs.client.ui.renderer;

import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.SCDetails;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.Renderer;

/**
 * Editable render, can delicate an renderer for number formatting
 * @author chrislam
 *
 */
public class NonTotalEditableColRenderer implements Renderer {

	private Renderer renderer;
	
	public NonTotalEditableColRenderer()
	{
		
	}
	
	public NonTotalEditableColRenderer(Renderer renderer)
	{
		this.renderer = renderer ;
	}
	
	public Renderer getRenderer() {
		return renderer;
	}

	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

	public String render(Object value, CellMetadata cellMetadata,
			Record record, int rowIndex, int colNum, Store store) {
		
		try{
		
		String returnStr = "";
		
		String lineType = record.getAsString("lineType");
		String approved = record.getAsString("approved");
		Integer resourceNo = record.getAsInteger("resourceNo");
		Double costRate = record.getAsDouble("costRate");
		if(this.renderer!=null)	{
			if(value!=null){
				if(lineType!=null && 
						SCDetails.APPROVED_DESC.equals(approved) && 
						(lineType.equals("BQ") || lineType.equals("V3")) || (lineType.equals("V1") && resourceNo!=null && resourceNo!=0 && costRate!=null)){
					returnStr = "<font color=#0000FF>"+ this.renderer.render(value, cellMetadata, record, rowIndex, colNum, store) +"</font>";
				}else
					returnStr = "<font color=#000000>"+ this.renderer.render(value, cellMetadata, record, rowIndex, colNum, store) +"</font>";
			}
		}
		else{
			if(value!=null){
				if(lineType!=null && 
						SCDetails.APPROVED_DESC.equals(approved) && 
						(lineType.equals("BQ") || lineType.equals("V3")) || (lineType.equals("V1") && resourceNo!=null && resourceNo!=0 && costRate!=null)){
					returnStr = "<font color=#0000FF>"+ (value!=null?((String)value):"") +"</font>";
				}else
					returnStr = "<font color=#000000>"+ (value!=null?((String)value):"") +"</font>";
			}
			
		}
		
		return returnStr;
		
		}catch(Exception e)
		{
			UIUtil.alert("EditableColRender ex");
			UIUtil.alert(e);
		}
		
		return "";
	}

}
