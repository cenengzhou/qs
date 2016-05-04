/**
 * koeyyeung
 * Jul 2, 20135:35:10 PM
 * Change from Window to Panel
 */
package com.gammon.qs.client.ui.panel.mainSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gammon.qs.application.GeneralPreferencesKey;
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
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.Resource;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVInputByResourcePaginationWrapper;
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
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

public class UpdateCumIVByResourceMainPanel extends Panel{
	private static final String MAIN_PANEL_ID = "UpdateCumIVByResourceMainPanel_ID";	
	private static final boolean SORTABLE = true;
	
	
	//UI
	private EditorGridPanel resultEditorGridPanel;

	//Button with security
	private ToolbarButton postIVButton;
	private ToolbarButton downloadExcelButton;
	private ToolbarButton uploadExcelButton;
	private ToolbarButton recalculateIVAmountButton;
	private ToolbarButton updateButton;
	
	private Window uploadExcelWindow;
	
	private GlobalMessageTicket globalMessageTicket;
	
	//UI-search panel
	private TextField billNoTextField;
	private TextField subBillNoTextField;
	private TextField pageNoTextField;
	private TextField itemNoTextField;
	private TextField resourceDescriptionTextField;
	private TextField objectCodeTextField;
	private TextField subsidiaryCodeTextField;
	private TextField packageNoTextField;
	
	private NumberField applyPercentageNumberField;

	//UI-result panel-Datastore, Records
	private Store dataStore;
	private final RecordDef resourceRecordDef = new RecordDef(new FieldDef[]{	
																			new StringFieldDef("billItemRecord"),
																			new StringFieldDef("objectCodeRecord"),
																			new StringFieldDef("subsidiaryCodeRecord"),
																			new StringFieldDef("resourceNoRecord"),
																			new StringFieldDef("packageNoRecord"),
																			new StringFieldDef("resourceDescriptionRecord"),
																			new StringFieldDef("quantityRecord"),
																			new StringFieldDef("unitRecord"),
																			new StringFieldDef("resourceRateRecord"),
																			new StringFieldDef("resourceAmountRecord"),
																			new StringFieldDef("postedIVAmountRecord"),
																			new StringFieldDef("cumIVQuantityRecord"),
																			new StringFieldDef("cumIVAmountRecord"),
																			new StringFieldDef("movementIVAmountRecord"),
																			//Non-displaying records
																			new StringFieldDef("id"),
																			new StringFieldDef("dbQuantityRecord"),
																			new StringFieldDef("dbResourceRateRecord"),
																			new StringFieldDef("dbResourceAmountRecord")});
	
	//UI-result editor grid panel
	private ArrowKeyNavigation arrowKeyNavigation;
	private PaginationToolbar paginationToolbar;
	private ToolbarTextItem totalNumOfRecordsTextItem;
	private ToolbarTextItem totalPostedIVAmountItem;
	private ToolbarTextItem totalCumIVAmountItem;
	private ToolbarTextItem totalMovementIVAmountItem;
	
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
	String resourceDescription;
	String objectCode;
	String subsidiaryCode;
	String packageNo;
	
	//Security Access Rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList; 
	
