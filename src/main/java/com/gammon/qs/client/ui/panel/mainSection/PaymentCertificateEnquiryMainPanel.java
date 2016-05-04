package com.gammon.qs.client.ui.panel.mainSection;

import java.util.Date;
import java.util.List;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.gammon.qs.client.controller.DetailSectionController;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.client.ui.renderer.DateRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.client.ui.window.mainSection.JobSelectionWindow;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.scPayment.SCPaymentCertWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowNumberingColumnConfig;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

/**
 * koeyyeung
 * Oct 9, 2013 9:54:01 AM
 * modified by irischau
 * on 10 Apr 2014
 */
public class PaymentCertificateEnquiryMainPanel extends Panel{
	private String mainSectionPanel_ID;
	private static final boolean SORTABLE = true;
	
	private GridPanel gridPanel = new GridPanel();
	
	//pagination toolbar
	private PaginationToolbar paginationToolbar;
	
	//toolbar
	private Toolbar toolbar = new Toolbar();
	
	private ToolbarButton previewPaymentCertificateButton;
	private ToolbarButton excelToolbarButton;
	private ToolbarButton printPaymentCertificateReportButton;
	private ToolbarButton printAllPaymentCertificateReportButton;
	private ToolbarButton tipsButton;
	private Item exportExcelButton;
	
	
	//Display Record
	private static final String DIRECT_PAYMENT_YES = "<font color=#E68550>Yes</font>";
	private static final String DIRECT_PAYMENT_NO = "No";
	
	private static final String JOB_NUMBER_RECORD_NAME = "jobNumberRecordName";
	private static final String COMPANY_RECORD_NAME = "companyRecordName";
	private static final String PACKAGE_NO_RECORD_NAME = "packageNoRecordName";
	private static final String SUBCONTRACTOR_NO_RECORD_NAME = "subcontractorNoRecordName";
	private static final String PAYMENT_TERM_RECORD_NAME = "paymentTermRecordName";
	
	private static final String PAYMENT_CERTIFICATE_NO_RECORD_NAME = "paymentCertNoRecordName";
	private static final String PAYMENT_STATUS_RECORD_NAME = "paymentStatusRecordName";
	private static final String MAIN_CONTRACT_CERTIFICATE_NO_RECORD_NAME = "mainContractCertificateNoRecordName";
	private static final String DUE_DATE_RECORD_NAME = "dueDateRecordName";
	private static final String AS_AT_DATE_RECORD_NAME = "asAtDateRecordName";
	private static final String SC_IPA_RECEIVED_DATE_RECORD_NAME = "scIPAReceivedDateRecordName";
	private static final String CERTIFICATE_ISSUE_DATE_RECORD_NAME = "certificateIssueDateRecordName";
	private static final String CERTIFICATE_AMOUNT_RECORD_NAME = "certificateAmountRecordName";
	private static final String GST_PAYABLE_RECORD_NAME = "GSTPayableRecordName";
	private static final String GST_RECEIVABLE_RECORD_NAME = "GSTReceivableRecordName";
	private static final String INTERIM_FINAL_PAYMENT_RECORD_NAME = "interimFinalPaymentRecordName";
	private static final String DIRECT_PAYMENT_RECORD_NAME = "directPaymentRecordName";
	
	private final RecordDef paymentRecordDef = new RecordDef( 
			new FieldDef[] {	
					new StringFieldDef(JOB_NUMBER_RECORD_NAME),
					new StringFieldDef(COMPANY_RECORD_NAME),
					new StringFieldDef(PACKAGE_NO_RECORD_NAME),
					new StringFieldDef(SUBCONTRACTOR_NO_RECORD_NAME),
					new StringFieldDef(PAYMENT_TERM_RECORD_NAME),	

					new IntegerFieldDef(PAYMENT_CERTIFICATE_NO_RECORD_NAME),
					new StringFieldDef(PAYMENT_STATUS_RECORD_NAME),
					new IntegerFieldDef(MAIN_CONTRACT_CERTIFICATE_NO_RECORD_NAME),
					new DateFieldDef(DUE_DATE_RECORD_NAME),
					new DateFieldDef(AS_AT_DATE_RECORD_NAME),
					new DateFieldDef(SC_IPA_RECEIVED_DATE_RECORD_NAME),
					new DateFieldDef(CERTIFICATE_ISSUE_DATE_RECORD_NAME),
					new FloatFieldDef(CERTIFICATE_AMOUNT_RECORD_NAME),
					new StringFieldDef(GST_PAYABLE_RECORD_NAME),
					new StringFieldDef(GST_RECEIVABLE_RECORD_NAME),
					new StringFieldDef(INTERIM_FINAL_PAYMENT_RECORD_NAME),
					new StringFieldDef(DIRECT_PAYMENT_RECORD_NAME),
			});
	
	
	private TextField jobNoTextField;
	private TextField packageNoTextField;
	private TextField companyTextField;
	private TextField subcontractorNoTextField;
	
	private ToolbarTextItem totalNoOfRecordsValueLabel;
	
