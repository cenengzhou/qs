package com.gammon.qs.client.ui.panel.detailSection;

import java.util.ArrayList;
import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.panel.PreferenceSetting;
import com.gammon.qs.client.ui.renderer.AmountRendererNonTotal;
import com.gammon.qs.client.ui.toolbar.GoToPageCommandAdapter;
import com.gammon.qs.client.ui.toolbar.PaginationToolbar;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.SCPaymentDetail;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.PaymentPaginationWrapper;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

@SuppressWarnings("unchecked")
public class PaymentDetailGridPanel extends EditorGridPanel implements PreferenceSetting{

	private Store dataStore;
	private String screenName = "payment-detail-editor-grid";
	
	private String jobNumber;
	private String packageNo;
	private Integer paymentCertNo;
	
	private Toolbar toolbar = new Toolbar();
	
	private ToolbarButton refreshButton;
	private ToolbarButton paymentCertViewButton;
	private ToolbarButton attachmentButton;
	private ToolbarButton excelButton;
	private Item excelExportButton;
	
	// added by brian on 20110314 - start
	@SuppressWarnings("unused")
	private Checkbox filterCheckbox;
	private ToolbarButton filterButton;
	private ToolbarTextItem filterLabel;
	private TextField filterField;
	
	private List<SCPaymentDetail> cachedSCPaymentDetailList;
	// added by brian on 20110314 - end
	
	private GlobalMessageTicket globalMessageTicket;
	
	// added by brian on 20110317
	private GlobalSectionController globalSectionController;
	
	//Pagination
	private PaginationToolbar paginationToolbar;
	private ToolbarTextItem totalMovementAmountTextItem = new ToolbarTextItem("Total Movement: ");
	private ToolbarTextItem totalCumCertAmountTextItem = new ToolbarTextItem("Total Cumulative Amount: ");
	private ToolbarTextItem totalPostedCertAmountTextItem = new ToolbarTextItem("Total Posted Amount: ");
	
	// used to check access rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	
	private final RecordDef paymentDetailRecordDef = new RecordDef(
		new FieldDef[] { 
			new StringFieldDef("jobNumber"),
			new StringFieldDef("subContractNo"),
			new StringFieldDef("certificateNo"),
			new StringFieldDef("lineType"),
			new StringFieldDef("billItem"),
			new StringFieldDef("movementAmount"),
			new StringFieldDef("cumulativeAmount"),
			new StringFieldDef("preCumCertAmount"),
			new StringFieldDef("description"),
			new StringFieldDef("sequenceNo"),
			new StringFieldDef("objectCode"),
			new StringFieldDef("subsidiaryCode")
			

	});
	
	/**
	 * @author koeyyeung
	 * modified on 26Sep,2013
	 * **/
	public PaymentDetailGridPanel (final GlobalSectionController globalSectionController, String aJobNumber, String aPackageNo, Integer aPaymentCertNo) {
		super();
		
		globalMessageTicket= new GlobalMessageTicket();
		this.globalSectionController = globalSectionController;
		this.setTrackMouseOver(true);
		this.setBorder(false);
		this.setAutoScroll(true);
		
		this.jobNumber = aJobNumber;
		this.packageNo = aPackageNo;
		this.paymentCertNo = aPaymentCertNo;
		
		// Repository instantiation
		userAccessRightsRepository = globalSectionController.getUserAccessRightsRepository();
		
		setupToolbar();
		setupGridPanel();
	}
	
	private void setupToolbar(){
		//obsolete
		paymentCertViewButton = new ToolbarButton();
		paymentCertViewButton.setText("Payment Cert");
		paymentCertViewButton.addListener(new ButtonListenerAdapter() {
			
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				globalSectionController.showPaymentCertViewWindow(jobNumber, packageNo, paymentCertNo);
			}
		});
		
