package com.gammon.qs.client.ui.toolbar;

import java.util.Date;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

public class MainSectionIconToolbar extends Toolbar{
	
	private ToolbarButton openJobButton;
	private ToolbarButton saveScreenPreferenceButton;
	@SuppressWarnings("unused")
	private ToolbarButton resetScreenPreferenceButton;
	
	private ToolbarTextItem currentJobLabel;
	private ToolbarButton approvalListButton;
	
	public MainSectionIconToolbar(final GlobalSectionController globalSectionController){
		super();
		
		openJobButton = new ToolbarButton();
		openJobButton.setIconCls("gammon-building-icon");
		openJobButton.setText("Click here to Select Job");
		openJobButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalSectionController.showOpenJobWindow();
			}
		});
		addButton(openJobButton);
		
		ToolTip openTip = new ToolTip();
		openTip.setTitle("Open new Job");
		openTip.setHtml("open a window for selecting job.");
		openTip.setDismissDelay(15000);
		openTip.setWidth(200);
		openTip.setTrackMouse(true);
		openTip.applyTo(openJobButton);
		
		approvalListButton = new ToolbarButton("Approval System");
		approvalListButton.setIconCls("gammon-icon");
		approvalListButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				if (globalSectionController.getJob()==null || globalSectionController.getJob().getJobNumber()==null || globalSectionController.getJob().getJobNumber().trim().length()<1 )
					MessageBox.alert("Please selected the Job first!");
				else{
					Date newDate = new Date();
					String time = newDate.getHours()+""+newDate.getMinutes()+""+newDate.getSeconds()+"";
					String date = newDate.getDate()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getYear()+1900);
					String stringToEncrypt = time+globalSectionController.getUser().getUsername()+date;
					globalSectionController.insertSingleSignOnKey(stringToEncrypt);
				}
			}
		});
		approvalListButton.setVisible(false);
		addSpacer();
		addSpacer();
		addSeparator();
		addButton(approvalListButton);
		addSeparator();
		
		ToolTip approvalListTip = new ToolTip();
		approvalListTip.setTitle("Open Approval List");
		approvalListTip.setHtml("Open Approval Process List or connect to approval System");
		approvalListTip.setDismissDelay(15000);
		approvalListTip.setWidth(200);
		approvalListTip.setTrackMouse(true);
		approvalListTip.applyTo(approvalListButton);
		
		saveScreenPreferenceButton = new ToolbarButton();
		saveScreenPreferenceButton.setIconCls("save-preference-icon");
		saveScreenPreferenceButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalSectionController.openSreenPreferenceWindow();
			}
		});
		
		ToolbarButton tipsButton = new ToolbarButton("Tips");
		tipsButton.setIconCls("bulb-icon");
		tipsButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalSectionController.showMessageBoardMainPanelByTipsButton(0);
			}
		});
		addFill();
		addSeparator();
		addButton(tipsButton);
		
		ToolbarButton homeButton = new ToolbarButton("Home");
		homeButton.setIconCls("home-icon");
		homeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalSectionController.showMessageBoardMainPanelByHomeButton();
			}
		});
		addFill();
		addSeparator();
		addButton(homeButton);
		
		ToolbarButton messageBoardButton = new ToolbarButton("Message Board");
		messageBoardButton.setIconCls("message-icon");
		messageBoardButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalSectionController.showMessageBoardMainPanel();
			}
		});
		addSeparator();
		addButton(messageBoardButton);
		
		ToolTip savePreferenceTip = new ToolTip();
		savePreferenceTip.setTitle("Change Screen Preference");
		savePreferenceTip.setHtml("Change the current screen settings");
		savePreferenceTip.setDismissDelay(15000);
		savePreferenceTip.setWidth(200);
		savePreferenceTip.setTrackMouse(true);
		savePreferenceTip.applyTo(saveScreenPreferenceButton);
		addSeparator();
		addButton(saveScreenPreferenceButton);
		addSeparator();
		currentJobLabel= new ToolbarTextItem("");
		addItem(currentJobLabel);
	}

	public void setOpenJobButton_JobNameDescription(String JobNameDescription) {
		openJobButton.setText(JobNameDescription);
		if (JobNameDescription!=null && JobNameDescription.trim().length()>0)
			approvalListButton.setVisible(true);
		else 
			approvalListButton.setVisible(false);
	}
}
