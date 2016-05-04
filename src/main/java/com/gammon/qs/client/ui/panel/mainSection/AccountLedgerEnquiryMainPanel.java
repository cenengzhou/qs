package com.gammon.qs.client.ui.panel.mainSection;

import java.util.Date;
import java.util.List;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
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
import com.gwtext.client.widgets.form.Label;
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
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowNumberingColumnConfig;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListener;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
/**
 * paulyiu
 * modified on 2015 07 30
 * Change from search panel to toolbar 
 */
public class AccountLedgerEnquiryMainPanel extends Panel{
	private static final String MAIN_PANEL_ID = "AccountLedgerEnquiryMainaPanel_ID";
	
	private GridPanel gridPanel;
	
	ToolbarButton searchButton;
	
	private Toolbar toolbar;
	private ToolbarButton downloadAccountLedgerReportButton;
	private Menu downloadAccountLedgerReportMenu;
	private Item downloadAccountLedgerExcel;
	private Item downloadAccountLedgerPDF;
	
	private AccountLedgerPaginationWrapper accountLedgerPaginationWrapper;
	private PaginationToolbar paginationToolbar;
	
	private Store dataStore;
	
	Renderer numberDP2 = new Renderer(){
		public String render(	Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
			String htmlAttr = "style='font-weight:bold;";
			if (Double.parseDouble(value.toString()) < 0) {
				htmlAttr += "color:red;";
			}
			cellMetadata.setHtmlAttribute(htmlAttr+"'");
			
			NumberFormat fmt = NumberFormat.getFormat("#,##0.00");
			value = fmt.format(Double.parseDouble(value.toString()));
			
			if(record.getAsString("explanation").equals("SUB TOTAL:") || record.getAsString("explanation").equals("TOTAL:"))
				return "<b>" + value + "</b>";
				
			return (value != null ? value.toString() : null);
		}
		
	};
	
	private final RecordDef accountLedgerRecordDef = new RecordDef(
			new FieldDef[]{
					new StringFieldDef("documentNumber"),
					new StringFieldDef("documentType"),
					new StringFieldDef("documentCompany"),
					new StringFieldDef("glDate"),
					new StringFieldDef("explanation"),
					
					new StringFieldDef("amount"),
					new StringFieldDef("postStatus"),
					new StringFieldDef("subLedgerType"),
					new StringFieldDef("subLedger"),
					new StringFieldDef("currencyCode"),
					
					new StringFieldDef("ledgerType"),
					new StringFieldDef("batchType"),
					new StringFieldDef("batchNumber"),
					new StringFieldDef("batchDate"),
					new StringFieldDef("transactionOriginator"),
					
					new StringFieldDef("userID"),
					new StringFieldDef("remark"),
					new StringFieldDef("poNumber"), // added by brian on 20110421
					new StringFieldDef("units")
			});
	
	
	private TextField accountCodeTextField;
	private TextField ledgerTypeTextField;
	private TextField subLedgerTextField;

	private DateField fromDateTextField;
	private DateField thruDateTextField;
	private ComboBox postStatusComboBox;
	private final Store storePostStatus = new SimpleStore(new String[]{"postValue", "postDisplay"}, getPostStatus());
	Label emptySpace = new Label();
	
	private static String[][] getPostStatus() {  
		return new String[][]{  
				new String[]{"posted", "Posted"},  
				new String[]{"unposted", "Unposted"},
				new String[]{"all", "All"}
		};  
	}

	private GlobalSectionController globalSectionController;

	private ToolbarTextItem totalAmount;

	private ToolbarTextItem totalNumberRecord;
	
