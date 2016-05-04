package com.gammon.qs.client.ui.window.treeSection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.ObjectSubsidiaryRuleRepositoryRemote;
import com.gammon.qs.client.repository.ObjectSubsidiaryRuleRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemote;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.StatusButtonPanel;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.DateUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.ObjectSubsidiaryRule;
import com.gammon.qs.domain.ObjectSubsidiaryRule.ObjectSubsidiaryRuleUpdateResponse;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;


/**
 * @author matthewatc
 * 16:51:15 29 Dec 2011 (UTC+8)
 * Window to search for display the properties of object subsidiary rule records
 * 
 * Modified:
 * @author matthewatc
 * 08:35:21 11 Jan 2012 (UTC+8)
 * Added capability for editing and creation of object subsidiary rule records
 * 
 * Modified:
 * @author matthewatc
 * 14:59:34 16 Jan 2012 (UTC+8)
 * Changed the behavior of editing: dirty records are now committed when the user clicks a button,
 * and all at once (in a single RPC).
 */
public class ObjectSubsidiaryRuleWindow extends Window {
	
	private GlobalSectionController globalSectionController;
	
	public static final String OBJ_SUB_WINDOW_ID = "ObjectSubsidiaryRuleWindow";
	public static final String RESULT_PANEL_ID = "ObjectSubsidiaryRuleWindowResultPanel";
	
	public static final String CODE_REGEX = "^[0-9]?[0-9]$"; // regex against which edits to the resource type, cost category and main trade fields are validated
	
	public static final Integer WINDOW_HEIGHT = 600;
	public static final Integer WINDOW_WIDTH = 745;
	public static final Integer SEARCH_FIELD_WIDTH = 110;
	public static final Integer SEARCH_PANEL_HEIGHT = 35;
	public static final Integer MAX_STATUS_WIDTH = 80; 
	
	public static final Integer CLOSE_BUTTON_WIDTH = 80; 
	public static final Integer CREATE_BUTTON_WIDTH = 90; 
	public static final Integer COMMIT_BUTTON_WIDTH = 100; 
	public static final Integer DETAILS_BUTTON_WIDTH = 100; 
		
	private Panel mainPanel;
	private Panel searchPanel;
	private Panel resultPanel;
	
	private StatusButtonPanel buttonPanel;
	
	private EditorGridPanel gridPanel;
	
	private PaginationToolbar paginationToolbar;
	
	private ColumnConfig[] columns;
	
	private TextField resourceField;
	private TextField costCategoryField;
	private TextField mainTradeField;
	
	private Button createButton;
	private Button commitButton;
	private Button detailsButton;
	
	private String resourceQuery;
	private String costCategoryQuery;
	private String mainTradeQuery;
	
	private ObjectSubsidiaryRuleRepositoryRemoteAsync objSubRuleRepository;
	
	private Store dataStore;
	
	private FieldListenerAdapter enterKeyAdapter;
	
	private PaginationWrapper<ObjectSubsidiaryRule> wrapper;
	
	private boolean canWrite = false; // true iff it is known that user has write permission
		
	private List<ObjectSubsidiaryRule[]> updateQueue; 
	private List<String> errorList;
	
	private final RecordDef objectSubsidiaryRuleRecordDef = new RecordDef(
			new FieldDef[] {
					new StringFieldDef("resourceType"),
					new StringFieldDef("costCategory"),
					new StringFieldDef("mainTrade"),
					new StringFieldDef("applicable"),
					new StringFieldDef("lastModifiedUser"),
					new StringFieldDef("lastModifiedDate"),
			}
	);

	protected Record record;
	
