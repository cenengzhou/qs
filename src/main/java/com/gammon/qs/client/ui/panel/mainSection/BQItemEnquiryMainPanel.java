package com.gammon.qs.client.ui.panel.mainSection;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.panel.detailSection.BQResourceDetailPanel;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.BQItem;
import com.gammon.qs.domain.BQLineType;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.BQItemPaginationWrapper;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
/**
 * @author koeyyeung
 * modified on 6 Aug, 2013
 * **/
public class BQItemEnquiryMainPanel extends EditorGridPanel{
	private Integer rowIndex = null;

	private Store dataStore;
	
	private String screenName = "bq-item-editor-grid";

	private GlobalMessageTicket globalMessageTicket;
	
	private static final String SEQUENCE_NO_RECORD_NAME = "sequenceNo";
	private static final String BPI_RECORD_NAME = "bpi";
	private static final String BILL_NO_RECORD_NAME = "billNo";
	private static final String SUB_BILL_NO_RECORD_NAME = "subBillNo";
	private static final String SECTION_RECORD_NAME = "section";
	private static final String PAGE_NO_RECORD_NAME = "pageNo";
	private static final String ITEM_NO_RECORD_NAME = "itemNo";
	private static final String DESCRIPTION_RECORD_NAME = "description";
	private static final String QUANTITY_RECORD_NAME = "quantity";
	private static final String UOM_RECORD_NAME = "uom";
	private static final String SELLING_RATE_RECORD_NAME = "sellingRate";
	private static final String TOTAL_SELLING_VALUE_RECORD_NAME = "totalSellingValue";
	private static final String COST_RATE_RECORD_NAME = "costRate";
	private static final String COST_AMOUNT_RECORD_NAME = "costAmount";
	private static final String REMEASURED_QUANTITY_RECORD_NAME = "remeasuredQuantity";
	private static final String BQ_TYPE_RECORD_NAME = "bqType";
	private static final String BQ_STATUS_RECORD_NAME = "bqStatus";
	private static final String LINE_TYPE_RECORD_NAME = "lineType";
	private static final String GENUINE_MARKUP_RATE_RECORD_NAME = "genuineMarkupRate";
	
	private RecordDef bqItemRecordDef = new RecordDef(
		new FieldDef[] {
			new StringFieldDef(SEQUENCE_NO_RECORD_NAME),
			new StringFieldDef(BPI_RECORD_NAME),					
			new StringFieldDef(BILL_NO_RECORD_NAME),
			new StringFieldDef(SUB_BILL_NO_RECORD_NAME),
			new StringFieldDef(SECTION_RECORD_NAME),

			new StringFieldDef(PAGE_NO_RECORD_NAME),					
			new StringFieldDef(ITEM_NO_RECORD_NAME),
			new StringFieldDef(DESCRIPTION_RECORD_NAME),
			new StringFieldDef(QUANTITY_RECORD_NAME),					
			new StringFieldDef(UOM_RECORD_NAME),

			new StringFieldDef(SELLING_RATE_RECORD_NAME),
			new StringFieldDef(TOTAL_SELLING_VALUE_RECORD_NAME),
			new StringFieldDef(COST_RATE_RECORD_NAME),
			new StringFieldDef(COST_AMOUNT_RECORD_NAME),
			new StringFieldDef(REMEASURED_QUANTITY_RECORD_NAME),
			new StringFieldDef(BQ_TYPE_RECORD_NAME),

			new StringFieldDef(BQ_STATUS_RECORD_NAME),
			new StringFieldDef(LINE_TYPE_RECORD_NAME),
			new StringFieldDef(GENUINE_MARKUP_RATE_RECORD_NAME)
	}
	);
	
	private TextField billNoTextField;
	private TextField subBillNoTextField;
	private TextField pageNoTextField;
	private TextField itemNoTextField;
	private TextField bqDescriptionTextField;
	
	private String MAIN_PANEL_ID;
	private PaginationToolbar paginationToolbar;
	private GlobalSectionController globalSectionController;

	private ToolbarTextItem totalSellingValueTextItem;

	private String billNo;
	private String subBillNo;
	private String pageNo;
	