	private static final String PAYMENT_STATUS_VALUE = "paymentStatusValue";
	private static final String PAYMENT_STATUS_DISPLAY = "paymentStatusDisplay"; 
	private	ComboBox paymentStatusComboBox = new ComboBox();
	private String[][] paymentStatus = new String[][] {};
	private Store paymentStatusStore = new SimpleStore(new String[] {PAYMENT_STATUS_VALUE, PAYMENT_STATUS_DISPLAY}, paymentStatus);
	
	private static final String PAYMENT_TYPE_VALUE = "paymentTypeValue";
	private static final String PAYMENT_TYPE_DISPLAY = "paymentTypeDisplay"; 
	private	ComboBox paymentTypeComboBox = new ComboBox();
	private String[][] paymentTypes = new String[][] {};
	private Store paymentTypeStore = new SimpleStore(new String[] {PAYMENT_TYPE_VALUE, PAYMENT_TYPE_DISPLAY}, paymentTypes);
	
	private static final String DIRECT_PAYMENT_VALUE = "directPaymentValue";
	private static final String DIRECT_PAYMENT_DISPLAY = "directPaymentDisplay"; 
	private	ComboBox directPaymentComboBox = new ComboBox();
	private String[][] directPayments = new String[][] {};
	private Store directPaymentStore = new SimpleStore(new String[] {DIRECT_PAYMENT_VALUE, DIRECT_PAYMENT_DISPLAY}, directPayments);
	
	private static final String PAYMENT_TERM_VALUE = "paymentTermValue";
	private static final String PAYMENT_TERM_DISPLAY = "paymentTermDisplay"; 
	private	ComboBox paymentTermComboBox = new ComboBox();
	private String[][] paymentTerms = new String[][] {};
	private Store paymentTermStore = new SimpleStore(new String[] {PAYMENT_TERM_VALUE, PAYMENT_TERM_DISPLAY}, paymentTerms);
	
	private ComboBox dueDateComboBox;
	private static String[][] getDueDateType() {  
		return new String[][]{  
				new String[]{"exactDate", "Exact Date"},  
				new String[]{"onOrBefore", "On Or Before"}
		};  
	}
	private DateField dueDateField;
	private DateField certIssueDateField;
	
	private Store dataStore;
	private GlobalMessageTicket globalMessageTicket;
	private GlobalSectionController globalSectionController;
	private DetailSectionController detailSectionController;
	
	private JobSelectionWindow jobSelectionWindow;
	
	private Integer selectedPaymentID = null;
	private String selectedPackageNo = null;
	private String selectedJobNo = null;
	private Integer selectedRow = null;
	
	public PaymentCertificateEnquiryMainPanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		this.detailSectionController = globalSectionController.getDetailSectionController();
		globalMessageTicket = new GlobalMessageTicket();
		mainSectionPanel_ID = globalSectionController.getMainSectionController().getMainPanel().getId();
		setupUI();
	}
	private void setupUI() {
		setBorder(false);
		setFrame(false);
		setPaddings(0);	
		setLayout(new RowLayout());
		
		setupSearchPanel();
		setupToolbar();
		setupGridPanel();
		
		//hide detail panel
		detailSectionController.getMainPanel().collapse();
	}
	
	private void setupToolbar(){
		toolbar.setHeight(25);
		
		//export excel button
		exportExcelButton = new Item("Export");
		exportExcelButton.setIconCls("download-icon");
		exportExcelButton.addListener(new BaseItemListenerAdapter(){

			public void onClick(BaseItem item, EventObject e) {
				
				globalMessageTicket.refresh();
				com.google.gwt.user.client.Window.open(GlobalParameter.PAYMENT_CERTIFICATE_ENQUIRY_EXCEL_EXPORT_URL
												 + "?jobNo=" + jobNoTextField.getText().trim()
												 + "&company=" + companyTextField.getText().trim()
												 + "&packageNo=" + packageNoTextField.getText().trim()
												 + "&subcontractorNo=" + subcontractorNoTextField.getText().trim()
												 + "&paymentStatus=" + paymentStatusComboBox.getValueAsString()
												 + "&paymentType=" + paymentTypeComboBox.getValueAsString()
												 + "&directPayment=" + directPaymentComboBox.getValueAsString()
												 + "&paymentTerm=" + paymentTermComboBox.getValueAsString()
												 + "&dueDateType=" + dueDateComboBox.getValueAsString()
												 + "&dueDate=" + date2String(dueDateField.getValue())
												 + "&certIssueDate=" + date2String(certIssueDateField.getValue())
												, "_blank", "");

			}
		});
		
		ToolTip exportExcelToolTip = new ToolTip();
		exportExcelToolTip.setTitle("Export");
		exportExcelToolTip.setHtml("Export Payment Certificate Enquiry as excel file.");
		exportExcelToolTip.setDismissDelay(15000);
		exportExcelToolTip.setWidth(200);
		exportExcelToolTip.setTrackMouse(true);
		exportExcelToolTip.applyTo(exportExcelButton);
		
		Menu excelMenu = new Menu();
		excelMenu.addItem(exportExcelButton);
		
		excelToolbarButton = new ToolbarButton("Excel");
		excelToolbarButton.setMenu(excelMenu);
		excelToolbarButton.setIconCls("excel-icon");

		
		//Preview Payment Button
		previewPaymentCertificateButton = new ToolbarButton();
		previewPaymentCertificateButton.setText("Preview Payment Certificate");
		previewPaymentCertificateButton.setIconCls("certificate-icon");
		previewPaymentCertificateButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				if(selectedPaymentID != null)
					globalSectionController.showPaymentCertViewWindow(selectedJobNo, selectedPackageNo, new Integer(selectedPaymentID));
				else
					MessageBox.alert("Please select a payment.");
			}
		});
		
		//Print Payment Certificate Button
		printPaymentCertificateReportButton = new ToolbarButton();
		printPaymentCertificateReportButton.setText("Print Unpaid Payment Certificate Report");
		printPaymentCertificateReportButton.setIconCls("certificate-icon");
		printPaymentCertificateReportButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				// Check the Access Right 'ROLE_QS_FINANCE' before print the report
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getUserAccessRightsRepository().getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010214_SCPAYMENT_CERT_REPORT_WINDOW, new AsyncCallback<List<String>>(){
					public void onSuccess(List<String> accessRightsReturned) {
						if(accessRightsReturned!=null && accessRightsReturned.size()> 0 && accessRightsReturned.contains("WRITE")){
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
								String supplierNumber = subcontractorNoTextField.getText() != null ? subcontractorNoTextField.getText() : "";
								com.google.gwt.user.client.Window.open(GlobalParameter.PRINT_UNPAID_PAYMENT_CERTIFICATE_REPORT_PDF+"?jobNumber="+jobNoTextField.getText() +"&company="+companyTextField.getText()+"&dueDate="+dueDate+"&dueDateType="+dueDateType+"&supplierNumber="+supplierNumber, "_blank", "");
							}
						}
						else{
							MessageBox.alert("You are not authorized to print the report.");
						}
					}
					public void onFailure(Throwable e) {
						UIUtil.alert(e.getMessage());				
					}
				});
			}
		});
		
		/**
		 * Button Added by Henry Lai
		 * 13-Nov-2014
		 */
		//Print All Payment Certificate Button
		printAllPaymentCertificateReportButton = new ToolbarButton();
		printAllPaymentCertificateReportButton.setText("Print Payment Certificate Report");
		printAllPaymentCertificateReportButton.setIconCls("certificate-icon");
		printAllPaymentCertificateReportButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getUserAccessJobsRepository().canAccessJob(globalSectionController.getUser().getUsername(), jobNoTextField.getValueAsString(), new AsyncCallback<Boolean>() {
					public void onSuccess(Boolean result) {
						if (result) {
							String jobNo = jobNoTextField.getValueAsString();
							String company = companyTextField.getValueAsString();
							Date date = dueDateField.getValue();
							String dueDate = date2String(date);
							String dueDateType = dueDateComboBox.getValue();
							if((jobNo==null || "".equals(jobNo.trim())) 
								&&(company==null || "".equals(company.trim())) ){
								MessageBox.alert("Please fill in at least 1 search field in 'Company' or 'job No.'.");
								return;
							}
							com.google.gwt.user.client.Window.open(GlobalParameter.PRINT_PAYMENT_CERTIFICATE_REPORT_PDF+"?jobNumber="+jobNoTextField.getText() +"&company="+companyTextField.getText()+"&dueDate="+dueDate+"&dueDateType="+dueDateType, "_blank", "");
							return;
							
						}else {
							UIUtil.alert("User does not have access to this job");
						}
					}
					public void onFailure(Throwable caught) {
						UIUtil.alert("Failed: Unable to authorize User: " + globalSectionController.getUser().getUsername() + " Job: " + jobNoTextField.getValueAsString() + ".");
						UIUtil.throwException(caught);
					}
				});
			}
		});
		
		ToolTip paymentCertToolTip = new ToolTip();
		paymentCertToolTip.setTitle("Payment Certificate");
		paymentCertToolTip.setHtml("To view and print the selected Payment Certificate in PDF");
		paymentCertToolTip.setDismissDelay(15000);
		paymentCertToolTip.setWidth(200);
		paymentCertToolTip.setTrackMouse(true);
		paymentCertToolTip.applyTo(previewPaymentCertificateButton);
		
		
		
		toolbar.addButton(excelToolbarButton);
		toolbar.addSeparator();
		toolbar.addButton(previewPaymentCertificateButton);
		toolbar.addSeparator();
		toolbar.addButton(printPaymentCertificateReportButton);
		toolbar.addSeparator();
		toolbar.addButton(printAllPaymentCertificateReportButton);
		toolbar.addSeparator();
		
		/**
		 * Button added by Henry Lai
		 * 02-Dec-2014
		 */
		tipsButton = new ToolbarButton("Payment Terms/Status Info Tips");
		tipsButton.setIconCls("bulb-icon");
		tipsButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalSectionController.showMessageBoardMainPanelByTipsButton(0);
			}
		});
		
		toolbar.addSeparator();
		toolbar.addFill();
		toolbar.addSeparator();
		toolbar.addButton(tipsButton);
		
		add(toolbar);
	}
	
	private void setupSearchPanel() {
		// main search panel
		Panel mainSearchPanel = new Panel();
		mainSearchPanel.setFrame(true);
		mainSearchPanel.setLayout(new HorizontalLayout(3));
		mainSearchPanel.setPaddings(0);
		mainSearchPanel.setHeight(130);
		
		// search panel
		Panel searchPanel = new Panel();
		searchPanel.setFrame(true);
		searchPanel.setHeight(115);
		searchPanel.setLayout(new TableLayout(6));
		searchPanel.setPaddings(0);
		
		// due date panel
		Panel dueDatePanel = new Panel();
		dueDatePanel.setFrame(true);
		dueDatePanel.setLayout(new VerticalLayout(3));
		dueDatePanel.setPaddings(0);
		dueDatePanel.setHeight(115);
		
		
		FieldListener searchListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER){
					globalMessageTicket.refresh();
					checkAccessiableAndSearch();
				}
			}
		};
		
		FieldListener jobSearchListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER){
					globalMessageTicket.refresh();
					checkAccessiableAndSearch();
				}
			}
			
			public void onFocus(Field field) {
				if (jobSelectionWindow != null && jobSelectionWindow.getJobNumber()!=null) {
					jobNoTextField.setValue(jobSelectionWindow.getJobNumber());
				}
			}
		};
		
		Label companyLabel = new Label("Company");
		companyLabel.setCtCls("table-cell");
		
		companyTextField = new TextField();
		companyTextField.setCtCls("table-cell");
		companyTextField.setWidth(140);
		companyTextField.addListener(searchListener);
		
		searchPanel.add(companyLabel);
		searchPanel.add(companyTextField);
		
		Panel jobSearchPanel = new Panel();
		jobSearchPanel.setLayout(new TableLayout(2));
		
		Label jobNoLabel = new Label("Job No.");
		jobNoLabel.setCtCls("table-cell");
		
		jobNoTextField = new TextField();
		jobNoTextField.setCtCls("table-cell");
		jobNoTextField.setWidth(110);
		jobNoTextField.setValue(globalSectionController.getJob().getJobNumber());
		jobNoTextField.addListener(jobSearchListener);
		jobNoTextField.focus();
		//jobNoTextField.setDisabled(true);

		// Search Job Button
		ToolbarButton jobSearchButton = new ToolbarButton();
		jobSearchButton.setIconCls("filter-icon");
		jobSearchButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				jobSelectionWindow = new JobSelectionWindow(PaymentCertificateEnquiryMainPanel.this,globalSectionController);
				jobSelectionWindow.setModal(true);
				jobSelectionWindow.show();
				jobNoTextField.focus();
			}
		});
		
		jobSearchPanel.add(jobNoTextField);
		jobSearchPanel.add(jobSearchButton);
		
		
		
		searchPanel.add(jobNoLabel);
		searchPanel.add(jobSearchPanel);
		
		Label packageNoLabel = new Label("Package No.");
		packageNoLabel.setCtCls("table-cell");
		
		packageNoTextField = new TextField();
		packageNoTextField.setCtCls("table-cell");
		packageNoTextField.setWidth(140);
		packageNoTextField.addListener(searchListener);
		
		searchPanel.add(packageNoLabel);
		searchPanel.add(packageNoTextField);
		
		
		Label subcontractorNoLabel = new Label("Subcontractor No.");
		subcontractorNoLabel.setCtCls("table-cell");
		
		subcontractorNoTextField = new TextField();
		subcontractorNoTextField.setCtCls("table-cell");
		subcontractorNoTextField.setWidth(140);
		subcontractorNoTextField.addListener(searchListener);
		
		searchPanel.add(subcontractorNoLabel);
		searchPanel.add(subcontractorNoTextField);
		
		//Payment Status
		Label paymentStatusLabel = new Label("Payment Status");
		paymentStatusLabel.setCls("table-cell");
		
	    paymentStatusComboBox.setCtCls("table-cell");
	    paymentStatusComboBox.setForceSelection(true);
	    paymentStatusComboBox.setMinChars(1);
	    paymentStatusComboBox.setValueField(PAYMENT_STATUS_VALUE);
	    paymentStatusComboBox.setDisplayField(PAYMENT_STATUS_DISPLAY);
	    paymentStatusComboBox.setMode(ComboBox.LOCAL);
	    paymentStatusComboBox.setTriggerAction(ComboBox.ALL);
	    paymentStatusComboBox.setEmptyText("ALL");
	    paymentStatusComboBox.setTypeAhead(true);
	    paymentStatusComboBox.setSelectOnFocus(true);
	    paymentStatusComboBox.setWidth(140);
	    paymentStatusComboBox.addListener(searchListener);
	    
	    paymentStatus = new String[][]{
				{" ","ALL"},
				{"PND","Pending"},
				{"SBM", "Submitted"},
				{"PCS","Waiting for Posting"},
				{"UFR","Under Finance Review"},
				{"APR","Posted to Finance"}
				};
	    paymentStatusStore = new SimpleStore(new String[]{PAYMENT_STATUS_VALUE,PAYMENT_STATUS_DISPLAY},paymentStatus);
	    paymentStatusComboBox.setStore(paymentStatusStore);
		searchPanel.add(paymentStatusLabel);
		searchPanel.add(paymentStatusComboBox);
		
		//Payment Type
		Label paymentTypeLabel = new Label("Payment Type");
		paymentTypeLabel.setCls("table-cell");
		
	    paymentTypeComboBox.setCtCls("table-cell");
	    paymentTypeComboBox.setForceSelection(true);
	    paymentTypeComboBox.setMinChars(1);
	    paymentTypeComboBox.setValueField(PAYMENT_TYPE_VALUE);
	    paymentTypeComboBox.setDisplayField(PAYMENT_TYPE_DISPLAY);
	    paymentTypeComboBox.setMode(ComboBox.LOCAL);
	    paymentTypeComboBox.setTriggerAction(ComboBox.ALL);
	    paymentTypeComboBox.setEmptyText("ALL");
	    paymentTypeComboBox.setTypeAhead(true);
	    paymentTypeComboBox.setSelectOnFocus(true);
	    paymentTypeComboBox.setWidth(140);
	    paymentTypeComboBox.addListener(searchListener);
	    
	    paymentTypes = new String[][]{
				{" ","ALL"},
				{"I","Interim"},
				{"F", "Final"}
				};
	    paymentTypeStore = new SimpleStore(new String[]{PAYMENT_TYPE_VALUE,PAYMENT_TYPE_DISPLAY},paymentTypes);
	    paymentTypeComboBox.setStore(paymentTypeStore);
		searchPanel.add(paymentTypeLabel);
		searchPanel.add(paymentTypeComboBox);
		
		//Direct Payment
		Label directPaymentLabel = new Label("Direct Payment");
		directPaymentLabel.setCls("table-cell");
		searchPanel.add(directPaymentLabel);
		
	    directPaymentComboBox.setCtCls("table-cell");
	    directPaymentComboBox.setForceSelection(true);
	    directPaymentComboBox.setMinChars(1);
	    directPaymentComboBox.setValueField(DIRECT_PAYMENT_VALUE);
	    directPaymentComboBox.setDisplayField(DIRECT_PAYMENT_DISPLAY);
	    directPaymentComboBox.setMode(ComboBox.LOCAL);
	    directPaymentComboBox.setTriggerAction(ComboBox.ALL);
	    directPaymentComboBox.setEmptyText("ALL");
	    directPaymentComboBox.setTypeAhead(true);
	    directPaymentComboBox.setSelectOnFocus(true);
	    directPaymentComboBox.setWidth(140);
	    directPaymentComboBox.addListener(searchListener);
	    
	   	directPayments = new String[][]{
	   			{" ","ALL"},
				{"Y","Yes"},
				{"N","No"}
				};
	   	directPaymentStore = new SimpleStore(new String[]{DIRECT_PAYMENT_VALUE,DIRECT_PAYMENT_DISPLAY},directPayments);
	    directPaymentComboBox.setStore(directPaymentStore);
		searchPanel.add(directPaymentLabel);
		searchPanel.add(directPaymentComboBox);
		
		//Payment Term
		Label paymentTermLabel = new Label("Payment Term");
		paymentTermLabel.setCls("table-cell");
		
		paymentTermComboBox.setCtCls("table-cell");
	    paymentTermComboBox.setForceSelection(true);
	    paymentTermComboBox.setMinChars(1);
	    paymentTermComboBox.setValueField(PAYMENT_TERM_VALUE);
	    paymentTermComboBox.setDisplayField(PAYMENT_TERM_DISPLAY);
	    paymentTermComboBox.setMode(ComboBox.LOCAL);
	    paymentTermComboBox.setTriggerAction(ComboBox.ALL);
	    paymentTermComboBox.setEmptyText("ALL");
	    paymentTermComboBox.setTypeAhead(true);
	    paymentTermComboBox.setSelectOnFocus(true);
	    paymentTermComboBox.setWidth(140);
	    paymentTermComboBox.addListener(searchListener);
	    
	    paymentTerms = new String[][]{
	    		{" ","ALL"},
				{"QS0","QS0"},
				{"QS1","QS1"},
				{"QS2","QS2"},
				{"QS3","QS3"},
				{"QS4","QS4"},
				{"QS5","QS5"},
				{"QS6","QS6"},
				{"QS7","QS7"},
				};
	    paymentTermStore = new SimpleStore(new String[]{PAYMENT_TERM_VALUE,PAYMENT_TERM_DISPLAY},paymentTerms);
	    paymentTermComboBox.setStore(paymentTermStore);
		searchPanel.add(paymentTermLabel);
		searchPanel.add(paymentTermComboBox);
		
		// cert issue date
		Label certIssueDateLabel = new Label("Cert. Issue Date");
		certIssueDateLabel.setCtCls("table-cell");
		
		certIssueDateField = new DateField("Date", "d/m/y", 140);
		certIssueDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		certIssueDateField.setCtCls("table-cell");
		certIssueDateField.setHideLabel(true);
		
		searchPanel.add(certIssueDateLabel);
		searchPanel.add(certIssueDateField);
		
		mainSearchPanel.add(searchPanel);
		
		FieldSet dueDateFieldSet = new FieldSet("Due Date");
		dueDateFieldSet.setBorder(true);
		
		Panel dueDateValuePanel = new Panel();
		dueDateValuePanel.setLayout(new TableLayout(2));
		dueDateValuePanel.setPaddings(0);
		
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
		dueDateComboBox.setWidth(100);  
		dueDateComboBox.setValue("onOrBefore");
		
		dueDateField = new DateField("Date", "d/m/y", 100);
		dueDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		dueDateField.setHideLabel(true);
		
		dueDateValuePanel.add(dueDateComboBox);
		dueDateValuePanel.add(dueDateField);
		
		dueDateFieldSet.add(dueDateValuePanel);
		dueDatePanel.add(new Panel());
		dueDatePanel.add(dueDateFieldSet);
		dueDatePanel.add(new Panel());
		mainSearchPanel.add(dueDatePanel);
		
		//button panel
		Panel buttonPanel = new Panel();
		buttonPanel.setFrame(false);
		buttonPanel.setLayout(new HorizontalLayout(20));
		
		// search button
		Button searchButton = new Button();
		searchButton.setText("Search");
		searchButton.setIconCls("find-icon");
		
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				checkAccessiableAndSearch();				
			}
		});
		buttonPanel.add(searchButton);
		mainSearchPanel.add(buttonPanel);
