package com.gammon.qs.client.ui.panel.mainSection;

import java.util.List;

import com.gammon.qs.application.User;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.repository.PackageRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.contraChargeEnquiry.ContraChargeSearchingCriteriaWrapper;
import com.gammon.qs.wrapper.contraChargeEnquiry.ContraChargePaginationWrapper;
import com.gammon.qs.wrapper.contraChargeEnquiry.ContraChargeEnquiryWrapper;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
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
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListener;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowNumberingColumnConfig;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

/**
 * @author xethhung
 * Enhancement for Enquiring Contra Charge 
 * Jun 25, 2015
 */
public class ContraChargeEnquiryMainPanel extends Panel{
	private static final String MAIN_PANEL_ID ="ContraChargeEnquiryMainPanel";
	
	private Store dataStore;
	
	private final String[] colLineType = new String[]{"Line Type","LineType"};
	private final String[] colBQItem = new String[]{"BQ Item","BQItem"};
	private final String[] colBQQuantity = new String[]{"BQ Quantity","BQQuantity"};
	private final String[] colSCRate = new String[]{"SC Rate","SCRate"};
	private final String[] colTotalAmount = new String[]{"Total Amount","TotalAmount"};
	private final String[] colPostedCertifiedAmount = new String[]{"Posted Certified Amount","PostedCertifiedAmount"};
	private final String[] colCumulativeCertifiedAmount = new String[]{"Cumulative Certified Amount","CumulativeCertifiedAmount"};
	private final String[] colMovementAmount = new String[]{"Movement Amount","MovementAmount"};
	private final String[] colObjectCode = new String[]{"Object Code","ObjectCode"};
	private final String[] colSubsidiaryCode = new String[]{"Subsidiary Code","SubsidiaryCode"};
	private final String[] colUnit = new String[]{"Unit","Unit"};
	private final String[] colSCNo = new String[]{"Subcontract No.","SCNo"};
	private final String[] colChargedBySubcontractor = new String[]{"Charged By Subcontractor","ChargedBySubcontractor"};
	private final String[] colDescription = new String[]{"Description","Description"};
	private final String[] colRemark = new String[]{"Remark","Remark"};

	
	
	private RecordDef recordDef = new RecordDef(
			new FieldDef[] {
					new StringFieldDef(colLineType[1]),
					new StringFieldDef(colBQItem[1]),
					new FloatFieldDef(colBQQuantity[1]),
					new FloatFieldDef(colSCRate[1]),
					new FloatFieldDef(colTotalAmount[1]),
					new FloatFieldDef(colPostedCertifiedAmount[1]),
					new FloatFieldDef(colCumulativeCertifiedAmount[1]),
					new FloatFieldDef(colMovementAmount[1]),
					new StringFieldDef(colObjectCode[1]),
					new StringFieldDef(colSubsidiaryCode[1]),
					new StringFieldDef(colUnit[1]),
					new StringFieldDef(colSCNo[1]),
					new StringFieldDef(colChargedBySubcontractor[1]),
					new StringFieldDef(colDescription[1]),
					new StringFieldDef(colRemark[1])
			}	
	);


	// used to check access rights
	@SuppressWarnings("unused")
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	@SuppressWarnings("unused")
	private List<String> accessRightsListForUpdate;

	private List<String> accessRightsList;
	
	private PackageRepositoryRemoteAsync packageRepository;
	
	private NumberField jobNumberNField;
	private TextField BQItemTField;
	private TextField Description;	
	private ComboBox lineTypeComboBox;
	private NumberField objectCodeNField;
	private NumberField subsidiaryCodeNField;
	private NumberField subcontractNumberCodeNField;
	private NumberField subcontractorNumberCodeNField;
	
	private ToolbarButton tempToolbarButton;
	private Item tempExcelButton;
	private Item tempPDFButton;
	
	private ToolbarTextItem totalNoOfRecordsValueLabel;

	private EditorGridPanel resultEditorGridPanel;

	@SuppressWarnings("unused")
	private User user;
	
	private PaginationToolbar paginationToolbar;
	
	private GlobalMessageTicket globalMessageTicket;
	private GlobalSectionController globalSectionController;


	public ContraChargeEnquiryMainPanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		globalMessageTicket = new GlobalMessageTicket();
		user = globalSectionController.getUser();

		userAccessRightsRepository = globalSectionController.getUserAccessRightsRepository();
		packageRepository = globalSectionController.getPackageRepository();
		
		setupUI();
	
	}
	private void setupUI() {
		setId(MAIN_PANEL_ID);		

		setLayout(new RowLayout());
		setPaddings(0);
		
		accessRightsList = null;
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getUserAccessRightsRepository().getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010213_SUBCONTRACT_ENQUIRY_MAINPANEL, new AsyncCallback<List<String>>(){
			public void onSuccess(List<String> accessRightsReturned) {
				accessRightsList = accessRightsReturned;
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}

			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
				UIUtil.alert(e.getMessage());
			}
		});

		
		setupSearchPanel();
		setupGridPanell();
		
		
	}
	
	private void setupSearchPanel() {
		Panel searchPanel = new Panel();
		searchPanel.setPaddings(2);
		searchPanel.setFrame(true);
		
		//search Panel
		searchPanel.setPaddings(2);
		searchPanel.setHeight(85);
		searchPanel.setLayout(new TableLayout(10));
		
		FieldListener searchListener = new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){
				if(e.getKey() == EventObject.ENTER){
					search();
				}
			}
		};	
		
		
		Label jobNumberLabel = new Label("Job No. ");
		jobNumberLabel.setCtCls("table-cell");
		searchPanel.add(jobNumberLabel);
		jobNumberNField = FieldFactory.createNumberField();
		jobNumberNField.setAllowBlank(true);
		jobNumberNField.setDecimalPrecision(0);
		jobNumberNField.setCtCls("table-cell");
		jobNumberNField.setWidth(120);
		searchPanel.add(jobNumberNField);
		jobNumberNField.setValue(this.globalSectionController.getJob().getJobNumber());
		jobNumberNField.addListener(searchListener);

		
		Label typeLabel = new Label("Line Type ");
		typeLabel.setCtCls("table-cell");		
		searchPanel.add(typeLabel);
		lineTypeComboBox = FieldFactory.createComboBox();
		lineTypeComboBox.setCtCls("table-cell");
		lineTypeComboBox.setWidth(120);
		searchPanel.add(lineTypeComboBox);
		lineTypeComboBox.addListener(searchListener);
		
		setStaticComboBoxOption(lineTypeComboBox, GlobalParameter.getContraChargeLineType(true));

		Label BQItemLabel = new Label("BQ Item ");
		BQItemLabel.setCtCls("table-cell");
		searchPanel.add(BQItemLabel);
		BQItemTField = FieldFactory.createTextField();
		BQItemTField.setAllowBlank(true);
		BQItemTField.setCtCls("table-cell");
		BQItemTField.setWidth(120);
		searchPanel.add(BQItemTField);
		BQItemTField.addListener(searchListener);

		Label DescriptionLabel = new Label("Description ");
		DescriptionLabel.setCtCls("table-cell");
		searchPanel.add(DescriptionLabel);
		Description = FieldFactory.createTextField();
		Description.setAllowBlank(true);
		Description.setCtCls("table-cell");
		Description.setWidth(120);
		searchPanel.add(Description);
		Description.addListener(searchListener);
		
		searchPanel.add(new Label(""));
		searchPanel.add(new Label(""));
		
		Label objctCodeLabel = new Label("Object Code");
		objctCodeLabel.setCtCls("table-cell");		
		searchPanel.add(objctCodeLabel);
		objectCodeNField = FieldFactory.createNumberField();
		objectCodeNField.setCtCls("table-cell");
		objectCodeNField.setWidth(120);
		objectCodeNField.setAllowBlank(true);
		objectCodeNField.setDecimalPrecision(0);
		searchPanel.add(objectCodeNField);
		objectCodeNField.addListener(searchListener);

		Label subsidiaryCodeLabel = new Label("Subsidiary Code ");
		subsidiaryCodeLabel.setCtCls("table-cell");		
		searchPanel.add(subsidiaryCodeLabel);
		subsidiaryCodeNField = FieldFactory.createNumberField();
		subsidiaryCodeNField.setAllowBlank(true);
		subsidiaryCodeNField.setDecimalPrecision(0);
		subsidiaryCodeNField.setCtCls("table-cell");
		subsidiaryCodeNField.setWidth(120);
		searchPanel.add(subsidiaryCodeNField);
		subsidiaryCodeNField.addListener(searchListener);

		Label subcontractNumberCodeLabel = new Label("Subcontract No. ");
		subcontractNumberCodeLabel.setCtCls("table-cell");		
		searchPanel.add(subcontractNumberCodeLabel);
		subcontractNumberCodeNField = FieldFactory.createNumberField();
		subcontractNumberCodeNField.setAllowBlank(true);
		subcontractNumberCodeNField.setDecimalPrecision(0);
		subcontractNumberCodeNField.setCtCls("table-cell");
		subcontractNumberCodeNField.setWidth(120);
		searchPanel.add(subcontractNumberCodeNField);
		subcontractNumberCodeNField.addListener(searchListener);

		Label subcontractorNumberCodeLabel = new Label("Subcontractor No. ");
		subcontractorNumberCodeLabel.setCtCls("table-cell");		
		searchPanel.add(subcontractorNumberCodeLabel);
		subcontractorNumberCodeNField = FieldFactory.createNumberField();
		subcontractorNumberCodeNField.setAllowBlank(true);
		subcontractorNumberCodeNField.setDecimalPrecision(0);
		subcontractorNumberCodeNField.setCtCls("table-cell");
		subcontractorNumberCodeNField.setWidth(120);
		searchPanel.add(subcontractorNumberCodeNField);
		subcontractorNumberCodeNField.addListener(searchListener);

		Button searchButton = new Button("Search");
		searchButton.setCtCls("right-align table-cell");
	    searchButton.setIconCls("find-icon");
	    searchButton.addListener(new ButtonListenerAdapter(){
	    	public void onClick(Button button, EventObject e) {
	    		globalMessageTicket.refresh();
	    		search();
	    	};
	    });
	    searchPanel.add(new Label(""));
	    searchPanel.add(searchButton);
		add(searchPanel);
	}
	
	
	
	private void setupToolbar() {
		Toolbar toolbar = new Toolbar();
		
		tempExcelButton = new Item("Export to Excel");
		tempExcelButton.setIconCls("excel-icon");
		tempExcelButton.addListener(new BaseItemListenerAdapter(){

			public void onClick(BaseItem item, EventObject e) {
				ContraChargeSearchingCriteriaWrapper criteria = new ContraChargeSearchingCriteriaWrapper();
				if(!checkAccessibility()){
					MessageBox.alert("No access right!");
					return;
				}

				if(!getSearchingCriteria(criteria)){
					MessageBox.alert("Job No. should not be empty, please fill in the job No.");
					return;
				}	
				globalMessageTicket.refresh();
				com.google.gwt.user.client.Window.open(GlobalParameter.CONTRA_CHARGE_ENQUIRY_REPORT_EXCEL_URL
												+ "?jobNo="+criteria.getJobNumber()
												+ "&lineType="+criteria.getLineType()
												+ "&BQItem="+criteria.getBQItem()
												+ "&description="+criteria.getDescription()
												+ "&objectCode="+criteria.getObjectCode()
												+ "&subsidiaryCode="+criteria.getSubsidiaryCode()
												+ "&subcontractNumber="+criteria.getSubcontractNumber()
												+ "&subcontractorNumber="+criteria.getSubcontractorNumber()
												, "_blank", "");

			}
		});

		tempPDFButton = new Item("Export to PDF");
		tempPDFButton.setIconCls("pdf-icon");
		tempPDFButton.addListener(new BaseItemListenerAdapter(){

			public void onClick(BaseItem item, EventObject e) {
				ContraChargeSearchingCriteriaWrapper criteria = new ContraChargeSearchingCriteriaWrapper();
				
				if(!checkAccessibility()){
					MessageBox.alert("No access right!");
					return;
				}
				
				if(!getSearchingCriteria(criteria)){
					MessageBox.alert("Job No. should not be empty, please fill in the job NO.");
					return;
				}	
				globalMessageTicket.refresh();
				com.google.gwt.user.client.Window.open(GlobalParameter.CONTRA_CHARGE_ENQUIRY_REPORT_PDF_URL
												+ "?jobNo="+criteria.getJobNumber()
												+ "&lineType="+criteria.getLineType()
												+ "&BQItem="+criteria.getBQItem()
												+ "&description="+criteria.getDescription()
												+ "&objectCode="+criteria.getObjectCode()
												+ "&subsidiaryCode="+criteria.getSubsidiaryCode()
												+ "&subcontractNumber="+criteria.getSubcontractNumber()
												+ "&subcontractorNumber="+criteria.getSubcontractorNumber()
												, "_blank", "");

			}
		});
		Menu tempMenu = new Menu();
		tempMenu.addItem(tempExcelButton);
		tempMenu.addItem(tempPDFButton);
		
		tempToolbarButton = new ToolbarButton("Contra Charge Report");
		tempToolbarButton.setMenu(tempMenu);
		tempToolbarButton.setIconCls("menu-show-icon");

		toolbar.addButton(tempToolbarButton);
		resultEditorGridPanel.setTopToolbar(toolbar);
	}
	
	private void setupGridPanell() {
		resultEditorGridPanel =  new EditorGridPanel();
		resultEditorGridPanel.setClicksToEdit(1);

		ColumnConfig colLineTypeColumnConfig = new ColumnConfig(colLineType[0],colLineType[1], 60, false);
		ColumnConfig colBQItemColumnConfig = new ColumnConfig(colBQItem[0],colBQItem[1], 100, false);
		ColumnConfig colBQQuantityColumnConfig = new ColumnConfig(colBQQuantity[0],colBQQuantity[1], 80, false);
		ColumnConfig colSCRateColumnConfig = new ColumnConfig(colSCRate[0],colSCRate[1], 70, false);
		ColumnConfig colTotalAmountColumnConfig = new ColumnConfig(colTotalAmount[0],colTotalAmount[1], 100, false);
		ColumnConfig colPostedCertifiedAmountColumnConfig = new ColumnConfig(colPostedCertifiedAmount[0],colPostedCertifiedAmount[1], 130, false);
		ColumnConfig colCumulativeCertifiedAmountColumnConfig = new ColumnConfig(colCumulativeCertifiedAmount[0],colCumulativeCertifiedAmount[1], 145, false);
		ColumnConfig colMovementAmountColumnConfig = new ColumnConfig(colMovementAmount[0],colMovementAmount[1], 100, false);
		ColumnConfig colObjectCodeColumnConfig = new ColumnConfig(colObjectCode[0],colObjectCode[1], 70, false);
		ColumnConfig colSubsidiaryCodeColumnConfig = new ColumnConfig(colSubsidiaryCode[0],colSubsidiaryCode[1], 92, false);
		ColumnConfig colUnitColumnConfig = new ColumnConfig(colUnit[0],colUnit[1], 29, false);
		ColumnConfig colSCNoColumnConfig = new ColumnConfig(colSCNo[0],colSCNo[1], 100, false);
		ColumnConfig colChargedBySubcontractorColumnConfig = new ColumnConfig(colChargedBySubcontractor[0],colChargedBySubcontractor[1], 145, false);
		ColumnConfig colRemarkColumnConfig = new ColumnConfig(colRemark[0],colRemark[1], 250, false);
		ColumnConfig colDescriptionColumnConfig = new ColumnConfig(colDescription[0],colDescription[1], 250, false);
		
		Renderer quantityRenderer = new QuantityRenderer(globalSectionController.getUser()){
			@Override
			public String render(String value)
			{
				if(value == null)
					return "";
				
				try{
					value = value.replace(",", "").trim();
					NumberFormat nf = NumberFormat.getFormat("#,##0.0000");
					return nf.format(Double.parseDouble(value));	
				}
				catch(Exception e){
					return "";
				}	
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

		Renderer rateRenderer = new RateRenderer(globalSectionController.getUser()){
			@Override
			public String render(String value)
			{
				if(value == null)
					return "";
				
				try{
					value = value.replace(",", "").trim();
					NumberFormat nf = NumberFormat.getFormat("#,##0.0000");
					return nf.format(Double.parseDouble(value));	
				}
				catch(Exception e){
					return "";
				}	
			}
		};
		
		colBQQuantityColumnConfig.setRenderer(quantityRenderer);
		colBQQuantityColumnConfig.setAlign(TextAlign.RIGHT);
		colSCRateColumnConfig.setRenderer(rateRenderer);
		colSCRateColumnConfig.setAlign(TextAlign.RIGHT);
		colTotalAmountColumnConfig.setRenderer(amountRenderer);
		colTotalAmountColumnConfig.setAlign(TextAlign.RIGHT);
		colPostedCertifiedAmountColumnConfig.setRenderer(amountRenderer);
		colPostedCertifiedAmountColumnConfig.setAlign(TextAlign.RIGHT);
		colCumulativeCertifiedAmountColumnConfig.setRenderer(amountRenderer);
		colCumulativeCertifiedAmountColumnConfig.setAlign(TextAlign.RIGHT);
		colMovementAmountColumnConfig.setRenderer(amountRenderer);
		colMovementAmountColumnConfig.setAlign(TextAlign.RIGHT);
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(recordDef);
		this.dataStore = new Store(proxy, reader);
		this.dataStore.load();
		resultEditorGridPanel.setStore(this.dataStore);
		
		BaseColumnConfig[] columns = new BaseColumnConfig[] {
			new RowNumberingColumnConfig(),
			colLineTypeColumnConfig,
			colBQItemColumnConfig,
			colBQQuantityColumnConfig,
			colSCRateColumnConfig,
			colTotalAmountColumnConfig,
			colPostedCertifiedAmountColumnConfig,
			colCumulativeCertifiedAmountColumnConfig,
			colMovementAmountColumnConfig,
			colObjectCodeColumnConfig,
			colSubsidiaryCodeColumnConfig,
			colUnitColumnConfig,
			colSCNoColumnConfig,
			colChargedBySubcontractorColumnConfig,
			colDescriptionColumnConfig,
			colRemarkColumnConfig
		};

		resultEditorGridPanel.setColumnModel(new ColumnModel(columns));
		GridView view = new CustomizedGridView();
		view.setAutoFill(false);
		view.setForceFit(false);
		view.setEnableRowBody(true);
		resultEditorGridPanel.setView(view);
		setupToolbar();
		
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(int pageNum){
				goToContraChargeListPage(pageNum);
			}
		});
		resultEditorGridPanel.setBottomToolbar(paginationToolbar);
		paginationToolbar.setTotalPage(1);
		paginationToolbar.setCurrentPage(0);
		
		paginationToolbar.addFill();
		totalNoOfRecordsValueLabel = new ToolbarTextItem("<b>Total Number of Records: </b>");
		paginationToolbar.addItem(totalNoOfRecordsValueLabel);
		
		add(resultEditorGridPanel);
	}
	
	@SuppressWarnings("rawtypes")
	private void goToContraChargeListPage(int pageNum){
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.obtainContraChargeByPage(pageNum, new AsyncCallback<ContraChargePaginationWrapper>(){
			@SuppressWarnings({ "unchecked" })
			public void onSuccess(ContraChargePaginationWrapper wrapper) {
				updatePaginationToolBar(wrapper);
				setNumberOfRecord(wrapper);
				populateGrid(wrapper.getCurrentPageContentList());
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
	}
	
	public void populateGrid(List<ContraChargeEnquiryWrapper> result) {
		this.dataStore.removeAll();
		for(ContraChargeEnquiryWrapper wrapper : result){
			this.dataStore.add(recordDef.createRecord(new Object[] {
						wrapper.getLineType(),
						wrapper.getBillItem(),
						wrapper.getBQQuantity(),
						wrapper.getScRate(),
						wrapper.getTotalAmount(),
						wrapper.getTotalPostedCertAmt(),
						wrapper.getCumCertifiedQuantity(),
						wrapper.getTotalIVAmt(),
						wrapper.getObjectCode(),
						wrapper.getSubsidiaryCode(),
						wrapper.getUnit(),
						wrapper.getSubcontractNumber(),
						wrapper.getChargedBySubcontractor(),
						wrapper.getDescription(),
						wrapper.getRemark()
						}
					)
				);
		}
	}

	@SuppressWarnings("rawtypes")
	private void search(){
		UIUtil.maskPanelById(MAIN_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		if(!checkAccessibility()){
			MessageBox.alert("No access right!");
			return;
		}
			 
		ContraChargeSearchingCriteriaWrapper criteria = new ContraChargeSearchingCriteriaWrapper();
		if(!getSearchingCriteria(criteria)){
			MessageBox.alert("Job No. should not be empty, please fill in the job No.");
			UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			return;
		}	
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.obtainContraChargeListPaginationWrapper(criteria.getJobNumber(), criteria.getLineType(), criteria.getBQItem(), criteria.getDescription(), criteria.getObjectCode(), criteria.getSubsidiaryCode(), criteria.getSubcontractNumber(), criteria.getSubcontractorNumber(), new AsyncCallback<ContraChargePaginationWrapper>(){
			@SuppressWarnings("unchecked")
			public void onSuccess(ContraChargePaginationWrapper wrapper) {
				updatePaginationToolBar(wrapper);
				goToContraChargeListPage(0);
				setNumberOfRecord(wrapper);
				populateGrid(wrapper.getCurrentPageContentList());
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(MAIN_PANEL_ID);
			}
		});
	}

	
	@SuppressWarnings("rawtypes")
	private void setNumberOfRecord(ContraChargePaginationWrapper wrapper){
		if (wrapper == null) {
			totalNoOfRecordsValueLabel.setText("<b>Total Number of Records: 0 </b>");
			return;
		}
		totalNoOfRecordsValueLabel.setText("<b>Total Number of Records: " + wrapper.getTotalRecords() + "</b>");
	}
	
	@SuppressWarnings("rawtypes")
	private void updatePaginationToolBar(ContraChargePaginationWrapper wrapper){
		paginationToolbar.setCurrentPage(wrapper.getCurrentPage());
		paginationToolbar.setTotalPage(wrapper.getTotalPage());
	}
	
	private void setStaticComboBoxOption(ComboBox combobox, String[][] valuePair){
		final String valueField = "VALUE";
		final String displayField = "DISPLAY";
		String[] header = new String[]{valueField,displayField};	
		Store store = new SimpleStore(header, valuePair);

		if(combobox != null){
			combobox.setStore(store);
			combobox.setDisplayField(displayField);
			combobox.setValueField(valueField);
			combobox.setValue(valuePair[0][0]);
			combobox.setEmptyText(valuePair[0][1]);
			combobox.setForceSelection(true);
		}
	}
	
	private boolean getSearchingCriteria(ContraChargeSearchingCriteriaWrapper searchingWrapper){
		@SuppressWarnings("unused")
		String[] tempArr = new String[8];
		String jobNumber = jobNumberNField.getText().trim();
		if(jobNumber == null || jobNumber.equals("") )
			return false;
		searchingWrapper.setJobNumber(jobNumber);
		String lineType = lineTypeComboBox.getText().trim();
		searchingWrapper.setLineType(lineType);
		String BQItem = BQItemTField.getText().trim();
		searchingWrapper.setBQItem(BQItem);
		String description = this.Description.getText().trim();
		searchingWrapper.setDescription(description);
		String objectCode = this.objectCodeNField.getText().trim();
		searchingWrapper.setObjectCode(objectCode);
		String subsidiaryCode = this.subsidiaryCodeNField.getText().trim();
		searchingWrapper.setSubsidiaryCode(subsidiaryCode);
		String subcontractNumber = this.subcontractNumberCodeNField.getText().trim();
		searchingWrapper.setSubcontractNumber(subcontractNumber);
		String subcontractorNumber = this.subcontractorNumberCodeNField.getText().trim();
		searchingWrapper.setSubcontractorNumber(subcontractorNumber);
		
		return true;
	}
	
	private boolean checkAccessibility(){
		if(accessRightsList == null){
			UIUtil.alert("Access violation");
			return false;
		}
		else if(accessRightsList.contains("READ")){
			return true;
		}
		else
			return false;
	}
}
