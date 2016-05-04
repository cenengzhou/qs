package com.gammon.qs.client.ui.window.detailSection;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.gwtext.client.core.Connection;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Form;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FormListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;

public class SCAttachmentWindow extends Window{
	
	//
	@SuppressWarnings("unused")
	private GlobalSectionController globalSectionController;
	
	private GlobalMessageTicket globalMessageTicket;
	
	private FormPanel uploadPanel;
	
	
	public SCAttachmentWindow(final GlobalSectionController globalSectionController, String subcontractNumber){
		
		super();
		this.globalSectionController = globalSectionController;
		globalMessageTicket = new GlobalMessageTicket();
		
		this.setTitle("Attachment Window");
		this.setPaddings(2);
		this.setWidth(800);
		this.setHeight(650);
		this.setClosable(false);
		this.setLayout(new FitLayout());
		
		Panel mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		
		this.uploadPanel = new FormPanel();
		uploadPanel.setFileUpload(true);
		
		final TextField fileTextField = new TextField("File", "file");
		fileTextField.setInputType("file");
		fileTextField.setAllowBlank(false);
		uploadPanel.add(fileTextField);
		
		
		final Hidden jobNumberHiddenField = new Hidden("jobNumber", globalSectionController.getJob().getJobNumber());
		uploadPanel.add(jobNumberHiddenField);
		
		final Hidden subcontractNoHiddenField = new Hidden("subcontractNo", subcontractNumber);
		uploadPanel.add(subcontractNoHiddenField);
		
		final Hidden sequenceNoHiddenfield = new Hidden("sequenceNo","0");
		uploadPanel.add(sequenceNoHiddenfield);
		
		
		uploadPanel.addFormListener(new FormListenerAdapter() {
			public void onActionComplete(Form form, int httpStatus, String responseText) {
				globalMessageTicket.refresh();
				uploadResponseCallback(responseText);
			}
			
			public void onActionFailed(Form form, int httpStatus, String responseText) {
				uploadResponseCallback(responseText);
			}
		});
		
		
		final Button button = new Button("Upload", new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				SCAttachmentWindow.this.uploadPanel.getForm().submit(GlobalParameter.UPLOAD_SC_ATTACHMENT_URL, null, Connection.POST, "processing...", false);
			}
		});
		uploadPanel.add(button);
		
		mainPanel.add(uploadPanel);
		this.add(mainPanel);
		
		Button closeWindowButton = new Button("Close");
		closeWindowButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, com.gwtext.client.core.EventObject e) {
					globalMessageTicket.refresh();
					globalSectionController.closeCurrentWindow();				
				};
		});		

		this.addButton(closeWindowButton);
	}
	
	private void uploadResponseCallback(String responseText) {
		
		JSONValue jsonValue = JSONParser.parse(responseText);
		JSONObject jsonObj = jsonValue.isObject();
		
		if (jsonObj.get("success").isBoolean().booleanValue()) {
			MessageBox.alert("Attachment uploaded successfully.");
		} else {
			MessageBox.alert("Upload failed! <br/> Cause: " + jsonObj.get("message").isString().stringValue());
		}
	}
	

}
