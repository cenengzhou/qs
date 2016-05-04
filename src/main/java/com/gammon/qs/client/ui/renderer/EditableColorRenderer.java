package com.gammon.qs.client.ui.renderer;

import com.gammon.qs.client.ui.util.UIUtil;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.Renderer;

/**
 * Editable render, can delicate an renderer for number formatting
 * @author chrislam
 *
 */
public class EditableColorRenderer implements Renderer {

	private Renderer renderer;
	
	public EditableColorRenderer(){
	}
	
	public EditableColorRenderer(Renderer renderer){
		this.renderer = renderer ;
	}
	
	public Renderer getRenderer() {
		return renderer;
	}

	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

	public String render(Object value, CellMetadata cellMetadata,Record record, int rowIndex, int colNum, Store store) {
		try {
			String returnStr = "";
			if ((store.getCount() - 1) == rowIndex) {
				if (this.renderer != null)
					return "<b> " + this.renderer.render(value, cellMetadata, record, rowIndex, colNum, store) + "</b>";
			}

			if (this.renderer != null)
				returnStr = "<font color=#0000FF>" + this.renderer.render(value, cellMetadata, record, rowIndex, colNum, store) + "</font>";
			else
				returnStr = "<font color=#0000FF>" + (value != null ? (String.valueOf(value)) : "") + "</font>";

			return returnStr;

		} catch (Exception e) {
			UIUtil.alert("EditableColRender Exception: " + e);
		}

		return "";
	}

}
