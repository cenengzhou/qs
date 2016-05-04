/**
 * koeyyeung
 * Jul 2, 20134:08:46 PM
 * Change from Window to Panel
 */
/**
 * Window for updating IV by BQ Item using method 3(Repackaging and IV posting via BQ Items and Resources)
 * 
 * @author tikywong
 * @since 1.5
 */
package com.gammon.qs.client.ui.panel.mainSection;

import java.util.ArrayList;
import java.util.List;

import com.gammon.qs.application.User;
import com.gammon.qs.client.controller.DetailSectionController;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.repository.BQRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.ArrowKeyNavigation;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.renderer.AmountRendererCheckIfTotal;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.RoundingUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.BQItem;
import com.gammon.qs.domain.Job;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVInputByBQItemPaginationWrapper;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.Connection;
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
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBoxConfig;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.MessageBox.PromptCallback;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Form;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.form.event.FormListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.EditorGridListener;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;


public class UpdateCumIVByBQItemMainPanel extends Panel{

	private static final String MAIN_PANEL_ID = "UpdateCumIVByBQItemMainPanel_ID";	
	private static final boolean SORTABLE = true;
	
	//UI
	private EditorGridPanel resultEditorGridPanel;

	//Button with security
	private ToolbarButton postIVButton;
	private ToolbarButton downloadExcelButton;
	private ToolbarButton uploadExcelButton;
	private ToolbarButton updateButton;
	
	private Window uploadExcelWindow;
	
	private GlobalMessageTicket globalMessageTicket;
	
	//UI-search panel
	private TextField billNoTextField;
	private TextField subBillNoTextField;
	private TextField pageNoTextField;
	private TextField itemNoTextField;
	private TextField bqDescriptionTextField;
	
	private NumberField applyPercentageNumberField;
	
	
	//UI-result panel-Datastore, Records
	private Store dataStore;
	private final RecordDef bqItemRecordDef = new RecordDef(new FieldDef[]{	new StringFieldDef("sequenceNoRecord"),
																			new StringFieldDef("billItemRecord"),
																			new StringFieldDef("bqDescriptionRecord"),
																			new StringFieldDef("remeasuredQuantityRecord"),
																			new StringFieldDef("unitRecord"),
																			new StringFieldDef("costRateRecord"),
																			new StringFieldDef("costAmountRecord"),
																			new StringFieldDef("postedIVAmountRecord"),
																			new StringFieldDef("cumIVQuantityRecord"),
																			new StringFieldDef("cumIVAmountRecord"),
																			new StringFieldDef("movementIVAmountRecord"),
																			new StringFieldDef("bqTypeRecord"),
																			//Non-displaying records
																			new StringFieldDef("id"),
																			new StringFieldDef("dbRemeasuredQuantityRecord"),
																			new StringFieldDef("dbCostRateRecord"),
																			new StringFieldDef("dbCostAmountRecord")});
	
	//UI-result editor grid panel
	private ArrowKeyNavigation arrowKeyNavigation;
	private PaginationToolbar paginationToolbar;
	private ToolbarTextItem totalNumOfRecordsTextItem;
	private ToolbarTextItem totalPostedIVAmountItem;
	private ToolbarTextItem totalCumIVAmountItem;
	private ToolbarTextItem totalMovementIVAmountItem;
	
//	private RowSelectionModel rowSelectionModel;
	
	//Global storage
	private GlobalSectionController globalSectionController;
	private DetailSectionController detailSectionController; 
	private String repackagingStatus;
	
	private BQRepositoryRemoteAsync bqRepository;
	
	private User user;
	private Job job;
	
	//Global Searching Parameters
	String billNo;
	String subBillNo;
	String pageNo;
	String itemNo;
	String bqDescription;
	
	//Security Access Rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;

	
	/**
	 * 
	 * @param globalSectionController For accessing information
	 * @param repackagingStatus Repackaging Status - 100(Unlocked), 200(Updated), 300(Snapshot), 900(Locked)
	 */
	
	public UpdateCumIVByBQItemMainPanel(GlobalSectionController globalSectionController, String repackagingStatus) {
		super();
		this.globalSectionController = globalSectionController;
		detailSectionController = this.globalSectionController.getDetailSectionController();
		this.repackagingStatus = repackagingStatus;
		job = this.globalSectionController.getJob();
		user = this.globalSectionController.getUser();
		globalMessageTicket = new GlobalMessageTicket();
				
		//REPOSITORIES setup
		userAccessRightsRepository = this.globalSectionController.getUserAccessRightsRepository();
		bqRepository = this.globalSectionController.getBqRepository();
		
		setupUI();
	}

	private void setupUI() {
		setLayout(new RowLayout());
		setHeader(true);
		setTitle("IV By BQ Item");
		
		setupSearchPanel();
		setupGridPanel();
		
		setId(MAIN_PANEL_ID);
		
		//hide detail panel
		detailSectionController.getMainPanel().collapse();
	}

