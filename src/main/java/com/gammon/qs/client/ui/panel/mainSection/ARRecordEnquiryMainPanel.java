package com.gammon.qs.client.ui.panel.mainSection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.ARRecord;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
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
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowNumberingColumnConfig;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
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
public class ARRecordEnquiryMainPanel extends Panel{
	private static final String AR_RECORD_ENQUIRY_MAIN_PANEL_ID = "ARRecordEnquiryMainPanel_ID";
	private static final int RECORDS_PER_PAGE = 50;

	private final GlobalSectionController globalSectionController;
	@SuppressWarnings("unused")
	private Panel searchPanel;
	private GridPanel gridPanel;

	private Store dataStore;

	private static final String DOCUMENT_NUMBER_RECORD_NAME = "documentNumberRecordName";
	private static final String DOCUMENT_TYPE_RECORD_NAME = "documentTypeRecordName";
	private static final String INVOICE_DATE_RECORD_NAME = "invoiceDateRecordName";
	private static final String GROSS_AMOUNT_RECORD_NAME = "grossAmountRecordName";
	private static final String OPEN_AMOUNT_RECORD_NAME = "openAmountRecordName";
	private static final String DUE_DATE_RECORD_NAME = "dueDateRecordName";
	private static final String DATE_CLOSED_RECORD_NAME = "dateClosedRecordName";
	private static final String CURRENCY_CODE_RECORD_NAME = "currencyCodeRecordName";
	private static final String PAY_STATUS_RECORD_NAME = "payStatusRecordName";
	private static final String REMARK_RECORD_NAME = "remarkRecordName";
	private static final String CUSTOMER_NUMBER_RECORD_NAME = "customerNumberRecordName";
	private static final String FOREIGN_AMOUNT_RECORD_NAME = "foreignAmountRecordName";
	private static final String FOREIGN_OPEN_AMOUNT_RECORD_NAME = "foreignAmountOpenRecordName";
	private static final String GL_DATE_RECORD_NAME = "glDateRecordName";
	private static final String BATCH_NUMBER_RECORD_NAME = "batchNumberRecordName";
	private static final String BATCH_TYPE_RECORD_NAME = "batchTypeRecordName";
	private static final String BATCH_DATE_RECORD_NAME = "batchDateRecordName";
	private static final String COMPANY_RECORD_NAME = "companyRecordName";
	private static final String REFERENCE_RECORD_NAME = "referenceRecordName";
	private static final String CUSTOMER_DESCRIPTION_RECORD_NAME = "customerDescriptionRecordName";
	
	private final RecordDef arRecordDef = new RecordDef(
			new FieldDef[]{
					new StringFieldDef(DOCUMENT_NUMBER_RECORD_NAME),
					new StringFieldDef(DOCUMENT_TYPE_RECORD_NAME),
					new StringFieldDef(INVOICE_DATE_RECORD_NAME),
					new StringFieldDef(GROSS_AMOUNT_RECORD_NAME),
					new StringFieldDef(OPEN_AMOUNT_RECORD_NAME),
					new StringFieldDef(DUE_DATE_RECORD_NAME),
					new StringFieldDef(DATE_CLOSED_RECORD_NAME),
					new StringFieldDef(CURRENCY_CODE_RECORD_NAME),
					
					new StringFieldDef(PAY_STATUS_RECORD_NAME),
					new StringFieldDef(REMARK_RECORD_NAME),
					new StringFieldDef(CUSTOMER_NUMBER_RECORD_NAME),
					new StringFieldDef(FOREIGN_AMOUNT_RECORD_NAME),
					new StringFieldDef(FOREIGN_OPEN_AMOUNT_RECORD_NAME),
					new StringFieldDef(GL_DATE_RECORD_NAME),
					new StringFieldDef(BATCH_NUMBER_RECORD_NAME),
					new StringFieldDef(BATCH_TYPE_RECORD_NAME),
					new StringFieldDef(BATCH_DATE_RECORD_NAME),
					new StringFieldDef(COMPANY_RECORD_NAME),
					new StringFieldDef(REFERENCE_RECORD_NAME),
					new StringFieldDef(CUSTOMER_DESCRIPTION_RECORD_NAME)
			});


	private TextField referenceTextField;
	private TextField customerNumberTextField;
	private TextField documentNumberTextField;
	private TextField documentTypeTextField;
	
