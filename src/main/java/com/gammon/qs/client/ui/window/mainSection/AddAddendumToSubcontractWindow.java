package com.gammon.qs.client.ui.window.mainSection;
import java.util.ArrayList;
import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.repository.BQResourceSummaryRepositoryRemote;
import com.gammon.qs.client.repository.BQResourceSummaryRepositoryRemoteAsync;
import com.gammon.qs.client.repository.PackageRepositoryRemote;
import com.gammon.qs.client.repository.PackageRepositoryRemoteAsync;
import com.gammon.qs.client.repository.RepackagingEntryRepositoryRemote;
import com.gammon.qs.client.repository.RepackagingEntryRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemote;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
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
import com.gammon.qs.domain.RepackagingEntry;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
import com.gammon.qs.wrapper.addAddendum.AddAddendumWrapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
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
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.CheckboxSelectionModel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;
import com.gwtext.client.widgets.layout.TableLayoutData;

public class AddAddendumToSubcontractWindow extends Window {
	private static final String RESULT_PANEL_ID ="repackagingEnquiryResultPanel"; 
	private static final String MAIN_PANEL_ID ="repackagingEnquiryPanel";
	private static final String WINDOW_ID = "AddAddendumToSubcontractWindow";

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

	private BQResourceSummaryRepositoryRemoteAsync bqResourceSummaryRepository;
	private RepackagingEntryRepositoryRemoteAsync repackagingEntryRepository;
	// used to check access rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	private PackageRepositoryRemoteAsync packageRepository;

	//data store
	private Store dataStore;

	//Record[] data 
	private ArrayList<Record> data=new ArrayList<Record>();
	private ArrayList<Record> newData=new ArrayList<Record>(); 
	
	private final CheckboxSelectionModel cbSelectionModel;   

	private TextField packageNoTextField;
	private TextField objectCodeTextField;
	private TextField subsidiaryCodeTextField;
	private TextField descriptionTextField;
	
	private ComboBox typeComboBox;

	private final CheckBox filterCheckBox;
	
