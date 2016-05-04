package com.gammon.qs.client.ui.renderer;

import java.util.Map;

import com.google.gwt.i18n.client.NumberFormat;

public class CustomizableRenderer {
	private Map<String, String> generalPreferences;
	private static final String BASE_FORMAT = "#,##0";
	private NumberFormat format;
	
	public CustomizableRenderer(Map<String, String> generalPreferences) {
		this.generalPreferences = generalPreferences;
	}

	protected NumberFormat getFormat(String key) {
		if (format == null) {
			StringBuffer formatString = new StringBuffer();
			formatString.append(BASE_FORMAT);
			
			if (generalPreferences.get(key) != null) {
				int decimalPlaces = Integer.parseInt((String)generalPreferences.get(key));
				if (decimalPlaces > 0) formatString.append(".");
				
				for(int i=0; i<decimalPlaces; i++) {
					formatString.append("0");
				}
			}
			
			format = NumberFormat.getFormat(formatString.toString());
		}
		return format;
	}
}
