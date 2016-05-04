package com.gammon.qs.client.ui.panel.mainSection;

import java.util.Date;
import java.util.List;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.gammon.qs.client.controller.DetailSectionController;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.panel.detailSection.PaymentHistoriesDetialPanel;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.APRecord;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.APRecordPaginationWrapper;
import com.gammon.qs.wrapper.PaymentHistoriesWrapper;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
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
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowNumberingColumnConfig;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
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
public class APRecordEnquiryMainPanel extends Panel{
	private static final String APR_RECORD_ENQUIRY_MAIN_PANEL_ID = "APRecordEnquiryMainPanel_ID";
	private static String DETAIL_SECTION_PANEL_ID = "";
	
	private GlobalSectionController globalSectionController;
	private DetailSectionController detailSectionController;
	
	private GridPanel gridPanel;
	
	private Store dataStore;
	
	private APRecordPaginationWrapper apRecordPaginationWrapper;
	private PaginationToolbar paginationToolbar;
	private RecordDef apRecordDef = new RecordDef(
			new FieldDef[]{
					new StringFieldDef("documentNumber"),
					new StringFieldDef("documentType"),
					new StringFieldDef("invoiceDate"),
					new StringFieldDef("glDate"),
					new StringFieldDef("dueDate"),
					
					new StringFieldDef("grossAmount"),
					new StringFieldDef("openAmount"),
					new StringFieldDef("foreignAmount"),
					new StringFieldDef("foreignAmountOpen"),
					new StringFieldDef("payStatus"),
					
					new StringFieldDef("supplierNumber"),
					new StringFieldDef("invoiceNumber"),
					new StringFieldDef("company"),
					new StringFieldDef("batchNumber"),
					new StringFieldDef("batchType"),
					
					new StringFieldDef("batchDate"),
					new StringFieldDef("currencyCode"),
					new StringFieldDef("subledger"),
					new StringFieldDef("subledgerType"),
			});
	
	
	private TextField jobNumberTextField;
	private TextField invoiceNumberTextField;
	private TextField supplierNumberTextField;
	private TextField documentNumberTextField;
	private TextField documentTypeTextField;
	private TextField subledgerTextField;
	
	// added by brian on 20110209 - START
	private Integer gridPanelRowIndex;
	//private Button downloadButton;
	// added by brian on 20110209 - END
	
	private Toolbar toolbar;
	private ToolbarButton downloadAPRecordReportButton;
	private Menu downloadAPRecordReportMenu;
	private Item downloadAPRecordExcel;
	private Item downloadAPRecordPDF;
	private ToolbarTextItem totalGrossAmount;
	private ToolbarTextItem totalOpenAmount;
	private ToolbarTextItem totalForeignAmount;
	private ToolbarTextItem totalForeignOpenAmount;
	private ToolbarTextItem totalNumberRecord;
	private Renderer numberDP2 = new Renderer(){
		public String render(	Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
			String htmlAttr = "style='font-weight:bold;";
			if (Double.parseDouble(value.toString()) < 0) {
				htmlAttr += "color:red;";
			}
			cellMetadata.setHtmlAttribute(htmlAttr+"'");
			
			NumberFormat fmt = NumberFormat.getFormat("#,##0.00");
			value = fmt.format(Double.parseDouble(value.toString()));
			
			if(record.getAsString("documentNumber").equals("Total:"))
				return "<b>" + value + "</b>";
				
			return (value != null ? value.toString() : null);
		}
		
	};
	public APRecordEnquiryMainPanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		this.detailSectionController = globalSectionController.getDetailSectionController();
		DETAIL_SECTION_PANEL_ID = detailSectionController.getMainPanel().getId();
		
		apRecordPaginationWrapper = new APRecordPaginationWrapper();
		
