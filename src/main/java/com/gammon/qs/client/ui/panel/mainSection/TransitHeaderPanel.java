package com.gammon.qs.client.ui.panel.mainSection;

import java.util.List;

import com.gammon.qs.client.controller.MainSectionController;
import com.gammon.qs.client.repository.TransitRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.TransitHeader;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.TransitHeaderResultWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;

public class TransitHeaderPanel extends FormPanel {
	// used to check access rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	
	
	private MainSectionController mainSectionController;
	private TransitRepositoryRemoteAsync transitRepository;
	private static final SimpleStore matchingCodeStore = new SimpleStore(
			new String[]{"code", "description"},
			new String[][]{new String[]{"EX", "EX - Expanded"},
					new String[]{"BS", "BS - Basic"},
					new String[]{"FD", "FD - Foundation"},
					new String[]{"SG", "SG - Singapore"}});
	private TextField jobNumberField;
	private TextField estimateNoField;
	private ComboBox matchingCodeField;
	private Button saveButton;
	
	public TransitHeaderPanel(MainSectionController mainSectionController, TransitHeader header) {
		super();
		this.setFrame(true);
		this.setPaddings(20, 30, 0, 0);
		this.setLabelWidth(110);
		this.mainSectionController = mainSectionController;
		
		transitRepository = this.mainSectionController.getGlobalSectionController().getTransitRepository();		
		userAccessRightsRepository = this.mainSectionController.getGlobalSectionController().getUserAccessRightsRepository();
//		
//		transitRepository = (TransitRepositoryRemoteAsync) GWT.create(TransitRepositoryRemote.class);
//		((ServiceDefTarget)transitRepository).setServiceEntryPoint(GlobalParameter.TRANSIT_REPOSITORY_URL);
		
		jobNumberField = new TextField("Job Number", "jobNumber");
		String jobNumber = mainSectionController.getCurrentJobNumber();
		if(jobNumber != null){
			jobNumberField.setValue(jobNumber);
			jobNumberField.setDisabled(true);
		}
		this.add(jobNumberField);
		
		estimateNoField = new TextField("Estimate Number", "estimateNo");
		estimateNoField.setAllowBlank(false);
		this.add(estimateNoField);
		
		matchingCodeField = new ComboBox("Matching Code", "matchingCode");
		matchingCodeField.setAllowBlank(false);
		matchingCodeField.setForceSelection(true);
		matchingCodeField.setStore(matchingCodeStore);
		matchingCodeField.setValueField("code");
		matchingCodeField.setDisplayField("description");
		this.add(matchingCodeField);
		
		TextField statusField = new TextField("Transit Status", "transitStatus", 200);
		statusField.setDisabled(true);
		this.add(statusField);
		
		saveButton = new Button("Save");
		saveButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				saveHeader();
			}
		});
		saveButton.hide();
		this.add(saveButton);
		
		securitySetup();
		
		if(header != null){
			estimateNoField.setValue(header.getEstimateNo());
			matchingCodeField.setValue(header.getMatchingCode());
			statusField.setValue(header.getStatus());
			if(TransitHeader.TRANSIT_COMPLETED.equals(header.getStatus()))
				saveButton.hide();
			
			// added by brian on 20110117
			// check whether disable save for transit header
			if(disableSaveButton(header)){
				saveButton.setDisabled(true);
				saveButton.hide();
			}
				
		}
	}
	
	private void saveHeader(){
		final String jobNumber = jobNumberField.getValueAsString();
		if(jobNumber == null || jobNumber.length() == 0){
			MessageBox.alert("Please open a job or input a new job number");
			return;
		}
		boolean newJob = !jobNumberField.isDisabled();
		final String estimateNo = estimateNoField.getValueAsString();
		if(estimateNo == null || estimateNo.trim().length() == 0){
			MessageBox.alert("Please input an estimate number");
			return;
		}
		final String matchingCode = matchingCodeField.getValueAsString();
		if(matchingCode == null || matchingCode.trim().length() == 0){
			MessageBox.alert("Please input an matching code");
			return;
		}
		createOrUpdateTransitHeader(jobNumber, estimateNo, matchingCode, newJob);
	}
	
	// added by Brian on 20110117
	// check whether disable save for transit header
	private boolean disableSaveButton(TransitHeader header){
		
		// enable save if no header
		if(header == null)
			return false;
		
		// enable save if no status
		if(header.getStatus() == null || header.getStatus().length() <= 0)
			return false;
		
		// disable save if RESOURCES_CONFIRMED
		if(TransitHeader.RESOURCES_CONFIRMED.equals(header.getStatus()))
			return true;
		
		// disable save if REPORT_PRINTED
		if(TransitHeader.REPORT_PRINTED.equals(header.getStatus()))
			return true;
		
		// disable save if TRANSIT_COMPLETED
		if(TransitHeader.TRANSIT_COMPLETED.equals(header.getStatus()))
			return true;
		
		return false;
	}
	
	private void createOrUpdateTransitHeader(String jobNumber, String estimateNo, String matchingCode, boolean newJob){
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, "Saving", true);
		SessionTimeoutCheck.renewSessionTimer();
		transitRepository.createOrUpdateTransitHeader(jobNumber, estimateNo, matchingCode, newJob, new AsyncCallback<TransitHeaderResultWrapper>(){
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
				UIUtil.checkSessionTimeout(e, false,mainSectionController.getGlobalSectionController().getUser());
			}

			public void onSuccess(TransitHeaderResultWrapper result) {
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
				if(result.getJob() != null)
					mainSectionController.getGlobalSectionController().checkUserAccessAndSetJob(result.getJob());
				if(result.getError() != null)
					MessageBox.alert(result.getError());
				else
					MessageBox.alert("Header Saved");
			}
		});
	}
	
	private void securitySetup(){
		// Enhancement: 
		// Check for access rights
		try{
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(this.mainSectionController.getGlobalSectionController().getUser().getUsername(), RoleSecurityFunctions.F010601_TRANSIT_HEADERPANEL, new AsyncCallback<List<String>>(){
				public void onSuccess(List<String> accessRightsReturned) {
					List<String> accessRightsList = accessRightsReturned;
					//Display Save Button for "WRITE" only
					if(accessRightsList!=null && accessRightsList.size()> 0 && accessRightsList.contains("WRITE"))
						saveButton.show();
					else
						saveButton.hide();						
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
