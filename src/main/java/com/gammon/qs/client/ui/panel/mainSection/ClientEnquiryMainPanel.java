
package com.gammon.qs.client.ui.panel.mainSection;


import com.gammon.qs.client.controller.DetailSectionController;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.panel.detailSection.subcontractorDetailPanel.SubcontractorEnquiryTabPanel;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.SubcontractorWrapper;
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
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.RowNumberingColumnConfig;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

/**
 * @author irischau
 *
 */
public class ClientEnquiryMainPanel extends Panel {
	private String mainSectionPanel_ID;
	private static final boolean SORTABLE = true;
	private static final String SEARCH = "search";
	private static final String EXPORT_EXCEL = "export";
	
	private GridPanel gridPanel;
	
	private ToolbarButton attachmentButton;
	private ToolbarButton excelToolbarButton;
	private Item exportExcelButton;
	
	//pagination toolbar
	private PaginationToolbar paginationToolbar;
	
	//Record names
	private static final String CLIENT_NO_RECORD_NAME = "clientNoRecordName";
	private static final String CLIENT_NAME_RECORD_NAME = "clientNameRecordName";
	private static final String BUSINESS_REG_NO_RECORD_NAME = "businessRegNoRecordName";
	
	// records
	private Store dataStore;
	private RecordDef recordDef = new RecordDef(
			new FieldDef[] {	
					new StringFieldDef(CLIENT_NO_RECORD_NAME),
					new StringFieldDef(CLIENT_NAME_RECORD_NAME),
					new StringFieldDef(BUSINESS_REG_NO_RECORD_NAME)
					});
	
	private TextField clientTextField;

	
	private GlobalMessageTicket globalMessageTicket;
	
	// UI controllers
	private GlobalSectionController globalSectionController;
	private DetailSectionController detailSectionController;
	
	public ClientEnquiryMainPanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		this.detailSectionController = globalSectionController.getDetailSectionController();
		globalMessageTicket = new GlobalMessageTicket();
		mainSectionPanel_ID = globalSectionController.getMainSectionController().getMainPanel().getId();
		
