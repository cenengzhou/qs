package com.gammon.qs.client.controller;

import java.util.List;

import com.gammon.qs.application.User;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.mainSection.MainCertificateGridPanel;
import com.gammon.qs.client.ui.panel.treeSection.BQTreePanel;
import com.gammon.qs.client.ui.panel.treeSection.IVTreePanel;
import com.gammon.qs.client.ui.panel.treeSection.JobCostTreePanel;
import com.gammon.qs.client.ui.panel.treeSection.JobInformationTreePanel;
import com.gammon.qs.client.ui.panel.treeSection.MainContractCertificateTreePanel;
import com.gammon.qs.client.ui.panel.treeSection.PackageTreePanel;
import com.gammon.qs.client.ui.panel.treeSection.PaymentTreePanel;
import com.gammon.qs.client.ui.panel.treeSection.RepackagingTreePanel;
import com.gammon.qs.client.ui.panel.treeSection.ReportsTreePanel;
import com.gammon.qs.client.ui.panel.treeSection.SCProvisionTreePanel;
import com.gammon.qs.client.ui.panel.treeSection.SubcontractorTreePanel;
import com.gammon.qs.client.ui.panel.treeSection.SystemAdminTreePanel;
import com.gammon.qs.client.ui.panel.treeSection.TransitTreePanel;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.TransitHeader;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.AccordionLayout;

public class TreeSectionController {
	// parent controller
	private GlobalSectionController globalSectionController;

	// UI component
	private Panel mainPanel;

	private BQTreePanel bqTreePanel;
	private PackageTreePanel packageTreePanel;	
	private PaymentTreePanel paymentTreePanel;
	private JobInformationTreePanel jobInformationTreePanel;
	private JobCostTreePanel jobCostTreePanel;
	private IVTreePanel ivTreePanel;
	private SCProvisionTreePanel scProvisionTreePanel;
	private MainContractCertificateTreePanel mainContractCertificateTreePanel;
	private RepackagingTreePanel repackagingTreePanel;
	private SystemAdminTreePanel systemAdminTreePanel;
	private ReportsTreePanel reportsTreePanel;
	private TransitTreePanel transitTreePanel;
	private SubcontractorTreePanel subcontractorTreePanel; 
	
	// Ready flag (to indicate if rendering of panel ready) 
	private boolean isBqPanelReady = true;
	private boolean isPackagePanelReady = true;
	private boolean isPaymentPanelReady = true;
	private boolean isJobHeaderPanelReady = true;

	private boolean isForecastPanelReady = true;
	private boolean isSystemAdminPanelReady = true;
	
	// used to check access rights
	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	
	private List<String> accessRightsList;

	public TreeSectionController(GlobalSectionController globalSectionController){
		this.globalSectionController = globalSectionController;
		
		// Repository initialization
		userAccessRightsRepository = globalSectionController.getUserAccessRightsRepository();

		setupUI();		
	}


