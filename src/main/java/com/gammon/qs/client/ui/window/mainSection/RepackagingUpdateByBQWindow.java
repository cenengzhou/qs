package com.gammon.qs.client.ui.window.mainSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gammon.qs.application.GeneralPreferencesKey;
import com.gammon.qs.application.User;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.BQRepositoryRemote;
import com.gammon.qs.client.repository.BQRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemote;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.panel.mainSection.RepackagingListGridPanel;
import com.gammon.qs.client.ui.renderer.AmountRendererCheckIfTotal;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.BQItem;
import com.gammon.qs.domain.Resource;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
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
import com.gwtext.client.widgets.ToolbarButton;
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
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.EditorGridListener;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

public class RepackagingUpdateByBQWindow extends Window {
	public static final String MAIN_PANEL_ID = "repackagingUpdateByBQWindow";
	
	private GlobalSectionController globalSectionController;
	private RepackagingListGridPanel repackagingListGridPanel;
	private Panel mainPanel;
	private Panel searchPanel;
	private EditorGridPanel resultEditorGridPanel;
	private GlobalMessageTicket globalMessageTicket;
	private Window childWindow;
	
	private BQRepositoryRemoteAsync bqRepository;
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	
	private Button saveButton;
	private ToolbarButton addChangeOrderButton;
	private ToolbarButton editChangeOrderButton;
	private ToolbarButton balanceResourcesButton;
	
	private PaginationToolbar paginationToolbar;
	//Search parameters (used for pagination)
	private String jobNumber;
	private String billNo;
	private String subBillNo;
	private String pageNo;
	private String itemNo;
	private String bqDescription;
	
	private RowSelectionModel rowSelectionModel;
	private Store dataStore;
	
	//Search fields
	private TextField billNoTextField;
	private TextField subBillNoTextField;
	private TextField pageNoTextField;
	private TextField itemNoTextField;
	private TextField bqDescriptionTextField;
	
	private RecordDef bqItemRecordDef = new RecordDef(
			new FieldDef[] {
					new StringFieldDef("id"),
					new StringFieldDef("bpi"),
					new StringFieldDef("bqType"),
					new StringFieldDef("description"),
					new StringFieldDef("unit"),
					new StringFieldDef("quantity"),
					new StringFieldDef("remeasuredQuantity"),
					new StringFieldDef("sellingRate"),
					new StringFieldDef("sellingAmount"),
					new StringFieldDef("costRate"),
					new StringFieldDef("costAmount"),
					
					//Reference only
					new StringFieldDef("postedIVAmount"),
					new StringFieldDef("cumulativeIVAmount")
				}
			);
	
	private ColumnConfig bpiColumn;
	private ColumnConfig bqTypeColumn;
	private ColumnConfig descriptionColumn;
	private ColumnConfig unitColumn;
	private ColumnConfig quantityColumn;
	private ColumnConfig remeasuredQuantityColumn;
	private ColumnConfig sellingRateColumn;
	private ColumnConfig sellingAmountColumn;
	private ColumnConfig costRateColumn;
	private ColumnConfig costAmountColumn;
	
