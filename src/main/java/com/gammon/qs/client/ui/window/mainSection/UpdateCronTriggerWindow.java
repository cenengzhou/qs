package com.gammon.qs.client.ui.window.mainSection;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.quartz.CronTriggers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.HorizontalLayout;


public class UpdateCronTriggerWindow extends Window {
	private static final String UPDATE_CRON_TRIGGER_WINDOW_ID="UpdateCronTriggerWindow";
	private GlobalMessageTicket globalMessageTicket;
	
	public UpdateCronTriggerWindow(final GlobalSectionController globalSectionController, final CronTriggers cronTrigger){
		globalMessageTicket = new GlobalMessageTicket();
		this.setClosable(false);
		setId(UPDATE_CRON_TRIGGER_WINDOW_ID);
		setTitle("Update Cron Trigger");
		setWidth(300);
		globalMessageTicket = new GlobalMessageTicket();
		Panel mainPanel = new Panel();
		mainPanel.setFrame(false);
		HorizontalLayout mainPanelLayout = new HorizontalLayout(30);
		mainPanelLayout.setColumns(1);
		mainPanel.setLayout(mainPanelLayout);
		FormPanel triggerNamePanel = new FormPanel();
		triggerNamePanel.setFrame(false);
		triggerNamePanel.setBorder(false);
		TextField triggerNameTextField = new TextField("Trigger Name","triggerName");
		triggerNameTextField.setValue(cronTrigger.getTriggerName());
		triggerNameTextField.setDisabled(true);
		triggerNamePanel.add(triggerNameTextField);
		triggerNamePanel.setPaddings(10);
		FormPanel triggerGroupPanel = new FormPanel();
		triggerGroupPanel.setFrame(false);
		triggerGroupPanel.setBorder(false);
		triggerGroupPanel.setPaddings(10);
		TextField triggerGroupTextField = new TextField("Trigger Group","triggerGroup");
		triggerGroupTextField.setValue(cronTrigger.getTriggerName());
		triggerGroupTextField.setDisabled(true);
		triggerGroupPanel.add(triggerGroupTextField);
		FormPanel cronExpressionPanel = new FormPanel();
		cronExpressionPanel.setFrame(false);
		final TextField cronExpressionTextField = new TextField("Cron Expression","cronExpression");
		cronExpressionTextField.setValue(cronTrigger.getCronExpression());
		cronExpressionPanel.add(cronExpressionTextField);
		cronExpressionPanel.setBorder(false);
		cronExpressionPanel.setPaddings(10);
		mainPanel.add(triggerNamePanel);
		mainPanel.add(triggerGroupPanel);
		mainPanel.add(cronExpressionPanel);
		add(mainPanel);
		Button saveButton = new Button("Save");
		saveButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				cronTrigger.setCronExpression(cronExpressionTextField.getValueAsString());
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getQrtzTriggerServiceRepository().updateCronTrigger(cronTrigger, new AsyncCallback<String>(){

					public void onFailure(Throwable e) {
						UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
					}

					public void onSuccess(String returnMessage) {
						if (returnMessage!=null && returnMessage.trim().length()>0)
							MessageBox.alert(returnMessage);
						else{
							MessageBox.alert("Save Success!");
							globalSectionController.closeCurrentWindow();
						}
					}
					
				});
			}
		});
		addButton(saveButton);
		Button cancelButton = new Button("Cancel");
		cancelButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e){
				globalSectionController.closeCurrentWindow();
			}
		});
		addButton(cancelButton);

	}
	
}
