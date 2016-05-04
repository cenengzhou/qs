package com.gammon.qs.client.ui.window.mainSection;

import java.util.ArrayList;
import java.util.List;

import com.gammon.qs.application.User;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.BQRepositoryRemote;
import com.gammon.qs.client.repository.BQRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemote;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
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
import com.gammon.qs.domain.Resource;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
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
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
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
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

public class AddAddendumToScMethodTwoWindow extends Window {
	public static final String MAIN_PANEL_ID = "repackagingUpdateByBQWindow";
	// added by brian on 20110310
	private String WINDOW_ID = "AddAddendumToScMethodTwoWindow";
	
	private GlobalSectionController globalSectionController;
	private RepackagingListGridPanel repackagingListGridPanel;
	private Panel mainPanel;
	private Panel searchPanel;
	private EditorGridPanel resultEditorGridPanel;
	private GlobalMessageTicket globalMessageTicket;
	
	private BQRepositoryRemoteAsync bqRepository;
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	
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
	
	private final CheckboxSelectionModel cbSelectionModel;
	
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
	
	private Store packageStore;
	private ComboBox packageComboBox;
		
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
	
	private Button saveButton;
	
	public AddAddendumToScMethodTwoWindow(final RepackagingListGridPanel repackagingListGridPanel){
		super();
		this.setModal(true);
		// added by brian on 20110311
		this.setId(WINDOW_ID);
		this.repackagingListGridPanel = repackagingListGridPanel;
		this.globalSectionController = repackagingListGridPanel.getGlobalSectionController();
		this.globalMessageTicket = new GlobalMessageTicket();
		
		bqRepository = (BQRepositoryRemoteAsync)GWT.create(BQRepositoryRemote.class);
		((ServiceDefTarget)bqRepository).setServiceEntryPoint(GlobalParameter.BQ_REPOSITORY_URL);
		userAccessRightsRepository = (UserAccessRightsRepositoryRemoteAsync) GWT.create(UserAccessRightsRepositoryRemote.class);
		((ServiceDefTarget)userAccessRightsRepository).setServiceEntryPoint(GlobalParameter.USER_ACCESS_RIGHTS_REPOSITORY_URL);
		
		this.setTitle("Add/Edit Addendum to Subcontract");
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
		
		bpiColumn = new ColumnConfig("B/P/I", "bpi", 120, false);
		packageNoColumn = new ColumnConfig("Package No.", "packageNo", 40, false);
		packageNoColumn.setEditor(new GridEditor(packageComboBox));
		packageNoColumn.setTooltip("Package No.");
		objectCodeColumn = new ColumnConfig("Object Code", "objectCode", 50, false);
		objectCodeColumn.setTooltip("Object Code");
		subsidiaryCodeColumn = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 65, false);
		subsidiaryCodeColumn.setTooltip("Subsidiary Code");
		descriptionColumn = new ColumnConfig("Description", "description", 250, false);
		descriptionColumn.setEditor(new GridEditor(new TextField()));
		unitColumn = new ColumnConfig("Unit", "unit", 40, false);
		quantityColumn = new ColumnConfig("Qty", "quantity", 110, false);
		quantityColumn.setRenderer(quantityRenderer);		
		quantityColumn.setAlign(TextAlign.RIGHT);
		rateColumn = new ColumnConfig("Rate", "rate", 120, false);
		rateColumn.setRenderer(rateRenderer);
		rateColumn.setAlign(TextAlign.RIGHT);
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
		
		resultEditorGridPanel.addEditorGridListener(new EditorGridListenerAdapter(){
			public void onAfterEdit(GridPanel grid, Record record,
					String field, Object newValue, Object oldValue,
					int rowIndex, int colIndex) {
				if(field.equals("packageNo")){
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
				saveAddendums();
			};
		});
		
		Button closeButton = new Button("Close");
		closeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				if(dataStore.getModifiedRecords().length == 0)
					repackagingListGridPanel.closeCurrentWindow();
				else{
					MessageBox.confirm("Confirm", "Are you sure you want to discard the changes?",   
	                        new MessageBox.ConfirmCallback() {   
	                            public void execute(String btnID) {
	                            	if (btnID.equals("yes"))
	                            		repackagingListGridPanel.closeCurrentWindow();
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
			userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010116_ADD_ADDENDUM_TO_SC_METHOD_TWO_WINDOW, new AsyncCallback<List<String>>(){
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

	private void populateGrid(RepackagingPaginationWrapper<Resource> wrapper){
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
					resource.getResourceType()					
			});
			dataStore.add(record);
		}
	}
	
	private void search(){
		objectCode = objectCodeField.getValueAsString();
		if(objectCode == null || objectCode.trim().length() == 0)
			objectCode = "14%";
		else if(!objectCode.startsWith("14")){
			MessageBox.alert("Object code must begin with '14'");
			return;
		}
		
		jobNumber = globalSectionController.getJob().getJobNumber();
		billNo = billNoField.getValueAsString();
		subBillNo = subBillNoField.getValueAsString();
		pageNo = pageNoField.getValueAsString();
		itemNo = itemNoField.getValueAsString();
		packageNo = packageNoField.getValueAsString();
		subsidiaryCode = subsidiaryCodeField.getValueAsString();
		description = descriptionField.getValueAsString();
		searchByPage(0);
	}
	
	private void searchByPage(int pageNum){
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Searching", true);
		SessionTimeoutCheck.renewSessionTimer();
		bqRepository.searchResourcesByPage(jobNumber, billNo, subBillNo, pageNo, itemNo, packageNo, 
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
	
	private void saveAddendums(){
		Record[] records = dataStore.getModifiedRecords();
		if(records.length == 0)
			return;
		List<Resource> resources = new ArrayList<Resource>(records.length);
		for(Record record : records)
			resources.add(resourceFromRecord(record));
		
		// modified by brian on 20110310
		// also mask save button
//		UIUtil.maskPanelById(MAIN_PANEL_ID, "Saving...", true);
		UIUtil.maskPanelById(WINDOW_ID, "Saving...", true);
		SessionTimeoutCheck.renewSessionTimer();
		bqRepository.saveResourceSubcontractAddendums(resources, new AsyncCallback<String>(){
			public void onSuccess(String errorMsg) {
				// modified by brian on 20110311
//				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				UIUtil.unmaskPanelById(WINDOW_ID);
				if(errorMsg == null){
					MessageBox.alert("Save successful");
					dataStore.commitChanges();
					repackagingListGridPanel.updateStatus("200");
				}
				else
					MessageBox.alert(errorMsg);
			}
			
			public void onFailure(Throwable e) {
				// modified by brian on 20110311
//				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				UIUtil.unmaskPanelById(WINDOW_ID);
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
		resource.setPackageNo(record.getAsString("packageNo").trim());
		resource.setObjectCode(record.getAsString("objectCode").trim());
		resource.setSubsidiaryCode(record.getAsString("subsidiaryCode").trim());
		resource.setDescription(record.getAsString("description").trim());
		resource.setUnit(record.getAsString("unit").trim());
		Double remeasuredQuant = record.getAsDouble("quantity");
		Double remeasuredFactor = record.getAsDouble("remeasuredFactor");
		Double quantity = remeasuredQuant/remeasuredFactor;
		resource.setQuantity(quantity);
		resource.setRemeasuredFactor(remeasuredFactor);
		resource.setCostRate(record.getAsDouble("rate"));
		resource.setResourceType(record.getAsString("resourceType"));
		return resource;
	}
}