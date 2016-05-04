package com.gammon.qs.client.ui.window.treeSection;

import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
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
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.TableLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;


public class ProvisionPostingWindow extends Window {
	@SuppressWarnings("unused")
	private static final String UI_ID = "ProvisionPostingWindow_ID";
	private TextField provisionJobTextField;
	private DateField glDateTextField;
	private GlobalMessageTicket globalMessageTicket;
	
	// used to check access rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	
	private Button provisionButton;
	
	public ProvisionPostingWindow(final GlobalSectionController globalSectionController) {

		super();

		userAccessRightsRepository = (UserAccessRightsRepositoryRemoteAsync) GWT.create(UserAccessRightsRepositoryRemote.class);
		((ServiceDefTarget)userAccessRightsRepository).setServiceEntryPoint(GlobalParameter.USER_ACCESS_RIGHTS_REPOSITORY_URL);
		
		
		setFrame(true);
		setLayout(new FitLayout());
		globalMessageTicket = new GlobalMessageTicket();
		setClosable(false);
		this.setPaddings(5);
		this.setWidth(450);
		this.setHeight(300);
		this.setPaddings(20);
		setTitle("SC Provision Posting");
		Panel basePanel  = new Panel();
		basePanel.setLayout(new VerticalLayout(1));
		FieldSet provisionFieldSet = new FieldSet("Provision",50);
		provisionFieldSet.setWidth(getWidth()-70);
		TableLayout provisionPanelLayout = new TableLayout(2);
		provisionPanelLayout.setSpacing("10");
		provisionFieldSet.setPaddings(10);
		provisionFieldSet.setLayout(provisionPanelLayout);
		Label provisionJobNumber = new Label("Job Number:");
		provisionJobNumber.setWidth(150);
		provisionFieldSet.add(provisionJobNumber);
		provisionJobTextField = new TextField ("Job Number","jobNumber",150);
		provisionJobTextField.setMaxLength(5);
		provisionFieldSet.add(provisionJobTextField);
		Panel dummyPanel = new Panel();
		dummyPanel.setBorder(false);
		provisionFieldSet.add(dummyPanel);
		Panel dummyPanel2 = new Panel();
		dummyPanel2.setBorder(false);
		provisionFieldSet.add(dummyPanel2);


		Label glDateLabel = new Label ("GL Date:");
		glDateLabel.setWidth(150);
		provisionFieldSet.add(glDateLabel);
		glDateTextField = new DateField("GL Date","Y/m/d");
		glDateTextField.addListener(FieldFactory.updateDatePickerWidthListener());
		glDateTextField.setWidth(150);
		glDateTextField.setAltFormats("dmy");
		provisionFieldSet.add(glDateTextField);
		Label widthBlankLabel = new Label("");
		provisionFieldSet.add(widthBlankLabel);

		provisionButton = new Button("  Post  ");
		provisionButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, EventObject e) {
				UIUtil.maskPanelById(ProvisionPostingWindow.this.getId(), "Posting", true );
				globalMessageTicket.refresh();
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getPackageRepository().generateSCProvisionManually(provisionJobTextField.getText(), glDateTextField.getValue(), new Boolean(false), globalSectionController.getUser().getUsername(), new AsyncCallback<String>(){
					public void onFailure(Throwable e) {
						UIUtil.unmaskPanelById(ProvisionPostingWindow.this.getId());
					}

					public void onSuccess(String errMsg) {
						if (errMsg!=null && errMsg.trim().length()>0)
							MessageBox.alert("         ",errMsg);
						UIUtil.unmaskPanelById(ProvisionPostingWindow.this.getId());
					}
					
				});
			};
		});
		provisionFieldSet.addButton(provisionButton);
		provisionButton.hide();
		basePanel.add(provisionFieldSet);		
		
		add(basePanel);
		Button closeButton = new Button("Close");
		closeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalSectionController.closeCurrentWindow();				
			};
		});
		addButton(closeButton);
		
		// Check for access rights - then add toolbar buttons accordingly
		try{
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010215_PROVISION_POSTING_WINDOW, new AsyncCallback<List<String>>(){
				public void onSuccess(List<String> accessRightsReturned) {
					try{
						accessRightsList = accessRightsReturned;
						displayButtons();				
					}catch(Exception e){
						UIUtil.alert(e);
					}
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
	
	private void displayButtons(){

		if(accessRightsList != null && accessRightsList.contains("WRITE")){
			provisionButton.show();
		}
	}
}
