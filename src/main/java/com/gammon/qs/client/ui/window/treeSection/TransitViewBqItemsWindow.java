package com.gammon.qs.client.ui.window.treeSection;

import java.util.Map;

import com.gammon.qs.application.GeneralPreferencesKey;
import com.gammon.qs.application.User;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.TransitRepositoryRemote;
import com.gammon.qs.client.repository.TransitRepositoryRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRendererCheckIfTotal;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.TransitBQ;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
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
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

public class TransitViewBqItemsWindow extends Window {
	public static String MAIN_PANEL_ID = "transitViewBqItemsWindow";
	private Panel mainPanel;
	private Panel searchPanel;
	private GridPanel gridPanel;
	private PaginationToolbar paginationToolbar;
	
	private TransitRepositoryRemoteAsync transitRepository;
	
	//Search fields
	private TextField billNoTextField;
	private TextField subBillNoTextField;
	private TextField pageNoTextField;
	private TextField itemNoTextField;
	private TextField descriptionTextField;
	private String jobNumber;
	
	private RecordDef bqItemRecordDef = new RecordDef(
			new FieldDef[] {
					new StringFieldDef("bpi"),
					new StringFieldDef("description"),
					new StringFieldDef("unit"),
					new StringFieldDef("quantity"),
					new StringFieldDef("sellingRate"),
					new StringFieldDef("value")
				}
			);
	
	private Store dataStore;
	private User user;
	
