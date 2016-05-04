package com.gammon.qs.client.ui.window.mainSection;

import java.util.ArrayList;
import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.ui.ArrowKeyNavigation;
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
import com.gammon.qs.domain.BQResourceSummary;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.BQResourceSummaryWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.gwtext.client.widgets.MessageBoxConfig;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.CheckboxColumnConfig;
import com.gwtext.client.widgets.grid.CheckboxSelectionModel;
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
import com.gwtext.client.widgets.layout.TableLayout;
import com.gwtext.client.widgets.layout.TableLayoutData;

public class RepackagingUpdateByResourceSummaryWindow extends Window {
	// added by brian on 20110321
	private static final String WINDOW_ID = "RepackagingUpdateByResourceSummaryWindow";
	private static final String MAIN_PANEL_ID ="repackagingEnquiryPanel"; 
	
	//UI
	private GlobalSectionController globalSectionController;
	private RepackagingListGridPanel repackagingListGridPanel;
	private GlobalMessageTicket globalMessageTicket;
	private Panel mainPanel;
	private Panel searchPanel;
	private Panel resultPanel;
	private EditorGridPanel resultEditorGridPanel;
	
	//Pagination
	private PaginationToolbar paginationToolbar;
	
	private List<String> accessRightsList;
	
	//data store
	private Store dataStore;
	
	private Window childWindow;
	private final CheckboxSelectionModel cbSelectionModel;   
	
	private TextField packageNoTextField;
	private TextField objectCodeTextField;
	private TextField subsidiaryCodeTextField;
	private TextField descriptionTextField;
	
	private ComboBox typeComboBox;
	private ComboBox levyExcludedComboBox;
	private ComboBox defectExcludedComboBox;
	
	private ToolbarButton addButton;
	private ToolbarButton mergeButton;
	private ToolbarButton splitButton;
	private ToolbarButton deleteVoButton;
	private Button updateButton;
	
	// cursor navigation
	private ArrowKeyNavigation arrowKeyNavigation;
	
