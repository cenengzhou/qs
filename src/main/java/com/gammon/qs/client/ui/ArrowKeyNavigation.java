package com.gammon.qs.client.ui;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.EditorGridPanel;

public class ArrowKeyNavigation extends FieldListenerAdapter {
	
	private EditorGridPanel grid;
	private int lastEditedCol;
	private int lastEditedRow;
	
	public ArrowKeyNavigation(EditorGridPanel panel) {
		this.grid = panel;
		this.lastEditedCol = -1;
		this.lastEditedRow = -1;
	}
	
	public void onSpecialKey(Field field, EventObject e) {
		if(e.getKey() == EventObject.UP) {
			grid.stopEditing();
			if (lastEditedRow-1 >= 0) {
				grid.startEditing(lastEditedRow-1, lastEditedCol);
			}
		} else if (e.getKey() == EventObject.DOWN) {
			grid.stopEditing();
			if (lastEditedRow+1 < grid.getStore().getCount()) {
				grid.startEditing(lastEditedRow+1, lastEditedCol);
			}
		}
	}
	
	public void startedEdit(int colIndex, int rowIndex) {
		this.lastEditedCol = colIndex;
		this.lastEditedRow = rowIndex;
	}
	
	public void resetState() {
		this.lastEditedCol = -1;
		this.lastEditedRow = -1;
	}

}