//		searchPanel.add(buttonPanel);
		add(mainSearchPanel);
		
//		add(searchPanel);
		
	}
	
	private void setupGridPanel() {
		
		gridPanel.setBorder(false);
		gridPanel.setFrame(false);
		gridPanel.setPaddings(0);
		gridPanel.setAutoScroll(true);
		gridPanel.setView(new CustomizedGridView());
		
		ColumnConfig jobNoColumnConfig 						= new ColumnConfig("Job No.", JOB_NUMBER_RECORD_NAME, 50, SORTABLE);
		ColumnConfig companyColumnConfig 					= new ColumnConfig("Company", COMPANY_RECORD_NAME , 60, SORTABLE);
		ColumnConfig packgeNoColumnConfig 					= new ColumnConfig("Package No.", PACKAGE_NO_RECORD_NAME , 80, SORTABLE);
		ColumnConfig subcontractorNoColumnConfig 			= new ColumnConfig("Subcontractor No.", SUBCONTRACTOR_NO_RECORD_NAME , 110, SORTABLE);
		ColumnConfig paymentTermColumnConfig 				= new ColumnConfig("Payment Term", PAYMENT_TERM_RECORD_NAME , 100, SORTABLE);
		
		ColumnConfig paymentCertNoColumnConfig 				= new ColumnConfig("Payment No.", PAYMENT_CERTIFICATE_NO_RECORD_NAME , 70, SORTABLE);
		ColumnConfig paymentStatusColumnConfig 				= new ColumnConfig("Status", PAYMENT_STATUS_RECORD_NAME, 160, SORTABLE);
		ColumnConfig mainContractPaymentCertColumnConfig 	= new ColumnConfig("Main Certificate No.", MAIN_CONTRACT_CERTIFICATE_NO_RECORD_NAME, 100, SORTABLE);
		ColumnConfig dueDateColumnConfig 					= new ColumnConfig("Due Date", DUE_DATE_RECORD_NAME, 80, SORTABLE);
		ColumnConfig asAtDateColumnnConfig 					= new ColumnConfig("As at Date", AS_AT_DATE_RECORD_NAME, 80, SORTABLE);
		ColumnConfig scIpaReceivedDateColumnConfig 			= new ColumnConfig("SC IPA Received Date",SC_IPA_RECEIVED_DATE_RECORD_NAME, 120, SORTABLE);
		ColumnConfig certIssueDateColumnConfig 				= new ColumnConfig("Certificate Issue Date", CERTIFICATE_ISSUE_DATE_RECORD_NAME, 100, SORTABLE);
		ColumnConfig certAmountColumnConfig 				= new ColumnConfig("Certificate Amount", CERTIFICATE_AMOUNT_RECORD_NAME, 100, SORTABLE);
		ColumnConfig gstPayableColumnConfig 				= new ColumnConfig("GST Payable", GST_PAYABLE_RECORD_NAME, 100, SORTABLE);
		ColumnConfig gstReceivableColumnConfig 				= new ColumnConfig("GST Receivable", GST_RECEIVABLE_RECORD_NAME, 100, SORTABLE);
		ColumnConfig interimFinalPaymentColumnConfig 		= new ColumnConfig("Interim/Final Payment",INTERIM_FINAL_PAYMENT_RECORD_NAME, 120, SORTABLE);
		ColumnConfig directPaymentColumnConfig 				= new ColumnConfig("Direct Payment?",DIRECT_PAYMENT_RECORD_NAME,80, SORTABLE);
		
		//Set Renderer
		//Date
		Renderer dateRenderer = new DateRenderer();		
		dueDateColumnConfig.setRenderer(dateRenderer);
		asAtDateColumnnConfig.setRenderer(dateRenderer);
		scIpaReceivedDateColumnConfig.setRenderer(dateRenderer);
		certIssueDateColumnConfig.setRenderer(dateRenderer);
		//Amount
		Renderer amountRenderer = new AmountRenderer(globalSectionController.getUser()){
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				if(value == null) 
					return String.valueOf(0.0);
				return render(value.toString());
			}
		};
		certAmountColumnConfig.setRenderer(amountRenderer);
		certAmountColumnConfig.setAlign(TextAlign.RIGHT);
		gstPayableColumnConfig.setRenderer(amountRenderer);
		gstPayableColumnConfig.setAlign(TextAlign.RIGHT);
		gstReceivableColumnConfig.setRenderer(amountRenderer);
		gstReceivableColumnConfig.setAlign(TextAlign.RIGHT);
		
		//Customize the columns
		BaseColumnConfig[] columns = new BaseColumnConfig[] {	new RowNumberingColumnConfig(),
																	companyColumnConfig,	
																	jobNoColumnConfig,
																	packgeNoColumnConfig,
																	subcontractorNoColumnConfig,
																	paymentCertNoColumnConfig,
																	mainContractPaymentCertColumnConfig,
																	paymentTermColumnConfig,
																	paymentStatusColumnConfig,
																	directPaymentColumnConfig,
																	interimFinalPaymentColumnConfig,
																	certAmountColumnConfig,
																	gstPayableColumnConfig,
																	gstReceivableColumnConfig,
																	dueDateColumnConfig,
																	asAtDateColumnnConfig,
																	scIpaReceivedDateColumnConfig,
																	certIssueDateColumnConfig
																	};
		gstPayableColumnConfig.setHidden(true);
		gstReceivableColumnConfig.setHidden(true);
		
		gridPanel.setColumnModel(new ColumnModel(columns));
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(paymentRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		gridPanel.setStore(dataStore);
		
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(int pageNum){
				obtainSCPaymentCertWrapperByPage(pageNum);
			}
		});
		gridPanel.setBottomToolbar(paginationToolbar);
		paginationToolbar.setTotalPage(1);
		paginationToolbar.setCurrentPage(0);
		
		paginationToolbar.addFill();
		
		totalNoOfRecordsValueLabel = new ToolbarTextItem("<b>Total Number of Records: </b>");
		paginationToolbar.addItem(totalNoOfRecordsValueLabel);
		
		add(gridPanel);
		
		setupListeners();
	}
	
	private void setupListeners(){
		gridPanel.addGridRowListener(new GridRowListenerAdapter() {
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				globalMessageTicket.refresh();
				
				if(selectedRow==null || selectedRow.intValue()!=rowIndex){
					selectedRow = new Integer(rowIndex);
					
					//Number of payment certificates (data store includes "total" line that's why minus 1)
					int numOfPaymentCertificate = dataStore.getCount()-1;
					
					//row index starts with 0
					if (rowIndex < numOfPaymentCertificate) {
						Record curRecord = dataStore.getAt(rowIndex);

						selectedPackageNo = curRecord.getAsString(PACKAGE_NO_RECORD_NAME);
						selectedPaymentID = curRecord.getAsInteger(PAYMENT_CERTIFICATE_NO_RECORD_NAME);
						selectedJobNo = curRecord.getAsString(JOB_NUMBER_RECORD_NAME);

					}
				}
			}
			
			public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
				globalMessageTicket.refresh();
				if(selectedPaymentID != null)
					globalSectionController.showPaymentCertViewWindow(selectedJobNo, selectedPackageNo, new Integer(selectedPaymentID));
				
			}
		});
	}
	
	private void obtainSCPaymentCertWrapperByPage(int pageNum) {
		UIUtil.maskPanelById(mainSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPaymentRepository().obtainSCPaymentCertWrapperListByPage(pageNum, new AsyncCallback<PaginationWrapper<SCPaymentCertWrapper>>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
			public void onSuccess(PaginationWrapper<SCPaymentCertWrapper> result) {
				populateGrid(result);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
		});
	}
	
	/**
	 * Henry Lai
	 * Oct 24, 2014 
	 */
	private void checkAccessiableAndSearch(){
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getUserAccessJobsRepository().canAccessJob(globalSectionController.getUser().getUsername(), jobNoTextField.getValueAsString(), new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {
				if (result) {
					search();
				}else {
					UIUtil.alert("User does not have access to this job");
				}
			}
			public void onFailure(Throwable caught) {
				UIUtil.alert("Failed: Unable to authorize User: " + globalSectionController.getUser().getUsername() + " Job: " + jobNoTextField.getValueAsString() + ".");
				UIUtil.throwException(caught);
			}
		});
		
	}
	
	private void search() {
		String jobNo = jobNoTextField.getValueAsString();
		String company = companyTextField.getValueAsString();
		String packageNo = packageNoTextField.getValueAsString();
		String subcontractorNo = subcontractorNoTextField.getValueAsString();
		String paymentStatus = paymentStatusComboBox.getValueAsString();
		String paymentType = paymentTypeComboBox.getValueAsString();
		String directPayment = directPaymentComboBox.getValueAsString();
		String paymentTerm = paymentTermComboBox.getValueAsString();
		Date dueDate = dueDateField.getValue();
		Date certIssueDate = certIssueDateField.getValue();
		String dueDateType = dueDateComboBox.getValue();
		
		if((jobNo==null || "".equals(jobNo.trim())) 
			&&(company==null || "".equals(company.trim())) 
			&&(packageNo==null || "".equals(packageNo))
			&&(company==null || "".equals(company))
			&&(subcontractorNo==null || "".equals(subcontractorNo))){
			MessageBox.alert("Please fill in at least 1 search field.");
			return;
		}
		
		Job job = new Job();
		job.setJobNumber(jobNo);
		job.setCompany(company);
		
		SCPackage scPackage = new SCPackage();
		scPackage.setPackageNo(packageNo);
		scPackage.setVendorNo(subcontractorNo);
		scPackage.setPaymentTerms(paymentTerm);
		
		SCPaymentCertWrapper scPaymentCertWrapper = new SCPaymentCertWrapper();
		scPaymentCertWrapper.setJob(job);
		scPaymentCertWrapper.setJobNo(jobNo);
		scPaymentCertWrapper.setScPackage(scPackage);
		scPaymentCertWrapper.setPaymentStatus(paymentStatus);
		scPaymentCertWrapper.setIntermFinalPayment(paymentType);
		scPaymentCertWrapper.setDirectPayment(directPayment);
		scPaymentCertWrapper.setDueDate(dueDate);
		scPaymentCertWrapper.setCertIssueDate(certIssueDate);
		
		UIUtil.maskPanelById(mainSectionPanel_ID, GlobalParameter.SEARCHING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPaymentRepository().obtainSCPaymentCertificatePaginationWrapper(scPaymentCertWrapper, dueDateType, new AsyncCallback<PaginationWrapper<SCPaymentCertWrapper>>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
			public void onSuccess(PaginationWrapper<SCPaymentCertWrapper> result) {
				populateGrid(result);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
		});
	}
	
	private void populateGrid(PaginationWrapper<SCPaymentCertWrapper> wrappers) {
		dataStore.removeAll();
		
		if(wrappers==null || wrappers.getCurrentPageContentList().size()==0){
			totalNoOfRecordsValueLabel.setText("<b>Total Number of Records: 0 </b>");
			paginationToolbar.setTotalPage(1);
			paginationToolbar.setCurrentPage(0);
			MessageBox.alert("No data found");
			return;
		}
		
		paginationToolbar.setTotalPage(wrappers.getTotalPage());
		paginationToolbar.setCurrentPage(wrappers.getCurrentPage());
		
		totalNoOfRecordsValueLabel.setText("<b>Total Number of Records: "+wrappers.getTotalRecords()+"</b>");
		for (SCPaymentCertWrapper scPaymentCertWrapper: wrappers.getCurrentPageContentList()) {
			
				Record 	record = paymentRecordDef.createRecord( new Object[] {
							scPaymentCertWrapper.getJobNo(),		
						   (scPaymentCertWrapper.getJob()==null)?"":scPaymentCertWrapper.getJob().getCompany(),
							scPaymentCertWrapper.getPackageNo(),
							(scPaymentCertWrapper.getScPackage()==null)?"":scPaymentCertWrapper.getScPackage().getVendorNo(),
							(scPaymentCertWrapper.getScPackage()==null)?"":scPaymentCertWrapper.getScPackage().getPaymentTerms(),
							scPaymentCertWrapper.getPaymentCertNo(),
							convertPaymentStatusToDescription(scPaymentCertWrapper.getPaymentStatus()),
							scPaymentCertWrapper.getMainContractPaymentCertNo(),
							scPaymentCertWrapper.getDueDate(),
							scPaymentCertWrapper.getAsAtDate(),
							scPaymentCertWrapper.getScIpaReceivedDate(),
							scPaymentCertWrapper.getCertIssueDate(),
							scPaymentCertWrapper.getCertAmount(),
							scPaymentCertWrapper.getGstPayable(),
							scPaymentCertWrapper.getGstReceivable(),
							"F".equals(scPaymentCertWrapper.getIntermFinalPayment())? SCPaymentCert.FINAL_PAYMENT:SCPaymentCert.INTERIM_PAYMENT,
							SCPaymentCert.NON_DIRECT_PAYMENT.equals(scPaymentCertWrapper.getDirectPayment())? DIRECT_PAYMENT_NO:DIRECT_PAYMENT_YES
										
					});

			dataStore.add(record);
		}
		
		if (!detailSectionController.getMainPanel().isCollapsed())
			detailSectionController.getMainPanel().collapse();
	}

	private String convertPaymentStatusToDescription(String paymentStatus){
		if(paymentStatus!=null){
			if(paymentStatus.equals("PND"))
				return "<font color=#007D00><b>Pending</b></font>";
			if(paymentStatus.equals("SBM"))
				return "<font color=#E68550><b>Submitted</b></font>";
			if(paymentStatus.equals("PCS"))
				return "<font color=#E68550><b>Waiting for Posting</b></font>";
			if(paymentStatus.equals("UFR"))
				return "<font color=#E68550><b>Under Finance Review</b></font>";
			if(paymentStatus.equals("APR"))
				return "<font color=#707070>Posted to Finance</font>";
		}
		
		return "";
	}
	
	private String date2String(Date date){
		if (date!=null)
			return (new SimpleDateFormat("dd/MM/yyyy")).format(date).toString();
		else
			return "";
	}
	
	public TextField getJobNoTextField() {
		return jobNoTextField;
	}
	public void setJobNoTextField(TextField jobNoTextField) {
		this.jobNoTextField = jobNoTextField;
	}
	
	
	
}
