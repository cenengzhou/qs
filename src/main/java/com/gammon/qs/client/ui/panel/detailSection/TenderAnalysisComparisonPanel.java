package com.gammon.qs.client.ui.panel.detailSection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.controller.DetailSectionController;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.mainSection.PackageEditorFormPanel;
import com.gammon.qs.client.ui.renderer.AmountRendererCheckIfTotal;
import com.gammon.qs.client.ui.renderer.QuantityRenderer;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.client.ui.window.detailSection.AddSCAddendumWindow;
import com.gammon.qs.client.ui.window.mainSection.EditSCDatesWindow;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.gammon.qs.wrapper.tenderAnalysis.TenderAnalysisComparisonWrapper;
import com.gammon.qs.wrapper.tenderAnalysis.TenderAnalysisDetailWrapper;
import com.gammon.qs.wrapper.tenderAnalysis.TenderAnalysisVendorWrapper;
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
import com.gwtext.client.data.IntegerFieldDef;
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
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FormListenerAdapter;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridHeaderListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

public class TenderAnalysisComparisonPanel extends GridPanel {
	private DetailSectionController detailSectionController;

	private Store dataStore;

	private RecordDef recordDef;

	private int highlightedColInd = 0;
	private String highlightedHeader = "";

	private ToolbarButton awardButton; 
	private ToolbarButton editVendorRatesButton;
	private ToolbarButton removeVendorButton;
	private ToolbarButton downloadXlsButton;
	private ToolbarButton uploadVendorXlsButton;
	private ToolbarButton recalculateIVButton;
	
	private ToolbarButton addendumToolbarButton;
	private Item addAddendumButton;
	private ToolbarButton workDoneToolbarButton;
	private Item updateCumWDQtyButton ;
	private ToolbarButton certificateToolbarButton;
	private Item updateCumCertifiedQtyButton ;
	
	
	private Window importWindow;
	private GlobalMessageTicket globalMessageTicket;

	// used to check access rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private List<String> accessRightsList;
	private String selectedCurrencyCode;
	private String selectedVendorName;
	private String companyBaseCurrency;
	private TenderAnalysisComparisonWrapper tenderAnalysisComparisonWrapper;
	private SCPackage scPackage;
	private String packageNo;
	private String jobNumber;
	private Integer packageStatus;
	private String selectedVendorNo;
	private String paymentRequestStatus="";
	private boolean showToolbar;
	private GlobalSectionController globalSectionController;
	private List<SCPaymentCert> scPaymentCertList;
	
	/* added option to hide toolbar, does not change behavior of existing constructor - matthewatc 2/2/12 */
	public TenderAnalysisComparisonPanel(final DetailSectionController detailSectionController, final TenderAnalysisComparisonWrapper tenderAnalysisComparison, String currency) {
		this(detailSectionController, tenderAnalysisComparison, currency, true);
	}

