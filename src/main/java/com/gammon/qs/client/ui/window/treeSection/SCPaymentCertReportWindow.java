package com.gammon.qs.client.ui.window.treeSection;
import java.util.Date;
import java.util.List;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemote;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.TableLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class SCPaymentCertReportWindow extends Window{
	//UI
	@SuppressWarnings("unused")
	private GlobalSectionController globalSectionController;
	private Panel mainPanel;
	private Panel searchPanel;
	private Panel dueDatePanel;
	private ComboBox dueDateComboBox;
	
	// used to check access rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	
	private Button genReportButton;
	private Button genExcelButton;
	private static String[][] getDueDateType() {  
		return new String[][]{  
				new String[]{"exactDate", "Exact Date"},  
				new String[]{"onOrBefore", "On Or Before"}
		};  
	} 

	public SCPaymentCertReportWindow(final GlobalSectionController globalSectionController, String jobNumber){
		super();
		this.globalSectionController =globalSectionController;
		this.setTitle("SC Payment Cert Reports");
		this.setPaddings(5);
		this.setWidth(365);
		this.setHeight(270);
		this.setModal(true);
		this.setClosable(false);
		this.setLayout(new FitLayout());
		this.mainPanel = new Panel();
		this.mainPanel.setLayout(new VerticalLayout(2));
		

		userAccessRightsRepository = (UserAccessRightsRepositoryRemoteAsync) GWT.create(UserAccessRightsRepositoryRemote.class);
		((ServiceDefTarget)userAccessRightsRepository).setServiceEntryPoint(GlobalParameter.USER_ACCESS_RIGHTS_REPOSITORY_URL);
		
		//search Panel
		this.searchPanel = new Panel();
		this.searchPanel.setPaddings(3);
		this.searchPanel.setFrame(true);
		this.searchPanel.setHeight(115);
		this.searchPanel.setWidth(340);
		TableLayout searchPanelLayout = new TableLayout(2);
		this.searchPanel.setLayout(searchPanelLayout);
		
		// Pack #
		Label companyLabel = new Label("Company : ");
		companyLabel.setCtCls("table-cell");
		final TextField companyTextField = new TextField("Company","company",150);
		companyTextField.setCtCls("table-cell");
		this.searchPanel.add(companyLabel);
		this.searchPanel.add(companyTextField);
//		this.searchPanel.add(new Label());
		
		// Supplier No #
		Label supplierNumberLabel = new Label("Supplier No. : ");
		supplierNumberLabel.setCtCls("table-cell");
		final TextField supplierNumberTextField = new TextField("Supplier No.","supplierNumber",150);
		supplierNumberTextField.setCtCls("table-cell");
		this.searchPanel.add(supplierNumberLabel);
		this.searchPanel.add(supplierNumberTextField);
//		this.searchPanel.add(new Label());
		

		// Job Number
		Label jobNumberLabel = new Label("Job Number : ");
		jobNumberLabel.setCtCls("table-cell");
		final TextField jobNumberTextField = new TextField("Job Number","jobNo",150);
		jobNumberTextField.setCtCls("table-cell");
		this.searchPanel.add(jobNumberLabel);
		this.searchPanel.add(jobNumberTextField);
		
		FieldSet dueDateFieldSet = new FieldSet("Due Date");
		dueDateFieldSet.setBorder(true);
		
		this.dueDatePanel = new Panel();
		this.dueDatePanel.setPaddings(3);
		this.dueDatePanel.setFrame(true);
		this.dueDatePanel.setHeight(75);
		this.dueDatePanel.setWidth(340);
		
		Panel dueDateValuePanel = new Panel();
		dueDateValuePanel.setLayout(new TableLayout(2));
		
		// Payment Cert #
		final Store storeSearchDueDateType = new SimpleStore(new String[]{"value", "display"}, getDueDateType());  
		storeSearchDueDateType.load();
		
		dueDateComboBox = new ComboBox("Type");   
		dueDateComboBox.setForceSelection(true);  
		dueDateComboBox.setMinChars(1);
		dueDateComboBox.setHideLabel(true);
		//dueDateComboBox.setFieldLabel("Cum/Movement");  
		dueDateComboBox.setStore(storeSearchDueDateType);  
		dueDateComboBox.setDisplayField("display");  
		dueDateComboBox.setValueField("value");
		dueDateComboBox.setMode(ComboBox.LOCAL);  
		dueDateComboBox.setTriggerAction(ComboBox.ALL);  
		dueDateComboBox.setEmptyText("DueDate");  
		dueDateComboBox.setTypeAhead(true);   
		dueDateComboBox.setWidth(150);  
		dueDateComboBox.setValue("onOrBefore");
		
		final DateField dueDateField = new DateField("Date", "d/m/y", 150);
		dueDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		dueDateField.setHideLabel(true);
		
		dueDateValuePanel.add(dueDateComboBox);
		dueDateValuePanel.add(dueDateField);
		
		dueDateFieldSet.add(dueDateValuePanel);

		dueDatePanel.add(dueDateFieldSet);
		
		this.mainPanel.add(searchPanel);
		this.mainPanel.add(dueDatePanel);
		this.add(mainPanel);

		genReportButton = new Button("Generate Report");
		genReportButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				if(companyTextField.getText()==null||companyTextField.getText().trim().equals(""))
					MessageBox.alert("Company cannot be blank.");
				else if(dueDateField.getText()==null||dueDateField.getText().trim().equals(""))
					MessageBox.alert("Due Date cannot be blank.");
				else{
					try {
						Integer.parseInt(companyTextField.getText());
					} catch (Exception e2) {
						MessageBox.alert("Please enter a valid company number.");
						return;
					}
					Date date = dueDateField.getValue();
					String dueDate = date2String(date);
					String dueDateType = dueDateComboBox.getValue();
					String supplierNumber = supplierNumberTextField.getText() != null ? supplierNumberTextField.getText() : "";
					com.google.gwt.user.client.Window.open(GlobalParameter.PRINT_UNPAID_PAYMENT_CERTIFICATE_REPORT_PDF+"?jobNumber="+jobNumberTextField.getText() +"&company="+companyTextField.getText()+"&dueDate="+dueDate+"&dueDateType="+dueDateType+"&supplierNumber="+supplierNumber, "_blank", "");
				}
			};

		});
		genReportButton.hide();
		
		genExcelButton = new Button("Generate Excel");
		genExcelButton.setIconCls("excel-icon");
		genExcelButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				if(companyTextField.getText()==null||companyTextField.getText().trim().equals(""))
					MessageBox.alert("Company cannot be blank.");
				else if(dueDateField.getText()==null||dueDateField.getText().trim().equals(""))
					MessageBox.alert("Due Date cannot be blank.");
				else{
					try {
						Integer.parseInt(companyTextField.getText());
					} catch (Exception e2) {
						MessageBox.alert("Please enter a valid company number.");
						return;
					}
					Date date = dueDateField.getValue();
					String dueDate = date2String(date);
					String dueDateType = dueDateComboBox.getValue();
					String supplierNumber = supplierNumberTextField.getText() != null ? supplierNumberTextField.getText() : "";
					com.google.gwt.user.client.Window.open(GlobalParameter.SUBCONTRACT_PAYMENT_ENQUIRY_EXCEL_DOWNLOAD_URL+"?jobNumber="+jobNumberTextField.getText() +"&company="+companyTextField.getText()+"&dueDate="+dueDate+"&dueDateType="+dueDateType+"&supplierNumber="+supplierNumber, "_blank", "");
				}
			};

		});
		this.addButton(genExcelButton);
		this.addButton(genReportButton);

		// Check for access rights - then add toolbar buttons accordingly
		try{
			UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010214_SCPAYMENT_CERT_REPORT_WINDOW, new AsyncCallback<List<String>>(){
				public void onSuccess(List<String> accessRightsReturned) {
					try{
						accessRightsList = accessRightsReturned;
						displayButtons();
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);						
					}catch(Exception e){
						UIUtil.alert(e);
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
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
		Button closeWindowButton = new Button("Close");
		closeWindowButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				globalSectionController.closeCurrentWindow();				
			};
		});		

		this.addButton(closeWindowButton);

	}
	private String date2String(Date date){
		if (date!=null)
			return (new SimpleDateFormat("dd/MM/yyyy")).format(date).toString();
		else
			return "";
	}
	
	private void displayButtons(){
		if(accessRightsList != null && accessRightsList.contains("WRITE")){
			genReportButton.show();
		}
	}
}
