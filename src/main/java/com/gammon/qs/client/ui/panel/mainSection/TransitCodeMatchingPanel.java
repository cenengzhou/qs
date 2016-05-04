package com.gammon.qs.client.ui.panel.mainSection;

import java.util.List;

import com.gammon.qs.client.controller.MainSectionController;
import com.gammon.qs.client.repository.TransitRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.TransitCodeMatch;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.Connection;
import com.gwtext.client.core.EventObject;
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
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Form;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.form.event.FormListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

public class TransitCodeMatchingPanel extends Panel {
	private MainSectionController mainSectionController;
	private List<String> accessRightsList;
	
	private Store dataStore;
	
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private TransitRepositoryRemoteAsync transitRepository;
	
	private RecordDef codeMatchRecordDef = new RecordDef(new FieldDef[]{
		new StringFieldDef("matchingType"),
		new StringFieldDef("resourceCode"),
		new StringFieldDef("objectCode"),
		new StringFieldDef("subsidiaryCode")
	});
	
	private static final SimpleStore matchingCodeStore = new SimpleStore(
			new String[]{"code", "description"},
			new String[][]{new String[]{"EX", "EX - Expanded"},
					new String[]{"BS", "BS - Basic"},
					new String[]{"FD", "FD - Foundation"},
					new String[]{"SG", "SG - Singapore"}});
	
	private PaginationToolbar paginationToolbar;
	
	//Search fields/parameters
	private ComboBox matchingTypeField;
	private TextField resourceCodeField;
	private TextField objectCodeField;
	private TextField subsidiaryCodeField;
	private String matchingType;
	private String resourceCode;
	private String objectCode;
	private String subsidiaryCode;
	
	private Button uploadButton;
	
