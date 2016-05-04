package com.gammon.qs.client.ui.panel.mainSection;

import java.util.Date;
import java.util.List;

import com.gammon.qs.application.User;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.PackageRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.client.ui.renderer.DateRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.DateUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.SCListPaginationWrapper;
import com.gammon.qs.wrapper.finance.SubcontractListWrapper;
import com.gammon.qs.wrapper.sclist.SCListWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.SortState;
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
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.Radio;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;
import com.gwtext.client.widgets.layout.TableLayoutData;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

/**
 * koeyyeung
 * Enhancement for Monthly SCPackage Snapshot 
 * Reports: Subcontract Report 
 * 			Subcontract Liability Report 
 * 			Subcontractor Analysis Report 
 * Modified on 25 Aug, 2014
 */
public class SubcontractEnquiryMainPanel extends Panel{
	private static final String MAIN_PANEL_ID ="SubcontractEnquiryMainPanel";
	
	private Store dataStore;

	// used to check access rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	private List<String> accessRightsListForUpdate;
	
	private PackageRepositoryRemoteAsync packageRepository;
	
	private TextField companyTextField;
	private TextField divisionTextField;
	private TextField jobNumberTextField;
	private TextField subcontractNumberTextField;
	private TextField subcontractorNumberTextField;
	private TextField workScopeTextField;
	private TextField clientNoTextField;
	private TextField yearTextField;
	
	private static final String SUBCONTRACTOR_NATURE_VALUE = "subcontractorNatureValue";
	private static final String SUBCONTRACTOR_NATURE_DISPLAY = "subcontractorNatureDisplay"; 
	private	ComboBox subcontractorNatureComboBox = new ComboBox();
	private String[][] subcontractorNature = new String[][] {};
	private Store subcontractorNatureStore = new SimpleStore(new String[] {SUBCONTRACTOR_NATURE_VALUE, SUBCONTRACTOR_NATURE_DISPLAY}, subcontractorNature);
	
	private static final String PAYMENT_TYPE_VALUE = "paymentTypeValue";
	private static final String PAYMENT_TYPE_DISPLAY = "paymentTypeDisplay"; 
	private	ComboBox paymentTypeComboBox = new ComboBox();
	private String[][] paymentTypes = new String[][] {};
	private Store paymentTypeStore = new SimpleStore(new String[] {PAYMENT_TYPE_VALUE, PAYMENT_TYPE_DISPLAY}, paymentTypes);
	
	
	private static final String SPLIT_TERMINIATE_STATUS_VALUE = "splitTerminateStatusValue";
	private static final String SPLIT_TERMINIATE_STATUS_DISPLAY = "splitTerminateStatusDisplay"; 
	private	ComboBox splitTerminateStatusComboBox = new ComboBox();
	private String[][] splitTerminateStatus = new String[][] {};
	private Store splitTerminateStatusStore = new SimpleStore(new String[] {SPLIT_TERMINIATE_STATUS_VALUE, SPLIT_TERMINIATE_STATUS_DISPLAY}, splitTerminateStatus);
	
	
	private static final String MONTH_VALUE = "monthValue";
	private static final String MONTH_DISPLAY = "monthDisplay"; 
	private	ComboBox monthComboBox = new ComboBox();
	private String[][] months = new String[][] {};
	private Store monthStore = new SimpleStore(new String[] {MONTH_VALUE, MONTH_DISPLAY}, months);
	
	private Checkbox completionDatecheckbox;
	
	private ToolbarButton subcontractReportToolbarButton;
	private Item downloadSubcontractExcelButton;
	private Item downloadSubcontractPDFButton;
	
	private ToolbarButton subcontractLiabilityReportToolbarButton;
	private Item downloadSubcontractLiabilityExcelButton;
	private Item downloadSubcontractLiabilityPDFButton;

	private ToolbarButton subcontractorAnalysisReportToolbarButton;
	private Item downloadsubcontractorAnalysisExcelButton;
	private Item downloadsubcontractorAnalysisPDFButton;

	private ToolbarButton	sCDetailReportToolbarButton;
	private Item	downloadSCDetailExcelButton;
	
	private ToolbarButton	recalculateScTotalsButton;

	private ToolbarTextItem asOfDateItem;
	private ToolbarTextItem totalNoOfRecordsValueLabel;

	private Radio asOfTodayRadio;
	private Radio monthEndHistoryRadio;
	
	private Record[] data = null;

	private EditorGridPanel resultEditorGridPanel;

	private User user;
	
	private PaginationToolbar paginationToolbar;
	
	private GlobalMessageTicket globalMessageTicket;
	
	private RecordDef recordDef = new RecordDef(
			new FieldDef[] {
					new StringFieldDef("jobNumber"),
					new StringFieldDef("jobDescription"),
					new DateFieldDef("jobCompletionDate"),
					new StringFieldDef("clientNumber"),
					new StringFieldDef("subcontractNumber"),
					new StringFieldDef("subcontractorNumber"),
					new StringFieldDef("subcontractorName"),
					new StringFieldDef("subcontractDescription"),
					new StringFieldDef("workScope"),
					new StringFieldDef("remeasuredSubcontractSum"),
					new StringFieldDef("addendum"),
					new StringFieldDef("revisedSubcontractSum"),
					new StringFieldDef("accumlatedRetentionAmt"),
					new StringFieldDef("retentionReleasedAmt"),
					new StringFieldDef("retentionBalanceAmt"),
					new StringFieldDef("paymentTypes"),
					new StringFieldDef("paymentTerms"),
					new StringFieldDef("scTerm"),
					new StringFieldDef("subcontractorNature"),
					new StringFieldDef("totalLiabilities"),
					new StringFieldDef("provision"),
					new StringFieldDef("balanceToComplete"),
					new StringFieldDef("totalCertAmtPrev"),
					new StringFieldDef("totalCertAmtCum"),
					new StringFieldDef("totalCCPostedAmt"),
					new StringFieldDef("totalMOSPostedAmt"),
					new DateFieldDef("requisitionApprovedDate"),
					new DateFieldDef("tenderAnalysisApprovedDate"),
					new DateFieldDef("preAwardMeetingDate"),
					new DateFieldDef("loaSignedDate"),
					new DateFieldDef("scDocScrDate"),
					new DateFieldDef("scDocLegalDate"),
					new DateFieldDef("workCommenceDate"),
					new DateFieldDef("onSiteStartDate"),
					new StringFieldDef("splitTerminateStatus"),
					
					new StringFieldDef("company"),
					new StringFieldDef("division"),
					new StringFieldDef("soloJV"),
					new StringFieldDef("JVPercent"),
					new DateFieldDef("actualPCCDate"),
					new StringFieldDef("completionStatus"),
					new StringFieldDef("currency"),
					new StringFieldDef("originalSubcontractSum"),
					new StringFieldDef("netCerAmt"),
			}	
	);

	private GlobalSectionController globalSectionController;


	public SubcontractEnquiryMainPanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		
		globalMessageTicket = new GlobalMessageTicket();
		user = globalSectionController.getUser();

		userAccessRightsRepository = globalSectionController.getUserAccessRightsRepository();
		packageRepository = globalSectionController.getPackageRepository();
		
