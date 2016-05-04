package com.gammon.qs.client.ui.panel.mainSection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gammon.qs.application.User;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.JobRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.renderer.AmountRendererNonTotal;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.DateUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.RetentionRelease;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.RetentionReleaseScheduleEnquiryWindowsWrapper;
import com.gammon.qs.wrapper.RetentionReleaseSchedulePaginationWrapper;
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
import com.gwtext.client.widgets.grid.CellMetadata;
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
public class RetentionReleaseScheduleEnquiryMainPanel extends Panel {

	private static final String MAIN_PANEL_ID = "RetentionReleaseScheduleEnquiryMainPanel_ID";
	private final GlobalSectionController globalSectionController;
	private Store dataStore;
	
	private final RecordDef retentionReleaseRecordDef = new RecordDef(
			new FieldDef[]{
					new StringFieldDef("company"),
				//	new StringFieldDef("companyName"),
					new StringFieldDef("division"),
					new StringFieldDef("jobNo"),
					new StringFieldDef("jobDesc"),
					new StringFieldDef("currency"),
					new StringFieldDef("soloJV"),
					new StringFieldDef("projectContractValue"),
					new StringFieldDef("maxRetentionPercentage"),
					new StringFieldDef("estimatedRetention"),
					new StringFieldDef("actualPCCDate"),
					new StringFieldDef("completionDate"),
					new StringFieldDef("dlp"),
					new StringFieldDef("cumRetentionRec"),
					new StringFieldDef("receiptAmt"),
					new StringFieldDef("receiptDate"),
					new StringFieldDef("certNo"),
					new StringFieldDef("certStatus"),
					new StringFieldDef("outstandingAmt"),
					new StringFieldDef("dueDate"),
					new StringFieldDef("contractualDueDate"),
					new StringFieldDef("status")
			});
	private PaginationToolbar paginationToolbar;
	
	private ComboBox companyComboBox= new ComboBox();
	private ComboBox divisionComboBox= new ComboBox();
	private ComboBox jobNoComboBox= new ComboBox();
	private ComboBox statusComboBox = new ComboBox();
	private ComboBox certStatusComboBox = new ComboBox();
	
	private static final String COMPANY_VALUE = "companyValue";
	private static final String COMPANY_DISPLAY = "companyDisplay";
	private static final String DIVISION_VALUE = "divisionValue";
	private static final String DIVISION_DISPLAY = "divisionDisplay";
	private static final String JOB_NO_VALUE = "jobNoValue";
	private static final String JOB_NO_DISPLAY = "JobNoDisplay";
	private static final String STATUS_VALUE = "statusValue";
	private static final String STATUS_DISPLAY = "statusDisplay";
	private static final String CERT_STATUS_VALUE = "certStatusValue";
	private static final String CERT_STATUS_DISPLAY = "certStatusDisplay";
	protected static final Object COMBOBOX_DIVISION = "divisionComboBox";
	protected static final Object COMBOBOX_COMPANY = "companyComboBox";
	
	private String[][] companys = new String[][] {};
	private String[][] divisions = new String[][] {};
	private String[][] jobNos = new String[][] {};
	private String[][] status = new String[][]{};
	private String[][] certStatus = new String[][]{};
	
	private Store companyStore = new SimpleStore(new String[] { COMPANY_VALUE, COMPANY_DISPLAY }, companys);
	private Store divisionStore = new SimpleStore(new String[] { DIVISION_VALUE, DIVISION_DISPLAY }, divisions);
	private Store jobNoStore = new SimpleStore(new String[] { JOB_NO_VALUE, JOB_NO_DISPLAY }, jobNos);
	private Store statusStore = new SimpleStore(new String[]{STATUS_VALUE, STATUS_DISPLAY}, status);
	private Store certStatusStore = new SimpleStore(new String[]{CERT_STATUS_VALUE,CERT_STATUS_DISPLAY},certStatus);
	
	private List<String> divisionSuggestList = new ArrayList<String>();
	private Item downloadRetentionReleaseExcelButton;
	private Item downloadRetentionReleasePDFButton;
	private ToolbarButton retrentionReleaseReportToolbarButton;
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

