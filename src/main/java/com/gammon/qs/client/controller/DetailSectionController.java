package com.gammon.qs.client.controller;

import java.util.List;

import com.gammon.qs.application.User;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.detailSection.AccountReceivableDetailPanel;
import com.gammon.qs.client.ui.panel.detailSection.MainCertificateContraChargeDetailPanel;
import com.gammon.qs.client.ui.panel.detailSection.SCDetailDetailPanel;
import com.gammon.qs.client.ui.panel.detailSection.TenderAnalysisComparisonPanel;
import com.gammon.qs.client.ui.panel.mainSection.MainCertificateGridPanel;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.client.ui.window.mainSection.ResetStatusPanel;
import com.gammon.qs.domain.ARRecord;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.MainCertReceiveDateWrapper;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.SCDetailsWrapper;
import com.gammon.qs.wrapper.tenderAnalysis.TenderAnalysisComparisonWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.layout.FitLayout;

public class DetailSectionController {
	private static final String DETAIL_SECTION_PANEL_ID = "DetailSectionPanel_ID";
	
	private GlobalSectionController globalSectionController;
	private Panel mainPanel; 
	private GridPanel gridPanel;
	private Panel panel;


	/**
	 * @return the globalSectionController
	 */
	public GlobalSectionController getGlobalSectionController() {
		return globalSectionController;
	}

	public DetailSectionController(GlobalSectionController globalSectionController ){
		this.globalSectionController = globalSectionController;
		setupUI();

	}

	private void setupUI(){
		this.mainPanel = new Panel();
		this.mainPanel.setLayout(new FitLayout());
		this.mainPanel.setBorder(false);
		this.mainPanel.setFrame(false);
		this.mainPanel.setHeight(300);
		this.mainPanel.setTitle("Details");
		this.mainPanel.setId(GlobalParameter.DETAIL_SECTION_PANEL_ID);

		this.mainPanel.setCollapsible(true);

		mainPanel.setId(DETAIL_SECTION_PANEL_ID);
	}

	public Panel getMainPanel() {
		return mainPanel;
	}

	/**
	 * To set the corresponding Grid Panel for display, use method populateEditorGridPanel() 
	 * @param mainPanel The Grid Panel for display
	 */
	public void setMainPanel(Panel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public GridPanel getGridPanel() {
		return gridPanel;
	}

	public void setGridPanel(GridPanel gridPanel) {
		this.gridPanel = gridPanel;
	}

	public Panel getPanel() {
		return panel;
	}

	public void setPanel(Panel panel) {
		this.panel = panel;
	}

	/**
	 * To populate the Grid Panel set(refresh)
	 */
	public void populateGridPanel(){
		if(this.mainPanel.isCollapsed()){
			mainPanel.expand();
		}
		
		this.mainPanel.removeAll(true);
		this.mainPanel.add(this.gridPanel);
		this.mainPanel.setAutoScroll(true);
		this.mainPanel.doLayout();
	}

	public void populatePanel() {
		if(this.mainPanel.isCollapsed()){
			mainPanel.expand();
		}
		
		this.mainPanel.removeAll(true);
		this.mainPanel.add(this.panel);
		this.mainPanel.setAutoScroll(true);
		this.mainPanel.doLayout();
	}
	
	/**
	 * to clear content in the Main Section
	 */
	public void resetPanel(){
		if(this.mainPanel.isCollapsed()){
			mainPanel.expand();
		}
		
		if(this.gridPanel!=null)
			this.gridPanel.destroy();

		if(this.panel!=null)
			this.panel.destroy();
		
		this.gridPanel = null;
		this.panel=null;
		this.mainPanel.removeAll(true);
		this.mainPanel.setTitle("Details");
	}
	
	/**
	 * @author Henry Lai
	 * Created on 30-10-2014 
	 */
	public void resetPanel(boolean collapse){
		if(collapse && !this.mainPanel.isCollapsed()){
			mainPanel.collapse();
		}
		
		if(this.gridPanel!=null)
			this.gridPanel.destroy();

		if(this.panel!=null)
			this.panel.destroy();
		
		this.gridPanel = null;
		this.panel=null;
		this.mainPanel.removeAll(true);
		this.mainPanel.setTitle("Details");
	}
	
	public void resetGridPanel(boolean collapse){
		if(this.gridPanel!=null)
			this.gridPanel.destroy();
		if(this.panel!=null)
			this.panel.destroy();
		this.gridPanel = null;
		this.panel=null;
		this.mainPanel.removeAll(true);
		this.mainPanel.setTitle("Details");
		
		if(collapse && !this.mainPanel.isCollapsed()){
			this.mainPanel.collapse();
		}
	}


	public void showAddendumEnquiryWindow(Integer subcontractNumber, String scLineType, boolean showButton){
		globalSectionController.showAddendumEnquiryWindow(subcontractNumber,  scLineType, showButton);
	}

	public void showEditAddendumWindow(String jobNumber, Integer subcontractNumber, Integer sequenceNumber, String billItem, Integer resourceNumber, String subsidiaryCode, String objectCode){
		globalSectionController.showEditAddendumWindow(jobNumber, subcontractNumber,  sequenceNumber, billItem, resourceNumber, subsidiaryCode, objectCode);
	}

	public void showSCAttachmentWindow( String subcontractNumber){
		globalSectionController.showSCAttachmentWindow(subcontractNumber);
	}


	public void populateGridPanelForSCDetails(PaginationWrapper<SCDetailsWrapper> scDetails) {
		SCDetailDetailPanel scDetailsEditorGridPanel = new SCDetailDetailPanel(this);
		scDetailsEditorGridPanel.populate(scDetails);
		setGridPanel(scDetailsEditorGridPanel);
		populateGridPanel();
	}

	public void populateGridPanelForTenderAnalysisComparison(final TenderAnalysisComparisonWrapper comparisonWrapper){
		String jobNumber = this.getGlobalSectionController().getJob().getJobNumber();
		final DetailSectionController detailSectionController = this;
		this.getGlobalSectionController().getPackageRepository().getCompanyBaseCurrency(jobNumber, new AsyncCallback<String>(){

			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser(),"DetailSectionController.populateGridPanelForTenderAnaysisComparison()");				
			}

			public void onSuccess(String currency) {
				SessionTimeoutCheck.renewSessionTimer();
				TenderAnalysisComparisonPanel comparisonPanel = new TenderAnalysisComparisonPanel(detailSectionController, comparisonWrapper, currency);
				setGridPanel(comparisonPanel);
				populateGridPanel();				
			}
		});
	}

