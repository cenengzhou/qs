/**
 * koeyyeung
 * Modified on Jul 3, 20134:23:40 PM
 * Change from Window to Panel
 */
package com.gammon.qs.client.ui.panel.mainSection;

import java.util.Date;
import java.util.List;

import com.gammon.qs.client.controller.DetailSectionController;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRendererCheckIfTotal;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.IVPostingHistory;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.IVHistoryPaginationWrapper;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.util.DateUtil;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.layout.FormLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.RowLayout;


public class IVHistoryEnquiryMainPanel extends Panel{
	private static final String MAIN_PANEL_ID = "IVHistoryEnquiryMainPanel_ID";
	private static final String DATE_FIELD_FORMAT_ALT = "dmY|dmy|d/m/y|d/m/Y|j/n/y|j/n/Y|d.m.y|d.m.Y|j.n.y|j.n.Y";
	private GlobalSectionController globalSectionController;
	private DetailSectionController detailSectionController;
	private GridPanel gridPanel;
	
	//Pagination
	private PaginationToolbar paginationToolbar;
	private ToolbarTextItem totalMovementTextItem;
	
	private Store dataStore;
	
	private final RecordDef ivHistoryRecordDef = new RecordDef(
			new FieldDef[]{
					new StringFieldDef("postingDate"),
					new StringFieldDef("packageNo"),
					new StringFieldDef("objectCode"),
					new StringFieldDef("subsidiaryCode"),
					new StringFieldDef("description"),
					new StringFieldDef("unit"),
					new StringFieldDef("rate"),
					new StringFieldDef("movementAmount"),
					new StringFieldDef("postingUser"),
					new StringFieldDef("documentNo")
			});
	
	
	private TextField packageNoTextField;
	private TextField objectCodeTextField;
	private TextField subsidiaryCodeTextField;
	private DateField fromDateField;
	private DateField toDateField;
	
	
	public IVHistoryEnquiryMainPanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		detailSectionController = this.globalSectionController.getDetailSectionController();
		