	private void setupSearchPanel() {
		//SEARCH PANEL setup
		Panel searchPanel = new Panel();
		searchPanel.setPaddings(0);
		searchPanel.setFrame(true);
		searchPanel.setHeight(40);
		searchPanel.setLayout(new TableLayout(11));
		searchPanel.setMargins(2);
		
		FieldListener searchListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER){
					globalMessageTicket.refresh();
					search();
				}
			}
		};
		
		Label billNoLabel = new Label("Bill No.");
		
		billNoTextField = new TextField("Bill No.", "billNo", 100);
		billNoTextField.addListener(searchListener);
		
		searchPanel.add(billNoLabel);
		
		Label subBillNoLabel = new Label("Sub-Bill No.");
		
		subBillNoTextField = new TextField("Sub-Bill No.", "subBillNo", 100);
		subBillNoTextField.addListener(searchListener);
		
		searchPanel.add(subBillNoLabel);
		searchPanel.add(subBillNoTextField);
		
		Label pageNoLabel = new Label("Page No.");
		
		pageNoTextField = new TextField("Page No.", "pageNo", 100);
		pageNoTextField.addListener(searchListener);
		
		searchPanel.add(pageNoLabel);
		searchPanel.add(pageNoTextField);
		
		Label itemNoLabel = new Label("Item No.");
		
		itemNoTextField = new TextField("Item No.", "itemNo", 100);
		itemNoTextField.addListener(searchListener);
		
		searchPanel.add(itemNoLabel);
		searchPanel.add(itemNoTextField);
		
		Label bqDescriptionLabel = new Label("BQ Description");
		
		bqDescriptionTextField = new TextField("BQ Description", "bqDescription", 100);
		bqDescriptionTextField.addListener(searchListener);
		
		searchPanel.add(bqDescriptionLabel);
		searchPanel.add(bqDescriptionTextField);
		
		Button searchButton = new Button("Search");
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				search();
			};
		});
		searchPanel.add(searchButton, new ColumnLayoutData(20));
		
		
		
		add(searchPanel);
	}

	
	private void setupToolbar(){
		Toolbar toolbar = new Toolbar();
		
		//Toolbar at the top of the window
		//1. Post IV Button
		postIVButton = new ToolbarButton("Post IV Movements");
		postIVButton.setCls("toolbar-button");
		postIVButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				if(dataStore.getModifiedRecords().length > 0){
					MessageBox.alert("Please save any changes before posting");
					return;
				}
				else
					postIVMovements();
			}
		});
		
		//2. Download Excel Button
		downloadExcelButton = new ToolbarButton("Export BQ Items to Excel");
		downloadExcelButton.setCls("toolbar-button");
		downloadExcelButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				exportExcel();
			}
		});
		
		//3. Upload Excel Button
		uploadExcelButton = new ToolbarButton("Import from Excel");
		uploadExcelButton.setCls("toolbar-button");
		uploadExcelButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				showUploadWindow();
			}	
		});
		
		
		updateButton = new ToolbarButton("Update");
		updateButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, EventObject e){
					globalMessageTicket.refresh();
					update();				
				};
		});
		
		
		//Buttons' visibility by user access rights
		/**
		 * Default visibility of buttons - Invisible
		 * Visibility will be set according to the access rights of the user
		 */
		postIVButton.setVisible(false);
		downloadExcelButton.setVisible(false);
		uploadExcelButton.setVisible(false);
		updateButton.setVisible(false);
		
		
		toolbar.addButton(postIVButton);
		toolbar.addSeparator();
		toolbar.addButton(downloadExcelButton);
		toolbar.addSeparator();
		toolbar.addButton(uploadExcelButton);
		toolbar.addSeparator();
		toolbar.addButton(updateButton);
		
		
		ToolbarTextItem applyPercentageTextItem = new ToolbarTextItem("Apply Percentage for IV");
		
		applyPercentageNumberField = new NumberField("ApplyPercentage", "applyPercentage", 50);
		applyPercentageNumberField.setCtCls("table-cell");
		
		ToolbarButton applyPercentageButton = new ToolbarButton("Apply %");
		applyPercentageButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				applyPercentage();
			};
		});
		applyPercentageButton.setCls("table-cell");
		
		toolbar.addFill();
		toolbar.addItem(applyPercentageTextItem);
		toolbar.addField(applyPercentageNumberField);
		toolbar.addSeparator();
		toolbar.addButton(applyPercentageButton);
		toolbar.addSeparator();
		
		setTopToolbar(toolbar);
	}
	
	private void setupGridPanel() {
		
		//RESULT EDITOR GRID PANEL setup
		resultEditorGridPanel = new EditorGridPanel();
		resultEditorGridPanel.setView(new GridView());
		arrowKeyNavigation = new ArrowKeyNavigation(resultEditorGridPanel);
//		rowSelectionModel = new RowSelectionModel();
		
		setupToolbar();
		
		//Renderers
		final Renderer quantityRenderer = new QuantityRenderer(user);
		final Renderer rateRenderer = new RateRenderer(user);
		final Renderer amountRenderer = new AmountRendererCheckIfTotal(user);
		
		Renderer editableQuantityRenderer = new Renderer(){
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				String str = quantityRenderer.render(value, cellMetadata, record, rowIndex, colNum, store);
				return "<span style=\"color:blue;\">" + str + "</span>";
			}
		};
		
		Renderer editableAmountRenderer = new Renderer(){
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				String str = amountRenderer.render(value, cellMetadata, record, rowIndex, colNum, store);
				return "<span style=\"color:blue;\">" + str + "</span>";
			}
		};
		
		//Columns
		ColumnConfig sequenceNoColConfig		= new ColumnConfig("Seq. No.", "sequenceNoRecord", 50, SORTABLE);
		ColumnConfig billItemColConfig 			= new ColumnConfig("Bill Item", "billItemRecord", 50, SORTABLE);
		ColumnConfig bqDescriptionColConfig 	= new ColumnConfig("BQ Description", "bqDescriptionRecord", 200, SORTABLE);
		ColumnConfig remeasuredQuantityColConfig= new ColumnConfig("Remeasured Quantity", "remeasuredQuantityRecord", 100, !SORTABLE, quantityRenderer);
		ColumnConfig unitColConfig				= new ColumnConfig("Unit", "unitRecord", 50, SORTABLE);
		ColumnConfig costRateColConfig			= new ColumnConfig("Cost Rate", "costRateRecord", 80, !SORTABLE, rateRenderer);
		ColumnConfig costAmountColConfig		= new ColumnConfig("Cost Amount", "costAmountRecord", 100, !SORTABLE, amountRenderer);
		ColumnConfig postedIVAmountColConfig	= new ColumnConfig("Posted IV Amount", "postedIVAmountRecord", 100, !SORTABLE, amountRenderer);
		ColumnConfig bqTypeColConfig			= new ColumnConfig("BQ Type", "bqTypeRecord", 50, SORTABLE);
		
		//Editable Columns
		ColumnConfig cumIVQuantityColConfig		= new ColumnConfig("Cum IV Quantity", "cumIVQuantityRecord", 100, !SORTABLE, editableQuantityRenderer);
		ColumnConfig cumIVAmountColConfig		= new ColumnConfig("Cum IV Amount", "cumIVAmountRecord", 100, !SORTABLE, editableAmountRenderer);
		ColumnConfig movementIVAmountColConfig	= new ColumnConfig("IV Movement Amount", "movementIVAmountRecord", 120, !SORTABLE, editableAmountRenderer);
		
		//Columns/Editable Columns alignment setup
		remeasuredQuantityColConfig.setAlign(TextAlign.RIGHT);
		costRateColConfig.setAlign(TextAlign.RIGHT);
		costAmountColConfig.setAlign(TextAlign.RIGHT);
		postedIVAmountColConfig.setAlign(TextAlign.RIGHT);
		
		cumIVQuantityColConfig.setAlign(TextAlign.RIGHT);
		cumIVAmountColConfig.setAlign(TextAlign.RIGHT);
		movementIVAmountColConfig.setAlign(TextAlign.RIGHT);
		
		//Editable Columns editor and arrow key navigation setup
		Field cumIVQuantityField = FieldFactory.createNegativeNumberField(2);
		cumIVQuantityField.addListener(arrowKeyNavigation);
		cumIVQuantityColConfig.setEditor(new GridEditor(cumIVQuantityField));
		
		Field cumIVAmountField = FieldFactory.createNegativeNumberField(2);
		cumIVAmountField.addListener(arrowKeyNavigation);
		cumIVAmountColConfig.setEditor(new GridEditor(cumIVAmountField));
		
		Field movementIVAmountField = FieldFactory.createNegativeNumberField(2);
		movementIVAmountField.addListener(arrowKeyNavigation);
		movementIVAmountColConfig.setEditor(new GridEditor(movementIVAmountField));
		
		//Datastore setup
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(bqItemRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		dataStore.setSortInfo(new SortState("billItem", SortDir.ASC));
		resultEditorGridPanel.setStore(dataStore);
				
		BaseColumnConfig[] columns = new BaseColumnConfig[]{sequenceNoColConfig,
															billItemColConfig,
															bqDescriptionColConfig,
															remeasuredQuantityColConfig,
															unitColConfig,
															costRateColConfig,
															costAmountColConfig,
															postedIVAmountColConfig,
															cumIVQuantityColConfig,
															cumIVAmountColConfig,
															movementIVAmountColConfig,
															bqTypeColConfig};
		resultEditorGridPanel.setColumnModel(new ColumnModel(columns));
		
		resultEditorGridPanel.addGridCellListener(new GridCellListenerAdapter(){
			/**
			 * @author peterchan
			 * May 6, 2011 3:05:08 PM
			 * @see com.gwtext.client.widgets.grid.event.GridCellListenerAdapter#onCellDblClick(com.gwtext.client.widgets.grid.GridPanel, int, int, com.gwtext.client.core.EventObject)
			 */
			public void onCellDblClick(GridPanel grid, int rowIndex, int colIndex, EventObject e) {
				String selectedCol = grid.getStore().getFields()[Integer.parseInt(grid.getColumnModel().getColumnId(colIndex))];
				if ("cumIVQuantityRecord".equals(selectedCol)||"movementIVAmountRecord".equals(selectedCol) ||"cumIVAmountRecord".equals(selectedCol))
					return;
				String bpi =grid.getStore().getAt(rowIndex).getAsString("billItemRecord");
				showUpdateIVByResourceMainPanel(bpi);
			}
		});
		
		resultEditorGridPanel.addEditorGridListener(new EditorGridListener(){
			public boolean doBeforeEdit(GridPanel grid, Record record, String field, Object value, int rowIndex, int colIndex) {
				globalMessageTicket.refresh();
				arrowKeyNavigation.startedEdit(colIndex, rowIndex);
				return true;
			}
			
			public boolean doValidateEdit(GridPanel grid, Record record, String field, Object value, Object originalValue, int rowIndex, int colIndex) {
				if(value!=null){
					//For Bill 80, "Resource Rate" is always 0, assume Resource Rate = 1 and set Cumulative IV Amount = Cumulative IV Quantity
					String bpi = record.getAsString("billItemRecord");
					String[] bpiSplit = bpi.split("/");
					boolean isBill80 = bpiSplit[0]!=null && !bpiSplit[0].trim().equals("") && bpiSplit[0].trim().equals("80");
					double dbRemeasuredQuantity = record.isEmpty("dbRemeasuredQuantityRecord")?0.00:record.getAsDouble("dbRemeasuredQuantityRecord");
					double dbCostAmount = isBill80?dbRemeasuredQuantity :(record.isEmpty("dbCostAmountRecord")?0.00:record.getAsDouble("dbCostAmountRecord"));
					
					//Validation 1: Cumulative IV Quantity
					if(field.equals("cumIVQuantityRecord")){					
						double newCumIVQuantity = value.toString().equals(GlobalParameter.NULL_REPLACEMENT)?0.00:Double.parseDouble(value.toString());
						
						//1a: cumIVQuantity, remeasuredQuantity need to have same sign
						if(!sameDoubleSign(RoundingUtil.round(newCumIVQuantity,2), RoundingUtil.round(dbRemeasuredQuantity,2))){
							MessageBox.alert("'Cumulative IV Quantity' and 'Remeasured Quantity' must have the same sign(+/-)");
							return false;
						}
						
						//1b. cumIVQuantity cannot exceed remeasuredQuantity
						if(checkifAmountexceededCostAmount(RoundingUtil.round(newCumIVQuantity,2), RoundingUtil.round(dbRemeasuredQuantity,2))){
							MessageBox.alert("Cumulative IV Quantity: "+newCumIVQuantity+" cannot exceed 'Remeasured Quantity'");
							return false;
						}
					}
					//Validation 2: Movement IV Amount
					else if(field.equals("movementIVAmountRecord")){
						double newMovementIVAmount = value.toString().equals(GlobalParameter.NULL_REPLACEMENT)?0.00:Double.parseDouble(value.toString());
						double postedIVAmount = record.isEmpty("postedIVAmountRecord")?0.00:record.getAsDouble("postedIVAmountRecord");
						double newCumulativeIVAmount = postedIVAmount+newMovementIVAmount;
						
						//2a: cumIVAmount, costAmount need to have same sign
						if(!sameDoubleSign(RoundingUtil.round(newCumulativeIVAmount,2), RoundingUtil.round(dbCostAmount,2))){
							MessageBox.alert("'Cumulative IV Quantity' and 'Cost Amount' must have the same sign(+/-). Please adjust the IV Movement Amount.");
							return false;
						}
						
						//2b: movementIVAmount+postedIVAmount cannot exceed costAmount
						if(checkifAmountexceededCostAmount(RoundingUtil.round(newCumulativeIVAmount,2),RoundingUtil.round(dbCostAmount,2))){
							MessageBox.alert("Posted IV Amount'+'IV Movement: "+newCumulativeIVAmount+" Amount cannot exceed 'Cost Amount'");
							return false;
						}
					}
					//Validation 3: Cumulative IV Amount
					else if(field.equals("cumIVAmountRecord")){
						double newCumulativeIVAmount = value.toString().equals(GlobalParameter.NULL_REPLACEMENT)?0.00:Double.parseDouble(value.toString());
						
						//3a: cumIVAmount, costAmount need to have same sign
						if(!sameDoubleSign(RoundingUtil.round(newCumulativeIVAmount,2), RoundingUtil.round(dbCostAmount,2))){
							MessageBox.alert("'Cumulative IV Amount' and 'Cost Amount' must have the same sign(+/-)");
							return false;
						}
						
						//3b: cumulativeIVAmount can not be greater than costAmount
						if(checkifAmountexceededCostAmount(RoundingUtil.round(newCumulativeIVAmount,2),RoundingUtil.round(dbCostAmount,2))){
							MessageBox.alert("Cumulative IV Amount: "+newCumulativeIVAmount+" cannot exceed 'Cost Amount'");
							return false;
						}
					}
				}
				return true;
			}
			
			public void onAfterEdit(GridPanel grid, Record record, String field, Object newValue, Object oldValue, int rowIndex, int colIndex) {
				double dbCostRate = record.getAsDouble("dbCostRateRecord");
				
				//For Bill 80, "Resource Rate" is always 0, assume Resource Rate = 1 and set Cumulative IV Amount = Cumulative IV Quantity
				String bpi = record.getAsString("billItemRecord");
				String[] bpiSplit = bpi.split("/");
				boolean isBill80 = bpiSplit[0]!=null && !bpiSplit[0].trim().equals("") && bpiSplit[0].trim().equals("80");
				
				if(field.equals("cumIVQuantityRecord")){
					double newCumulativeIVQuantity = newValue.toString().equals(GlobalParameter.NULL_REPLACEMENT)?0.00:Double.parseDouble(newValue.toString());
					double postedIVAmount = record.isEmpty("postedIVAmountRecord")?0.00:record.getAsDouble("postedIVAmountRecord");
					
					//Calculations
					double newCumulativeIVAmount = isBill80? newCumulativeIVQuantity : (newCumulativeIVQuantity * dbCostRate);
					double newMovementIVAmount = newCumulativeIVAmount - postedIVAmount;
					record.set("cumIVAmountRecord", Double.toString(newCumulativeIVAmount));
					record.set("movementIVAmountRecord", Double.toString(newMovementIVAmount));
				}
				else if(field.equals("movementIVAmountRecord")){
					double newMovementIVAmount = 	newValue.toString().equals(GlobalParameter.NULL_REPLACEMENT)?
													0.00:Double.parseDouble(newValue.toString());
					double postedIVAmount = record.isEmpty("postedIVAmountRecord")?
											0.00:record.getAsDouble("postedIVAmountRecord");
					
					//Calculations
					double newCumulativeIVAmount = postedIVAmount + newMovementIVAmount;
					double newCumulativeIVQuantity = isBill80 ? newCumulativeIVAmount :(dbCostRate==0 ? 0.00:newCumulativeIVAmount / dbCostRate);
					record.set("cumIVAmountRecord", Double.toString(newCumulativeIVAmount));
					record.set("cumIVQuantityRecord", Double.toString(newCumulativeIVQuantity));
				}
				else if(field.equals("cumIVAmountRecord")){
					double newCumulativeIVAmount = newValue.toString().equals(GlobalParameter.NULL_REPLACEMENT)?
													0.00:Double.parseDouble(newValue.toString());
					double postedIVAmount = record.isEmpty("postedIVAmountRecord")?
											0.00:record.getAsDouble("postedIVAmountRecord");
					
					double newMovementIVAmount = newCumulativeIVAmount-postedIVAmount;
					double newCumulativeQuantity = isBill80 ? newCumulativeIVAmount : (dbCostRate==0 ? 0.00 : newCumulativeIVAmount / dbCostRate);
					record.set("movementIVAmountRecord", Double.toString(newMovementIVAmount));
					record.set("cumIVQuantityRecord", Double.toString(newCumulativeQuantity));
				}
			}
		});
		
		//pagination toolbar setup
		paginationToolbar = new PaginationToolbar();
		totalNumOfRecordsTextItem = new ToolbarTextItem("<b>0 Records match search criteria</b>");
		totalPostedIVAmountItem = new ToolbarTextItem("<b>Total Posted IV Amount: 0</b>");
		totalCumIVAmountItem = new ToolbarTextItem("<b>Total Cumulative IV Amount: 0</b>");
		totalMovementIVAmountItem = new ToolbarTextItem("<b>Total IV Movement Amount: 0</b>");
		
		paginationToolbar.addFill();
		paginationToolbar.addItem(totalNumOfRecordsTextItem);
		
		paginationToolbar.addSpacer();
		paginationToolbar.addSeparator();
		paginationToolbar.addSpacer();
		paginationToolbar.addItem(totalPostedIVAmountItem);
		
		paginationToolbar.addSpacer();
		paginationToolbar.addSeparator();
		paginationToolbar.addSpacer();
		paginationToolbar.addItem(totalCumIVAmountItem);
		
		paginationToolbar.addSpacer();
		paginationToolbar.addSeparator();
		paginationToolbar.addSpacer();
		paginationToolbar.addItem(totalMovementIVAmountItem);
		
		paginationToolbar.setTotalPage(1);
		paginationToolbar.setCurrentPage(0);
		
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(final int pageNum){
				globalMessageTicket.refresh();
				if(dataStore.getModifiedRecords().length>0){
					MessageBoxConfig messageBoxCofig = new MessageBoxConfig();
					
					messageBoxCofig.setTitle("Continue?");
					messageBoxCofig.setMsg("If you continue to next page, the changes you made on this page will be lost.  Continue to next page?");   
					messageBoxCofig.setButtons(MessageBox.YESNO);
					messageBoxCofig.setCallback(new PromptCallback() {
						public void execute(String btnID, String text) {
							if (btnID.toLowerCase().equals("yes")) 
								getPage(pageNum);	
						}
					});
					
					MessageBox.show(messageBoxCofig);
				}
				else
					getPage(pageNum);
			}
		});
		
		resultEditorGridPanel.setBottomToolbar(paginationToolbar);
		
		add(resultEditorGridPanel);
		
		
		SessionTimeoutCheck.renewSessionTimer();
		userAccessRightsRepository.getAccessRights(user.getUsername(), RoleSecurityFunctions.F010117_UPDATE_IV_BY_BQ_ITEM_MAINPANEL, new AsyncCallback<List<String>>(){
			public void onSuccess(List<String> accessRightsReturned) {
				try{
					accessRightsList = accessRightsReturned;
					securitySetup();
				}catch(Exception e){
					UIUtil.alert(e);
				}
			}

			public void onFailure(Throwable e) {
				UIUtil.alert(e.getMessage());
			}
		});
	
	}

	/**
	 * Security Setup for the following buttons:
	 * 1. Post IV Movements
	 * 2. Export All BQ Items to Excel
	 * 3. Import from Excel
	 * 4. Save
	 * @author tikywong
	 * Mar 31, 2011 5:08:13 PM
	 */
	private void securitySetup(){
		if(accessRightsList != null && accessRightsList.contains("WRITE")){
			downloadExcelButton.setVisible(true);
			if("900".equals(repackagingStatus)){
				postIVButton.setVisible(true);
				updateButton.setVisible(true);
				uploadExcelButton.setVisible(true);
			}
		}
		
		if(accessRightsList != null && accessRightsList.contains("READ"))
			downloadExcelButton.setVisible(true);
	}
	
	/**
	 * @author tikywong
	 * Mar 31, 2011 5:05:10 PM
	 */
	private void update(){
		List<BQItem> bqItems = new ArrayList<BQItem>();
		
		//Get the modified data list and create the bqItems with the cumIVQuantityRecord, cumIVAmountRecord
		Record[] records = dataStore.getModifiedRecords();
		for(Record record: records)
			bqItems.add(bqItemFromRecord(record));
		
		//No update if no data has been modified
		if(bqItems.size()==0){
			MessageBox.alert("No records changed");
			return;
		}
		
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.SAVING_MSG, true);
		try{
			SessionTimeoutCheck.renewSessionTimer();
			bqRepository.updateBQItemsIVAmount(bqItems, user.getUsername(), new AsyncCallback<Boolean>(){
				public void onSuccess(Boolean success) {
					UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					dataStore.commitChanges();
					MessageBox.alert("Changes saved successfully.");
				}
				public void onFailure(Throwable e) {
					UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
				}				
			});
		}catch(Exception e){
			UIUtil.alert(e);			
		}
	}
	
	/**
	 * @author tikywong
	 * Apr 8, 2011 10:23:04 AM
	 * 
	 * Create a BQItem by inserting BQItem's id, ivCumAmount, ivCumQty
	 */
	private BQItem bqItemFromRecord(Record record){
		BQItem bqItem = new BQItem();
		String id = record.getAsString("id");
		
		if(id!=null && !id.equals(""))
			bqItem.setId(new Long(id));
		
		bqItem.setIvCumAmount(Double.valueOf(record.getAsDouble("cumIVAmountRecord")));
		bqItem.setIvCumQty(Double.valueOf(record.getAsDouble("cumIVQuantityRecord")));
		
		return bqItem;
	}
	
