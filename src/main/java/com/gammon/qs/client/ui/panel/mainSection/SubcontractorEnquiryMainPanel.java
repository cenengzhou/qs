
package com.gammon.qs.client.ui.panel.mainSection;

import java.util.List;

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
import com.gammon.qs.wrapper.UDC;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
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
 * @author koeyyeung
 *
 */
public class SubcontractorEnquiryMainPanel extends Panel {
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
	private static final String SUBCONTRACTOR_NO_RECORD_NAME = "subcontractorNoRecordName";
	private static final String SUBCONTRACTOR_NAME_RECORD_NAME = "subcontractorNameRecordName";
	private static final String BUSINESS_REG_NO_RECORD_NAME = "businessRegNoRecordName";
	private static final String VENDOR_TYPE_RECORD_NAME = "vendorTypeRecordName";
	private static final String VENDOR_STATUS_RECORD_NAME = "workStatusRecordName";
	private static final String SUBCONTRACTOR_APPROVAL_RECORD_NAME = "subcontractorApprovalRecordName";
	private static final String HOLD_PAYMENT_RECORD_NAME = "holdPaymentRecordName";
	private static final String SC_FINANCIAL_ALERT_RECORD_NAME = "scFinancialAlertRecordName";
	
	// records
	private Store dataStore;
	private RecordDef recordDef = new RecordDef(
			new FieldDef[] {	
					new StringFieldDef(SUBCONTRACTOR_NO_RECORD_NAME),
					new StringFieldDef(SUBCONTRACTOR_NAME_RECORD_NAME),
					new StringFieldDef(BUSINESS_REG_NO_RECORD_NAME),
					new StringFieldDef(VENDOR_TYPE_RECORD_NAME),
					new StringFieldDef(VENDOR_STATUS_RECORD_NAME),
					new StringFieldDef(SUBCONTRACTOR_APPROVAL_RECORD_NAME),
					new StringFieldDef(HOLD_PAYMENT_RECORD_NAME),
					new StringFieldDef(SC_FINANCIAL_ALERT_RECORD_NAME)
					});
	
	private TextField subcontractorTextField;
	
	private static final String WORK_SCOPE_NAME = "Work Scope (Trade)";
	private static final String WORK_SCOPE_VALUE = "workScopeValue";
	private static final String WORK_SCOPE_DISPLAY = "workScopeDisplay";
	private ComboBox workScopeComboBox = new ComboBox();
	private String[][] workScopes = new String[][] {};
	private Store workScopeStore = new SimpleStore(new String[] {WORK_SCOPE_VALUE, WORK_SCOPE_DISPLAY }, workScopes);
	
	private GlobalMessageTicket globalMessageTicket;
	
	// UI controllers
	private GlobalSectionController globalSectionController;
	private DetailSectionController detailSectionController;
	
	public SubcontractorEnquiryMainPanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		this.detailSectionController = globalSectionController.getDetailSectionController();
		globalMessageTicket = new GlobalMessageTicket();
		mainSectionPanel_ID = globalSectionController.getMainSectionController().getMainPanel().getId();
		
		obtainWorkScopeStore();
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

		
		Label subcontractorNameLabel = new Label("Subcontractor No. / Name");
		subcontractorNameLabel.setCtCls("table-cell");
		
		subcontractorTextField = new TextField();
		subcontractorTextField.setCtCls("table-cell");
		subcontractorTextField.setWidth(150);
		subcontractorTextField.addListener(searchListener);
		
		searchPanel.add(subcontractorNameLabel);
		searchPanel.add(subcontractorTextField);
		
		
		Label workScopeLabel = new Label(WORK_SCOPE_NAME);
		workScopeLabel.setCls("table-cell");
		
		workScopeComboBox.setCtCls("table-cell");
		workScopeComboBox.setForceSelection(true);
		workScopeComboBox.setDisplayField(WORK_SCOPE_DISPLAY);
		workScopeComboBox.setValueField(WORK_SCOPE_VALUE);
		workScopeComboBox.setStore(workScopeStore);
		workScopeComboBox.setWidth(250);
		workScopeComboBox.setEmptyText("ALL");
		workScopeComboBox.setSelectOnFocus(true);
		workScopeComboBox.setTypeAhead(true);
		workScopeComboBox.addListener(searchListener);
		
		searchPanel.add(workScopeLabel);
		searchPanel.add(workScopeComboBox);
		
		Label emptyLabel = new Label();
		emptyLabel.setWidth(50);
		searchPanel.add(emptyLabel);
		
		//button panel
		Panel buttonPanel = new Panel();
		buttonPanel.setFrame(false);
		buttonPanel.setLayout(new HorizontalLayout(20));
		