	public ObjectSubsidiaryRuleWindow (GlobalSectionController globalSectionControllerArgument) {
		
		super();
		this.globalSectionController = globalSectionControllerArgument;
		
		objSubRuleRepository = (ObjectSubsidiaryRuleRepositoryRemoteAsync) GWT.create(ObjectSubsidiaryRuleRepositoryRemote.class);
		((ServiceDefTarget)objSubRuleRepository).setServiceEntryPoint(GlobalParameter.OBJ_SUB_REPOSITORY_URL);
		
		updateQueue = new ArrayList<ObjectSubsidiaryRule[]>();
		errorList = new ArrayList<String>();
		
		configureTitle();
		
		this.setLayout(new FitLayout());
		this.setHeight(WINDOW_HEIGHT);
		this.setWidth(WINDOW_WIDTH);
		this.setId(OBJ_SUB_WINDOW_ID);
		this.setClosable(false);
		
		mainPanel = new Panel();
		mainPanel.setFrame(true);
		mainPanel.setLayout(new RowLayout());
		
		searchPanel = new Panel();
		searchPanel.setLayout(new TableLayout(8));
		searchPanel.setHeight(SEARCH_PANEL_HEIGHT);
		searchPanel.setPaddings(2);
		
		setUpSearchPanel();
		
		setUpResultPanel();
		
		buttonPanel = new StatusButtonPanel(MAX_STATUS_WIDTH);
		buttonPanel.setBorder(false);
		buttonPanel.setHeight(30);
		buttonPanel.setPaddings(4);
			
		detailsButton = new Button("View Details");
		detailsButton.addClass("right-align-button");
		detailsButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e){
				showDetailsWindow();
			}
		});
		buttonPanel.addButton(detailsButton, DETAILS_BUTTON_WIDTH, false);
		
		commitButton = new Button("Commit Changes");
		commitButton.addClass("right-align-button");
		commitButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e){
				if(canWrite) { commitChanges(); }
			}
		});
		buttonPanel.addButton(commitButton, COMMIT_BUTTON_WIDTH, false);
		commitButton.disable();
		
		createButton = new Button("Create New");
		createButton.addClass("right-align-button");
		createButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e){
				if(canWrite) { globalSectionController.showCreateObjectSubsidiaryRuleWindow(); }
			}
		});
		buttonPanel.addButton(createButton, CREATE_BUTTON_WIDTH, false);

		
		Button closeButton = new Button("Close");
		closeButton.addClass("right-align-button");
		closeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e){
				globalSectionController.closeCurrentWindow();
			}
		});
		buttonPanel.addButton(closeButton, CLOSE_BUTTON_WIDTH, true);
		
		Label placeholderLabel = new Label("");
		buttonPanel.add(placeholderLabel);

		mainPanel.add(searchPanel);
		mainPanel.add(resultPanel);
		mainPanel.add(buttonPanel);
		
		this.add(mainPanel);
		
		this.addListener( new WindowListenerAdapter() {
			public void onResize(Window source, int width, int height) {
				buttonPanel.resize();
			}	
		});
		
		getPermissions();
		resourceField.focus();
	}
	
	private void showDetailsWindow() {
		globalSectionController.showErrorDetailsWindow("View Details", "The following errors occured:", errorList);
	}
	
	private void commitChanges() {
		commitButton.disable();
		errorList.clear();
		UIUtil.maskPanelById(RESULT_PANEL_ID, "Updating records...", false);

		try {
			for(Record rec : dataStore.getModifiedRecords()) {
				if(dataStore.indexOf(rec) != -1) {
					ObjectSubsidiaryRule oldRule = wrapper.getCurrentPageContentList().get(dataStore.indexOf(rec));
					ObjectSubsidiaryRule newRule = new ObjectSubsidiaryRule(oldRule);
					
					newRule.setApplicable(rec.getAsString("applicable"));
					newRule.setResourceType(rec.getAsString("resourceType"));
					newRule.setCostCategory(rec.getAsString("costCategory"));
					newRule.setMainTrade(rec.getAsString("mainTrade"));
					
					if(!newRule.matches(oldRule, true)) {
						queueUpdateRule(oldRule, newRule);
					} else {
						rec.commit();
					}
				} else {
					errorList.add("Could not find record " + rec.getAsString("resourceType") + "/" + rec.getAsString("costCategory") + "/" + rec.getAsString("mainTrade") + " in DataStore");
				}
			}
			
			flushUpdateQueue();
			
		} catch (Exception e) {
			buttonPanel.showStatus("An unexpected exception occured: " + e + ": " + e.getMessage());
		}
	}
	
	private void flushUpdateQueue() {
		SessionTimeoutCheck.renewSessionTimer();
		objSubRuleRepository.updateMultipleObjectSubsidiaryRules(updateQueue, new AsyncCallback<List<ObjectSubsidiaryRuleUpdateResponse>>() {
						
			public void onFailure(Throwable e) {
				buttonPanel.showStatus("Update failed: " + e.getMessage());
				commitButton.enable();
				UIUtil.unmaskPanelById(RESULT_PANEL_ID);
			}
			
			public void onSuccess(List<ObjectSubsidiaryRuleUpdateResponse> ret) {
				try {
					for(ObjectSubsidiaryRuleUpdateResponse response : ret) {			
						if(response.updated) {
							
							/* find the (unique) original rule in the paginationWrapper */
							ObjectSubsidiaryRule oldRule = null;
							for(ObjectSubsidiaryRule osr : wrapper.getCurrentPageContentList()) {
								if(osr.getApplicable().equals(response.oldRule.getApplicable()) && osr.getResourceType().equals(response.oldRule.getResourceType()) 
										&& osr.getCostCategory().equals(response.oldRule.getCostCategory()) && osr.getMainTrade().equals(response.oldRule.getMainTrade())) 
								{
									oldRule = osr;
									break;
								}
							}
							
							if(oldRule == null) {
								errorList.add("could not find object subsidiary rule: " + response.newRule.getResourceType() + "/" + response.newRule.getCostCategory() + "/" + response.newRule.getMainTrade());
								break;
							}
							
							/* get the record that corresponds to oldRule */
							Record rec = dataStore.getAt(wrapper.getCurrentPageContentList().indexOf(oldRule));
							
							
							/* sync the properties of oldRule with the new state */
							oldRule.setApplicable(rec.getAsString("applicable"));
							oldRule.setResourceType(rec.getAsString("resourceType"));
							oldRule.setCostCategory(rec.getAsString("costCategory"));
							oldRule.setMainTrade(rec.getAsString("mainTrade"));
							
							/* simulate updating the modified user and date */
							rec.beginEdit();
							rec.set("lastModifiedUser", globalSectionController.getUser().getUsername());
							rec.set("lastModifiedDate", DateTimeFormat.getFormat(GlobalParameter.DATETIME_FORMAT).format(new Date()));
							rec.endEdit();
							
							rec.commit();
						} else {
							errorList.add(response.message);
						}
	
					}
					commitButton.enable();
					UIUtil.unmaskPanelById(RESULT_PANEL_ID);
					
					updateQueue.clear();
					
					if(errorList.isEmpty()) { // if all updates were successful
						buttonPanel.hideButton(detailsButton);
						buttonPanel.showStatus("All records successfully updated");
					} else { // if at least one update failed
						buttonPanel.showButton(detailsButton);
						buttonPanel.showStatus("Some records not updated; 'View Details' for more info");
					}
				} catch(Exception e) {
					buttonPanel.showStatus("An unexpected exception occured while updating records: " + e + ": " + e.getMessage());
				}
			}
		});
	}
	
	
	private void queueUpdateRule(ObjectSubsidiaryRule oldRule, ObjectSubsidiaryRule newRule) {
		ObjectSubsidiaryRule[] queueArray = new ObjectSubsidiaryRule[2];
		queueArray[0] = oldRule;
		queueArray[1] = newRule;
		updateQueue.add(queueArray);
	}
	
	private void getPermissions() {
		
		UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository = (UserAccessRightsRepositoryRemoteAsync) GWT.create(UserAccessRightsRepositoryRemote.class);
		((ServiceDefTarget)userAccessRightsRepository).setServiceEntryPoint(GlobalParameter.USER_ACCESS_RIGHTS_REPOSITORY_URL);
		SessionTimeoutCheck.renewSessionTimer();
		userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010512_OBJECT_SUBSIDIARY_RULE_WINDOW, new AsyncCallback<List<String>>(){
			public void onSuccess(List<String> accessRightsReturned) {
					if(accessRightsReturned.contains("WRITE")) {
						setCanWrite(true);
					} else {
						setCanWrite(false);
					}
			}
			
			public void onFailure(Throwable e) {
				buttonPanel.showStatus("An error occured while retrieving permissions from the server: " + e.getMessage());
				setCanWrite(false);
			}
		});
	}
	
	private void setCanWrite(boolean value){
		this.canWrite = value;
		
		if(canWrite) {
			buttonPanel.showButton(createButton);
			buttonPanel.showButton(commitButton);
		}
	}
	
	private void setUpResultPanel() {
		resultPanel = new Panel();
		
		resultPanel.setBorder(true);
		resultPanel.setFrame(true);
		resultPanel.setPaddings(5);
		resultPanel.setAutoScroll(true);
		resultPanel.setId(RESULT_PANEL_ID);
		
		resultPanel.setLayout(new FitLayout());
		gridPanel =  new EditorGridPanel();
		
		ColumnConfig resourceColumn = 			new ColumnConfig("Resource Type"		, "resourceType"	, 100, false);
		ColumnConfig costCategoryColumn = 		new ColumnConfig("Cost Category"		, "costCategory"	, 100, false);
		ColumnConfig mainTradeColumn = 			new ColumnConfig("Main Trade"			, "mainTrade"		, 100, false);
		ColumnConfig applicableColumn =			new ColumnConfig("Applicable"			, "applicable"		, 100, false);
		ColumnConfig lastModifiedUserColumn = 	new ColumnConfig("Last Modified User"	, "lastModifiedUser", 110, false);
		ColumnConfig lastModifiedDateColumn = 	new ColumnConfig("Last Modified Date"	, "lastModifiedDate", 130, false);
		
		TextField resourceColumnField = new TextField();
		resourceColumnField.setRegex(CODE_REGEX);
		resourceColumn.setEditor(new GridEditor(resourceColumnField));
		
		TextField costCategoryColumnField = new TextField();
		costCategoryColumnField.setRegex(CODE_REGEX);
		costCategoryColumn.setEditor(new GridEditor(costCategoryColumnField));
		
		TextField mainTradeColumnField = new TextField();
		mainTradeColumnField.setRegex(CODE_REGEX);
		mainTradeColumn.setEditor(new GridEditor(mainTradeColumnField));
		
		applicableColumn.setEditor(new GridEditor(new TextField())); //dummy textfield, editing should never get this far
		
		
		columns = new ColumnConfig[] {
				resourceColumn,
				costCategoryColumn,
				mainTradeColumn,
				applicableColumn,
				lastModifiedUserColumn,
				lastModifiedDateColumn
		};
		
		
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(objectSubsidiaryRuleRecordDef);
		this.dataStore = new Store(proxy, reader);
		this.dataStore.load();
		gridPanel.setStore(this.dataStore);
		gridPanel.setColumnModel(new ColumnModel(columns));
		
		setUpEditing();		
		
		resultPanel.add(gridPanel);
		
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setTotalPage(0);
		paginationToolbar.setCurrentPage(0);
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(int pageNum){
				doSearch(pageNum);
			}
		});
		
		resultPanel.setBottomToolbar(paginationToolbar);
	}
	
	private void setUpEditing() {
		gridPanel.addEditorGridListener( new EditorGridListenerAdapter(){
			public boolean doBeforeEdit(GridPanel grid, Record record, String field, Object value, int rowIndex,int colIndex) {
				if(!canWrite) { return false; }
				if(field.equals("applicable")) {
					String val = (String)value;
					// flip value on double-click
					if (val.trim().equals("Y")) { 
						val = "N"; 
					} else if (val.trim().equals("N")) { 
						val = "Y"; 
					} else {
						buttonPanel.showStatus("Error in updating applicability");
					}
					
					//record.beginEdit();
					record.set("applicable", val);
					//record.endEdit();
					
					commitButton.enable();
					return false;
				} else if(field.equals("lastModifiedDate") || field.equals("lastModifiedUser")) {
					return false; 
				}
				
				buttonPanel.showStatus("");
				return true;
			}
			
			public boolean doValidateEdit(GridPanel grid, Record record, String field, Object value, Object originalValue , int rowIndex, int colIndex) {
				String val = (String)value;
				String origVal = (String)originalValue;
				if(val.equals(origVal)) { return false; }
				if(!val.matches(CODE_REGEX)) { 
					buttonPanel.showStatus("Edit failed: code must be either 1 or two digits");
					return false; 
				}
				return true;
			}
			
			public void onAfterEdit(GridPanel grid, Record record, String field, Object newValue, Object oldValue, int rowIndex, int colIndex) {
				commitButton.enable();
			}
		});
	}
	
	public void populateGrid(PaginationWrapper<ObjectSubsidiaryRule> wrapper) {
		this.wrapper = wrapper;
		dataStore.removeAll();
		
		List<ObjectSubsidiaryRule> objectSubsidiaryRuleList = wrapper.getCurrentPageContentList();
		Record[] records = new Record[objectSubsidiaryRuleList.size()];
		
		int index = 0;
		
		for(ObjectSubsidiaryRule osr : objectSubsidiaryRuleList) {
			records[index] = objectSubsidiaryRuleRecordDef.createRecord(new Object[] {
					osr.getResourceType(),
					osr.getCostCategory(),
					osr.getMainTrade(),
					osr.getApplicable(),
					osr.getLastModifiedUser(),
//					DateTimeFormat.getFormat(GlobalParameter.DATETIME_FORMAT).format(osr.getLastModifiedDate())
					//DateTimeFormat.getShortDateTimeFormat().format(osr.getLastModifiedDate())
					DateUtil.formatDate(osr.getLastModifiedDate(), GlobalParameter.DATETIME_FORMAT)
			});
			
			index++;
		}
		
		dataStore.add(records);
		
		paginationToolbar.setTotalPage(wrapper.getTotalPage());
		paginationToolbar.setCurrentPage(wrapper.getCurrentPage());
	}
	
	private void setUpSearchPanel() {
		
		//search when user hits return on any of the search fields
		enterKeyAdapter = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER){
					resourceQuery = null;
					costCategoryQuery = null;
					mainTradeQuery = null;
					if (resourceField.getValueAsString().length() >= 1) { resourceQuery =  resourceField.getValueAsString(); }
					if (costCategoryField.getValueAsString().length() >= 1) { costCategoryQuery =  costCategoryField.getValueAsString();  }
					if (mainTradeField.getValueAsString().length() >= 1) { mainTradeQuery =  mainTradeField.getValueAsString();  }
					
					doSearch(0);
				}
			}
		};
		
		Label resourceLabel = new Label("Resource:");
		resourceLabel.setCls("table-cell");
		searchPanel.add(resourceLabel);
		resourceField = new TextField();
		resourceField.setWidth(SEARCH_FIELD_WIDTH);
		resourceField.addListener(enterKeyAdapter);
		searchPanel.add(resourceField);
		
		Label costCategoryLabel = new Label("Cost Category:");
		costCategoryLabel.setCls("table-cell");
		searchPanel.add(costCategoryLabel);
		costCategoryField = new TextField();
		costCategoryField.setWidth(SEARCH_FIELD_WIDTH);
		costCategoryField.addListener(enterKeyAdapter);
		searchPanel.add(costCategoryField);
		
		Label mainTradeLabel = new Label("Main Trade:");
		mainTradeLabel.setCls("table-cell");
		searchPanel.add(mainTradeLabel);
		mainTradeField = new TextField();
		mainTradeField.setWidth(SEARCH_FIELD_WIDTH);
		mainTradeField.addListener(enterKeyAdapter);
		searchPanel.add(mainTradeField);
		
		Button searchButton = new Button("Search");
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e) { 
				resourceQuery = null;
				costCategoryQuery = null;
				mainTradeQuery = null;
				if (resourceField.getValueAsString().length() >= 1) { resourceQuery =  resourceField.getValueAsString(); }
				if (costCategoryField.getValueAsString().length() >= 1) { costCategoryQuery =  costCategoryField.getValueAsString(); } 
				if (mainTradeField.getValueAsString().length() >= 1) { mainTradeQuery =  mainTradeField.getValueAsString(); }
				doSearch(0);
			}
		});
		
		Panel spacer = new Panel();
		spacer.setWidth(40);
		searchPanel.add(spacer);
		
		searchButton.setCls("table-cell-right-align");
		searchPanel.add(searchButton);
		searchPanel.add(new Label(""));
	}
	
	private void configureTitle() {
		this.setTitle("Object Subsidiary Rule");
	}
	
	private void doSearch(int page) {
		UIUtil.maskPanelById(RESULT_PANEL_ID, "Searching...", false);
		SessionTimeoutCheck.renewSessionTimer();
		objSubRuleRepository.findObjectSubsidiaryRuleByPage(page, resourceQuery, costCategoryQuery, mainTradeQuery, new AsyncCallback<PaginationWrapper<ObjectSubsidiaryRule>>() {
			
			public void onSuccess(PaginationWrapper<ObjectSubsidiaryRule> wrapper) {
				UIUtil.unmaskPanelById(RESULT_PANEL_ID);
				populateGrid(wrapper);	
			}
	
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(RESULT_PANEL_ID);
				buttonPanel.showStatus("Failed to retrieve results: " + e.getMessage());
			}
		});
	}
}
