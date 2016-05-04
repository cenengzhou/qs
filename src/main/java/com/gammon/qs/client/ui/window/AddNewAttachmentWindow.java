package com.gammon.qs.client.ui.window;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.util.FileNameSeparator;
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

public class AddNewAttachmentWindow extends Window{

	//
	private GlobalSectionController globalSectionController;

	private FormPanel uploadPanel;
	private String parentWindow;
	private FileNameSeparator fileNameSeparator;
	private GlobalMessageTicket globalMessageTicket;

	public AddNewAttachmentWindow(final ShowAttachmentWindow showAttachmentWindow, final GlobalSectionController globalSectionController, String nameObject, final String textKey, final String subcontractNumber, String sequenceNumber, final String parentWindow){

		super();
		this.globalSectionController = globalSectionController;
		this.parentWindow = parentWindow;
		globalMessageTicket = new GlobalMessageTicket();
		
		this.setTitle("Add Attachment");
		this.setPaddings(2);
		this.setWidth(800);
		this.setHeight(350);
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

		final Hidden sequenceNoHiddenfield = new Hidden("sequenceNo",sequenceNumber);
		uploadPanel.add(sequenceNoHiddenfield);

		final Hidden nameObjectHiddenField = new Hidden("nameObject", nameObject);
		uploadPanel.add(nameObjectHiddenField);

		final Hidden textKeyHiddenField = new Hidden("textKey", textKey);
		uploadPanel.add(textKeyHiddenField);	
		
		final Hidden createdUserHiddenField = new Hidden("createdUser", globalSectionController.getUser().getUsername());
		uploadPanel.add(createdUserHiddenField);

		uploadPanel.addFormListener(new FormListenerAdapter() {
			public void onActionComplete(Form form, int httpStatus, String responseText) {
				uploadResponseCallback(responseText);
				showAttachmentWindow.loadAttachmentList(textKey);
			}

			public void onActionFailed(Form form, int httpStatus, String responseText) {
				uploadResponseCallback(responseText);
			}
		});

		final Button button = new Button("Upload", new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				fileNameSeparator = new FileNameSeparator(fileTextField.getValueAsString().trim(), '\\', '.');
				
				
				int extensionSeparatorLength = 0;
				if (fileTextField.getValueAsString().trim().lastIndexOf(".")>0)
					extensionSeparatorLength = 1;
				//UIUtil.alert("Length: "+(fileNameSeparator.filename().trim().length()+fileNameSeparator.extension().trim().length()+extensionSeparatorLength));
				if ((fileNameSeparator.filename().trim().length()+fileNameSeparator.extension().trim().length()+extensionSeparatorLength)>100)
					MessageBox.alert("Sorry! Your file name is longer than 100 characters, please rename it with a shorter file name for uploading");
				else{
					boolean validateChar = true;
					for (int i=0;i<fileNameSeparator.filename().trim().length()&&validateChar;i++)
						if (!(Character.isLetterOrDigit(fileNameSeparator.filename().charAt(i))||
							new Character('_').equals(fileNameSeparator.filename().charAt(i))||
							new Character('.').equals(fileNameSeparator.filename().charAt(i))||
							new Character('-').equals(fileNameSeparator.filename().charAt(i))||
							new Character(' ').equals(fileNameSeparator.filename().charAt(i))||
							new Character('(').equals(fileNameSeparator.filename().charAt(i))||
							new Character(')').equals(fileNameSeparator.filename().charAt(i))||
							new Character('&').equals(fileNameSeparator.filename().charAt(i)))){
							MessageBox.alert("Sorry! Special Character '"+fileNameSeparator.filename().charAt(i)+"' existed in your file name, please rename it for uploading");
							validateChar= false;
						}
					if (validateChar)
						AddNewAttachmentWindow.this.uploadPanel.getForm().submit(GlobalParameter.UPLOAD_SC_ATTACHMENT_URL, null, Connection.POST, "processing...", false);
				}
			}
		});
		uploadPanel.add(button);

		mainPanel.add(uploadPanel);
		this.add(mainPanel);

		Button closeWindowButton = new Button("Close");
		closeWindowButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				if (parentWindow.equals("p"))
					globalSectionController.closePromptWindow();		
				else if (parentWindow.equals("a")||"v".equalsIgnoreCase(parentWindow))
					globalSectionController.closeAddendumEnquiryAddFileWindow();
			};
		});		

		this.addButton(closeWindowButton);
	}

	private void uploadResponseCallback(String responseText) {
		JSONValue jsonValue = JSONParser.parse(responseText);
		JSONObject jsonObj = jsonValue.isObject();

		if (jsonObj.get("success").isBoolean().booleanValue()) {
			MessageBox.alert("Attachment uploaded successfully.");
			if (parentWindow.equals("p"))
				globalSectionController.closePromptWindow();		
			else if (parentWindow.equals("a")||parentWindow.equals("v"))
				globalSectionController.closeAddendumEnquiryAddFileWindow();
		} else {
			MessageBox.alert("Upload failed! <br/> Cause: " + jsonObj.get("message").isString().stringValue());
		}
	}


}