	// added by brian on 20110210 - START
	private Integer gridPanelRowIndex;
	private List<ARRecord> localArRecordList;
	// added by brian on 20110210 - END

	private PaginationToolbar paginationToolbar;

	private Toolbar toolbar;
	private ToolbarButton downloadARRecordReportButton;
	private Menu downloadARRecordReportMenu;
	private Item downloadARRecordExcel;
	private Item downloadARRecordPDF;
	private ToolbarTextItem totalGrossAmount;
	private ToolbarTextItem totalOpenAmount;
	private ToolbarTextItem totalForeignAmount;
	private ToolbarTextItem totalForeignOpenAmount;
	private ToolbarTextItem totalNumberRecord;
	private Timer timer;
	
	public ARRecordEnquiryMainPanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		
		setupUI();
	}

	private void setupUI() {
		setLayout(new RowLayout());
		setId(AR_RECORD_ENQUIRY_MAIN_PANEL_ID);
		setupToolbar();
		setupGridPanel();
		
	}
	
	//add by paulyiu 20150730
			private void setupToolbar(){
				toolbar = new Toolbar();
				
				FieldListener searchListener = new FieldListenerAdapter(){
					public void onSpecialKey(Field field, EventObject e){
						if(e.getKey() == EventObject.ENTER)
							search(0);
					}
				};

				toolbar.addText("Reference:");
				referenceTextField = new TextField("Reference", "referenceSearch", 100);
				referenceTextField.setEmptyText("e.g. 13389001  ");
				ToolTip referenceTextFieldToolTip = new ToolTip();
				referenceTextFieldToolTip.setTitle("Reference");
				referenceTextFieldToolTip.setHtml("Enter reference to search");
				referenceTextFieldToolTip.applyTo(referenceTextField);
				referenceTextField.addListener(searchListener);
				toolbar.addField(referenceTextField);
				toolbar.addSpacer();
				
				toolbar.addText("Customer Number:");
				customerNumberTextField = new TextField("Customer Number", "customerNumberSearch", 100);
				customerNumberTextField.setEmptyText("e.g. 30150  ");
				customerNumberTextField.setValidator(new Validator(){

					public boolean validate(String value) throws ValidationException {
						if(!value.matches("^[0-9]*$")){
							MessageBox.alert("Please input number only");
							//customerNumberTextField.focus();
							//customerNumberTextField.selectText();
							return false;
						}
						return true;
					}
					
				});
				ToolTip customerNumberTextFieldToolTip = new ToolTip();
				customerNumberTextFieldToolTip.setTitle("Customer Number");
				customerNumberTextFieldToolTip.setHtml("Enter customer number to search");
				customerNumberTextFieldToolTip.applyTo(customerNumberTextField);
				customerNumberTextField.addListener(searchListener);
				toolbar.addField(customerNumberTextField);
				toolbar.addSpacer();
				
				toolbar.addText("Document Number:");
				documentNumberTextField = new TextField("Document Number", "documentNumberSearch", 100);
				documentNumberTextField.setEmptyText("e.g. 21931  ");
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
				documentTypeTextField = new TextField("Document Type", "documentTypeSearch", 100);
				documentTypeTextField.setEmptyText("e.g. RI  ");
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
						search(0);
					};
				});
				ToolTip searchButtonToolTip = new ToolTip();
				searchButtonToolTip.setTitle("Search");
				searchButtonToolTip.setHtml("Press to search");
				searchButtonToolTip.applyTo(searchButton);
				toolbar.addButton(searchButton);
				toolbar.addSeparator();
				
				downloadARRecordExcel = new Item("To Excel");
				downloadARRecordExcel.setIconCls("excel-icon");
				downloadARRecordExcel.addListener(downloadListener(GlobalParameter.ACCOUNT_CUSTOMER_LEDGER_REPORT_EXPORT_URL,"xls"));
				downloadARRecordPDF = new Item("To PDF");
				downloadARRecordPDF.setIconCls("pdf-icon");
				downloadARRecordPDF.addListener(downloadListener(GlobalParameter.ACCOUNT_CUSTOMER_LEDGER_REPORT_EXPORT_URL,"pdf"));
				downloadARRecordReportMenu = new Menu();
				downloadARRecordReportMenu.addItem(downloadARRecordExcel);
				downloadARRecordReportMenu.addItem(downloadARRecordPDF);
				downloadARRecordReportButton = new ToolbarButton("Export");
				downloadARRecordReportButton.setIconCls("download-icon");
				downloadARRecordReportButton.setMenu(downloadARRecordReportMenu);
				ToolTip downloadARRecordReportButtonToolTip = new ToolTip();
				downloadARRecordReportButtonToolTip.setTitle("Supplier Ledger Report");
				downloadARRecordReportButtonToolTip.setHtml("Press to download report");
				downloadARRecordReportButtonToolTip.applyTo(downloadARRecordReportButton);
				toolbar.addButton(downloadARRecordReportButton);
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
			
		private void downloadReport(final	String url,final String fileType) {
			String reference = referenceTextField.getValueAsString().trim();
			String customerNumber = customerNumberTextField.getValueAsString().trim();
			String documentNumber = documentNumberTextField.getValueAsString().trim();
			String documentType = documentTypeTextField.getValueAsString().trim();
			
			if(dataStore.getCount() == 0){
				MessageBox.alert("This function will only download the items matching the search query. Please search before downloading");
				return;
			}
			
			com.google.gwt.user.client.Window.open(
					url
					+ "?jobNumber="+ globalSectionController.getJob().getJobNumber()
					+ "&reference=" + reference
					+ "&customerNumber=" + customerNumber
					+ "&documentNumber=" + documentNumber
					+ "&documentType=" + documentType
					+ "&fileType=" + fileType,"_blank", "");
		}

	private void setupGridPanel() {
		//GRID PANEL
		gridPanel = new GridPanel();
		
		Renderer numberDP2 = new Renderer(){
			public String render(	Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				String htmlAttr = "style='font-weight:bold;";
				if (Double.parseDouble(value.toString()) < 0) {
					htmlAttr += "color:red;";
				}
				cellMetadata.setHtmlAttribute(htmlAttr+"'");
				
				NumberFormat fmt = NumberFormat.getFormat("#,##0.00");
				value = fmt.format(Double.parseDouble(value.toString()));
				
				if(record.getAsString(DOCUMENT_NUMBER_RECORD_NAME).equals("Total:"))
					return "<b>" + value + "</b>";
					
				return (value != null ? value.toString() : null);
			}
			
		};
		gridPanel.addGridRowListener(new GridRowListenerAdapter(){
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e){
					gridPanelRowIndex = new Integer(rowIndex);
					showAccountReceivibleDetails();
			}
		});
		
		localArRecordList = new ArrayList<ARRecord>();
				
		//Document Number
		ColumnConfig documentNumberColConfig = new ColumnConfig("Document No.", DOCUMENT_NUMBER_RECORD_NAME, 100, false);
		//Document Type
		ColumnConfig documentTypeColConfig = new ColumnConfig("Document Type", DOCUMENT_TYPE_RECORD_NAME, 85, false);
		//Invoice Date
		ColumnConfig invoiceDateColConfig = new ColumnConfig("Invoice Date", INVOICE_DATE_RECORD_NAME, 90, false);
		//Gross Amount
		ColumnConfig grossAmountColConfig = new ColumnConfig("Gross Amount", GROSS_AMOUNT_RECORD_NAME, 110, false);
		grossAmountColConfig.setRenderer(numberDP2);
		grossAmountColConfig.setAlign(TextAlign.RIGHT);
		//Open Amount
		ColumnConfig openAmountColConfig = new ColumnConfig("Open Amount", OPEN_AMOUNT_RECORD_NAME, 110, false);
		openAmountColConfig.setRenderer(numberDP2);
		openAmountColConfig.setAlign(TextAlign.RIGHT);
		
		//Due Date
		ColumnConfig dueDateColConfig = new ColumnConfig("Due Date", DUE_DATE_RECORD_NAME, 90, false);
		
		// added by brian on 20110315
		//Date Closed
		ColumnConfig dateClosedColConfig = new ColumnConfig("Date Closed", DATE_CLOSED_RECORD_NAME, 90, false);
		
		//Currency Code
		ColumnConfig currencyCodeColConfig = new ColumnConfig("Currency Code", CURRENCY_CODE_RECORD_NAME, 100, false);
		
		//Pay Status
		ColumnConfig payStatusColConfig = new ColumnConfig("Pay Status", PAY_STATUS_RECORD_NAME, 70, false);
		//Remark
		ColumnConfig remarkColConfig = new ColumnConfig("Remark", REMARK_RECORD_NAME, 250, false);
		
		//Customer Number
		ColumnConfig customerNumberColConfig = new ColumnConfig("Customer No.", CUSTOMER_NUMBER_RECORD_NAME, 80, false);
		//Foreign Amount
		ColumnConfig foreignAmountColConfig = new ColumnConfig("Foreign Amount", FOREIGN_AMOUNT_RECORD_NAME, 110, false);
		foreignAmountColConfig.setRenderer(numberDP2);
		foreignAmountColConfig.setAlign(TextAlign.RIGHT);
		//Foreign Amount Open
		ColumnConfig foreignAmountOpenColConfig = new ColumnConfig("Foreign Open Amount", FOREIGN_OPEN_AMOUNT_RECORD_NAME, 130, false);
		foreignAmountOpenColConfig.setRenderer(numberDP2);
		foreignAmountOpenColConfig.setAlign(TextAlign.RIGHT);
		//G/L Date
		ColumnConfig glDateColConfig = new ColumnConfig("G/L Date", GL_DATE_RECORD_NAME, 90, false);
		//Batch Number
		ColumnConfig batchNumberColConfig = new ColumnConfig("Batch No.", BATCH_NUMBER_RECORD_NAME, 80, false);
		
		//Batch Type
		ColumnConfig batchTypeColConfig = new ColumnConfig("Batch Type", BATCH_TYPE_RECORD_NAME, 75, false);
		//Batch Date
		ColumnConfig batchDateColConfig = new ColumnConfig("Batch Date", BATCH_DATE_RECORD_NAME, 80, false);
		//Company
		ColumnConfig companyColConfig = new ColumnConfig("Company", COMPANY_RECORD_NAME, 80, false);
		//Reference
		ColumnConfig referenceColConfig= new ColumnConfig("Reference", REFERENCE_RECORD_NAME, 85, false);
		//Customer Description
		ColumnConfig customerDescriptionColConfig = new ColumnConfig("Customer Description", CUSTOMER_DESCRIPTION_RECORD_NAME, 250, false);
		
		BaseColumnConfig[] columns = new BaseColumnConfig[]{
				new RowNumberingColumnConfig(),
				referenceColConfig,
				customerNumberColConfig,
				customerDescriptionColConfig,
				documentTypeColConfig,
				
				 documentNumberColConfig,
				 grossAmountColConfig,
				 openAmountColConfig,
				 foreignAmountColConfig,
				 foreignAmountOpenColConfig,
			 
				 payStatusColConfig,
				 companyColConfig,
				 currencyCodeColConfig, 
				 glDateColConfig,
				 invoiceDateColConfig,
				 
				 dueDateColConfig,
				 dateClosedColConfig, // added by brian on 20110315
				
				 batchNumberColConfig,		
				 batchTypeColConfig,
				 batchDateColConfig,
				 remarkColConfig
		};
		
		gridPanel.setColumnModel(new ColumnModel(columns));
		gridPanel.setView(new CustomizedGridView());
		
		//Setup data store(JDE --> data store)
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(arRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		gridPanel.setStore(dataStore);
		
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setTotalPage(0);
		paginationToolbar.setCurrentPage(0);
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(int pageNum){
				search(pageNum);
			}
		});
		
		totalGrossAmount = new ToolbarTextItem("<b>Gross Amount:</b>");
		totalOpenAmount = new ToolbarTextItem("<b>Open Amount:</b>");
		totalForeignAmount = new ToolbarTextItem("<b>Foreign Amount:</b>");
		totalForeignOpenAmount = new ToolbarTextItem("<b>Foreign Open Amount:</b>");
		totalNumberRecord = new ToolbarTextItem("<b>Total number of records:0</b>");
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
		
		add(gridPanel);
		timer =new Timer(){
			@Override
			public void run() {
				search(0);
				timer=null;
			}
		};
		timer.schedule(1000);
	}
	
	private void search(int pageNum){
		String reference = referenceTextField.getText().trim().equals("")?null:referenceTextField.getText().trim(); 
		String customerNumber = customerNumberTextField.getText().trim().equals("")?null:customerNumberTextField.getText().trim();
		String documentNumber = documentNumberTextField.getText().trim().equals("")?null:documentNumberTextField.getText().trim();
		String documentType = documentTypeTextField.getText().trim().equals("")?null:documentTypeTextField.getText().trim();
		
		UIUtil.maskPanelById(AR_RECORD_ENQUIRY_MAIN_PANEL_ID, "Searching...", true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getJobCostRepository().getARRecordListByPage(globalSectionController.getJob().getJobNumber(), reference, customerNumber, documentNumber, documentType, pageNum, RECORDS_PER_PAGE, new AsyncCallback<PaginationWrapper<ARRecord>>() {
			public void onSuccess(PaginationWrapper<ARRecord> wrapper){
				populateGridByPage(wrapper);
				UIUtil.unmaskPanelById(AR_RECORD_ENQUIRY_MAIN_PANEL_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(AR_RECORD_ENQUIRY_MAIN_PANEL_ID);
				UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
			}
		});
	}	
	
	public void populateGridByPage(PaginationWrapper<ARRecord> wrapper) {
		paginationToolbar.setTotalPage(wrapper.getTotalPage());
		paginationToolbar.setCurrentPage(wrapper.getCurrentPage());
		totalNumberRecord.setText("<b>Total number of records:"+wrapper.getTotalRecords()+"</b>");
		populateGrid(wrapper.getCurrentPageContentList());
	}
	
	public void populateGrid(List<ARRecord> arRecordList){
		dataStore.removeAll();
		
		// added by brian on 20110210
		// to cache the AR Record list for show Account Receivable details
		localArRecordList = arRecordList;
		
		Double totalGross = 0.00;
		Double totalOpen = 0.00;
		Double totalForeign = 0.00;
		Double totalForeignOpen = 0.00;
		
		for(ARRecord arRecord : arRecordList){		
			Record record = arRecordDef.createRecord(new Object[]{
					arRecord.getDocumentNumber(),
					arRecord.getDocumentType(),
					date2String(arRecord.getInvoiceDate()),
					arRecord.getGrossAmount(),
					arRecord.getOpenAmount(),
					
					date2String(arRecord.getDueDate()),
					date2String(arRecord.getDateClosed()), // added by brian on 20110315
					arRecord.getCurrency(),
					arRecord.getPayStatus(),
					arRecord.getRemark(),
					
					arRecord.getCustomerNumber(),
					arRecord.getForeignAmount(),
					arRecord.getForeignOpenAmount(),
					date2String(arRecord.getGlDate()),
					arRecord.getBatchNumber(),
					
					arRecord.getBatchType(),
					date2String(arRecord.getBatchDate()),
					arRecord.getCompany(),
					arRecord.getReference(),
					arRecord.getCustomerDescription()
			});
			totalGross += arRecord.getGrossAmount();
			totalOpen += arRecord.getOpenAmount();
			totalForeign += arRecord.getForeignAmount();
			totalForeignOpen += arRecord.getForeignOpenAmount();
			dataStore.add(record);
		}
		NumberFormat fmt = NumberFormat.getFormat("#,##0.00");
		totalGrossAmount.setText("<b>Gross Amount:"+fmt.format(totalGross)+"</b>");
		totalOpenAmount.setText("<b>Open Amount:"+fmt.format(totalOpen)+"</b>");
		totalForeignAmount.setText("<b>Foreign Amount:"+fmt.format(totalForeign)+"</b>");
		totalForeignOpenAmount.setText("<b>Foreign Open Amount:"+fmt.format(totalForeignOpen)+"</b>");
		
		if(arRecordList!=null && arRecordList.size()>0){
		}
		else
			MessageBox.alert("No data was found.");
	}
	
	private String date2String(Date date){
		if (date!=null)
			return (new SimpleDateFormat("dd/MM/yyyy")).format(date);
		else
			return " ";
	}
	
	/**@author koeyyeung
	 * modified on 07 June, 2013
	 */
	// added by brian on 20110209
	// get the data to trigger account receivable details 
	private void showAccountReceivibleDetails(){
		Record currentRecord = dataStore.getAt(gridPanelRowIndex);
		
		if(currentRecord.getAsString(REFERENCE_RECORD_NAME) == null || "".equals(currentRecord.getAsString(REFERENCE_RECORD_NAME))){
			globalSectionController.getDetailSectionController().resetPanel();
		}
		else{
			if(this.localArRecordList != null){
				ARRecord arRecord  = this.localArRecordList.get(gridPanelRowIndex);
				if(arRecord != null && arRecord.getDocumentNumber() != null) {
					globalSectionController.getDetailSectionController().populateAccountReceivableDetailsPanel(arRecord);
				} else {
					return;
				}
			}
			else
				return;
		}
	}

}
