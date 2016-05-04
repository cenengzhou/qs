package com.gammon.qs.client.ui.window.mainSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.gammon.qs.application.GeneralPreferencesKey;
import com.gammon.qs.application.User;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRendererCheckIfTotal;
import com.gammon.qs.client.ui.renderer.NonTotalEditableColRenderer;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
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
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
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
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;

public class AddBQResourceSummaryWindow extends Window {
	Logger logger = Logger.getLogger(getClass().getName());
	// added by brian on 20110401
	public static final String WINDOW_ID = "AddBQResourceSummaryWindow";
	public static final String MAIN_PANEL_ID ="addResourceSummaryMainPanel"; 
	
	private RepackagingUpdateByResourceSummaryWindow parentWindow;
	private GlobalSectionController globalSectionController;
	private GlobalMessageTicket globalMessageTicket;
		
	private Panel mainPanel;
	private EditorGridPanel editorGridPanel;
	
	//data store
	private Store dataStore;
	
	private ColumnConfig packageNoColConfig;
	private ColumnConfig objectCodeColConfig;
	private ColumnConfig subsidiaryCodeColConfig;
	private ColumnConfig resourceDescriptionColConfig;
	private ColumnConfig unitColConfig;
	private ColumnConfig quantityColConfig;
	private ColumnConfig rateColConfig;
	private ColumnConfig amountColConfig;
	private ColumnConfig resourceTypeColConfig;
	private ColumnConfig excludeLevyColConfig;
	private ColumnConfig excludeDefectColConfig;
	
	private final RecordDef addResourceSummaryRecordDef = new RecordDef(
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
	private ComboBox resourceTypeComboBox;
	private ComboBox exclusiveComboBox;
	private Record selectedPackageRecord;
	private List<String> packagesToBeReset = new ArrayList<String>();
	
	public AddBQResourceSummaryWindow(final RepackagingUpdateByResourceSummaryWindow parentWindow){
		super();
		globalMessageTicket = new GlobalMessageTicket();
		this.setModal(true);
		this.parentWindow = parentWindow;
		this.globalSectionController = parentWindow.getGlobalSectionController();
		
		this.setTitle("Add Resource");
		this.setPaddings(5);
		this.setWidth(1000);
		this.setHeight(150);
		this.setClosable(false);
		this.setLayout(new FitLayout());
		// added by brian on 20110322
		this.setId(WINDOW_ID);
		
		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setId(MAIN_PANEL_ID);
				
		Renderer quantityRenderer = new QuantityRenderer(globalSectionController.getUser());
		Renderer amountRenderer = new AmountRendererCheckIfTotal(globalSectionController.getUser());
		Renderer rateRenderer = new RateRenderer(globalSectionController.getUser());
		
		editorGridPanel = new EditorGridPanel();
		
		unitComboBox = new ComboBox();
		
		Store unitStore = globalSectionController.getUnitStore();
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
		packageComboBox.setStore(packageStore);
		packageComboBox.addListener(new ComboBoxListenerAdapter(){
			public void onSelect(ComboBox comboBox, Record record, int index){
				selectedPackageRecord = record;
			}
		});
		
		resourceTypeComboBox = new ComboBox();
		Store resourceTypeStore = new SimpleStore("resourceType", new String[]{"VO", "OI"});
		resourceTypeComboBox.setStore(resourceTypeStore);
		resourceTypeComboBox.setDisplayField("resourceType");
		resourceTypeComboBox.setValueField("resourceType");
		resourceTypeComboBox.setForceSelection(true);
		
		exclusiveComboBox = new ComboBox();
		Store exclusiveStore = new SimpleStore("exclusive", new String[]{"Levy Exclusive", "Defect Exclusive"});
		exclusiveComboBox.setStore(exclusiveStore);
		exclusiveComboBox.setDisplayField("exclusive");
		exclusiveComboBox.setValueField("exclusive");
		exclusiveComboBox.setForceSelection(true);
		exclusiveComboBox.setListWidth(150);
		
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
				boolean checked = ((Boolean) value).booleanValue();  
				return checked ? "Y" : "N";
			}
		};
		
