package com.gammon.qs.client.ui.window.mainSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gammon.qs.application.GeneralPreferencesKey;
import com.gammon.qs.application.User;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRendererCheckIfTotal;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.util.RoundingUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.BooleanFieldDef;
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
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
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
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;

public class MergeOrSplitBQResourceSummaryWindow extends Window {
	// added by brian on 20110401
	public static final String WINDOW_ID = "MergeOrSplitBQResourceSummaryWindow";
	public static final String MAIN_PANEL_ID ="mergeSplitResourceSummaryMainPanel";
	public static final String MERGE = "Merge";
	public static final String SPLIT = "Split";
	
	private RepackagingUpdateByResourceSummaryWindow parentWindow;
	private String type;
	
	private double oldTotalAmount;
	private double newTotalAmount;
	
	private GlobalSectionController globalSectionController;
		
	private Panel mainPanel;
	private Panel topPanel;
	private Panel bottomPanel;
	private GridPanel topGridPanel;
	private EditorGridPanel bottomEditGridPanel;
	
	//data store
	private Store oldDataStore;
	private Store newDataStore;
	
	private final RecordDef resourceSummaryRecordDef = new RecordDef(
			new FieldDef[]{
					new StringFieldDef("id"),
					new StringFieldDef("packageNo"),
					new StringFieldDef("objectCode"),
					new StringFieldDef("subsidiaryCode"),
					new StringFieldDef("resourceDescription"),
					new StringFieldDef("unit"),
					new StringFieldDef("quantity"),
					new StringFieldDef("rate"),
					new StringFieldDef("amount"),
					new StringFieldDef("resourceType"),
					new BooleanFieldDef("excludeLevy"),
					new BooleanFieldDef("excludeDefect")
			});
	
	private ComboBox unitComboBox;
	private ComboBox packageComboBox;
	private Record selectedPackageRecord;
	private List<String> packagesToBeReset = new ArrayList<String>();
	private Record editedRecord;
	private GlobalMessageTicket globalMessageTicket;
	
	public MergeOrSplitBQResourceSummaryWindow(final RepackagingUpdateByResourceSummaryWindow repackagingWindow, final String type){
		super();
		this.setModal(true);
		this.parentWindow = repackagingWindow;
		this.type = type; //Type is MERGE or SPLIT
		this.globalSectionController = parentWindow.getGlobalSectionController();
		globalMessageTicket = new GlobalMessageTicket();
		if(type.equals(MERGE))
			this.setTitle("Merge Resources");
		else
			this.setTitle("Split Resource");
		this.setPaddings(5);
		this.setWidth(1000);
		this.setHeight(500);
		this.setClosable(false);
		this.setLayout(new FitLayout());
		this.setId(WINDOW_ID);
		
		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setId(MAIN_PANEL_ID);
		topPanel = new Panel();
		topPanel.setLayout(new RowLayout());
		bottomPanel = new Panel();
		bottomPanel.setLayout(new RowLayout());
				
		Renderer quantityRenderer = new QuantityRenderer(globalSectionController.getUser());
		Renderer amountRenderer = new AmountRendererCheckIfTotal(globalSectionController.getUser());
		Renderer rateRenderer = new RateRenderer(globalSectionController.getUser());
		
		topGridPanel = new GridPanel();
		bottomEditGridPanel = new EditorGridPanel();
		
		unitComboBox = new ComboBox();
		
		Store unitStore = globalSectionController.getUnitStore();
		if(unitStore == null)
			unitStore = new SimpleStore(new String[]{"unitCode", "description"}, new String[][]{});
		unitComboBox.setDisplayField("description");
		unitComboBox.setValueField("unitCode");
		unitComboBox.setSelectOnFocus(true);
		unitComboBox.setForceSelection(true);
		unitComboBox.setListWidth(200);
		unitStore.load();
		unitComboBox.setStore(unitStore);
		
		packageComboBox = new ComboBox();
		Store packageStore = globalSectionController.getUnawardedPackageStore();
		if(packageStore == null)
			packageStore = new SimpleStore(new String[]{"packageNo", "description"}, new String[][]{});
		packageComboBox.setDisplayField("description");
		packageComboBox.setValueField("packageNo");
		packageComboBox.setSelectOnFocus(true);
		packageComboBox.setForceSelection(true);
		packageComboBox.setListWidth(200);
		packageStore.load();
		packageComboBox.setStore(packageStore);
		packageComboBox.addListener(new ComboBoxListenerAdapter(){
			public void onSelect(ComboBox comboBox, Record record, int index){
				selectedPackageRecord = record;
			}
		});
		
		User user = globalSectionController.getUser();
		Map<String, String> prefs = user.getGeneralPreferences();
		String quantDP = prefs.get(GeneralPreferencesKey.QUANTITY_DECIMAL_PLACES);
		if(quantDP == null)
			quantDP = "1";
		String rateDP = prefs.get(GeneralPreferencesKey.RATE_DECIMAL_PLACES);
		if(rateDP == null)
			rateDP = "2";
		NumberField quantNumberField = new NumberField();
		quantNumberField.setDecimalPrecision(Integer.valueOf(quantDP));
		NumberField rateNumberField = new NumberField();
		rateNumberField.setDecimalPrecision(Integer.valueOf(rateDP));
		
		Renderer checkedRenderer = new Renderer(){
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				
				if(record.getAsBoolean("isTotal")){
					return "";
				}

				boolean checked = ((Boolean) value).booleanValue();  
				return checked ? "Y" : "N";
			}
		};
		
