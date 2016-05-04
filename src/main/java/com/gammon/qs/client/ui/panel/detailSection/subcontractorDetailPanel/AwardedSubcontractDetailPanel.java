package com.gammon.qs.client.ui.panel.detailSection.subcontractorDetailPanel;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
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
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.CheckItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtext.client.widgets.menu.event.CheckItemListenerAdapter;


/**
 * @author koeyyeung
 * 
 * modified by irischau
 * on 14 Apr 2014
 * able to filter the result
 */
public class AwardedSubcontractDetailPanel extends GridPanel{
	//pagination toolbar
	private PaginationToolbar paginationToolbar;
	private ToolbarButton excelToolbarButton;
	private Item exportExcelButton;
	
	private ToolbarTextItem jobFilterLabel;
	private ToolbarTextItem divisionFilterLabel;
	private ToolbarTextItem packageFilterLabel;
	private ToolbarTextItem paymentTermFilterLabel;
	private ToolbarTextItem paymentStatusFilterLabel;
	
	private TextField jobFilterField;
	private TextField divisionFilterField;
	private TextField packageFilterField;
	
	private ToolbarButton paymentTypeButton = new ToolbarButton("ALL");		
	private CheckItem paymentTermCheckItem;
	private ToolbarButton paymentTermButton = new ToolbarButton("ALL");	
	private CheckItem paymentTypeCheckItem;
	
	private Toolbar toolbar = new Toolbar();
	
	private Store dataStore;
	//Record names
	private static final String JOB_NO_RECORD_NAME = "jobNoRecordName";
	private static final String JOB_DESCRIPTION_RECORD_NAME = "jobDescriptionRecordName";
	private static final String JOB_DIVISION_RECORD_NAME = "jobDivisionRecordName";
	private static final String SUBCONTRACTOR_NO_RECORD_NAME = "subcontractorNoRecordName";
	private static final String SUBCONTRACTOR_DESCRIPTION_RECORD_NAME = "subcontractorDescriptionRecordName";
	private static final String REMEASURED_SC_SUM_RECORD_NAME = "remeasuredSCSumRecordName";
	private static final String APPROVED_VO_AMOUNT_RECORD_NAME = "approvedVOAmountRecordName";
	private static final String REVISED_SUBCON_SUM_RECORD_NAME = "revisedSubconSumRecordName";
	private static final String BALANCE_TO_COMPELTE_RECORD_NAME = "balanceToCompleteRecordName";
	private static final String PAYMENT_TERMS_RECORD_NAME = "paymentTermsRecordName";
	private static final String PAYMENT_STATUS_RECORD_NAME = "paymentStatusRecordName";
	// records
	private final RecordDef recordDef = new RecordDef(
			new FieldDef[] {	
					new StringFieldDef(JOB_NO_RECORD_NAME),
					new StringFieldDef(JOB_DESCRIPTION_RECORD_NAME),
					new StringFieldDef(JOB_DIVISION_RECORD_NAME),
					new StringFieldDef(SUBCONTRACTOR_NO_RECORD_NAME),
					new StringFieldDef(SUBCONTRACTOR_DESCRIPTION_RECORD_NAME),
					new StringFieldDef(REMEASURED_SC_SUM_RECORD_NAME),
					new StringFieldDef(APPROVED_VO_AMOUNT_RECORD_NAME),
					new StringFieldDef(REVISED_SUBCON_SUM_RECORD_NAME),
					new StringFieldDef(BALANCE_TO_COMPELTE_RECORD_NAME),
					new StringFieldDef(PAYMENT_TERMS_RECORD_NAME),
					new StringFieldDef(PAYMENT_STATUS_RECORD_NAME),
					});
	
	private GlobalMessageTicket globalMessageTicket;
	private GlobalSectionController globalSectionController;
	private String detailSectionPanel_ID;
	private String subcontractorNo;
	
