package com.gammon.qs.client.ui.panel.detailSection.subcontractorDetailPanel;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.TenderAnalysis;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.tenderAnalysis.SubcontractorTenderAnalysisWrapper;
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
import com.gwtext.client.widgets.menu.CheckItem;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.CheckItemListenerAdapter;

/**
 * @author koeyyeung
 * modifed by irischau
 * on 14 Apr 2014
 * to filter the result
 */
public class TenderAnalysisHistoryDetailPanel extends GridPanel{
	private Store dataStore;
	//Record names
	private static final String JOB_NO_RECORD_NAME = "jobNoRecordName";
	private static final String JOB_DIVISION_RECORD_NAME = "jobDivisionRecordName";
	private static final String PACKAGE_NO_RECORD_NAME = "subconNoRecordName";
	private static final String BUDGET_AMOUNT_RECORD_NAME = "budgetAmountRecordName";
	private static final String QUOTED_AMOUNT_RECORD_NAME = "quotedkAmountRecordName";
	private static final String TENDER_STATUS_RECORD_NAME = "tenderStatusRecordName";
	private static final String STATUS_DESCPRITION_RECORD_NAME = "StatusDescriptionRecordName";
	
	// records
	private final RecordDef recordDef = new RecordDef(
			new FieldDef[] {	
					new StringFieldDef(JOB_NO_RECORD_NAME),
					new StringFieldDef(JOB_DIVISION_RECORD_NAME),
					new StringFieldDef(PACKAGE_NO_RECORD_NAME),
					new StringFieldDef(BUDGET_AMOUNT_RECORD_NAME),
					new StringFieldDef(QUOTED_AMOUNT_RECORD_NAME),
					new StringFieldDef(TENDER_STATUS_RECORD_NAME),
					new StringFieldDef(STATUS_DESCPRITION_RECORD_NAME)
					});
	
	//pagination toolbar
	private PaginationToolbar paginationToolbar;
	
	private ToolbarTextItem jobFilterLabel;
	private ToolbarTextItem divisionFilterLabel;
	private ToolbarTextItem packageFilterLabel;
	private ToolbarTextItem tenderStatusFilterLabel;
	
	private TextField jobFilterField;
	private TextField divisionFilterField;
	private TextField packageFilterField;
	
	private ToolbarButton tenderStatusButton = new ToolbarButton("ALL");
	private CheckItem tenderStatusCheckItem;
	
	private Toolbar toolbar = new Toolbar();
	
	private GlobalMessageTicket globalMessageTicket;
	private GlobalSectionController globalSectionController;
	private String detailSectionPanel_ID;
	
	private String subcontractorNo;
	private String[] tenderStatusItem = {"AWD - Awarded Subcontract", "RCM - Approving or Rejected Subcontract", "Not Awarded"};
	
	public TenderAnalysisHistoryDetailPanel(GlobalSectionController globalSectionController, String subcontractorNo) {
		super();
		this.globalSectionController = globalSectionController;
		detailSectionPanel_ID = globalSectionController.getDetailSectionController().getMainPanel().getId();
		globalMessageTicket = new GlobalMessageTicket();
		this.subcontractorNo = subcontractorNo;
		setupToolbar();
		setupGrid();
		
		search(subcontractorNo, null, null, null, null);
	}
	
