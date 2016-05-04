package com.gammon.qs.client.ui.panel.treeSection;

import com.gammon.qs.client.controller.TreeSectionController;
import com.gammon.qs.shared.GlobalParameter;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

public class TransitTreePanel extends TreePanel {
	private TreeNode rootNode;
	
	// added by brian on 20110301
	private TreeNode transitStatusEnquiryNode;
	
	private TreeNode createHeaderNode;
	private TreeNode importBqItemsNode;
	private TreeNode itemEnquiryNode;
	private TreeNode importResourcesNode;
	private TreeNode resourceEnquiryNode;
	private TreeNode resourceCodeMatchingNode;
	private TreeNode unitCodeMatchingNode;
	private TreeNode confirmNode;
	private TreeNode completeNode;
	private TreeNode postBudgetNode;
	private TreeNode printBQMasterReconciliationReport;
	private TreeNode printBQRecourseReconciliationReport;
	
	public TransitTreePanel(final TreeSectionController treeViewController){
		super();
		this.setTitle("Transit");
		this.setBorder(false);
		
		rootNode = new TreeNode("Transit");
		
		transitStatusEnquiryNode = new TreeNode("Transit Status Enquiry");
		rootNode.appendChild(transitStatusEnquiryNode);
		
		createHeaderNode = new TreeNode("Create/Update Transit Header");
		rootNode.appendChild(createHeaderNode);
		
		importBqItemsNode = new TreeNode("Import BQ Items");
		rootNode.appendChild(importBqItemsNode);
		
		itemEnquiryNode = new TreeNode("View Imported BQ Items");
		rootNode.appendChild(itemEnquiryNode);
		
		importResourcesNode = new TreeNode("Import Resources");
		rootNode.appendChild(importResourcesNode);
		
		resourceEnquiryNode = new TreeNode("View/Edit Imported Resources");
		rootNode.appendChild(resourceEnquiryNode);
		
		confirmNode = new TreeNode("Confirm Resources");
		rootNode.appendChild(confirmNode);
		
		printBQMasterReconciliationReport = new TreeNode("Print BQ Master Reconciliation Report");
		rootNode.appendChild(printBQMasterReconciliationReport);
		
		printBQRecourseReconciliationReport = new TreeNode("Print BQ Resource Reconciliation Report");
		rootNode.appendChild(printBQRecourseReconciliationReport);
		
		completeNode = new TreeNode("Complete Transit");
		rootNode.appendChild(completeNode);
		
		postBudgetNode = new TreeNode("Post Budget");
		rootNode.appendChild(postBudgetNode);
		
		resourceCodeMatchingNode = new TreeNode("Resource Code Matching");
		rootNode.appendChild(resourceCodeMatchingNode);
		
		unitCodeMatchingNode = new TreeNode("Unit Code Matching");
		rootNode.appendChild(unitCodeMatchingNode);
		
		this.addListener(new TreePanelListenerAdapter(){
			public void onClick(TreeNode node, EventObject e){
				if(node.getText().equals(transitStatusEnquiryNode.getText()))
					treeViewController.navigateToTransitStatus();
				else if(node.getText().equals(createHeaderNode.getText()))
					treeViewController.requestNavigateToTransitHeader();
				else if(node.getText().equals(importBqItemsNode.getText()))
					treeViewController.requestNavigateToTransitImport(GlobalParameter.TRANSIT_BQ);
				else if(node.getText().equals(itemEnquiryNode.getText()))
					treeViewController.requestNavigateToTransitItemEnquiry();
				else if(node.getText().equals(importResourcesNode.getText()))
					treeViewController.requestNavigateToTransitImport(GlobalParameter.TRANSIT_RESOURCE);
				else if(node.getText().equals(resourceEnquiryNode.getText()))
					treeViewController.showTransitResourceWindow();
				else if(node.getText().equals(confirmNode.getText()))
					treeViewController.confirmTransitResources();
				else if(node.getText().equals(printBQMasterReconciliationReport.getText()))
					treeViewController.printMasterReport(treeViewController.getGlobalSectionController().getJob().getJobNumber());
				else if(node.getText().equals(printBQRecourseReconciliationReport.getText()))
					treeViewController.printResourceReport(treeViewController.getGlobalSectionController().getJob().getJobNumber());
				else if(node.getText().equals(completeNode.getText()))
					treeViewController.completeTransit();
				else if(node.getText().equals(postBudgetNode.getText()))
					treeViewController.postBudget();
				else if(node.getText().equals(resourceCodeMatchingNode.getText()))
					treeViewController.navigateToTransitCodeMatching();
				else if(node.getText().equals(unitCodeMatchingNode.getText()))
					treeViewController.navigateToTransitUomMatching();
			}
		});
		this.setRootNode(rootNode);
		rootNode.expand();
		this.expandAll();
	}
	
	public void updateRootText(String text) {
		this.rootNode.setText(text);
	}
}
