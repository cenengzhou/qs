/**
 * koeyyeung
 * Jul 4, 201312:00:45 PM
 * Change from Window to Panel
 */

/**
 * @author heisonwong
 * 
 * 3Aug 2015 14:40
 * 
 * Change Order type to dropdown list, add Excel and PDF Report
 * 
 * **/

package com.gammon.qs.client.ui.panel.mainSection;

import java.util.Date;
import java.util.List;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRendererCheckIfTotal;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.PORecord;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.PurchaseOrderEnquiryWrapper;
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
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

public class PurchaseOrderEnquiryMainPanel extends Panel{

	private static final String MAIN_PANEL_ID = "PurchaseOrderEnquiryMainPanel_ID";
	private final GlobalSectionController globalSectionController;
	
	/**
	 * PAGINATION
	 */
	private PaginationToolbar paginationToolbar = null;
	private PurchaseOrderEnquiryWrapper purchaseOrderEnquiryWrapper;
	
	/**
	 * PANELS
	 */
	private GridPanel gridPanel;

	
	/**
	 * DATA
	 */
	private Store dataStore;
	private final FieldDef[] fieldDef = new FieldDef[]{	
			new StringFieldDef("jobNumber"), // MCU
			new StringFieldDef("supplierNumber"), // AN8
			new StringFieldDef("orderNumber"), // DOCO
			new StringFieldDef("orderType"), // DCTO
			new StringFieldDef("lineDescription"), // DSC1
			new StringFieldDef("lineDescription2"), // DSC2
			new StringFieldDef("objectCode"), // OBJ
			new StringFieldDef("subsidiaryCode"), // SUB
			new StringFieldDef("currency"), // CRCD
			new StringFieldDef("originalOrderedAmount"), // AEXP
			new StringFieldDef("orderDate"), // TRDJ
			new StringFieldDef("gLDate") // DGL
			};
	
	private final RecordDef purchaseOrderRecordDef = new RecordDef(fieldDef);
	
	// input text fields
	private TextField jobNumberTextField; // MCU
	private TextField supplierNumberTextField; // AN8
	private TextField orderNumberTextField; // DOCO
	//private TextField orderTypeTextField; // DCTO //disableded by heisonwong
	private TextField lineDescriptionTextField;
	private ComboBox orderTypeComboBox = new ComboBox();//added by heisonwong
	private ToolbarButton reportToolbarButton;
	private Item downloadExcelButton;
	private Item downloadPDFButton;
	private ToolbarTextItem totalNoOfRecordsValueLabel;
	
	
	private Store storeOrderType = new SimpleStore(new String[]{"orderType", "orderTypeDescription"}, this.getOrderType());//added by heisonwong 
	
	private String[][] getOrderType(){ //added by heisonwong
		return new String[][]{
			 new String[]{"", ""},
			 new String[]{"GH","GH - Plant Blanket Sundry Order"},
			 new String[]{"P3","P3 - Payment Requisition"},
			 new String[]{"GB","GB - Blanket Purchase Order"},
			 new String[]{"GE","GE - Plant Sundry Release Order"},
			 new String[]{"GL","GL - Release Purchase Order"},
			 new String[]{"GM","GM - Purchase Order of Fixed Asset"},
			 new String[]{"GP","GP - Purchase Order"},
			 new String[]{"GX","GX - Order exceed or w/o budget"},
			 new String[]{"GY","GY - BPO exceed or w/o budget"},
			 new String[]{"OB","OB - Blanket Purchase Order"},
			 new String[]{"OF","OF - PO of Fixed Asset"},
			 new String[]{"OH","OH - Blanket Sundry Order"},
			 new String[]{"OL","OL - Release Purchase Order"},
			 new String[]{"ON","ON - Sundry Release Order"},
			 new String[]{"OP","OP - Purchase Order"},
			 new String[]{"OS","OS - Purchase Order of Fixed Asset"},
			 new String[]{"OX","OX - Order exceed or w/o budget"},
			 new String[]{"OY","OY - BPO exceed or w/o budget"},
			 new String[]{"O2","O2 - Supplier Catalog Order"},
			 new String[]{"OR","OR - Purchase Requisition"},
			 new String[]{"GR","GR - Purchase Requisition - CSD"},
			 new String[]{"P4","P4 - Reimbursement of Expense"}
		};
	}
	
	
	