	public BQItemEnquiryMainPanel(final GlobalSectionController globalSectionController, String billNo, String subBillNo, String pageNo){
		super();
		this.globalSectionController = globalSectionController;
		globalMessageTicket = new GlobalMessageTicket();
		MAIN_PANEL_ID = globalSectionController.getMainSectionController().getMainPanel().getId();
		
		this.billNo=billNo;
		this.subBillNo=subBillNo;
		this.pageNo=pageNo;
		
		setupUI();
	}
	private void setupUI(){
		this.setTrackMouseOver(true);
		this.setBorder(false);
		this.setAutoScroll(true);
		
		setupGridPanel();
		
		search(billNo, subBillNo, pageNo, "", "");
	}
	
	private void setupToolbar(){
		Toolbar toolbar = new Toolbar();
		
		//Filter Listener
		FieldListener fieldListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER){
					search(billNoTextField.getText().trim(), subBillNoTextField.getText().trim(), 
							pageNoTextField.getText().trim(),itemNoTextField.getText().trim(), bqDescriptionTextField.getText().trim());
				}
			}
		};

		//bill number
		ToolbarTextItem billNoLabel = new ToolbarTextItem("Bill No.:");
		billNoTextField = new TextField();
		billNoTextField.setWidth(120);
		billNoTextField.addListener(fieldListener);
		billNoTextField.setValue(billNo);
		toolbar.addItem(billNoLabel);
		toolbar.addField(billNoTextField);
		toolbar.addSpacer();

		//subbill number
		ToolbarTextItem subBillNoLabel = new ToolbarTextItem("SubBill No.:");
		subBillNoTextField = new TextField();
		subBillNoTextField.setWidth(120);
		subBillNoTextField.addListener(fieldListener);
		subBillNoTextField.setValue(subBillNo);
		toolbar.addItem(subBillNoLabel);
		toolbar.addField(subBillNoTextField);
		toolbar.addSpacer();
		
		//page number
		ToolbarTextItem pageNoLabel = new ToolbarTextItem("Page No.:");
		pageNoTextField = new TextField();
		pageNoTextField.setWidth(120);
		pageNoTextField.addListener(fieldListener);
		pageNoTextField.setValue(pageNo);
		toolbar.addItem(pageNoLabel);
		toolbar.addField(pageNoTextField);
		toolbar.addSpacer();
		
		//item number
		ToolbarTextItem itemNoLabel = new ToolbarTextItem("Item No.:");
		itemNoTextField = new TextField();
		itemNoTextField.setWidth(120);
		itemNoTextField.addListener(fieldListener);
		toolbar.addItem(itemNoLabel);
		toolbar.addField(itemNoTextField);
		toolbar.addSpacer();

		//bq description
		ToolbarTextItem bqDescriptionLabel = new ToolbarTextItem("BQ Description:");
		bqDescriptionTextField = new TextField();
		bqDescriptionTextField.setWidth(150);
		bqDescriptionTextField.addListener(fieldListener);
		toolbar.addItem(bqDescriptionLabel);
		toolbar.addField(bqDescriptionTextField);
		toolbar.addSpacer();
		toolbar.addSeparator();
		
		//Search Button
		ToolbarButton searchButton = new ToolbarButton("Search");
		searchButton.setIconCls("find-icon");
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				search(billNoTextField.getText().trim(), subBillNoTextField.getText().trim(), 
						pageNoTextField.getText().trim(),itemNoTextField.getText().trim(), bqDescriptionTextField.getText().trim());
			};
		});
		toolbar.addButton(searchButton);
		toolbar.addSeparator();
		
		ToolTip searchButtonToolTip = new ToolTip();
		searchButtonToolTip.setTitle("Search");
		searchButtonToolTip.setHtml("Search BQ");
		searchButtonToolTip.applyTo(searchButton);
		
		//download excel button
		Item exportExcelButton = new Item("Export BQ");
		exportExcelButton.setIconCls("download-icon");
		exportExcelButton.addListener(new BaseItemListenerAdapter(){
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				com.google.gwt.user.client.Window.open(GlobalParameter.BQ_EXCEL_DOWNLOAD_URL
													+"?jobNumber="+globalSectionController.getJob().getJobNumber()
													+"&billNo="+billNoTextField.getText().trim()
													+"&subBillNo="+subBillNoTextField.getText().trim()
													+"&pageNo="+pageNoTextField.getText().trim()
													+"&itemNo="+itemNoTextField.getText().trim()
													+"&bqDesc="+bqDescriptionTextField.getText().trim()
													, "_blank", "");
			}
		});
		
		Menu excelMenu = new Menu();
		excelMenu.addItem(exportExcelButton);
		
		ToolbarButton excelToolbarButton = new ToolbarButton("Excel");
		excelToolbarButton.setMenu(excelMenu);
		excelToolbarButton.setIconCls("excel-icon");
		toolbar.addButton(excelToolbarButton);
		toolbar.addSeparator();
		
		setTopToolbar(toolbar);
	}
	
	private void setupGridPanel(){
		setupToolbar();
		
		ColumnConfig seqNoColumn = new ColumnConfig("Seq", SEQUENCE_NO_RECORD_NAME,30, false);
		ColumnConfig bpiColumn = new ColumnConfig("B/P/I", BPI_RECORD_NAME, 120 , false);

		ColumnConfig billNoColumn = new ColumnConfig("Bill No.", BILL_NO_RECORD_NAME, 50 , false);
		billNoColumn.setHidden(true);

		ColumnConfig subBillNoColumn = new ColumnConfig("Sub-Bill No.", SUB_BILL_NO_RECORD_NAME, 50 , false);		
		subBillNoColumn.setHidden(true);

		ColumnConfig sectionColumn = new ColumnConfig("Section", SECTION_RECORD_NAME, 50, false);		
		sectionColumn.setHidden(true);

		ColumnConfig pageNoColumn = new ColumnConfig("Page No.", PAGE_NO_RECORD_NAME, 50 , false);
		pageNoColumn.setHidden(true);
		
		ColumnConfig itemNoColumn = new ColumnConfig("Item No.", ITEM_NO_RECORD_NAME, 70 , false);
		itemNoColumn.setHidden(true);


		Renderer quantityRenderer = new QuantityRenderer(globalSectionController.getUser());
		Renderer amountRenderer = new AmountRenderer(globalSectionController.getUser());
		Renderer rateRenderer = new RateRenderer(globalSectionController.getUser());


		ColumnConfig descriptionColumn = new ColumnConfig("Description", DESCRIPTION_RECORD_NAME, 250,false);
		descriptionColumn.setRenderer( new Renderer(){
			public String render(Object value, CellMetadata cellMetadata, Record record,
					int rowIndex, int colNum, Store store) {

				String strValue = (String) value;

				if(!BQLineType.BQ_LINE.equals(record.getAsString(LINE_TYPE_RECORD_NAME)))
					return "<font color=#0000FF>"+ (value!=null? strValue:"")+"</font>";

				return (String)value;
			}
		});

		ColumnConfig quantityColumn = new ColumnConfig("Qty", QUANTITY_RECORD_NAME,100,false);
		quantityColumn.setRenderer(quantityRenderer);		
		quantityColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig uomColumn = new ColumnConfig("Unit", UOM_RECORD_NAME,40,false);		

		ColumnConfig sellingRateColumn = new ColumnConfig("Selling Rate", SELLING_RATE_RECORD_NAME,100,false);
		sellingRateColumn.setRenderer(rateRenderer);
		sellingRateColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig costRateColumn = new ColumnConfig("Cost Rate", COST_RATE_RECORD_NAME, 100, false);
		costRateColumn.setRenderer(rateRenderer);
		costRateColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig costAmountColumn = new ColumnConfig("Cost Amount", COST_AMOUNT_RECORD_NAME, 100, false);
		costAmountColumn.setRenderer(rateRenderer);
		costAmountColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig totalSellingValueColumn = new ColumnConfig("Selling Value", TOTAL_SELLING_VALUE_RECORD_NAME,100,false);
		totalSellingValueColumn.setRenderer(amountRenderer);
		totalSellingValueColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig remeasuredQuantityColumn = new ColumnConfig("Remeasured Qty", REMEASURED_QUANTITY_RECORD_NAME,100,false);
		remeasuredQuantityColumn.setRenderer(quantityRenderer);
		remeasuredQuantityColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig bqTypeColumn = new ColumnConfig("BQ Type", BQ_TYPE_RECORD_NAME,50,false);
		ColumnConfig bqStatusColumn = new ColumnConfig("BQ Status", BQ_STATUS_RECORD_NAME,60,false);
		ColumnConfig genuineMarkupColumn = new ColumnConfig("Genuine Markup", GENUINE_MARKUP_RATE_RECORD_NAME, 100, false);
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(bqItemRecordDef);
		this.dataStore = new Store(proxy, reader);
		this.dataStore.load();
		this.dataStore.setSortInfo(new SortState(SEQUENCE_NO_RECORD_NAME,SortDir.ASC));
		this.setStore(this.dataStore);

		ColumnConfig[] columns = new ColumnConfig[] {
				seqNoColumn,
				bpiColumn,

				billNoColumn,
				subBillNoColumn,
				sectionColumn,
				pageNoColumn,
				itemNoColumn,


				descriptionColumn,	
				uomColumn,
				quantityColumn,
				remeasuredQuantityColumn,

				sellingRateColumn,
				totalSellingValueColumn,
				costRateColumn,
				costAmountColumn,

				bqTypeColumn,
				bqStatusColumn,
				genuineMarkupColumn
		};

		ColumnConfig[] customizedColumns = globalSectionController.applyScreenPreferences(this.getScreenName(), columns);
		this.setColumnModel(new ColumnModel(customizedColumns));

		GridView view = new CustomizedGridView();
		view.setAutoFill(false);
		view.setForceFit(false);
		this.setView(view);

		this.addGridRowListener(new GridRowListenerAdapter(){
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e)	{	
				globalMessageTicket.refresh();
				if(BQItemEnquiryMainPanel.this.rowIndex== null || BQItemEnquiryMainPanel.this.rowIndex.intValue() != rowIndex){					
					BQItemEnquiryMainPanel.this.rowIndex =  new Integer(rowIndex);

					Record curRecord = dataStore.getAt(rowIndex);

					String billNo = (curRecord.getAsObject(BILL_NO_RECORD_NAME)!=null? curRecord.getAsString(BILL_NO_RECORD_NAME):"");
					String subBillNo = (curRecord.getAsObject(SUB_BILL_NO_RECORD_NAME)!=null?curRecord.getAsString(SUB_BILL_NO_RECORD_NAME):"");
					//String sectionNo = (curRecord.getAsObject(SECTION_RECORD_NAME)!=null? curRecord.getAsString(SECTION_RECORD_NAME):"");
					String pageNo = (curRecord.getAsObject(PAGE_NO_RECORD_NAME)!=null? curRecord.getAsString(PAGE_NO_RECORD_NAME):""); 
					String itemNo  = (curRecord.getAsObject(ITEM_NO_RECORD_NAME)!=null ? curRecord.getAsString(ITEM_NO_RECORD_NAME): "");

					if(BQLineType.BQ_LINE.equals(curRecord.getAsString(LINE_TYPE_RECORD_NAME))){
						globalSectionController.getDetailSectionController().resetPanel();
						globalSectionController.getDetailSectionController().getMainPanel().setTitle("Resource Enquiry");
						BQResourceDetailPanel bqResourceDetailPanel = new BQResourceDetailPanel(globalSectionController, billNo, subBillNo, pageNo, itemNo);
						globalSectionController.getDetailSectionController().setPanel(bqResourceDetailPanel);
						globalSectionController.getDetailSectionController().populatePanel();
					}
				}
			}
		});


		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(int pageNum){
				globalMessageTicket.refresh();
				goToBQItemsPage(pageNum);
			}
		});
		totalSellingValueTextItem = new ToolbarTextItem("");
		paginationToolbar.addFill();
		paginationToolbar.addItem(totalSellingValueTextItem);
		
		setBottomToolbar(paginationToolbar);
		paginationToolbar.setTotalPage(0);
		paginationToolbar.setCurrentPage(0);
	}

	private void populateGrid(BQItemPaginationWrapper wrapper) {
		dataStore.removeAll();
		if (wrapper == null) 
			return;

		paginationToolbar.setTotalPage(wrapper.getTotalPage());
		paginationToolbar.setCurrentPage(wrapper.getCurrentPage());
		NumberFormat nf = NumberFormat.getFormat("#,##0.00");
		totalSellingValueTextItem.setText("<b>Total Selling Value:&nbsp;&nbsp;" + nf.format(wrapper.getTotalSellingValue()) + "</b>");
		
		@SuppressWarnings("unused")
		double totalSellingValue = 0 ;
		for (BQItem curBQItem: wrapper.getCurrentPageContentList()){
			if(curBQItem.getBQLineType().getName().equals(BQLineType.BQ_LINE)){
				Double remeasuredQuantity = curBQItem.getRemeasuredQuantity();
				if(remeasuredQuantity == null)
					remeasuredQuantity = Double.valueOf(0);
				Double sellingValue = curBQItem.getSellingRate() != null ? this.calcuTotalSellingValue(curBQItem.getSellingRate(), remeasuredQuantity) : new Double(0);
				totalSellingValue += sellingValue;
				
				Record record = bqItemRecordDef.createRecord(new Object[]{
						curBQItem.getSequenceNo(),	
						//bill item
						((curBQItem.getPage().getBill().getBillNo()!=null) ?curBQItem.getPage().getBill().getBillNo().trim():"")+"/"
						+((curBQItem.getPage().getBill().getSubBillNo()!=null) ?curBQItem.getPage().getBill().getSubBillNo().trim():"")+"/"
						+((curBQItem.getPage().getBill().getSectionNo()!=null) ?curBQItem.getPage().getBill().getSectionNo().trim():"")+"/"
						+(curBQItem.getPage()!=null ?(curBQItem.getPage().getPageNo()!=null?curBQItem.getPage().getPageNo().trim():""):"")+"/"
						+(curBQItem.getItemNo()!=null&& !"".equals(curBQItem.getItemNo()) ?curBQItem.getItemNo():" "),
						
						(curBQItem.getPage().getBill().getBillNo()!=null?curBQItem.getPage().getBill().getBillNo().trim():""),
						(curBQItem.getPage().getBill().getSubBillNo()!=null?curBQItem.getPage().getBill().getSubBillNo().trim():""),
						(curBQItem.getPage().getBill().getSectionNo()!=null?curBQItem.getPage().getBill().getSectionNo().trim():""),		

						(curBQItem.getPage()!=null && curBQItem.getPage().getPageNo()!=null?curBQItem.getPage().getPageNo().trim():""),
						(curBQItem.getItemNo() !=null ? curBQItem.getItemNo():""),								
						curBQItem.getDescription(),
						curBQItem.getQuantity(),
						curBQItem.getUnit(),		

						curBQItem.getSellingRate(),
						sellingValue,
						curBQItem.getCostRate(),
						curBQItem.getCostRate() != null ? curBQItem.getCostRate() * remeasuredQuantity : "0", 
						curBQItem.getRemeasuredQuantity(),
						curBQItem.getBqType(),	

						curBQItem.getBqStatus(),
						(curBQItem.getBQLineType().getName()!=null?curBQItem.getBQLineType().getName():""),
						curBQItem.getGenuineMarkupRate()
				});
				dataStore.add(record);
			}
			else{
				Record record = bqItemRecordDef.createRecord(new Object[]{
						curBQItem.getSequenceNo(),								
						"",
						"",
						"",
						"",		

						"",
						"",								
						curBQItem.getDescription(),
						"",
						"",		

						"",
						"",
						"",
						"",
						"",
						"",	

						"",
						(curBQItem.getBQLineType().getName()!=null?curBQItem.getBQLineType().getName():""),
						""
				});
				dataStore.add(record);
			}
		}
	}
	
	public void search(String billNo, String subBillNo, String pageNo, String itemNo, String bqDesc){
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getBqRepository().obtainBQItem(globalSectionController.getJob().getJobNumber(), billNo, subBillNo, pageNo, itemNo, bqDesc, new AsyncCallback<BQItemPaginationWrapper>(){
			public void onSuccess(BQItemPaginationWrapper result) {
				populateGrid(result);
				globalSectionController.getDetailSectionController().resetPanel();
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
	}

	public void goToBQItemsPage(int pageNum){
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getBqRepository().getBqItemsByPage(pageNum, new AsyncCallback<BQItemPaginationWrapper>(){
			public void onSuccess(BQItemPaginationWrapper wrapper) {
				populateGrid(wrapper);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
	}
	
	// calculation 
	public Double calcuTotalSellingValue(Double sellingRate, Double quantity){
		return new Double(Math.round(sellingRate * quantity));
	}

	public String getScreenName() {
		return screenName;
	}

}
