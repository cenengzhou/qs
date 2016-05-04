package com.gammon.qs.client.ui.panel.mainSection;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.gammon.qs.application.User;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.JobRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.renderer.AmountRendererNonTotal;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.RoundingUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertEnquiryResultWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertEnquirySearchingWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
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
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowNumberingColumnConfig;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

/**
 * Paul Yiu
 * modified on 2015-09-01
 * Change from search panel to toolbar 
 */
public class MainCertEnquiryPanel extends Panel {

	private static final String MAIN_PANEL_ID = "MainCertEnquiry_ID";
	private final GlobalSectionController globalSectionController;
	private GridPanel resultPanel;
	private Store dataStore;

	private static final String[] COMPANY = new String[]{"company","Company"};
	private static final String[] DIVISION = new String[]{"division","Division"};
	
	private static final String[] JOB_NUMBER = new String[]{"jobNumber","Job Number"};
	private static final String[] CERTIFICATE_NUMBER = new String[]{"certificateNumber","Certificate No."};
	private static final String[] CLIENT_CERT_NO = new String[]{"clientCertNo","Client Cert No."};
	private static final String[] APPLIED_MAIN_CONTRACTOR_AMOUNT = new String[]{"appliedMainContractorAmount"};
	private static final String[] APPLIED_NSCNDSC_AMOUNT =  new String[]{"appliedNSCNDSCAmount"};
	private static final String[] APPLIED_MOS_AMOUNT = new String[]{"appliedMOSAmount"};
	private static final String[] APPLIED_MAIN_CONTRACTOR_RETENTION = new String[]{"appliedMainContractorRetention"};
	private static final String[] APPLIED_MAIN_CONTRACTOR_RETENTION_RELEASED = new String[]{"appliedMainContractorRetentionReleased"};
	private static final String[] APPLIED_RETENTION_FOR_NSCNDSC = new String[]{"appliedRetentionforNSCNDSC"};
	private static final String[] APPLIED_RETENTION_FOR_NSCNDSC_RELEASED = new String[]{"appliedRetentionforNSCNDSCReleased"};
	private static final String[] APPLIED_MOS_RETENTION= new String[]{"appliedMOSRetention"};
	private static final String[] APPLIED_MOS_RETENTION_RELEASED = new String[]{"appliedMOSRetentionReleased"};
	private static final String[] APPLIED_CONTRA_CHARGE_AMOUNT = new String[]{"appliedContraChargeAmount"};
	private static final String[] APPLIED_ADJUSTMENT_AMOUNT = new String[]{"appliedAdjustmentAmount"};
	private static final String[] APPLIED_ADVANCE_PAYMENT = new String[]{"appliedAdvancePayment"};
	private static final String[] APPLIED_CPF_AMOUNT = new String[]{"appliedCPFAmount"};
	private static final String[] IPA_SUBMISSION_DATE =	new String[]{"ipaSubmissionDate"};
	private static final String[] IPD_SENTOUT_DATE= new String[]{"ipaSentoutDate"};
	private static final String[] CERTIFIED_MAIN_CONTRACTOR_AMOUNT = new String[]{"certifiedMainContractorAmount","Main Contractor Amount"};
	private static final String[] CERTIFIED_NSCNDSC_AMOUNT = new String[]{"certifiedNSCNDSCAmount","NSC/NDSC Amount"};
	private static final String[] CERTIFIED_MOS_AMOUNT = new String[]{"certifiedMOSAmount","MOS Amount"};
	private static final String[] CERTIFIED_MAIN_CONTRACTOR_RETENTION = new String[]{"certifiedMainContractorRetention","Retention"};
	private static final String[] CERTIFIED_MAIN_CONTRACTOR_RETENTION_RELEASED = new String[]{"certifiedMainContractorRetentionReleased","Retention Released"};
	private static final String[] CERTIFIED_RETENTION_FOR_NSCNDSC = new String[]{"certifiedRetentionforNSCNDSC","Retention for NSC/NDSC"};
	private static final String[] CERTIFIED_RETENTION_FOR_NSCNDSC_RELEASED = new String[]{"certifiedRetentionforNSCNDSCReleased","Retention for NSC/NDSC Released"};
	private static final String[] CERTIFIED_MOS_RETENTION = new String[]{"certifiedMOSRetention","MOS Retention"};
	private static final String[] CERTIFIED_MOS_RETENTION_RELEASED = new String[]{"certifiedMOSRetentionReleased","MOS Retention Released"};
	private static final String[] CERTIFIED_CONTRA_CHARGE_AMOUNT = new String[]{"certifiedContraChargeAmount","Contra Charge Amount"};
	private static final String[] CERTIFIED_ADJUSTMENT_AMOUNT = new String[]{"certifiedAdjustmentAmount","Adjustment Amount"};
	private static final String[] CERTIFIED_ADVANCE_PAYMENT = new String[]{"certifiedAdvancePayment","Advanced Payment"};
	private static final String[] CERTIFIED_CPF_AMOUNT = new String[]{"certifiedCPFAmount","CPF Amount"};
	private static final String[] GST_RECEIVABLE = new String[]{"gstReceivable","GST Amount"};
	private static final String[] GST_PAYABLE = new String[]{"gstPayable","GST for Contra Charge"};
	private static final String[] CERTIFICATE_STATUS = new String[]{"certificateStatus","Certificate Status"};
	private static final String[] CERTIFICATE_STATUS_DESC = new String[]{"certificateStatusDescription","Certificate Status Description"};
	private static final String[] AR_DOC_NUMBER = new String[]{"arDocNumber"};
	private static final String[] CERTIFICATE_ISSUE_DATE = new String[]{"certificateIssueDate","Certificate Issue Date"};
	private static final String[] AS_AT_DATE = new String[]{"asAtDate","As At Date"};
	private static final String[] CERTIFICATE_STATUS_CHANGE_DATE = new String[]{"certificateStatusChangeDate","Certificate Status Change Date"};
	private static final String[] CERTIFICATE_DUE_DATE = new String[]{"certificateDueDate","Certificate Due Date"};
	private static final String[] REMARK = new String[]{"remark","Remark"};
	private static final String[] NET_MOVEMENT_AMOUNT = new String[]{"Net Movement Amount"};
	private static final String[] NET_GST_RECEIVABLE_MOVEMENT_AMOUNT = new String[]{"Net GST Receivable Movement Amount"};
	private static final String[] NET_GST_PAYABLE_MOVEMENT_AMOUNT = new String[]{"Net GST Payable Movement Amount"};
	private static final String[] ACTUAL_RECEIPT_DATE = new String[]{"Actual Receipt Date","Actual Receipt Date"};