	public AccountLedgerEnquiryMainPanel(GlobalSectionController globalSectionController, String accountCode, String ledgerType, String subLedger, String subLedgerType, Date fromDate, Date thruDate, String postFlag) {
		super();
		this.globalSectionController = globalSectionController;
		this.accountLedgerPaginationWrapper = new AccountLedgerPaginationWrapper();
		
		setTitle("Account Ledger Enquiry");
		setLayout(new RowLayout());
		//setupSearchPanel();
		
		setupToolbar();
		setupGridPanel();
		setSearchPanelTextfield(accountCode, ledgerType, subLedger, subLedgerType, fromDate, thruDate, postFlag);
		setId(MAIN_PANEL_ID);
		
		//hide detail panel
		globalSectionController.getDetailSectionController().getMainPanel().collapse();	
		
		searchAccountLedger(accountCode, ledgerType, subLedger, subLedgerType, postFlag, fromDate, thruDate);
	}

	
	public AccountLedgerEnquiryMainPanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		this.accountLedgerPaginationWrapper = new AccountLedgerPaginationWrapper();
		
		setTitle("Account Ledger Enquiry Job:"+globalSectionController.getJob().getJobNumber());
		setLayout(new RowLayout());
		
		setupToolbar();
		setupGridPanel();
		
		
		setId(MAIN_PANEL_ID);
		