		packageNoColConfig = new ColumnConfig("Package No.", "packageNo", 75, false);
		packageNoColConfig.setEditor(new GridEditor(packageComboBox));
		objectCodeColConfig = new ColumnConfig("Object Code", "objectCode", 75, false);
		objectCodeColConfig.setEditor(new GridEditor(new TextField()));
		subsidiaryCodeColConfig = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 65, false);
		subsidiaryCodeColConfig.setEditor(new GridEditor(new TextField()));
		resourceDescriptionColConfig = new ColumnConfig("Resource Description", "resourceDescription", 200, false);
		resourceDescriptionColConfig.setEditor(new GridEditor(new TextField()));
		unitColConfig = new ColumnConfig("Unit", "unit", 30, false);
		unitColConfig.setEditor(new GridEditor(unitComboBox));
		
		quantityColConfig = new ColumnConfig("Quantity", "quantity", 110, false);
		Field quantyField = FieldFactory.createNegativeNumberField(3);
		quantityColConfig.setRenderer(new NonTotalEditableColRenderer(quantityRenderer));
		quantityColConfig.setAlign(TextAlign.RIGHT);
		quantityColConfig.setEditor(new GridEditor(quantyField));
	
		
		quantityColConfig.setEditor(new GridEditor(quantNumberField));
		rateColConfig = new ColumnConfig("Rate", "rate", 70, false);
		Field rateField = FieldFactory.createNegativeNumberField(3);
		rateColConfig.setRenderer(new NonTotalEditableColRenderer(rateRenderer));
		rateColConfig.setAlign(TextAlign.RIGHT);
		rateColConfig.setEditor(new GridEditor(rateField));
		
		rateColConfig.setEditor(new GridEditor(rateNumberField));
		amountColConfig = new ColumnConfig("Amount", "amount", 110, false);
		amountColConfig.setRenderer(amountRenderer);
		amountColConfig.setAlign(TextAlign.RIGHT);
		resourceTypeColConfig = new ColumnConfig("Type", "resourceType", 40, false);
		resourceTypeColConfig.setEditor(new GridEditor(resourceTypeComboBox));
		excludeLevyColConfig = new ColumnConfig("Levy Excluded", "excludeLevy", 80, false);
		excludeLevyColConfig.setRenderer(checkedRenderer);
		excludeLevyColConfig.setTooltip("Resource IV movement is excluded from levy provisions");
		excludeDefectColConfig = new ColumnConfig("Defect Excluded", "excludeDefect", 100, false);
		excludeDefectColConfig.setRenderer(checkedRenderer);
		excludeDefectColConfig.setTooltip("Resource IV movement is excluded from defect provisions");
		
		editorGridPanel.addGridCellListener(new GridCellListenerAdapter(){
			public void onCellClick(GridPanel grid, int rowIndex, 
					int colIndex, EventObject e){
				globalMessageTicket.refresh();
				String col = grid.getColumnModel().getDataIndex(colIndex);
				if(col.equals("excludeLevy") || col.equals("excludeDefect")){
					Record rec = dataStore.getAt(rowIndex);
					boolean checked = rec.getAsBoolean(col);
					rec.set(col, !checked);
				}
			}
		});

		
		//Create empty record to edit and add.
		MemoryProxy proxy = new MemoryProxy(new Object[][]{new Object[]{null,"","","","","","0","0","0"}});
		ArrayReader reader = new ArrayReader(addResourceSummaryRecordDef);
		this.dataStore = new Store(proxy, reader);
		this.dataStore.load();
		editorGridPanel.setStore(this.dataStore);
		
		BaseColumnConfig[] columns = new BaseColumnConfig[]{
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
		
		editorGridPanel.setColumnModel(new ColumnModel(columns));
		
		GridView view = new CustomizedGridView();
		view.setAutoFill(true);
		view.setForceFit(true);
		editorGridPanel.setView(view);
		
		mainPanel.add(editorGridPanel);
		this.add(mainPanel);
		
		//EditorGridListener - update amount field when rate/qty are changed
		editorGridPanel.addEditorGridListener(new EditorGridListener(){
			public boolean doBeforeEdit(GridPanel grid, Record record,
					String field, Object value, int rowIndex, int colIndex) {
				globalMessageTicket.refresh();
				if(field.equals("packageNo") && (record.getAsString("objectCode") == null || !record.getAsString("objectCode").startsWith("14"))){
					MessageBox.alert("Only items with an object code beginning with '14' can be added to a package.");
					return false;
				}
				return true;
			}

			public boolean doValidateEdit(GridPanel grid, final Record record,
					final String field, final Object value, Object originalValue,
					int rowIndex, int colIndex) {
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
				if(field.equals("packageNo") && (selectedPackageRecord.getAsString("status").equals("160") || selectedPackageRecord.getAsString("status").equals("340")) && !packagesToBeReset.contains(selectedPackageRecord.getAsString("packageNo"))){
					MessageBox.confirm("Warning", "Tender Analysis for this package will be reset when you save these changes. Press 'Yes' to continue, or 'No' to cancel.", new MessageBox.ConfirmCallback(){
								public void execute(String btnID) {
									if(btnID.equals("yes")){
										packagesToBeReset.add(selectedPackageRecord.getAsString("packageNo"));
										record.set(field, value);
									}									
								}	
					});
					return false;
				}
				return true;
			}

			public void onAfterEdit(GridPanel grid, Record record,
					String field, Object newValue, Object oldValue,
					int rowIndex, int colIndex) {
				if(field.equalsIgnoreCase("quantity") || field.equalsIgnoreCase("rate")){
					Double quantity = record.getAsDouble("quantity");
					Double rate = record.getAsDouble("rate");
					Double amount = quantity*rate;
					logger.info("Edited rate or quantity. New amount = " + amount);
					record.set("amount", amount.toString());
				}
			}
		});
		
		Button addRecordButton = new Button("Add");
		addRecordButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, EventObject e) {
					globalMessageTicket.refresh();
					editorGridPanel.stopEditing();
					UIUtil.maskPanelById(WINDOW_ID, GlobalParameter.LOADING_MSG, true);
					Record rec = dataStore.getAt(0);
					StringBuffer sb = new StringBuffer();
					if(rec.getAsString("objectCode") == null || rec.getAsString("objectCode").trim().length() != 6)
						sb.append("Object code must be 6 digits in length<br/>");
					if(rec.getAsString("subsidiaryCode") == null || rec.getAsString("subsidiaryCode").trim().length() != 8)
						sb.append("Subsidiary code must be 8 digits in length<br/>");
					if(rec.getAsString("resourceDescription") == null || rec.getAsString("resourceDescription").trim().length() == 0)
						sb.append("Description must not be blank<br/>");
					if(rec.getAsString("unit") == null || rec.getAsString("unit").length() == 0)
						sb.append("Unit must not be blank<br/>");
					

					boolean isVO = false;
					if(rec.getAsString("subsidiaryCode").startsWith("4") && "80".equals(rec.getAsString("subsidiaryCode").substring(2, 4)))
						isVO = true;
					if(rec.getAsString("resourceType") == null)
						sb.append("Resource type must not be blank");
					else if(rec.getAsString("resourceType").equals("VO") && !isVO)
						sb.append("VO must have the subsidiary code which starts with [4X80XXXX]");
					
					if(sb.length() != 0){
						UIUtil.unmaskPanelById(WINDOW_ID);
						MessageBox.alert(sb.toString());
						return;
					}
					createNewResource();
				};
		});
		this.addButton(addRecordButton);
		
		Button closeWindowButton = new Button("Close");
		closeWindowButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, EventObject e) {
					parentWindow.closeChildWindow();				
				};
		});		
		this.addButton(closeWindowButton);
	}
	
	public void createNewResource(){
		parentWindow.addResourceSummary(dataStore.getAt(0));
	}

}
