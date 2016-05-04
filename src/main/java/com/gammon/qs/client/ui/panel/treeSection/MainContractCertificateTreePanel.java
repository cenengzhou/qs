/**
 * koeyyeung
 * Jun 17, 20133:39:10 PM
 */
package com.gammon.qs.client.ui.panel.treeSection;

import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.controller.TreeSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.mainSection.MainCertEnquiryPanel;
import com.gammon.qs.client.ui.panel.mainSection.RetentionReleaseScheduleEnquiryMainPanel;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;
import com.gwtext.client.core.EventObject;

public class MainContractCertificateTreePanel extends TreePanel{
	
	//Window Titles
	private static final String MAIN_CERT_NODE_NAME = "IPA/IPC";
	private static final String RETENTION_RELEASE_SCHEDULE_NODE_NAME="Retention Release Schedule Enquiry";
	private static final String MAIN_CERT_ENQUIRE_NODE_NAME = "Main Contract Certificate Enquiry";
	
	//private static final String 
	
	private TreeNode rootNode;
	private TreeNode mainCertNode;
	private TreeNode retentionReleaseScheduleEnquiryNode;
	private TreeNode mainCertEnquiryNode;
	
	private GlobalMessageTicket globalMessageTicket;
	private GlobalSectionController globalSectionController;
	public MainContractCertificateTreePanel(final TreeSectionController treeViewController) {
		super();
		this.globalSectionController = treeViewController.getGlobalSectionController();
		globalMessageTicket = new GlobalMessageTicket();

		this.setTitle("Main Contract Certificate");
		this.setBorder(false);
		this.setAutoScroll(true);

		rootNode = new TreeNode("Main Contract Certificate");
		
		mainCertNode = new TreeNode(MAIN_CERT_NODE_NAME);
		rootNode.appendChild(mainCertNode);

		retentionReleaseScheduleEnquiryNode = new TreeNode(RETENTION_RELEASE_SCHEDULE_NODE_NAME);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getUserAccessRightsRepository().getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.F010406_RETENTION_RELEASE_SCHEDULE_ENQUIRY_MAINPANEL, new AsyncCallback<List<String>>() {

			public void onSuccess(List<String> securityList) {
				if (securityList.contains("WRITE")|| securityList.contains("READ"))
					rootNode.appendChild(retentionReleaseScheduleEnquiryNode);		
			}

			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true,treeViewController.getGlobalSectionController().getUser());
			}
		});

		mainCertEnquiryNode = new TreeNode(MAIN_CERT_ENQUIRE_NODE_NAME);
		rootNode.appendChild(mainCertEnquiryNode);

		rootNode.expand();
		this.setRootNode(rootNode);
		
		this.addListener(new TreePanelListenerAdapter() {
			public void onClick(TreeNode node, EventObject e) {
				globalMessageTicket.refresh();
				
				if (node.getText().equals(MAIN_CERT_NODE_NAME)){
					treeViewController.populateMainCertificate();
				}
				else if (node.getText().equals(RETENTION_RELEASE_SCHEDULE_NODE_NAME)){
					showRetenionReleaseScheduleEnquiryMainPanel();
				}
				else if (node.getText().equals(MAIN_CERT_ENQUIRE_NODE_NAME)){
					showMainCertEnquiryPanel();
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

	public void showRetenionReleaseScheduleEnquiryMainPanel() {
		globalSectionController.getMainSectionController().resetMainPanel();
		RetentionReleaseScheduleEnquiryMainPanel releaseScheduleEnquiryMainPanel = new RetentionReleaseScheduleEnquiryMainPanel(globalSectionController);
		globalSectionController.getMainSectionController().setContentPanel(releaseScheduleEnquiryMainPanel);
		globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();
		globalSectionController.getDetailSectionController().resetPanel();
	}

	public void showMainCertEnquiryPanel() {
		globalSectionController.getMainSectionController().resetMainPanel();
		MainCertEnquiryPanel mainCertEnquiryPanel = new MainCertEnquiryPanel(globalSectionController);
		globalSectionController.getMainSectionController().setContentPanel(mainCertEnquiryPanel);
		globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();
		globalSectionController.getDetailSectionController().resetPanel();
	}
	
}
