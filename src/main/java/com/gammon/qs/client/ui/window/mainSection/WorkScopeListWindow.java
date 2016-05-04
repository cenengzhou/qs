package com.gammon.qs.client.ui.window.mainSection;


import java.util.List;


import com.gammon.qs.client.controller.GlobalSectionController;

import com.gammon.qs.client.ui.ArrowKeyNavigation;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.wrapper.UDC;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

public class WorkScopeListWindow extends Window {
	private static final String RESULT_PANEL_ID ="resultPanel"; 
	private static final String MAIN_PANEL_ID ="mainPanel"; 
	private static final String WORK_SCOPE_LIST_WINDOW = "workScopeWindow";
	
	//UI
	private Panel mainPanel;
	private Panel searchPanel;
	private Panel resultPanel;
	private EditorGridPanel resultEditorGridPanel;

	
	//data store
	private Store dataStore;
	
	// cursor navigation
	private ArrowKeyNavigation arrowKeyNavigation;
	
	private ColumnConfig workScopeColConfig;
	private ColumnConfig descriptionColConfig;
	
	private GlobalSectionController globalSectionController;
	private GlobalMessageTicket globalMessageTicket;
	
	
	private final RecordDef workScopeRecordDef = new RecordDef(
			new FieldDef[]{
					new StringFieldDef("workScope"),
					new StringFieldDef("description")
			});
	
	private Button closeWindowButton;
	private Button selectButton;
	private List<UDC> workScopeList;
	private Integer rowInteger;
	private Record selectedRecord;
	
	public WorkScopeListWindow(final EditPackageHeaderWindow editPackageHeaderWindow){
		super();
		globalMessageTicket = new GlobalMessageTicket();
		this.setModal(true);
		this.setArrowKeyNavigation(new ArrowKeyNavigation(resultEditorGridPanel));
		this.setTitle("Work Scope List");
		this.setPaddings(5);
		this.setWidth(400);
		this.setHeight(500);
		this.setClosable(false);
		this.setLayout(new FitLayout());
		this.setId(WorkScopeListWindow.WORK_SCOPE_LIST_WINDOW);
		this.globalSectionController = editPackageHeaderWindow.getGlobalSectionController();
		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setId(MAIN_PANEL_ID);

		//search Panel
		searchPanel = new Panel();
		searchPanel.setPaddings(3);
		searchPanel.setFrame(true);
		searchPanel.setHeight(50);
		searchPanel.setLayout(new TableLayout(8));
		resultPanel = new Panel();
		resultPanel.setId(RESULT_PANEL_ID);
		resultPanel.setBorder(true);
		resultPanel.setFrame(true);
		resultPanel.setPaddings(5);
		resultPanel.setAutoScroll(true);
		resultPanel.setLayout(new FitLayout());

		workScopeColConfig = new ColumnConfig("Work Scope", "workScope", 100, false);
		descriptionColConfig = new ColumnConfig("Description", "description", 230, false);
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(workScopeRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		resultEditorGridPanel =  new EditorGridPanel();
		resultEditorGridPanel.setStore(dataStore);
		BaseColumnConfig[] columns = new BaseColumnConfig[]{
				workScopeColConfig,
				descriptionColConfig
		};
		resultEditorGridPanel.setColumnModel(new ColumnModel(columns));
		
		selectButton = new Button("Select");
		selectButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				globalMessageTicket.refresh();
				selectedRecord = dataStore.getAt(rowInteger);
				String workScope = (selectedRecord.getAsObject("workScope") != null ? selectedRecord.getAsString("workScope") : "");
				String description = (selectedRecord.getAsObject("description") != null ? selectedRecord.getAsString("description") : "");
				editPackageHeaderWindow.setSelectedWorkScope(workScope, description);
				editPackageHeaderWindow.closeCurrentWindow();
			};
		});
		
		closeWindowButton = new Button("Close");
		closeWindowButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				editPackageHeaderWindow.closeCurrentWindow();
			};
		});
		//mainPanel.add(searchPanel);
		resultPanel.add(resultEditorGridPanel);
		mainPanel.add(resultPanel);
		this.add(mainPanel);
		this.addButton(selectButton);
		this.addButton(closeWindowButton);
		resultEditorGridPanel.addGridRowListener(new GridRowListenerAdapter() {
			public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
				globalMessageTicket.refresh();
				Record curRecord = dataStore.getAt(rowIndex);
				String workScope = (curRecord.getAsObject("workScope") != null ? curRecord.getAsString("workScope") : "");
				String description = (curRecord.getAsObject("description") != null ? curRecord.getAsString("description") : "");
				editPackageHeaderWindow.setSelectedWorkScope(workScope, description);
				editPackageHeaderWindow.closeCurrentWindow();
			}
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				rowInteger = rowIndex;
			}
		});
		getWorkScopeList();
	}
	public void populateGrid(){
		dataStore.removeAll();
		for(UDC curObj: workScopeList){
			Record record = workScopeRecordDef.createRecord(new Object[]{
					curObj.getCode(),
					curObj.getDescription()
			});
			dataStore.add(record);
		}
	}

	public void getWorkScopeList() {
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPackageRepository().obtainWorkScopeList(new AsyncCallback<List<UDC>>() {
			public void onFailure(Throwable e) {
			}

			public void onSuccess(List<UDC> result) {
				workScopeList = result;
				populateGrid();
			}
		});
	}
	
	public void setArrowKeyNavigation(ArrowKeyNavigation arrowKeyNavigation) {
		this.arrowKeyNavigation = arrowKeyNavigation;
	}

	public ArrowKeyNavigation getArrowKeyNavigation() {
		return arrowKeyNavigation;
	}
}