	private String currentJobNo;

	public RetentionReleaseScheduleEnquiryMainPanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;

		globalMessageTicket = new GlobalMessageTicket();
		user = globalSectionController.getUser();
		currentJobNo = globalSectionController.getJob()!=null?globalSectionController.getJob().getJobNumber():"Please select a Job";
		jobRepository = globalSectionController.getJobRepository();
		setupToolbar();
		setupUI();
	}

	private void setupUI() {
		setLayout(new RowLayout());
		setPaddings(0);

		obtainCompanyStore();
		obtainDivisionStore();
		obtainJobNoStore();
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
				String jobNoDefaultValue = jobNoComboBox.isDisabled()?currentJobNo:"";
				if (field.getName().equals(COMBOBOX_COMPANY)){
					divisionComboBox.setValue("");
					jobNoComboBox.setValue(jobNoDefaultValue);
					statusComboBox.setValue("");
					certStatusComboBox.setValue("");
				}
				if(field.getName().equals(COMBOBOX_DIVISION)){
					jobNoComboBox.setValue(jobNoDefaultValue);
					statusComboBox.setValue("");
					certStatusComboBox.setValue("");
				}
				super.onChange(field, newVal, oldVal);
			}
		};
	
		toolbar.addText("Company:");
		toolbar.addSpacer();
		companyComboBox.setForceSelection(false);
		companyComboBox.setMinChars(1);
		companyComboBox.setName("companyComboBox");
