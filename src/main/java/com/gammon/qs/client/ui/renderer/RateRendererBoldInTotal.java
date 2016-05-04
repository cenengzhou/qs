package com.gammon.qs.client.ui.renderer;

import com.gammon.qs.application.GeneralPreferencesKey;
import com.gammon.qs.application.User;
import com.google.gwt.i18n.client.NumberFormat;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.Renderer;

public class RateRendererBoldInTotal extends CustomizableRenderer implements Renderer {
	
	public RateRendererBoldInTotal(User user) {
		super(user.getGeneralPreferences());
	}

	public String render(String value)
	{
		if(value == null)
			return "";
		
		try{
			value = value.replace(",", "").trim();
			NumberFormat nf = this.getFormat(GeneralPreferencesKey.RATE_DECIMAL_PLACES);
			return nf.format(Double.parseDouble(value));	
		}
		catch(Exception e){
			return "";
		}	
	}

	public String render(Object value, CellMetadata cellMetadata,
			Record record, int rowIndex, int colNum, Store store) {
		if(value == null)
			return "";
		if(rowIndex == (store.getCount() - 1))
			return "<b>" + render(value.toString()) + "</b>";
		return render(value.toString());
	}

}
