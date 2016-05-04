package com.gammon.qs.client.ui.window.mainSection;

import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.RepackagingDetailRepositoryRemote;
import com.gammon.qs.client.repository.RepackagingDetailRepositoryRemoteAsync;
import com.gammon.qs.client.repository.RepackagingEntryRepositoryRemote;
import com.gammon.qs.client.repository.RepackagingEntryRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemote;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.panel.mainSection.RepackagingListGridPanel;
import com.gammon.qs.client.ui.renderer.AmountRendererCheckIfTotal;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.RepackagingEntry;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.RepackagingDetailComparisonWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
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
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

public class RepackagingDetailWindow extends Window {
	private static final String RESULT_PANEL_ID ="repackagingDetailResultPanel"; 
	private static final String MAIN_PANEL_ID ="repackagingDetailPanel"; 
	
	private GlobalSectionController globalSectionController;
	private RepackagingEntry repackagingEntry;
	private RepackagingListGridPanel repackagingListGridPanel; 

	//UI
	private Panel mainPanel;
	private Panel searchPanel;
	private Panel resultPanel;
	private GridPanel resultGridPanel;
	
	private GlobalMessageTicket globalMessageTicket;
	
	//Pagination
	private PaginationToolbar paginationToolbar;
	
	private RepackagingDetailRepositoryRemoteAsync repackagingDetailRepository;
	private RepackagingEntryRepositoryRemoteAsync repackagingEntryRepository;
	// used to check access rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	
	private final TextField packageNoTextField;
	private final TextField objectCodeTextField;
	private final TextField subsidiaryCodeTextField;
	
	private Checkbox showChangesCheckBox;
	
	Button confirmButton;
		
	//data store
	private Store dataStore;
		
	private ColumnConfig packageNoColConfig;
	private ColumnConfig objectCodeColConfig;
	private ColumnConfig subsidiaryCodeColConfig;
	private ColumnConfig descriptionColConfig;
	private ColumnConfig unitColConfig;
	private ColumnConfig rateColConfig;
	private ColumnConfig amountColConfig;
	private ColumnConfig previousAmountColConfig;
	private ColumnConfig varianceColConfig;
	private ColumnConfig resourceTypeColConfig;
	
	private final RecordDef resourceSummaryRecordDef = new RecordDef(
			new FieldDef[]{
					new StringFieldDef("packageNo"),
					new StringFieldDef("objectCode"),
					new StringFieldDef("subsidiaryCode"),
					new StringFieldDef("description"),
					new StringFieldDef("unit"),
					new StringFieldDef("rate"),
					new StringFieldDef("amount"),
					new StringFieldDef("previousAmount"),
					new StringFieldDef("variance"),
					new StringFieldDef("resourceType")
			});
	
	//Search parameters
	private String packageNo;
	private String objectCode;
	private String subsidiaryCode;
	
	private ToolbarTextItem totalBudgetTextItem = new ToolbarTextItem("<b>Total Budget: 0</b>");
	private ToolbarTextItem totalSellingValueTextItem = new ToolbarTextItem("<b>Total Selling Value: 0</b>");
		
