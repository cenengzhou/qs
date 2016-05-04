/**
 * koeyyeung
 * modified on Jul 2, 201310:36:19 AM
 * Change from window to panel
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
import com.gammon.qs.client.repository.BQResourceSummaryRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.ArrowKeyNavigation;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRendererCheckIfTotal;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.RoundingUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.BQResourceSummary;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.IVInputPaginationWrapper;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.BooleanFieldDef;
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
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
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
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

/**
 * koeyyeung
 * modified on 29th May, 2015
 * allow IV input for packages with "Final" status
 */
public class UpdateCumIVFinalByResourceSummaryMainPanel extends Panel{
	private static final String MAIN_PANEL_ID = "ivInputPanel";
	
	private GlobalSectionController globalSectionController;
	private DetailSectionController detailSectionController;
	private EditorGridPanel resultEditorGridPanel;
	private GlobalMessageTicket globalMessageTicket;

	//Pagination
	private PaginationToolbar paginationToolbar;
	
	private BQResourceSummaryRepositoryRemoteAsync bqResourceSummaryRepository;
	
	private Store dataStore;
	
	private TextField packageNoTextField;
	private TextField objectCodeTextField;
	private TextField subsidiaryCodeTextField;
	private TextField descriptionTextField;
	private NumberField applyPercentageNumberField;
	