//	/**
//	 * @author tikywong
//	 * Mar 31, 2011 5:00:14 PM
//	 */
	private void showUploadWindow(){
		uploadExcelWindow = new Window();
		uploadExcelWindow.setLayout(new FitLayout());
		uploadExcelWindow.setWidth(250);
		uploadExcelWindow.setTitle("Import IV amounts from Excel");

		final FormPanel uploadPanel = new FormPanel();
		uploadPanel.setHeight(25);
		uploadPanel.setPaddings(0, 2, 2, 0);
		uploadPanel.setFileUpload(true);

		final TextField fileTextField = new TextField("File", "file");
		fileTextField.setHideLabel(true);
		fileTextField.setInputType("file");
		fileTextField.setAllowBlank(false);
		uploadPanel.add(fileTextField);

		final Hidden jobNumberField = new Hidden("jobNumber", job.getJobNumber());
		final Hidden usernameField = new Hidden("username", user.getUsername());
		
		uploadPanel.add(jobNumberField);
		uploadPanel.add(usernameField);
		
		uploadPanel.addFormListener(new FormListenerAdapter() {
			public void onActionComplete(Form form, int httpStatus, String responseText) {
				uploadBQItemsResponseCallback(responseText);					
			}
			
			public void onActionFailed(Form form, int httpStatus, String responseText) {
				uploadBQItemsResponseCallback(responseText);
			}
		});
		
		
		Button uploadButton = new Button("Upload");
		uploadButton.setCls("table-cell");
		uploadButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				uploadPanel.getForm().submit(GlobalParameter.BQITEM_IV_UPLOAD_URL, null, Connection.POST, "Importing...", false);
			}});
		
		Button closeButton = new Button("Close");
		closeButton.setCls("table-cell");
		closeButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				uploadExcelWindow.close();
			}});
		
		uploadExcelWindow.setButtons(new Button[]{uploadButton, closeButton});
		uploadExcelWindow.add(uploadPanel);
		uploadExcelWindow.show();
	}
	
	/**
	 * @author tikywong
	 * May 17, 2011 11:28:41 AM
	 */
	private void uploadBQItemsResponseCallback(String responseText){
		JSONValue jsonValue = JSONParser.parse(responseText);
		JSONObject jsonObj = jsonValue.isObject();
		
		if (jsonObj.get("success").isBoolean().booleanValue()) {
			MessageBox.alert("Imported successfully.");
			uploadExcelWindow.close();
		}
		else{
			String error = jsonObj.get("error").isString().toString();
			MessageBox.alert("Import Failed: " + error);
		}
	}
	
	/**
	 * @author tikywong
	 * Mar 31, 2011 4:59:30 PM
	 * modified by irischau
	 * on 04 Apr 2014
	 */
	private void exportExcel(){
		String jobNumber = job.getJobNumber();
		com.google.gwt.user.client.Window.open(GlobalParameter.BQITEM_IV_DOWNLOAD_URL + "?jobNumber=" + jobNumber
																							+ "&billNo=" + billNoTextField.getText().trim()
																							+ "&subBillNo=" + subBillNoTextField.getText().trim()
																							+ "&pageNo=" + pageNoTextField.getText().trim()
																							+ "&itemNo=" + itemNoTextField.getText().trim()
																							+ "&bqDescription=" + bqDescriptionTextField.getText().trim()
																							, "_blank", "");
	}
	
	/**
	 * 
	 * @author tikywong
	 * Mar 31, 2011 4:58:39 PM
	 */
	private void postIVMovements(){
		/*
		 * add IV movemnt amount of Resource summary
		 * @author Peter Chan
		 */
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getBqResourceSummaryRepository().getIVMovementOfJobFromResourceSummary(job, new AsyncCallback<Double>(){
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
			}

			public void onSuccess(Double ivMovementAmount) {
				MessageBox.confirm("Confirmation", "Post IV Movements("+ivMovementAmount+")?", new MessageBox.ConfirmCallback(){
					public void execute(String btnID) {
						if(btnID.equals("yes"))
							globalSectionController.postIVMovements(false);
					}
				});
				
			}
			
		});
	}
	
	/**
	 * @author tikywong
	 * Mar 31, 2011 3:58:39 PM
	 */
	private void getPage(int pageNum){
		SessionTimeoutCheck.renewSessionTimer();
		bqRepository.getBQItemsForIVInputByPage(pageNum, new AsyncCallback<IVInputByBQItemPaginationWrapper>(){
			public void onSuccess(final IVInputByBQItemPaginationWrapper wrapper) {
				populateGrid(wrapper);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
			}
		});
	}
	
