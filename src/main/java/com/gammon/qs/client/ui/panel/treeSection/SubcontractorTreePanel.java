package com.gammon.qs.client.ui.panel.treeSection;

import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.mainSection.ClientEnquiryMainPanel;
import com.gammon.qs.client.ui.panel.mainSection.SubcontractEnquiryMainPanel;
import com.gammon.qs.client.ui.panel.mainSection.SubcontractorEnquiryMainPanel;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

/**
 * @author koeyyeung
 * refactored on 12 Aug, 2013
 * **/
public class SubcontractorTreePanel  extends TreePanel{
	private GlobalSectionController globalSectionController;

	public static final String SUBCONTRACTOR_ENQUIRY_NODE_NAME = "Subcontractor Enquiry";
	public static final String SUBCONTRACT_ENQUIRY_NODE_NAME = "Subcontract Enquiry";
	public static final String CLIENT_ENQUIRY_NODE_NAME = "Client Enquiry";
	
	private GlobalMessageTicket globalMessageTicket;

	private TreeNode rootNode;
	private TreeNode subcontractorEnquiryNode;
	private TreeNode subcontractEnquiryNode;
	private TreeNode clientEnquiryNode;

	public SubcontractorTreePanel(GlobalSectionController globalSectionController){
		super();
		this.globalSectionController = globalSectionController;
		globalMessageTicket = new GlobalMessageTicket();
		
		this.setTitle("Subcontract/Subcontractor/Client Enquiry");
		this.setBorder(false);
		this.setAutoScroll(true);

		rootNode = new TreeNode("Subcontract/Subcontractor/Client Enquiry");
		subcontractEnquiryNode = new TreeNode(SUBCONTRACT_ENQUIRY_NODE_NAME);
		subcontractorEnquiryNode = new TreeNode(SUBCONTRACTOR_ENQUIRY_NODE_NAME);
		clientEnquiryNode = new TreeNode(CLIENT_ENQUIRY_NODE_NAME);
		
		rootNode.appendChild(subcontractEnquiryNode);
		rootNode.appendChild(subcontractorEnquiryNode);
		rootNode.appendChild(clientEnquiryNode);
		
		rootNode.expand();
		this.setRootNode(rootNode);

		this.addListener(new TreePanelListenerAdapter() {
			public void onClick(TreeNode node, EventObject e) {
				globalMessageTicket.refresh();

				if(node.getText().equals(SUBCONTRACT_ENQUIRY_NODE_NAME)){
					showSubcontractEnquiryMainPanel();
				}
				else if(node.getText().equals(SUBCONTRACTOR_ENQUIRY_NODE_NAME)){
					showSubcontractorEnquiryMainPanel();
				}
				else if(node.getText().equals(CLIENT_ENQUIRY_NODE_NAME)){
					showClientEnquiryMainPanel();
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

	public void showSubcontractEnquiryMainPanel() {
		globalSectionController.getMainSectionController().resetMainPanel();
		SubcontractEnquiryMainPanel financeSubcontractListMainPanel = new SubcontractEnquiryMainPanel(globalSectionController);
		globalSectionController.getMainSectionController().setContentPanel(financeSubcontractListMainPanel);
		globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();
		globalSectionController.getDetailSectionController().resetPanel();
	}

	public void showSubcontractorEnquiryMainPanel() {
		String username = globalSectionController.getUser().getUsername();
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getUserAccessRightsRepository().getAccessRights(username, RoleSecurityFunctions.C010001_ADDRESS_BOOK, new AsyncCallback<List<String>>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}
			public void onSuccess(List<String> rights) {
				if (rights != null && rights.contains("GAMMON")){
					globalSectionController.getMainSectionController().resetMainPanel();
					SubcontractorEnquiryMainPanel subcontractorEnquiryMainPanel = new SubcontractorEnquiryMainPanel(globalSectionController);
					globalSectionController.getMainSectionController().setContentPanel(subcontractorEnquiryMainPanel);
					globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();
					globalSectionController.getDetailSectionController().resetPanel();
				}else
					MessageBox.alert("Sorry! You are not authorized to view this page.");
			}
		});
	}
	
	public void showClientEnquiryMainPanel() {
		String username = globalSectionController.getUser().getUsername();
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getUserAccessRightsRepository().getAccessRights(username, RoleSecurityFunctions.C010001_ADDRESS_BOOK, new AsyncCallback<List<String>>() {
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}
			public void onSuccess(List<String> rights) {
				if (rights != null && rights.contains("GAMMON")){
					globalSectionController.getMainSectionController().resetMainPanel();
					ClientEnquiryMainPanel clientEnquiryMainPanel = new ClientEnquiryMainPanel(globalSectionController);
					globalSectionController.getMainSectionController().setContentPanel(clientEnquiryMainPanel);
					globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();
					globalSectionController.getDetailSectionController().resetPanel();
				}else
					MessageBox.alert("Sorry! You are not authorized to view this page.");
			}
		});
	}
	

}