	private ArrowKeyNavigation arrowKeyNavigation;
	
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
					new StringFieldDef("ivCumQuantity"),
					new StringFieldDef("ivCumAmount"),
					new StringFieldDef("ivMovement"),
					new StringFieldDef("ivPostedAmount"),
					new BooleanFieldDef("excludeLevy"),
					new BooleanFieldDef("excludeDefect"),
					new BooleanFieldDef("forIvRollbackOnly"),
					new StringFieldDef("finalized")
			});
	
	//Buttons with security
	private ToolbarButton updatesButton;
	private ToolbarButton postIVButton;
	private ToolbarButton downloadExcelButton;
	
	//For security access rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	
	//Search parameters
	private String packageNo;
	private String objectCode;
	private String subsidiaryCode;
	private String description;
	
	
	private NumberFormat numberFormat = NumberFormat.getFormat("#,##0.00");
	
	private List<String> awardedPackageNos;
	
	private ToolbarTextItem totalRowsTextItem = new ToolbarTextItem("<b>0 Records</b>");
	private ToolbarTextItem totalPostedIVTextItem = new ToolbarTextItem("<b>Total Posted IV: 0</b>");
	private ToolbarTextItem totalCumIVTextItem = new ToolbarTextItem("<b>Total Cum IV: 0</b>");
	private ToolbarTextItem finalMovementTextItem = new ToolbarTextItem("<b>Movement(Finalized Package): 0</b>");
	
	private String repackagingStatus;

	public UpdateCumIVFinalByResourceSummaryMainPanel(final GlobalSectionController globalSectionController, String repackagingStatus) {
		super();
		this.globalSectionController = globalSectionController;
		detailSectionController = this.globalSectionController.getDetailSectionController();
		this.repackagingStatus = repackagingStatus;
		globalMessageTicket = new GlobalMessageTicket();
		
		bqResourceSummaryRepository = this.globalSectionController.getBqResourceSummaryRepository();
		userAccessRightsRepository = this.globalSectionController.getUserAccessRightsRepository();
		
		setupUI();
		
	}

	private void setupUI() {
		setLayout(new RowLayout());
		
		setupSearchPanel();
		setupGridPanel();
		
		setId(MAIN_PANEL_ID);
		
		
		//hide detail panel
		detailSectionController.getMainPanel().collapse();
	}

	private void setupSearchPanel() {
		//search Panel
		Panel searchPanel = new Panel();
		searchPanel.setPaddings(0);
		searchPanel.setFrame(true);
		searchPanel.setHeight(40);
		searchPanel.setLayout(new TableLayout(9));
		
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
		searchPanel.add(packageNoTextField);
		
		Label objectCodeLabel = new Label("Object Code");
		objectCodeLabel.setCls("table-cell");
		searchPanel.add(objectCodeLabel, new ColumnLayoutData(20));
		objectCodeTextField = new TextField("Object Code", "objectCode", 100);
		objectCodeTextField.setCtCls("table-cell");
		objectCodeTextField.setCls("table-cell");
		objectCodeTextField.setValue("14*");
		objectCodeTextField.addListener(searchListener);
		searchPanel.add(objectCodeTextField);
		
		Label subsidiaryCodeLabel = new Label("Subsidiary Code");
		subsidiaryCodeLabel.setCls("table-cell");
		searchPanel.add(subsidiaryCodeLabel, new ColumnLayoutData(20));
		subsidiaryCodeTextField = new TextField("Subsidiary Code", "subsidiaryCode", 100);
		subsidiaryCodeTextField.setCtCls("table-cell");
		subsidiaryCodeTextField.setCls("table-cell");
		subsidiaryCodeTextField.addListener(searchListener);
		searchPanel.add(subsidiaryCodeTextField);
		
		Label descriptionLabel = new Label("Description");
		descriptionLabel.setCls("table-cell");
		searchPanel.add(descriptionLabel);
		descriptionTextField = new TextField("Description", "description", 100);
		descriptionTextField.setCtCls("table-cell");
		descriptionTextField.setCls("table-cell");
		descriptionTextField.addListener(searchListener);
		searchPanel.add(descriptionTextField);
				
		Button searchButton = new Button("Search");
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				search();
			};
		});
		searchButton.setCls("table-cell");
		searchPanel.add(searchButton, new ColumnLayoutData(20));
		
		add(searchPanel);
	}

	private void setupToolbar(){
		Toolbar toolbar = new Toolbar();
		
		
		postIVButton = new ToolbarButton("Post IV Movements");
		postIVButton.setIconCls("post-icon");
		postIVButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, EventObject e) {
					globalMessageTicket.refresh();
					if(dataStore.getModifiedRecords().length > 0){
						MessageBox.alert("Please save any changes before posting");
						return;
					}
					else{
						postIVMovements();
					}
				};
		});
		downloadExcelButton = new ToolbarButton("Export Resources Summary to Excel");
		downloadExcelButton.setIconCls("excel-icon");
		downloadExcelButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				exportExcel();
			}	
		});
		
		updatesButton = new ToolbarButton("Update");
		updatesButton.setIconCls("save-button-icon");
		updatesButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, EventObject e) {
					globalMessageTicket.refresh();
					update();				
				};
		});
		
		postIVButton.setVisible(false);
		updatesButton.setVisible(false);
		downloadExcelButton.setVisible(false);
		
		toolbar.addButton(postIVButton);
		toolbar.addSeparator();
		toolbar.addButton(updatesButton);
		toolbar.addSeparator();
		toolbar.addButton(downloadExcelButton);
		toolbar.addSeparator();
		
		@SuppressWarnings("unused")
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
		
		/*toolbar.addFill();
		toolbar.addItem(applyPercentageTextItem);
		toolbar.addField(applyPercentageNumberField);
		toolbar.addSeparator();
		toolbar.addButton(applyPercentageButton);
		toolbar.addSeparator();*/
		
		setTopToolbar(toolbar);
	}
	
	private void setupGridPanel() {
		User user = globalSectionController.getUser();
		Map<String, String> prefs = user.getGeneralPreferences();
		String quantDP = prefs.get(GeneralPreferencesKey.QUANTITY_DECIMAL_PLACES);
		if(quantDP == null)
			quantDP = "1";
		String amountDP = prefs.get(GeneralPreferencesKey.AMOUNT_DECIMAL_PLACES);
		if(amountDP == null)
			amountDP = "2";			
		
		setupToolbar();
		
		awardedPackageNos = globalSectionController.getAwardedPackageNos();
		if(awardedPackageNos == null)
			awardedPackageNos = new ArrayList<String>(1);
		
		resultEditorGridPanel =  new EditorGridPanel();
		this.arrowKeyNavigation = new ArrowKeyNavigation(resultEditorGridPanel);
		final Renderer quantityRenderer = new QuantityRenderer(user);
		final Renderer amountRenderer = new AmountRendererCheckIfTotal(user);
		Renderer rateRenderer = new RateRenderer(user);
		
		Renderer editableAmountRenderer = new Renderer(){
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				String str = amountRenderer.render(value, cellMetadata, record, rowIndex, colNum, store);
				return "<span style=\"color:blue;\">" + str + "</span>";
			}
		};
		
		Renderer editableQuantityRenderer = new Renderer(){
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				String str = quantityRenderer.render(value, cellMetadata, record, rowIndex, colNum, store);
				return "<span style=\"color:blue;\">" + str + "</span>";
			}
		};
		
		Renderer checkedRenderer = new Renderer(){
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				boolean checked = ((Boolean) value).booleanValue();  
				return checked ? "Y" : "N";
			}
		};
		
		NumberField quantNumberField = new NumberField();
		quantNumberField.setDecimalPrecision(Integer.valueOf(quantDP));
		NumberField amountNumberField = new NumberField();
		amountNumberField.setDecimalPrecision(Integer.valueOf(amountDP));
		
		ColumnConfig packageNoColConfig = new ColumnConfig("Package No.", "packageNo", 40, false);
		ColumnConfig objectCodeColConfig = new ColumnConfig("Object Code", "objectCode", 50, false);
		ColumnConfig subsidiaryCodeColConfig = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 65, false);
		ColumnConfig resourceDescriptionColConfig = new ColumnConfig("Resource Description", "resourceDescription", 200, false);
		ColumnConfig unitColConfig = new ColumnConfig("Unit", "unit", 30, false);
		ColumnConfig quantityColConfig = new ColumnConfig("Quantity", "quantity", 100, false);
		quantityColConfig.setRenderer(quantityRenderer);
		quantityColConfig.setAlign(TextAlign.RIGHT);
		ColumnConfig rateColConfig = new ColumnConfig("Rate", "rate", 110, false);
		rateColConfig.setRenderer(rateRenderer);
		rateColConfig.setAlign(TextAlign.RIGHT);
		ColumnConfig amountColConfig = new ColumnConfig("Amount", "amount", 110, false);
		amountColConfig.setRenderer(amountRenderer);
		amountColConfig.setAlign(TextAlign.RIGHT);
		
		ColumnConfig ivCumQuantityColConfig = new ColumnConfig("Cum IV Quantity", "ivCumQuantity", 110, false);
		Field cumQtyField = FieldFactory.createNegativeNumberField(4);
		cumQtyField.addListener(this.arrowKeyNavigation);
		ivCumQuantityColConfig.setRenderer(editableQuantityRenderer);
		ivCumQuantityColConfig.setAlign(TextAlign.RIGHT);
		ivCumQuantityColConfig.setEditor(new GridEditor(cumQtyField));
		
		ColumnConfig ivCumAmountColConfig = new ColumnConfig("Cum IV Amount", "ivCumAmount", 110, false);
		Field cumAmtField = FieldFactory.createNegativeNumberField(2);
		cumAmtField.addListener(this.arrowKeyNavigation);
		ivCumAmountColConfig.setRenderer(editableAmountRenderer);
		ivCumAmountColConfig.setAlign(TextAlign.RIGHT);
		ivCumAmountColConfig.setEditor(new GridEditor(cumAmtField));
		
		
		ColumnConfig ivMovementColConfig = new ColumnConfig("IV Movement", "ivMovement", 110, false);
		Field movementField = FieldFactory.createNegativeNumberField(2);
		movementField.addListener(this.arrowKeyNavigation);
		ivMovementColConfig.setRenderer(editableAmountRenderer);
		ivMovementColConfig.setAlign(TextAlign.RIGHT);
		ivMovementColConfig.setEditor(new GridEditor(movementField));
		//ivMovementColConfig.setEditor(new GridEditor((NumberField)amountNumberField.cloneComponent()));
		
		ColumnConfig ivPostedAmountColConfig = new ColumnConfig("Posted IV Amount", "ivPostedAmount", 110, false);
		ivPostedAmountColConfig.setRenderer(amountRenderer);
		ivPostedAmountColConfig.setAlign(TextAlign.RIGHT);
		ColumnConfig excludeLevyColConfig = new ColumnConfig("Levy Excluded", "excludeLevy", 60, false);
		excludeLevyColConfig.setRenderer(checkedRenderer);
		excludeLevyColConfig.setTooltip("Resource IV movement is excluded from levy provisions");
		ColumnConfig excludeDefectColConfig = new ColumnConfig("Defect Excluded", "excludeDefect", 60, false);
		excludeDefectColConfig.setRenderer(checkedRenderer);
		excludeDefectColConfig.setTooltip("Resource IV movement is excluded from defect provisions");
		ColumnConfig finalizedColConfig = new ColumnConfig("Finalized", "finalized", 80, false);
		finalizedColConfig.setTooltip("Resources with final IV posting done cannot be posted again.");
		
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(resourceSummaryRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		dataStore.setSortInfo(new SortState("objectCode", SortDir.ASC));
		resultEditorGridPanel.setStore(dataStore);
		
		BaseColumnConfig[] columns = new BaseColumnConfig[]{
				packageNoColConfig,
				objectCodeColConfig,
				subsidiaryCodeColConfig,
				resourceDescriptionColConfig,
				unitColConfig,
				quantityColConfig,
				rateColConfig,
				amountColConfig,
				ivCumQuantityColConfig,
				ivCumAmountColConfig,
				ivMovementColConfig,
				ivPostedAmountColConfig,
				excludeLevyColConfig,
				excludeDefectColConfig,
				finalizedColConfig
		};
		
		resultEditorGridPanel.setColumnModel(new ColumnModel(columns));
				
		resultEditorGridPanel.addEditorGridListener(new EditorGridListener(){

			public boolean doBeforeEdit(GridPanel grid, Record record,
					String field, Object value, int rowIndex, int colIndex) {
				globalMessageTicket.refresh();
				boolean isEditable = true;
				if(record.getAsBoolean("forIvRollbackOnly"))
					isEditable = false;
				else if(record.getAsDouble("amount") == 0){
					isEditable = false;
				}
				else{
					String finalized = record.getAsString("finalized");
					if(BQResourceSummary.POSTED.equals(finalized)){
						MessageBox.alert("Final IV posting has been done for this package already. No more IV posting is allowed.");
						isEditable =  false;
					}
				}
				if (isEditable) {
					arrowKeyNavigation.startedEdit(colIndex, rowIndex);
				} else {
					arrowKeyNavigation.resetState();
				}
				return isEditable;
			}

			public boolean doValidateEdit(GridPanel grid, Record record,
					String field, Object value, Object originalValue,
					int rowIndex, int colIndex) {
				if(value != null){
					boolean isVO = false;
					if(record.getAsString("subsidiaryCode").startsWith("4") && "80".equals(record.getAsString("subsidiaryCode").substring(2, 4)))
						isVO = true;
					
					if(field.equals("ivCumQuantity")){
						double cumIvQty = Double.parseDouble(value.toString());
						double qty = record.getAsDouble("quantity");
						if(!isVO && qty != 0 && RoundingUtil.round(Math.abs(cumIvQty)-Math.abs(qty),5) > 0){
							MessageBox.alert("Cumulative IV quantity cannot be greater than the resource quantity");
							return false;
						}
						if(qty != 0 && cumIvQty/qty < 0){
							MessageBox.alert("Cumulative IV quantity must have the same sign (+/-) as the resource quantity");
							return false;
						}
					}
					else{
						double newValue = 0;
						if(field.equals("ivMovement"))
							newValue = record.getAsDouble("ivPostedAmount");
						newValue += Double.parseDouble(value.toString());
						double maxValue = record.getAsDouble("amount");			
						if(!isVO && maxValue != 0 && RoundingUtil.round(Math.abs(newValue)-Math.abs(maxValue),5) > 0){
							MessageBox.alert("Cum IV amount cannot be greater than total amount");
							return false;
						}
						else if(maxValue != 0 && newValue/maxValue < 0){
							MessageBox.alert("Cum IV amount must have the same sign (+/-) as total amount");
							return false;
						}
					}
				}	
				return true;
			}

			public void onAfterEdit(GridPanel grid, Record record,
					String field, Object value, Object oldValue,
					int rowIndex, int colIndex) {
				double ivPosted = record.getAsDouble("ivPostedAmount");
				double rate = record.getAsDouble("rate");
				if(field.equals("ivCumQuantity")){
					double ivQuant = value != null ? Double.parseDouble(value.toString()) : 0.0;
					record.set("ivCumAmount", Double.toString(ivQuant * rate));
					record.set("ivMovement", Double.toString(ivQuant * rate - ivPosted));
				}
				else if(field.equals("ivMovement")){
					double ivMovement = value != null ? Double.parseDouble(value.toString()) : 0.0;
					record.set("ivCumAmount", Double.toString(ivPosted + ivMovement));
					record.set("ivCumQuantity", Double.toString((ivPosted + ivMovement)/rate));
				}
				else{
					double ivCum = value != null ? Double.parseDouble(value.toString()) : 0.0;
					record.set("ivMovement", Double.toString(ivCum - ivPosted));
					record.set("ivCumQuantity", Double.toString((ivCum)/rate));
				}
			}
		});
		
		GridView view = new CustomizedGridView();
		resultEditorGridPanel.setView(view);
		
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(final int pageNum){
				globalMessageTicket.refresh();
				if (dataStore.getModifiedRecords().length > 0){
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
		paginationToolbar.addItem(totalRowsTextItem);
		paginationToolbar.addSpacer();
		paginationToolbar.addSeparator();
		paginationToolbar.addSpacer();
		paginationToolbar.addItem(totalPostedIVTextItem);
		paginationToolbar.addSpacer();
		paginationToolbar.addSeparator();
		paginationToolbar.addSpacer();
		paginationToolbar.addItem(totalCumIVTextItem);
		paginationToolbar.addSpacer();
		paginationToolbar.addSeparator();
		paginationToolbar.addSpacer();
		paginationToolbar.addItem(finalMovementTextItem);
		
		
		resultEditorGridPanel.setBottomToolbar(paginationToolbar);
		paginationToolbar.setTotalPage(0);
		paginationToolbar.setCurrentPage(0);

		add(resultEditorGridPanel);
		
		SessionTimeoutCheck.renewSessionTimer();
		userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010103_UPDATE_IV_BY_RESOURCE_SUMMARY_MAINPANEL, new AsyncCallback<List<String>>(){
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
	
	
	public void search(){
		packageNo = packageNoTextField.getText().trim();
		if("".equals(objectCodeTextField.getText().trim()))
			objectCodeTextField.setValue("14*");
		objectCode = objectCodeTextField.getText().trim();
		subsidiaryCode = subsidiaryCodeTextField.getText().trim();
		description = descriptionTextField.getText().trim();
		
		if(!objectCode.startsWith("14")){
			MessageBox.alert("Only object code starts with '14' can be searched for subcontract package with status 'Final'.");
			return;
		}
		
		searchByPage(0);
	}
	
	public void searchByPage(final int pageNum){
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		bqResourceSummaryRepository.recalculateResourceSummaryIVForFinalizedPackage(globalSectionController.getJob(), packageNo, objectCode, subsidiaryCode, description, new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {
				SessionTimeoutCheck.renewSessionTimer();
				bqResourceSummaryRepository.obtainResourceSummariesSearchByPageForIVInput(globalSectionController.getJob(), 
						packageNo, objectCode, subsidiaryCode, description, pageNum, true,
						new AsyncCallback<IVInputPaginationWrapper>(){
					public void onSuccess(final IVInputPaginationWrapper wrapper) {
						populateGrid(wrapper);
						UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					}
					public void onFailure(Throwable e) {
						UIUtil.throwException(e);
						UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					}
				});
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
	}
	
	public void populateGrid(IVInputPaginationWrapper wrapper){
		if(wrapper == null)
			return;
		dataStore.removeAll();
		int rows = wrapper.getTotalRecords();
		
		totalRowsTextItem.setText("<b>" + rows + " Records</b>");
		totalPostedIVTextItem.setText("<b>Posted IV:&nbsp;" + numberFormat.format(wrapper.getIvPostedTotal()) + "</b>");
		totalCumIVTextItem.setText("<b>Cum IV:&nbsp;" + numberFormat.format(wrapper.getIvCumTotal()) + "</b>");
		finalMovementTextItem.setText("<b>Movement(Finalized Package):&nbsp;" + numberFormat.format(wrapper.getIvFinalMovement()) + "</b>");
		
		paginationToolbar.setTotalPage(wrapper.getTotalPage());
		paginationToolbar.setCurrentPage(wrapper.getCurrentPage());
		try{
			for (BQResourceSummary resourceSummary : wrapper.getCurrentPageContentList()){
				Double quantity = resourceSummary.getQuantity();
				if(quantity == null)
					quantity = Double.valueOf(0);
				Double rate = resourceSummary.getRate();
				if(rate == null)
					rate = Double.valueOf(0);
				Double postedIV = resourceSummary.getPostedIVAmount();
				if(postedIV == null)
					postedIV = Double.valueOf(0);
				Double cumIV = resourceSummary.getCurrIVAmount();
				if(cumIV == null)
					cumIV = Double.valueOf(0);
				Record record = resourceSummaryRecordDef.createRecord(new Object[]{
					resourceSummary.getId().toString(),
					resourceSummary.getPackageNo(),
					resourceSummary.getObjectCode(),
					resourceSummary.getSubsidiaryCode(),
					resourceSummary.getResourceDescription(),
					resourceSummary.getUnit(),
					quantity,
					rate,
					quantity*rate,
					rate.equals(Double.valueOf(0)) ? rate : cumIV/rate,
					cumIV,
					cumIV - postedIV,
					postedIV,
					resourceSummary.getExcludeLevy(),
					resourceSummary.getExcludeDefect(),
					resourceSummary.getForIvRollbackOnly(),
					"N".equals(resourceSummary.getFinalized())?"Not Finalized":resourceSummary.getFinalized()
				});
				dataStore.addSorted(record);
			}
		}
		catch(Exception e){
			UIUtil.alert(e);
		}
		
		//hide detail panel
		if (!globalSectionController.getDetailSectionController().getMainPanel().isCollapsed())
			globalSectionController.getDetailSectionController().getMainPanel().collapse();
	}
	
	private void securitySetup(){
		if(accessRightsList != null && accessRightsList.contains("WRITE")){
			downloadExcelButton.setVisible(true);
			if(	"900".equals(repackagingStatus) && 
				(globalSectionController.getJob().getRepackagingType()!=null && 
						(globalSectionController.getJob().getRepackagingType().equals("1") || globalSectionController.getJob().getRepackagingType().equals("2")))){
				postIVButton.setVisible(true);
				updatesButton.setVisible(true);
			}
		}
		if(accessRightsList != null && accessRightsList.contains("READ")){
			downloadExcelButton.setVisible(true);
		}
	}
	
	private void update(){
		saveResourceSummaries(dataStore.getModifiedRecords());
	}
	
	private void postIVMovements(){
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getBqResourceSummaryRepository().obtainIVMovementByJob(globalSectionController.getJob(), true, new AsyncCallback<List<Double>>() {
			public void onSuccess(List<Double> resultList) {
				MessageBox.confirm("Confirmation",
									"Post IV Movements? <br/>"+
									"Movements (Finalized Package): "+numberFormat.format(resultList.get(0)) + "<br/><br/>" +
									"<b>Please note that IV Posting of Finalized Package can be posted only once. <br/>" +
									"No admendment can be made after posting.</b> ", new MessageBox.ConfirmCallback(){
					public void execute(String btnID) {
						if(btnID.equals("yes")){
							globalSectionController.postIVMovements(true);
						}
					}
				});
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}
		});
	}
	
	private void exportExcel(){
		String jobNumber = globalSectionController.getJob().getJobNumber();
		com.google.gwt.user.client.Window.open(GlobalParameter.BQ_RESOURCE_SUMMARY_IV_DOWNLOAD_URL + "?jobNumber=" + jobNumber
																										+ "&packageNo=" + packageNoTextField.getText().trim()
																										+ "&objectCode=" + objectCodeTextField.getText().trim()
																										+ "&subsidiaryCode=" + subsidiaryCodeTextField.getText().trim()
																										+ "&description=" + descriptionTextField.getText().trim()
																										+ "&packageList=Final"
																										, "_blank", "");
	}
	
	private void saveResourceSummaries(Record[] records){
		List<BQResourceSummary> resourceSummaries = new ArrayList<BQResourceSummary>();
		for(Record record : records){
			resourceSummaries.add(resourceSummaryFromRecord(record));
		}
		if(resourceSummaries.size() == 0){
			MessageBox.alert("No records changed");
			return;
		}
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.SAVING_MSG, true);
		try{
			SessionTimeoutCheck.renewSessionTimer();
			bqResourceSummaryRepository.updateResourceSummariesIVAmountForFinalizedPackage(resourceSummaries, new AsyncCallback<Boolean>(){
				public void onSuccess(Boolean success) {
					dataStore.commitChanges();
					search();
					UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				}
				public void onFailure(Throwable e) {
					UIUtil.throwException(e);
					UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				}				
			});
		}
		catch(Exception e){
			UIUtil.alert(e);
		}
	}
	
	public BQResourceSummary resourceSummaryFromRecord(Record record){
		BQResourceSummary resourceSummary = new BQResourceSummary();
		String id = record.getAsString("id");
		if(id != null && id.length() > 0)
			resourceSummary.setId(new Long(id));
		resourceSummary.setCurrIVAmount(Double.valueOf(record.getAsDouble("ivCumAmount")));
		return resourceSummary;
	}
	
	public void applyPercentage(){
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
					if(updateDataOfApplyPercentage(records, percentage))
						;
					else
						MessageBox.alert("Error occured during apply percentage");
				}
			}
		}
	}
	
	private boolean updateDataOfApplyPercentage(Record[] records, Double percentage){
		boolean appliedPercentage = true;
		for(Record curRecord : records){		
			// added by brian on 20110329 - start
			// if the package is awarded do not apply percentage,
			// and no update  = no update error so status is true
			String packageNo = curRecord.getAsString("packageNo");
			String objectCode = curRecord.getAsString("objectCode");
			if(packageNo != null && objectCode.startsWith("14") && awardedPackageNos.contains(packageNo))
				; //status = true;
			else{
				// added by brian on 20110329 - end
				Double MaxQty = curRecord.getAsDouble("quantity");
				Double rate = curRecord.getAsDouble("rate");
				Double postedIVAmt = curRecord.getAsDouble("ivPostedAmount");
				
				Double newIVCumQty = (MaxQty * (percentage / 100.0));
				Double newIVCumAmt = (newIVCumQty * rate);
				Double newIVmovement = (newIVCumAmt - postedIVAmt);
				
				curRecord.set("ivCumQuantity", newIVCumQty.toString());
				curRecord.set("ivCumAmount", newIVCumAmt.toString());
				curRecord.set("ivMovement", newIVmovement.toString());
				
			}
		}
		return appliedPercentage;
	}
	
}