	/**
	 * 
	 * @param globalSectionController For accessing information
	 * @param repackagingStatus Repackaging Status - 100(Unlocked), 200(Updated), 300(Snapshot), 900(Locked)
	 */
	public UpdateCumIVByResourceMainPanel(GlobalSectionController globalSectionController, String repackagingStatus) {
		super();
		//DATA setup
		this.globalSectionController = globalSectionController;
		detailSectionController = this.globalSectionController.getDetailSectionController();
		this.repackagingStatus =repackagingStatus;
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
		setTitle("IV By Resource");
		
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
		searchPanel.setHeight(80);
		searchPanel.setLayout(new TableLayout(9));
		searchPanel.setMargins(2);	
		searchPanel.setPaddings(2);
		
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
		billNoTextField.setCtCls("table-cell");
		searchPanel.add(billNoLabel);
		searchPanel.add(billNoTextField);
		
		Label subBillNoLabel = new Label("Sub-Bill No.");
		subBillNoTextField = new TextField("Sub-Bill No.", "subBillNo", 100);
		subBillNoTextField.addListener(searchListener);
		subBillNoTextField.setCtCls("table-cell");
		searchPanel.add(subBillNoLabel);
		searchPanel.add(subBillNoTextField);
		
		Label pageNoLabel = new Label("Page No.");
		
		pageNoTextField = new TextField("Page No.", "pageNo", 100);
		pageNoTextField.addListener(searchListener);
		pageNoTextField.setCtCls("table-cell");
		searchPanel.add(pageNoLabel);
		searchPanel.add(pageNoTextField);
		
		Label itemNoLabel = new Label("Item No.");
		
		itemNoTextField = new TextField("Item No.", "itemNo", 100);
		itemNoTextField.addListener(searchListener);
		itemNoTextField.setCtCls("table-cell");
		searchPanel.add(itemNoLabel);
		searchPanel.add(itemNoTextField);
		
		Label emptySpace1 = new Label();
		emptySpace1.setHtml("&nbsp;");
		emptySpace1.setWidth(20);
		searchPanel.add(emptySpace1);
		
		
		Label resourceDescriptionLabel = new Label("Resource Description");
		
		resourceDescriptionTextField = new TextField("Resource Description", "resourceDescription", 100);
		resourceDescriptionTextField.addListener(searchListener);
		resourceDescriptionTextField.setCtCls("table-cell");
		searchPanel.add(resourceDescriptionLabel);
		searchPanel.add(resourceDescriptionTextField);
		
		Label objectCodeLabel = new Label("Object Code");
		
		objectCodeTextField = new TextField("Object Code", "objectCode", 100);
		objectCodeTextField.addListener(searchListener);
		objectCodeTextField.setCtCls("table-cell");
		searchPanel.add(objectCodeLabel);
		searchPanel.add(objectCodeTextField);
		
		Label subsidiaryCodeLabel = new Label("Subsidiary Code");
		
		subsidiaryCodeTextField = new TextField("Subsidiary Code", "subsidiaryCode", 100);
		subsidiaryCodeTextField.setCtCls("table-cell");
		subsidiaryCodeTextField.addListener(searchListener);
		subsidiaryCodeTextField.setCtCls("table-cell");
		searchPanel.add(subsidiaryCodeLabel);
		searchPanel.add(subsidiaryCodeTextField);
		
		Label packageNoLabel = new Label("Package No");
		
		packageNoTextField = new TextField("Package No", "packageNo", 100);
		packageNoTextField.addListener(searchListener);
		packageNoTextField.setCtCls("table-cell");
		searchPanel.add(packageNoLabel);
		searchPanel.add(packageNoTextField);
		
		
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
		downloadExcelButton = new ToolbarButton("Export Resources to Excel");
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
		
		//4. Recalculate Button
		recalculateIVAmountButton = new ToolbarButton("Recalculate Resource IV");
		recalculateIVAmountButton.setCls("toolbar-button");
		recalculateIVAmountButton.setTooltip("Recalculate Resource IV", "Recalculate BQItem, Resource, BQResourceSummary, SCDetail & SCPackage IV/Workdone amounts");
		recalculateIVAmountButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				confirmToRecalculateIV();
			}	
		});
		
		this.setTopToolbar(new Button[]{postIVButton, downloadExcelButton, uploadExcelButton, recalculateIVAmountButton});
		
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
		recalculateIVAmountButton.setVisible(false);
		updateButton.setVisible(false);
		
		toolbar.addButton(postIVButton);
		toolbar.addSeparator();
		toolbar.addButton(downloadExcelButton);
		toolbar.addSeparator();
		toolbar.addButton(uploadExcelButton);
		toolbar.addSeparator();
		toolbar.addButton(recalculateIVAmountButton);
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

		
		toolbar.addFill();
		toolbar.addItem(applyPercentageTextItem);
		toolbar.addField(applyPercentageNumberField);
		toolbar.addSeparator();
		toolbar.addButton(applyPercentageButton);
		toolbar.addSeparator();
		
		setTopToolbar(toolbar);
	}
	
	private void setupGridPanel() {
		//PREFERENCE setup
		Map<String, String> preferences = user.getGeneralPreferences();
		String quantityDP = preferences.get(GeneralPreferencesKey.QUANTITY_DECIMAL_PLACES);
		if(quantityDP==null)
			quantityDP = "1";
		String amountDP = preferences.get(GeneralPreferencesKey.AMOUNT_DECIMAL_PLACES);
		if(amountDP==null)
			amountDP = "2";
		
		
		//RESULT EDITOR GRID PANEL setup
		resultEditorGridPanel = new EditorGridPanel();
		resultEditorGridPanel.setView(new GridView());
		arrowKeyNavigation = new ArrowKeyNavigation(resultEditorGridPanel);
		
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
		ColumnConfig billItemColConfig	 			= new ColumnConfig("Bill Item", "billItemRecord", 50, SORTABLE);
		ColumnConfig objectCodeColConfig			= new ColumnConfig("Object Code", "objectCodeRecord", 80, SORTABLE);
		ColumnConfig subsidiaryCodeColConfig		= new ColumnConfig("Subsidiary Code", "subsidiaryCodeRecord", 100, SORTABLE);
		ColumnConfig resourceNoColConfig			= new ColumnConfig("Resource No", "resourceNoRecord", 60, SORTABLE);
		ColumnConfig packageNoColConfig				= new ColumnConfig("Package No", "packageNoRecord", 50, SORTABLE);
		ColumnConfig resourceDescriptionColConfig	= new ColumnConfig("Resource Description", "resourceDescriptionRecord", 200, SORTABLE);
		ColumnConfig quantityColConfig				= new ColumnConfig("Quantity", "quantityRecord", 80, !SORTABLE, quantityRenderer);
		ColumnConfig unitColConfig					= new ColumnConfig("Unit", "unitRecord", 50, SORTABLE);
		ColumnConfig resourceRateColConfig			= new ColumnConfig("Resource Rate", "resourceRateRecord", 80, !SORTABLE, rateRenderer);
		ColumnConfig resourceAmountColConfig		= new ColumnConfig("Resource Amount", "resourceAmountRecord", 100, !SORTABLE, amountRenderer);
		ColumnConfig postedIVAmountColConfig		= new ColumnConfig("Posted IV Amount", "postedIVAmountRecord", 100, !SORTABLE, amountRenderer);
		
		//Editable Columns
		ColumnConfig cumIVQuantityColConfig		= new ColumnConfig("Cum IV Quantity", "cumIVQuantityRecord", 100, !SORTABLE, editableQuantityRenderer);
		ColumnConfig cumIVAmountColConfig		= new ColumnConfig("Cum IV Amount", "cumIVAmountRecord", 100, !SORTABLE, editableAmountRenderer);
		ColumnConfig movementIVAmountColConfig	= new ColumnConfig("IV Movement Amount", "movementIVAmountRecord", 120, !SORTABLE, editableAmountRenderer);
		
		//Columns/Editable Columns alignment setup
		quantityColConfig.setAlign(TextAlign.RIGHT);
		resourceRateColConfig.setAlign(TextAlign.RIGHT);
		resourceAmountColConfig.setAlign(TextAlign.RIGHT);
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
		ArrayReader reader = new ArrayReader(resourceRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		dataStore.setSortInfo(new SortState("billItem", SortDir.ASC));
		resultEditorGridPanel.setStore(dataStore);
		
		BaseColumnConfig[] columns = new BaseColumnConfig[]{billItemColConfig,
															objectCodeColConfig,
															subsidiaryCodeColConfig,
															resourceNoColConfig,
															packageNoColConfig,
															resourceDescriptionColConfig,
															quantityColConfig,
															unitColConfig,
															resourceRateColConfig,
															resourceAmountColConfig,
															postedIVAmountColConfig,
															cumIVQuantityColConfig,
															cumIVAmountColConfig,
															movementIVAmountColConfig};
		resultEditorGridPanel.setColumnModel(new ColumnModel(columns));
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
					
					double dbQuantity = record.isEmpty("dbQuantityRecord")?0.00:record.getAsDouble("dbQuantityRecord");
					double dbResourceAmount = isBill80 ? dbQuantity : record.isEmpty("dbResourceAmountRecord")?0.00:record.getAsDouble("dbResourceAmountRecord");
					double postedIVAmount = record.isEmpty("postedIVAmountRecord")?0.00:record.getAsDouble("postedIVAmountRecord");
					
					//Validation 1: Cumulative IV Quantity
					if(field.equals("cumIVQuantityRecord")){					
						double newCumIVQuantity = value.toString().equals(GlobalParameter.NULL_REPLACEMENT)?0.00:Double.parseDouble(value.toString());
						
						//1a: cumIVQuantity, quantity need to have same sign
						if(!sameDoubleSign(RoundingUtil.round(newCumIVQuantity,2), RoundingUtil.round(dbQuantity,2))){
							MessageBox.alert("'Cumulative IV Quantity' and 'Quantity' must have the same sign(+/-)");
							return false;
						}
						
						//1b. cumIVQuantity cannot exceed quantity
						if(checkifAmountexceededCostAmount(RoundingUtil.round(newCumIVQuantity,2), RoundingUtil.round(dbQuantity,2))){
							MessageBox.alert("Cumulative IV Quantity: "+newCumIVQuantity+" cannot exceed 'Quantity'");
							return false;
						}
					}
					//Validation 2: Movement IV Amount
					else if(field.equals("movementIVAmountRecord")){
						double newMovementIVAmount = value.toString().equals(GlobalParameter.NULL_REPLACEMENT)?0.00:Double.parseDouble(value.toString());
						double newCumulativeIVAmount = postedIVAmount+newMovementIVAmount;
						
						//2a: cumIVAmount, resourceAmount need to have same sign
						if(!sameDoubleSign(RoundingUtil.round(newCumulativeIVAmount,2), RoundingUtil.round(dbResourceAmount,2))){
							MessageBox.alert("'Cumulative IV Amount' and 'Resource Amount' must have the same sign(+/-). Please adjust the IV Movement Amount.");
							return false;
						}
						
						//2b: movementIVAmount+postedIVAmount cannot exceed costAmount
						if(checkifAmountexceededCostAmount(RoundingUtil.round(newCumulativeIVAmount,2),RoundingUtil.round(dbResourceAmount,2))){
							MessageBox.alert("Posted IV Amount'+'IV Movement: "+newCumulativeIVAmount+" Amount cannot exceed 'Resource Amount'");
							return false;
						}
					}
					//Validation 3: Cumulative IV Amount
					else if(field.equals("cumIVAmountRecord")){
						double newCumulativeIVAmount = value.toString().equals(GlobalParameter.NULL_REPLACEMENT)?0.00:Double.parseDouble(value.toString());
						
						//3a: cumIVQuantity, resourceAmount need to have same sign
						if(!sameDoubleSign(RoundingUtil.round(newCumulativeIVAmount,2), RoundingUtil.round(dbResourceAmount,2))){
							MessageBox.alert("'Cumulative IV Amount' and 'Resource Amount' must have the same sign(+/-)");
							return false;
						}
						
						//3a: cumulativeIVAmount can not be greater than costAmount
						if(checkifAmountexceededCostAmount(RoundingUtil.round(newCumulativeIVAmount,2),RoundingUtil.round(dbResourceAmount,2))){
							MessageBox.alert("Cumulative IV Amount: "+newCumulativeIVAmount+" cannot exceed 'Resource Amount'");
							return false;
						}
					}
				}
				return true;
			}
			
			public void onAfterEdit(GridPanel grid, Record record, String field, Object newValue, Object oldValue, int rowIndex, int colIndex) {
				//For Bill 80, "Resource Rate" is always 0, assume Resource Rate = 1 and set Cumulative IV Amount = Cumulative IV Quantity
				String bpi = record.getAsString("billItemRecord");
				String[] bpiSplit = bpi.split("/");
				boolean isBill80 = bpiSplit[0]!=null && !bpiSplit[0].trim().equals("") && bpiSplit[0].trim().equals("80");
				
				double dbResourceRate = record.getAsDouble("dbResourceRateRecord");
				double postedIVAmount = record.isEmpty("postedIVAmountRecord")?0.00:record.getAsDouble("postedIVAmountRecord");

				if(field.equals("cumIVQuantityRecord")){
					double newCumulativeIVQuantity = newValue==null?0.00:Double.parseDouble(newValue.toString());
					
					double newCumulativeIVAmount = isBill80 ? newCumulativeIVQuantity : (newCumulativeIVQuantity * dbResourceRate);
					double newMovementIVAmount = newCumulativeIVAmount - postedIVAmount;
					record.set("cumIVAmountRecord", newCumulativeIVAmount);
					record.set("movementIVAmountRecord", newMovementIVAmount);
				}
				else if(field.equals("movementIVAmountRecord")){
					double newMovementIVAmount = newValue==null?0.00:Double.parseDouble(newValue.toString());

					double newCumulativeIVAmount = postedIVAmount + newMovementIVAmount;
					double newCumulativeIVQuantity = isBill80? newCumulativeIVAmount : (dbResourceRate==0 ? 0.00 : newCumulativeIVAmount / dbResourceRate);
					record.set("cumIVAmountRecord", newCumulativeIVAmount);
					record.set("cumIVQuantityRecord", newCumulativeIVQuantity);
				}
				else if(field.equals("cumIVAmountRecord")){
					double newCumulativeIVAmount = newValue==null?0.00:Double.parseDouble(newValue.toString());
					
					double newMovementIVAmount = newCumulativeIVAmount-postedIVAmount;
					double newCumulativeQuantity = isBill80 ? newCumulativeIVAmount : (dbResourceRate==0 ? 0.00 : newCumulativeIVAmount / dbResourceRate);
					record.set("movementIVAmountRecord", newMovementIVAmount);
					record.set("cumIVQuantityRecord", newCumulativeQuantity);
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
							if (btnID.toLowerCase().equals("yes")){
								getPage(pageNum);	
							}
						}
					});
					
					MessageBox.show(messageBoxCofig);
				}
				else{
					getPage(pageNum);
				}
			}
		});
		
		resultEditorGridPanel.setBottomToolbar(paginationToolbar);
		
		add(resultEditorGridPanel);
		
		SessionTimeoutCheck.renewSessionTimer();
		userAccessRightsRepository.getAccessRights(user.getUsername(), RoleSecurityFunctions.F010118_UPDATE_IV_BY_RESOURCE_MAINPANEL, new AsyncCallback<List<String>>(){
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
	 * @author tikywong
	 * May 20, 2011 2:16:26 PM
	 */
	private void confirmToRecalculateIV() {
		MessageBox.confirm("Confirmation", "Are you sure that you want to recalculate the ALL IV amounts of Job"+job.getJobNumber()+"?", new MessageBox.ConfirmCallback(){
			public void execute(String btnID) {
				if(btnID.equals("yes")){
					UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.RECALCULATING_MSG, true);
					SessionTimeoutCheck.renewSessionTimer();
					bqRepository.recalculateIVAmountsByResourceForMethodThree(job.getJobNumber(), new AsyncCallback<Boolean>(){
						public void onFailure(Throwable e) {
							UIUtil.unmaskPanelById(MAIN_PANEL_ID);
							UIUtil.checkSessionTimeout(e, false,globalSectionController.getUser());
						}

						public void onSuccess(Boolean success) {	
							UIUtil.unmaskPanelById(MAIN_PANEL_ID);
							if(success)
								MessageBox.alert("IV amounts have been recalculated.");
							else
								MessageBox.alert("Failed to recalculate IV amounts.");
						}
					});
				}
			}
		});
	}

	/**
	 * @author tikywong
	 * Apr 11, 2011 10:55:31 AM
	 */
	private boolean sameDoubleSign(double a, double b) {
		if(a<=0 && b<=0)	//i.e. -10<=0, -2<=0 or 0<=0, -2<=0
			return true;
		else if(a>=0 && b>=0)	//i.e. 10>=0, 2>=0 or 0>=0, 2>=0
			return true;
		else
			return false;
	}

	/**
	 * @author tikywong
	 * Apr 11, 2011 10:55:25 AM
	 */
	private boolean checkifAmountexceededCostAmount(double amount, double costAmount) {
		if(costAmount<0 && costAmount<=amount && amount<=0) //i.e. -10<0, -10<=-2, -2<=0
			return false;
		else if(costAmount>0 && costAmount>=amount && amount>=0)	//i.e. 10>0, 10>=2, 2>=0
			return false;
		else if(costAmount==0 && amount==0)
			return false;
		else
			return true;
	}

	/**
	 * @author tikywong
	 * Apr 1, 2011 9:31:47 AM
	 */
	private void securitySetup() {
		if(accessRightsList != null && accessRightsList.contains("WRITE")){
			downloadExcelButton.setVisible(true);
			if("900".equals(repackagingStatus)){
				postIVButton.setVisible(true);
				uploadExcelButton.setVisible(true);
				recalculateIVAmountButton.setVisible(true);
				updateButton.setVisible(true);
			}
		}
		
		if(accessRightsList != null && accessRightsList.contains("READ"))
			downloadExcelButton.setVisible(true);		
	}

	/**
	 * @author tikywong
	 * Apr 1, 2011 9:31:25 AM
	 */
	private void update() {
		List<Resource> resources = new ArrayList<Resource>();
		
		//Get the modified data list and create the resources with the cumIVQuantityRecord, cumIVAmountRecord
		Record[] records = dataStore.getModifiedRecords();
		for(Record record: records)
			resources.add(resourceFromRecord(record));
		
		//No update if no data has been modified
		if(resources.size()==0){
			MessageBox.alert("No records changed");
			return;
		}
		
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.SAVING_MSG, true);
		try{
			SessionTimeoutCheck.renewSessionTimer();
			bqRepository.updateResourcesIVAmount(resources, user.getUsername(), new AsyncCallback<Boolean>(){
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
	 * Apr 11, 2011 9:59:31 AM
	 * 
	 * Create a Resource by inserting Resource's id, ivCumAmount, ivCumQty
	 */
	private Resource resourceFromRecord(Record record) {
		Resource resource = new Resource();
		String id = record.getAsString("id");
		
		if(id!=null && !id.equals(""))
			resource.setId(new Long(id));
		
		resource.setIvCumAmount(Double.valueOf(record.getAsDouble("cumIVAmountRecord")));
		resource.setIvCumQuantity(Double.valueOf(record.getAsDouble("cumIVQuantityRecord")));
		resource.setIvMovementAmount(Double.valueOf(record.getAsDouble("movementIVAmountRecord")));
		return resource;
	}

	/**
	 * @author tikywong
	 * Apr 1, 2011 9:31:16 AM
	 */
	private void showUploadWindow() {
		uploadExcelWindow = new Window();
		uploadExcelWindow.setLayout(new FitLayout());
		uploadExcelWindow.setWidth(250);
		uploadExcelWindow.setTitle("Import IV amounts from Excel");

		final FormPanel uploadPanel = new FormPanel();
		uploadPanel.setHeight(30);
		uploadPanel.setPaddings(2, 2, 2, 2);
		uploadPanel.setFileUpload(true);

		final TextField fileTextField = new TextField("File", "file");
		fileTextField.setHideLabel(true);
		fileTextField.setInputType("file");
		fileTextField.setAllowBlank(false);
		fileTextField.setCtCls("table-cell");
		uploadPanel.add(fileTextField);

		final Hidden jobNumberField = new Hidden("jobNumber", job.getJobNumber());
		final Hidden usernameField = new Hidden("username", user.getUsername());
		
		uploadPanel.add(jobNumberField);
		uploadPanel.add(usernameField);
		
		uploadPanel.addFormListener(new FormListenerAdapter() {
			public void onActionComplete(Form form, int httpStatus, String responseText) {
				uploadResourcesResponseCallback(responseText);				
			}
			
			public void onActionFailed(Form form, int httpStatus, String responseText) {
				uploadResourcesResponseCallback(responseText);
			}
		});
		
		
		Button uploadButton = new Button("Upload");
		uploadButton.setCtCls("table-cell");
		uploadButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				uploadPanel.getForm().submit(GlobalParameter.RESOURCE_IV_UPLOAD_URL, null, Connection.POST, "Importing...", false);
			}});
		
		Button closeButton = new Button("Close");
		
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
	private void uploadResourcesResponseCallback(String responseText){
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
	 * Apr 1, 2011 9:31:08 AM
	 */
	private void exportExcel() {
		String jobNumber = job.getJobNumber();
		com.google.gwt.user.client.Window.open(GlobalParameter.RESOURCE_IV_DOWNLOAD_URL + "?jobNumber=" + jobNumber
																								+ "&billNo=" + billNoTextField.getValueAsString().trim()
																								+ "&subBillNo=" + subBillNoTextField.getValueAsString().trim()
																								+ "&pageNo=" + pageNoTextField.getValueAsString().trim()
																								+ "&itemNo=" + itemNoTextField.getValueAsString().trim()
																								+ "&resourceDescription=" + resourceDescriptionTextField.getValueAsString().trim()
																								+ "&objectCode=" + objectCodeTextField.getValueAsString().trim()
																								+ "&subsidiaryCode=" + subsidiaryCodeTextField.getValueAsString().trim()
																								+ "&packageNo=" + packageNoTextField.getValueAsString().trim()
																								, "_blank", "");
	}

	/**
	 * @author tikywong
	 * updated 
	 * @author peterchan
	 * Apr 1, 2011 9:31:01 AM
	 */
	private void postIVMovements() {
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
	 * Apr 1, 2011 9:30:43 AM
	 */
	private void getPage(int pageNum) {
		SessionTimeoutCheck.renewSessionTimer();
		bqRepository.getResourcesForIVInputByPage(pageNum, new AsyncCallback<IVInputByResourcePaginationWrapper>(){
			public void onSuccess(final IVInputByResourcePaginationWrapper wrapper) {
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
	 * Apr 7, 2011 1:36:52 PM
	 */
	protected void populateGrid(IVInputByResourcePaginationWrapper wrapper) {
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
			for(Resource resource: wrapper.getCurrentPageContentList()){
				//For Bill 80, "Resource Rate" is always 0, assume Resource Rate = 1 and set Cumulative IV Amount = Cumulative IV Quantity
				String billNo = resource.getRefBillNo();
				String[] bpiSplit = billNo.split("/");
				boolean isBill80 = bpiSplit[0]!=null && !bpiSplit[0].trim().equals("") && bpiSplit[0].trim().equals("80");
				
				String bpi = 	(resource.getRefBillNo()==null?"":resource.getRefBillNo().trim()) + "/" +
								(resource.getRefSubBillNo()==null?"":resource.getRefSubBillNo().trim()) + "/" +
								"/"+
								(resource.getRefPageNo()==null?"":resource.getRefPageNo().trim()) + "/" +
								(resource.getRefItemNo()==null?"":resource.getRefItemNo().trim());

				Double costRate				= 	resource.getCostRate()==null?0.00:resource.getCostRate();
				//By Peter Chan 2011-09-20
				Double quantity				= 	(resource.getQuantity()==null?0.00:resource.getQuantity()) * (resource.getRemeasuredFactor()==null?0.00:resource.getRemeasuredFactor());
				Double cumulativeIVAmount	= 	resource.getIvCumAmount()==null?0.00:resource.getIvCumAmount();
				Double resourceAmount 		= 	quantity * costRate;
				Double cumulativeIVQuantity =	isBill80 ? cumulativeIVAmount : (costRate==0? 0.00 : cumulativeIVAmount / costRate);
				Double ivMovementAmount = 		cumulativeIVAmount - (resource.getIvPostedAmount()==null?0.00:resource.getIvPostedAmount());

				Record record = resourceRecordDef.createRecord(new Object[]{
								bpi,
								resource.getObjectCode(),
								resource.getSubsidiaryCode(),
								resource.getResourceNo(),
								resource.getPackageNo(),
								resource.getDescription(),
								quantity,
								resource.getUnit(),
								costRate,
								resourceAmount,
								resource.getIvPostedAmount(),
								RoundingUtil.round(cumulativeIVQuantity, 2),
								cumulativeIVAmount,
								ivMovementAmount,
								//Non-displaying records
								resource.getId().toString(),
								resource.getQuantity()*resource.getRemeasuredFactor(),
								resource.getCostRate(),
								resourceAmount
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
	 * Apr 1, 2011 9:30:23 AM
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
	 * Apr 19, 2011 2:21:45 PM
	 */
	private boolean updateDataOfApplyPercentage(Record[] records, Double percentage) {
		boolean updatedAll = true;
		for(Record record : records){
			if(record.isEmpty("dbQuantityRecord") || record.isEmpty("dbResourceRateRecord") || record.isEmpty("postedIVAmountRecord")){
				updatedAll = false;
				break;
			}
			
			//For Bill 80, "Resource Rate" is always 0, assume Resource Rate = 1 and set Cumulative IV Amount = Cumulative IV Quantity
			String bpi = record.getAsString("billItemRecord");
			String[] bpiSplit = bpi.split("/");
			boolean isBill80 = bpiSplit[0]!=null && !bpiSplit[0].trim().equals("") && bpiSplit[0].trim().equals("80");
			
			double maxDBQuantity = record.isEmpty("dbQuantityRecord")?0.00:record.getAsDouble("dbQuantityRecord");
			double dbResourceRate = record.isEmpty("dbResourceRateRecord")?0.00:record.getAsDouble("dbResourceRateRecord");
			double postedIVAmount = record.isEmpty("postedIVAmountRecord")?0.00:record.getAsDouble("postedIVAmountRecord");
			
			double updatedCumIVQuantity = (maxDBQuantity * (percentage / 100.0));
			double updatedCumIVAmount = isBill80 ? updatedCumIVQuantity :(updatedCumIVQuantity * dbResourceRate);
			double updatedMovementIVAmount = (updatedCumIVAmount - postedIVAmount);
			
			record.set("cumIVQuantityRecord", updatedCumIVQuantity);
			record.set("cumIVAmountRecord", updatedCumIVAmount);
			record.set("movementIVAmountRecord", updatedMovementIVAmount);
		}
		return updatedAll;
	}

	/**
	 * @author tikywong
	 * Apr 1, 2011 9:30:10 AM
	 */
	private void search() {
		billNo = billNoTextField.getText().trim();
		subBillNo = subBillNoTextField.getText().trim();
		pageNo = pageNoTextField.getText().trim();
		itemNo = itemNoTextField.getText().trim();
		resourceDescription = resourceDescriptionTextField.getText().trim();
		objectCode = objectCodeTextField.getText().trim();
		subsidiaryCode = subsidiaryCodeTextField.getText().trim();
		packageNo = packageNoTextField.getText().trim();
		
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.SEARCHING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		bqRepository.searchResourcesForIVInput(job.getJobNumber(), billNo, subBillNo, pageNo, itemNo, resourceDescription, objectCode, subsidiaryCode, packageNo, new AsyncCallback<IVInputByResourcePaginationWrapper>(){
			public void onSuccess(final IVInputByResourcePaginationWrapper wrapper) {
				getPage(0);
			}
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
			}
	});
		
	}
	
	public void search(String bpi){
		String[] bpiSplit = bpi.split("/");
		billNoTextField.setValue(bpiSplit[0]);
		billNoTextField.setDisabled(true);
		
		subBillNoTextField.setValue(bpiSplit[1]);
		subBillNoTextField.setDisabled(true);
		
		pageNoTextField.setValue(bpiSplit[3]);
		pageNoTextField.setDisabled(true);
		
		itemNoTextField.setValue(bpi.substring(bpiSplit[0].length()+bpiSplit[1].length()+bpiSplit[3].length()+4));
		itemNoTextField.setDisabled(true);

		search();
	}
	
}
