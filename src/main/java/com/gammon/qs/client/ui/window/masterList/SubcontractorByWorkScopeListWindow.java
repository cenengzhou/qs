package com.gammon.qs.client.ui.window.masterList;


import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.ArrowKeyNavigation;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.masterList.WorkScopePanel;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.shared.GlobalParameter;
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

public class SubcontractorByWorkScopeListWindow extends Window {
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
	
	private ColumnConfig vendorNoColConfig;
	private ColumnConfig vendorNameColConfig;
	private ColumnConfig approvalStatusColConfig;
	private ColumnConfig vendorTypeColConfig;
	private ColumnConfig vendorStatusColConfig;
	
	private GlobalSectionController globalSectionController;
	private GlobalMessageTicket globalMessageTicket;
	
	
	private final RecordDef subcontractorRecordDef = new RecordDef(
			new FieldDef[]{
					new StringFieldDef("vendorNo"),
					new StringFieldDef("vendorName"),
					new StringFieldDef("approvalStatus"),
					new StringFieldDef("vendorType"),
					new StringFieldDef("vendorStatus")
			});
	
	@SuppressWarnings("unused")
	private Record selectedRecord;
	@SuppressWarnings("unused")
	private WorkScopePanel workScopePanel;

	private Button closeWindowButton;
	private Button selectButton;
	private List<MasterListVendor> subcontractorList;
	private Integer rowInteger;
	private String workScope;
	
