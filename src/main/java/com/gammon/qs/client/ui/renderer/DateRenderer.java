package com.gammon.qs.client.ui.renderer;

import java.util.Date;

import com.gammon.qs.client.ui.util.DateUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.Renderer;

public class DateRenderer implements Renderer {

	public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
		try {
			if (value == null || "".equals(value))
				return "";

			Date dateObj = (Date) value;

			return DateUtil.formatDate(dateObj);

		} catch (Exception e) {
			UIUtil.alert(e);
			return null;
		}
	}

}