/**
 * @author koeyyeung
 * created in May, 2013
 * **/
	public void populateGridPanelForMainCertContraCharge(MainCertificateGridPanel mainCertificateGridPanel, MainContractCertificate mainCert) {
		resetPanel();
		MainCertificateContraChargeDetailPanel mainCertificateContraChargeDetailPanel = new MainCertificateContraChargeDetailPanel(globalSectionController, mainCertificateGridPanel, mainCert);
		if(mainCert != null)
			mainPanel.setTitle("Main Certificate Contra Charge - No. "+mainCert.getCertificateNumber());
		else
			mainPanel.setTitle("Main Certificate Contra Charge - No. ");
		setGridPanel(mainCertificateContraChargeDetailPanel);
		populateGridPanel();
	}

	public void populateAccountReceivableDetailsPanel(final ARRecord arRecord){
		UIUtil.maskPanelById(DETAIL_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		if (arRecord != null && arRecord.getCompany() != null && arRecord.getDocumentNumber() != null) {
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getMainContractCertificateRepository().getMainCertReceiveDateAndAmount(arRecord.getCompany(), arRecord.getDocumentNumber().toString(),  new AsyncCallback<List<MainCertReceiveDateWrapper>>(){
				public void onFailure(Throwable e) {
					UIUtil.throwException(e);
					UIUtil.unmaskPanelById(DETAIL_SECTION_PANEL_ID);
				}
				public void onSuccess(List<MainCertReceiveDateWrapper> resultList){
					mainPanel.setTitle("Account Receivable Details");
					AccountReceivableDetailPanel accountReceivableDetailsDetailsPanel = new AccountReceivableDetailPanel(globalSectionController, arRecord, resultList);
					setPanel(accountReceivableDetailsDetailsPanel);
					populatePanel();
					UIUtil.unmaskPanelById(DETAIL_SECTION_PANEL_ID);
				}
			});
		} else {
			mainPanel.setTitle("Account Receivable Details");
			//arRecord could be null or just either company/documentNumber is null 
			AccountReceivableDetailPanel accountReceivableDetailsDetailsPanel = new AccountReceivableDetailPanel(globalSectionController, arRecord, null);
			setPanel(accountReceivableDetailsDetailsPanel);
			populatePanel();
			UIUtil.unmaskPanelById(DETAIL_SECTION_PANEL_ID);
		}
	}

	public void recalculateIV(Job job, String packageNo) {		
		UIUtil.maskPanelById(DETAIL_SECTION_PANEL_ID, "Recalculating", true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPackageRepository().recalculateResourceSummaryIvFromSc(job, packageNo, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(DETAIL_SECTION_PANEL_ID);
				UIUtil.throwException(e);
			}

			public void onSuccess(Boolean success) {
				UIUtil.unmaskPanelById(DETAIL_SECTION_PANEL_ID);
				if (success)
					MessageBox.alert("IV amounts have been recalculated.");
				else
					MessageBox.alert("Failed: IV amounts were unable to be recalculated.");
			}
		});
	}
	
	public User getUser() {
		return this.globalSectionController.getUser();
	}

	public ColumnConfig[] applyScreenPreferences(String screenName, ColumnConfig[] columns) {
		return this.globalSectionController.applyScreenPreferences(screenName, columns);

	}

	/**
	 * created by matthewlam, 2015-01-21
	 * Bug Fix #79 revamp of Power User Administration page
	 */
	public void populateByResetStatusPanel(String title,
											String[][] choices,
											String jobNumber,
											String packageNumber,
											String paymentNumber,
											String mainCertNumber,
											String currentValue) {
		resetPanel();
		mainPanel.setTitle(title);
		setPanel(new ResetStatusPanel(globalSectionController,
				title,
				choices,
				jobNumber,
				packageNumber,
				paymentNumber,
				mainCertNumber,
				currentValue));
		populatePanel();
	}

}
