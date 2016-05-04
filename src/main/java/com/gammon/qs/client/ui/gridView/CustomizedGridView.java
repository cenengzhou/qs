package com.gammon.qs.client.ui.gridView;

import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.RowParams;

public class CustomizedGridView extends GridView{

	public String getRowClass(Record record, int index, RowParams rowParams,  Store store){	
		return "default-font-size";
	}


}
