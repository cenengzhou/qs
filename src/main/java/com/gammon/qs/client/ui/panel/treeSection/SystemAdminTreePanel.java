package com.gammon.qs.client.ui.panel.treeSection;

import java.util.List;

import com.gammon.qs.client.controller.DetailSectionController;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.controller.MainSectionController;
import com.gammon.qs.client.controller.TreeSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.mainSection.EmailNotificationInfoGridPanel;
import com.gammon.qs.client.ui.panel.mainSection.ManualSchedulerAdminMainPanel;
import com.gammon.qs.client.ui.panel.mainSection.PowerUserAdminPanel;
import com.gammon.qs.client.ui.panel.mainSection.SchedulerEditorGridPanel;
import com.gammon.qs.client.ui.panel.mainSection.SystemConstantEnquiryPanel;
import com.gammon.qs.domain.quartz.QrtzTriggers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

public class SystemAdminTreePanel extends TreePanel{
	//note attribute
	private final static String NODE_ATTRIBUTE_MANUAL_SCHEDULER_ADMIN="Manual Scheduler Administration";
	private final static String NODE_ATTRIBUTE_SYSTEM_SCHEDULER_ADMIN="System Scheduler Administration";
	private final static String NODE_ATTRIBUTE_POWER_USER_ADMIN="Power User Administration";
	private final static String NODE_ATTRIBUTE_SYSTEM_CONSTANT = "System Constant Enquiry";
	private final static String NODE_ATTRIBUTE_EMAIL_NOTIFICATION_INFO = "Email Notifaication Information";
	
	//UI Controller 
	private GlobalMessageTicket globalMessageTicket;
	
	private TreeNode rootNode;
	private TreeNode manualSchedulerAdminNode;
	private TreeNode schedulerAdminNode;
	private TreeNode powerUserAdminNode;
	private TreeNode emailNotificationInfoNode;

	/**
	 * @author matthewatc
	 * 11:35:12 13 Jan 2012 (UTC+8)
	 * added node to access system constant enquiry window
	 */
	private TreeNode systemConstantNode;
	
	private GlobalSectionController globalSectionController;
	private DetailSectionController detailSectionController;
	private MainSectionController mainSectionController;
	@SuppressWarnings("unused")
	private Window currentWindow;
	
	public SystemAdminTreePanel(final TreeSectionController treeViewController){
		super();
		globalMessageTicket = new GlobalMessageTicket();
		this.globalSectionController = treeViewController.getGlobalSectionController();
		this.mainSectionController = globalSectionController.getMainSectionController();
		this.detailSectionController = globalSectionController.getDetailSectionController();
		this.currentWindow = globalSectionController.getCurrentWindow();
		
		this.setTitle("System Admin");
		this.setBorder(false);
		this.setAutoScroll(true);
					
		rootNode = new TreeNode("System Admin");

		manualSchedulerAdminNode = new TreeNode(NODE_ATTRIBUTE_MANUAL_SCHEDULER_ADMIN);
		schedulerAdminNode = new TreeNode(NODE_ATTRIBUTE_SYSTEM_SCHEDULER_ADMIN);
		powerUserAdminNode = new TreeNode(NODE_ATTRIBUTE_POWER_USER_ADMIN);
		systemConstantNode = new TreeNode(NODE_ATTRIBUTE_SYSTEM_CONSTANT);
		emailNotificationInfoNode = new TreeNode(NODE_ATTRIBUTE_EMAIL_NOTIFICATION_INFO);
		
		rootNode.appendChild(schedulerAdminNode);
		rootNode.appendChild(manualSchedulerAdminNode);
		rootNode.appendChild(powerUserAdminNode);
		rootNode.appendChild(systemConstantNode);
		rootNode.appendChild(emailNotificationInfoNode);
		
		rootNode.expand();
		this.setRootNode(rootNode);
		
		this.addListener(new TreePanelListenerAdapter() {
			public void onClick(TreeNode node, EventObject e) {
				globalMessageTicket.refresh();
				if(node.getText().equals(NODE_ATTRIBUTE_SYSTEM_SCHEDULER_ADMIN)){
					showQRTZ_TRIGGER();
				}
				else if (node.getText().equals(NODE_ATTRIBUTE_MANUAL_SCHEDULER_ADMIN)){
					showSystemAdminOtherAdminPanel();
				}
				else if (node.getText().equals(NODE_ATTRIBUTE_POWER_USER_ADMIN)){
					navigateToPowerUserAdminPanel();
				}
				else if (node.getText().equals(NODE_ATTRIBUTE_SYSTEM_CONSTANT)){
					showSystemConstantGridPanel();
				}
				else if(node.getText().equals(NODE_ATTRIBUTE_EMAIL_NOTIFICATION_INFO)){
					showEmailNotificationInfoGridPanel();
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

	public void showQRTZ_TRIGGER() {
		mainSectionController.resetMainPanel();
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getQrtzTriggerServiceRepository().getAllTriggers(new AsyncCallback<List<QrtzTriggers>>(){
			public void onFailure(Throwable e) {
				MessageBox.alert(e.getMessage());
			}
			public void onSuccess(List<QrtzTriggers> qrtzList) {
				SchedulerEditorGridPanel schedulerEditorGridPanel = new SchedulerEditorGridPanel(globalSectionController);
				schedulerEditorGridPanel.populate(qrtzList);
				mainSectionController.setGridPanel(schedulerEditorGridPanel);
				mainSectionController.populateMainPanelWithGridPanel();
				detailSectionController.resetPanel();
			}
		});
	}
	
	public void showSystemAdminOtherAdminPanel() {
		mainSectionController.resetMainPanel();
		detailSectionController.resetPanel();
		ManualSchedulerAdminMainPanel otherAdminPanel = new ManualSchedulerAdminMainPanel(globalSectionController);
		mainSectionController.setContentPanel(otherAdminPanel);
		mainSectionController.populateMainPanelWithContentPanel();
	}
	
	/**
	 * 
	 * @author tikywong
	 * refactored on January 8, 2014 2:12:47 PM
	 */
	public void navigateToPowerUserAdminPanel() {
		mainSectionController.resetMainPanel();
		detailSectionController.resetPanel();
		PowerUserAdminPanel powerUserAdminPanel = new PowerUserAdminPanel(globalSectionController);
		mainSectionController.setContentPanel(powerUserAdminPanel);
		mainSectionController.populateMainPanelWithContentPanel();
	}
	
	/**
	 * @author matthewatc
	 * 11:37:19 13 Jan 2012 (UTC+8)
	 * Shows the system constant enquiry window
	 */
	public void showSystemConstantGridPanel(){	
		mainSectionController.resetMainPanel();
		SystemConstantEnquiryPanel systemConstantEnquiryPanel = new SystemConstantEnquiryPanel(globalSectionController);
		mainSectionController.setContentPanel(systemConstantEnquiryPanel);
		mainSectionController.populateMainPanelWithContentPanel();
		detailSectionController.resetPanel();
	}
	
	/**
	 * @author koeyyeung
	 * created on 06 June, 2013
	 */
	public void showEmailNotificationInfoGridPanel(){
		mainSectionController.resetMainPanel();
		EmailNotificationInfoGridPanel emailNotificationInfoGridPanel = new EmailNotificationInfoGridPanel(globalSectionController);
		mainSectionController.setGridPanel(emailNotificationInfoGridPanel);
		mainSectionController.populateMainPanelWithGridPanel();
		detailSectionController.resetPanel();
	}

	public void updateRootText(String string) {
		rootNode.setText(string);
	}
}