//		companyComboBox.setValueField(COMPANY_VALUE);
		companyComboBox.setDisplayField(COMPANY_DISPLAY);
		companyComboBox.setMode(ComboBox.LOCAL);
		companyComboBox.setTriggerAction(ComboBox.ALL);
		companyComboBox.setEmptyText("All");
		companyComboBox.setTypeAhead(false);
		companyComboBox.setSelectOnFocus(true);
		companyComboBox.setWidth(70);
		companyComboBox.setListWidth(70);
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
//		divisionComboBox.setValueField(DIVISION_VALUE);
		divisionComboBox.setDisplayField(DIVISION_DISPLAY);
		divisionComboBox.setMode(ComboBox.LOCAL);
		divisionComboBox.setTriggerAction(ComboBox.ALL);
		divisionComboBox.setEmptyText("All");
		divisionComboBox.setTypeAhead(false);
		divisionComboBox.setSelectOnFocus(true);
		divisionComboBox.setWidth(60);
		divisionComboBox.setListWidth(60);
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
//		jobNoComboBox.setValueField(JOB_NO_VALUE);
		jobNoComboBox.setDisplayField(JOB_NO_DISPLAY);
		jobNoComboBox.setMode(ComboBox.LOCAL);
		jobNoComboBox.setTriggerAction(ComboBox.ALL);
		jobNoComboBox.setEmptyText("All");
		jobNoComboBox.setTypeAhead(true);
		jobNoComboBox.setSelectOnFocus(true);
		jobNoComboBox.setWidth(70);
		jobNoComboBox.setListWidth(70);
		jobNoComboBox.addListener(searchListener);
		jobNoComboBox.setStore(jobNoStore);
		jobNoComboBox.setDisabled(true);
		//jobNoComboBox.setValidator(numberOnlyValidator());
		ToolTip jobNoComboBoxToolTip = new ToolTip();
		jobNoComboBoxToolTip.setTitle("Job No.:");
		jobNoComboBoxToolTip.setHtml("Enter Job No. or <br>choose one from the list<br>Filter: Exactly<br>eg: search '13389' will return '13389' only");
		jobNoComboBoxToolTip.applyTo(jobNoComboBox);
		toolbar.addField(jobNoComboBox);
		toolbar.addSeparator();	
		
		toolbar.addText("Status:");
		toolbar.addSpacer();
		status = new String[][]{
				{"","All"},
				{RetentionRelease.STATUS_ACTUAL,"Actual"},
				{RetentionRelease.STATUS_FORECAST,"Forecast"}
		};
		statusStore = new SimpleStore(new String[]{STATUS_VALUE,STATUS_DISPLAY},status);
		statusComboBox.setForceSelection(false);
		statusComboBox.setMinChars(1);
		statusComboBox.setValueField(STATUS_VALUE);
		statusComboBox.setDisplayField(STATUS_DISPLAY);
		statusComboBox.setMode(ComboBox.LOCAL);
		statusComboBox.setTriggerAction(ComboBox.ALL);
		statusComboBox.setEmptyText("All");
		statusComboBox.setTypeAhead(true);
		statusComboBox.setSelectOnFocus(true);
		statusComboBox.setWidth(80);
		statusComboBox.setListWidth(80);
		statusComboBox.setStore(statusStore);
		statusStore.commitChanges();
		ToolTip statusComboBoxToolTip = new ToolTip();
		statusComboBoxToolTip.setTitle("Status.:");
		statusComboBoxToolTip.setHtml("Enter Status. or <br>choose one from the list");
		statusComboBoxToolTip.applyTo(statusComboBox);
		statusComboBox.addListener(searchListener);
		toolbar.addField(statusComboBox);
		toolbar.addSeparator();	
		
		toolbar.addText("Certificate Status:");
		toolbar.addSpacer();
		int certStatusSize = GlobalParameter.getMainCertficateStatusesFull(true).length;
		certStatus = new String[certStatusSize][2];
		for(int i=0;i<certStatusSize;i++){
			certStatus[i][0] = GlobalParameter.getMainCertficateStatusesFull(true)[i][0];
			if(GlobalParameter.getMainCertficateStatusesFull(true)[i][0].equals("")){
				certStatus[i][1] = GlobalParameter.getMainCertficateStatusesFull(true)[i][1];
			}else{
				certStatus[i][1] = certStatus[i][0] + " - " + GlobalParameter.getMainCertficateStatusesFull(true)[i][1];
			}
		}
		certStatusStore = new SimpleStore(new String[]{CERT_STATUS_VALUE,CERT_STATUS_DISPLAY},certStatus);
		certStatusComboBox.setForceSelection(false);
		certStatusComboBox.setMinChars(1);
		certStatusComboBox.setValueField(CERT_STATUS_VALUE);
		certStatusComboBox.setDisplayField(CERT_STATUS_DISPLAY);
		certStatusComboBox.setMode(ComboBox.LOCAL);
		certStatusComboBox.setTriggerAction(ComboBox.ALL);
		certStatusComboBox.setEmptyText("All");
		certStatusComboBox.setTypeAhead(true);
		certStatusComboBox.setSelectOnFocus(true);
		certStatusComboBox.setWidth(160);
		certStatusComboBox.setListWidth(250);
		certStatusComboBox.setStore(certStatusStore);
		certStatusStore.commitChanges();
		ToolTip certStatusComboBoxToolTip = new ToolTip();
		certStatusComboBoxToolTip.setTitle("Status.:");
		certStatusComboBoxToolTip.setHtml("Enter Certificate Status. or <br>choose one from the list");
		certStatusComboBoxToolTip.applyTo(certStatusComboBox);
		certStatusComboBox.addListener(searchListener);
		toolbar.addField(certStatusComboBox);
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
		
		downloadRetentionReleaseExcelButton = new Item("To Excel");
		downloadRetentionReleaseExcelButton.setIconCls("excel-icon");
		downloadRetentionReleaseExcelButton.addListener(downloadListener(GlobalParameter.RETENTION_RELEASE_SCHEDULE_REPORT_DOWNLOAD_URL, "xls"));

		downloadRetentionReleasePDFButton = new Item("To PDF");
		downloadRetentionReleasePDFButton.setIconCls("pdf-icon");
		downloadRetentionReleasePDFButton.addListener(downloadListener(GlobalParameter.RETENTION_RELEASE_SCHEDULE_REPORT_DOWNLOAD_URL, "pdf"));
		
		Menu reportMenu = new Menu();
		reportMenu.addItem(downloadRetentionReleaseExcelButton);
		reportMenu.addItem(downloadRetentionReleasePDFButton);
		retrentionReleaseReportToolbarButton = new ToolbarButton("Export");
		retrentionReleaseReportToolbarButton.setMenu(reportMenu);
		retrentionReleaseReportToolbarButton.setIconCls("download-icon");
		retrentionReleaseReportToolbarButton.setVisible(false);
		ToolTip retrentionReleaseReportToolbarButtonToolTip = new ToolTip();
		retrentionReleaseReportToolbarButtonToolTip.setTitle("Report export:");
		retrentionReleaseReportToolbarButtonToolTip.setHtml("Choose report type and click to download");
		retrentionReleaseReportToolbarButtonToolTip.applyTo(retrentionReleaseReportToolbarButton);
		toolbar.addButton(retrentionReleaseReportToolbarButton);
		toolbar.addSeparator();
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getUserAccessRightsRepository().getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010406_RETENTION_RELEASE_SCHEDULE_ENQUIRY_MAINPANEL, new AsyncCallback<List<String>>() {

			public void onSuccess(	List<String> secList) {
				if (secList.contains("WRITE")) {
					jobNoComboBox.setDisabled(false);
				} else {
					if (globalSectionController.getJob() != null && globalSectionController.getJob().getJobNumber() != null && !"".equals(globalSectionController.getJob().getJobNumber().trim())) {
						jobNoComboBox.setValue(globalSectionController.getJob().getJobNumber().trim());
					} else {
						jobNoComboBox.setValue(currentJobNo);
						MessageBox.alert("Please select job from the job list before search");
					}
				}
				searchButton.setVisible(true);
				retrentionReleaseReportToolbarButton.setVisible(true);
			}

			public void onFailure(	Throwable e) {
				UIUtil.alert(e.getMessage());
				UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
			}
		});
		setTopToolbar(toolbar);
	}

	private void cleanComboBox(ComboBox box){
		box.setValue("All");
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
		GridPanel resultPanel = new GridPanel();
		final Renderer amountRenderer = new AmountRendererNonTotal(globalSectionController.getUser());
		Renderer numberBoldRed = new Renderer() {
			public String render(	Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				String htmlAttr = "style='font-weight:bold;";
				if (value!=null && Double.parseDouble(value.toString()) < 0) {
					htmlAttr += "color:red;";
				}
				cellMetadata.setHtmlAttribute(htmlAttr+"'");
				value = amountRenderer.render(value, cellMetadata, record, rowIndex, colNum, store);

				return (value != null ? value.toString() : null);
			}

		};
		ColumnConfig companyColConfig = new ColumnConfig("Company","company",70,true);
		ColumnConfig divisionColConfig = new ColumnConfig("Division","division",70,true);
		ColumnConfig jobNoColConfig = new ColumnConfig("Job","jobNo",70,true);
		ColumnConfig jobDescColConfig = new ColumnConfig("Job Description","jobDesc",200,true);
		ColumnConfig estimatedRetentionColConfig = new ColumnConfig("Estimated Total Retention","estimatedRetention",140,true);
		estimatedRetentionColConfig.setRenderer(numberBoldRed);
		estimatedRetentionColConfig.setAlign(TextAlign.RIGHT);
		ColumnConfig projectContractValueColConfig = new ColumnConfig("Projected Contract Value","projectContractValue",140,true);
		projectContractValueColConfig.setRenderer(numberBoldRed);
		projectContractValueColConfig.setAlign(TextAlign.RIGHT);
		ColumnConfig maxRetentionPercentageColConfig = new ColumnConfig("Max Retention %","maxRetentionPercentage",140,true);
		maxRetentionPercentageColConfig.setRenderer(new RateRenderer(globalSectionController.getUser()));
		maxRetentionPercentageColConfig.setAlign(TextAlign.RIGHT);
		ColumnConfig completionDateColConfig = new ColumnConfig("Anticipated Completion Date","completionDate",150,true);
		ColumnConfig dlpColConfig = new ColumnConfig("DLP","dlp",45,true);
		dlpColConfig.setAlign(TextAlign.CENTER);
		ColumnConfig cumRetentionRecColConfig = new ColumnConfig("Cumulative Retention Receivable","cumRetentionRec",170,true);
		cumRetentionRecColConfig.setRenderer(numberBoldRed);
		cumRetentionRecColConfig.setAlign(TextAlign.RIGHT);
		ColumnConfig receiptAmtColConfig = new ColumnConfig("Receipt Amount","receiptAmt",100,true);
		receiptAmtColConfig.setRenderer(numberBoldRed);
		receiptAmtColConfig.setAlign(TextAlign.RIGHT);
		ColumnConfig receiptDateColConfig = new ColumnConfig("Receipt Date","receiptDate",90,true);
		ColumnConfig mainCertColConfig = new ColumnConfig("Cert No","certNo",65,true);
		mainCertColConfig.setAlign(TextAlign.CENTER);
		ColumnConfig outstandingAmtColConfig = new ColumnConfig("Outstanding Retention Receivable","outstandingAmt",170,true);
		outstandingAmtColConfig.setRenderer(numberBoldRed);
		outstandingAmtColConfig.setAlign(TextAlign.RIGHT);
		ColumnConfig dueDateColConfig = new ColumnConfig("Anticipated Due Date","dueDate",125,true);
		ColumnConfig contractualDueDateConfig = new ColumnConfig("Contractual Due Date","contractualDueDate",125,true);
		ColumnConfig statusColConfig = new ColumnConfig("Status","status",70,true);
		ColumnConfig companyNameColConfig = new ColumnConfig("Company Name","companyName",150,true);
		companyNameColConfig.setHidden(true);
		ColumnConfig currencyColConfig = new ColumnConfig("Currency","currency",60,true);
		ColumnConfig soloJVColConfig = new ColumnConfig("Solo/JV","soloJV",60,true);
		ColumnConfig actualPCCDateColConfig = new ColumnConfig("Actual PCC Date","actualPCCDate",100,true);
		ColumnConfig certStatusColConfig = new ColumnConfig("Certificate Status","certStatus",250,true);
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(retentionReleaseRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		resultPanel.setWidth(1400);
		resultPanel.setStore(dataStore);
		RowNumberingColumnConfig rowNumberColumnConfig = new RowNumberingColumnConfig();
		BaseColumnConfig[] columns = new BaseColumnConfig[]{
				rowNumberColumnConfig,
				companyColConfig,
			//	companyNameColConfig,
				divisionColConfig,
				jobNoColConfig,
				jobDescColConfig,
				currencyColConfig,
				soloJVColConfig,
				projectContractValueColConfig,
				maxRetentionPercentageColConfig,
				estimatedRetentionColConfig,
				actualPCCDateColConfig,
				completionDateColConfig,
				dlpColConfig,
				cumRetentionRecColConfig,
				receiptAmtColConfig,
				receiptDateColConfig,
				mainCertColConfig,
				certStatusColConfig,
				outstandingAmtColConfig,
				dueDateColConfig,
				contractualDueDateConfig,
				statusColConfig
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
				if (dataStore!=null && dataStore.getRecords()!=null && dataStore.getRecords().length>0)
					SessionTimeoutCheck.renewSessionTimer();
					globalSectionController.getRetentionReleaseScheduleRepository().obtainRetentionReleaseScheduleForPaginationWrapperByPage(pageNo, new AsyncCallback<RetentionReleaseSchedulePaginationWrapper>() {
						
						public void onSuccess(RetentionReleaseSchedulePaginationWrapper wrapperList) {
							populateGrid(wrapperList);
							UIUtil.unmaskPanelById(getId());
						}
						
						public void onFailure(Throwable e) {
							UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
							UIUtil.unmaskPanelById(getId());
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

	
	private void search() {
		String division = divisionComboBox.getValueAsString();
		String company = companyComboBox.getValueAsString();
		String jobNo = jobNoComboBox.getValueAsString();
		String status = statusComboBox.getValueAsString();
		String certStatus = certStatusComboBox.getValueAsString();
		if("".equals(company) && "".equals(division) && "".equals(jobNo) && "".equals(status) && "".equals(certStatus)){
			MessageBox.alert("Please fill in at least 1 search field.");
			return;
		}
		
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getRetentionReleaseScheduleRepository().obtainRetentionReleaseScheduleForPaginationWrapper(division, company, jobNo,status,certStatus, new AsyncCallback<RetentionReleaseSchedulePaginationWrapper>() {
			
			public void onSuccess(RetentionReleaseSchedulePaginationWrapper resultSet) {
				populateGrid(resultSet);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
	}
	
	public void populateGrid(RetentionReleaseSchedulePaginationWrapper resultSet){
		dataStore.removeAll();
		if (resultSet!=null && resultSet.getCurrentPageContentList()!=null && resultSet.getCurrentPageContentList().size()>0){
			paginationToolbar.setTotalPage(resultSet.getTotalPage());
			paginationToolbar.setCurrentPage(resultSet.getCurrentPage());
			totalRecordsTextItem.setText("<b>Records: " + resultSet.getTotalRecords() + "</b>");
			
			for (RetentionReleaseScheduleEnquiryWindowsWrapper wrapper:resultSet.getCurrentPageContentList()){
				String certStatusString = "";
				for(String[] s: GlobalParameter.getMainCertficateStatusesFull(false)){
					if(wrapper.getMainCertStatus().equals(s[0])){
						certStatusString = s[0] + " - " + s[1];
						break;
					}
				}
				
				dataStore.add(retentionReleaseRecordDef.createRecord(new Object[]{
						wrapper.getCompany(),
					//	wrapper.getCompanyName(),
						wrapper.getDivision(),
						wrapper.getJobNo(),
						wrapper.getJobDesc(),
						wrapper.getCurrency(),
						wrapper.getSoloJV(),
						wrapper.getProjectedContractValue(),
						wrapper.getMaxRetentionPercentage(),
						wrapper.getEstimatedRetention(),
						wrapper.getActualPCCDate()==null?"":DateUtil.formatDate(wrapper.getActualPCCDate(), GlobalParameter.DATE_FORMAT),
						wrapper.getCompletionDate()==null?"":DateUtil.formatDate(wrapper.getCompletionDate(), GlobalParameter.DATE_FORMAT) ,
						wrapper.getDlp(),
						wrapper.getCumRetentionRec(),
						wrapper.getReceiptAmt(),
						wrapper.getReceiptDate()==null?"":DateUtil.formatDate(wrapper.getReceiptDate(), GlobalParameter.DATE_FORMAT) ,
						wrapper.getMainCert(),
						certStatusString,
						wrapper.getOutstandingAmt(),
						wrapper.getDueDate()==null?"":DateUtil.formatDate(wrapper.getDueDate(), GlobalParameter.DATE_FORMAT),
						wrapper.getContractualDueDate()==null?"":DateUtil.formatDate(wrapper.getContractualDueDate(), GlobalParameter.DATE_FORMAT),
						RetentionRelease.STATUS_ACTUAL.equals(wrapper.getStatus())?"Actual":RetentionRelease.STATUS_FORECAST.equals(wrapper.getStatus())?"Forecast":"Cert not yet received"
				}));
			}
		}else{
			totalRecordsTextItem.setText("<b>Records: 0</b>");
		}
		
		//hide detail panel
		if(!globalSectionController.getDetailSectionController().getMainPanel().isCollapsed())
			globalSectionController.getDetailSectionController().getMainPanel().collapse();
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
				if (dataStore.getCount() == 0) {
					MessageBox.alert("This function will only download the items matching the search query. Please search before downloading");
					return;
				}
				String parameters = "?jobNumber=" + currentJobNo
									+ "&jobNo=" + encodeURIComponent(jobNoComboBox.getValueAsString())
									+ "&division=" + encodeURIComponent(divisionComboBox.getValueAsString())
									+ "&company=" + encodeURIComponent(companyComboBox.getValueAsString())
									+ "&status=" + encodeURIComponent(statusComboBox.getValueAsString())
									+ "&certStatus=" + encodeURIComponent(certStatusComboBox.getValueAsString())
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
					jobNos[0][1] = "All";
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
					divisions[0][1] = "All";
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
					companys[0][1] = "All";
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
