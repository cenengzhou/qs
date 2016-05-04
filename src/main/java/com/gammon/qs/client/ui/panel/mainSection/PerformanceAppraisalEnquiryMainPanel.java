package com.gammon.qs.client.ui.panel.mainSection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gammon.qs.client.controller.DetailSectionController;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.performanceAppraisal.PerformanceAppraisalPaginationWrapper;
import com.gammon.qs.wrapper.performanceAppraisal.PerformanceAppraisalWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

/**
 * koeyyeung
 * modified on Jun 19, 201311:12:45 AM
 * Change from Window to Main Panel
 */
public class PerformanceAppraisalEnquiryMainPanel extends Panel{
	private static final String MAIN_PANEL_ID = "PerformanceAppraisalEnquiryMainPanel_ID";
	private GlobalSectionController globalSectionController;

	/**
	 * PAGINATION
	 */
	private PaginationToolbar paginationToolbar = new PaginationToolbar();
	


	/**
	 * OUTPUT DATA
	 */
	private Store dataStore;
	private final FieldDef[] fieldDef = new FieldDef[]{
			new StringFieldDef("jobNumber"), // MCU
			new IntegerFieldDef("subcontractNumber"), // DC07
			new StringFieldDef("subcontractDescription"), // get from package description
			new StringFieldDef("reviewNumber"), // OSEQ
			new IntegerFieldDef("vendorNumber"), // AN8
			new StringFieldDef("vendorName"), // need to call other web service
			new StringFieldDef("performance"), // AA1
			new StringFieldDef("performanceGroup"), // SCGC
			new StringFieldDef("performanceGroupDescription"),
			new StringFieldDef("status"), // EV04
	};
	private final RecordDef performanceAppraisalRecordDef = new RecordDef(fieldDef);

	// INPUT FIELD
	private TextField jobNumberTextField; // MCU
	private TextField reviewNumberTextField; // OSEQ
	private TextField appraisalTypeTextField; // APTY
	private ComboBox performanceGroupComboBox = new ComboBox(); // SCGC
	private TextField vendorNumberTextField; // AN8
	private TextField subcontractNumberTextField; // DC07
	private ComboBox statusComboBox; // EV04
	private Store storeStatus = new SimpleStore(new String[]{"statusValue", "statusDisplay"}, PerformanceAppraisalEnquiryMainPanel.getStatus());
	private static final String PERFORMANCE_GROUP_VALUE = "performanceGroupValue";
	private static final String PERFORMANCE_GROUP_DISPLAY = "performanceGroupDisplay";
	private String[][] performanceGroups = new String[][]{};
	private Store storePerformanceGroup = new SimpleStore(new String[]{PERFORMANCE_GROUP_VALUE, PERFORMANCE_GROUP_DISPLAY}, performanceGroups);


	// store status
	private static String[][] getStatus(){
		return new String[][]{
				new String[]{"", "All"},
				new String[]{"A", "Approved"},
				new String[]{"E", "Evaluated"},
				new String[]{"N", "Not Evaluated"},
				new String[]{"P", "Pending for approval"},
				new String[]{"V", "Verified for approval"}
		};
	}

	private String jobNumberValue;
	private Map<String, String> appraisalPerformanceGroupMap;
	private DetailSectionController detailSectionController;
	
	public PerformanceAppraisalEnquiryMainPanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		this.detailSectionController = globalSectionController.getDetailSectionController();
		this.jobNumberValue = this.globalSectionController.getJob().getJobNumber();
		
		obtainPerformaceAppraisalGroup();
		setupUI();
		