		setupUI();
	}

	private void setupUI() {
		setBorder(false);
		setFrame(false);
		setPaddings(0);	
		
		setLayout(new RowLayout());
		
		setupSearchPanel();
		setupGridPanel();
	}


	private void setupSearchPanel() {
		// search panel
		Panel searchPanel = new Panel();
		searchPanel.setFrame(true);
		searchPanel.setHeight(45);
		searchPanel.setLayout(new HorizontalLayout(10));
		searchPanel.setPaddings(0);
		
		FieldListener searchListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER){
					globalMessageTicket.refresh();
					validate(SEARCH);
				}
			}
		};		
		

		 
		Label clientNameLabel = new Label("Client No. / Name");
		clientNameLabel.setCtCls("table-cell");
		
		clientTextField = new TextField();
		clientTextField.setCtCls("table-cell");
		clientTextField.setWidth(150);
		clientTextField.addListener(searchListener);
		
		
		searchPanel.add(clientNameLabel);
		searchPanel.add(clientTextField);
		
		Label emptyLabel = new Label();
		emptyLabel.setWidth(50);
		searchPanel.add(emptyLabel);		
		
		//button panel
		Panel buttonPanel = new Panel();
		buttonPanel.setFrame(false);
		buttonPanel.setLayout(new HorizontalLayout(20));
		
		// search button
		Button searchButton = new Button();
		searchButton.setText("Search");
		searchButton.setIconCls("find-icon");
		
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				globalSectionController.getDetailSectionController().resetPanel();
				validate(SEARCH);				
			}
		});
		buttonPanel.add(searchButton);
		searchPanel.add(buttonPanel);
		
		
		add(searchPanel);
	}
	
	private void setupToolbar(){
		Toolbar toolbar = new Toolbar();
		
		/*---------------------------Excel-----------------------------*/
		exportExcelButton = new Item("Export");
		exportExcelButton.setIconCls("download-icon");
		exportExcelButton.addListener(new BaseItemListenerAdapter(){
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				validate(EXPORT_EXCEL);
			}
		});
		Menu excelMenu = new Menu();
		excelMenu.addItem(exportExcelButton);
		
		excelToolbarButton = new ToolbarButton("Excel");
		excelToolbarButton.setMenu(excelMenu);
		excelToolbarButton.setIconCls("excel-icon");
		/*---------------------------Excel-----------------------------*/
		
		
		/*------------------------Attachment---------------------------*/
		attachmentButton = new ToolbarButton("Attachment");
		attachmentButton.setIconCls("attachment-icon");
		attachmentButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				Record record = gridPanel.getSelectionModel().getSelected();
				if(record!=null)
					globalSectionController.showAttachmentWindow("Attachment_Vendor", record.getAsString(CLIENT_NO_RECORD_NAME));
				else
					MessageBox.alert("Please select a row to view the attachment.");
			}
		});
		/*-----------------------Attachment---------------------------*/
		
		toolbar.addButton(excelToolbarButton);
		toolbar.addSeparator();
		toolbar.addButton(attachmentButton);
		toolbar.addSeparator();
		gridPanel.setTopToolbar(toolbar);
	}
	
	
	private void setupGridPanel() {
		gridPanel = new GridPanel();
		gridPanel.setBorder(false);
		gridPanel.setFrame(false);
		gridPanel.setPaddings(0);
		gridPanel.setAutoScroll(true);
		gridPanel.setView(new CustomizedGridView());
		
		BaseColumnConfig[] columns;

		setupToolbar();
		
		//Column headers	
		ColumnConfig clientNoColConfig = new ColumnConfig("Client No.",CLIENT_NO_RECORD_NAME,100,SORTABLE);
		ColumnConfig subconNameColConfig = new ColumnConfig("Client Name", CLIENT_NAME_RECORD_NAME, 280, SORTABLE);
		ColumnConfig businessRegNoColConfig = new ColumnConfig("Business Registration No.", BUSINESS_REG_NO_RECORD_NAME, 145, SORTABLE);
		
		columns = new BaseColumnConfig[]{ 
				new RowNumberingColumnConfig(),
				clientNoColConfig, 
				subconNameColConfig,
				businessRegNoColConfig
		};
		
		gridPanel.setColumnModel(new ColumnModel(columns));
		
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(recordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		gridPanel.setStore(dataStore);
		
		RowSelectionModel rowSelectionModel = new RowSelectionModel();
		gridPanel.setSelectionModel(rowSelectionModel);

        gridPanel.addGridRowListener(new GridRowListenerAdapter(){
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				globalMessageTicket.refresh();
				Record curRecord = dataStore.getAt(rowIndex);
				String clientNo = curRecord.getAsString(CLIENT_NO_RECORD_NAME);
				populateDetailSectionPanel(clientNo);
			}
        });
        
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(int pageNum){
				obtainClientWrapperByPage(pageNum);
			}
		});
		gridPanel.setBottomToolbar(paginationToolbar);
		paginationToolbar.setTotalPage(1);
		paginationToolbar.setCurrentPage(0);
		
		add(gridPanel);		
	}

	private void obtainClientWrapperByPage(int pageNum){
		UIUtil.maskPanelById(mainSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getSubcontractorRepository().obtainSubcontractorWrapperListByPage(pageNum, new AsyncCallback<PaginationWrapper<SubcontractorWrapper>>(){
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
			public void onSuccess(PaginationWrapper<SubcontractorWrapper> wrappers) {
				populateGrid(wrappers);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
		});
	}
	
	private void validate(String method){
		String client = clientTextField.getValueAsString();
//		String workScope = workScopeComboBox.getText().trim();
		
		if(client.length()>0){
			try {
				Integer.parseInt(client.trim());
			} catch (NumberFormatException e) {
				client = ("*".concat(client)).concat("*"); // input String is a description but not a number	
			}
		}else{
			MessageBox.alert("Please input client to search.");
			return;
		}
		
		boolean asteriskSearch = checkAsteriskSearch(client);
		if(asteriskSearch){
			MessageBox.alert("Client cannot be searched with '*' only.");
			return;
		}



		if(SEARCH.equals(method))
			search(client);
		else if(EXPORT_EXCEL.equals(method))
			com.google.gwt.user.client.Window.open(GlobalParameter.SUBCONTRACTOR_EXCEL_EXPORT_URL+"?subcontractor="+client+"&workScope=&type=ClientEnquiry", "_blank", "");
	}
	
	public void search(String client) {
		UIUtil.maskPanelById(mainSectionPanel_ID, GlobalParameter.SEARCHING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getSubcontractorRepository().obtainClientPaginationWrapper(client, new AsyncCallback<PaginationWrapper<SubcontractorWrapper>>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
			public void onSuccess(PaginationWrapper<SubcontractorWrapper> clientWrappers) {
				detailSectionController.resetPanel();
				populateGrid(clientWrappers);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
		});
	}

	private void populateGrid(PaginationWrapper<SubcontractorWrapper> clientWrappers) {
		dataStore.removeAll();
	
		if(clientWrappers==null || clientWrappers.getCurrentPageContentList().size()==0){
			MessageBox.alert("No data found");
			return;
		}

		paginationToolbar.setTotalPage(clientWrappers.getTotalPage());
		paginationToolbar.setCurrentPage(clientWrappers.getCurrentPage());

		for(SubcontractorWrapper clientWrapper: clientWrappers.getCurrentPageContentList()){
			Record record = recordDef.createRecord(new Object[]{
					clientWrapper.getSubcontractorNo(),
					clientWrapper.getSubcontractorName(),
					clientWrapper.getBusinessRegistrationNo()
			});
			dataStore.add(record);
		}
	}
	
	private void populateDetailSectionPanel(String clientNo) {
		detailSectionController.resetPanel();
		SubcontractorEnquiryTabPanel subconTabPanel = new SubcontractorEnquiryTabPanel(globalSectionController,clientNo, "ClientEnquiry");
		detailSectionController.setPanel(subconTabPanel);
		detailSectionController.populatePanel();
	}
	

	private boolean checkAsteriskSearch(String client){
		boolean asteriskSearch = true;
		if(client==null || "".equals(client))
			asteriskSearch=false;
		else{
			for(int i =0; i<client.length(); i++){
				if('*'!=client.charAt(i)){
					asteriskSearch = false;
				}
			}
		}
		return asteriskSearch;
	}

	public TextField getSubcontractorTextField() {
		return clientTextField;
	}
	
}