	public RepackagingDetailWindow(final RepackagingListGridPanel repackagingListGridPanel, final RepackagingEntry repackagingEntry){
		super();
		this.globalSectionController = repackagingListGridPanel.getGlobalSectionController();
		this.repackagingEntry = repackagingEntry;
		this.repackagingListGridPanel = repackagingListGridPanel;
		globalMessageTicket = new GlobalMessageTicket();
		
		repackagingDetailRepository = (RepackagingDetailRepositoryRemoteAsync) GWT.create(RepackagingDetailRepositoryRemote.class);
		((ServiceDefTarget)repackagingDetailRepository).setServiceEntryPoint(GlobalParameter.REPACKAGING_DETAIL_REPOSITORY_URL);
		repackagingEntryRepository = (RepackagingEntryRepositoryRemoteAsync) GWT.create(RepackagingEntryRepositoryRemote.class);
		((ServiceDefTarget)repackagingEntryRepository).setServiceEntryPoint(GlobalParameter.REPACKAGING_ENTRY_REPOSITORY_URL);
		userAccessRightsRepository = (UserAccessRightsRepositoryRemoteAsync) GWT.create(UserAccessRightsRepositoryRemote.class);
		((ServiceDefTarget)userAccessRightsRepository).setServiceEntryPoint(GlobalParameter.USER_ACCESS_RIGHTS_REPOSITORY_URL);

		this.setTitle("Repackaging Review");
		this.setPaddings(5);
		this.setWidth(1024);
		this.setHeight(650);
		this.setModal(true);
		this.setClosable(false);
		this.setLayout(new FitLayout());
		
		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setId(MAIN_PANEL_ID);
		
		//search Panel
		searchPanel = new Panel();
		searchPanel.setPaddings(3);
		searchPanel.setFrame(true);
		searchPanel.setHeight(50);
		searchPanel.setLayout(new TableLayout(9));
		
		FieldListener searchListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER)
					search();
			}
		};
		
		Label packageNoLabel = new Label("Package No.");
		packageNoLabel.setCls("table-cell");
		searchPanel.add(packageNoLabel);
		packageNoTextField = new TextField("Package No.", "packageNo", 100);
		packageNoTextField.setCtCls("table-cell");
		packageNoTextField.setCls("table-cell");
		packageNoTextField.addListener(searchListener);
		searchPanel.add(packageNoTextField);
		
		Label objectCodeLabel = new Label("Object Code");
		objectCodeLabel.setCls("table-cell");
		searchPanel.add(objectCodeLabel);
		objectCodeTextField = new TextField("Object Code", "objectCode", 100);
		objectCodeTextField.setCtCls("table-cell");
		objectCodeTextField.setCls("table-cell");
		objectCodeTextField.addListener(searchListener);
		searchPanel.add(objectCodeTextField);
		
		Label subsidiaryCodeLabel = new Label("Subsidiary Code");
		subsidiaryCodeLabel.setCls("table-cell");
		searchPanel.add(subsidiaryCodeLabel);
		subsidiaryCodeTextField = new TextField("Subsidiary Code", "subsidiaryCode", 100);
		subsidiaryCodeTextField.setCtCls("table-cell");
		subsidiaryCodeTextField.setCls("table-cell");
		subsidiaryCodeTextField.addListener(searchListener);
		searchPanel.add(subsidiaryCodeTextField);
		
		showChangesCheckBox = new Checkbox("Show Changes Only");
		showChangesCheckBox.setCtCls("table-cell");
		showChangesCheckBox.setLabelStyle("table-cell");
		searchPanel.add(showChangesCheckBox);
		showChangesCheckBox.setChecked(true); //Default to checked
		
		Button searchButton = new Button("Search");
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				search();
			};
		});
		searchButton.setCtCls("table-cell");
		searchPanel.add(searchButton);
		
		Button exportButton = new Button("Export to Excel");
		exportButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				exportToExcel();
			};
		});
		exportButton.setCtCls("table-cell");
		searchPanel.add(exportButton);
		
		resultPanel = new Panel();
		resultPanel.setId(RESULT_PANEL_ID);
		resultPanel.setBorder(true);
		resultPanel.setFrame(true);
		resultPanel.setPaddings(5);
		resultPanel.setAutoScroll(true);
		resultPanel.setLayout(new FitLayout());
		
		resultGridPanel =  new GridPanel();
		Renderer amountRenderer = new AmountRendererCheckIfTotal(globalSectionController.getUser());
		Renderer rateRenderer = new RateRenderer(globalSectionController.getUser());
		
		packageNoColConfig = new ColumnConfig("Package No.", "packageNo", 40, false);
		objectCodeColConfig = new ColumnConfig("Object Code", "objectCode", 50, false);
		subsidiaryCodeColConfig = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 65, false);
		descriptionColConfig = new ColumnConfig("Resource Description", "description", 240, false);
		unitColConfig = new ColumnConfig("Unit", "unit", 30, false);
		rateColConfig = new ColumnConfig("Rate", "rate", 125, false);
		rateColConfig.setRenderer(rateRenderer);
		rateColConfig.setAlign(TextAlign.RIGHT);
		amountColConfig = new ColumnConfig("Current Amount", "amount", 125, false);
		amountColConfig.setRenderer(amountRenderer);
		amountColConfig.setAlign(TextAlign.RIGHT);
		previousAmountColConfig = new ColumnConfig("Previous Amount", "previousAmount", 125, false);
		previousAmountColConfig.setRenderer(amountRenderer);
		previousAmountColConfig.setAlign(TextAlign.RIGHT);
		varianceColConfig = new ColumnConfig("Variance", "variance", 125, false);
		varianceColConfig.setRenderer(amountRenderer);
		varianceColConfig.setAlign(TextAlign.RIGHT);
		resourceTypeColConfig = new ColumnConfig("Type", "resourceType", 30, false);
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(resourceSummaryRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		resultGridPanel.setStore(dataStore);
		
		BaseColumnConfig[] columns = new BaseColumnConfig[]{
				packageNoColConfig,
				objectCodeColConfig,
				subsidiaryCodeColConfig,
				descriptionColConfig,
				unitColConfig,
				rateColConfig,
				previousAmountColConfig,
				amountColConfig,
				varianceColConfig,
				resourceTypeColConfig
		};
		
		resultGridPanel.setColumnModel(new ColumnModel(columns));
		
		GridView view = new CustomizedGridView();
		view.setAutoFill(true);
		view.setForceFit(false);
		resultGridPanel.setView(view);
		
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(int pageNum){
				globalMessageTicket.refresh();
				searchByPage(pageNum);
			}
		});
		paginationToolbar.addFill();
		paginationToolbar.addItem(totalBudgetTextItem);
		paginationToolbar.addSpacer();
		paginationToolbar.addSeparator();
		paginationToolbar.addSpacer();
		paginationToolbar.addItem(totalSellingValueTextItem);
		
		resultGridPanel.setBottomToolbar(paginationToolbar);
		paginationToolbar.setTotalPage(0);
		paginationToolbar.setCurrentPage(0);
		
		resultPanel.add(resultGridPanel);
		mainPanel.add(searchPanel);
		mainPanel.add(resultPanel);
		this.add(mainPanel);
		
		if(repackagingEntry.getStatus().equals("300")){
			confirmButton = new Button("Confirm");
			confirmButton.addListener(new ButtonListenerAdapter() {
				public void onClick(Button button, EventObject e){
					globalMessageTicket.refresh();
					MessageBox.confirm("Confirm", "Are you sure you want to confirm and lock these resources?", new MessageBox.ConfirmCallback(){
						public void execute(String btnID) {
							if(btnID.equals("yes")){
								checkEntryStatusAndPostDetails();
							}
						}
					});
				}
			});
			this.addButton(confirmButton);
			confirmButton.hide();
		}
		
		Button closeWindowButton = new Button("Close");
		closeWindowButton.addListener(new ButtonListenerAdapter(){ 
				public void onClick(Button button, EventObject e) {
					clearCacheAndCloseWindow();				
				};
		});		
		this.addButton(closeWindowButton);
		
		// Check for access rights - then add toolbar buttons accordingly
		try{
			UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.B010101_REPACKAGING_DETAIL_WINDOW, new AsyncCallback<List<String>>(){
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
		if(accessRightsList != null && accessRightsList.contains("ENABLE")){
			if(confirmButton != null)
				confirmButton.show();
		}
	}
	
	private void search(){
		packageNo = packageNoTextField.getText().trim();
		objectCode = objectCodeTextField.getText().trim();
		subsidiaryCode = subsidiaryCodeTextField.getText().trim();
		boolean changesOnly = showChangesCheckBox.getValue();
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Loading...", true);
		try {
			SessionTimeoutCheck.renewSessionTimer();
			repackagingDetailRepository.getRepackagingDetailsNewSearch(repackagingEntry, packageNo, 
					objectCode, subsidiaryCode, changesOnly, new AsyncCallback<RepackagingPaginationWrapper<RepackagingDetailComparisonWrapper>>(){
				public void onSuccess(RepackagingPaginationWrapper<RepackagingDetailComparisonWrapper> detailComparisons) {
					populateGrid(detailComparisons);
					UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				}
				public void onFailure(Throwable e) {
					UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
				}
			});
		} catch (Exception e1) {
			UIUtil.alert(e1.getMessage());
		}
	}
	
	/* added Excel export functionality - matthewatc 3/2/12 */
	private void exportToExcel() {
		String packageNoQuery = packageNoTextField.getText().trim();
		String objectCodeQuery = objectCodeTextField.getText().trim();
		String subsidiaryCodeQuery = subsidiaryCodeTextField.getText().trim();
		Boolean changesOnly = showChangesCheckBox.getValue();
		
		com.google.gwt.user.client.Window.open( 
				GlobalParameter.REPACKAGING_DETAIL_EXCEL_DOWNLOAD_URL + 
				"?repackagingEntryId=" + repackagingEntry.getId() +
				"&packageNo=" + packageNoQuery +
				"&objectCode=" + objectCodeQuery +
				"&subsidiaryCode=" + subsidiaryCodeQuery +
				"&changesOnly=" + changesOnly.toString() , "_blank", "");
	}
	
	private void searchByPage(int pageNum){
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Loading...", true);
		try {
			SessionTimeoutCheck.renewSessionTimer();
			repackagingDetailRepository.getRepackagingDetailsByPage(pageNum, new AsyncCallback<RepackagingPaginationWrapper<RepackagingDetailComparisonWrapper>>(){
				public void onSuccess(RepackagingPaginationWrapper<RepackagingDetailComparisonWrapper> detailComparisons) {
					populateGrid(detailComparisons);
					UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				}
				public void onFailure(Throwable e) {
					UIUtil.unmaskPanelById(MAIN_PANEL_ID);
					UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
				}
			});
		} catch (Exception e1) {
			UIUtil.alert(e1.getMessage());
		}
	}
	
	public void populateGrid(RepackagingPaginationWrapper<RepackagingDetailComparisonWrapper> wrapper){
		dataStore.removeAll();
				
		int totalPage = 0;
		int currentPage = 0;
		Double totalBudget = Double.valueOf(0);
		Double totalSellingValue = Double.valueOf(0);
		Double previousBudget = Double.valueOf(0);
		Double previousSellingValue = Double.valueOf(0);
		if(wrapper != null){
			totalBudget = wrapper.getTotalBudget() != null ? wrapper.getTotalBudget() : Double.valueOf(0);
			totalSellingValue = wrapper.getTotalSellingValue() != null ? wrapper.getTotalSellingValue() : Double.valueOf(0);
			previousBudget = wrapper.getPreviousBudget() != null ? wrapper.getPreviousBudget() : Double.valueOf(0);
			previousSellingValue = wrapper.getPreviousSellingValue() != null ? wrapper.getPreviousSellingValue() : Double.valueOf(0);
			totalPage = wrapper.getTotalPage();
			currentPage = wrapper.getCurrentPage();
		}
		paginationToolbar.setTotalPage(totalPage);
		paginationToolbar.setCurrentPage(currentPage);

		NumberFormat nf = NumberFormat.getFormat("#,##0.0#");
		totalBudgetTextItem.setText("<b>Budget (Current / Previous):&nbsp;&nbsp;" + nf.format(totalBudget) + " / " + nf.format(previousBudget) + "</b>");
		totalSellingValueTextItem.setText("<b>Selling Value:&nbsp;&nbsp;" + nf.format(totalSellingValue) + " / " + nf.format(previousSellingValue) + "</b>");
		
		if(wrapper == null)
			return;
		
		if(wrapper.getCurrentPageContentList().size() == 0 && "2".equals(globalSectionController.getJob().getRepackagingType())
				&& showChangesCheckBox.getValue()){
			MessageBox.alert("There are no changes at the resource summary level.<br/>Please note that subcontract resources are grouped by package number and account code.");
			return;
		}
		
		try{
			Record[] records = new Record[wrapper.getCurrentPageContentList().size()];
			int ind = 0;
			for (RepackagingDetailComparisonWrapper detailComparison : wrapper.getCurrentPageContentList()){
				Double amount = detailComparison.getAmount();
				Double prevAmount = detailComparison.getPreviousAmount();
				Double diff = amount == null ? -prevAmount : prevAmount == null ? amount : amount - prevAmount;
				records[ind++] = resourceSummaryRecordDef.createRecord(new Object[]{
					detailComparison.getPackageNo(),
					detailComparison.getObjectCode(),
					detailComparison.getSubsidiaryCode(),
					detailComparison.getDescription(),
					detailComparison.getUnit(),
					detailComparison.getRate(),
					amount,
					prevAmount,
					diff,
					detailComparison.getResourceType()
				});
			}
			dataStore.add(records);
		}
		catch(Exception e){
			UIUtil.alert(e);
		}
	}
	
	private void clearCacheAndCloseWindow(){
		SessionTimeoutCheck.renewSessionTimer();
		repackagingDetailRepository.clearCache(new AsyncCallback<Boolean>(){
			public void onSuccess(Boolean success) {
				repackagingListGridPanel.closeCurrentWindow();
			}
			
			public void onFailure(Throwable e) {
				UIUtil.alert(e.getMessage());
			}
		});
	}
	
	private void checkEntryStatusAndPostDetails(){
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Checking Entry Status", true);
		SessionTimeoutCheck.renewSessionTimer();
		repackagingEntryRepository.getRepackagingEntry(repackagingEntry.getId(), new AsyncCallback<RepackagingEntry>(){
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				UIUtil.checkSessionTimeout(e, false,globalSectionController.getUser());
			}

			public void onSuccess(RepackagingEntry repackagingEntry) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				if("900".equals(repackagingEntry.getStatus()))
					MessageBox.alert("This entry has already been confirmed");
				else if("200".equals(repackagingEntry.getStatus()))
					MessageBox.alert("New repackaging updates have been made - please regenerate the snapshot before confirming");
				else{
					postRepackagingDetails();
				}
			}
		});
	}
	
	private void postRepackagingDetails(){
		UIUtil.maskPanelById(MAIN_PANEL_ID, "Posting details", true);
		SessionTimeoutCheck.renewSessionTimer();
		repackagingDetailRepository.postRepackagingDetails(repackagingEntry, globalSectionController.getUser().getUsername(), new AsyncCallback<Boolean>(){
			public void onSuccess(Boolean success) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				if(success != null && success){
					repackagingListGridPanel.updateStatus("900");
					repackagingListGridPanel.closeCurrentWindow();
				}
				else
					UIUtil.alert("An error occured while posting repackaging details to JDE.");
			}
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				UIUtil.checkSessionTimeout(e, false,globalSectionController.getUser());
			}			
		});
	}
	
	public void setGlobalSectionController(GlobalSectionController globalSectionController) {
		this.globalSectionController = globalSectionController;
	}

	public GlobalSectionController getGlobalSectionController() {
		return globalSectionController;
	}
}