	public RepackagingUpdateByBQWindow(final RepackagingListGridPanel repackagingListGridPanel){
		super();
		this.setModal(true);
		this.repackagingListGridPanel = repackagingListGridPanel;
		this.globalSectionController = repackagingListGridPanel.getGlobalSectionController();
		this.globalMessageTicket = new GlobalMessageTicket();
		
		bqRepository = (BQRepositoryRemoteAsync)GWT.create(BQRepositoryRemote.class);
		((ServiceDefTarget)bqRepository).setServiceEntryPoint(GlobalParameter.BQ_REPOSITORY_URL);
		userAccessRightsRepository = (UserAccessRightsRepositoryRemoteAsync) GWT.create(UserAccessRightsRepositoryRemote.class);
		((ServiceDefTarget)userAccessRightsRepository).setServiceEntryPoint(GlobalParameter.USER_ACCESS_RIGHTS_REPOSITORY_URL);
		
		this.setTitle("Repackaging - Update BQ Items");
		this.setPaddings(5);
		this.setWidth(1100);
		this.setHeight(650);
		this.setClosable(false);
		this.setLayout(new FitLayout());
		
		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setId(MAIN_PANEL_ID);
		
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
		bqDescriptionTextField = new TextField("BQ Description", "bqDescription", 200);
		bqDescriptionTextField.setCtCls("table-cell");
		bqDescriptionTextField.addListener(searchListener);
		searchPanel.add(bqDescriptionTextField);
		
		Button searchButton = new Button("Search");
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				search();
			};
			
		});
		searchButton.setCls("table-cell");
		searchPanel.add(searchButton);
		
		//Grid panel
		resultEditorGridPanel = new EditorGridPanel();
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
		
		bpiColumn = new ColumnConfig("Bill Item", "bpi", 120, false);
		bqTypeColumn = new ColumnConfig("Type", "bqType", 35, false);
		descriptionColumn = new ColumnConfig("Description", "description", 220, false);
		unitColumn = new ColumnConfig("Unit", "unit", 35, false);
		quantityColumn = new ColumnConfig("Qty", "quantity", 100, false);
		quantityColumn.setRenderer(quantityRenderer);		
		quantityColumn.setAlign(TextAlign.RIGHT);
		remeasuredQuantityColumn = new ColumnConfig("Remeasured Qty", "remeasuredQuantity", 100, false);		
		remeasuredQuantityColumn.setRenderer(new Renderer(){
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				String str = quantityRenderer.render(value, cellMetadata, record, rowIndex, colNum, store);
				return "<span style=\"color:blue;\">" + str + "</span>";
			}
		});
		remeasuredQuantityColumn.setAlign(TextAlign.RIGHT);
		remeasuredQuantityColumn.setEditor(new GridEditor(quantNumberField));
		sellingRateColumn = new ColumnConfig("Selling Rate", "sellingRate", 100, false);
		sellingRateColumn.setRenderer(rateRenderer);
		sellingRateColumn.setAlign(TextAlign.RIGHT);
		sellingAmountColumn = new ColumnConfig("Selling Amount", "sellingAmount", 120, false);
		sellingAmountColumn.setRenderer(amountRenderer);
		sellingAmountColumn.setAlign(TextAlign.RIGHT);
		costRateColumn = new ColumnConfig("Cost Rate", "costRate", 100, false);
		costRateColumn.setRenderer(rateRenderer);
		costRateColumn.setAlign(TextAlign.RIGHT);
		costAmountColumn = new ColumnConfig("Cost Amount", "costAmount", 120, false);
		costAmountColumn.setRenderer(amountRenderer);
		costAmountColumn.setAlign(TextAlign.RIGHT);
		
		ColumnConfig[] columns = new ColumnConfig[]{
			bpiColumn,
			bqTypeColumn,
			descriptionColumn,
			unitColumn,
			quantityColumn,
			remeasuredQuantityColumn,
			sellingRateColumn,
			sellingAmountColumn,
			costRateColumn,
			costAmountColumn
		};
		
		resultEditorGridPanel.setColumnModel(new ColumnModel(columns));
		
		resultEditorGridPanel.addEditorGridListener(new EditorGridListener(){
			public boolean doBeforeEdit(GridPanel grid, Record record, String field, Object value, int rowIndex, int colIndex) {
				if(record.getAsDouble("quantity") == 0.0)
					return false;
				/**
				 * Removed By PeterChan
				 * Aug 09, 2011
				 * @author tikywong
				 * April 19, 2011
				 */
//				if(globalSectionController.getJob().getRepackagingType().equals("3") &&
//						(record.isEmpty("postedIVAmount") || record.getAsDouble("postedIVAmount") != 0.00 ||
//						 record.isEmpty("cumulativeIVAmount") || record.getAsDouble("cumulativeIVAmount") != 0.00)){
//						MessageBox.alert("Select BQItem's Remeasured Quantity cannot be edited - Posted IV Amount or Cumulative IV Amount is not zero.");
//						return false;
//				}
				return true;
			}

			public boolean doValidateEdit(GridPanel grid, Record record,
					String field, Object value, Object originalValue,
					int rowIndex, int colIndex) {
				double remeasuredQuantity = Double.parseDouble(value.toString());
				if(remeasuredQuantity < 0){
					MessageBox.alert("Remeasured Quantity cannot be negative");
					return false;
				}
				/**
				 * New remeasured quantity cannot smaller than original quantity in method 3
				 * @author peterchan
				 * Aug 09, 2011
				 */
				if(globalSectionController.getJob().getRepackagingType().equals("3") ){
					double originalQty = Double.parseDouble(originalValue.toString());
					if (originalQty>remeasuredQuantity &&
							(record.isEmpty("postedIVAmount") || record.getAsDouble("postedIVAmount") != 0.00 ||
							 record.isEmpty("cumulativeIVAmount") || record.getAsDouble("cumulativeIVAmount") != 0.00)){
						MessageBox.alert("New quantity cannot smaller than original quantity if IV Amount is not zero");
						return false;
					}
				}
				return true;
			}

			public void onAfterEdit(GridPanel grid, Record record,
					String field, Object newValue, Object oldValue,
					int rowIndex, int colIndex) {
				double remeasuredQuantity = Double.parseDouble(newValue.toString());
				double sellingAmount = record.getAsDouble("sellingRate") * remeasuredQuantity;
				record.set("sellingAmount", Double.toString(sellingAmount));
				double costAmount = record.getAsDouble("costRate") * remeasuredQuantity;
				record.set("costAmount", Double.toString(costAmount));
			}
		});
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(bqItemRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		resultEditorGridPanel.setStore(dataStore);
		
		GridView view = new CustomizedGridView();
		view.setAutoFill(true);
		view.setForceFit(false);
		resultEditorGridPanel.setView(view);
		
		rowSelectionModel = new RowSelectionModel();
		resultEditorGridPanel.setSelectionModel(rowSelectionModel);
		
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
		resultEditorGridPanel.setBottomToolbar(paginationToolbar);
		
		addChangeOrderButton = new ToolbarButton("Add Change Order Item");
		addChangeOrderButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				openAddChangeOrderWindow(); 
			};
		});
		addChangeOrderButton.setCls("toolbar-button");
		
		editChangeOrderButton = new ToolbarButton("Edit Change Order Item");
		editChangeOrderButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				openAddChangeOrderWindowWithBqItem(); 
			};
		});
		editChangeOrderButton.setCls("toolbar-button");
		
		balanceResourcesButton = new ToolbarButton("Balance Resources In Bq Item");
		balanceResourcesButton.setTooltip("Balance Resources In Bq Item", "Balance the resources in the selected bq item after updating the remeasured quantity");
		balanceResourcesButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				openBalanceResourcesWindow(); 
			};
		});
		balanceResourcesButton.setCls("toolbar-button");
		
		resultEditorGridPanel.setTopToolbar(new ToolbarButton[]{addChangeOrderButton, editChangeOrderButton, balanceResourcesButton});
		addChangeOrderButton.hide();
		editChangeOrderButton.hide();
		balanceResourcesButton.hide();
		
		mainPanel.add(searchPanel);
		mainPanel.add(resultEditorGridPanel);
		this.add(mainPanel);
		
		saveButton = new Button("Save");
		saveButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				resultEditorGridPanel.stopEditing();
				saveUpdates();
			};
		});
		
		Button closeButton = new Button("Close");
		closeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				if(dataStore.getModifiedRecords().length == 0)
					repackagingListGridPanel.closeCurrentWindow();
				else{
					MessageBox.confirm("Confirm", "Are you sure you want to discard the changes?",   
	                        new MessageBox.ConfirmCallback() {   
	                            public void execute(String btnID) {
	                            	if (btnID.equals("yes"))
	                            		repackagingListGridPanel.closeCurrentWindow();
	                            }   
	                        });
				}
			};
		});
		
		this.setButtons(new Button[]{saveButton, closeButton});
		saveButton.hide();
		// Check for access rights - then add toolbar buttons accordingly
		try{
			UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010114_REPACKAGING_UPDATE_BY_BQ_WINDOW, new AsyncCallback<List<String>>(){
				public void onSuccess(List<String> accessRightsReturned) {
					try{
						accessRightsList = accessRightsReturned;
						displayButtons();
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);						
					}catch(Exception e){
						UIUtil.alert(e);
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
					}
				}
				
				public void onFailure(Throwable e) {
					UIUtil.alert(e.getMessage());
				}
			});
		}
		catch(Exception e){
			UIUtil.alert(e.getMessage());
		}
	}
	
	private void displayButtons(){
		if(accessRightsList != null && accessRightsList.contains("WRITE")){
			addChangeOrderButton.show();
			editChangeOrderButton.show();
			balanceResourcesButton.show();
			saveButton.show();
		}
	}

	private void populateGrid(RepackagingPaginationWrapper<BQItem> wrapper){
		this.doLayout();
		dataStore.rejectChanges();
		dataStore.removeAll();

		paginationToolbar.setTotalPage(wrapper.getTotalPage());
		paginationToolbar.setCurrentPage(wrapper.getCurrentPage());
		for(BQItem item : wrapper.getCurrentPageContentList()){
			String bpi = "";
			bpi += item.getRefBillNo() == null ? "/" : item.getRefBillNo() + "/";
			bpi += item.getRefSubBillNo() == null ? "/" : item.getRefSubBillNo() + "/";
			bpi += item.getRefSectionNo() == null ? "/" : item.getRefSectionNo() + "/";
			bpi += item.getRefPageNo() == null ? "/" : item.getRefPageNo() + "/";
			bpi += item.getItemNo() == null ? "" : item.getItemNo();

			Record record = bqItemRecordDef.createRecord(new Object[]{
					item.getId(),
					bpi,
					item.getBqType(),
					item.getDescription(),
					item.getUnit(),
					item.getQuantity(),
					item.getRemeasuredQuantity(),
					item.getSellingRate(),
					item.getSellingRate() * item.getRemeasuredQuantity(),
					item.getCostRate(),
					item.getCostRate() * item.getRemeasuredQuantity(),
					//Reference only
					item.getIvPostedAmount(),
					item.getIvCumAmount()
			});
			dataStore.add(record);
		}
	}
	
	private void search(){
		jobNumber = globalSectionController.getJob().getJobNumber();
		billNo = billNoTextField.getValueAsString();
		billNo = billNo != null ? billNo.trim() : null;
		subBillNo = subBillNoTextField.getValueAsString();
		subBillNo = subBillNo != null ? subBillNo.trim() : null;
		pageNo = pageNoTextField.getValueAsString();
		pageNo = pageNo != null ? pageNo.trim() : null;
		itemNo = itemNoTextField.getValueAsString();
		itemNo = itemNo != null ? itemNo.trim() : null;
		bqDescription = bqDescriptionTextField.getValueAsString();
		bqDescription = bqDescription != null ? bqDescription.trim() : null;
		searchByPage(0);
	}
	
	private void searchByPage(int pageNum){
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Searching", true);
		SessionTimeoutCheck.renewSessionTimer();
		bqRepository.searchBQItemsByPage(jobNumber, billNo, subBillNo, pageNo, itemNo, bqDescription, pageNum, new AsyncCallback<RepackagingPaginationWrapper<BQItem>>(){
			public void onSuccess(RepackagingPaginationWrapper<BQItem> wrapper) {
				populateGrid(wrapper);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				UIUtil.alert(e.getMessage());
			}
		});
	}
	
	private void saveUpdates(){
		Record[] records = dataStore.getModifiedRecords();
		if(records.length == 0)
			return;
		
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Saving Updates", true);
		List<BQItem> bqItems = new ArrayList<BQItem>();
		for(Record record : records)
			bqItems.add(bqItemFromRecord(record));
		SessionTimeoutCheck.renewSessionTimer();
		bqRepository.updateBQItemQuantities(globalSectionController.getJob(), bqItems, new AsyncCallback<String>(){
			public void onSuccess(String error) {
				if(error == null){
					dataStore.commitChanges();
					repackagingListGridPanel.updateStatus("200");
					UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					MessageBox.alert("Update successful");
				}
				else{
					MessageBox.alert("Please use the 'Balance Resources' window to update this BQ Item:<br/><br/>" + error);
					search();
				}
			}
			
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				UIUtil.checkSessionTimeout(e, false,globalSectionController.getUser());
			}
		});
	}
	
	private BQItem bqItemFromRecord(Record record){
		BQItem bqItem = new BQItem();
		bqItem.setId(Long.valueOf(record.getAsString("id")));
		bqItem.setRemeasuredQuantity(record.getAsDouble("remeasuredQuantity"));
		return bqItem;
	}
	
	public void getBillItemFieldsForChangeOrder(String bqType){
		SessionTimeoutCheck.renewSessionTimer();
		bqRepository.getBillItemFieldsForChangeOrder(globalSectionController.getJob(), bqType, new AsyncCallback<BQItem>(){
			public void onSuccess(BQItem bqItem) {
				((AddBqChangeOrderWindow)childWindow).populateFields(bqItem);
			}
			
			public void onFailure(Throwable e) {
				MessageBox.alert("An error occurred while trying to create a new BQ Item.</br>" + 
						e.getMessage());
			}
		});  
	}
	
	private void openAddChangeOrderWindow(){
		if(childWindow != null){
			MessageBox.alert("Please close other change order windows before continuing");
			return;
		}
		
		childWindow = new AddBqChangeOrderWindow(this);
		childWindow.show();
	}
	
	private void openAddChangeOrderWindowWithBqItem(){
		final Record record = rowSelectionModel.getSelected();
		if(record == null){
			MessageBox.alert("Please select a change order item to edit");
			return;
		}
		
		String bqType = record.getAsString("bqType");
		if("CC".equals(bqType) || "CL".equals(bqType) || "DW".equals(bqType) ||
				"OI".equals(bqType) || "VO".equals(bqType)){
			//get bqItem from db, and open window
			SessionTimeoutCheck.renewSessionTimer();
			bqRepository.getBqItemWithResources(new Long(record.getAsString("id")), new AsyncCallback<BQItem>(){
				public void onSuccess(BQItem bqItem) {
					/**
					 * @author tikywong
					 * April 18, 2011
					 */
					if(globalSectionController.getJob().getRepackagingType().equals("3") &&
						(record.isEmpty("postedIVAmount") || record.getAsDouble("postedIVAmount") != 0.00 ||
						 record.isEmpty("cumulativeIVAmount") || record.getAsDouble("cumulativeIVAmount") != 0.00)){
						MessageBox.alert("Select BQItem cannot be edited - Posted IV Amount or Cumulative IV Amount is not zero.");
						return;
					}
					openAddChangeOrderWindowWithBqItem(bqItem);
				}
				
				public void onFailure(Throwable e) {
					UIUtil.checkSessionTimeout(e, false,globalSectionController.getUser());
				}
			});
		}
		else
			MessageBox.alert("Only change order items can be edited - type must be CC, CL, DW, OI or VO");
	}
	
	private void openAddChangeOrderWindowWithBqItem(BQItem bqItem){
		AddBqChangeOrderWindow changeOrderWindow = new AddBqChangeOrderWindow(this);
		changeOrderWindow.show(); //Have to show and render fields before populating - fields aren't disabled properly otherwise
		changeOrderWindow.populateFields(bqItem);
		childWindow = changeOrderWindow;
	}
	
	//called by addBqChangeOrderWindow
	public void validateBqItemThenAddResources(final BQItem bqItem){
		//validate the bqItem
		UIUtil.maskPanelById(childWindow.getId(), "Validating", true);
		SessionTimeoutCheck.renewSessionTimer();
		bqRepository.validateBqItemChangeOrder(bqItem, new AsyncCallback<String>(){
			public void onSuccess(String error) {
				UIUtil.unmaskPanelById(childWindow.getId());
				if(error == null)
					openAddResourcesToBqWindow(bqItem);
				else
					MessageBox.alert(error);
			}
			
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(childWindow.getId());
				MessageBox.alert("An error occurred while trying to validate the BQ item.</br>" + 
						e.getMessage());
			}
		});	
	}
	
	private void openAddResourcesToBqWindow(BQItem bqItem){
		childWindow.close();
		AddResourcesToBqCoWindow window = new AddResourcesToBqCoWindow(this);
		window.populateGrid(bqItem);
		childWindow = window;
		childWindow.show();
	}
	
	public void saveChangeOrderBqAndResources(BQItem bqItem, List<Resource> resourceList){
		UIUtil.maskPanelById(childWindow.getId(), "Saving", true);
		SessionTimeoutCheck.renewSessionTimer();
		bqRepository.saveChangeOrderBqAndResources(bqItem, resourceList, new AsyncCallback<String>(){
			public void onSuccess(String error) {
				UIUtil.unmaskPanelById(childWindow.getId());
				if(error == null){
					closeChildWindow();
					searchByPage(0);
					repackagingListGridPanel.updateStatus("200");
				}
				else
					MessageBox.alert(error);
			}
			
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(childWindow.getId());
				MessageBox.alert("An error occurred while trying to save the BQ item and resources.</br>" + 
						e.getMessage());
			}
		});
	}
	
	private void openBalanceResourcesWindow(){
		final Record record = rowSelectionModel.getSelected();
		if(record == null){
			MessageBox.alert("Please select a change order item to edit");
			return;
		}

		UIUtil.maskPanelById(MAIN_PANEL_ID, "Retrieving Resources", true);
		Long id = new Long(record.getAsString("id"));
		SessionTimeoutCheck.renewSessionTimer();
		bqRepository.getResourcesByBqItemId(id, new AsyncCallback<List<Resource>>(){
			public void onSuccess(List<Resource> resources) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				BalanceResourcesWindow balanceResourcesWindow = new BalanceResourcesWindow(RepackagingUpdateByBQWindow.this, record);
				balanceResourcesWindow.populateGrid(resources);
				childWindow = balanceResourcesWindow;
				childWindow.show();
			}
			
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				MessageBox.alert("An error occurred while trying retrieving resources for this bq item.</br>" + 
						e.getMessage());
			}
		});
		
	}
	
	public void saveBalancedResources(Record[] records, Double remeasuredQuantity){
		List<Resource> resources = new ArrayList<Resource>(records.length);
		for(Record record : records){
			Resource resource = resourceFromRecord(record);
			if(resource == null)
				return;
			resources.add(resource);
		}
		
		UIUtil.maskPanelById(BalanceResourcesWindow.MAIN_PANEL_ID, "Saving...", true);
		SessionTimeoutCheck.renewSessionTimer();
		bqRepository.saveBalancedResources(resources, remeasuredQuantity, new AsyncCallback<String>(){
			public void onSuccess(String errorMsg) {
				UIUtil.unmaskPanelById(BalanceResourcesWindow.MAIN_PANEL_ID);
				if(errorMsg == null){
					MessageBox.alert("Save successful");
					closeChildWindow();
					repackagingListGridPanel.updateStatus("200");
					search();
				}
				else
					MessageBox.alert(errorMsg);
			}
			
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(BalanceResourcesWindow.MAIN_PANEL_ID);
				UIUtil.checkSessionTimeout(e, false,globalSectionController.getUser());
			}
		});
	}
	
	private Resource resourceFromRecord(Record record){
		Resource resource = new Resource();
		if(record.getAsString("id") != null)
			resource.setId(Long.valueOf(record.getAsString("id")));
		String[] bpi = record.getAsString("bpi").split("/");
		if(bpi.length == 5){
			resource.setRefBillNo(bpi[0]);
			resource.setRefSubBillNo(bpi[1]);
			resource.setRefSectionNo(bpi[2]);
			resource.setRefPageNo(bpi[3]);
			resource.setRefItemNo(bpi[4]);
		}
		resource.setJobNumber(jobNumber);
		String packageNo = record.getAsString("packageNo");
		if(packageNo == null || packageNo.trim().length() == 0)
			packageNo = "0";
		resource.setPackageNo(packageNo);
		String objectCode = record.getAsString("objectCode");
		if(objectCode == null || objectCode.trim().length() != 6){
			MessageBox.alert("Object Code must be 6 digits in length");
			return null;
		}
		resource.setObjectCode(objectCode);
		String subsidiaryCode = record.getAsString("subsidiaryCode");
		if(subsidiaryCode == null || subsidiaryCode.trim().length() != 8){
			MessageBox.alert("Subsidiary Code must be 8 digits in length");
			return null;
		}
		resource.setSubsidiaryCode(subsidiaryCode);
		String description = record.getAsString("description");
		if(description == null || description.trim().length() == 0){
			MessageBox.alert("Description must not be blank");
			return null;
		}
		resource.setDescription(description);
		resource.setUnit(record.getAsString("unit").trim());
		Double remeasuredQuant = record.getAsDouble("quantity");
		Double remeasuredFactor = record.getAsDouble("remeasuredFactor");
		/**
		 * @author tikywong
		 * modified on 24 July, 2012
		 * For equations that involve division, handle it by setting the value to zero
		 */
		Double quantity = (remeasuredQuant==null || remeasuredQuant==0 || remeasuredFactor==null || remeasuredFactor==0)? 0.0 : (remeasuredQuant/remeasuredFactor);
		resource.setQuantity(quantity);
		resource.setRemeasuredFactor(remeasuredFactor);
		resource.setCostRate(record.getAsDouble("rate"));
		resource.setResourceType(record.getAsString("resourceType"));
		//By Peter Chan 2011-09-20
		if ("3".equals(globalSectionController.getJob().getRepackagingType().trim())){
			double ivMovementAmount=0;
			if (record.getAsString("cumulativeIVAmount")!=null && !"".equals(record.getAsString("cumulativeIVAmount"))){
				resource.setIvCumAmount(record.getAsDouble("cumulativeIVAmount"));
				ivMovementAmount = record.getAsDouble("cumulativeIVAmount");
				if (resource.getCostRate().doubleValue()!=0.0)
					resource.setIvCumQuantity(record.getAsDouble("cumulativeIVAmount")/resource.getCostRate());
				else
					resource.setIvCumQuantity(0.0);
			}else resource.setIvCumQuantity(0.0);
			if (record.getAsString("postedIVAmount")!=null && !"".equals(record.getAsString("postedIVAmount"))){
				resource.setIvPostedAmount(record.getAsDouble("postedIVAmount"));
				ivMovementAmount = ivMovementAmount-record.getAsDouble("postedIVAmount");
				if (resource.getCostRate().doubleValue()!=0.0)
					resource.setIvPostedQuantity(record.getAsDouble("postedIVAmount")/resource.getCostRate());
				else
					resource.setIvPostedQuantity(0.0);
			}else resource.setIvPostedQuantity(0.0);
			resource.setIvMovementAmount(ivMovementAmount);

		}
		if(record.getAsString("resourceNo")!=null && !"".equals(record.getAsString("resourceNo").trim()))
			resource.setResourceNo(record.getAsInteger("resourceNo"));
		else resource.setResourceNo(null);
		return resource;
	}
	
	public void closeChildWindow(){
		childWindow.close();
		childWindow = null;
	}
	
	public GlobalSectionController getGlobalSectionController(){
		return globalSectionController;
	}
}