	private ColumnConfig packageNoColConfig;
	private ColumnConfig objectCodeColConfig;
	private ColumnConfig subsidiaryCodeColConfig;
	private ColumnConfig resourceDescriptionColConfig;
	private ColumnConfig unitColConfig;
	private ColumnConfig quantityColConfig;
	private ColumnConfig rateColConfig;
	private ColumnConfig amountColConfig;
	private ColumnConfig postedIVColConfig;
	private ColumnConfig resourceTypeColConfig;
	private ColumnConfig excludeLevyColConfig;
	private ColumnConfig excludeDefectColConfig;
	
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
					new StringFieldDef("postedIVAmount"),
					new StringFieldDef("resourceType"),
					new BooleanFieldDef("excludeLevy"),
					new BooleanFieldDef("excludeDefect"),
					new StringFieldDef("cumIVAmount")
			});
	
	private Store packageStore;
	private List<String> uneditablePackageNos;
	//private List<String> uneditableUnawardedPackageNos;
	//private List<String> uneditableResourceSummaryIDs;
	
	private ComboBox unitComboBox;
	private ComboBox packageComboBox;
	private Record selectedPackageRecord;
	private List<String> packagesToBeReset = new ArrayList<String>();
	
	//Search parameters
	private String packageNo;
	private String objectCode;
	private String subsidiaryCode;
	private String description;
	private String type;
	private String levyExcluded;
	private String defectExcluded;
	
	private ToolbarTextItem totalAmountTextItem = new ToolbarTextItem("<b>0 Records match search criteria</b>");
	private ToolbarTextItem totalBudgetTextItem = new ToolbarTextItem("<b>Total Budget: 0</b>");
	private ToolbarTextItem totalSellingValueTextItem = new ToolbarTextItem("<b>Total Selling Value: 0</b>");
	
	private String lastEditedId;
	private Long repackagingEntryId;
	
	public RepackagingUpdateByResourceSummaryWindow(final RepackagingListGridPanel repackagingListGridPanel, Long repackagingEntryId){
		super();
		this.setModal(true);
		this.repackagingListGridPanel = repackagingListGridPanel;
		this.globalSectionController = repackagingListGridPanel.getGlobalSectionController();
		this.repackagingEntryId = repackagingEntryId;
		this.setArrowKeyNavigation(new ArrowKeyNavigation(resultEditorGridPanel));
		cbSelectionModel = new CheckboxSelectionModel();
		globalMessageTicket = new GlobalMessageTicket();
		
        setupUI();
        globalSectionController.setCurrentWindow(this);
        globalSectionController.getCurrentWindow().show();
 
        globalSectionController.refreshUneditablePackageNos();
		//globalSectionController.refreshUneditableUnawardedPackageNos();
		//globalSectionController.refreshUneditableResourceSummariesID();
	}
	
	private void setupUI(){
		setTitle("Repackaging Enquiry");
		setPaddings(5);
		setWidth(1280);
		setHeight(650);
		setClosable(false);
		setLayout(new FitLayout());
		setId(WINDOW_ID);
		
		
		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setId(MAIN_PANEL_ID);
		
		//search Panel
		searchPanel = new Panel();
		searchPanel.setPaddings(3);
		searchPanel.setFrame(true);
		searchPanel.setHeight(50);
		searchPanel.setLayout(new TableLayout(15));
		
		FieldListener searchListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER)
					search();
			}
		};
		
		Label packageNoLabel = new Label("Package No.");
		packageNoLabel.setCls("table-cell");
		searchPanel.add(packageNoLabel);
		packageNoTextField = new TextField("Package No.", "packageNo", 100);
		packageNoTextField.setCtCls("table-cell");
		packageNoTextField.setCls("table-cell");
		packageNoTextField.addListener(searchListener);
		packageNoTextField.setWidth(100);
		searchPanel.add(packageNoTextField);
		
		Label objectCodeLabel = new Label("Object Code");
		objectCodeLabel.setCls("table-cell");
		searchPanel.add(objectCodeLabel);
		objectCodeTextField = new TextField("Object Code", "objectCode", 100);
		objectCodeTextField.setCtCls("table-cell");
		objectCodeTextField.setCls("table-cell");
		objectCodeTextField.addListener(searchListener);
		objectCodeTextField.setWidth(100);
		searchPanel.add(objectCodeTextField);
		
		Label subsidiaryCodeLabel = new Label("Subsidiary Code");
		subsidiaryCodeLabel.setCls("table-cell");
		searchPanel.add(subsidiaryCodeLabel);
		subsidiaryCodeTextField = new TextField("Subsidiary Code", "subsidiaryCode", 100);
		subsidiaryCodeTextField.setCtCls("table-cell");
		subsidiaryCodeTextField.setCls("table-cell");
		subsidiaryCodeTextField.addListener(searchListener);
		subsidiaryCodeTextField.setWidth(100);
		searchPanel.add(subsidiaryCodeTextField);
		
		Label descriptionLabel = new Label("Description");
		descriptionLabel.setCls("table-cell");
		searchPanel.add(descriptionLabel);
		descriptionTextField = new TextField("Description", "description", 100);
		descriptionTextField.setCtCls("table-cell");
		descriptionTextField.setCls("table-cell");
		descriptionTextField.addListener(searchListener);
		descriptionTextField.setWidth(100);
		searchPanel.add(descriptionTextField);
		
		Label typeLabel = new Label("Type");
		typeLabel.setCls("table-cell");
		searchPanel.add(typeLabel);
		typeComboBox = FieldFactory.createComboBox();
		typeComboBox.setCtCls("table-cell");
		typeComboBox.addListener(searchListener);
		typeComboBox.setWidth(55);
		searchPanel.add(typeComboBox);
		
		setStaticComboBoxOption(typeComboBox, new String[][]{
				{"", "ALL"},
				{"blank","Blank"},
				{"VO","VO"},
				{"OI","OI"}
		});

		Label levyExcludedLabel = new Label("Levy Excluded");
		levyExcludedLabel.setCls("table-cell");
		searchPanel.add(levyExcludedLabel);
		levyExcludedComboBox = FieldFactory.createComboBox();
		levyExcludedComboBox.setCtCls("table-cell");
		levyExcludedComboBox.addListener(searchListener);
		searchPanel.add(levyExcludedComboBox);
		levyExcludedComboBox.setWidth(50);
		setStaticComboBoxOption(levyExcludedComboBox, new String[][]{
				{"", "ALL"},
				{"Y","Y"},
				{"N","N"}
		});

		
		Label defectExcludedLabel = new Label("Defect Excluded");
		defectExcludedLabel.setCls("table-cell");
		searchPanel.add(defectExcludedLabel);
		defectExcludedComboBox = FieldFactory.createComboBox();
		defectExcludedComboBox.setCtCls("table-cell");
		defectExcludedComboBox.addListener(searchListener);
		searchPanel.add(defectExcludedComboBox);
		defectExcludedComboBox.setWidth(50);
		setStaticComboBoxOption(defectExcludedComboBox, new String[][]{
				{"", "ALL"},
				{"Y","Y"},
				{"N","N"}
		});

		Button searchButton = new Button("Search");
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				globalMessageTicket.refresh();
				search();
			};
			
		});
		searchButton.setCls("table-cell");
		searchPanel.add(searchButton, new TableLayoutData(100));
		
		resultPanel = new Panel();
		resultPanel.setBorder(true);
		resultPanel.setFrame(true);
		resultPanel.setPaddings(2);
		resultPanel.setAutoScroll(true);
		resultPanel.setLayout(new FitLayout());
		
		resultEditorGridPanel =  new EditorGridPanel();
		Renderer amountRenderer = new AmountRendererCheckIfTotal(globalSectionController.getUser());
		Renderer rateRenderer = new RateRenderer(globalSectionController.getUser());

		//Normal Un-Editable Quantity Renderer with a special case that Subsidiary Code = 99019999 & Resource Description = 'resourceDescription' is editable
		final Renderer quantityRenderer = new QuantityRenderer(globalSectionController.getUser());
		Renderer editableQuantityRenderer = new Renderer() {			
			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				String str = quantityRenderer.render(value, cellMetadata, record, rowIndex, colNum, store);	
				if(record.getAsString("objectCode").equals("199999") && record.getAsString("subsidiaryCode").equals("99019999") && 
				   record.getAsString("resourceDescription").trim().equals("Genuine Markup of Change Order"))
					return "<span style=\"color:blue;\">" + str + "</span>";
				else
					return str;
			}
		};

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
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPackageRepository().getUnawardedPackageNos(
				globalSectionController.getJob(), new AsyncCallback<String[][]>(){
			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, false,globalSectionController.getUser());
			}
			public void onSuccess(String[][] unawardedPackageNos) {
				if(unawardedPackageNos == null)
					return;
				packageStore = new SimpleStore(new String[]{"packageNo", "description", "status"}, unawardedPackageNos);
				if(packageStore == null)
					packageStore = new SimpleStore(new String[]{"packageNo", "description", "status"}, new String[][]{});
				packageStore.load(); 
				packageComboBox.setDisplayField("description");
				packageComboBox.setValueField("packageNo");
				packageComboBox.setSelectOnFocus(true);
				packageComboBox.setForceSelection(true);
				packageComboBox.setListWidth(200);
				packageComboBox.setStore(packageStore);

			}
		});

		packageComboBox.addListener(new ComboBoxListenerAdapter(){
			public void onSelect(ComboBox comboBox, Record record, int index){
				selectedPackageRecord = record;
			}
		});
		
		Renderer checkedRenderer = new Renderer(){
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				boolean checked = ((Boolean) value).booleanValue();  
				return checked ? "Y" : "N";
			}
		};
		
		packageNoColConfig = new ColumnConfig("Package No.", "packageNo", 40, false);
		packageNoColConfig.setEditor(new GridEditor(packageComboBox));
		packageNoColConfig.setTooltip("Package No.");
		objectCodeColConfig = new ColumnConfig("Object Code", "objectCode", 50, false);
		objectCodeColConfig.setEditor(new GridEditor(new TextField()));
		objectCodeColConfig.setTooltip("Object Code");
		subsidiaryCodeColConfig = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 65, false);
		subsidiaryCodeColConfig.setEditor(new GridEditor(new TextField()));
		subsidiaryCodeColConfig.setTooltip("Subsidiary Code");
		resourceDescriptionColConfig = new ColumnConfig("Resource Description", "resourceDescription", 220, false);
		resourceDescriptionColConfig.setEditor(new GridEditor(new TextField()));
		resourceDescriptionColConfig.setTooltip("Resource Description");
		unitColConfig = new ColumnConfig("Unit", "unit", 30, false);
		unitColConfig.setEditor(new GridEditor(unitComboBox));
		unitColConfig.setTooltip("Unit");
		
		//Quantity Column is editable when Object Code = 199999, Subsidiary Code = 99019999 & Resource Description = "Genuine Markup of Change Order"
		Field quantityField = FieldFactory.createNegativeNumberField(3);
		quantityColConfig = new ColumnConfig("Quantity", "quantity", 110, false);
		quantityColConfig.setEditor(new GridEditor(quantityField));
		//quantityColConfig.setEditor(new GridEditor(new TextField()));
		quantityField.addListener(this.arrowKeyNavigation);
		quantityColConfig.setRenderer(editableQuantityRenderer);
		//quantityColConfig.setRenderer(new EditableColorRenderer(editableQuantityRenderer)); //Renderer that makes the whole column to be blue
		quantityColConfig.setAlign(TextAlign.RIGHT);
		quantityColConfig.setTooltip("Quantity");	
		
		rateColConfig = new ColumnConfig("Rate", "rate", 120, false);
		rateColConfig.setRenderer(rateRenderer);
		rateColConfig.setAlign(TextAlign.RIGHT);
		rateColConfig.setTooltip("Rate");
		amountColConfig = new ColumnConfig("Amount", "amount", 120, false);
		amountColConfig.setRenderer(amountRenderer);
		amountColConfig.setAlign(TextAlign.RIGHT);
		amountColConfig.setTooltip("Amount");
		postedIVColConfig = new ColumnConfig("Posted IV", "postedIVAmount", 120, false);
		postedIVColConfig.setRenderer(amountRenderer);
		postedIVColConfig.setAlign(TextAlign.RIGHT);
		postedIVColConfig.setTooltip("Posted IV");
		resourceTypeColConfig = new ColumnConfig("Type", "resourceType", 30, false);
		resourceTypeColConfig.setTooltip("Resource Type");
		excludeLevyColConfig = new ColumnConfig("Levy Excluded", "excludeLevy", 30, false);
		excludeLevyColConfig.setRenderer(checkedRenderer);
		excludeLevyColConfig.setTooltip("Resource IV movement is excluded from levy provisions");
		excludeDefectColConfig = new ColumnConfig("Defect Excluded", "excludeDefect", 30, false);
		excludeDefectColConfig.setRenderer(checkedRenderer);
		excludeDefectColConfig.setTooltip("Resource IV movement is excluded from defect provisions");

		resultEditorGridPanel.addGridCellListener(new GridCellListenerAdapter(){
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
		
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(resourceSummaryRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		resultEditorGridPanel.setStore(dataStore);
		
		BaseColumnConfig[] columns = new BaseColumnConfig[]{
				new CheckboxColumnConfig(cbSelectionModel),
				packageNoColConfig,
				objectCodeColConfig,
				subsidiaryCodeColConfig,
				resourceDescriptionColConfig,
				unitColConfig,
				quantityColConfig,
				rateColConfig,
				amountColConfig,
				postedIVColConfig,
				resourceTypeColConfig,
				excludeLevyColConfig,
				excludeDefectColConfig
		};
		
		resultEditorGridPanel.setColumnModel(new ColumnModel(columns));
		resultEditorGridPanel.setSelectionModel(cbSelectionModel);
				
		//Validation - check postedIVAmount and whether package status > 100 (packageNo in uneditablePackageNos)
		resultEditorGridPanel.addEditorGridListener(new EditorGridListener(){

			public boolean doBeforeEdit(GridPanel grid, Record record,
					String field, Object value, int rowIndex, int colIndex) {
				globalMessageTicket.refresh();
				String error = null;
				boolean isEditable = false;
				uneditablePackageNos = globalSectionController.getUneditablePackageNos();
				/*uneditableUnawardedPackageNos = globalSectionController.getUneditableUnawardedPackageNos();
				uneditableResourceSummaryIDs = globalSectionController.getUneditableResourceSummaries();*/
				if(record.getAsString("packageNo") != null && uneditablePackageNos.contains(record.getAsString("packageNo")) && record.getAsString("objectCode").startsWith("14"))
					error = "Selected field cannot be edited - this package is submitted or awarded";
				/*else if(record.getAsString("packageNo") != null && uneditableUnawardedPackageNos.contains(record.getAsString("packageNo")))
					error = "Selected field cannot be edited - this package has Payment Requisition submitted.";
				else if(record.getAsString("packageNo") != null && uneditableResourceSummaryIDs.contains(record.getAsString("id")))
					error = "Selected field cannot be edited - this resource is being used in Payment Requisition.";*/
				else if(field.equals("packageNo") && (!record.getAsString("objectCode").startsWith("14") && !record.getAsString("objectCode").startsWith("13")))
					error = "Only resources with an object code beginning with '14' can be added to a package.<br/>Resources with an object code beginning with '13' can be removed from the package.";
				else if(field.equals("quantity")){
					if(!record.getAsString("objectCode").equals("199999") || !record.getAsString("subsidiaryCode").equals("99019999"))
						error = "Selected field cannot be edited - Only quantities with an object code equals '199999' and a subsidiary code equals '99019999'can be edited.";
					else if(record.getAsString("objectCode").equals("199999") && record.getAsString("subsidiaryCode").equals("99019999") && !record.getAsString("resourceDescription").trim().equals("Genuine Markup of Change Order"))
						error = "Selected field cannot be edited - Only quantities with an object code equals '199999', a subsidiary code equals '99019999' and resource description equals 'Genuine Markup of Change Order' can be edited.";
				}
				else if(record.getAsDouble("postedIVAmount") != 0 && (field.equals("packageNo") || field.equals("objectCode") || field.equals("subsidiaryCode")))
					error = "Selected field cannot be edited - resource has posted IV amount:" + record.getAsDouble("postedIVAmount");
				
				
				//Error
				if(error != null){
					if(record.getId().equals(lastEditedId))
						MessageBox.alert(error);
					else
						lastEditedId = record.getId();
					
					isEditable =  false;
				}else{
					lastEditedId = record.getId();
					isEditable =  true;
				}
				
				//Editable
				if (isEditable)
					arrowKeyNavigation.startedEdit(colIndex, rowIndex);
				else 
					arrowKeyNavigation.resetState();

				return isEditable;
			}

			public boolean doValidateEdit(GridPanel grid, final Record record, final String field, final Object value, Object originalValue, int rowIndex, int colIndex) {
				
				// added by brian on 20110311 - start
				// add validation of checking the package no is submitted or awarded if the object code is changed to start with 14
 				String error = null;
 				String newObjectCode = "";
 				
 				if(field.equals("objectCode") && value != null)
 					newObjectCode = ((String)value);
 				
				if(record.getAsString("packageNo") != null && uneditablePackageNos.contains(record.getAsString("packageNo")) 
//						&& record.getAsString("objectCode").startsWith("14"))
						&& newObjectCode.startsWith("14"))
					error = "This package is submitted or awarded - please use the 'Subcontract Addendum' window to edit this resource";
			/*	else if(record.getAsString("packageNo") != null && uneditableUnawardedPackageNos.contains(record.getAsString("packageNo")))
					error = "Selected field cannot be edited - this package has Payment Requisition submitted.";*/
				
				if(error != null){
					MessageBox.alert(error);
					return false;
				}
				
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
				else if(field.equals("subsidiaryCode")){
					if(value == null || ((String)value).length() != 8){
						MessageBox.alert("Subsidiary code must be 8 digits in length");
						return false;
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
				}
				else if(field.equals("quantity")){
					 if(value != null && record.getAsString("objectCode").equals("199999") && record.getAsString("subsidiaryCode").equals("99019999") && record.getAsString("resourceDescription").trim().equals("Genuine Markup of Change Order") &&
							 (Double.parseDouble((String)value) < record.getAsDouble("postedIVAmount") || 
							  Double.parseDouble((String)value) < record.getAsDouble("cumIVAmount"))){
						 MessageBox.alert("Quantity cannot be smaller than Posted IV or Cumulative IV.");
					 	return false;
					 }
				}
				
				//Check if package status is 160, and warn user that the TA will be reset.
				String packageNosForReset =  null;
				if(!field.equals("packageNo") && record.getAsString("packageNo") != null){
					selectedPackageRecord = null;
					Record[] packageRecs = packageComboBox.getStore().query("packageNo", record.getAsString("packageNo").trim());
					if(packageRecs.length == 1)
						selectedPackageRecord = packageRecs[0];
				}
				else if(field.equals("packageNo") && originalValue != null){ //if the package no is being changed, check if the old package must be reset
					Record[] packageRecs = packageComboBox.getStore().query("packageNo", ((String)originalValue).trim());
					if(packageRecs.length == 1 && (packageRecs[0].getAsString("status").equals("160") || packageRecs[0].getAsString("status").equals("340")))
						packageNosForReset = (String)originalValue;
				} 
				if(selectedPackageRecord != null && (selectedPackageRecord.getAsString("status").equals("160") || selectedPackageRecord.getAsString("status").equals("340")) && 
						!packagesToBeReset.contains(selectedPackageRecord.getAsString("packageNo"))){ //check if the current/new package must be reset
					if(packageNosForReset != null)
						packageNosForReset += " and " + selectedPackageRecord.getAsString("packageNo");
					else
						packageNosForReset = selectedPackageRecord.getAsString("packageNo");
				}
				if(packageNosForReset != null){
					MessageBox.confirm("Warning", "Tender Analysis for package " + packageNosForReset + " will be reset when you save these changes. Press 'Yes' to continue, or 'No' to cancel.", new MessageBox.ConfirmCallback(){
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

			public void onAfterEdit(GridPanel grid, Record record, String field, Object newValue, Object oldValue, int rowIndex, int colIndex) {
				/*if(record.getAsString("packageNo") != null && uneditableUnawardedPackageNos.contains(record.getAsString("packageNo"))){
					MessageBox.alert("Selected field cannot be edited - this package has Payment Requisition submitted.");
					record.set("packageNo", "0");
					return;
				}*/
			}
		});
		
		Toolbar toolbar = new Toolbar();
		toolbar.setHeight(30);
			
		splitButton = new ToolbarButton();
		splitButton.setText("Split");
		splitButton.setIconCls("split-icon");
		splitButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				splitResource();
			}
		});
		ToolTip splitToolTip = new ToolTip();
		splitToolTip.setTitle("Split");
		splitToolTip.setHtml("Split a resource into two or more resources");
		splitToolTip.applyTo(splitButton);
		
		mergeButton = new ToolbarButton();
		mergeButton.setText("Merge");
		mergeButton.setIconCls("merge-icon");
		mergeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				mergeResources();
			}
		});
		ToolTip mergeToolTip = new ToolTip();
		mergeToolTip.setTitle("Merge");
		mergeToolTip.setHtml("Merge two or more resources into a single resource");
		mergeToolTip.applyTo(mergeButton);
		
		addButton = new ToolbarButton();
		addButton.setText("Add");
		addButton.setIconCls("add-button-icon");
		addButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				openAddWindow();
			}
		});
		ToolTip addToolTip = new ToolTip();
		addToolTip.setTitle("Add");
		addToolTip.setHtml("Create a new resource");
		addToolTip.applyTo(addButton);
		
		deleteVoButton = new ToolbarButton("Delete VO");
		deleteVoButton.setTooltip("Delete VO", "Delete the selected VO resource(s)");
		deleteVoButton.setIconCls("cancel-icon");
		deleteVoButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
