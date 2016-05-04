package com.gammon.qs.client.ui.panel.detailSection;

import java.util.List;

import com.gammon.qs.application.GeneralPreferencesKey;
import com.gammon.qs.client.controller.DetailSectionController;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.PackageRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.panel.PreferenceSetting;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.client.ui.window.detailSection.AddSCAddendumWindow;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.SCDetailsWrapper;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.Connection;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Form;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FormListenerAdapter;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.TableLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

@SuppressWarnings("unchecked")
public class SCDetailDetailPanel extends GridPanel  implements PreferenceSetting{
	private String PANEL_ID = "scDetailsEditorGridPanel";
	private Store dataStore;
	private ColumnConfig[] columns;



	private String screenName = "sc-details-editor-grid-panel";

	//parameter 
	private String packageType = "";
	private String scLineType ="";

	private String sequenceNumber ="";
	private String billItem ="";
	private String resourceNumber ="";
	private String subsidiaryCode ="";
	private String objectCode ="";

	private String submittedAddendum="";
	
	Job job;
	String packageNo;


	FormPanel uploadPanel;
	Window promptWindow ;

	//Remote service
	private PackageRepositoryRemoteAsync packageRepository;
	
	//Controllers
	private GlobalSectionController globalSectionController;
	private DetailSectionController detailSectionController;

	//pagination
	private int currentPage =0;
	private int totalPage =0;
	private PaginationToolbar paginationToolbar;

	//toolbar
	private Toolbar toolbar = new Toolbar();
	
	private ToolbarButton addendumToolbarButton;
	private Item addAddendumButton;
	private Item updateAddendumButton;
	private Item submitAddendumButton;
	private Item viewAddendumButton;
	
	private ToolbarButton workDoneToolbarButton;
	private Item scDetailSearchAndMassUpdateCumWDQtyButton ;
	
	private ToolbarButton certificateToolbarButton;
	private Item scDetailSearchAndMassUpdateCumCertifiedQtyButton ;
	
	private ToolbarButton excelToolbarButton;
	private Item uploadSCXlsButton;
	private Item downloadSCXlsButton;
	
	private ToolbarButton openSCAttachmentButton;
	private ToolbarButton refreshButton;
	private ToolbarButton recalculateIVButton;

	
	private GlobalMessageTicket globalMessageTicket;

	private final RecordDef scDetailsRecordDef = new RecordDef(
			new FieldDef[] {
					new StringFieldDef("jobNumber"),
					new StringFieldDef("sequenceNo"),
					new StringFieldDef("resourceNo"),
					new StringFieldDef("bqItem"),
					new StringFieldDef("description"),
					new StringFieldDef("bqQuantity"),
					new StringFieldDef("toBeApprovedQuantity"),
					new StringFieldDef("ecaRate"),
					new StringFieldDef("scRate"),
					new StringFieldDef("toBeApprovedRate"),

					new StringFieldDef("totalAmount"),
					new StringFieldDef("toBeApprovedAmount"),
					new StringFieldDef("objectCode"),
					new StringFieldDef("subsidiaryCode"),
					new StringFieldDef("lineType"),
					new StringFieldDef("approved"),
					new StringFieldDef("unit"),
					new StringFieldDef("remark"),
					new StringFieldDef("ccSCNo"),
					new StringFieldDef("postedWorkDoneQty"),
					new StringFieldDef("postedWorkDoneAmt"),
					new StringFieldDef("currentWorkDoneQty"),
					new StringFieldDef("currentWorkDoneAmt"),
					new StringFieldDef("workDoneMovementAmt"),
					new StringFieldDef("ivAmount"),
					new StringFieldDef("postedCertifiedQty"),
					new StringFieldDef("postedCertifiedAmt"),
					new StringFieldDef("currentCertifiedQty"),
					new StringFieldDef("currentCertifiedAmt"),
					new StringFieldDef("certifiedMovementAmt"),
					new StringFieldDef("provisionAmt"),
					new StringFieldDef("projProvisionAmt"),

					new StringFieldDef("balanceType"),
					new StringFieldDef("packageNo") ,
					// Source Type
					new StringFieldDef("sourceType"),
					new StringFieldDef("oldCurrentWorkDoneAmt")
			}
	);
	// used to check access rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;

	
	
	
	public SCDetailDetailPanel(final DetailSectionController detailSectionController) {
		super();

		globalMessageTicket = new GlobalMessageTicket();
		this.detailSectionController = detailSectionController;
		globalSectionController = detailSectionController.getGlobalSectionController();
		
		// Repository initialization
		userAccessRightsRepository = globalSectionController.getUserAccessRightsRepository();
		packageRepository = globalSectionController.getPackageRepository();		
		
		// Data initialization
		job = globalSectionController.getJob();
		packageNo = globalSectionController.getSelectedPackageNumber();
		
		globalSectionController.getPackageEditorFormPanel().getStore();
		
		setupUI();
	}
	
