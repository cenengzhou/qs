package com.gammon.qs.client.ui.window.treeSection;

import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemote;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.TableLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;


public class CreateAccountMasterWindow extends Window {
	private static final String UI_ID = "CreateAccountMasterWindow";
	private TextField objectTextField;
	private TextField subsidiaryTextField;
	private GlobalMessageTicket globalMessageTicket;
	private Checkbox bqResourceCheckBox;
	private Checkbox resourceSummaryCheckBox;
	private Checkbox scDetailsCheckBox;
	private Checkbox forecastCheckBox;
	private Checkbox selectAllCheckBox;
	// used to check access rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	
	private Button createAccountButton;
	private Button createAccountGroupButton;
	
	
	public CreateAccountMasterWindow(final GlobalSectionController globalSectionController) {

		super();
		this.setId(UI_ID);
		
		// Repository instantiation
		userAccessRightsRepository = (UserAccessRightsRepositoryRemoteAsync) GWT.create(UserAccessRightsRepositoryRemote.class);
		((ServiceDefTarget)userAccessRightsRepository).setServiceEntryPoint(GlobalParameter.USER_ACCESS_RIGHTS_REPOSITORY_URL);

		setFrame(true);
		setLayout(new FitLayout());
		globalMessageTicket = new GlobalMessageTicket();
		setClosable(false);
		this.setPaddings(5);
		this.setWidth(450);
		this.setHeight(450);
		this.setPaddings(20);
		setTitle("Create Account Master");
		Panel basePanel  = new Panel();
		basePanel.setLayout(new VerticalLayout(1));
		FieldSet createOneAccFieldSet = new FieldSet("Create Account by Object Subsidiary",50);
		createOneAccFieldSet.setWidth(getWidth()-70);
		TableLayout createOneAccPanelLayout = new TableLayout(4);
		createOneAccPanelLayout.setSpacing("10");
		createOneAccFieldSet.setPaddings(10);
		createOneAccFieldSet.setLayout(createOneAccPanelLayout);
		Label objectLabel = new Label("Object");
		objectLabel.setWidth(150);
		createOneAccFieldSet.add(objectLabel);
		objectTextField= new TextField ("Object","object",150);
		objectTextField.setMaxLength(6);
		createOneAccFieldSet.add(objectTextField);
		Panel dummyPanel = new Panel();
		dummyPanel.setBorder(false);
		createOneAccFieldSet.add(dummyPanel);
		Panel dummyPanel2 = new Panel();
		dummyPanel2.setBorder(false);
		createOneAccFieldSet.add(dummyPanel2);


		Label subsidiaryLabel = new Label ("Subsidiary");
		subsidiaryLabel.setWidth(150);
		createOneAccFieldSet.add(subsidiaryLabel);
		subsidiaryTextField = new TextField("Subsidiary","subsidiary",150);
		subsidiaryTextField.setMaxLength(8);
		subsidiaryTextField.setWidth(150);
		createOneAccFieldSet.add(subsidiaryTextField);
		Label widthBlankLabel = new Label("");
		createOneAccFieldSet.add(widthBlankLabel);

		createAccountButton = new Button("  Create  ");
		createAccountButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, EventObject e) {
				UIUtil.maskPanelById(UI_ID, "Creating", true );
				globalMessageTicket.refresh();
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getJobCostRepository()
				.createAccountMaster(globalSectionController.getJob().getJobNumber(), 
						objectTextField.getValueAsString(), 
						subsidiaryTextField.getValueAsString(),
						new AsyncCallback<String>(){

							public void onFailure(Throwable e) {
								UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
								UIUtil.unmaskPanelById(UI_ID);
							}

							public void onSuccess(String errMsg) {
								if (errMsg!=null && errMsg.trim().length()>0)
									MessageBox.alert(errMsg);
								else
									MessageBox.alert("Account created.");
								UIUtil.unmaskPanelById(UI_ID);
							}
					
				});
			};
		});
		createOneAccFieldSet.addButton(createAccountButton);
		basePanel.add(createOneAccFieldSet);

		// sec
		FieldSet createGroupFieldSet = new FieldSet("Create Account by Job",50);
		createGroupFieldSet.setWidth(getWidth()-70);
		TableLayout createGroupPanelLayout = new TableLayout(2);
		createGroupPanelLayout.setSpacing("10");
		createGroupFieldSet.setPaddings(10);
		createGroupFieldSet.setLayout(createGroupPanelLayout);
		bqResourceCheckBox = new Checkbox("Resource","Resource");
		bqResourceCheckBox.setWidth(180);
		bqResourceCheckBox.addListener(new CheckboxListenerAdapter(){
			public void onCheck(Checkbox field, boolean checked) {
				if (!checked)
					selectAllCheckBox.setValue(false);
			}
		});
		
		resourceSummaryCheckBox = new Checkbox("Resource Summary","ResourceSummary");
		resourceSummaryCheckBox.setWidth(200);
		resourceSummaryCheckBox.addListener(new CheckboxListenerAdapter(){
			public void onCheck(Checkbox field, boolean checked) {
				if (!checked)
					selectAllCheckBox.setValue(false);
			}
		});
		
		scDetailsCheckBox = new Checkbox("SC Details","SC Details");
		scDetailsCheckBox.setWidth(180);
		scDetailsCheckBox.addListener(new CheckboxListenerAdapter(){
			public void onCheck(Checkbox field, boolean checked) {
				if (!checked)
					selectAllCheckBox.setValue(false);
			}
		});
		
		forecastCheckBox = new Checkbox("Forecasts","Forecast");
		forecastCheckBox.setWidth(180);
		forecastCheckBox.setValue(false);
		forecastCheckBox.addListener(new CheckboxListenerAdapter(){
			public void onCheck(Checkbox field, boolean checked) {
				if (!checked)
					selectAllCheckBox.setValue(false);
			}
		});
		
		
		selectAllCheckBox = new Checkbox("Select All","Select All");
		selectAllCheckBox.addListener(new CheckboxListenerAdapter(){
			public void onCheck(Checkbox field, boolean checked) {
				if (checked){
					bqResourceCheckBox.setValue(true);
					resourceSummaryCheckBox.setValue(true);
					scDetailsCheckBox.setValue(true);
//					forecastCheckBox.setValue(true);
				}
			}
		});
		createGroupFieldSet.add(bqResourceCheckBox);
		createGroupFieldSet.add(resourceSummaryCheckBox);
		createGroupFieldSet.add(scDetailsCheckBox);