	private void setupGrid() {
		setTitle("Tender Analysis History");
		setIconCls("history-icon");
		setBorder(false);
		setFrame(false);
		setPaddings(0);
		setAutoScroll(true);
		setView(new CustomizedGridView());
		
		Renderer colorRenderer = new Renderer() {
			public String render(Object value, CellMetadata cellMetadata,
					Record record, int rowIndex, int colNum, Store store) {
				if(value!=null){
					if(TenderAnalysis.TA_STATUS_AWD.equals(value)){
						return "<font color="+GlobalParameter.GREEN_COLOR+">"+ value.toString()+"</font>";
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
		ColumnConfig jobNoColConfig = new ColumnConfig("Job No.",JOB_NO_RECORD_NAME, 80,true);
		ColumnConfig jobDivisionColConfig = new ColumnConfig("Job Division",JOB_DIVISION_RECORD_NAME, 80,true);
		ColumnConfig packageNoColConfig = new ColumnConfig("Package No.", PACKAGE_NO_RECORD_NAME, 80, true);
		ColumnConfig budgetAmountColConfig = new ColumnConfig("Budget Amount", BUDGET_AMOUNT_RECORD_NAME, 160,true);
		budgetAmountColConfig.setAlign(TextAlign.RIGHT);
		budgetAmountColConfig.setRenderer(amountRenderer);
		ColumnConfig quotedAmountColConfig = new ColumnConfig("Quoted. Amount", QUOTED_AMOUNT_RECORD_NAME ,160,true);
		quotedAmountColConfig.setAlign(TextAlign.RIGHT);
		quotedAmountColConfig.setRenderer(amountRenderer);
		ColumnConfig tenderStatusColConfig = new ColumnConfig("Tender Status", TENDER_STATUS_RECORD_NAME, 110,true);
		tenderStatusColConfig.setRenderer(colorRenderer);
		ColumnConfig statusDescriptionColConfig = new ColumnConfig("Tender Status Description", STATUS_DESCPRITION_RECORD_NAME, 200,true);
		tenderStatusColConfig.setRenderer(colorRenderer);
		
		BaseColumnConfig[] columns;
		columns = new BaseColumnConfig[]{
				new RowNumberingColumnConfig(),
				jobNoColConfig, 
				jobDivisionColConfig,
				packageNoColConfig,
				budgetAmountColConfig,
				quotedAmountColConfig,
				tenderStatusColConfig,
				statusDescriptionColConfig
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
				obtainByPage(pageNum);
			}
		});
		setBottomToolbar(paginationToolbar);
		paginationToolbar.setTotalPage(1);
		paginationToolbar.setCurrentPage(0);
	}
	
	private void setupToolbar(){
		toolbar = new Toolbar();
		
		jobFilterLabel = new ToolbarTextItem("Job No.: ");
		jobFilterField = new TextField();
		
		divisionFilterLabel = new ToolbarTextItem("Division: ");
		divisionFilterField = new TextField();
		
		packageFilterLabel = new ToolbarTextItem("Package No.: ");
		packageFilterField = new TextField();

		tenderStatusFilterLabel = new ToolbarTextItem("Tender Status: ");
		tenderStatusButton = new ToolbarButton("ALL");
		
		FieldListener searchListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER){
//					globalMessageTicket.refresh();
					 String jobNumber = jobFilterField.getText() != null && jobFilterField.getText().trim().length() > 0 ? jobFilterField.getText() : "";
					 String division = divisionFilterField.getText() != null && divisionFilterField.getText().trim().length() > 0 ? divisionFilterField.getText().toUpperCase() : "";
					 String  packageNumber = packageFilterField.getText() != null && packageFilterField.getText().trim().length() > 0 ? packageFilterField.getText() : "";
					 String  tenderStatus = tenderStatusButton.getText().equals("ALL") ? "" : tenderStatusButton.getText();

					search(subcontractorNo, division, jobNumber, packageNumber, tenderStatus);
				}
			}
		};
		
		jobFilterField.addListener(searchListener);
		divisionFilterField.addListener(searchListener);
		packageFilterField.addListener(searchListener);
		
		Menu tenderStatusMenu = new Menu();

        CheckItemListenerAdapter listener = new CheckItemListenerAdapter() {  
            public void onCheckChange(CheckItem item, boolean checked) {  
				if (item != null && item.getText() != null && item.getText().length() > 0){
//					item.setChecked(checked);
//					tenderStatusButton = new ToolbarButton();
					tenderStatusButton.setText(item.getText());
				}
            }  
        };
        
		CheckItem tenderStatusCheckItemAll= new CheckItem("ALL");
		tenderStatusCheckItemAll.setGroup("tenderStatus");
//		tenderStatusCheckItemAll.setChecked(true);
		tenderStatusMenu.addItem(tenderStatusCheckItemAll);
		tenderStatusCheckItemAll.addListener(listener);
		
		for(String tenderStatus : tenderStatusItem){
			tenderStatusCheckItem= new CheckItem(tenderStatus);
			tenderStatusCheckItem.setGroup("tenderStatus");
			tenderStatusMenu.addItem(tenderStatusCheckItem);
			tenderStatusCheckItem.addListener(listener);
		}

