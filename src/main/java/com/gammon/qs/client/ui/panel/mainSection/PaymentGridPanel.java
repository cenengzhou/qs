package com.gammon.qs.client.ui.panel.mainSection;

import java.util.Date;
import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.repository.PaymentRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.ArrowKeyNavigation;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.panel.PreferenceSetting;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.client.ui.renderer.DateRenderer;
import com.gammon.qs.client.ui.renderer.EditableColorRenderer;
import com.gammon.qs.client.ui.util.RoundingUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.paymentCertView.PaymentCertViewWrapper;
import com.gammon.qs.wrapper.scPayment.PaymentDueDateAndValidationResponseWrapper;
import com.gammon.qs.wrapper.scPayment.SCPaymentCertWithGSTWrapper;
import com.gammon.qs.wrapper.scPayment.SCPaymentCertsWrapper;
import com.gammon.qs.wrapper.submitPayment.SubmitPaymentResponseWrapper;
import com.gammon.qs.wrapper.supplierMaster.SupplierMasterWrapper;
import com.gammon.qs.wrapper.updatePaymentCert.UpdatePaymentCertificateResultWrapper;
import com.gammon.qs.wrapper.updatePaymentCert.UpdatePaymentCertificateWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
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
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;


/**
 * @author tikywong
 * refactored on July 31, 2013
 */
@SuppressWarnings("unchecked")
public class PaymentGridPanel extends EditorGridPanel implements PreferenceSetting{
	private String screenName = "payment-editor-grid";
	private String mainSectionPanel_ID;
	public static final String INTEGER_REGEX = "^[0-9]*$"; // regex with which to validate integer-only text input
	
	private static final String PAYMENT_CERTIFICATE_NO_RECORD_NAME = "paymentCertNoRecordName";
	private static final String PAYMENT_STATUS_RECORD_NAME = "paymentStatusRecordName";
	private static final String PAYMENT_STATUS_DESCRIPTION_RECORD_NAME = "paymentStatusDescriptionRecordName";
	private static final String MAIN_CONTRACT_CERTIFICATE_NO_RECORD_NAME = "mainContractCertificateNoRecordName";
	private static final String DUE_DATE_RECORD_NAME = "dueDateRecordName";
	private static final String AS_AT_DATE_RECORD_NAME = "asAtDateRecordName";
	private static final String IPA_OR_INVOICE_RECEIVED_DATE_RECORD_NAME = "scIPAReceivedDateRecordName";
	private static final String CERTIFICATE_ISSUE_DATE_RECORD_NAME = "certificateIssueDateRecordName";
	private static final String CERTIFICATE_AMOUNT_RECORD_NAME = "certificateAmountRecordName";
	private static final String GST_PAYABLE_RECORD_NAME = "GSTPayableRecordName";
	private static final String GST_RECEIVABLE_RECORD_NAME = "GSTReceivableRecordName";
	private static final String INTERIM_FINAL_PAYMENT_RECORD_NAME = "interimFinalPaymentRecordName";
	private static final String DIRECT_PAYMENT_RECORD_NAME = "directPaymentRecordName";
	//No display on UI, for reference only
	private static final String JOB_NUMBER_RECORD_NAME = "jobNumberRecordName";
	private static final String PACKAGE_NO_RECORD_NAME = "packageNoRecordName";
	private static final String PAYMENT_TERM_RECORD_NAME = "paymentTermRecordName";
	
	//Display Record
	private static final String DIRECT_PAYMENT_YES = "<font color=#E68550>Yes</font>";
	private static final String DIRECT_PAYMENT_NO = "No";
	
	private GlobalSectionController globalSectionController;
	
	//UI Buttons
	private Toolbar toolbar = new Toolbar();
	private ToolbarButton paymentToolbarButton;
	private Item updateButton;
	private Item submitButton;
	
	private ToolbarButton previewPaymentCertificateButton;
	private ToolbarButton refreshButton;
	private ToolbarButton interimFinalPaymentButton;	
	private ToolbarButton tipsButton;
	private final String INTERIM2FINAL = "To Final Payment";
	private final String FINAL2INTERIM = "To Interim Payment";
	
	//UI Columns
	private Store dataStore;
	
	
	private static final String MAIN_CERT_VALUE = "mainCertValue";
	private static final String MAIN_CERT_DISPLAY = "mainCertDisplay";
	private	ComboBox mainCertEditorGridComboBox = new ComboBox();
	private String[][] mainCerts = new String[][] {};
	private Store mainCertStore = new SimpleStore(new String[] {MAIN_CERT_VALUE, MAIN_CERT_DISPLAY }, mainCerts);
	
	//Remote service
	private PaymentRepositoryRemoteAsync paymentRepository;
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	
	//Cursor Navigation
	private ArrowKeyNavigation arrowKeyNavigation;
	
	//calculate value
	private Double totalCertificateAmount = new Double(0);		//Cumulative Certified Amount
	private Double totalGSTPayableAmount = new Double(0);		//Cumulative GST Payable Amount
	private Double totalGSTReceivableAmount = new Double(0);	//Cumulative GST Receivable Amount
	
	private Integer selectedPaymentCertNo = null;
	private String selectedPaymentPaymentStatus = null;
	private String selectedPaymentinterimFinal = null;
	private String selectedPaymentDirectPayment = null;
	private Double selectedPaymentCertAmt = null;
	private Double selectedPaymentGSTPayableAmt = null;
	private Double selectedPaymentGSTReceivableAmt = null;
	
	private SCPaymentCertsWrapper scPaymentCertsWrapper;
	private Job job;
	private SCPackage scPackage;
	private SupplierMasterWrapper supplierMasterWrapper;
	
	private Integer selectedRow;
	
	private List<String> accessRightsList;
	private GlobalMessageTicket globalMessageTicket;
	
	private final RecordDef paymentRecordDef = new RecordDef( 
		new FieldDef[] {	
						new IntegerFieldDef(PAYMENT_CERTIFICATE_NO_RECORD_NAME),
						new StringFieldDef(PAYMENT_STATUS_RECORD_NAME),
						new StringFieldDef(PAYMENT_STATUS_DESCRIPTION_RECORD_NAME),
						new IntegerFieldDef(MAIN_CONTRACT_CERTIFICATE_NO_RECORD_NAME),
						new DateFieldDef(DUE_DATE_RECORD_NAME),
						new DateFieldDef(AS_AT_DATE_RECORD_NAME),
						new DateFieldDef(IPA_OR_INVOICE_RECEIVED_DATE_RECORD_NAME),
						new DateFieldDef(CERTIFICATE_ISSUE_DATE_RECORD_NAME),
						new FloatFieldDef(CERTIFICATE_AMOUNT_RECORD_NAME),
						new StringFieldDef(GST_PAYABLE_RECORD_NAME),
						new StringFieldDef(GST_RECEIVABLE_RECORD_NAME),
						new StringFieldDef(INTERIM_FINAL_PAYMENT_RECORD_NAME),
						new StringFieldDef(DIRECT_PAYMENT_RECORD_NAME),
						//no display on UI, for reference only
						new StringFieldDef(JOB_NUMBER_RECORD_NAME),
						new StringFieldDef(PACKAGE_NO_RECORD_NAME),
						new StringFieldDef(PAYMENT_TERM_RECORD_NAME),
		}	
	);
	