		// search button
		Button searchButton = new Button();
		searchButton.setText(" Search");
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
					globalSectionController.showAttachmentWindow("Attachment_Vendor", record.getAsString(SUBCONTRACTOR_NO_RECORD_NAME));
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
		ColumnConfig subcontractorNoColConfig = new ColumnConfig("Subcontractor No.",SUBCONTRACTOR_NO_RECORD_NAME,100,SORTABLE);
		ColumnConfig subconNameColConfig = new ColumnConfig("Subcontractor Name", SUBCONTRACTOR_NAME_RECORD_NAME, 280, SORTABLE);
		ColumnConfig businessRegNoColConfig = new ColumnConfig("Business Registration No.", BUSINESS_REG_NO_RECORD_NAME, 90, SORTABLE);
		ColumnConfig vendortypColConfig = new ColumnConfig("Vendor Type", VENDOR_TYPE_RECORD_NAME, 185,SORTABLE);
		ColumnConfig vendorStatusColConfig = new ColumnConfig("Vendor Status", VENDOR_STATUS_RECORD_NAME, 150,SORTABLE);
		ColumnConfig subcontractorApprovalColConfig = new ColumnConfig("Approved Subcontractor", SUBCONTRACTOR_APPROVAL_RECORD_NAME, 130,SORTABLE);
		ColumnConfig holdPaymentColConfig = new ColumnConfig("Payment on Hold", HOLD_PAYMENT_RECORD_NAME, 100,SORTABLE);
		ColumnConfig scFinancialAlertColumnConfig = new ColumnConfig("on Hold by Finance", SC_FINANCIAL_ALERT_RECORD_NAME, 100, SORTABLE);
		ColumnConfig recommendedColConfig = new ColumnConfig("Recommended", "" ,90,true);
		recommendedColConfig.setRenderer(new Renderer() {
			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				if (record.getAsString(SUBCONTRACTOR_APPROVAL_RECORD_NAME).contains("Yes") && 
						record.getAsString(HOLD_PAYMENT_RECORD_NAME).contains("No") && 
						record.getAsString(SC_FINANCIAL_ALERT_RECORD_NAME).contains("No"))
						return "<img src=\"" + GlobalParameter.BASED_URL + "images/good.png" + "\"/>";
					else if (	record.getAsString(SUBCONTRACTOR_APPROVAL_RECORD_NAME).contains("No") && 
								record.getAsString(HOLD_PAYMENT_RECORD_NAME).contains("Yes"))
						return "<img src=\"" + GlobalParameter.BASED_URL + "images/bad.png" + "\"/>";
				else
					return "";
			}  
		});  
		
		columns = new BaseColumnConfig[]{ 
				new RowNumberingColumnConfig(),
				subcontractorNoColConfig, 
				subconNameColConfig,
				businessRegNoColConfig,
				vendortypColConfig,
				vendorStatusColConfig,
				subcontractorApprovalColConfig,
				holdPaymentColConfig,
				scFinancialAlertColumnConfig,
				recommendedColConfig
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
				String subcontractorNo = curRecord.getAsString(SUBCONTRACTOR_NO_RECORD_NAME);
				populateDetailSectionPanel(subcontractorNo);
			}
        });
        
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(int pageNum){
				obtainSubcontractorWrapperByPage(pageNum);
			}
		});
		gridPanel.setBottomToolbar(paginationToolbar);
		paginationToolbar.setTotalPage(1);
		paginationToolbar.setCurrentPage(0);
		
		add(gridPanel);		
	}

	private void obtainSubcontractorWrapperByPage(int pageNum){
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
		String subcontractor = subcontractorTextField.getValueAsString();
		String workScope = workScopeComboBox.getText().trim();
		
		if(subcontractor.length()>0){
			try {
				Integer.parseInt(subcontractor.trim());
			} catch (NumberFormatException e) {
				subcontractor = ("*".concat(subcontractor)).concat("*"); // input String is a description but not a number	
			}
		}
		
		boolean asteriskSearch = checkAsteriskSearch(subcontractor);
		if(asteriskSearch){
			MessageBox.alert("Subcontractor cannot be searched with '*' only.");
			return;
		}

		if((workScope==null || "".equals(workScope)) && ((subcontractor==null ||"".equals(subcontractor)))){
			MessageBox.alert("Please input work scope or subcontractor to search.");
			return;
		}

		if(SEARCH.equals(method))
			search(subcontractor, workScope);
		else if(EXPORT_EXCEL.equals(method))
			com.google.gwt.user.client.Window.open(GlobalParameter.SUBCONTRACTOR_EXCEL_EXPORT_URL+"?subcontractor="+subcontractor+"&workScope="+workScope+"&type=SubcontractorEnquiry", "_blank", "");
	}
	
	public void search(String subcontractor, String workScope) {
		UIUtil.maskPanelById(mainSectionPanel_ID, GlobalParameter.SEARCHING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getSubcontractorRepository().obtainSubcontractorPaginationWrapper(workScope, subcontractor, new AsyncCallback<PaginationWrapper<SubcontractorWrapper>>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
			public void onSuccess(PaginationWrapper<SubcontractorWrapper> subcontractorWrappers) {
				detailSectionController.resetPanel();
				populateGrid(subcontractorWrappers);
				UIUtil.unmaskPanelById(mainSectionPanel_ID);
			}
		});
	}

	private void populateGrid(PaginationWrapper<SubcontractorWrapper> subcontractorWrappers) {
		dataStore.removeAll();
	
		if(subcontractorWrappers==null || subcontractorWrappers.getCurrentPageContentList().size()==0){
			MessageBox.alert("No data found");
			return;
		}

		paginationToolbar.setTotalPage(subcontractorWrappers.getTotalPage());
		paginationToolbar.setCurrentPage(subcontractorWrappers.getCurrentPage());

		for(SubcontractorWrapper subcontractorWrapper: subcontractorWrappers.getCurrentPageContentList()){
			Record record = recordDef.createRecord(new Object[]{
					subcontractorWrapper.getSubcontractorNo(),
					subcontractorWrapper.getSubcontractorName(),
					subcontractorWrapper.getBusinessRegistrationNo(),
					SubcontractorWrapper.convertVendorType((subcontractorWrapper.getVendorType()==null)?"":subcontractorWrapper.getVendorType().trim()),
					SubcontractorWrapper.convertVendorStatus((subcontractorWrapper.getVendorStatus()==null)?"":subcontractorWrapper.getVendorStatus().trim()),
					convertSubcontractorApproval(subcontractorWrapper.getSubcontractorApproval()),
					convertHoldPayment(subcontractorWrapper.getHoldPayment()),
					SubcontractorWrapper.convertSCFinancialAlertStatus(subcontractorWrapper.getScFinancialAlert())
			});
			dataStore.add(record);
		}
	}
	
	private void populateDetailSectionPanel(String subcontractorNo) {
		detailSectionController.resetPanel();
		SubcontractorEnquiryTabPanel subconTabPanel = new SubcontractorEnquiryTabPanel(globalSectionController,subcontractorNo, "SubcontractorEnquiry");
		detailSectionController.setPanel(subconTabPanel);
		detailSectionController.populatePanel();
	}
	
	private void obtainWorkScopeStore() {
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getUnitRepository().getAllWorkScopes(new AsyncCallback<List<UDC>>() {
			public void onSuccess(List<UDC> workScopeList) {
				workScopeStore.removeAll();
				
				if(workScopeList == null) return;
				
				RecordDef jobRecordDef = new RecordDef(new FieldDef[] { new StringFieldDef(WORK_SCOPE_VALUE), 
						  												new StringFieldDef(WORK_SCOPE_DISPLAY)});
				workScopes = new String[workScopeList.size()+1][2];
				workScopes[0][0]=null;
				workScopes[0][1]="ALL";
				workScopeStore.add(jobRecordDef.createRecord(workScopes[0]));
				
				for (int i = 0; i < workScopeList.size(); i++) {
					workScopes[i+1][0] = (String)workScopeList.get(i).getCode();
					workScopes[i+1][1] = (String)workScopeList.get(i).getCode()+" - "+(String)workScopeList.get(i).getDescription();
					workScopeStore.add(jobRecordDef.createRecord(workScopes[i+1]));
				}	

				workScopeComboBox.setListWidth(300);
				workScopeStore.commitChanges();
				
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}
		});
	}

	private boolean checkAsteriskSearch(String subcontractor){
		boolean asteriskSearch = true;
		if(subcontractor==null || "".equals(subcontractor))
			asteriskSearch=false;
		else{
			for(int i =0; i<subcontractor.length(); i++){
				if('*'!=subcontractor.charAt(i)){
					asteriskSearch = false;
				}
			}
		}
		return asteriskSearch;
	}
	
	private String convertSubcontractorApproval(String subcontractorApproval){
		if("Y".equals(subcontractorApproval==null?"":subcontractorApproval.trim()))
			subcontractorApproval = "Yes";
		else if ("N".equals(subcontractorApproval==null?"":subcontractorApproval.trim()))
			subcontractorApproval = "No";
		else
			subcontractorApproval = "-"; //N/A

		if("No".equals(subcontractorApproval)){
			return "<font color="+GlobalParameter.RED_COLOR+">"+ subcontractorApproval+"</font>";
		}
		return subcontractorApproval;
	
	}
	
	private String convertHoldPayment(String holdPayment){
		if("Y".equals(holdPayment==null?"":holdPayment.trim()))
			holdPayment = "Yes";
		else if("N".equals(holdPayment==null?"":holdPayment.trim()))
			holdPayment = "No";
		else
			holdPayment = "-"; //N/A
		
		if("Yes".equals(holdPayment))
			return "<font color="+GlobalParameter.RED_COLOR+">"+ holdPayment+"</font>";
		return holdPayment;
	}

	public TextField getSubcontractorTextField() {
		return subcontractorTextField;
	}

	public ComboBox getWorkScopeComboBox() {
		return workScopeComboBox;
	}
	
}