	private void setupUI(){
		mainPanel = new Panel("Navigations");
		mainPanel.setBorder(false);
		mainPanel.setCollapsible(true);
		mainPanel.setWidth(250);
		mainPanel.setLayout(new AccordionLayout());
		mainPanel.setId(GlobalParameter.TREEVIEW_SECTION_PANEL_ID);

		bqTreePanel = new BQTreePanel(globalSectionController , this);
		bqTreePanel.setIconCls("bill-icon");
		mainPanel.add(bqTreePanel);

		packageTreePanel = new PackageTreePanel(this);
		packageTreePanel.setIconCls("award-icon");
		mainPanel.add(packageTreePanel);

		paymentTreePanel = new PaymentTreePanel(this);
		paymentTreePanel.setIconCls("payment-icon");
		mainPanel.add(paymentTreePanel);

		repackagingTreePanel = new RepackagingTreePanel(this);
		repackagingTreePanel.setIconCls("package-icon");
		mainPanel.add(repackagingTreePanel);

		jobCostTreePanel = new JobCostTreePanel(this);
		jobCostTreePanel.setIconCls("cost-icon");
		mainPanel.add(jobCostTreePanel);

		ivTreePanel = new IVTreePanel(globalSectionController);
		ivTreePanel.setIconCls("calculator-icon");
		mainPanel.add(ivTreePanel);
		
		scProvisionTreePanel = new SCProvisionTreePanel(globalSectionController);
		scProvisionTreePanel.setIconCls("history-icon");
		mainPanel.add(scProvisionTreePanel);
		
		mainContractCertificateTreePanel = new MainContractCertificateTreePanel(this);
		mainContractCertificateTreePanel.setIconCls("certificate-icon");
		mainPanel.add(mainContractCertificateTreePanel);

		jobInformationTreePanel = new JobInformationTreePanel(this);
		jobInformationTreePanel.setIconCls("info-icon");
		mainPanel.add(jobInformationTreePanel);
		
		subcontractorTreePanel = new SubcontractorTreePanel(globalSectionController);
		subcontractorTreePanel.setIconCls("enquiry-icon");
		mainPanel.add(subcontractorTreePanel);
		
		/**
		 * Commented out by Henry Lai
		 * 25-Nov-2014
		 * All Functions are obsolete and can be replaced by other functions.
		 * The reports tree node is empty.
		 */
		//reportsTreePanel = new ReportsTreePanel(this);
		//mainPanel.add(reportsTreePanel);

		transitTreePanel = new TransitTreePanel(this);
		transitTreePanel.setIconCls("bill-icon");
		mainPanel.add(transitTreePanel);
		
		systemAdminTreePanel = new SystemAdminTreePanel(this);
		systemAdminTreePanel.setIconCls("admin-icon");
		
		partialTreeShowControl();
	}


	public Panel getMainPanel() {
		return mainPanel;
	}

	public void setBqPanelReady(boolean isBqPanelReady) {
		this.isBqPanelReady = isBqPanelReady;
		unMaskPanelAfterPanelsIsReady();
	}
	public void setPackagePanelReady(boolean isPackagePanelReady){
		this.isPackagePanelReady = isPackagePanelReady;
		unMaskPanelAfterPanelsIsReady();
	}
	public void setPaymentPanelReady(boolean isPaymentPanelReady) {
		this.isPaymentPanelReady = isPaymentPanelReady;
		unMaskPanelAfterPanelsIsReady();
	}


	/**
	 * To unmasked the panel after rendering.
	 * Unmask only if all trees are rendered.
	 */
	private void unMaskPanelAfterPanelsIsReady(){
		if(	isBqPanelReady && isJobHeaderPanelReady  && 
			isPaymentPanelReady && isPackagePanelReady && 
			isForecastPanelReady && isSystemAdminPanelReady) {
			mainPanel.doLayout();
			UIUtil.unmaskPanelById(GlobalParameter.MAIN_PANEL_ID);	
		}		
	}

	/**
	 * To refresh the content of tree panel
	 * @param job the opened job
	 */
	public void refreshTreePanels(Job job){
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.M010008_SYSTEM_ADMIN_TREEPANEL, new AsyncCallback<List<String>>() {
			public void onSuccess(List<String> accessRightsReturned) {
				accessRightsList = accessRightsReturned;
				if (accessRightsList.contains("WRITE"))
					mainPanel.add(systemAdminTreePanel);
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}

			public void onFailure(Throwable e) {
				UIUtil.alert("Failed: Unable to obtain Access Right for User: "+globalSectionController.getUser().getUsername());
				UIUtil.throwException(e);
			}
		});
		UIUtil.maskPanelById(GlobalParameter.MAIN_PANEL_ID, GlobalParameter.LOADING_MSG, true);

			//BQ Tree
			bqTreePanel.clear();
			bqTreePanel.refreshTree(job);
			
			//Package Tree
			packageTreePanel.clear();
			packageTreePanel.refreshPackageTreePanel();
	
			//Payment Tree
			paymentTreePanel.clear();
			paymentTreePanel.refreshPaymentTreePanel();
			
			//Job Cost Enquiry Tree
			jobCostTreePanel.clear();
	
			//IV Tree
			ivTreePanel.clear();
			ivTreePanel.openJob(job.getRepackagingType());
	
			scProvisionTreePanel.clear();
			
			//Main Contract Certificate Tree
			mainContractCertificateTreePanel.clear();