		setupUI();
	
	}
	private void setupUI() {
		setLayout(new RowLayout());
		setPaddings(0);
		
		setupSearchPanel();
		setupGridPanell();
		
		setId(MAIN_PANEL_ID);		
		
		//hide detail panel
		globalSectionController.getDetailSectionController().getMainPanel().collapse();
	}
	
	private void setupSearchPanel() {
		Panel searchPanel = new Panel();
		searchPanel.setPaddings(2);
		searchPanel.setFrame(true);
		searchPanel.setHeight(125);
		searchPanel.setLayout(new ColumnLayout());
		
		//search Panel
		Panel leftColumnPanel = new Panel();
		leftColumnPanel.setPaddings(2);
		leftColumnPanel.setHeight(120);
		leftColumnPanel.setLayout(new TableLayout(8));
		
		FieldListener searchListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER){
					search();
				}
			}
		};	
		
		Label companyLabel = new Label("Company");
		companyLabel.setCls("table-cell");
		leftColumnPanel.add(companyLabel);
		companyTextField =new TextField();
		companyTextField.setWidth(80);
		companyTextField.setCtCls("table-cell");
		companyTextField.addListener(searchListener);
		leftColumnPanel.add(companyTextField);

		Label divisionLabel = new Label("Division");
		divisionLabel.setCls("table-cell");
		leftColumnPanel.add(divisionLabel);
		divisionTextField =new TextField();
		divisionTextField.setWidth(80);
		divisionTextField.setCtCls("table-cell");
		divisionTextField.addListener(searchListener);
		leftColumnPanel.add(divisionTextField);

		Label jobNumberLabel = new Label("Job No.");
		jobNumberLabel.setCls("table-cell");
		leftColumnPanel.add(jobNumberLabel);
		jobNumberTextField =new TextField();
		jobNumberTextField.setWidth(80);
		jobNumberTextField.setCtCls("table-cell");
		jobNumberTextField.addListener(searchListener);
		leftColumnPanel.add(jobNumberTextField);
		if(globalSectionController.getJob()!=null)
			jobNumberTextField.setValue(globalSectionController.getJob().getJobNumber());
		
		Label clientNoLabel = new Label("Client No.");
		clientNoLabel.setCls("table-cell");
		leftColumnPanel.add(clientNoLabel);
		clientNoTextField =new TextField();
		clientNoTextField.setWidth(80);
		clientNoTextField.setCtCls("table-cell");
		clientNoTextField.addListener(searchListener);
		leftColumnPanel.add(clientNoTextField);
		
		
		Label subcontractNumberLabel = new Label("Subcontract No.");
		subcontractNumberLabel.setCls("table-cell");
		leftColumnPanel.add(subcontractNumberLabel);
		subcontractNumberTextField =new TextField();
		subcontractNumberTextField.setWidth(80);
		subcontractNumberTextField.setCtCls("table-cell");
		subcontractNumberTextField.addListener(searchListener);
		leftColumnPanel.add(subcontractNumberTextField);


		Label subcontractorNumberLabel = new Label("Subcontractor No.");
		subcontractorNumberLabel.setCls("table-cell");
		leftColumnPanel.add(subcontractorNumberLabel);
		subcontractorNumberTextField =new TextField();
		subcontractorNumberTextField.setWidth(80);
		subcontractorNumberTextField.setCtCls("table-cell");
		subcontractorNumberTextField.addListener(searchListener);
		leftColumnPanel.add(subcontractorNumberTextField);

		
		Label subcontractorNatureLabel = new Label("Subcontractor Nature");
		subcontractorNatureLabel.setCls("table-cell");
		leftColumnPanel.add(subcontractorNatureLabel);

		subcontractorNatureComboBox.setCtCls("table-cell");
		subcontractorNatureComboBox.setForceSelection(true);
		subcontractorNatureComboBox.setMinChars(1);
		subcontractorNatureComboBox.setValueField(SUBCONTRACTOR_NATURE_VALUE);
		subcontractorNatureComboBox.setDisplayField(SUBCONTRACTOR_NATURE_DISPLAY);
		subcontractorNatureComboBox.setMode(ComboBox.LOCAL);
		subcontractorNatureComboBox.setTriggerAction(ComboBox.ALL);
		subcontractorNatureComboBox.setEmptyText("ALL");
		subcontractorNatureComboBox.setTypeAhead(true);
		subcontractorNatureComboBox.setSelectOnFocus(true);
		subcontractorNatureComboBox.setWidth(80);
		subcontractorNatureComboBox.addListener(searchListener);
		
		subcontractorNature = new String[][]{
				{"","ALL"},
				{"DSC", "DSC"},
				{"NDSC","NDSC"},
				{"NSC", "NSC"}
				};
		subcontractorNatureStore = new SimpleStore(new String[]{SUBCONTRACTOR_NATURE_VALUE,SUBCONTRACTOR_NATURE_DISPLAY},subcontractorNature);
		subcontractorNatureComboBox.setStore(subcontractorNatureStore);
		leftColumnPanel.add(subcontractorNatureComboBox);

		Label workScopeLabel = new Label("Work Scope (Trade)");
		workScopeLabel.setCls("table-cell");
		leftColumnPanel.add(workScopeLabel);
		workScopeTextField =new TextField();
		workScopeTextField.setWidth(80);
		workScopeTextField.setCtCls("table-cell");
		workScopeTextField.addListener(searchListener);
		leftColumnPanel.add(workScopeTextField);

		
	    Label splitTerminateStatusLabel = new Label("Split Terminate Status");
	    splitTerminateStatusLabel.setCls("table-cell");
		leftColumnPanel.add(splitTerminateStatusLabel);
		
		splitTerminateStatusComboBox.setCtCls("table-cell");
	    splitTerminateStatusComboBox.setForceSelection(true);
	    splitTerminateStatusComboBox.setMinChars(1);
	    splitTerminateStatusComboBox.setValueField(SPLIT_TERMINIATE_STATUS_VALUE);
	    splitTerminateStatusComboBox.setDisplayField(SPLIT_TERMINIATE_STATUS_DISPLAY);
	    splitTerminateStatusComboBox.setMode(ComboBox.LOCAL);
	    splitTerminateStatusComboBox.setTriggerAction(ComboBox.ALL);
	    splitTerminateStatusComboBox.setEmptyText("ALL");
	    splitTerminateStatusComboBox.setTypeAhead(true);
	    splitTerminateStatusComboBox.setSelectOnFocus(true);
	    splitTerminateStatusComboBox.setWidth(80);
	    splitTerminateStatusComboBox.setListWidth(150);
	    splitTerminateStatusComboBox.addListener(searchListener);
	    
	    splitTerminateStatus = new String[][]{
				{"","ALL"},
				{SCPackage.SPLITTERMINATE_DEFAULT, 	SCPackage.SPLITTERMINATESTATUSES[0][1]},
				{SCPackage.SPLIT_SUBMITTED, 		SCPackage.SPLITTERMINATESTATUSES[1][1]},
				{SCPackage.TERMINATE_SUBMITTED,		SCPackage.SPLITTERMINATESTATUSES[2][1]},
				{SCPackage.SPLIT_APPROVED, 			SCPackage.SPLITTERMINATESTATUSES[3][1]},
				{SCPackage.TERMINATE_APPROVED, 		SCPackage.SPLITTERMINATESTATUSES[4][1]},
				{SCPackage.SPLIT_REJECTED, 			SCPackage.SPLITTERMINATESTATUSES[5][1]},
				{SCPackage.TERMINATE_REJECTED, 		SCPackage.SPLITTERMINATESTATUSES[6][1]}
				};
	    splitTerminateStatusStore = new SimpleStore(new String[]{SPLIT_TERMINIATE_STATUS_VALUE,SPLIT_TERMINIATE_STATUS_DISPLAY},splitTerminateStatus);
	    splitTerminateStatusComboBox.setStore(splitTerminateStatusStore);
	    leftColumnPanel.add(splitTerminateStatusComboBox);
	    
	    Label paymentTypeLabel = new Label("Payment Type");
		paymentTypeLabel.setCls("table-cell");
		leftColumnPanel.add(paymentTypeLabel);
		
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
	    paymentTypeComboBox.setWidth(80);
	    paymentTypeComboBox.setListWidth(180);
	    paymentTypeComboBox.addListener(searchListener);
	    
	    paymentTypes = new String[][]{
				{"","ALL"},
				{"N","No Payment"},
				{"D","Payment Requisition"},
				{"I","Interim"},
				{"NDI","Interim+Requisition+No Payment"},
				{"F", "Final"}
				};
	    paymentTypeStore = new SimpleStore(new String[]{PAYMENT_TYPE_VALUE,PAYMENT_TYPE_DISPLAY},paymentTypes);
	    paymentTypeComboBox.setStore(paymentTypeStore);
	    leftColumnPanel.add(paymentTypeComboBox);
	    
	    completionDatecheckbox = new Checkbox("Show Job Completion Date");
	    completionDatecheckbox.setCtCls("table-cell");
		
		ToolTip completionDateToolTip = new ToolTip();
		completionDateToolTip.setTitle("Job Completion Date");
		completionDateToolTip.setHtml("QS System may take much longer time to complete the searching or to generate the Excel file");
		completionDateToolTip.setDismissDelay(15000);
		completionDateToolTip.setWidth(200);
		completionDateToolTip.setTrackMouse(true);
		completionDateToolTip.applyTo(completionDatecheckbox);
	    
		leftColumnPanel.add(completionDatecheckbox, new TableLayoutData(3));
		
		Button searchButton = new Button("Search");
	    searchButton.setIconCls("find-icon");
	    searchButton.addListener(new ButtonListenerAdapter(){
	    	public void onClick(Button button, EventObject e) {
	    		globalMessageTicket.refresh();
	    		search();
	    	};
	    });
	    leftColumnPanel.add(searchButton);
	    
	    
	    searchPanel.add(leftColumnPanel, new ColumnLayoutData(0.75));
	    
	    
	    Panel radioPanel = new Panel();
	    radioPanel.setFrame(true);
		radioPanel.setBorder(true);
		radioPanel.setLayout(new TableLayout(2));
		radioPanel.setPaddings(1);
	    
		String AS_OF_TODAY_ID = "asOfTodayRadio_ID";
		String MONTH_END_HOSTORY_ID = "monthEndHistoryRadio_ID";
		String PERIOD_RADIO_GP = "periodRadio";//group
		
		asOfTodayRadio = new Radio("As Of Today", PERIOD_RADIO_GP);
		asOfTodayRadio.setCls("table-cell");
		asOfTodayRadio.setCtCls("table-cell");
		asOfTodayRadio.setId(AS_OF_TODAY_ID);
		asOfTodayRadio.setValue(true);
		asOfTodayRadio.addListener(new CheckboxListenerAdapter(){
			public void onCheck(Checkbox field, boolean checked) {
				if(checked){
					asOfDateItem.setText("As of "+DateUtil.formatDate(new Date(), "dd-MM-yyyy"));
					monthComboBox.reset();
					yearTextField.reset();
					monthComboBox.setDisabled(true);
					yearTextField.setDisabled(true);
				}
			}
		});
		radioPanel.add(asOfTodayRadio);		
		
		monthEndHistoryRadio = new Radio("Month End History", PERIOD_RADIO_GP);
		monthEndHistoryRadio.setCls("table-cell");
		monthEndHistoryRadio.setCtCls("table-cell");
		monthEndHistoryRadio.setId(MONTH_END_HOSTORY_ID);
		monthEndHistoryRadio.setValue(false);
		monthEndHistoryRadio.addListener(new CheckboxListenerAdapter(){
			public void onCheck(Checkbox field, boolean checked) {
				if(checked){
					monthComboBox.setDisabled(false);
					yearTextField.setDisabled(false);
					yearTextField.setValue(DateUtil.formatDate(new Date(), "yyyy"));
					asOfDateItem.setText("");
				}
			}
		});
		
		radioPanel.add(monthEndHistoryRadio);		
		
		radioPanel.add(new Label());
		
		Panel periodPanel = new Panel();
		periodPanel.setBorder(false);
		periodPanel.setLayout(new TableLayout(2));
		periodPanel.setPaddings(1);
		
	    Label monthLabel = new Label("Month");
	    monthLabel.setCls("table-cell");
	    periodPanel.add(monthLabel);
		
		monthComboBox.setCtCls("table-cell");
	    monthComboBox.setForceSelection(true);
	    monthComboBox.setMinChars(1);
	    monthComboBox.setValueField(MONTH_VALUE);
	    monthComboBox.setDisplayField(MONTH_DISPLAY);
	    monthComboBox.setMode(ComboBox.LOCAL);
	    monthComboBox.setTriggerAction(ComboBox.ALL);
	    monthComboBox.setEmptyText("Select Month");
	    monthComboBox.setTypeAhead(true);
	    monthComboBox.setSelectOnFocus(true);
	    monthComboBox.setWidth(100);
	    monthComboBox.setListWidth(100);
	    monthComboBox.addListener(searchListener);
	    
	    months = new String[][]{
	    		{"","Select Month"},
	    		{"1","January"},
	    		{"2","February"},
	    		{"3","March"},
	    		{"4","April"},
	    		{"5","May"},
	    		{"6","June"},
	    		{"7","July"},
	    		{"8","August"},
	    		{"9","September"},
	    		{"10","October"},
	    		{"11","November"},
	    		{"12","December"}
	    };
	    monthStore = new SimpleStore(new String[]{MONTH_VALUE,MONTH_DISPLAY},months);
	    monthComboBox.setStore(monthStore);
	    monthComboBox.setDisabled(true);
	    periodPanel.add(monthComboBox);
		
	    Label yearLabel = new Label("Year");
	    yearLabel.setCls("table-cell");
	    periodPanel.add(yearLabel);
		
	    yearTextField = new TextField();
	    yearTextField.setCtCls("table-cell");
	    yearTextField.setEmptyText("e.g. "+DateUtil.formatDate(new Date(), "yyyy"));
	    yearTextField.setWidth(100);
	    yearTextField.addListener(searchListener);
	    
	    yearTextField.setDisabled(true);
	    periodPanel.add(yearTextField);
	    
	    radioPanel.add(periodPanel);
	    
	    searchPanel.add(radioPanel, new ColumnLayoutData(0.25));
		add(searchPanel);
	}
	
	
	
	private void setupToolbar() {
		Toolbar toolbar = new Toolbar();

		/*---------------------------------Subcontract Report---------------------------------------*/
		downloadSubcontractExcelButton = new Item("Export to Excel");
		downloadSubcontractExcelButton.setIconCls("excel-icon");
		downloadSubcontractExcelButton.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				if (monthEndHistoryRadio.getValue() && ((!"".equals(monthComboBox.getText()) && "".equals(yearTextField.getText().trim())) || (!"".equals(yearTextField.getText().trim()) && "".equals(monthComboBox.getText())))) {
					MessageBox.alert("Please fill in both month and year for searching Month End History.");
					return;
				}
				String parameters = obtainParameters();
				com.google.gwt.user.client.Window.open(GlobalParameter.FINANCE_SUBCONTRACT_LIST_DOWNLOAD_URL + parameters, "_blank", "");
			}
		});
		downloadSubcontractExcelButton.hide();

		downloadSubcontractPDFButton = new Item("Export to PDF");
		downloadSubcontractPDFButton.setIconCls("pdf-icon");
		downloadSubcontractPDFButton.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				if (monthEndHistoryRadio.getValue() && ((!"".equals(monthComboBox.getText()) && "".equals(yearTextField.getText().trim())) || (!"".equals(yearTextField.getText().trim()) && "".equals(monthComboBox.getText())))) {
					MessageBox.alert("Please fill in both month and year for searching Month End History.");
					return;
				}
				String parameters = obtainParameters();
				com.google.gwt.user.client.Window.open(GlobalParameter.SUBCONTRACT_REPORT_EXPORT_URL + parameters, "_blank", "");
			}
		});
		downloadSubcontractPDFButton.hide();

		Menu reportMenu = new Menu();
		reportMenu.addItem(downloadSubcontractExcelButton);
		reportMenu.addItem(downloadSubcontractPDFButton);

		subcontractReportToolbarButton = new ToolbarButton("Subcontract Report");
		subcontractReportToolbarButton.setMenu(reportMenu);
		subcontractReportToolbarButton.setIconCls("menu-show-icon");
		/*-----------------------------------Subcontract Report---------------------------------------------*/

		/*---------------------------------Subcontract Liability Report---------------------------------------*/
		downloadSubcontractLiabilityExcelButton = new Item("Export to Excel");
		downloadSubcontractLiabilityExcelButton.setIconCls("excel-icon");
		downloadSubcontractLiabilityExcelButton.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				if (monthEndHistoryRadio.getValue() && ((!"".equals(monthComboBox.getText()) && "".equals(yearTextField.getText().trim())) || (!"".equals(yearTextField.getText().trim()) && "".equals(monthComboBox.getText())))) {
					MessageBox.alert("Please fill in both month and year for searching Month End History.");
					return;
				}
				String parameters = obtainParameters();
				com.google.gwt.user.client.Window.open(GlobalParameter.SUBCONTRACT_LIABILITY_EXCEL_EXPORT_URL + parameters, "_blank", "");
			}
		});
		downloadSubcontractLiabilityExcelButton.hide();

		downloadSubcontractLiabilityPDFButton = new Item("Export to PDF");
		downloadSubcontractLiabilityPDFButton.setIconCls("pdf-icon");
		downloadSubcontractLiabilityPDFButton.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				if (monthEndHistoryRadio.getValue() && ((!"".equals(monthComboBox.getText()) && "".equals(yearTextField.getText().trim())) || (!"".equals(yearTextField.getText().trim()) && "".equals(monthComboBox.getText())))) {
					MessageBox.alert("Please fill in both month and year for searching Month End History.");
					return;
				}
				String parameters = obtainParameters();
				com.google.gwt.user.client.Window.open(GlobalParameter.SUBCONTRACT_LIABILITY_REPORT_EXPORT_URL + parameters, "_blank", "");
			}
		});
		downloadSubcontractLiabilityPDFButton.hide();

		Menu liabilityReportMenu = new Menu();
		liabilityReportMenu.addItem(downloadSubcontractLiabilityExcelButton);
		liabilityReportMenu.addItem(downloadSubcontractLiabilityPDFButton);

		subcontractLiabilityReportToolbarButton = new ToolbarButton("Subcontract Liability Report");
		subcontractLiabilityReportToolbarButton.setMenu(liabilityReportMenu);
		subcontractLiabilityReportToolbarButton.setIconCls("menu-show-icon");
		/*-----------------------------------Subcontract Liability Report---------------------------------------------*/

		/*---------------------------------Subcontractor Analysis Report---------------------------------------*/
		downloadsubcontractorAnalysisExcelButton = new Item("Export to Excel");
		downloadsubcontractorAnalysisExcelButton.setIconCls("excel-icon");
		downloadsubcontractorAnalysisExcelButton.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				if (monthEndHistoryRadio.getValue() && ((!"".equals(monthComboBox.getText()) && "".equals(yearTextField.getText().trim())) || (!"".equals(yearTextField.getText().trim()) && "".equals(monthComboBox.getText())))) {
					MessageBox.alert("Please fill in both month and year for searching Month End History.");
					return;
				}
				String parameters = obtainParameters();
				com.google.gwt.user.client.Window.open(GlobalParameter.SUBCONTRACTOR_ANALYSIS_EXCEL_EXPORT_URL + parameters, "_blank", "");
			}
		});
		downloadsubcontractorAnalysisExcelButton.hide();

		downloadsubcontractorAnalysisPDFButton = new Item("Export to PDF");
		downloadsubcontractorAnalysisPDFButton.setIconCls("pdf-icon");
		downloadsubcontractorAnalysisPDFButton.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				if (monthEndHistoryRadio.getValue() && ((!"".equals(monthComboBox.getText()) && "".equals(yearTextField.getText().trim())) || (!"".equals(yearTextField.getText().trim()) && "".equals(monthComboBox.getText())))) {
					MessageBox.alert("Please fill in both month and year for searching Month End History.");
					return;
				}
				String parameters = obtainParameters();
				com.google.gwt.user.client.Window.open(GlobalParameter.SUBCONTRACTOR_ANALYSIS_REPORT_EXPORT_URL + parameters, "_blank", "");
			}
		});
		downloadsubcontractorAnalysisPDFButton.hide();

		Menu subcontractorAnalysisReportMenu = new Menu();
		subcontractorAnalysisReportMenu.addItem(downloadsubcontractorAnalysisExcelButton);
		subcontractorAnalysisReportMenu.addItem(downloadsubcontractorAnalysisPDFButton);

		subcontractorAnalysisReportToolbarButton = new ToolbarButton("Subcontractor Analysis Report");
		subcontractorAnalysisReportToolbarButton.setMenu(subcontractorAnalysisReportMenu);
		subcontractorAnalysisReportToolbarButton.setIconCls("menu-show-icon");
		/*-----------------------------------Subcontractor Analysis Report---------------------------------------------*/
		
		
		/*-----------------------------------Export All SC Details----------------------------------------------------*/
		downloadSCDetailExcelButton = new Item("Export to Excel");
		downloadSCDetailExcelButton.setIconCls("excel-icon");
		downloadSCDetailExcelButton.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				if (globalSectionController.getJob()!=null && globalSectionController.getJob().getJobNumber()!=null && globalSectionController.getJob().getJobNumber().trim().length()>0){
					String jobNo = globalSectionController.getJob().getJobNumber();
					com.google.gwt.user.client.Window.open(GlobalParameter.SCDETAILS_EXCEL_DOWNLOAD_FOR_JOB_URL+ "?jobNumber=" + jobNo.trim(), "_blank", "");
				}else
					MessageBox.alert("No job selected.");
			}
		});
		
		Menu scDetailReportMenu = new Menu();
		scDetailReportMenu.addItem(downloadSCDetailExcelButton);

		sCDetailReportToolbarButton = new ToolbarButton("Subcontract Details Report");
		sCDetailReportToolbarButton.setMenu(scDetailReportMenu);
		sCDetailReportToolbarButton.setTooltip("Export", "Export all Subcontract Details of the Job");
		sCDetailReportToolbarButton.setIconCls("menu-show-icon");
		/*-----------------------------------Export All SC Details----------------------------------------------------*/
		
		/*
		 * modified by matthewlam, 20150210
		 * Bug fix #44 - Migrate functions from "Awarded Subcontract" to Subcontract Enquiry
		 */
		recalculateScTotalsButton = new ToolbarButton("Recalculate Subcontract Summary");
		recalculateScTotalsButton.setIconCls("calculator-icon");
		recalculateScTotalsButton.setTooltip("Recalculate Subcontract Total Figures", "Recalculate Subcontract Workdone and Payment totals from Subcontract Details of the Job");
		recalculateScTotalsButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, "Recalculating", true);
				try {
					SessionTimeoutCheck.renewSessionTimer();
					packageRepository.calculateTotalWDandCertAmount(globalSectionController.getJob().getJobNumber(), null, true, new AsyncCallback<Boolean>() {
						public void onFailure(Throwable e) {
							UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
							UIUtil.checkSessionTimeout(e, false, globalSectionController.getUser());
						}

						public void onSuccess(Boolean success) {
							UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
							// globalSectionController.populateSCLiabilitiesAndPaymentScreen();
							search();
						}
					});
				} catch (Exception ex) {
					UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
					UIUtil.checkSessionTimeout(ex, false, globalSectionController.getUser());
				}
			}
		});

		asOfDateItem = new ToolbarTextItem("As of " + DateUtil.formatDate(new Date(), "dd-MM-yyyy"));
		
		recalculateScTotalsButton.setVisible(false);
		sCDetailReportToolbarButton.setVisible(false);
		
		toolbar.addButton(subcontractReportToolbarButton);
		toolbar.addSeparator();
		toolbar.addButton(subcontractLiabilityReportToolbarButton);
		toolbar.addSeparator();
		toolbar.addButton(subcontractorAnalysisReportToolbarButton);
		toolbar.addSeparator();
		toolbar.addButton(sCDetailReportToolbarButton);
		toolbar.addSeparator();
		toolbar.addButton(recalculateScTotalsButton);
		toolbar.addSeparator();
		toolbar.addFill();
		toolbar.addItem(asOfDateItem);

		// Check for access rights - then add toolbar buttons accordingly
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010213_SUBCONTRACT_ENQUIRY_MAINPANEL, new AsyncCallback<List<String>>() {
			public void onSuccess(List<String> accessRightsReturned) {
				accessRightsList = accessRightsReturned;
				//displayButtons();
				SessionTimeoutCheck.renewSessionTimer();
				userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), "F010216", new AsyncCallback<List<String>>(){
					public void onSuccess(List<String> accessRightsReturned) {
						accessRightsListForUpdate = accessRightsReturned;
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
						displayButtons();
					}

					public void onFailure(Throwable e) {
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
						UIUtil.alert(e.getMessage());
					}
				});
			}

			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
				UIUtil.alert(e.getMessage());
			}
		});

		resultEditorGridPanel.setTopToolbar(toolbar);
	}
	
	private void setupGridPanell() {
		//Grid Panel 
		resultEditorGridPanel =  new EditorGridPanel();
		resultEditorGridPanel.setClicksToEdit(1);
		//this.arrowKeyNavigation = new ArrowKeyNavigation(this);
		
		setupToolbar();
		
		Renderer amountRender = new AmountRenderer(globalSectionController.getUser());
		ColumnConfig companyColumnConfig = new ColumnConfig("Company", "company" , 60, false);
		ColumnConfig divisionColumnConfig = new ColumnConfig("Division", "division" , 60, false);
		ColumnConfig soloJVColumnConfig = new ColumnConfig("Solo/JV", "soloJV" , 60, false);
		ColumnConfig jvPercentColumnConfig = new ColumnConfig("JV%", "JVPercent" , 50, false);
		ColumnConfig currencyColumnConfig = new ColumnConfig("Currency", "currency" , 60, false);
		
		ColumnConfig jobNumberColumnConfig = new ColumnConfig("Job No", "jobNumber" , 60, false);
		ColumnConfig jobDescriptionColumnConfig = new ColumnConfig("Job Descrption", "jobDescription" , 120, false);
		jobDescriptionColumnConfig.setHidden(true);
		ColumnConfig jobCompletionDateColumnConfig = new ColumnConfig("Job Completion Date", "jobCompletionDate" , 90, false);
		jobCompletionDateColumnConfig.setRenderer(new DateRenderer());
		jobCompletionDateColumnConfig.setHidden(true);
		
		ColumnConfig clientNumberColumnConfig = new ColumnConfig("Client No", "clientNumber" , 60, false);
		
		ColumnConfig subcontractNumberColumnConfig = new ColumnConfig("SC No", "subcontractNumber" , 60, false);
		ColumnConfig subcontractorNumberColumnConfig = new ColumnConfig("Subcontractor No", "subcontractorNumber" , 100, false);
		ColumnConfig subcontractorNameColumnConfig = new ColumnConfig("Subcontractor Name", "subcontractorName" , 180, false);

		ColumnConfig subcontractDescriptionColumnConfig = new ColumnConfig("Description", "subcontractDescription", 180, false);
		ColumnConfig workScopeColumnConfig = new ColumnConfig("Work Scope(Trade)", "workScope", 70, false);
		
		ColumnConfig remeasuredSubcontractSumColumnConfig = new ColumnConfig("Remeasured Subcontract Sum", "remeasuredSubcontractSum", 160, false);
		remeasuredSubcontractSumColumnConfig.setAlign(TextAlign.RIGHT);
		remeasuredSubcontractSumColumnConfig.setRenderer(amountRender);

		ColumnConfig addendumColumnConfig = new ColumnConfig("Addendum", "addendum", 120, false);
		addendumColumnConfig.setAlign(TextAlign.RIGHT);
		addendumColumnConfig.setRenderer(amountRender);

		ColumnConfig revisedSubcontractSumColumnConfig = new ColumnConfig("Revised Subcontract Sum", "revisedSubcontractSum", 160, false);
		revisedSubcontractSumColumnConfig.setAlign(TextAlign.RIGHT);
		revisedSubcontractSumColumnConfig.setRenderer(amountRender);
		
		ColumnConfig splitTerminateStatusColumnConfig = new ColumnConfig("Split Terminate Status", "splitTerminateStatus", 110, false);
		ColumnConfig paymentStatusColumnConfig = new ColumnConfig("Payment Type", "paymentTypes", 80, false);
		ColumnConfig paymentTermsColumnConfig = new ColumnConfig("Payment Terms", "paymentTerms", 80, false);
		ColumnConfig scTermColumnConfig = new ColumnConfig("SC Term", "scTerm", 120, false);
		ColumnConfig subcontractorNatureColumnConfig = new ColumnConfig("Subcontractor Nature", "subcontractorNature", 100, false);
		ColumnConfig totalLiabilitiesColumnConfig = new ColumnConfig("Work Done Amount(Cum.)", "totalLiabilities", 120, false);
		totalLiabilitiesColumnConfig.setAlign(TextAlign.RIGHT);
		totalLiabilitiesColumnConfig.setRenderer(amountRender);
		ColumnConfig provisionColumnConfig = new ColumnConfig("Provision", "provision", 120, false);
		provisionColumnConfig.setAlign(TextAlign.RIGHT);
		provisionColumnConfig.setRenderer(amountRender);
		ColumnConfig balanceToCompleteColumnConfig = new ColumnConfig("Balance to Complete", "balanceToComplete", 120, false);
		balanceToCompleteColumnConfig.setAlign(TextAlign.RIGHT);
		balanceToCompleteColumnConfig.setRenderer(amountRender);
		ColumnConfig totalCertAmtPrevColumnConfig = new ColumnConfig("Cert. Amount (Posted)", "totalCertAmtPrev", 120, false);
		totalCertAmtPrevColumnConfig.setAlign(TextAlign.RIGHT);
		totalCertAmtPrevColumnConfig.setRenderer(amountRender);
		ColumnConfig totalCertAmtCumColumnConfig = new ColumnConfig("Cert. Amount (Cum.)", "totalCertAmtCum", 120, false);
		totalCertAmtCumColumnConfig.setAlign(TextAlign.RIGHT);
		totalCertAmtCumColumnConfig.setRenderer(amountRender);
		ColumnConfig totalCCPostedAmtColumnConfig = new ColumnConfig("Contra Charge", "totalCCPostedAmt", 120, false);
		totalCCPostedAmtColumnConfig.setAlign(TextAlign.RIGHT);
		totalCCPostedAmtColumnConfig.setRenderer(amountRender);
		ColumnConfig totalMOSPostedAmtColumnConfig = new ColumnConfig("Material On Site", "totalMOSPostedAmt", 120, false);
		totalMOSPostedAmtColumnConfig.setAlign(TextAlign.RIGHT);
		totalMOSPostedAmtColumnConfig.setRenderer(amountRender);		
		ColumnConfig accumlatedRetentionColumnConfig = new ColumnConfig("Accumlated Retention(RT)", "accumlatedRetentionAmt", 130, false);
		accumlatedRetentionColumnConfig.setAlign(TextAlign.RIGHT);
		accumlatedRetentionColumnConfig.setRenderer(amountRender);
		ColumnConfig retentionReleasedColumnConfig = new ColumnConfig("Retention Released(RR)", "retentionReleasedAmt", 130, false);
		retentionReleasedColumnConfig.setAlign(TextAlign.RIGHT);
		retentionReleasedColumnConfig.setRenderer(amountRender);
		ColumnConfig retentionBalanceColumnConfig = new ColumnConfig("Retention Balance", "retentionBalanceAmt", 120, false);
		retentionBalanceColumnConfig.setAlign(TextAlign.RIGHT);
		retentionBalanceColumnConfig.setRenderer(amountRender);
		ColumnConfig originalSubcontractSumColumnConfig = new ColumnConfig("Original SC Sum", "originalSubcontractSum", 120, false);
		originalSubcontractSumColumnConfig.setAlign(TextAlign.RIGHT);
		originalSubcontractSumColumnConfig.setRenderer(amountRender);
		ColumnConfig netCerAmtColumnConfig = new ColumnConfig("Net Cer. Amt(excl. CC)", "netCerAmt", 120, false);
		netCerAmtColumnConfig.setAlign(TextAlign.RIGHT);
		netCerAmtColumnConfig.setRenderer(amountRender);
		
		
		ColumnConfig completionStatusColumnConfig = new ColumnConfig("Completion Status", "completionStatus" , 60, false);
		completionStatusColumnConfig.setHidden(true);
		
		ColumnConfig requisitionApprovedDateColumnConfig = new ColumnConfig("SC Requisition Approved Date", "requisitionApprovedDate" , 100, false);
		requisitionApprovedDateColumnConfig.setRenderer(new DateRenderer());
		requisitionApprovedDateColumnConfig.setHidden(true);
		ColumnConfig tenderAnalysisApprovalDateColumnConfig = new ColumnConfig("SC Tender Analysis Approved Date", "tenderAnalysisApprovedDate" , 100, false);
		tenderAnalysisApprovalDateColumnConfig.setRenderer(new DateRenderer());
		tenderAnalysisApprovalDateColumnConfig.setHidden(true);
		ColumnConfig preAwardMeetingDateColumnConfig = new ColumnConfig("Pre-Award Finalization Meeting Date", "preAwardMeetingDate" , 100, false);
		preAwardMeetingDateColumnConfig.setRenderer(new DateRenderer());
		preAwardMeetingDateColumnConfig.setHidden(true);
		ColumnConfig loaSignedDateColumnConfig = new ColumnConfig("LOA Signed Date", "loaSignedDate" , 100, false);
		loaSignedDateColumnConfig.setRenderer(new DateRenderer());
		loaSignedDateColumnConfig.setHidden(true);
		ColumnConfig scDocScrDateColumnConfig = new ColumnConfig("SC Doc executed by Subcontractor Date", "scDocScrDate" , 100, false);
		scDocScrDateColumnConfig.setRenderer(new DateRenderer());
		scDocScrDateColumnConfig.setHidden(true);
		ColumnConfig scDocLegalDateColumnConfig = new ColumnConfig("SC Doc executed by Legal Date", "scDocLegalDate" , 100, false);
		scDocLegalDateColumnConfig.setRenderer(new DateRenderer());
		scDocLegalDateColumnConfig.setHidden(true);
		ColumnConfig workCommenceDateColumnConfig = new ColumnConfig("Work Commence Date", "workCommenceDate" , 100, false);
		workCommenceDateColumnConfig.setRenderer(new DateRenderer());
		workCommenceDateColumnConfig.setHidden(true);
		ColumnConfig onSiteStartDateColumnConfig = new ColumnConfig("On Site Start Date", "onSiteStartDate" , 100, false);
		onSiteStartDateColumnConfig.setRenderer(new DateRenderer());
		onSiteStartDateColumnConfig.setHidden(true);
		ColumnConfig actualPCCDateColumnConfig = new ColumnConfig("Actual PCC Date", "actualPCCDate" , 100, false);
		actualPCCDateColumnConfig.setRenderer(new DateRenderer());
		actualPCCDateColumnConfig.setHidden(true);
		
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(recordDef);
		this.dataStore = new Store(proxy, reader);
		this.dataStore.load();
		resultEditorGridPanel.setStore(this.dataStore);
		this.dataStore.setSortInfo(new SortState("jobNumber",SortDir.ASC));
		
		ColumnConfig[] columns = new ColumnConfig[] {
				companyColumnConfig,
				divisionColumnConfig,
				jobNumberColumnConfig,
				jobDescriptionColumnConfig,
				soloJVColumnConfig,
				jvPercentColumnConfig,
				currencyColumnConfig,
				jobCompletionDateColumnConfig,
				clientNumberColumnConfig,
				subcontractNumberColumnConfig,
				subcontractorNameColumnConfig,
				subcontractorNumberColumnConfig,
				subcontractDescriptionColumnConfig,
				workScopeColumnConfig,
				splitTerminateStatusColumnConfig,
				paymentStatusColumnConfig,
				paymentTermsColumnConfig,
				scTermColumnConfig,
				subcontractorNatureColumnConfig,
				originalSubcontractSumColumnConfig,
				remeasuredSubcontractSumColumnConfig,
				addendumColumnConfig,
				revisedSubcontractSumColumnConfig,
				accumlatedRetentionColumnConfig,
				retentionReleasedColumnConfig,
				retentionBalanceColumnConfig,
				netCerAmtColumnConfig,
				totalCertAmtPrevColumnConfig,
				totalCertAmtCumColumnConfig,
				totalLiabilitiesColumnConfig,
				provisionColumnConfig,
				balanceToCompleteColumnConfig,
				totalCCPostedAmtColumnConfig,
				totalMOSPostedAmtColumnConfig,
				completionStatusColumnConfig,
				actualPCCDateColumnConfig,
				requisitionApprovedDateColumnConfig,
				tenderAnalysisApprovalDateColumnConfig,
				preAwardMeetingDateColumnConfig,
				loaSignedDateColumnConfig,
				scDocScrDateColumnConfig,
				scDocLegalDateColumnConfig,
				workCommenceDateColumnConfig,
				onSiteStartDateColumnConfig
		};

		resultEditorGridPanel.setColumnModel(new ColumnModel(columns));
		GridView view = new CustomizedGridView();
		view.setAutoFill(false);
		view.setForceFit(false);
		view.setEnableRowBody(true);
		resultEditorGridPanel.setView(view);
		
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(int pageNum){
				goToSubContractListPage(pageNum);
			}
		});
		resultEditorGridPanel.setBottomToolbar(paginationToolbar);
		paginationToolbar.setTotalPage(1);
		paginationToolbar.setCurrentPage(0);
		
		paginationToolbar.addFill();
		
		totalNoOfRecordsValueLabel = new ToolbarTextItem("<b>Total Number of Records: </b>");
		paginationToolbar.addItem(totalNoOfRecordsValueLabel);
		
		RowSelectionModel rowSelectionModel = new RowSelectionModel();
		resultEditorGridPanel.setSelectionModel(rowSelectionModel);
		
		add(resultEditorGridPanel);
	
	}
	
	private void goToSubContractListPage(int pageNum){
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.obtainSubcontractListByPage(pageNum, new AsyncCallback<SCListPaginationWrapper>(){
			public void onSuccess(SCListPaginationWrapper wrapper) {
				populateGrid(wrapper);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
	}
	
	public void populateGrid(SCListPaginationWrapper result) {
		Double totalRemeasuredSubcontractSum = 0.0;
		Double totalAddendum = 0.0;
		Double totalRevisedSubcontractSum = 0.0;
		Double totalLiabilities = 0.0;
		Double totalProvision = 0.0;
		Double totalBalanceToComplete = 0.0;
		Double totalPrevCertAmt = 0.0;
		Double totalCumCertAmt = 0.0;
		Double totalCCPostedAmt = 0.0;
		Double totalMOSPostedAmt = 0.0;
		Double totalAccumlatedRetentionAmt = 0.0;
		Double totalRetentionReleasedAmt = 0.0;
		Double totalRetentionBalanceAmt = 0.0;
		Double totalOriginalSubcontractSumAmt = 0.0;
		Double totalNetCertAmt = 0.0;

		this.dataStore.removeAll();

		if (result == null) {
			totalNoOfRecordsValueLabel.setText("<b>Total Number of Records: 0 </b>");
			paginationToolbar.setTotalPage(1);
			paginationToolbar.setCurrentPage(0);
			return;
		}
		paginationToolbar.setTotalPage(result.getTotalPage());
		paginationToolbar.setCurrentPage(result.getCurrentPage());

		totalNoOfRecordsValueLabel.setText("<b>Total Number of Records: " + result.getTotalRecords() + "</b>");

		List<SCListWrapper> wrapperList = result.getCurrentPageContentList();

		if (wrapperList.size() == 0) {
			if (!"".equals(monthComboBox.getText()) && !"".equals(yearTextField.getText().trim())) {
				MessageBox.alert("No data is found for Snapshot period:<br> " + "Month: " + monthComboBox.getText() + " - Year: " + yearTextField.getText().trim());
			} else {
				MessageBox.alert("No data is found");
			}
			return;
		}

		if (wrapperList.get(0).getSnapshotDate() != null)
			asOfDateItem.setText("As of " + DateUtil.formatDate(wrapperList.get(0).getSnapshotDate(), "dd-MM-yyyy"));

		data = new Record[wrapperList.size() + 1];
		for (int i = 0; i < wrapperList.size(); i++) {
			data[i] = recordDef.createRecord(new Object[] { wrapperList.get(i).getJobNumber(),
					wrapperList.get(i).getJobDescription(),
					wrapperList.get(i).getJobAnticipatedCompletionDate(),
					wrapperList.get(i).getClientNo(),
					wrapperList.get(i).getPackageNo(),
					wrapperList.get(i).getVendorNo(),
					wrapperList.get(i).getVendorName(),
					wrapperList.get(i).getDescription(),
					wrapperList.get(i).getWorkScope(),
					wrapperList.get(i).getRemeasuredSubcontractSum(),
					wrapperList.get(i).getAddendum(),
					wrapperList.get(i).getSubcontractSum(),
					wrapperList.get(i).getAccumlatedRetentionAmt(),
					wrapperList.get(i).getRetentionReleasedAmt(),
					wrapperList.get(i).getRetentionBalanceAmt(),

					wrapperList.get(i).getPaymentStatus(),
					wrapperList.get(i).getPaymentTerms(),
					wrapperList.get(i).getSubcontractTerm(),
					wrapperList.get(i).getSubcontractorNature(),
					wrapperList.get(i).getTotalLiabilities(),
					wrapperList.get(i).getTotalProvision(),
					wrapperList.get(i).getBalanceToComplete(),
					wrapperList.get(i).getTotalPostedCertAmt(),
					wrapperList.get(i).getTotalCumCertAmt(),
					wrapperList.get(i).getTotalCCPostedAmt(),
					wrapperList.get(i).getTotalMOSPostedAmt(),

					wrapperList.get(i).getRequisitionApprovedDate(),
					wrapperList.get(i).getTenderAnalysisApprovedDate(),
					wrapperList.get(i).getPreAwardMeetingDate(),
					wrapperList.get(i).getLoaSignedDate(),
					wrapperList.get(i).getScDocScrDate(),
					wrapperList.get(i).getScDocLegalDate(),
					wrapperList.get(i).getWorkCommenceDate(),
					wrapperList.get(i).getOnSiteStartDate(),
					wrapperList.get(i).getSplitTerminateStatus(),

					wrapperList.get(i).getCompany(),
					wrapperList.get(i).getDivision(),
					wrapperList.get(i).getSoloJV(),
					(wrapperList.get(i).getJvPercentage() == 0 ? "-" : wrapperList.get(i).getJvPercentage()),
					wrapperList.get(i).getActualPCCDate(),
					wrapperList.get(i).getCompletionStatus(),
					wrapperList.get(i).getCurrency(),
					wrapperList.get(i).getOriginalSubcontractSum(),
					wrapperList.get(i).getNetCertifiedAmount() });

			if (wrapperList.get(i).getRemeasuredSubcontractSum() != null)
				totalRemeasuredSubcontractSum += wrapperList.get(i).getRemeasuredSubcontractSum();
			if (wrapperList.get(i).getAddendum() != null)
				totalAddendum += wrapperList.get(i).getAddendum();
			if (wrapperList.get(i).getSubcontractSum() != null)
				totalRevisedSubcontractSum += wrapperList.get(i).getSubcontractSum();

			totalLiabilities += wrapperList.get(i).getTotalLiabilities() != null ? wrapperList.get(i).getTotalLiabilities() : new Double(0);
			totalProvision += wrapperList.get(i).getTotalProvision() != null ? wrapperList.get(i).getTotalProvision() : new Double(0);
			totalBalanceToComplete += wrapperList.get(i).getBalanceToComplete() != null ? wrapperList.get(i).getBalanceToComplete() : new Double(0);
			totalPrevCertAmt += wrapperList.get(i).getTotalPostedCertAmt() != null ? wrapperList.get(i).getTotalPostedCertAmt() : new Double(0);
			totalCumCertAmt += wrapperList.get(i).getTotalCumCertAmt() != null ? wrapperList.get(i).getTotalCumCertAmt() : new Double(0);
			totalCCPostedAmt += wrapperList.get(i).getTotalCCPostedAmt() != null ? wrapperList.get(i).getTotalCCPostedAmt() : new Double(0);
			totalMOSPostedAmt += wrapperList.get(i).getTotalMOSPostedAmt() != null ? wrapperList.get(i).getTotalMOSPostedAmt() : new Double(0);
			totalAccumlatedRetentionAmt += wrapperList.get(i).getAccumlatedRetentionAmt() != null && !wrapperList.get(i).getAccumlatedRetentionAmt().toString().trim().equals("") ? wrapperList.get(i).getAccumlatedRetentionAmt() : new Double(0);
			totalRetentionReleasedAmt += wrapperList.get(i).getRetentionReleasedAmt() != null && !wrapperList.get(i).getRetentionReleasedAmt().toString().trim().equals("") ? wrapperList.get(i).getRetentionReleasedAmt() : new Double(0);
			totalRetentionBalanceAmt += wrapperList.get(i).getRetentionBalanceAmt() != null && !wrapperList.get(i).getRetentionBalanceAmt().toString().trim().equals("") ? wrapperList.get(i).getRetentionBalanceAmt() : new Double(0);
			totalOriginalSubcontractSumAmt += wrapperList.get(i).getOriginalSubcontractSum() != null && !wrapperList.get(i).getOriginalSubcontractSum().toString().trim().equals("") ? wrapperList.get(i).getOriginalSubcontractSum() : new Double(0);
			totalNetCertAmt += wrapperList.get(i).getNetCertifiedAmount() != null && !wrapperList.get(i).getNetCertifiedAmount().toString().trim().equals("") ? wrapperList.get(i).getNetCertifiedAmount() : new Double(0);
		}

		data[wrapperList.size()] = recordDef.createRecord(new Object[] { "",
				"",
				null,
				"",
				"",
				"",
				"",
				"",
				"",
				totalRemeasuredSubcontractSum,
				totalAddendum,
				totalRevisedSubcontractSum,
				totalAccumlatedRetentionAmt,
				totalRetentionReleasedAmt,
				totalRetentionBalanceAmt,
				"",
				"",
				"",
				"<b>PAGE TOTAL</b>",
				totalLiabilities,
				totalProvision,
				totalBalanceToComplete,
				totalPrevCertAmt,
				totalCumCertAmt,
				totalCCPostedAmt,
				totalMOSPostedAmt,
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				totalOriginalSubcontractSumAmt,
				totalNetCertAmt });
		dataStore.add(data);

		dataStore.add(recordDef.createRecord(new Object[] { "",
				"",
				null,
				"",
				"",
				"",
				"",
				"",
				"",
				result.getOverallRemeasuredSubcontractSum(),
				result.getOverallAddendum(),
				result.getOverallRevisedSubcontractSum(),
				result.getOverallAccumlatedRetentionAmount(),
				result.getOverallRetentionReleasedAmount(),
				result.getOverallRetentionBalanceAmount(),

				"",
				"",
				"",
				"<b>GRAND TOTAL</b>",
				result.getOverallLiabilities(),
				result.getOverallProvision(),
				result.getOverallBalanceToComplete(),
				result.getOverallPostedCertAmount(),
				result.getOverallCumCertAmount(),
				result.getOverallCCPostedAmount(),
				result.getOverallMOSPostedAmount(),
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				result.getOverallOriginalSubcontractSumAmount(),
				result.getOverallNetCertAmount() }));

		if (!globalSectionController.getDetailSectionController().getMainPanel().isCollapsed())
			globalSectionController.getDetailSectionController().getMainPanel().collapse();

	}

	private void search(){
		String company = companyTextField.getText().trim();
		String division = divisionTextField.getText().trim();
		String jobNumber = jobNumberTextField.getText().trim();
		String subcontractNumber = subcontractNumberTextField.getText().trim();
		String subcontractorNumber = subcontractorNumberTextField.getText().trim();
		String subcontractorNature = subcontractorNatureComboBox.getText();
		String paymentStatus = paymentTypeComboBox.getText();
		String workScope = workScopeTextField.getText().trim();
		String clientNo = clientNoTextField.getText().trim(); 
		String splitTerminateStatus = splitTerminateStatusComboBox.getText();
		String month = monthComboBox.getText();
		String year = yearTextField.getText().trim();
		
		dataStore.removeAll();
		
		if(!completionDatecheckbox.getValue())
			resultEditorGridPanel.getColumnModel().setHidden(7, true);
		else
			resultEditorGridPanel.getColumnModel().setHidden(7, false);
		
		if((jobNumber==null || "".equals(jobNumber.trim())) 
			&&(subcontractorNumber==null || "".equals(subcontractorNumber.trim())) 
			&&(clientNo==null || "".equals(clientNo))
			&&(company==null || "".equals(company))
			&&(division==null || "".equals(division))
			&&(subcontractNumber==null || "".equals(subcontractNumber))
			&&(workScope==null || "".equals(workScope))){
			MessageBox.alert("Please fill in at least 1 search field.");
			return;
		}
		
		if(monthEndHistoryRadio.getValue()&&((!"".equals(month) && "".equals(year)) || (!"".equals(year) && "".equals(month)))){
			MessageBox.alert("Please fill in both month and year for searching Month End History.");
			return;
		}
		
		SubcontractListWrapper searchWrapper= new SubcontractListWrapper();
		searchWrapper.setCompany(company);
		searchWrapper.setDivision(division);
		searchWrapper.setJobNumber(jobNumber);
		searchWrapper.setSubcontractNo(subcontractNumber);
		searchWrapper.setSubcontractorNo(subcontractorNumber);
		searchWrapper.setSubcontractorNature(subcontractorNature);
		searchWrapper.setPaymentStatus(paymentStatus);
		searchWrapper.setWorkscope(workScope);
		searchWrapper.setClientNo(clientNo);
		searchWrapper.setIncludeJobCompletionDate(completionDatecheckbox.getValue());
		searchWrapper.setSplitTerminateStatus(splitTerminateStatus);
		searchWrapper.setMonth(month);
		searchWrapper.setYear(year);
		
		
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.obtainSubcontractListPaginationWrapper(searchWrapper, new AsyncCallback<SCListPaginationWrapper>(){

			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				UIUtil.checkSessionTimeout(e, false,user);
			}

			public void onSuccess(SCListPaginationWrapper result) {
				populateGrid(result);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
	}

	private void displayButtons(){
		if(accessRightsList != null){
			if(accessRightsList.contains("WRITE")){
				subcontractReportToolbarButton.show();
				downloadSubcontractExcelButton.show();
				downloadSubcontractPDFButton.show();
				
				subcontractLiabilityReportToolbarButton.show();
				downloadSubcontractLiabilityExcelButton.show();
				downloadSubcontractLiabilityPDFButton.show();
				
				subcontractorAnalysisReportToolbarButton.show();
				downloadsubcontractorAnalysisExcelButton.show();
				downloadsubcontractorAnalysisPDFButton.show();
				
				jobNumberTextField.enable();
			}
			else if(accessRightsList.contains("READ")){
				subcontractReportToolbarButton.show();
				downloadSubcontractExcelButton.show();
				downloadSubcontractPDFButton.show();
				
				subcontractLiabilityReportToolbarButton.show();
				downloadSubcontractLiabilityExcelButton.show();
				downloadSubcontractLiabilityPDFButton.show();
				
				subcontractorAnalysisReportToolbarButton.show();
				downloadsubcontractorAnalysisExcelButton.show();
				downloadsubcontractorAnalysisPDFButton.show();
			}
		}
		if(accessRightsListForUpdate!=null){
			if (accessRightsListForUpdate.contains("WRITE")){
				recalculateScTotalsButton.setVisible(true);
				sCDetailReportToolbarButton.setVisible(true);
			}
			if(accessRightsListForUpdate.contains("READ")){
				sCDetailReportToolbarButton.setVisible(true);
			}
		}
	}
	
	private String obtainParameters(){
		String parameters = "?company="+companyTextField.getText().trim()+
		"&division="+("E&M".equals(divisionTextField.getText().trim())?"EM":divisionTextField.getText().trim())+
		"&jobNumber="+jobNumberTextField.getText().trim()+
		"&subcontractNumber="+subcontractNumberTextField.getText().trim()+
		"&subcontractorNumber="+subcontractorNumberTextField.getText().trim()+
		"&subcontractorNature="+subcontractorNatureComboBox.getText()+
		"&paymentType="+paymentTypeComboBox.getText()+
		"&workScope="+workScopeTextField.getText().trim()+
		"&clientNo="+clientNoTextField.getText().trim()+
		"&includeJobCompletionDate="+completionDatecheckbox.getValue()+
		"&splitTerminateStatus="+splitTerminateStatusComboBox.getText()+
		"&month="+monthComboBox.getText()+
		"&year="+yearTextField.getText().trim();
		
		return parameters;
	}
	
}
