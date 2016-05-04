package com.gammon.qs.client.ui.toolbar;

import com.gammon.qs.client.controller.MasterListSectionController;
//import com.gammon.qs.client.ui.util.UIUtil;
//import com.gwtext.client.core.EventCallback;
import com.gwtext.client.core.EventObject;
//import com.gwtext.client.core.Function;
//import com.gwtext.client.util.DelayedTask;
import com.gwtext.client.widgets.Button;
//import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
//import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;



public class MasterListSectionToolbar extends Toolbar{
	
	
	private TextField searchField;
	private ToolbarButton filterButton;
	private ToolbarButton searchButton;
	/* added excel export functionality - matthewatc 3/2/12 */
	private ToolbarButton exportButton;
	
	public MasterListSectionToolbar(final MasterListSectionController controller) {
		
		
		this.addText("");
		
		/*this.filterButton = new ToolbarButton("Search");
		
		//filterButton.setEnableToggle(true);
		filterButton.addListener(new ButtonListenerAdapter() {
			
			
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {				
				
				controller.applySearh(searchField.getValueAsString());
			};
		});
		this.addButton(filterButton);
		*/
		
		
		
		this.searchField = new TextField();
		searchField.setWidth(170);
		searchField.setMaxLength(40);
		searchField.setGrow(false);
		searchField.setSelectOnFocus(true);
		searchField.addListener(new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){			
				if(e.getKey()==13)
					controller.applySearch(searchField.getValueAsString());			
			}
		});
		/*searchField.addListener(new TextFieldListenerAdapter() {
			public void onRender(Component component) {
				MasterListSectionToolbar.this.searchField.getEl().addListener("keyup", new EventCallback() {
					public void execute(EventObject e) {
						(new DelayedTask()).delay(1000, new Function() {
							public void execute() {
								controller.applySearh("*"+searchField.getValueAsString()+"*");
							}
						});
					}
				});
			}
		});*/
		this.addField(searchField);
		
		
		searchButton =  new ToolbarButton();
		searchButton.setText("Search");		
		
		searchButton.setCtCls("toolbar-button");
		
		
		
		searchButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {				
				
				controller.applySearch(searchField.getValueAsString());
			};
		});
		this.addButton(searchButton);
		
		
		exportButton =  new ToolbarButton();
		exportButton.setText("Export to Excel");		
		exportButton.setCtCls("toolbar-button");	
		exportButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {				
				controller.exportWorkScope(searchField.getValueAsString());
			};
		});
		this.addButton(exportButton);
		exportButton.setVisible(false);
	}
	
	public void reset() {
		this.searchField.setValue("");
		this.filterButton.setPressed(false);
	}
	
	public boolean isFilterOn() {
		return this.filterButton.isPressed();
	}
	
	public String getFilterString() {
		return this.searchField.getValueAsString();
	}
	
	public void showExportButton() {
		exportButton.setVisible(true);
	}
	
	public void hideExportButton() {
		exportButton.setVisible(false);
	}

}