		//hide detailSectionPanel
		if (!detailSectionController.getMainPanel().isCollapsed())
			detailSectionController.getMainPanel().collapse();
	}

	private void setupUI() {
		setLayout(new RowLayout());	

		setupSearchPanel();
		setupGridPanel();

		setId(MAIN_PANEL_ID);
	}

	private void setupSearchPanel() {
		
		
		
	
		//initialize SEARCHPANEL
		Panel searchPanel = new Panel();
		searchPanel.setPaddings(0);
		searchPanel.setFrame(true);
		searchPanel.setHeight(70);
		searchPanel.setAutoWidth(true);
		searchPanel.setLayout(new TableLayout(2));

		Panel searchPanelLeft = new Panel();
		searchPanelLeft.setLayout(new TableLayout(4));
		searchPanelLeft.setPaddings(0);

		Panel searchPanelRight = new Panel();
		searchPanelRight.setPaddings(0);
		searchPanelRight.setLayout(new TableLayout(1));

		for(Panel p : new Panel[]{searchPanelLeft, searchPanelRight}) {
			p.setBorder(false);
			searchPanel.add(p);
		}

		Panel searchPanelRightUpper = new Panel();
		Panel searchPanelRightLower = new Panel();
		for(Panel p : new Panel[]{searchPanelRightUpper, searchPanelRightLower}) {
			p.setLayout(new TableLayout(4));
			p.setBorder(false);
			p.setPaddings(0);
			searchPanelRight.add(p);
		}

		// Label for input field
		Label jobNumberLabel = new Label("Job Number:");
		Label reviewNumberLabel = new Label("Review Number:");
		Label appraisalTypeLabel = new Label("Appraisal Type:");
		Label performanceGroupLabel = new Label("Performance Group:");
		Label vendorNumberLabel = new Label("Vendor Number:");
		Label subcontractNumberLabel = new Label("Subcontract Number:");
		Label statusLabel = new Label("Status:");

		// INPUT FIELD
		jobNumberTextField = new TextField("Job Number", "jobNumberSearch", 100); // MCU
		reviewNumberTextField  = new TextField("Review Number", "reviewNumberSearch", 100); // OSEQ
		appraisalTypeTextField  = new TextField("Appraisal Type", "appraisalTypeSearch", 100); // APTY
		performanceGroupComboBox = new ComboBox(); // SCGC
		vendorNumberTextField = new TextField("Vendor Number", "vendorNumberSearch", 100); // AN8
		subcontractNumberTextField = new TextField("Subcontract Number", "subcontractNumberSearch", 100); // DC07
		statusComboBox = new ComboBox(); // EV04

		// vendorNumber or AN8
		vendorNumberLabel.setAutoShow(true);
		addLabel2Panel(vendorNumberLabel, searchPanelLeft);
		addTextField2Panel(vendorNumberTextField, searchPanelLeft);

		// jobNumber or MCU
		addLabel2Panel(jobNumberLabel, searchPanelLeft);
		jobNumberTextField.setValue(jobNumberValue);
		// modified by brian on 20110126
		// allow user to search job number
		//		jobNumberTextField.setDisabled(true);
		addTextField2Panel(jobNumberTextField, searchPanelLeft);

		// subcontractNumber or DC07
		addLabel2Panel(subcontractNumberLabel, searchPanelRightUpper);
		addTextField2Panel(subcontractNumberTextField, searchPanelRightUpper);

		// status Label
		addLabel2Panel(statusLabel, searchPanelRightUpper);

		// statusComboBox
		storeStatus.load();
		statusComboBox.setForceSelection(true);
		statusComboBox.setStore(storeStatus);
		statusComboBox.setDisplayField("statusDisplay");
		statusComboBox.setValueField("statusValue");
		statusComboBox.setWidth(130);
		statusComboBox.setValue("");
		searchPanelRightUpper.add(statusComboBox);

		// appraisalType or APTY
		addLabel2Panel(appraisalTypeLabel, searchPanelLeft);
		addTextField2Panel(appraisalTypeTextField, searchPanelLeft);

		// reviewNumber or OSEQ
		addLabel2Panel(reviewNumberLabel, searchPanelLeft);
		addTextField2Panel(reviewNumberTextField, searchPanelLeft);

		// performanceGroup or SCGC
		addLabel2Panel(performanceGroupLabel, searchPanelRightLower);
		storePerformanceGroup.load();
		performanceGroupComboBox.setForceSelection(true);
		performanceGroupComboBox.setDisplayField(PERFORMANCE_GROUP_DISPLAY);
		performanceGroupComboBox.setValueField(PERFORMANCE_GROUP_VALUE);
		performanceGroupComboBox.setStore(storePerformanceGroup);
		performanceGroupComboBox.setWidth(250);
		performanceGroupComboBox.setValue("All");
		searchPanelRightLower.add(performanceGroupComboBox);

		// Search button
		Button searchButton = new Button("Search");
		searchButton.addClass("right-align-button");
		searchButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				search();
			}
		});
		searchPanelRightLower.add(searchButton);

		Button exportButton = new Button("Export to Excel");
		exportButton.addClass("right-align-button");
		exportButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				exportToExcel();
			}
		});
		searchPanelRightLower.add(exportButton);

		add(searchPanel);
	}


	private void setupGridPanel() {
		// initialize GRIDPANEL
		GridPanel gridPanel = new GridPanel();
		GridView view = new CustomizedGridView();
		view.setForceFit(false);
		gridPanel.setView(view);

		// RENDERER
		final Renderer rateRenderer = new RateRenderer(globalSectionController.getUser());

		ColumnConfig jobNumberColConfig = new ColumnConfig("Job Number", "jobNumber", 80, true);
		ColumnConfig subcontractNumberColConfig = new ColumnConfig("Subcontract Number", "subcontractNumber", 75, true);
		ColumnConfig subcontractDescriptionColConfig = new ColumnConfig("Subcontract Description", "subcontractDescription", 250, true);
		ColumnConfig reviewNumberColConfig = new ColumnConfig("Review Number", "reviewNumber", 80, true);
		ColumnConfig vendorNumberColConfig = new ColumnConfig("Vendor Number", "vendorNumber", 80, true);
		ColumnConfig vendorNameColConfig = new ColumnConfig("Vendor Name", "vendorName", 200, true);
		ColumnConfig performanceColConfig = new ColumnConfig("Performance", "performance", 80, true);
		performanceColConfig.setRenderer(rateRenderer);

		ColumnConfig performanceGroupColConfig = new ColumnConfig("Performance Group", "performanceGroup", 100, true);
		ColumnConfig performanceGroupDescriptionColConfig = new ColumnConfig("Performance Group Description", "performanceGroupDescription", 200, true);
		ColumnConfig statusColConfig = new ColumnConfig("Status", "status", 50, true);

		ColumnConfig[] columns = new ColumnConfig[]{
				jobNumberColConfig,
				subcontractNumberColConfig,
				subcontractDescriptionColConfig,
				reviewNumberColConfig,
				vendorNumberColConfig,
				vendorNameColConfig,
				performanceColConfig,
				performanceGroupColConfig,
				performanceGroupDescriptionColConfig,
				statusColConfig
		};
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(int pageNumber){
				getAppraisalResultByPage(globalSectionController.getJob().getJobNumber(), pageNumber);
			}
		});

		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(performanceAppraisalRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();

		gridPanel.setColumnModel(new ColumnModel(columns));
		gridPanel.setStore(dataStore);
		gridPanel.setBottomToolbar(paginationToolbar);
		paginationToolbar.setDisabled(true);

		add(gridPanel);
	}

	private void exportToExcel() {
		String jobNumber =  jobNumberTextField.getValueAsString().trim();
		String sequenceNumber = reviewNumberTextField.getText().trim();
		String appraisalType = appraisalTypeTextField.getText().trim();
		String performanceGroup = performanceGroupComboBox.getValue().equals("All") ? "" : performanceGroupComboBox.getValue();
		String vendorNumber = vendorNumberTextField.getText().trim();
		String subcontractNumber = subcontractNumberTextField.getText().trim();
		String status = this.statusComboBox.getText().trim();
		if(!"A".equals(status) && !"E".equals(status) && !"N".equals(status) && !"P".equals(status) && !"V".equals(status)) {
			status = "";
		}

		com.google.gwt.user.client.Window.open( 
				GlobalParameter.PERFORMANCE_APPRAISAL_EXCEL_DOWNLOAD_URL + 
				"?jobNumber=" + jobNumber +
				"&sequenceNumber=" + sequenceNumber +
				"&appraisalType=" + appraisalType +
				"&groupCode=" + performanceGroup +
				"&vendorNumber=" + vendorNumber +
				"&subcontractNumber=" + subcontractNumber +
				"&status=" + status , "_blank", "");
	}

	public void search(){
		dataStore.removeAll();


		String regex = "\\d*";

		Integer reviewNumber = null;
		if(reviewNumberTextField.getText() == null)
			reviewNumber = null;
		else if(reviewNumberTextField.getText().trim().length() <= 0)
			reviewNumber = null;
		else if(reviewNumberTextField.getText().matches(regex))
			reviewNumber = Integer.parseInt(reviewNumberTextField.getText().trim()); // OSEQ
		else
			MessageBox.alert("Please input number for Review Number.");

		String jobNumber =  jobNumberTextField.getValueAsString().trim();
		String appraisalType = appraisalTypeTextField.getText().trim(); // APTY
		String performanceGroup = performanceGroupComboBox.getValue().equals("All") ? "" : performanceGroupComboBox.getValue(); // SCGC

		Integer vendorNumber = null;
		if(vendorNumberTextField.getText() == null)
			vendorNumber = null;
		else if(vendorNumberTextField.getText().trim().length() <= 0)
			vendorNumber = null;
		else if(vendorNumberTextField.getText().matches(regex))
			vendorNumber = Integer.parseInt(vendorNumberTextField.getText().trim()); // AN8
		else
			MessageBox.alert("Please input number for Vendor Number");

		Integer subcontractNumber = null;
		if(subcontractNumberTextField.getText() == null)
			subcontractNumber = null;
		else if(subcontractNumberTextField.getText().trim().length() <= 0)
			subcontractNumber = null;
		else if(subcontractNumberTextField.getText().matches(regex))
			subcontractNumber = Integer.parseInt(subcontractNumberTextField.getText().trim()); // DC07
		else
			MessageBox.alert("Please input number for Subcontract number");

		String status = this.statusComboBox.getText().trim();
		if(!"A".equals(status) && !"E".equals(status) && !"N".equals(status) && !"P".equals(status) && !"V".equals(status)){
			status = null;
		}


		searchAppraisalResult(jobNumber, reviewNumber, appraisalType, performanceGroup, vendorNumber, subcontractNumber, status, 0);
	}

	public void populateGridByPage(PerformanceAppraisalPaginationWrapper wrapper){
		if(wrapper == null){
			dataStore.removeAll();
			this.paginationToolbar.setDisabled(true);
			MessageBox.alert("No data was found"); 
			return;
		}
		else if(wrapper.getCurrentPageContentList() != null && wrapper.getCurrentPageContentList().size() >= 0){
			this.paginationToolbar.setDisabled(false);
			this.paginationToolbar.setTotalPage(wrapper.getTotalPage());
			this.paginationToolbar.setCurrentPage(wrapper.getCurrentPage());
			populateContentList(wrapper.getCurrentPageContentList());
		}
		else{
			dataStore.removeAll();
			this.paginationToolbar.setDisabled(true);
			MessageBox.alert("No data was found"); 
			return;
		}
		
		//hide detailSectionPanel
		if (!detailSectionController.getMainPanel().isCollapsed())
			detailSectionController.getMainPanel().collapse();
	}

	public void populateContentList(List<PerformanceAppraisalWrapper> appraisalsList){
		dataStore.removeAll();
		if(appraisalsList == null){
			this.paginationToolbar.setDisabled(true);
			MessageBox.alert("No data was found");
		}
		else if(appraisalsList.size() == 0){
			this.paginationToolbar.setDisabled(true);
			MessageBox.alert("No data was found");
		}

		else{
			for(PerformanceAppraisalWrapper appraisal : appraisalsList){
				Record record = performanceAppraisalRecordDef.createRecord(new Object[]{
						appraisal.getJobNumber(),
						appraisal.getSubcontractNumber(),
						appraisal.getSubcontractDescription(),
						Format.leftPad(appraisal.getReviewNumber().toString(), 4, "0"),
						appraisal.getVendorNumber(),
						appraisal.getVendorName(),
						appraisal.getScore(),
						appraisal.getPerformanceGroup(),
						"Loading...",
						appraisal.getStatus()
				});
				dataStore.add(record);
			}
			populatePerformanceGroupDescriptions();
		}
	}

	private void populatePerformanceGroupDescriptions() {
		for(Record rec : dataStore.getRecords()) {
			if(appraisalPerformanceGroupMap.containsKey(rec.getAsString("performanceGroup").trim())) {
				rec.set("performanceGroupDescription", appraisalPerformanceGroupMap .get(rec.getAsString("performanceGroup").trim()));
				rec.commit();
			} else {
				rec.set("performanceGroupDescription", "Description not found");
				rec.commit();
			}
		}
	}

	private void addLabel2Panel(Label label, Panel p){
		label.setAutoWidth(true);
		label.setCls("table-cell");
		p.add(label);
	}

	private void addTextField2Panel(TextField textfld, Panel p){
		textfld.setCtCls("table-cell");
		p.add(textfld);
	}

	// last modified: brian tse
	public void getAppraisalResultByPage(String jobNumber, int pageNumber){
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPackageRepository().getAppraisalResultByPage(jobNumber, pageNumber, new AsyncCallback<PerformanceAppraisalPaginationWrapper>(){
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onSuccess(PerformanceAppraisalPaginationWrapper wrapper) {
				populateGridByPage(wrapper);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
	}

	// last modified: brian tse
	public void searchAppraisalResult(String jobNumber, Integer sequenceNumber, String appraisalType, String groupCode, Integer vendorNumber, Integer subcontractNumber, String status, int pageNumber){
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.SEARCHING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPackageRepository().getSearchAppraisalResult(globalSectionController.getUser().getUsername(), jobNumber, sequenceNumber, appraisalType, groupCode, vendorNumber, subcontractNumber, status, pageNumber, new AsyncCallback<PerformanceAppraisalPaginationWrapper>(){
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onSuccess(PerformanceAppraisalPaginationWrapper wrapper) {
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
				populateGridByPage(wrapper);
			}

		});
	}

	/**
	 * @author irischau
	 * added on Apr 02, 2014
	 * avoid preload data at globalSectionController
	 */	
	private void obtainPerformaceAppraisalGroup(){
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getUnitRepository().getAppraisalPerformanceGroupMap(new AsyncCallback<Map<String, String>>() {
			public void onSuccess(Map<String, String> map) {
				storePerformanceGroup.removeAll();
				if(map==null)return;
				appraisalPerformanceGroupMap = map;
				RecordDef performanceGroupRecordDef = new RecordDef(new FieldDef[] { new StringFieldDef("performanceGroupValue"), 
																					new StringFieldDef("performanceGroupDisplay")});
				
				performanceGroups = new String[map.size()][2];
				performanceGroups[0][0]=null;
				performanceGroups[0][1]="ALL";
				storePerformanceGroup.add(performanceGroupRecordDef.createRecord(performanceGroups[0]));
				
				String[] performanceGroupKeys = map.keySet().toArray(new String[0]);
				Arrays.sort(performanceGroupKeys);
				
				for(int i = 0; i < performanceGroupKeys.length; i++) {
					performanceGroups[i][0] = (String) performanceGroupKeys[i];
					performanceGroups[i][1] =  performanceGroupKeys[i] + ": " + map.get(performanceGroupKeys[i]);
					storePerformanceGroup.add(performanceGroupRecordDef.createRecord(performanceGroups[i]));
				}
				
				performanceGroupComboBox.setListWidth(200);
				storePerformanceGroup.commitChanges();
			}
			
			public void onFailure(Throwable e) {
				MessageBox.alert("Database access exception: " + e);
				GlobalParameter.setPerformanceGroupMap(new HashMap<String, String>());
			}
		});
	}

}
