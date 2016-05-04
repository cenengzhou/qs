/**
 * koeyyeung
 * Jul 4, 20132:41:32 PM
 */
package com.gammon.qs.client.ui.panel.treeSection;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.panel.mainSection.SCProvisionHistoryEnquiryMainPanel;
import com.gammon.qs.client.ui.window.treeSection.ProvisionPostingWindow;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

public class SCProvisionTreePanel extends TreePanel {

	public static final String		SC_PROVISION_HISTORY_ENQUIRY_NODE_NAME	= "Subcontract Provision History Enquiry";
	public static final String		SC_PROVISION_NODE_NAME					= "Subcontract Provision Posting";

	private TreeNode				rootNode;
	private TreeNode				provisionPostingNode;
	private TreeNode				provisionHistoryNode;

	private GlobalMessageTicket		globalMessageTicket;
	private GlobalSectionController	globalSectionController;

	public SCProvisionTreePanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		globalMessageTicket = new GlobalMessageTicket();

		this.setTitle("Subcontract Provision");
		this.setBorder(false);
		this.setAutoScroll(true);

		rootNode = new TreeNode("Subcontract Provision");

		provisionHistoryNode = new TreeNode(SC_PROVISION_HISTORY_ENQUIRY_NODE_NAME);
		provisionPostingNode = new TreeNode(SC_PROVISION_NODE_NAME);

		rootNode.appendChild(provisionHistoryNode);
		rootNode.appendChild(provisionPostingNode);

		rootNode.expand();
		this.setRootNode(rootNode);

		this.addListener(new TreePanelListenerAdapter() {
			public void onClick(TreeNode node, EventObject e) {
				globalMessageTicket.refresh();

				if (node.getText().equals(SC_PROVISION_HISTORY_ENQUIRY_NODE_NAME)) {
					showSCProvisionHistoryEnquiryMainPanel();
				} else if (node.getText().equals(SC_PROVISION_NODE_NAME)) {
					showProvisionPosting();
				}
			}

			public void onContextMenu(TreeNode node, EventObject e) {
				while (node != null) {
					node.collapse();
					node = (TreeNode) node.getParentNode();
				}
			}
		});
		this.expandAll();
	}

	public void showProvisionPosting() {
		if (globalSectionController.getCurrentWindow() != null) {
			globalSectionController.getCurrentWindow().close();
			globalSectionController.getCurrentWindow().destroy();
		}
		globalSectionController.setCurrentWindow(new ProvisionPostingWindow(globalSectionController));
		globalSectionController.getCurrentWindow().show();

	}

	// added by brian on 20110426
	public void showSCProvisionHistoryEnquiryMainPanel() {
		globalSectionController.getMainSectionController().resetMainPanel();
		SCProvisionHistoryEnquiryMainPanel scProvisionHistoryEnquiryMainPanel = new SCProvisionHistoryEnquiryMainPanel(globalSectionController);
		globalSectionController.getMainSectionController().setContentPanel(scProvisionHistoryEnquiryMainPanel);
		globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();
		globalSectionController.getDetailSectionController().resetPanel();
	}

}
