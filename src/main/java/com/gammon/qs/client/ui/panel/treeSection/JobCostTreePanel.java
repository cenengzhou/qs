package com.gammon.qs.client.ui.panel.treeSection;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.controller.TreeSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.panel.mainSection.APRecordEnquiryMainPanel;
import com.gammon.qs.client.ui.panel.mainSection.ARRecordEnquiryMainPanel;
import com.gammon.qs.client.ui.panel.mainSection.JobCostEnquiryMainPanel;
import com.gammon.qs.client.ui.panel.mainSection.AccountLedgerEnquiryMainPanel;
import com.gammon.qs.client.ui.panel.mainSection.BudgetForecastImportMainPanel;
import com.gammon.qs.client.ui.panel.mainSection.PerformanceAppraisalEnquiryMainPanel;
import com.gammon.qs.client.ui.panel.mainSection.PurchaseOrderEnquiryMainPanel;
import com.gammon.qs.client.ui.window.treeSection.CreateAccountMasterWindow;
import com.gammon.qs.client.ui.window.treeSection.ObjectSubsidiaryRuleWindow;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

public class JobCostTreePanel  extends TreePanel{
	private TreeSectionController treeViewController;
	private GlobalSectionController globalSectionController;
	/**
	 * @author tikywong
	 * April 19, 2011
	 */
	public static final String ACCOUNT_BALANCE_BY_DATE_RANAGE_NODE_NAME = "Job Cost Enquiry";
	public static final String ACCOUNT_LEDGER_NODE_NAME = "Account Ledger Enquiry";
	public static final String APRECORD_NODE_NAME = "Supplier Ledger Enquiry";
	public static final String ARRECORD_NODE_NAME = "Customer Ledger Enquiry";
	public static final String PERFORMANCE_APPRAISAL_ENQUIRY_NODE_NAME = "Performance Appraisal Enquiry";
	public static final String PURCHASE_ORDER_ENQUIRY_NODE_NAME = "Purchase Order Enquiry";

	
	public static final String CREATE_ACCOUNT_MASTER_NODE_NAME = "Create Account Master";
	private final static String NODE_ATTRIBUTE_OBJ_SUBSIDIARY = "Object Subsidiary Rule Search";
	private final static String BUDGET_AND_FORECAST_IMPORT_NODE_NAME = "Budget and Forecast Import";
	
	private GlobalMessageTicket globalMessageTicket;

	private TreeNode rootNode;
	private TreeNode enquiryNode;
	private TreeNode operationNode;
	private TreeNode apRecordEnquiryNode;
	private TreeNode arRecordEnquiryNode;
	private TreeNode accountBalanceByDateRangeNode;
	private TreeNode accountLedgerEnquiryNode;
	private TreeNode purchaseOrderEnquiryNode;
	private TreeNode createAccountMasterNode;
	private TreeNode performanceAppraisalNode;
	private TreeNode budgetAndForecastNode;
	private TreeNode objSubSearchNode;


	public JobCostTreePanel(final TreeSectionController treeViewController){
		super();
		this.treeViewController = treeViewController;
		this.globalSectionController = this.treeViewController.getGlobalSectionController();
		globalMessageTicket = new GlobalMessageTicket();
		
		this.setTitle("Job Cost");
		this.setBorder(false);
		this.setAutoScroll(true);

		rootNode = new TreeNode("Job Cost");

		enquiryNode = new TreeNode("Enquiry");
		operationNode = new TreeNode("Operation");
		
		accountBalanceByDateRangeNode = new TreeNode(ACCOUNT_BALANCE_BY_DATE_RANAGE_NODE_NAME);
		accountLedgerEnquiryNode = new TreeNode(ACCOUNT_LEDGER_NODE_NAME);
		apRecordEnquiryNode = new TreeNode(APRECORD_NODE_NAME);
		arRecordEnquiryNode = new TreeNode(ARRECORD_NODE_NAME);
		performanceAppraisalNode = new TreeNode(PERFORMANCE_APPRAISAL_ENQUIRY_NODE_NAME);
		purchaseOrderEnquiryNode = new TreeNode(PURCHASE_ORDER_ENQUIRY_NODE_NAME);
		
		enquiryNode.appendChild(accountBalanceByDateRangeNode);
		enquiryNode.appendChild(accountLedgerEnquiryNode);
		enquiryNode.appendChild(apRecordEnquiryNode);
		enquiryNode.appendChild(arRecordEnquiryNode);
		enquiryNode.appendChild(performanceAppraisalNode);
		enquiryNode.appendChild(purchaseOrderEnquiryNode);		
		
		
		budgetAndForecastNode = new TreeNode(BUDGET_AND_FORECAST_IMPORT_NODE_NAME);
		createAccountMasterNode = new TreeNode(CREATE_ACCOUNT_MASTER_NODE_NAME);
		/* 
		 * Add as object subsidiary rule search window.
		 * By Peter CHAN
		 */
		objSubSearchNode = new TreeNode(NODE_ATTRIBUTE_OBJ_SUBSIDIARY);
	
		operationNode.appendChild(budgetAndForecastNode);
		operationNode.appendChild(createAccountMasterNode);
		operationNode.appendChild(objSubSearchNode);

		rootNode.appendChild(enquiryNode);
		rootNode.appendChild(operationNode);
		
		rootNode.expand();
		this.setRootNode(rootNode);

		this.addListener(new TreePanelListenerAdapter() {
			public void onClick(TreeNode node, EventObject e) {
				globalMessageTicket.refresh();

				if(node.getText().equals(APRECORD_NODE_NAME)){
					showAPRecordEnquiryMainPanel();
				}
				else if(node.getText().equals(ARRECORD_NODE_NAME)){
					showARRecordEnquiryMainPanel();
				}
				else if(node.getText().equals(ACCOUNT_BALANCE_BY_DATE_RANAGE_NODE_NAME)){
					showAccountBalanceByDateRangeMainPanel();
				}
				else if(node.getText().equals(ACCOUNT_LEDGER_NODE_NAME)){
					showAccountLedgerEnquiryMainPanel();
				}
				else if(node.getText().equals(PERFORMANCE_APPRAISAL_ENQUIRY_NODE_NAME)){
					showPerformanceAppraisalEnquiryMainPanel();
				}
				else if(node.getText().equals(PURCHASE_ORDER_ENQUIRY_NODE_NAME)){
					showPurchaseOrderEnquiryMainPanel();
				}
				
				else if (node.getText().equals(BUDGET_AND_FORECAST_IMPORT_NODE_NAME)){
					showBudgetForecastImportMainPanel();
				}
				
				else if (node.getText().equals(CREATE_ACCOUNT_MASTER_NODE_NAME)){
					showCreateAccountMaster();
				}
				else if (node.getText().equals(NODE_ATTRIBUTE_OBJ_SUBSIDIARY)){
					showObjectSubsidiaryRuleWindow();
				}
			}

			public void onContextMenu(TreeNode node, EventObject e){
				while(node != null){
					node.collapse();
					node = (TreeNode)node.getParentNode();
				}
			}
		});
		this.expandAll();
	}
	