	private void setupUI(){
		setId(PANEL_ID);
		addGridRowListener(new GridRowListenerAdapter() {
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				Record curRecord = dataStore.getAt(rowIndex);
				
				sequenceNumber = (curRecord.getAsObject("sequenceNo") != null ? curRecord.getAsString("sequenceNo").trim() : "0");
				billItem = (curRecord.getAsObject("bqItem") != null ? curRecord.getAsString("bqItem").trim() : "0");
				resourceNumber = (curRecord.getAsObject("resourceNo") != null ? curRecord.getAsString("resourceNo").trim() : "0");
				subsidiaryCode = (curRecord.getAsObject("subsidiaryCode") != null ? curRecord.getAsString("subsidiaryCode").trim() : "");
				objectCode = (curRecord.getAsObject("objectCode") != null ? curRecord.getAsString("objectCode").trim() : "0");
			}
		});

		setTrackMouseOver(true);
		setBorder(false);
		setAutoScroll(true);

		//Renderer Setup
		Renderer customRenderer = new Renderer() {
			public String render(Object value, CellMetadata cellMetadata, Record record,
					int rowIndex, int colNum, Store store) {

				String strValue = (String) value;
				strValue = strValue.replaceAll(",", "");
				double raw = Double.parseDouble(strValue.trim());
				StringBuffer formatString = new StringBuffer();
				formatString.append("#,##0");

				int decimalPlaces = Integer.parseInt((String) detailSectionController.getUser().getGeneralPreferences().get(GeneralPreferencesKey.AMOUNT_DECIMAL_PLACES));
				if (decimalPlaces > 0)
					formatString.append(".");

				for (int i = 0; i < decimalPlaces; i++) {
					formatString.append("0");
				}

				NumberFormat format = NumberFormat.getFormat(formatString.toString());

				if ("SUB TOTAL:".equals(record.getAsString("description"))
						|| "GRAND TOTAL:".equals(record.getAsString("description"))) {
					return "<b>" + format.format(raw) + "</b>";
				}
				return format.format(raw);
			
			}
		};
		

		Renderer quantityRenderer = new QuantityRenderer(detailSectionController.getUser());
		Renderer rateRenderer = new RateRenderer(detailSectionController.getUser());

		//Columns setup
		ColumnConfig sequenceNoColumn = new ColumnConfig("Sequence No", "sequenceNo", 40, false);
		ColumnConfig resourceNoColumn = new ColumnConfig("Resource No", "resourceNo", 40, false);
		ColumnConfig bqItemColumn = new ColumnConfig("BQ Item", "bqItem", 90, false);
		ColumnConfig descriptionColumn = new ColumnConfig("Description", "description", 200, false);
		
		descriptionColumn.setRenderer(new Renderer() {
			public String render(Object value, CellMetadata cellMetadata, Record record,
					int rowIndex, int colNum, Store store) {

				if ("SUB TOTAL:".equals(record.getAsString("description"))
						|| "GRAND TOTAL:".equals(record.getAsString("description"))) {
					return "<b>" + (String) value + "</b>";
				}
				return (String) value;

			}
		});
	
		ColumnConfig bqQuantityColumn = new ColumnConfig("BQ Qty", "bqQuantity", 100, false);
		bqQuantityColumn.setRenderer(quantityRenderer);
		bqQuantityColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig toBeApprovedQuantityColumn = new ColumnConfig("To Be Approved Qty", "toBeApprovedQuantity", 100, false);
		toBeApprovedQuantityColumn.setRenderer(quantityRenderer);
		toBeApprovedQuantityColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig ecaRateColumn = new ColumnConfig("Cost Rate", "ecaRate", 100, false);
		ecaRateColumn.setRenderer(rateRenderer);
		ecaRateColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig scRateColumn = new ColumnConfig("SC Rate", "scRate", 100, false);
		scRateColumn.setRenderer(rateRenderer);
		scRateColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig toBeApprovedRateColumn = new ColumnConfig("To Be Approved Rate", "toBeApprovedRate", 100, false);
		toBeApprovedRateColumn.setRenderer(rateRenderer);
		toBeApprovedRateColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig totalAmountColumn = new ColumnConfig("Total Amount", "totalAmount", 100, false);
		totalAmountColumn.setAlign(TextAlign.RIGHT);
		totalAmountColumn.setRenderer( customRenderer);