	public SubcontractorByWorkScopeListWindow(final WorkScopePanel workScopePanel, String workScope){
		super();
		globalMessageTicket = new GlobalMessageTicket();
		this.globalSectionController = workScopePanel.getGlobalSectionController();
		this.setModal(true);
		this.setArrowKeyNavigation(new ArrowKeyNavigation(resultEditorGridPanel));
		this.setTitle("Subcontractor List of Work Scope " + workScope);
		this.setPaddings(5);
		this.setWidth(800);
		this.setHeight(500);
		this.setClosable(false);
		this.setLayout(new FitLayout());
		this.setId(SubcontractorByWorkScopeListWindow.WORK_SCOPE_LIST_WINDOW);
		this.workScopePanel = workScopePanel;
		this.workScope = workScope;
		
		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setId(MAIN_PANEL_ID);
		
		//UIUtil.maskPanelById(WorkScopeListWindow.WORK_SCOPE_LIST_WINDOW, "Loading....", false);
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
		
		vendorNoColConfig = new ColumnConfig("Vendor Number", "vendorNo", 100, true);
		vendorNameColConfig = new ColumnConfig("Vendor Name", "vendorName", 230, true);
		approvalStatusColConfig = new ColumnConfig("Approval Status","approvalStatus", 100, true);
		vendorTypeColConfig = new ColumnConfig("Vendor Type","vendorType", 200, true);
		vendorStatusColConfig = new ColumnConfig("Vendor Status","vendorStatus", 100, true);
		
		

		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(subcontractorRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		resultEditorGridPanel =  new EditorGridPanel();
		resultEditorGridPanel.setStore(dataStore);
		BaseColumnConfig[] columns = new BaseColumnConfig[]{
				vendorNoColConfig,
				vendorNameColConfig,
				approvalStatusColConfig,
				vendorTypeColConfig,
				vendorStatusColConfig
		};
		resultEditorGridPanel.setColumnModel(new ColumnModel(columns));
		
		selectButton = new Button("Select");
		selectButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				openAddressBookWindow(rowInteger);
			};
		});
		
		closeWindowButton = new Button("Close");
		closeWindowButton.addListener(new ButtonListenerAdapter(){ 
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				workScopePanel.closeCurrentWindow();
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
				openAddressBookWindow(rowIndex);
			}
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				rowInteger = rowIndex;
			}
		});
		getSubcontractorList();
	}
	
	public void populateGrid(){
		try{
			dataStore.removeAll();
			for(MasterListVendor curObj: subcontractorList){
				if(curObj != null){
					String vendorType="";
					String vendorStatus="";
					if(null != curObj.getVendorType()){
						if(!"".equals(curObj.getVendorType().trim())){
							int type = Integer.parseInt(curObj.getVendorType().trim());
							switch (type){
							case 1: vendorType = "Supplier";break;
							case 2: vendorType = "Subcontractor";break;
							case 3: vendorType = "Both(Supplier & Subcontractor)";break;
							default: vendorType = "";
							}
						}
					}
					if(null != curObj.getVendorStatus()){
						if(!"".equals(curObj.getVendorStatus().trim())){
							int status = Integer.parseInt(curObj.getVendorStatus().trim());
							switch (status){
							case 1: vendorStatus = "Performance being observed";break;
							case 2: vendorStatus = "Suspended";break;
							case 3: vendorStatus = "Blacklisted";break;
							case 4: vendorStatus = "Obsolete";break;
							case 5: vendorStatus = "On HSE League Table";break;
							case 6: vendorStatus = "Observed & On HSE League";break;
							case 7: vendorStatus = "Suspended & On HSE League";break;
							default: vendorStatus = "";
							}
						}
					}
					Record record = subcontractorRecordDef.createRecord(new Object[]{
							curObj.getVendorNo() == null ? "" : curObj.getVendorNo(),
							curObj.getVendorName() == null ? "" : curObj.getVendorName(),
							curObj.getApprovalStatus() == null ? "" : curObj.getApprovalStatus(),
							vendorType,
							vendorStatus
					});
//					i += 1;
//					UIUtil.alert("i = "+ i);
//					UIUtil.alert("curObj.getVendorNo() " + curObj.getVendorNo());
//					UIUtil.alert("curObj.getVendorName() " + curObj.getVendorName());
//					UIUtil.alert("curObj.getApprovalStatus() " + curObj.getApprovalStatus());
//					UIUtil.alert("vendorType " + vendorType);
//					UIUtil.alert("vendorStatus " + vendorStatus);
					dataStore.add(record);
				}
			}
		}catch(Exception e){
			UIUtil.alert(e);
		}
	}
	public void getSubcontractorList(){
		try {
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getMasterListRepository().obtainVendorListByWorkScopeWithUser(workScope,
					globalSectionController.getUser().getUsername(), new AsyncCallback<List<MasterListVendor>>(){

				public void onFailure(Throwable arg0) {
					UIUtil.alert(arg0);
					
				}

				public void onSuccess(List<MasterListVendor> result) {
					subcontractorList = result;
					populateGrid();
				}
			});
		} catch (Exception e) {
			UIUtil.alert(e);
		}
	}
	public void openAddressBookWindow(int rowIndex){
		globalMessageTicket.refresh();
    	UIUtil.maskPanelById(GlobalParameter.MAIN_PANEL_ID, GlobalParameter.LOADING_MSG, true);
   
    	Record curRecord = dataStore.getAt(rowIndex);
    	
    	String addressNumber = curRecord.getAsString("vendorNo");
    	SessionTimeoutCheck.renewSessionTimer();
    	globalSectionController.getMasterListRepository().searchVendorAddressDetails(addressNumber, new AsyncCallback<MasterListVendor>(){

			public void onFailure(Throwable e) {
				UIUtil.alert(e);
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());	
			}

			public void onSuccess(MasterListVendor masterListVendor) {
            	//((AddressBookDetailsWindow)currentWindow).
            	Window currentWindow = globalSectionController.getCurrentWindow();   
            	currentWindow = new AddressBookDetailsWindow(globalSectionController);
            	((AddressBookDetailsWindow)currentWindow).populateGrid(masterListVendor);
            	globalSectionController.setCurrentWindow(currentWindow);
            	currentWindow.show();
            	UIUtil.unmaskMainPanel();
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