		setupUI();
	}


	private void setupUI() {
		setLayout(new RowLayout());
		
		setupSearchPanel();
		setupGridPanel();
		
		setId(MAIN_PANEL_ID);
		
		//hide detail panel
		detailSectionController.getMainPanel().collapse();
		
	}


	private void setupSearchPanel() {
		//SEARCH PANEL
		Panel searchPanel = new Panel();
		searchPanel.setPaddings(0);
		searchPanel.setFrame(true);
		searchPanel.setHeight(45);
		searchPanel.setLayout(new HorizontalLayout(5));
		FieldListener searchListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER)
					newSearch();
			}
		};
		
		Label packageNoLabel = new Label("Package No.");
		packageNoLabel.setAutoWidth(true);
		packageNoLabel.setCls("table-cell");
		packageNoTextField = new TextField("Package No.", "packageNoSearch", 100);
		packageNoTextField.addListener(searchListener);
		searchPanel.add(packageNoLabel);
		searchPanel.add(packageNoTextField);
		
		Label objectCodeLabel = new Label("Object Code");
		objectCodeLabel.setAutoWidth(true);
		objectCodeLabel.setCls("table-cell");
		objectCodeTextField = new TextField("Object Code", "objectCodeSearch", 100);
		objectCodeTextField.addListener(searchListener);
		searchPanel.add(objectCodeLabel);
		searchPanel.add(objectCodeTextField);
		
		Label subsidiaryCodeLabel = new Label("Subsidiary Code");
		subsidiaryCodeLabel.setAutoWidth(true);
		subsidiaryCodeLabel.setCls("table-cell");
		subsidiaryCodeTextField = new TextField("Subsidiary Code", "subsidiaryCodeSearch", 100);
		subsidiaryCodeTextField.addListener(searchListener);
		searchPanel.add(subsidiaryCodeLabel);
		searchPanel.add(subsidiaryCodeTextField);
		
		Label dateLabel = new Label("Date From/To:");
		dateLabel.setAutoWidth(true);
		dateLabel.setCls("table-cell");
		fromDateField = new DateField("Date From", "fromDateSearch", 100);
		fromDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		fromDateField.setFormat("d/m/y");
		fromDateField.setAltFormats(DATE_FIELD_FORMAT_ALT);
		fromDateField.setHideLabel(true);
		toDateField = new DateField("To", "toDateSearch", 100);
		toDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		toDateField.setFormat("d/m/y");
		toDateField.setAltFormats(DATE_FIELD_FORMAT_ALT);
		toDateField.setHideLabel(true);
		searchPanel.add(dateLabel);
		
		Panel[] formPanels = new Panel[2];
		for(int i = 0; i < 2; i++) {
			formPanels[i] = new Panel();
			formPanels[i].setLayout(new FormLayout());
			formPanels[i].setBorder(false);
		}	
		
		formPanels[0].add(fromDateField);
		formPanels[1].add(toDateField);
		
		for(Panel p : formPanels) {
			searchPanel.add(p);
		}
		
		Button searchButton = new Button("Search");
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				newSearch();
			};
		});
		Label emptySpace = new Label();
		emptySpace.setHtml("&nbsp;");
		emptySpace.setWidth(50);
		searchPanel.add(emptySpace);
		searchPanel.add(searchButton);
		
		add(searchPanel);
	}

	private void setupToolbar(){
		Toolbar toolbar = new Toolbar();
		
		ToolbarButton exportButton = new ToolbarButton("Export to Excel");
		exportButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				exportToExcel();
			};
		});
		
		toolbar.addButton(exportButton);
		toolbar.addSeparator();
		
		gridPanel.setTopToolbar(toolbar);
	}
	
	private void setupGridPanel() {
		//GRID PANEL
		gridPanel = new GridPanel();
		setupToolbar();
		
		Renderer amountRenderer = new AmountRendererCheckIfTotal(globalSectionController.getUser());
		Renderer rateRenderer = new RateRenderer(globalSectionController.getUser());
		
		ColumnConfig postingDateColConfig = new ColumnConfig("Posting Date", "postingDate", 85, false);
		ColumnConfig packageNoColConfig = new ColumnConfig("Package No.", "packageNo", 80, false);
		ColumnConfig objectCodeColConfig = new ColumnConfig("Object Code", "objectCode", 90, false);
		ColumnConfig subsidiaryCodeColConfig = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 100, false);
		ColumnConfig descriptionColConfig = new ColumnConfig("Resource Description", "description", 300, false);
		ColumnConfig unitColConfig = new ColumnConfig("Unit", "unit", 40, false);
		ColumnConfig rateColConfig = new ColumnConfig("Rate", "rate", 60, false);
		ColumnConfig movementAmountColConfig = new ColumnConfig("IV Movement Amount", "movementAmount", 125, false);
		ColumnConfig postingUserColConfig = new ColumnConfig("Username", "postingUser", 110, false);
		ColumnConfig documentNoColConfig = new ColumnConfig("Document No.", "documentNo", 100, false);
		
		rateColConfig.setRenderer(rateRenderer);
		movementAmountColConfig.setRenderer(amountRenderer);
		
		ColumnConfig[] columns = new ColumnConfig[]{
				postingDateColConfig,
				packageNoColConfig,
				objectCodeColConfig,
				subsidiaryCodeColConfig,
				descriptionColConfig,
				unitColConfig,
				rateColConfig,
				movementAmountColConfig,
				postingUserColConfig,
				documentNoColConfig
		};
		
		gridPanel.setColumnModel(new ColumnModel(columns));
		GridView view = new CustomizedGridView();
		view.setAutoFill(true);
		view.setForceFit(false);
		gridPanel.setView(view);
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(ivHistoryRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		gridPanel.setStore(dataStore);
		
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(int pageNum){
				searchByPage(pageNum);
			}
		});
		paginationToolbar.addFill();
		totalMovementTextItem = new ToolbarTextItem("<b>Total Movement: </b>");
		paginationToolbar.addItem(totalMovementTextItem);
		gridPanel.setBottomToolbar(paginationToolbar);
		paginationToolbar.setTotalPage(0);
		paginationToolbar.setCurrentPage(0);
		
		add(gridPanel);
	}

	/**
	 * @author matthewatc
	 * 
	 * exports the results for the query specified by the values in the search fields as an excel spreadsheet
	 * (note that this does not export the contents of the gridPanel, but rather exports what would be the 
	 * contents of the gridPanel if a search had been conducted with the currently-entered query parameters)
	 */
	private void exportToExcel() {
		DateTimeFormat df = DateTimeFormat.getFormat("ss-mm-HH-dd-MM-yyyy");
		
		String jobNumber = globalSectionController.getJob().getJobNumber() != null ? globalSectionController.getJob().getJobNumber() : "";
		String packageNo = packageNoTextField.getText() != null ? packageNoTextField.getText().trim() : "";
		String objectCode = objectCodeTextField.getText() != null ? objectCodeTextField.getText().trim() : "";
		String subsidiaryCode = subsidiaryCodeTextField.getText() != null ? subsidiaryCodeTextField.getText().trim() : "";
		String fromDate = fromDateField.getValue() != null ? df.format(fromDateField.getValue()) : "";
		String toDate = toDateField.getValue() != null ? df.format(toDateField.getValue()) : "";
		
		com.google.gwt.user.client.Window.open( 
				GlobalParameter.IV_POSTING_HISTORY_EXCEL_DOWNLOAD_URL + 
				"?jobNumber=" + jobNumber +
				"&packageNo=" + packageNo +
				"&objectCode=" + objectCode +
				"&subsidiaryCode=" + subsidiaryCode +
				"&fromDate=" + fromDate +
				"&toDate=" + toDate, "_blank", "");
		
	}
		
	private void newSearch(){
		String packageNo = packageNoTextField.getText().trim();
		String objectCode = objectCodeTextField.getText().trim();
		String subsidiaryCode = subsidiaryCodeTextField.getText().trim();
		Date fromDate = fromDateField.getValue();
		Date toDate = toDateField.getValue();
		obtainIVPostingHistory(packageNo, objectCode, subsidiaryCode, fromDate, toDate);
	}
	
	private void searchByPage(int pageNum){
		getIVHistoryByPage(pageNum);
	}
	
	public void populateGrid(IVHistoryPaginationWrapper wrapper){
		dataStore.removeAll();
		int totalPage = 0;
		int currentPage = 0;
		Double totalMovement = null;
		if(wrapper != null){
			totalMovement = wrapper.getTotalMovement();
			totalPage = wrapper.getTotalPage();
			currentPage = wrapper.getCurrentPage();
		}
		paginationToolbar.setTotalPage(totalPage);
		paginationToolbar.setCurrentPage(currentPage);
		if(totalMovement == null)
			totalMovement = Double.valueOf(0);
		NumberFormat nf = NumberFormat.getFormat("#,##0.0#");
		totalMovementTextItem.setText("<b>Total Movement:&nbsp;&nbsp;" + nf.format(totalMovement) + "</b>");
		
		if(wrapper == null)
			return;
		
		List<IVPostingHistory> ivHistoryList = wrapper.getCurrentPageContentList();
		for(IVPostingHistory ivHistory : ivHistoryList){
			Record record = ivHistoryRecordDef.createRecord(new Object[]{
					DateUtil.format(ivHistory.getCreatedDate(), "d/m/y"),
					ivHistory.getPackageNo(),
					ivHistory.getObjectCode(),
					ivHistory.getSubsidiaryCode(),
					ivHistory.getResourceDescription(),
					ivHistory.getUnit(),
					ivHistory.getRate(),
					ivHistory.getIvMovementAmount(),
					ivHistory.getCreatedUser(),
					ivHistory.getDocumentNo()
			});
			dataStore.add(record);
		}
		
		//hide detail panel
		if(!globalSectionController.getDetailSectionController().getMainPanel().isCollapsed())
			globalSectionController.getDetailSectionController().getMainPanel().collapse();
	}

	private void obtainIVPostingHistory(String packageNo, String objectCode, String subsidiaryCode, Date fromDate, Date toDate){
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.SEARCHING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getIvPostingHistoryRepository().obtainIVPostingHistory(globalSectionController.getJob().getJobNumber(), packageNo, objectCode, subsidiaryCode, fromDate, toDate, new AsyncCallback<IVHistoryPaginationWrapper>(){
			public void onSuccess(IVHistoryPaginationWrapper wrapper) {
				populateGrid(wrapper);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
	}
	
	private void getIVHistoryByPage(int pageNum){
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.SEARCHING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getIvPostingHistoryRepository().getIVPostingHistoryByPage(pageNum, new AsyncCallback<IVHistoryPaginationWrapper>(){
			public void onSuccess(IVHistoryPaginationWrapper wrapper) {
				populateGrid(wrapper);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
	}
	
}