		tenderStatusButton.setMenu(tenderStatusMenu);
		
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
				
		
		//Filter's Button
		ToolbarButton filterButton = new ToolbarButton();
		filterButton.setIconCls("filter-icon");
		filterButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				String jobNumber = jobFilterField.getText() != null && jobFilterField.getText().trim().length() > 0 ? jobFilterField.getText() : null;
				String division = divisionFilterField.getText() != null && divisionFilterField.getText().trim().length() > 0 ? divisionFilterField.getText().toUpperCase() : null;
				String packageNumber = packageFilterField.getText() != null && packageFilterField.getText().trim().length() > 0 ? packageFilterField.getText() : null;
				String tenderStatus = convertDescriptionToStatus(tenderStatusButton.getText());
				search(subcontractorNo, division, jobNumber, packageNumber, tenderStatus);

			}
		});
		
		//Toolbar - Filter Button's Tips
		ToolTip filterButtonToolTip = new ToolTip();
		filterButtonToolTip.setTitle("Filter");
		filterButtonToolTip.setHtml("Filter by Line Type");
		filterButtonToolTip.applyTo(filterButton);
		
		toolbar.addItem(jobFilterLabel);
		toolbar.addField(jobFilterField);
		toolbar.addSeparator();
		toolbar.addItem(divisionFilterLabel);
		toolbar.addField(divisionFilterField);
		toolbar.addSeparator();
		toolbar.addItem(packageFilterLabel);
		toolbar.addField(packageFilterField);
		toolbar.addSeparator();
		toolbar.addItem(tenderStatusFilterLabel);
	    toolbar.addButton(tenderStatusButton);
		toolbar.addSeparator();
		toolbar.addButton(filterButton);
		toolbar.addSeparator();
		toolbar.addFill();
		toolbar.addButton(refreshButton);
		setTopToolbar(toolbar);
	}
	
	private void search(String subcontractorNo, String division, String jobNumber, String packageNumber, String tenderStatus) {
		UIUtil.maskPanelById(detailSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getSubcontractorRepository().obtainTenderAnalysisPaginationWrapper(subcontractorNo,division, jobNumber, packageNumber, tenderStatus, new AsyncCallback<PaginationWrapper<SubcontractorTenderAnalysisWrapper>>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
			public void onSuccess(PaginationWrapper<SubcontractorTenderAnalysisWrapper> wrappers) {
				populate(wrappers);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
		});
		
		
	}
	private void populate(PaginationWrapper<SubcontractorTenderAnalysisWrapper> wrappers) {
		dataStore.removeAll();
		if(wrappers==null || wrappers.getCurrentPageContentList().size()==0)
			return;
		
		paginationToolbar.setTotalPage(wrappers.getTotalPage());
		paginationToolbar.setCurrentPage(wrappers.getCurrentPage());
		
		for(final SubcontractorTenderAnalysisWrapper wrapper: wrappers.getCurrentPageContentList()){
			Record record = recordDef.createRecord(new Object[]{
					wrapper.getJobNo(),
					wrapper.getDivision(),
					wrapper.getPackageNo(),
					wrapper.getBudgetAmount(),
					wrapper.getQuotedAmount(),
					wrapper.getStatus(),
					convertStatusToDescription(wrapper.getStatus())
				});
				dataStore.add(record);
		}
	}

	private void obtainByPage(int pageNum) {
		UIUtil.maskPanelById(detailSectionPanel_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getSubcontractorRepository().obtainTenderAnalysisWrapperByPage(pageNum, new AsyncCallback<PaginationWrapper<SubcontractorTenderAnalysisWrapper>>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
			public void onSuccess(PaginationWrapper<SubcontractorTenderAnalysisWrapper> wrappers) {
				populate(wrappers);
				UIUtil.unmaskPanelById(detailSectionPanel_ID);
			}
		});
	}

	private String convertStatusToDescription(String status) {
		status = status==null?"":status.trim();
		if("AWD".equals(status))
			return "Awarded Subcontract";
		else if("RCM".equals(status))
			return "Approving or Rejected Subcontract";
		else
			return "Not Awarded";
	}
	
	private String convertDescriptionToStatus(String description) {
		description = description==null?"":description.trim();
		if(tenderStatusItem[0].trim().equals(description))
			return "AWD";
		else if(tenderStatusItem[1].trim().equals(description))
			return "RCM";
		else if(tenderStatusItem[2].trim().equals(description))
			return "";
		else
			return null;
	}
	
}