		ToolTip saveToolTip = new ToolTip();
		saveToolTip.setTitle("Subcontract Payment Certificate");
		saveToolTip.setHtml("View subcontract payment certificate");
		saveToolTip.setDismissDelay(15000);
		saveToolTip.setWidth(200);
		saveToolTip.setTrackMouse(true);
		saveToolTip.applyTo(paymentCertViewButton);
		//this.setTopToolbar(paymentCertViewButton);
		
		
		attachmentButton = new ToolbarButton();
		attachmentButton.setText("Attachment");
		attachmentButton.setIconCls("attachment-icon");
		attachmentButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				globalSectionController.showAttachmentWindow("Attachment_Payment", jobNumber, packageNo, paymentCertNo.toString());
			}
		});
		
		/*----------------Excel----------------------*/
		excelExportButton = new Item("Export");
		excelExportButton.setIconCls("save-button-icon");
		excelExportButton.addListener(new BaseItemListenerAdapter(){
			public void onClick(BaseItem item, EventObject e) {
				globalMessageTicket.refresh();
				com.google.gwt.user.client.Window.open(GlobalParameter.SCPAMENT_EXCEL_DOWNLOAD_URL+"?jobNumber="+globalSectionController.getJob().getJobNumber() +"&packageNumber="+globalSectionController.getSelectedPackageNumber()+"&paymentCertNo="+paymentCertNo, "_blank", "");
			}
		});
		
		ToolTip excelExportToolTip = new ToolTip();
		excelExportToolTip.setTitle("Export");
		excelExportToolTip.setHtml("Export SCPaymentDetails as excel file.");
		excelExportToolTip.setDismissDelay(15000);
		excelExportToolTip.setWidth(200);
		excelExportToolTip.setTrackMouse(true);
		excelExportToolTip.applyTo(excelExportButton);
		
		Menu addendumMenu = new Menu();
		addendumMenu.addItem(excelExportButton);
		
		excelButton = new ToolbarButton("Excel");
		excelButton.setMenu(addendumMenu);
		excelButton.setIconCls("excel-icon");
		/*----------------Excel----------------------*/
		
		// added by brian on 20110317
		filterField = new TextField();
		
		refreshButton = new ToolbarButton();
		refreshButton.setText("Refresh");
		refreshButton.setIconCls("toggle-button-icon");
		refreshButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				globalMessageTicket.refresh();
				// modified by brian on 20110317
				if(filterField.getText() != null && filterField.getText().length() > 0)
					globalSectionController.navigateToPaymentDetails(packageNo, paymentCertNo, filterField.getText());
				else
					globalSectionController.navigateToPaymentDetails(packageNo, paymentCertNo, "");
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
		
		
		
		// added by brian on 20110314 - start
		cachedSCPaymentDetailList = new ArrayList<SCPaymentDetail>();
		
		filterLabel = new ToolbarTextItem("Filter by Line Type: ");
		
		//Filter's Button
		filterButton = new ToolbarButton();
		filterButton.setIconCls("filter-icon");
		filterButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				filterAndPopulateGrid();
			}
		});
		
		//Toolbar - Filter Button's Tips
		ToolTip filterButtonToolTip = new ToolTip();
		filterButtonToolTip.setTitle("Filter");
		filterButtonToolTip.setHtml("Filter by Line Type");
		filterButtonToolTip.applyTo(filterButton);
		
		
		
		excelButton.setVisible(false);
		excelExportButton.setVisible(false);
		paymentCertViewButton.setVisible(false);
		attachmentButton.setVisible(false);
		
		toolbar.addButton(attachmentButton);
		toolbar.addSeparator();
		toolbar.addButton(excelButton);
		toolbar.addSeparator();
		toolbar.addItem(filterLabel);
		toolbar.addField(filterField);
		toolbar.addButton(filterButton);
		toolbar.addFill();
		toolbar.addButton(refreshButton);
		
		this.setTopToolbar(toolbar);
		
		// Check for access rights
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010206_PAYMENT_DETAIL_GRIDPANEL, new AsyncCallback<List<String>>(){
			public void onSuccess(List<String> accessRightsReturned) {
				try{
					accessRightsList = accessRightsReturned;

					UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
					securitySetup();
				}catch(Exception e){
					UIUtil.alert(e);
				}
			}

			public void onFailure(Throwable e) {
				UIUtil.alert(e.getMessage());
			}
		});
	}
	
	private void setupGridPanel(){
		Renderer amountRender = new AmountRendererNonTotal(globalSectionController.getUser());
		
		/*ColumnConfig jobNumberColumnConfig = new ColumnConfig("Job Number", "jobNumber", 90, true);
		ColumnConfig subContractNoColumnConfig = new ColumnConfig("Sub Contact No", "subContractNo", 150, true);
		ColumnConfig certificateNoColumnConfig = new ColumnConfig("Certificate No", "certificateNo", 150, true);*/
		ColumnConfig lineTypeColumnConfig = new ColumnConfig("Line Type", "lineType", 70, false);
		ColumnConfig billItemColumnConfig = new ColumnConfig("Bill Item", "billItem", 100, false);
		ColumnConfig movementAmountColumnConfig = new ColumnConfig("Movement Amount", "movementAmount", 160, false);
		movementAmountColumnConfig.setAlign(TextAlign.RIGHT);
		movementAmountColumnConfig.setRenderer(amountRender);
		ColumnConfig cumulativeAmountColumnConfig = new ColumnConfig("Cumulative Certified Amount", "cumulativeAmount", 160, false);
		cumulativeAmountColumnConfig.setAlign(TextAlign.RIGHT);
		cumulativeAmountColumnConfig.setRenderer(amountRender);
		ColumnConfig preCumCertAmountColumnConfig = new ColumnConfig("Posted Certified Amount","preCumCertAmount",160, false);
		preCumCertAmountColumnConfig.setAlign(TextAlign.RIGHT);
		preCumCertAmountColumnConfig.setRenderer(amountRender);
		ColumnConfig descriptionColumnConfig = new ColumnConfig("Description", "description", 200, false);
		ColumnConfig sequenceNoConfig = new ColumnConfig("Sequence No", "sequenceNo", 80, false);
		ColumnConfig objectCodeColumnConfig = new ColumnConfig("Object Code", "objectCode", 100, false);
		ColumnConfig subsidiaryCodeColumnConfig = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 100, false);
		
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(paymentDetailRecordDef);
		this.dataStore = new Store(proxy, reader);
		this.dataStore.load();
		this.dataStore.setSortInfo(new SortState("jobNumber",SortDir.ASC));
		this.setStore(this.dataStore);
		
		
		ColumnConfig[] columns = new ColumnConfig[] {
				lineTypeColumnConfig,
				billItemColumnConfig,
				movementAmountColumnConfig,
				cumulativeAmountColumnConfig,
				preCumCertAmountColumnConfig,
				descriptionColumnConfig,
				sequenceNoConfig,
				objectCodeColumnConfig,
				subsidiaryCodeColumnConfig
		};
		
		ColumnConfig[] customizedColumns = globalSectionController.applyScreenPreferences(this.getScreenName(), columns);
		this.setColumnModel(new ColumnModel(customizedColumns));
		//this.setColumnModel (new ColumnModel(columns));
		GridView view = new CustomizedGridView();
		view.setAutoFill(false);
		view.setForceFit(false);
		this.setView(view);
		
		paginationToolbar = new PaginationToolbar();
		paginationToolbar.setGoToPageAdapter(new GoToPageCommandAdapter(){
			public void goToPage(int pageNum){
				globalSectionController.goToPaymentDetailsPage(PaymentDetailGridPanel.this, pageNum);
			}
		});
		paginationToolbar.addSpacer();
		paginationToolbar.addSeparator();
		paginationToolbar.addSpacer();
		paginationToolbar.addItem(totalMovementAmountTextItem);
		paginationToolbar.addSpacer();
		paginationToolbar.addSeparator();
		paginationToolbar.addSpacer();
		paginationToolbar.addItem(totalCumCertAmountTextItem);
		paginationToolbar.addSpacer();
		paginationToolbar.addSeparator();
		paginationToolbar.addSpacer();
		paginationToolbar.addItem(totalPostedCertAmountTextItem);
		this.setBottomToolbar(paginationToolbar);
		paginationToolbar.setTotalPage(0);
		paginationToolbar.setCurrentPage(0);
	}
	
	public void populateGrid(PaymentPaginationWrapper wrapper) {
		if(wrapper == null)
			return;
		
		NumberFormat nf = NumberFormat.getFormat("#,##0.0#");
		
		totalMovementAmountTextItem.setText("<b>Total Movement: " + nf.format(wrapper.getTotalMovementAmount()) + "</b>");
		totalCumCertAmountTextItem.setText("<b>Total Cum. Amount: " + nf.format(wrapper.getTotalCumCertAmount()) + "</b>");
		totalPostedCertAmountTextItem.setText("<b>Total Posted Amount: " + nf.format(wrapper.getTotalPostedCertAmount()) + "</b>");
		
		paginationToolbar.setTotalPage(wrapper.getTotalPage());
		paginationToolbar.setCurrentPage(wrapper.getCurrentPage());
		
		List<SCPaymentDetail> scPaymentDetailList = wrapper.getCurrentPageContentList();
		
		// added by brian on 20110314 - start
		// add filter by Line Type
		this.cachedSCPaymentDetailList.addAll(scPaymentDetailList);
		// added by brian on 20110314 - end
		
		Record[] data = new Record[scPaymentDetailList.size()];
		
		for (int i=0; i<scPaymentDetailList.size(); i++) {
			SCPaymentDetail scPaymentDetail = scPaymentDetailList.get(i);
			
			try {
				Double movementAmount = scPaymentDetail.getMovementAmount() != null ? scPaymentDetail.getMovementAmount() : 0;
				Double cumAmount = scPaymentDetail.getCumAmount() != null ? scPaymentDetail.getCumAmount() : 0; 

				data[i] = paymentDetailRecordDef.createRecord( new Object[] {
					scPaymentDetail.getScPaymentCert().getScPackage().getJob().getJobNumber(),
					scPaymentDetail.getScPaymentCert().getScPackage().getPackageNo(),
					scPaymentDetail.getPaymentCertNo(),
					scPaymentDetail.getLineType(),
					scPaymentDetail.getBillItem(),
					movementAmount,
					cumAmount,
					cumAmount - movementAmount,
					wrapper.getScDetailsDescription().get(i),
					scPaymentDetail.getScSeqNo(),
					scPaymentDetail.getObjectCode(),
					scPaymentDetail.getSubsidiaryCode()
				});
			} catch(Exception e) {
				UIUtil.alert(e);
			}
		}
		this.dataStore.removeAll();
		this.dataStore.add(data);
		globalMessageTicket.refresh();
		
	}

	public String getScreenName() {
		return screenName;
	}
	
	private void securitySetup(){
		if (accessRightsList.contains("READ")){
			paymentCertViewButton.setVisible(true);
			attachmentButton.setVisible(true);
			excelButton.setVisible(true);
			excelExportButton.setVisible(true);
		}
		if (accessRightsList.contains("WRITE")){
			paymentCertViewButton.setVisible(true);
			attachmentButton.setVisible(true);
			excelButton.setVisible(true);
			excelExportButton.setVisible(true);
		}
	}
	
	private void filterAndPopulateGrid(){
		
		if(this.filterField.getText() != null && filterField.getText().length() > 0)
			globalSectionController.navigateToPaymentDetails(packageNo, paymentCertNo, filterField.getText());
		else
			globalSectionController.navigateToPaymentDetails(packageNo, paymentCertNo, "");
	}
}
