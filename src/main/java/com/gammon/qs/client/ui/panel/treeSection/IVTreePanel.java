/**
 * koeyyeung
 * Jun 17, 20133:38:12 PM
 */
package com.gammon.qs.client.ui.panel.treeSection;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.mainSection.IVHistoryEnquiryMainPanel;
import com.gammon.qs.client.ui.panel.mainSection.UpdateCumIVByBQItemMainPanel;
import com.gammon.qs.client.ui.panel.mainSection.UpdateCumIVByResourceMainPanel;
import com.gammon.qs.client.ui.panel.mainSection.UpdateCumIVByResourceSummaryMainPanel;
import com.gammon.qs.client.ui.panel.mainSection.UpdateCumIVFinalByResourceSummaryMainPanel;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.RepackagingEntry;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;
import com.gwtext.client.core.EventObject;

public class IVTreePanel extends TreePanel{
	
	public static final String UPDATE_IV_BY_BQITEM_NODE_NAME = "Update/Post IV Movements by BQ Item";
	public static final String UPDATE_IV_BY_RESOURCE_NODE_NAME = "Update/Post IV Movements by Resource";
	public static final String UPDATE_IV_BY_RESOURCESUMMARY_NODE_NAME = "Update/Post IV Movements";
	public static final String UPDATE_IV_FINAL_BY_RESOURCESUMMARY_NODE_NAME = "Update/Post IV Movements(Finalized Package)";
	public static final String IV_HISTORY_NODE_NAME = "Review IV Posting History";
	
	private TreeNode rootNode;
	private TreeNode updateIVByResourceSummaryNode;
	private TreeNode updateIVByBQItemNode;
	private TreeNode updateIVByResourceNode;
	private TreeNode updateIVFinalByResourceSummaryNode;
	private TreeNode ivHistoryNode;
	
	private String currentRepackagingStatus;
	
	private GlobalMessageTicket globalMessageTicket;
	
	private GlobalSectionController globalSectionController;
	
	public IVTreePanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		globalMessageTicket = new GlobalMessageTicket();
		
		this.setTitle("Internal Valuation");
		this.setBorder(false);
		this.setAutoScroll(true);

		rootNode = new TreeNode("IV");
		
		//Different Update IV Windows for Repackaging Type(1,2) and 3
		//1.UPDATE_IV_BY_BQITEM_NODE_NAME, 2.UPDATE_IV_BY_RESOURCE_NODE_NAME or 3.UPDATE_IV_BY_RESOURCESUMMARY_NODE_NAME
		updateIVByBQItemNode = new TreeNode(UPDATE_IV_BY_BQITEM_NODE_NAME); 
		updateIVByResourceNode = new TreeNode(UPDATE_IV_BY_RESOURCE_NODE_NAME);
		updateIVByResourceSummaryNode = new TreeNode(UPDATE_IV_BY_RESOURCESUMMARY_NODE_NAME); 
		updateIVFinalByResourceSummaryNode = new  TreeNode(UPDATE_IV_FINAL_BY_RESOURCESUMMARY_NODE_NAME);
		ivHistoryNode = new TreeNode(IV_HISTORY_NODE_NAME);

		rootNode.appendChild(updateIVByResourceSummaryNode);
		rootNode.appendChild(updateIVFinalByResourceSummaryNode);
		rootNode.appendChild(ivHistoryNode);
		
		rootNode.expand();
		this.setRootNode(rootNode);
		
