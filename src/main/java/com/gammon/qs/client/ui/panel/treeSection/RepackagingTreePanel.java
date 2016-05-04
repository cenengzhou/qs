package com.gammon.qs.client.ui.panel.treeSection;

import com.gammon.qs.client.controller.TreeSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

public class RepackagingTreePanel extends TreePanel {
	private TreeNode rootNode;
	private TreeNode repackagingNode;
	private GlobalMessageTicket globalMessageTicket;
	
	public RepackagingTreePanel(final TreeSectionController treeViewController){
		super();
		this.setTitle("Repackaging");
		this.setBorder(false);
		globalMessageTicket = new GlobalMessageTicket();
		rootNode = new TreeNode("root");
		repackagingNode = new TreeNode("Repackaging");
		rootNode.appendChild(repackagingNode);
		this.setRootNode(rootNode);
		
		this.addListener(new TreePanelListenerAdapter(){
			public void onClick(TreeNode node, EventObject e){
				globalMessageTicket.refresh();
				if(node.getText().equals("Repackaging"))
					treeViewController.requestNavigateToRepackaging();
			}
		});
		
		this.expandAll();
	}
	
	public void updateRootText(String text){
		rootNode.setText(text);
	}
	
}
