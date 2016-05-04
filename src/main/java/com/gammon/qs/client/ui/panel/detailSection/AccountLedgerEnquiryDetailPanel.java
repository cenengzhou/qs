package com.gammon.qs.client.ui.panel.detailSection;

import java.util.Date;
import java.util.List;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.panel.mainSection.AccountLedgerEnquiryMainPanel;
import com.gammon.qs.client.ui.renderer.AmountRendererCheckIfTotal;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.AccountLedgerWrapper;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.AccountLedgerPaginationWrapper;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.ObjectFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
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
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

/**
 * koeyyeung
 * Aug 21, 2013 9:39:55 AM
 */

public class AccountLedgerEnquiryDetailPanel extends GridPanel{
	//pagination toolbar
	private PaginationToolbar paginationToolbar;
	
	@SuppressWarnings("unused")
	private ToolbarButton excelToolbarButton;
	@SuppressWarnings("unused")
	private Item exportExcelButton;
	
	private Store dataStore;
	
	private static final String DOCUMENT_NUMBER_RECORD_NAME = "documentNumber";
	private static final String DOCUMENT_TYPE_RECORD_NAME = "documentType";
	private static final String DOCUMENT_COMPANY_RECORD_NAME = "documentCompany";
	private static final String GL_DATE_RECORD_NAME = "glDate";
	private static final String EXPLANATION_RECORD_NAME = "explanation";
	private static final String AMOUNT_RECORD_NAME = "amount";
	private static final String POST_STATUS_RECORD_NAME = "postStatus";
	private static final String SUBLEDGER_TYPE_RECORD_NAME = "subLedgerType";
	private static final String SUBLEDGER_RECORD_NAME = "subLedger";
	private static final String CURRENCY_CODE_RECORD_NAME = "currencyCode";
	private static final String LEDGER_TYPE_RECORD_NAME = "ledgerType";
	private static final String BATCH_TYPE_RECORD_NAME = "batchType";
	private static final String BATCH_NUMBER_RECORD_NAME = "batchNumber";
	private static final String BATCH_DATE_RECORD_NAME = "batchDate";
	private static final String TRANSACTION_ORIGINATOR_RECORD_NAME = "transactionOriginator";
	private static final String USER_ID_RECORD_NAME = "userID";
	private static final String REMARK_RECORD_NAME = "remark";
	private static final String PO_NUMBER_RECORD_NAME = "poNumber";
	private static final String UNITS_RECORD_NAME = "units";
	
	private RecordDef accountLedgerRecordDef = new RecordDef(
			new FieldDef[]{
					new ObjectFieldDef("Row Number"),
					new StringFieldDef(DOCUMENT_NUMBER_RECORD_NAME),
					new StringFieldDef(DOCUMENT_TYPE_RECORD_NAME),
					new StringFieldDef(DOCUMENT_COMPANY_RECORD_NAME),
					new StringFieldDef(GL_DATE_RECORD_NAME),
					new StringFieldDef(EXPLANATION_RECORD_NAME),
					
					new StringFieldDef(AMOUNT_RECORD_NAME),
					new StringFieldDef(POST_STATUS_RECORD_NAME),
					new StringFieldDef(SUBLEDGER_TYPE_RECORD_NAME),
					new StringFieldDef(SUBLEDGER_RECORD_NAME),
					new StringFieldDef(CURRENCY_CODE_RECORD_NAME),
					
					new StringFieldDef(LEDGER_TYPE_RECORD_NAME),
					new StringFieldDef(BATCH_TYPE_RECORD_NAME),
					new StringFieldDef(BATCH_NUMBER_RECORD_NAME),
					new StringFieldDef(BATCH_DATE_RECORD_NAME),
					new StringFieldDef(TRANSACTION_ORIGINATOR_RECORD_NAME),
					
					new StringFieldDef(USER_ID_RECORD_NAME),
					new StringFieldDef(REMARK_RECORD_NAME),
					new StringFieldDef(PO_NUMBER_RECORD_NAME), 
					new StringFieldDef(UNITS_RECORD_NAME)
			});
	
	//Search Fields
	private TextField ledgerTypeTextField;
	private TextField subLedgerTextField;

	private DateField fromDateTextField;
	private DateField thruDateTextField;
	private ComboBox postStatusComboBox;
	private static final String POST_DISPLAY_FIELD = "postDisplay";
	private static final String POST_VALUE_FIELD = "postValue";
	private final Store storePostStatus = new SimpleStore(new String[]{POST_VALUE_FIELD, POST_DISPLAY_FIELD}, getPostStatus());
	