		//hide detail panel
		globalSectionController.getDetailSectionController().getMainPanel().collapse();	
	}
	
	//add by paulyiu 20150730
	private void setupToolbar(){
		toolbar = new Toolbar();
		
		FieldListener searchListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER)
					newSearch();
			}
		};
		
		toolbar.addText("Account Code:");
		toolbar.addSpacer();
		accountCodeTextField = new TextField( "accountCodeSearch");
		accountCodeTextField.setWidth(120);;
		accountCodeTextField.addListener(searchListener);
		accountCodeTextField.setEmptyText("e.g. 13389.140299.29999999  ");
		ToolTip accountCodeTextFieldToolTip = new ToolTip();
		accountCodeTextFieldToolTip.setTitle("Account Code");
		accountCodeTextFieldToolTip.setHtml("Enter account code to search");
		accountCodeTextFieldToolTip.applyTo(accountCodeTextField);
		toolbar.addField(accountCodeTextField);
		toolbar.addSpacer();
		
		toolbar.addText("Ledger Type:");
		toolbar.addSpacer();
		ledgerTypeTextField = new TextField("ledgerTypeSearch");
		ledgerTypeTextField.setWidth(40);;
		ledgerTypeTextField.setEmptyText("AA  ");
		ToolTip ledgerTypeTextFieldToolTip = new ToolTip();
		ledgerTypeTextFieldToolTip.setTitle("Ledger Code");
		ledgerTypeTextFieldToolTip.setHtml("Enter ledger code to search");
		ledgerTypeTextFieldToolTip.applyTo(ledgerTypeTextField);
		ledgerTypeTextField.addListener(searchListener);
		ledgerTypeTextField.setValue("AA");
		toolbar.addField(ledgerTypeTextField);
		toolbar.addSpacer();
		
		toolbar.addText("Subcontract Number:");
		toolbar.addSpacer();
		subLedgerTextField = new TextField("subledgerSearch");
		subLedgerTextField.setWidth(60);
		subLedgerTextField.setEmptyText("e.g. 1001  ");
		ToolTip subLedgerTextFieldToolTip = new ToolTip();
		subLedgerTextFieldToolTip.setTitle("Subcontract Number");
		subLedgerTextFieldToolTip.setHtml("Enter subcontract number to search");
		subLedgerTextFieldToolTip.applyTo(subLedgerTextField);
		subLedgerTextField.addListener(searchListener);
		subLedgerTextField.setValidator(new Validator(){
			public boolean validate(String value) throws ValidationException {
				if(!value.matches("^[0-9\\*]*$")){
					MessageBox.alert("Please input number only");
					//subLedgerTextField.focus();
					//subLedgerTextField.selectText();
					return false;
				}
				return true;
			}
		});
		toolbar.addField(subLedgerTextField);
		toolbar.addSpacer();
		
		toolbar.addText("Post Code:");
		toolbar.addSpacer();
		postStatusComboBox = new ComboBox();
		storePostStatus.load();   
		postStatusComboBox.setForceSelection(true);  
		postStatusComboBox.setMinChars(1);  
		postStatusComboBox.setFieldLabel("Post Status");  
		postStatusComboBox.setStore(storePostStatus);  
		postStatusComboBox.setDisplayField("postDisplay");  
		postStatusComboBox.setValueField("postValue");
		postStatusComboBox.setMode(ComboBox.LOCAL);  
		postStatusComboBox.setTriggerAction(ComboBox.ALL);  
		postStatusComboBox.setEmptyText("Post Status"); 
		postStatusComboBox.setTypeAhead(true);  
		postStatusComboBox.setWidth(40);  
		postStatusComboBox.setValue("all");
		postStatusComboBox.addListener(searchListener);
		ToolTip postStatusComboBoxToolTip = new ToolTip();
		postStatusComboBoxToolTip.setTitle("Post Code");
		postStatusComboBoxToolTip.setHtml("Choose post code from the list");
		postStatusComboBoxToolTip.applyTo(postStatusComboBox);
		toolbar.addField(postStatusComboBox);
		toolbar.addSpacer();
		
		toolbar.addText("From Date:");
		toolbar.addSpacer();
		fromDateTextField = new DateField("From Date","fromDateSearch", 100);
		fromDateTextField.addListener(FieldFactory.updateDatePickerWidthListener());
		fromDateTextField.setFormat("d/m/y");
		fromDateTextField.setEmptyText("e.g. 01/01/2014  ");
		ToolTip fromDateTextFieldToolTip = new ToolTip();
		fromDateTextFieldToolTip.setTitle("From Date:");
		fromDateTextFieldToolTip.setHtml("Choose from the date picker<br>Filter: Match Exactly<br>Date only");
		fromDateTextFieldToolTip.applyTo(fromDateTextField);
		fromDateTextField.addListener(searchListener);
		toolbar.addField(fromDateTextField);
		toolbar.addSpacer();
		
		toolbar.addText("Thru Date:");
		toolbar.addSpacer();
		thruDateTextField = new DateField("Thru Date", "thruDateSearch", 100);
		thruDateTextField.addListener(FieldFactory.updateDatePickerWidthListener());
		thruDateTextField.setFormat("d/m/y");
		thruDateTextField.setEmptyText("e.g. 31/01/2014  ");
		ToolTip thruDateTextFieldToolTip = new ToolTip();
		thruDateTextFieldToolTip.setTitle("Thru Date:");
		thruDateTextFieldToolTip.setHtml("Choose from the date picker<br>Filter: Match Exactly<br>Date only");
		thruDateTextFieldToolTip.applyTo(thruDateTextField);
		thruDateTextField.addListener(searchListener);
		toolbar.addField(thruDateTextField);
		toolbar.addSeparator();
		
		searchButton = new ToolbarButton("Search");
		searchButton.setIconCls("find-icon");
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				newSearch();
			};
		});
		ToolTip searchButtonToolTip = new ToolTip();
		searchButtonToolTip.setTitle("Search:");
		searchButtonToolTip.setHtml("Press to search");
		searchButtonToolTip.applyTo(searchButton);
		toolbar.addButton(searchButton);
		toolbar.addSeparator();
		
		downloadAccountLedgerExcel = new Item("To Excel");
		downloadAccountLedgerExcel.setIconCls("excel-icon");
		downloadAccountLedgerExcel.addListener(downloadListener(GlobalParameter.ACCOUNT_LEDGER_REPORT_EXPORT_URL,"xls"));
		downloadAccountLedgerPDF = new Item("To PDF");
		downloadAccountLedgerPDF.setIconCls("pdf-icon");
		downloadAccountLedgerPDF.addListener(downloadListener(GlobalParameter.ACCOUNT_LEDGER_REPORT_EXPORT_URL,"pdf"));
		downloadAccountLedgerReportMenu = new Menu();
		downloadAccountLedgerReportMenu.addItem(downloadAccountLedgerExcel);
		downloadAccountLedgerReportMenu.addItem(downloadAccountLedgerPDF);
		downloadAccountLedgerReportButton = new ToolbarButton("Export");
		downloadAccountLedgerReportButton.setIconCls("download-icon");
		downloadAccountLedgerReportButton.setMenu(downloadAccountLedgerReportMenu);
		ToolTip downloadAccountLedgerReportButtonToolTip = new ToolTip();
		downloadAccountLedgerReportButtonToolTip.setTitle("Account Ledger Report");
		downloadAccountLedgerReportButtonToolTip.setHtml("Press to download report");
		downloadAccountLedgerReportButtonToolTip.applyTo(downloadAccountLedgerReportButton);
		toolbar.addButton(downloadAccountLedgerReportButton);
		toolbar.addSeparator();
		setTopToolbar(toolbar);
	}
		
	private BaseItemListener downloadListener(final String url,final String fileType) {
		BaseItemListenerAdapter listener = new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				//globalMessageTicket.refresh();
				downloadReport(url,fileType);
			}
		};
		return listener;
	}


	private void downloadReport(String url,String fileType) {
		String accountCode = accountCodeTextField.getText().trim();
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
					url
					+ "?jobNumber="+ globalSectionController.getJob().getJobNumber()
					+ "&accountCode=" + accountCode
					+ "&ledgerType=" + ledgerType
					+ "&subLedger=" + subLedger
					+ "&postFlag=" + postFlag
					+ "&fromDate=" + fromDate
					+ "&thruDate=" + thruDate
					+ "&fileType=" + fileType,"_blank", "");
	}
	
	private void setupGridPanel(){
		//////////////////
		//GRID PANEL
		//////////////////
		gridPanel = new GridPanel();
		
		//Document Number
		ColumnConfig documentNumberColConfig = new ColumnConfig("DOC Number", "documentNumber", 80, false);
		documentNumberColConfig.setSortable(true);
		//Document Type
		ColumnConfig documentTypeColConfig = new ColumnConfig("DOC Type", "documentType", 80, false);
		documentTypeColConfig.setSortable(true);
		//G/L Date
		ColumnConfig glDateColConfig = new ColumnConfig("G/L Date", "glDate", 100, false);
		glDateColConfig.setSortable(true);
		//Explanation
		ColumnConfig explanationColConfig = new ColumnConfig("Explanation", "explanation", 150, false);
		explanationColConfig.setSortable(true);
		//Amount
		ColumnConfig amountColConfig = new ColumnConfig("Amount", "amount", 120, false);
		amountColConfig.setRenderer(numberDP2);
		amountColConfig.setSortable(true);
		amountColConfig.setAlign(TextAlign.RIGHT);
		
		//Ledger Type
		ColumnConfig ledgerTypeColConfig = new ColumnConfig("Ledger Type", "ledgerType", 50, false);
		ledgerTypeColConfig.setSortable(true);
		//Post Code
		ColumnConfig postedCodeColConfig = new ColumnConfig("Post", "postStatus", 50, false);
		postedCodeColConfig.setSortable(true);
		//SubLedger
		ColumnConfig subLedgerColConfig = new ColumnConfig("SubLedger", "subLedger", 80, false);
		subLedgerColConfig.setSortable(true);
		//SubLedger Type
		ColumnConfig subLedgerTypeColConfig = new ColumnConfig("SubLedger Type", "subLedgerType", 80, false);
		subLedgerTypeColConfig.setSortable(true);
		//Document Company
		ColumnConfig documentCompanyColConfig = new ColumnConfig("Company", "documentCompany", 60, false);
		documentCompanyColConfig.setSortable(true);

		//Currency Code
		ColumnConfig currencyCodeColConfig = new ColumnConfig("Currency", "currencyCode", 50, false);
		currencyCodeColConfig.setSortable(true);
		//Remark
		ColumnConfig remarkColConfig = new ColumnConfig("Remark", "remark", 150, false);
		remarkColConfig.setSortable(true);
		
		// added by brian on 20110421 - start
		ColumnConfig poNumberColConfig = new ColumnConfig("Purchase Order Number", "poNumber", 50, true);
		// added by brian on 20110421 - end
		
		//Transaction Originator
		ColumnConfig transactionOriginatorColConfig = new ColumnConfig("Transaction Originator", "transactionOriginator", 80, false);
		transactionOriginatorColConfig.setSortable(true);
		//User ID
		ColumnConfig userIDColConfig = new ColumnConfig("User ID", "userID", 80, false);
		userIDColConfig.setSortable(true);
		//Batch Type
		ColumnConfig batchTypeColConfig = new ColumnConfig("Batch Type", "batchType", 50, false);
		batchTypeColConfig.setSortable(true);
		
		//Batch Number
		ColumnConfig batchNumberColConfig = new ColumnConfig("Batch Number", "batchNumber", 80, false);
		batchNumberColConfig.setSortable(true);
		//Batch Date
		ColumnConfig batchDateColConfig = new ColumnConfig("Batch Date", "batchDate", 100, false);
		batchDateColConfig.setSortable(true);
		
		ColumnConfig unitsColConfig = new ColumnConfig("Units","units",60,false);
		unitsColConfig.setRenderer(new QuantityRenderer(globalSectionController.getUser()));
	
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
				 poNumberColConfig, // added by brain on 20110421
				 
				 batchTypeColConfig,
				 batchNumberColConfig, 
				 batchDateColConfig 
		};
		
		//Put the columns in the grid
		gridPanel.setColumnModel(new ColumnModel(columns));
		GridView view = new CustomizedGridView();
		view.setForceFit(false);
		gridPanel.setView(view);
		
		//Pagination
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(final int pageNum){
				getAccountLedgerPaginationWrapperByPage(pageNum);			
			}
		});
		
		gridPanel.setBottomToolbar(paginationToolbar);
		if(accountLedgerPaginationWrapper.getCurrentPageContentList()==null || accountLedgerPaginationWrapper.getCurrentPageContentList().size()==0)
			paginationToolbar.setTotalPage(1);
		else
			paginationToolbar.setTotalPage(accountLedgerPaginationWrapper.getTotalPage());
		paginationToolbar.setCurrentPage(0);
		
		totalAmount = new ToolbarTextItem("<b>Total Amount:</b>");
		totalNumberRecord = new ToolbarTextItem("<b>Total Number of Records:0</b>");
		paginationToolbar.addFill();
		paginationToolbar.addItem(totalAmount);
		paginationToolbar.addSeparator();
		paginationToolbar.addItem(totalNumberRecord);
		//Setup data store(JDE --> data store)
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(accountLedgerRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		gridPanel.setStore(dataStore);
		
		add(gridPanel);
	}
	
	private void setSearchPanelTextfield(String accountCode, String ledgerType, String subLedger, String subLedgerType, Date fromDate, Date thruDate, String postFlag){	
		
		accountCodeTextField.setValue(accountCode);

		ledgerTypeTextField.setValue(ledgerType);

		subLedgerTextField.setValue(subLedger);
//		subLedgerTypeTextField.setValue(subLedgerType);

		fromDateTextField.setValue((new SimpleDateFormat("dd/MM/yy")).format(fromDate));
		thruDateTextField.setValue((new SimpleDateFormat("dd/MM/yy")).format(thruDate));
		if(postFlag==null)
			postStatusComboBox.setValue("all");
		else if(postFlag.equals(" "))
			postStatusComboBox.setValue("unposted");
		else if(postFlag.equals("P"))
			postStatusComboBox.setValue("posted");
	}
	
	public void newSearch(){
		String accountCode = accountCodeTextField.getText().trim();
		String ledgerType = ledgerTypeTextField.getText().trim().equals("")?null:ledgerTypeTextField.getText().trim();
//		String currencyCode = currencyCodeTextField.getText().trim();
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
			postFlag=null;//postFlag = "";	
		Date fromDate = fromDateTextField.getValue();
		Date thruDate = thruDateTextField.getValue();
		
		if(accountCode==null||ledgerType==null||fromDate==null||thruDate==null)
			MessageBox.alert("Please enter Account Code, Ledger Type, From Date and Thru Date.");
		else
			searchAccountLedger(accountCode, ledgerType, subLedger, subLedgerType, postFlag, fromDate, thruDate);
	}	
	
	public void populateGridByList(List<AccountLedgerWrapper> accountLedgerList){
		dataStore.removeAll();
		Double totalAmount = 0.00;
		for(AccountLedgerWrapper accountLedger: accountLedgerList){
			
			Record record = accountLedgerRecordDef.createRecord(new Object[]{
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
					accountLedger.getPurchaseOrder(), // added by brian on 20110421
					accountLedger.getUnits()
			});

			totalAmount+=accountLedger.getAmount();
			
			dataStore.add(record);
		}
		if(accountLedgerList!=null && accountLedgerList.size()>0){
		}
		else{
			this.paginationToolbar.setTotalPage(1);		//Display total page = 1
			this.paginationToolbar.setCurrentPage(0);	//Display current page = 1
			MessageBox.alert("No data was found.");
		}
	}
	
	public void populateGridByPage(AccountLedgerPaginationWrapper accountLedgerPaginationWrapper) {
		this.accountLedgerPaginationWrapper = accountLedgerPaginationWrapper;
		this.paginationToolbar.setTotalPage(accountLedgerPaginationWrapper.getTotalPage());
		this.paginationToolbar.setCurrentPage(accountLedgerPaginationWrapper.getCurrentPage());
		NumberFormat fmt = NumberFormat.getFormat("#,##0.00");
		populateGridByList(accountLedgerPaginationWrapper.getCurrentPageContentList());
		totalAmount.setText("<b>Total Amount:"+ fmt.format(accountLedgerPaginationWrapper.getTotalAmount()) + "</b>");
		totalNumberRecord.setText("<b>Total Number of Records:"+ accountLedgerPaginationWrapper.getTotalRecords()+"</b>");

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

	//Go to application server to get data page by page(not using web service at all)
	public void getAccountLedgerPaginationWrapperByPage(Integer pageNum) {
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.SEARCHING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getJobCostRepository().getAccountLedgerPaginationWrapperByPage(pageNum, new AsyncCallback<AccountLedgerPaginationWrapper>(){
			public void onSuccess(AccountLedgerPaginationWrapper accountLedgerPaginationWrapper) {
				populateGridByPage(accountLedgerPaginationWrapper);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
		
	}	

	public void searchAccountLedger(String accountCode, String ledgerType, String subLedger, String subLedgerType, String postFlag, Date fromDate, Date thruDate) {
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.SEARCHING_MSG, true);
		if(accountCode.substring(0, 5).equals(globalSectionController.getJob().getJobNumber()) || accountCode.substring(0, 5).equals(globalSectionController.getJob().getCompany())){	
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getJobCostRepository().getAccountLedgerByAccountCodeList(accountCode, postFlag, ledgerType, fromDate, thruDate, subLedgerType, subLedger, new AsyncCallback<AccountLedgerPaginationWrapper>(){
				public void onSuccess(AccountLedgerPaginationWrapper accountLedgerPaginationWrapper) {
					populateGridByPage(accountLedgerPaginationWrapper);
					UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					}
				public void onFailure(Throwable e) {
					UIUtil.throwException(e);
					UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					}
				});
		}
		else{
			MessageBox.alert("Please Switch the job before accessing Account Code["+accountCode+"].");
			UIUtil.unmaskPanelById(MAIN_PANEL_ID);
		}
		
	}
	
}
