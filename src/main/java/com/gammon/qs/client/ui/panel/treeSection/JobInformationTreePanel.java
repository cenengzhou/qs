package com.gammon.qs.client.ui.panel.treeSection;

import com.gammon.qs.client.controller.DetailSectionController;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.controller.MainSectionController;
import com.gammon.qs.client.controller.TreeSectionController;
import com.gammon.qs.client.repository.JobRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.mainSection.JobGeneralInformationMainPanel;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.Job;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

public class JobInformationTreePanel  extends TreePanel{
	private static final String NODE_GENERAL_INFORMATION = "General Information";
	private static final String NODE_DATES = "Dates";
	
	private TreeNode rootNode;
	private TreeNode generalInformationNode;
	private TreeNode datesNode;
	
	private GlobalMessageTicket globalMessageTicket;
	private GlobalSectionController globalSectionController;
	private MainSectionController mainSectionController;
	private DetailSectionController detailSectionController;
	
	private JobRepositoryRemoteAsync jobRepository;

	public JobInformationTreePanel(final TreeSectionController treeViewController){
		super();
		globalMessageTicket = new GlobalMessageTicket();
		
		this.globalSectionController = treeViewController.getGlobalSectionController();
		this.mainSectionController = globalSectionController.getMainSectionController();
		this.detailSectionController = globalSectionController.getDetailSectionController();
		
		//Initialize Repositories
		jobRepository = globalSectionController.getJobRepository();
		
		this.setTitle("Job Information");
		this.setBorder(false);
		this.setAutoScroll(true);

		rootNode = new TreeNode("root");

		generalInformationNode = new TreeNode(NODE_GENERAL_INFORMATION);
		datesNode = new TreeNode(NODE_DATES);
		
		rootNode.appendChild(generalInformationNode);
		rootNode.appendChild(datesNode);

		rootNode.expand();
		setRootNode(rootNode);

		addListener(new TreePanelListenerAdapter() {
			public void onClick(TreeNode node, EventObject e) {
				globalMessageTicket.refresh();
				
				if(node.getText().equals(NODE_GENERAL_INFORMATION)) 
					navigateToJobGeneralInformationMainPanel();
				else if(node.getText().equals(NODE_DATES))
					treeViewController.navigateToJobDates();	
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

	public void updateRootText(String text) {
		rootNode.setText(text);
	}
	
	public void navigateToJobGeneralInformationMainPanel() {
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		mainSectionController.resetMainPanel();
		SessionTimeoutCheck.renewSessionTimer();
		jobRepository.obtainJob(globalSectionController.getJob().getJobNumber(), new AsyncCallback<Job>() {
			public void onSuccess(Job job) {
				JobGeneralInformationMainPanel jobHeaderFormPanel = new JobGeneralInformationMainPanel(globalSectionController, job);

				mainSectionController.setContentPanel(jobHeaderFormPanel);
				mainSectionController.populateMainPanelWithContentPanel();
				detailSectionController.resetPanel();

				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}

			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}
		});
	}
}