		ColumnConfig toBeApprovedAmountColumn = new ColumnConfig("To Be Approved Amount", "toBeApprovedAmount", 100, false);
		toBeApprovedAmountColumn.setRenderer(customRenderer);
		toBeApprovedAmountColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig objectCodeColumn = new ColumnConfig("Object Code", "objectCode", 70, false);
		ColumnConfig subsidiaryCodeColumn = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 90, false);
		ColumnConfig lineTypeColumn = new ColumnConfig("Type", "lineType", 40, false);
		ColumnConfig approvedColumn = new ColumnConfig("Approved", "approved", 70, false);
		approvedColumn.setRenderer(new Renderer() {
			// change font color to red if the status is not "Approved" 
			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				String htmlAttr = "";

				if (value != null && "Not Approved".equals(value)) {
					htmlAttr = "style='color:"+GlobalParameter.RED_COLOR+";'";
					cellMetadata.setHtmlAttribute(htmlAttr);
				}
				if (value != null && "Suspended".equals(value)) {
					htmlAttr = "style='color:"+GlobalParameter.GREY_COLOR+";'";
					cellMetadata.setHtmlAttribute(htmlAttr);
				}
				return (value != null ? value.toString() : null);
			}
		});

		ColumnConfig unitColumn = new ColumnConfig("Unit", "unit", 40, false);
		ColumnConfig remarkColumn = new ColumnConfig("Remark", "remark", 100, false);
		ColumnConfig ccSCNoColumn = new ColumnConfig("Contra Charge SC No", "ccSCNo", 100, false);

		ColumnConfig postedWorkDoneQtyColumn = new ColumnConfig("Posted Work Done Qty", "postedWorkDoneQty", 100, false);
		postedWorkDoneQtyColumn.setRenderer(quantityRenderer);
		postedWorkDoneQtyColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig postedWorkDoneAmtColumn = new ColumnConfig("Posted Work Done Amt", "postedWorkDoneAmt", 100, false);
		postedWorkDoneAmtColumn.setRenderer(customRenderer);
		postedWorkDoneAmtColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig currentWorkDoneQtyColumn = new ColumnConfig("Cum Work Done Qty", "currentWorkDoneQty", 100, false);
		currentWorkDoneQtyColumn.setRenderer(quantityRenderer);
		currentWorkDoneQtyColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig currentWorkDoneAmtColumn = new ColumnConfig("Cum Work Done Amt", "currentWorkDoneAmt", 100, false);
		currentWorkDoneAmtColumn.setRenderer(customRenderer);
		currentWorkDoneAmtColumn.setAlign(TextAlign.RIGHT);
		
		ColumnConfig workDoneMovementColumn = new ColumnConfig("Work Done Movement", "workDoneMovementAmt", 100, false);
		workDoneMovementColumn.setRenderer(customRenderer);
		workDoneMovementColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig postedCertifiedQtyColumn = new ColumnConfig("Posted Certified Qty", "postedCertifiedQty", 100, false);
		postedCertifiedQtyColumn.setRenderer(quantityRenderer);
		postedCertifiedQtyColumn.setAlign(TextAlign.RIGHT);		

		ColumnConfig currentCertifiedQtyColumn = new ColumnConfig("Cum Certified Qty", "currentCertifiedQty", 100, false);
		currentCertifiedQtyColumn.setRenderer(quantityRenderer);
		currentCertifiedQtyColumn.setAlign(TextAlign.RIGHT);	

		ColumnConfig postedCertifiedAmtColumn = new ColumnConfig("Posted Certified Amt", "postedCertifiedAmt", 100, false);
		postedCertifiedAmtColumn.setRenderer(customRenderer);
		postedCertifiedAmtColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig currentCertifiedAmtColumn  = new ColumnConfig("Cum Certified Amt", "currentCertifiedAmt", 100, false);
		currentCertifiedAmtColumn.setRenderer(customRenderer);
		currentCertifiedAmtColumn.setAlign(TextAlign.RIGHT);
		
		ColumnConfig certifiedMovementColumn  = new ColumnConfig("Certified Movement", "certifiedMovementAmt", 100, false);
		certifiedMovementColumn.setRenderer(customRenderer);
		certifiedMovementColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig provisionAmtColumn  = new ColumnConfig("Provision", "provisionAmt", 100, false);
		provisionAmtColumn.setRenderer(customRenderer);
		provisionAmtColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig projProvisionAmtColumn  = new ColumnConfig("Projected Provision", "projProvisionAmt", 100, false);
		projProvisionAmtColumn.setRenderer(customRenderer);
		projProvisionAmtColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig ivAmountColumn = new ColumnConfig("IV Amount", "ivAmount", 100, false);
		ivAmountColumn.setRenderer(customRenderer);
		ivAmountColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig balanceTypeColumn = new ColumnConfig("Balance Type", "balanceType", 80, false);

		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(scDetailsRecordDef);
		this.dataStore = new Store(proxy, reader);
		this.dataStore.load();
		this.dataStore.setSortInfo(new SortState("sequenceNo",SortDir.ASC));
		this.setStore(this.dataStore);

		columns = new ColumnConfig[] { 				
				lineTypeColumn, 
				bqItemColumn, 
				descriptionColumn,
				bqQuantityColumn, 
				toBeApprovedQuantityColumn, 
				ecaRateColumn,
				scRateColumn, 
				toBeApprovedRateColumn,
				totalAmountColumn, 
				toBeApprovedAmountColumn, 
				postedWorkDoneQtyColumn,
				postedWorkDoneAmtColumn,
				currentWorkDoneQtyColumn,
				currentWorkDoneAmtColumn,
				workDoneMovementColumn,
				ivAmountColumn,
				postedCertifiedQtyColumn,
				postedCertifiedAmtColumn,
				currentCertifiedQtyColumn,
				currentCertifiedAmtColumn,
				certifiedMovementColumn,
				provisionAmtColumn,
				projProvisionAmtColumn,
				objectCodeColumn,
				subsidiaryCodeColumn, 
				approvedColumn,
				unitColumn, 
				remarkColumn, 
				ccSCNoColumn, 
				sequenceNoColumn,
				resourceNoColumn,
				balanceTypeColumn
		};
		ColumnConfig[] customizedColumns = detailSectionController.applyScreenPreferences(this.getScreenName(), columns);
		this.setColumnModel(new ColumnModel(customizedColumns));

		GridView view = new CustomizedGridView();
		view.setAutoFill(false);
		view.setForceFit(false);
		setView(view);

		setupToolbar();
		setupPaginationToolbar();
		securitySetup();

	}
	
	private void setupToolbar(){
		/*-------------------------- Addendum -------------------------*/
		addAddendumButton = new Item("Add");
		addAddendumButton.setIconCls("add-button-icon");
		addAddendumButton.addListener(new BaseItemListenerAdapter(){
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				// added by brian on 20110321
				UIUtil.maskPanelById(PANEL_ID, GlobalParameter.LOADING_MSG, true);
				Integer packageNum = new Integer(0);
				packageNum = new Integer(packageNo);
				if (!"F".equals(globalSectionController.getPackageEditorFormPanel().getPaymentStatus())){
					if(globalSectionController.getCurrentWindow() == null){
						globalSectionController.setCurrentWindow(new AddSCAddendumWindow(globalSectionController ,  globalSectionController.getJob().getJobNumber(), globalSectionController.getJob().getDescription(),  packageNum, false));
						globalSectionController.getCurrentWindow().show();
					}
				}
				else
					UIUtil.alert("SC Package was final paid.");
				// added by brian on 20110321
				UIUtil.unmaskPanelById(PANEL_ID);
			}
		});
		
		updateAddendumButton = new Item("Update");
		updateAddendumButton.setIconCls("save-button-icon");
		updateAddendumButton.addListener(new BaseItemListenerAdapter(){
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				if (resourceNumber=="")
					MessageBox.alert("Select a resources to update.");
				else {
					try{
						Integer packageNumberInt = new Integer(packageNo);
						Integer sequenceNumberInt = new Integer(sequenceNumber);
						Integer resourceNumberInt = new Integer(resourceNumber);
						detailSectionController.showEditAddendumWindow(job.getJobNumber(), packageNumberInt, sequenceNumberInt, billItem, resourceNumberInt, subsidiaryCode, objectCode);
					}catch(Exception ex){
						UIUtil.alert(ex.getMessage());
					}
				}
			}
		});
		
		submitAddendumButton = new Item("Submit");
		submitAddendumButton.setIconCls("submit-icon");
		submitAddendumButton.addListener(new BaseItemListenerAdapter(){
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				detailSectionController.showAddendumEnquiryWindow(Integer.parseInt(packageNo), scLineType, true);	
			}
		});
		
		viewAddendumButton = new Item("View");
		viewAddendumButton.setIconCls("view-icon");
		viewAddendumButton.addListener(new BaseItemListenerAdapter(){
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				detailSectionController.showAddendumEnquiryWindow(Integer.parseInt(packageNo), scLineType, false);	
			}
		});
		
		Menu addendumMenu = new Menu();
		addendumMenu.addItem(viewAddendumButton);
		addendumMenu.addItem(addAddendumButton);
		addendumMenu.addItem(updateAddendumButton);
		addendumMenu.addItem(submitAddendumButton);
		
		addendumToolbarButton = new ToolbarButton("Addendum");
		addendumToolbarButton.setMenu(addendumMenu);
		addendumToolbarButton.setIconCls("menu-show-icon");
		/*-------------------------- Addendum -------------------------*/
		
		/*------------------------- Work Done -------------------------*/
		scDetailSearchAndMassUpdateCumWDQtyButton = new Item("Update");
		scDetailSearchAndMassUpdateCumWDQtyButton.setIconCls("save-button-icon");
		scDetailSearchAndMassUpdateCumWDQtyButton.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				detailSectionController.getGlobalSectionController().showUpdateWDandCertQtyWindow("Cum WD Qty");
			}
		});

		ToolTip scDetailSearchAndMassUpdateCumWDQtyToolTip = new ToolTip();
		scDetailSearchAndMassUpdateCumWDQtyToolTip.setTitle("Update Work Done Qty");
		scDetailSearchAndMassUpdateCumWDQtyToolTip.setHtml("Search SC Detail Information and Mass Update for Cum WD Qty");
		scDetailSearchAndMassUpdateCumWDQtyToolTip.setDismissDelay(15000);
		scDetailSearchAndMassUpdateCumWDQtyToolTip.setWidth(200);
		scDetailSearchAndMassUpdateCumWDQtyToolTip.setTrackMouse(true);
		scDetailSearchAndMassUpdateCumWDQtyToolTip.applyTo(scDetailSearchAndMassUpdateCumWDQtyButton);
		
		Menu wdMenu = new Menu();
		wdMenu.addItem(scDetailSearchAndMassUpdateCumWDQtyButton);
		
		workDoneToolbarButton = new ToolbarButton("Work Done");
		workDoneToolbarButton.setMenu(wdMenu);
		workDoneToolbarButton.setIconCls("menu-show-icon");
		/*------------------------- Work Done -------------------------*/
		
		/*------------------------- Certificate -------------------------*/
		scDetailSearchAndMassUpdateCumCertifiedQtyButton = new Item("Update");
		scDetailSearchAndMassUpdateCumCertifiedQtyButton.setIconCls("save-button-icon");
		scDetailSearchAndMassUpdateCumCertifiedQtyButton.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				detailSectionController.getGlobalSectionController().showUpdateWDandCertQtyWindow("Cum Certified Qty");
			}
		});
		ToolTip scDetailSearchAndMassUpdateCumCertifiedQtyToolTip = new ToolTip();
		scDetailSearchAndMassUpdateCumCertifiedQtyToolTip.setTitle("Update Certified Qty");
		scDetailSearchAndMassUpdateCumCertifiedQtyToolTip.setHtml("Search SC Detail Information and Mass Update for Cum Certified Qty");
		scDetailSearchAndMassUpdateCumCertifiedQtyToolTip.setDismissDelay(15000);
		scDetailSearchAndMassUpdateCumCertifiedQtyToolTip.setWidth(200);
		scDetailSearchAndMassUpdateCumCertifiedQtyToolTip.setTrackMouse(true);
		scDetailSearchAndMassUpdateCumCertifiedQtyToolTip.applyTo(scDetailSearchAndMassUpdateCumCertifiedQtyButton);
		
		Menu certMenu = new Menu();
		certMenu.addItem(scDetailSearchAndMassUpdateCumCertifiedQtyButton);
		
		certificateToolbarButton = new ToolbarButton("Certificate");
		certificateToolbarButton.setMenu(certMenu);
		certificateToolbarButton.setIconCls("menu-show-icon");
		/*------------------------- Certificate -------------------------*/
		
		/*---------------------------Excel-----------------------------*/
		uploadSCXlsButton = new Item("Import");
		uploadSCXlsButton.setIconCls("upload-icon");
		uploadSCXlsButton.addListener(new BaseItemListenerAdapter(){
			public void onClick(BaseItem item, EventObject e) {
				promptImportWindow(detailSectionController.getGlobalSectionController().getJob().getJobNumber(), packageNo, detailSectionController.getGlobalSectionController().getUser().getUsername().toString());
				promptWindow.show();
			}
		});
		ToolTip uploadSCXlsToolTip = new ToolTip();
		uploadSCXlsToolTip.setTitle("Import Excel");
		uploadSCXlsToolTip.setHtml("Import SCDetails from Excel file");
		uploadSCXlsToolTip.setDismissDelay(15000);
		uploadSCXlsToolTip.setWidth(200);
		uploadSCXlsToolTip.setTrackMouse(true);
		uploadSCXlsToolTip.applyTo(uploadSCXlsButton);
		
		downloadSCXlsButton = new Item("Export");
		downloadSCXlsButton.setIconCls("download-icon");
		downloadSCXlsButton.addListener(new BaseItemListenerAdapter(){
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				com.google.gwt.user.client.Window.open(GlobalParameter.SCDETAILS_EXCEL_DOWNLOAD_URL+"?jobNumber="+job.getJobNumber() +"&packageNumber="+packageNo+"&packageType="+packageType, "_blank", "");
			}
		});
		ToolTip downloadSCXlsToolTip = new ToolTip();
		downloadSCXlsToolTip.setTitle("Export");
		downloadSCXlsToolTip.setHtml("Export SCDetails as excel file.");
		downloadSCXlsToolTip.setDismissDelay(15000);
		downloadSCXlsToolTip.setWidth(200);
		downloadSCXlsToolTip.setTrackMouse(true);
		downloadSCXlsToolTip.applyTo(downloadSCXlsButton);
		
		Menu excelMenu = new Menu();
		excelMenu.addItem(uploadSCXlsButton);
		excelMenu.addItem(downloadSCXlsButton);
		
		excelToolbarButton = new ToolbarButton("Excel");
		excelToolbarButton.setMenu(excelMenu);
		excelToolbarButton.setIconCls("excel-icon");
		/*---------------------------Excel-----------------------------*/
		
		/*------------------ Attachment ---------------------------*/
		openSCAttachmentButton = new ToolbarButton();
		openSCAttachmentButton.setText("Attachment");
		openSCAttachmentButton.setIconCls("attachment-icon");
		openSCAttachmentButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e)
			{	
				globalMessageTicket.refresh();
				if (!"".equals(sequenceNumber))
					detailSectionController.getGlobalSectionController().showAttachmentWindow("Attachment_SCDetail", job.getJobNumber(),packageNo,sequenceNumber.trim(),billItem.substring(0, billItem.indexOf("/")), resourceNumber, subsidiaryCode, objectCode);
				else
					MessageBox.alert("No detail has been selected.");
			}
		});
		/*------------------ Attachment ---------------------------*/
		

		recalculateIVButton = new ToolbarButton("Recalculate Resource Summary IV");
		recalculateIVButton.setTooltip("Recalculate Resource IV", "Recalculate Resource Summary IV amounts from corresponding SC Details");
		recalculateIVButton.setIconCls("calculator-icon");
		recalculateIVButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				globalSectionController.getDetailSectionController().recalculateIV(job, packageNo);
			}
		});

		/*------------------ Refresh----------------*/
		refreshButton = new ToolbarButton();
		refreshButton.setText("Refresh");
		refreshButton.setIconCls("toggle-button-icon");
		refreshButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				globalSectionController.populateSCPackageMainPanelandDetailPanel(packageNo);
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
		/*------------------ Refresh----------------*/
		
		
		addendumToolbarButton.setVisible(false);
		addAddendumButton.setVisible(false);
		updateAddendumButton.setVisible(false);
		submitAddendumButton.setVisible(false);

		workDoneToolbarButton.setVisible(false);
		scDetailSearchAndMassUpdateCumWDQtyButton.setVisible(false);
		
		certificateToolbarButton.setVisible(false);
		scDetailSearchAndMassUpdateCumCertifiedQtyButton.setVisible(false);
		
		excelToolbarButton.setVisible(false);
		downloadSCXlsButton.setVisible(false);
		uploadSCXlsButton.setVisible(false);
		
		openSCAttachmentButton.setVisible(false);
		recalculateIVButton.setVisible(false);

		
		toolbar.addButton(addendumToolbarButton);
		toolbar.addSeparator();
		toolbar.addButton(workDoneToolbarButton);
		toolbar.addSeparator();
		toolbar.addButton(certificateToolbarButton);
		toolbar.addSeparator();
		toolbar.addButton(excelToolbarButton);
		toolbar.addSeparator();
		toolbar.addButton(openSCAttachmentButton);
		toolbar.addSeparator();
		toolbar.addButton(recalculateIVButton);
		toolbar.addSeparator();
		toolbar.addButton(refreshButton);
		toolbar.addSeparator();
		
		this.setTopToolbar(toolbar);
	}
	
	private void setupPaginationToolbar() {
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter() {

			public void goToPage(final int pageNum) {
				globalMessageTicket.refresh();
				globalSectionController.goToPagePackageSCDetails(pageNum);
			}
		});
		setBottomToolbar(paginationToolbar);
	}
	
	private void securitySetup() {
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		userAccessRightsRepository.getAccessRights(detailSectionController.getUser().getUsername(), RoleSecurityFunctions.F010202_SCDETAIL_DETAILPANEL, new AsyncCallback<List<String>>() {
			public void onSuccess(List<String> accessRightsReturned) {
				accessRightsList = accessRightsReturned;

				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);

				if (accessRightsList.contains("WRITE")) {
					addendumToolbarButton.setVisible(true);
					addAddendumButton.setVisible(true);
					updateAddendumButton.setVisible(true);
					submitAddendumButton.setVisible(true);
					
					excelToolbarButton.setVisible(true);
					downloadSCXlsButton.setVisible(true);
					uploadSCXlsButton.setVisible(true);
					
					openSCAttachmentButton.setVisible(true);
					
					workDoneToolbarButton.setVisible(true);
					scDetailSearchAndMassUpdateCumWDQtyButton.setVisible(true);
					
					certificateToolbarButton.setVisible(true);
					scDetailSearchAndMassUpdateCumCertifiedQtyButton.setVisible(true);
					
					recalculateIVButton.setVisible(true);
				}
				if (accessRightsList.contains("READ")) {
					addendumToolbarButton.setVisible(true);
					submitAddendumButton.setVisible(true);
					
					openSCAttachmentButton.setVisible(true);
					
					excelToolbarButton.setVisible(true);
					downloadSCXlsButton.setVisible(true);
					
					workDoneToolbarButton.setVisible(true);
					scDetailSearchAndMassUpdateCumWDQtyButton.setVisible(true);
					
					certificateToolbarButton.setVisible(true);
					scDetailSearchAndMassUpdateCumCertifiedQtyButton.setVisible(true);
				}

			}

			public void onFailure(Throwable e) {
				UIUtil.alert(e.getMessage());
			}
		});
	}
	
	public void populate(PaginationWrapper<SCDetailsWrapper> scDetailsWrapper) {
		this.dataStore.removeAll();
		List<SCDetailsWrapper> scDetails = scDetailsWrapper.getCurrentPageContentList();
		if(scDetails == null || scDetails.size() == 0)
			return;
		this.currentPage = scDetailsWrapper.getCurrentPage();
		this.totalPage = scDetailsWrapper.getTotalPage();
		this.paginationToolbar.setTotalPage(totalPage);
		this.paginationToolbar.setCurrentPage(currentPage);

		Double toBeApprovedAmount = new Double(0) ;
		Double totalAmount = new Double(0) ;
		Double totalProvisionAmt = new Double(0) ;
		Double totalProjProvisionAmt = new Double(0) ;
		Double thisProvisionAmt = new Double(0) ;
		Double thisProjProvisionAmt = new Double(0) ;
		Double totalCurrentCertAmt = new Double(0);
		Double totalPostedCertAmt = new Double(0);
		Double totalCurrentWorkdoneAmt = new Double(0);
		Double totalPostedWorkdoneAmt = new Double(0);
		Double thisCurrentCertAmt = new Double(0);
		Double thisPostedCertAmt = new Double(0);
		Double thisCurrentWorkdoneAmt = new Double(0);
		Double thisPostedWorkdoneAmt = new Double(0);
		Double thisIVAmount = new Double(0);
		Double totalIVAmount = new Double(0);

		Record[] records = new Record[ (scDetails.size())];
		for (int i=0; i<scDetails.size();i++) {
			SCDetailsWrapper scDetail = scDetails.get(i);
			if (scDetail.getDescription().equals("SUB TOTAL:") || scDetail.getDescription().equals("GRAND TOTAL:")){
				records[i] = this.scDetailsRecordDef.createRecord(new Object[] {
						"","","","",
						scDetail.getDescription(),
						"","","","","",
						scDetail.getTotalAmount(),
						scDetail.getToBeApprovedAmount(),
						"","","","","","","","",
						scDetail.getTotalPostedWorkdoneAmt(),
						"",
						scDetail.getTotalCurrentWorkdoneAmt(),
						scDetail.getTotalCurrentWorkdoneAmt() - scDetail.getTotalPostedWorkdoneAmt(),
						scDetail.getTotalIVAmt(),
						"",
						scDetail.getTotalPostedCertAmt(),
						"",
						scDetail.getTotalCurrentCertAmt(),
						scDetail.getTotalCurrentCertAmt() - scDetail.getTotalPostedCertAmt(),
						scDetail.getTotalProvisionAmt(),
						scDetail.getTotalProjProvisionAmt(),
						"","","",""
				});

			}
			else{
				packageType = scDetail.getScPackage().getPackageType();
				scLineType = scDetail.getLineType();
				
				toBeApprovedAmount +=scDetail.getToBeApprovedAmount()!=null?scDetail.getToBeApprovedAmount():0.0 ;
				totalAmount += scDetail.getTotalAmount()!=null?scDetail.getTotalAmount():0.0 ;

				Double scRate = new Double(0.0);
				double percentageRate=1;
				if ("%".equals(scDetail.getBalanceType()))
					percentageRate = scDetail.getQuantity()/100;
				if (scDetail.getScRate()!=null){
					scRate = scDetail.getScRate();
					if (scDetail.getCumWorkDoneQuantity()!=null)
						thisCurrentWorkdoneAmt =CalculationUtil.round(scDetail.getCumWorkDoneQuantity()*scDetail.getScRate()*percentageRate, 2);
					if (scDetail.getPostedWorkDoneQuantity()!=null)
						thisPostedWorkdoneAmt = CalculationUtil.round(scDetail.getPostedWorkDoneQuantity()*scDetail.getScRate()*percentageRate, 2);
					if (scDetail.getPostedCertifiedQuantity()!=null)
						thisPostedCertAmt = CalculationUtil.round(scDetail.getPostedCertifiedQuantity()*scDetail.getScRate()*percentageRate, 2);
					if (scDetail.getCumCertifiedQuantity()!=null)
						thisCurrentCertAmt = CalculationUtil.round(scDetail.getCumCertifiedQuantity()*scDetail.getScRate()*percentageRate, 2);

				}
				
				 thisIVAmount = 0.0;
				 if (scDetail.getCostRate()!= null && scDetail.getCumWorkDoneQuantity()!=null)
					 thisIVAmount =CalculationUtil.round(scDetail.getCostRate() * scDetail.getCumWorkDoneQuantity(), 2);
				 String lineType = scDetail.getLineType() == null ? "" : scDetail.getLineType().trim();
				 thisProvisionAmt = new Double(lineType.equals("C1")||lineType.equals("C2")||lineType.equals("RR")||lineType.equals("RT")||lineType.equals("MS")?0: thisCurrentWorkdoneAmt-thisPostedCertAmt);
				 thisProjProvisionAmt = new Double(lineType.equals("C1")||lineType.equals("C2")||lineType.equals("RR")||lineType.equals("RT")||lineType.equals("MS")? 0:thisCurrentWorkdoneAmt-thisCurrentCertAmt);
				String approved = scDetail.getApproved() == null ? "" : "A".equals(scDetail.getApproved().trim()) ? "Approved" : "S".equals(scDetail.getApproved().trim()) ? "Suspended" : "Not Approved";
				 totalProvisionAmt += thisProvisionAmt;
				 totalProjProvisionAmt += thisProjProvisionAmt;
				 totalCurrentCertAmt += thisCurrentCertAmt;
				 totalCurrentWorkdoneAmt += thisCurrentWorkdoneAmt;
				 totalPostedCertAmt += thisPostedCertAmt;
				 totalPostedWorkdoneAmt += thisPostedWorkdoneAmt;
				 totalIVAmount += thisIVAmount;
				 records[i] = this.scDetailsRecordDef.createRecord(new Object[] {
						job.getJobNumber(),				
						scDetail.getSequenceNo(),
						scDetail.getResourceNo()==null?" ":scDetail.getResourceNo(),
						scDetail.getBillItem()==null?"":scDetail.getBillItem(),
						scDetail.getDescription(),
						scDetail.getQuantity(),
						scDetail.getToBeApprovedQuantity(),
						scDetail.getCostRate()==null?0.0:scDetail.getCostRate(),
						scRate,
						scDetail.getToBeApprovedRate()!=null?scDetail.getToBeApprovedRate():"",

						scDetail.getTotalAmount(),
						scDetail.getToBeApprovedAmount(),
						scDetail.getObjectCode(),
						scDetail.getSubsidiaryCode(),
						lineType,
						approved,
						scDetail.getUnit(),
						scDetail.getRemark()!=null?scDetail.getRemark():"",
						scDetail.getContraChargeSCNo()!=null?scDetail.getContraChargeSCNo():"",
						scDetail.getPostedWorkDoneQuantity()!=null?scDetail.getPostedWorkDoneQuantity():0.0,
						thisPostedWorkdoneAmt,
						scDetail.getCumWorkDoneQuantity()!=null?scDetail.getCumWorkDoneQuantity():0.0,
						thisCurrentWorkdoneAmt,
						thisCurrentWorkdoneAmt - thisPostedWorkdoneAmt,
						thisIVAmount,
						scDetail.getPostedCertifiedQuantity()!=null?scDetail.getPostedCertifiedQuantity():0.0,
						thisPostedCertAmt,
						scDetail.getCumCertifiedQuantity()!=null?scDetail.getCumCertifiedQuantity():0.0,
						thisCurrentCertAmt,
						thisCurrentCertAmt - thisPostedCertAmt,
						thisProvisionAmt,
						thisProjProvisionAmt,
						"",
						scDetail.getScPackage().getPackageNo(),

						scDetail.getSourceType(),
						scDetail.getCumWorkDoneQuantity()!=null?scDetail.getCumWorkDoneQuantity():0.0
				 });
			}		
		}

		this.dataStore.add(records);

		//
		//Get the Addendum Status of Selected Package
		//By Peter Chan
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.getSCPackage(job.getJobNumber(), packageNo, new AsyncCallback<SCPackage>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}

			public void onSuccess(SCPackage scPackage) {
				submittedAddendum = scPackage.getSubmittedAddendum();
				if (submittedAddendum.equals(SCPackage.ADDENDUM_SUBMITTED))
					addAddendumButton.setDisabled(true);
				else
					addAddendumButton.setDisabled(false);
			}

		});
		globalMessageTicket.refresh();
	}


	public String getScreenName() {
		return screenName;
	}

	private void uploadResponseCallback(String responseText) {

		JSONValue jsonValue = JSONParser.parse(responseText);
		JSONObject jsonObj = jsonValue.isObject();

		if (jsonObj.get("success").isBoolean().booleanValue()) {
			MessageBox.alert(jsonObj.get("numRecordImported").isNumber().doubleValue() + " records imported successfully.");
		} else {
			MessageBox.alert("Import failed! <br/>" + jsonObj.get("message").isString().stringValue());
		}
	}
	
	public void promptImportWindow(String jobNumber, String packageNumber, String userID) {
		//Prompt a window for importing an Excel file
		promptWindow = new Window();
		Button closePromptWindowButton = new Button("Close");
		Button promptWindowImportButton = new Button("Import");
		closePromptWindowButton.setCls("table-cell");
		promptWindowImportButton.setCls("table-cell");
		this.uploadPanel = new FormPanel();
		this.uploadPanel.setWidth(350);
		this.uploadPanel.setHeight(30);
		this.uploadPanel.setLayout(new TableLayout(4));
		closePromptWindowButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				promptWindow.close();
			}});
		promptWindowImportButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				uploadPanel.getForm().submit(GlobalParameter.SCDETAIL_EXCEL_UPLOAD_URL, null, Connection.POST, "processing...", false);				
			}});

		this.uploadPanel.setFileUpload(true);
		Label labelFile = new Label("File: ");
		labelFile.setCls("table-cell");
		final TextField fileTextField = new TextField("", "file");
		fileTextField.setInputType("file");
		fileTextField.setAllowBlank(false);
		this.uploadPanel.add(labelFile);
		this.uploadPanel.add(fileTextField);
		this.uploadPanel.add(promptWindowImportButton);
		this.uploadPanel.add(closePromptWindowButton);
		final Hidden jobNumberHiddenField = new Hidden("jobNumber", jobNumber);
		final Hidden packageNumberHiddenField = new Hidden("packageNumber", packageNumber);
		final Hidden paymentStatusHiddenField = new Hidden("paymentStatus", detailSectionController.getGlobalSectionController().getPackageEditorFormPanel().getPaymentStatus().trim());
		final Hidden paymentRequestStatusHiddenField = new Hidden("paymentRequestStatus", detailSectionController.getGlobalSectionController().getPackageEditorFormPanel().getPaymentRequestStatus().trim());
		final Hidden legacyJobFlagHiddenField = new Hidden("legacyJobFlag",detailSectionController.getGlobalSectionController().getPackageEditorFormPanel().getLegacyJobFlag().trim());
		final Hidden allowManualInputSCWorkdoneHiddenField = new Hidden("allowManualInputSCWorkdone", detailSectionController.getGlobalSectionController().getPackageEditorFormPanel().getAllowManualInputSCWorkdone().trim());
		final Hidden userIDHiddenField = new Hidden("userID", userID);

		this.uploadPanel.add(jobNumberHiddenField);
		this.uploadPanel.add(packageNumberHiddenField);
		this.uploadPanel.add(userIDHiddenField);
		this.uploadPanel.add(paymentStatusHiddenField);
		this.uploadPanel.add(paymentRequestStatusHiddenField);
		this.uploadPanel.add(legacyJobFlagHiddenField);
		this.uploadPanel.add(allowManualInputSCWorkdoneHiddenField);
		this.uploadPanel.addFormListener(new FormListenerAdapter() {
			public void onActionComplete(Form form, int httpStatus, String responseText) {
				uploadResponseCallback(responseText);
			}

			public void onActionFailed(Form form, int httpStatus, String responseText) {
				uploadResponseCallback(responseText);
			}
		});

		promptWindow.setWidth(360);
		promptWindow.setTitle("Import SC Detail from Excel File");
		promptWindow.add(this.uploadPanel);
	}

}
