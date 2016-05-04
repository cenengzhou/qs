package com.gammon.qs.client.ui.window;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.Radio;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;

public class ScreenPreferenceWindow extends Window {
	private Label descriptionLabel;
	private boolean isSave = true;
	private Radio saveRadio;
	private Radio resetRadio;
	private GlobalSectionController globalSectionController;
	
	public ScreenPreferenceWindow(final GlobalSectionController globalSectionController){
		super();		
		this.setTitle("Screen Preference");
		this.setPaddings(5);
		this.setWidth(300);
		this.setHeight(150);
		this.setClosable(false);
		this.globalSectionController = globalSectionController;
		
		Panel mainPanel = new Panel();
		mainPanel.setHeight(150);
		mainPanel.setWidth(270);
		saveRadio = new Radio("Save", "savePreference");
		resetRadio = new Radio("Reset", "resetPreference");
		saveRadio.setChecked(true);
		descriptionLabel = new Label();
		saveRadio.addListener(new CheckboxListenerAdapter(){
			public void onCheck(Checkbox field, boolean checked){
				if(checked){
					descriptionLabel.setText("Save the current modified screen preference.");
					resetRadio.setChecked(false);
					isSave = true;
				}
			}
		});
		resetRadio.addListener(new CheckboxListenerAdapter(){
			public void onCheck(Checkbox field, boolean checked){
				if(checked){
					descriptionLabel.setText("Reset the current screen to the default screen preference.");
					saveRadio.setChecked(false);
					isSave = false;
				}
			}
		});
		this.add(saveRadio);
		this.add(resetRadio);
		this.add(descriptionLabel);
		
		Button okButton = new Button("OK");
		okButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, EventObject e) {
				if(isSave){
					globalSectionController.saveCurrentScreenPreference();
				}else
					globalSectionController.resetCurrentScreenPreference();
				closeWindow(); 
			};
		});
		
		Button closeButton = new Button("Cancel");
		closeButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, EventObject e) {
				closeWindow();
			};
		});
		
		this.addButton(okButton);
		this.addButton(closeButton);
	}
	private void closeWindow(){
		this.close();
		this.globalSectionController.setCurrentWindow(null);
	}
}