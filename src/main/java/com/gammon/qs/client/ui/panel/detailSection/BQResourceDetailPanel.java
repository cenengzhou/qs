package com.gammon.qs.client.ui.panel.detailSection;


import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.Resource;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.BQPaginationWrapper;
import com.gammon.qs.wrapper.updateIVByResource.ResourceWrapper;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtext.client.core.EventObject;

public class BQResourceDetailPanel extends Panel{
	private GridPanel gridPanel;
	private GlobalSectionController globalSectionController;
	private Store dataStore;
	private GlobalMessageTicket globalMessageTicket;
	
	private String screenName = "bq-item-resource-editor-grid";
	
	//pagination
	private int currentPage =0;
	private int totalPage =0;
	private PaginationToolbar paginationToolbar;
	private ToolbarTextItem totalCostTextItem;
	
	private RecordDef resourceRecordDef = new RecordDef(new FieldDef[]{
			new StringFieldDef("subsidaryCode"),
			new StringFieldDef("objectCode"),
			new StringFieldDef("uom"),
			new StringFieldDef("quantity"),
			new StringFieldDef("type"),
			
			new StringFieldDef("costRate"),
			new StringFieldDef("costAmount"),
			
			new StringFieldDef("materialWastage"),
			new StringFieldDef("resourceNo"),			
			new StringFieldDef("splitStatus"),
			new StringFieldDef("packageNo"),
			new StringFieldDef("packageType"),
			
			new StringFieldDef("packageNature"),
			new StringFieldDef("description"),
			new StringFieldDef("packageStatus"),
		});
	
	private TextField billNoTextField;
	private TextField subBillNoTextField;
	private TextField pageNoTextField;
	private TextField itemNoTextField;
	private TextField resourceDescTextField;
	private TextField subsidiaryCodeTextField;
	private TextField objectCodeTextField;
	private TextField packageNoTextField;
	private TextField packageTypeTextField;
	
	private String detailSectionPanel_ID;
	
	private String billNo;
	private String subBillNo;
	private String pageNo;
	private String itemNo;
	
	public BQResourceDetailPanel(GlobalSectionController globalSectionController, String billNo, String subBillNo, String pageNo, String itemNo){
		super();
		globalMessageTicket  = new GlobalMessageTicket();
		this.globalSectionController = globalSectionController;
		detailSectionPanel_ID = globalSectionController.getDetailSectionController().getMainPanel().getId();
		
		this.billNo=billNo;
		this.subBillNo=subBillNo;
		this.pageNo=pageNo;
		this.itemNo=itemNo;
		
		setupUI();
	}
	
	private void setupUI(){
		setLayout(new RowLayout());
		setPaddings(1);
		setupGridPanel();
		
		search(billNo, subBillNo, pageNo, itemNo, "", "", "", "", "");
	}
		