	public void showAPRecordEnquiryMainPanel(){
		globalSectionController.getMainSectionController().resetMainPanel();
		APRecordEnquiryMainPanel apRecordEnquiryMainPanel = new APRecordEnquiryMainPanel(globalSectionController);
		globalSectionController.getMainSectionController().setContentPanel(apRecordEnquiryMainPanel);
		globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();
		globalSectionController.getDetailSectionController().resetPanel();
	}
	
	public void showARRecordEnquiryMainPanel(){
		globalSectionController.getMainSectionController().resetMainPanel();
		ARRecordEnquiryMainPanel aRRecordEnquiryMainPanel = new ARRecordEnquiryMainPanel(globalSectionController);
		globalSectionController.getMainSectionController().setContentPanel(aRRecordEnquiryMainPanel);
		globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();
		globalSectionController.getDetailSectionController().resetPanel();
	}

	public void showAccountBalanceByDateRangeMainPanel() {
		globalSectionController.getMainSectionController().resetMainPanel();
		JobCostEnquiryMainPanel accountBalanceByDateRangeMainPanel = new JobCostEnquiryMainPanel(globalSectionController);
		globalSectionController.getMainSectionController().setContentPanel(accountBalanceByDateRangeMainPanel);
		globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();
		globalSectionController.getDetailSectionController().resetPanel();
	}
	
	public void showAccountLedgerEnquiryMainPanel() {
		globalSectionController.getMainSectionController().resetMainPanel();
		AccountLedgerEnquiryMainPanel accountLedgerEnquiryMainPanel = new AccountLedgerEnquiryMainPanel(globalSectionController);
		globalSectionController.getMainSectionController().setContentPanel(accountLedgerEnquiryMainPanel);
		globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();	
		globalSectionController.getDetailSectionController().resetPanel();
	}
	
	public void showPerformanceAppraisalEnquiryMainPanel(){
		globalSectionController.getMainSectionController().resetMainPanel();
		PerformanceAppraisalEnquiryMainPanel performanceAppraisalEnquiryMainPanel = new PerformanceAppraisalEnquiryMainPanel(globalSectionController);
		globalSectionController.getMainSectionController().setContentPanel(performanceAppraisalEnquiryMainPanel);
		globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();	
		globalSectionController.getDetailSectionController().resetPanel();
	}
	
	public void showBudgetForecastImportMainPanel(){
		globalSectionController.getMainSectionController().resetMainPanel();
		BudgetForecastImportMainPanel budgetAndForecastImportMainPanel = new BudgetForecastImportMainPanel(globalSectionController);
		//budgetAndForecastImportMainPanel.search();
		globalSectionController.getMainSectionController().setContentPanel(budgetAndForecastImportMainPanel);
		globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();
		globalSectionController.getDetailSectionController().resetPanel();
	}
	
	public void showPurchaseOrderEnquiryMainPanel(){
		globalSectionController.getMainSectionController().resetMainPanel();
		PurchaseOrderEnquiryMainPanel purchaseOrderEnquiryMainPanel = new PurchaseOrderEnquiryMainPanel(globalSectionController);
		globalSectionController.getMainSectionController().setContentPanel(purchaseOrderEnquiryMainPanel);
		globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();
		globalSectionController.getDetailSectionController().resetPanel();
	}
	
	/**
	 * @author matthewatc
	 * 16:56:11 29 Dec 2011 (UTC+8)
	 * Shows the object subsidiary rule search window
	 */
	public void showObjectSubsidiaryRuleWindow(){	
		if(globalSectionController.getCurrentWindow() == null){	
			globalSectionController.setCurrentWindow(new ObjectSubsidiaryRuleWindow(globalSectionController));
			globalSectionController.getCurrentWindow().show();
			globalSectionController.getCurrentWindow().setConstrainHeader(true);
		}
	}
	
	
	public void showCreateAccountMaster() {
		if (globalSectionController.getCurrentWindow()!=null){
			globalSectionController.getCurrentWindow().close();
			globalSectionController.getCurrentWindow().destroy();
		}
		globalSectionController.setCurrentWindow(new CreateAccountMasterWindow(globalSectionController));
		globalSectionController.getCurrentWindow().show();		
	}
	



}