package com.gammon.qs.client.ui.window.mainSection;

import java.util.List;
import java.util.Map;

import com.gammon.qs.application.GeneralPreferencesKey;
import com.gammon.qs.application.User;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemote;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRendererCheckIfTotal;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.util.RoundingUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
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

public class MergeOrSplitResourceWindow extends Window {
	public static final String MAIN_PANEL_ID = "mergeOrSplitResourceWindow";
	public static final String MERGE = "Merge";
	public static final String SPLIT = "Split";
	
	private double oldTotalAmount;
	private double newTotalAmount;
	private double oldPostedIVAmount;
	private double oldCumIVAmount;	
	private GlobalSectionController globalSectionController;
	
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	
	private Button saveButton;
		
	private Panel mainPanel;
	private Panel topPanel;
	private Panel bottomPanel;
	private GridPanel topGridPanel;
	private EditorGridPanel bottomEditGridPanel;
	
	//data store
	private Store oldDataStore;
	private Store newDataStore;
	
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
					new StringFieldDef("postedIVAmount"),
					new StringFieldDef("cumulativeIVAmount")
				}
			);
	
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
	private ColumnConfig postedIVAmountColumn;
	private ColumnConfig cumIVAmountColumn;
	
	private ComboBox unitComboBox;
	private ComboBox packageComboBox;
	private GlobalMessageTicket globalMessageTicket;
	private double newPostedIVAmount;
	private double newCumIVAmount;

	
	public MergeOrSplitResourceWindow(final RepackagingUpdateByResourceWindow parentWindow, final String type){
		super();
		this.setModal(true);
		this.globalSectionController = parentWindow.getGlobalSectionController();
		globalMessageTicket = new GlobalMessageTicket();
		if(type.equals(MERGE))
			this.setTitle("Merge Resources");
		else
			this.setTitle("Split Resource");
		this.setPaddings(5);
		this.setWidth(1024);
		this.setHeight(500);
		this.setClosable(false);
		this.setLayout(new FitLayout());
		
		userAccessRightsRepository = (UserAccessRightsRepositoryRemoteAsync) GWT.create(UserAccessRightsRepositoryRemote.class);
		((ServiceDefTarget)userAccessRightsRepository).setServiceEntryPoint(GlobalParameter.USER_ACCESS_RIGHTS_REPOSITORY_URL);
		
		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setId(MAIN_PANEL_ID);
		topPanel = new Panel();
		topPanel.setLayout(new FitLayout());
		bottomPanel = new Panel();
		bottomPanel.setLayout(new FitLayout());
		
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
		
		User user = globalSectionController.getUser();
		Map<String, String> prefs = user.getGeneralPreferences();
		String quantDP = prefs.get(GeneralPreferencesKey.QUANTITY_DECIMAL_PLACES);
		if(quantDP == null)
			quantDP = "2";
		String rateDP = prefs.get(GeneralPreferencesKey.RATE_DECIMAL_PLACES);
		if(rateDP == null)
			rateDP = "2";
		String amtDP = prefs.get(GeneralPreferencesKey.AMOUNT_DECIMAL_PLACES);
		if (amtDP == null)
			amtDP = "2";
		NumberField quantNumberField = new NumberField();
		quantNumberField.setDecimalPrecision(Integer.valueOf(quantDP));
		NumberField rateNumberField = new NumberField();
		rateNumberField.setDecimalPrecision(Integer.valueOf(rateDP));
		NumberField amountNumberField = new NumberField();
		amountNumberField.setDecimalPrecision(Integer.valueOf(amtDP));
		NumberField postedAmountNumberField = new NumberField();
		postedAmountNumberField.setDecimalPrecision(Integer.valueOf(amtDP));

		
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
		postedIVAmountColumn = new ColumnConfig("Posted IV Amount", "postedIVAmount", 120, false);
		postedIVAmountColumn.setRenderer(amountRenderer);
		postedIVAmountColumn.setAlign(TextAlign.RIGHT);
		postedIVAmountColumn.setEditor(new GridEditor(postedAmountNumberField));
		postedIVAmountColumn.setTooltip("Posted IV Amount");
		cumIVAmountColumn = new ColumnConfig("Cum IV Amount", "cumulativeIVAmount", 120, false);
		cumIVAmountColumn.setRenderer(amountRenderer);
		cumIVAmountColumn.setAlign(TextAlign.RIGHT);
		cumIVAmountColumn.setEditor(new GridEditor(amountNumberField));
		cumIVAmountColumn.setTooltip("Cum IV Amount");

		
		
		ColumnConfig[] columns;
		if ("3".equals(globalSectionController.getJob().getRepackagingType().trim()))
			columns = new ColumnConfig[]{
				bpiColumn,
				packageNoColumn,
				objectCodeColumn,
				subsidiaryCodeColumn,
				descriptionColumn,
				unitColumn,
				quantityColumn,
				rateColumn,
				amountColumn,
				resourceTypeColumn,
				postedIVAmountColumn,
				cumIVAmountColumn
			};
		else
			columns = new ColumnConfig[]{
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
		
		topGridPanel.setColumnModel(new ColumnModel(columns));
		bottomEditGridPanel.setColumnModel(new ColumnModel(columns));
		
		//Configure datastores
		MemoryProxy oldProxy = new MemoryProxy(new Object[][]{});
		ArrayReader oldReader = new ArrayReader(resourceRecordDef);
		oldDataStore = new Store(oldProxy, oldReader);
		oldDataStore.load();
		topGridPanel.setStore(oldDataStore);
		MemoryProxy newProxy = new MemoryProxy(new Object[][]{});
		ArrayReader newReader = new ArrayReader(resourceRecordDef);
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
		//add grids to panels to main panel to window.
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
				else if(field.equals("subsidiaryCode") && (value == null || ((String)value).length() != 8)){
					MessageBox.alert("Subsidiary code must be 8 digits in length");
					return false;
				}
				else if(field.equalsIgnoreCase("quantity") || field.equalsIgnoreCase("rate")){
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
				}
				return true;
			}
			
			public void onAfterEdit(GridPanel grid, Record record,
					String field, Object newValue, Object oldValue,
					int rowIndex, int colIndex) {
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
				else if(field.equals("packageNo")){
					if(newValue == null || newValue.toString().trim().length() == 0)
						record.set("packageNo", "0");
				}else if(field.equals("postedIVAmount")){
					double dblNewValue = 0.0;
					if (newValue!=null&&!"".equals(newValue.toString().trim()))
						dblNewValue = Double.parseDouble(newValue.toString().trim());
					double dblOldValue = 0.0;
					if (oldValue!=null&&!"".equals(oldValue.toString().trim()))
						dblOldValue  = Double.parseDouble(oldValue.toString().trim());
					newPostedIVAmount = newPostedIVAmount-dblOldValue+dblNewValue;
					newDataStore.getAt(newDataStore.getCount()-1).set("postedIVAmount", Double.toString(newPostedIVAmount));
				}else if(field.equals("cumulativeIVAmount")){
					double dblNewValue = 0.0;
					if (newValue!=null&&!"".equals(newValue.toString().trim()))
						dblNewValue = Double.parseDouble(newValue.toString().trim());
					double dblOldValue = 0.0;
					if (oldValue!=null&&!"".equals(oldValue.toString().trim()))
						dblOldValue  = Double.parseDouble(oldValue.toString().trim());
					newCumIVAmount = newCumIVAmount-dblOldValue+dblNewValue;
					newDataStore.getAt(newDataStore.getCount()-1).set("cumulativeIVAmount", Double.toString(newCumIVAmount));
				}
			}
		});
		
		saveButton = new Button(type);
		saveButton.addListener(new ButtonListenerAdapter(){
				public void onClick(Button button, EventObject e) {
					globalMessageTicket.refresh();
					bottomEditGridPanel.stopEditing();
					if(RoundingUtil.round(oldTotalAmount, 4) != RoundingUtil.round(newTotalAmount, 4)){
						MessageBox.alert("Total amounts must match!");
						return;
					}
					if(type.equals(SPLIT) && newDataStore.getCount() < 3){
						MessageBox.alert("Please split into at least 2 new resources");
						return;
					}
					if ("3".equals(globalSectionController.getJob().getRepackagingType().trim())){
						if (RoundingUtil.round(oldPostedIVAmount, 4)!=RoundingUtil.round(newPostedIVAmount, 4)){
							MessageBox.alert("Posted IV amount must match");
							return;
						}
						if (RoundingUtil.round(oldCumIVAmount, 4)!=RoundingUtil.round(newCumIVAmount, 4)){
							MessageBox.alert("Cumulative IV amount must match");
							return;
						}
					}
					parentWindow.saveSplitMergeResources(oldDataStore.getRange(0, oldDataStore.getCount()-2), newDataStore.getRange(0, newDataStore.getCount()-2));
				};
		});
		
		Button closeButton = new Button("Close");
		closeButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, EventObject e) {
					parentWindow.closeChildWindow();				
				};
		});		
		
		this.setButtons(new Button[]{saveButton, closeButton});
		saveButton.hide();
		
		// Check for access rights - then add toolbar buttons accordingly
		try{
			UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010113_MERGE_OR_SPLIT_RESOURCE_WINDOW, new AsyncCallback<List<String>>(){
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
		if(accessRightsList != null && accessRightsList.contains("WRITE"))
			saveButton.show();
	}
	
	public void populateGrid(Record[] records){
		if(records.length == 0)
			return;
		
		oldDataStore.removeAll();
		oldTotalAmount = 0;
		oldPostedIVAmount = 0;
		oldCumIVAmount = 0;
		if ("3".equals(globalSectionController.getJob().getRepackagingType().trim()))
			for(Record record: records){
				oldTotalAmount += record.getAsDouble("amount");
				oldPostedIVAmount += record.getAsDouble("postedIVAmount");
				oldCumIVAmount += record.getAsDouble("cumulativeIVAmount");
				oldDataStore.add(record);
			}
		else
			for(Record record: records){
				oldTotalAmount += record.getAsDouble("amount");
				oldDataStore.add(record);
			}
		Record oldAmountRecord = resourceRecordDef.createRecord(new Object[14]);
		oldAmountRecord.set("amount", Double.toString(oldTotalAmount));
		oldAmountRecord.set("isTotal", true);
		if ("3".equals(globalSectionController.getJob().getRepackagingType().trim())){
			oldAmountRecord.set("postedIVAmount", Double.toString(oldPostedIVAmount));
			oldAmountRecord.set("cumulativeIVAmount", Double.toString(oldCumIVAmount));
		}
			
		oldDataStore.add(oldAmountRecord);
		oldAmountRecord.commit();

		newDataStore.removeAll();
		newTotalAmount = 0;
		newPostedIVAmount = 0.0;
		newCumIVAmount = 0.0;
		Record newRecord = resourceRecordDef.createRecord(new Object[14]);
		Record record = records[0];
		newRecord.set("bpi", record.getAsString("bpi"));
		newRecord.set("remeasuredFactor", record.getAsString("remeasuredFactor"));
		newRecord.set("quantity", "0");
		newRecord.set("rate", "0");
		newRecord.set("amount", "0");
		newRecord.set("packageNo", record.getAsString("packageNo"));
		newRecord.set("objectCode", record.getAsString("objectCode"));
		newRecord.set("subsidiaryCode", record.getAsString("subsidiaryCode"));
		newRecord.set("resourceDescription", record.getAsString("resourceDescription"));
		newRecord.set("unit", record.getAsString("unit"));
		newRecord.set("resourceType", record.getAsString("resourceType"));
		if ("3".equals(globalSectionController.getJob().getRepackagingType().trim())){
			newRecord.set("cumulativeIVAmount", "0");
			newRecord.set("postedIVAmount", "0");			
		}
		newDataStore.add(newRecord);
		Record newAmountRecord = resourceRecordDef.createRecord(new Object[14]);
		newAmountRecord.set("amount", "0");
		if ("3".equals(globalSectionController.getJob().getRepackagingType().trim())){
			newAmountRecord.set("cumulativeIVAmount", "0");
			newAmountRecord.set("postedIVAmount", "0");	
		}
		newAmountRecord.set("isTotal", true);
		newDataStore.add(newAmountRecord);
		newAmountRecord.commit();
	}
	
	private void addEmptyResource(){
		Record record = oldDataStore.getAt(0);
		Record newRecord = resourceRecordDef.createRecord(new Object[14]);
		newRecord.set("bpi", record.getAsString("bpi"));
		newRecord.set("packageNo", record.getAsString("packageNo"));
		newRecord.set("objectCode", record.getAsString("objectCode"));
		newRecord.set("subsidiaryCode", record.getAsString("subsidiaryCode"));
		newRecord.set("resourceDescription", record.getAsString("resourceDescription"));
		newRecord.set("unit", record.getAsString("unit"));
		newRecord.set("remeasuredFactor", record.getAsString("remeasuredFactor"));
		newRecord.set("resourceType", record.getAsString("resourceType"));
		newRecord.set("quantity", "0");
		newRecord.set("rate", "0");
		newRecord.set("amount", "0");
		if ("3".equals(globalSectionController.getJob().getRepackagingType().trim())){
			newRecord.set("cumulativeIVAmount", "0");
			newRecord.set("postedIVAmount", "0");	
		}
		newDataStore.insert(newDataStore.getCount()-1, newRecord);
		newDataStore.commitChanges();	
	}
}