	private void setupToolbar(){
		Toolbar upperToolbar = new Toolbar();
		upperToolbar.setHeight(25);
		
		//Filter Listener
		FieldListener fieldListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER){
					search(billNoTextField.getText(), subBillNoTextField.getText(), pageNoTextField.getText(),
						   itemNoTextField.getText(), resourceDescTextField.getText(), subsidiaryCodeTextField.getText(),
						   objectCodeTextField.getText(), packageNoTextField.getText(), packageTypeTextField.getText());
				}
			}
		};

		//bill number
		ToolbarTextItem billNoLabel = new ToolbarTextItem("<span style='padding-right:48px'>Bill No:</span>");
		billNoTextField = new TextField();
		billNoTextField.setWidth(120);
		billNoTextField.addListener(fieldListener);
		billNoTextField.setValue(billNo);
		upperToolbar.addItem(billNoLabel);
		upperToolbar.addField(billNoTextField);
		upperToolbar.addSpacer();

		//subbill number
		ToolbarTextItem subBillNoLabel = new ToolbarTextItem("<span style='padding-right:15px'>SubBill No:</span>");
		subBillNoTextField = new TextField();
		subBillNoTextField.setWidth(120);
		subBillNoTextField.addListener(fieldListener);
		subBillNoTextField.setValue(subBillNo);
		upperToolbar.addItem(subBillNoLabel);
		upperToolbar.addField(subBillNoTextField);
		upperToolbar.addSpacer();
		
		//page number
		ToolbarTextItem pageNoLabel = new ToolbarTextItem("<span style='padding-right:16px'>Page No:</span>");
		pageNoTextField = new TextField();
		pageNoTextField.setWidth(120);
		pageNoTextField.addListener(fieldListener);
		pageNoTextField.setValue(pageNo);
		upperToolbar.addItem(pageNoLabel);
		upperToolbar.addField(pageNoTextField);
		upperToolbar.addSpacer();
		
		//item number
		ToolbarTextItem itemNoLabel = new ToolbarTextItem("<span style='padding-right:28px'>Item No:</span>");
		itemNoTextField = new TextField();
		itemNoTextField.setWidth(120);
		itemNoTextField.addListener(fieldListener);
		itemNoTextField.setValue(itemNo);
		upperToolbar.addItem(itemNoLabel);
		upperToolbar.addField(itemNoTextField);
		upperToolbar.addSpacer();

		//resource description
		ToolbarTextItem resourceDesLabel = new ToolbarTextItem("Resource Description:");
		resourceDescTextField = new TextField();
		resourceDescTextField.setWidth(150);
		resourceDescTextField.addListener(fieldListener);
		upperToolbar.addItem(resourceDesLabel);
		upperToolbar.addField(resourceDescTextField);
		upperToolbar.addSpacer();
		upperToolbar.addSeparator();

		//Search Button
		ToolbarButton searchButton = new ToolbarButton("Search");
		searchButton.setIconCls("find-icon");
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				search(billNoTextField.getText(), subBillNoTextField.getText(), pageNoTextField.getText(),
						   itemNoTextField.getText(), resourceDescTextField.getText(), subsidiaryCodeTextField.getText(),
						   objectCodeTextField.getText(), packageNoTextField.getText(), packageTypeTextField.getText());
			};
		});
		upperToolbar.addButton(searchButton);
		upperToolbar.addSeparator();
		
		ToolTip searchButtonToolTip = new ToolTip();
		searchButtonToolTip.setTitle("Search");
		searchButtonToolTip.setHtml("Search Resources");
		searchButtonToolTip.applyTo(searchButton);
		
		//download excel button
		Item exportExcelButton = new Item("Export Rescources");
		exportExcelButton.setIconCls("download-icon");
		exportExcelButton.addListener(new BaseItemListenerAdapter(){
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				com.google.gwt.user.client.Window.open(GlobalParameter.IV_EXCEL_DOWNLOAD_URL
													+"?jobNumber="+globalSectionController.getJob().getJobNumber()
													+"&billNo="+billNoTextField.getText().trim()
													+"&subBillNo="+subBillNoTextField.getText().trim()
													+"&pageNo="+pageNoTextField.getText().trim()
													+"&itemNo="+itemNoTextField.getText().trim()
													+"&subsidiaryCode="+subsidiaryCodeTextField.getText().trim()
													+"&objectCode="+objectCodeTextField.getText().trim()
													+"&resDescription="+resourceDescTextField.getText().trim()
													+"&packageNo="+packageNoTextField.getText().trim()
													+"&packageType="+packageTypeTextField.getText().trim()
													, "_blank", "");				
			}
		});

		Menu excelMenu = new Menu();
		excelMenu.addItem(exportExcelButton);
		
		ToolbarButton excelToolbarButton = new ToolbarButton("Excel");
		excelToolbarButton.setMenu(excelMenu);
		excelToolbarButton.setIconCls("excel-icon");
		upperToolbar.addButton(excelToolbarButton);
		upperToolbar.addSeparator();
		
		
		Toolbar lowerToolbar = new Toolbar();
		lowerToolbar.setHeight(25);
		
		//subsidiary code
		ToolbarTextItem subsidiaryCodeLabel = new ToolbarTextItem("Subsidiary Code:");
		subsidiaryCodeTextField = new TextField();
		subsidiaryCodeTextField.setWidth(120);
		subsidiaryCodeTextField.addListener(fieldListener);
		lowerToolbar.addItem(subsidiaryCodeLabel);
		lowerToolbar.addField(subsidiaryCodeTextField);
		lowerToolbar.addSpacer();
		
		//object code
		ToolbarTextItem objectCodeLabel = new ToolbarTextItem("Object Code:");
		objectCodeTextField = new TextField();
		objectCodeTextField.setWidth(120);
		objectCodeTextField.addListener(fieldListener);
		lowerToolbar.addItem(objectCodeLabel);
		lowerToolbar.addField(objectCodeTextField);
		lowerToolbar.addSpacer();
		
		//package code
		ToolbarTextItem packageNoLabel = new ToolbarTextItem("Package No:");
		packageNoTextField = new TextField();
		packageNoTextField.setWidth(120);
		packageNoTextField.addListener(fieldListener);
		lowerToolbar.addItem(packageNoLabel);
		lowerToolbar.addField(packageNoTextField);
		lowerToolbar.addSpacer();
		
		//package type
		ToolbarTextItem packageTypeLabel = new ToolbarTextItem("Package Type:");
		packageTypeTextField = new TextField();
		packageTypeTextField.setWidth(120);
		packageTypeTextField.addListener(fieldListener);
		lowerToolbar.addItem(packageTypeLabel);
		lowerToolbar.addField(packageTypeTextField);
		lowerToolbar.addSpacer();
		
		add(upperToolbar);
		add(lowerToolbar);
	}
	
	private void setupGridPanel(){
		gridPanel = new GridPanel();
		gridPanel.setBorder(false);
		gridPanel.setFrame(false);
		gridPanel.setPaddings(0);
		gridPanel.setAutoScroll(true);
		gridPanel.setView(new CustomizedGridView());
		
		setupToolbar();
		
		Renderer quantityRenderer = new QuantityRenderer(globalSectionController.getUser());
		Renderer rateRenderer = new RateRenderer(globalSectionController.getUser());
		
		ColumnConfig resourceNoColumn = new ColumnConfig("Resource No.", "resourceNo", 40 , false);	
		ColumnConfig descriptionColumn = new ColumnConfig("Description", "description", 250 , false);
		ColumnConfig quantityColumn = new ColumnConfig("Qty", "quantity", 100 , false);
		quantityColumn.setRenderer(quantityRenderer);
		quantityColumn.setAlign(TextAlign.RIGHT);
		ColumnConfig costRateColumn = new ColumnConfig("Cost Rate", "costRate", 100 , false);
		costRateColumn.setRenderer(rateRenderer);
		costRateColumn.setAlign(TextAlign.RIGHT);		
		ColumnConfig costAmountColumn = new ColumnConfig("Cost Amount", "costAmount", 100 , false);
		costAmountColumn.setRenderer(rateRenderer);
		costAmountColumn.setAlign(TextAlign.RIGHT);
		ColumnConfig subsidaryCodeColumn = new ColumnConfig("Subsidary Code", "subsidaryCode", 90 , false);		
		ColumnConfig objectCodeColumn = new ColumnConfig("Object Code", "objectCode", 70 , false);
		ColumnConfig uomColumn = new ColumnConfig("Unit", "uom", 40 , false);
		ColumnConfig typeColumn = new ColumnConfig("Type", "type", 40, false);
		ColumnConfig materialWastageColumn = new ColumnConfig("Material Wastage %", "materialWastage", 100 , false);
		materialWastageColumn.setRenderer(rateRenderer);
		materialWastageColumn.setAlign(TextAlign.RIGHT);
		ColumnConfig splitStatusColumn = new ColumnConfig("Resource Status", "splitStatus", 80 , false);				
		ColumnConfig packageNoColumn = new ColumnConfig("Package No.", "packageNo", 80 , false);
		ColumnConfig packageTypeColumn = new ColumnConfig("Package Type", "packageType", 80 , false);	
		ColumnConfig packageNatureColumn = new ColumnConfig("Package Nature", "packageNature", 90 , false);		
		ColumnConfig packageStatusColumn = new ColumnConfig("Package Status", "packageStatus", 90 , false);

		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(resourceRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		gridPanel.setStore(this.dataStore);

		ColumnConfig[] columns = new ColumnConfig[] {	
				resourceNoColumn,
				objectCodeColumn,
				subsidaryCodeColumn,
				descriptionColumn,				
				quantityColumn,	
				uomColumn,
				costRateColumn,
				costAmountColumn,
				packageNoColumn,
				materialWastageColumn,
				splitStatusColumn,
				packageTypeColumn,				
				packageNatureColumn,				
				packageStatusColumn,
				typeColumn
		};
		ColumnConfig[] customizedColumns = globalSectionController.applyScreenPreferences(this.getScreenName(), columns);
		gridPanel.setColumnModel(new ColumnModel(customizedColumns));
		
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(int pageNum){
				globalMessageTicket.refresh();
				obtainResourcesByPage(pageNum);
			}
		});
		totalCostTextItem = new ToolbarTextItem("");
		paginationToolbar.addFill();
		paginationToolbar.addItem(totalCostTextItem);
		gridPanel.setBottomToolbar(paginationToolbar);		
		
		add(gridPanel);
	}
	
	
	public void search(String billNo, String subBillNo, String pageNo, String itemNo, String resDescription, String subsidiaryCode, String objectCode, String packageNo, String packageType) {
		ResourceWrapper wrapper = new ResourceWrapper();
		wrapper.setJobNumber(globalSectionController.getJob().getJobNumber());
		wrapper.setBillNo(billNo==null?"":billNo.replace("*", "%").trim());
		wrapper.setSubBillNo(subBillNo==null?"":subBillNo.replace("*", "%").trim());
		wrapper.setPageNo(pageNo==null?"":pageNo.replace("*", "%").trim());
		wrapper.setItemNo(itemNo==null?"":itemNo.replace("*", "%").trim());
		wrapper.setSubsidiaryCode(subsidiaryCode==null?"":subsidiaryCode.replace("*", "%").trim());
		wrapper.setObjectCode(objectCode==null?"":objectCode.replace("*", "%").trim());
		wrapper.setResDescription(resDescription==null?"":resDescription.replace("*", "%"));
		wrapper.setPackageNo(packageNo==null?"":packageNo.replace("*", "%"));
		wrapper.setPackageType(packageType==null?"":packageType.replace("*", "%").trim());
		
		UIUtil.maskPanelById(detailSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getBqRepository().obtainResourcesByWrapper(wrapper, new AsyncCallback<BQPaginationWrapper>() {
			public void onSuccess(BQPaginationWrapper wrapper) {
				populateGrid(wrapper);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
		});
	}
	
	public void populateGrid(BQPaginationWrapper wrapper) {
		this.dataStore.removeAll();

		if (wrapper == null) 
			return;
		
		List<Resource> resourceList = wrapper.getCurrentPageContentList();
		this.currentPage = wrapper.getCurrentPage();
		this.totalPage = wrapper.getTotalPage();
		Double totalCost = wrapper.getTotalCost();
		if(totalCost == null)
			totalCost = Double.valueOf(0);

		this.paginationToolbar.setTotalPage(totalPage);
		this.paginationToolbar.setCurrentPage(currentPage);
		NumberFormat nf = NumberFormat.getFormat("#,##0.00");
		totalCostTextItem.setText("<b>Total Cost Amount:&nbsp;&nbsp;" + nf.format(totalCost) + "</b>");				
		Record[] data = new Record[resourceList.size()];

		for(int i=0; i< resourceList.size(); i++){
			Resource curResource = resourceList.get(i);
			data[i]= resourceRecordDef.createRecord(new Object[]{
					curResource.getSubsidiaryCode(),
					curResource.getObjectCode(),
					curResource.getUnit(),
					curResource.getQuantity() * curResource.getRemeasuredFactor(),
					curResource.getResourceType(),

					curResource.getCostRate(),
					//By Peter Chan 2011-09-20
					(curResource.getQuantity() != null && curResource.getCostRate() != null && curResource.getRemeasuredFactor()!=null) ? curResource.getQuantity() * curResource.getCostRate()*curResource.getRemeasuredFactor() : "0",

					curResource.getMaterialWastage(),

					curResource.getResourceNo(),							
					curResource.getSplitStatus(),
					curResource.getPackageNo(),
					curResource.getPackageType(),

					curResource.getPackageNature(),
					curResource.getDescription(),
					curResource.getPackageStatus()
			});
		}
	
		this.dataStore.add(data);
		globalMessageTicket.refresh();
	}

	private void obtainResourcesByPage(int pageNum){
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getBqRepository().obtainResourcesByPage(pageNum, new AsyncCallback<BQPaginationWrapper>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}
			public void onSuccess(BQPaginationWrapper wrapper) {
				populateGrid(wrapper);
			}
		});
	}
	
	public String getScreenName() {
		return screenName;
	}
}
