package com.gammon.qs.client.ui.panel.treeSection;


import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.controller.TreeSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.panel.mainSection.SubcontractorPaymentExceptionalReportMainPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

public class ReportsTreePanel extends TreePanel{
	
	private static final String NODE_NAME_SC_PAYMENT_EXCEPTIONAL_REPORT = "Subcontractor Payment Exceptional Report";
	//private static final String NODE_NAME_PRINT_SC_PAYMENT_CERT = "Print SC Payment Cert by Batch";
	
	private TreeNode rootNode;
	@SuppressWarnings("unused")
	private TreeNode paymentNode;
	@SuppressWarnings("unused")
	private TreeNode subcontractorPaymentExceptionalReportNode;
	
	private GlobalSectionController globalSectionController;
	private GlobalMessageTicket globalMessageTicket;
	public ReportsTreePanel(final TreeSectionController treeViewController){
		super();
		globalMessageTicket = new GlobalMessageTicket();
		this.setTitle("Reports");
		this.setBorder(false);
		this.setAutoScroll(true);
		
		this.globalSectionController = treeViewController.getGlobalSectionController();
		rootNode = new TreeNode("Finance");

		/**
		 * Commented out by Henry Lai
		 * 25-Nov-2014
		 * Functions are obsolete and can be replaced by
		 * 'Subcontract Payment' -> 'Payment Certificate Enquiry'.
		 */
		//paymentNode = new TreeNode(NODE_NAME_PRINT_SC_PAYMENT_CERT);
		
		//subcontractorPaymentExceptionalReportNode = new TreeNode(NODE_NAME_SC_PAYMENT_EXCEPTIONAL_REPORT);
		
		//rootNode.appendChild(paymentNode);
		//rootNode.appendChild(subcontractorPaymentExceptionalReportNode);
		
		rootNode.expand();
		this.setRootNode(rootNode);
		
		this.addListener(new TreePanelListenerAdapter() {
			public void onClick(TreeNode node, EventObject e) {
				globalMessageTicket.refresh();
				/*if(node.getText().equals(NODE_NAME_PRINT_SC_PAYMENT_CERT)) {
					globalSectionController.showSCPaymentCertReportWindow();
				}
				else*/ if (node.getText().equals(NODE_NAME_SC_PAYMENT_EXCEPTIONAL_REPORT)){
					populateSCPaymentExceptionalReportMainPanel();
				}
				
			}

			public void onContextMenu(TreeNode node, EventObject e)
			{
				while(node != null)
				{
					node.collapse();
					node = (TreeNode)node.getParentNode();
				}
			}
		});
		
		this.expandAll();
	}

	public void updateRootText(String string) {
		rootNode.setText(string);
		
	}
	
	public void populateSCPaymentExceptionalReportMainPanel(){
		globalSectionController.getDetailSectionController().resetPanel();
		globalSectionController.getMainSectionController().resetMainPanel();
		SubcontractorPaymentExceptionalReportMainPanel scPaymentExceptionalReportMainPanel = new SubcontractorPaymentExceptionalReportMainPanel(globalSectionController);
		globalSectionController.getMainSectionController().setContentPanel(scPaymentExceptionalReportMainPanel);
		globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();
	}
	
}