	public PaymentGridPanel(final GlobalSectionController globalSectionController, SCPaymentCertsWrapper scPaymentCertsWrapper) {
		super();
		this.globalSectionController = globalSectionController;
		this.scPaymentCertsWrapper = scPaymentCertsWrapper;
		globalMessageTicket = new GlobalMessageTicket();
		mainSectionPanel_ID = globalSectionController.getMainSectionController().getMainPanel().getId();
		
		preloadData();
		setupUI();

	}
	
	public void preloadData(){
		job = scPaymentCertsWrapper.getJob();
		scPackage = scPaymentCertsWrapper.getScPackage();
		supplierMasterWrapper = scPaymentCertsWrapper.getSupplierMasterWrapper();
		
		totalCertificateAmount = scPaymentCertsWrapper.getTotalCertificateAmount();
		totalGSTPayableAmount = scPaymentCertsWrapper.getTotalGSTPayableAmount();
		totalGSTReceivableAmount = scPaymentCertsWrapper.getTotalGSTReceivableAmount();
		
		if (scPackage!=null && (scPackage.getPaymentTerms().equalsIgnoreCase("QS1") || scPackage.getPaymentTerms().equalsIgnoreCase("QS2")))
			obtainMainCertStore();
	}
	
	private void setupUI(){
		// Repository instantiation
		paymentRepository = globalSectionController.getPaymentRepository();
		userAccessRightsRepository = globalSectionController.getUserAccessRightsRepository();
		
		//Main Panel
		setTrackMouseOver(true);
		setBorder(false);
		setAutoScroll(true);
		
		
		
		//Grid Panel
		setupGridPanel();
		
		//Tool Bar
		setupToolbar();
	}
	
	private void setupGridPanel(){
		arrowKeyNavigation = new ArrowKeyNavigation(this);
		
		setupGridPanelColumns();
		
		//Data Store
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(paymentRecordDef);
		
		dataStore = new Store(proxy, reader);
		dataStore.load();
		dataStore.setSortInfo(new SortState(PAYMENT_CERTIFICATE_NO_RECORD_NAME, SortDir.ASC));
		setStore(dataStore);
		
		//Grid View (Font size)
		GridView view = new CustomizedGridView();
		view.setAutoFill(false);
		view.setForceFit(false);
		setView(view);
		
		
		setupListeners();
	}
	
