package com.gammon.qs.client.ui.panel.treeSection;

import com.gammon.qs.client.controller.TreeSectionController;
import com.gwtext.client.widgets.tree.TreePanel;
@Deprecated
public class ProvisionPostingTreePanel extends TreePanel {

/*	private TreeNode rootNode;
	private TreeNode provisionPostingNode;
	private GlobalMessageTicket globalMessageTicket;
	public static final String SC_PROVISION_NODE_NAME = "Subcontract Provision Posting";*/
	
	public ProvisionPostingTreePanel(final TreeSectionController treeViewController){
/*		super();
		this.setTitle("SC Provision");
		setBorder(false);
		globalMessageTicket = new GlobalMessageTicket();
		rootNode = new TreeNode("root");
		provisionPostingNode = new TreeNode(SC_PROVISION_NODE_NAME);
		rootNode.appendChild(provisionPostingNode);
		setRootNode(rootNode);
		addListener(new TreePanelListenerAdapter(){
			public void onClick(TreeNode node, EventObject e) {
				globalMessageTicket.refresh();
				if (node.getText().equals(SC_PROVISION_NODE_NAME))
  					treeViewController.getGlobalSectionController().showProvisionPosting();
			}
		});*/
	}
	
/*	public void updateRootText(String text){
		rootNode.setText(text);
	}
*/
}