	public TenderAnalysisComparisonPanel(final DetailSectionController detailSectionController, final TenderAnalysisComparisonWrapper tenderAnalysisComparison, String currency, boolean showToolbar){
		super();
		this.showToolbar = showToolbar;
		this.companyBaseCurrency = currency;
		this.tenderAnalysisComparisonWrapper = tenderAnalysisComparison;
		this.detailSectionController = detailSectionController;
		globalSectionController=detailSectionController.getGlobalSectionController();
		userAccessRightsRepository=globalSectionController.getUserAccessRightsRepository();
		globalMessageTicket = new GlobalMessageTicket();
		if(tenderAnalysisComparisonWrapper != null){
			this.packageNo = tenderAnalysisComparisonWrapper.getPackageNo();
			this.packageStatus = tenderAnalysisComparisonWrapper.getPackageStatus();
		}
		
		scPackage = globalSectionController.getMainSectionController().getPackageEditorFormPanel().getScPackage();
		jobNumber = globalSectionController.getJob().getJobNumber();
		UIUtil.maskPanelById(detailSectionController.getMainPanel().getId(), GlobalParameter.LOADING_MSG, true);
		try {
			globalSectionController.getScPaymentCertRepository().obtainSCPaymentCertListByPackageNo(jobNumber, Integer.valueOf(packageNo), new AsyncCallback<List<SCPaymentCert>>(){
				@Override
				public void onFailure(Throwable e) {
					scPaymentCertList = null;
					UIUtil.throwException(e);
					UIUtil.unmaskPanelById(detailSectionController.getMainPanel().getId());
				}

				@Override
				public void onSuccess(List<SCPaymentCert> result) {
					scPaymentCertList = result;
					UIUtil.unmaskPanelById(detailSectionController.getMainPanel().getId());
				}
				
			});
		} catch (NumberFormatException | DatabaseOperationException e1) {
			e1.printStackTrace();
		}
		paymentRequestStatus = globalSectionController.getPackageEditorFormPanel().getPaymentRequestStatus().trim();
		
		this.setAutoScroll(true);
		
		final Renderer quantityRenderer = new QuantityRenderer(this.detailSectionController.getUser());
		final Renderer amountRenderer = new AmountRendererCheckIfTotal(this.detailSectionController.getUser());

		ColumnConfig billItemColConfig = new ColumnConfig("B/P/I", "billItem", 80, false);
		ColumnConfig objectCodeColConfig = new ColumnConfig("Object Code", "objectCode", 50, false);
		ColumnConfig subsidiaryCodeColConfig = new ColumnConfig("Subsidiary Code", "subsidiaryCode", 65, false);
		ColumnConfig resourceDescriptionColConfig = new ColumnConfig("Resource Description", "description", 200, false);
		ColumnConfig unitColConfig = new ColumnConfig("Unit", "unit", 35, false);
		ColumnConfig quantityColConfig = new ColumnConfig("Quantity", "quantity", 100, false);
		quantityColConfig.setRenderer(quantityRenderer);
		quantityColConfig.setAlign(TextAlign.RIGHT);

		// added by brian on 20110223
		ColumnConfig budgetedRateColConfig = new ColumnConfig("Budgeted Amount<br/>(" + currency + ")", "budgetedAmount", 120, false);
		budgetedRateColConfig.setRenderer(amountRenderer);
		budgetedRateColConfig.setAlign(TextAlign.RIGHT);

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		columns.add(billItemColConfig);
		columns.add(objectCodeColConfig);
		columns.add(subsidiaryCodeColConfig);
		columns.add(resourceDescriptionColConfig);
		columns.add(unitColConfig);
		columns.add(quantityColConfig);
		columns.add(budgetedRateColConfig);

		List<FieldDef> fields = new ArrayList<FieldDef>();
		fields.add(new StringFieldDef("billItem"));
		fields.add(new StringFieldDef("objectCode"));
		fields.add(new StringFieldDef("subsidiaryCode"));
		fields.add(new StringFieldDef("description"));
		fields.add(new StringFieldDef("unit"));
		fields.add(new StringFieldDef("quantity"));
		fields.add(new StringFieldDef("budgetedAmount"));
		fields.add(new IntegerFieldDef("sequenceNo"));

		//Add columns and fields for vendors
		if(tenderAnalysisComparisonWrapper != null){
			int counter = 0;
			for(TenderAnalysisVendorWrapper vendorWrapper : tenderAnalysisComparisonWrapper.getVendorWrappers()){
				counter += 1;
				String vendorNo = vendorWrapper.getVendorNo().toString();
				String vendorName = vendorWrapper.getVendorName();
				if(vendorName == null || vendorName.length() == 0)
					vendorName = vendorNo;
				String currencyCode = vendorWrapper.getCurrencyCode();
				String exchangeRate = vendorWrapper.getExchangeRate().toString();
				String title = vendorName + "<br/>(" + currencyCode + " - " + exchangeRate + ")";
				/*if(packageStatus.intValue() == 330 && vendorWrapper.getStatus() != null && vendorWrapper.getStatus().equals("RCM")){
					title = "<b style=\"color:red;\">" + title + "</b>";
				}
				else */if(vendorWrapper.getStatus() != null && vendorWrapper.getStatus().equals("RCM")){
					this.selectedVendorNo = String.valueOf(vendorWrapper.getVendorNo());
					highlightedColInd = 6+counter; //6 refers to first 6 base columns
					highlightedHeader=title;
				}
				
				/* highlight the awarded tender in red if the package has been awarded - matthewatc 3/2/12 */
				if(packageStatus != null && packageStatus >= 500 && vendorWrapper.getStatus().equals("AWD")) {
					title = "<b style=\"color:red;\">" + title + "</b>";
				}

				ColumnConfig vendorColumn = new ColumnConfig(title, vendorNo, 120, false);
				vendorColumn.setRenderer(new Renderer(){
					public String render(Object value, CellMetadata cellMetadata,
							Record record, int rowIndex, int colNum, Store store) {
						String str = amountRenderer.render(value, cellMetadata, record, rowIndex, colNum, store);
						if(value != null && record.getAsBoolean("isCustom"))
							str = "<span style=\"color:blue;\">" + str + "</span>";
						return str;
					}
				});
				vendorColumn.setAlign(TextAlign.RIGHT);
				columns.add(vendorColumn);
				fields.add(new StringFieldDef(vendorNo));
			}
		}
		recordDef = new RecordDef(fields.toArray(new FieldDef[]{}));

		this.setColumnModel(new ColumnModel(columns.toArray(new ColumnConfig[]{})));
		this.setEnableColumnMove(false);
		this.setEnableHdMenu(false);

		/* don't enable the highlight-on-click behavior for the column headers if the package has already been awarded - matthewatc 3/2/12 */
		if(packageStatus != null && packageStatus.intValue() != 330 && packageStatus < 500){
			this.addGridHeaderListener(new GridHeaderListenerAdapter(){
				//on header select, if header is a vendor amount column, change color to red to show that it is selected (for approval)
				public void onHeaderClick(GridPanel grid, int colIndex, EventObject e){
					//if col is one of the base detail columns, do nothing
					if(colIndex < 7)
						return;
					
					//reset the currently highlighted col
					if(highlightedColInd > 6){
						grid.getColumnModel().setColumnHeader(highlightedColInd, highlightedHeader);
						if(colIndex == highlightedColInd){
							highlightedColInd = 0;
							return;
						}
					}
					highlightedColInd = colIndex;
					highlightedHeader = grid.getColumnModel().getColumnHeader(colIndex);
					selectedVendorNo = grid.getColumnModel().getDataIndex(colIndex);
					String redHeader = "<b style=\"color:red\">" + highlightedHeader + "</b>";
					grid.getColumnModel().setColumnHeader(colIndex, redHeader);
				}
			});
		}

		/* all the toolbar code is now wrapped in this if block - matthewatc 3/2/12 */
		if(showToolbar) {
			Toolbar toolbar = new Toolbar();
			awardButton = new ToolbarButton();
			awardButton.setText("Award Subcontract");
			awardButton.setIconCls("award-icon");
			awardButton.setTooltip("Award subcontract", "Select a vendor by clicking on the appropriate column header");
			awardButton.addListener(new ButtonListenerAdapter(){
				public void onClick(Button button, EventObject e){
					globalMessageTicket.refresh();
					if(packageStatus != null && packageStatus.equals(Integer.valueOf(330))){
						MessageBox.alert("This package has already been submitted for approval");
						return;
					}
					if(highlightedColInd == 0){
						MessageBox.alert("No vendor is selected. Please select a vendor by clicking on the appropriate column header");
						return;	
					}
					selectedVendorNo = getColumnModel().getDataIndex(highlightedColInd);
					for(TenderAnalysisVendorWrapper curWrapper: tenderAnalysisComparisonWrapper.getVendorWrappers()){
						if(curWrapper.getVendorNo().toString().trim().equals(selectedVendorNo.trim())){
							selectedCurrencyCode = curWrapper.getCurrencyCode();
							selectedVendorName = curWrapper.getVendorName();
							break;
						}
					}					
					
					UIUtil.maskPanelById(TenderAnalysisComparisonPanel.this.getId(), GlobalParameter.LOADING_MSG, true);
					UIUtil.maskPanelById(PackageEditorFormPanel.PACKAGE_MAIN_PANEL, GlobalParameter.LOADING_MSG, true);
					awardSubcontractor();
				}
			});
			editVendorRatesButton = new ToolbarButton();
			editVendorRatesButton.setText("Edit Feedback Rates");
			editVendorRatesButton.setTooltip("Edit Vendor Feedback Rates", "Select a vendor by clicking on the appropriate column header");
			editVendorRatesButton.setIconCls("edit-button-icon");
			editVendorRatesButton.addListener(new ButtonListenerAdapter(){
				public void onClick(Button button, EventObject e){
					globalMessageTicket.refresh();
					
					if(paymentRequestStatus.equals(SCPaymentCert.PAYMENTSTATUS_SBM_SUBMITTED) 
							|| paymentRequestStatus.equals(SCPaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING) 
							|| paymentRequestStatus.equals(SCPaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW)){

						MessageBox.alert("Payment Requisition Submitted. Vendor Feedback Rate will be frozen.");
					}else{
						if(highlightedColInd == 0){
							MessageBox.alert("No vendor is selected. Please select a vendor by clicking on the appropriate column header");
							return;	
						}
						final String vendorNo = getColumnModel().getDataIndex(highlightedColInd);
						SessionTimeoutCheck.renewSessionTimer();
						globalSectionController.getTenderAnalysisRepository().insertTenderAnalysisDetailForVendor(globalSectionController.getJob(), packageNo, Integer.valueOf(vendorNo), new AsyncCallback<Boolean>(){
							public void onSuccess(Boolean result) {
								globalSectionController.showInputVendorFeedbackRateWindow(packageNo, Integer.valueOf(vendorNo));
								globalSectionController.populateTenderAnalysisComparisonPanel(packageNo);
							}
							public void onFailure(Throwable e) {
								UIUtil.throwException(e);
							}
						});
					}
					
				}
			});
			removeVendorButton = new ToolbarButton("Remove Vendor Feedback");
			removeVendorButton.setTooltip("Remove Vendor Feedback", "Select a vendor by clicking on the appropriate column header");
			removeVendorButton.setIconCls("cancel-icon");
			removeVendorButton.addListener(new ButtonListenerAdapter(){
				public void onClick(Button button, EventObject e){
					globalMessageTicket.refresh();
					
					if(paymentRequestStatus.equals(SCPaymentCert.PAYMENTSTATUS_SBM_SUBMITTED) 
							|| paymentRequestStatus.equals(SCPaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING) 
							|| paymentRequestStatus.equals(SCPaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW)){

						MessageBox.alert("Payment Requisition Submitted. Vendor Feedback will be frozen.");
					}else{
						if(highlightedColInd == 0){
							MessageBox.alert("No vendor is selected. Please select a vendor by clicking on the appropriate column header");
							return;	
						}
						final Integer vendorNo = Integer.valueOf(getColumnModel().getDataIndex(highlightedColInd));
						MessageBox.confirm("Confirmation", "Are you sure you want to remove this vendor's feedback rates?", new MessageBox.ConfirmCallback(){
							public void execute(String btnID) {
								if(btnID.equals("yes"))
									globalSectionController.deleteTenderAnalysis(packageNo, vendorNo);
							}					
						});
					}
				}
			});
			downloadXlsButton = new ToolbarButton();
			downloadXlsButton.setText("Export to Excel");
			downloadXlsButton.setTooltip("Export TA details to Excel", "Create template spreadsheet used for vendor feedback");
			downloadXlsButton.setIconCls("download-icon");
			downloadXlsButton.addListener(new ButtonListenerAdapter() {
				public void onClick(Button button, EventObject e) {
					globalMessageTicket.refresh();
					String jobNumber = globalSectionController.getJob().getJobNumber();
					String vendorNo = highlightedColInd == 0 ? "0" : getColumnModel().getDataIndex(highlightedColInd);
					com.google.gwt.user.client.Window.open(GlobalParameter.TENDER_ANALYSIS_VENDOR_EXCEL_DOWNLOAD_URL + "?jobNumber="+jobNumber +"&packageNo="+packageNo+"&vendorNo="+vendorNo, "_blank", "");
				}	
			});
			uploadVendorXlsButton = new ToolbarButton();
			uploadVendorXlsButton.setText("Import Vendor Feedback");
			uploadVendorXlsButton.setTooltip("Import Vendor Feedback", "Import vendor feedback using the provided template (from 'Export to Excel')");
			uploadVendorXlsButton.setIconCls("upload-icon");
			uploadVendorXlsButton.addListener(new ButtonListenerAdapter() {
				public void onClick(Button button, EventObject e) {
					globalMessageTicket.refresh();
				
					if(scPackage.getPaymentStatus()!=null && SCPackage.DIRECT_PAYMENT.equals(scPackage.getPaymentStatus())){
						MessageBox.alert("Payment Requisition does not support 'Import from Excel' function.");
						return;
					}
					
					showImportWindow();
				}	
			});

			/**
			 * @author koeyyeung
			 * added on 20 Dec, 214
			 * Payment Requisition Revamp**/
			/*-------------------------- Addendum -------------------------*/
			addAddendumButton = new Item("Add");
			addAddendumButton.setIconCls("add-button-icon");
			addAddendumButton.addListener(new BaseItemListenerAdapter(){
				public void onClick(BaseItem item, EventObject e) {
					if(globalSectionController.getCurrentWindow() == null){
						globalSectionController.setCurrentWindow(new AddSCAddendumWindow(globalSectionController ,  globalSectionController.getJob().getJobNumber(), globalSectionController.getJob().getDescription(),  Integer.valueOf(packageNo), true));
						globalSectionController.getCurrentWindow().show();
					}
				}
			});
			
			Menu addendumMenu = new Menu();
			addendumMenu.addItem(addAddendumButton);
			
			addendumToolbarButton = new ToolbarButton("Addendum");
			addendumToolbarButton.setMenu(addendumMenu);
			addendumToolbarButton.setIconCls("menu-show-icon");
			/*-------------------------- Addendum -------------------------*/
			
			/*------------------------- Work Done -------------------------*/
			updateCumWDQtyButton = new Item("Update");
			updateCumWDQtyButton.setIconCls("save-button-icon");
			updateCumWDQtyButton.addListener(new BaseItemListenerAdapter() {
				public void onClick(BaseItem item, EventObject e) {
					globalMessageTicket.refresh();
					if(selectedVendorNo!=null && !selectedVendorNo.equals(scPackage.getVendorNo())){
						MessageBox.alert("Vendor must be matched with the one in previous Payment Requisition.");
						return;	
					}

					if(highlightedColInd == 0 && selectedVendorNo==null){
						MessageBox.alert("No vendor is selected. Please select a vendor by clicking on the appropriate column header");
						return;	
					}
					//generate SC details
					UIUtil.maskPanelById(TenderAnalysisComparisonPanel.this.getId(), GlobalParameter.LOADING_MSG, true);
					UIUtil.maskPanelById(PackageEditorFormPanel.PACKAGE_MAIN_PANEL, GlobalParameter.LOADING_MSG, true);
					SessionTimeoutCheck.renewSessionTimer();
					globalSectionController.getPackageRepository().generateSCDetailsForPaymentRequisition(globalSectionController.getJob().getJobNumber(), packageNo, selectedVendorNo, new AsyncCallback<String>() {
						public void onSuccess(String resultString) {
							UIUtil.unmaskPanelById(TenderAnalysisComparisonPanel.this.getId());
							UIUtil.unmaskPanelById(PackageEditorFormPanel.PACKAGE_MAIN_PANEL);
							if(resultString==null ||resultString.length()==0){
								globalSectionController.populateSCPackageMainPanelandDetailPanel(packageNo);
								globalSectionController.showUpdateWDandCertQtyForPaymentReqWindow("Cum WD Qty");
							}
							else
								MessageBox.alert(resultString);
						}
						public void onFailure(Throwable e) {
							UIUtil.unmaskPanelById(TenderAnalysisComparisonPanel.this.getId());
							UIUtil.unmaskPanelById(PackageEditorFormPanel.PACKAGE_MAIN_PANEL);
							UIUtil.throwException(e);
						}
					});
				}
			});

			ToolTip updateCumWDQtyToolTip = new ToolTip();
			updateCumWDQtyToolTip.setTitle("Update Work Done Qty");
			updateCumWDQtyToolTip.setHtml("Search SC Detail Information and Mass Update for Cum WD Qty");
			updateCumWDQtyToolTip.setDismissDelay(15000);
			updateCumWDQtyToolTip.setWidth(200);
			updateCumWDQtyToolTip.setTrackMouse(true);
			updateCumWDQtyToolTip.applyTo(updateCumWDQtyButton);
			
			Menu wdMenu = new Menu();
			wdMenu.addItem(updateCumWDQtyButton);
			
			workDoneToolbarButton = new ToolbarButton("Work Done");
			workDoneToolbarButton.setMenu(wdMenu);
			workDoneToolbarButton.setIconCls("menu-show-icon");
			/*------------------------- Work Done -------------------------*/
			
			/*------------------------- Certificate -------------------------*/
			updateCumCertifiedQtyButton = new Item("Update");
			updateCumCertifiedQtyButton.setIconCls("save-button-icon");
			updateCumCertifiedQtyButton.addListener(new BaseItemListenerAdapter() {
				public void onClick(BaseItem item, EventObject e) {
					globalMessageTicket.refresh();
					
					if(scPackage.getSubcontractStatus()==null ||scPackage.getSubcontractStatus()<160){
						MessageBox.alert("Please Input Tender Analysis.");
						return;	
					}
					
					String alertMessage = "Would you like to generate a new Subcontract Payment Requisition for Non-Awarded Subcontract "+packageNo+"(Special approval route NP is required)?";
					MessageBox.confirm("Payment Requsition", alertMessage,
							new MessageBox.ConfirmCallback(){
						public void execute(String btnID) {
							if(btnID.equals("yes")){
								if(highlightedColInd == 0 && selectedVendorNo==null){
									MessageBox.alert("No vendor is selected. Please select a vendor by clicking on the appropriate column header");
									return;	
								}

								//generate SC details
								UIUtil.maskPanelById(TenderAnalysisComparisonPanel.this.getId(), GlobalParameter.LOADING_MSG, true);
								UIUtil.maskPanelById(PackageEditorFormPanel.PACKAGE_MAIN_PANEL, GlobalParameter.LOADING_MSG, true);
								SessionTimeoutCheck.renewSessionTimer();
								globalSectionController.getPackageRepository().generateSCDetailsForPaymentRequisition(globalSectionController.getJob().getJobNumber(), packageNo, selectedVendorNo, new AsyncCallback<String>() {									public void onSuccess(String resultString) {
										UIUtil.unmaskPanelById(TenderAnalysisComparisonPanel.this.getId());
										UIUtil.unmaskPanelById(PackageEditorFormPanel.PACKAGE_MAIN_PANEL);
										if(resultString==null ||resultString.length()==0){
												globalSectionController.populateSCPackageMainPanelandDetailPanel(packageNo);
												globalSectionController.showUpdateWDandCertQtyForPaymentReqWindow("Cum Certified Qty");
											}
										else
											MessageBox.alert(resultString);
										
									}
									public void onFailure(Throwable e) {
										UIUtil.unmaskPanelById(TenderAnalysisComparisonPanel.this.getId());
										UIUtil.unmaskPanelById(PackageEditorFormPanel.PACKAGE_MAIN_PANEL);
										UIUtil.throwException(e);
									}
								});
							}
						}
					});
				}
			});
			ToolTip updateCumCertifiedQtyToolTip = new ToolTip();
			updateCumCertifiedQtyToolTip.setTitle("Update Certified Qty");
			updateCumCertifiedQtyToolTip.setHtml("Search SC Detail Information and Mass Update for Cum Certified Qty");
			updateCumCertifiedQtyToolTip.setDismissDelay(15000);
			updateCumCertifiedQtyToolTip.setWidth(200);
			updateCumCertifiedQtyToolTip.setTrackMouse(true);
			updateCumCertifiedQtyToolTip.applyTo(updateCumCertifiedQtyButton);
			
			Menu certMenu = new Menu();
			certMenu.addItem(updateCumCertifiedQtyButton);
			
			certificateToolbarButton = new ToolbarButton("Certificate");
			certificateToolbarButton.setMenu(certMenu);
			certificateToolbarButton.setIconCls("menu-show-icon");
			/*------------------------- Certificate -------------------------*/
			
			
			recalculateIVButton = new ToolbarButton("Recalculate Resource Summary IV");
			recalculateIVButton.setTooltip("Recalculate Resource IV", "Recalculate Resource Summary IV amounts from corresponding SC Details");
			recalculateIVButton.setIconCls("calculator-icon");
			recalculateIVButton.addListener(new ButtonListenerAdapter() {
				public void onClick(Button button, EventObject e){
					globalMessageTicket.refresh();
					globalSectionController.getDetailSectionController().recalculateIV(globalSectionController.getJob(), packageNo);
				}
			});
			
			
			awardButton.setVisible(false);
			editVendorRatesButton.setVisible(false);
			removeVendorButton.setVisible(false);
			downloadXlsButton.setVisible(false);
			uploadVendorXlsButton.setVisible(false);
			addendumToolbarButton.setVisible(false);
			workDoneToolbarButton.setVisible(false);
			certificateToolbarButton.setVisible(false);
			recalculateIVButton.setVisible(false);
			
			//Disabled if status < 160
			if(tenderAnalysisComparisonWrapper == null || scPackage.getSubcontractStatus()==null ||scPackage.getSubcontractStatus()<160){
				awardButton.setDisabled(true);
				editVendorRatesButton.setDisabled(true);
				removeVendorButton.setDisabled(true);
				downloadXlsButton.setDisabled(true);
				uploadVendorXlsButton.setDisabled(true);
				addendumToolbarButton.setDisabled(true);
				workDoneToolbarButton.setDisabled(true);
				certificateToolbarButton.setDisabled(true);
				recalculateIVButton.setDisabled(true);
			}
			
			
			// Check for access rights - then add toolbar buttons accordingly
			UIUtil.maskPanelById(detailSectionController.getMainPanel().getId(), GlobalParameter.LOADING_MSG, true);
			SessionTimeoutCheck.renewSessionTimer();
			userAccessRightsRepository.getAccessRights(detailSectionController.getUser().getUsername(), RoleSecurityFunctions.F010204_TENDER_ANALYSIS_COMPARISONPANEL, new AsyncCallback<List<String>>(){
				public void onSuccess(List<String> accessRightsReturned) {
					accessRightsList = accessRightsReturned;
					securitySetup();
					if(packageStatus != null && packageStatus.equals(Integer.valueOf(330)))
						setupAfterAward();
					else{
						if(paymentRequestStatus.equals(SCPaymentCert.PAYMENTSTATUS_SBM_SUBMITTED) 
								|| paymentRequestStatus.equals(SCPaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING) 
								|| paymentRequestStatus.equals(SCPaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW)){

							setupAfterPaymentSubmitted();
						}
						if(scPaymentCertList!=null && scPaymentCertList.size()>0)
							uploadVendorXlsButton.disable();
					}
					UIUtil.unmaskPanelById(detailSectionController.getMainPanel().getId());
				}
				public void onFailure(Throwable e) {
					UIUtil.throwException(e);
					UIUtil.unmaskPanelById(detailSectionController.getMainPanel().getId());
				}
			});

			toolbar.addButton(awardButton);
			toolbar.addSeparator();
			toolbar.addButton(editVendorRatesButton);
			toolbar.addSeparator();
			toolbar.addButton(removeVendorButton);
			toolbar.addSeparator();
			toolbar.addButton(downloadXlsButton);
			toolbar.addSeparator();
			toolbar.addButton(uploadVendorXlsButton);
			toolbar.addSeparator();
			toolbar.addButton(addendumToolbarButton);
			toolbar.addSeparator();
			toolbar.addButton(workDoneToolbarButton);
			toolbar.addSeparator();
			toolbar.addButton(certificateToolbarButton);
			toolbar.addSeparator();
			toolbar.addButton(recalculateIVButton);
			toolbar.addSeparator();
			this.setTopToolbar(toolbar);
		}

		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(recordDef);
		dataStore = new Store(proxy, reader);
		dataStore.setDefaultSort("sequenceNo", SortDir.ASC);
		dataStore.load();
		this.setStore(dataStore);

		
		if(highlightedColInd!=0 && !"".equals(highlightedHeader)){
			String redHeader = "<b style=\"color:red\">" + highlightedHeader + "</b>";
			this.getColumnModel().setColumnHeader(highlightedColInd, redHeader);
		}
			
		
		populateGrid(tenderAnalysisComparisonWrapper);
	
		
		//Validations for WorkDone and Cert Update
		//Should only be activated with at least one payment which status is in APR
		if(packageNo!=null && packageStatus != null && packageStatus >= 160){
			UIUtil.maskPanelById(detailSectionController.getMainPanel().getId(), GlobalParameter.LOADING_MSG, true);
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getPaymentRepository().obtainSCPaymentCertListByStatus(globalSectionController.getJob().getJobNumber(), packageNo, SCPaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE, SCPaymentCert.DIRECT_PAYMENT, new AsyncCallback<List<SCPaymentCert>>() {
				public void onFailure(Throwable e) {
					UIUtil.throwException(e);
					UIUtil.unmaskPanelById(detailSectionController.getMainPanel().getId());
				}
				public void onSuccess(List<SCPaymentCert> postedSCPaymentList) {
					if(getShowToolbar() && postedSCPaymentList.size()==0){
						addendumToolbarButton.setDisabled(true);
						workDoneToolbarButton.setDisabled(true);
						recalculateIVButton.setDisabled(true);
					}
					UIUtil.unmaskPanelById(detailSectionController.getMainPanel().getId());
				}
			});
		}

		//At least one subcontractor should be present for making direct payment
		if(getShowToolbar() && TenderAnalysisComparisonPanel.this.getColumnModel().getColumnCount() < 8 || packageStatus == null || packageStatus < 160){
			certificateToolbarButton.setDisabled(true);
		}
		
	}
	
