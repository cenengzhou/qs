package com.gammon.qs.client.ui.renderer;

import com.gammon.qs.application.GeneralPreferencesKey;
import com.gammon.qs.application.User;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.SCDetails;
import com.google.gwt.i18n.client.NumberFormat;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.Renderer;

public class AddendumEnquiryYellowAquaQuantityRenderer extends CustomizableRenderer implements Renderer {

	public AddendumEnquiryYellowAquaQuantityRenderer(User user) {
		super(user.getGeneralPreferences());
	}


	public String render(Object value, CellMetadata cellMetadata,
			Record record, int rowIndex, int colNum, Store store) {

		try{

			String strValue = value+"";


			if(value ==null || "".equals(strValue.trim()))
				return "";


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



			NumberFormat format = this.getFormat(GeneralPreferencesKey.QUANTITY_DECIMAL_PLACES);

			// To be approved VO (Yellow)
			if (/*store.getAt(rowIndex).getAsString("sourceType").equals("A") && !store.getAt(rowIndex).getAsString("lineType").trim().equals("OA")*/
					store.getAt(rowIndex).getAsString("lineType").trim().equals("V1")||store.getAt(rowIndex).getAsString("lineType").trim().equals("V2")||store.getAt(rowIndex).getAsString("lineType").trim().equals("V3")
					||store.getAt(rowIndex).getAsString("lineType").trim().equals("L1")||store.getAt(rowIndex).getAsString("lineType").trim().equals("L2")
					||store.getAt(rowIndex).getAsString("lineType").trim().equals("D1")||store.getAt(rowIndex).getAsString("lineType").trim().equals("D2")
					||store.getAt(rowIndex).getAsString("lineType").trim().equals("CF")
					) {
				if (!store.getAt(rowIndex).getAsString("approved").equals(SCDetails.SUSPEND)) {	
					//if ((!store.getAt(rowIndex).getAsString("approved").equals("A") || !store.getAt(rowIndex).getAsString("bqQuantity").equals(store.getAt(rowIndex).getAsString("toBeApprovedQuantity"))) || !store.getAt(rowIndex).getAsString("scRate").equals(store.getAt(rowIndex).getAsString("toBeApprovedRate"))) {
					if ((store.getAt(rowIndex).getAsDouble("bqQuantity")!=store.getAt(rowIndex).getAsDouble("toBeApprovedQuantity"))|| store.getAt(rowIndex).getAsDouble("scRate")!=(store.getAt(rowIndex).getAsDouble("toBeApprovedRate"))) {
						if (!store.getAt(rowIndex).getAsString("approved").equals(SCDetails.SUSPEND)) {
							return "<table width=\"100%\" height=\"100%\"><tr align = RIGHT><td style=\"background-color:#FFFF00;\"><font color=#000000>"+ format.format(raw.doubleValue())+"</font></td></tr></table>";                 
						} 
						else {
							return format.format(raw.doubleValue());
						}
					} 
					else {
						return format.format(raw.doubleValue());
					}
				} 
				else {
					return format.format(raw.doubleValue());
				}
			}
			//Changed BQ (Aqua)
			else if (store.getAt(rowIndex).getAsString("lineType").trim().equals("B1") || store.getAt(rowIndex).getAsString("lineType").trim().equals("BQ")) {
				if (store.getAt(rowIndex).getAsDouble("bqQuantity")!=(store.getAt(rowIndex).getAsDouble("toBeApprovedQuantity")))
					return "<table width=\"100%\" height=\"100%\"><tr align = RIGHT><td style=\"background-color:#00FFFF;\"><font color=#000000>"+ format.format(raw.doubleValue())+"</font></td></tr></table>";                 
				else 
					return format.format(raw.doubleValue());
			}
			else {
				return format.format(raw.doubleValue());
			}

		}catch(Exception e)
		{				
			UIUtil.alert(e);
		}

		return "0";


	}
}