package com.gammon.qs.client.ui.window.mainSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gammon.qs.application.GeneralPreferencesKey;
import com.gammon.qs.application.User;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.panel.mainSection.RepackagingListGridPanel;
import com.gammon.qs.client.ui.renderer.AmountRendererCheckIfTotal;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.RoundingUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.Resource;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
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
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
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
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

public class RepackagingUpdateByResourceWindow extends Window {
	public static final String MAIN_PANEL_ID = "repackagingUpdateByBQWindow";
	
	private GlobalSectionController globalSectionController;
	private RepackagingListGridPanel repackagingListGridPanel;
	private Window childWindow; //Split/Merge/Balance window
	private Panel mainPanel;
	private Panel searchPanel;
	private EditorGridPanel resultEditorGridPanel;
	private GlobalMessageTicket globalMessageTicket;
	
	private List<String> accessRightsList;
	
	private Button saveButton;
	private ToolbarButton splitButton;
	private ToolbarButton mergeButton;
	private ToolbarButton balanceButton;
	
	private PaginationToolbar paginationToolbar;
	//Search parameters (used for pagination)
	private String jobNumber;
	private String billNo;
	private String subBillNo;
	private String pageNo;
	private String itemNo;
	private String packageNo;
	private String objectCode;
	private String subsidiaryCode;
	private String description;
	
	private Store dataStore;
	
	//Search fields
	private TextField billNoField;
	private TextField subBillNoField;
	private TextField pageNoField;
	private TextField itemNoField;
	private TextField packageNoField;
	private TextField objectCodeField;
	private TextField subsidiaryCodeField;
	private TextField descriptionField;
	
	private CheckboxSelectionModel cbSelectionModel;
	
	private RecordDef resourceRecordDef = new RecordDef(
			new FieldDef[] {
					new StringFieldDef("id"),
					new StringFieldDef("bpi"),
					new StringFieldDef("packageNo"),
					new StringFieldDef("objectCode"),
					new StringFieldDef("subsidiaryCode"),
					new StringFieldDef("description"),
					new StringFieldDef("unit"),
					new StringFieldDef("quantity"), //remeasured
					new StringFieldDef("remeasuredFactor"),
					new StringFieldDef("rate"),
					new StringFieldDef("amount"),
					new StringFieldDef("resourceType"),
					new StringFieldDef("originalQuantity"),
					new StringFieldDef("originalRate"),
					
					//reference only
					new StringFieldDef("postedIVAmount"),
					new StringFieldDef("cumulativeIVAmount")
				}
			);
	
	private Store packageStore;
	private List<String> uneditablePackageNos;
	//private List<String> uneditableUnawardedPackageNos;
	//private List<String> uneditableResourceIDs;
	
	private ComboBox unitComboBox;
	private ComboBox packageComboBox;
	
	private String lastEditedId;
	
	private ColumnConfig bpiColumn;
	private ColumnConfig packageNoColumn;
	private ColumnConfig objectCodeColumn;
	private ColumnConfig subsidiaryCodeColumn;
	private ColumnConfig descriptionColumn;
	private ColumnConfig unitColumn;
	private ColumnConfig quantityColumn;
	private ColumnConfig rateColumn;
	private ColumnConfig amountColumn;
	private ColumnConfig resourceTypeColumn;
	
	public RepackagingUpdateByResourceWindow(RepackagingListGridPanel repackagingListGridPanel){
		super();
		this.setModal(true);
		this.repackagingListGridPanel = repackagingListGridPanel;
		this.globalSectionController = repackagingListGridPanel.getGlobalSectionController();
		this.globalMessageTicket = new GlobalMessageTicket();
		
		setupUI();
		globalSectionController.setCurrentWindow(this);
		globalSectionController.getCurrentWindow().show();

		globalSectionController.refreshUneditablePackageNos();
		//globalSectionController.refreshUneditableUnawardedPackageNos();
		//globalSectionController.refreshUneditableResourcesID();
	}
	