	private static String[][] getPostStatus() {  
		return new String[][]{  
				new String[]{"posted", "Posted"},  
				new String[]{"unposted", "Unposted"},
				new String[]{"all", "All"}
		};  
	}
	
	private GlobalMessageTicket globalMessageTicket;
	private GlobalSectionController globalSectionController;
	private String detailSectionPanel_ID;
	private String accountCode;
	private String ledgerType;
	private String subLedger;
	private String subLedgerType;
	private Date fromDate;
	private Date thruDate;
	private String postFlag;

	private Item downloadVariationOrderExcelButton;

	private Item downloadVariationOrderPDFButton;

	private ToolbarButton accountLedgerEnquiryDetailToolbarButton;

	private ToolbarTextItem totalNumberRecordToolbarTextItem;

	private ToolbarTextItem totalAmountToolbarTextItem;
	public AccountLedgerEnquiryDetailPanel(GlobalSectionController globalSectionController, String accountCode, String ledgerType, String subLedger, String subLedgerType, Date fromDate, Date thruDate, String postFlag) {
		super();
		this.globalSectionController = globalSectionController;
		detailSectionPanel_ID = globalSectionController.getDetailSectionController().getMainPanel().getId();
		this.globalMessageTicket = new GlobalMessageTicket();
		
		this.accountCode = accountCode;
		this.ledgerType = ledgerType;
		this.subLedger = subLedger;
		this.subLedgerType = subLedgerType;
		this.fromDate = fromDate;
		this.thruDate = thruDate;
		this.postFlag = postFlag;
		setupGridPanel();
		
		searchAccountLedger(accountCode, ledgerType, subLedger, subLedgerType, postFlag, fromDate, thruDate);
	}
	