		setupUI();
	}
	
	private void setupUI() {
		setLayout(new RowLayout());
		
		setupToolbar();
		setupGridPanel();
		
		setId(APR_RECORD_ENQUIRY_MAIN_PANEL_ID);
	}
	
	//add by paulyiu 20150730
		private void setupToolbar(){
			toolbar = new Toolbar();
			
			FieldListener searchListener = new FieldListenerAdapter(){
				public void onSpecialKey(Field field, EventObject e){
					if(e.getKey() == EventObject.ENTER)
						newSearch();
				}
				@Override
				public void onValid(Field field) {
					if (field.getName().equals(subledgerTextField.getName())){
						supplierNumberTextField.setValue("");
					}
					super.onValid(field);
				}
			};
			
			toolbar.addText("Job No.:");
			jobNumberTextField = new TextField("Job No.", "jobNumberSearch", 50);
			jobNumberTextField.setValue(globalSectionController.getJob().getJobNumber());
			jobNumberTextField.setDisabled(true);
			toolbar.addField(jobNumberTextField);
			toolbar.addSpacer();
			
			toolbar.addText("Subcontract No.:");
			subledgerTextField = new TextField("Subledger", "subledgerSearch", 60);
			subledgerTextField.setEmptyText("e.g. 1001  ");
			ToolTip subLedgerTextFieldToolTip = new ToolTip();
			subLedgerTextFieldToolTip.setTitle("Subcontract Number");
			subLedgerTextFieldToolTip.setHtml("Enter subcontract number to search");
			subLedgerTextFieldToolTip.applyTo(subledgerTextField);
			subledgerTextField.setValidator(new Validator(){
				public boolean validate(String value) throws ValidationException {
					if(!value.matches("^[0-9]*$")){
						MessageBox.alert("Please input number only");
						return false;
					}
					return true;
				}
				
			});
			subledgerTextField.addListener(searchListener);
			subledgerTextField.setValidateOnBlur(false);
			toolbar.addField(subledgerTextField);
			toolbar.addSpacer();
			
			toolbar.addText("Supplier No.:");
			supplierNumberTextField = new TextField("Supplier Number", "supplierNumberSearch", 60);
			supplierNumberTextField.setEmptyText("e.g. 30167  ");
			supplierNumberTextField.setValidator(new Validator(){

				public boolean validate(String value) throws ValidationException {
					if(!value.matches("^[0-9]*$")){
						MessageBox.alert("Please input number only");
						return false;
					}
					return true;
				}
				
			});
			ToolTip supplierNumberTextFieldToolTip = new ToolTip();
			supplierNumberTextFieldToolTip.setTitle("Supplier Number");
			supplierNumberTextFieldToolTip.setHtml("Enter supplier number to search");
			supplierNumberTextFieldToolTip.applyTo(supplierNumberTextField);
			supplierNumberTextField.addListener(searchListener);
			toolbar.addField(supplierNumberTextField);
			toolbar.addSpacer();
			
			toolbar.addText("Invoice No.:");
			invoiceNumberTextField = new TextField("Invoice Number", "invoiceNumberSearch", 120);
			invoiceNumberTextField.setEmptyText("e.g. 13389/1001/0001  ");
			ToolTip invoiceNumberTextFieldToolTip = new ToolTip();
			invoiceNumberTextFieldToolTip.setTitle("Invoice Number");
			invoiceNumberTextFieldToolTip.setHtml("Enter invoice number to search");
			invoiceNumberTextFieldToolTip.applyTo(invoiceNumberTextField);
			invoiceNumberTextField.addListener(searchListener);
			toolbar.addField(invoiceNumberTextField);
			toolbar.addSpacer();
			
			toolbar.addText("Document No.:");
			documentNumberTextField = new TextField("Document No.", "documentNumberSearch", 80);
			documentNumberTextField.setEmptyText("e.g. 1466613  ");
			documentNumberTextField.setValidator(new Validator(){

				public boolean validate(String value) throws ValidationException {
					if(!value.matches("^[0-9]*$")){
						MessageBox.alert("Please input number only");
						//documentNumberTextField.focus();
						//documentNumberTextField.selectText();
						return false;
					}
					return true;
				}
				
			});
			ToolTip documentNumberTextFieldToolTip = new ToolTip();
			documentNumberTextFieldToolTip.setTitle("Document Number");
			documentNumberTextFieldToolTip.setHtml("Enter document number to search");
			documentNumberTextFieldToolTip.applyTo(documentNumberTextField);
			documentNumberTextField.addListener(searchListener);
			toolbar.addField(documentNumberTextField);
			toolbar.addSpacer();
			
			toolbar.addText("Document Type:");
			documentTypeTextField = new TextField("Document Type", "documentTypeSearch", 50);
			documentTypeTextField.setEmptyText("e.g. PS  ");
			ToolTip documentTypeTextFieldToolTip = new ToolTip();
			documentTypeTextFieldToolTip.setTitle("Document Type");
			documentTypeTextFieldToolTip.setHtml("Enter document type to search");
			documentTypeTextFieldToolTip.applyTo(documentTypeTextField);
			documentTypeTextField.addListener(searchListener);
			toolbar.addField(documentTypeTextField);
			toolbar.addSeparator();
			
			ToolbarButton searchButton = new ToolbarButton("Search");
			searchButton.setIconCls("find-icon");
			searchButton.addListener(new ButtonListenerAdapter(){
				public void onClick(Button button, EventObject e) {
					newSearch();
				};
			});
			ToolTip searchButtonToolTip = new ToolTip();
			searchButtonToolTip.setTitle("Search");
			searchButtonToolTip.setHtml("Press to search");
			searchButtonToolTip.applyTo(searchButton);
			toolbar.addButton(searchButton);
			toolbar.addSeparator();
			
			downloadAPRecordExcel = new Item("To Excel");
			downloadAPRecordExcel.setIconCls("excel-icon");
			downloadAPRecordExcel.addListener(downloadListener(GlobalParameter.ACCOUNT_PAYABLE_REPORT_EXPORT_URL,"xls"));
			downloadAPRecordPDF = new Item("To PDF");
			downloadAPRecordPDF.setIconCls("pdf-icon");
			downloadAPRecordPDF.addListener(downloadListener(GlobalParameter.ACCOUNT_PAYABLE_REPORT_EXPORT_URL,"pdf"));
			downloadAPRecordReportMenu = new Menu();
			downloadAPRecordReportMenu.addItem(downloadAPRecordExcel);
			downloadAPRecordReportMenu.addItem(downloadAPRecordPDF);
			downloadAPRecordReportButton = new ToolbarButton("Export");
			downloadAPRecordReportButton.setIconCls("download-icon");
			downloadAPRecordReportButton.setMenu(downloadAPRecordReportMenu);
			ToolTip downloadAPRecordReportButtonToolTip = new ToolTip();
			downloadAPRecordReportButtonToolTip.setTitle("Supplier Ledger Report");
			downloadAPRecordReportButtonToolTip.setHtml("Choose report to download");
			downloadAPRecordReportButtonToolTip.applyTo(downloadAPRecordReportButton);
			toolbar.addButton(downloadAPRecordReportButton);
			toolbar.addSeparator();
			setTopToolbar(toolbar);

		}

		private BaseItemListener downloadListener(final String url,final String fileType) {
			BaseItemListenerAdapter listener = new BaseItemListenerAdapter() {
				public void onClick(BaseItem item, EventObject e) {
					downloadReport(url,fileType);
				}
			};
			return listener;
		}
		
	protected void downloadReport(final	String url,final String fileType) {
		final String subledgerType="";
		final String subledger=subledgerTextField.getValueAsString();
		String supplierNumber = supplierNumberTextField.getValueAsString();
		
		if(dataStore.getCount() == 0){
			MessageBox.alert("This function will only download the items matching the search query. Please search before downloading");
			return;
		}
		
		if(subledger!=null && !subledger.equals("") && supplierNumber.equals("")){
			try{
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getPackageRepository().obtainSCPackage(globalSectionController.getJob(), subledger, new AsyncCallback<SCPackage>(){
					public void onSuccess(SCPackage scPackage){
						if(scPackage!=null && scPackage.getVendorNo()!=null){
							supplierNumberTextField.setValue(scPackage.getVendorNo());
							com.google.gwt.user.client.Window.open(
									url + 
									"?jobNumber="+jobNumberTextField.getValueAsString().trim()+
									"&invoiceNumber="+invoiceNumberTextField.getValueAsString()+
									"&supplierNumber="+scPackage.getVendorNo()+
									"&documentNumber="+documentNumberTextField.getValueAsString()+
									"&documentType="+documentTypeTextField.getValueAsString()+
									"&subledger="+subledger+
									"&subledgerType="+subledgerType+
									"&fileType="+fileType, "_blank", "");
						}
						else
							MessageBox.alert("Package Number/Supplier does not exist.");
					}
					public void onFailure(Throwable e) {
						UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
					}
				});
			}catch (Exception e1){
				UIUtil.checkSessionTimeout(e1, true,globalSectionController.getUser(),this.getClass().getName()+".onClick()+"+this.getClass().getName()+".downloadButton");
			}
		}
		else
			com.google.gwt.user.client.Window.open(
					url + 
					"?jobNumber="+jobNumberTextField.getValueAsString().trim()+
					"&invoiceNumber="+invoiceNumberTextField.getValueAsString()+
					"&supplierNumber="+supplierNumberTextField.getValueAsString()+
					"&documentNumber="+documentNumberTextField.getValueAsString()+
					"&documentType="+documentTypeTextField.getValueAsString()+
					"&subledger="+subledgerTextField.getValueAsString()+
					"&subledgerType="+subledgerType+
					"&fileType="+ fileType, "_blank", "");

	}
			
		

	private void setupGridPanel() {
		//GRID PANEL
		gridPanel = new GridPanel();
		gridPanel.setLayout(new FitLayout());
		
		ColumnConfig invoiceNumberColConfig = new ColumnConfig("Invoice No.", "invoiceNumber", 100, false);
		ColumnConfig subledgerColConfig = new ColumnConfig("Subledger", "subledger", 100, false);
		ColumnConfig supplierNumberColConfig = new ColumnConfig("Supplier No.", "supplierNumber", 80, false);
		ColumnConfig documentTypeColConfig = new ColumnConfig("Document Type", "documentType", 100, false);
		ColumnConfig documentNumberColConfig = new ColumnConfig("Document No.", "documentNumber", 85, false);
		
		ColumnConfig grossAmountColConfig = new ColumnConfig("Gross Amount", "grossAmount", 100, false);
		grossAmountColConfig.setRenderer(numberDP2);
		grossAmountColConfig.setAlign(TextAlign.RIGHT);
		
		ColumnConfig openAmountColConfig = new ColumnConfig("Open Amount", "openAmount", 100, false);
		openAmountColConfig.setRenderer(numberDP2);
		openAmountColConfig.setAlign(TextAlign.RIGHT);
		
		ColumnConfig foreignAmountColConfig = new ColumnConfig("Foreign Amount", "foreignAmount", 100, false);
		foreignAmountColConfig.setRenderer(numberDP2);
		foreignAmountColConfig.setAlign(TextAlign.RIGHT);
		
		ColumnConfig foreignAmountOpenColConfig = new ColumnConfig("Foreign Open Amount", "foreignAmountOpen", 100, false);
		foreignAmountOpenColConfig.setRenderer(numberDP2);
		foreignAmountOpenColConfig.setAlign(TextAlign.RIGHT);
		
		ColumnConfig payStatusColConfig = new ColumnConfig("Pay Status", "payStatus", 65, false);
		
		ColumnConfig companyColConfig = new ColumnConfig("Company", "company", 85, false);
		ColumnConfig currencyCodeColConfig = new ColumnConfig("Currency", "currencyCode", 55, false);
		ColumnConfig invoiceDateColConfig = new ColumnConfig("Invoice Date", "invoiceDate", 100, false);
		ColumnConfig glDateColConfig = new ColumnConfig("G/L Date", "glDate", 100, false);
		ColumnConfig dueDateColConfig = new ColumnConfig("Due Date", "dueDate", 100, false);
		ColumnConfig batchNumberColConfig = new ColumnConfig("Batch No.", "batchNumber", 65, false);
		ColumnConfig batchTypeColConfig = new ColumnConfig("Batch Type", "batchType", 75, false);
		ColumnConfig batchDateColConfig = new ColumnConfig("Batch Date", "batchDate", 100, false);	
		ColumnConfig subledgerTypeColConfig = new ColumnConfig("Subledge Type", "subledgerType", 100, false);		
		

		// modified by brian on 20110209
		BaseColumnConfig[] columns = new BaseColumnConfig[]{
				new RowNumberingColumnConfig(),
				invoiceNumberColConfig,
				subledgerColConfig,
				supplierNumberColConfig,
				documentTypeColConfig,
				
				documentNumberColConfig,
				grossAmountColConfig,
				openAmountColConfig,
				foreignAmountColConfig,
				foreignAmountOpenColConfig,
				
				payStatusColConfig,
				companyColConfig,
				currencyCodeColConfig,
				invoiceDateColConfig,
				glDateColConfig,
				
				dueDateColConfig,
				batchNumberColConfig,
				batchTypeColConfig,
				batchDateColConfig,
				subledgerTypeColConfig
		};

		gridPanel.setColumnModel(new ColumnModel(columns));
		GridView view = new CustomizedGridView();
		view.setForceFit(false);
		gridPanel.setView(view);
		
		//Pagination
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(final int pageNum){
				getAPRecordPaginationWrapperByPage(pageNum);			
			}
		});

		totalGrossAmount = new ToolbarTextItem("<b>Gross Amount:</b>");
		totalOpenAmount = new ToolbarTextItem("<b>Open Amount:</b>");
		totalForeignAmount = new ToolbarTextItem("<b>Foreign Amount:</b>");
		totalForeignOpenAmount = new ToolbarTextItem("<b>Foreign Open Amount:</b>");
		totalNumberRecord = new ToolbarTextItem("<b>Total Number of Records:0</b>");
		paginationToolbar.addFill();
		paginationToolbar.addItem(totalGrossAmount);
		paginationToolbar.addSeparator();
		paginationToolbar.addItem(totalOpenAmount);
		paginationToolbar.addSeparator();
		paginationToolbar.addItem(totalForeignAmount);
		paginationToolbar.addSeparator();
		paginationToolbar.addItem(totalForeignOpenAmount);
		paginationToolbar.addSeparator();
		paginationToolbar.addItem(totalNumberRecord);
		gridPanel.setBottomToolbar(paginationToolbar);
		if(apRecordPaginationWrapper.getCurrentPageContentList()==null || apRecordPaginationWrapper.getCurrentPageContentList().size()==0)
			paginationToolbar.setTotalPage(1);
		else
			paginationToolbar.setTotalPage(apRecordPaginationWrapper.getTotalPage());
		paginationToolbar.setCurrentPage(0);
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(apRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		gridPanel.setStore(dataStore);
		
		//Pop-up PaymentHistoriesWindow for Details
		gridPanel.addGridRowListener(new GridRowListenerAdapter(){
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e){
					gridPanelRowIndex = new Integer(rowIndex);
					showPaymentHistories();
			}
		});
		
		add(gridPanel);
	}

	private void newSearch(){
		final String jobNumber = jobNumberTextField.getText().trim();
		final String invoiceNumber = invoiceNumberTextField.getText().trim();
		final String documentNumber = documentNumberTextField.getText().trim();
		final String documentType = documentTypeTextField.getText().trim();
		final String subledger = subledgerTextField.getValueAsString().trim();
		final String subledgerType = null;
		final String supplierNumber = supplierNumberTextField.getText().trim();	
		
		//Add supplier number automatically to UI when subledger is provided
		if(subledger!=null && !subledger.equals("") && supplierNumber.equals("")){
			try{
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getPackageRepository().obtainSCPackage(globalSectionController.getJob(), subledger, new AsyncCallback<SCPackage>(){
					public void onSuccess(SCPackage scPackage){
						if(scPackage!=null && scPackage.getVendorNo()!=null){
							supplierNumberTextField.setValue(scPackage.getVendorNo());	//supplier number is provided on fly
							searchAPRecord(jobNumber, invoiceNumber, supplierNumberTextField.getText().trim(), documentNumber, documentType, subledger, subledgerType);
						}
						else
							MessageBox.alert("Package Number/Supplier does not exist.");
					}
					public void onFailure(Throwable e) {
						UIUtil.alert("fail");
						UIUtil.alert(e.getMessage());
					}
				});
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		else
			searchAPRecord(jobNumber, invoiceNumber, supplierNumber, documentNumber, documentType, subledger, subledgerType);
	}
	
	public void populateGridByList(List<APRecord> apRecordList){
		dataStore.removeAll();
		
		Double totalGross = 0.00;
		Double totalOpen = 0.00;
		Double totalForeign = 0.00;
		Double totalForeignOpen = 0.00;

		for(final APRecord apRecord : apRecordList){
			Record record = apRecordDef.createRecord(new Object[]{
					apRecord.getDocumentNumber(),
					apRecord.getDocumentType(),
					date2String(apRecord.getInvoiceDate()),
					date2String(apRecord.getGlDate()),
					date2String(apRecord.getDueDate()),
					
					apRecord.getGrossAmount(),
					apRecord.getOpenAmount(),
					apRecord.getForeignAmount(),
					apRecord.getForeignAmountOpen(),
					apRecord.getPayStatus(),
					
					apRecord.getSupplierNumber(),
					apRecord.getInvoiceNumber(),
					apRecord.getCompany(),
					apRecord.getBatchNumber(),
					apRecord.getBatchType(),
					
					date2String(apRecord.getBatchDate()),
					apRecord.getCurrency(),
					apRecord.getSubledger(),
					apRecord.getSubledgerType(),
			});
			totalGross+=apRecord.getGrossAmount();
			totalOpen+=apRecord.getOpenAmount();
			totalForeign+=apRecord.getForeignAmount();
			totalForeignOpen+=apRecord.getForeignAmountOpen();
			
			dataStore.add(record);
		}
		if(apRecordList==null || apRecordList.size()==0){
			this.paginationToolbar.setTotalPage(1);		//Display total page = 1
			this.paginationToolbar.setCurrentPage(0);	//Display current page = 1
			MessageBox.alert("No data was found.");
		}
	}
	
	public void populateGridByPage(APRecordPaginationWrapper apRecordPaginationWrapper){
		this.apRecordPaginationWrapper = apRecordPaginationWrapper;
		this.paginationToolbar.setTotalPage(apRecordPaginationWrapper.getTotalPage());
		this.paginationToolbar.setCurrentPage(apRecordPaginationWrapper.getCurrentPage());
		NumberFormat fmt = NumberFormat.getFormat("#,##0.00");
		populateGridByList(this.apRecordPaginationWrapper.getCurrentPageContentList());
		totalGrossAmount.setText("<b>Gross Amount:"+fmt.format(apRecordPaginationWrapper.getTotalGross())+"</b>");
		totalOpenAmount.setText("<b>Open Amount:"+fmt.format(apRecordPaginationWrapper.getTotalOpen())+"</b>");
		totalForeignAmount.setText("<b>Foreign Amount:"+fmt.format(apRecordPaginationWrapper.getTotalForeign())+"</b>");
		totalForeignOpenAmount.setText("<b>Foreign Open Amount:"+fmt.format(apRecordPaginationWrapper.getTotalForeignOpen())+"</b>");
		totalNumberRecord.setText("<b>Total Number of Records:"+apRecordPaginationWrapper.getTotalRecords()+"</b>");
	}
	
	private String date2String(Date date){
		if (date!=null)
			return (new SimpleDateFormat("dd/MM/yyyy")).format(date);
		else
			return " ";
	}
	
	/**@author koeyyeung
	 * modified on 18 June, 2013
	 */
	// added by brian on 20110209
	// get the data to trigger PaymentHistoriesWindow
	private void showPaymentHistories(){
		Record currentRecord = dataStore.getAt(gridPanelRowIndex);
		
		if(currentRecord.getAsString("invoiceNumber") != null && !"".equals(currentRecord.getAsString("invoiceNumber"))){
			if(this.apRecordPaginationWrapper.getCurrentPageContentList() != null){
				APRecord aPrecord  = this.apRecordPaginationWrapper.getCurrentPageContentList().get(gridPanelRowIndex);
				
				// for display only - start
				navigateToPaymentHistory(aPrecord, aPrecord.getCompany(), aPrecord.getDocumentType(), aPrecord.getDocumentNumber(), aPrecord.getSupplierNumber());
			}
		}
	}	
	
	// added by brian on 20110208
	// get the payment history information and initialize the display window
	public void navigateToPaymentHistory(final APRecord apRecord, String company, String documentType, Integer documentNumber, Integer supplierNumber) {

		UIUtil.maskPanelById(DETAIL_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getJobCostRepository().getAPPaymentHistories(company, documentType, supplierNumber, documentNumber, new AsyncCallback<List<PaymentHistoriesWrapper>>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(DETAIL_SECTION_PANEL_ID);
			}

			public void onSuccess(List<PaymentHistoriesWrapper> paymentHistoriesList) {
				detailSectionController.getMainPanel().setTitle("Account Payable Details");
				PaymentHistoriesDetialPanel paymentHistoriesDetialPanel = new PaymentHistoriesDetialPanel(globalSectionController, apRecord, paymentHistoriesList);
				detailSectionController.setPanel(paymentHistoriesDetialPanel);
				detailSectionController.populatePanel();
				UIUtil.unmaskPanelById(DETAIL_SECTION_PANEL_ID);
			}
		});
	}

	public void searchAPRecord(String jobNumber, String invoiceNumber, String supplierNumber, String documentNumber, String documentType, String subledger, String subledgerType) {
		UIUtil.maskPanelById(APR_RECORD_ENQUIRY_MAIN_PANEL_ID, "Searching...", true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getJobCostRepository().obtainAPRecordPaginationWrapper(globalSectionController.getJob().getJobNumber(), invoiceNumber, supplierNumber, documentNumber, documentType, subledger, subledgerType, new AsyncCallback<APRecordPaginationWrapper>() {
			
			public void onSuccess(APRecordPaginationWrapper apRecordPaginationWrapper) {
				populateGridByPage(apRecordPaginationWrapper);
				UIUtil.unmaskPanelById(APR_RECORD_ENQUIRY_MAIN_PANEL_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser(),"GlobalSectionController."+this.getClass().getName()+".searchAPRecord()");
				UIUtil.unmaskPanelById(APR_RECORD_ENQUIRY_MAIN_PANEL_ID);
			}
		});
	}
	
	public void getAPRecordPaginationWrapperByPage(Integer pageNum) {
		UIUtil.maskPanelById(APR_RECORD_ENQUIRY_MAIN_PANEL_ID, "Searching...", true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getJobCostRepository().getAPRecordPaginationWrapperByPage(pageNum, new AsyncCallback<APRecordPaginationWrapper>(){
			public void onSuccess(APRecordPaginationWrapper apRecordPaginationWrapper) {
				populateGridByPage(apRecordPaginationWrapper);
				UIUtil.unmaskPanelById(APR_RECORD_ENQUIRY_MAIN_PANEL_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser(),"GlobalSectionController."+this.getClass().getName()+".getAPRecordPaginationWrapperbyPage(Integer)");
				UIUtil.unmaskPanelById(APR_RECORD_ENQUIRY_MAIN_PANEL_ID);
			}
		});
	}
	
}