		this.addListener(new TreePanelListenerAdapter() {
			public void onClick(TreeNode node, EventObject e) {
				globalMessageTicket.refresh();
				
				if(node.getText().equals(UPDATE_IV_BY_RESOURCESUMMARY_NODE_NAME)){
					showUpdateIVByResourceSummaryMainPanel();
				}
				if(node.getText().equals(UPDATE_IV_FINAL_BY_RESOURCESUMMARY_NODE_NAME)){
					showUpdateIVFinalByResourceSummaryMainPanel();
				}
				else if(node.getText().equals(UPDATE_IV_BY_BQITEM_NODE_NAME)){
					showUpdateIVByBQItemMainPanel();
				}
				else if(node.getText().equals(UPDATE_IV_BY_RESOURCE_NODE_NAME)){
					showUpdateIVByResourceMainPanel();
				}
				else if(node.getText().equals(IV_HISTORY_NODE_NAME)){
					showIVHistoryEnquiryMainPanel();
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

	
	/**
	 * @author tikywong
	 * Apr 19, 2011 9:14:42 AM
	 */
	public void openJob(String repackagingType){
		populateJobCostTreeByRepackagingType(repackagingType);
	}
	
	/**
	 * @author tikywong
	 * Apr 19, 2011 9:14:36 AM
	 * Different Update IV Panels for Repackaging Type(1,2) and 3
	 */
	public void populateJobCostTreeByRepackagingType(String repackagingType){
		//Repackaging Type = 3 (By BQItem/Resource)
		if(repackagingType!=null && repackagingType.equals("3")){
			//Insert the Update IV nodes of Repackaging Type 3
			if(!rootNode.contains(updateIVByBQItemNode)){
				updateIVByBQItemNode = new TreeNode(UPDATE_IV_BY_BQITEM_NODE_NAME);
				rootNode.insertBefore(updateIVByBQItemNode, ivHistoryNode);
			}
			if(!rootNode.contains(updateIVByResourceNode)){
				updateIVByResourceNode = new TreeNode(UPDATE_IV_BY_RESOURCE_NODE_NAME);
				rootNode.insertBefore(updateIVByResourceNode, ivHistoryNode);
			}
			if(rootNode.contains(updateIVFinalByResourceSummaryNode))
				rootNode.removeChild(updateIVFinalByResourceSummaryNode);
		}
		
		//Repackaging Type = 1/2 (By ResourceSummary)
		if(repackagingType!=null && (repackagingType.equals("1") || repackagingType.equals("2"))){
			//Clear the UpdateIV node of Repackaging Type 3
			if(rootNode.contains(updateIVByBQItemNode))
				rootNode.removeChild(updateIVByBQItemNode);
			if(rootNode.contains(updateIVByResourceNode))
				rootNode.removeChild(updateIVByResourceNode);
			if(!rootNode.contains(updateIVFinalByResourceSummaryNode)){
				updateIVFinalByResourceSummaryNode = new TreeNode(UPDATE_IV_FINAL_BY_RESOURCESUMMARY_NODE_NAME);
				rootNode.insertBefore(updateIVFinalByResourceSummaryNode, ivHistoryNode);
			}
		}
	}
	
	
	public void showUpdateIVByResourceSummaryMainPanel(){
		currentRepackagingStatus =null;
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getRepackagingEntryRepository().getLatestRepackagingEntry(globalSectionController.getJob(), new AsyncCallback<RepackagingEntry>(){
			public void onSuccess(RepackagingEntry repackagingEntry) {
				if(repackagingEntry != null)
					currentRepackagingStatus = repackagingEntry.getStatus();

				globalSectionController.getDetailSectionController().resetPanel();
				globalSectionController.getMainSectionController().resetMainPanel();
				UpdateCumIVByResourceSummaryMainPanel updateCumIVByResourceSummaryMainPanel = new UpdateCumIVByResourceSummaryMainPanel(globalSectionController, currentRepackagingStatus);
				globalSectionController.getMainSectionController().setContentPanel(updateCumIVByResourceSummaryMainPanel);
				globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();

			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}
		});
	}
	
	/**
	 * @author koeyyeung
	 * created on 28th May, 2015**/
	public void showUpdateIVFinalByResourceSummaryMainPanel(){
		currentRepackagingStatus =null;
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getRepackagingEntryRepository().getLatestRepackagingEntry(globalSectionController.getJob(), new AsyncCallback<RepackagingEntry>(){
			public void onSuccess(RepackagingEntry repackagingEntry) {
				if(repackagingEntry != null)
					currentRepackagingStatus = repackagingEntry.getStatus();

				globalSectionController.getDetailSectionController().resetPanel();
				globalSectionController.getMainSectionController().resetMainPanel();
				UpdateCumIVFinalByResourceSummaryMainPanel updateCumIVByResourceSummaryMainPanel = new UpdateCumIVFinalByResourceSummaryMainPanel(globalSectionController, currentRepackagingStatus);
				globalSectionController.getMainSectionController().setContentPanel(updateCumIVByResourceSummaryMainPanel);
				globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();

			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}
		});
	}
	
	/**
	 * Fill in the Repackaging Status (100, 200, 300, 900) by RepackagingEntry.Status
	 * @author tikywong
	 */
	public void showUpdateIVByBQItemMainPanel() {
		currentRepackagingStatus =null;
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getRepackagingEntryRepository().getLatestRepackagingEntry(globalSectionController.getJob(), new AsyncCallback<RepackagingEntry>(){
			public void onSuccess(RepackagingEntry repackagingEntry) {
				if(repackagingEntry != null)
					currentRepackagingStatus = repackagingEntry.getStatus();

				globalSectionController.getMainSectionController().resetMainPanel();
				globalSectionController.getDetailSectionController().resetPanel();
				UpdateCumIVByBQItemMainPanel updateCumIVByBQItemMainPanel = new UpdateCumIVByBQItemMainPanel(globalSectionController, currentRepackagingStatus);
				globalSectionController.getMainSectionController().setContentPanel(updateCumIVByBQItemMainPanel);
				globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();

			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}
		});		
	}
	
	
	/**
	 * Fill in the Repackaging Status (100, 200, 300, 900) by RepackagingEntry.Status
	 * 
	 * @author tikywong
	 */
	public void showUpdateIVByResourceMainPanel() {
		currentRepackagingStatus =null;
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getRepackagingEntryRepository().getLatestRepackagingEntry(globalSectionController.getJob(), new AsyncCallback<RepackagingEntry>(){
			public void onSuccess(RepackagingEntry repackagingEntry) {
				if(repackagingEntry != null)
					currentRepackagingStatus = repackagingEntry.getStatus();

				globalSectionController.getMainSectionController().resetMainPanel();
				globalSectionController.getDetailSectionController().resetPanel();
				UpdateCumIVByResourceMainPanel updateCumIVByResourceMainPanel = new UpdateCumIVByResourceMainPanel(globalSectionController, currentRepackagingStatus);
				globalSectionController.getMainSectionController().setContentPanel(updateCumIVByResourceMainPanel);
				globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();

			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}
		});	
	}
	
	public void showIVHistoryEnquiryMainPanel(){
		globalSectionController.getMainSectionController().resetMainPanel();
		globalSectionController.getDetailSectionController().resetPanel();
		IVHistoryEnquiryMainPanel ivHistoryEnquiryMainPanel = new IVHistoryEnquiryMainPanel(globalSectionController);
		globalSectionController.getMainSectionController().setContentPanel(ivHistoryEnquiryMainPanel);
		globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();
	}
	
	//getter, setter
	public String getCurrentRepackagingStatus() {
		return currentRepackagingStatus;
	}
	public void setCurrentRepackagingStatus(String currentRepackagingStatus) {
		this.currentRepackagingStatus = currentRepackagingStatus;
	}
}
