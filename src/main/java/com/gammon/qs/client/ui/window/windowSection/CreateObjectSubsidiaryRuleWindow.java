package com.gammon.qs.client.ui.window.windowSection;

import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.ObjectSubsidiaryRuleRepositoryRemote;
import com.gammon.qs.client.repository.ObjectSubsidiaryRuleRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemote;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.StatusButtonPanel;
import com.gammon.qs.domain.ObjectSubsidiaryRule;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.CheckBox;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.FormLayout;
import com.gwtext.client.widgets.layout.RowLayout;

/**
 * @author matthewatc
 * 17:29:18 11 Jan 2012 (UTC+8)
 * Window to create a new object subsidiary rule
 */
public class CreateObjectSubsidiaryRuleWindow extends Window {
	
	public static final String NUMERIC_CODE_REGEX = "^[0-9]?[0-9]$";
	public static final String CODE_REGEX = "^[A-Z0-9]?[A-Z0-9]$"; // regex to validate code input fields (resource type, cost category, main trade)
	
	public static final int WINDOW_WIDTH = 500;
	public static final int WINDOW_HEIGHT = 200;
	
	public static final int CREATE_BUTTON_WIDTH = 90;
	public static final int CLOSE_BUTTON_WIDTH = 60;
	public static final int MAX_STATUS_LENGTH = 50;
	
	private GlobalSectionController globalSectionController;
	
	private Panel formPanel;
	private StatusButtonPanel buttonPanel;
		
	private Button createButton;
	private Button closeButton;
	
	private TextField resourceTypeField;
	private TextField costCategoryField;
	private TextField mainTradeField;
	
	private TextField[] fields; //holds resourceTypeField, costCategoryField, mainTradeField for convenient iteration
	
	private CheckBox applicableCheckBox;
	
	private boolean canWrite = false;
			
	private ObjectSubsidiaryRuleRepositoryRemoteAsync objSubRuleRepository;
	
	public CreateObjectSubsidiaryRuleWindow(GlobalSectionController globalSectionController) {
		this.globalSectionController = globalSectionController;
		
		objSubRuleRepository = (ObjectSubsidiaryRuleRepositoryRemoteAsync) GWT.create(ObjectSubsidiaryRuleRepositoryRemote.class);
		((ServiceDefTarget)objSubRuleRepository).setServiceEntryPoint(GlobalParameter.OBJ_SUB_REPOSITORY_URL);
				
		this.setLayout(new RowLayout());
		this.setClosable(false);
		this.setWidth(WINDOW_WIDTH);
		this.setHeight(WINDOW_HEIGHT);
		this.setTitle("Create Object Subsidiary Rule");
		
		constructForm();
		setUpButtonPanel();
		
		getPermissions();
		
		this.addListener( new WindowListenerAdapter() {
			public void onResize(Window source, int width, int height) {
				buttonPanel.resize();
			}	
		});
		
		this.add(formPanel);
		this.add(buttonPanel);
		resourceTypeField.focus();
	}
	
	private void getPermissions() {
		
		UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository = (UserAccessRightsRepositoryRemoteAsync) GWT.create(UserAccessRightsRepositoryRemote.class);
		((ServiceDefTarget)userAccessRightsRepository).setServiceEntryPoint(GlobalParameter.USER_ACCESS_RIGHTS_REPOSITORY_URL);
		SessionTimeoutCheck.renewSessionTimer();
		userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010512_OBJECT_SUBSIDIARY_RULE_WINDOW, new AsyncCallback<List<String>>(){
			public void onSuccess(List<String> accessRightsReturned) {
					if(accessRightsReturned.contains("WRITE")) {
						setCanWrite(true);
					} else {
						setCanWrite(false);
					}
			}
			
			public void onFailure(Throwable e) {
				buttonPanel.showStatus("An error occured while retrieving permissions from the server: " + e.getMessage());
				setCanWrite(false);
			}
		});
	}
	
	private void setCanWrite(boolean value){
		this.canWrite = value;
		
		if(canWrite) {
			buttonPanel.showButton(createButton);
		}
	}
	
	private void setUpButtonPanel() {
		buttonPanel = new StatusButtonPanel(MAX_STATUS_LENGTH);
		buttonPanel.setBorder(false);
		buttonPanel.setFrame(true);
		buttonPanel.setHeight(40);
		
		createButton = new Button("Create");
		createButton.addClass("right-align-button");
		createButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, com.gwtext.client.core.EventObject e){
				createButton.disable();
				doCreate();
			}
		});
		buttonPanel.addButton(createButton, CREATE_BUTTON_WIDTH, false);
		if(canWrite) { buttonPanel.showButton(createButton); }
		
		closeButton = new Button("Close");
		closeButton.addClass("right-align-button");
		closeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e){
				globalSectionController.closePromptWindow();
			}
		});
		buttonPanel.addButton(closeButton, CLOSE_BUTTON_WIDTH, true);
	}
	
	private void doCreate() {
		if(canWrite) {
			buttonPanel.showStatus("Creating...");
			
			for(TextField f : fields) {
				if(!(f.isValid())) {
					buttonPanel.showStatus("Create failed: One or more entry fields has invalid input");
					createButton.enable();
					return;
				}
				if(!(f.getText().length() >= 1)) {
					buttonPanel.showStatus("Create failed: fields cannot be empty");
					createButton.enable();
					return;
				}
			}
			
			ObjectSubsidiaryRule newRule = new ObjectSubsidiaryRule();
			newRule.setResourceType(resourceTypeField.getValueAsString());
			newRule.setCostCategory(costCategoryField.getValueAsString());
			newRule.setMainTrade(mainTradeField.getValueAsString());
			if(applicableCheckBox.isChecked()){
				newRule.setApplicable("Y");
			} else {
				newRule.setApplicable("N");
			}
			SessionTimeoutCheck.renewSessionTimer();
			objSubRuleRepository.createObjectSubsidiaryRule(newRule, new AsyncCallback<Boolean>() {
				
				public void onSuccess(Boolean ret) {
					buttonPanel.showStatus("Create succeeded");
					createButton.enable();
				}
		
				public void onFailure(Throwable e) {
					buttonPanel.showStatus("Create failed: " + e.getMessage());
					createButton.enable();
				}
			});
		} else {
			buttonPanel.showStatus("Create failed: user does not have write permission");
			createButton.enable();
		}
	}
	
	private void constructForm() {

		formPanel = new Panel();
		formPanel.setLayout(new FormLayout());
		formPanel.setFrame(true);
		
		resourceTypeField = new TextField("Resource Type");
		costCategoryField = new TextField("Cost Category");
		mainTradeField = new TextField("Main Trade");
		applicableCheckBox = new CheckBox("Applicable");
		
		fields = new TextField[] {
				resourceTypeField,
				costCategoryField,
				mainTradeField
		};
		
		int i = 0;
		for(TextField f : fields) {
			if(i==2)
				f.setRegex(CODE_REGEX);
			else
				f.setRegex(NUMERIC_CODE_REGEX);
			i++;
			formPanel.add(f);
		}
		
		formPanel.add(applicableCheckBox);
	}
}