	private String jobNumberValue;
	
	public PurchaseOrderEnquiryMainPanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		jobNumberValue = this.globalSectionController.getJob().getJobNumber();
		
		setupUI();
	}

	private void setupUI() {
		setLayout(new RowLayout());
		
		//setupSearchPanel();
		setupToolbar();
		setupGridPanel();
		setId(MAIN_PANEL_ID);
		
		//hide detail panel
		
		globalSectionController.getDetailSectionController().getMainPanel().collapse();
		
	}

	private void setupToolbar() {
		Toolbar toolbar = new Toolbar();
		toolbar.setHeight(35);
		//Filter Listener
		FieldListener fieldListener = new FieldListenerAdapter(){
/*			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER){
					search(jobNumberTextField.getText().trim(), supplierNumberTextField.getText().trim(), 
							orderNumberTextField.getText().trim(),lineDescriptionTextField.getText().trim(), orderTypeComboBox.getText().trim());
				}
			}*/
		};
		
		//Job Number
		ToolbarTextItem jobNumberLabel = new ToolbarTextItem("Job No.");
		jobNumberTextField = new TextField();
		jobNumberTextField.setWidth(100);
		jobNumberTextField.addListener(fieldListener);
		jobNumberTextField.setValue(jobNumberValue);
		jobNumberTextField.setDisabled(true);//non-editable
		toolbar.addItem(jobNumberLabel);
		toolbar.addField(jobNumberTextField);
		toolbar.addSpacer();
		
		//Supplier Number
		ToolbarTextItem supplierNumberLabel = new ToolbarTextItem("Supplier No.");
		supplierNumberTextField = new TextField();
		supplierNumberTextField.setWidth(100);
		supplierNumberTextField.addListener(fieldListener);
		toolbar.addItem(supplierNumberLabel);
		toolbar.addField(supplierNumberTextField);
		toolbar.addSpacer();
		
		//Order Number
		ToolbarTextItem orderNumberLabel = new ToolbarTextItem("Order No.");
		orderNumberTextField = new TextField();
		orderNumberTextField.setWidth(100);
		orderNumberTextField.addListener(fieldListener);
		toolbar.addItem(orderNumberLabel);
		toolbar.addField(orderNumberTextField);
		toolbar.addSpacer();
		
		//Line Description
		ToolbarTextItem lineDescriptionLabel = new ToolbarTextItem("Line Description");
		lineDescriptionTextField = new TextField();
		lineDescriptionTextField.setWidth(200);
		lineDescriptionTextField.addListener(fieldListener);
		toolbar.addItem(lineDescriptionLabel);
		toolbar.addField(lineDescriptionTextField);
		toolbar.addSpacer();
		
		//Order Type
		ToolbarTextItem orderTypeLabel = new ToolbarTextItem("Order Type");
		orderTypeComboBox = new ComboBox();
		
		storeOrderType.load();
		orderTypeComboBox.setForceSelection(false);
		orderTypeComboBox.setStore(storeOrderType);
		orderTypeComboBox.setDisplayField("orderTypeDescription");
		orderTypeComboBox.setValueField("orderType");
		orderTypeComboBox.setWidth(200);
		orderTypeComboBox.setEmptyText("");
		orderTypeComboBox.setCtCls("table-cell");
		orderTypeComboBox.setMinChars(1);
		orderTypeComboBox.setListWidth(260);
		orderTypeComboBox.setWidth(120);
		orderTypeComboBox.addListener(fieldListener);
		toolbar.addItem(orderTypeLabel);
		toolbar.addField(orderTypeComboBox);
		toolbar.addSpacer();
		toolbar.addSeparator();
		
		//Search Button
		ToolbarButton searchButton = new ToolbarButton("Search");
		searchButton.setIconCls("find-icon");
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				search();
			};
		});
		toolbar.addButton(searchButton);
		toolbar.addSeparator();
		
		/**generate Excel Report by heisonwong**/
		downloadExcelButton = new Item("Export to Excel");
		downloadExcelButton.setIconCls("excel-icon");
		downloadExcelButton.addListener(new BaseItemListenerAdapter() {

		public void onClick(BaseItem item, EventObject e) {
			if(orderTypeComboBox.getText().toUpperCase().trim().equals("") && supplierNumberTextField.getText().trim().equals("") && orderNumberTextField.getText().equals("")){
					MessageBox.alert("Please search with at least one of the the following searching criteria: Supplier No., Order No. or Order Type");
				}
			else if(lineDescriptionTextField.getText().contains(".") || lineDescriptionTextField.getText().contains("?") || lineDescriptionTextField.getText().contains("$")
					|| lineDescriptionTextField.getText().contains("^") || lineDescriptionTextField.getText().contains("[") || lineDescriptionTextField.getText().contains("]")
					|| lineDescriptionTextField.getText().contains("{") || lineDescriptionTextField.getText().contains("}") || lineDescriptionTextField.getText().contains("|")){
				MessageBox.alert("Special characters: . ? $ ^ [ ] { } | are not allowed to search.");
			}
			else{
					String parameters = obtainParameters();
					com.google.gwt.user.client.Window.open(GlobalParameter.PURCHASE_ORDER_EXCEL_URL + parameters, "_blank", "");
				}
			}
		});

		/**generate PDF Report by heisonwong**/
		downloadPDFButton = new Item("Export to PDF");
		downloadPDFButton.setIconCls("pdf-icon");
		downloadPDFButton.addListener(new BaseItemListenerAdapter() {
			
			public void onClick(BaseItem item, EventObject e) {
				if(orderTypeComboBox.getText().toUpperCase().trim().equals("") && supplierNumberTextField.getText().trim().equals("") && orderNumberTextField.getText().equals("")){
					MessageBox.alert("Please search with at least one of the the following searching criteria: Supplier No., Order No. or Order Type");
				}
				else if(lineDescriptionTextField.getText().contains(".") || lineDescriptionTextField.getText().contains("?") || lineDescriptionTextField.getText().contains("$")
						|| lineDescriptionTextField.getText().contains("^") || lineDescriptionTextField.getText().contains("[") || lineDescriptionTextField.getText().contains("]")
						|| lineDescriptionTextField.getText().contains("{") || lineDescriptionTextField.getText().contains("}") || lineDescriptionTextField.getText().contains("|")){
					MessageBox.alert("Special characters: . ? $ ^ [ ] { } | are not allowed to search.");
				}
				else
				{
					String parameters = obtainParameters();
					com.google.gwt.user.client.Window.open(GlobalParameter.PURCHASE_ORDER_REPORT_URL + parameters, "_blank", "");
				}
			}
		});
		Menu reportMenu = new Menu();
		reportMenu.addItem(downloadExcelButton);
		reportMenu.addItem(downloadPDFButton);
		reportToolbarButton = new ToolbarButton("Reports");
		reportToolbarButton.setMenu(reportMenu);
		reportToolbarButton.setIconCls("menu-show-icon");
		toolbar.addButton(reportToolbarButton);
		toolbar.addSeparator();
		
		add(toolbar);
	}
	

	private void setupGridPanel() {
		//initialize GRIDPANEL
		gridPanel = new GridPanel();
		GridView view = new CustomizedGridView();
		view.setForceFit(false);
		gridPanel.setView(view);
		
		// Set the amount column follow the users preference
		Renderer amountRenderer = new AmountRendererCheckIfTotal(globalSectionController.getUser());
		
		//GRIDPANEL-COLUMNS
		ColumnConfig supplierNumberColConfig = new ColumnConfig("Supplier No.", "supplierNumber", 100, true);
		ColumnConfig orderNumberColConfig = new ColumnConfig("Order No.", "orderNumber", 100, true);
		ColumnConfig orderTypeColConfig = new ColumnConfig("Order Type", "orderType", 100, true);
		ColumnConfig lineDescriptionColConfig = new ColumnConfig("Line Description", "lineDescription", 200, true);
		ColumnConfig lineDescription2ColConfig = new ColumnConfig("Line Description 2", "lineDescription2", 200, true);
		ColumnConfig objectCodeColConfig = new ColumnConfig("Object Code", "objectCode", 100, true);
		ColumnConfig subsidiaryCodeColConfig = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 150, true);
		ColumnConfig currencyColConfig = new ColumnConfig("Currency", "currency", 100, true);
		ColumnConfig originalOrderAmountColConfig = new ColumnConfig("Original Ordered Amount", "originalOrderedAmount", 200, true);
		ColumnConfig orderDateColConfig = new ColumnConfig("Order Date", "orderDate", 100, true);
		ColumnConfig glDateColConfig = new ColumnConfig("G/L Date", "gLDate", 100, true);
		
		originalOrderAmountColConfig.setRenderer(amountRenderer);
		originalOrderAmountColConfig.setAlign(TextAlign.RIGHT);
		
		ColumnConfig[] columns = new ColumnConfig[]{
													supplierNumberColConfig,
													orderNumberColConfig,
													orderTypeColConfig,
													lineDescriptionColConfig,
													lineDescription2ColConfig,
													objectCodeColConfig,
													subsidiaryCodeColConfig,
													currencyColConfig,
													originalOrderAmountColConfig,
													orderDateColConfig,
													glDateColConfig};
		
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(int pageNum){
				getPurchaseOrderEnquiryWrapperByPage(pageNum);
			}
		});
		
		//initialize DATA
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(purchaseOrderRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		
		//items added in GRIDPANEL
		gridPanel.setColumnModel(new ColumnModel(columns));
		gridPanel.setStore(dataStore);
		gridPanel.setBottomToolbar(paginationToolbar);
		paginationToolbar.setDisabled(true);
		
		paginationToolbar.addFill(); //to add space to make totalnoOf record align right
		
		totalNoOfRecordsValueLabel = new ToolbarTextItem("<b>Total No. of Records: </b>");
		paginationToolbar.addItem(totalNoOfRecordsValueLabel);
		
		add(gridPanel);
		
	}

	// Search Purchase Order
	private void search(){
		String supplierNumber = supplierNumberTextField.getText().trim(); // AN8
		String orderNumber = orderNumberTextField.getText().trim();  // DOCO
		/**change from text field to combobox by heisonwong**/
		String orderType = orderTypeComboBox.getText().toUpperCase().trim();
		String lineDescription = lineDescriptionTextField.getText().trim();
		
		//Validation
		if(supplierNumber.equals("") && orderNumber.equals("") && orderType.equals("")){
			MessageBox.alert("Please search with at least one of the the following searching criteria: Supplier Number, Order No. or Order Type");
		}
		else if(lineDescription.contains(".") || lineDescription.contains("?") || lineDescription.contains("$")
				|| lineDescription.contains("^") || lineDescription.contains("[") || lineDescription.contains("]")
				|| lineDescription.contains("{") || lineDescription.contains("}") || lineDescription.contains("|")){
			MessageBox.alert("Special characters: . ? $ ^ [ ] { } | are not allowed to search.");
		}
		else{
			searchPORecordByPage(orderNumber, orderType, supplierNumber, lineDescription, 0);
		}
	}
	
	public void populateGridByList(List<PORecord> poRecordList){
		dataStore.removeAll();

		for(PORecord poRecord : poRecordList){
			Record record = purchaseOrderRecordDef.createRecord(new Object[]{
					poRecord.getCostCenter(),
					poRecord.getAddressNumber(),
					poRecord.getDocumentOrderInvoiceE(),
					poRecord.getOrderType(),
					poRecord.getDescriptionLine1(),
					poRecord.getDescriptionLine2(),
					poRecord.getObjectAccount(),
					poRecord.getSubsidiary(),
					poRecord.getCurrencyCodeFrom(),
					poRecord.getAmountExtendedPrice(),
					date2String(poRecord.getDateTransactionJulian()),
					date2String(poRecord.getDtForGLAndVouch1())
			});
			dataStore.add(record);
		}
		
		if(poRecordList != null && poRecordList.size() > 0){
			totalNoOfRecordsValueLabel.setText("<b>Total No. of Records: " + poRecordList.size() + "</b>");
		}
		else{
			paginationToolbar.setDisabled(true);
			MessageBox.alert("No data was found");
		}
	}
	
	public void populateGridByPage(PurchaseOrderEnquiryWrapper pOEnquiryWrapper){
		if(pOEnquiryWrapper == null){
			dataStore.removeAll();
			this.paginationToolbar.setDisabled(true);
			MessageBox.alert("No data was found");
			return;
		}
		else if(pOEnquiryWrapper != null && pOEnquiryWrapper.getTotalRecords() > 0){
			
			purchaseOrderEnquiryWrapper = pOEnquiryWrapper;
			paginationToolbar.setDisabled(false);
			
			// ******* MUST SET CURRENTPAGE FIRST, THEN SET TOTAL PAGE *******
			// CurrentPage will be displayed as "set value" + 1  ( P.S. the first page is 0 when pass back to java class )
			paginationToolbar.setTotalPage(purchaseOrderEnquiryWrapper.getTotalPage());
			paginationToolbar.setCurrentPage(purchaseOrderEnquiryWrapper.getCurrentPage());
			
			// Call populateGridByList and pass the current page content list to display
			populateGridByList(purchaseOrderEnquiryWrapper.getCurrentPageContentList());
		}
		
/*		//hide detail panel
			globalSectionController.getDetailSectionController().getMainPanel().collapse();*/
	}
	
	// Convert Date to String
	private String date2String(Date date){
		if (date!=null)
			return (new SimpleDateFormat("dd/MM/yyyy")).format(date);
		else
			return " ";
	}
	
	public void getPurchaseOrderEnquiryWrapperByPage(int pageNum) {
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.SEARCHING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getJobCostRepository().getPurchaseOrderEnquiryWrapperByPage(pageNum, new AsyncCallback<PurchaseOrderEnquiryWrapper>(){
			public void onSuccess(PurchaseOrderEnquiryWrapper wrapper) {
				populateGridByPage(wrapper);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
	}
	// This function calls Web Service
	public void searchPORecordByPage(String orderNumber, String orderType, String supplierNumber, String description, int pageNum){
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.SEARCHING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getJobCostRepository().getPurchaseOrderEnquiryWrapperByPage(globalSectionController.getJob().getJobNumber(), orderNumber, orderType, supplierNumber, description, pageNum, new AsyncCallback<PurchaseOrderEnquiryWrapper>(){
			public void onSuccess(PurchaseOrderEnquiryWrapper wrapper) { 
				populateGridByPage(wrapper);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
		
	}	

	/**
	 * added by heisonwong, 
	 * obtaining parameters for generating reports	
	 * **/
	
	private String obtainParameters(){ 
		String parameters = "?jobNumber="+globalSectionController.getJob().getJobNumber()+
				"&supplierNumber=" + supplierNumberTextField.getText().trim()+
				"&orderNumber=" + orderNumberTextField.getText().trim()+
				"&orderType=" + orderTypeComboBox.getText().toUpperCase().trim()+
				"&lineDescription=" + lineDescriptionTextField.getText().trim()+
				"&reportName=" + "purchaseOrderReport";
		return parameters;
	}
	
}