	private void setupUI(){
		this.setTitle("Repackaging - Update Resources");
		this.setPaddings(5);
		this.setWidth(1024);
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
		searchPanel.setLayout(new TableLayout(9));
		
		FieldListener searchListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER){
					search();
				}
			}
		};
		
		Label billNoLabel = new Label("Bill No");
		billNoLabel.setCls("table-cell");
		searchPanel.add(billNoLabel);
		billNoField =new TextField("Bill No", "billNo", 100);
		billNoField.setCtCls("table-cell");
		billNoField.addListener(searchListener);
		searchPanel.add(billNoField);
		
		Label subBillNoLabel = new Label("SubBill No");
		subBillNoLabel.setCls("table-cell");
		searchPanel.add(subBillNoLabel);
		subBillNoField =new TextField("SubBill No", "subBillNo", 100);
		subBillNoField.setCtCls("table-cell");
		subBillNoField.addListener(searchListener);
		searchPanel.add(subBillNoField);
		
		Label pageNoLabel = new Label("Page No");
		pageNoLabel.setCls("table-cell");
		searchPanel.add(pageNoLabel);
		pageNoField =new TextField("Page No", "pageNo", 100);
		pageNoField.setCtCls("table-cell");
		pageNoField.addListener(searchListener);
		searchPanel.add(pageNoField);
		
		Label itemNoLabel = new Label("Item No");
		itemNoLabel.setCls("table-cell");
		searchPanel.add(itemNoLabel);
		itemNoField =new TextField("Item No", "itemNo", 100);
		itemNoField.setCtCls("table-cell");
		itemNoField.addListener(searchListener);
		searchPanel.add(itemNoField);
		
		searchPanel.add(new Label("")); //Empty label
		
		Label packageNoLabel = new Label("Package No");
		packageNoLabel.setCls("table-cell");
		searchPanel.add(packageNoLabel);
		packageNoField = new TextField("Package No", "packageNo", 100);
		packageNoField.setCtCls("table-cell");
		packageNoField.addListener(searchListener);
		searchPanel.add(packageNoField);
		
		Label objectCodeLabel = new Label("Object Code");
		objectCodeLabel.setCls("table-cell");
		searchPanel.add(objectCodeLabel);
		objectCodeField = new TextField("Object Code", "objectCode", 100);
		objectCodeField.setCtCls("table-cell");
		objectCodeField.addListener(searchListener);
		searchPanel.add(objectCodeField);
		
		Label subsidiaryCodeLabel = new Label("Subsidiary Code");
		subsidiaryCodeLabel.setCls("table-cell");
		searchPanel.add(subsidiaryCodeLabel);
		subsidiaryCodeField = new TextField("Subsidiary Code", "subsidiaryCode", 100);
		subsidiaryCodeField.setCtCls("table-cell");
		subsidiaryCodeField.addListener(searchListener);
		searchPanel.add(subsidiaryCodeField);
		
		Label descriptionLabel = new Label("Description");
		descriptionLabel.setCls("table-cell");
		searchPanel.add(descriptionLabel);
		descriptionField = new TextField("Description", "description", 200);
		descriptionField.setCtCls("table-cell");
		descriptionField.addListener(searchListener);
		searchPanel.add(descriptionField);
		
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
		User user = globalSectionController.getUser();
		resultEditorGridPanel = new EditorGridPanel();
		final Renderer quantityRenderer = new QuantityRenderer(user);
		final Renderer amountRenderer = new AmountRendererCheckIfTotal(user);
		final Renderer rateRenderer = new RateRenderer(user);
		Map<String, String> prefs = user.getGeneralPreferences();
		String quantDPStr = prefs.get(GeneralPreferencesKey.QUANTITY_DECIMAL_PLACES);
		if(quantDPStr == null)
			quantDPStr = "2";
		final int quantDP = Integer.parseInt(quantDPStr);
		NumberField quantNumberField = new NumberField();
		quantNumberField.setDecimalPrecision(quantDP);
		String rateDPStr = prefs.get(GeneralPreferencesKey.RATE_DECIMAL_PLACES);
		if(rateDPStr == null)
			rateDPStr = "2";
		final int rateDP = Integer.parseInt(rateDPStr);
		NumberField rateNumberField = new NumberField();
		rateNumberField.setDecimalPrecision(rateDP);
		
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

		
		uneditablePackageNos = globalSectionController.getUneditablePackageNos();
		if(uneditablePackageNos == null)
			uneditablePackageNos = new ArrayList<String>();
		
		bpiColumn = new ColumnConfig("B/P/I", "bpi", 120, false);
		packageNoColumn = new ColumnConfig("Package No.", "packageNo", 40, false);
		packageNoColumn.setEditor(new GridEditor(packageComboBox));
		packageNoColumn.setTooltip("Package No.");
		objectCodeColumn = new ColumnConfig("Object Code", "objectCode", 50, false);
		objectCodeColumn.setEditor(new GridEditor(new TextField()));
		objectCodeColumn.setTooltip("Object Code");
		subsidiaryCodeColumn = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 65, false);
		subsidiaryCodeColumn.setEditor(new GridEditor(new TextField()));
		subsidiaryCodeColumn.setTooltip("Subsidiary Code");
		descriptionColumn = new ColumnConfig("Description", "description", 250, false);
		descriptionColumn.setEditor(new GridEditor(new TextField()));
		unitColumn = new ColumnConfig("Unit", "unit", 40, false);
		unitColumn.setEditor(new GridEditor(unitComboBox));
		quantityColumn = new ColumnConfig("Qty", "quantity", 110, false);
		quantityColumn.setRenderer(quantityRenderer);		
		quantityColumn.setAlign(TextAlign.RIGHT);
		quantityColumn.setEditor(new GridEditor(quantNumberField));
		rateColumn = new ColumnConfig("Rate", "rate", 120, false);
		rateColumn.setRenderer(rateRenderer);
		rateColumn.setAlign(TextAlign.RIGHT);
		rateColumn.setEditor(new GridEditor(rateNumberField));
		amountColumn = new ColumnConfig("Amount", "amount", 120, false);
		amountColumn.setRenderer(amountRenderer);
		amountColumn.setAlign(TextAlign.RIGHT);
		resourceTypeColumn = new ColumnConfig("Type", "resourceType", 30, false);
		resourceTypeColumn.setTooltip("Resource Type");
		
		cbSelectionModel = new CheckboxSelectionModel();
		
		BaseColumnConfig[] columns = new BaseColumnConfig[]{
			new CheckboxColumnConfig(cbSelectionModel),
			bpiColumn,
			packageNoColumn,
			objectCodeColumn,
			subsidiaryCodeColumn,
			descriptionColumn,
			unitColumn,
			quantityColumn,
			rateColumn,
			amountColumn,
			resourceTypeColumn
		};
		
		resultEditorGridPanel.setColumnModel(new ColumnModel(columns));
		resultEditorGridPanel.setSelectionModel(cbSelectionModel);
		
		resultEditorGridPanel.addEditorGridListener(new EditorGridListener(){
			
			public boolean doBeforeEdit(GridPanel grid, Record record,
					String field, Object value, int rowIndex, int colIndex) {
				globalMessageTicket.refresh();
				uneditablePackageNos = globalSectionController.getUneditablePackageNos();
				//uneditableUnawardedPackageNos = globalSectionController.getUneditableUnawardedPackageNos();
				//uneditableResourceIDs = globalSectionController.getUneditableResourceIDs();
				/**
				 * @author tikywong
				 * April 18, 2011
				 */
				//If the Posted IV Amount or Cumulative IV Amount are not 0, no update can be done
				if(globalSectionController.getJob().getRepackagingType().equals("3") &&
					((record.isNull("postedIVAmount") || record.isEmpty("postedIVAmount") || record.getAsDouble("postedIVAmount") != 0.00) ||
					 (record.isNull("cumulativeIVAmount") || record.isEmpty("cumulativeIVAmount") || record.getAsDouble("cumulativeIVAmount") != 0.00))){
					MessageBox.alert("The resource cannot be updated if its Posted IV Amount or Cumulative IV Amount is not zero.");
					return false;
				}
				
				//If quantity/rate is modified, you can only modify that field
				if(record.isModified("quantity") && !field.equals("quantity"))
					return false;
				if(record.isModified("rate") && !field.equals("rate"))
					return false;
				//you can't modify quantity/rate if it is 0, or if any other fields have been modified
				if(field.equals("quantity") && (Double.parseDouble(value.toString()) == 0.0 || (record.isDirty() && !record.isModified("quantity"))))
					return false;
				if(field.equals("rate") && (Double.parseDouble(value.toString()) == 0.0 || (record.isDirty() && !record.isModified("rate"))))
					return false;					
				
				//Other errors (with messages)
				String error = null;
				if(record.getAsString("packageNo") != null && uneditablePackageNos.contains(record.getAsString("packageNo")) 
						&& record.getAsString("objectCode").startsWith("14"))
					error = "This package is submitted or awarded - please use the 'Subcontract Addendum' window to edit this resource";
				/*else if(record.getAsString("packageNo") != null && uneditableUnawardedPackageNos.contains(record.getAsString("packageNo")))
					error = "Selected field cannot be edited - this package has Payment Requisition submitted.";
				else if(record.getAsString("packageNo") != null && uneditableResourceIDs.contains(record.getAsString("id")))
					error = "Selected field cannot be edited - this resource is being used in Payment Requisition.";*/
				else if(field.equals("packageNo") && !(record.getAsString("objectCode").startsWith("14") || record.getAsString("objectCode").startsWith("13")))
					error = "Only resources with an object code beginning with '14' can be added to a package.";
				if(error != null){
					if(record.getId().equals(lastEditedId)) //Don't display error message on first attempt to edit (when a user presses the 'enter' key to finish editing, the grid automatically tries to edit the next record)
						MessageBox.alert(error);
					else
						lastEditedId = record.getId();
					return false;
				}
				lastEditedId = record.getId();
				return true;
			}

			public boolean doValidateEdit(GridPanel grid, final Record record,
					final String field, final Object value, Object originalValue,
					int rowIndex, int colIndex) {
				
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
				/*else if(record.getAsString("packageNo") != null && uneditableUnawardedPackageNos.contains(record.getAsString("packageNo")))
					error = "Selected field cannot be edited - this package has Payment Requisition submitted.";*/
				
				
				if(error != null){
					MessageBox.alert(error);
					return false;
				}
				// added by brian on 20110311 - end
				
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
						if(!record.getAsString("objectCode").startsWith("14")){
							MessageBox.alert("To assign a Resource to a package, Object Code must begin with '14'<br/>" + 
									"Resources with object code beginning with '13' can only be removed from a package");
							return false;
						}
						if(((String)value).startsWith("3") && !record.getAsString("subsidiaryCode").startsWith("3")){
							MessageBox.alert("Resources in NSC packages (beginning with '3') must have a subsidiary code beginning with '3'");
							return false;
						}
					}
				}
				else if(field.equals("quantity")){
					double newQuant = RoundingUtil.round(Double.parseDouble(value.toString()), quantDP);
					double oldQuant = RoundingUtil.round(record.getAsDouble("originalQuantity"), quantDP);
					if(newQuant > oldQuant){
						MessageBox.alert("Quantity cannot be increased above the original value (" + oldQuant + ")");
						return false;
					}
					else if(newQuant < 0.0){
						MessageBox.alert("Quantity must be larger than 0");
						return false;
					}
				}
				else if(field.equals("rate")){
					double newRate = RoundingUtil.round(Double.parseDouble(value.toString()), rateDP);
					double oldRate = RoundingUtil.round(record.getAsDouble("originalRate"), rateDP);
					if(newRate > oldRate){
						MessageBox.alert("Rate cannot be increased above the original value (" + oldRate + ")");
						return false;
					}
					else if(newRate == 0.0 || newRate/oldRate < 0.0){ //Don't need to check if oldRate == 0, because editing wouldn't have been allowed if it was
						MessageBox.alert("The new rate must have the same sign (+/-) as the original rate");
						return false;
					}
				}
				return true;
			}

			public void onAfterEdit(GridPanel grid, Record record,
					String field, Object newValue, Object oldValue,
					int rowIndex, int colIndex) {
				
				/*if(record.getAsString("packageNo") != null && uneditableUnawardedPackageNos.contains(record.getAsString("packageNo"))){
					MessageBox.alert("Selected field cannot be edited - this package has Payment Requisition submitted.");
					record.set("packageNo", "0");
					return;
				}*/
				
				if(field.equals("objectCode")){
					String objectCodeType = ((String)newValue).substring(0, 2);
					String resourceType = "";
					if("11".equals(objectCodeType))
						resourceType = "LR";
					else if("12".equals(objectCodeType))
						resourceType = "PT";
					else if("13".equals(objectCodeType))
						resourceType = "ML";
					else if("14".equals(objectCodeType))
						resourceType = "SC";
					else if("15".equals(objectCodeType))
						resourceType = "OS";
					else if("19".equals(objectCodeType))
						resourceType = "IC";
					record.set("resourceType", resourceType);
				}
				else if(field.equals("quantity")){
					double quantity = Double.parseDouble(newValue.toString());
					double rate = record.getAsDouble("rate");
					record.set("amount", Double.toString(quantity * rate));
					double originalQuantity = RoundingUtil.round(record.getAsDouble("originalQuantity"), rateDP);
					if(quantity == originalQuantity)
						record.commit();
				}
				else if(field.equals("rate")){
					double quantity = record.getAsDouble("quantity");
					double rate = Double.parseDouble(newValue.toString());
					record.set("amount", Double.toString(quantity * rate));
					double originalRate = RoundingUtil.round(record.getAsDouble("originalRate"), rateDP);
					if(rate == originalRate)
						record.commit();
				}
				else if(field.equals("packageNo")){
					if(newValue == null || newValue.toString().trim().length() == 0)
						record.set("packageNo", "0");
				}
			}
		});
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(resourceRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		resultEditorGridPanel.setStore(this.dataStore);
		
		GridView view = new CustomizedGridView();
		view.setAutoFill(false);
		view.setForceFit(false);
		resultEditorGridPanel.setView(view);
		
		//Split/Merge/Balance buttons
		splitButton = new ToolbarButton("Split");
		splitButton.setTooltip("Split Resource", "Split a resource into two or more resources");
		splitButton.setCls("toolbar-button");
		splitButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				showSplitWindow();
			};
		});
		mergeButton = new ToolbarButton("Merge");
		mergeButton.setCls("toolbar-button");
		mergeButton.setTooltip("Merge Resources", "Merge two or more resources into a single resource");
		mergeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				showMergeWindow();
			};
		});
		balanceButton = new ToolbarButton("Balance");
		balanceButton.setCls("toolbar-button");
		balanceButton.setTooltip("Balance Resource(s)", "Reassign the quantity/rate of one or more resources");
		balanceButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				getResourcesAndBalance();
			};
		});
		resultEditorGridPanel.setTopToolbar(new Button[]{splitButton, mergeButton, balanceButton});
		splitButton.hide();
		mergeButton.hide();
		balanceButton.hide();
		
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
					globalSectionController.closeCurrentWindow();
				else{
					MessageBox.confirm("Confirm", "Are you sure you want to discard the changes?",   
	                        new MessageBox.ConfirmCallback() {   
	                            public void execute(String btnID) {
	                            	if (btnID.equals("yes"))
	                            		globalSectionController.closeCurrentWindow();
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
			globalSectionController.getUserAccessRightsRepository().getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010115_REPACKAGING_UPDATE_BY_RESOURCE_WINDOW, new AsyncCallback<List<String>>(){
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
			splitButton.show();
			mergeButton.show();
			balanceButton.show();
			saveButton.show();
		}
	}

	private void populateGrid(RepackagingPaginationWrapper<Resource> wrapper){
		this.doLayout();
		dataStore.rejectChanges();
		dataStore.removeAll();
		
		paginationToolbar.setTotalPage(wrapper.getTotalPage());
		paginationToolbar.setCurrentPage(wrapper.getCurrentPage());
		if(wrapper.getCurrentPageContentList() == null)
			return;
		for(Resource resource : wrapper.getCurrentPageContentList()){
			String bpi = "";
			bpi += resource.getRefBillNo() == null ? "/" : resource.getRefBillNo() + "/";
			bpi += resource.getRefSubBillNo() == null ? "/" : resource.getRefSubBillNo() + "/";
			bpi += resource.getRefSectionNo() == null ? "/" : resource.getRefSectionNo() + "/";
			bpi += resource.getRefPageNo() == null ? "/" : resource.getRefPageNo() + "/";
			bpi += resource.getRefItemNo() == null ? "" : resource.getRefItemNo();
			Double quantity = resource.getQuantity() * resource.getRemeasuredFactor();
			Record record = resourceRecordDef.createRecord(new Object[]{
					resource.getId(),
					bpi,
					resource.getPackageNo(),
					resource.getObjectCode(),
					resource.getSubsidiaryCode(),
					resource.getDescription(),
					resource.getUnit(),
					quantity,
					resource.getRemeasuredFactor(),
					resource.getCostRate(),
					quantity * resource.getCostRate(),
					resource.getResourceType(),
					quantity, //original quant
					resource.getCostRate(), //original rate
					//Reference only
					resource.getIvPostedAmount(),
					resource.getIvCumAmount()
			});
			dataStore.add(record);
		}
	}
	
	private void search(){
		jobNumber = globalSectionController.getJob().getJobNumber();
		billNo = billNoField.getValueAsString();
		subBillNo = subBillNoField.getValueAsString();
		pageNo = pageNoField.getValueAsString();
		itemNo = itemNoField.getValueAsString();
		packageNo = packageNoField.getValueAsString();
		objectCode = objectCodeField.getValueAsString();
		subsidiaryCode = subsidiaryCodeField.getValueAsString();
		description = descriptionField.getValueAsString();
		searchByPage(0);
	}
	
	private void searchByPage(int pageNum){
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Searching", true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getBqRepository().searchResourcesByPage(jobNumber, billNo, subBillNo, pageNo, itemNo, packageNo, 
				objectCode, subsidiaryCode, description, pageNum, new AsyncCallback<RepackagingPaginationWrapper<Resource>>(){
			public void onSuccess(RepackagingPaginationWrapper<Resource> wrapper) {
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
		List<Resource> resources = new ArrayList<Resource>(records.length);
		for(Record record : records){
			Resource resource = resourceFromRecord(record);
			if(resource == null)
				return;
			resources.add(resource);
		}
		
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Saving...", true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getBqRepository().saveResourceUpdates(resources, new AsyncCallback<String>(){
			public void onSuccess(String errorMsg) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				if(errorMsg == null){
					MessageBox.alert("Save successful");
					dataStore.commitChanges();
					repackagingListGridPanel.updateStatus("200");
				}
				else
					MessageBox.alert(errorMsg);
			}
			
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				UIUtil.checkSessionTimeout(e, false,globalSectionController.getUser());
			}
		});
	}
	
	public void saveSplitMergeResources(Record[] oldRecords, Record[] newRecords){
		List<Resource> oldResources = new ArrayList<Resource>(oldRecords.length);
		List<Resource> newResources = new ArrayList<Resource>(newRecords.length);
		for(Record record : oldRecords){
			Resource resource = resourceFromRecord(record);
			if(resource == null)
				return;
			oldResources.add(resource);
		}
		for(Record record : newRecords){
			Resource resource = resourceFromRecord(record);
			if(resource == null)
				return;
			newResources.add(resource);
		}
		
		UIUtil.maskPanelById(MergeOrSplitResourceWindow.MAIN_PANEL_ID, "Saving...", true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getBqRepository().saveSplitMergeResources(oldResources, newResources, new AsyncCallback<String>(){
			public void onSuccess(String errorMsg) {
				UIUtil.unmaskPanelById(MergeOrSplitResourceWindow.MAIN_PANEL_ID);
				if(errorMsg == null){
					MessageBox.alert("Save successful");
					closeChildWindow();
					repackagingListGridPanel.updateStatus("200");
					searchByPage(0);
				}
				else
					MessageBox.alert(errorMsg);
			}
			
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(MergeOrSplitResourceWindow.MAIN_PANEL_ID);
				UIUtil.checkSessionTimeout(e, false,globalSectionController.getUser());
			}
		});
	}
	
	public void saveBalancedResources(Record[] records){
		List<Resource> resources = new ArrayList<Resource>(records.length);
		for(Record record : records){
			Resource resource = resourceFromRecord(record);
			if(resource == null)
				return;
			resources.add(resource);
		}
		
		UIUtil.maskPanelById(BalanceResourcesWindow.MAIN_PANEL_ID, "Saving...", true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getBqRepository().saveBalancedResources(resources, null, new AsyncCallback<String>(){
			public void onSuccess(String errorMsg) {
				UIUtil.unmaskPanelById(BalanceResourcesWindow.MAIN_PANEL_ID);
				if(errorMsg == null){
					MessageBox.alert("Save successful");
					closeChildWindow();
					repackagingListGridPanel.updateStatus("200");
					searchByPage(0);
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
	
	public void showSplitWindow(){
		/*uneditableUnawardedPackageNos = globalSectionController.getUneditableUnawardedPackageNos();
		uneditableResourceIDs = globalSectionController.getUneditableResourceIDs();*/
		if(childWindow != null){
			MessageBox.alert("Please close other split/merge/balance windows");
			return;
		}
		if(cbSelectionModel.getCount() != 1){
			MessageBox.alert("Please select only 1 resource to split");
			return;
		}
		Record record = cbSelectionModel.getSelected();
		if(record.isDirty()){
			MessageBox.alert("Please save changes before splitting");
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
		if(record.getAsString("packageNo") != null && uneditableResourceIDs.contains(record.getAsString("id"))){
			MessageBox.alert("Selected field cannot be split - this resource is being used in Payment Requisition.");
			return;
		}*/
		/**
		 * Allowing Method 3 to split resource even IV has posted
		 * @author peterchan
		 * 
		 * @author tikywong
		 * April 18, 2011
		 */
//		if(globalSectionController.getJob().getRepackagingType().equals("3") &&
//			(record.isNull("postedIVAmount") || record.isEmpty("postedIVAmount") || record.getAsDouble("postedIVAmount") != 0.00 ||
//			 record.isNull("cumulativeIVAmount") || record.isEmpty("cumulativeIVAmount") || record.getAsDouble("cumulativeIVAmount") != 0.00)){
//			MessageBox.alert("Selected resource cannot be split - posted IV Amount or cumulative IV Amount are not zero.");
//			return;
//		}
		MergeOrSplitResourceWindow splitWindow = new MergeOrSplitResourceWindow(this, MergeOrSplitResourceWindow.SPLIT);
		splitWindow.populateGrid(new Record[]{record});
		childWindow = splitWindow;
		childWindow.show();
	}
	
	public void showMergeWindow(){
		/*uneditableUnawardedPackageNos = globalSectionController.getUneditableUnawardedPackageNos();
		uneditableResourceIDs = globalSectionController.getUneditableResourceIDs();*/
		if(childWindow != null){
			MessageBox.alert("Please close other split/merge/balance windows");
			return;
		}
		if(cbSelectionModel.getCount() < 2){
			MessageBox.alert("Please select at least 2 resources to merge");
			return;
		}
		Record[] records = cbSelectionModel.getSelections();
		double lastAmount = records[0].getAsDouble("amount");
		String bpi = records[0].getAsString("bpi");
		for(Record record : records){
			if(record.isDirty()){
				MessageBox.alert("Please save changes before merging");
				return;
			}
			if(!bpi.equals(record.getAsString("bpi"))){
				MessageBox.alert("Resources must be in the same bill item");
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
			if(record.getAsString("packageNo") != null && uneditablePackageNos.contains(record.getAsString("packageNo"))
					&& record.getAsString("objectCode").startsWith("14")){
				MessageBox.alert("Resource " + record.getAsString("description") + " cannot be merged - package has been submitted or awarded");
				return;
			}
			/*if(record.getAsString("packageNo") != null && uneditableUnawardedPackageNos.contains(record.getAsString("packageNo"))){
				MessageBox.alert("Resource " + record.getAsString("description") + " cannot be merged - this package has Payment Requisition submitted.");
				return;
			}
			if(record.getAsString("packageNo") != null && uneditableResourceIDs.contains(record.getAsString("id"))){
				MessageBox.alert("Resource " + record.getAsString("description") + " cannot be merged - this resource is being used in Payment Requisition.");
				return;
			}*/
			/**
			 * Remove it
			 * Allow split/merge the resources even there is IV.
			 * @author peterchan
			 * 
			 * 
			 * @author tikywong
			 * April 18, 2011
			 */
//			if(globalSectionController.getJob().getRepackagingType().equals("3") &&
//					(record.isNull("postedIVAmount") || record.isEmpty("postedIVAmount") || record.getAsDouble("postedIVAmount") != 0.00 ||
//					 record.isNull("cumulativeIVAmount") || record.isEmpty("cumulativeIVAmount") || record.getAsDouble("cumulativeIVAmount") != 0.00)){
//					MessageBox.alert("Resource " + record.getAsString("description") + " cannot be merged - posted IV Amount or cumulative IV Amount is not zero.");
//					return;
//				}
		}
		MergeOrSplitResourceWindow splitWindow = new MergeOrSplitResourceWindow(this, MergeOrSplitResourceWindow.MERGE);
		splitWindow.populateGrid(records);
		childWindow = splitWindow;
		childWindow.show();
	}
	
	private void getResourcesAndBalance(){
		if(childWindow != null){
			MessageBox.alert("Please close other split/merge/balance windows");
			return;
		}
		
		Record[] records = cbSelectionModel.getSelections();
		if(records == null || records.length == 0){
			MessageBox.alert("Please select BQ Item (resource) to balance");
			return;
		}
		else if(records.length > 1){
			MessageBox.alert("Please select just one resource - all resources from the same BQ Item will be automatically selected");
			return;
		}
		
		/**
		 * Remove it
		 * Allow split/merge the resources even there is IV.
		 * @author peterchan
		 * 
		 * @author tikywong
		 * April 18, 2011
		 */
//		if(globalSectionController.getJob().getRepackagingType().equals("3") &&
//				(records[0].isNull("postedIVAmount") || records[0].isEmpty("postedIVAmount") || records[0].getAsDouble("postedIVAmount") != 0.00 ||
//						records[0].isNull("cumulativeIVAmount") || records[0].isEmpty("cumulativeIVAmount") || records[0].getAsDouble("cumulativeIVAmount") != 0.00)){
//				MessageBox.alert("Selected resource cannot be balanced - posted IV Amount or cumulative IV Amount are not zero.");
//				return;
//			}
		
		String jobNumber = globalSectionController.getJob().getJobNumber();
		String bpi = records[0].getAsString("bpi");
		
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Retrieving Resources", true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getBqRepository().getResourcesByBpi(jobNumber, bpi, new AsyncCallback<List<Resource>>(){
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				UIUtil.checkSessionTimeout(e, false,globalSectionController.getUser());
			}

			public void onSuccess(List<Resource> resources) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				showBalanceWindow(resources);
			}
			
		});
	}
	
	private void showBalanceWindow(List<Resource> resources){
		BalanceResourcesWindow balanceWindow = new BalanceResourcesWindow(this);
		balanceWindow.populateGrid(resources);
		childWindow = balanceWindow;
		childWindow.show();
	}
	
	public void closeChildWindow(){
		childWindow.close();
		childWindow = null;
	}
	
	public GlobalSectionController getGlobalSectionController(){
		return globalSectionController;
	}
}