	private void setupToolbar() {
		Toolbar toolbar = new Toolbar();
		
		FieldListener fieldListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER){
					globalMessageTicket.refresh();
					newSearch();
				}
			}
		};

		//SubLedger	
		ToolbarTextItem subLedgerLabel = new ToolbarTextItem("Subcontract Number:");
		subLedgerTextField = new TextField();
		subLedgerTextField.setWidth(120);
		subLedgerTextField.addListener(fieldListener);
		subLedgerTextField.setValue(subLedger);
		ToolTip subLedgerTextFieldToolTip = new ToolTip();
		subLedgerTextFieldToolTip.setTitle("Subcontract Number:");
		subLedgerTextFieldToolTip.setHtml("Enter Subcontract Number to search<br>Filter: Match Exactly<br>Number only");
		subLedgerTextFieldToolTip.applyTo(subLedgerTextField);
		subLedgerTextField.setEmptyText("1001  ");
		subLedgerTextField.setValidator(new Validator(){

			public boolean validate(String value) throws ValidationException {
				if(!value.matches("^[0-9\\*]*$")){
					MessageBox.alert("Please input number only");
					return false;
				}
				return true;
			}
			
		});
		toolbar.addItem(subLedgerLabel);
		toolbar.addField(subLedgerTextField);
		toolbar.addSpacer();
		
		//Ledger Type
		ToolbarTextItem ledgerTypeLabel = new ToolbarTextItem("Ledger Type:");
		ledgerTypeTextField = new TextField();
		ledgerTypeTextField.setWidth(120);
		ledgerTypeTextField.addListener(fieldListener);
		ledgerTypeTextField.setValue(ledgerType);
		ToolTip ledgerTypeTextFieldToolTip = new ToolTip();
		ledgerTypeTextFieldToolTip.setTitle("Ledger Code");
		ledgerTypeTextFieldToolTip.setHtml("Enter ledger code to search");
		ledgerTypeTextFieldToolTip.applyTo(ledgerTypeTextField);
		ledgerTypeTextField.setEmptyText("AA  ");
		toolbar.addItem(ledgerTypeLabel);
		toolbar.addField(ledgerTypeTextField);
		toolbar.addSpacer();
		
		//Post Code
		ToolbarTextItem postLabel = new ToolbarTextItem("Post Code:");
		postStatusComboBox = new ComboBox();
		
		storePostStatus.load();
		postStatusComboBox.setForceSelection(true);  
		postStatusComboBox.setMinChars(1);  
		postStatusComboBox.setFieldLabel("Post Status");  
		postStatusComboBox.setStore(storePostStatus);  
		postStatusComboBox.setDisplayField(POST_DISPLAY_FIELD);  
		postStatusComboBox.setValueField(POST_VALUE_FIELD);
		postStatusComboBox.setMode(ComboBox.LOCAL);  
		postStatusComboBox.setTriggerAction(ComboBox.ALL);  
		postStatusComboBox.setEmptyText("Post Status"); 
		postStatusComboBox.setTypeAhead(true);  
		postStatusComboBox.setCtCls("table-cell");
		postStatusComboBox.setWidth(120);  
		postStatusComboBox.setValue("all");
		postStatusComboBox.addListener(fieldListener);
		
		if(postFlag==null)
			postStatusComboBox.setValue("all");
		else if(postFlag.equals(" "))
			postStatusComboBox.setValue("unposted");
		else if(postFlag.equals("P"))
			postStatusComboBox.setValue("posted");
		ToolTip postStatusComboBoxToolTip = new ToolTip();
		postStatusComboBoxToolTip.setTitle("Post Code");
		postStatusComboBoxToolTip.setHtml("Choose post code from the list");
		postStatusComboBoxToolTip.applyTo(postStatusComboBox);
		toolbar.addItem(postLabel);
		toolbar.addField(postStatusComboBox);
		toolbar.addSpacer();
		
		//From Date
		ToolbarTextItem fromDateLabel = new ToolbarTextItem("From Date:");
		fromDateTextField = new DateField("From Date", "fromDateSearch", 100);
		fromDateTextField.addListener(FieldFactory.updateDatePickerWidthListener());
		fromDateTextField.setFormat("d/m/y");
		fromDateTextField.addListener(fieldListener);
		fromDateTextField.setValue((new SimpleDateFormat("dd/MM/yy")).format(fromDate));
		ToolTip fromDateTextFieldToolTip = new ToolTip();
		fromDateTextFieldToolTip.setTitle("From Date:");
		fromDateTextFieldToolTip.setHtml("Choose from the date picker<br>Filter: Match Exactly<br>Date only");
		fromDateTextFieldToolTip.applyTo(fromDateTextField);
		toolbar.addItem(fromDateLabel);
		toolbar.addField(fromDateTextField);
		toolbar.addSpacer();
		
		//Thru Date
		ToolbarTextItem thruDateLabel = new ToolbarTextItem("Thru Date:");
		thruDateTextField = new DateField("Thru Date", "thruDateSearch", 100);
		thruDateTextField.addListener(FieldFactory.updateDatePickerWidthListener());
		thruDateTextField.setFormat("d/m/y");
		thruDateTextField.addListener(fieldListener);
		thruDateTextField.setValue((new SimpleDateFormat("dd/MM/yy")).format(thruDate));
		ToolTip thruDateTextFieldToolTip = new ToolTip();
		thruDateTextFieldToolTip.setTitle("Thru Date:");
		thruDateTextFieldToolTip.setHtml("Choose from the date picker<br>Filter: Match Exactly<br>Date only");
		thruDateTextFieldToolTip.applyTo(thruDateTextField);
		toolbar.addItem(thruDateLabel);
		toolbar.addField(thruDateTextField);
		toolbar.addSpacer();
		toolbar.addSeparator();
		
		//Search Button
		ToolbarButton searchButton = new ToolbarButton("Filter");
		searchButton.setIconCls("filter-icon");
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				newSearch();
			};
		});
		ToolTip searchButtonToolTip = new ToolTip();
		searchButtonToolTip.setTitle("Search:");
		searchButtonToolTip.setHtml("Press to search");
		searchButtonToolTip.applyTo(searchButton);
		toolbar.addButton(searchButton);
		toolbar.addSeparator();
		
		/*---------------------------Excel-----------------------------*/
//		exportExcelButton = new Item("Export");
//		exportExcelButton.setIconCls("download-icon");
//		exportExcelButton.addListener(new BaseItemListenerAdapter(){
//			public void onClick(BaseItem item, EventObject e) {
//				globalMessageTicket.refresh();
//				exportExcel();
//			}
//		});
//
//		Menu excelMenu = new Menu();
//		excelMenu.addItem(exportExcelButton);
//		
//		excelToolbarButton = new ToolbarButton("Excel");
//		excelToolbarButton.setMenu(excelMenu);
//		excelToolbarButton.setIconCls("excel-icon");
		/*---------------------------Excel-----------------------------*/

		downloadVariationOrderExcelButton = new Item("Export to Excel");
		downloadVariationOrderExcelButton.setIconCls("excel-icon");
		downloadVariationOrderExcelButton.addListener(downloadListener(GlobalParameter.ACCOUNT_LEDGER_REPORT_EXPORT_URL, "xls"));

		downloadVariationOrderPDFButton = new Item("Export to PDF");
		downloadVariationOrderPDFButton.setIconCls("pdf-icon");
		downloadVariationOrderPDFButton.addListener(downloadListener(GlobalParameter.ACCOUNT_LEDGER_REPORT_EXPORT_URL, "pdf"));
		
		Menu reportMenu = new Menu();
		reportMenu.addItem(downloadVariationOrderExcelButton);
		reportMenu.addItem(downloadVariationOrderPDFButton);