	/**
	 * Setup column's editability, visibility
	 * @author tikywong
	 * created on Aug 9, 2013 10:42:30 AM
	 */
	private void setupGridPanelColumns(){
		ColumnConfig paymentCertNoColumnConfig 				= new ColumnConfig("No.", PAYMENT_CERTIFICATE_NO_RECORD_NAME , 30);
		ColumnConfig paymentStatusColumnConfig 				= new ColumnConfig("Status", PAYMENT_STATUS_DESCRIPTION_RECORD_NAME, 160);
		ColumnConfig mainContractPaymentCertColumnConfig 	= new ColumnConfig("Main Certificate No.", MAIN_CONTRACT_CERTIFICATE_NO_RECORD_NAME, 100);
		ColumnConfig dueDateColumnConfig 					= new ColumnConfig("Due Date", DUE_DATE_RECORD_NAME, 80);
		ColumnConfig asAtDateColumnnConfig 					= new ColumnConfig("As at Date", AS_AT_DATE_RECORD_NAME, 80);
		ColumnConfig ipaOrInvoiceReceivedDateColumnConfig 	= new ColumnConfig("SC IPA Received Date",IPA_OR_INVOICE_RECEIVED_DATE_RECORD_NAME, 120);
		ColumnConfig certIssueDateColumnConfig 				= new ColumnConfig("Certificate Issue Date", CERTIFICATE_ISSUE_DATE_RECORD_NAME, 100);
		ColumnConfig certAmountColumnConfig 				= new ColumnConfig("Certificate Amount", CERTIFICATE_AMOUNT_RECORD_NAME, 100);
		ColumnConfig gstPayableColumnConfig 				= new ColumnConfig("GST Payable", GST_PAYABLE_RECORD_NAME, 100);
		ColumnConfig gstReceivableColumnConfig 				= new ColumnConfig("GST Receivable", GST_RECEIVABLE_RECORD_NAME, 100);
		ColumnConfig interimFinalPaymentColumnConfig 		= new ColumnConfig("Interim/Final Payment",INTERIM_FINAL_PAYMENT_RECORD_NAME, 120);
		ColumnConfig directPaymentColumnConfig 				= new ColumnConfig("Payment Requisition?",DIRECT_PAYMENT_RECORD_NAME,110);

		//Customize the columns
		ColumnConfig[] columns = new ColumnConfig[] {	paymentCertNoColumnConfig,
														paymentStatusColumnConfig,
														mainContractPaymentCertColumnConfig,
														dueDateColumnConfig,
														asAtDateColumnnConfig,
														ipaOrInvoiceReceivedDateColumnConfig,
														certIssueDateColumnConfig,
														certAmountColumnConfig,
														gstPayableColumnConfig,
														gstReceivableColumnConfig,
														interimFinalPaymentColumnConfig,
														directPaymentColumnConfig};
		
		ColumnConfig[] customizedColumns = globalSectionController.applyScreenPreferences(screenName, columns);
		setColumnModel(new ColumnModel(customizedColumns));
		//Set Renderer
		//Date
		Renderer dateRenderer = new DateRenderer();		
		dueDateColumnConfig.setRenderer(dateRenderer);
		asAtDateColumnnConfig.setRenderer(dateRenderer);
		ipaOrInvoiceReceivedDateColumnConfig.setRenderer(dateRenderer);
		certIssueDateColumnConfig.setRenderer(dateRenderer);
		//Amount
		Renderer amountRender = new AmountRenderer(globalSectionController.getUser());
		certAmountColumnConfig.setRenderer(amountRender);
		gstPayableColumnConfig.setRenderer(amountRender);
		gstReceivableColumnConfig.setRenderer(amountRender);
		
		//Set Editable Renderer
		if( job!=null && (job.getDivision().equals("SGP") || job.getJobNumber().startsWith("14"))){
			Field gstPayableField = FieldFactory.createNegativeNumberField(2);
			if(!SCPackage.INTERNAL_TRADING.equals(scPackage.getFormOfSubcontract())){
				gstPayableColumnConfig.setRenderer(new EditableColorRenderer(amountRender));
				gstPayableColumnConfig.setEditor(new GridEditor(gstPayableField));

				Field gstReceivableField = FieldFactory.createNegativeNumberField(2);

				gstReceivableColumnConfig.setRenderer(new EditableColorRenderer(amountRender));
				gstReceivableColumnConfig.setEditor(new GridEditor(gstReceivableField));
			}
		}
		
		if (scPackage.getPaymentTerms() != null) {
			if (scPackage.getPaymentTerms().equalsIgnoreCase("QS0")) {
				DateField dueDateField = FieldFactory.createDateField();
				dueDateField.addListener(FieldFactory.updateDatePickerWidthListener());
				dueDateColumnConfig.setRenderer(new EditableColorRenderer(dateRenderer));
				dueDateColumnConfig.setEditor(new GridEditor(dueDateField));
			}else if (scPackage.getPaymentTerms().equalsIgnoreCase("QS1") || scPackage.getPaymentTerms().equalsIgnoreCase("QS2")) {
				//Field mainContractPaymentCertField = FieldFactory.createTextFieldAllowNull();

				mainCertEditorGridComboBox.setCtCls("table-cell");
				mainCertEditorGridComboBox.setMinChars(1);
				mainCertEditorGridComboBox.setStore(mainCertStore);
				//mainCertStore.setAutoLoad(true);
				mainCertEditorGridComboBox.setDisplayField(MAIN_CERT_DISPLAY);
				mainCertEditorGridComboBox.setMode(ComboBox.LOCAL);
				mainCertEditorGridComboBox.setTriggerAction(ComboBox.ALL);
				//mainCertEditorGridComboBox.setValueField(MAIN_CERT_VALUE);
				mainCertEditorGridComboBox.setForceSelection(false);
				mainCertEditorGridComboBox.setTypeAhead(true);
				mainCertEditorGridComboBox.setSelectOnFocus(true);
				mainCertEditorGridComboBox.setWidth(150);
				mainCertEditorGridComboBox.setPosition(0,-5);
				
				mainContractPaymentCertColumnConfig.setRenderer(new EditableColorRenderer());
				mainContractPaymentCertColumnConfig.setEditor(new GridEditor(mainCertEditorGridComboBox));
				//mainContractPaymentCertColumnConfig.setEditor(new GridEditor(mainContractPaymentCertField));
			}else if (scPackage.getPaymentTerms().equalsIgnoreCase("QS3") 
					||scPackage.getPaymentTerms().equalsIgnoreCase("QS4")
				    ||scPackage.getPaymentTerms().equalsIgnoreCase("QS5") 
				    ||scPackage.getPaymentTerms().equalsIgnoreCase("QS6")
				    ||scPackage.getPaymentTerms().equalsIgnoreCase("QS7")) {
				DateField ipaOrInvoiceReceivedDateColumnField = FieldFactory.createDateField();
				ipaOrInvoiceReceivedDateColumnField.addListener(FieldFactory.updateDatePickerWidthListener());
				ipaOrInvoiceReceivedDateColumnConfig.setRenderer(new EditableColorRenderer(dateRenderer));
				ipaOrInvoiceReceivedDateColumnConfig.setEditor(new GridEditor(ipaOrInvoiceReceivedDateColumnField));
			}
			
			DateField asAtDateField = FieldFactory.createDateField();
			asAtDateField.addListener(FieldFactory.updateDatePickerWidthListener());
			asAtDateColumnnConfig.setRenderer(new EditableColorRenderer(dateRenderer));
			asAtDateColumnnConfig.setEditor(new GridEditor(asAtDateField));
		}
		
		//Set Alignment
		//Right
		certAmountColumnConfig.setAlign(TextAlign.RIGHT);
		gstPayableColumnConfig.setAlign(TextAlign.RIGHT);
		gstReceivableColumnConfig.setAlign(TextAlign.RIGHT);
		//Center
		paymentCertNoColumnConfig.setAlign(TextAlign.CENTER);
		paymentStatusColumnConfig.setAlign(TextAlign.CENTER);
		interimFinalPaymentColumnConfig.setAlign(TextAlign.CENTER);
		directPaymentColumnConfig.setAlign(TextAlign.CENTER);
		
		//To determine whether to display or enable edit function for the special columns 
		//Set Hidden
		//Singapore jobs
		if( job!=null && 
			(!job.getDivision().equals("SGP") &&
			 !job.getJobNumber().startsWith("14"))){
			gstPayableColumnConfig.setHidden(true);
			gstReceivableColumnConfig.setHidden(true);
		}else if(job!=null && (job.getDivision().equals("SGP") || job.getJobNumber().startsWith("14"))){
			if(SCPackage.INTERNAL_TRADING.equals(scPackage.getFormOfSubcontract())){
				gstPayableColumnConfig.setHidden(true);
				gstReceivableColumnConfig.setHidden(true);
			}
		}

		
		//QS0, QS3, QS4-QS7
		if(scPackage.getPaymentTerms()!=null){
			if(scPackage.getPaymentTerms().equalsIgnoreCase("QS0")
			|| scPackage.getPaymentTerms().equalsIgnoreCase("QS3") 
			|| scPackage.getPaymentTerms().equalsIgnoreCase("QS4") 
			|| scPackage.getPaymentTerms().equalsIgnoreCase("QS5")
			|| scPackage.getPaymentTerms().equalsIgnoreCase("QS6") 
			|| scPackage.getPaymentTerms().equalsIgnoreCase("QS7")){
				mainContractPaymentCertColumnConfig.setHidden(true);
				if(!scPackage.getPaymentTerms().equalsIgnoreCase("QS0") && !scPackage.getPaymentTerms().equalsIgnoreCase("QS3"))
					ipaOrInvoiceReceivedDateColumnConfig.setHeader("Invoice Received Date");
			}
			if(!scPackage.getPaymentTerms().equalsIgnoreCase("QS3") 
			&& !scPackage.getPaymentTerms().equalsIgnoreCase("QS4") 
			&& !scPackage.getPaymentTerms().equalsIgnoreCase("QS5")
			&& !scPackage.getPaymentTerms().equalsIgnoreCase("QS6") 
			&& !scPackage.getPaymentTerms().equalsIgnoreCase("QS7"))
				ipaOrInvoiceReceivedDateColumnConfig.setHidden(true);	
		}
		
		
	}
	
	/**
	 * Setup Row Listener and Grid Listener
	 * @author tikywong
	 * created on Aug 9, 2013 10:41:21 AM
	 */
	private void setupListeners(){
		//1. Row Listener
		addGridRowListener(new GridRowListenerAdapter() {
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				globalMessageTicket.refresh();
				
				if(selectedRow==null || selectedRow.intValue()!=rowIndex){
					selectedRow = new Integer(rowIndex);
					
					//Number of payment certificates (data store includes "total" line that's why minus 1)
					int numOfPaymentCertificate = dataStore.getCount()-1;
					
					//row index starts with 0
					if (rowIndex<numOfPaymentCertificate){
						Record curRecord = dataStore.getAt(rowIndex);
						
						selectedPaymentCertNo 			= 	curRecord.getAsInteger(PAYMENT_CERTIFICATE_NO_RECORD_NAME);
						selectedPaymentPaymentStatus 	= 	curRecord.getAsString(PAYMENT_STATUS_RECORD_NAME);
						selectedPaymentDirectPayment 	= 	curRecord.getAsString(DIRECT_PAYMENT_RECORD_NAME);
						selectedPaymentCertAmt 			= 	curRecord.getAsObject(CERTIFICATE_AMOUNT_RECORD_NAME)==null?
															new Double(0.00):curRecord.getAsDouble(CERTIFICATE_AMOUNT_RECORD_NAME);	
						selectedPaymentGSTPayableAmt 	= 	curRecord.getAsObject(GST_PAYABLE_RECORD_NAME)==null?
															new Double(0.00):curRecord.getAsDouble(GST_PAYABLE_RECORD_NAME);
						selectedPaymentGSTReceivableAmt = 	curRecord.getAsObject(GST_RECEIVABLE_RECORD_NAME)==null? 
															new Double(0.00):curRecord.getAsDouble(GST_RECEIVABLE_RECORD_NAME);
															
						//Interim / Final Payment Button 
						selectedPaymentinterimFinal = curRecord.getAsString(INTERIM_FINAL_PAYMENT_RECORD_NAME);
						if(  SCPaymentCert.PAYMENTSTATUS_PND_PENDING.equals(selectedPaymentPaymentStatus) &&
							"No".equals(selectedPaymentDirectPayment) && 
							 accessRightsList.contains("WRITE"))
							interimFinalPaymentButton.setVisible(true);
						else
							interimFinalPaymentButton.setVisible(false);
						
						if (SCPaymentCert.FINAL_PAYMENT.equals(selectedPaymentinterimFinal))
							interimFinalPaymentButton.setText(FINAL2INTERIM);
						else 
							interimFinalPaymentButton.setText(INTERIM2FINAL);

						globalSectionController.navigateToPaymentDetails(scPackage.getPackageNo(), selectedPaymentCertNo, "");
					}
				}
			}
		});
		