	/**
	 * Award subcontractor
	 * warn user but allow continue process 
	 * if scFinancialAlert is not null
	 * @author paulnpyiu
	 * @since 21 Feb 2016
	 */
	private void awardSubcontractor() {
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getMasterListRepository().obtainAllVendorList(selectedVendorNo, new AsyncCallback<List<MasterListVendor>>() {
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(TenderAnalysisComparisonPanel.this.getId());
				UIUtil.unmaskPanelById(PackageEditorFormPanel.PACKAGE_MAIN_PANEL);
				UIUtil.alert(e.getMessage());
			}
			public void onSuccess(List<MasterListVendor> masterListVendors) {
				if(masterListVendors!=null && masterListVendors.size()>0){
					String scFinancialAlert = masterListVendors.get(0).getScFinancialAlert();
					if(scFinancialAlert!= null && !"".equals(scFinancialAlert)){
						MessageBox.confirm("SC Credit Warning", globalSectionController.getSubcontractHoldMessage(),
								new MessageBox.ConfirmCallback() {
									public void execute(String btnID) {
										if(btnID.equals("yes")){
											checkForeignCurrencyAndConfirmAward();
										}
									}
								});
						UIUtil.unmaskPanelById(TenderAnalysisComparisonPanel.this.getId());
						UIUtil.unmaskPanelById(PackageEditorFormPanel.PACKAGE_MAIN_PANEL);
					} else {
						checkForeignCurrencyAndConfirmAward();
					}
				}else{
					MessageBox.alert("Vendor: "+selectedVendorNo +"cannot be found in address book.");
					UIUtil.unmaskPanelById(TenderAnalysisComparisonPanel.this.getId());
					UIUtil.unmaskPanelById(PackageEditorFormPanel.PACKAGE_MAIN_PANEL);
				}
			}
		});
	}
	
	/**
	 * Check foreign currency
	 * warn user but allow continue process 
	 * if subcontractor's currency is not same as jobsite's base currency
	 * @author paulnpyiu
	 * @since 21 Feb 2016
	 */
	private void checkForeignCurrencyAndConfirmAward(){
		if(!selectedCurrencyCode.equals(companyBaseCurrency)){
			UIUtil.unmaskPanelById(TenderAnalysisComparisonPanel.this.getId());
			UIUtil.unmaskPanelById(PackageEditorFormPanel.PACKAGE_MAIN_PANEL);
			String foreignCurrentAlert = "The currency is in " + selectedCurrencyCode + ", are you sure to continue?";
			MessageBox.confirm("Foreign Current", foreignCurrentAlert,
					new MessageBox.ConfirmCallback(){
				public void execute(String btnID) {
					if(btnID.equals("yes")){
						confirmAwardToSubcontractor();
					} 
				}
			});
		} else {
			confirmAwardToSubcontractor();
		}
	}

	/**
	 * Confirm award subcontractor
	 * Award subcontractor if user confirm to process
	 * @author paulnpyiu
	 * @since 21 Feb 2016
	 */
	private void confirmAwardToSubcontractor(){
		final String alertMessage = "Are you sure that you want to award this subcontract to vendor " + selectedVendorNo + " (" + selectedVendorName + ")?";
		//check whether the SC Document signed or not
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPackageRepository().getSCPackage(globalSectionController.getJob().getJobNumber(), packageNo, new AsyncCallback<SCPackage>() {
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(TenderAnalysisComparisonPanel.this.getId());
				UIUtil.unmaskPanelById(PackageEditorFormPanel.PACKAGE_MAIN_PANEL);
				UIUtil.alert(e.getMessage());
			}
			public void onSuccess(final SCPackage scPackage) {
				UIUtil.unmaskPanelById(TenderAnalysisComparisonPanel.this.getId());
				UIUtil.unmaskPanelById(PackageEditorFormPanel.PACKAGE_MAIN_PANEL);
				//Confirm that user wants to award sc to selected vendor, then award it..				
				MessageBox.confirm("Award Subcontract", alertMessage,
						new MessageBox.ConfirmCallback(){
					public void execute(String btnID) {
						if(btnID.equals("yes")){
							if (globalSectionController.getCurrentWindow()==null){
								globalSectionController.setCurrentWindow(new EditSCDatesWindow(globalSectionController, scPackage, true));
								globalSectionController.getCurrentWindow().show();
							}
						}
					}
				});
			}
		});
	}

	private void populateGrid(TenderAnalysisComparisonWrapper tenderAnalysisComparison){
		if(tenderAnalysisComparison == null)
			return;
		Map<TenderAnalysisDetailWrapper, Map<Integer, Double>> detailWrapperMap = tenderAnalysisComparison.getDetailWrapperMap();
		List<TenderAnalysisVendorWrapper> vendors = tenderAnalysisComparison.getVendorWrappers();
		//Map to keep track of total budget/amounts
		Map<Integer, Double> totals = new HashMap<Integer, Double>();
		totals.put(Integer.valueOf(0), Double.valueOf(0.0));
		for(TenderAnalysisVendorWrapper vendor : vendors){
			totals.put(vendor.getVendorNo(), Double.valueOf(0.0));
		}
		dataStore.setSortInfo(new SortState("sequenceNo", SortDir.ASC));

		List<TenderAnalysisDetailWrapper> details = new ArrayList<TenderAnalysisDetailWrapper>(detailWrapperMap.keySet());
		Collections.sort(details, new Comparator<TenderAnalysisDetailWrapper>(){
			public int compare(TenderAnalysisDetailWrapper o1,
					TenderAnalysisDetailWrapper o2) {
				if(!o1.getLineType().equals(o2.getLineType())){
					if("BQ".equals(o1.getLineType()))
						return -1;
					else
						return 1;
				}
				if(o1.getBillItem() != null && o2.getBillItem() != null){
					int billComp = o1.getBillItem().compareTo(o2.getBillItem());
					if(billComp != 0)
						return billComp;
				}
				int objComp = o1.getObjectCode().compareTo(o2.getObjectCode());
				if(objComp != 0)
					return objComp;
				int subComp = o1.getSubsidiaryCode().compareTo(o2.getSubsidiaryCode());
				if(subComp != 0)
					return subComp;
				return o1.getDescription().compareTo(o2.getDescription());
			}
		});

		for(TenderAnalysisDetailWrapper detail : details){
			Map<Integer, Double> vendorRates = detailWrapperMap.get(detail);
			//fill in base details, including budgeted amount
			Record record = recordDef.createRecord(new Object[recordDef.getFields().length]);
			record.set("billItem", detail.getBillItem());
			record.set("objectCode", detail.getObjectCode());
			record.set("subsidiaryCode", detail.getSubsidiaryCode());
			record.set("description", detail.getDescription());
			record.set("unit", detail.getUnit());
			Double quantity = (detail.getQuantity() == null) ? Double.valueOf(0) : detail.getQuantity();
			Double rate = vendorRates.remove(Integer.valueOf(0));
			Double amount = Double.valueOf(0);
			if(rate==null){
				record.set("isCustom", true);
			} 
			else{
				amount = quantity*rate;
				record.set("budgetedAmount", amount.toString());
			}
			record.set("quantity", quantity.toString());			
			record.set("sequenceNo", detail.getSequenceNo());
			Double baseTotal = totals.get(Integer.valueOf(0));
			baseTotal += amount;
			totals.put(Integer.valueOf(0), baseTotal);
			//fill in vendor feedback amounts
			for(Map.Entry<Integer, Double> vendorNoRate : vendorRates.entrySet()){
				Integer vendorNo = vendorNoRate.getKey();
				Double vendorRate = (vendorNoRate.getValue()== null) ? Double.valueOf(0.0) : vendorNoRate.getValue();
				Double vendorAmount = quantity*vendorRate;
				record.set(vendorNo.toString(), vendorAmount.toString());
				Double vendorTotal = totals.get(vendorNo);
				vendorTotal += vendorAmount;
				totals.put(vendorNo, vendorTotal);
			}
			record.commit(); //Have to commit individual records. store.commitChanges() doesn't commit newly added records 
			dataStore.add(record);
		}
		//Row showing totals
		Record totalsRecord = recordDef.createRecord(new Object[recordDef.getFields().length]);
		Double budgetedTotal = totals.remove(Integer.valueOf(0));
		totalsRecord.set("budgetedAmount", budgetedTotal.toString());
		for(Map.Entry<Integer, Double> vendorTotal: totals.entrySet()){
			totalsRecord.set(vendorTotal.getKey().toString(), vendorTotal.getValue().toString());
		}
		totalsRecord.set("isTotal", true);
		totalsRecord.commit();
		dataStore.add(totalsRecord);
	}

	private void securitySetup(){
		if (getShowToolbar() && accessRightsList.contains("WRITE")){
			awardButton.setVisible(true);
			editVendorRatesButton.setVisible(true);
			removeVendorButton.setVisible(true);
			downloadXlsButton.setVisible(true);
			uploadVendorXlsButton.setVisible(true);
			addendumToolbarButton.setVisible(true);
			workDoneToolbarButton.setVisible(true);
			certificateToolbarButton.setVisible(true);
			recalculateIVButton.setVisible(true);
		}
		if(getShowToolbar() && accessRightsList.contains("READ")){
			downloadXlsButton.setVisible(true);
		}
	}

	private void setupAfterAward(){
		if(getShowToolbar() ){
			awardButton.disable();
			editVendorRatesButton.disable();
			removeVendorButton.disable();
			uploadVendorXlsButton.disable();
		}
	}
	
	private void setupAfterPaymentSubmitted(){
		if(getShowToolbar() ){
			editVendorRatesButton.disable();
			removeVendorButton.disable();
			uploadVendorXlsButton.disable();
		}
	}

	private void showImportWindow(){
		importWindow = new Window();
		importWindow.setLayout(new FitLayout());
		importWindow.setWidth(250);
		importWindow.setTitle("Import Vendor Feedback from Excel");
		importWindow.setModal(true);

		final FormPanel uploadPanel = new FormPanel();
		uploadPanel.setHeight(25);
		uploadPanel.setPaddings(0, 2, 2, 0);
		uploadPanel.setFileUpload(true);
		final TextField fileTextField = new TextField("File", "file");
		fileTextField.setHideLabel(true);
		fileTextField.setInputType("file");
		fileTextField.setAllowBlank(false);
		uploadPanel.add(fileTextField);
		uploadPanel.addFormListener(new FormListenerAdapter(){
			public void onActionComplete(Form form, int httpStatus, String responseText) {
				globalSectionController.getTenderAnalysisVendorUpload();
				importWindow.close();
			}

			public void onActionFailed(Form form, int httpStatus, String responseText) {
				JSONValue jsonValue = JSONParser.parse(responseText);
				JSONObject jsonObj = jsonValue.isObject();
				MessageBox.alert(jsonObj.get("error").isString().stringValue());
			}
		});

		Button importButton = new Button("Import");
		importButton.setCls("table-cell");
		importButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				uploadPanel.getForm().submit(GlobalParameter.TENDER_ANALYSIS_VENDOR_EXCEL_UPLOAD_URL, null, Connection.POST, "Uploading...", false);
			}});
		Button closeButton = new Button("Close");
		closeButton.setCls("table-cell");
		closeButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				importWindow.close();
			}});
		importWindow.setButtons(new Button[]{importButton, closeButton});
		importWindow.add(uploadPanel);
		importWindow.show();
	}
	

	public void awardSC(){
		//award Subcontract
		UIUtil.maskPanelById(TenderAnalysisComparisonPanel.this.getId(), "Submitting...", true);
		UIUtil.maskPanelById(PackageEditorFormPanel.PACKAGE_MAIN_PANEL, "Submitting...", true);
		Job job = globalSectionController.getJob();//.getJobNumber();
		String subcontractNo = globalSectionController.getSelectedPackageNumber();
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPackageRepository().submitAwardApproval(job.getJobNumber(), subcontractNo, selectedVendorNo ,selectedCurrencyCode, detailSectionController.getUser().getUsername(),new AsyncCallback<String>(){
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(TenderAnalysisComparisonPanel.this.getId());
				UIUtil.unmaskPanelById(PackageEditorFormPanel.PACKAGE_MAIN_PANEL);
			}
			public void onSuccess(String resultMsg) {
				if (resultMsg.length() == 0){
					MessageBox.alert("Award Approval has been submitted.");
					globalSectionController.populateSCPackageMainPanelandDetailPanel(globalSectionController.getSelectedPackageNumber());
				}else{
					MessageBox.alert(resultMsg);
				}
				UIUtil.unmaskPanelById(TenderAnalysisComparisonPanel.this.getId());
				UIUtil.unmaskPanelById(PackageEditorFormPanel.PACKAGE_MAIN_PANEL);
			}
		});
	}
	
	public boolean getShowToolbar() {
		return showToolbar;
	}

}