	private final RecordDef recordDef = new RecordDef(
			new FieldDef[] {	
					new StringFieldDef(COMPANY[0]),
					new StringFieldDef(DIVISION[0]),
					new StringFieldDef(JOB_NUMBER[0]),
					new StringFieldDef(CERTIFICATE_NUMBER[0]),
					new StringFieldDef(CLIENT_CERT_NO[0]),
					new StringFieldDef(APPLIED_MAIN_CONTRACTOR_AMOUNT[0]),
					new StringFieldDef(APPLIED_NSCNDSC_AMOUNT[0]),
					new StringFieldDef(APPLIED_MOS_AMOUNT[0]),
					new StringFieldDef(APPLIED_MAIN_CONTRACTOR_RETENTION[0]),
					new StringFieldDef(APPLIED_MAIN_CONTRACTOR_RETENTION_RELEASED[0]),
					new StringFieldDef(APPLIED_RETENTION_FOR_NSCNDSC[0]),
					new StringFieldDef(APPLIED_RETENTION_FOR_NSCNDSC_RELEASED[0]),
					new StringFieldDef(APPLIED_MOS_RETENTION[0]),
					new StringFieldDef(APPLIED_MOS_RETENTION_RELEASED[0]),
					new StringFieldDef(APPLIED_CONTRA_CHARGE_AMOUNT[0]),
					new StringFieldDef(APPLIED_ADJUSTMENT_AMOUNT[0]),
					new StringFieldDef(APPLIED_ADVANCE_PAYMENT[0]),
					new StringFieldDef(APPLIED_CPF_AMOUNT[0]),
					new StringFieldDef(IPA_SUBMISSION_DATE[0]),
					new StringFieldDef(IPD_SENTOUT_DATE[0]),
					new StringFieldDef(CERTIFIED_MAIN_CONTRACTOR_AMOUNT[0]),
					new StringFieldDef(CERTIFIED_NSCNDSC_AMOUNT[0]),
					new StringFieldDef(CERTIFIED_MOS_AMOUNT[0]),
					new StringFieldDef(CERTIFIED_MAIN_CONTRACTOR_RETENTION[0]),
					new StringFieldDef(CERTIFIED_MAIN_CONTRACTOR_RETENTION_RELEASED[0]),
					new StringFieldDef(CERTIFIED_RETENTION_FOR_NSCNDSC[0]),
					new StringFieldDef(CERTIFIED_RETENTION_FOR_NSCNDSC_RELEASED[0]),
					new StringFieldDef(CERTIFIED_MOS_RETENTION[0]),
					new StringFieldDef(CERTIFIED_MOS_RETENTION_RELEASED[0]),
					new StringFieldDef(CERTIFIED_CONTRA_CHARGE_AMOUNT[0]),
					new StringFieldDef(CERTIFIED_ADJUSTMENT_AMOUNT[0]),
					new StringFieldDef(CERTIFIED_ADVANCE_PAYMENT[0]),
					new StringFieldDef(CERTIFIED_CPF_AMOUNT[0]),
					new StringFieldDef(GST_RECEIVABLE[0]),
					new StringFieldDef(GST_PAYABLE[0]),
					new StringFieldDef(CERTIFICATE_STATUS[0]),
					new StringFieldDef(CERTIFICATE_STATUS_DESC[0]),
					new StringFieldDef(AR_DOC_NUMBER[0]),
					new StringFieldDef(CERTIFICATE_ISSUE_DATE[0]),
					new StringFieldDef(AS_AT_DATE[0]),
					new StringFieldDef(CERTIFICATE_STATUS_CHANGE_DATE[0]),
					new StringFieldDef(CERTIFICATE_DUE_DATE[0]),
					new StringFieldDef(REMARK[0]),
					new StringFieldDef(NET_MOVEMENT_AMOUNT[0]),
					new StringFieldDef(NET_GST_RECEIVABLE_MOVEMENT_AMOUNT[0]),
					new StringFieldDef(NET_GST_PAYABLE_MOVEMENT_AMOUNT[0]),
					new StringFieldDef(ACTUAL_RECEIPT_DATE[0])
			});

	private PaginationToolbar paginationToolbar;
	
	private ComboBox companyComboBox= new ComboBox();
	private ComboBox divisionComboBox= new ComboBox();
	private ComboBox jobNoComboBox= new ComboBox();
	private ComboBox statusComboBox= new ComboBox();
	//private ComboBox statusComboBox= FieldFactory.createComboBox();
	
	private static final String COMPANY_VALUE = "companyValue";
	private static final String COMPANY_DISPLAY = "companyDisplay";
	private static final String DIVISION_VALUE = "divisionValue";
	private static final String DIVISION_DISPLAY = "divisionDisplay";
	private static final String JOB_NO_VALUE = "jobNoValue";
	private static final String JOB_NO_DISPLAY = "JobNoDisplay";
	private static final String STATUS_VALUE = "statusValue";
	private static final String STATUS_DISPLAY = "statusDisplay";
	
	private String[][] companys = new String[][] {};
	private String[][] divisions = new String[][] {};
	private String[][] jobNos = new String[][] {};
	private String[][] status = new String[][]{new String[]{"","All"}, new String[]{"Y","Y"}, new String[]{"N","N"}};
	
	private MainContractCertEnquirySearchingWrapper wrapper = new MainContractCertEnquirySearchingWrapper();
	
	private Store companyStore = new SimpleStore(new String[] { COMPANY_VALUE, COMPANY_DISPLAY }, companys);
	private Store divisionStore = new SimpleStore(new String[] { DIVISION_VALUE, DIVISION_DISPLAY }, divisions);
	private Store jobNoStore = new SimpleStore(new String[] { JOB_NO_VALUE, JOB_NO_DISPLAY }, jobNos);
	private Store statusStore = new SimpleStore(new String[] { STATUS_VALUE, STATUS_DISPLAY }, status);
	private List<String> divisionSuggestList = new ArrayList<String>();
	private Item downloadMainCertExcelButton;
	private Item downloadMainCertPDFButton;
	private Item downloadContractReceivalbleExcelButton;
	private Item downloadContractReceivalblePDFButton;
	private ToolbarButton mainContractCertReportToolbarButton;
	private ToolbarButton contractReceivalbleReportToolbarButton;
	private ToolbarButton searchButton;
	private JobRepositoryRemoteAsync jobRepository;
	private User user;
	private GlobalMessageTicket globalMessageTicket;
	private ToolbarTextItem totalProjectedValueTextItem;
	private ToolbarTextItem totalEstimatedRetentionTextItem;
	private ToolbarTextItem totalCumulativeReceivableTextItem;
	private ToolbarTextItem totalReceiptAmountTextItem;
	private ToolbarTextItem totalOutstatndingReceivableTextItem;
	private ToolbarTextItem totalRecordsTextItem;

	private static String FILE_TYPE_XLS = "xls";
	private static String FILE_TYPE_PDF = "pdf";
	