//		reportMenu.addItem(downloadPDFTest);
//		reportMenu.addItem(downloadXLSTest);
		accountLedgerEnquiryDetailToolbarButton = new ToolbarButton("Account Ledger Enquiry Report");
		accountLedgerEnquiryDetailToolbarButton.setMenu(reportMenu);
		accountLedgerEnquiryDetailToolbarButton.setIconCls("menu-show-icon");
		ToolTip accountLedgerEnquiryDetailtToolbarButtonToolTip = new ToolTip();
		accountLedgerEnquiryDetailtToolbarButtonToolTip.setTitle("Report export:");
		accountLedgerEnquiryDetailtToolbarButtonToolTip.setHtml("Choose report type and click to download");
		accountLedgerEnquiryDetailtToolbarButtonToolTip.applyTo(accountLedgerEnquiryDetailToolbarButton);
		
		toolbar.addButton(accountLedgerEnquiryDetailToolbarButton);
		toolbar.addSeparator();
		setTopToolbar(toolbar);
	}
	
	private BaseItemListenerAdapter downloadListener(	final String url, final String fileType) {
		return new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				if (dataStore.getCount() == 0) {
					MessageBox.alert("This function will only download the items matching the search query. Please search before downloading");
					return;
				}
				globalMessageTicket.refresh();
				
				String ledgerType = ledgerTypeTextField.getText().trim();
				String subLedger = subLedgerTextField.getText().trim();
				String postFlag = postStatusComboBox.getValueAsString().trim();	
				String fromDate = date2String(fromDateTextField.getValue());
				String thruDate = date2String(thruDateTextField.getValue());

				String parameters =  "?jobNumber="+ globalSectionController.getJob().getJobNumber()
						+ "&accountCode=" + accountCode
						+ "&ledgerType=" + ledgerType
						+ "&subLedger=" + subLedger
						+ "&postFlag=" + postFlag
						+ "&fromDate=" + fromDate
						+ "&thruDate=" + thruDate
						+ "&fileType=" + fileType;
				if(dataStore.getCount() == 0){
					MessageBox.alert("This function will only download the items matching the search query. Please search before downloading");
					return;
				}
				
				if(accountCode==null ||"".equals(accountCode)
						 || ledgerType==null || "".equals(ledgerType)
						 || fromDate==null || "".equals(fromDate)
						 || thruDate==null || "".equals(thruDate))
					MessageBox.alert("Please enter Account Code, Ledger Type, From Date and Thru Date.");
				else		
				com.google.gwt.user.client.Window.open(url + parameters, "_blank", "");
			}
		};
	}
	
	private void setupGridPanel(){
		setBorder(false);
		setFrame(false);
		setPaddings(0);
		setAutoScroll(true);
		setView(new CustomizedGridView());
		setupToolbar();
		
		Renderer colorRenderer = new Renderer() {
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				if(value!=null){
					if ("PX".equals(record.getAsString(DOCUMENT_TYPE_RECORD_NAME)) || "JE".equals(record.getAsString(DOCUMENT_TYPE_RECORD_NAME)))
						return "<font color="+GlobalParameter.ORANGE_COLOR+">"+value.toString()+"</font>";
				}else
					return "";
				return String.valueOf(value);
			}
		};
		
		final Renderer amountRenderer = new AmountRendererCheckIfTotal(globalSectionController.getUser());
		Renderer boldAmountRenderer = new Renderer() {
			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				String str = amountRenderer.render(value, cellMetadata, record, rowIndex, colNum, store);
				if(record.getAsString(EXPLANATION_RECORD_NAME).equals("SUB TOTAL:") || record.getAsString(EXPLANATION_RECORD_NAME).equals("TOTAL:"))
					return "<b>" + str + "</b>";
				else if ("PX".equals(record.getAsString(DOCUMENT_TYPE_RECORD_NAME)) || "JE".equals(record.getAsString(DOCUMENT_TYPE_RECORD_NAME)))
					return "<b><font color="+GlobalParameter.ORANGE_COLOR+">"+str+"</font><b>";
				if(value!= null && Double.valueOf(value.toString())<0)
					return "<b><font color="+GlobalParameter.RED_COLOR+">"+str+"</font></b>";
				return "<b>"+str+"</b>";
			}
		};
		
		//Document Number
		ColumnConfig documentNumberColConfig = new ColumnConfig("DOC Number", DOCUMENT_NUMBER_RECORD_NAME, 80, false);
		documentNumberColConfig.setSortable(true);
		documentNumberColConfig.setRenderer(colorRenderer);
		
		//Document Type
		ColumnConfig documentTypeColConfig = new ColumnConfig("DOC Type", DOCUMENT_TYPE_RECORD_NAME, 80, false);
		documentTypeColConfig.setSortable(true);
		documentTypeColConfig.setRenderer(colorRenderer);
		
		//G/L Date
		ColumnConfig glDateColConfig = new ColumnConfig("G/L Date", GL_DATE_RECORD_NAME, 100, false);
		glDateColConfig.setSortable(true);
		glDateColConfig.setRenderer(colorRenderer);
		
		//Explanation
		ColumnConfig explanationColConfig = new ColumnConfig("Explanation", EXPLANATION_RECORD_NAME, 220, false);
		explanationColConfig.setSortable(true);
		explanationColConfig.setRenderer(colorRenderer);
		
		//Amount
		ColumnConfig amountColConfig = new ColumnConfig("Amount", AMOUNT_RECORD_NAME, 120, false);
		amountColConfig.setRenderer(boldAmountRenderer);
		amountColConfig.setSortable(true);
		amountColConfig.setAlign(TextAlign.RIGHT);
		
		//Ledger Type
		ColumnConfig ledgerTypeColConfig = new ColumnConfig("Ledger Type", LEDGER_TYPE_RECORD_NAME, 80, false);
		ledgerTypeColConfig.setSortable(true);
		ledgerTypeColConfig.setRenderer(colorRenderer);
		
		//Post Code
		ColumnConfig postedCodeColConfig = new ColumnConfig("Post", POST_STATUS_RECORD_NAME, 50, false);
		postedCodeColConfig.setSortable(true);
		postedCodeColConfig.setRenderer(colorRenderer);
		
		//SubLedger
		ColumnConfig subLedgerColConfig = new ColumnConfig("SubLedger", SUBLEDGER_RECORD_NAME, 80, false);
		subLedgerColConfig.setSortable(true);
		subLedgerColConfig.setRenderer(colorRenderer);
		
		//SubLedger Type
		ColumnConfig subLedgerTypeColConfig = new ColumnConfig("SubLedger Type", SUBLEDGER_TYPE_RECORD_NAME, 80, false);
		subLedgerTypeColConfig.setSortable(true);
		//subLedgerTypeColConfig.setRenderer(colorRenderer);
		
		//Document Company
		ColumnConfig documentCompanyColConfig = new ColumnConfig("Company", DOCUMENT_COMPANY_RECORD_NAME, 60, false);
		documentCompanyColConfig.setSortable(true);
		//documentCompanyColConfig.setRenderer(colorRenderer);

		//Currency Code
		ColumnConfig currencyCodeColConfig = new ColumnConfig("Currency", CURRENCY_CODE_RECORD_NAME, 50, false);
		currencyCodeColConfig.setSortable(true);
		//currencyCodeColConfig.setRenderer(colorRenderer);
		
		//Remark
		ColumnConfig remarkColConfig = new ColumnConfig("Remark", REMARK_RECORD_NAME, 150, false);
		remarkColConfig.setSortable(true);
		//remarkColConfig.setRenderer(colorRenderer);
		
		ColumnConfig poNumberColConfig = new ColumnConfig("Purchase Order Number", PO_NUMBER_RECORD_NAME, 50, true);
		//poNumberColConfig.setRenderer(colorRenderer);
		
		//Transaction Originator
		ColumnConfig transactionOriginatorColConfig = new ColumnConfig("Transaction Originator", TRANSACTION_ORIGINATOR_RECORD_NAME, 80, false);
		transactionOriginatorColConfig.setSortable(true);
		//transactionOriginatorColConfig.setRenderer(colorRenderer);
		
		//User ID
		ColumnConfig userIDColConfig = new ColumnConfig("User ID", USER_ID_RECORD_NAME, 80, false);
		userIDColConfig.setSortable(true);
		//userIDColConfig.setRenderer(colorRenderer);
		
		//Batch Type
		ColumnConfig batchTypeColConfig = new ColumnConfig("Batch Type", BATCH_TYPE_RECORD_NAME, 50, false);
		batchTypeColConfig.setSortable(true);
		//batchTypeColConfig.setRenderer(colorRenderer);
		
		//Batch Number
		ColumnConfig batchNumberColConfig = new ColumnConfig("Batch Number", BATCH_NUMBER_RECORD_NAME, 80, false);
		batchNumberColConfig.setSortable(true);
		//batchNumberColConfig.setRenderer(colorRenderer);
		
		//Batch Date
		ColumnConfig batchDateColConfig = new ColumnConfig("Batch Date", BATCH_DATE_RECORD_NAME, 100, false);
		batchDateColConfig.setSortable(true);
		//batchDateColConfig.setRenderer(colorRenderer);
		
		ColumnConfig unitsColConfig = new ColumnConfig("Units",UNITS_RECORD_NAME,60,false);
		unitsColConfig.setRenderer(new QuantityRenderer(globalSectionController.getUser()));
		//unitsColConfig.setRenderer(colorRenderer);
		
		//hide columns
		subLedgerTypeColConfig.setHidden(true);
		documentCompanyColConfig.setHidden(true);
		currencyCodeColConfig.setHidden(true);
		remarkColConfig.setHidden(true);
		poNumberColConfig.setHidden(true);
		transactionOriginatorColConfig.setHidden(true);
		userIDColConfig.setHidden(true);
		batchTypeColConfig.setHidden(true);
		batchNumberColConfig.setHidden(true);
		batchDateColConfig.setHidden(true);
		unitsColConfig.setHidden(true);
		
		BaseColumnConfig[] columns = new BaseColumnConfig[]{
				new RowNumberingColumnConfig(),
				documentTypeColConfig,
				documentNumberColConfig,
				glDateColConfig,
				explanationColConfig,
				amountColConfig,

				ledgerTypeColConfig,
				postedCodeColConfig,
				subLedgerTypeColConfig,
				subLedgerColConfig,
				documentCompanyColConfig,

				currencyCodeColConfig,
				unitsColConfig,
				transactionOriginatorColConfig,
				userIDColConfig,	
				remarkColConfig,
				poNumberColConfig,

				batchTypeColConfig,
				batchNumberColConfig, 
				batchDateColConfig 
		};
		
		setColumnModel(new ColumnModel(columns));
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(accountLedgerRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		setStore(dataStore);
		
		this.addGridRowListener(new GridRowListenerAdapter(){
			public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
				navigateToAccountLedgerMainPanel();
			}
		});
		
		//Pagination
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(final int pageNum){
				getAccountLedgerPaginationWrapperByPage(pageNum);			
			}
		});
		totalAmountToolbarTextItem = new ToolbarTextItem("<b>Total Amount:</b>");
		totalNumberRecordToolbarTextItem = new ToolbarTextItem("<b>Total Number of Records:0</b>");
		paginationToolbar.addFill();
		paginationToolbar.addItem(totalAmountToolbarTextItem);
		paginationToolbar.addSeparator();
		paginationToolbar.addItem(totalNumberRecordToolbarTextItem);
		setBottomToolbar(paginationToolbar);
		paginationToolbar.setTotalPage(1);
		paginationToolbar.setCurrentPage(0);
		
		/*setBottomToolbar(paginationToolbar);
		if(accountLedgerPaginationWrapper.getCurrentPageContentList()==null || accountLedgerPaginationWrapper.getCurrentPageContentList().size()==0)
			paginationToolbar.setTotalPage(1);
		else
			paginationToolbar.setTotalPage(accountLedgerPaginationWrapper.getTotalPage());
		paginationToolbar.setCurrentPage(0);*/
	}
	
	public void getAccountLedgerPaginationWrapperByPage(Integer pageNum) {
		UIUtil.maskPanelById(detailSectionPanel_ID, GlobalParameter.SEARCHING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getJobCostRepository().getAccountLedgerPaginationWrapperByPage(pageNum, new AsyncCallback<AccountLedgerPaginationWrapper>(){
			public void onSuccess(AccountLedgerPaginationWrapper accountLedgerPaginationWrapper) {
				populateGridByPage(accountLedgerPaginationWrapper);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
		});
	}	
	
	public void searchAccountLedger(String accountCode, String ledgerType, String subLedger, String subLedgerType, String postFlag, Date fromDate, Date thruDate) {
		UIUtil.maskPanelById(detailSectionPanel_ID, GlobalParameter.SEARCHING_MSG, true);
		if(accountCode.substring(0, 5).equals(globalSectionController.getJob().getJobNumber()) || accountCode.substring(0, 5).equals(globalSectionController.getJob().getCompany())){	
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getJobCostRepository().getAccountLedgerByAccountCodeList(accountCode, postFlag, ledgerType, fromDate, thruDate, subLedgerType, subLedger, new AsyncCallback<AccountLedgerPaginationWrapper>(){
				public void onSuccess(AccountLedgerPaginationWrapper accountLedgerPaginationWrapper) {
					populateGridByPage(accountLedgerPaginationWrapper);
					UIUtil.unmaskPanelById(detailSectionPanel_ID);
					}
				public void onFailure(Throwable e) {
					UIUtil.throwException(e);
					UIUtil.unmaskPanelById(detailSectionPanel_ID);
					}
				});
		}
		else{
			MessageBox.alert("Please Switch the job before accessing Account Code["+accountCode+"].");
			UIUtil.unmaskPanelById(detailSectionPanel_ID);
		}
		
	}
	
	public void newSearch(){
		String ledgerType = ledgerTypeTextField.getText().trim().equals("")?null:ledgerTypeTextField.getText().trim();
		String subLedger = subLedgerTextField.getText().trim().equals("")?null:subLedgerTextField.getText().trim();
		String subLedgerType = null;
		if(subLedger!=null && !subLedger.equals(""))
			subLedgerType = "X";
		String postFlag = null;
		if(postStatusComboBox.getValueAsString().trim().equals("posted"))
			postFlag = "P";
		if(postStatusComboBox.getValueAsString().trim().equals("unposted"))
			postFlag = " ";
		if(postStatusComboBox.getValueAsString().trim().equals("all"))
			postFlag=null;
		Date fromDate = fromDateTextField.getValue();
		Date thruDate = thruDateTextField.getValue();
		
		if(accountCode==null||ledgerType==null||fromDate==null||thruDate==null)
			MessageBox.alert("Please enter Account Code, Ledger Type, From Date and Thru Date.");
		else
			searchAccountLedger(accountCode, ledgerType, subLedger, subLedgerType, postFlag, fromDate, thruDate);
	}	
	
	public void populateGridByPage(AccountLedgerPaginationWrapper accountLedgerPaginationWrapper) {
		dataStore.removeAll();
		Double totalAmount = 0.00;

		paginationToolbar.setTotalPage(accountLedgerPaginationWrapper.getTotalPage());
		paginationToolbar.setCurrentPage(accountLedgerPaginationWrapper.getCurrentPage());
		NumberFormat fmt = NumberFormat.getFormat("#,##0.00");
		totalAmountToolbarTextItem.setText("<b>Total Amount:"+ fmt.format(accountLedgerPaginationWrapper.getTotalAmount()) + "</b>");
		totalNumberRecordToolbarTextItem.setText("<b>Total Number of Records:"+ accountLedgerPaginationWrapper.getTotalRecords()+"</b>");

		List<AccountLedgerWrapper> accountLedgerList = accountLedgerPaginationWrapper.getCurrentPageContentList();
		
		if(accountLedgerList==null || accountLedgerList.size()==0){
			paginationToolbar.setTotalPage(1);		//Display total page = 1
			paginationToolbar.setCurrentPage(0);	//Display current page = 1
		}
		
		for(AccountLedgerWrapper accountLedger: accountLedgerList){
			Record record = accountLedgerRecordDef.createRecord(new Object[]{
					null,
					accountLedger.getDocumentNumber(),
					accountLedger.getDocumentType(),
					accountLedger.getDocumentCompany(),
					date2String(accountLedger.getGlDate()),
					accountLedger.getExplanation(),

					accountLedger.getAmount(),
					accountLedger.getPostedCode(),
					accountLedger.getSubledgerType(),
					accountLedger.getSubledger(),
					accountLedger.getCurrencyCode(),

					accountLedger.getLedgerType(),
					accountLedger.getBatchType(),
					accountLedger.getBatchNumber(),
					date2String(accountLedger.getBatchDate()),
					accountLedger.getTransactionOriginator(),

					accountLedger.getUserId(),
					accountLedger.getExplanationRemark(),
					accountLedger.getPurchaseOrder(),
					accountLedger.getUnits()
			});
			totalAmount+=accountLedger.getAmount();
			dataStore.add(record);
		}
		
//		if(paginationToolbar.getCurrentPageField().getValue().intValue() == Integer.parseInt(paginationToolbar.getTotalPageText().getText())){
//			Record totalRecord = accountLedgerRecordDef.createRecord(new Object[]{
//					"",
//					"",
//					"",
//					"",
//					"TOTAL:",
//					
//					accountLedgerPaginationWrapper.getTotalAmount(),
//					"",
//					"",
//					"",
//					"",
//					
//					"",
//					"",
//					"",
//					"",
//					"",
//					"",
//					
//					"",
//					"",
//					""
//			});
//			dataStore.add(totalRecord);
//		}
	}
	
	@SuppressWarnings("unused")
	private void exportExcel(){
		String ledgerType = ledgerTypeTextField.getText().trim();
		String subLedger = subLedgerTextField.getText().trim();
		
		String postFlag = postStatusComboBox.getValueAsString().trim();	
		
		String fromDate = date2String(fromDateTextField.getValue());
		String thruDate = date2String(thruDateTextField.getValue());
		
		if(dataStore.getCount() == 0){
			MessageBox.alert("This function will only download the items matching the search query. Please search before downloading");
			return;
		}
		
		if(accountCode==null ||"".equals(accountCode)
				 || ledgerType==null || "".equals(ledgerType)
				 || fromDate==null || "".equals(fromDate)
				 || thruDate==null || "".equals(thruDate))
			MessageBox.alert("Please enter Account Code, Ledger Type, From Date and Thru Date.");
		else		
			com.google.gwt.user.client.Window.open(
					GlobalParameter.ACCOUNT_LEDGER_EXCEL_DOWNLOAD_URL
					+ "?accountCode=" + accountCode
					+ "&ledgerType=" + ledgerType
					+ "&subLedger=" + subLedger
					+ "&postFlag=" + postFlag
					+ "&fromDate=" + fromDate
					+ "&thruDate=" + thruDate,"_blank", "");
	}

	private void navigateToAccountLedgerMainPanel() {
		MessageBox.confirm("", "You are going to navigate to Account Ledger Enquiry, are you sure you want to proceed?",
				new MessageBox.ConfirmCallback() {
			public void execute(String btnID) {
				if (btnID.equals("yes")){
					globalSectionController.getMainSectionController().resetMainPanel();
					AccountLedgerEnquiryMainPanel accountLedgerEnquiryMainPanel = new AccountLedgerEnquiryMainPanel(globalSectionController, accountCode, ledgerType, subLedger, subLedgerType, fromDate, thruDate, postFlag);
					globalSectionController.getMainSectionController().setContentPanel(accountLedgerEnquiryMainPanel);
					globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();					
					
				}
			}
		});
	}
	
	private String date2String(Date date){
		if (date!=null)
			return (new SimpleDateFormat("dd/MM/yyyy")).format(date);
		else
			return " ";
	}
	
	/*private void highlightText(AccountLedgerWrapper accountLedger, String color){
		Record record = accountLedgerRecordDef.createRecord(new Object[]{
				setColor(accountLedger.getDocumentNumber()==null?"":accountLedger.getDocumentNumber().toString(), color),
				setColor(accountLedger.getDocumentType(), color),
				setColor(accountLedger.getDocumentCompany(), color),
				setColor(date2String(accountLedger.getGlDate()), color),
				setColor(accountLedger.getExplanation(), color),

				accountLedger.getAmount(),
				setColor(accountLedger.getPostedCode(), color),
				setColor(accountLedger.getSubledgerType(), color),
				setColor(accountLedger.getSubledger(), color),
				setColor(accountLedger.getCurrencyCode(), color),

				setColor(accountLedger.getLedgerType(), color),
				setColor(accountLedger.getBatchType(), color),
				setColor(accountLedger.getBatchNumber()==null?"":accountLedger.getBatchNumber().toString(), color),
				setColor(date2String(accountLedger.getBatchDate()), color),
				setColor(accountLedger.getTransactionOriginator(), color),

				setColor(accountLedger.getUserId(), color),
				setColor(accountLedger.getExplanationRemark(), color),
				setColor(accountLedger.getPurchaseOrder(), color),
				setColor(accountLedger.getUnits()==null?"":accountLedger.getUnits().toString(), color)
		});
		dataStore.add(record);
	}*/
	
	/*private String setColor(String string, String color){
			return "<font color="+color+">"+ string+"</font>";
	}*/
	
}