	public TransitViewBqItemsWindow(final GlobalSectionController globalSectionController){
		super();
		this.setId(MAIN_PANEL_ID);
		this.setTitle("Transit - View BQ Items");
		this.setModal(true);
		this.setClosable(false);
		this.setLayout(new FitLayout());
		this.setPaddings(5);
		this.setWidth(1024);
		this.setHeight(650);
		jobNumber = globalSectionController.getJob().getJobNumber();
		user=globalSectionController.getUser();
		transitRepository = (TransitRepositoryRemoteAsync) GWT.create(TransitRepositoryRemote.class);
		((ServiceDefTarget)transitRepository).setServiceEntryPoint(GlobalParameter.TRANSIT_REPOSITORY_URL);
		
		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		
		//search Panel
		searchPanel = new Panel();
		searchPanel.setPaddings(3);
		searchPanel.setFrame(true);
		searchPanel.setHeight(85);	
		searchPanel.setLayout(new TableLayout(6));
		
		FieldListener searchListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER){
					search();
				}
			}
		};
		
		Label billNoLabel = new Label("Bill No:");
		billNoLabel.setCls("table-cell");
		searchPanel.add(billNoLabel);
		billNoTextField =new TextField("Bill No", "billNo", 100);
		billNoTextField.setCtCls("table-cell");
		billNoTextField.addListener(searchListener);
		searchPanel.add(billNoTextField);
		
		Label subBillNoLabel = new Label("SubBill No");
		subBillNoLabel.setCls("table-cell");
		searchPanel.add(subBillNoLabel);
		subBillNoTextField =new TextField("SubBill No", "subBillNo", 100);
		subBillNoTextField.setCtCls("table-cell");
		subBillNoTextField.addListener(searchListener);
		searchPanel.add(subBillNoTextField);
		
		Label pageNoLabel = new Label("Page No");
		pageNoLabel.setCls("table-cell");
		searchPanel.add(pageNoLabel);
		pageNoTextField =new TextField("Page No", "pageNo", 100);
		pageNoTextField.setCtCls("table-cell");
		pageNoTextField.addListener(searchListener);
		searchPanel.add(pageNoTextField);
		
		Label itemNoLabel = new Label("Item No");
		itemNoLabel.setCls("table-cell");
		searchPanel.add(itemNoLabel);
		itemNoTextField =new TextField("Item No", "itemNo", 100);
		itemNoTextField.setCtCls("table-cell");
		itemNoTextField.addListener(searchListener);
		searchPanel.add(itemNoTextField);
		
		Label bqDescriptionLabel = new Label("BQ Description");
		bqDescriptionLabel.setCls("table-cell");
		searchPanel.add(bqDescriptionLabel);
		descriptionTextField = new TextField("BQ Description", "bqDescription", 200);
		descriptionTextField.setCtCls("table-cell");
		descriptionTextField.addListener(searchListener);
		searchPanel.add(descriptionTextField);
		
		Button searchButton = new Button("Search");
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				search();
			};
		});
		searchButton.setCls("table-cell");
		searchPanel.add(searchButton);
		
		gridPanel = new GridPanel();
		
		User user = globalSectionController.getUser();
		final Renderer quantityRenderer = new QuantityRenderer(user);
		final Renderer amountRenderer = new AmountRendererCheckIfTotal(user);
		final Renderer rateRenderer = new RateRenderer(user);
		Map<String, String> prefs = user.getGeneralPreferences();
		String quantDP = prefs.get(GeneralPreferencesKey.QUANTITY_DECIMAL_PLACES);
		if(quantDP == null)
			quantDP = "1";
		NumberField quantNumberField = new NumberField();
		quantNumberField.setDecimalPrecision(Integer.valueOf(quantDP));
		
		ColumnConfig bpiColumn = new ColumnConfig("Bill Item", "bpi", 120, false);
		ColumnConfig descriptionColumn = new ColumnConfig("Description", "description", 220, false);
		descriptionColumn.setRenderer(new Renderer(){
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				String str = (String)value;
				if(record.getAsString("unit") == null || record.getAsString("unit").length() == 0)
					str = "<span style=\"color:blue;\">" + str + "</span>";
				
				if("TOTAL:".equals(record.getAsString("description"))){
					str = (String)value;
					str = "<b>" + str + "</b>";
				}
				
				return str;
			}
		});
		ColumnConfig unitColumn = new ColumnConfig("Unit", "unit", 35, false);
		ColumnConfig quantityColumn = new ColumnConfig("Qty", "quantity", 100, false);
		quantityColumn.setRenderer(quantityRenderer);		
		quantityColumn.setAlign(TextAlign.RIGHT);
		ColumnConfig sellingRateColumn = new ColumnConfig("Selling Rate", "sellingRate", 100, false);
		sellingRateColumn.setRenderer(rateRenderer);
		sellingRateColumn.setAlign(TextAlign.RIGHT);
		ColumnConfig valueColumn = new ColumnConfig("Value", "value", 120, false);
		valueColumn.setRenderer(amountRenderer);
		valueColumn.setAlign(TextAlign.RIGHT);
		
		ColumnConfig[] columns = new ColumnConfig[]{
			bpiColumn,
			descriptionColumn,
			unitColumn,
			quantityColumn,
			sellingRateColumn,
			valueColumn
		};
		
		gridPanel.setColumnModel(new ColumnModel(columns));
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(bqItemRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		gridPanel.setStore(dataStore);
		
		GridView view = new CustomizedGridView();
		view.setAutoFill(true);
		view.setForceFit(false);
		gridPanel.setView(view);
		
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setTotalPage(0);
		paginationToolbar.setCurrentPage(0);
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(final int pageNum){
				if(dataStore.getModifiedRecords().length > 0){
					MessageBox.confirm("Continue?", "If you continue to next page, the changes you made on this page will be lost.  Continue to next page?", 
							new MessageBox.ConfirmCallback(){
								public void execute(String btnID) {
									if(btnID.equals("yes"))
										searchByPage(pageNum);
								}
					});
				}
				else
					searchByPage(pageNum);
			}
		});
		gridPanel.setBottomToolbar(paginationToolbar);
		
		mainPanel.add(searchPanel);
		mainPanel.add(gridPanel);
		this.add(mainPanel);
		
		Button closeButton = new Button("Close");
		closeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalSectionController.closeCurrentWindow();
			};
		});
		this.addButton(closeButton);
	}
	
	private void search(){
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Retrieving Records", true);
		SessionTimeoutCheck.renewSessionTimer();
		transitRepository.getTransitBQItemsNewSearch(jobNumber, billNoTextField.getValueAsString(), 
				subBillNoTextField.getValueAsString(), pageNoTextField.getValueAsString(), itemNoTextField.getValueAsString(), 
				descriptionTextField.getValueAsString(), new AsyncCallback<PaginationWrapper<TransitBQ>>(){
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, false,user);
			}

			public void onSuccess(PaginationWrapper<TransitBQ> wrapper) {
				populateGrid(wrapper);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}			
		});
	}
	
	private void searchByPage(int pageNum){
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Retrieving Records", true);
		SessionTimeoutCheck.renewSessionTimer();
		transitRepository.getTransitBQItemsByPage(pageNum,  new AsyncCallback<PaginationWrapper<TransitBQ>>(){
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, false,user);
			}

			public void onSuccess(PaginationWrapper<TransitBQ> wrapper) {
				populateGrid(wrapper);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}			
		});
	}
	
	private void populateGrid(PaginationWrapper<TransitBQ> wrapper){
		dataStore.removeAll();
		if(wrapper == null)
			return;
		paginationToolbar.setTotalPage(wrapper.getTotalPage());
		paginationToolbar.setCurrentPage(wrapper.getCurrentPage());
		if(wrapper.getCurrentPageContentList() == null)
			return;
		
		for(TransitBQ bq : wrapper.getCurrentPageContentList()){
			String bpi = "";
			
			if(!"TOTAL:".equals(bq.getDescription())){
				bpi += bq.getBillNo() == null ? "/" : bq.getBillNo() + "/";
				bpi += bq.getSubBillNo() == null ? "//" : bq.getSubBillNo() + "//";
				bpi += bq.getPageNo() == null ? "/" : bq.getPageNo() + "/";
				bpi += bq.getItemNo() == null ? "" : bq.getItemNo();
			}
			
			String quantity = "";
			if(!"TOTAL:".equals(bq.getDescription()))
				quantity = bq.getQuantity() != null ? bq.getQuantity().toString() : "";
			else
				quantity = "";
			
			String sellingRate = "";
			if(!"TOTAL:".equals(bq.getDescription()))
				sellingRate = bq.getSellingRate() != null ? bq.getSellingRate().toString() : "";
			else
				sellingRate = "";
				
			Record record = bqItemRecordDef.createRecord(new Object[]{
					bpi,
					bq.getDescription(),
					bq.getUnit(),
					quantity,
					sellingRate,
					bq.getValue() != null ? bq.getValue().toString() : ""
			});
			dataStore.add(record);
		}
	}
}