//				globalMessageTicket.refresh();
				try{
					deleteVoResources();
				}catch(Exception ex){
					MessageBox.alert(ex.getMessage());
				}
			}
		});
		
		toolbar.addButton(splitButton);
		toolbar.addSeparator();
		toolbar.addButton(mergeButton);
		toolbar.addSeparator();
		toolbar.addButton(addButton);
		toolbar.addSeparator();
		toolbar.addButton(deleteVoButton);
		resultEditorGridPanel.setTopToolbar(toolbar);
		
		splitButton.hide();
		mergeButton.hide();
		addButton.hide();
		deleteVoButton.hide();
		
		// Check for access rights - then add toolbar buttons accordingly
		try{
			UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getUserAccessRightsRepository().getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010105_REPACKAGING_UPDATE_BY_RESOURCE_SUMMARY_WINDOW, new AsyncCallback<List<String>>(){
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
		
		GridView view = new CustomizedGridView();
		view.setAutoFill(true);
		view.setForceFit(true);
		resultEditorGridPanel.setView(view);
		
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(final int pageNum){
				globalMessageTicket.refresh();
				if (recordsIsDirty()){
					MessageBox.show(new MessageBoxConfig() {   
						{   
							setTitle("Continue?");   
							setMsg("If you continue to next page, the changes you made on this page will be lost.  Continue to next page?");   
							setButtons(MessageBox.YESNO);   
							setCallback(new MessageBox.PromptCallback() {   
								public void execute(String btnID, String text) {   
									if (btnID.toLowerCase().equals("yes")) {
										searchByPage(pageNum);
									}
									else if (btnID.toLowerCase().equals("no")){
										
									}
								}
							});   

						}   
					});  
				}
				else{
					searchByPage(pageNum);
				}
				
			}
		});
		paginationToolbar.addFill();
		paginationToolbar.addItem(totalAmountTextItem);
		paginationToolbar.addSpacer();
		paginationToolbar.addSeparator();
		paginationToolbar.addSpacer();
		paginationToolbar.addItem(totalBudgetTextItem);
		paginationToolbar.addSpacer();
		paginationToolbar.addSeparator();
		paginationToolbar.addSpacer();
		paginationToolbar.addItem(totalSellingValueTextItem);
		
		resultEditorGridPanel.setBottomToolbar(paginationToolbar);
		paginationToolbar.setTotalPage(0);
		paginationToolbar.setCurrentPage(0);
		
		resultPanel.add(resultEditorGridPanel);
		mainPanel.add(searchPanel);
		mainPanel.add(resultPanel);
		this.add(mainPanel);
		
		updateButton = new Button("Update");
		updateButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, com.gwtext.client.core.EventObject e) {
					globalMessageTicket.refresh();
					saveUpdates();
				};
		});		
		this.addButton(updateButton);
		
		updateButton.hide();
		
		Button closeWindowButton = new Button("Close");
		closeWindowButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, com.gwtext.client.core.EventObject e) {
					if (dataStore.getModifiedRecords().length!=0){
						MessageBox.confirm("Confirm", "Are you sure you want to discard the changes?",   
		                        new MessageBox.ConfirmCallback() {   
		                            public void execute(String btnID) {
		                            	if (btnID.equals("yes"))
		                            		globalSectionController.closeCurrentWindow();
		                            }
		                        });
					}
					else
						globalSectionController.closeCurrentWindow();
				};
		});		
		this.addButton(closeWindowButton);
	}
	
	private void displayButtons(){
		if(accessRightsList != null && accessRightsList.contains("WRITE")){
			splitButton.show();
			mergeButton.show();
			addButton.show();
			deleteVoButton.show();
			updateButton.show();
		}
	}
	
	public void search(){
		packageNo = packageNoTextField.getText().trim();
		objectCode = objectCodeTextField.getText().trim();
		subsidiaryCode = subsidiaryCodeTextField.getText().trim();
		description = descriptionTextField.getText().trim();
		
		type = typeComboBox.getValue();
		levyExcluded = levyExcludedComboBox.getValue();
		defectExcluded = defectExcludedComboBox.getValue();
		
		searchByPage(0);
	}
	
	public void searchByPage(int pageNum){
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Loading...", true);
		try {
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getBqResourceSummaryRepository().obtainResourceSummariesSearchByPage(globalSectionController.getJob(), packageNo, objectCode, 
					subsidiaryCode, description, type, levyExcluded, defectExcluded, pageNum, new AsyncCallback<RepackagingPaginationWrapper<BQResourceSummary>>(){
				public void onSuccess(final RepackagingPaginationWrapper<BQResourceSummary> wrapper) {
					populateGrid(wrapper);
					UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				}
				public void onFailure(Throwable e) {
					UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
				}
			});
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void populateGrid(RepackagingPaginationWrapper<BQResourceSummary> wrapper){
		dataStore.rejectChanges();
		dataStore.removeAll();
		paginationToolbar.setTotalPage(wrapper.getTotalPage());
		paginationToolbar.setCurrentPage(wrapper.getCurrentPage());
		int rows = wrapper.getTotalRecords();
		Double totalBudget = wrapper.getTotalBudget();
		Double totalSellingValue = wrapper.getTotalSellingValue();
		NumberFormat nf = NumberFormat.getFormat("#,##0.0#");
		totalAmountTextItem.setText("<b>" + rows + " Records match search criteria</b>");
		totalBudgetTextItem.setText("<b>Total Budget:&nbsp;&nbsp;" + nf.format(totalBudget) + "</b>");
		totalSellingValueTextItem.setText("<b>Total Selling Value:&nbsp;&nbsp;" + nf.format(totalSellingValue) + "</b>");
		try{
			for (BQResourceSummary resourceSummary : wrapper.getCurrentPageContentList()){
				Record record = recordFromResourceSummary(resourceSummary);
				dataStore.add(record);
			}
		}
		catch(Exception e){
			UIUtil.alert(e);
		}
	}
	
	public void saveUpdates(){
		resultEditorGridPanel.stopEditing();
		saveResourceSummaries(dataStore.getModifiedRecords());
	}
	
	//Add new resource summary (from add window)
	public void addResourceSummary(final Record record){
		// modified by brian on 20110401
		UIUtil.maskPanelById(AddBQResourceSummaryWindow.WINDOW_ID, "Saving resource", true);
		BQResourceSummary resourceSummary = resourceSummaryFromRecord(record);
		try{
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getBqResourceSummaryRepository().saveResourceSummary(resourceSummary, repackagingEntryId, new AsyncCallback<BQResourceSummaryWrapper>(){
				public void onSuccess(BQResourceSummaryWrapper wrapper){
					// modified by brian on 20110401
					UIUtil.unmaskPanelById(AddBQResourceSummaryWindow.WINDOW_ID);
					if(wrapper.getError() != null && wrapper.getError().length() > 0){
						String errorMsg = wrapper.getError();
						MessageBox.alert("Invalid inputs", errorMsg);
					}
					else{
						MessageBox.alert("Resource successfully added");
						closeChildWindow();
						repackagingListGridPanel.updateStatus("200");
					}
				}
				public void onFailure(Throwable e) {
					UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
				}
			});
		}
		catch(Exception e){
			UIUtil.alert(e);
		}
	}
	
	public void saveResourceSummaries(Record[] records){
		if(records.length == 0){
			MessageBox.alert("No records have been modified");
			return;
		}
		List<BQResourceSummary> resourceSummaries = new ArrayList<BQResourceSummary>();	
		for(Record record : records){
			BQResourceSummary resourceSummary = resourceSummaryFromRecord(record);
			if(resourceSummaries.contains(resourceSummary)){
				MessageBox.alert("Duplicate resource: " + "Package: " + resourceSummary.getPackageNo() + 
						", Object Code: " + resourceSummary.getObjectCode() + ", Subsidiary Code: " + resourceSummary.getSubsidiaryCode() + 
						", Unit: " + resourceSummary.getUnit() + ", Rate: " + resourceSummary.getRate());
				return;
			}
			else
				resourceSummaries.add(resourceSummary);
		}
		// modified by brian on 20110321
		UIUtil.maskPanelById(WINDOW_ID, "Saving Updates", true);
		try{
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getBqResourceSummaryRepository().saveResourceSummaries(resourceSummaries, repackagingEntryId, new AsyncCallback<BQResourceSummaryWrapper>(){
				public void onSuccess(BQResourceSummaryWrapper wrapper) {
					if(wrapper.getError() != null && wrapper.getError().length() > 0)
						MessageBox.alert("Invalid Inputs", wrapper.getError());
					else{
						dataStore.commitChanges();
						MessageBox.alert("Records Saved");
						repackagingListGridPanel.updateStatus("200");
						globalSectionController.getTreeSectionController().getPaymentTreePanel().refreshPaymentTreePanel();
					}
					UIUtil.unmaskPanelById(WINDOW_ID);
				}
				public void onFailure(Throwable e) {
					UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
					UIUtil.unmaskPanelById(WINDOW_ID);
				}				
			});
		}
		catch(Exception e){
			UIUtil.alert(e);			
		}
	}
	
	public void saveSplitMergeResources(final Record[] oldRecords, final Record[] newRecords){
		// modified by brian on 20110401
		UIUtil.maskPanelById(MergeOrSplitBQResourceSummaryWindow.WINDOW_ID, "Saving resource(s)", true);
		final List<BQResourceSummary> oldResources = new ArrayList<BQResourceSummary>();
		final List<BQResourceSummary> newResources = new ArrayList<BQResourceSummary>();
		for(Record record : oldRecords)
			oldResources.add(resourceSummaryFromRecord(record));
		for(Record record : newRecords){
			BQResourceSummary resourceSummary = resourceSummaryFromRecord(record);
			if(newResources.contains(resourceSummary)){
				// modified by brian on 20110321
//				UIUtil.unmaskPanelById(MergeOrSplitBQResourceSummaryWindow.MAIN_PANEL_ID);
				UIUtil.unmaskPanelById(MergeOrSplitBQResourceSummaryWindow.WINDOW_ID);
				MessageBox.alert("Duplicate resource: " + "Package: " + resourceSummary.getPackageNo() + 
						", Object Code: " + resourceSummary.getObjectCode() + ", Subsidiary Code: " + resourceSummary.getSubsidiaryCode() + 
						", Unit: " + resourceSummary.getUnit() + ", Rate: " + resourceSummary.getRate());
				return;
			}
			else
				newResources.add(resourceSummary);
		}
		try{
			//Save new resources and, if successful, deactivate the old resources
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getBqResourceSummaryRepository().splitOrMergeResources(oldResources, newResources, repackagingEntryId, new AsyncCallback<BQResourceSummaryWrapper>(){
				public void onSuccess(BQResourceSummaryWrapper wrapper) {
					if(wrapper.getError() != null && wrapper.getError().length() > 0){
						MessageBox.alert("Invalid Inputs", wrapper.getError());
					}
					else{
						searchByPage(0);
						repackagingListGridPanel.updateStatus("200");
						if(childWindow != null){
							closeChildWindow();
						}
					}
					// modified by brian on 20110321
					UIUtil.unmaskPanelById(MergeOrSplitBQResourceSummaryWindow.WINDOW_ID);
				}
				public void onFailure(Throwable e) {
					UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
				}				
			});
		}
		catch(Exception e){
			UIUtil.alert(e);			
		}
	}
	
	public BQResourceSummary resourceSummaryFromRecord(Record record){
		BQResourceSummary resourceSummary = new BQResourceSummary();
		resourceSummary.setJob(globalSectionController.getJob());
		String id = record.getAsString("id");
		if(id != null && id.length() > 0)
			resourceSummary.setId(new Long(id));
		if(record.getAsString("packageNo") != null && record.getAsString("packageNo").trim().length()>0){
//			if(record.getAsString("packageNo").trim().equals("Deselect From Package"))
//				resourceSummary.setPackageNo("");
//			else
				resourceSummary.setPackageNo(record.getAsString("packageNo").trim());
		}
		if(record.getAsString("objectCode") != null)
			resourceSummary.setObjectCode(record.getAsString("objectCode").trim());
		if(record.getAsString("subsidiaryCode") != null)
			resourceSummary.setSubsidiaryCode(record.getAsString("subsidiaryCode").trim());
		if(record.getAsString("resourceDescription") != null)
			resourceSummary.setResourceDescription(record.getAsString("resourceDescription").trim());
		if(record.getAsString("unit") != null)
			resourceSummary.setUnit(record.getAsString("unit").trim());
		resourceSummary.setQuantity(record.getAsDouble("quantity"));
		resourceSummary.setRate(record.getAsDouble("rate"));
		resourceSummary.setResourceType(record.getAsString("resourceType"));
		resourceSummary.setExcludeLevy(record.getAsBoolean("excludeLevy"));
		resourceSummary.setExcludeDefect(record.getAsBoolean("excludeDefect"));
		return resourceSummary;
	}
	
	public Record recordFromResourceSummary(BQResourceSummary resourceSummary){
		Double quantity = resourceSummary.getQuantity();
		Double rate = resourceSummary.getRate();
		Double amount = quantity*rate;
		Record record;
		
		record = resourceSummaryRecordDef.createRecord(new Object[]{
				resourceSummary.getId().toString(),
				resourceSummary.getPackageNo(),
				resourceSummary.getObjectCode(),
				resourceSummary.getSubsidiaryCode(),
				resourceSummary.getResourceDescription(),
				resourceSummary.getUnit(),
				quantity,
				rate,
				amount,
				resourceSummary.getPostedIVAmount(),
				resourceSummary.getResourceType(),
				resourceSummary.getExcludeLevy(),
				resourceSummary.getExcludeDefect(),
				resourceSummary.getCurrIVAmount()
		});
		return record;
	}
	
	public void closeChildWindow(){
		childWindow.close();
		childWindow = null;
	}
	
	public void openAddWindow(){
		if(childWindow == null){
			childWindow = new AddBQResourceSummaryWindow(this);
			childWindow.show();
		}
	}
	
	public void splitResource(){
		uneditablePackageNos = globalSectionController.getUneditablePackageNos();
		/*uneditableUnawardedPackageNos = globalSectionController.getUneditableUnawardedPackageNos();
		uneditableResourceSummaryIDs = globalSectionController.getUneditableResourceSummaries();*/
		if(childWindow != null){
			MessageBox.alert("Please close other split/merge/add windows");
			return;
		}
		if(cbSelectionModel.getCount() != 1){
			MessageBox.alert("Please select only 1 resource to split");
			return;
		}
		Record record = cbSelectionModel.getSelected();
		if(record.getAsDouble("postedIVAmount") != 0){
			MessageBox.alert("Selected resource cannot be split - resource has posted IV amount: " + record.getAsDouble("postedIVAmount"));
			return;
		}
		if(record.getAsString("packageNo") != null && uneditablePackageNos.contains(record.getAsString("packageNo"))
				&& record.getAsString("objectCode").startsWith("14")){
			MessageBox.alert("Selected resource cannot be split - package has been submitted or awarded");
			return;
		}
		/*if(record.getAsString("packageNo") != null && uneditableUnawardedPackageNos.contains(record.getAsString("packageNo"))){
			MessageBox.alert("Selected field cannot be split - this package has Payment Requisition submitted.");
			return;
		}
		if(record.getAsString("packageNo") != null && uneditableResourceSummaryIDs.contains(record.getAsString("id"))){
			MessageBox.alert("Selected field cannot be split - this resource is being used in Payment Requisition.");
			return;
		}*/
		MergeOrSplitBQResourceSummaryWindow splitWindow = new MergeOrSplitBQResourceSummaryWindow(this, MergeOrSplitBQResourceSummaryWindow.SPLIT);
		splitWindow.populateGrid(cbSelectionModel.getSelections());
		childWindow = splitWindow;
		childWindow.show();
	}
	
	public void mergeResources(){
		uneditablePackageNos = globalSectionController.getUneditablePackageNos();
		/*uneditableUnawardedPackageNos = globalSectionController.getUneditableUnawardedPackageNos();
		uneditableResourceSummaryIDs = globalSectionController.getUneditableResourceSummaries();*/
		if(childWindow != null){
			MessageBox.alert("Please close other split/merge/add windows");
			return;
		}
		if(cbSelectionModel.getCount() < 2){
			MessageBox.alert("Please select at least 2 resources to merge");
			return;
		}
		Record firstRecord = cbSelectionModel.getSelected();
		String resourceType = firstRecord.getAsString("resourceType");
		double lastAmount = firstRecord.getAsDouble("amount");
		for(Record record : cbSelectionModel.getSelections()){
			if(record.getAsString("resourceType") != resourceType && (record.getAsString("resourceType") == null || !record.getAsString("resourceType").equals(resourceType))){
				MessageBox.alert("Resources cannot be merged - resources must have the same type");
				return;
			}
			double amount = record.getAsDouble("amount");
			if(amount != 0){
				if(lastAmount/amount < 0){
					MessageBox.alert("Resources cannot be merged - amounts must all have the same sign (+/-)");
					return;
				}
				lastAmount = amount;
			}
			if(record.getAsDouble("postedIVAmount") != 0){
				MessageBox.alert("Resource " + record.getAsString("resourceDescription") + " cannot be merged - resource has posted IV amount.");
				return;
			}
			if(record.getAsString("packageNo") != null && uneditablePackageNos.contains(record.getAsString("packageNo")) 
					&& record.getAsString("objectCode").startsWith("14")){
				MessageBox.alert("Resource " + record.getAsString("resourceDescription") + " cannot be merged - package has been submitted or awarded");
				return;
			}
			/*if(record.getAsString("packageNo") != null && uneditableUnawardedPackageNos.contains(record.getAsString("packageNo"))){
				MessageBox.alert("Resource " + record.getAsString("resourceDescription") + " cannot be merged - this package has Payment Requisition submitted.");
				return;
			}
			if(record.getAsString("packageNo") != null && uneditableResourceSummaryIDs.contains(record.getAsString("id"))){
				MessageBox.alert("Resource " + record.getAsString("resourceDescription") + " cannot be merged - this resource is being used in Payment Requisition.");
				return;
			}*/
		}
		MergeOrSplitBQResourceSummaryWindow mergeWindow = new MergeOrSplitBQResourceSummaryWindow(this, MergeOrSplitBQResourceSummaryWindow.MERGE);
		mergeWindow.populateGrid(cbSelectionModel.getSelections());
		childWindow = mergeWindow;
		childWindow.show();
	}
	
	private boolean recordsIsDirty(){
		Record[] records = dataStore.getRecords();
		for (int i=0; i< records.length-1; i++ ) {
			if (records[i].isDirty() ) {
				return true;
			}
		}
		return false;
	}
	
	private void deleteVoResources(){
		Record[] records = cbSelectionModel.getSelections();
		if(records.length == 0){
			MessageBox.alert("Please select VO resources to delete");
			return;
		}

		List<BQResourceSummary> resourceSummaries = new ArrayList<BQResourceSummary>(records.length);
		for(Record record : records){
			if(!"VO".equals(record.getAsString("resourceType")) && !"OI".equals(record.getAsString("resourceType"))){
				MessageBox.alert("Only VO/OI resources can be deleted");
				return;
			}
			if(record.getAsString("packageNo") != null && record.getAsString("packageNo").trim().length() != 0){
				MessageBox.alert("Please remove the VO from its package before deleting");
				return;
			}
			if(record.getAsDouble("postedIVAmount") != 0){
				MessageBox.alert("Resources with non-zero posted IV cannot be deleted");
				return;
			}
			resourceSummaries.add(resourceSummaryFromRecord(record));
		}
		
		// modified by brian on 20110321
		UIUtil.maskPanelById(WINDOW_ID, "Deleting resources", true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getBqResourceSummaryRepository().deleteVoResources(resourceSummaries, new AsyncCallback<String>(){
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(WINDOW_ID);
				UIUtil.checkSessionTimeout(e, false,globalSectionController.getUser());
			}

			public void onSuccess(String error) {
				UIUtil.unmaskPanelById(WINDOW_ID);
				if(error == null){
					repackagingListGridPanel.updateStatus("200");
					MessageBox.alert("Resources deleted successfully");
					searchByPage(0);
				}
				else
					MessageBox.alert(error + "<br/>(Another user may have updated the resource)");
			}
		});
	}

	public GlobalSectionController getGlobalSectionController() {
		return globalSectionController;
	}

	public void setChildWindow(Window childWindow) {
		this.childWindow = childWindow;
	}

	public Window getChildWindow() {
		return childWindow;
	}

	public void setArrowKeyNavigation(ArrowKeyNavigation arrowKeyNavigation) {
		this.arrowKeyNavigation = arrowKeyNavigation;
	}

	public ArrowKeyNavigation getArrowKeyNavigation() {
		return arrowKeyNavigation;
	}
	
	/*
	 * set combobox option
	 * @author xethhung
	 * 2015-06-07 
	 */
	private void setStaticComboBoxOption(ComboBox combobox, String[][] valuePair){
		final String valueField = "VALUE";
		final String displayField = "DISPLAY";
		String[] header = new String[]{valueField,displayField};	
		Store store = new SimpleStore(header, valuePair);

		if(combobox != null){
			combobox.setStore(store);
			combobox.setDisplayField(displayField);
			combobox.setValueField(valueField);
			combobox.setValue(valuePair[0][0]);
			combobox.setEmptyText(valuePair[0][1]);
			combobox.setForceSelection(true);
		}
	}

}
