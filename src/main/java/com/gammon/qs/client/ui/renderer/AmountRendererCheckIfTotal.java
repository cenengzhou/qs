package com.gammon.qs.client.ui.renderer;

import com.gammon.qs.application.User;
import com.gammon.qs.application.GeneralPreferencesKey;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.i18n.client.NumberFormat;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.Renderer;

//public class AmountRenderer implements Renderer{
public class AmountRendererCheckIfTotal extends CustomizableRenderer implements Renderer{
	
	public AmountRendererCheckIfTotal(User user) {
		super(user.getGeneralPreferences());
	}
	
	public String render(String str){
		if(str == null)
			return GlobalParameter.NULL_REPLACEMENT;
		
		try{
			str = str.replace(",", "").trim();
			NumberFormat nf = this.getFormat(GeneralPreferencesKey.AMOUNT_DECIMAL_PLACES);
			return nf.format(Double.parseDouble(str));	
		}
		catch(Exception e){
			return GlobalParameter.NULL_REPLACEMENT;
		}	
	}
	
	public String render(Object value, CellMetadata cellMetadata,
			Record record, int rowIndex, int colNum, Store store) {
		if(value == null)
			return GlobalParameter.NULL_REPLACEMENT;
		String result = render(value.toString());
		if(record.getAsBoolean("isTotal"))
			result = "<b>" + result + "</b>";
		return result;
	}

}
