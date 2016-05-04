package com.gammon.qs.client.ui.panel.mainSection;

import java.util.List;

import com.gammon.qs.client.controller.MainSectionController;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.Connection;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Form;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FormListenerAdapter;

public class TransitImportPanel extends FormPanel {
	private MainSectionController mainSectionController;
	
	// used to check access rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	
	Button uploadButton;
	
	public TransitImportPanel(MainSectionController mainSectionController, String type) {
		super();
		this.mainSectionController = mainSectionController;
		
		this.userAccessRightsRepository = this.mainSectionController.getGlobalSectionController().getUserAccessRightsRepository();
	
		this.setTitle("Import " + type);
		this.setFileUpload(true);
		this.setFrame(true);
		this.setPaddings(20, 30, 0, 0);
		this.setLabelWidth(110);
		
		TextField fileField = new TextField("Excel File", "file");
		fileField.setInputType("file");
		fileField.setAllowBlank(false);
		this.add(fileField);
		
		final Hidden typeHiddenField = new Hidden("type", type);
		this.add(typeHiddenField);
		final Hidden jobNumberHiddenField = new Hidden("jobNumber", mainSectionController.getCurrentJobNumber());
		this.add(jobNumberHiddenField);
		
		uploadButton = new Button("Upload", new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				getForm().submit(GlobalParameter.TRANSIT_UPLOAD_URL, null, Connection.POST, "processing...", false);
			}
		});
		uploadButton.hide();
		this.add(uploadButton);
		
		securitySetup();
		
		this.addFormListener(new FormListenerAdapter() {
			public void onActionComplete(Form form, int httpStatus, String responseText) {
				transitResponseCallback(responseText);
			}
			
			public void onActionFailed(Form form, int httpStatus, String responseText) {
				transitResponseCallback(responseText);
			}
		});
	}
	
	private void transitResponseCallback(String responseText) {
		JSONValue jsonValue = JSONParser.parse(responseText);
		JSONObject jsonObj = jsonValue.isObject();
		
		if (jsonObj.get("success").isBoolean().booleanValue()) {
			// added by brian on 20110225 - start
			if(jsonObj.get("haveWarning").isBoolean().booleanValue()){
				MessageBox.alert(jsonObj.get("numRecordImported").isNumber().doubleValue() + " records imported successfully with warning.");
				String message = jsonObj.get("message").isString().stringValue();
				if(GlobalParameter.TRANSIT_SUCCESS_WITH_WARNING.equals(message)){
					MessageBox.confirm("Warning", "Import successful with warning!<br/>Download warning reprot?", new MessageBox.ConfirmCallback() {
						
						public void execute(String btnID) {
							if("yes".equals(btnID))
								com.google.gwt.user.client.Window.open(GlobalParameter.TRANSIT_DOWNLOAD_URL + "?type=" + GlobalParameter.TRANSIT_SUCCESS_WITH_WARNING, "_blank", "");
						}
					});
				}
			}
			else{
				MessageBox.alert(jsonObj.get("numRecordImported").isNumber().doubleValue() + " records imported successfully.");
			}
			
			
			// added by brian on 20110225 - end
			
//			MessageBox.alert(jsonObj.get("numRecordImported").isNumber().doubleValue() + " records imported successfully.");
		} else {
			String error = jsonObj.get("message").isString().stringValue();
			if(GlobalParameter.TRANSIT_ERROR.equals(error)){
				MessageBox.confirm("Error", "Import failed!<br/>Download error report?", new MessageBox.ConfirmCallback(){
					public void execute(String btnID) {
						if("yes".equals(btnID))
							com.google.gwt.user.client.Window.open(GlobalParameter.TRANSIT_DOWNLOAD_URL + "?type=" + GlobalParameter.TRANSIT_ERROR, "_blank", "");		
					}
				});
			}
			else
				MessageBox.alert("Import failed! <br/> Cause: " + error);
		}
	}
	
	private void securitySetup(){
		// Enhancement: 
		// Check for access rights
		try{
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(this.mainSectionController.getGlobalSectionController().getUser().getUsername(), RoleSecurityFunctions.F010602_TRANSIT_IMPORTPANEL, new AsyncCallback<List<String>>(){
				public void onSuccess(List<String> accessRightsReturned) {
					accessRightsList = accessRightsReturned;
					//Display Upload Button for "WRITE" only
					if(accessRightsList!=null && accessRightsList.size()> 0 && accessRightsList.contains("WRITE"))
						uploadButton.show();
					else
						uploadButton.hide();						
				}
				
				public void onFailure(Throwable e) {
					UIUtil.alert(e.getMessage());
				}
			});
		}
		catch(Exception e){
			UIUtil.alert(e.getMessage());
		}
	}
}