		//Configure column configs
		ColumnConfig packageNoColConfig = new ColumnConfig("Package No.", "packageNo", 75, false);
		packageNoColConfig.setEditor(new GridEditor(packageComboBox));
		ColumnConfig objectCodeColConfig = new ColumnConfig("Object Code", "objectCode", 75, false);
		objectCodeColConfig.setEditor(new GridEditor(new TextField()));
		ColumnConfig subsidiaryCodeColConfig = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 65, false);
		subsidiaryCodeColConfig.setEditor(new GridEditor(new TextField()));
		ColumnConfig resourceDescriptionColConfig = new ColumnConfig("Resource Description", "resourceDescription", 200, false);
		resourceDescriptionColConfig.setEditor(new GridEditor(new TextField()));
		ColumnConfig unitColConfig = new ColumnConfig("Unit", "unit", 30, false);
		unitColConfig.setEditor(new GridEditor(unitComboBox));
		ColumnConfig quantityColConfig = new ColumnConfig("Quantity", "quantity", 110, false);
		quantityColConfig.setRenderer(quantityRenderer);
		quantityColConfig.setAlign(TextAlign.RIGHT);
		quantityColConfig.setEditor(new GridEditor(quantNumberField));
		ColumnConfig rateColConfig = new ColumnConfig("Rate", "rate", 70, false);
		rateColConfig.setRenderer(rateRenderer);
		rateColConfig.setAlign(TextAlign.RIGHT);
		rateColConfig.setEditor(new GridEditor(rateNumberField));
		ColumnConfig amountColConfig = new ColumnConfig("Amount", "amount", 110, false);
		amountColConfig.setRenderer(amountRenderer);
		amountColConfig.setAlign(TextAlign.RIGHT);
		ColumnConfig resourceTypeColConfig = new ColumnConfig("Type", "resourceType", 40, false);
		ColumnConfig excludeLevyColConfig = new ColumnConfig("Levy Excluded", "excludeLevy", 80, false);
		excludeLevyColConfig.setRenderer(checkedRenderer);
//		excludeLevyColConfig.setTooltip("Resource IV movement is excluded from levy provisions");
		ColumnConfig excludeDefectColConfig = new ColumnConfig("Defect Excluded", "excludeDefect", 100, false);
		excludeDefectColConfig.setRenderer(checkedRenderer);
//		excludeDefectColConfig.setTooltip("Resource IV movement is excluded from defect provisions");
		
		bottomEditGridPanel.addGridCellListener(new GridCellListenerAdapter(){
			public void onCellClick(GridPanel grid, int rowIndex, 
					int colIndex, EventObject e){
				globalMessageTicket.refresh();
				String col = grid.getColumnModel().getDataIndex(colIndex);
				if(col.equals("excludeLevy") || col.equals("excludeDefect")){
					Record rec = newDataStore.getAt(rowIndex);
					boolean checked = rec.getAsBoolean(col);
					rec.set(col, !checked);
				}
			}
		});
		
		ColumnConfig[] columns = new ColumnConfig[]{
				packageNoColConfig,
				objectCodeColConfig,
				subsidiaryCodeColConfig,
				resourceDescriptionColConfig,
				unitColConfig,
				quantityColConfig,
				rateColConfig,
				amountColConfig,
				resourceTypeColConfig,
				excludeLevyColConfig,
				excludeDefectColConfig
		};
		
		
		
		topGridPanel.setColumnModel(new ColumnModel(columns));
		bottomEditGridPanel.setColumnModel(new ColumnModel(columns));
		
		//Configure datastores
		MemoryProxy oldProxy = new MemoryProxy(new Object[][]{});
		ArrayReader oldReader = new ArrayReader(resourceSummaryRecordDef);
		oldDataStore = new Store(oldProxy, oldReader);
		oldDataStore.load();
		topGridPanel.setStore(oldDataStore);
		MemoryProxy newProxy = new MemoryProxy(new Object[][]{});
		ArrayReader newReader = new ArrayReader(resourceSummaryRecordDef);
		newDataStore = new Store(newProxy, newReader);
		newDataStore.load();
		bottomEditGridPanel.setStore(newDataStore);
		
		GridView topView = new CustomizedGridView();
		topView.setAutoFill(true);
