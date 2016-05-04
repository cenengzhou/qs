package com.gammon.qs.client.ui.window.mainSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gammon.qs.application.GeneralPreferencesKey;
import com.gammon.qs.application.User;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.BQRepositoryRemote;
import com.gammon.qs.client.repository.BQRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemote;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRendererCheckIfTotal;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.BQItem;
import com.gammon.qs.domain.Resource;
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
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.CellMetadata;
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

public class AddResourcesToBqCoWindow extends Window{
	public static final String MAIN_PANEL_ID = "addBqItemWindow";
	private GlobalSectionController globalSectionController;
	private RepackagingUpdateByBQWindow repackagingUpdateByBQWindow;
	private Panel mainPanel;
	private GridPanel bqGridPanel;
	private EditorGridPanel resourceGridPanel;
	private GlobalMessageTicket globalMessageTicket;
	
	private BQRepositoryRemoteAsync bqRepository;
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	
	private Button saveButton;
	
	private Store bqDataStore;
	private Store resourceDataStore;
	
	private Store packageStore;
	private ComboBox unitComboBox;
	private ComboBox packageComboBox;
	
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
					new StringFieldDef("costAmount")
				}
			);
	
	private ColumnConfig bpiColumn;
	private ColumnConfig bqTypeColumn;
	private ColumnConfig descriptionColumn;
	private ColumnConfig unitColumn;
	private ColumnConfig quantityColumn;
	private ColumnConfig sellingRateColumn;
	private ColumnConfig sellingAmountColumn;
	private ColumnConfig costRateColumn;
	private ColumnConfig costAmountColumn;
	
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
					new StringFieldDef("resourceType")
				}
			);
	private ColumnConfig resourceBpiColumn;
	private ColumnConfig resourcePackageNoColumn;
	private ColumnConfig resourceObjectCodeColumn;
	private ColumnConfig resourceSubsidiaryCodeColumn;
	private ColumnConfig resourceDescriptionColumn;
	private ColumnConfig resourceUnitColumn;
	private ColumnConfig resourceQuantityColumn;
	private ColumnConfig resourceRateColumn;
	private ColumnConfig resourceAmountColumn;
	private ColumnConfig resourceTypeColumn;
	
	private BQItem bqItem;
	private String bpi;
	private List<Resource> resources;
	private double bqTotalCost;
	private double resourceTotalCost;
	
	public AddResourcesToBqCoWindow(final RepackagingUpdateByBQWindow repackagingUpdateByBQWindow){
		super();
		this.setModal(true);
		this.setClosable(false);
		this.repackagingUpdateByBQWindow = repackagingUpdateByBQWindow;
		globalSectionController = repackagingUpdateByBQWindow.getGlobalSectionController();
		globalMessageTicket = new GlobalMessageTicket();
		
		bqRepository = (BQRepositoryRemoteAsync)GWT.create(BQRepositoryRemote.class);
		((ServiceDefTarget)bqRepository).setServiceEntryPoint(GlobalParameter.BQ_REPOSITORY_URL);
		userAccessRightsRepository = (UserAccessRightsRepositoryRemoteAsync) GWT.create(UserAccessRightsRepositoryRemote.class);
		((ServiceDefTarget)userAccessRightsRepository).setServiceEntryPoint(GlobalParameter.USER_ACCESS_RIGHTS_REPOSITORY_URL);
		
		this.setTitle("BQ Change Order - Add/Edit Resources");
		this.setPaddings(5);
		this.setWidth(1100);
		this.setHeight(650);
		this.setClosable(false);
		this.setLayout(new FitLayout());
		
		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setId(MAIN_PANEL_ID);
		bqGridPanel = new GridPanel();
		bqGridPanel.setHeight(50); //Only need space for one row
		resourceGridPanel = new EditorGridPanel();
		
		final Renderer quantityRenderer = new QuantityRenderer(globalSectionController.getUser());
		final Renderer amountRenderer = new AmountRendererCheckIfTotal(globalSectionController.getUser());
		final Renderer rateRenderer = new RateRenderer(globalSectionController.getUser());

		User user = globalSectionController.getUser();
		Map<String, String> prefs = user.getGeneralPreferences();
		String quantDP = prefs.get(GeneralPreferencesKey.QUANTITY_DECIMAL_PLACES);
		if(quantDP == null)
			quantDP = "1";
		NumberField quantNumberField = new NumberField();
		quantNumberField.setDecimalPrecision(Integer.valueOf(quantDP));
		String rateDP = prefs.get(GeneralPreferencesKey.QUANTITY_DECIMAL_PLACES);
		if(rateDP == null)
			rateDP = "1";
		NumberField rateNumberField = new NumberField();
		rateNumberField.setDecimalPrecision(Integer.valueOf(rateDP));
		
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
		packageStore = globalSectionController.getUnawardedPackageStore();
		if(packageStore == null)
			packageStore = new SimpleStore(new String[]{"packageNo", "description", "status"}, new String[][]{});
		packageStore.load();
		packageComboBox.setDisplayField("description");
		packageComboBox.setValueField("packageNo");
		packageComboBox.setSelectOnFocus(true);
		packageComboBox.setForceSelection(true);
		packageComboBox.setListWidth(200);
		packageComboBox.setStore(packageStore);
		
		//BQ Item columns
		bpiColumn = new ColumnConfig("Bill Item", "bpi", 120, false);
		bqTypeColumn = new ColumnConfig("Type", "bqType", 35, false);
		descriptionColumn = new ColumnConfig("Description", "description", 220, false);
		unitColumn = new ColumnConfig("Unit", "unit", 35, false);
		quantityColumn = new ColumnConfig("Qty", "quantity", 100, false);
		quantityColumn.setRenderer(quantityRenderer);		
		quantityColumn.setAlign(TextAlign.RIGHT);
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
		costAmountColumn.setRenderer(new Renderer(){
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				String str = amountRenderer.render(value, cellMetadata, record, rowIndex, colNum, store);
				return "<b>" + str + "</b>";
			}
		});
		costAmountColumn.setAlign(TextAlign.RIGHT);
		
		ColumnConfig[] bqColumns = new ColumnConfig[]{
			bpiColumn,
			bqTypeColumn,
			descriptionColumn,
			unitColumn,
			quantityColumn,
			sellingRateColumn,
			sellingAmountColumn,
			costRateColumn,
			costAmountColumn
		};
		bqGridPanel.setColumnModel(new ColumnModel(bqColumns));
		
		MemoryProxy bqProxy = new MemoryProxy(new Object[][]{});
		ArrayReader bqReader = new ArrayReader(bqItemRecordDef);
		bqDataStore = new Store(bqProxy, bqReader);
		bqDataStore.load();
		bqGridPanel.setStore(bqDataStore);
		
		GridView bqView = new CustomizedGridView();
		bqView.setAutoFill(true);
		bqGridPanel.setView(bqView);
		
		//Resource columns
		resourceBpiColumn = new ColumnConfig("B/P/I", "bpi", 120, false);
		resourcePackageNoColumn = new ColumnConfig("Package No.", "packageNo", 40, false);
		resourcePackageNoColumn.setEditor(new GridEditor(packageComboBox));
		resourcePackageNoColumn.setTooltip("Package No.");
		resourceObjectCodeColumn = new ColumnConfig("Object Code", "objectCode", 50, false);
		resourceObjectCodeColumn.setEditor(new GridEditor(new TextField()));
		resourceObjectCodeColumn.setTooltip("Object Code");
		resourceSubsidiaryCodeColumn = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 65, false);
		resourceSubsidiaryCodeColumn.setEditor(new GridEditor(new TextField()));
		resourceSubsidiaryCodeColumn.setTooltip("Subsidiary Code");
		resourceDescriptionColumn = new ColumnConfig("Description", "description", 250, false);
		resourceDescriptionColumn.setEditor(new GridEditor(new TextField()));
		resourceUnitColumn = new ColumnConfig("Unit", "unit", 40, false);
		resourceUnitColumn.setEditor(new GridEditor(unitComboBox));
		resourceQuantityColumn = new ColumnConfig("Qty", "quantity", 110, false);
		resourceQuantityColumn.setRenderer(quantityRenderer);		
		resourceQuantityColumn.setAlign(TextAlign.RIGHT);
		resourceQuantityColumn.setEditor(new GridEditor(quantNumberField));
		resourceRateColumn = new ColumnConfig("Rate", "rate", 120, false);
		resourceRateColumn.setRenderer(rateRenderer);
		resourceRateColumn.setAlign(TextAlign.RIGHT);
		resourceRateColumn.setEditor(new GridEditor(rateNumberField));
		resourceAmountColumn = new ColumnConfig("Amount", "amount", 120, false);
		resourceAmountColumn.setRenderer(amountRenderer);
		resourceAmountColumn.setAlign(TextAlign.RIGHT);
		resourceTypeColumn = new ColumnConfig("Type", "resourceType", 30, false);
		resourceTypeColumn.setTooltip("Resource Type");
		
		ColumnConfig[] resourceColumns = new ColumnConfig[]{
				resourceBpiColumn,
				resourcePackageNoColumn,
				resourceObjectCodeColumn,
				resourceSubsidiaryCodeColumn,
				resourceDescriptionColumn,
				resourceUnitColumn,
				resourceQuantityColumn,
				resourceRateColumn,
				resourceAmountColumn,
				resourceTypeColumn
		};
		resourceGridPanel.setColumnModel(new ColumnModel(resourceColumns));
		
		//resource grid listener (validation, etc)
		resourceGridPanel.addEditorGridListener(new EditorGridListener(){
			public boolean doBeforeEdit(GridPanel grid, Record record,
					String field, Object value, int rowIndex, int colIndex) {
				globalMessageTicket.refresh();
				String bqType = bqItem.getBqType();
				if(field.equals("subsidiaryCode") && ("CL".equals(bqType) || "DW".equals(bqType) || "VO".equals(bqType))){
					MessageBox.alert("The bq type is " + bqType + ". The subsidiary code cannot be edited");
					return false;
				}
				if(field.equals("packageNo") && !record.getAsString("objectCode").startsWith("14")){
					MessageBox.alert("Only resources with an object code beginning with '14' can be added to a package.");
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
				else if(field.equals("subsidiaryCode")){
					if(value == null || ((String)value).length() != 8){
						MessageBox.alert("Subsidiary code must be 8 digits in length");
						return false;
					}
				}
				else if(field.equals("packageNo")){
					if(value != null && ((String)value).length() > 1 && !record.getAsString("objectCode").startsWith("14")){
						MessageBox.alert("Resources in a package must have an object code beginning with '14'");
						return false;
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
				else if(field.equals("quantity")){
					double quantity = Double.parseDouble(newValue.toString());
					double rate = record.getAsDouble("rate");
					double newAmount = quantity * rate;
					double oldAmount = record.getAsDouble("amount");
					record.set("amount", Double.toString(newAmount));
					
					resourceTotalCost += newAmount - oldAmount;
					resourceDataStore.getAt(resourceDataStore.getCount() - 1).set("amount", Double.toString(resourceTotalCost)); 
				}
				else if(field.equals("rate")){
					double quantity = record.getAsDouble("quantity");
					double rate = Double.parseDouble(newValue.toString());
					double newAmount = quantity * rate;
					double oldAmount = record.getAsDouble("amount");
					record.set("amount", Double.toString(newAmount));
					
					resourceTotalCost += newAmount - oldAmount;
					resourceDataStore.getAt(resourceDataStore.getCount() - 1).set("amount", Double.toString(resourceTotalCost)); 
				}
				else if(field.equals("packageNo")){
					if(newValue == null || newValue.toString().trim().length() == 0)
						record.set("packageNo", "0");
				}
			}
		});
		
		MemoryProxy resourceProxy = new MemoryProxy(new Object[][]{});
		ArrayReader resourceReader = new ArrayReader(resourceRecordDef);
		resourceDataStore = new Store(resourceProxy, resourceReader);
		resourceDataStore.load();
		resourceGridPanel.setStore(resourceDataStore);
		
		GridView resourceView = new CustomizedGridView();
		resourceView.setAutoFill(true);
		resourceGridPanel.setView(resourceView);
		
		ToolbarButton addResourceButton = new ToolbarButton("Add Resource");
		addResourceButton.setTooltip("Add Resource", "Add a new resource to this bq item");
		addResourceButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				addResource();
			};
		});
		
		resourceGridPanel.setTopToolbar(addResourceButton);
		
		mainPanel.add(bqGridPanel);
		mainPanel.add(resourceGridPanel);
		this.add(mainPanel);
		
		saveButton = new Button("Save");
		saveButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				resourceGridPanel.stopEditing();
				save();
			};
		});
		
		Button closeButton = new Button("Close");
		closeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				repackagingUpdateByBQWindow.closeChildWindow();
			};
		});
		
		this.setButtons(new Button[]{saveButton, closeButton});
		saveButton.hide();
		
		// Check for access rights - then add toolbar buttons accordingly
		try{
			UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010111_ADD_RESOURCES_TO_BQ_CO_WINDOW, new AsyncCallback<List<String>>(){
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
	
	public void populateGrid(BQItem bqItem){
		//BQ Item
		this.bqItem = bqItem;
		bpi = "";
		bpi += bqItem.getRefBillNo() == null ? "/" : bqItem.getRefBillNo() + "/";
		bpi += bqItem.getRefSubBillNo() == null ? "/" : bqItem.getRefSubBillNo() + "/";
		bpi += bqItem.getRefSectionNo() == null ? "/" : bqItem.getRefSectionNo() + "/";
		bpi += bqItem.getRefPageNo() == null ? "/" : bqItem.getRefPageNo() + "/";
		bpi += bqItem.getItemNo() == null ? "" : bqItem.getItemNo();
		bqTotalCost = bqItem.getCostRate() * bqItem.getRemeasuredQuantity();
		Record bqRecord = bqItemRecordDef.createRecord(new Object[]{
				bqItem.getId(),
				bpi,
				bqItem.getBqType(),
				bqItem.getDescription(),
				bqItem.getUnit(),
				bqItem.getQuantity(),
				bqItem.getRemeasuredQuantity(),
				bqItem.getSellingRate(),
				bqItem.getSellingRate() * bqItem.getRemeasuredQuantity(),
				bqItem.getCostRate(),
				bqTotalCost
		});
		bqDataStore.add(bqRecord);
		
		//Resources
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Loading...", true);
		try{
			globalSectionController.getResourceRepository().obtainResourceListByBQItem(bqItem, new AsyncCallback<List<Resource>>(){

				@Override
				public void onFailure(Throwable e) {
					UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					UIUtil.throwException(e);
				}

				@Override
				public void onSuccess(List<Resource> result) {
					resources = result;
					UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				}
				
			});
		}catch(DatabaseOperationException e){
			UIUtil.throwException(e);
		}
		
		if(resources == null || resources.size() == 0)
			addResource();
		else{
			for(Resource resource : resources){
				Double quantity = resource.getQuantity() * resource.getRemeasuredFactor();
				Double resourceCost = quantity * resource.getCostRate();
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
						resourceCost,
						resource.getResourceType()					
				});
				resourceTotalCost += resourceCost;
				resourceDataStore.add(record);
			}
		}
		
		//add resource total cost row
		Record totalCostRecord = resourceRecordDef.createRecord(new Object[12]);
		totalCostRecord.set("amount", Double.toString(resourceTotalCost));
		totalCostRecord.set("isTotal", true);
		resourceDataStore.add(totalCostRecord);
	}
	
	private void addResource(){
		Record record = resourceRecordDef.createRecord(new Object[12]);
		record.set("bpi", bpi);
		record.set("packageNo", "0");
		record.set("quantity", "0");
		/**
		 * @author tikywong
		 * modified on 24 July, 2012 
		 * For equations that involve division, handle it by setting the value to zero
		 */
		record.set("remeasuredFactor", (bqItem.getRemeasuredQuantity()==null || bqItem.getRemeasuredQuantity()==0 || bqItem.getQuantity()==null || bqItem.getQuantity()==0)? "0" : (bqItem.getRemeasuredQuantity() / bqItem.getQuantity()));
		record.set("rate", "0");
		record.set("amount", "0");
		
		String bqType = bqItem.getBqType();
		if("CL".equals(bqType))
			record.set("subsidiaryCode", "49019999");
		else if("DW".equals(bqType))
			record.set("subsidiaryCode", "49039999");
		else if("VO".equals(bqType))
			record.set("subsidiaryCode", "49809999");
		
		int count = resourceDataStore.getCount();
		if(count == 0)
			resourceDataStore.add(record);
		else
			resourceDataStore.insert(count - 1, record); //insert before the total amount row 
	}

	private void save(){
		if(bqTotalCost != resourceTotalCost){
			MessageBox.alert("Total amounts don't match!</br>The total cost amount of the resources must be equal to that of the bill item.");
			return;
		}
		
		Record[] records = resourceDataStore.getRecords();
		List<Resource> resourceList = new ArrayList<Resource>(records.length - 1);
		for(int i = 0; i < records.length - 1; i++){
			Resource resource = resourceFromRecord(records[i]);
			resource.setBqItem(bqItem);
			resourceList.add(resource);
		}
		repackagingUpdateByBQWindow.saveChangeOrderBqAndResources(bqItem, resourceList);
	}
	
	private Resource resourceFromRecord(Record record){
		Resource resource = new Resource();
		resource.setBqItem(bqItem);
		if(record.getAsString("id") != null)
			resource.setId(Long.valueOf(record.getAsString("id")));
		resource.setRefBillNo(bqItem.getRefBillNo());
		resource.setRefSubBillNo(bqItem.getRefSubBillNo());
		resource.setRefPageNo(bqItem.getRefPageNo());
		resource.setRefSectionNo(bqItem.getRefSectionNo());
		resource.setRefItemNo(bqItem.getItemNo());
		resource.setJobNumber(bqItem.getRefJobNumber());
		resource.setPackageNo(record.getAsString("packageNo").trim());
		resource.setObjectCode(record.getAsString("objectCode").trim());
		resource.setSubsidiaryCode(record.getAsString("subsidiaryCode").trim());
		resource.setDescription(record.getAsString("description").trim());
		resource.setUnit(record.getAsString("unit").trim());
		Double remeasuredQuant = record.getAsDouble("quantity");
		Double remeasuredFactor = record.getAsDouble("remeasuredFactor");
		/**
		 * @author tikywong
		 * modified on 24 July, 2012 
		 * For equations that involve division, handle it by setting the value to zero
		 */
		Double quantity = (remeasuredQuant==null || remeasuredQuant==0 || remeasuredFactor==null || remeasuredFactor==0)? 0.0 : remeasuredQuant/remeasuredFactor;
		resource.setQuantity(quantity);
		resource.setRemeasuredFactor(remeasuredFactor);
		resource.setCostRate(record.getAsDouble("rate"));
		resource.setResourceType(record.getAsString("resourceType"));
		return resource;
	}
}