		//Job Tree
		jobInformationTreePanel.clear();
		jobInformationTreePanel.updateRootText(job.getJobNumber() + " - " + job.getDescription());

		//Repackaging Tree
		repackagingTreePanel.clear();
		repackagingTreePanel.updateRootText(job.getJobNumber() +" - " + job.getDescription());

		//System Admin Tree
		systemAdminTreePanel.clear();
		systemAdminTreePanel.updateRootText(job.getJobNumber() +" - " + job.getDescription());
		
		partialTreeShowControl();

	}

	private void partialTreeShowControl() {
		boolean convertedStatus = false;
		if (globalSectionController.getJob() != null && 
			globalSectionController.getJob().getConversionStatus() != null && 
			"Y".equals(globalSectionController.getJob().getConversionStatus().trim())) {
			convertedStatus = true;
		}
		bqTreePanel.setVisible(convertedStatus);
		packageTreePanel.setVisible(convertedStatus);
		paymentTreePanel.setVisible(convertedStatus);
		repackagingTreePanel.setVisible(convertedStatus);
	}
	
	public void refreshPackageTreePanel(){
		packageTreePanel.refreshPackageTreePanel();
	}

	public void requestNavigateToPackage(String packageNo) {
		globalSectionController.populateSCPackageMainPanelandDetailPanel(packageNo);
	}

	public void resetMainSectionDetailSection() {
		globalSectionController.resetMainSectionDetailSection();
	}

	public void requestNavigateToTransitHeader() {
		globalSectionController.navigateToTransitHeader();
	}
	
	public void requestNavigateToTransitImport(String type) {
		globalSectionController.navigateToTransitImport(type);
	}
	
	public void navigateToTransitCodeMatching() {
		globalSectionController.navigateToTransitCodeMatching();
	}
	
	public void navigateToTransitUomMatching() {
		globalSectionController.navigateToTransitUomMatching();
	}
	
	public void requestNavigateToTransitItemEnquiry(){
		globalSectionController.navigateToTransitItemEnquiry();	
	}
	
	public void showTransitResourceWindow(){
		globalSectionController.showTransitResourceWindow();
	}

	public void requestNavigateToRepackaging(){
		globalSectionController.navigateToRepackaging();
	}
	
	public void confirmTransitResources(){
		globalSectionController.confirmTransitResources();
	}
	
	public void completeTransit(){
		globalSectionController.completeTransit();
	}
	
	public void postBudget(){
		globalSectionController.postBudgetAfterTransit();
	}

	public User getUser() {
		return this.globalSectionController.getUser();
	}		

	public GlobalSectionController getGlobalSectionController() {
		return globalSectionController;
	}

	public void setGlobalSectionController(
			GlobalSectionController globalSectionController) {
		this.globalSectionController = globalSectionController;
	}

	public IVTreePanel getIvTreePanel() {
		return ivTreePanel;
	}

	public void setIvTreePanel(IVTreePanel ivTreePanel) {
		this.ivTreePanel = ivTreePanel;
	}	
	public BQTreePanel getBqTreePanel() {
		return bqTreePanel;
	}


	public void setBqTreePanel(BQTreePanel bqTreePanel) {
		this.bqTreePanel = bqTreePanel;
	}


	public PackageTreePanel getPackageTreePanel() {
		return packageTreePanel;
	}


	public void setPackageTreePanel(PackageTreePanel packageTreePanel) {
		this.packageTreePanel = packageTreePanel;
	}


	public PaymentTreePanel getPaymentTreePanel() {
		return paymentTreePanel;
	}


	public void setPaymentTreePanel(PaymentTreePanel paymentTreePanel) {
		this.paymentTreePanel = paymentTreePanel;
	}


	public JobInformationTreePanel getJobInformationTreePanel() {
		return jobInformationTreePanel;
	}


	public void setJobInformationTreePanel(JobInformationTreePanel jobInformationTreePanel) {
		this.jobInformationTreePanel = jobInformationTreePanel;
	}


	public JobCostTreePanel getJobCostTreePanel() {
		return jobCostTreePanel;
	}


	public void setJobCostTreePanel(JobCostTreePanel jobCostTreePanel) {
		this.jobCostTreePanel = jobCostTreePanel;
	}


	public SCProvisionTreePanel getScProvisionTreePanel() {
		return scProvisionTreePanel;
	}


	public void setScProvisionTreePanel(SCProvisionTreePanel scProvisionTreePanel) {
		this.scProvisionTreePanel = scProvisionTreePanel;
	}


	public RepackagingTreePanel getRepackagingTreePanel() {
		return repackagingTreePanel;
	}


	public void setRepackagingTreePanel(RepackagingTreePanel repackagingTreePanel) {
		this.repackagingTreePanel = repackagingTreePanel;
	}


	public SystemAdminTreePanel getSystemAdminTreePanel() {
		return systemAdminTreePanel;
	}


	public void setSystemAdminTreePanel(SystemAdminTreePanel systemAdminTreePanel) {
		this.systemAdminTreePanel = systemAdminTreePanel;
	}


	public ReportsTreePanel getReportsTreePanel() {
		return reportsTreePanel;
	}


	public void setReportsTreePanel(ReportsTreePanel reportsTreePanel) {
		this.reportsTreePanel = reportsTreePanel;
	}


	public TransitTreePanel getTransitTreePanel() {
		return transitTreePanel;
	}


	public void setTransitTreePanel(TransitTreePanel transitTreePanel) {
		this.transitTreePanel = transitTreePanel;
	}
	
	public SubcontractorTreePanel getSubcontractorTreePanel() {
		return subcontractorTreePanel;
	}


	public void setSubcontractorTreePanel(SubcontractorTreePanel subcontractorTreePanel) {
		this.subcontractorTreePanel = subcontractorTreePanel;
	}


	/**
	 * 
	 * @author tikywong
	 * modified on May 14, 2013 7:43:15 PM
	 */
	public void populateMainCertificate() {
		MainCertificateGridPanel mainCertificateGridPanel = new MainCertificateGridPanel(globalSectionController);
		mainCertificateGridPanel.goToPageMainCertificate(0);
		globalSectionController.getMainSectionController().resetMainPanel();
		globalSectionController.getMainSectionController().setContentPanel(mainCertificateGridPanel);
		globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();
		globalSectionController.getDetailSectionController().resetPanel();
	}

	// last modified: Brian Tse
	private void printTransitMasterReport(){
		com.google.gwt.user.client.Window.open(GlobalParameter.PRINT_BQ_MASTER_RECONCILIATION_REPORT +"?jobNumber="+ this.getGlobalSectionController().getJob().getJobNumber(), "_blank", "");
	}
	
	// last modified: Brian Tse
	private void printTransitResourceReport(){
		com.google.gwt.user.client.Window.open(GlobalParameter.PRINT_BQ_RECOURSE_RECONCILIATION_REPORT +"?jobNumber="+ this.getGlobalSectionController().getJob().getJobNumber(), "_blank", "");
	}
	
	// last modified : Brian Tse
	public void printMasterReport(String jobNumber){
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getTransitRepository().allowPrint(jobNumber, new AsyncCallback<Boolean>(){

			public void onFailure(Throwable e) {
				UIUtil.alert(e.getLocalizedMessage());
			}

			public void onSuccess(Boolean allow) {
				if(allow)
					printTransitMasterReport();
				else
					MessageBox.alert("Please confirm resource before printing any report."); 
			}
		});
	}
	
	// Last Modified: Brian Tse
	public void printResourceReport(String jobNumber){
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getTransitRepository().allowPrint(jobNumber, new AsyncCallback<Boolean>(){

			public void onFailure(Throwable e) {
				UIUtil.alert(e.getLocalizedMessage());
			}

			public void onSuccess(Boolean allow) {
				if(allow)
					printTransitResourceReport();
				else
					MessageBox.alert("Please confirm resource before printing any report."); 
			}
		});
	}
	
	// added by brian on 20110301
	public void navigateToTransitStatus(){
		// get all transit ignore completed by default
		globalSectionController.navigateToTransitStatus(TransitHeader.TRANSIT_COMPLETED, false);
	}
	
	// added by brian on 20110427
	public void navigateToJobDates() {
		globalSectionController.navigateToJobDates();
	}
}