	//private Button saveUpdatesButton;
	private Button addAddendumButton;

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
					new StringFieldDef("oldPackageNo")
			});

	private Store packageStore;
	private List<String> awardedPackageNos;

	private ComboBox unitComboBox;
	private ComboBox packageComboBox;

	//Search parameters
	private String packageNo;
	private String objectCode;
	private String subsidiaryCode;
	private String description;
	private String type;
	private String levyExcluded = "";
	private String defectExcluded = "";
	
	private ToolbarTextItem totalAmountTextItem = new ToolbarTextItem("<b>0 Records match search criteria</b>");
	private ToolbarTextItem totalBudgetTextItem = new ToolbarTextItem("<b>Total Budget: 0</b>");
	private ToolbarTextItem totalSellingValueTextItem = new ToolbarTextItem("<b>Total Selling Value: 0</b>");

	private Long repackagingEntryId;

	public AddAddendumToSubcontractWindow(final RepackagingListGridPanel repackagingListGridPanel, Long repackagingEntryId){
		super();
		this.setModal(true);
		this.repackagingListGridPanel = repackagingListGridPanel;
		this.globalSectionController = repackagingListGridPanel.getGlobalSectionController();
		this.repackagingEntryId = repackagingEntryId;
		this.setArrowKeyNavigation(new ArrowKeyNavigation(resultEditorGridPanel));
		cbSelectionModel = new CheckboxSelectionModel();
		globalMessageTicket = new GlobalMessageTicket();

		

		bqResourceSummaryRepository = (BQResourceSummaryRepositoryRemoteAsync) GWT.create(BQResourceSummaryRepositoryRemote.class);
		((ServiceDefTarget)bqResourceSummaryRepository).setServiceEntryPoint(GlobalParameter.BQ_RESOURCE_SUMMARY_REPOSITORY_URL);
		repackagingEntryRepository = (RepackagingEntryRepositoryRemoteAsync) GWT.create(RepackagingEntryRepositoryRemote.class);
		((ServiceDefTarget)repackagingEntryRepository).setServiceEntryPoint(GlobalParameter.REPACKAGING_ENTRY_REPOSITORY_URL);
		userAccessRightsRepository = (UserAccessRightsRepositoryRemoteAsync) GWT.create(UserAccessRightsRepositoryRemote.class);
		((ServiceDefTarget)userAccessRightsRepository).setServiceEntryPoint(GlobalParameter.USER_ACCESS_RIGHTS_REPOSITORY_URL);
		packageRepository = (PackageRepositoryRemoteAsync) GWT.create(PackageRepositoryRemote.class);
		((ServiceDefTarget)packageRepository).setServiceEntryPoint(GlobalParameter.PACKAGE_REPOSITORY_URL);

		obtainAwardedPackageNos();
		if(awardedPackageNos == null)
			awardedPackageNos = new ArrayList<String>();
		
		this.setTitle("Add/Edit Addendum to Subcontract");
		this.setPaddings(5);
		this.setWidth(1280);
		this.setHeight(650);
		this.setClosable(false);
		this.setLayout(new FitLayout());

		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setId(MAIN_PANEL_ID);
		// added by brian on 20110311
		this.setId(WINDOW_ID);

		//search Panel
		searchPanel = new Panel();
		searchPanel.setPaddings(3);
		searchPanel.setFrame(true);
		searchPanel.setHeight(50);
		searchPanel.setLayout(new TableLayout(13));

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
		searchPanel.add(objectCodeLabel);
		objectCodeTextField = new TextField("Object Code", "objectCode", 100);
		objectCodeTextField.setCtCls("table-cell");
		objectCodeTextField.setCls("table-cell");
		objectCodeTextField.addListener(searchListener);
		searchPanel.add(objectCodeTextField);

		Label subsidiaryCodeLabel = new Label("Subsidiary Code");
		subsidiaryCodeLabel.setCls("table-cell");
		searchPanel.add(subsidiaryCodeLabel);
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

		Label typeLabel = new Label("Type");
		typeLabel.setCls("table-cell");
		searchPanel.add(typeLabel);
		typeComboBox = FieldFactory.createComboBox();
		typeComboBox.setCtCls("table-cell");
		typeComboBox.setWidth(55);
		typeComboBox.addListener(searchListener);
		searchPanel.add(typeComboBox);
		setStaticComboBoxOption(typeComboBox, new String[][]{
				{"", "ALL"},
				{"blank","Blank"},
				{"VO","VO"},
				{"OI","OI"}
		});

		
		Button searchButton = new Button("Search");
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				globalMessageTicket.refresh();
				search();
			};

		});
		searchButton.setCls("table-cell");
		
		Panel filterPanel = new Panel();
		this.filterCheckBox = new CheckBox();
		Label filterLabel = new Label("Show non awarded resources");
		filterLabel.setCls("table-cell");
		filterCheckBox.addClickListener(new ClickListener(){

			public void onClick(Widget arg0) {
				filter();
				
			}
			
		});
		filterPanel.add(filterLabel);
		filterPanel.add(filterCheckBox);
		searchPanel.add(filterPanel);
		searchPanel.add(searchButton, new TableLayoutData(100));
		resultPanel = new Panel();
		resultPanel.setId(RESULT_PANEL_ID);
		resultPanel.setBorder(true);
		resultPanel.setFrame(true);
		resultPanel.setPaddings(2);
		resultPanel.setAutoScroll(true);
		resultPanel.setLayout(new FitLayout());

		resultEditorGridPanel =  new EditorGridPanel();
		Renderer quantityRenderer = new QuantityRenderer(globalSectionController.getUser());
		Renderer amountRenderer = new AmountRendererCheckIfTotal(globalSectionController.getUser());
		Renderer rateRenderer = new RateRenderer(globalSectionController.getUser());

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
		packageStore = globalSectionController.getAwardedPackageStore();
		if(packageStore == null)
			packageStore = new SimpleStore(new String[]{"packageNo", "description", "status"}, new String[][]{});
		packageStore.load();
		packageComboBox.setDisplayField("description");
		packageComboBox.setValueField("packageNo");
		packageComboBox.setSelectOnFocus(true);
		packageComboBox.setForceSelection(true);
		packageComboBox.setListWidth(200);
		packageComboBox.setStore(packageStore);

		Renderer checkedRenderer = new Renderer(){
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				boolean checked = ((Boolean) value).booleanValue();  
				//				return checked ? "<img class=\"checkbox\" src=\"com.gammon.qs.Main/images/checked.png\"/>" : "";
				return checked ? "Y" : "N";
			}
		};

		packageNoColConfig = new ColumnConfig("Package No.", "packageNo", 40, false);
		packageNoColConfig.setEditor(new GridEditor(packageComboBox));
		packageNoColConfig.setTooltip("Package No.");
		objectCodeColConfig = new ColumnConfig("Object Code", "objectCode", 50, false);
		//objectCodeColConfig.setEditor(new GridEditor(new TextField()));
		objectCodeColConfig.setTooltip("Object Code");
		subsidiaryCodeColConfig = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 65, false);
		//subsidiaryCodeColConfig.setEditor(new GridEditor(new TextField()));
		subsidiaryCodeColConfig.setTooltip("Subsidiary Code");
		resourceDescriptionColConfig = new ColumnConfig("Resource Description", "resourceDescription", 220, false);
		resourceDescriptionColConfig.setEditor(new GridEditor(new TextField()));
		resourceDescriptionColConfig.setTooltip("Resource Description");
		unitColConfig = new ColumnConfig("Unit", "unit", 30, false);
		//unitColConfig.setEditor(new GridEditor(unitComboBox));
		unitColConfig.setTooltip("Unit");
		quantityColConfig = new ColumnConfig("Quantity", "quantity", 110, false);
		quantityColConfig.setRenderer(quantityRenderer);
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
				//new CheckboxColumnConfig(cbSelectionModel),
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
				//excludeLevyColConfig,
				//excludeDefectColConfig
		};

		resultEditorGridPanel.setColumnModel(new ColumnModel(columns));
		resultEditorGridPanel.setSelectionModel(cbSelectionModel);

		//Validation - check postedIVAmount and whether package status > 100 (packageNo in awardedPackageNos)
		resultEditorGridPanel.addEditorGridListener(new EditorGridListenerAdapter(){

			public boolean doBeforeEdit(GridPanel grid, Record record,
					String field, Object value, int rowIndex, int colIndex) {
				if(record.getAsDouble("postedIVAmount") != 0){
					MessageBox.alert("Please reset posted IV to 0 before adding to package.");
					return false;
				}
				if(record.getAsString("packageNo") != null && !awardedPackageNos.contains(record.getAsString("packageNo"))){
					MessageBox.alert("Please update non-awarded packages in Repackaging Enquiry Update Screen.");
					return false;
				}
				return true;
			}
		});


		// Check for access rights - then add toolbar buttons accordingly
		try{
			UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010109_ADD_ADDENDUM_TO_SUBCONTRACT_WINDOW, new AsyncCallback<List<String>>(){
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


		addAddendumButton = new Button("Save Updates");
		addAddendumButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				addAddendumButton.setDisabled(true);
				globalMessageTicket.refresh();
				checkEntryStatusAndAddAddendum();			
			};
		});		

		this.addButton(addAddendumButton);

		addAddendumButton.hide();

		Button closeWindowButton = new Button("Close");
		closeWindowButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				if (dataStore.getModifiedRecords().length!=0){
					MessageBox.confirm("Confirm", "Are you sure you want to discard the changes?",   
	                        new MessageBox.ConfirmCallback() {   
	                            public void execute(String btnID) {
	                            	if (btnID.equals("yes"))
	                            		repackagingListGridPanel.closeCurrentWindow();
	                            }   
	                        });   
				}
				else
					repackagingListGridPanel.closeCurrentWindow();				
			};
		});		
		this.addButton(closeWindowButton);
	}

	private void displayButtons(){
		if(accessRightsList != null && accessRightsList.contains("WRITE")){
		//	saveUpdatesButton.show();
			addAddendumButton.show();
		}
	}

	public void search(){
		packageNo = packageNoTextField.getText().trim();
		objectCode = objectCodeTextField.getText().trim();
		if (objectCode.length() == 0)
			objectCode = "14%";
		else if(!objectCode.startsWith("14")){
			MessageBox.alert("Object code must begin with '14'");
			return;
		}
		subsidiaryCode = subsidiaryCodeTextField.getText().trim();
		description = descriptionTextField.getText().trim();
		type = typeComboBox.getValue();
		
		newData.clear();
		data.clear();
		searchByPage(0);
	}

	public void searchByPage(int pageNum){
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Loading...", true);
		
		try {
			SessionTimeoutCheck.renewSessionTimer();
			bqResourceSummaryRepository.obtainResourceSummariesSearchByPage(globalSectionController.getJob(), packageNo, objectCode, 
					subsidiaryCode, description, type, levyExcluded, defectExcluded, pageNum, new AsyncCallback<RepackagingPaginationWrapper<BQResourceSummary>>(){
				public void onSuccess(final RepackagingPaginationWrapper<BQResourceSummary> wrapper) {
					populateGrid(wrapper);
					if (filterCheckBox.isChecked())
						filter();
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
				data.add(record);
				if (record.getAsString("packageNo")==null || "".equals(record.getAsString("packageNo")))
					newData.add(record);
				dataStore.add(record);
			}
		}
		catch(Exception e){
			UIUtil.alert(e);
		}
	}

	public void saveResourceSummaries(List<BQResourceSummary> resourceSummaries){
		// modified by brian on 20110321
		UIUtil.maskPanelById(WINDOW_ID, "Updating", true);
		try{
			SessionTimeoutCheck.renewSessionTimer();
			bqResourceSummaryRepository.saveResourceSummariesScAddendum(resourceSummaries, repackagingEntryId, new AsyncCallback<Boolean>(){
				public void onSuccess(Boolean success) {
					UIUtil.unmaskPanelById(WINDOW_ID);
					dataStore.commitChanges();
					repackagingListGridPanel.updateStatus("200");
					search();
					MessageBox.alert("Update Successful");
					addAddendumButton.setDisabled(false);
				}
				public void onFailure(Throwable e) {
					UIUtil.unmaskPanelById(WINDOW_ID);
					UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
					addAddendumButton.setDisabled(false);
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
		if(record.getAsString("packageNo") != null && record.getAsString("packageNo").trim().length() > 0)
			resourceSummary.setPackageNo(record.getAsString("packageNo").trim());
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
		resourceSummary.setExcludeLevy(record.getAsBoolean("excludeLevy"));
		resourceSummary.setExcludeDefect(record.getAsBoolean("excludeDefect"));
		return resourceSummary;
	}

	public Record recordFromResourceSummary(BQResourceSummary resourceSummary){
		Double quantity = resourceSummary.getQuantity();
		Double rate = resourceSummary.getRate();
		Double amount = quantity*rate;
		Record record = resourceSummaryRecordDef.createRecord(new Object[]{
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
				resourceSummary.getPackageNo()
		});
		return record;
	}

	private boolean recordsIsDirty(){
		boolean isDirty = false;
		Record[] records = dataStore.getRecords();
		for (int i=0; i< records.length-1; i++ ) {
			if (records[i].isDirty() ) {
				isDirty = true;
				return isDirty;
			}
		}
		return isDirty;
	}
	public void setGlobalSectionController(GlobalSectionController globalSectionController) {
		this.globalSectionController = globalSectionController;
	}

	public GlobalSectionController getGlobalSectionController() {
		return globalSectionController;
	}

	public void setArrowKeyNavigation(ArrowKeyNavigation arrowKeyNavigation) {
		this.arrowKeyNavigation = arrowKeyNavigation;
	}

	public ArrowKeyNavigation getArrowKeyNavigation() {
		return arrowKeyNavigation;
	}
	
	private void checkForDuplicateResourceSummaries(){
		Record[] records = dataStore.getModifiedRecords();
		if(records.length == 0){
			MessageBox.alert("No updates to be saved");
			addAddendumButton.setDisabled(false);
			return;
		}
		final List<BQResourceSummary> resourceSummaries = new ArrayList<BQResourceSummary>();
		for(Record record : records){
			BQResourceSummary resourceSummary = resourceSummaryFromRecord(record);
			if(resourceSummaries.contains(resourceSummary)){
				MessageBox.alert("Duplicate resource: " + "Package: " + resourceSummary.getPackageNo() + 
						", Object Code: " + resourceSummary.getObjectCode() + ", Subsidiary Code: " + resourceSummary.getSubsidiaryCode() + 
						", Unit: " + resourceSummary.getUnit() + ", Rate: " + resourceSummary.getRate());
				addAddendumButton.setDisabled(false);
				return;
			}
			else
				resourceSummaries.add(resourceSummary);
		}
		// modified by brian on 20110321
		UIUtil.maskPanelById(WINDOW_ID, "Checking for duplicate resources", true);
		SessionTimeoutCheck.renewSessionTimer();
		bqResourceSummaryRepository.checkForDuplicates(resourceSummaries, globalSectionController.getJob(), new AsyncCallback<String>(){
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(WINDOW_ID);
				addAddendumButton.setDisabled(false);
				UIUtil.checkSessionTimeout(e, false,globalSectionController.getUser());
			}

			public void onSuccess(String error) {
				UIUtil.unmaskPanelById(WINDOW_ID);
				if(error != null){
					MessageBox.alert(error);
					addAddendumButton.setDisabled(false);
				}
				else
					setupAddendumWrapper(resourceSummaries);
			}
			
		});
	}

	private void setupAddendumWrapper(List<BQResourceSummary> resourceSummaries)
	{
		UIUtil.maskPanelById(WINDOW_ID, "Updating", true);
		ArrayList<AddAddendumWrapper> wrapperList = new ArrayList<AddAddendumWrapper>();
		Record[] records = dataStore.getModifiedRecords();
		for(int i =0; i <records.length; i++){
			AddAddendumWrapper wrapper = new AddAddendumWrapper();

			wrapper.setJobNumber(globalSectionController.getJob().getJobNumber());
			if (" ".equals(records[i].getAsString("packageNo")))
				wrapper.setScbcontractNo(null);
			else
				wrapper.setScbcontractNo(new Integer(records[i].getAsString("packageNo")));
			//Only VO will be V1 line , OI or Original Resource should be V3
			if (records[i].getAsString("resourceType").equals("VO"))
				wrapper.setScLineType("V1");
			else
				wrapper.setScLineType("V3");
			wrapper.setBqDescription(records[i].getAsString("resourceDescription"));
			wrapper.setObject(records[i].getAsString("objectCode"));
			wrapper.setSubsidiary(records[i].getAsString("subsidiaryCode"));

			wrapper.setUnit(records[i].getAsString("unit"));
			wrapper.setUserID(this.globalSectionController.getUser().getUsername());

			wrapper.setBqQuantity(records[i].getAsDouble("quantity"));

			wrapper.setCostRate(records[i].getAsDouble("rate"));
			wrapper.setResourceNo(Integer.parseInt(records[i].getAsString("id")));
			wrapper.setScRate(0.00);
			wrapper.setOldPackageNo(records[i].getAsString("oldPackageNo"));
			wrapperList.add(wrapper);
		}
		addAddendum(wrapperList, resourceSummaries);
	}
	
	private void checkEntryStatusAndAddAddendum(){
		UIUtil.maskPanelById(WINDOW_ID, "Checking Entry Status", true);
		SessionTimeoutCheck.renewSessionTimer();
		repackagingEntryRepository.getRepackagingEntry(repackagingEntryId, new AsyncCallback<RepackagingEntry>(){
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(WINDOW_ID);
				UIUtil.checkSessionTimeout(e, false,globalSectionController.getUser());
				addAddendumButton.setDisabled(false);
			}

			public void onSuccess(RepackagingEntry repackagingEntry) {
				UIUtil.unmaskPanelById(WINDOW_ID);
				if("900".equals(repackagingEntry.getStatus())){
					MessageBox.alert("This entry has already been confirmed");
					addAddendumButton.setDisabled(false);
				}
				else
					checkForDuplicateResourceSummaries();
			}
			
		});
	}

	private void addAddendum(ArrayList<AddAddendumWrapper> wrapperList, final List<BQResourceSummary> resourceSummaries){
		SessionTimeoutCheck.renewSessionTimer();
			packageRepository.addAddendumByWrapperListStr(wrapperList, new AsyncCallback<String>() {			
				public void onSuccess(String result) {
					UIUtil.unmaskPanelById(WINDOW_ID);
					if("".equals(result) || result == null){
						saveResourceSummaries(resourceSummaries);
					}
					else{
						MessageBox.alert(result);
						addAddendumButton.setDisabled(false);
					}
				}
				public void onFailure(Throwable e) {
					UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
					UIUtil.unmaskPanelById(WINDOW_ID);
					addAddendumButton.setDisabled(false);
				}
			});
	}
	
	public void filter(){
		if (filterCheckBox.isChecked() && newData!=null){
			Record[] newDataRecords = new Record[newData.size()];
			for (int i =0; i < newData.size(); i++)
				newDataRecords[i] = newData.get(i);
			
			dataStore.removeAll();
			dataStore.add(newDataRecords);
		}
		else{
			Record[] dataRecords = new Record[data.size()];
			for (int i =0; i < data.size(); i++)
				dataRecords[i] = data.get(i);
			
			dataStore.removeAll();
			dataStore.add(dataRecords);
		}
	}
	
	private void obtainAwardedPackageNos(){
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.getAwardedPackageNos(globalSectionController.getJob(), new AsyncCallback<List<String>>(){
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}
			public void onSuccess(List<String> packageNos) {
				awardedPackageNos = packageNos;
			}
		});
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
