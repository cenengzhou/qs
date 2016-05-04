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
import com.gammon.qs.domain.TransitUomMatch;
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
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
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

public class TransitUomMatchingPanel extends Panel {
	private MainSectionController mainSectionController;
	private List<String> accessRightsList;
	
	private Store dataStore;
	
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private TransitRepositoryRemoteAsync transitRepository;
	
	private RecordDef codeMatchRecordDef = new RecordDef(new FieldDef[]{
		new StringFieldDef("causewayUom"),
		new StringFieldDef("jdeUom")
	});
	
	private PaginationToolbar paginationToolbar;
	
	//Search fields/parameters
	private TextField causewayUomField;
	private TextField jdeUomField;
	private String causewayUom;
	private String jdeUom;
	
	Button uploadButton;
	
	public TransitUomMatchingPanel(MainSectionController mainSectionController) {
		super();
		this.mainSectionController = mainSectionController;
		
		this.setTitle("Unit Code Matching");
		this.setFrame(true);
		this.setLayout(new RowLayout());
		
		this.userAccessRightsRepository = mainSectionController.getGlobalSectionController().getUserAccessRightsRepository();
		this.transitRepository = mainSectionController.getGlobalSectionController().getTransitRepository();
		
		
//		transitRepository = (TransitRepositoryRemoteAsync) GWT.create(TransitRepositoryRemote.class);
//		((ServiceDefTarget)transitRepository).setServiceEntryPoint(GlobalParameter.TRANSIT_REPOSITORY_URL);
		
		Panel searchPanel = new Panel();
		searchPanel.setFrame(true);
		searchPanel.setHeight(40);
		searchPanel.setLayout(new TableLayout());
		
		FieldListener searchListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER)
					search();
			}
		};
		
		Label causewayUomLabel = new Label("Causeway Unit Code");
		causewayUomLabel.setCls("table-cell");
		searchPanel.add(causewayUomLabel, new ColumnLayoutData(20));
		causewayUomField = new TextField("Causeway Unit Code", "jdeUom", 60);
		causewayUomField.addListener(searchListener);
		searchPanel.add(causewayUomField);
		
		Label jdeUomLabel = new Label("JDE Unit Code");
		jdeUomLabel.setCls("table-cell");
		searchPanel.add(jdeUomLabel, new ColumnLayoutData(20));
		jdeUomField = new TextField("JDE Unit Code", "jdeUom", 60);
		jdeUomField.addListener(searchListener);
		searchPanel.add(jdeUomField);
		
		searchPanel.add(new Label(""));
		
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
				com.google.gwt.user.client.Window.open(GlobalParameter.TRANSIT_DOWNLOAD_URL + "?type=" + GlobalParameter.TRANSIT_UOM_MATCHING, "_blank", "");
			}
		});
		toolbarPanel.add(downloadButton);
		
		TextField fileField = new TextField("Excel File", "file");
		fileField.setInputType("file");
		fileField.setAllowBlank(false);
		toolbarPanel.add(fileField);
		
		Hidden typeHiddenField = new Hidden("type", GlobalParameter.TRANSIT_UOM_MATCHING);
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
				
		ColumnConfig causewayColumn = new ColumnConfig("Causeway UOM", "causewayUom", 100, false);
		ColumnConfig jdeUomColumn = new ColumnConfig("JDE UOM", "jdeUom", 60, false);
				
		ColumnModel columns = new ColumnModel(new ColumnConfig[]{
			causewayColumn,
			jdeUomColumn
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
	
	private void search(){
		causewayUom = causewayUomField.getValueAsString();
		jdeUom = jdeUomField.getValueAsString();
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, "Searching", true);
		SessionTimeoutCheck.renewSessionTimer();
		transitRepository.searchTransitUomMatches(causewayUom, jdeUom, 
				new AsyncCallback<PaginationWrapper<TransitUomMatch>>(){
					public void onFailure(Throwable e) {
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
						UIUtil.checkSessionTimeout(e, false,mainSectionController.getGlobalSectionController().getUser());
					}

					public void onSuccess(PaginationWrapper<TransitUomMatch> wrapper) {
						populateGrid(wrapper);
						UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
					}
		});
	}
	
	private void searchByPage(int pageNum){
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, "Searching", true);
		SessionTimeoutCheck.renewSessionTimer();
		transitRepository.searchTransitUomMatchesByPage(pageNum, new AsyncCallback<PaginationWrapper<TransitUomMatch>>(){
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
				UIUtil.checkSessionTimeout(e, false,mainSectionController.getGlobalSectionController().getUser());
			}

			public void onSuccess(PaginationWrapper<TransitUomMatch> wrapper) {
				populateGrid(wrapper);
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}
		});
	}
	
	private void populateGrid(PaginationWrapper<TransitUomMatch> wrapper){
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
		for(TransitUomMatch codeMatch : wrapper.getCurrentPageContentList()){
			Record record = codeMatchRecordDef.createRecord(new Object[]{
				codeMatch.getCausewayUom(),
				codeMatch.getJdeUom()
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
	
	private void securitySetup() {
		// Enhancement: 
		// Check for access rights
		try{
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(this.mainSectionController.getGlobalSectionController().getUser().getUsername(), RoleSecurityFunctions.F010606_TRANSIT_UOM_MATCHINGPANEL, new AsyncCallback<List<String>>(){
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
}