	public TransitCodeMatchingPanel(MainSectionController mainSectionController) {
		super();
		this.mainSectionController = mainSectionController;
		
		this.setTitle("Resource Code Matching");
		this.setFrame(true);
		this.setLayout(new RowLayout());
		
		this.userAccessRightsRepository = mainSectionController.getGlobalSectionController().getUserAccessRightsRepository();
		this.transitRepository = mainSectionController.getGlobalSectionController().getTransitRepository();
//		transitRepository = (TransitRepositoryRemoteAsync) GWT.create(TransitRepositoryRemote.class);
//		((ServiceDefTarget)transitRepository).setServiceEntryPoint(GlobalParameter.TRANSIT_REPOSITORY_URL);
		
		Panel searchPanel = new Panel();
		searchPanel.setFrame(true);
		searchPanel.setHeight(40);
		searchPanel.setLayout(new TableLayout(10));
		
		FieldListener searchListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER)
					search();
			}
		};
		
		Label matchingTypeLabel = new Label("Matching Type");
		matchingTypeLabel.setCls("table-cell");
		searchPanel.add(matchingTypeLabel, new ColumnLayoutData(20));
		matchingTypeField = new ComboBox("Matching Type", "matchingType", 50);
		matchingTypeField.setListWidth(150);
		matchingTypeField.setForceSelection(true);
		matchingTypeField.setStore(matchingCodeStore);
		matchingTypeField.setValueField("code");
		matchingTypeField.setDisplayField("description");
		matchingTypeField.setCtCls("table-cell");
		matchingTypeField.setCls("table-cell");
		searchPanel.add(matchingTypeField);
		
		Label resourceCodeLabel = new Label("Resource Code");
		resourceCodeLabel.setCls("table-cell");
		searchPanel.add(resourceCodeLabel, new ColumnLayoutData(20));
		resourceCodeField = new TextField("Resource Code", "resourceCode", 100);
		resourceCodeField.setCtCls("table-cell");
		resourceCodeField.setCls("table-cell");
		resourceCodeField.addListener(searchListener);
		searchPanel.add(resourceCodeField);
		
		Label objectCodeLabel = new Label("Object Code");
		objectCodeLabel.setCls("table-cell");
		searchPanel.add(objectCodeLabel, new ColumnLayoutData(20));
		objectCodeField = new TextField("Object Code", "objectCode", 100);
		objectCodeField.setCtCls("table-cell");
		objectCodeField.setCls("table-cell");
		objectCodeField.addListener(searchListener);
		searchPanel.add(objectCodeField);
		
		Label subsidiaryCodeLabel = new Label("Subsidiary Code");
		subsidiaryCodeLabel.setCls("table-cell");
		searchPanel.add(subsidiaryCodeLabel, new ColumnLayoutData(20));
		subsidiaryCodeField = new TextField("Subsidiary Code", "subsidiaryCode", 100);
		subsidiaryCodeField.setCtCls("table-cell");
		subsidiaryCodeField.setCls("table-cell");
		subsidiaryCodeField.addListener(searchListener);
		searchPanel.add(subsidiaryCodeField);
		
		Button searchButton = new Button("Search");
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				search();
			};
		});
		searchButton.setCls("table-cell");
		searchPanel.add(searchButton, new ColumnLayoutData(20));
		
		final FormPanel formPanel = new FormPanel();
		formPanel.setFileUpload(true);
		formPanel.setPaddings(5);
		formPanel.setHeight(35);
		Panel toolbarPanel = new Panel();
		toolbarPanel.setLayout(new HorizontalLayout(20));
		
		Button downloadButton = new Button("Download to Excel", new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if(dataStore.getCount() == 0){
					MessageBox.alert("This function will only download the items matching the search query. Please search for the desired items before downloading");
					return;
				}
				com.google.gwt.user.client.Window.open(GlobalParameter.TRANSIT_DOWNLOAD_URL + "?type=" + GlobalParameter.TRANSIT_CODE_MATCHING, "_blank", "");
			}
		});
		toolbarPanel.add(downloadButton);
		
		TextField fileField = new TextField("Excel File", "file");
		fileField.setInputType("file");
		fileField.setAllowBlank(false);
		toolbarPanel.add(fileField);
		
		Hidden typeHiddenField = new Hidden("type", GlobalParameter.TRANSIT_CODE_MATCHING);
		toolbarPanel.add(typeHiddenField);
		
		Hidden jobNumberHiddenField = new Hidden("jobNumber", mainSectionController.getCurrentJobNumber());
		toolbarPanel.add(jobNumberHiddenField);
		
		uploadButton = new Button("Upload from Excel", new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				formPanel.getForm().submit(GlobalParameter.TRANSIT_UPLOAD_URL, null, Connection.POST, "processing...", false);
			}
		});
		uploadButton.hide();
		toolbarPanel.add(uploadButton);
		
		securitySetup();
		
		formPanel.add(toolbarPanel);
		
		formPanel.addFormListener(new FormListenerAdapter() {
			public void onActionComplete(Form form, int httpStatus, String responseText) {
				transitResponseCallback(responseText);
			}
			
			public void onActionFailed(Form form, int httpStatus, String responseText) {
				transitResponseCallback(responseText);
			}
		});
		
		GridPanel gridPanel = new GridPanel();
		
		ColumnConfig matchingTypeColumn = new ColumnConfig("Matching Type", "matchingType", 100, false);
		ColumnConfig resourceCodeColumn = new ColumnConfig("Resource Code", "resourceCode", 100, false);
		ColumnConfig objectCodeColumn = new ColumnConfig("Object Code", "objectCode", 100, false);
		ColumnConfig subsidiaryCodeColumn = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 100, false);
		
		ColumnModel columns = new ColumnModel(new ColumnConfig[]{
			matchingTypeColumn,
			resourceCodeColumn,
			objectCodeColumn,
			subsidiaryCodeColumn
		});
		gridPanel.setColumnModel(columns);
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(codeMatchRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		gridPanel.setStore(dataStore);
		
		GridView view = new CustomizedGridView();
		gridPanel.setView(view);
		
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(int pageNum){
				searchByPage(pageNum);
			}
		});
		paginationToolbar.setTotalPage(0);
		paginationToolbar.setCurrentPage(0);
		gridPanel.setBottomToolbar(paginationToolbar);
		
		this.add(searchPanel);
		this.add(formPanel);
		this.add(gridPanel);
	}
	
	private void securitySetup() {
		// Enhancement: 
		// Check for access rights
		try{
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(this.mainSectionController.getGlobalSectionController().getUser().getUsername(), RoleSecurityFunctions.F010605_TRANSIT_CODE_MATCHINGPANEL, new AsyncCallback<List<String>>(){
				public void onSuccess(List<String> accessRightsReturned) {
					accessRightsList = accessRightsReturned;

					//Display Upload Button for "WRITE" only
					if(accessRightsList!=null && accessRightsList.size()> 0 && accessRightsList.contains("WRITE"))
						uploadButton.show();
					else
						uploadButton.hide();
				}
				
				public void onFailure(Throwable e) {
					UIUtil.alert(e.getMessage());
				}
			});
			
			
		}catch(Exception e){
			UIUtil.alert(e.getMessage());
		}		
	}

	private void search(){
		matchingType = matchingTypeField.getValueAsString();
		resourceCode = resourceCodeField.getValueAsString();
		objectCode = objectCodeField.getValueAsString();
		subsidiaryCode = subsidiaryCodeField.getValueAsString();
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, "Searching", true);
		SessionTimeoutCheck.renewSessionTimer();
		transitRepository.searchTransitCodeMatches(matchingType, resourceCode, objectCode, subsidiaryCode, 
				new AsyncCallback<PaginationWrapper<TransitCodeMatch>>(){
					public void onFailure(Throwable e) {
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
						UIUtil.checkSessionTimeout(e, false,mainSectionController.getGlobalSectionController().getUser());
					}

					public void onSuccess(PaginationWrapper<TransitCodeMatch> wrapper) {
						populateGrid(wrapper);
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
					}
			
		});
	}
	
	private void searchByPage(int pageNum){
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, "Searching", true);
		SessionTimeoutCheck.renewSessionTimer();
		transitRepository.searchTransitCodeMatchesByPage(pageNum, new AsyncCallback<PaginationWrapper<TransitCodeMatch>>(){
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
				UIUtil.checkSessionTimeout(e, false,mainSectionController.getGlobalSectionController().getUser());
			}

			public void onSuccess(PaginationWrapper<TransitCodeMatch> wrapper) {
				populateGrid(wrapper);
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}
		});
	}
	
	private void populateGrid(PaginationWrapper<TransitCodeMatch> wrapper){
		dataStore.removeAll();
		if(wrapper == null){
			paginationToolbar.setTotalPage(0);
			paginationToolbar.setCurrentPage(0);
			return;
		}
		paginationToolbar.setTotalPage(wrapper.getTotalPage());
		paginationToolbar.setCurrentPage(wrapper.getCurrentPage());
		if(wrapper.getCurrentPageContentList() == null)
			return;
		for(TransitCodeMatch codeMatch : wrapper.getCurrentPageContentList()){
			Record record = codeMatchRecordDef.createRecord(new Object[]{
				codeMatch.getMatchingType(),
				codeMatch.getResourceCode(),
				codeMatch.getObjectCode(),
				codeMatch.getSubsidiaryCode()
			});
			dataStore.add(record);
		}
	}
	
	private void transitResponseCallback(String responseText) {
		JSONValue jsonValue = JSONParser.parse(responseText);
		JSONObject jsonObj = jsonValue.isObject();
		
		if (jsonObj.get("success").isBoolean().booleanValue()) {
			MessageBox.alert(jsonObj.get("numRecordImported").isNumber().doubleValue() + " records imported successfully.");
		} else {
			MessageBox.alert("Import failed! <br/> Cause: " + jsonObj.get("message").isString().stringValue());
		}
	}
}