//		topView.setForceFit(true);
		topGridPanel.setView(topView);
		GridView bottomView = new CustomizedGridView();
		bottomView.setAutoFill(true);
//		bottomView.setForceFit(true);
		bottomEditGridPanel.setView(bottomView);
		
		//Disable drag and drop of columns
		topGridPanel.setEnableColumnMove(false);
		bottomEditGridPanel.setEnableColumnMove(false);		
		
		//if split, make top panel short, and add button to bottom panel. if merge, make bottom panel short
		if(type.equals(SPLIT)){
			topPanel.setHeight(150);
			
			Toolbar toolbar = new Toolbar();
			
			ToolbarButton addResourceButton = new ToolbarButton();
			addResourceButton.setText("Add Resource");
			addResourceButton.setCls("toolbar-button");
			addResourceButton.addListener(new ButtonListenerAdapter(){
				public void onClick(Button button, EventObject e){
					globalMessageTicket.refresh();
					addEmptyResource();
				}
			});
			
			toolbar.addButton(addResourceButton);
			bottomPanel.setTopToolbar(toolbar);
		}
		else{
			bottomPanel.setHeight(150);
		}
		//add grids to panels to panel to this.
		topPanel.add(topGridPanel);
		bottomPanel.add(bottomEditGridPanel);
		mainPanel.add(topPanel);
		mainPanel.add(bottomPanel);
		this.add(mainPanel);
		
		//EditorGridListener - update amount field when rate/qty are changed
		bottomEditGridPanel.addEditorGridListener(new EditorGridListener(){
			public boolean doBeforeEdit(GridPanel grid, Record record,
					String field, Object value, int rowIndex, int colIndex) {
				globalMessageTicket.refresh();
				if(rowIndex == (newDataStore.getCount()-1))
					return false;
				if(field.equals("packageNo") && !(record.getAsString("objectCode").startsWith("14") || record.getAsString("objectCode").startsWith("13"))){
					MessageBox.alert("Only resources with an object code beginning with '14' can be added to a package.<br/>Resources with an object code beginning with '13' can be removed from the package.");
					return false;
				}
				return true;
			}

			public boolean doValidateEdit(GridPanel grid, Record record,
					String field, Object value, Object originalValue,
					int rowIndex, int colIndex) {
				editedRecord = record;
				if(field.equals("objectCode")){
					if(value == null || ((String)value).length() != 6){
						MessageBox.alert("Object code must be 6 digits in length");
						return false;
					}
					if(record.getAsString("packageNo") != null && record.getAsString("packageNo").length() > 1 && !((String)value).startsWith("14")){
						MessageBox.alert("Resources in a package must have an object code beginning with '14'");
						return false;
					}
				}
				if(field.equals("subsidiaryCode") && (value == null || ((String)value).length() != 8)){
					MessageBox.alert("Subsidiary code must be 8 digits in length");
					return false;
				}
				if(field.equalsIgnoreCase("quantity") || field.equalsIgnoreCase("rate")){
					double quantity = record.getAsDouble("quantity");
					double rate = record.getAsDouble("rate");
					if(field.equalsIgnoreCase("quantity"))
						quantity = Double.parseDouble(value.toString());
					else
						rate = Double.parseDouble(value.toString());
					double amount = quantity*rate;
					if(oldTotalAmount != 0 && amount/oldTotalAmount < 0){
						MessageBox.alert("Amounts must have the same sign (+/-) as the original record(s)");
						return false;	
					}
					else if(oldTotalAmount == 0 && amount != 0){
						MessageBox.alert("Original amount is 0");
						return false;
					}
					else{
						double oldAmount = record.getAsDouble("amount");
						record.set("amount", Double.toString(amount));
						newTotalAmount += amount - oldAmount;
						newDataStore.getAt(newDataStore.getCount()-1).set("amount", Double.toString(newTotalAmount));
					}
				}
				else if(field.equals("packageNo")){
					if(value != null && ((String)value).trim().length() > 1){
						if(!record.getAsString("objectCode").startsWith("14") && !record.getAsString("objectCode").startsWith("13")){
							MessageBox.alert(	"To assign a Resource to a package, Object Code must begin with '14'./n" +
												"To reset a Resource, Object Code must begin with '13'.");
							return false;
						}
						else if(record.getAsString("objectCode").startsWith("13")){
							MessageBox.alert("A resource with object code beginning with '13' can only be removed from the package(assign to a blank package).");
							return false;
						}
					}
					if(value != null && ((String)value).startsWith("3") && !record.getAsString("subsidiaryCode").startsWith("3")){
						MessageBox.alert("Resources in NSC packages (beginning with '3') must have a subsidiary code beginning with '3'");
						return false;
					}
					if((selectedPackageRecord.getAsString("status").equals("160") || selectedPackageRecord.getAsString("status").equals("340")) &&
							!packagesToBeReset.contains(selectedPackageRecord.getAsString("packageNo"))){
						MessageBox.confirm("Warning", "Tender Analysis for this package will be reset when you save these changes. Press 'Yes' to continue, or 'No' to cancel.", new MessageBox.ConfirmCallback(){
									public void execute(String btnID) {
										if(btnID.equals("yes")){
											packagesToBeReset.add(selectedPackageRecord.getAsString("packageNo"));
											editedRecord.set("packageNo", selectedPackageRecord.getAsString("packageNo"));
										}									
									}	
						});
						return false;
					}
				}
				return true;
			}
			
			public void onAfterEdit(GridPanel grid, Record record,
					String field, Object newValue, Object oldValue,
					int rowIndex, int colIndex) {
			}
		});
		
		Button saveButton = new Button(type);
		saveButton.addListener(new ButtonListenerAdapter(){
				public void onClick(Button button, com.gwtext.client.core.EventObject e) {
					globalMessageTicket.refresh();
					// added by brian on 20110321
					UIUtil.maskPanelById(WINDOW_ID, GlobalParameter.LOADING_MSG, true);
					bottomEditGridPanel.stopEditing();
					if(RoundingUtil.round(oldTotalAmount, 4) != RoundingUtil.round(newTotalAmount, 4)){
						UIUtil.unmaskPanelById(WINDOW_ID);
						MessageBox.alert("Total amounts must match!");
						return;
					}
					if(type.equals(SPLIT) && newDataStore.getCount() < 3){
						UIUtil.unmaskPanelById(WINDOW_ID);
						MessageBox.alert("Please split into at least 2 new resources");
						return;
					}
					parentWindow.saveSplitMergeResources(oldDataStore.getRange(0, oldDataStore.getCount()-2), newDataStore.getRange(0, newDataStore.getCount()-2));
				};
		});		
		this.addButton(saveButton);
		
		Button closeWindowButton = new Button("Close");
		closeWindowButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, com.gwtext.client.core.EventObject e) {
					parentWindow.closeChildWindow();				
				};
		});		
		this.addButton(closeWindowButton);
	}
	
	public void populateGrid(Record[] records){
		if(records.length == 0)
			return;
		
		oldDataStore.removeAll();
		oldTotalAmount = 0;
		for(Record record: records){
			oldTotalAmount += record.getAsDouble("amount");
			oldDataStore.add(record);
		}
		Record oldAmountRecord = resourceSummaryRecordDef.createRecord(new Object[]{null,"","","","","","","",oldTotalAmount,null, null, null});
		oldAmountRecord.set("isTotal", true);
		oldDataStore.add(oldAmountRecord);
		
		newDataStore.removeAll();
		newTotalAmount = 0;
		Record newRecord = resourceSummaryRecordDef.createRecord(new Object[]{null,"","","","","","0","0","0",records[0].getAsString("resourceType"),null, null});
		if(type.equals(SPLIT)){
			Record record = records[0];
			newRecord.set("packageNo", record.getAsString("packageNo"));
			newRecord.set("objectCode", record.getAsString("objectCode"));
			newRecord.set("subsidiaryCode", record.getAsString("subsidiaryCode"));
			newRecord.set("resourceDescription", record.getAsString("resourceDescription"));
			newRecord.set("unit", record.getAsString("unit"));
			newRecord.set("excludeLevy", record.getAsBoolean("excludeLevy"));
			newRecord.set("excludeDefect", record.getAsBoolean("excludeDefect"));
		}	
		newDataStore.add(newRecord);
		Record newAmountRecord = resourceSummaryRecordDef.createRecord(new Object[]{null,"","","","","","","",newTotalAmount,null, null, null});
		newAmountRecord.set("isTotal", true);
		newDataStore.add(newAmountRecord);

	}
	
	public void addEmptyResource(){
		Record record = oldDataStore.getAt(0);
		Record newRecord = resourceSummaryRecordDef.createRecord(new Object[]{null,"","","","","","0","0","0",record.getAsString("resourceType"),record.getAsString("excludeLevy"), record.getAsString("excludeDefect")});
		newRecord.set("packageNo", record.getAsString("packageNo"));
		newRecord.set("objectCode", record.getAsString("objectCode"));
		newRecord.set("subsidiaryCode", record.getAsString("subsidiaryCode"));
		newRecord.set("resourceDescription", record.getAsString("resourceDescription"));
		newRecord.set("unit", record.getAsString("unit"));
		newDataStore.insert(newDataStore.getCount()-1, newRecord);
		newDataStore.commitChanges();	
	}
}