/**
	 * @author tikywong
	 * Apr 1, 2011 5:39:16 PM
	 * @param wrapper
	 */
	private void populateGrid(IVInputByBQItemPaginationWrapper wrapper) {
		if(wrapper==null){
			paginationToolbar.setTotalPage(1);		//Display total page = 1
			paginationToolbar.setCurrentPage(0);	//Display current page = 1
			MessageBox.alert("No data was found.");
			return;
		}
		
		//toolbar setup
		dataStore.removeAll();
		Integer numOfRecords = wrapper.getTotalRecords();
		double totalCumulativeIVAmount = wrapper.getTotalCumulativeIVAmount();
		double totalPostedIVAmount = wrapper.getTotalPostedIVAmount();
		double totalIVMovementAmount = wrapper.getTotalIVMovementAmount();
		NumberFormat numberFormat = NumberFormat.getFormat("#,##0.0#");
		
		totalNumOfRecordsTextItem.setText("<b>"+numOfRecords.toString()+" Records</b>");
		totalPostedIVAmountItem.setText("<b>Posted IV:&nbsp;&nbsp;</b>"+numberFormat.format(totalPostedIVAmount)+"</b>");
		totalCumIVAmountItem.setText("<b>Cumulative IV:&nbsp;&nbsp;</b>"+numberFormat.format(totalCumulativeIVAmount)+"</b>");
		totalMovementIVAmountItem.setText("<b>Movement:&nbsp;&nbsp;</b>"+numberFormat.format(totalIVMovementAmount)+"</b>");
		paginationToolbar.setTotalPage(wrapper.getTotalPage());
		paginationToolbar.setCurrentPage(wrapper.getCurrentPage());
		
		//populate the grid
		try{
			for(BQItem bqItem: wrapper.getCurrentPageContentList()){
				String bpi = 	(bqItem.getRefBillNo()==null?"":bqItem.getRefBillNo().trim()) + "/" +
								(bqItem.getRefSubBillNo()==null?"":bqItem.getRefSubBillNo().trim()) + "//" +
								(bqItem.getRefPageNo()==null?"":bqItem.getRefPageNo().trim()) + "/" +
								(bqItem.getItemNo()==null?"":bqItem.getItemNo().trim());
				
				//References
				Double cumulativeIVAmount = bqItem.getIvCumAmount()==null?0.00:bqItem.getIvCumAmount();
				Double costRate = bqItem.getCostRate()==null?0.00:bqItem.getCostRate();
				
				//For Bill 80, "Resource Rate" is always 0, assume costRate = 1 and set cumulative IV Quantity = cumulative IV Amount
				String billNo = bqItem.getRefBillNo();
				boolean isBill80 = billNo!=null && !billNo.trim().equals("") && billNo.trim().equals("80");
				
				//Calculations
				Double costAmount = costRate * (bqItem.getRemeasuredQuantity()==null?0.00:bqItem.getRemeasuredQuantity());
				Double cumulativeIVQuantity = isBill80 ? cumulativeIVAmount : (costRate==0 ? 0.00 : (cumulativeIVAmount / costRate));
				Double ivMovementAmount = 	cumulativeIVAmount - (bqItem.getIvPostedAmount()==null?0.00:bqItem.getIvPostedAmount());
				
				Record record = bqItemRecordDef.createRecord(new Object[]{
								bqItem.getSequenceNo(),
								bpi,
								bqItem.getDescription(),
								bqItem.getRemeasuredQuantity(),
								bqItem.getUnit(),
								costRate,
								costAmount,
								bqItem.getIvPostedAmount(),
								RoundingUtil.round(cumulativeIVQuantity, 2),
								cumulativeIVAmount,
								ivMovementAmount,
								bqItem.getBqType(),
								//Non-displaying records
								bqItem.getId().toString(),
								bqItem.getRemeasuredQuantity(),
								bqItem.getCostRate(),
								costAmount
				});
				dataStore.addSorted(record);
				
			}
		}catch(Exception e){
			UIUtil.alert(e);
		}
		
		//hide detail panel
		if (!globalSectionController.getDetailSectionController().getMainPanel().isCollapsed())
			globalSectionController.getDetailSectionController().getMainPanel().collapse();

	}


	/**
	 * @author tikywong
	 * Mar 31, 2011 3:57:43 PM
	 */
	private void search() {
		billNo = billNoTextField.getText().trim();
		subBillNo = subBillNoTextField.getText().trim();
		pageNo = pageNoTextField.getText().trim();
		itemNo = itemNoTextField.getText().trim();
		bqDescription = bqDescriptionTextField.getText().trim();
		
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.SEARCHING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		bqRepository.searchBQItemsForIVInput(job.getJobNumber(), billNo, subBillNo, pageNo, itemNo, bqDescription, new AsyncCallback<IVInputByBQItemPaginationWrapper>(){
				public void onSuccess(final IVInputByBQItemPaginationWrapper wrapper) {
					getPage(0);
				}
				public void onFailure(Throwable e) {
					UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
				}
		});

	}
	
	/**
	 * @author tikywong
	 * Mar 31, 2011 3:58:33 PM
	 */
	private void applyPercentage() {
		String applyPercentageForValidate = applyPercentageNumberField.getText().trim();
		// check if percentage is null or empty
		if(applyPercentageForValidate == null || applyPercentageForValidate.length() <= 0)
			MessageBox.alert("Please input number to apply %");
		else{
			Double percentage = applyPercentageNumberField.getValue().doubleValue();
			// check if percentage in between 0 and 100
			if(percentage < 0.0 || percentage > 100.0)
				MessageBox.alert("Please input a valid percentage!");
			else{
				Record[] records = this.dataStore.getRecords();
				// check if no record
				if(records == null || records.length <= 0)
					MessageBox.alert("There is no record to apply percentage.");
				else{
					if(!updateDataOfApplyPercentage(records, percentage))
						MessageBox.alert("Error occured during apply percentage");
				}
			}
		}	
	}
	
	/**
	 * @author tikywong
	 * Apr 19, 2011 2:39:09 PM
	 */
	private boolean updateDataOfApplyPercentage(Record[] records, Double percentage) {
		boolean updatedAll = true;
		for(Record record : records){
			if(record.isEmpty("dbRemeasuredQuantityRecord") || record.isEmpty("dbCostRateRecord") || record.isEmpty("postedIVAmountRecord")){
				updatedAll = false;
				break;
			}
			
			//References
			double maxDBQuantity = record.isEmpty("dbRemeasuredQuantityRecord")?0.00:record.getAsDouble("dbRemeasuredQuantityRecord");
			double dbResourceRate = record.isEmpty("dbCostRateRecord")?0.00:record.getAsDouble("dbCostRateRecord");
			double postedIVAmount = record.isEmpty("postedIVAmountRecord")?0.00:record.getAsDouble("postedIVAmountRecord");
			
			//For Bill 80, "Resource Rate" is always 0, assume Resource Rate = 1 and set Cumulative IV Amount = Cumulative IV Quantity
			String bpi = record.getAsString("billItemRecord");
			String[] bpiSplit = bpi.split("/");
			boolean isBill80 = bpiSplit[0]!=null && !bpiSplit[0].trim().equals("") && bpiSplit[0].trim().equals("80");
			
			//Calculate by percentage
			double updatedCumIVQuantity = (maxDBQuantity * (percentage / 100.0));
			double updatedCumIVAmount = isBill80 ? updatedCumIVQuantity : (updatedCumIVQuantity * dbResourceRate);
			double updatedMovementIVAmount = (updatedCumIVAmount - postedIVAmount);
			
			record.set("cumIVQuantityRecord", updatedCumIVQuantity);
			record.set("cumIVAmountRecord", updatedCumIVAmount);
			record.set("movementIVAmountRecord", updatedMovementIVAmount);
		}
		return updatedAll;
	}


	/**
	 * @author tikywong
	 * Apr 8, 2011 2:41:03 PM
	 */
	private Boolean sameDoubleSign(double a, double b){
		if(a<=0 && b<=0)	//i.e. -10<=0, -2<=0 or 0<=0, -2<=0
			return true;
		else if(a>=0 && b>=0)	//i.e. 10>=0, 2>=0 or 0>=0, 2>=0
			return true;
		else
			return false;
	}
	
	private Boolean checkifAmountexceededCostAmount(double amount, double costAmount){
		if(costAmount<0 && costAmount<=amount && amount<=0) //i.e. -10<0, -10<=-2, -2<=0
			return false;
		else if(costAmount>0 && costAmount>=amount && amount>=0)	//i.e. 10>0, 10>=2, 2>=0
			return false;
		else if(costAmount==0 && amount==0)
			return false;
		else
			return true;
	}

	public void showUpdateIVByResourceMainPanel(String billItem) {
		globalSectionController.getMainSectionController().resetMainPanel();
		UpdateCumIVByResourceMainPanel updateCumIVByResourceMainPanel = new UpdateCumIVByResourceMainPanel(globalSectionController, repackagingStatus);
		updateCumIVByResourceMainPanel.search(billItem);
		globalSectionController.getMainSectionController().setContentPanel(updateCumIVByResourceMainPanel);
		globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();
		globalSectionController.getDetailSectionController().resetPanel();
		
	}
	
}