	public MainCertEnquiryPanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;

		globalMessageTicket = new GlobalMessageTicket();
		user = globalSectionController.getUser();

		jobRepository = globalSectionController.getJobRepository();
		setupUI();
	}

	private void setupUI() {
		setLayout(new RowLayout());
		setPaddings(0);

		obtainCompanyStore();
		obtainDivisionStore();
		obtainJobNoStore();
		obtainStatusStore();
		setupToolbar();
		setupGridPanel();
		setId(MAIN_PANEL_ID);

		// hide detail panel
		globalSectionController.getDetailSectionController().getMainPanel().collapse();
	}

	private void setupToolbar() {
		Toolbar toolbar = new Toolbar();
		FieldListener searchListener = new FieldListenerAdapter() {
			public void onSpecialKey(	Field field, EventObject e) {
				if (e.getKey() == EventObject.ENTER) {
					search();
				}
			}
			@Override
			public void onChange(Field field, Object newVal, Object oldVal) {
				if (field.getName().equals("companyComboBox")){
					divisionComboBox.setValue("");
					jobNoComboBox.setValue("");
				}
				if(field.getName().equals("divisionComboBox")){
					jobNoComboBox.setValue("");
				}
				super.onChange(field, newVal, oldVal);
			}
		};
	
		toolbar.addText("Company:");
		toolbar.addSpacer();
		companyComboBox.setForceSelection(false);
		companyComboBox.setMinChars(1);
		companyComboBox.setName("companyComboBox");
		companyComboBox.setDisplayField(COMPANY_DISPLAY);
		companyComboBox.setMode(ComboBox.LOCAL);
		companyComboBox.setTriggerAction(ComboBox.ALL);
		companyComboBox.setEmptyText("ALL");
		companyComboBox.setTypeAhead(false);
		companyComboBox.setSelectOnFocus(true);
		companyComboBox.setWidth(100);
		companyComboBox.setListWidth(100);
		companyComboBox.addListener(searchListener);
		companyComboBox.setStore(companyStore);
		companyComboBox.setValidator(numberOnlyValidator());
		ToolTip companyComboBoxToolTip = new ToolTip();
		companyComboBoxToolTip.setTitle("Company:");
		companyComboBoxToolTip.setHtml("Enter Company No. or <br>choose one from the list<br>Restriction: Number Only<br>Filter: startWith<br>eg: search '0102' will return '01021' and '01022'");
		companyComboBoxToolTip.applyTo(companyComboBox);
		toolbar.addField(companyComboBox);
		toolbar.addSpacer();
		
		toolbar.addText("Division:");
		toolbar.addSpacer();
		divisionComboBox.setName("divisionComboBox");
		divisionComboBox.setForceSelection(false);
		divisionComboBox.setMinChars(1);
		divisionComboBox.setDisplayField(DIVISION_DISPLAY);
		divisionComboBox.setMode(ComboBox.LOCAL);
		divisionComboBox.setTriggerAction(ComboBox.ALL);
		divisionComboBox.setEmptyText("ALL");
		divisionComboBox.setTypeAhead(false);
		divisionComboBox.setSelectOnFocus(true);
		divisionComboBox.setWidth(100);
		divisionComboBox.setListWidth(100);
		divisionComboBox.addListener(searchListener);
		divisionComboBox.setStore(divisionStore);
		ToolTip divisionComboBoxToolTip = new ToolTip();
		divisionComboBoxToolTip.setTitle("Division:");
		divisionComboBoxToolTip.setHtml("Enter Division or <br>choose one from the list<br>Filter: Match anywhere<br>eg: search 'm' will return 'E&M'");
		divisionComboBoxToolTip.applyTo(divisionComboBox);
		toolbar.addField(divisionComboBox);
		toolbar.addSpacer();	
	
		toolbar.addText("Job No.:");
		toolbar.addSpacer();
		jobNoComboBox.setForceSelection(false);
		jobNoComboBox.setMinChars(1);
		jobNoComboBox.setDisplayField(JOB_NO_DISPLAY);
		jobNoComboBox.setMode(ComboBox.LOCAL);
		jobNoComboBox.setTriggerAction(ComboBox.ALL);
		jobNoComboBox.setEmptyText("ALL");
		jobNoComboBox.setTypeAhead(true);
		jobNoComboBox.setSelectOnFocus(true);
		jobNoComboBox.setWidth(100);
		jobNoComboBox.setListWidth(100);
		jobNoComboBox.addListener(searchListener);
		jobNoComboBox.setStore(jobNoStore);
		if(globalSectionController.getJob()!=null)
			jobNoComboBox.setValue(globalSectionController.getJob().getJobNumber());
		//jobNoComboBox.setDisabled(true);
		
		ToolTip jobNoComboBoxToolTip = new ToolTip();
		jobNoComboBoxToolTip.setTitle("Job No.:");
		jobNoComboBoxToolTip.setHtml("Enter Job No. or <br>choose one from the list<br>Filter: Exactly<br>eg: search '13389' will return '13389' only");
		jobNoComboBoxToolTip.applyTo(jobNoComboBox);
		toolbar.addField(jobNoComboBox);

		toolbar.addText("Status:");
		toolbar.addSpacer();
		statusComboBox.setForceSelection(true);
		statusComboBox.setMinChars(1);
		statusComboBox.setDisplayField(STATUS_DISPLAY);
		statusComboBox.setMode(ComboBox.LOCAL);
		statusComboBox.setTriggerAction(ComboBox.ALL);
		statusComboBox.setEmptyText("ALL");
		statusComboBox.setTypeAhead(true);
		statusComboBox.setSelectOnFocus(true);
		statusComboBox.setWidth(230);
		statusComboBox.setListWidth(230);
		statusComboBox.addListener(searchListener);
		//statusComboBox.setStore(jobNoStore);
		statusComboBox.setStore(statusStore);
		statusComboBox.setDisabled(false);
		/*
		statusComboBox.setDisplayField(STATUS_DISPLAY);
		statusComboBox.setValueField(STATUS_VALUE);
		statusComboBox.setStore(statusStore);
		*/
		ToolTip statusComboBoxToolTip = new ToolTip();
		statusComboBoxToolTip.setTitle("Status:");
		statusComboBoxToolTip.setHtml("Enter status or <br>choose one from the list<br>Filter: Exactly<br>Optional: Yes<br>eg: search 'All' will return status of Yes and No only");
		statusComboBoxToolTip.applyTo(statusComboBox);
		toolbar.addField(statusComboBox);
		toolbar.addSeparator();	

		searchButton = new ToolbarButton("Search");
		searchButton.setIconCls("find-icon");
		searchButton.addListener(searchButtonListener());
		
		ToolTip searchButtonToolTip = new ToolTip();
		searchButtonToolTip.setTitle("Search:");
		searchButtonToolTip.setHtml("Press to search");
		searchButtonToolTip.applyTo(searchButton);
		searchButton.setVisible(false);
		toolbar.addButton(searchButton);
		toolbar.addSeparator();
		
		//Main Contract Certificate Report
		downloadMainCertExcelButton = new Item("Export to Excel");
		downloadMainCertExcelButton.setIconCls("excel-icon");
		downloadMainCertExcelButton.addListener(downloadListener(GlobalParameter.MAIN_CERTIFICATE_ENQUIRY_URL, FILE_TYPE_XLS));

		downloadMainCertPDFButton = new Item("Export to PDF");
		downloadMainCertPDFButton.setIconCls("pdf-icon");
		downloadMainCertPDFButton.addListener(downloadListener(GlobalParameter.MAIN_CERTIFICATE_ENQUIRY_URL, FILE_TYPE_PDF));
		
		Menu mainCertReportMenu = new Menu();
		mainCertReportMenu.addItem(downloadMainCertExcelButton);
		mainCertReportMenu.addItem(downloadMainCertPDFButton);
		mainContractCertReportToolbarButton = new ToolbarButton("Main Contract Certificate Report");
		mainContractCertReportToolbarButton.setMenu(mainCertReportMenu);
		mainContractCertReportToolbarButton.setIconCls("menu-show-icon");
		mainContractCertReportToolbarButton.setVisible(false);
		ToolTip mainContractCertReportToolbarButtonToolTip = new ToolTip();
		mainContractCertReportToolbarButtonToolTip.setTitle("Report export:");
		mainContractCertReportToolbarButtonToolTip.setHtml("Choose report type and click to download");
		mainContractCertReportToolbarButtonToolTip.applyTo(mainContractCertReportToolbarButton);
		toolbar.addButton(mainContractCertReportToolbarButton);
		toolbar.addSeparator();
		
		//Contract Receivable Settlement Report
		downloadContractReceivalbleExcelButton = new Item("Export to Excel");
		downloadContractReceivalbleExcelButton.setIconCls("excel-icon");
		downloadContractReceivalbleExcelButton.addListener(downloadListener(GlobalParameter.CONTRACT_RECEIVABLE_ENQUIRY_URL, FILE_TYPE_XLS));

		downloadContractReceivalblePDFButton = new Item("Export to PDF");
		downloadContractReceivalblePDFButton.setIconCls("pdf-icon");
		downloadContractReceivalblePDFButton.addListener(downloadListener(GlobalParameter.CONTRACT_RECEIVABLE_ENQUIRY_URL, FILE_TYPE_PDF));
		
		
		Menu contractReceivableReportMenu = new Menu();
		contractReceivableReportMenu.addItem(downloadContractReceivalbleExcelButton);
		contractReceivableReportMenu.addItem(downloadContractReceivalblePDFButton);
		contractReceivalbleReportToolbarButton  = new ToolbarButton("Contract Receivable Settlement Report");
		contractReceivalbleReportToolbarButton.setMenu(contractReceivableReportMenu);
		contractReceivalbleReportToolbarButton.setIconCls("menu-show-icon");
		contractReceivalbleReportToolbarButton.setVisible(false);
		ToolTip contractReceivableReportToolbarButtonToolTip = new ToolTip();
		contractReceivableReportToolbarButtonToolTip.setTitle("Report export:");
		contractReceivableReportToolbarButtonToolTip.setHtml("Choose report type and click to download");
		contractReceivableReportToolbarButtonToolTip.applyTo(contractReceivalbleReportToolbarButton);
		toolbar.addButton(contractReceivalbleReportToolbarButton);
		toolbar.addSeparator();
		
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getUserAccessRightsRepository().getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010807_MAIN_CERTIFICATE_ENQUIRY_MAINPANEL, new AsyncCallback<List<String>>() {
			public void onSuccess(	List<String> accessRightList) {
				if (accessRightList.contains("READ")) {
					searchButton.setVisible(true);
					mainContractCertReportToolbarButton.setVisible(true);
				}
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getUserAccessRightsRepository().getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.R010801_CONTRACT_RECEIVABLE_SETTLEMENT_REPORT, new AsyncCallback<List<String>>() {
					public void onFailure(Throwable e) {
						UIUtil.throwException(e);
					}
					public void onSuccess(List<String> accessRightList) {
						if (accessRightList.contains("READ"))
							contractReceivalbleReportToolbarButton.setVisible(true);
					}
				});
			}

			public void onFailure(	Throwable e) {
				UIUtil.throwException(e);
				UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
			}
		});
		setTopToolbar(toolbar);
	}

	private Validator numberOnlyValidator() {
		return new Validator(){
			public boolean validate(String value) throws ValidationException {
				if(!value.matches("^[0-9\\*]*$")){
					MessageBox.alert("Please input number only");
					return false;
				}
				return true;
			}
		};
	}

	private void setupGridPanel() {
		Panel bottomPanel = new Panel();
		resultPanel = new GridPanel();
		Renderer amountRender = new AmountRendererNonTotal(globalSectionController.getUser());

		ColumnConfig JobNumberColumnConfig = new ColumnConfig(JOB_NUMBER[1],JOB_NUMBER[0],85,false);
		ColumnConfig companyColumnConfig = new ColumnConfig(COMPANY[1],COMPANY[0],85,false);
		ColumnConfig divisionColumnConfig = new ColumnConfig(DIVISION[1],DIVISION[0],85,false);
		ColumnConfig certificateNumberColumnConfig = new ColumnConfig(CERTIFICATE_NUMBER[1],CERTIFICATE_NUMBER[0],85,false);
		
		ColumnConfig clientCertNoColumnConfig = new ColumnConfig(CLIENT_CERT_NO[1], CLIENT_CERT_NO[0], 80, false);
		
		ColumnConfig certifiedMainContractorAmountColumnConfig = new ColumnConfig(CERTIFIED_MAIN_CONTRACTOR_AMOUNT[1],CERTIFIED_MAIN_CONTRACTOR_AMOUNT[0],130,false); 
		certifiedMainContractorAmountColumnConfig.setAlign(TextAlign.RIGHT);
		
		ColumnConfig certifiedNSCNDSCAmountColumnConfig = new ColumnConfig(CERTIFIED_NSCNDSC_AMOUNT[1],CERTIFIED_NSCNDSC_AMOUNT[0],100,false); 
		certifiedNSCNDSCAmountColumnConfig.setAlign(TextAlign.RIGHT);
		
		ColumnConfig certifiedMOSAmountColumnConfig = new ColumnConfig(CERTIFIED_MOS_AMOUNT[1],CERTIFIED_MOS_AMOUNT[0],120,false); 
		certifiedMOSAmountColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedMainContractorRetentionColumnConfig = new ColumnConfig(CERTIFIED_MAIN_CONTRACTOR_RETENTION[1],CERTIFIED_MAIN_CONTRACTOR_RETENTION[0],120,false);
		certifiedMainContractorRetentionColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedMainContractorRetentionReleasedColumnConfig = new ColumnConfig(CERTIFIED_MAIN_CONTRACTOR_RETENTION_RELEASED[1],CERTIFIED_MAIN_CONTRACTOR_RETENTION_RELEASED[0],120,false); 
		certifiedMainContractorRetentionReleasedColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedRetentionforNSCNDSCColumnConfig = new ColumnConfig(CERTIFIED_RETENTION_FOR_NSCNDSC[1],CERTIFIED_RETENTION_FOR_NSCNDSC[0],125,false); 
		certifiedRetentionforNSCNDSCColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedRetentionforNSCNDSCReleasedColumnConfig = new ColumnConfig(CERTIFIED_RETENTION_FOR_NSCNDSC_RELEASED[1],CERTIFIED_RETENTION_FOR_NSCNDSC_RELEASED[0],140,false); 
		certifiedRetentionforNSCNDSCReleasedColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedMOSRetentionColumnConfig = new ColumnConfig(CERTIFIED_MOS_RETENTION[1],CERTIFIED_MOS_RETENTION[0],120,false); 
		certifiedMOSRetentionColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedMOSRetentionReleasedColumnConfig = new ColumnConfig(CERTIFIED_MOS_RETENTION_RELEASED[1],CERTIFIED_MOS_RETENTION_RELEASED[0],130,false); 
		certifiedMOSRetentionReleasedColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedContraChargeAmountColumnConfig = new ColumnConfig(CERTIFIED_CONTRA_CHARGE_AMOUNT[1],CERTIFIED_CONTRA_CHARGE_AMOUNT[0],120,false); 
		certifiedContraChargeAmountColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedAdjustmentAmountColumnConfig = new ColumnConfig(CERTIFIED_ADJUSTMENT_AMOUNT[1],CERTIFIED_ADJUSTMENT_AMOUNT[0],110,false);
		certifiedAdjustmentAmountColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedAdvancePaymentColumnConfig = new ColumnConfig(CERTIFIED_ADVANCE_PAYMENT[1],CERTIFIED_ADVANCE_PAYMENT[0],110,false);
		certifiedAdvancePaymentColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certifiedCPFAmountColumnConfig = new ColumnConfig(CERTIFIED_CPF_AMOUNT[1],CERTIFIED_CPF_AMOUNT[0],150,false); 
		certifiedCPFAmountColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig gstReceivableColumnConfig = new ColumnConfig(GST_RECEIVABLE[1],GST_RECEIVABLE[0],120,false);
		gstReceivableColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig gstPayableColumnConfig = new ColumnConfig(GST_PAYABLE[1],GST_PAYABLE[0],120,false);
		gstPayableColumnConfig.setAlign(TextAlign.RIGHT);

		ColumnConfig certificateStatusColumnConfig = new ColumnConfig(CERTIFICATE_STATUS[1],CERTIFICATE_STATUS[0],100,false); 
		
		ColumnConfig certificateStatusDescColumnConfig = new ColumnConfig(CERTIFICATE_STATUS_DESC[1],CERTIFICATE_STATUS_DESC[0],220,false);
		
		//ColumnConfig arDocNumberColumnConfig = new ColumnConfig("AR Doc No.",AR_DOC_NUMBER,80,false); 

		ColumnConfig certificateIssueDateColumnConfig = new ColumnConfig(CERTIFICATE_ISSUE_DATE[1],CERTIFICATE_ISSUE_DATE[0],95,false); 
		
		ColumnConfig asAtDateColumnConfig = new ColumnConfig(AS_AT_DATE[1],AS_AT_DATE[0],75,false); 
		
		ColumnConfig certificateStatusChangeDateColumnConfig = new ColumnConfig(CERTIFICATE_STATUS_CHANGE_DATE[1],CERTIFICATE_STATUS_CHANGE_DATE[0],150,false); 
		
		ColumnConfig certificateDueDateColumnConfig = new ColumnConfig(CERTIFICATE_DUE_DATE[1],CERTIFICATE_DUE_DATE[0],120,false); 

		ColumnConfig actualReceiptDateColumnConfig = new ColumnConfig(ACTUAL_RECEIPT_DATE[1],ACTUAL_RECEIPT_DATE[0],95,false); 
		
		ColumnConfig remarkColumnConfig = new ColumnConfig(REMARK[1], REMARK[0], 400, false);
		
		certifiedMainContractorAmountColumnConfig.setRenderer(amountRender);
		certifiedNSCNDSCAmountColumnConfig.setRenderer(amountRender);
		certifiedMOSAmountColumnConfig.setRenderer(amountRender);
		certifiedMainContractorRetentionColumnConfig.setRenderer(amountRender);
		certifiedMainContractorRetentionReleasedColumnConfig.setRenderer(amountRender);
		certifiedRetentionforNSCNDSCColumnConfig.setRenderer(amountRender);
		certifiedRetentionforNSCNDSCReleasedColumnConfig.setRenderer(amountRender);
		certifiedMOSRetentionColumnConfig.setRenderer(amountRender);
		certifiedMOSRetentionReleasedColumnConfig.setRenderer(amountRender);
		certifiedContraChargeAmountColumnConfig.setRenderer(amountRender);
		certifiedAdjustmentAmountColumnConfig.setRenderer(amountRender);
		certifiedAdvancePaymentColumnConfig.setRenderer(amountRender);
		certifiedCPFAmountColumnConfig.setRenderer(amountRender);
		gstReceivableColumnConfig.setRenderer(amountRender);
		gstPayableColumnConfig.setRenderer(amountRender);
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(recordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		resultPanel.setWidth(1400);
		resultPanel.setStore(dataStore);
		BaseColumnConfig[] columns = new BaseColumnConfig[] {
				new RowNumberingColumnConfig(),
				companyColumnConfig,
				divisionColumnConfig,
				JobNumberColumnConfig,
				certificateNumberColumnConfig,
				clientCertNoColumnConfig,
				certifiedMainContractorAmountColumnConfig,
				certifiedNSCNDSCAmountColumnConfig,
				certifiedMOSAmountColumnConfig,
				certifiedMainContractorRetentionColumnConfig,
				certifiedMainContractorRetentionReleasedColumnConfig,
				certifiedRetentionforNSCNDSCColumnConfig,
				certifiedRetentionforNSCNDSCReleasedColumnConfig,
				certifiedMOSRetentionColumnConfig,
				certifiedMOSRetentionReleasedColumnConfig,
				certifiedContraChargeAmountColumnConfig,
				certifiedAdjustmentAmountColumnConfig,
				certifiedAdvancePaymentColumnConfig,
				certifiedCPFAmountColumnConfig,
				gstReceivableColumnConfig,
				gstPayableColumnConfig,
				certificateStatusColumnConfig,
				certificateStatusDescColumnConfig,
				//arDocNumberColumnConfig,
				certificateIssueDateColumnConfig,
				asAtDateColumnConfig,
				certificateStatusChangeDateColumnConfig,
				certificateDueDateColumnConfig,
				actualReceiptDateColumnConfig,
				remarkColumnConfig
		};
		resultPanel.setColumnModel(new ColumnModel(columns));
		GridView view = new GridView();
		view.setForceFit(false);
		resultPanel.setView(view);
		resultPanel.enable();
		bottomPanel.setLayout(new FitLayout());
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){

			public void goToPage(int pageNo){
				UIUtil.maskPanelById(getId(), GlobalParameter.LOADING_MSG, true);
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getMainContractCertificateRepository().obtainMainContractCertificateList(wrapper, pageNo, new AsyncCallback<MainContractCertEnquiryResultWrapper>(){
					public void onFailure(Throwable e) {
						UIUtil.unmaskPanelById(getId());
						UIUtil.checkSessionTimeout(e,true,globalSectionController.getUser(),"MainCertificateGridPanel.mainCerticateGridPanel().mainContractCertificateRepository.getMainContractCertificateList");
					}

					public void onSuccess(MainContractCertEnquiryResultWrapper fullCertList) {
						UIUtil.unmaskPanelById(getId());
						populateGrid(fullCertList);
					}

				});
			}
		});
		paginationToolbar.setTotalPage(0);
		paginationToolbar.setCurrentPage(0);
		
		paginationToolbar.addFill();

		totalRecordsTextItem = new ToolbarTextItem("<b>Records:0</b>");

		paginationToolbar.addItem(totalRecordsTextItem);
		paginationToolbar.addSeparator();
		
		resultPanel.setBottomToolbar(paginationToolbar);
		bottomPanel.add(resultPanel);
		bottomPanel.setFrame(false);

		add(bottomPanel);	
	}

	private MainContractCertEnquirySearchingWrapper genSearchWrapper(){
		String division = divisionComboBox.getValueAsString();
		String company = companyComboBox.getValueAsString();
		String jobNo = jobNoComboBox.getValueAsString();
		String status = statusComboBox.getValueAsString();
		for(String[] str : this.status){
			if(str[1].contentEquals(status))
				status = str[0];
		}
		
		if("".equals(company) && "".equals(division) && "".equals(jobNo) && "".equals(status)){
			MessageBox.alert("Please fill in at least 1 search field.");
			return null;
		}
		
		wrapper = new MainContractCertEnquirySearchingWrapper();
		wrapper.setCompanyNo(company.replace("*", "").replace("%", ""));
		wrapper.setDivisionCode(division.replace("*", "").replace("%", ""));
		wrapper.setJobNo(jobNo.replace("*", "").replace("%", ""));
		wrapper.setStatus(status);
		return wrapper;
	}
	
	private void search() {
		wrapper = genSearchWrapper();
		
		if(wrapper==null)
			return;
		
		UIUtil.maskPanelById(getId(), GlobalParameter.SEARCHING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMainContractCertificateRepository().obtainMainContractCertificateList(wrapper, -1, new AsyncCallback<MainContractCertEnquiryResultWrapper>(){
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(getId());
				UIUtil.checkSessionTimeout(e,true,globalSectionController.getUser(),"MainCertificateGridPanel.mainCerticateGridPanel().mainContractCertificateRepository.getMainContractCertificateList");
			}

			public void onSuccess(MainContractCertEnquiryResultWrapper fullCertList) {
				UIUtil.unmaskPanelById(getId());
				populateGrid(fullCertList);
			}

		});
		
	}
	
	public void populateGrid(MainContractCertEnquiryResultWrapper resultWrapper){
		PaginationWrapper<MainContractCertificate> resultSet = resultWrapper.getPaginatedMainCerts();
		List<Job> jobs = resultWrapper.getJobs();
		Iterator<Job> iterator = jobs.iterator();
		Job currentJob = null;
		paginationToolbar.setTotalPage(resultSet.getTotalPage());
		paginationToolbar.setCurrentPage(resultSet.getCurrentPage());
		totalRecordsTextItem.setText("<b>Records: " + resultSet.getTotalRecords() + "</b>");
		
		dataStore.removeAll();
		List<MainContractCertificate> wrapper = resultSet.getCurrentPageContentList();
		Record[] data = new Record[wrapper.size()];

		@SuppressWarnings("unused")
		int latestCertIndex = 0;
		int latestMainCertNo = 0; 
		int recordIndex = wrapper.size()-1;
		MainContractCertificate  previousCert = null;
		for (int i = 0; i < wrapper.size(); i++){
			MainContractCertificate mainContractCertificate = wrapper.get(i);

			if(mainContractCertificate.getCertificateNumber()>latestMainCertNo){
				latestMainCertNo = mainContractCertificate.getCertificateNumber();
				latestCertIndex = i;
			}

			//Calculate posting amount
			double movement = obtainMainCertNetAmountMovement(mainContractCertificate, previousCert);
			double netGstReceivable = 0.0;
			double netGstPayable = 0.0;
			if(previousCert!=null){
				netGstReceivable = RoundingUtil.round(mainContractCertificate.getGstReceivable() - previousCert.getGstReceivable(), 2);
				netGstPayable = RoundingUtil.round(mainContractCertificate.getGstPayable() - previousCert.getGstPayable(), 2);
			}else{
				netGstReceivable = RoundingUtil.round(mainContractCertificate.getGstReceivable(), 2);
				netGstPayable = RoundingUtil.round(mainContractCertificate.getGstPayable(), 2);
			}


			double appliedMainContractorAmount = mainContractCertificate.getAppliedMainContractorAmount()==null?0:mainContractCertificate.getAppliedMainContractorAmount();
			double appliedNSCNDSCAmount = mainContractCertificate.getAppliedNSCNDSCAmount()==null?0:mainContractCertificate.getAppliedNSCNDSCAmount();
			double appliedMOSAmount = mainContractCertificate.getAppliedMOSAmount()==null?0:mainContractCertificate.getAppliedMOSAmount();
			double appliedMainContractorRetention = mainContractCertificate.getAppliedMainContractorRetention()==null?0:mainContractCertificate.getAppliedMainContractorRetention();
			double appliedMainContractorRetentionReleased= mainContractCertificate.getAppliedMainContractorRetentionReleased()==null?0:mainContractCertificate.getAppliedMainContractorRetentionReleased();
			double appliedRetentionforNSCNDSC = mainContractCertificate.getAppliedRetentionforNSCNDSC()==null?0:mainContractCertificate.getAppliedRetentionforNSCNDSC();
			double appliedRetentionforNSCNDSCReleased = mainContractCertificate.getAppliedRetentionforNSCNDSCReleased()==null?0:mainContractCertificate.getAppliedRetentionforNSCNDSCReleased();
			double appliedMOSRetention = mainContractCertificate.getAppliedMOSRetention()==null?0:mainContractCertificate.getAppliedMOSRetention();
			double appliedMOSRetentionReleased = mainContractCertificate.getAppliedMOSRetentionReleased()==null?0:mainContractCertificate.getAppliedMOSRetentionReleased();
			double appliedContraChargeAmount = mainContractCertificate.getAppliedContraChargeAmount()==null?0:mainContractCertificate.getAppliedContraChargeAmount();
			double appliedAdjustmentAmount = mainContractCertificate.getAppliedAdjustmentAmount()==null?0:mainContractCertificate.getAppliedAdjustmentAmount();
			double appliedAdvancePayment = mainContractCertificate.getAppliedAdvancePayment() == null ? 0.0 : mainContractCertificate.getAppliedAdvancePayment();
			double appliedCPFAmount = mainContractCertificate.getAppliedCPFAmount()==null?0:mainContractCertificate.getAppliedCPFAmount();
			double certifiedMainContractorAmount = mainContractCertificate.getCertifiedMainContractorAmount()==null?0:mainContractCertificate.getCertifiedMainContractorAmount();
			double certifiedNSCNDSCAmount = mainContractCertificate.getCertifiedNSCNDSCAmount()==null?0:mainContractCertificate.getCertifiedNSCNDSCAmount();
			double certifiedMOSAmount = mainContractCertificate.getCertifiedMOSAmount()==null?0:mainContractCertificate.getCertifiedMOSAmount();
			double certifiedMainContractorRetention = mainContractCertificate.getCertifiedMainContractorRetention()==null?0:mainContractCertificate.getCertifiedMainContractorRetention();
			double certifiedMainContractorRetentionReleased = mainContractCertificate.getCertifiedMainContractorRetentionReleased()==null?0:mainContractCertificate.getCertifiedMainContractorRetentionReleased();
			double certifiedRetentionforNSCNDSC = mainContractCertificate.getCertifiedRetentionforNSCNDSC()==null?0:mainContractCertificate.getCertifiedRetentionforNSCNDSC();
			double certifiedRetentionforNSCNDSCReleased = mainContractCertificate.getCertifiedRetentionforNSCNDSCReleased()==null?0:mainContractCertificate.getCertifiedRetentionforNSCNDSCReleased();
			double certifiedMOSRetention = mainContractCertificate.getCertifiedMOSRetention()==null?0:mainContractCertificate.getCertifiedMOSRetention();
			double certifiedMOSRetentionReleased = mainContractCertificate.getCertifiedMOSRetentionReleased()==null?0:mainContractCertificate.getCertifiedMOSRetentionReleased();
			double certifiedContraChargeAmount = mainContractCertificate.getCertifiedContraChargeAmount()==null?0:mainContractCertificate.getCertifiedContraChargeAmount();
			double certifiedAdjustmentAmount = mainContractCertificate.getCertifiedAdjustmentAmount()==null?0:mainContractCertificate.getCertifiedAdjustmentAmount();
			double certifiedAdvancePayment = mainContractCertificate.getCertifiedAdvancePayment() == null ? 0.0 : mainContractCertificate.getCertifiedAdvancePayment();
			double certifiedCPFAmount = mainContractCertificate.getCertifiedCPFAmount()==null?0:mainContractCertificate.getCertifiedCPFAmount();
			double gstReceivable = mainContractCertificate.getGstReceivable()==null?0:mainContractCertificate.getGstReceivable();
			double gstPayable = mainContractCertificate.getGstPayable()==null?0:mainContractCertificate.getGstPayable();
			
			if((currentJob == null 
					|| !currentJob.getJobNumber().trim().equals(mainContractCertificate.getJobNo().trim()))
					&& iterator.hasNext()
					)
				currentJob = iterator.next();
			
			data[recordIndex] = recordDef.createRecord(new Object[]{
					currentJob.getCompany(),
					currentJob.getDivision(),
					mainContractCertificate.getJobNo(),
					mainContractCertificate.getCertificateNumber(),
					mainContractCertificate.getClientCertNo(),
					appliedMainContractorAmount,
					appliedNSCNDSCAmount,
					appliedMOSAmount,
					appliedMainContractorRetention,
					appliedMainContractorRetentionReleased,
					appliedRetentionforNSCNDSC,
					appliedRetentionforNSCNDSCReleased,
					appliedMOSRetention,
					appliedMOSRetentionReleased,
					appliedContraChargeAmount,
					appliedAdjustmentAmount,
					appliedAdvancePayment,
					appliedCPFAmount,
					date2String(mainContractCertificate.getIpaSubmissionDate()),
					date2String(mainContractCertificate.getIpaSentoutDate()),
					certifiedMainContractorAmount,
					certifiedNSCNDSCAmount,
					certifiedMOSAmount,
					certifiedMainContractorRetention,
					certifiedMainContractorRetentionReleased,
					certifiedRetentionforNSCNDSC,
					certifiedRetentionforNSCNDSCReleased,
					certifiedMOSRetention,
					certifiedMOSRetentionReleased,
					certifiedContraChargeAmount,
					certifiedAdjustmentAmount,
					certifiedAdvancePayment,
					certifiedCPFAmount,
					gstReceivable,
					gstPayable,
					mainContractCertificate.getCertificateStatus(),
					convertCertStatusToDescription(mainContractCertificate.getCertificateStatus()),
					mainContractCertificate.getArDocNumber(),
					date2String(mainContractCertificate.getCertIssueDate()),
					date2String(mainContractCertificate.getCertAsAtDate()),
					date2String(mainContractCertificate.getCertStatusChangeDate()),
					date2String(mainContractCertificate.getCertDueDate()),
					mainContractCertificate.getRemark(),
					movement,
					netGstReceivable,
					netGstPayable,
					date2String(mainContractCertificate.getActualReceiptDate())
			});

			previousCert = mainContractCertificate;
			recordIndex--;
		}

		dataStore.add(data);

		//hide detail panel
		if(!globalSectionController.getDetailSectionController().getMainPanel().isCollapsed())
			globalSectionController.getDetailSectionController().getMainPanel().collapse();
	}
	
	private String date2String(Date date){
		if (date!=null)
			return (new SimpleDateFormat("dd/MM/yyyy")).format(date);
		else
			return " ";
	}

	private Double obtainMainCertNetAmountMovement(MainContractCertificate latestMainCert, MainContractCertificate previousMainCert){
		Double movement = null;
		if(latestMainCert!=null){
			if(previousMainCert!=null)
				movement = latestMainCert.calculateCertifiedNetAmount()-previousMainCert.calculateCertifiedNetAmount();
			else
				movement = latestMainCert.calculateCertifiedNetAmount();
		}

		return movement;
	}
	
	/** @author koeyyeung
	 *  @created on 7 May, 2013
	 * */
	private String convertCertStatusToDescription(String certStatus){
		if(certStatus!=null){
			if(certStatus.equals(MainContractCertificate.CERT_CREATED))
				return MainContractCertificate.CERT_CREATED_DESC;
			if(certStatus.equals(MainContractCertificate.IPA_SENT))
				return MainContractCertificate.IPA_SENT_DESC;
			if(certStatus.equals(MainContractCertificate.CERT_CONFIRMED))
				return MainContractCertificate.CERT_CONFIRMED_DESC;
			if(certStatus.equals(MainContractCertificate.CERT_WAITING_FOR_APPROVAL))
				return MainContractCertificate.CERT_WAITING_FOR_APPROVAL_DESC;
			if(certStatus.equals(MainContractCertificate.CERT_POSTED))
				return MainContractCertificate.CERT_POSTED_DESC;
		}
		return "";
	}


	private ButtonListenerAdapter searchButtonListener() {
		return new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				search();
			};
		};
	}
	private BaseItemListenerAdapter downloadListener(final String url, final String fileType) {
		return new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				wrapper = genSearchWrapper();
				
				if(wrapper==null)
					return;

				String parameters = "?jobNo=" + encodeURIComponent(wrapper.getJobNo())
									+ "&division=" + encodeURIComponent(wrapper.getDivisionCode())
									+ "&company=" + encodeURIComponent(wrapper.getCompanyNo())
									+ "&status=" + encodeURIComponent(wrapper.getStatus())
									+ "&fileType=" + fileType;

				com.google.gwt.user.client.Window.open(url + parameters, "_blank", "");
			}
		};
	}
	
    /**
     * The encodeURIComponent() function encodes a URI component.
	 * This function encodes special characters. 
	 * In addition, it encodes the following characters: , / ? : @ & = + $ #
     * @param str the string to convert
     * @return  A String, representing the encoded string
     */
    public static native String encodeURIComponent(String str)/*-{
        return str == null ? null : encodeURIComponent(new $wnd.String(str));
    }-*/;

	private void obtainJobNoStore() {
		SessionTimeoutCheck.renewSessionTimer();
		jobRepository.getAllJobList(new AsyncCallback<List<Job>>() {
			public void onSuccess(	List<Job> jobList) {
				jobNoStore.removeAll();
				if (jobList != null) {
					RecordDef jobNoDef = new RecordDef(new FieldDef[] { new StringFieldDef(JOB_NO_VALUE), new StringFieldDef(JOB_NO_DISPLAY) });
					jobNos = new String[jobList.size() + 1][2];
					jobNos[0][0] = "";
					jobNos[0][1] = "ALL";
					jobNoStore.add(jobNoDef.createRecord(jobNos[0]));

					List<String> jobNoList = new ArrayList<String>();
					for (Job job : jobList) {
						jobNoList.add(job.getJobNumber());
					}
					Collections.sort(jobNoList);
					for (int i = 0; i < jobNoList.size(); i++) {
						jobNos[i + 1][0] = jobNoList.get(i);
						jobNos[i + 1][1] = jobNoList.get(i);
						jobNoStore.add(jobNoDef.createRecord(jobNos[i + 1]));
					}
					jobNoStore.commitChanges();
				}
			}

			public void onFailure(	Throwable e) {
				UIUtil.checkSessionTimeout(e, false, user);
			}
		});
	}
	
	private void obtainStatusStore() {
		RecordDef statusDef = new RecordDef(new FieldDef[] { new StringFieldDef(STATUS_VALUE), new StringFieldDef(STATUS_DISPLAY) });
		status = GlobalParameter.getMainCertficateStatusesFull(true);
		boolean first = true;
		for(String[] str : status){
			if(!first)
				str[1] = str[1]+"("+str[0]+")";
			first=false;
		}
		statusStore = new SimpleStore(new String[] { STATUS_VALUE, STATUS_DISPLAY },status);
		statusStore.removeAll();
		for(String[] str : status){
			statusStore.add(statusDef.createRecord(str));
		}
		statusStore.commitChanges();
	}

	private void obtainDivisionStore() {
		SessionTimeoutCheck.renewSessionTimer();
		jobRepository.obtainAllJobDivision(new AsyncCallback<List<String>>() {
			public void onSuccess(	List<String> divisionList) {
				divisionStore.removeAll();
				divisionSuggestList = new ArrayList<String>();
				if (divisionList != null) {
					RecordDef divisionDef = new RecordDef(new FieldDef[] { new StringFieldDef(DIVISION_VALUE), new StringFieldDef(DIVISION_DISPLAY) });
					divisions = new String[divisionList.size() + 1][2];
					divisions[0][0] = "";
					divisions[0][1] = "ALL";
					divisionStore.add(divisionDef.createRecord(divisions[0]));

					Collections.sort(divisionList);
					divisionSuggestList.addAll(divisionList);
					for (int i = 0; i < divisionList.size(); i++) {
						divisions[i + 1][0] = divisionList.get(i);
						divisions[i + 1][1] = divisionList.get(i);
						divisionStore.add(divisionDef.createRecord(divisions[i + 1]));
					}
					divisionStore.commitChanges();
				}
			}

			public void onFailure(	Throwable e) {
				UIUtil.checkSessionTimeout(e, false, user);
			}
		});
	}

	private void obtainCompanyStore() {
		SessionTimeoutCheck.renewSessionTimer();
		jobRepository.obtainAllJobCompany(new AsyncCallback<List<String>>() {
			public void onSuccess(	List<String> companyList) {
				companyStore.removeAll();
				if (companyList != null) {
					RecordDef companyDef = new RecordDef(new FieldDef[] { new StringFieldDef(COMPANY_VALUE), new StringFieldDef(COMPANY_DISPLAY) });
					companys = new String[companyList.size() + 1][2];
					companys[0][0] = "";
					companys[0][1] = "ALL";
					companyStore.add(companyDef.createRecord(companys[0]));

					Collections.sort(companyList);
					for (int i = 0; i < companyList.size(); i++) {
						companys[i + 1][0] = companyList.get(i);
						companys[i + 1][1] = companyList.get(i);
						companyStore.add(companyDef.createRecord(companys[i + 1]));
					}
					companyStore.commitChanges();
				}
			}

			public void onFailure(	Throwable e) {
				UIUtil.checkSessionTimeout(e, false, user);
			}
		});
	}
}
