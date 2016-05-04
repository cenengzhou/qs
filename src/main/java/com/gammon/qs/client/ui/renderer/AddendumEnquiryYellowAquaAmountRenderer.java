package com.gammon.qs.client.ui.renderer;

import com.gammon.qs.application.GeneralPreferencesKey;
import com.gammon.qs.application.User;
import com.gammon.qs.domain.SCDetails;
import com.google.gwt.i18n.client.NumberFormat;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.Renderer;

public class AddendumEnquiryYellowAquaAmountRenderer extends CustomizableRenderer implements Renderer {

	public AddendumEnquiryYellowAquaAmountRenderer(User user) {
		super(user.getGeneralPreferences());
	}

	public String render(Object value, CellMetadata cellMetadata,
			Record record, int rowIndex, int colNum, Store store) {

		try{
			String strReturnValue = "0";
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


			//NumberFormat format = NumberFormat.getFormat("#,##0.00");
			NumberFormat format = this.getFormat(GeneralPreferencesKey.AMOUNT_DECIMAL_PLACES);

			//			if( (store.getCount()-1)== rowIndex)
			//				strReturnValue = "<b>"+format.format(raw.doubleValue()) +"</b>";
			//			else		
			strReturnValue = format.format(raw.doubleValue());
			
			return check(store.getAt(rowIndex), strReturnValue);

		}catch(Exception e)
		{

		}

		return "0";

	}
	
	public String check(Record record, String strReturnValue){
		// To be approved VO (Yellow)
		if (/*store.getAt(rowIndex).getAsString("sourceType").equals("A") &&/*!store.getAt(rowIndex).getAsString("lineType").trim().equals("OA")*/
				record.getAsString("lineType").trim().equals("V1")||record.getAsString("lineType").trim().equals("V2")||record.getAsString("lineType").trim().equals("V3")
				||record.getAsString("lineType").trim().equals("L1")||record.getAsString("lineType").trim().equals("L2")
				||record.getAsString("lineType").trim().equals("D1")||record.getAsString("lineType").trim().equals("D2")
				||record.getAsString("lineType").trim().equals("CF")) {
//			if (!record.getAsString("approved").equals("A")){
				if (record.getAsDouble("bqQuantity")!=(record.getAsDouble("toBeApprovedQuantity")) || record.getAsDouble("scRate")!=(record.getAsDouble("toBeApprovedRate"))||!record.getAsString("approved").equals("A")) {

					if (!record.getAsString("approved").equals(SCDetails.SUSPEND)) {
						return "<table width=\"100%\" height=\"100%\"><tr align = RIGHT><td style=\"background-color:#FFFF00;\"><font color=#000000>"+ strReturnValue+"</font></td></tr></table>";                 
					} 
					else {
						return strReturnValue;
					}
				} 
				else {
					return strReturnValue;
				}
//			}
		}
		// Changed BQ (Aqua)
		else if (record.getAsString("lineType").trim().equals("B1") || record.getAsString("lineType").trim().equals("BQ")) {
			if (record.getAsDouble("bqQuantity")!=(record.getAsDouble("toBeApprovedQuantity")))
				return "<table width=\"100%\" height=\"100%\"><tr align = RIGHT><td style=\"background-color:#00FFFF;\"><font color=#000000>"+ strReturnValue+"</font></td></tr></table>";                 
			else 
				return strReturnValue;
		}
		else {
			return strReturnValue;
		}
	}
}