//		createGroupFieldSet.add(forecastCheckBox);
		createGroupFieldSet.add(selectAllCheckBox);


		createAccountGroupButton = new Button("  Create  ");
		createAccountGroupButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, EventObject e) {
				UIUtil.maskPanelById(UI_ID, "Creating", true );
				globalMessageTicket.refresh();
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getJobCostRepository()
				.createAccountMasterByGroup(bqResourceCheckBox.getValue(), resourceSummaryCheckBox.getValue(),
						scDetailsCheckBox.getValue(), forecastCheckBox.getValue(),
						globalSectionController.getJob().getJobNumber(), 
					new AsyncCallback<String>(){

						public void onFailure(Throwable e) {
							UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
							UIUtil.unmaskPanelById(UI_ID);
						}

						public void onSuccess(String errMsg) {
							if (errMsg!=null && errMsg.trim().length()>0)
								MessageBox.alert(errMsg);
							UIUtil.unmaskPanelById(UI_ID);
						}
					
				});
			};
		});
		createGroupFieldSet.addButton(createAccountGroupButton);
		basePanel.add(createGroupFieldSet);
		//sec end
		add(basePanel);
		Button closeButton = new Button("Close");
		closeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalSectionController.closeCurrentWindow();				
			};
		});
		addButton(closeButton);
		
		createAccountButton.setVisible(false);
		createAccountGroupButton.setVisible(false);
		UIUtil.maskPanelById(UI_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010508_CREATE_ACCOUNT_MASTER_WINDOW, new AsyncCallback<List<String>>(){
			public void onSuccess(List<String> accessRightsReturned) {
				try{
					accessRightsList = accessRightsReturned;

					UIUtil.unmaskPanelById(UI_ID);
					securitySetup();
				}catch(Exception e){
					UIUtil.unmaskPanelById(UI_ID);
					UIUtil.alert(e);
				}
			}

			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(UI_ID);
				UIUtil.alert(e.getMessage());
			}
		});
	}
	
	private void securitySetup() {
		// if accessRights contain "WRITE"
		if (accessRightsList.contains("WRITE")){
			createAccountButton.setVisible(true);
			createAccountGroupButton.setVisible(true);
		}
	}
}