		//2. Grid Listener
		addEditorGridListener(new EditorGridListenerAdapter(){
			//Editable if payment status is PENDING or DIRECT PAYMENT
			@Override
			public boolean doBeforeEdit(GridPanel grid, Record record, String field, Object value, int rowIndex,int colIndex){				
				globalMessageTicket.refresh();
				boolean isEditable = false;
				
				if (rowIndex<dataStore.getCount()-1){
					String paymentStatus = record.getAsString(PAYMENT_STATUS_RECORD_NAME);
					@SuppressWarnings("unused")
					String directPayment = record.getAsString(DIRECT_PAYMENT_RECORD_NAME);
					
					/*if(SCPaymentCert.PAYMENTSTATUS_PND_PENDING.equals(paymentStatus) && 
					  !DIRECT_PAYMENT_YES.equals(directPayment))*/
					if(SCPaymentCert.PAYMENTSTATUS_PND_PENDING.equals(paymentStatus))	
						isEditable = true;
					
					if (isEditable)
						arrowKeyNavigation.startedEdit(colIndex, rowIndex);
					else
						arrowKeyNavigation.resetState();
				}
				return isEditable;
			}
			
			@Override
			public boolean doValidateEdit(GridPanel grid, Record record, String field, Object value, Object originalValue, int rowIndex, int colIndex) {
				globalMessageTicket.refresh();
				boolean isValidInput = true;
				
				// Number only for Main Contract Certificate No.
				if(field.equals(MAIN_CONTRACT_CERTIFICATE_NO_RECORD_NAME)){
					String mainCertNo = String.valueOf(value);
					if(!mainCertNo.matches(INTEGER_REGEX)){
						MessageBox.alert("Please enter numbers only for Main Contract Certificate.");
						record.set(MAIN_CONTRACT_CERTIFICATE_NO_RECORD_NAME, originalValue);
						isValidInput = false;
						
					}
				}
				
				return isValidInput;
			}
			
			@Override
			public void onAfterEdit(GridPanel grid, Record record, String field, Object newValue, Object oldValue, final int rowIndex, int colIndex){
				Double oldValueDouble = 0.00;
				Double newValueDouble = 0.00;
		
				String newValueStr = String.valueOf(newValue);
				String oldValueStr = String.valueOf(oldValue);
			
				//Job "SGP" or "14"
				if(field.equals(GST_PAYABLE_RECORD_NAME)){
					newValueDouble = Double.valueOf(newValueStr!= null && !"".equals(newValueStr.trim())? newValueStr.trim():"0.00");
					oldValueDouble =  Double.valueOf(oldValueStr!=null  && !"".equals(oldValueStr.trim())? oldValueStr.trim():"0.00");
					totalGSTPayableAmount += (newValueDouble - oldValueDouble) ; 
					dataStore.getRecordAt(dataStore.getRecords().length-1).set(GST_PAYABLE_RECORD_NAME, totalGSTPayableAmount.toString());
				}
				else if (field.equals(GST_RECEIVABLE_RECORD_NAME)){
					newValueDouble = Double.valueOf(newValueStr!= null && !"".equals(newValueStr.trim())? newValueStr.trim():"0.00");
					oldValueDouble = Double.valueOf(oldValueStr!=null  && !"".equals(oldValueStr.trim())? oldValueStr.trim():"0.00");
					totalGSTReceivableAmount += (newValueDouble - oldValueDouble);
					dataStore.getRecordAt(dataStore.getRecords().length-1).set(GST_RECEIVABLE_RECORD_NAME, totalGSTReceivableAmount.toString());
				}
			
				//QS3(ipaReceivedDate), QS4-QS7(invoiceReceivedDate)
				Date ipaOrInvoiceReceivedDate 	= record.getAsDate(IPA_OR_INVOICE_RECEIVED_DATE_RECORD_NAME);
				if(field.equals(IPA_OR_INVOICE_RECEIVED_DATE_RECORD_NAME))
					ipaOrInvoiceReceivedDate = (Date)newValue;
				
				//QS1 and QS2
				Integer parentJobMainContractPaymentCert = null;
				if ((record.getAsString(MAIN_CONTRACT_CERTIFICATE_NO_RECORD_NAME) != null) && (!"".equals(record.getAsString(MAIN_CONTRACT_CERTIFICATE_NO_RECORD_NAME).trim()))){		
					parentJobMainContractPaymentCert = record.getAsInteger(MAIN_CONTRACT_CERTIFICATE_NO_RECORD_NAME)==0?null:record.getAsInteger(MAIN_CONTRACT_CERTIFICATE_NO_RECORD_NAME);
				}
				
				//For all payments
				Date asAtDate = record.getAsDate(AS_AT_DATE_RECORD_NAME);
				if(field.equals(AS_AT_DATE_RECORD_NAME))
					asAtDate = (Date)newValue;

				Date dueDate = record.getAsDate(DUE_DATE_RECORD_NAME);
				if(field.equals(DUE_DATE_RECORD_NAME))
					dueDate = (Date)newValue;
			
				if(!"QS0".equals(scPackage.getPaymentTerms())){
					UIUtil.maskPanelById(mainSectionPanel_ID, "Calculating Payment Due Date...", true);
					SessionTimeoutCheck.renewSessionTimer();
					paymentRepository.calculatePaymentDueDate(	job.getJobNumber() ,scPackage.getPackageNo(), selectedPaymentCertNo, parentJobMainContractPaymentCert, 
																asAtDate, ipaOrInvoiceReceivedDate, dueDate, new AsyncCallback<PaymentDueDateAndValidationResponseWrapper>(){
						public void onFailure(Throwable ex) {
							UIUtil.throwException(ex);
							UIUtil.unmaskPanelById(mainSectionPanel_ID);
						}
						public void onSuccess(final PaymentDueDateAndValidationResponseWrapper wrapper){
								if(wrapper.isvalid()){
									if(wrapper.getErrorMsg()!=null){
										MessageBox.confirm("Warning", 
												"Invoice Received Date is suggested to be within 30 days of Payment As At Date. Continue?",
												new MessageBox.ConfirmCallback() {
											public void execute(String btnID) {
												if (btnID.equals("yes")){
													if(wrapper.getDueDate()!=null)
														dataStore.getAt(rowIndex).set(DUE_DATE_RECORD_NAME, wrapper.getDueDate());
													else
														dataStore.getAt(rowIndex).set(DUE_DATE_RECORD_NAME, "");
												}else{
													dataStore.getAt(rowIndex).set(DUE_DATE_RECORD_NAME, "");
												}
											}
										});
									}else{
										if(wrapper.getDueDate()!=null)
											dataStore.getAt(rowIndex).set(DUE_DATE_RECORD_NAME, wrapper.getDueDate());
										else
											dataStore.getAt(rowIndex).set(DUE_DATE_RECORD_NAME, "");
									}
								}
								else
									MessageBox.alert(wrapper.getErrorMsg());
								
								UIUtil.unmaskPanelById(mainSectionPanel_ID);
						}
					});
				}
			}
		});
	}
	
	private void setupToolbar(){
		/*-------------------------- Payment -------------------------*/
		//update Payment Certificate
		updateButton = new Item("Update");
		updateButton.setIconCls("save-button-icon");
		updateButton.addListener(new BaseItemListenerAdapter(){
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				updateAndSubmitPaymentCertificate(new Boolean(false));
			}
		});
		
		//Set the tooltip for the save button
		ToolTip saveToolTip = new ToolTip();
		saveToolTip.setTitle("Save Changes");
		saveToolTip.setHtml("Save the changes made on the grid");
		saveToolTip.setDismissDelay(15000);
		saveToolTip.setWidth(200);
		saveToolTip.setTrackMouse(true);
		saveToolTip.applyTo(updateButton);
		
		//submit Payment Certificate
		submitButton = new Item("Submit");
		submitButton.setIconCls("submit-icon");
		submitButton.addListener(new BaseItemListenerAdapter(){
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				beforeSubmission();
			}
		});
		
		ToolTip submitToolTip = new ToolTip();
		submitToolTip.setTitle("Submit Payment");
		submitToolTip.setHtml("Submit the pending payment on the grid");
		submitToolTip.setDismissDelay(15000);
		submitToolTip.setWidth(200);
		submitToolTip.setTrackMouse(true);
		submitToolTip.applyTo(submitButton);
		
		Menu addendumMenu = new Menu();
		addendumMenu.addItem(updateButton);
		addendumMenu.addItem(submitButton);
		
		paymentToolbarButton = new ToolbarButton("Payment");
		paymentToolbarButton.setMenu(addendumMenu);
		paymentToolbarButton.setIconCls("menu-show-icon");
		/*-------------------------- Payment -------------------------*/
		
		
		/*-------------Preview Payment Button---------------*/
		previewPaymentCertificateButton = new ToolbarButton();
		previewPaymentCertificateButton.setText("Preview Payment Certificate");
		previewPaymentCertificateButton.setIconCls("certificate-icon");
		previewPaymentCertificateButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				if(selectedPaymentCertNo != null)
					globalSectionController.showPaymentCertViewWindow(globalSectionController.getJob().getJobNumber(), globalSectionController.getSelectedPackageNumber(), new Integer(selectedPaymentCertNo));
				else
					MessageBox.alert("Please select a payment.");
			}
		});
		
		ToolTip paymentCertToolTip = new ToolTip();
		paymentCertToolTip.setTitle("Payment Certificate");
		paymentCertToolTip.setHtml("To view and print the selected Payment Certificate in PDF");
		paymentCertToolTip.setDismissDelay(15000);
		paymentCertToolTip.setWidth(200);
		paymentCertToolTip.setTrackMouse(true);
		paymentCertToolTip.applyTo(previewPaymentCertificateButton);
		/*-------------Preview Payment Button---------------*/
		
		
		
		/*------------set To Final/Interim Payment-------------------*/
		interimFinalPaymentButton = new ToolbarButton();
		interimFinalPaymentButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				interimFinalPayment();
			}
		});
		ToolTip ifPaymentToolTip = new ToolTip();
		ifPaymentToolTip.setTabCls("Interim/Final Payment");
		ifPaymentToolTip.setHtml("Change Interim to Final Payment or Interim to Final Payment");
		ifPaymentToolTip.setDismissDelay(15000);
		ifPaymentToolTip.setWidth(300);
		ifPaymentToolTip.setTrackMouse(true);
		ifPaymentToolTip.applyTo(interimFinalPaymentButton);
		/*------------set To Final/Interim Payment-------------------*/
		
		/*--------------------- Refresh---------------------*/
		refreshButton = new ToolbarButton();
		refreshButton.setText("Refresh");
		refreshButton.setIconCls("toggle-button-icon");
		refreshButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				globalSectionController.getTreeSectionController().getPaymentTreePanel().showPaymentCertificateMainPanel(scPackage.getPackageNo());
			}
		});
		
		ToolTip refreshButtonTip = new ToolTip();
		refreshButtonTip.setTitle("Refresh");
		refreshButtonTip.setHtml("Refresh Current Page");
		refreshButtonTip.setCtCls("toolbar-button");
		refreshButtonTip.setDismissDelay(15000);
		refreshButtonTip.setWidth(300);
		refreshButtonTip.setTrackMouse(true);
		refreshButtonTip.applyTo(refreshButton);
		/*--------------------- Refresh---------------------*/
		
		/*---------------------*/
	 
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
		
		paymentToolbarButton.setVisible(false);
		updateButton.setVisible(false);
		submitButton.setVisible(false);
		interimFinalPaymentButton.setVisible(false);
		previewPaymentCertificateButton.setVisible(false);
		
		toolbar.addButton(paymentToolbarButton);
		toolbar.addSeparator();
		toolbar.addButton(previewPaymentCertificateButton);
		toolbar.addSeparator();
		toolbar.addButton(interimFinalPaymentButton);
		toolbar.addSeparator();
		toolbar.addButton(refreshButton);
		toolbar.addSeparator();
		toolbar.addButton(tipsButton);
		toolbar.addFill();
		toolbar.addText("Payment Terms: <b>"+scPackage.getPaymentTerms()+"</b>");
		
		setTopToolbar(toolbar);
	}
	
	/**
	 * 
	 * @author tikywong
	 * refactored on Jul 31, 2013 2:13:40 PM
	 */
	public void populateGrid() {
		dataStore.removeAll();
		//Obtain the full list of payment certificates
		List<SCPaymentCertWithGSTWrapper> scPaymentCertWithGSTWrapperList = scPaymentCertsWrapper.getScPaymentCertWithGSTWrapperList();
		if (scPaymentCertWithGSTWrapperList==null)
			return;
		
		//Set records
		for (SCPaymentCertWithGSTWrapper scPaymentCertWithGSTWrapper:scPaymentCertWithGSTWrapperList) {	
			Record record = paymentRecordDef.createRecord( new Object[] {
							scPaymentCertWithGSTWrapper.getPaymentCertNo(),
							scPaymentCertWithGSTWrapper.getPaymentStatus(), 
							convertPaymentStatusToDescription(scPaymentCertWithGSTWrapper.getPaymentStatus()),
							scPaymentCertWithGSTWrapper.getMainContractPaymentCertNo(),
							scPaymentCertWithGSTWrapper.getDueDate(),
							scPaymentCertWithGSTWrapper.getAsAtDate(),
							scPaymentCertWithGSTWrapper.getScIpaReceivedDate(),
							scPaymentCertWithGSTWrapper.getCertIssueDate(),
							scPaymentCertWithGSTWrapper.getCertAmount(),
							scPaymentCertWithGSTWrapper.getGstPayable(),
							scPaymentCertWithGSTWrapper.getGstReceivable(),
							"F".equals(scPaymentCertWithGSTWrapper.getIntermFinalPayment())? SCPaymentCert.FINAL_PAYMENT:SCPaymentCert.INTERIM_PAYMENT,
							SCPaymentCert.NON_DIRECT_PAYMENT.equals(scPaymentCertWithGSTWrapper.getDirectPayment())? DIRECT_PAYMENT_NO:DIRECT_PAYMENT_YES,
								
							//no display on UI, for reference only
							scPaymentCertWithGSTWrapper.getJobNo(),
							scPaymentCertWithGSTWrapper.getPackageNo(),
							scPackage.getPaymentTerms(),
			});
			dataStore.add(record);
		}
	
		//total line
		Record record = paymentRecordDef.createRecord( new Object[] {
						"",
						"",
						"",
						"",
						"",
						"",
						"",
						"",
						totalCertificateAmount,
						totalGSTPayableAmount,
						totalGSTReceivableAmount,
						"",
						"",
						//no display on UI, for reference only
						"",
						"",
						""
			});
		dataStore.add(record);
		
		interimFinalPaymentButton.setVisible(false);
	
		// Check for access rights - then add tool bar buttons accordingly
		SessionTimeoutCheck.renewSessionTimer();
		userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010205_SCPAYMENTCERTIFICATE_MAINPANEL, new AsyncCallback<List<String>>(){
			public void onSuccess(List<String> accessRightsReturned) {
					accessRightsList = accessRightsReturned;
					securitySetup();
			}
			
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);					
			}
		});
	}
	
	/**
	 * 
	 * @author tikywong
	 * refactored on Aug 8, 2013 5:08:03 PM
	 */
	private void updateAndSubmitPaymentCertificate(final Boolean submitPayment){
		UIUtil.maskPanelById(mainSectionPanel_ID, GlobalParameter.SAVING_MSG, true);
		
		UpdatePaymentCertificateWrapper updatePaymentCertSaveWrapper = new UpdatePaymentCertificateWrapper();
		Record[] records = dataStore.getRecords();
		
		//1. obtain dirty record
		Record record = null;
		for(Record r:records){
			if (r.isDirty()){
				record = r;
				break;
			}
		}
		
		// 2. Update dirty record
		if(record!=null){
			//2a. gather information
			Integer paymentCertNo = null;
			if ((record.getAsString(PAYMENT_CERTIFICATE_NO_RECORD_NAME) != null) && (!"".equals(record.getAsString(PAYMENT_CERTIFICATE_NO_RECORD_NAME).trim()))){		
				paymentCertNo = record.getAsInteger(PAYMENT_CERTIFICATE_NO_RECORD_NAME);
			}	
			Date asAtDate = record.getAsDate(AS_AT_DATE_RECORD_NAME);
			
			Integer parentJobMainContractPaymentCert = null;
			if ((record.getAsString(MAIN_CONTRACT_CERTIFICATE_NO_RECORD_NAME) != null) && (!"".equals(record.getAsString(MAIN_CONTRACT_CERTIFICATE_NO_RECORD_NAME).trim()))){		
				parentJobMainContractPaymentCert = record.getAsInteger(MAIN_CONTRACT_CERTIFICATE_NO_RECORD_NAME)==0?null:record.getAsInteger(MAIN_CONTRACT_CERTIFICATE_NO_RECORD_NAME);
			}
				
			Date dueDate = null;
			if (scPackage.getPaymentTerms().equals("QS0"))
				dueDate = record.getAsDate(DUE_DATE_RECORD_NAME);
			
			Date sclpaReceivedDate = null;
			if(scPackage.getPaymentTerms().equals("QS3") 
			 ||scPackage.getPaymentTerms().equals("QS4") 
			 ||scPackage.getPaymentTerms().equals("QS5") 
			 ||scPackage.getPaymentTerms().equals("QS6") 
			 ||scPackage.getPaymentTerms().equals("QS7"))
				sclpaReceivedDate = record.getAsDate(IPA_OR_INVOICE_RECEIVED_DATE_RECORD_NAME);
			
			Double gstPayable = null;
			Double gstReceivable = null;
			if( job.getDivision().equals("SGP") || job.getJobNumber().startsWith("14")){
				if(!SCPackage.INTERNAL_TRADING.equals(scPackage.getFormOfSubcontract())){
					gstPayable = 	record.getAsObject(GST_PAYABLE_RECORD_NAME)==null?
									new Double(0.00):record.getAsDouble(GST_PAYABLE_RECORD_NAME);
					gstReceivable = record.getAsObject(GST_RECEIVABLE_RECORD_NAME)==null?
									new Double(0.00):record.getAsDouble(GST_RECEIVABLE_RECORD_NAME);
				}
			}
			
			//2b. set wrapper to be updated
			updatePaymentCertSaveWrapper.setJob(job);
			updatePaymentCertSaveWrapper.setUserId(globalSectionController.getUser().getUsername());
			updatePaymentCertSaveWrapper.setJobNumber(job.getJobNumber());
			updatePaymentCertSaveWrapper.setPackageNo(scPackage.getPackageNo());
			updatePaymentCertSaveWrapper.setPaymentCertNo(paymentCertNo);
			updatePaymentCertSaveWrapper.setParentJobMainContractPaymentCert(parentJobMainContractPaymentCert);
			updatePaymentCertSaveWrapper.setAsAtDate(asAtDate);
			updatePaymentCertSaveWrapper.setPaymentTerm(scPackage.getPaymentTerms());
			
			if (dueDate != null)
				updatePaymentCertSaveWrapper.setDueDate(dueDate);
			if(sclpaReceivedDate!=null)
				updatePaymentCertSaveWrapper.setSclpaReceivedDate(sclpaReceivedDate);
			if(gstPayable!=null)
				updatePaymentCertSaveWrapper.setGstPayable(gstPayable);
			if(gstReceivable!=null)
				updatePaymentCertSaveWrapper.setGstReceivable(gstReceivable);
	
			//2c. update the database
			SessionTimeoutCheck.renewSessionTimer();
			paymentRepository.updatePaymentCertificate(updatePaymentCertSaveWrapper, new AsyncCallback<UpdatePaymentCertificateResultWrapper>() {
					public void onSuccess(UpdatePaymentCertificateResultWrapper wrapper) {
						if (wrapper.getIsUpdateSuccess()) {
							dataStore.commitChanges();
							
							//2d. submit payment
							if (submitPayment)
								submitPayment();
							else {
								MessageBox.alert("Payment Certificate has been updated successfully.");
								UIUtil.unmaskPanelById(mainSectionPanel_ID);
								globalSectionController.getTreeSectionController().getPaymentTreePanel().showPaymentCertificateMainPanel(scPackage.getPackageNo());
							}
						}
						// Failed to update the Payment Certificate
						else {
							MessageBox.alert(wrapper.getMessage());
							UIUtil.unmaskPanelById(mainSectionPanel_ID);
						}
					}
					public void onFailure(Throwable e) {
						UIUtil.throwException(e);
						UIUtil.unmaskPanelById(mainSectionPanel_ID);
					}
			});
		}
		
		//3. submit payment (with no data update)
		if (submitPayment) 
			submitPayment();
		else 
			UIUtil.unmaskPanelById(mainSectionPanel_ID);
	}
	
	private void submitPayment(){	
		Record[] records2 = dataStore.getRecords();
		
		boolean isSubmited= false;
		
		for(Record curRecord: records2){
			String status = curRecord.getAsString(PAYMENT_STATUS_RECORD_NAME); 
			
			if("PND".equals(status.trim())){
				Double certAmount = curRecord.getAsString(CERTIFICATE_AMOUNT_RECORD_NAME)!=null && !"".equals(curRecord.getAsString(CERTIFICATE_AMOUNT_RECORD_NAME).trim())? new Double(curRecord.getAsString(CERTIFICATE_AMOUNT_RECORD_NAME).trim()):null;
				Integer paymentCertNo = curRecord.getAsString(PAYMENT_CERTIFICATE_NO_RECORD_NAME)!=null && !"".equals(curRecord.getAsString(PAYMENT_CERTIFICATE_NO_RECORD_NAME).trim())?new Integer(curRecord.getAsString(PAYMENT_CERTIFICATE_NO_RECORD_NAME).trim()):null;
				String userID = globalSectionController.getUser().getUsername().toUpperCase();
				String currentMainCertNo = curRecord.getAsString(MAIN_CONTRACT_CERTIFICATE_NO_RECORD_NAME);
				SessionTimeoutCheck.renewSessionTimer();
				paymentRepository.submitPayment(job.getJobNumber(), Integer.valueOf(scPackage.getPackageNo()), paymentCertNo, certAmount, userID, currentMainCertNo, new AsyncCallback<SubmitPaymentResponseWrapper>(){
					public void onSuccess(SubmitPaymentResponseWrapper result) {
						if(result.getIsUpdated()){
							MessageBox.alert("You have successfully submitted the Subcontract Payment Approval request. <br/> Please Check status via Approval System");
							dataStore.commitChanges();
							globalSectionController.getTreeSectionController().getPaymentTreePanel().showPaymentCertificateMainPanel(globalSectionController.getSelectedPackageNumber());
						}
						else{
							MessageBox.alert("<centre>Fail to submit payment.<br>"+result.getErrorMsg()+"</centre>");
						}
						UIUtil.unmaskPanelById(mainSectionPanel_ID);
					}
					public void onFailure(Throwable e) {		
						UIUtil.throwException(e);
						UIUtil.unmaskPanelById(mainSectionPanel_ID);
					}
				});
				isSubmited = true;
				break;
			}
		}
		if(!isSubmited)
			MessageBox.alert("No Payment pending for submit");
	}

	private void securitySetup(){
		if (accessRightsList.contains("WRITE")){
			paymentToolbarButton.setVisible(true);
			updateButton.setVisible(true);
			submitButton.setVisible(true);
			previewPaymentCertificateButton.setVisible(true);
		}
		
		if (accessRightsList.contains("READ")){
			previewPaymentCertificateButton.setVisible(true);
		}

		UIUtil.unmaskPanelById(mainSectionPanel_ID);		
	}
	
	private void beforeSubmission(){
		submitButton.setDisabled(true);
		
		Record[] records = dataStore.getRecords();
		
		for(int i=0; i< records.length-1; i++) {
			if(records[i].isDirty()) {
				MessageBox.alert("Please save the modified SC Payment Certficate before submission.");
				return;
			}
		}
		
		//Validation 1: No payment if Supplier Master has no information of such subcontractor
		if(supplierMasterWrapper==null){
			MessageBox.alert("Subcontractor: "+scPackage.getVendorNo()+" - Unable to verify its Hold Payment Status. Supplier Master Information doesn't exist. <br/>"+
							 "Please log a helpdesk call @ http://helpdesk");
			submitButton.setDisabled(false);
		}
		//Validation 2: No payment if the subcontractor is being hold payment
		else if(supplierMasterWrapper.getHoldPaymentCode().equals(SCPaymentCert.HOLD_PAYMENT)){
			MessageBox.alert(	"No payment can be submitted due to ALL the payments of Subcontractor: "+supplierMasterWrapper.getAddressNumber()+" are being hold.  <br/>"+
								globalSectionController.getPaymentHoldMessage());
			submitButton.setDisabled(false);
		}	
		//Validation 3: No payment if no SC Payment Certificate is selected
		else if(null == selectedPaymentPaymentStatus){
			MessageBox.alert("Please pick a SC payment!");
			submitButton.setDisabled(false);
		//Validation 4: No payment if payment status = 'APR'
		}else if (selectedPaymentPaymentStatus.trim().equals(SCPaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE)){ 
			MessageBox.alert("Sorry! You cannot submit this SC payment, because it was approved!");
			submitButton.setDisabled(false);
		//Validation 5: No payment if payment status = 'SBM' or 'PCS'
		}else if (selectedPaymentPaymentStatus.trim().equals(SCPaymentCert.PAYMENTSTATUS_SBM_SUBMITTED)|| selectedPaymentPaymentStatus.trim().equals(SCPaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING)){
			MessageBox.alert("Sorry! This payment has been submitted!");
			submitButton.setDisabled(false);
		}else if (selectedPaymentPaymentStatus.trim().equals(SCPaymentCert.PAYMENTSTATUS_PND_PENDING)){
			String jobNumber = globalSectionController.getJob().getJobNumber();
			String packageNo = globalSectionController.getSelectedPackageNumber();
					
			//Warnings & Validations
			if(selectedPaymentCertAmt != null){
				//Warning 1: Total Certificate amount < 0
				if(selectedPaymentCertAmt < 0.0)
					confirmSubmitPayment(jobNumber, packageNo, "The Certificate Amount is less than $0.00.<br/>Do you still wish to submit the payment?");
				//Warning 2: Total Certificate amount = 0
				else if(selectedPaymentCertAmt == 0.0)
					confirmSubmitPayment(jobNumber, packageNo, "The Certificate Amount is $0.00.<br/>Do you still wish to submit payment?");
				//Validation 6: No payment if Addendum Approval is submitted
				else{
					SessionTimeoutCheck.renewSessionTimer();
					globalSectionController.getPackageRepository().getSCPackage(jobNumber, packageNo, new AsyncCallback<SCPackage>(){
						public void onFailure(Throwable e) {
							UIUtil.throwException(e);
						}
						
						public void onSuccess(SCPackage scPackage){
							if (SCPackage.ADDENDUM_NOT_SUBMITTED.equals(scPackage.getSubmittedAddendum())){
								//Fixing for 20110331
								//Validation 7: No payment if Total Movement Amount != Total Amount Due != Certificate Amount
								verifyPaymentAmountBeforeSubmission();
							}else{
								MessageBox.alert("Payment cannot be submitted.<br/>Addendum approval request was submitted.");
								submitButton.setDisabled(false);
							}
						}
					});
				}
			}
			//Validation 8: No payment if Total Certificate Amount = null
			else{
				MessageBox.alert("Certificate Amount cannot be null.");
				submitButton.setDisabled(false);

			}
		}
	}
	
	private void confirmSubmitPayment(final String jobNumber, final String packageNo, final String message){
		MessageBox.confirm("Confirmation", message, new MessageBox.ConfirmCallback(){
			public void execute(String btnID) {
				SessionTimeoutCheck.renewSessionTimer();
				if(btnID.equals("yes"))
					globalSectionController.getPackageRepository().getSCPackage(jobNumber, packageNo, new AsyncCallback<SCPackage>(){
						public void onFailure(Throwable e) {
							UIUtil.throwException(e);
						}

						public void onSuccess(SCPackage result){
							if (SCPackage.ADDENDUM_NOT_SUBMITTED.equals(result.getSubmittedAddendum())){
									//Fixing for 20110331
									//Validation 7: No payment if Total Movement Amount != Total Amount Due != Certificate Amount
									verifyPaymentAmountBeforeSubmission();
							}else{
								MessageBox.alert("Payment cannot be submitted.<br/>Addendum approval request was submitted.");
								submitButton.setDisabled(false);
							}
						}
				});
				else
					submitButton.setDisabled(false);
			}
		});
	}
	
	//Fixing for 20110331
	//Verify if the Total Movement Amount and Total Amount Due match in the Payment Certificate before payment submission
	private void verifyPaymentAmountBeforeSubmission(){	
		//Validation: No payment if Total Movement Amount != Total Amount Due != Certificate Amount
		final String jobNumber = globalSectionController.getJob().getJobNumber();
		final String packageNo = globalSectionController.getSelectedPackageNumber();
		final int paymentCertNo = selectedPaymentCertNo;

		UIUtil.maskPanelById(mainSectionPanel_ID, "Validating the certificate amount...", true);
		SessionTimeoutCheck.renewSessionTimer();
		paymentRepository.getSCPaymentDetailConsolidateView(jobNumber, packageNo, paymentCertNo, new AsyncCallback<PaymentCertViewWrapper>() {
			public void onSuccess(PaymentCertViewWrapper paymentCertViewWrapper) {	
				Double totalRowCertAmountWithGPGR = new Double(selectedPaymentCertAmt+selectedPaymentGSTPayableAmt-selectedPaymentGSTReceivableAmt);
				if(paymentCertViewWrapper==null){
					MessageBox.alert("Please select a payment certificate.");
					UIUtil.unmaskPanelById(mainSectionPanel_ID);
					submitButton.setDisabled(false);
				}
				//Check if Total Movement Amount = Total Amount Due
				//SubMovement5, AmountDueMovement are ensured not null 
				else if(RoundingUtil.round(paymentCertViewWrapper.getSubMovement5().doubleValue(), 2) !=  RoundingUtil.round(paymentCertViewWrapper.getAmountDueMovement().doubleValue(), 2)){
					MessageBox.alert(	"Total Movement Amount: "+RoundingUtil.round(paymentCertViewWrapper.getSubMovement5().doubleValue(), 2)+"</br>"+
										"Total Amount Due: "+RoundingUtil.round(paymentCertViewWrapper.getAmountDueMovement().doubleValue(), 2)+"</br>"+
										"Total Movement Amount and Total Amount Due do not match in the payment certificate.</br>" +
										"Please verify the figures before submitting the payment.");
					UIUtil.unmaskPanelById(mainSectionPanel_ID);
					submitButton.setDisabled(false);
				}
				//Check if Total Certificate Amount = Total Amount Due
				//SubMovement5,AmountDueMovement are ensured to be equal
				else if(RoundingUtil.round(totalRowCertAmountWithGPGR, 2) != RoundingUtil.round(paymentCertViewWrapper.getAmountDueMovement().doubleValue(), 2)){ 
					MessageBox.alert(	"Total Certificate Amount: "+RoundingUtil.round(totalRowCertAmountWithGPGR, 2)+"</br>"+
										"Total Amount Due: "+RoundingUtil.round(paymentCertViewWrapper.getAmountDueMovement().doubleValue(), 2)+"</br>"+
										"Certificate Amount and Total Amount Due do not match in the payment certificate.</br>" +
										"Please verify the figures before submitting the payment.");
					UIUtil.unmaskPanelById(mainSectionPanel_ID);
					submitButton.setDisabled(false);

				}
				//Submit Payment if all validations are passed
				else{
					UIUtil.unmaskPanelById(mainSectionPanel_ID);
					updateAndSubmitPaymentCertificate(new Boolean(true));
					submitButton.setDisabled(false);
				}
			}
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
				submitButton.setDisabled(false);				
			}
		});	
	}
	
	private void interimFinalPayment(){
		UIUtil.maskPanelById(mainSectionPanel_ID, GlobalParameter.CALCULATING_MSG, true);
		String ifFlag="F";
		if (selectedPaymentinterimFinal!=null && SCPaymentCert.FINAL_PAYMENT.equals(selectedPaymentinterimFinal))
			ifFlag="I";
		try {
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getPackageRepository().triggerUpdateSCPaymentDetail(job.getJobNumber(),scPackage.getPackageNo(), ifFlag, globalSectionController.getUser().getUsername(),selectedPaymentDirectPayment, new AsyncCallback<Boolean>(){

				public void onFailure(Throwable e) {
					UIUtil.alert(e.getLocalizedMessage());
					UIUtil.unmaskMainPanel();
				}

				public void onSuccess(Boolean updateSucces) {
					if (updateSucces)
						globalSectionController.getTreeSectionController().getPaymentTreePanel().showPaymentCertificateMainPanel(scPackage.getPackageNo());
					UIUtil.unmaskMainPanel();
				}
				
			});
		} catch (NumberFormatException e1) {
			UIUtil.alert("Package Number Invalid!");
			e1.printStackTrace();
		} catch (Exception e1) {
			UIUtil.alert(e1.getLocalizedMessage());
			e1.printStackTrace();
		}
	}
	
	/**
	 * @author koeyyeung
	 * created on 22 Sep, 2013
	 * Obtain paid Main Cert Nos for QS1 && QS2
	 * **/
	
	public void obtainMainCertStore(){
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMainContractCertificateRepository().obtainPaidMainCertList(job.getJobNumber(), new AsyncCallback<List<Integer>>() {
			public void onSuccess(List<Integer> paidMainCertList) {
				mainCertStore.removeAll();
				if (paidMainCertList==null) return;
				RecordDef materialTypeRecordDef = new RecordDef(new FieldDef[]{new StringFieldDef(MAIN_CERT_VALUE),
																			   new StringFieldDef(MAIN_CERT_DISPLAY)});
				mainCerts = new String[paidMainCertList.size()][2];
								
				for(int i = 0; i < paidMainCertList.size(); i++){
					mainCerts[i][0] = paidMainCertList.get(i).toString();
					mainCerts[i][1] = paidMainCertList.get(i).toString();
					mainCertStore.add(materialTypeRecordDef.createRecord(mainCerts[i]));
				}
				mainCertEditorGridComboBox.setListWidth(150);
				mainCertStore.commitChanges();
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);	
			}
		});
	}
	
	/**
	 * 
	 * @author tikywong
	 * created on Jul 5, 2012 2:43:39 PM
	 * To convert Payment Status from PND, SBM, UFR, PCS or APR to detail description
	 */
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

	public String getScreenName() {
		return screenName;
	}
}