	private String division = null;
	private String jobNumber = null;
	private String packageNumber = null;
	private String paymentTerm = null;
	private String paymentType = null;
	private String [] paymentTypeItem = {"Final", "Interim"};
	
	

	public AwardedSubcontractDetailPanel(GlobalSectionController globalSectionController, String subcontractorNo) {
		super();
		this.globalSectionController = globalSectionController;
		detailSectionPanel_ID = globalSectionController.getDetailSectionController().getMainPanel().getId();
		this.subcontractorNo = subcontractorNo;
		globalMessageTicket = new GlobalMessageTicket();
		
		search(subcontractorNo, null,null,null,null,null);
		setupToolbar();
		setupGrid();
	}

	private void setupToolbar(){
		
		jobFilterLabel = new ToolbarTextItem("Job No.: ");
		jobFilterField = new TextField();
		
		divisionFilterLabel = new ToolbarTextItem("Division: ");
		divisionFilterField = new TextField();
		
		packageFilterLabel = new ToolbarTextItem("Package No.: ");
		packageFilterField = new TextField();
		
		paymentTermFilterLabel = new ToolbarTextItem("Payment Terms: ");
		
		paymentStatusFilterLabel = new ToolbarTextItem("Payment Type: ");
		
		FieldListener searchListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER){
					String jobNumber = jobFilterField.getText() != null && jobFilterField.getText().trim().length() > 0 ? jobFilterField.getText() : null;
					String division = divisionFilterField.getText() != null && divisionFilterField.getText().trim().length() > 0 ? divisionFilterField.getText().toUpperCase() : null;
					String packageNumber = packageFilterField.getText() != null && packageFilterField.getText().trim().length() > 0 ? packageFilterField.getText() : null;
					String paymentTerm = paymentTermButton.getText().equals("ALL") ? null : paymentTermButton.getText();
					String paymentType = paymentTypeButton.getText().equals("ALL") ? null : paymentTypeButton.getText().equals("Final") ? "F" : "I";

					search(subcontractorNo, division, jobNumber, packageNumber, paymentTerm, paymentType);
				}
			}
		};

		
		jobFilterField.addListener(searchListener);
		divisionFilterField.addListener(searchListener);
		packageFilterField.addListener(searchListener);
		
		Menu paymentTermMenu = new Menu();
		paymentTermMenu.setWidth("200");
		
		Menu paymentTypeMenu = new Menu();
		paymentTypeMenu.setWidth("200");


		CheckItemListenerAdapter paymentTypeListener = new CheckItemListenerAdapter() {
			public void onCheckChange(CheckItem item, boolean checked) {
				if (item.getText() != null && item.getText().length() > 0) {
					paymentTypeButton.setText(item.getText());
				}

			}
		};

		CheckItemListenerAdapter paymentTermListener = new CheckItemListenerAdapter() {
			public void onCheckChange(CheckItem item, boolean checked) {
				if (item.getText() != null && item.getText().length() > 0) {
					if (paymentTermButton != null && item != null) {
						paymentTermButton.setText(item.getText());
					}
				}
			}
		};

        CheckItem paymentTermCheckItemAll = new CheckItem("ALL");
        paymentTermCheckItemAll.setGroup("paymentTerm");
        paymentTermCheckItemAll.addListener(paymentTermListener);
//        paymentTermCheckItemAll.setChecked(true);
        paymentTermMenu.addItem(paymentTermCheckItemAll);
        
		for (int i = 0; i < 8; i++) {
			paymentTermCheckItem = new CheckItem("QS" + i);
			paymentTermCheckItem.setGroup("paymentTerm");
			paymentTermCheckItem.addListener(paymentTermListener);
			paymentTermMenu.addItem(paymentTermCheckItem);
		}
		paymentTermButton.setMenu(paymentTermMenu);
		
		CheckItem paymentTypeCheckItemALL = new CheckItem("ALL");
		paymentTypeCheckItemALL.setGroup("paymentType");
//		paymentTypeCheckItemALL.setChecked(true);
		paymentTypeMenu.addItem(paymentTypeCheckItemALL);
		paymentTypeCheckItemALL.addListener(paymentTypeListener);
		
        
		for (String paymentType : paymentTypeItem) {
			paymentTypeCheckItem = new CheckItem(paymentType);
			paymentTypeCheckItem.setGroup("paymentType");
			paymentTypeMenu.addItem(paymentTypeCheckItem);
			paymentTypeCheckItem.addListener(paymentTypeListener);
		}
		paymentTypeButton.setMenu(paymentTypeMenu);

		
		//Filter's Button
		ToolbarButton filterButton = new ToolbarButton();
		filterButton.setIconCls("filter-icon");
		filterButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				String jobNumber = jobFilterField.getText() != null && jobFilterField.getText().trim().length() > 0 ? jobFilterField.getText() : null;
				String division = divisionFilterField.getText() != null && divisionFilterField.getText().trim().length() > 0 ? divisionFilterField.getText().toUpperCase() : null;
				String packageNumber = packageFilterField.getText() != null && packageFilterField.getText().trim().length() > 0 ? packageFilterField.getText() : null;
				String paymentTerm = paymentTermButton.getText().equals("ALL") ? null : paymentTermButton.getText();
				String paymentType = paymentTypeButton.getText().equals("ALL") ? null : paymentTypeButton.getText().equals("Final") ? "F" : "I";

				search(subcontractorNo, division, jobNumber, packageNumber, paymentTerm, paymentType);
			}
		});
		
		
		/*---------------------------Excel-----------------------------*/
		exportExcelButton = new Item("Export");
		exportExcelButton.setIconCls("download-icon");
		exportExcelButton.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();

				jobNumber = jobFilterField.getText() != null && jobFilterField.getText().trim().length() > 0 ? jobFilterField.getText() : "";
				division = divisionFilterField.getText() != null && divisionFilterField.getText().trim().length() > 0 ? divisionFilterField.getText().toUpperCase() : "";
				packageNumber = packageFilterField.getText() != null && packageFilterField.getText().trim().length() > 0 ? packageFilterField.getText() : "";
				paymentTerm = paymentTermButton.getText().equals("ALL") ? "" : paymentTermButton.getText();
				paymentType = paymentTypeButton.getText().equals("ALL") ? "" : paymentTypeButton.getText().equals("Final") ? "F" : "I";

				com.google.gwt.user.client.Window.open(GlobalParameter.AWARDED_SUBCONTRACTS_EXCEL_EXPORT_URL + "?subcontractorNo=" + subcontractorNo + "&division=" + division + "&jobNumber=" + jobNumber + "&packageNumber=" + packageNumber + "&paymentTerm=" + paymentTerm + "&paymentType=" + paymentType, "_blank", "");
			}
		});

		Menu excelMenu = new Menu();
		excelMenu.addItem(exportExcelButton);
		
		excelToolbarButton = new ToolbarButton("Excel");
		excelToolbarButton.setMenu(excelMenu);
		excelToolbarButton.setIconCls("excel-icon");
		/*---------------------------Excel-----------------------------*/
		
		//Toolbar - Filter Button's Tips
		ToolTip filterButtonToolTip = new ToolTip();
		filterButtonToolTip.setTitle("Filter");
		filterButtonToolTip.setHtml("Filter by Line Type");
		filterButtonToolTip.applyTo(filterButton);
		
		ToolbarButton refreshButton = new ToolbarButton();
		refreshButton.setText("Refresh");
		refreshButton.setIconCls("toggle-button-icon");
		refreshButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();

			}
		});
	 

		ToolTip refreshButtonTip = new ToolTip();
		refreshButtonTip.setTitle("Refresh");
		refreshButtonTip.setHtml("Refresh Current Page");
		refreshButtonTip.setCtCls("toolbar-button");
		refreshButtonTip.setDismissDelay(15000);
		refreshButtonTip.setWidth(300);
		refreshButtonTip.setTrackMouse(true);
		refreshButtonTip.applyTo(refreshButton);
		
		toolbar.addItem(jobFilterLabel);
		toolbar.addField(jobFilterField);
		toolbar.addSeparator();
		toolbar.addItem(divisionFilterLabel);
		toolbar.addField(divisionFilterField);
		toolbar.addSeparator();
		toolbar.addItem(packageFilterLabel);
		toolbar.addField(packageFilterField);
		toolbar.addSeparator();
		toolbar.addItem(paymentTermFilterLabel);
		toolbar.addButton(paymentTermButton);
		toolbar.addSeparator();
		toolbar.addItem(paymentStatusFilterLabel);
	    toolbar.addButton(paymentTypeButton);
		toolbar.addSeparator();
		toolbar.addButton(filterButton);
		toolbar.addSeparator();
		toolbar.addButton(excelToolbarButton);
		toolbar.addSeparator();
		toolbar.addFill();
		toolbar.addButton(refreshButton);
		setTopToolbar(toolbar);
	}
	
	private void setupGrid() {
		setTitle("Awarded Subcontracts");
		setIconCls("award-icon");
		setBorder(false);
		setFrame(false);
		setPaddings(0);
		setAutoScroll(true);
		setView(new CustomizedGridView());
		
		
		
		Renderer colorRenderer = new Renderer() {
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				if(value!=null){
					if("QS0".equals(value)){
						return "<font color="+GlobalParameter.ORANGE_COLOR+">"+ value.toString()+"</font>";
					}else if ("Final".equals(value)){
						return "<font color="+GlobalParameter.GREY_COLOR+">"+ value.toString()+"</font>";
					}else return value.toString();
				}
				else return "";
			}
		};
		
		Renderer amountRenderer = new AmountRenderer(globalSectionController.getUser()){
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				if(value == null) 
					return String.valueOf(0.0);
				return render(value.toString());
			}
		};
		
		//Column headers	
		ColumnConfig jobNoColConfig = new ColumnConfig("Job No.",JOB_NO_RECORD_NAME,60,true);
		ColumnConfig jobDescriptionColConfig = new ColumnConfig("Job Description", JOB_DESCRIPTION_RECORD_NAME, 180, true);
		ColumnConfig jobDivisionColConfig = new ColumnConfig("Job Division", JOB_DIVISION_RECORD_NAME, 80, true);
		ColumnConfig subconNoColConfig = new ColumnConfig("Package No.", SUBCONTRACTOR_NO_RECORD_NAME, 65,true);
		ColumnConfig subconDescriptionColConfig = new ColumnConfig("Package Description", SUBCONTRACTOR_DESCRIPTION_RECORD_NAME ,180,true);
		
		ColumnConfig remeasuredSCSumColConfig = new ColumnConfig("Remeasured SC Sum", REMEASURED_SC_SUM_RECORD_NAME, 110,true);
		remeasuredSCSumColConfig.setAlign(TextAlign.RIGHT);
		remeasuredSCSumColConfig.setRenderer(amountRenderer);
		
		ColumnConfig approvedVOAmountColConfig = new ColumnConfig("Approved VO Amount", APPROVED_VO_AMOUNT_RECORD_NAME, 110,true);
		approvedVOAmountColConfig.setAlign(TextAlign.RIGHT);
		approvedVOAmountColConfig.setRenderer(amountRenderer);
		
		ColumnConfig revisedSCSumColConfig = new ColumnConfig("Revised SC Sum (Remeasured SC Sum + Approved VO Amount)", REVISED_SUBCON_SUM_RECORD_NAME, 120,true);
		revisedSCSumColConfig.setAlign(TextAlign.RIGHT);
		revisedSCSumColConfig.setRenderer(amountRenderer);
		
		ColumnConfig balanceToCompleteColConfig = new ColumnConfig("Balance To Complete (Revised SC Sum - Total CumWork Done Amount)", BALANCE_TO_COMPELTE_RECORD_NAME, 110,true);
		balanceToCompleteColConfig.setAlign(TextAlign.RIGHT);
		balanceToCompleteColConfig.setRenderer(amountRenderer);
		
		ColumnConfig paymentTermsColConfig = new ColumnConfig("Payment Terms", PAYMENT_TERMS_RECORD_NAME, 85,true);
		paymentTermsColConfig.setRenderer(colorRenderer);
		ColumnConfig paymentStatusColConfig = new ColumnConfig("Payment Type", PAYMENT_STATUS_RECORD_NAME, 85,true);
		paymentStatusColConfig.setRenderer(colorRenderer);
		
		BaseColumnConfig[] columns;
		columns = new BaseColumnConfig[]{
				new RowNumberingColumnConfig(),
				jobNoColConfig, 
				jobDescriptionColConfig,
				jobDivisionColConfig,
				subconNoColConfig,
				subconDescriptionColConfig,
				paymentTermsColConfig,
				paymentStatusColConfig,
				remeasuredSCSumColConfig,
				approvedVOAmountColConfig,
				revisedSCSumColConfig,
				balanceToCompleteColConfig,
		};
		
		setColumnModel(new ColumnModel(columns));
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(recordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		setStore(dataStore);
		
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(int pageNum){
				obtainSCPackagesByPage(pageNum);
			}
		});
		setBottomToolbar(paginationToolbar);
		paginationToolbar.setTotalPage(1);
		paginationToolbar.setCurrentPage(0);
	}

	private void search(String subcontractorNo, String division, String jobNumber, String packageNumber, String paymentTerm, String paymentType) {

		UIUtil.maskPanelById(detailSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getSubcontractorRepository().obtainPackagesByVendorNoPaginationWrapper(subcontractorNo,division,jobNumber, packageNumber, paymentTerm, paymentType,   new AsyncCallback<PaginationWrapper<SCPackage>>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
			public void onSuccess(PaginationWrapper<SCPackage> wrappers) {
				populateGrid(wrappers);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
		});
	}
	

	private void obtainSCPackagesByPage(int pageNum){
		UIUtil.maskPanelById(detailSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getSubcontractorRepository().obtainPackagesByPage(pageNum, new AsyncCallback<PaginationWrapper<SCPackage>>(){
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
			public void onSuccess(PaginationWrapper<SCPackage> wrappers) {
				populateGrid(wrappers);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
		});
	}
	
	private void populateGrid(PaginationWrapper<SCPackage> wrappers) {
		dataStore.removeAll();
		if(wrappers==null ||wrappers.getCurrentPageContentList().size()==0)
			return;
		
		paginationToolbar.setTotalPage(wrappers.getTotalPage());
		paginationToolbar.setCurrentPage(wrappers.getCurrentPage());
		
		
		for(SCPackage scPackage: wrappers.getCurrentPageContentList()){
			
			double revisedSCSum = (scPackage.getRemeasuredSubcontractSum()==null?0.0:scPackage.getRemeasuredSubcontractSum())+(scPackage.getApprovedVOAmount()==null?0.0:scPackage.getApprovedVOAmount());
			Record record  = recordDef.createRecord(new Object[]{
					scPackage.getJob().getJobNumber(),
					scPackage.getJob().getDescription(),
					scPackage.getJob().getDivision(),
					scPackage.getPackageNo(),
					scPackage.getDescription(),
					scPackage.getRemeasuredSubcontractSum(),
					scPackage.getApprovedVOAmount(),
					revisedSCSum,
					revisedSCSum - (scPackage.getTotalCumWorkDoneAmount()==null?0.0:scPackage.getTotalCumWorkDoneAmount()),//balance to complete
					scPackage.getPaymentTerms(),
					"F".equals(scPackage.getPaymentStatus()==null?"":scPackage.getPaymentStatus().trim())?SCPaymentCert.FINAL_PAYMENT:SCPaymentCert.INTERIM_PAYMENT
			});
			dataStore.add(record);

		}

	}
	

}
